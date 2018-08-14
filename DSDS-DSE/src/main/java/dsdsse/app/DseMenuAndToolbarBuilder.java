package dsdsse.app;

import adf.menu.*;
import adf.ui.controls.buttons.AdfTestToggleButton;
import adf.utils.BuildUtils;
import dsdsse.matrixview.MclnMatrixViewController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 9/20/2016.
 */
public class DseMenuAndToolbarBuilder {

    /*
       Button types"
       1) JButton may be clicked many times.
       2) Two states button (ToggleButton is candidate)
          Properties:
           a) has two states and may show different icons at these states
           b) second click flips button back to main state.
           c) state can be flipped back and fourth by any other class
           d) if added to a group flips back to main state when some other button is clicked.
           e) can be enabled or disabled

     */

    public static boolean TOGGLE_MENU_AND_TOOLBAR_BUTTONS = false;

    public static final String ICON_CLASS_PATH_PREFIX = "/dsdsse-resources/images/app-icons/";

    private static final String RENAME_PROJECT_ICON = "rename-proj.png";
    private static final String RENAME_MODEL_ICON = "rename-model.png";
    private static final String CLEAR_DESIGN_SPACE_ICON = "discard-model-blue.png";
    private static final String OPEN_PROJECT_ICON = "open.png";
    private static final String SAVE_PROJECT_AS_ICON = "save.png";
    private static final String SHOW_PRINT_PREVIEW_ICON = "show-print.png";
    private static final String HIDE_PRINT_PREVIEW_ICON = "hide-print-2.png";

    private static final String DEVELOPMENT_MODE_ICON = "edit_mode.png";
    private static final String CREATING_PROPERTIES_ICON = "create-property-nodes.png";
    private static final String STOP_CREATING_PROPERTIES_ICON = "stop-creating-property-nodes.png";
    private static final String CREATING_CONDITIONS_ICON = "create-condition-nodes.png";
    private static final String STOP_CREATING_CONDITIONS_ICON = "stop-creating-condition-nodes.png";
    private static final String CREATING_POLYLINE_ARCS_ICON = "create-polyline-arcs.png";
    private static final String STOP_CREATING_POLYLINE_ARCS_ICON = "stop-creating-polyline-arcs.png";
    private static final String CREATING_SPLINE_ARCS_ICON = "create-spline-arcs.png";
    private static final String STOP_CREATING_SPLINE_ARCS_ICON = "stop-creating-spline-arcs.png";

    private static final String CREATING_FRAGMENT_ICON = "create-fragments.png";
    private static final String STOP_CREATING_FRAGMENTS_ICON = "stop-creating-fragments.png";

    private static final String MOVE_ELEMENTS_ICON = "move-elements.png";
    private static final String STOP_MOVING_ELEMENTS_ICON = "stop-moving-elements.png";
    private static final String MOVE_FRAGMENT_ICON = "move-fragments.png";
    private static final String STOP_MOVING_FRAGMENTS_ICON = "stop-moving-fragments.png";
    private static final String MOVE_MODEL_ICON = "move-model.png";
    private static final String STOP_MOVING_MODEL_ICON = "stop-moving-model.png";
    private static final String DELETE_ICON = "delete-elements-blue.png";
    private static final String STOP_DELETING_ICON = "stop-deleting-elements-blue.png";

    private static final String INITIALIZER_ICON = "initialize-model.png";
    private static final String STOP_INITIALING_ICON = "stop-initializing-model.png";

    private static final String SIMULATION_MODE_ICON = "exec_mode.png";
    private static final String START_SIMULATION_ICON = "start_running.gif";
    private static final String STOP_SIMULATION_ICON = "stop-simulation.png";
    private static final String PAUSE_SIMULATION_ICON = "pause-simulation.png";
    private static final String RESUME_SIMULATION_ICON = "resume-simulation.png";
    private static final String SINGLE_SIMULATION_STEP_ICON = "step-simulation.png";
    private static final String RESET_SIMULATION_ICON = "reset_orange.png";

    private static final String SHOW_SETUP_ICON = "show-setup.png";
    private static final String CLOSE_SETUP_ICON = "close-setup.png";
    private static final String HIDE_SETUP_ICON = "hide-setup.png";
    private static final String SHOW_HELP_CONTENT_ICON = "show-doc.png";
    private static final String CLOSE_HELP_CONTENT_ICON = "close-doc.png";
    private static final String HIDE_HELP_CONTENT_ICON = "hide-doc.png";

