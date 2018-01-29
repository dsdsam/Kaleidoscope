package dsdsse.graphview;

import dsdsse.app.AppController;
import dsdsse.app.AppStateModel;
import dsdsse.app.DsdsseMainFrame;
import dsdsse.designspace.initializer.InitAssistantInterface;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 8/27/13
 * Time: 7:11 PM
 */
public class EditorPopupMenu extends MclnGraphViewPopupMenu {

    private static final String MENU_ITEM_DELETE_PROPERTY = "Delete The Property";
    private static final String MENU_ITEM_MOVE_PROPERTY = "Move The Property";
    private static final String MENU_ITEM_INIT_PROPERTY = "Initialize The Property";

    private static final String MENU_ITEM_DELETE_CONDITION = "Delete The Condition";
    private static final String MENU_ITEM_MOVE_CONDITION = "Move The Condition";

    private static final String MENU_ITEM_DELETE_ARC = "Delete The Arc";
    private static final String MENU_ITEM_MOVE_ARC = "Move The Arc";
    private static final String MENU_ITEM_INIT_ARC = "Initialize The Arc";

    private static List<String> propertyMenuItems = new ArrayList();
    private static List<String> conditionMenuItems = new ArrayList();
    private static List<String> arcMenuItems = new ArrayList();

    static {
        propertyMenuItems.add("-");
        propertyMenuItems.add(MENU_ITEM_INIT_PROPERTY);
        propertyMenuItems.add("-");
        propertyMenuItems.add(MENU_ITEM_DELETE_PROPERTY);
    }

    static {
        conditionMenuItems.add("-");
        conditionMenuItems.add(MENU_ITEM_DELETE_CONDITION);
    }

    static {
        arcMenuItems.add("-");
        arcMenuItems.add(MENU_ITEM_INIT_ARC);
        arcMenuItems.add("-");
        arcMenuItems.add(MENU_ITEM_DELETE_ARC);
    }

    //
    //   I n s t a n c e
    //

    private MclnPropertyView mclnPropertyView;
    private MclnConditionView mclnConditionView;
    private MclnArcView mclnArcView;


    private ActionListener propertyPopupMenuItemActionListener = (ActionEvent e) -> {
//        System.out.println("Item selected " + e.getActionCommand());
        if (MENU_ITEM_DELETE_PROPERTY.equalsIgnoreCase(e.getActionCommand())) {
            MclnGraphViewEditor.getInstance().processUserDeletesElementViaPopup(mclnPropertyView);

        } else if (MENU_ITEM_INIT_PROPERTY.equalsIgnoreCase(e.getActionCommand())) {
            DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
            AppController.getInstance().setInitAssistantToInitializeProperty(mainFrame,
                    EditorPopupMenu.this.mclnPropertyView);
        }

    };

    private ActionListener conditionPopupMenuItemActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("Item selected " + e.getActionCommand());
            if (MENU_ITEM_DELETE_CONDITION.equalsIgnoreCase(e.getActionCommand())) {
                MclnGraphViewEditor.getInstance().processUserDeletesElementViaPopup(mclnConditionView);
            }
        }
    };

    private ActionListener arcPopupMenuItemActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("Item selected " + e.getActionCommand());
            if (MENU_ITEM_DELETE_ARC.equalsIgnoreCase(e.getActionCommand())) {
                MclnGraphViewEditor.getInstance().processUserDeletesElementViaPopup(mclnArcView);

            } else if (MENU_ITEM_INIT_ARC.equalsIgnoreCase(e.getActionCommand())) {
                DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
                AppController.getInstance().setInitAssistantToInitializeArc(mainFrame,
                        EditorPopupMenu.this.mclnArcView);
            }
        }
    };


    /**
     * C o n s t r u c t i n g
     *
     * @param mclnPropertyView
     */
    EditorPopupMenu(MclnPropertyView mclnPropertyView) {
        this.mclnPropertyView = mclnPropertyView;
        initPopupMenu(propertyMenuItems, propertyPopupMenuItemActionListener);
    }

    EditorPopupMenu(MclnConditionView mclnConditionView) {
        this.mclnConditionView = mclnConditionView;
        initPopupMenu(conditionMenuItems, conditionPopupMenuItemActionListener);
    }

    EditorPopupMenu(MclnArcView mclnArcView) {
        this.mclnArcView = mclnArcView;
        initPopupMenu(arcMenuItems, arcPopupMenuItemActionListener);
    }


    private void initPopupMenu(List<String> menuItems, ActionListener popupMenuItemActionListener) {
        for (String itemText : menuItems) {
            if ("-".equalsIgnoreCase(itemText)) {
                add(new JSeparator());
            } else {
                JMenuItem menuItem = new JMenuItem(itemText);
                menuItem.setEnabled(false);
                menuItem.setForeground(Color.BLACK);
                menuItem.setUI(new BasicMenuItemUI() {
                    {
                        selectionBackground = Color.ORANGE;
                        selectionForeground = Color.DARK_GRAY;
                    }
                });
                menuItem.addActionListener(popupMenuItemActionListener);

                //
                //   E n a b l i n g   m e n u   i t e m s
                //
                boolean printToolIsActive = AppStateModel.isPrintToolActive();
                /**
                 * The deletion of individual entity is allowed when either Create Properties or
                 * Create Conditions are ON or none of Creation operations is ON
                 */
                boolean creationOperationIsON = AppStateModel.isCreationOperationOn();
//                System.out.println("EditorPopupMenu: Creation operation is On: " + creationOperationIsON);
                if (!printToolIsActive && (AppStateModel.getCurrentOperation().isCreatingProperties() ||
                        AppStateModel.getCurrentOperation().isCreatingConditions() ||
                        !creationOperationIsON) && (MENU_ITEM_DELETE_PROPERTY.equals(itemText)
                        || MENU_ITEM_DELETE_CONDITION.equals(itemText) || MENU_ITEM_DELETE_ARC.equals(itemText))) {
                    menuItem.setEnabled(true);
                }

           /*
              The Initialize menu item is enabled when Init Assistant is either closed or open and idling
            */
                boolean initAssistantCanBeInitialised = AppStateModel.canInitAssistantBeInitialized();
//                System.out.println("EditorPopupMenu: initAssistant Can Be Initialised: " + initAssistantCanBeInitialised);
                boolean operationCanBeInterrupted = AppStateModel.canOperationBeInterrupted();
//                System.out.println("EditorPopupMenu: initAssistant Can Be Interrupted: " + operationCanBeInterrupted);
                boolean initAssistantUpAndRunning = InitAssistantInterface.isInitAssistantUpAndRunning();
//                System.out.println("EditorPopupMenu: initAssistant ia Up An dRunning: " + initAssistantUpAndRunning);
                if (!printToolIsActive && initAssistantCanBeInitialised &&
                        (MENU_ITEM_INIT_PROPERTY.equals(itemText) || MENU_ITEM_INIT_ARC.equals(itemText))) {
                    menuItem.setEnabled(true);
                }
                add(menuItem);
            }
        }
    }
}
