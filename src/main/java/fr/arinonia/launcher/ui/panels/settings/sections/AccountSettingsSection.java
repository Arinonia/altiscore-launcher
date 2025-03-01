package fr.arinonia.launcher.ui.panels.settings.sections;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.components.settings.SettingsItem;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AccountSettingsSection extends VBox {
    private final Launcher launcher;

    public AccountSettingsSection(Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(15);
        this.setPadding(new Insets(10));
        setupItems();
    }

    private void setupItems() {
        this.getChildren().clear();
        final Label sectionTitle = new Label("Paramètres du Compte");
        sectionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Account selectedAccount = this.launcher.getSelectedAccount();
        if (selectedAccount != null) {
            this.getChildren().addAll(
                    sectionTitle,
                    createActiveAccountSection(selectedAccount),
                    createAccountsList()
            );
        } else {
            final VBox noAccountBox = new VBox(10);
            noAccountBox.setAlignment(Pos.CENTER);
            final Label noAccountLabel = new Label("Aucun compte connecté");
            noAccountLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

            final CustomButton loginButton = new CustomButton("Se connecter");
            loginButton.setOnAction(e -> this.launcher.getAuthenticationManager().authenticate(null));

            noAccountBox.getChildren().addAll(noAccountLabel, loginButton);

            this.getChildren().addAll(sectionTitle, noAccountBox);
        }
    }

    private VBox createActiveAccountSection(Account account) {
        final VBox accountBox = new VBox(10);
        accountBox.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
        accountBox.setPadding(new Insets(15));

        final Label activeAccountTitle = new Label("Compte Actif");
        activeAccountTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final HBox accountInfo = new HBox(15);
        accountInfo.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        final ImageView avatarView = new ImageView();
        avatarView.setFitHeight(48);
        avatarView.setFitWidth(48);
        final Circle clip = new Circle(24, 24, 24);
        avatarView.setClip(clip);

        // Placeholder en attendant le chargement de l'avatar
        final Region placeholder = new Region();
        placeholder.setPrefSize(48, 48);
        placeholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 24;");

        // Chargement asynchrone de l'avatar
        new Thread(() -> {
            final Image avatar = new Image("https://mc-heads.net/avatar/" + account.getUuid(), true);
            avatar.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 1.0) {
                    Platform.runLater(() -> avatarView.setImage(avatar));
                }
            });
        }).start();

        final VBox accountDetails = new VBox(5);
        final Label usernameLabel = new Label(account.getUsername());
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift';");

        final Label accountTypeLabel = new Label("Compte Microsoft");
        accountTypeLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        accountDetails.getChildren().addAll(usernameLabel, accountTypeLabel);

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final CustomButton logoutButton = new CustomButton("Déconnexion");
        logoutButton.setOnAction(e -> {
            this.launcher.getAuthConfigManager().logout(account.getUuid());
            this.launcher.setSelectedAccount(null);//! maybe this should be in the logout method
            setupItems();
        });

        accountInfo.getChildren().addAll(avatarView, accountDetails, spacer, logoutButton);
        accountBox.getChildren().addAll(activeAccountTitle, accountInfo);

        return accountBox;
    }

    private VBox createAccountsList() {
        final VBox accountsListBox = new VBox(10);
        accountsListBox.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
        accountsListBox.setPadding(new Insets(15));

        final Label accountsTitle = new Label("Autres Comptes");
        accountsTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        accountsListBox.getChildren().add(accountsTitle);

        for (Account account : this.launcher.getAuthConfigManager().getAccounts()) {
            if (!account.getUuid().equals(this.launcher.getSelectedAccount().getUuid())) {
                accountsListBox.getChildren().add(createAccountListItem(account));
            }
        }

        final CustomButton addAccountButton = new CustomButton("+ Ajouter un compte");
        addAccountButton.setOnAction(e -> this.launcher.getAuthenticationManager().authenticate(null));
        accountsListBox.getChildren().add(addAccountButton);

        return accountsListBox;
    }

    private HBox createAccountListItem(Account account) {
        final HBox accountItem = new HBox(10);
        accountItem.setAlignment(Pos.CENTER_LEFT);
        accountItem.setPadding(new Insets(5));

        final ImageView avatarView = new ImageView();
        avatarView.setFitHeight(32);
        avatarView.setFitWidth(32);
        final Circle clip = new Circle(16, 16, 16);
        avatarView.setClip(clip);

        new Thread(() -> {
            final Image avatar = new Image("https://mc-heads.net/avatar/" + account.getUuid(), true);
            avatar.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 1.0) {
                    Platform.runLater(() -> avatarView.setImage(avatar));
                }
            });
        }).start();

        final Label usernameLabel = new Label(account.getUsername());
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final CustomButton switchButton = new CustomButton("Sélectionner");
        switchButton.setOnAction(e -> {
            this.launcher.setSelectedAccount(account);
            setupItems(); // Rafraîchir la vue
        });

        final CustomButton removeButton = new CustomButton("✕");
        removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: rgb(180, 180, 180);");
        removeButton.setOnAction(e -> {
            this.launcher.getAuthConfigManager().removeAccount(account.getUuid());
            setupItems(); // Rafraîchir la vue
        });

        accountItem.getChildren().addAll(avatarView, usernameLabel, spacer, switchButton, removeButton);
        return accountItem;
    }
}