    private static final String CANCEL_OPERATION_ICON = "stop-operation.png";

    private static DseMenuAndToolbarBuilder dseMenuAndToolbarBuilder;

    /**
     * @return
     */
    public static synchronized DseMenuAndToolbarBuilder getInstance() {
        if (dseMenuAndToolbarBuilder == null) {
            dseMenuAndToolbarBuilder = new DseMenuAndToolbarBuilder();
        }
        return dseMenuAndToolbarBuilder;
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

    private static final Map<String, AdfBasicAction> menuItemLabelToAction = new HashMap();
    private static final Map<String, AbstractButton> menuItemLabelToToolbarButton = new HashMap();
    private static final AdfMenuActionListener GRAPH_VIEW_MENU_ACTION_LISTENER =
            AppController.getInstanceToGetMenuListener().getMenuListener();
    private static final AdfMenuActionListener MATRIX_VIEW_MENU_ACTION_LISTENER =
            MclnMatrixViewController.getInstance().getMenuListener();

    // The group is used to group Project menu operations
    private final AdfEnableDisableAllActionGroup projectMenuActionGroup = AppController.getEnableDisableAllActionGroup();

    // The group is used to group Demo menu operations
    private final AdfEnableDisableAllActionGroup demoMenuActionGroup = AppController.getDemoMenuActionGroup();

    // The group is used to group Setup, Help and Init Assistant menu operations
    private static final AdfJButtonActionGroup ADF_J_BUTTON_ACTION_GROUP = new AdfJButtonActionGroup();

    // The group is used to group Editing menu operations
    private static final AdfToggleButtonActionGroup ADF_TOGGLE_BUTTON_ACTION_GROUP = AppController.getToggleButtonActionGroup();

    private AdfMenuBar designerViewMenuBar;
    private AdfMenuBar matrixViewMenuBar;


    /**
     *
     */
    private DseMenuAndToolbarBuilder() {

    }


    public static final AdfBasicAction getUIAction(String title) {
        AdfBasicAction action = menuItemLabelToAction.get(title);
        return action;
    }

    public static final AbstractButton getSimulationControlButton(String title) {
        AbstractButton button = menuItemLabelToToolbarButton.get(title);
        return button;
    }

    /**
     * @return
     */
    public AdfMenuBar buildMenuBar() {
        return buildMenuBur(GRAPH_VIEW_MENU_ACTION_LISTENER, ICON_CLASS_PATH_PREFIX, menuItemLabelToAction);
    }

    public AdfMenuBar buildMclnMatrixViewMenuBar() {
        return buildMclnMatrixViewMenuBar(MATRIX_VIEW_MENU_ACTION_LISTENER, ICON_CLASS_PATH_PREFIX, menuItemLabelToAction);
    }

    /**
     * @param adfMenuActionListener
     * @param iconClassPath
     * @param menuLabelToActionMap
     * @return
     */
    public AdfMenuBar buildMenuBur(AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                   Map<String, AdfBasicAction> menuLabelToActionMap) {
        if(designerViewMenuBar != null){
            return designerViewMenuBar;
        }

        AdfMenuBar menuBar = new AdfMenuBar(adfMenuActionListener, iconClassPath, menuLabelToActionMap);

        // Project menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_PROJECT);
        menuBar.addMenuItem(AppController.MENU_ITEM_NEW_PROJECT, projectMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_CHANGE_ATTRIBUTES, RENAME_PROJECT_ICON, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_RENAME_MODEL, RENAME_MODEL_ICON, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_CLEAR_DESIGN_SPACE, CLEAR_DESIGN_SPACE_ICON, projectMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_SAVE, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_SAVE_AS, SAVE_PROJECT_AS_ICON, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_OPEN_PROJECT, OPEN_PROJECT_ICON, projectMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_SHOW_PRINT_CONTENT, SHOW_PRINT_PREVIEW_ICON, projectMenuActionGroup);
//        menu.addToggleMenuItem(AppController.MENU_ITEM_HIDE_PRINT_CONTENT, HIDE_PRINT_PREVIEW_ICON, true);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_EXIT, projectMenuActionGroup);

        // Project View Menu
        menuBar.addAdfAppMenu(AppController.MENU_PROJECT_VIEW);
        menuBar.addMenuItem(AppController.MENU_ITEM_GRAPH_VIEW, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_MATRIX_VIEW, projectMenuActionGroup);

        // Demo menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_EXAMPLES);

        menuBar.addMenuItem(AppController.MENU_BASIC_BLOCK, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_LOGICAL_BLOCKS, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_TWO_RULES, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_THREE_RULES, demoMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_SINGLE_PROPERTY, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_TRIGGER, demoMenuActionGroup);
//        adfMenuBar.addMenuItem(AppController.MENU_TERNARY_COUNTER, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_MUT_EXCL, demoMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_DIN_PHIL, demoMenuActionGroup);

        // Development menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_DEVELOPMENT_MODE);
        menuBar.addMenuItem(AppController.MENU_ITEM_SET_DEVELOPMENT_MODE, DEVELOPMENT_MODE_ICON);

        // creation items

        menuBar.addAdfAppMenu(AppController.MENU_ITEM_CREATION_MODE, false);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_CREATE_PROPERTIES,
                CREATING_PROPERTIES_ICON, STOP_CREATING_PROPERTIES_ICON);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_CREATE_CONDITIONS,
                CREATING_CONDITIONS_ICON, STOP_CREATING_CONDITIONS_ICON);

