package fr.arinonia.launcher.ui;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.utils.Constants;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PanelManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelManager.class);
    private final Map<Class<? extends IPanel>, IPanel> panels = new HashMap<>();
    private final Launcher launcher;
    private Stage stage;
    private GridPane layout;
    private IPanel currentPanel;



    public PanelManager(final Launcher launcher) {
        this.launcher = launcher;
    }

    public void init(final Stage stage) {
        this.stage = stage;
        LOGGER.info("Initializing panels' manager");
        this.loadFonts();
        LOGGER.info("Fonts loaded");

        this.stage.setTitle(String.format("%s | %s", Constants.APP_NAME, Constants.APP_VERSION));
        this.stage.setMinWidth(1280.0D);
        this.stage.setMinHeight(720.0D);
        this.stage.setWidth(1280.0D);
        this.stage.setHeight(720.0D);
        this.stage.centerOnScreen();

        final InputStream is = Launcher.class.getResourceAsStream("/images/icon.png");

        if (is != null) {
            this.stage.getIcons().add(new Image(is));
        }
        this.stage.setScene(new Scene(this.layout = new GridPane()));
        //this.setBackground();
        this.stage.show();
    }

    public void addPanel(IPanel panel) {
        panels.put(panel.getClass(), panel);
        LOGGER.info("Added panel: {}", panel.getClass().getSimpleName());
    }

    public <T extends IPanel> T getPanel(Class<T> panelClass) {
        return panelClass.cast(panels.get(panelClass));
    }

    public void showPanel(final Class<? extends IPanel> panelClass, final boolean transition) {
        final IPanel panel = panels.get(panelClass);
        if (panel == null) {
            LOGGER.error("Panel not found: {}", panelClass.getSimpleName());
            return;
        }

        if (this.currentPanel != null) {
            this.currentPanel.onHide();
        }

        this.layout.getChildren().clear();
        this.layout.getRowConstraints().clear();
        this.layout.getColumnConstraints().clear();

        panel.init(this);
        this.layout.getChildren().add(panel.getLayout());
        this.layout.requestFocus();
        this.currentPanel = panel;

        LOGGER.info("{} initialized", panel.getClass().getSimpleName());
        if (!transition) return;
        panel.onShow();
    }

    public void showPanel(final Class<? extends IPanel> panelClass) {
        this.showPanel(panelClass, true);
    }

    private void loadFonts() {
        Font.loadFont(this.getClass().getResourceAsStream("/fonts/roboto.ttf"), 12.0D);
        Font.loadFont(this.getClass().getResourceAsStream("/fonts/bahnschrift.ttf"), 12.0D);
    }


    public Launcher getLauncher() {
        return this.launcher;
    }
}