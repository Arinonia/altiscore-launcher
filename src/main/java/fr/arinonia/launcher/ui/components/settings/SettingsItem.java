package fr.arinonia.launcher.ui.components.settings;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SettingsItem extends VBox {
    private final String title;
    private final String description;
    private final Node control;
    private Label warningLabel;
    private boolean isEnabled = true;

    public SettingsItem(final String title, final String description, final Node control) {
        super(5);
        this.title = title;
        this.description = description;
        this.control = control;
        setupItem();
    }

    private void setupItem() {
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");

        final Label titleLabel = new Label(this.title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label descriptionLabel = new Label(this.description);
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);

        this.warningLabel = new Label();
        this.warningLabel.setStyle("-fx-text-fill: #ff7675; -fx-font-size: 12px; -fx-font-family: 'Bahnschrift';");
        this.warningLabel.setWrapText(true);
        this.warningLabel.setVisible(false);
        this.warningLabel.setManaged(false);

        final HBox controlContainer = new HBox(10);
        controlContainer.setAlignment(Pos.CENTER_RIGHT);

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        controlContainer.getChildren().addAll(spacer, this.control);

        this.getChildren().addAll(titleLabel, descriptionLabel, this.warningLabel, controlContainer);

        setupHoverEffect();
    }

    private void setupHoverEffect() {
        this.setOnMouseEntered(e -> {
            if (this.isEnabled) {
                this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 10;");
            }
        });

        this.setOnMouseExited(e -> {
            if (this.isEnabled) {
                this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
            } else {
                this.setStyle("-fx-background-color: rgba(180, 180, 180, 0.1); -fx-background-radius: 10;");
            }
        });
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        this.control.setDisable(!enabled);

        if (!enabled) {
            this.setStyle("-fx-background-color: rgba(180, 180, 180, 0.1); -fx-background-radius: 10;");
        } else {
            this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
        }
    }

    public void setWarning(String warningMessage) {
        if (warningMessage != null && !warningMessage.isEmpty()) {
            this.warningLabel.setText("⚠ " + warningMessage);
            this.warningLabel.setVisible(true);
            this.warningLabel.setManaged(true);
        } else {
            this.warningLabel.setVisible(false);
            this.warningLabel.setManaged(false);
        }
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }
}