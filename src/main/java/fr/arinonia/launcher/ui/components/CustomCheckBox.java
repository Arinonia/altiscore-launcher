package fr.arinonia.launcher.ui.components;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CustomCheckBox extends CheckBox {

    private static final String DEFAULT_STYLE = """
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-family: 'Bahnschrift';
            """;

    private static final String CHECKBOX_STYLE = """
            .check-box .box {
                -fx-background-color: rgba(149, 128, 255, 0.2);
                -fx-background-radius: 4px;
                -fx-border-radius: 4px;
                -fx-padding: 3px;
            }

            .check-box:hover .box {
                -fx-background-color: rgba(149, 128, 255, 0.3);
            }

            .check-box .mark {
                -fx-background-color: transparent;
                -fx-shape: "M 9.97498,1.22334L 4.6983,9.09834L 4.52164,9.09834L 0,5.19331L 1.27664,3.52165L 4.255,6.08833L 8.33331,1.52588e-005L 9.97498,1.22334 Z ";
            }

            .check-box:selected .box {
                -fx-background-color: rgb(149, 128, 255);
            }

            .check-box:selected .mark {
                -fx-background-color: white;
            }

            .check-box:selected:hover .box {
                -fx-background-color: rgb(129, 108, 235);
            }
            """;

    public CustomCheckBox() {
        super();
        setupCheckBox();
    }

    public CustomCheckBox(final String text) {
        super(text);
        setupCheckBox();
    }

    private void setupCheckBox() {
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
        return this.getClass().getResource("/css/custom-checkbox.css").toExternalForm();
    }

    public static String getCssContent() {
        return CHECKBOX_STYLE;
    }
}