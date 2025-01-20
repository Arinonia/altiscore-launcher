package fr.arinonia.launcher.ui.components.home;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class SidebarItem extends HBox {
    private static final String DEFAULT_STYLE = """
            -fx-background-color: transparent;
            -fx-background-radius: 10;
            -fx-cursor: hand;
            """;

    private static final String SELECTED_STYLE = """
            -fx-background-color: rgba(149, 128, 255, 0.3);
            -fx-background-radius: 10;
            -fx-cursor: hand;
            """;

    private static final String HOVER_STYLE = """
            -fx-background-color: rgba(149, 128, 255, 0.2);
            -fx-background-radius: 10;
            -fx-cursor: hand;
            """;

    private final Label titleLabel;
    private final Label iconLabel;
    private boolean isSelected = false;

    public SidebarItem(final Sidebar.SidebarSection section) {
        this.setPadding(new Insets(12.0D));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(10.0D);
        this.setStyle(DEFAULT_STYLE);

        this.iconLabel = new Label(section.getIcon());
        this.iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        this.titleLabel = new Label(section.getLabel());
        this.titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");

        this.getChildren().addAll(this.iconLabel, this.titleLabel);

        setupHoverEffect();
    }

    private void setupHoverEffect() {
        this.setOnMouseEntered(e -> {
            if (!this.isSelected) {
                this.setStyle(HOVER_STYLE);
            }
        });

        this.setOnMouseExited(e -> {
            if (!this.isSelected) {
                this.setStyle(DEFAULT_STYLE);
            }
        });
    }

    public void setSelected(final boolean selected) {
        this.isSelected = selected;
        this.setStyle(selected ? SELECTED_STYLE : DEFAULT_STYLE);
        this.titleLabel.setStyle(selected ?
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift'; -fx-font-weight: bold;" :
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Bahnschrift';");
    }

    public void setExpanded(final boolean expanded) {
        if (expanded) {
            this.titleLabel.setVisible(true);
            this.titleLabel.setManaged(true);
            final FadeTransition fadeIn = new FadeTransition(Duration.millis(150), this.titleLabel);
            fadeIn.setFromValue(0.0D);
            fadeIn.setToValue(1.0D);
            fadeIn.play();
        } else {
            final FadeTransition fadeOut = new FadeTransition(Duration.millis(150), this.titleLabel);
            fadeOut.setFromValue(1.0D);
            fadeOut.setToValue(0.0D);
            fadeOut.setOnFinished(e -> {
                this.titleLabel.setVisible(false);
                this.titleLabel.setManaged(false);
            });
            fadeOut.play();
        }
    }
}