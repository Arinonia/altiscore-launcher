package fr.arinonia.launcher.ui.components;

import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CustomComboBox<T> extends ComboBox<T> {

    private static final String DEFAULT_STYLE = """
            -fx-background-color: rgba(149, 128, 255, 0.2);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-family: 'Bahnschrift';
            -fx-background-radius: 5px;
            -fx-prompt-text-fill: rgba(255, 255, 255, 0.7);
            -fx-mark-color: white;
            """;

    public CustomComboBox() {
        super();
        setupComboBox();
    }

    private void setupComboBox() {
        this.setStyle(DEFAULT_STYLE);
        this.getStylesheets().add(getCustomStylesheet());

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(3);
        shadow.setOffsetY(1);
        this.setEffect(shadow);
    }

    private String getCustomStylesheet() {
        return this.getClass().getResource("/css/custom-combobox.css").toExternalForm();
    }
}