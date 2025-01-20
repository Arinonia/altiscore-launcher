package fr.arinonia.launcher.ui.panels.home;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.components.profile.ProfileCard;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ProfilesPanel extends VBox {
    private final Launcher launcher;
    private final FlowPane profilesGrid;
    private final VBox contentBox;
    private final StackPane mainContent;
    private VBox creationForm;

    public ProfilesPanel(Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(20);
        this.setFillWidth(true);
        VBox.setVgrow(this, Priority.ALWAYS);

        // Header avec titre et bouton
        HBox header = createHeader();

        // StackPane pour gérer la superposition des vues
        this.mainContent = new StackPane();
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Content section avec scroll
        this.contentBox = new VBox(20);
        this.contentBox.setPadding(new Insets(20));
        this.contentBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        // Grid pour les profils
        this.profilesGrid = new FlowPane(20, 20);
        this.profilesGrid.setPadding(new Insets(10));
        this.profilesGrid.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(profilesGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.contentBox.getChildren().add(scrollPane);

        // Vue de création de profil
        this.creationForm = createProfileCreationForm();
        this.creationForm.setVisible(false);
        this.creationForm.setOpacity(0);

        this.mainContent.getChildren().addAll(this.contentBox, this.creationForm);
        this.getChildren().addAll(header, this.mainContent);

        loadProfiles();
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 15, 0, 15));

        Label title = new Label("Profils Minecraft");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        CustomButton newProfileButton = new CustomButton("Nouveau profil", "➕");
        newProfileButton.setOnAction(e -> showProfileCreation());

        header.getChildren().addAll(title, spacer, newProfileButton);
        return header;
    }

    private void loadProfiles() {
        profilesGrid.getChildren().clear();

        // Ajout du profil par défaut
        ProfileCard defaultProfile = new ProfileCard(
                "Fabric 1.21.4",
                "Profil par défaut",
                "fabric",
                "1.21.4",
                true
        );
        profilesGrid.getChildren().add(defaultProfile);

        // TODO: Charger les autres profils depuis la configuration
    }

    private VBox createProfileCreationForm() {
        VBox form = new VBox(20);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");
        VBox.setVgrow(form, Priority.ALWAYS);

        // En-tête avec retour
        HBox formHeader = new HBox(15);
        formHeader.setAlignment(Pos.CENTER_LEFT);

        CustomButton backButton = new CustomButton("Retour", "←");
        backButton.setOnAction(e -> hideProfileCreation());

        Label title = new Label("Créer un nouveau profil");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        formHeader.getChildren().addAll(backButton, title);

        // Conteneur scrollable pour le formulaire
        VBox formContent = new VBox(20);
        ScrollPane scrollPane = new ScrollPane(formContent);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Sections du formulaire
        formContent.getChildren().addAll(
                createBasicInfoSection(),
                createVersionSection(),
                createAdvancedSection()
        );

        // Boutons d'action
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        CustomButton createButton = new CustomButton("Créer le profil");
        createButton.setOnAction(e -> createProfile());

        actions.getChildren().add(createButton);

        form.getChildren().addAll(formHeader, scrollPane, actions);
        return form;
    }

    private VBox createBasicInfoSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 10;");

        Label title = new Label("Informations de base");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        // Nom
        Label nameLabel = new Label("Nom du profil");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Bahnschrift';");
        TextField nameField = createStyledTextField("Mon super profil");

        // Description
        Label descLabel = new Label("Description (optionnelle)");
        descLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Bahnschrift';");
        TextArea descArea = createStyledTextArea("Une description de votre profil...");

        section.getChildren().addAll(title, nameLabel, nameField, descLabel, descArea);
        return section;
    }

    private VBox createVersionSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 10;");

        Label title = new Label("Version et ModLoader");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        // ModLoader
        Label modloaderLabel = new Label("ModLoader");
        modloaderLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Bahnschrift';");
        ComboBox<String> modloaderBox = createStyledComboBox();
        modloaderBox.getItems().addAll("Fabric", "Forge", "Vanilla", "NeoForge");
        modloaderBox.setValue("Fabric");

        // Version
        Label versionLabel = new Label("Version");
        versionLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Bahnschrift';");
        ComboBox<String> versionBox = createStyledComboBox();
        versionBox.getItems().addAll("1.21.4", "1.20.4", "1.20.2", "1.20.1");
        versionBox.setValue("1.21.4");

        section.getChildren().addAll(title, modloaderLabel, modloaderBox, versionLabel, versionBox);
        return section;
    }

    private VBox createAdvancedSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 10;");

        Label title = new Label("Paramètres avancés");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        // RAM
        Label ramLabel = new Label("Allocation de RAM (GB)");
        ramLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Bahnschrift';");
        Spinner<Integer> ramSpinner = createStyledSpinner(1, 32, 4);

        section.getChildren().addAll(title, ramLabel, ramSpinner);
        return section;
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-prompt-text-fill: gray;
        """);
        return field;
    }

    private TextArea createStyledTextArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setPrefRowCount(3);
        area.setWrapText(true);
        area.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-prompt-text-fill: gray;
            -fx-control-inner-background: transparent;
        """);
        return area;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-mark-color: white;
        """);
        return comboBox;
    }

    private Spinner<Integer> createStyledSpinner(int min, int max, int initial) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initial);
        spinner.setEditable(true);
        spinner.setPrefWidth(200);
        spinner.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: white;
            -fx-background-radius: 5;
        """);
        return spinner;
    }

    private void showProfileCreation() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), this.contentBox);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            this.contentBox.setVisible(false);
            this.creationForm.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), this.creationForm);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void hideProfileCreation() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), this.creationForm);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            this.creationForm.setVisible(false);
            this.contentBox.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), this.contentBox);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void createProfile() {
        // TODO: Implémentation de la création du profil
        hideProfileCreation();
    }
}