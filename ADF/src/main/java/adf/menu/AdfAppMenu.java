package adf.menu;

import adf.utils.BuildUtils;

import javax.swing.*;
import java.util.Map;

/**
 * Each App Menu has Menu Item Label To Action Map provided by menu creator.
 * All added to App Menu items have optional Abstract Action Group.
 * When a Menu Item is created, the menu item Action is created and is
 * assotiated with provided Action Group. Hence, when a menu item is clicked
 * associated action is called that in turn calls Action group.
 * <p>
 * Created by Admin on 1/15/2017.
 */
public class AdfAppMenu extends JMenu {

    public static AdfAppMenu create(String menuLabel, AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                    Map<String, AdfBasicAction> menuItemLabelToAction) {
        return new AdfAppMenu(menuLabel, adfMenuActionListener, iconClassPath, menuItemLabelToAction);
    }

    //
    //   I n s t a n c e
    //

    private final AdfMenuActionListener adfMenuActionListener;
    private final String iconClassPath;
    private final Map<String, AdfBasicAction> menuItemLabelToActionMap;

    private AdfAppMenu(String menuLabel, AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                       Map<String, AdfBasicAction> menuItemLabelToActionMap) {
        super(menuLabel);
        this.adfMenuActionListener = adfMenuActionListener;
        this.iconClassPath = iconClassPath;
        this.menuItemLabelToActionMap = menuItemLabelToActionMap;
    }

    JComponent addMenuItem(String label) {
        JComponent menuItem = addMenuItem(label, null,true);
        return menuItem;
    }

    JComponent addMenuItem(String label, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = addMenuItem(label, null, true, adfAbstractActionGroup);
        return menuItem;
    }

    JComponent addMenuItem(String label, String iconName, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = addMenuItem(label, iconName, true, adfAbstractActionGroup);
        return menuItem;
    }

    JComponent addMenuItem(String label, String iconName, boolean enabled) {
        JComponent menuItem = addMenuItem(label, iconName, enabled, null);
        return menuItem;
    }


    JComponent addToggleMenuItem(String label, String iconName, String selectedIconName, boolean enabled) {
        JComponent menuItem = null;
        try {
            menuItem = createToggleMenuItem(label, iconName, selectedIconName, enabled);
            add(menuItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuItem;
    }

    public JComponent addMenuSeparator() {
        JSeparator separator = new JSeparator();
        add(separator);
        return separator;
    }

    public AdfAppMenu addSubmenu(AdfAppMenu adfAppMenu) {
        add(adfAppMenu);
        return adfAppMenu;
    }

    //
    //   P r i v a t e   m e t h o d s
    //

    private JComponent addMenuItem(String label, String iconName, boolean enabled,
                                   AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = createTextMenuItemWithActionGroup(label, iconName, enabled, adfAbstractActionGroup);
        add(menuItem);
        return menuItem;
    }

    /**
     * Created JMenuItem with added AdfJButtonAction that is
     * registered in Abstract Action Group provided by menu creator
     *
     * @param label
     * @param iconName
     * @param enabled
     * @param adfAbstractActionGroup
     * @return
     */
    private JComponent createTextMenuItemWithActionGroup(String label, String iconName, boolean enabled,
                                                         AdfAbstractActionGroup adfAbstractActionGroup) {
        AdfBasicAction adfBasicAction;
        if (iconName == null) {
            adfBasicAction = new AdfJButtonAction(label, adfMenuActionListener);
        } else {
            ImageIcon imageIcon = BuildUtils.getImageIcon(iconClassPath + iconName);
            adfBasicAction = new AdfJButtonAction(label, imageIcon, adfMenuActionListener);
        }
        JMenuItem menuItem = new JMenuItem(adfBasicAction);
        adfBasicAction.setEnabled(enabled);
        if (menuItemLabelToActionMap != null) {
            menuItemLabelToActionMap.put(label, adfBasicAction);
        }

        if (adfAbstractActionGroup != null) {
            adfAbstractActionGroup.registerAction(adfBasicAction);
        }
        return menuItem;
    }

    /**
     * Creation of Toggle Item menu
     *
     * @param label
     * @param iconName
     * @param selectedIconName
     * @param enabled
     * @return
     */
    private JComponent createToggleMenuItem(String label, String iconName, String selectedIconName, boolean enabled) {

        ImageIcon mainIcon = BuildUtils.getImageIcon(iconClassPath + iconName);
        ImageIcon selectedIcon = BuildUtils.getImageIcon(iconClassPath + selectedIconName);

        if (mainIcon == null) {
            new Exception("Icon \"" + iconName + "\" not found").printStackTrace();
            return null;
        }
        if (selectedIcon == null) {
            new Exception("Icon \"" + iconName + "\" not found").printStackTrace();
            return null;
        }

        AdfToggleButtonMenuItem adfToggleButtonMenuItem = new AdfToggleButtonMenuItem(label, mainIcon, false);
        ImageIcon disabledMainIcon = BuildUtils.getDisabledImageIcon(mainIcon);
        adfToggleButtonMenuItem.setDisabledIcon(disabledMainIcon);

        adfToggleButtonMenuItem.setSelectedIcon(selectedIcon);
        ImageIcon disabledSelectedIcon = BuildUtils.getDisabledImageIcon(selectedIcon);
        adfToggleButtonMenuItem.setDisabledSelectedIcon(disabledSelectedIcon);

        // adding Toggle Button Action to this menu item
        AdfBasicAction adfBasicAction = new AdfToggleButtonAction(label, mainIcon, adfMenuActionListener);
        adfToggleButtonMenuItem.setAction(adfBasicAction);
        adfToggleButtonMenuItem.setActionCommand(label);

        adfBasicAction.setEnabled(enabled);
        adfBasicAction.setSelected(false);

        // adding Toggle Button Action to the applications's Action Map
        if (menuItemLabelToActionMap != null) {
            menuItemLabelToActionMap.put(label, adfBasicAction);
        }
        return adfToggleButtonMenuItem;
    }
}
