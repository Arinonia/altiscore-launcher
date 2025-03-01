package fr.arinonia.launcher.ui.panels.home;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.ui.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class ShopPanel extends VBox {
    private final Launcher launcher;
    private static class ShopItem {
        String name;
        String description;
        String price;
        String imageUrl;
        String category;
        FontAwesomeIcon icon;

        public ShopItem(final String name, final String description, final String price, final String imageUrl,
                        final String category, final FontAwesomeIcon icon) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imageUrl = imageUrl;
            this.category = category;
            this.icon = icon;
        }
    }

    private final List<ShopItem> sampleItems = Arrays.asList(
            new ShopItem("VIP", "Accès aux serveurs VIP et commandes exclusives", "5.99€", null, "ranks", FontAwesomeIcon.STAR),
            new ShopItem("VIP+", "Tous les avantages VIP et plus encore", "9.99€", null, "ranks", FontAwesomeIcon.STAR),
            new ShopItem("MVP", "Le statut ultime avec tous les privilèges", "19.99€", null, "ranks", FontAwesomeIcon.TROPHY),
            new ShopItem("Cape Dragon", "Une cape légendaire aux couleurs du dragon", "3.99€", null, "cosmetics", FontAwesomeIcon.FLAG),
            new ShopItem("Pet Creeper", "Un compagnon creeper qui vous suit partout", "2.99€", null, "cosmetics", FontAwesomeIcon.PAW),
            new ShopItem("Pack Survie", "Tout ce dont vous avez besoin pour bien démarrer", "7.99€", null, "packs", FontAwesomeIcon.CUBE),
            new ShopItem("Pack Aventurier", "Équipement complet pour vos aventures", "12.99€", null, "packs", FontAwesomeIcon.COMPASS)
    );

    public ShopPanel(final Launcher launcher) {
        this.launcher = launcher;
        this.setSpacing(20);
        initialize();
    }

    private void initialize() {
        final VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");
        VBox.setVgrow(container, Priority.ALWAYS);

        final HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(15);

        final Label titleLabel = new Label("Boutique");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final HBox balanceBox = new HBox(10);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.setStyle("-fx-background-color: rgba(149, 128, 255, 0.2); -fx-background-radius: 10; -fx-padding: 8 15;");

        final Label balanceLabel = new Label("Votre solde: ");
        balanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        final Label balanceValue = new Label("0.00€");
        balanceValue.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final CustomButton addFundsButton = new CustomButton("Ajouter des fonds", FontAwesomeIcon.PLUS_CIRCLE);

        balanceBox.getChildren().addAll(balanceLabel, balanceValue);
        header.getChildren().addAll(titleLabel, spacer, balanceBox, addFundsButton);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add(getClass().getResource("/css/scrollbar.css").toExternalForm());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        final VBox shopContent = new VBox(20);
        shopContent.setPadding(new Insets(10, 5, 10, 5));

        shopContent.getChildren().add(createCategorySection("Rangs & Grades", "ranks"));
        shopContent.getChildren().add(createCategorySection("Cosmétiques", "cosmetics"));
        shopContent.getChildren().add(createCategorySection("Packs & Bundles", "packs"));
        shopContent.getChildren().add(createCategorySection("Dev", "dev"));
        shopContent.getChildren().add(createHistorySection());

        scrollPane.setContent(shopContent);
        container.getChildren().addAll(header, scrollPane);

        this.getChildren().add(container);
    }

    private VBox createCategorySection(final String categoryTitle, final String categoryId) {
        final VBox section = new VBox(15);
        section.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
        section.setPadding(new Insets(15));

        final Label sectionTitle = new Label(categoryTitle);
        sectionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final FlowPane itemsGrid = new FlowPane(20, 20);
        itemsGrid.setAlignment(Pos.TOP_LEFT);

        final List<ShopItem> categoryItems = this.sampleItems.stream()
                .filter(item -> item.category.equals(categoryId))
                .toList();

        if (categoryItems.isEmpty()) {
            final Label emptyLabel = new Label("Aucun article disponible dans cette catégorie pour le moment");
            emptyLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
            itemsGrid.getChildren().add(emptyLabel);
        } else {
            for (final ShopItem item : categoryItems) {
                itemsGrid.getChildren().add(createShopItemCard(item));
            }
        }

        section.getChildren().addAll(sectionTitle, itemsGrid);
        return section;
    }

    private VBox createHistorySection() {
        final VBox section = new VBox(15);
        section.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 10;");
        section.setPadding(new Insets(15));

        final Label sectionTitle = new Label("Historique des achats");
        sectionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final VBox emptyHistoryBox = new VBox(10);
        emptyHistoryBox.setAlignment(Pos.CENTER);
        emptyHistoryBox.setPadding(new Insets(30, 0, 30, 0));

        final Label emptyHistoryLabel = new Label("Vous n'avez pas encore effectué d'achats");
        emptyHistoryLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        final CustomButton viewAllButton = new CustomButton("Voir tous les achats");
        //viewAllButton.setStyle("-fx-background-color: rgba(149, 128, 255, 0.2);");
        viewAllButton.setDisable(true);
        emptyHistoryBox.getChildren().addAll(emptyHistoryLabel, viewAllButton);
        section.getChildren().addAll(sectionTitle, emptyHistoryBox);
        return section;
    }

    private VBox createShopItemCard(ShopItem item) {
        final VBox card = new VBox(10);
        card.setPrefWidth(220);
        card.setPrefHeight(220);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 10;");
        card.setAlignment(Pos.TOP_LEFT);

        final FontAwesomeIconView iconView = new FontAwesomeIconView(item.icon);
        iconView.setGlyphSize(32);
        iconView.setFill(Color.web("rgb(149, 128, 255)"));

        final StackPane iconContainer = new StackPane(iconView);
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefHeight(50);
        iconContainer.setStyle("-fx-background-color: rgba(149, 128, 255, 0.1); -fx-background-radius: 5;");

        final Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final Label descriptionLabel = new Label(item.description);
        descriptionLabel.setStyle("-fx-text-fill: rgb(180, 180, 180); -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
        descriptionLabel.setWrapText(true);
        VBox.setVgrow(descriptionLabel, Priority.ALWAYS);

        final HBox purchaseBox = new HBox(10);
        purchaseBox.setAlignment(Pos.CENTER_RIGHT);

        final Label priceLabel = new Label(item.price);
        priceLabel.setStyle("-fx-text-fill: rgb(149, 128, 255); -fx-font-size: 16px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");

        final CustomButton buyButton = new CustomButton("Acheter");

        purchaseBox.getChildren().addAll(priceLabel, buyButton);

        card.getChildren().addAll(iconContainer, nameLabel, descriptionLabel, purchaseBox);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 10;"));

        return card;
    }
}