package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.api.models.Server;
import fr.arinonia.launcher.ui.AbstractPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class HomePanel extends AbstractPanel {
    private VBox serversContainer;

    //! Add a way to go discord
    @Override
    protected void setupPanel() {
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(36, 17, 70), rgb(48, 25, 88));");
        final VBox mainContainer = new VBox(10);
        this.setPriority(mainContainer);
        mainContainer.setPadding(new Insets(20));

        final TopBar topBar = new TopBar(this);
        final HBox contentContainer = createContentContainer();

        mainContainer.getChildren().addAll(topBar, contentContainer);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        this.layout.getChildren().add(mainContainer);
    }

    private HBox createContentContainer() {
        final HBox contentContainer = new HBox(20);
        contentContainer.setAlignment(Pos.CENTER);

        final VBox serversSection = createServersSection();
        HBox.setHgrow(serversSection, Priority.ALWAYS);

        final NewsSection newsSection = new NewsSection(this.panelManager.getLauncher());

        contentContainer.getChildren().addAll(serversSection, newsSection);
        return contentContainer;
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

        initializeServers();

        serversSection.getChildren().addAll(serversTitle, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return serversSection;
    }

    private void initializeServers() {
        this.serversContainer.getChildren().clear();
        for (final Server server : this.panelManager.getLauncher().getServers()) {
            this.serversContainer.getChildren().add(new ServerItem(this, server));
        }
    }
}