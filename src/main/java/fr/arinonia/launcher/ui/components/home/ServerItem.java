package fr.arinonia.launcher.ui.components.home;

import fr.arinonia.launcher.api.models.Server;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.minecraft.MinecraftServerPing;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.panels.home.HomePanel;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ServerItem extends HBox {
    private static final String BASE_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;";
    private final Server server;
    private Label playersLabel;
    private Label statusLabel;
    private Region statusDot;
    private MinecraftServerPing.ServerStatus serverStatus;
    private final CustomButton joinButton;
    private final Account account;

    public ServerItem(final HomePanel homePanel, final Server server) {
        super(15);
        this.server = server;
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle(BASE_STYLE);
        this.setupHover();

        final StackPane iconPlaceholder = createServerIcon();
        final VBox serverInfo = createServerInfo();
        this.account = homePanel.getPanelManager().getLauncher().getSelectedAccount();
        this.joinButton = createJoinButton(this.account != null); //! maybe set to false as default then update and check if account is null

        this.getChildren().addAll(iconPlaceholder, serverInfo, joinButton);

        final String[] hostParts = server.getHost().split(":");
        MinecraftServerPing.pingServer(
                hostParts[0],
                hostParts.length > 1 ? Integer.parseInt(hostParts[1]) : 25565,
                1000
        ).thenAccept(status -> {
            this.serverStatus = status;
            updateServerStatus();
        });
    }

    private void setupHover() {
        this.setOnMouseEntered(e -> this.setStyle(BASE_STYLE.replace("0.1", "0.2")));
        this.setOnMouseExited(e -> this.setStyle(BASE_STYLE));
    }

    private StackPane createServerIcon() {
        final double ICON_SIZE = 48.0D;
        final StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setMinSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setMaxSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 5;");

        final Label placeholderLabel = new Label(server.getName().substring(0, 1).toUpperCase());
        placeholderLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift';");
        iconContainer.getChildren().add(placeholderLabel);

        if (server.getIcon() != null && !server.getIcon().isEmpty()) {
            final Image image = new Image(server.getIcon(), true);
            final ImageView imageView = new ImageView(image);

            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(iconContainer.widthProperty());
            imageView.fitHeightProperty().bind(iconContainer.heightProperty());

            final Rectangle clip = new Rectangle(ICON_SIZE, ICON_SIZE);
            clip.setArcWidth(10.0D);
            clip.setArcHeight(10.0D);
            iconContainer.setClip(clip);

            image.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 1.0D) {
                    Platform.runLater(() -> {
                        final FadeTransition fadeIn = new FadeTransition(Duration.millis(200), imageView);
                        fadeIn.setFromValue(0.0D);
                        fadeIn.setToValue(1.0D);
                        iconContainer.getChildren().setAll(imageView);
                        fadeIn.play();
                    });
                }
            });

            image.errorProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    Platform.runLater(() -> {
                        placeholderLabel.setStyle("-fx-text-fill: #ff3333; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift';");
                    });
                }
            });
        }

        return iconContainer;
    }

    private VBox createServerInfo() {
        final VBox serverInfo = new VBox(5.0D);
        HBox.setHgrow(serverInfo, Priority.ALWAYS);

        final Label nameLabel = new Label(server.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label descriptionLabel = new Label(server.getDescription());
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);

        final HBox statusContainer = createStatusContainer();

        serverInfo.getChildren().addAll(nameLabel, descriptionLabel, statusContainer);
        return serverInfo;
    }

    private HBox createStatusContainer() {
        final HBox statusContainer = new HBox(10.0D);
        statusContainer.setAlignment(Pos.CENTER_LEFT);

        final Label versionLabel = new Label(String.format("Version %s (%s)",
                server.getVersion(),
                server.getModLoader().getValue().toLowerCase())
        );
        versionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        this.statusDot = new Region();
        this.statusDot.setPrefSize(8.0D, 8.0D);
        this.statusDot.setStyle("-fx-background-radius: 4; -fx-background-color: #ff3333;");

        this.statusLabel = new Label("Hors ligne");
        this.statusLabel.setStyle("-fx-text-fill: #ff3333; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        this.playersLabel = new Label("0/0 joueurs");
        this.playersLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        statusContainer.getChildren().addAll(versionLabel, space, this.statusDot, this.statusLabel, this.playersLabel);
        return statusContainer;
    }

    private void updateServerStatus() {
        if (this.serverStatus != null) {
            Platform.runLater(() -> {
                final boolean isOnline = this.serverStatus.isOnline();

                this.statusDot.setStyle("-fx-background-radius: 4; -fx-background-color: " +
                        (isOnline ? "#44b700" : "#ff3333") + ";");
                this.statusLabel.setText(isOnline ? "En ligne" : "Hors ligne");
                this.statusLabel.setStyle("-fx-text-fill: " +
                        (isOnline ? "#44b700" : "#ff3333") +
                        "; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

                if (isOnline) {
                    this.playersLabel.setText(String.format("%d/%d joueurs",
                            this.serverStatus.getOnlinePlayers(),
                            this.serverStatus.getMaxPlayers())
                    );
                }
                updateJoinButton();
            });
        }
    }

    private CustomButton createJoinButton(final boolean canJoin) {
        final CustomButton joinButton = new CustomButton("Rejoindre");
        joinButton.setDisable(!canJoin);
        return joinButton;
    }

    private void updateJoinButton() {
        final boolean canJoin = this.serverStatus != null &&
                this.serverStatus.isOnline() &&
                this.account != null;
        this.joinButton.setDisable(!canJoin);
    }
}