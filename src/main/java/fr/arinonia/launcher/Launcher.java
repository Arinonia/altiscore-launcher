package fr.arinonia.launcher;

import fr.arinonia.launcher.ui.PanelManager;
import fr.arinonia.launcher.ui.panels.home.HomePanel;
import fr.arinonia.launcher.ui.panels.loading.LoadingPanel;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    private PanelManager panelManager;

    public void init(final Stage stage) {
        LOGGER.info("Starting launcher...");
        this.panelManager = new PanelManager(this);
        this.panelManager.init(stage);
        //TODO show home panel
        this.panelManager.addPanel(new LoadingPanel());
        this.panelManager.addPanel(new HomePanel());
        this.panelManager.showPanel(LoadingPanel.class);
    }

    public PanelManager getPanelManager() {
        return this.panelManager;
    }
}
