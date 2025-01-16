package fr.arinonia.launcher.ui.components.home;

import fr.arinonia.launcher.api.models.News;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class NewsCard extends VBox {
    private final String title;
    private final String content;
    private final String imageUrl;
    private final String author;
    private final News.NewsType type;
    private final String createdAt;
    private final boolean important;

    public NewsCard(final String title, final String content, final String imageUrl,
                    final String author, final News.NewsType type, final String createdAt,
                    final boolean important) {
        super(5);
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.author = author;
        this.type = type;
        this.createdAt = createdAt;
        this.important = important;

        setupCard();
    }

    private void setupCard() {
        this.setPadding(new Insets(10));
        this.setStyle(getBackgroundStyle(false));

        if (this.imageUrl != null) {
            final ImageView imageView = new ImageView(new Image(this.imageUrl, true));
            imageView.setFitWidth(260);

            final StackPane imageContainer = new StackPane(imageView);
            imageContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
            imageContainer.setPrefHeight(120);
            imageContainer.setMinHeight(120);
            imageContainer.setMaxHeight(120);

            imageView.setPreserveRatio(true);
            imageContainer.setClip(new Rectangle(260, 120));

            this.getChildren().add(imageContainer);
        }

        final Label titleLabel = new Label(this.title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        if (this.important) {
            final Label warningLabel = new Label(" ⚠ ");
            warningLabel.setStyle("-fx-text-fill: #ff7675;");
            titleLabel.setGraphic(warningLabel);
        }

        final Label contentLabel = new Label(this.content);
        contentLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");
        contentLabel.setWrapText(true);
        contentLabel.setMaxHeight(60);


        final HBox metaInfo = new HBox(10);
        metaInfo.setAlignment(Pos.CENTER_LEFT);

        final Label authorLabel = new Label(this.author);
        authorLabel.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 11px; -fx-font-family: 'Bahnschrift';");

        final Label dateLabel = new Label(this.createdAt);
        dateLabel.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 11px; -fx-font-family: 'Bahnschrift';");

        final Label typeLabel = new Label(this.type.getDisplayName());
        typeLabel.setStyle("-fx-text-fill: " + getTypeColor() + "; -fx-font-size: 11px; -fx-font-family: 'Bahnschrift';");

        metaInfo.getChildren().addAll(authorLabel, dateLabel, typeLabel);

        this.getChildren().addAll(titleLabel, contentLabel, metaInfo);

        this.setOnMouseEntered(e -> this.setStyle(getBackgroundStyle(true)));
        this.setOnMouseExited(e -> this.setStyle(getBackgroundStyle(false)));
    }

    private String getBackgroundStyle(final boolean hover) {
        final String baseOpacity = hover ? "0.2" : "0.1";
        return "-fx-background-color: rgba(149, 128, 255, " + baseOpacity + "); -fx-background-radius: 10;";
    }

    private String getTypeColor() {
        return switch (this.type) {
            case ANNOUNCEMENT -> "#ff7675";
            case UPDATE -> "#74b9ff";
            case EVENT -> "#55efc4";
            default -> "rgb(149, 128, 255)";
        };
    }
}
