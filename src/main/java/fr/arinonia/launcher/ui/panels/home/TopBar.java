package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.utils.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class TopBar extends HBox {
    private static final String STYLE = "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);";

    public TopBar() {
        super(15);
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setPadding(new Insets(10, 15, 10, 15));
        this.setStyle(STYLE);


        setupComponents();
        setupShadow();
    }

    private void setupComponents() {
        final ImageView logoView = createLogo();
        final Label titleLabel = createTitle();
        final Region spacer = createSpacer();
        final CustomButton authButton = createAuthButton();
        //final CustomButton discordButton = new CustomButton("Discord", "\uD83D\uDC9A");

        this.getChildren().addAll(logoView, titleLabel, spacer, /*discordButton,*/ authButton);
    }

    private ImageView createLogo() {
        final ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/icon.png")));
        logoView.setFitHeight(30);
        logoView.setFitWidth(30);
        return logoView;
    }

    private Label createTitle() {
        final Label titleLabel = new Label(Constants.APP_NAME);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        return titleLabel;
    }

    private Region createSpacer() {
        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private CustomButton createAuthButton() {
        final CustomButton authButton = new CustomButton("Se connecter");
        authButton.setOnAction(e -> System.out.println("Auth button clicked"));
        return authButton;
    }

    private void setupShadow() {
        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(5);
        this.setEffect(shadow);
    }

}
