package adf.menu;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AdfMenuBar extends JMenuBar {

    //
    //   I n s t a n c e
    //

    private final AdfMenuActionListener adfMenuActionListener;
    private final String iconClassPath;
    private final Map<String, AdfBasicAction> menuItemToActionMap;
    private AdfAppMenu lastlyAddedAdfAppMenu;
    Color bgColor = Color.lightGray;
    Color fgColor = Color.white;

    public AdfMenuBar() {
        this.adfMenuActionListener = null;
        this.iconClassPath = null;
        this.menuItemToActionMap = null;
    }

    /**
     * @param adfMenuActionListener
     * @param iconClassPath
     * @param menuItemToActionMap
     */
    public AdfMenuBar(AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                      Map<String, AdfBasicAction> menuItemToActionMap) {
        this.adfMenuActionListener = adfMenuActionListener;
        this.iconClassPath = iconClassPath;
        this.menuItemToActionMap = menuItemToActionMap;
    }

    public AdfAppMenu addAdfAppMenu(String menuLabel) {
        AdfAppMenu adfAppMenu = addAdfAppMenu(menuLabel, false);
        return adfAppMenu;
    }

    public AdfAppMenu addAdfAppMenu(String menuLabel, boolean rightAlignment) {
        AdfAppMenu adfAppMenu = addAdfAppMenu(menuLabel, rightAlignment, null);
        return adfAppMenu;
    }

    public AdfAppMenu addAdfAppMenu(String menuLabel, boolean rightAlignment,
                                    AdfAbstractActionGroup adfAbstractActionGroup) {
        AdfAppMenu adfAppMenu = createAdfAppMenu(menuLabel, rightAlignment, adfAbstractActionGroup);
        return adfAppMenu;
    }

    /**
     * @param menuLabel
     * @return
     */
    private AdfAppMenu createAdfAppMenu(String menuLabel, boolean rightAlignment, AdfAbstractActionGroup adfAbstractActionGroup) {
        AdfAppMenu adfAppMenu = AdfAppMenu.create(menuLabel, adfMenuActionListener, iconClassPath,
                menuItemToActionMap, adfAbstractActionGroup);
        lastlyAddedAdfAppMenu = adfAppMenu;
        if (rightAlignment) {
            add(Box.createHorizontalGlue());
        }
        add(adfAppMenu);
        return adfAppMenu;
    }

    //
    //   A d d i n g   M e n u   I t e m
    //

    public JComponent addMenuItem(String label) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label);
        return menuItem;
    }

    public JComponent addMenuItem(String label, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label, adfAbstractActionGroup);
        return menuItem;
    }

    public JComponent addMenuItem(String label, boolean enabled) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label, null, enabled);
        return menuItem;
    }

    public JComponent addMenuItem(String label, String iconName) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label, iconName, true);
        return menuItem;
    }

    public JComponent addMenuItem(String label, String iconName, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label, iconName, adfAbstractActionGroup);
        return menuItem;
    }

    public JComponent addToggleMenuItem(String label, String iconName, String selectedIconName) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addToggleMenuItem(label, iconName, selectedIconName, true);
        return menuItem;
    }

    public JComponent addMenuItem(String label, String iconName, boolean enabled) {
        JComponent menuItem = lastlyAddedAdfAppMenu.addMenuItem(label, iconName, enabled);
        return menuItem;
    }

    public JComponent addMenuSeparator() {
        JSeparator separator = new JSeparator();
        lastlyAddedAdfAppMenu.add(separator);
        return separator;
    }

    //
    //   Finding items
    //

    public JMenu findMenuBarItemByName(String menuText) {
        MenuElement[] menuElements = getSubElements();
        for (MenuElement menuElement : menuElements) {
            if (!(menuElement instanceof JMenu)) {
                continue;
            }
            JMenu menu = (JMenu) menuElement;
            String text = menu.getText();
            if (text != null && text.equals(menuText)) {
                return menu;
            }
        }
        return null;
    }

    public JMenuItem findSubMenuItemByName(String menuItemText, String subMenuItemText) {
        JMenu menu = findMenuBarItemByName(menuItemText);
        if (menu == null) {
            return null;
        }
        MenuElement[] menuElements = menu.getSubElements();
        for (MenuElement menuElement : menuElements) {
            if (!(menuElement instanceof JPopupMenu)) {
                continue;
            }
            JPopupMenu popupMenu = (JPopupMenu) menuElement;
            MenuElement[] subMenuElements = popupMenu.getSubElements();
            for (MenuElement subMenuElement : subMenuElements) {
                JMenuItem subMenuItem = (JMenuItem) subMenuElement;
                String text = subMenuItem.getText();
                if (text != null && text.equals(subMenuItemText)) {
                    return subMenuItem;
                }
            }
        }
        return null;
    }
}