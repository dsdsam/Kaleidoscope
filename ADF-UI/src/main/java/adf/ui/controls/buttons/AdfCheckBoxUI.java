package adf.ui.controls.buttons;

import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.View;
import java.awt.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfCheckBoxUI extends MetalCheckBoxUI {
    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();

    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;

        ButtonModel model = b.getModel();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();

        Insets i = c.getInsets();
        size = b.getSize(size);
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = size.width - (i.right + viewRect.x);
        viewRect.height = size.height - (i.bottom + viewRect.y);
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;

        Icon altIcon = b.getIcon();
        Icon selectedIcon = null;
        Icon disabledIcon = null;

        String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewRect, iconRect, textRect,
                b.getText() == null ? 0 : b.getIconTextGap());

        // fill background
        if (c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }

        // Draw the Text
        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
                if (b.hasFocus() && b.isFocusPainted() &&
                        textRect.width > 0 && textRect.height > 0) {
                    //paintFocus(g, textRect, size);
                }
            }
        }
        // Paint the radio button
        if (altIcon != null) {

            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    altIcon = b.getDisabledSelectedIcon();
                } else {
                    altIcon = b.getDisabledIcon();
                }
            } else if (model.isPressed() && model.isArmed()) {
                altIcon = b.getPressedIcon();
                if (altIcon == null) {
                    // Use selected icon
                    altIcon = b.getSelectedIcon();
                }
            } else if (model.isSelected()) {
                if (b.isRolloverEnabled() && model.isRollover()) {
                    altIcon = (Icon) b.getRolloverSelectedIcon();
                    if (altIcon == null) {
                        altIcon = (Icon) b.getSelectedIcon();
                    }
                } else {
                    altIcon = (Icon) b.getSelectedIcon();
                }
            } else if (b.isRolloverEnabled() && model.isRollover()) {
                altIcon = (Icon) b.getRolloverIcon();
            }

            if (altIcon == null) {
                altIcon = b.getIcon();
            }
            altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

        } else {
            g.setColor(Color.white);
            //paint checkbox icon
            paintIcon(c, g, iconRect.x, iconRect.y);
        }

    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        FontMetrics fm = g.getFontMetrics();
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        /* Draw the Text */
        if (model.isEnabled()) {
            /*** paint the text normally */
            g.setColor(b.getForeground());
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + fm.getAscent() + getTextShiftOffset());
        } else {
            /*** paint the text disabled ***/  // changed to pick up foreground color
            g.setColor(b.getForeground().darker());
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                    textRect.x, textRect.y + fm.getAscent());
            //  g.setColor(b.getForeground().darker());
            //  BasicGraphicsUtils.drawStringUnderlineCharAt(g,text, mnemonicIndex,
            //              textRect.x - 1, textRect.y + fm.getAscent() - 1);
        }
    }

    protected int getControlSize() {
        return 13;
    }

    // method to paint checkbox icon white
    public void paintIcon(Component c, Graphics g, int x, int y) {
        JCheckBox cb = (JCheckBox) c;
        ButtonModel model = cb.getModel();
        int controlSize = getControlSize();

        boolean drawCheck = model.isSelected();

        if (model.isEnabled()) {
            //paint white icon
            g.setColor(Color.white);
            g.fillRect(x, y, controlSize - 1, controlSize - 1);

            if (model.isPressed() && model.isArmed()) {
                g.setColor(MetalLookAndFeel.getControlShadow());
                g.fillRect(x, y, controlSize - 1, controlSize - 1);
                drawPressed3DBorder(g, x, y, controlSize, controlSize);
            } else {
                drawFlush3DBorder(g, x, y, controlSize, controlSize);
            }
            g.setColor(MetalLookAndFeel.getControlInfo());
        } else {
            g.setColor(MetalLookAndFeel.getControlShadow());
            g.drawRect(x, y, controlSize - 2, controlSize - 2);
        }

        if (model.isSelected()) {
            drawCheck(c, g, x, y);
        }
    }

    protected void drawCheck(Component c, Graphics g, int x, int y) {
        int controlSize = getControlSize();
        g.fillRect(x + 3, y + 5, 2, controlSize - 8);
        g.drawLine(x + (controlSize - 4), y + 3, x + 5, y + (controlSize - 6));
        g.drawLine(x + (controlSize - 4), y + 4, x + 5, y + (controlSize - 5));
    }

    public int getIconWidth() {
        return getControlSize();
    }

    public int getIconHeight() {
        return getControlSize();
    }

    //method from metalutils
    void drawPressed3DBorder(Graphics g, int x, int y, int w, int h) {
        g.translate(x, y);

        drawFlush3DBorder(g, 0, 0, w, h);

        g.setColor(Color.white);//MetalLookAndFeel.getControlShadow() );
        g.drawLine(1, 1, 1, h - 2);
        g.drawLine(1, 1, w - 2, 1);
        g.translate(-x, -y);
    }

    //method from metalutils
    void drawFlush3DBorder(Graphics g, int x, int y, int w, int h) {
        g.translate(x, y);
        g.setColor(MetalLookAndFeel.getControlDarkShadow());
        g.drawRect(0, 0, w - 2, h - 2);
        g.setColor(MetalLookAndFeel.getControlHighlight());
        g.drawRect(1, 1, w - 2, h - 2);
        g.setColor(MetalLookAndFeel.getControl());
        g.drawLine(0, h - 1, 1, h - 2);
        g.drawLine(w - 1, 0, w - 2, 1);
        g.translate(-x, -y);
    }
}


