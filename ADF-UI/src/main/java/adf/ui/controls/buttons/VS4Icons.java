package adf.ui.controls.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class VS4Icons {
    public final static int       DEFAULT_CHECK_SIZE = 12;

    private final static VS4Icons INSTANCE           = new VS4Icons();

    private VS4Icons()
    {
        // no constructor accessible
    }

    public static VS4Icons getInstance()
    {
        return INSTANCE;
    }

    public Icon getRadioButtonIcon(int size, int offset)
    {
        return new VS4RadioButtonIcon(size, offset);
    }

    public Icon getRadioButtonIcon(int size)
    {
        return new VS4RadioButtonIcon(size);
    }

    public Icon getRadioButtonIcon()
    {
        return new VS4RadioButtonIcon();
    }

    public Icon getCheckBoxIcon(int size, int offset)
    {
        return new VS4CheckBoxIcon(size, offset);
    }

    public Icon getCheckBoxIcon(int size)
    {
        return new VS4CheckBoxIcon(size);
    }

    public Icon getCheckBoxIcon()
    {
        return new VS4CheckBoxIcon();
    }

    public Icon getArrowIcon(int orientation,
                             Color colour1,
                             Color colour2,
                             int width)
    {
        return this.getArrowIcon(orientation, colour1, colour2, null, null,width, 0);
    }

    public Icon getArrowIcon(int orientation,
                             Color colour1,
                             Color colour2,
                             int width,
                             int offset)
    {
        return new VS4ArrowIcon(orientation, colour1, colour2, null,null,width, offset);
    }

    public Icon getArrowIcon(int orientation,
                             Color colour1,
                             Color colour2,
                             Color armedColour1,
                             Color armedColour2,
                             int width,
                             int offset)
    {
        return new VS4ArrowIcon(orientation, colour1, colour2, armedColour1,armedColour2,width, offset);
    }

    private static class VS4ArrowIcon implements Icon
    {
        private final Polygon triangle;
        private final int     triangleWidth;
        private final int     triangleHeight;
        private final Color   colour1;
        private final Color   colour2;
        private final Color   armedColour1;
        private final Color   armedColour2;
        private final int     x_offset;
        private final float   hsb[] = new float[3];

        public VS4ArrowIcon(int orientation,
                            Color colour1,
                            Color colour2,
                            Color armedColour1,
                            Color armedColour2,
                            int width,
                            int x_offset)
        {
            int x[];
            int y[];

            switch (orientation)
            {
                case SwingConstants.NORTH:
                    x = new int[] { width, 0, width * 2 + 1, width + 2 };
                    y = new int[] { 0, width, width, 0 };
                    break;
                case SwingConstants.SOUTH:
                    x = new int[] { width, 0, width * 2, width };
                    y = new int[] { width, 0, 0, width };
                    break;
                case SwingConstants.EAST:
                    x = new int[] { width, 0, 0, width };
                    y = new int[] { width, 0, width * 2 + 1, width + 1 };
                    break;
                case SwingConstants.WEST:
                    x = new int[] { 0, width, width, 0 };
                    y = new int[] { width, 0, width * 2 + 1, width + 2 };
                    break;
                default:
                    throw new IllegalArgumentException("Invalid orientation: "
                            + orientation);
            }

            this.triangle = new Polygon(x, y, x.length);

            final Rectangle r = this.triangle.getBounds();

            this.triangleWidth = r.width;
            this.triangleHeight = r.height;
            this.colour1 = colour1;
            this.colour2 = colour2;
            this.armedColour1 = armedColour1;
            this.armedColour2 = armedColour2;
            this.x_offset = x_offset;
        }

        public int getIconHeight()
        {
            return this.triangleHeight;
        }

        public int getIconWidth()
        {
            return this.triangleWidth + this.x_offset;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            final Graphics2D g2 = (Graphics2D) g.create();

            Color c1 = this.colour1;
            Color c2 = this.colour2;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);

            if (c instanceof AbstractButton)
            {
                final AbstractButton b = (AbstractButton) c;
                final ButtonModel m = b.getModel();

                if (!m.isEnabled())
                {
                    c1=UIManager.getColor("Button.darkShadow");
                    c2=UIManager.getColor("Button.darkShadow");
                }

                if (m.isRollover() || m.isArmed())
                {
                    if ( this.armedColour1 != null )
                    {
                        c1 = this.armedColour1;
                        c2 = this.armedColour2;
                    }
                    else
                    {
                        Color.RGBtoHSB(c1.getRed(),
                                c1.getGreen(),
                                c1.getBlue(),
                                this.hsb);

                        if (this.hsb[2] > 0.5f)
                        {
                            c1 = c1.darker();
                            c2 = c2.darker();
                        }
                        else
                        {
                            c1 = c1.brighter();
                            c2 = c2.brighter();
                        }
                    }
                }
            }

            final GradientPaint paint = new GradientPaint(0,
                    0,
                    c1,
                    this.triangleWidth,
                    this.triangleHeight,
                    c2);

            x += this.x_offset;

            g2.translate(x, y);
            g2.setPaint(paint);
            g2.fill(this.triangle);
            g2.dispose();
        }
    }

    private static class VS4CheckBoxIcon implements Icon
    {
        private final int             checkOffset;
        protected final GradientPaint checkShading;
        protected final int           checkSize;
        private final BasicStroke     stroke = new BasicStroke(1.25f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER);

        public VS4CheckBoxIcon()
        {
            this(DEFAULT_CHECK_SIZE, 0);
        }

        public VS4CheckBoxIcon(int checkSize)
        {
            this(checkSize, 0);
        }

        public VS4CheckBoxIcon(int checkSize, int checkOffset)
        {
            this.checkSize = checkSize;
            this.checkOffset = checkOffset;
            this.checkShading = new GradientPaint(0,
                    0,
                    Color.lightGray,
                    this.checkSize,
                    this.checkSize,
                    Color.white);
        }

        public int getIconHeight()
        {
            return this.checkSize;
        }

        public int getIconWidth()
        {
            return this.checkSize + this.checkOffset;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            final Graphics2D g2 = (Graphics2D) g.create();
            final AbstractButton b = (AbstractButton) c;
            final ButtonModel bm = b.getModel();
            final Color checkCol = c.isEnabled()
                    ? c.getForeground()
                    : c.getBackground().darker();
            final boolean check = b.isSelected();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (bm.isArmed() || bm.isRollover())
            {
                g2.setColor(Color.white);
                g2.fillRect(x, y, this.checkSize, this.checkSize);
                g2.setColor(c.getBackground().darker());

                for (int i = 0; i < 6; i++)
                {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            1.0f / (2 + (i * i) / 2)));

                    g2.drawRect(x + i,
                            y + i,
                            this.checkSize - i * 2,
                            this.checkSize - i * 2);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        1.0f));
            }
            else
            {
                g2.setPaint(this.checkShading);

                g2.fillRect(x, y, this.checkSize, this.checkSize);
            }

            g2.setColor(c.getParent().getBackground().darker());
            g2.drawRect(x, y, this.checkSize, this.checkSize);
            g2.setColor(checkCol);
            g2.setStroke(this.stroke);
            if (check)
            {
                x += 3;

                for (int i = 0; i < 3; i++, x++)
                {
                    g2.drawLine(x, y + i + 5, x, y + i + 7);
                }

                for (int i = 0; i < 4; i++, x++)
                {
                    g2.drawLine(x, y + 8 - i, x, y + 6 - i);
                }
            }

            g2.dispose();
        }
    }

    private static class VS4RadioButtonIcon extends VS4CheckBoxIcon
    {
        private final Ellipse2D.Double border = new Ellipse2D.Double();
        private final Ellipse2D.Double dot    = new Ellipse2D.Double();

        public VS4RadioButtonIcon()
        {
            super();
        }

        public VS4RadioButtonIcon(int checkSize)
        {
            super(checkSize);
        }

        public VS4RadioButtonIcon(int checkSize, int checkOffset)
        {
            super(checkSize, checkOffset);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            final Color checkCol = c.isEnabled()
                    ? c.getForeground()
                    : c.getBackground().darker();

            final AbstractButton b = (AbstractButton) c;
            final ButtonModel bm = b.getModel();
            final boolean check = b.isSelected();
            final Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            this.border.setFrame(x, y, this.checkSize, this.checkSize);

            if (bm.isArmed() || bm.isRollover())
            {
                g2.setColor(Color.white);
                g2.fill(this.border);
                g2.setColor(c.getBackground().darker());

                for (int i = 0; i < 6; i++)
                {
                    this.border.setFrame(x + i,
                            y + i,
                            this.checkSize - (i * 2),
                            this.checkSize - (i * 2));

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            1.0f / (1 + i * i)));
                    g2.draw(this.border);
                }
                this.border.setFrame(x, y, this.checkSize, this.checkSize);
            }
            else
            {
                g2.setPaint(this.checkShading);

                g2.fill(this.border);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.0f));

            g2.setColor(c.getParent().getBackground().darker());
            g2.draw(this.border);
            g2.setColor(checkCol);

            if (check)
            {
                this.dot.setFrame(x + 4,
                        y + 4,
                        this.checkSize - 7,
                        this.checkSize - 7);

                g2.fill(this.dot);
            }

            g2.dispose();
        }
    }
}

