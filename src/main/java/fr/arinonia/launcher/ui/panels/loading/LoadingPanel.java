package fr.arinonia.launcher.ui.panels.loading;

import fr.arinonia.launcher.ui.AbstractPanel;

import fr.arinonia.launcher.ui.panels.home.HomePanel;
import fr.arinonia.launcher.utils.CallBack;
import fr.arinonia.launcher.utils.Constants;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LoadingPanel extends AbstractPanel {
    private Label statusLabel;
    private Label subStatusLabel;
    private LoadingAnimation loadingAnimation;
    private LoadingProgressBar progressBar;

    @Override
    protected void setupPanel() {
        final VBox centerContainer = new VBox(30);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setMaxWidth(500);

        setupLogo(centerContainer);
        setupComponents(centerContainer);

        this.layout.setAlignment(Pos.CENTER);
        this.layout.getChildren().add(centerContainer);
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(36, 17, 70), rgb(48, 25, 88));");
    }

    private void setupLogo(final VBox container) {
        try {
            final ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/icon.png")));
            logoView.setFitWidth(120);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);

            final DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
            dropShadow.setRadius(20);
            logoView.setEffect(dropShadow);

            setupLogoPulseAnimation(logoView);
            container.getChildren().add(logoView);
        } catch (final Exception ignored) {
            final Label logoText = new Label(Constants.APP_NAME);
            logoText.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");
            container.getChildren().add(logoText);
        }
    }

    private void setupLogoPulseAnimation(final ImageView logoView) {
        final ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(2), logoView);
        pulseAnimation.setFromX(1.0);
        pulseAnimation.setFromY(1.0);
        pulseAnimation.setToX(1.1);
        pulseAnimation.setToY(1.1);
        pulseAnimation.setAutoReverse(true);
        pulseAnimation.setCycleCount(Timeline.INDEFINITE);
        pulseAnimation.play();
    }

    private void setupComponents(final VBox container) {
        this.loadingAnimation = new LoadingAnimation();
        this.progressBar = new LoadingProgressBar();

        this.statusLabel = new Label("Initialisation");
        this.statusLabel.setStyle("-fx-text-fill: rgb(222, 222, 222); -fx-font-size: 18px; -fx-font-weight: bold;");

        this.subStatusLabel = new Label("Préparation du launcher...");
        this.subStatusLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        container.getChildren().addAll(
                this.loadingAnimation,
                this.progressBar,
                this.statusLabel,
                this.subStatusLabel
        );
    }

    public void updateStatus(final String mainStatus, final String subStatus, final double progress) {
        Platform.runLater(() -> {
            if (mainStatus != null) {
                this.statusLabel.setText(mainStatus);
            }
            if (subStatus != null) {
                this.subStatusLabel.setText(subStatus);
            }

            this.progressBar.updateProgress(progress);

            /*if (progress >= 1.0) {
                onLoadingComplete();
            }*/
        });
    }

    private void onLoadingComplete() {

        final FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this.layout);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> getPanelManager().showPanel(HomePanel.class));
        fadeOut.play();
    }

    @Override
    public void onShow() {
        super.onShow();
        this.loadingAnimation.play();

        this.progressBar.reset();
        this.statusLabel.setText("Initialisation");
        this.subStatusLabel.setText("Préparation du launcher...");

        final FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this.layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        startLoading();
    }

    private void startLoading() {
        this.getPanelManager().getLauncher().getLauncherLoader().loadAll(new CallBack() {
            @Override
            public void onProgress(final double progress, final String status) {
                System.out.println("Progress: " + progress + " - " + status);
                Platform.runLater(() -> updateStatus(status, null, progress));
            }

            @Override
            public void onComplete() {
                if (getPanelManager().getLauncher().getLauncherLoader().hasErrors()) {
                    getPanelManager().getLauncher().getLauncherLoader().getErrors().forEach((component, error) -> {
                        System.out.println("Error: " + component + " - " + error);
                    });
                    showError("Une erreur est survenue lors du chargement des composants.");
                    return;
                }
                Platform.runLater(() -> onLoadingComplete());
            }

            @Override
            public void onError(final String error) {
                Platform.runLater(() -> showError(error));
            }
        });

    }

    // need a better way to handle errors (add exception and step to CallBack)
    // maybe i need to create a notification system from panelManager
    private void showError(final String error) {
        System.out.println("Error: " + error);
    }
}