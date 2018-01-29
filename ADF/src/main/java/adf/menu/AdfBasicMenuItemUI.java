package adf.menu;

import sun.swing.MenuItemLayoutHelper;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * Created by Admin on 3/19/2017.
 */
public class AdfBasicMenuItemUI extends BasicMenuItemUI {

    protected void paintMenuItem(Graphics g, JComponent c,
                                 Icon checkIcon, Icon arrowIcon,
                                 Color background, Color foreground,
                                 int defaultTextIconGap) {
        // Save original graphics font and color
        Font holdf = g.getFont();
        Color holdc = g.getColor();

        JMenuItem mi = (JMenuItem) c;
        g.setFont(mi.getFont());

        Rectangle viewRect = new Rectangle(0, 0, mi.getWidth(), mi.getHeight());
        applyInsets(viewRect, mi.getInsets());
        MenuItemLayoutHelper lh = new MenuItemLayoutHelper(mi, checkIcon,
                arrowIcon, viewRect, defaultTextIconGap, acceleratorDelimiter,
                mi.getComponentOrientation().isLeftToRight(),
                 mi.getFont(),
                acceleratorFont, MenuItemLayoutHelper.useCheckAndArrow(menuItem),
                getPropertyPrefix());
        MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem();

        paintBackground(g, mi, background);
        paintIcon(  g,  (AbstractButton) c, lr.getIconRect()) ;
        paintText(g, lh, lr);

        // Restore original graphics font and color
        g.setColor(holdc);
        g.setFont(holdf);
    }

    private void applyInsets(Rectangle rect, Insets insets) {
        if(insets != null) {
            rect.x += insets.left;
            rect.y += insets.top;
            rect.width -= (insets.right + rect.x);
            rect.height -= (insets.bottom + rect.y);
        }
    }

    private void paintText(Graphics g, MenuItemLayoutHelper lh,
                           MenuItemLayoutHelper.LayoutResult lr) {
        if (!lh.getText().equals("")) {
            if (lh.getHtmlView() != null) {
                // Text is HTML
                lh.getHtmlView().paint(g, lr.getTextRect());
            } else {
                // Text isn't HTML
                paintText(g, lh.getMenuItem(), lr.getTextRect(), lh.getText());
            }
        }
    }

    protected void paintIcon(Graphics g, AbstractButton b, Rectangle iconRect) {
        ButtonModel model = b.getModel();
        Icon icon = null;

        if(!model.isEnabled()) {
            if(model.isSelected()) {
                icon = b.getDisabledSelectedIcon();
            } else {
                icon = b.getDisabledIcon();
            }
        } else if(model.isPressed() && model.isArmed()) {
            icon = b.getPressedIcon();
            if(icon == null) {
                // Use selected icon
                icon = b.getSelectedIcon();
            }
        } else if(model.isSelected()) {
            if(b.isRolloverEnabled() && model.isRollover()) {
                icon = b.getRolloverSelectedIcon();
                if (icon == null) {
                    icon = b.getSelectedIcon();
                }
            } else {
                icon = b.getSelectedIcon();
            }
        } else if(b.isRolloverEnabled() && model.isRollover()) {
            icon = b.getRolloverIcon();
        }

        if(icon == null) {
            icon = b.getIcon();
        }

        icon.paintIcon(b, g, iconRect.x, iconRect.y);
    }

}
