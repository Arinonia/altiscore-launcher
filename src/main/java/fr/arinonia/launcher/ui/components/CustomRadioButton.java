package fr.arinonia.launcher.ui.components;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CustomRadioButton extends RadioButton {

    private static final String DEFAULT_STYLE = """
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-family: 'Bahnschrift';
            """;

    private static final String RADIO_STYLE = """
            .radio-button .radio {
                -fx-background-color: rgba(149, 128, 255, 0.2);
                -fx-background-radius: 10px;
                -fx-border-radius: 10px;
                -fx-padding: 3px;
            }

            .radio-button:hover .radio {
                -fx-background-color: rgba(149, 128, 255, 0.3);
            }

            .radio-button .dot {
                -fx-background-color: transparent;
                -fx-padding: 4px;
            }

            .radio-button:selected .radio .dot {
                -fx-background-color: rgb(149, 128, 255);
                -fx-background-radius: 5px;
                -fx-padding: 4px;
            }

            .radio-button:selected:hover .radio {
                -fx-background-color: rgba(149, 128, 255, 0.4);
            }
            """;

    public CustomRadioButton(final String text) {
        super(text);
        setupButton();
    }

    public CustomRadioButton(final String text, final ToggleGroup group) {
        super(text);
        this.setToggleGroup(group);
        setupButton();
    }

    private void setupButton() {
        this.setStyle(DEFAULT_STYLE);
        this.getStylesheets().add(getCustomStylesheet());
        this.setCursor(Cursor.HAND);
        this.setAlignment(Pos.CENTER_LEFT);

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(3);
        shadow.setOffsetY(1);
        this.setEffect(shadow);
    }

    private String getCustomStylesheet() {
        return this.getClass().getResource("/css/custom-radio.css").toExternalForm();
    }

    public static String getCssContent() {
        return RADIO_STYLE;
    }
}