package fr.arinonia.launcher.ui.components;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CustomSpinner extends Spinner<Integer> {

    private static final String DEFAULT_STYLE = """
            -fx-font-size: 14px;
            -fx-font-family: 'Bahnschrift';
            """;

    public CustomSpinner(int min, int max, int initial) {
        super(min, max, initial);
        setupSpinner();
    }

    public CustomSpinner(int min, int max, int initial, int step) {
        super(min, max, initial, step);
        setupSpinner();
    }

    public CustomSpinner(SpinnerValueFactory<Integer> valueFactory) {
        super(valueFactory);
        setupSpinner();
    }

    private void setupSpinner() {
        this.setStyle(DEFAULT_STYLE);
        this.getStylesheets().add(getCustomStylesheet());
        this.setEditable(true);
        this.setPrefWidth(200);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(3);
        shadow.setOffsetY(1);
        this.setEffect(shadow);

        this.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }

            try {
                Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                this.getEditor().setText(oldValue);
            }
        });

        this.getEditor().focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // Quand on perd le focus
                commitEditorText();
            }
        });
    }

    private void commitEditorText() {
        try {
            final String text = this.getEditor().getText();
            if (text.isEmpty()) {
                final Integer value = this.getValue();
                this.getEditor().setText(value.toString());
                return;
            }

            int value = Integer.parseInt(text);
            final SpinnerValueFactory<Integer> factory = this.getValueFactory();

            if (factory instanceof final SpinnerValueFactory.IntegerSpinnerValueFactory intFactory) {
                if (value < intFactory.getMin()) {
                    value = intFactory.getMin();
                    this.getEditor().setText(Integer.toString(value));
                } else if (value > intFactory.getMax()) {
                    value = intFactory.getMax();
                    this.getEditor().setText(Integer.toString(value));
                }
            }

            this.getValueFactory().setValue(value);
        } catch (final NumberFormatException e) {
            final Integer value = this.getValue();
            this.getEditor().setText(value.toString());
        }
    }

    private String getCustomStylesheet() {
        return this.getClass().getResource("/css/custom-spinner.css").toExternalForm();
    }
}