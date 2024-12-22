package fr.arinonia.launcher.ui.components;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CustomButton extends Button {
    private static final String DEFAULT_STYLE = """
            -fx-background-color: rgb(149, 128, 255);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-font-family: 'Bahnschrift';
            -fx-background-radius: 5;
            """;

    private static final String HOVER_STYLE = """
            -fx-background-color: rgb(129, 108, 235);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-font-family: 'Bahnschrift';
            -fx-background-radius: 5;
            """;

    public CustomButton(final String text) {
        super(text);
        setupButton();
    }

    public CustomButton(final String text, final String emoji) {
        super(emoji + " " + text);
        setupButton();
    }

    private void setupButton() {
        this.setCursor(Cursor.HAND);
        this.setStyle(DEFAULT_STYLE);

        this.setOnMouseEntered(e -> this.setStyle(HOVER_STYLE));
        this.setOnMouseExited(e -> this.setStyle(DEFAULT_STYLE));

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(5);
        this.setEffect(shadow);
    }

    public void setAsMenuButton() {
        this.setPrefWidth(200);
    }
}