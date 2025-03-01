package fr.arinonia.launcher.ui.components.profile;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.arinonia.launcher.ui.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ProfileCard extends VBox {
    private static final String BASE_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;";
    private static final String HOVER_STYLE = "-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 10;";

    public ProfileCard(final String name, final String description, final String modLoader, final String version, final boolean isDefault) {
        this.setPrefWidth(350);
        this.setMinHeight(200);
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle(BASE_STYLE);
        this.setCursor(javafx.scene.Cursor.HAND);

        final HBox mainContainer = new HBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        final VBox leftColumn = createLeftColumn(modLoader);

        final VBox rightColumn = new VBox(10);
        rightColumn.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        final HBox header = createHeader(name, isDefault);

        final Label descriptionLabel = createDescriptionLabel(description);

        final VBox profileInfo = createProfileInfo(modLoader, version);

        final HBox statsContainer = createStatsContainer();

        final Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        final HBox actions = createActionButtons(isDefault);

        rightColumn.getChildren().addAll(header, descriptionLabel, profileInfo, statsContainer, spacer, actions);
        mainContainer.getChildren().addAll(leftColumn, rightColumn);

        this.getChildren().add(mainContainer);

        setupHoverEffects();
    }

    private VBox createLeftColumn(final String modLoader) {
        final VBox column = new VBox(10);
        column.setAlignment(Pos.TOP_CENTER);
        column.setPrefWidth(64);

        final FontAwesomeIconView modLoaderIcon = getModLoaderIcon(modLoader);
        modLoaderIcon.setGlyphSize(36);
        modLoaderIcon.setFill(Color.web("rgb(149, 128, 255)"));

        final Circle statusDot = new Circle(4);
        statusDot.setFill(Color.web("#44b700"));

        final Label statusLabel = new Label("Actif");
        statusLabel.setStyle("-fx-text-fill: #44b700; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        column.getChildren().addAll(modLoaderIcon, statusDot, statusLabel);
        return column;
    }

    private FontAwesomeIconView getModLoaderIcon(final String modLoader) {
        FontAwesomeIcon icon;

        //! replace with better icons later
        switch (modLoader.toLowerCase()) {
            case "fabric":
                icon = FontAwesomeIcon.SCISSORS;
                break;
            case "forge":
                icon = FontAwesomeIcon.FIRE;
                break;
            case "neoforge":
                icon = FontAwesomeIcon.FIRE_EXTINGUISHER;
                break;
            case "vanilla":
            default:
                icon = FontAwesomeIcon.CUBE;
                break;
        }

        return new FontAwesomeIconView(icon);
    }

    private HBox createHeader(final String name, final boolean isDefault) {
        final HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        final Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final HBox badges = new HBox(5);
        badges.setAlignment(Pos.CENTER_LEFT);

        if (isDefault) {
            badges.getChildren().add(createBadge("Par défaut", "#44b700"));
        }

        header.getChildren().addAll(nameLabel, badges);
        return header;
    }

    private Label createDescriptionLabel(final String description) {
        final Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxHeight(40);
        return descriptionLabel;
    }

    private VBox createProfileInfo(final String modLoader, final String version) {
        final VBox infoContainer = new VBox(5);

        final Label versionInfo = new Label(modLoader.substring(0, 1).toUpperCase() + modLoader.substring(1) + " " + version);
        versionInfo.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        infoContainer.getChildren().add(versionInfo);
        return infoContainer;
    }

    private HBox createStatsContainer() {
        final HBox statsContainer = new HBox(15);
        statsContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 5; -fx-padding: 8;");

        final VBox modsStats = createStatItem("Mods", "23", FontAwesomeIcon.PUZZLE_PIECE);

        final VBox sizeStats = createStatItem("Taille", "1.2 GB", FontAwesomeIcon.DATABASE);

        final VBox lastPlayedStats = createStatItem("Dernier lancement", "Il y a 2h", FontAwesomeIcon.CLOCK_ALT);

        statsContainer.getChildren().addAll(modsStats, sizeStats, lastPlayedStats);
        return statsContainer;
    }

    private VBox createStatItem(final String label, final String value, final FontAwesomeIcon icon) {
        final VBox stat = new VBox(2);
        stat.setAlignment(Pos.CENTER_LEFT);

        final HBox valueBox = new HBox(5);
        valueBox.setAlignment(Pos.CENTER_LEFT);

        final FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setGlyphSize(12);
        iconView.setFill(Color.web("rgb(149, 128, 255)"));

        final Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        valueBox.getChildren().addAll(iconView, valueLabel);

        final Label descLabel = new Label(label);
        descLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");

        stat.getChildren().addAll(valueBox, descLabel);
        return stat;
    }

    private Label createBadge(final String text, final String color) {
        final Label badge = new Label(text);
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

    private HBox createActionButtons(final boolean isDefault) {
        final HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        final FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY);
        playIcon.setGlyphSize(14);
        playIcon.setFill(Color.WHITE);

        final CustomButton playButton = new CustomButton("Jouer");
        playButton.setGraphic(playIcon);
        playButton.setGraphicTextGap(5);

        if (!isDefault) {
            final FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            editIcon.setGlyphSize(14);
            editIcon.setFill(Color.web("rgb(180, 180, 180)"));

            final CustomButton editButton = new CustomButton("Modifier");
            editButton.setGraphic(editIcon);
            editButton.setGraphicTextGap(5);
            //editButton.setStyle("-fx-background-color: transparent;");

            final FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            deleteIcon.setGlyphSize(14);
            deleteIcon.setFill(Color.web("rgb(180, 180, 180)"));

            final CustomButton deleteButton = new CustomButton("Supprimer");
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setGraphicTextGap(5);
            deleteButton.setStyle("-fx-background-color: transparent;");

            actions.getChildren().addAll(playButton, editButton, deleteButton);
        } else {
            final FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            editIcon.setGlyphSize(14);
            editIcon.setFill(Color.web("rgb(180, 180, 180)"));

            final CustomButton editButton = new CustomButton("Modifier");
            editButton.setGraphic(editIcon);
            editButton.setGraphicTextGap(5);
            //editButton.setStyle("-fx-background-color: transparent;");

            actions.getChildren().addAll(playButton, editButton);
        }

        return actions;
    }

    private void setupHoverEffects() {
        this.setOnMouseEntered(e -> this.setStyle(HOVER_STYLE));
        this.setOnMouseExited(e -> this.setStyle(BASE_STYLE));
    }
}