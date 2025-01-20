package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.ui.AbstractPanel;
import fr.arinonia.launcher.ui.components.home.Sidebar;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class HomePanel extends AbstractPanel {
    private VBox mainContainer;
    private HBox contentContainer;
    private VBox contentSection;
    private Sidebar sidebar;
    private Sidebar.SidebarSection currentSection;
    private TopBar topBar;

    @Override
    protected void setupPanel() {
        initializePanel();
    }

    private void initializePanel() {
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(36, 17, 70), rgb(48, 25, 88));");

        this.mainContainer = new VBox(10.0D);
        this.setPriority(this.mainContainer);
        this.mainContainer.setPadding(new Insets(20.0D));

        this.topBar = new TopBar(this);

        this.contentContainer = new HBox(20.0D);
        VBox.setVgrow(this.contentContainer, Priority.ALWAYS);

        this.sidebar = new Sidebar(this::handleSectionChange);

        this.contentSection = new VBox(0.0D);
        this.contentSection.setFillWidth(true);
        HBox.setHgrow(this.contentSection, Priority.ALWAYS);
        VBox.setVgrow(this.contentSection, Priority.ALWAYS);

        this.contentContainer.getChildren().addAll(this.sidebar, this.contentSection);
        this.mainContainer.getChildren().addAll(this.topBar, this.contentContainer);
        this.layout.getChildren().add(this.mainContainer);

        this.currentSection = Sidebar.SidebarSection.SERVERS;
        updateContent(Sidebar.SidebarSection.SERVERS);
    }

    private void updateContent(final Sidebar.SidebarSection section) {
        this.contentSection.getChildren().clear();
        Region content = null;

        switch (section) {
            case SERVERS -> {
                content = new ServersPanel(this.panelManager.getLauncher());
            }
            case PARTNERS -> {
                content = new PartnersPanel(this.panelManager.getLauncher());
            }
            case MY_SERVERS -> {
                content = new MyServersPanel(this.panelManager.getLauncher());
            }
            case PROFILES -> {
                content = new ProfilesPanel(this.panelManager.getLauncher());
            }
        }

        if (content != null) {
            VBox.setVgrow(content, Priority.ALWAYS);
            this.contentSection.getChildren().add(content);
        }
    }

    private void handleSectionChange(final Sidebar.SidebarSection newSection) {
        if (this.currentSection != newSection) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150.0D), this.contentSection);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                updateContent(newSection);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(150), this.contentSection);
                fadeIn.setFromValue(0.0D);
                fadeIn.setToValue(1.0D);
                fadeIn.play();
            });

            fadeOut.play();
            this.currentSection = newSection;
        }
    }

    @Override
    public void onHide() {
        super.onHide();
        this.layout.getChildren().clear();
    }

    public TopBar getTopBar() {
        return this.topBar;
    }
}