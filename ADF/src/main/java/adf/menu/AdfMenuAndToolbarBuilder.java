package adf.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 1/14/2017.
 */
public class AdfMenuAndToolbarBuilder {

    private static final String ADF_ICON_CLASS_PATH_PREFIX = "/adf-resources/images/app-icons/";

    public static AdfMenuAndToolbarBuilder adfMenuAndToolbarBuilder;

    public static synchronized AdfMenuAndToolbarBuilder getInstance() {
        if (adfMenuAndToolbarBuilder == null) {
            adfMenuAndToolbarBuilder = new AdfMenuAndToolbarBuilder();
        }
        return adfMenuAndToolbarBuilder;
    }

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


    //
    //   I n s t a n c e
    //

    protected AdfMenuAndToolbarBuilder() {

    }

    private static final AdfMenuActionListener menuActionListener = (ActionEvent e) -> {
return true;
    };

    private final Map<String, AdfBasicAction> menuItemLabelToAction = new HashMap();

    /**
     * @return
     */
    public JMenuBar buildMenuBur() {
        return buildMenuBur(menuActionListener, ADF_ICON_CLASS_PATH_PREFIX, menuItemLabelToAction);
    }

    /**
     * @param adfMenuActionListener
     * @param iconClassPath
     * @param menuLabelToActionMap
     * @return
     */
    private JMenuBar buildMenuBur(AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                  Map<String, AdfBasicAction> menuLabelToActionMap) {

        AdfMenuBar adfMenuBar = new AdfMenuBar(adfMenuActionListener, iconClassPath, menuLabelToActionMap);

        //
        adfMenuBar.addAdfAppMenu(AdfMenuAndToolbarController.MENU_ITEM_FILES);
        adfMenuBar.addMenuItem(AdfMenuAndToolbarController.MENU_ITEM_OPEN);
        adfMenuBar.addMenuSeparator();
        adfMenuBar.addMenuItem(AdfMenuAndToolbarController.MENU_ITEM_EXIT);

        return adfMenuBar;
    }

    private void testAdfMenuBarBuilder() {
//        JMenuBar menuBar = buildMenuBur(ICON_CLASS_PATH_PREFIX, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdfMenuAndToolbarBuilder adfMenuAndToolbarBuilder = new AdfMenuAndToolbarBuilder();
            adfMenuAndToolbarBuilder.testAdfMenuBarBuilder();
        });
    }
}
