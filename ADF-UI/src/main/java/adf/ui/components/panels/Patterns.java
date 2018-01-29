package adf.ui.components.panels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: dk
 * Date: Jul 1, 2004
 * Time: 4:05:12 PM
 * <br/>
 */
public class Patterns {
    static BufferedImage lightPattern = null;
    static BufferedImage mediumPattern = null;
    static BufferedImage darkPattern = null;
    static BufferedImage navyPattern = null;
    static BufferedImage blotterTabPattern = null;
    static BufferedImage blotterCellPattern = null;
    static TexturePaint lightTexture = null;
    static TexturePaint mediumTexture = null;
    static TexturePaint darkTexture = null;
    static TexturePaint navyTexture = null;
    static TexturePaint blotterTabTexture = null;
    static TexturePaint blotterCellTexture = null;
    final static Rectangle2D.Double patternAnchor = new Rectangle2D.Double(0.0, 0.0, 1, 8);
    final static Rectangle2D blotterTabAnchor = new Rectangle2D.Double(0, 0, 1, 5);
    final static Rectangle2D blotterCellAnchor = new Rectangle2D.Double(0, 0, 1, 15);

    private Patterns() {
    }

    public static BufferedImage getLightPattern() {
        if (lightPattern == null) {
            Color[] colors = new Color[] {
                    new Color(154, 166, 214), new Color(145, 157, 205), new Color(149, 161, 209),
                    new Color(145, 157, 205), new Color(151, 163, 211), new Color(148, 160, 208),
                    new Color(144, 156, 204), new Color(142, 154, 202),
                };

            lightPattern = getPattern(lightPattern, colors);
        }

        return lightPattern;
    }

    public static TexturePaint getLightTexture() {
        if (lightTexture == null) {
            lightTexture = new TexturePaint(getLightPattern(), patternAnchor);
        }

        return lightTexture;
    }

    public static BufferedImage getMediumPattern() {
        if (mediumPattern == null) {
            Color[] colors = new Color[] {
                    new Color(126, 138, 190), new Color(122, 133, 187), new Color(121, 132, 188),
                    new Color(120, 131, 187), new Color(126, 138, 190), new Color(120, 131, 187),
                    new Color(122, 133, 189), new Color(120, 131, 187),
                };

            mediumPattern = getPattern(mediumPattern, colors);
        }

        return mediumPattern;
    }

    public static TexturePaint getMediumTexture() {
        if (mediumTexture == null) {
            mediumTexture = new TexturePaint(getMediumPattern(), patternAnchor);
        }

        return mediumTexture;
    }

    public static BufferedImage getDarkPattern() {
        if (darkPattern == null) {
            Color[] colors = new Color[] {
                    new Color(109, 118, 175), new Color(104, 114, 175), new Color(102, 112, 173),
                    new Color(102, 112, 173), new Color(107, 117, 176), new Color(102, 112, 173),
                    new Color(104, 114, 173), new Color(103, 113, 174),
                };

            darkPattern = getPattern(darkPattern, colors);
        }

        return darkPattern;
    }

    public static TexturePaint getDarkTexture() {
        if (darkTexture == null) {
            darkTexture = new TexturePaint(getDarkPattern(), patternAnchor);
        }

        return darkTexture;
    }

    public static BufferedImage getNavyPattern() {
        if (navyPattern == null) {
            Color[] colors = new Color[] {
                    new Color(0, 0, 109), new Color(2, 0, 102), new Color(0, 0, 98), new Color(1, 1, 101),
                    new Color(0, 0, 109), new Color(1, 0, 102), new Color(0, 0, 98), new Color(1, 1, 101),
                };

            navyPattern = getPattern(navyPattern, colors);
        }

        return navyPattern;
    }

    public static TexturePaint getNavyTexture() {
        if (navyTexture == null) {
            navyTexture = new TexturePaint(getNavyPattern(), patternAnchor);
        }

        return navyTexture;
    }

    public static BufferedImage getBlotterTabPattern() {
        if (blotterTabPattern == null) {
            Color[] colors = new Color[] {
                    new Color(168, 187, 255), new Color(161, 179, 255), new Color(163, 183, 254),
                    new Color(161, 179, 255), new Color(161, 179, 255),
                };

            blotterTabPattern = getPattern(blotterTabPattern, colors);
        }

        return blotterTabPattern;
    }

    public static TexturePaint getBlotterTabTexture() {
        if (blotterTabTexture == null) {
            blotterTabTexture = new TexturePaint(getBlotterTabPattern(), blotterTabAnchor);
        }

        return blotterTabTexture;
    }

    public static BufferedImage getBlotterCellPattern() {
        if (blotterCellPattern == null) {
            Color[] colors = new Color[] {
                    new Color(160, 179, 238), new Color(160, 179, 237), new Color(164, 183, 239),
                    new Color(163, 179, 239), new Color(164, 180, 239), new Color(165, 181, 240),
                    new Color(171, 188, 240), new Color(168, 185, 239), new Color(167, 184, 240),
                    new Color(167, 184, 240), new Color(171, 188, 240), new Color(168, 185, 239),
                    new Color(170, 187, 239), new Color(167, 187, 240), new Color(174, 190, 239),
                };

            blotterCellPattern = getPattern(blotterCellPattern, colors);
        }

        return blotterCellPattern;
    }

    public static TexturePaint getBlotterCellTexture() {
        if (blotterCellTexture == null) {
            blotterCellTexture = new TexturePaint(getBlotterCellPattern(), blotterCellAnchor);
        }

        return blotterCellTexture;
    }

    static BufferedImage getPattern(BufferedImage pattern, Color[] colors) {
        BufferedImage bufImg = new BufferedImage(1, (colors == null) ? 0 : colors.length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dImage = bufImg.createGraphics();

        for (int i = 0; i < colors.length; i++) {
            g2dImage.setPaint(colors[i]);
            g2dImage.fill(new Rectangle2D.Double(0, i, 1, 1));
        }

        pattern = bufImg;

        return bufImg;
    }
}