        menuBar.addToggleMenuItem(AppController.MENU_ITEM_CREATE_POLYLINE_ARCS,
                CREATING_POLYLINE_ARCS_ICON, STOP_CREATING_POLYLINE_ARCS_ICON);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_CREATE_SPLINE_ARCS,
                CREATING_SPLINE_ARCS_ICON, STOP_CREATING_SPLINE_ARCS_ICON);

        menuBar.addToggleMenuItem(AppController.MENU_ITEM_CREATE_FRAGMENTS,
                CREATING_FRAGMENT_ICON, STOP_CREATING_FRAGMENTS_ICON);

        // I preserve this code because it shows how to use submenu
//        AdfAppMenu arcCreationMenu = AdfAppMenu.create("Create Arcs of Type", adfMenuActionListener, ICON_CLASS_PATH_PREFIX, menuItemLabelToAction);
//        creationMenu.addSubmenu(arcCreationMenu);
//        arcCreationMenu.addToggleMenuItem(AppController.MENU_ITEM_CREATE_SPLINE_ARCS, CREATING_ARCS_ICON, STOP_CREATING_ARCS_ICON, true);
//        arcCreationMenu.addToggleMenuItem(AppController.MENU_ITEM_CREATE_POLYLINE_ARCS, CREATING_ARCS_ICON, STOP_CREATING_ARCS_ICON, true);

        // Modification items
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_MODIFICATION);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_MOVE_ELEMENTS, MOVE_ELEMENTS_ICON, STOP_MOVING_ELEMENTS_ICON);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_MOVE_FRAGMENT, MOVE_FRAGMENT_ICON, STOP_MOVING_FRAGMENTS_ICON);
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_MOVE_MODEL, MOVE_MODEL_ICON, STOP_MOVING_MODEL_ICON);
        menuBar.addMenuSeparator();
        menuBar.addToggleMenuItem(AppController.MENU_ITEM_DELETE_ELEMENT, DELETE_ICON, STOP_DELETING_ICON);

        // Init Assistant item
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_LAUNCH_IA);
        menuBar.addMenuItem(AppController.MENU_ITEM_LAUNCH_IA, INITIALIZER_ICON);

        // Simulation menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_SIMULATION_MODE);
        menuBar.addMenuItem(AppController.MENU_ITEM_SET_SIMULATION_MODE, SIMULATION_MODE_ICON, projectMenuActionGroup);

        // Preferences menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_PREFERENCES);
        menuBar.addMenuItem(AppController.MENU_ITEM_SHOW_SETUP_PANEL, SHOW_SETUP_ICON);

        // Help menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_HELP, true);
        /**
         * Enabling this item will require:
         * 1) to stash and restore current project;
         * 2) to stash and restore current App state
         */
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_RUN_ALL_SHOWS_SEQUENTIALLY, projectMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_HOW_TO_USE_CREATION_OPERATIONS, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_HOW_USE_MODIFICATION_OPERATIONS, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_HOW_TO_RUN_MODEL_SIMULATION, projectMenuActionGroup);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_WHAT_IS_DSDS_DSE, SHOW_HELP_CONTENT_ICON);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_RUNNING_EXAMPLES);
        menuBar.addMenuItem(AppController.MENU_ITEM_WHAT_IS_CREATOR);
        menuBar.addMenuItem(AppController.MENU_ITEM_WHAT_IS_SIM_ENG);
