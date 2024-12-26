package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.ui.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ServerItem extends HBox {
    private static final String BASE_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;";
    public ServerItem(final HomePanel homePanel, final String name, final String version, final boolean isOnline, final int players, final int maxPlayers) {
        super(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle(BASE_STYLE);
        this.setupHover();

        final Region iconPlaceholder = createIconPlaceholder();
        final VBox serverInfo = createServerInfo(name, version, isOnline, players, maxPlayers);
        final Account account = homePanel.getPanelManager().getLauncher().getSelectedAccount();
        final CustomButton joinButton = createJoinButton(account != null && isOnline);

        this.getChildren().addAll(iconPlaceholder, serverInfo, joinButton);
    }

    private void setupHover() {
        this.setOnMouseEntered(e -> this.setStyle(BASE_STYLE.replace("0.1", "0.2")));
        this.setOnMouseExited(e -> this.setStyle(BASE_STYLE));
    }

    private Region createIconPlaceholder() {
        final Region iconPlaceholder = new Region();
        iconPlaceholder.setPrefSize(32, 32);
        iconPlaceholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 5;");
        return iconPlaceholder;
    }

    private VBox createServerInfo(final String name, final String version, final boolean isOnline, final int players, final int maxPlayers) {
        final VBox serverInfo = new VBox(5);
        HBox.setHgrow(serverInfo, Priority.ALWAYS);

        final Label nameLabel = createNameLabel(name);
        final HBox statusContainer = createStatusContainer(version, isOnline, players, maxPlayers);

        serverInfo.getChildren().addAll(nameLabel, statusContainer);
        return serverInfo;
    }

    private Label createNameLabel(final String name) {
        final Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        return nameLabel;
    }

    private HBox createStatusContainer(final String version, final boolean isOnline, final int players, final int maxPlayers) {
        final HBox statusContainer = new HBox(10);
        statusContainer.setAlignment(Pos.CENTER_LEFT);

        final Label versionLabel = new Label("Version " + version);
        versionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        final Region statusDot = new Region();
        statusDot.setPrefSize(8, 8);
        statusDot.setStyle("-fx-background-radius: 4; -fx-background-color: " + (isOnline ? "#44b700" : "#ff3333") + ";");

        final Label statusLabel = new Label(isOnline ? "En ligne" : "Hors ligne");
        statusLabel.setStyle("-fx-text-fill: " + (isOnline ? "#44b700" : "#ff3333") + "; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final Label playersLabel = new Label(String.format("%d/%d joueurs", players, maxPlayers));
        playersLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        statusContainer.getChildren().addAll(versionLabel, space, statusDot, statusLabel, playersLabel);
        return statusContainer;
    }

    private CustomButton createJoinButton(final boolean isOnline) {
        final CustomButton joinButton = new CustomButton("Rejoindre");
        joinButton.setDisable(!isOnline);
        return joinButton;
    }

}
