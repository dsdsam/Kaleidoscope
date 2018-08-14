package adf.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BuildUtils {

    public static final Logger logger = Logger.getLogger(BuildUtils.class.getName());

    private static final String ADF_ICON_RESOURCE_CLASSPATH_PREFIX = "/adf-resouces/adf-icons/";
    private static final String ADF_IMAGE_RESOURCE_CLASSPATH_PREFIX = "/adf-resouces/images/";

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


    /**
     * @param text
     * @param color
     * @param alignment
     * @param fontSize
     * @return
     */
    public static JLabel makeLabel(String text, Color color, int alignment, int fontSize) {
        JLabel label = new JLabel(text, alignment);
        label.setForeground(color);
        resetComponentFont(label, Font.BOLD, fontSize);
        return label;
    }

    /**
     * @param text
     * @param fontName
     * @param color
     * @param alignment
     * @param fontSize
     * @return
     */
    public static JLabel makeLabel(String text, String fontName, Color color, int alignment, int fontSize) {
        JLabel label = new JLabel(text, alignment);
        label.setForeground(color);
        setCompFont(label, fontName, Font.BOLD, fontSize);
        return label;
    }

    /**
     * @param comp
     * @param fontName
     * @param style
     * @param fontSize
     */
    public static void setCompFont(java.awt.Component comp, String fontName, int style, int fontSize) {
        comp.setFont(new java.awt.Font(fontName, style, fontSize));
    }

    /**
     * @param comp
     * @param style
     * @param fontSize
     */
    public static void resetComponentFont(Component comp, int style, int fontSize) {
        Font font = comp.getFont();
        comp.setFont(font.deriveFont(style, fontSize));
    }

    public static JLabel makeLabelAndAddToPanel(JPanel panel, String text,
                                                Color color, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setForeground(color);
        BuildUtils.resetComponentFont(label, Font.BOLD, 10);
        panel.add(label);
        return label;
    }

    public static List copyList(List otherList) {
        List newList = new ArrayList(otherList);
        return newList;
    }

    public static ImageIcon getAdfIconFromClassPath(String iconFileName) {
        String iconResourcePath = ADF_ICON_RESOURCE_CLASSPATH_PREFIX + iconFileName;
        return getImageIcon(iconResourcePath);
    }

    public static ImageIcon getAdfImageIconFromClassPath(String imageFileName) {
        String iconResourcePath = ADF_IMAGE_RESOURCE_CLASSPATH_PREFIX + imageFileName;
        return getImageIcon(iconResourcePath);
    }

    /**
     * Used by DSDSSE and SE
     *
     * @param iconClassPath
     * @return
     */
    public static ImageIcon getImageIcon(String iconClassPath) {
        ImageIcon imageIcon = null;
        URL url = BuildUtils.class.getResource(iconClassPath);
        if (url != null) {
            imageIcon = new ImageIcon(url);
        }
        return imageIcon;
    }

    public static ImageIcon getDisabledImageIcon(ImageIcon imageIcon){
        ImageProducer prod = new FilteredImageSource(imageIcon.getImage().getSource(), filter);
        Image disabledImage = Toolkit.getDefaultToolkit().createImage(prod);
        ImageIcon disabledImageIcon = new ImageIcon(disabledImage);
        return disabledImageIcon;
    }

    public static JButton createIconButton(String iconClassPath, boolean enabled, String tipText,
                                           ActionListener actionListener) {
        ImageIcon imageIcon = BuildUtils.getImageIcon(iconClassPath);
        JButton button = new JButton(imageIcon);
        ImageProducer prod = new FilteredImageSource(imageIcon.getImage().getSource(), filter);
        Image disabledImage = Toolkit.getDefaultToolkit().createImage(prod);
        button.setDisabledIcon(new ImageIcon(disabledImage));
        button.setFocusPainted(false);
        button.setToolTipText(tipText);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addActionListener(actionListener);
        button.setEnabled(enabled);
        return button;
    }

    public static final JToggleButton createToggleIconButton(String defaultIconClassPath, String selectedIconClassPath,
                                                             boolean selected, boolean enabled, ItemListener itemListener) {
        ImageIcon defaultImageIcon = BuildUtils.getImageIcon(defaultIconClassPath);
        ImageIcon selectedImageIcon = BuildUtils.getImageIcon(selectedIconClassPath);
        JToggleButton toggleButton = new JToggleButton(defaultImageIcon, selected);
        toggleButton.setSelectedIcon(selectedImageIcon);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(null);
        toggleButton.setEnabled(enabled);
        toggleButton.addItemListener(itemListener);
        return toggleButton;
    }
}