//        adfMenuBar.addMenuItem(AppController.MENU_ITEM_MENU_COMMANDS);
//        adfMenuBar.addMenuSeparator();
//        adfMenuBar.addMenuItem(AppController.MENU_ITEM_WHAT_IS_TD_IG);
//        adfMenuBar.addMenuItem(AppController.MENU_ITEM_WHAT_IS_SD_IG);
        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_ABOUT);

        designerViewMenuBar = menuBar;
        return menuBar;
    }

    /**
     *
     * @param adfMenuActionListener
     * @param iconClassPath
     * @param menuLabelToActionMap
     * @return
     */
    private AdfMenuBar buildMclnMatrixViewMenuBar(AdfMenuActionListener adfMenuActionListener, String iconClassPath,
                                    Map<String, AdfBasicAction> menuLabelToActionMap) {
        if(matrixViewMenuBar != null){
            return matrixViewMenuBar;
        }

        AdfMenuBar menuBar = new AdfMenuBar(adfMenuActionListener, iconClassPath, menuLabelToActionMap);

        // Project menu
        menuBar.addAdfAppMenu(AppController.MENU_ITEM_PROJECT);

        menuBar.addMenuSeparator();
        menuBar.addMenuItem(AppController.MENU_ITEM_EXIT );

        // Project View Menu
        menuBar.addAdfAppMenu(AppController.MENU_PROJECT_VIEW);
        menuBar.addMenuItem(AppController.MENU_ITEM_GRAPH_VIEW, projectMenuActionGroup);
        menuBar.addMenuItem(AppController.MENU_ITEM_MATRIX_VIEW, projectMenuActionGroup);

        matrixViewMenuBar = menuBar;
        return menuBar;
    }

    //
    //   M e n u   T o o l b a r   s t u f f
    //

    public static JToolBar initToolBar() {

        JToolBar toolBar = new JToolBar() {
            @Override
            public Component add(Component comp) {
                if (comp == null) {
                    return comp;
                }
                addImpl(comp, null, -1);
                return comp;
            }
        };
//        AppController.getInstance();

        toolBar.addSeparator();

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_CHANGE_ATTRIBUTES,
                RENAME_PROJECT_ICON, " Rename Project ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null));

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_RENAME_MODEL,
                RENAME_MODEL_ICON, " Rename Model ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null));

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_CLEAR_DESIGN_SPACE,
                CLEAR_DESIGN_SPACE_ICON, " Discard Developed Model ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null));

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_SAVE_AS, SAVE_PROJECT_AS_ICON,
                " Save Project As ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null));

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_OPEN_PROJECT,
                OPEN_PROJECT_ICON, " Open Existing Project ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null));

        if (!TOGGLE_MENU_AND_TOOLBAR_BUTTONS) {

            toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_SHOW_PRINT_CONTENT, SHOW_PRINT_PREVIEW_ICON,
                    " Show the Project's Print Preview Panel ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null, true));

        } else {

            // Print toggle button

//            ItemListener printButtonItemListener = (e) -> {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    AppController.getInstance().onShowPrintPreviewContent();
//                } else {
//                    AppController.getInstance().onHidePrintPreviewContent();
//                }
//            };

//        printToggleButton = createToolbarToggleIconJButton(SHOW_PRINT_PREVIEW_ICON, HIDE_PRINT_PREVIEW_ICON,
//                AppController.MENU_ITEM_SHOW_PRINT_CONTENT, AppController.MENU_ITEM_SHOW_PRINT_CONTENT,
//                false, true, " Print project ", printButtonItemListener);
//        toolBar.add(printToggleButton);
        }

        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();

        //
        //    E D I T   M O D E   B U T T O N S
        //

        AbstractButton tbbSetEditMode = makeMenuAndToolbarButton(AppController.MENU_ITEM_SET_DEVELOPMENT_MODE,
                DEVELOPMENT_MODE_ICON, " Enable Development Mode ", true, GRAPH_VIEW_MENU_ACTION_LISTENER, null);
        toolBar.add(tbbSetEditMode);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_SET_DEVELOPMENT_MODE, tbbSetEditMode);

        toolBar.addSeparator();

        AbstractButton tbbCreateProperty = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_CREATE_PROPERTIES,
                CREATING_PROPERTIES_ICON, STOP_CREATING_PROPERTIES_ICON, " Creating Properties ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbCreateProperty);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_CREATE_PROPERTIES, tbbCreateProperty);

        AbstractButton tbbCreateCondition = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_CREATE_CONDITIONS,
                CREATING_CONDITIONS_ICON, STOP_CREATING_CONDITIONS_ICON, " Creating Conditions ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbCreateCondition);

        toolBar.addSeparator();

        AbstractButton tbbCreatePolylineArc = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_CREATE_POLYLINE_ARCS,
                CREATING_POLYLINE_ARCS_ICON, STOP_CREATING_POLYLINE_ARCS_ICON, " Creating Polyline Arcs ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbCreatePolylineArc);

        AbstractButton tbbCreateSplineArc = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_CREATE_SPLINE_ARCS,
                CREATING_SPLINE_ARCS_ICON, STOP_CREATING_SPLINE_ARCS_ICON, " Creating Spline Arcs ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbCreateSplineArc);

        toolBar.addSeparator();

        AbstractButton tbbCreateFragment = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_CREATE_FRAGMENTS,
                CREATING_FRAGMENT_ICON, STOP_CREATING_FRAGMENTS_ICON, " Creating Fragments ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
