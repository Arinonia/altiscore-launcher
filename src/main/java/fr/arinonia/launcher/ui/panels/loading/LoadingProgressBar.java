package fr.arinonia.launcher.ui.panels.loading;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoadingProgressBar extends StackPane {
    private final Rectangle progressBar;
    private Timeline progressTimeline;

    public LoadingProgressBar() {
        this.setAlignment(Pos.CENTER);
        this.setTranslateY(20);

        final Rectangle backgroundBar = createBackgroundBar();
        this.progressBar = createProgressBar();

        this.getChildren().addAll(backgroundBar, this.progressBar);
    }

    private Rectangle createBackgroundBar() {
        final Rectangle backgroundBar = new Rectangle(300, 4);
        backgroundBar.setArcWidth(4);
        backgroundBar.setArcHeight(4);
        backgroundBar.setFill(Color.rgb(48, 25, 88));
        return backgroundBar;
    }

    private Rectangle createProgressBar() {
        final Rectangle progressBar = new Rectangle(300, 4);
        progressBar.setArcWidth(4);
        progressBar.setArcHeight(4);
        progressBar.setFill(Color.rgb(149, 128, 255));
        progressBar.setScaleX(0);

        final DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(149, 128, 255, 0.7));
        glow.setRadius(10);
        progressBar.setEffect(glow);

        return progressBar;
    }

    public void updateProgress(final double progress) {
        if (this.progressTimeline != null) {
            this.progressTimeline.stop();
        }

        this.progressTimeline = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(this.progressBar.scaleXProperty(), progress, javafx.animation.Interpolator.EASE_BOTH)
                )
        );
        this.progressTimeline.play();
    }

    public void reset() {
        if (this.progressTimeline != null) {
            this.progressTimeline.stop();
        }
        this.progressBar.setScaleX(0);
    }
}