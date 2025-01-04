package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.utils.CallBack;
import fr.arinonia.launcher.utils.Constants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TopBar extends HBox {
    private static final String STYLE = "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);";
    private final HomePanel homePanel;
    private Node accountSection;

    public TopBar(final HomePanel homePanel) {
        super(15.0D);
        this.homePanel = homePanel;
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setPadding(new Insets(10.0D, 15.0D, 10.0D, 15.0D));
        this.setStyle(STYLE);

        setupComponents();
        setupShadow();
    }

    private void setupComponents() {
        final ImageView logoView = createLogo();
        final Label titleLabel = createTitle();
        final Region spacer = createSpacer();
        this.accountSection = createAccountSection();

        this.getChildren().addAll(logoView, titleLabel, spacer, accountSection);
    }

    private ImageView createLogo() {
        final ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/icon.png")));
        logoView.setFitHeight(30.0D);
        logoView.setFitWidth(30.0D);
        return logoView;
    }

    private Label createTitle() {
        final Label titleLabel = new Label(Constants.APP_NAME);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        return titleLabel;
    }

    private Region createSpacer() {
        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private Node createAccountSection() {
        final Account account = this.homePanel.getPanelManager().getLauncher().getSelectedAccount();
        if (account == null) {
            return createAuthButton();
        } else {
            return createAccountInfo(account);
        }
    }

    private CustomButton createAuthButton() {
        final CustomButton authButton = new CustomButton("Se connecter");
        authButton.setOnAction(e -> {
            this.homePanel.getPanelManager().getLauncher().getAuthenticationManager().authenticate(new CallBack() {
                @Override
                public void onComplete() {
                    updateAccountSection();
                }
            });
        });
        return authButton;
    }

    private HBox createAccountInfo(final Account account) {
        final HBox container = new HBox(10.0D);
        container.setAlignment(Pos.CENTER);

        final ImageView avatarView = new ImageView();
        avatarView.setFitHeight(30.0D);
        avatarView.setFitWidth(30.0D);

        final Region placeholder = new Region();
        placeholder.setPrefSize(30.0D, 30.0D);
        placeholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 15;");
        avatarView.setClip(new Circle(15.0D, 15.0D, 15.0D));

        new Thread(() -> {
            final Image avatar = new Image("https://mc-heads.net/avatar/" + account.getUuid(),
                    true);
            avatar.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 1.0D) {
                    Platform.runLater(() -> {
                        avatarView.setImage(avatar);
                    });
                }
            });
        }).start();
        avatarView.setImage(null);

        final VBox accountDetails = new VBox(2.0D);
        accountDetails.setAlignment(Pos.CENTER_LEFT);

        final Label usernameLabel = new Label(account.getUsername());
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label accountTypeLabel = new Label("Compte Microsoft");
        accountTypeLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        accountDetails.getChildren().addAll(usernameLabel, accountTypeLabel);

        final CustomButton logoutButton = new CustomButton("Déconnexion");
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: rgb(180, 180, 180); -fx-font-family: 'Bahnschrift';");
        logoutButton.setOnAction(e -> {
            this.homePanel.getPanelManager().getLauncher().setSelectedAccount(null);
            updateAccountSection();
        });
        logoutButton.setOnMouseEntered(e ->
                logoutButton.setStyle("-fx-background-color: rgba(180, 180, 180, 0.2); -fx-text-fill: white; -fx-font-family: 'Bahnschrift';")
        );
        logoutButton.setOnMouseExited(e ->
                logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: rgb(180, 180, 180); -fx-font-family: 'Bahnschrift';")
        );

        container.getChildren().addAll(avatarView, accountDetails, logoutButton);
        return container;
    }

    private void setupShadow() {
        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(5);
        this.setEffect(shadow);
    }

    public void updateAccountSection() {
        Platform.runLater(() -> {
            this.getChildren().remove(this.accountSection);
            this.accountSection = createAccountSection();
            this.getChildren().add(this.accountSection);
        });
    }
}