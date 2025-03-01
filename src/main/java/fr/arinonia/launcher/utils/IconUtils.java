package fr.arinonia.launcher.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

public class IconUtils {

    public static FontAwesomeIconView createIcon(final FontAwesomeIcon icon) {
        return createIcon(icon, 16, Color.WHITE);
    }

    public static FontAwesomeIconView createIcon(final FontAwesomeIcon icon, final int size) {
        return createIcon(icon, size, Color.WHITE);
    }

    public static FontAwesomeIconView createIcon(final FontAwesomeIcon icon, final int size, final Color color) {
        final FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setGlyphSize(size);
        iconView.setFill(color);
        return iconView;
    }

    public static FontAwesomeIconView createIcon(final FontAwesomeIcon icon, int size, final String colorHex) {
        return createIcon(icon, size, Color.web(colorHex));
    }
    public static void updateIcon(final FontAwesomeIconView iconView, final int size, final Color color) {
        iconView.setGlyphSize(size);
        iconView.setFill(color);
    }
}