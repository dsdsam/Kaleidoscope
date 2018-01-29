package adf.ui.laf.combobox;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

/**
 * Created by u0180093 on 4/26/2016.
 */
public class AdfComboBoxArrowButtonUI extends AdfButton3DUI implements SwingConstants {
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color SHADOW_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.2f);
    protected static Rectangle viewRect = new Rectangle();
    protected static Rectangle textRect = new Rectangle();
    protected static Rectangle iconRect = new Rectangle();
    final public static int[] arrowImageData = {
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0xff808080, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0xff808080, 0xff808080, 0xff808080, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0xff808080, 0xff808080,
            0xff808080, 0xff808080, 0xff808080, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0xff808080, 0xff808080, 0xff808080, 0xff808080, 0xff808080,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0xff808080, 0xff808080, 0xff808080, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0xff808080, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0x00ffffff, 0x00ffffff, 0x00ffffff, 16
    };
    private ImageIcon arrowImage = new ImageIcon(StyleSupport.getImage(arrowImageData));
    private boolean useComboBoxFont;

    public static ComponentUI createUI(JComponent c) {
        // create a new instance here - there might be different colors
        return new AdfComboBoxArrowButtonUI();
    }

    public void setUseComboBoxFont(boolean useComboBoxFont) {
        this.useComboBoxFont = useComboBoxFont;
    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        textRect.x = viewRect.x + c.getInsets().left; // Make this left-justified
        Font oldFont = g.getFont();
        if (useComboBoxFont) {
            if (c instanceof AdfComboBoxUI.AdfArrowComboBoxButton) {
                JComboBox comboBox = ((AdfComboBoxUI.AdfArrowComboBoxButton) c).getJComboBox();
                Font font = comboBox.getFont();
                g.setFont(font);
            }
        }
        super.paintText(g, c, textRect, text);
        g.setFont(oldFont);
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c); // This will also set the iconRect field

        //// Draw the line and shadow to the left of the icon
        // draw main separator line
        Color color = g.getColor();

        g.setColor(LINE_COLOR);

        int lineX = c.getWidth() - 20;
        int shadowX = c.getWidth() - 19;

        if (lineX <= 0) {
            return;
        }

        g.drawLine(lineX, 1, lineX, c.getHeight() - 2);

        // draw shadow
        g.setColor(SHADOW_COLOR);
        g.drawLine(shadowX, 1, shadowX, c.getHeight() - 2);

        // reset color
        g.setColor(color);
    }

    /**
     * Set the image's color
     */
    protected void updateColor(AbstractButton b) {
        if (b.getBackground() != cachedBg) {
            super.updateColor(b);

            // adjust the arrows icon so that it's slightly tinted with the background color
            ImageFilter filter = new ColorFilter(sourceBg, StyleSupport.brighter(b.getBackground(), 0.9f));
            ImageProducer imageProducer = new FilteredImageSource(arrowImage.getImage().getSource(), filter);
            Image image = Toolkit.getDefaultToolkit().createImage(imageProducer);

            this.arrowImage = new ImageIcon(image);
            b.setIcon(arrowImage);
        }
    }
}

