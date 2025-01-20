package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.Launcher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PartnersPanel extends VBox {
    private final Launcher launcher;

    public PartnersPanel(final Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(20.0D);
        this.setAlignment(Pos.CENTER);
        initialize();
    }

    private void initialize() {
        final VBox container = new VBox(15.0D);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20.0D));
        container.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        final Label title = new Label("Serveurs partenaires");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label comingSoon = new Label("Bientôt disponible");
        comingSoon.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 16px; -fx-font-family: 'Bahnschrift';");

        container.getChildren().addAll(title, comingSoon);
        VBox.setVgrow(container, Priority.ALWAYS);

        this.getChildren().add(container);
    }
}