package adf.ui.controls.buttons;

import adf.ui.controls.StyleSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.text.View;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class OrionToggleButtonUI extends MetalToggleButtonUI
{
    private int borderInset = 3;

    // QF-364 "For all buttons/drop-down-boxes where the text is too large to fit,
    // the text will be truncated and suffixed by a '...' - with one exception.
    // On the Provider buttons, the text should shrink to fit the button."
    //
    // We mimic this stuff from "com.fxall.gui.laf.OrionButtonUI" to provide
    // the same text-shrinking functionality.
    //
    // The button whose painting is being delegated to this UI
    protected AbstractButton abstractbutton = null;
    /**
     * This UI will try to use a JButton.fitTextToWidth() before using
     * this UIs getFitTextToWidth() so that a buton instance can override
     * this UIs behavior.  If JButton.fitTextToWidth() is not defined
     * (which is highly likely, then this UI defers to getFitTextToWidth)
     */
    protected Method fitTextToWidthMethod = null;
    protected boolean fitTextToWidth = OrionButtonUI.bDEFAULT_FIT_TO_WIDTH;
    //
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        try {
            abstractbutton = b;
            boolean methodIsThere = false;
            for(Method m : b.getClass().getMethods()){
                if(m.getName().equals(OrionButtonUI.sFIT_TEXT_TO_WIDTH_METHOD)){
                    methodIsThere = true;
                    break;
                }
            }
            if(methodIsThere){
                fitTextToWidthMethod = (b == null) ? null : b.getClass().getMethod(OrionButtonUI.sFIT_TEXT_TO_WIDTH_METHOD, null);
            }
        }
        catch (NoSuchMethodException e) {
            Logger.getLogger(b.getClass().getName()).severe(e.getMessage());
            fitTextToWidthMethod = null;
        }
    }
    /**
     * Returns true if text should be made to
     * fit on the button by shrinking the font.
     * <p>If the Button delegating painting to this
     * UI defines getFitTextToWidth():Boolean then
     * this method defers to that method
     * @return
     */
    public boolean getFitTextToWidth() {
        try {
            return (fitTextToWidthMethod == null) ? fitTextToWidth :
                    ((Boolean)fitTextToWidthMethod.invoke(abstractbutton, null)).booleanValue();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return fitTextToWidth;
    }
    //
    public void setFitTextToWidth(boolean fit) {
        fitTextToWidth = fit;
    }


    public OrionToggleButtonUI() {
    }

    // ********************************
    //        Create PLAF
    // ********************************
    public static ComponentUI createUI(JComponent b) {
        return new OrionToggleButtonUI();
    }

    public void update(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;

// It is not used now, because the default imalge already shows the "Up" state
//   while the separate painting is used for the "Pressed" state.
//        if (b.isOpaque()) {
//            paintUp(g, c);
//        }

        paint(g, c);
    }

    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Dimension size = b.getSize();
        FontMetrics fm = g.getFontMetrics();

        Insets i = c.getInsets();

        Rectangle viewRect = new Rectangle(size);

        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);

        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        Font f = c.getFont();

        g.setFont(f);

        // layout the text and icon
        String text = OrionButtonUI.layoutText(g, b, viewRect, textRect, iconRect, f, getFitTextToWidth());

        g.setColor(b.getBackground());

        if ((model.isArmed() && model.isPressed()) || model.isSelected()) {
            paintButtonPressed(g, b);
        }

        // Paint the Icon
        if (b.getIcon() != null) {
            paintIcon(g, b, iconRect);
        }

        // Draw the Text
        if ((text != null) && !text.equals("")) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);

            if (v != null) {
                v.paint(g, textRect);
            }
            else {
                paintText(g, b, textRect, text);
            }
        }

        // draw the dashed focus line.
        if (b.isFocusPainted() && b.hasFocus()) {
            paintFocus(g, b, viewRect, textRect, iconRect);
        }
    }

    /**
     * Paints the button sticking up (unselected)
     */
    private void paintUp(Graphics g, JComponent c) {
        JToggleButton b = (JToggleButton) c;
        Color c1 = g.getColor();
        int width = b.getSize().width;
        int height = b.getSize().height;

        // first draw the overall rect
        g.setColor(StyleSupport.brighter(b.getBackground(), 0.8f));
        g.fillRect(0, 0, width, height);

        // then the bottom-right triangle
        int[] xs = new int[3];
        int[] ys = new int[3];
        Color color = StyleSupport.darker(b.getBackground(), 0.5f);

        for (int i = 0; i < borderInset; i++) {
            Color tc = StyleSupport.brighter(color, 0.95f);

            color = tc;
            g.setColor(tc);
            xs[0] = width - i;
            ys[0] = i;
            xs[1] = width - i;
            ys[1] = height - i;
            xs[2] = i;
            ys[2] = height - i;
            g.fillPolygon(xs, ys, 3);
        }

        // then the diagonal shadow line
        g.setColor(StyleSupport.darker(color, 0.9f));
        g.drawLine(0, height - 1, width - 1, 0);

        // then the inner rectangle
        g.setColor(b.getBackground());
        g.fillRect(borderInset, borderInset, width - borderInset - 3, height - borderInset - 3);

        g.setColor(c1); // reset the color
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b.isContentAreaFilled()) {
            Color c1 = g.getColor();
            int width = b.getSize().width;
            int height = b.getSize().height;

            // first draw the overall rect
            g.setColor(StyleSupport.darker(b.getBackground(), 0.4f));
            g.fillRect(0, 0, width, height);

            // then the bottom-right triangle
            int[] xs = new int[3];
            int[] ys = new int[3];
            Color color = StyleSupport.darker(b.getBackground(), 0.9f);

            for (int i = 0; i < borderInset; i++) {
                Color tc = StyleSupport.darker(color, 0.9f);

                color = tc;
                g.setColor(tc);
                xs[0] = width - i;
                ys[0] = i;
                xs[1] = width - i;
                ys[1] = height - i;
                xs[2] = i;
                ys[2] = height - i;
                g.fillPolygon(xs, ys, 3);
            }

            // then the diagonal shadow line
            g.setColor(StyleSupport.darker(b.getBackground(), 0.1f));
            g.drawLine(0, height - 1, width - 1, 0);

            // then the inner rectangle
            g.setColor(b.getBackground());
            g.fillRect(borderInset, borderInset, width - borderInset - 3, height - borderInset - 3);

            g.setColor(c1); // reset the color
        }
    }
}
