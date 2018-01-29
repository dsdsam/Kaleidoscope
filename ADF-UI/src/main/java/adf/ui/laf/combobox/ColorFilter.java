package adf.ui.laf.combobox;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * Created by u0180093 on 4/26/2016.
 */
public class ColorFilter extends RGBImageFilter {
    private float fromHue;
    private float toHue;
    private float fromSat;
    private float toSat;
    private float fromBri;
    private float toBri;
    private float hOffset;
    private float sOffset;
    private float bOffset;

    public ColorFilter(Color r, Color c) {
        /*
         *
         * determine what the offsets for hue,
         * saturation, and brightness must be
         * in order to translate the Color r
         * into the Color c
         *
         */
        float[] newHSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

        toHue = newHSB[0];
        toSat = newHSB[1];
        toBri = newHSB[2];

        float[] rootHSB = Color.RGBtoHSB(r.getRed(), r.getGreen(), r.getBlue(), null);

        fromHue = rootHSB[0];
        fromSat = rootHSB[1];
        fromBri = rootHSB[2];

        hOffset = toHue - fromHue;
        sOffset = toSat - fromSat;
        bOffset = toBri - fromBri;

        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, null);

        /*
         * use this to change colorize via toHue
         */
        hsb[0] += hOffset;
        hsb[1] += sOffset;
        hsb[2] += bOffset;

        /*
         * suppress clipping that may have occurred
         */
        hsb[0] = Math.max(0, Math.min(1, hsb[0]));
        hsb[1] = Math.max(0, Math.min(1, hsb[1]));
        hsb[2] = Math.max(0, Math.min(1, hsb[2]));

        return (Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0xffffff) | (rgb & 0xff000000);
    }
}
