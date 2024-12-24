package fr.arinonia.launcher.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public abstract class AbstractPanel implements IPanel {
    protected final GridPane layout = new GridPane();
    protected PanelManager panelManager;

    @Override
    public void init(final PanelManager panelManager) {
        this.panelManager = panelManager;
        setPriority(this.layout);
        setupPanel();
    }

    protected abstract void setupPanel();

    protected void setPriority(final Node element) {
        GridPane.setHgrow(element, Priority.ALWAYS);
        GridPane.setVgrow(element, Priority.ALWAYS);
    }

    protected void setAlignment(final Node element, final HPos hPos, final VPos vPos) {
        GridPane.setHalignment(element, hPos);
        GridPane.setValignment(element, vPos);
    }

    @Override
    public void onShow() {
        final FadeTransition transition = new FadeTransition();
        transition.setNode(this.layout);
        transition.setFromValue(0.0D);
        transition.setToValue(1.0D);
        transition.setAutoReverse(true);
        transition.play();
    }

    @Override
    public Region getLayout() {
        return this.layout;
    }

    @Override
    public PanelManager getPanelManager() {
        return this.panelManager;
    }
}
