package adf.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.net.URL;

/**
 * Created by Admin on 3/5/2017.
 */
public class IconUtils {

    private static GrayFilter filter = new GrayFilter(true, 40) {
        @Override
        public int filterRGB(int x, int y, int rgb) {
            // Use NTSC conversion formula.
//                int gray = (int)((0.30 * ((rgb >> 16) & 0xff) +
//                        0.59 * ((rgb >> 8) & 0xff) +
//                        0.11 * (rgb & 0xff)) / 3);

            int gray = (int) ((0.40 * ((rgb >> 16) & 0xff) +
                    0.40 * ((rgb >> 8) & 0xff) +
                    0.40 * (rgb & 0xff)));

            int percent = 65;
            if (true) {
                gray = (255 - ((255 - gray) * (100 - percent) / 100));
            } else {
                gray = (gray * (100 - percent) / 100);
            }

            if (gray < 0) gray = 0;
            if (gray > 255) gray = 255;
            return (rgb & 0xff000000) | (gray << 16) | (gray << 8) | (gray << 0);
        }
    };

//    /**
//     * Used by DSDSSE and SE
//     *
//     * @param iconResourcePath
//     * @return
//     */
//    public static ImageIcon getImageIcon(String iconResourcePath) {
//        ImageIcon imageIcon = null;
//        URL url = IconUtils.class.getResource(iconResourcePath);
//        if (url != null) {
//            imageIcon = new ImageIcon(url);
//        }
//        return imageIcon;
//    }
//
//    public static ImageIcon getDisabledImageIcon(ImageIcon imageIcon){
//        ImageProducer prod = new FilteredImageSource(imageIcon.getImage().getSource(), filter);
//        Image disabledImage = Toolkit.getDefaultToolkit().createImage(prod);
//        ImageIcon disabledImageIcon = new ImageIcon(disabledImage);
//        return disabledImageIcon;
//    }
}
