package fr.arinonia.launcher.ui.panels.loading;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingAnimation extends VBox {

    private ParallelTransition parallelTransition;
    private static final int NUM_CIRCLES = 3;

    public LoadingAnimation() {
        this.setAlignment(Pos.CENTER);
        setupAnimation();
    }

    private void setupAnimation() {
       final StackPane circlesContainer = new StackPane();
       circlesContainer.setAlignment(Pos.CENTER);

       final Circle[] circles = createCircles();
       this.parallelTransition = new ParallelTransition();


       for (int i = 0; i < NUM_CIRCLES; i++) {
           setupCircleAnimations(circles[i], i);
       }

       circlesContainer.getChildren().addAll(circles);
       this.getChildren().add(circlesContainer);
    }

    private Circle[] createCircles() {
        final Circle[] circles = new Circle[NUM_CIRCLES];

        for (int i = 0; i < NUM_CIRCLES; i++) {
            circles[i] = new Circle(8, Color.rgb(149, 128, 255));
            circles[i].setTranslateX(i * 30 - 30);

            final GaussianBlur blur = new GaussianBlur(2);
            circles[i].setEffect(blur);
        }
        return circles;
    }

    private void setupCircleAnimations(final Circle circle, final int index) {
        final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), circle);
        translateTransition.setByY(-20);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setDelay(Duration.millis(index * 100));

        final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), circle);
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setDelay(Duration.millis(index * 100));

        this.parallelTransition.getChildren().addAll(translateTransition, scaleTransition);
    }

    public void play() {
        this.parallelTransition.play();
    }

    public void stop() {
        this.parallelTransition.stop();
    }
}
