package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.ui.components.CustomButton;
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

public class TopBar extends HBox {
    private static final String STYLE = "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);";
    private final HomePanel homePanel;
    private Node accountSection;

    public TopBar(final HomePanel homePanel) {
        super(15);
        this.homePanel = homePanel;
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setPadding(new Insets(10, 15, 10, 15));
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
        logoView.setFitHeight(30);
        logoView.setFitWidth(30);
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
        System.out.println(account);
        if (account == null) {
            return createAuthButton();
        } else {
            return createAccountInfo(account);
        }
    }

    private CustomButton createAuthButton() {
        final CustomButton authButton = new CustomButton("Se connecter");
        authButton.setOnAction(e -> {
            this.homePanel.getPanelManager().getLauncher().getAuthenticationManager().authenticate();
        });
        return authButton;
    }

    private HBox createAccountInfo(final Account account) {
        final HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER);

        // Avatar (placeholder pour l'instant)
        final Region avatarPlaceholder = new Region();
        avatarPlaceholder.setPrefSize(30, 30);
        avatarPlaceholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 15;");

        // Informations du compte
        final VBox accountDetails = new VBox(2);
        accountDetails.setAlignment(Pos.CENTER_LEFT);

        final Label usernameLabel = new Label(account.getUsername());
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label accountTypeLabel = new Label("Compte Microsoft");
        accountTypeLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        accountDetails.getChildren().addAll(usernameLabel, accountTypeLabel);

        // Bouton de déconnexion
        final CustomButton logoutButton = new CustomButton("⚪");
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: rgb(180, 180, 180);");
        logoutButton.setOnAction(e -> {
            //this.homePanel.getPanelManager().getLauncher().getAuthenticationManager().logout();
            this.homePanel.getPanelManager().getLauncher().setSelectedAccount(null);//! replace with another account if you have multiple accounts
            updateAccountSection(); // Met à jour la section après déconnexion
            System.out.println("logout");
        });
        logoutButton.setOnMouseEntered(e ->
                logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;")
        );
        logoutButton.setOnMouseExited(e ->
                logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: rgb(180, 180, 180);")
        );

        container.getChildren().addAll(avatarPlaceholder, accountDetails, logoutButton);
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