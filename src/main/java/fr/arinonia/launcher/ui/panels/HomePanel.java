package fr.arinonia.launcher.ui.panels;

import fr.arinonia.launcher.ui.AbstractPanel;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.utils.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class HomePanel extends AbstractPanel {
    private VBox serversContainer;

    //! Add a way to go discord
    @Override
    protected void setupPanel() {
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(36, 17, 70), rgb(48, 25, 88));");

        final VBox mainContainer = new VBox(10);
        this.setPriority(mainContainer);
        mainContainer.setPadding(new Insets(20));

        final HBox topBar = createTopBar();

        final HBox contentContainer = new HBox(20);
        contentContainer.setAlignment(Pos.CENTER);

        final VBox serversSection = createServersSection();
        HBox.setHgrow(serversSection, Priority.ALWAYS);

        final VBox settingsSection = createSettingsSection();

        contentContainer.getChildren().addAll(serversSection, settingsSection);

        mainContainer.getChildren().addAll(topBar, contentContainer);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        this.layout.getChildren().add(mainContainer);
    }

    private HBox createTopBar() {
        final HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("""
            -fx-background-color: rgba(0, 0, 0, 0.3);
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);
        """);
        final ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/icon.png")));
        logoView.setFitHeight(30);
        logoView.setFitWidth(30);

        final Label titleLabel = new Label(Constants.APP_NAME);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final CustomButton authButton = new CustomButton("Se connecter");

        topBar.getChildren().addAll(logoView, titleLabel, spacer, authButton);

        final DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        dropShadow.setRadius(10);
        topBar.setEffect(dropShadow);

        return topBar;
    }

    private VBox createServersSection() {
        final VBox serversSection = new VBox(15);
        serversSection.setPadding(new Insets(20));
        serversSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        final Label serversTitle = new Label("Serveurs disponibles");
        serversTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        this.serversContainer = new VBox(10);
        final ScrollPane scrollPane = new ScrollPane(this.serversContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        addServerItem("Altis Life", "1.20.1", true, 145, 200);
        addServerItem("SkyBlock", "1.20.1", true, 87, 100);
        addServerItem("Prison", "1.20.2", false, 0, 150);
        addServerItem("Creative", "1.20.1", true, 23, 50);
        addServerItem("Survie", "1.20.1", true, 56, 100);

        serversSection.getChildren().addAll(serversTitle, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return serversSection;
    }

    private VBox createSettingsSection() {
        final VBox settingsSection = new VBox(15);
        settingsSection.setPadding(new Insets(20));
        settingsSection.setPrefWidth(300);
        settingsSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        final Label settingsTitle = new Label("Paramètres");
        settingsTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final VBox settingsButtons = new VBox(10);
        settingsButtons.setAlignment(Pos.TOP_CENTER);

        final Button[] settingsOptions = {
                createSettingsButton("Général", "⚙", true),
                createSettingsButton("Java", "☕", true),
                createSettingsButton("RAM", "📊", false),
                createSettingsButton("Launcher", "🚀", true)
        };

        settingsButtons.getChildren().addAll(settingsOptions);
        settingsSection.getChildren().addAll(settingsTitle, settingsButtons);

        return settingsSection;
    }

    private void addServerItem(final String serverName, final String version, final boolean isOnline, final int players, final int maxPlayers) {
        final HBox serverItem = new HBox(15);
        serverItem.setPadding(new Insets(15));
        serverItem.setAlignment(Pos.CENTER_LEFT);
        final String baseStyle = "-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;";

        serverItem.setStyle(baseStyle);
        serverItem.setOnMouseEntered(e ->
                serverItem.setStyle(baseStyle.replace("0.1", "0.2"))
        );
        serverItem.setOnMouseExited(e ->
                serverItem.setStyle(baseStyle)
        );
        final Region iconPlaceholder = new Region();
        iconPlaceholder.setPrefSize(32, 32);
        iconPlaceholder.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 5;");

        final VBox serverInfo = new VBox(5);
        HBox.setHgrow(serverInfo, Priority.ALWAYS);

        final Label nameLabel = new Label(serverName);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final HBox statusContainer = new HBox(10);
        statusContainer.setAlignment(Pos.CENTER_LEFT);

        final Label versionLabel = new Label("Version " + version);
        versionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final Region statusDot = new Region();
        statusDot.setPrefSize(8, 8);
        statusDot.setStyle("-fx-background-radius: 4; -fx-background-color: " + (isOnline ? "#44b700" : "#ff3333") + ";");

        final Label statusLabel = new Label(isOnline ? "En ligne" : "Hors ligne");
        statusLabel.setStyle("-fx-text-fill: " + (isOnline ? "#44b700" : "#ff3333") + "; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final Label playersLabel = new Label(String.format("%d/%d joueurs", players, maxPlayers));
        playersLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        final CustomButton joinButton = new CustomButton("Rejoindre");
        joinButton.setDisable(!isOnline);

        statusContainer.getChildren().addAll(versionLabel, new Region(), statusDot, statusLabel, playersLabel);
        HBox.setHgrow(statusContainer.getChildren().get(1), Priority.ALWAYS);

        serverInfo.getChildren().addAll(nameLabel, statusContainer);
        serverItem.getChildren().addAll(iconPlaceholder, serverInfo, joinButton);

        serverItem.setOnMouseEntered(e ->
                serverItem.setStyle("-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 10;")
        );
        serverItem.setOnMouseExited(e ->
                serverItem.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;")
        );

        this.serversContainer.getChildren().add(serverItem);
    }

    private Button createSettingsButton(final String text, final String emoji, final boolean disabled) {
        final CustomButton button = new CustomButton(emoji + " " + text);
        button.setAsMenuButton();
        button.setDisable(disabled);
        return button;
    }

}