package fr.arinonia.launcher.ui.components.profile;

import fr.arinonia.launcher.ui.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ProfileCard extends VBox {
    private static final String BASE_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;";
    private static final String HOVER_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 10;";

    public ProfileCard(String name, String description, String modLoader, String version, boolean isDefault) {
        this.setPrefWidth(300); // Augmenté pour accueillir plus d'informations
        this.setMinHeight(200); // Augmenté pour accueillir plus d'informations
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle(BASE_STYLE);
        this.setCursor(javafx.scene.Cursor.HAND);

        // Container principal pour diviser la carte en deux colonnes
        HBox mainContainer = new HBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Colonne de gauche avec l'icône
        VBox leftColumn = createLeftColumn(modLoader);

        // Colonne de droite avec les informations
        VBox rightColumn = new VBox(10);
        rightColumn.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        // En-tête (titre + badges)
        HBox header = createHeader(name, isDefault);

        // Description
        Label descriptionLabel = createDescriptionLabel(description);

        // Informations sur le profil
        VBox profileInfo = createProfileInfo(modLoader, version);

        // Stats du profil
        HBox statsContainer = createStatsContainer();

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Boutons d'action
        HBox actions = createActionButtons(isDefault);

        rightColumn.getChildren().addAll(header, descriptionLabel, profileInfo, statsContainer, spacer, actions);
        mainContainer.getChildren().addAll(leftColumn, rightColumn);

        this.getChildren().add(mainContainer);

        // Effets de survol
        setupHoverEffects();
    }

    private VBox createLeftColumn(String modLoader) {
        VBox column = new VBox(10);
        column.setAlignment(Pos.TOP_CENTER);
        column.setPrefWidth(64);

        // Icône du ModLoader
        ImageView modLoaderIcon = new ImageView();
        modLoaderIcon.setFitWidth(48);
        modLoaderIcon.setFitHeight(48);
        // TODO: Charger l'icône appropriée selon le ModLoader

        // Statut du profil (point vert/rouge)
        Circle statusDot = new Circle(4);
        statusDot.setFill(Color.web("#44b700")); // Vert par défaut

        Label statusLabel = new Label("Actif");
        statusLabel.setStyle("-fx-text-fill: #44b700; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        column.getChildren().addAll(modLoaderIcon, statusDot, statusLabel);
        return column;
    }

    private HBox createHeader(String name, boolean isDefault) {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        HBox badges = new HBox(5);
        badges.setAlignment(Pos.CENTER_LEFT);

        if (isDefault) {
            badges.getChildren().add(createBadge("Par défaut", "#44b700"));
        }

        header.getChildren().addAll(nameLabel, badges);
        return header;
    }

    private Label createDescriptionLabel(String description) {
        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxHeight(40); // Limite la hauteur de la description
        return descriptionLabel;
    }

    private VBox createProfileInfo(String modLoader, String version) {
        VBox infoContainer = new VBox(5);

        Label versionInfo = new Label(modLoader.substring(0, 1).toUpperCase() + modLoader.substring(1) + " " + version);
        versionInfo.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        infoContainer.getChildren().add(versionInfo);
        return infoContainer;
    }

    private HBox createStatsContainer() {
        HBox statsContainer = new HBox(15);
        statsContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 5; -fx-padding: 8;");

        // Stats pour les mods
        VBox modsStats = createStatItem("Mods", "23");

        // Stats pour la taille
        VBox sizeStats = createStatItem("Taille", "1.2 GB");

        // Stats pour le dernier lancement
        VBox lastPlayedStats = createStatItem("Dernier lancement", "Il y a 2h");

        statsContainer.getChildren().addAll(modsStats, sizeStats, lastPlayedStats);
        return statsContainer;
    }

    private VBox createStatItem(String label, String value) {
        VBox stat = new VBox(2);
        stat.setAlignment(Pos.CENTER_LEFT);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        Label descLabel = new Label(label);
        descLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        stat.getChildren().addAll(valueLabel, descLabel);
        return stat;
    }

    private Label createBadge(String text, String color) {
        Label badge = new Label(text);
        badge.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-padding: 2 8;
            -fx-background-radius: 5;
            -fx-font-size: 12px;
            -fx-font-family: 'Bahnschrift';
        """, color));
        return badge;
    }

    private HBox createActionButtons(boolean isDefault) {
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        CustomButton playButton = new CustomButton("Jouer", "▶");

        if (!isDefault) {
            CustomButton editButton = new CustomButton("Modifier", "✏️");
            editButton.setStyle("-fx-background-color: transparent;");

            CustomButton deleteButton = new CustomButton("Supprimer", "🗑️");
            deleteButton.setStyle("-fx-background-color: transparent;");

            actions.getChildren().addAll(playButton, editButton, deleteButton);
        } else {
            CustomButton editButton = new CustomButton("Modifier", "✏️");
            editButton.setStyle("-fx-background-color: transparent;");
            actions.getChildren().addAll(playButton, editButton);
        }

        return actions;
    }

    private void setupHoverEffects() {
        this.setOnMouseEntered(e -> this.setStyle(HOVER_STYLE));
        this.setOnMouseExited(e -> this.setStyle(BASE_STYLE));
    }
}