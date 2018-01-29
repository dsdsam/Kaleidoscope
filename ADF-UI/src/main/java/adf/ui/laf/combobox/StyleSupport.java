package adf.ui.laf.combobox;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by u0180093 on 4/26/2016.
 */
public abstract class StyleSupport {
    /**
     * Creates a BufferedImage based on a width and height and
     * an array of ints where each int specfies the ARGB data for a pixel.
     *
     * @param width Width of image represented by data
     * @param height Height of image represented by data
     * @param data Array of type int where each int is a 32bit ARGB value.
     * If there are more than width*height elements in the array, only the
     * first width*height elements will be interpreted as pixel data.
     * @return
     */
    public static BufferedImage getImage(int width, int height, int[] data) {
        BufferedImage image;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        image = (BufferedImage) gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        image.setRGB(0, 0, width, height, data, 0, width);

        return image;
    }

    /**
     * Creates a BufferedImage based on an array of ints where
     * all but the last int specifies the ARGB data for a pixel
     * and the last int specifies the width of the Image.
     *
     * @param data
     * @return
     */
    public static BufferedImage getImage(int[] data) {
        int w = data[data.length - 1];
        int h = (data.length - 1) / w;

        return getImage(w, h, data);
    }

    /**
     * Copy of {@link java.awt.Color#brighter()} except it uses a custom factor
     */
    public static Color brighter(Color c, float factor) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - factor));

        if ((r == 0) && (g == 0) && (b == 0)) {
            return new Color(i, i, i);
        }

        if ((r > 0) && (r < i)) {
            r = i;
        }

        if ((g > 0) && (g < i)) {
            g = i;
        }

        if ((b > 0) && (b < i)) {
            b = i;
        }

        return new Color(Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255),
                Math.min((int) (b / factor), 255));
    }

    /**
     * Copy of {@link java.awt.Color#darker( )} except it uses a custom factor
     */
    public static Color darker(Color c, float factor) {
        return new Color(Math.max((int) (c.getRed() * factor), 0), Math.max((int) (c.getGreen() * factor), 0),
                Math.max((int) (c.getBlue() * factor), 0));
    }
}
