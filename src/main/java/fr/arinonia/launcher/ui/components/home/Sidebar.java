package fr.arinonia.launcher.ui.components.home;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Sidebar extends VBox {
    private final SidebarCallback callback;
    private SidebarItem selectedItem;
    private boolean isExpanded = true;
    private static final double EXPANDED_WIDTH = 250;
    private static final double COLLAPSED_WIDTH = 80;
    private Timeline animation;

    public interface SidebarCallback {
        void onSectionSelected(SidebarSection section);
    }

    public enum SidebarSection {
        SERVERS("Home", "🌐"),
        PARTNERS("Serveurs partenaires", "🤝"),
        MY_SERVERS("Mods", "⭐"),
        PROFILES("Profils", "👤");

        private final String label;
        private final String icon;

        SidebarSection(final String label, final String icon) {
            this.label = label;
            this.icon = icon;
        }

        public String getLabel() {
            return this.label;
        }

        public String getIcon() {
            return this.icon;
        }
    }

    public Sidebar(final SidebarCallback callback) {
        this.callback = callback;
        this.setPrefWidth(EXPANDED_WIDTH);
        this.setMinWidth(EXPANDED_WIDTH);
        this.setMaxWidth(EXPANDED_WIDTH);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;");
        this.setPadding(new Insets(20));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        setupHeader();
        setupItems();
        setupToggleButton();
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private void setupHeader() {
        final Label titleLabel = new Label("Navigation");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;");
        titleLabel.visibleProperty().bind(this.prefWidthProperty().greaterThan(COLLAPSED_WIDTH + 50));
        this.getChildren().add(titleLabel);
    }

    private void setupToggleButton() {
        final SidebarToggleButton toggleButton = new SidebarToggleButton(this::toggleExpanded);
        this.getChildren().add(0, toggleButton);
    }

    private void setupItems() {
        for (final SidebarSection section : SidebarSection.values()) {
            final SidebarItem item = new SidebarItem(section);

            if (this.selectedItem == null) {
                this.selectedItem = item;
                item.setSelected(true);
            }

            item.setOnMouseClicked(e -> {
                if (this.selectedItem != null) {
                    this.selectedItem.setSelected(false);
                }
                this.selectedItem = item;
                item.setSelected(true);

                if (this.callback != null) {
                    this.callback.onSectionSelected(section);
                }
            });

            this.getChildren().add(item);
        }
    }

    public void toggleExpanded() {
        if (this.animation != null) {
            this.animation.stop();
        }

        final double targetWidth = isExpanded ? COLLAPSED_WIDTH : EXPANDED_WIDTH;
        this.animation = new Timeline(
                new KeyFrame(Duration.millis(250.0D),
                        new KeyValue(this.prefWidthProperty(), targetWidth),
                        new KeyValue(this.minWidthProperty(), targetWidth),
                        new KeyValue(this.maxWidthProperty(), targetWidth)
                )
        );

        this.animation.setOnFinished(e -> {
            this.isExpanded = !this.isExpanded;
            getChildren().forEach(node -> {
                if (node instanceof SidebarItem) {
                    ((SidebarItem) node).setExpanded(this.isExpanded);
                }
            });
        });

        this.animation.play();
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }
}