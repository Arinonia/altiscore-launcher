package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.api.models.News;
import fr.arinonia.launcher.ui.components.home.NewsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class NewsSection extends VBox {
    private final Launcher launcher;
    public NewsSection(final Launcher launcher) {
        super(15.0D);
        this.launcher = launcher;
        this.setPadding(new Insets(5.0D));
        this.setPrefWidth(340.0D);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");
        setupComponents();
    }

    private void setupComponents() {
        final Label newsTitle = new Label("Nouveautés");
        newsTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        newsTitle.setPadding(new Insets(15, 15, 5, 15));

        final VBox newsContainer = new VBox(10);
        newsContainer.setAlignment(Pos.TOP_LEFT);
        newsContainer.setPadding(new Insets(0, 20, 15, 15));
        newsContainer.setStyle("-fx-background-insets: 0 10 0 0;"); //right padding

        final VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(newsTitle, newsContainer);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(contentBox);
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().add(scrollPane);
        VBox.setVgrow(this, Priority.ALWAYS);

        final List<News> newsList = launcher.getNews();
        for (final News news : newsList) {
            final NewsCard newsCard = new NewsCard(
                    news.getTitle(),
                    news.getContent(),
                    news.getImageUrl(),
                    news.getAuthor(),
                    news.getType(),
                    news.getCreatedAt(),
                    news.isImportant()
            );
            newsContainer.getChildren().add(newsCard);
        }
    }
}