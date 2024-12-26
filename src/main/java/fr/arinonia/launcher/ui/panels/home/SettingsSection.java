package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.ui.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SettingsSection extends VBox {

    public SettingsSection() {
        super(15.0D);
        this.setPadding(new Insets(20.0D));
        this.setPrefWidth(300.0D);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        setupComponents();
    }

    private void setupComponents() {
        final Label settingsTitle = createTitle();
        final VBox settingsButtons = createButtonsContainer();

        this.getChildren().addAll(settingsTitle, settingsButtons);
    }

    private Label createTitle() {
        final Label settingsTitle = new Label("Paramètres");
        settingsTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        return settingsTitle;
    }

    private VBox createButtonsContainer() {
        final VBox settingsButtons = new VBox(10);
        settingsButtons.setAlignment(Pos.TOP_CENTER);

        final Button[] settingsOptions = {
                createSettingsButton("Général", "⚙", true),
                createSettingsButton("Java", "☕", true),
                createSettingsButton("RAM", "📊", false),
                createSettingsButton("Launcher", "🚀", true)
        };

        settingsButtons.getChildren().addAll(settingsOptions);
        return settingsButtons;
    }

    private Button createSettingsButton(final String text, final String emoji, final boolean disabled) {
        final CustomButton button = new CustomButton(emoji + " " + text);
        button.setAsMenuButton();
        button.setDisable(disabled);
        return button;
    }
}
