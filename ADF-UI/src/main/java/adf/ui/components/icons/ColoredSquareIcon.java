package adf.ui.components.icons;

import javax.swing.*;
import java.awt.*;

public class ColoredSquareIcon implements Icon {

    public static ColoredSquareIcon createInstance(int stateID, Color itemColor) {
        return new ColoredSquareIcon(stateID, itemColor);
    }

    private final int ID;
    private final Color color;

    private ColoredSquareIcon(int ID, Color color) {
        this.ID = ID;
        this.color = color;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
        g.setColor(oldColor);
    }

    public int getID() {
        return ID;
    }

    public Color getItemColor() {
        return color;
    }

    public int getIconWidth() {
        return 10;
    }

    public int getIconHeight() {
        return 10;
    }
}