//        AbstractButton tbbCreateFragment = makeMenuAndToolbarButton(CREATING_FRAGMENT_ICON, AppController.MENU_ITEM_CREATE_FRAGMENTS,
//                " Creating fragments ", false, menuActionListener);
        toolBar.add(tbbCreateFragment);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_CREATE_FRAGMENTS, tbbCreateFragment);

        toolBar.addSeparator();

        AbstractButton tbbMoveElements = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_MOVE_ELEMENTS,
                MOVE_ELEMENTS_ICON, STOP_MOVING_ELEMENTS_ICON, " Move the Model's Selected Elements ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbMoveElements);

        AbstractButton tbbMoveFragment = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_MOVE_FRAGMENT,
                MOVE_FRAGMENT_ICON, STOP_MOVING_FRAGMENTS_ICON, " Move the Model's Selected Fragment ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbMoveFragment);

        AbstractButton tbbMoveModel = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_MOVE_MODEL,
                MOVE_MODEL_ICON, STOP_MOVING_MODEL_ICON, " Move the Entire Model ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
        toolBar.add(tbbMoveModel);


        //        AbstractButton tbbDelete = makeMenuAndToolbarButton(DELETE_ICON, AppController.MENU_ITEM_DELETE_ELEMENT,
//                " Delete selected element ", true, menuActionListener);
        AbstractButton tbbDelete = makeMenuAndToolbarToggleButton(AppController.MENU_ITEM_DELETE_ELEMENT,
                DELETE_ICON, STOP_DELETING_ICON, " Delete Selected Element ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_TOGGLE_BUTTON_ACTION_GROUP);
//        tbbDelete.setEnabled(true);
        toolBar.add(tbbDelete);

        toolBar.addSeparator();

        //
        //    I N I T I A L I Z A T I O N   B U T T O N
        //

        AbstractButton tbbInitAssistant = makeMenuAndToolbarButton(AppController.MENU_ITEM_LAUNCH_IA,
                INITIALIZER_ICON, " Show Initialization Assistant ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_J_BUTTON_ACTION_GROUP, true);
        toolBar.add(tbbInitAssistant);

        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();

        //
        //    E X E C U T I N     M O D E    B U T T O N S
        //

        AbstractButton tbbSetRunMode = makeMenuAndToolbarButton(AppController.MENU_ITEM_SET_SIMULATION_MODE,
                SIMULATION_MODE_ICON, " Enable Simulation Mode ", false, GRAPH_VIEW_MENU_ACTION_LISTENER, null);
        toolBar.add(tbbSetRunMode);

        toolBar.addSeparator();

        AbstractButton tbbStartExecution = makeSimulationToolbarButton(AppController.MENU_ITEM_START_SIMULATION,
                START_SIMULATION_ICON, " Start simulation ", false, GRAPH_VIEW_MENU_ACTION_LISTENER);
        toolBar.add(tbbStartExecution);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_START_SIMULATION, tbbStartExecution);

        AbstractButton tbbStopExecution = makeSimulationToolbarButton(AppController.MENU_ITEM_STOP_SIMULATION,
                STOP_SIMULATION_ICON, " Stop simulation ", false, GRAPH_VIEW_MENU_ACTION_LISTENER);
        toolBar.add(tbbStopExecution);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_STOP_SIMULATION, tbbStopExecution);

        AbstractButton tbbStepExecution = makeSimulationToolbarButton(AppController.MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP,
                SINGLE_SIMULATION_STEP_ICON, " Perform one simulation step ", false, GRAPH_VIEW_MENU_ACTION_LISTENER);
        toolBar.add(tbbStepExecution);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP, tbbStepExecution);

        AbstractButton tbbResetModel = makeSimulationToolbarButton(AppController.MENU_ITEM_RESET_SIMULATION,
                RESET_SIMULATION_ICON, " Reset to initial state ", false, GRAPH_VIEW_MENU_ACTION_LISTENER);
        toolBar.add(tbbResetModel);
        menuItemLabelToToolbarButton.put(AppController.MENU_ITEM_RESET_SIMULATION, tbbResetModel);

        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.addSeparator();

        //
        //   P R E F E R E N C E S   A N D   H E L P   B U T T O N S
        //

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_SHOW_SETUP_PANEL,
                SHOW_SETUP_ICON, " Show Preferences Setup Panel ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_J_BUTTON_ACTION_GROUP, true));

        toolBar.addSeparator();

        toolBar.add(makeMenuAndToolbarButton(AppController.MENU_ITEM_WHAT_IS_DSDS_DSE,
                SHOW_HELP_CONTENT_ICON, " Show Help Panel ", true,
                GRAPH_VIEW_MENU_ACTION_LISTENER, ADF_J_BUTTON_ACTION_GROUP, true));

        return toolBar;
    }

    /**
     * @param iconName
     * @param menuItem
     * @param tipText
     * @param enabled
     * @param adfMenuActionListener
     * @return
     */
    private static JButton makeSimulationToolbarButton(String menuItem, String iconName,
                                                       String tipText, boolean enabled,
                                                       AdfMenuActionListener adfMenuActionListener) {
        ImageIcon menuIcon = BuildUtils.getImageIcon(ICON_CLASS_PATH_PREFIX + iconName);
        if (menuIcon == null) {
            new Exception("icon " + iconName + " not found").printStackTrace();
            return null;
        }

        JButton toolbarButton = new AdfJButton(menuIcon);
        ImageIcon disabledIcon = BuildUtils.getDisabledImageIcon(menuIcon);
        toolbarButton.setDisabledIcon(disabledIcon);

        toolbarButton.setFocusPainted(false);

        toolbarButton.addActionListener(null);
        toolbarButton.setMargin(new Insets(0, 0, 0, 0));
        toolbarButton.setHorizontalAlignment(SwingConstants.CENTER);
        toolbarButton.setVerticalAlignment(SwingConstants.CENTER);

        AdfBasicAction action = menuItemLabelToAction.get(menuItem);
        if (action == null) {
            // At 03-16-2017 Simulation Control Toolbar Buttons do not have counterpart in menu
            // So, Action is created here
            action = new AdfDefaultButtonAction(null, menuIcon, adfMenuActionListener);
            menuItemLabelToAction.put(menuItem, action);
        }

        toolbarButton.setAction(action);
        toolbarButton.setActionCommand(menuItem);
        toolbarButton.setToolTipText(tipText);

        toolbarButton.setEnabled(enabled);
        return toolbarButton;
    }

    private static AbstractButton makeMenuAndToolbarButton(String menuItem, String iconName,
                                                           String tipText, boolean enabled,
                                                           AdfMenuActionListener adfMenuActionListener,
                                                           AdfJButtonActionGroup toolbarButtonActionGroup) {
        JButton button = createMenuToolbarButton(menuItem, iconName, tipText, enabled,
                adfMenuActionListener, toolbarButtonActionGroup, false);
        return button;
    }

    /**
     * @param iconName
     * @param menuItem
     * @param tipText
     * @param enabled
     * @param adfMenuActionListener
     * @return
     */
    private static AbstractButton makeMenuAndToolbarButton(String menuItem, String iconName,
                                                           String tipText, boolean enabled,
                                                           AdfMenuActionListener adfMenuActionListener,
                                                           AdfJButtonActionGroup toolbarButtonActionGroup,
                                                           boolean addToDseMenuMediator) {
        JButton button = createMenuToolbarButton(menuItem, iconName, tipText, enabled,
                adfMenuActionListener, toolbarButtonActionGroup, addToDseMenuMediator);
        return button;
    }

    /**
     * @param menuItem
     * @param iconName
     * @param selectedIconName
     * @param tipText
     * @param enabled
     * @param adfMenuActionListener
     * @param adfToggleButtonActionGroup
     * @return
     */
    private static AbstractButton makeMenuAndToolbarToggleButton(String menuItem,
                                                                 String iconName, String selectedIconName,
                                                                 String tipText, boolean enabled,
                                                                 AdfMenuActionListener adfMenuActionListener,
                                                                 AdfToggleButtonActionGroup adfToggleButtonActionGroup) {
        AbstractButton toolbarButton = createMenuAndToolbarToggleButton(menuItem, iconName, selectedIconName,
                tipText, enabled, adfMenuActionListener, adfToggleButtonActionGroup);
        return toolbarButton;
    }

    /**
     * @param menuItem
     * @param iconName
     * @param secondIconName
     * @param tipText
     * @param enabled
     * @param adfMenuActionListener
     * @param adfToggleButtonActionGroup
     * @return
     */
    private static Color borderColor = new Color(221, 221, 221);

    private static AbstractButton createMenuAndToolbarToggleButton(String menuItem,
                                                                   String iconName, String secondIconName,
                                                                   String tipText, boolean enabled,
                                                                   AdfMenuActionListener adfMenuActionListener,
                                                                   AdfToggleButtonActionGroup adfToggleButtonActionGroup) {

        ImageIcon mainIcon = BuildUtils.getImageIcon(ICON_CLASS_PATH_PREFIX + iconName);
        ImageIcon secondIcon = BuildUtils.getImageIcon(ICON_CLASS_PATH_PREFIX + secondIconName);

        if (mainIcon == null || secondIcon == null) {
            new Exception("icon names: " + iconName + ", " + secondIconName +
                    ": one or both icons not found").printStackTrace();
            return null;
        }

        JToggleButton toolbarToggleButton = new AdfToggleButton(mainIcon);
        ImageIcon disabledMainIcon = BuildUtils.getDisabledImageIcon(mainIcon);
        toolbarToggleButton.setDisabledIcon(disabledMainIcon);

        toolbarToggleButton.setSelectedIcon(secondIcon);
        ImageIcon disabledSelectedIcon = BuildUtils.getDisabledImageIcon(secondIcon);
        toolbarToggleButton.setDisabledSelectedIcon(disabledSelectedIcon);

        toolbarToggleButton.setFocusPainted(false);

        toolbarToggleButton.addActionListener(null);
        toolbarToggleButton.setMargin(new Insets(0, 0, 0, 0));
        toolbarToggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toolbarToggleButton.setVerticalAlignment(SwingConstants.CENTER);
        toolbarToggleButton.setContentAreaFilled(false);

        Border comb = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                new MatteBorder(0, 0, 1, 0, borderColor));
        toolbarToggleButton.setBorder(comb);
