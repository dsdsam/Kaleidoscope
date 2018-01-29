package adf.ui.components.borders;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Date: May 6, 2008
 * Time: 2:45:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundedLineBorder implements Border {

    private final Color lineColor;

    public RoundedLineBorder(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(lineColor);
        g2.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
    }
}
