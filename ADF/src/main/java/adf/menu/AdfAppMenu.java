package adf.menu;

import adf.utils.BuildUtils;

import javax.swing.*;
import java.util.Map;

/**
 * Created by Admin on 1/15/2017.
 */
public class AdfAppMenu extends JMenu {

    private static AdfToggleButtonActionGroup adfToggleButtonActionGroup = new AdfToggleButtonActionGroup();

    public static AdfAppMenu create(String menuLabel, AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                    Map<String, AdfBasicAction> menuItemLabelToAction) {
        return new AdfAppMenu(menuLabel, adfMenuActionListener, iconClassPath, menuItemLabelToAction, null);
    }

    public static AdfAppMenu create(String menuLabel, AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                    Map<String, AdfBasicAction> menuItemLabelToAction,
                                    AdfAbstractActionGroup adfAbstractActionGroup) {
        return new AdfAppMenu(menuLabel, adfMenuActionListener, iconClassPath, menuItemLabelToAction,
                adfAbstractActionGroup);
    }

    //
    //   I n s t a n c e
    //

    private final AdfMenuActionListener adfMenuActionListener;
    private final String iconClassPath;
    private final Map<String, AdfBasicAction> menuItemLabelToActionMap;
//    private final AdfAbstractActionGroup adfAbstractActionGroup;

    AdfAppMenu(String menuLabel, AdfMenuActionListener adfMenuActionListener, String iconClassPath,
               Map<String, AdfBasicAction> menuItemLabelToActionMap, AdfAbstractActionGroup adfAbstractActionGroup) {
        super(menuLabel);
        this.adfMenuActionListener = adfMenuActionListener;
        this.iconClassPath = iconClassPath;
        this.menuItemLabelToActionMap = menuItemLabelToActionMap;
//        this.adfAbstractActionGroup = adfAbstractActionGroup;
    }

    public JComponent addMenuItem(String label) {
        JComponent menuItem = addMenuItem(label, true);
        return menuItem;
    }

    public JComponent addMenuItem(String label, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = addMenuItem(label, null, true, adfAbstractActionGroup);
        return menuItem;
    }

    public JComponent addMenuItem(String label, String iconName, AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = addMenuItem(label, iconName, true, adfAbstractActionGroup);
        return menuItem;
    }

    public JComponent addMenuItem(String label, boolean enabled) {
        JComponent menuItem = addMenuItem(label, null, enabled);
        return menuItem;
    }



    public JComponent addMenuItem(String label, String iconName, boolean enabled) {
        JComponent menuItem = addMenuItem(label, iconName, enabled, null);
        return menuItem;
    }

    public JComponent addMenuItem(String label, String iconName, boolean enabled,
                                  AdfAbstractActionGroup adfAbstractActionGroup) {
        JComponent menuItem = createTextMenuItem(label, iconName, enabled, adfAbstractActionGroup);
        add(menuItem);
        return menuItem;
    }

    public JComponent addToggleMenuItem(String label, String iconName, String selectedIconName, boolean enabled) {
        JComponent menuItem = createToggleMenuItem(label, iconName, selectedIconName, enabled);
        add(menuItem);
        return menuItem;
    }

    public JComponent addMenuSeparator() {
        JSeparator separator = new JSeparator();
        add(separator);
        return separator;
    }

    /**
     * Created JMenuItem with AdfJButtonAction
     *
     * @param label
     * @param iconName
     * @param enabled
     * @param adfAbstractActionGroup
     * @return
     */
    private JComponent createTextMenuItem(String label, String iconName, boolean enabled,
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
        menuItemLabelToActionMap.put(label, adfBasicAction);

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

//        adfToggleButtonMenuItem.setIconTextGap(5);

        AdfBasicAction adfBasicAction = new AdfToggleButtonAction(label, mainIcon, adfMenuActionListener);
        adfToggleButtonMenuItem.setAction(adfBasicAction);
        adfToggleButtonMenuItem.setActionCommand(label);

        adfBasicAction.setEnabled(enabled);
        adfBasicAction.setSelected(false);
        menuItemLabelToActionMap.put(label, adfBasicAction);
        return adfToggleButtonMenuItem;
    }
}
