package fr.arinonia.launcher.ui.components.home;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SidebarToggleButton extends StackPane {
    private final FontAwesomeIconView iconView;
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

        this.iconView = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_LEFT);
        this.iconView.setGlyphSize(14);
        this.iconView.setFill(Color.WHITE);

        this.getChildren().add(this.iconView);

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
        final RotateTransition rotation = new RotateTransition(Duration.millis(250.0D), this.iconView);
        rotation.setByAngle(180);
        rotation.setOnFinished(e -> {
            this.isExpanded = !this.isExpanded;
            this.iconView.setIcon(this.isExpanded ? FontAwesomeIcon.CHEVRON_LEFT : FontAwesomeIcon.CHEVRON_RIGHT);
        });
        rotation.play();

        if (this.onToggle != null) {
            this.onToggle.run();
        }
    }
}