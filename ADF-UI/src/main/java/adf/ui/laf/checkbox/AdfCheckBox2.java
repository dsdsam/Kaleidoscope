package adf.ui.laf.checkbox;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.*;

/**
 * Created by Admin on 10/9/2016.
 */
public class AdfCheckBox2 extends JCheckBox {

    public AdfCheckBox2() {
    }

    public AdfCheckBox2(final Action a) {
        super(a);
    }

    public AdfCheckBox2(final Icon icon, final boolean selected) {
        super(icon, selected);
    }

    public AdfCheckBox2(final Icon icon) {
        super(icon);
    }

    public AdfCheckBox2(final String text, final boolean selected) {
        super(text, selected);
    }

    public AdfCheckBox2(final String text, final Icon icon, final boolean selected) {
        super(text, icon, selected);
    }

    public AdfCheckBox2(final String text, final Icon icon) {
        super(text, icon);
    }

    public AdfCheckBox2(final String text) {
        super(text);
    }

    @Override
    public void updateUI() {
        this.setUI(OrionCheckBoxUI.createUI(this));
    }

    static class OrionCheckBoxUI extends BasicCheckBoxUI {
        private final Icon checkIcon;
        private final Rectangle viewRect;
        private final Rectangle iconRect;
        private final Rectangle textRect;
        private final Stroke focusStroke;

        private static OrionCheckBoxUI theUI;// = new OrionCheckBoxUI();

//        private OrionCheckBoxUI() {
//            this(VS4Icons.getInstance().getCheckBoxIcon());
//        }

        protected OrionCheckBoxUI(final Icon checkIcon) {
            this.checkIcon = checkIcon;
            this.viewRect = new Rectangle();
            this.iconRect = new Rectangle();
            this.textRect = new Rectangle();
            this.focusStroke = new BasicStroke(1.5f);
        }

//        @Override
//        public void installUI(final JComponent c) {
//            super.installUI(c);
//
//            c.setFont(OrionConstants.FONT_PLAIN);
//            c.setOpaque(false);
//        }

        @Override
        public void paint(final Graphics g, final JComponent c) {
            final Graphics2D g2 = (Graphics2D) g.create();

            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                this.paintContents(g2, c);
            } finally {
                g2.dispose();
            }
        }

        private void paintContents(final Graphics2D g, final JComponent c) {
            final AbstractButton b = (AbstractButton) c;
            final ButtonModel model = b.getModel();

            final Dimension size = c.getSize();

            this.viewRect.setBounds(0, 0, size.width, size.height);
            this.iconRect.setLocation(0, 0);

            final Font f = c.getFont();
            g.setFont(f);
            final FontMetrics fm = g.getFontMetrics();

            final Icon altIcon = b.getIcon();
            final Icon icon = altIcon != null ? altIcon : this.checkIcon;

            final String text = SwingUtilities.layoutCompoundLabel(c,
                    fm,
                    b.getText(),
                    icon,
                    b.getVerticalAlignment(),
                    b.getHorizontalAlignment(),
                    b.getVerticalTextPosition(),
                    b.getHorizontalTextPosition(),
                    this.viewRect,
                    this.iconRect,
                    this.textRect,
                    4);

            if (c.isOpaque()) {
                g.setColor(b.getBackground());
                g.fillRect(0, 0, size.width, size.height);
            }

            icon.paintIcon(c, g, this.iconRect.x, this.iconRect.y);

            if (text != null) {
                g.setColor(model.isEnabled()
                        ? b.getForeground()
                        : b.getBackground().darker());

//                DrawingUtilities.getInstance().drawText(g,
//                        model.getMnemonic(),
//                        text,
//                        this.textRect.x,
//                        this.textRect.y + fm.getAscent());
            }

            if (c.hasFocus()) {
                this.paintFocus(g, this.iconRect, size);
            }
        }

        @Override
        protected void paintFocus(final Graphics g,
                                  final Rectangle rect,
                                  final Dimension d) {
//            g.setColor(OrionConstants.FOCUS_KEYLINE);

            ((Graphics2D) g).setStroke(this.focusStroke);
            ((Graphics2D) g).draw(rect);
        }

        @Override
        public Dimension getPreferredSize(final JComponent c) {
            if (c.getComponentCount() > 0) {
                return null;
            }

            final AbstractButton b = (AbstractButton) c;

            final String text = b.getText();

            final Icon buttonIcon = null == b.getIcon()
                    ? this.getDefaultIcon()
                    : b.getIcon();

            final Font font = b.getFont();
            final FontMetrics fm = b.getFontMetrics(font);

            this.viewRect.setBounds(0, 0, Short.MAX_VALUE, Short.MAX_VALUE);
            this.iconRect.setBounds(0, 0, 0, 0);
            this.textRect.setBounds(0, 0, 0, 0);

            SwingUtilities.layoutCompoundLabel(c,
                    fm,
                    text,
                    buttonIcon,
                    b.getVerticalAlignment(),
                    b.getHorizontalAlignment(),
                    b.getVerticalTextPosition(),
                    b.getHorizontalTextPosition(),
                    this.viewRect,
                    this.iconRect,
                    this.textRect,
                    text == null ? 0 : 4); // Hardcoded to 4 to work on Java5

            final int x1 = Math.min(this.iconRect.x, this.textRect.x);
            final int x2 = Math.max(this.iconRect.x + this.iconRect.width,
                    this.textRect.x + this.textRect.width);
            final int y1 = Math.min(this.iconRect.y, this.textRect.y);
            final int y2 = Math.max(this.iconRect.y + this.iconRect.height,
                    this.textRect.y + this.textRect.height);

            int width = x2 - x1;
            int height = y2 - y1;

            final Insets prefInsets = b.getInsets(new Insets(0, 0, 0, 0));

            width += prefInsets.left + prefInsets.right;
            height += prefInsets.top + prefInsets.bottom;

            return new Dimension(width, height);
        }

        public static ComponentUI createUI(final JComponent jComponent) {
            return theUI;
        }
    }
}