//        toolbarToggleButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        AdfBasicAction adfBasicAction = menuItemLabelToAction.get(menuItem);
        if (adfBasicAction == null) {
            adfBasicAction = new AdfToggleButtonAction(menuItem, mainIcon, adfMenuActionListener);
            menuItemLabelToAction.put(menuItem, adfBasicAction);
        }
        toolbarToggleButton.setAction(adfBasicAction);
        toolbarToggleButton.setActionCommand(menuItem);
        toolbarToggleButton.setToolTipText(tipText);

        if (adfToggleButtonActionGroup != null) {
            adfToggleButtonActionGroup.registerAction(adfBasicAction);
        }

        adfBasicAction.setEnabled(enabled);
        return toolbarToggleButton;
    }


    /**
     * @param iconName
     * @param menuItem
     * @param tipText
     * @param enabled
     * @param adfMenuActionListener
     * @return
     */
    private static JButton createMenuToolbarButton(String menuItem, String iconName, String tipText,
                                                   boolean enabled, AdfMenuActionListener adfMenuActionListener,
                                                   AdfJButtonActionGroup toolbarButtonActionGroup,
                                                   boolean addToDseMenuMediator) {
        ImageIcon menuIcon = BuildUtils.getImageIcon(ICON_CLASS_PATH_PREFIX + iconName);
        if (menuIcon == null) {
            new Exception("icon " + iconName + " not found").printStackTrace();
            return null;
        }

        JButton toolbarButton = new AdfJButton(menuIcon);
        ImageIcon disabledIcon = BuildUtils.getDisabledImageIcon(menuIcon);
        toolbarButton.setDisabledIcon(disabledIcon);

        toolbarButton.setFocusPainted(false);

        toolbarButton.addActionListener(null);
        toolbarButton.setMargin(new Insets(0, 0, 0, 0));
        toolbarButton.setHorizontalAlignment(SwingConstants.CENTER);
        toolbarButton.setVerticalAlignment(SwingConstants.CENTER);

        AdfBasicAction action = menuItemLabelToAction.get(menuItem);
        if (action == null) {
            action = new AdfJButtonAction(menuItem, adfMenuActionListener);
            menuItemLabelToAction.put(menuItem, action);
        }

        toolbarButton.setAction(action);
        toolbarButton.setActionCommand(menuItem);
        toolbarButton.setToolTipText(tipText);

        action.setEnabled(enabled);

        if (addToDseMenuMediator) {
            DseMenuMediator.registerAction(menuItem, action);
        } else if (toolbarButtonActionGroup != null) {
            toolbarButtonActionGroup.registerAction(action);
        }

        return toolbarButton;
    }

    //
    //   M e n u   A c t i o n
    //

    public static class MenuAndToolbarActionA extends AbstractAction {
        private String menuItemLabel;
        private AbstractButton button;
        private ActionListener menuActionListener;

        /**
         * Creating Action without Icon
         *
         * @param menuItemLabel
         * @param menuActionListener
         */
        public MenuAndToolbarActionA(String menuItemLabel, ActionListener menuActionListener) {
            this(null, menuItemLabel, menuActionListener);
        }

        /**
         * Creating Action with Icon
         *
         * @param menuItemLabel
         * @param icon
         */
        public MenuAndToolbarActionA(String menuItemLabel, Icon icon, ActionListener menuActionListener) {
            super(menuItemLabel, icon);
            this.menuActionListener = menuActionListener;
        }

        MenuAndToolbarActionA(JButton button, String menuItemLabel, ActionListener menuActionListener) {
            super(menuItemLabel);
            this.menuItemLabel = menuItemLabel;
            this.button = button;
            this.menuActionListener = menuActionListener;
        }

        private void setButton(AbstractButton button) {
            this.button = button;
        }

        public void setSelected() {
            if (button != null) {
                AdfTestToggleButton adfToggleIconJButton = (AdfTestToggleButton) button;
                boolean currentStatus = adfToggleIconJButton.isSelected();
                if (!currentStatus) {
                    return;
                }
                adfToggleIconJButton.setSelected(!currentStatus);
            }
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (menuActionListener == null) {
                return;
            }
            menuActionListener.actionPerformed(ae);
            if (button != null && button instanceof AdfTestToggleButton) {
                AdfTestToggleButton adfToggleIconJButton = (AdfTestToggleButton) button;
                boolean currentStatus = adfToggleIconJButton.isSelected();
                adfToggleIconJButton.setSelected(!currentStatus);
            }
        }

        @Override
        public void setEnabled(boolean newValue) {
            super.setEnabled(newValue);
            if (button != null) {
                button.setEnabled(newValue);
            }
        }
    }
}
