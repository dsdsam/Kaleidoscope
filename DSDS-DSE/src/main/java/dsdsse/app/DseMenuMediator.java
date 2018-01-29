package dsdsse.app;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 4/28/2017.
 */
public class DseMenuMediator {

    /**
     * Here are general cases when Menu item and corresponding Toolbar button can be enabled or disabled.
     * <p>
     * 1) Via clicking on Menu item or Toolbar button
     * 2) Via calling enable method on associated Action
     * 3) By calling one the public methods of this class
     * <p>
     * Enabling or disabling Menu item and corresponding Toolbar button.
     * <p>
     * 1) Some may be enabled or disabled upon startup
     * 2) Clicking menu item disables it.
     * 3) Clicking on menu item activates action that may require to disable some other menu items.
     * When action is fulfilled the disabled menu items should be re-enabled.
     * This is accomplished by creating action groups and via this class.
     * As some menu items are members of some group, they may be disabled and re-enabled
     * via group enabling/disabling mechanism
     */

    private static final Map<String, AbstractAction> registeredActions = new HashMap<>();
    private static final Map<String, Boolean> lockedActions = new HashMap<>();

    public static final void registerAction(String menuItem, AbstractAction action) {
        registeredActions.put(menuItem, action);
        lockedActions.put(menuItem, Boolean.FALSE);
        System.out.println("DseMenuMediator.registerAction: menu item = " + menuItem +
                ", action = " + action.getClass().getSimpleName());
    }

    public static final AbstractAction unregisterAction(String menuItem) {
        AbstractAction action = registeredActions.remove(menuItem);
        return action;
    }

    // ================================================================================================================

    //
    //   S t a t e   C o n t r o l   M e t h o d s
    //

    // ================================================================================================================

    //   G R O U P

    public static final void enableAll(boolean enable) {
        AbstractAction action;
        boolean locked;
        action = registeredActions.get(AppController.MENU_ITEM_SHOW_PRINT_CONTENT);
        action.setEnabled(enable);

        action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        locked = lockedActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        if (!locked) {
            // not locked -> just enable ot disable
            action.setEnabled(enable);
        } else {
            // locked
        }

        action = registeredActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
        locked = lockedActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
        if (!locked) {
            // not locked -> just enable ot disable
            action.setEnabled(enable);
        } else {
            // locked
        }

        action = registeredActions.get(AppController.MENU_ITEM_WHAT_IS_DSDS_DSE);
        action.setEnabled(enable);
    }

    //   P R I N T

    /**
     * Called to enable currently disabled other members
     */
    public static void openPrintContextMenuItemClickedAndDisabled() {
//        enablePrintContextMenuItem(false);
//        enableInitAssistantMenuItem(true);
//        enableShowSetupMenuItem(true);
//        enableHelpMenuItem(true);

    }

    public static void closingPrintContextAndMenuItemIsEnabled() {

        //
//        AbstractAction setupAction = registeredActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
//        AbstractAction helpAction = registeredActions.get(AppController.MENU_ITEM_SHOW_HELP_PANEL);
//        if(setupAction.isEnabled() && helpAction.isEnabled()){
//            enableInitAssistantMenuItem(true);
//        }
        enableInitAssistantMenuItemAndUnlock();
    }

    //   I N I T   A S S I S T A N T

    /**
     * Called to enable currently disabled other members
     */
    public static void disableInitAssistantOnMenuItemClicked() {
        /*
            This call is needed to disable Init Assistant menu item
            when it is open not from menu but to from Edit popup
         */
        disableInitAssistantMenuItemAndLock();

        enableShowSetupMenuItem(true);
        enableHelpMenuItem(true);
    }

    /**
     * Called when Init Assistant Close button clicked
     */
    public static void enableInitAssistant() {
        enableInitAssistantMenuItemAndUnlock();
    }

    /**
     *
     * @param enable
     */
    public static void enableOrDisableInitAssistantOnCreationStartedOrStopped(boolean enable) {
        AbstractAction   action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        boolean  locked = lockedActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        if (enable) {
            if (!locked) {
                // not locked -> just enable
                action.setEnabled(enable);
            } else {
                // locked -> leave it disabled
            }
        } else {
            // disable
            action.setEnabled(false);
        }
    }


    //   S E T U P

    /**
     * Called to enable currently disabled other members
     */
    public static void openSetupPanelMenuItemClickedAndDisabled() {
//        enablePrintContextMenuItem(true);

        // do not enable IA when Print is open
        AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_PRINT_CONTENT);
        if (action.isEnabled()) {
            enableInitAssistantMenuItemAndUnlock();
        }

        enableHelpMenuItem(true);
    }

    //   H E L P

    /**
     * Called to enable currently disabled other members
     */
    public static void helpMenuItemClickedAndDisabled() {
//        enablePrintContextMenuItem(true);

        // do not enable IA when Print is open
        AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_PRINT_CONTENT);
        if (action.isEnabled()) {
            enableInitAssistantMenuItemAndUnlock();
        }

        enableShowSetupMenuItem(true);
    }

    // ================================================================================================================

    //
    //   M e t h o d s   t o   e x e c u t e   e n a b l i n g / d i s a b l i n g
    //

    // ================================================================================================================

    //   P r i n t

    private static final void enablePrintContextMenuItem(boolean enable) {
        if (enable) {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_PRINT_CONTENT);
            action.setEnabled(enable);
        } else {
//            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_PRINT_CONTENT);
//            action.setEnabled(enable);
        }
    }

    //   I n i t   A s s i s t a n t

    private static final void disableAndLockOrEnableAndUnlockInitAssistant(boolean enable) {
        if (enable) {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
            action.setEnabled(enable);
            lockedActions.put(AppController.MENU_ITEM_LAUNCH_IA, Boolean.FALSE);
        } else {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
            action.setEnabled(enable);
            lockedActions.put(AppController.MENU_ITEM_LAUNCH_IA, Boolean.TRUE);
        }
    }

    private static final void enableInitAssistantMenuItemAndUnlock() {
        AbstractAction action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        action.setEnabled(true);
        lockedActions.put(AppController.MENU_ITEM_LAUNCH_IA, Boolean.FALSE);
    }

    private static final void disableInitAssistantMenuItemAndLock() {
        AbstractAction action = registeredActions.get(AppController.MENU_ITEM_LAUNCH_IA);
        action.setEnabled(false);
        lockedActions.put(AppController.MENU_ITEM_LAUNCH_IA, Boolean.TRUE);
    }

    //   S e t u p

    private static final void enableShowSetupMenuItem(boolean enable) {
        if (enable) {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
            action.setEnabled(enable);
            lockedActions.put(AppController.MENU_ITEM_SHOW_SETUP_PANEL, Boolean.FALSE);
        } else {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
            action.setEnabled(enable);
            lockedActions.put(AppController.MENU_ITEM_SHOW_SETUP_PANEL, Boolean.TRUE);
        }
//        if (enable) {
//            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_SETUP_PANEL);
//            action.setEnabled(enable);
//        }
    }

    //   H e l p

    private static final void enableHelpMenuItem(boolean enable) {
        if (enable) {
            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_WHAT_IS_DSDS_DSE);
            action.setEnabled(enable);
        } else {
//            AbstractAction action = registeredActions.get(AppController.MENU_ITEM_SHOW_HELP_PANEL);
        }
    }
}
