package fr.arinonia.launcher.ui.components.home;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SidebarToggleButton extends StackPane {
    private final Label arrowLabel;
    private boolean isExpanded = true;
    private final Runnable onToggle;

    public SidebarToggleButton(final Runnable onToggle) {
        this.onToggle = onToggle;
        this.setPrefSize(30, 30);
        this.setMaxSize(30, 30);
        this.setMinSize(30, 30);
        this.setAlignment(Pos.CENTER);
        this.setStyle("""
                -fx-background-color: rgba(149, 128, 255, 0.2);
                -fx-background-radius: 15;
                -fx-cursor: hand;
                """);

        this.arrowLabel = new Label("⮜");
        this.arrowLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        this.getChildren().add(this.arrowLabel);

        setupInteractions();
    }

    private void setupInteractions() {
        this.setOnMouseEntered(e ->
                this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.3); -fx-background-radius: 15; -fx-cursor: hand;")
        );

        this.setOnMouseExited(e ->
                this.setStyle("-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 15; -fx-cursor: hand;")
        );

        this.setOnMouseClicked(e -> toggle());
    }

    private void toggle() {
        final RotateTransition rotation = new RotateTransition(Duration.millis(250.0D), this.arrowLabel);
        rotation.setByAngle(180);
        rotation.setOnFinished(e -> {
            this.isExpanded = !this.isExpanded;
            this.arrowLabel.setRotate(this.isExpanded ? 0.0D : 180.0D);
        });
        rotation.play();

        if (this.onToggle != null) {
            this.onToggle.run();
        }
    }
}
