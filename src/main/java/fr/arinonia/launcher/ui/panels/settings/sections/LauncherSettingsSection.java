package fr.arinonia.launcher.ui.panels.settings.sections;

import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.components.CustomCheckBox;
import fr.arinonia.launcher.ui.components.CustomComboBox;
import fr.arinonia.launcher.ui.components.CustomRadioButton;
import fr.arinonia.launcher.ui.components.settings.SettingsItem;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LauncherSettingsSection extends VBox {

    public LauncherSettingsSection() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        setupItems();
    }

    private void setupItems() {
        final Label sectionTitle = new Label("Paramètres du Launcher");
        sectionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final ToggleGroup themeGroup = new ToggleGroup();
        final HBox themeBox = new HBox(10);

        final CustomRadioButton darkTheme = new CustomRadioButton("Sombre");
        darkTheme.setToggleGroup(themeGroup);
        darkTheme.setSelected(true);
        darkTheme.setStyle("-fx-text-fill: white;");

        final CustomRadioButton lightTheme = new CustomRadioButton("Clair");
        lightTheme.setToggleGroup(themeGroup);
        lightTheme.setStyle("-fx-text-fill: white;");

        themeBox.getChildren().addAll(darkTheme, lightTheme);

        final TextField installPathField = new TextField();
        installPathField.setEditable(false);
        installPathField.setPrefWidth(300);
        installPathField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-text-fill: white;");

        final CustomButton browseButton = new CustomButton("Parcourir");

        final HBox pathBox = new HBox(10);
        pathBox.getChildren().addAll(installPathField, browseButton);

        final SettingsItem themeItem = new SettingsItem(
                "Thème",
                "Choisissez l'apparence du launcher",
                themeBox
        );
        themeItem.setEnabled(false);
        themeItem.setWarning("W.I.P");
        final SettingsItem installPathItem = new SettingsItem(
                "Dossier d'installation",
                "Emplacement des fichiers du jeu",
                pathBox
        );
        installPathItem.setEnabled(false);
        installPathItem.setWarning("W.I.P");

        final CustomCheckBox closeOnLaunchCheckbox = new CustomCheckBox();
        closeOnLaunchCheckbox.setStyle("-fx-text-fill: white;");

        final SettingsItem closeOnLaunchItem = new SettingsItem(
                "Fermer après le lancement",
                "Fermer automatiquement le launcher quand le jeu démarre",
                closeOnLaunchCheckbox
        );


        final CustomComboBox<String> langBox = new CustomComboBox<>();
        langBox.getItems().addAll("Français", "English");
        langBox.setValue("Français");

        final SettingsItem langItem = new SettingsItem(
                "Langue",
                "Choisissez la langue du launcher",
                langBox
        );

        this.getChildren().addAll(
                sectionTitle,
                themeItem,
                installPathItem,
                closeOnLaunchItem,
                langItem
        );
    }
}