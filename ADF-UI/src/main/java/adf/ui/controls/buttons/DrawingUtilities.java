package adf.ui.controls.buttons;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class DrawingUtilities {
    private final static DrawingUtilities INSTANCE = new DrawingUtilities();

    private DrawingUtilities()
    {
        // no impl
    }

    public static DrawingUtilities getInstance()
    {
        return INSTANCE;
    }

    public void drawGlowText(final Graphics2D g2,
                             int blurRadius,
                             float blurSpeed,
                             int underlineChar,
                             String label,
                             int x,
                             int y)
    {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        for (int j = -blurRadius; j <= blurRadius; j++)
        {
            for (int i = -blurRadius; i <= blurRadius; i++)
            {
                double dist = Math.sqrt(i * i + j * j) * blurSpeed;

                if (dist > 0.0d)
                    dist = blurRadius * Math.max(dist, blurRadius + blurSpeed);

                final float alpha = (dist > 0.0d)
                        ? (float) (1.0f / dist)
                        : 1.0f;

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        alpha));

                BasicGraphicsUtils.drawString(g2,
                        label,
                        underlineChar,
                        x + i,
                        y + j);
            }
        }
    }

    public void drawText(Graphics g, int underlineChar, String txt, int x, int y)
    {
        this.drawText(g, underlineChar, txt, x, y, true);
    }

    public void drawText(Graphics g,
                         int underlineChar,
                         String txt,
                         int x,
                         int y,
                         boolean useAntiAlias)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                useAntiAlias
                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        BasicGraphicsUtils.drawString(g, txt, underlineChar, x, y);
    }

    public void drawUnderlineText(Graphics g,
                                  String txt,
                                  int x,
                                  int y,
                                  boolean useAntiAlias)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                useAntiAlias
                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.drawString(txt, x, y);
        FontMetrics fm = g.getFontMetrics();

        g.drawLine(x, y + 1, x + fm.stringWidth(txt), y + 1);
    }
}

