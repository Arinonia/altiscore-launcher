package fr.arinonia.launcher.ui;

import fr.arinonia.launcher.Launcher;
import javafx.scene.layout.Region;

public interface IPanel {

    Region getLayout();
    default void onShow() {}
    default void onHide() {}
    void init(final PanelManager panelManager);
    PanelManager getPanelManager();
}