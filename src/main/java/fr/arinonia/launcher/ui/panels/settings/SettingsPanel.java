package fr.arinonia.launcher.ui.panels.settings;

import fr.arinonia.launcher.ui.AbstractPanel;
import fr.arinonia.launcher.ui.components.CustomButton;
import fr.arinonia.launcher.ui.panels.home.HomePanel;
import fr.arinonia.launcher.ui.panels.settings.sections.AccountSettingsSection;
import fr.arinonia.launcher.ui.panels.settings.sections.LauncherSettingsSection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;


public class SettingsPanel extends AbstractPanel {
    private VBox settingsContainer;
    private String currentCategory = "Compte";

    @Override
    public void onShow() {
        super.onShow();
        initializePanel();
    }

    @Override
    public void onHide() {
        super.onHide();
        this.layout.getChildren().clear();
    }

    private void initializePanel() {
        this.layout.getChildren().clear();
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(36, 17, 70), rgb(48, 25, 88));");

        final VBox mainContainer = new VBox(10);
        this.setPriority(mainContainer);
        mainContainer.setPadding(new Insets(20));

        setupTopBar(mainContainer);
        setupContent(mainContainer);

        this.layout.getChildren().add(mainContainer);
        showSettingsSection(this.currentCategory);
    }

    @Override
    protected void setupPanel() {
        initializePanel();
    }

    private void setupTopBar(final VBox mainContainer) {
        final HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10;");

        final Label titleLabel = new Label("Paramètres");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final CustomButton backButton = new CustomButton("Retour");
        backButton.setOnAction(e -> this.getPanelManager().showPanel(HomePanel.class));

        topBar.getChildren().addAll(titleLabel, spacer, backButton);
        mainContainer.getChildren().add(topBar);
    }

    private void setupContent(final VBox mainContainer) {
        final HBox contentContainer = new HBox(20);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        final VBox categoriesSection = createCategoriesSection();

        final VBox settingsSection = createSettingsSection();
        HBox.setHgrow(settingsSection, Priority.ALWAYS);

        contentContainer.getChildren().addAll(categoriesSection, settingsSection);
        mainContainer.getChildren().add(contentContainer);
    }

    private VBox createCategoriesSection() {
        final VBox categoriesSection = new VBox(10);
        categoriesSection.setPadding(new Insets(20));
        categoriesSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");
        categoriesSection.setPrefWidth(250);

        final String[] categories = {
                "Compte", "Launcher", "Java", "Performances"
        };

        for (final String category : categories) {
            final CustomButton categoryButton = new CustomButton(category);
            categoryButton.setAsMenuButton();
            categoryButton.setDisable(category.equals(this.currentCategory));
            categoryButton.setOnAction(e -> {
                this.currentCategory = category;
                showSettingsSection(category);
                updateCategoryButtons(categoriesSection);
            });
            categoriesSection.getChildren().add(categoryButton);
        }

        return categoriesSection;
    }

    private void updateCategoryButtons(final VBox categoriesSection) {
        categoriesSection.getChildren().forEach(node -> {
            if (node instanceof final CustomButton button) {
                button.setDisable(button.getText().equals(this.currentCategory));
            }
        });
    }

    private VBox createSettingsSection() {
        final VBox settingsSection = new VBox(15);
        settingsSection.setPadding(new Insets(20));
        settingsSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");

        this.settingsContainer = new VBox(10);
        this.settingsContainer.setPadding(new Insets(10));

        final ScrollPane scrollPane = new ScrollPane(this.settingsContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());

        showWelcomeMessage();

        settingsSection.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return settingsSection;
    }

    private void showSettingsSection(final String category) {
        this.settingsContainer.getChildren().clear();

        switch (category) {
            case "Compte" -> this.settingsContainer.getChildren().add(new AccountSettingsSection(this.panelManager.getLauncher()));
            case "Launcher" -> this.settingsContainer.getChildren().add(new LauncherSettingsSection());
            case "Java", "Performances" -> showComingSoon();
            default -> showWelcomeMessage();
        }
    }

    private void showWelcomeMessage() {
        final Label placeholderLabel = new Label("Sélectionnez une catégorie pour afficher les paramètres");
        placeholderLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
        this.settingsContainer.getChildren().add(placeholderLabel);
    }

    private void showComingSoon() {
        final VBox comingSoonContainer = new VBox(15);
        comingSoonContainer.setAlignment(Pos.CENTER);
        comingSoonContainer.setPadding(new Insets(40));
        comingSoonContainer.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");

        final Label emojiLabel = new Label("🚧");
        emojiLabel.setStyle("-fx-font-size: 48px;");

        final Label titleLabel = new Label("En développement");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label descriptionLabel = new Label("Cette section est actuellement en cours de développement et sera disponible dans une prochaine mise à jour.");
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        comingSoonContainer.getChildren().addAll(emojiLabel, titleLabel, descriptionLabel);
        this.settingsContainer.getChildren().add(comingSoonContainer);
    }
}