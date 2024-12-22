package fr.arinonia.launcher.ui.panels;

import fr.arinonia.launcher.ui.AbstractPanel;
import fr.arinonia.launcher.utils.Constants;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;

public class LoadingPanel extends AbstractPanel {
    private Label statusLabel;
    private Label subStatusLabel;
    private VBox loadingAnimationContainer;
    private StackPane progressBarContainer;
    private Rectangle progressBar;
    private Timeline progressTimeline;
    private ParallelTransition parallelTransition;

    @Override
    protected void setupPanel() {
        final VBox centerContainer = new VBox(30);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setMaxWidth(500);

        setupLogo(centerContainer);

        this.loadingAnimationContainer = new VBox();
        this.progressBarContainer = new StackPane();

        setupLoadingAnimation();
        setupProgressBar();

        setupStatusLabels();

        centerContainer.getChildren().addAll(
                this.loadingAnimationContainer,
                this.progressBarContainer,
                this.statusLabel,
                this.subStatusLabel
        );

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

            final ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(2), logoView);
            pulseAnimation.setFromX(1.0);
            pulseAnimation.setFromY(1.0);
            pulseAnimation.setToX(1.1);
            pulseAnimation.setToY(1.1);
            pulseAnimation.setAutoReverse(true);
            pulseAnimation.setCycleCount(Timeline.INDEFINITE);
            pulseAnimation.play();

            container.getChildren().add(logoView);
        } catch (final Exception ignored) {
            final Label logoText = new Label(Constants.APP_NAME);
            logoText.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");
            container.getChildren().add(logoText);
        }
    }

    private void setupLoadingAnimation() {
        final int numberOfCircles = 3;
        final Circle[] circles = new Circle[numberOfCircles];

        final StackPane circlesContainer = new StackPane();
        circlesContainer.setAlignment(Pos.CENTER);

        for (int i = 0; i < numberOfCircles; i++) {
            circles[i] = new Circle(8, Color.rgb(149, 128, 255));
            circles[i].setTranslateX(i * 30 - 30);

            GaussianBlur blur = new GaussianBlur(2);
            circles[i].setEffect(blur);
        }

        this.parallelTransition = new ParallelTransition();
        for (int i = 0; i < numberOfCircles; i++) {
            final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), circles[i]);
            translateTransition.setByY(-20);
            translateTransition.setAutoReverse(true);
            translateTransition.setCycleCount(Timeline.INDEFINITE);
            translateTransition.setDelay(Duration.millis(i * 100));

            final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), circles[i]);
            scaleTransition.setFromX(0.8);
            scaleTransition.setFromY(0.8);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(Timeline.INDEFINITE);
            scaleTransition.setDelay(Duration.millis(i * 100));

            this.parallelTransition.getChildren().addAll(translateTransition, scaleTransition);
        }

        circlesContainer.getChildren().addAll(circles);
        this.loadingAnimationContainer.getChildren().add(circlesContainer);
        this.loadingAnimationContainer.setAlignment(Pos.CENTER);
    }

    private void setupStatusLabels() {
        this.statusLabel = new Label("Initialisation");
        this.statusLabel.setStyle("-fx-text-fill: rgb(222, 222, 222); -fx-font-size: 18px; -fx-font-weight: bold;");

        this.subStatusLabel = new Label("Préparation du launcher...");
        this.subStatusLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
    }

    private void setupProgressBar() {
        this.progressBar = new Rectangle(300, 4);
        this.progressBar.setArcWidth(4);
        this.progressBar.setArcHeight(4);

        this.progressBar.setFill(Color.rgb(149, 128, 255));
        this.progressBar.setScaleX(0);

        final DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(149, 128, 255, 0.7));
        glow.setRadius(10);
        this.progressBar.setEffect(glow);

        final Rectangle backgroundBar = new Rectangle(300, 4);
        backgroundBar.setArcWidth(4);
        backgroundBar.setArcHeight(4);
        backgroundBar.setFill(Color.rgb(48, 25, 88));

        this.progressBarContainer.getChildren().addAll(backgroundBar, this.progressBar);
        this.progressBarContainer.setAlignment(Pos.CENTER);
        this.progressBarContainer.setTranslateY(20);
    }

    public void updateStatus(final String mainStatus, final String subStatus, final double progress) {
        Platform.runLater(() -> {
            if (mainStatus != null) {
                this.statusLabel.setText(mainStatus);
            }
            if (subStatus != null) {
                this.subStatusLabel.setText(subStatus);
            }

            if (this.progressTimeline != null) {
                this.progressTimeline.stop();
            }

            this.progressTimeline = new Timeline(
                    new KeyFrame(Duration.millis(500),
                            new KeyValue(this.progressBar.scaleXProperty(), progress, Interpolator.EASE_BOTH)
                    )
            );
            this.progressTimeline.play();

            if (progress >= 1.0) {
                onLoadingComplete();
            }
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
        this.parallelTransition.play();

        this.progressBar.setScaleX(0);
        this.statusLabel.setText("Initialisation");
        this.subStatusLabel.setText("Préparation du launcher...");

        final FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this.layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        startLoading();
    }

    private void startLoading() {
        new Thread(() -> {
            try {
                updateStatus("Vérification", "Recherche de mises à jour...", 0.2);
                Thread.sleep(1800);

                updateStatus("Configuration", "Chargement des paramètres...", 0.4);
                Thread.sleep(1800);

                updateStatus("Actualités", "Récupération des news...", 0.6);
                Thread.sleep(1800);

                updateStatus("Serveurs", "Connexion aux serveurs de jeu...", 0.8);
                Thread.sleep(1800);

                updateStatus("Finalisation", "Lancement du launcher...", 1.0);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}