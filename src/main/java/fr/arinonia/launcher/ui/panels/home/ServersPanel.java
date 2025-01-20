package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.api.models.Server;
import fr.arinonia.launcher.ui.components.home.ServerItem;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class ServersPanel extends HBox {
    private final Launcher launcher;
    private VBox serversContainer;

    public ServersPanel(final Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(20);
        initialize();
    }

    private void initialize() {
        final VBox serverSection = createServerSection();
        HBox.setHgrow(serverSection, Priority.ALWAYS);

        final NewsSection newsSection = new NewsSection(this.launcher);

        this.getChildren().addAll(serverSection, newsSection);
    }

    private VBox createServerSection() {
        final VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        final Label title = new Label("Serveurs disponibles");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        this.serversContainer = new VBox(10);
        final ScrollPane scrollPane = new ScrollPane(this.serversContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());

        initializeServers();

        section.getChildren().addAll(title, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return section;
    }

    private void initializeServers() {
        this.serversContainer.getChildren().clear();
        for (final Server server : this.launcher.getServers()) {
            final ServerItem serverItem = new ServerItem(this.launcher, server);
            this.serversContainer.getChildren().add(serverItem);
        }
    }
}