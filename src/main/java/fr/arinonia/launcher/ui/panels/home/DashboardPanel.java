package fr.arinonia.launcher.ui.panels.home;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.components.home.ServerItem;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class DashboardPanel extends HBox {
    private final Launcher launcher;

    public DashboardPanel(final Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(20);
        initialize();
    }

    private void initialize() {
        final VBox mainSection = createMainSection();
        HBox.setHgrow(mainSection, Priority.ALWAYS);

        // Section des news (à droite)
        final NewsSection newsSection = new NewsSection(this.launcher);

        this.getChildren().addAll(mainSection, newsSection);
    }

    private VBox createMainSection() {
        final VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        final HBox welcomeSection = createWelcomeSection();

        final VBox serversContainer = new VBox(10);
        serversContainer.setPadding(new Insets(15));

        final Label serversTitle = new Label("Serveurs disponibles");
        serversTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        for (int i = 0; i < Math.min(3, this.launcher.getServers().size()); i++) {
            final ServerItem serverItem = new ServerItem(this.launcher, this.launcher.getServers().get(i));
            serversContainer.getChildren().add(serverItem);
        }

        // Lien pour voir tous les serveurs
        final HBox viewAllServers = new HBox();
        viewAllServers.setAlignment(Pos.CENTER_RIGHT);

        final Label viewAllLabel = new Label("Voir tous les serveurs >");
        viewAllLabel.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-cursor: hand;");
        viewAllLabel.setOnMouseEntered(e -> viewAllLabel.setStyle("-fx-text-fill: rgb(169, 148, 255); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-cursor: hand;"));
        viewAllLabel.setOnMouseExited(e -> viewAllLabel.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-cursor: hand;"));

        viewAllServers.getChildren().add(viewAllLabel);

        serversContainer.getChildren().addAll(viewAllServers);

        section.getChildren().addAll(welcomeSection, serversTitle, serversContainer);
        return section;
    }

    private HBox createWelcomeSection() {
        final HBox welcomeBox = new HBox(15);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);
        welcomeBox.setPadding(new Insets(15));
        welcomeBox.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 15;");

        final Account account = this.launcher.getSelectedAccount();

        final ImageView avatarView = new ImageView();
        avatarView.setFitHeight(64);
        avatarView.setFitWidth(64);
        final Circle clip = new Circle(32, 32, 32);
        avatarView.setClip(clip);

        final Region placeholder = new Region();
        placeholder.setPrefSize(64, 64);
        placeholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 32;");

        if (account != null) {
            new Thread(() -> {
                final Image avatar = new Image("https://mc-heads.net/avatar/" + account.getUuid(), true);
                avatar.progressProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.doubleValue() == 1.0) {
                        Platform.runLater(() -> avatarView.setImage(avatar));
                    }
                });
            }).start();
        } else {
            avatarView.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");
        }

        final VBox welcomeText = new VBox(5);
        final Label welcomeLabel = new Label(account != null ?
                "Bienvenue, " + account.getUsername() :
                "Bienvenue sur AltisCore");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label subtitleLabel = new Label("Prêt à rejoindre l'aventure ?");
        subtitleLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 16px; -fx-font-family: 'Bahnschrift';");

        welcomeText.getChildren().addAll(welcomeLabel, subtitleLabel);

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final CustomButton playButton = new CustomButton("JOUER", FontAwesomeIcon.PLAY);
        playButton.setDisable(account == null);

        welcomeBox.getChildren().addAll(avatarView, welcomeText, spacer, playButton);
        return welcomeBox;
    }
}