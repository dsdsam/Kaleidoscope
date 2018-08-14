package dsdsse.app;

import adf.menu.*;
import adf.preferences.GroupChangeListener;
import adf.utils.BuildUtils;
import dsdsse.animation.*;
import dsdsse.designspace.*;
import dsdsse.designspace.controller.DesignSpaceController;
import dsdsse.designspace.executor.MclnSimulationController;
import dsdsse.designspace.initializer.InitAssistantInterface;
import dsdsse.graphview.DesignerArcView;
import dsdsse.graphview.MclnGraphViewEditor;
import dsdsse.help.HelpPanelHolder;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.preferences.GroupID;
import dsdsse.preferences.PreferencesSetupPanel;
import dsdsse.splash.DsdsseSplash;
import dsdsse.welcome.WelcomePanel;
import mcln.model.MclnModel;
import mcln.model.MclnProject;
import mcln.model.ProjectAttributes;
import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:46:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppController {

    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    public static final String DEMO_PROJECT_BASIC_BLOCK = "Basic Block";
    public static final String DEMO_PROJECT_LOGICAL_BLOCKS = "Logical Blocks";
    public static final String DEMO_PROJECT_TWO_RULES = "Two Rules";
    public static final String DEMO_PROJECT_THREE_RULES = "Three Rules";
    public static final String DEMO_PROJECT_TRIGGER = "Trigger";
    public static final String DEMO_SINGLE_PROPERTY = "Single Property";
    public static final String DEMO_TERNARY_COUNTER = "Ternary Counter";
    public static final String DEMO_PROJECT_MUTUAL_EXCLUSION = "Mutual Exclusion";
    public static final String DEMO_PROJECT_DINING_PHILOSOPHERS = "Dining Philosophers";


    // Project Menu
    public static final String MENU_ITEM_PROJECT = "  Project  ";
    public static final String MENU_ITEM_NEW_PROJECT = "Create New Project";
    public static final String MENU_ITEM_CHANGE_ATTRIBUTES = "Change Project Attributes";
    public static final String MENU_ITEM_RENAME_MODEL = "Rename Model";
    public static final String MENU_ITEM_CLEAR_DESIGN_SPACE = "Clear Design Space";
    public static final String MENU_ITEM_OPEN_PROJECT = "Retrieve Project";
    public static final String MENU_ITEM_SAVE = "Save Project";
    public static final String MENU_ITEM_SAVE_AS = "Save Project As";

    public static final String MENU_ITEM_SHOW_PRINT_CONTENT = "Print Preview Panel";
    public static final String MENU_ITEM_HIDE_PRINT_CONTENT = "Hide Print Content";
    public static final String MENU_ITEM_EXIT = "Exit";

    // Project View Menu
    public static final String MENU_PROJECT_VIEW = "  View  ";
    public static final String MENU_ITEM_GRAPH_VIEW = "Graph View";
    public static final String MENU_ITEM_MATRIX_VIEW = "Matrix View";

    // Examples Menu
    public static final String MENU_ITEM_EXAMPLES = "  Examples  ";
    public static final String MENU_BASIC_BLOCK = DEMO_PROJECT_BASIC_BLOCK;
    public static final String MENU_LOGICAL_BLOCKS = DEMO_PROJECT_LOGICAL_BLOCKS;
    public static final String MENU_TWO_RULES = DEMO_PROJECT_TWO_RULES;
    public static final String MENU_THREE_RULES = DEMO_PROJECT_THREE_RULES;
    public static final String MENU_TRIGGER = DEMO_PROJECT_TRIGGER;
    public static final String MENU_SINGLE_PROPERTY = DEMO_SINGLE_PROPERTY;
    public static final String MENU_TERNARY_COUNTER = DEMO_TERNARY_COUNTER;
    public static final String MENU_MUT_EXCL = DEMO_PROJECT_MUTUAL_EXCLUSION;
    public static final String MENU_DIN_PHIL = DEMO_PROJECT_DINING_PHILOSOPHERS;

    // Create Menu
    public static final String MENU_ITEM_DEVELOPMENT_MODE = "  Development  ";
    public static final String MENU_ITEM_SET_DEVELOPMENT_MODE = "Set Development Mode";
    public static final String MENU_ITEM_CREATION_MODE = "  Creation  ";
    public static final String MENU_ITEM_CREATE_PROPERTIES = "Property Nodes";
    public static final String MENU_ITEM_CREATE_CONDITIONS = "Condition Nodes";
    public static final String MENU_ITEM_CREATE_POLYLINE_ARCS = "Polyline Arcs";
    public static final String MENU_ITEM_CREATE_SPLINE_ARCS = "Spline Arcs";
    public static final String MENU_ITEM_CREATE_FRAGMENTS = "Simple Fragments";

    // Modify Menu
    public static final String MENU_ITEM_MODIFICATION = "  Modification  ";
    public static final String MENU_ITEM_MOVE_ELEMENTS = "Move Model Elements";
    public static final String MENU_ITEM_MOVE_FRAGMENT = "Move Model Fragments";
    public static final String MENU_ITEM_MOVE_MODEL = "Move Entire Model";
    public static final String MENU_ITEM_DELETE_ELEMENT = "Delete Model Elements";

    public static final String MENU_ITEM_LAUNCH_IA = "  Initialization  ";

    // Simulation Menu
    public static final String MENU_ITEM_SIMULATION_MODE = "  Simulation  ";
    public static final String MENU_ITEM_SET_SIMULATION_MODE = "Set Simulation Mode";
    public static final String MENU_ITEM_START_SIMULATION = "Start Simulation";
    public static final String MENU_ITEM_STOP_SIMULATION = "Stop Simulation";
    public static final String MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP = "Execute One Simulation Step";
    public static final String MENU_ITEM_RESET_SIMULATION = "Reset Simulation";

    // Preferences Menu
    public static final String MENU_ITEM_PREFERENCES = "  Preferences  ";
    public static final String MENU_ITEM_SHOW_SETUP_PANEL = "Preferences Setup Panel";
    public static final String MENU_ITEM_HIDE_SETUP_PANEL = "Hide Preferences Setup";

    // Help Menu
    public static final String MENU_ITEM_HELP = "  Presentation Shows & Help  ";
    //    public static final String MENU_ITEM_HELP_PANEL_BUTTON = "Help Toolbar Button";
    public static final String MENU_ITEM_RUN_ALL_SHOWS_SEQUENTIALLY = "Run All Presentation Shows Sequentially";
    public static final String MENU_ITEM_HOW_TO_USE_CREATION_OPERATIONS = "How to Use Model Creation Operations (Show)";
    public static final String MENU_ITEM_HOW_USE_MODIFICATION_OPERATIONS = "How to Use Model Modification Operations (Show)";
    public static final String MENU_ITEM_HOW_TO_RUN_MODEL_SIMULATION = "How to Run Model Simulation (Show)";
    //    public static final String MENU_ITEM_SHOW_HELP_PANEL = "Help Contents Panel";
    public static final String MENU_ITEM_WHAT_IS_DSDS_DSE = "What is DSDS DSE";
    public static final String MENU_ITEM_RUNNING_EXAMPLES = "Running Model Examples";
    public static final String MENU_ITEM_WHAT_IS_CREATOR = "The Graphical Model Creator";
    public static final String MENU_ITEM_SIMPLE_PROJECT = "Creating Simple Project";
    public static final String MENU_ITEM_WHAT_IS_SIM_ENG = "The Simulating Engine";
    public static final String MENU_ITEM_MENU_COMMANDS = "All Menu and Toolbar Commands";
    public static final String MENU_ITEM_WHAT_IS_TD_IG = "The Time Driven Input Generator";
    public static final String MENU_ITEM_WHAT_IS_SD_IG = "The State Driven Input Generator";

    public static final String MENU_ITEM_ABOUT = "About DSDS DSE";

    private static final String DEMO_PROJECT_NAME = "Demo Project";

    private static final AppController appController = new AppController();

    private static String MESSAGE_HEADER = "Menu & Toolbar Controller";

    private static final String CAN_MODEL_INITIALIZATION_TO_BE_CANCELED_MESSAGE =
            "<html><div style=\"text-align:center; \">" +
                    "Selected menu operation requires to cancel model initialization.&nbsp;&nbsp;<br>" +
                    "Is it OK to cancel initialization ?" +
                    "</div></html>";

    private static final String CAN_MODEL_EDITING_TO_BE_CANCELED_MESSAGE =
            "<html><div style=\"text-align:center; \">" +
                    "Selected menu operation requires to cancel current editing operation.&nbsp;&nbsp;<br>" +
                    "Is it OK to cancel current editing operation ?" +
                    "</div></html>";

    private static final String CANNOT_START_PRESENTATION_MESSAGE =
            "<html><div style=\"text-align:center; \">" +
                    "Cannot launch required presentation show as current operation cannot be interrupted.&nbsp;&nbsp;<br>" +
                    "</div></html>";

    //
    //
    //

    private static final StringBuilder stringBuilder = new StringBuilder();

    private static final String SELECTED_OPERATION_IS_TO_DISCARD_CURRENT_PROJECT =
            " Selected operation is to discard current Project";
    private static final String SELECTED_OPERATION_IS_TO_CLEAR_CURRENT_MODEL =
            " Selected operation is to clear current Model";
    private static final String UNSAVED_INITIALIZATIONS_QUESTION =
            "There are unsaved initialization(s) in the Project. Do you want to save the changes ?   ";
    private static final String STRUCTURE_CHANGED_QUESTION =
            "The Project structure was changed. Do you want to save the changes ?   ";
    private static final String MODEL_OR_FRAGMENT_MOVED_QUESTION =
            "The Model or its fragment location was changed. Do you want to save the change ?   ";
    private static final String[] SAVE_PROJECT_OPTIONS = {"Save Project", "Discard Project", "Cancel Operation"};
    private static final int USER_CHOICE_DIALOG_CLOSED = -1;
    private static final int USER_CHOICE_SAVE_PROJECT = 0;
    private static final int USER_CHOICE_DISCARD_PROJECT = 1;
    private static final int USER_CHOICE_CANCEL_OPERATION = 2;

    private static final GroupChangeListener groupChangeListener = (preference) -> {
        boolean status = DsdsseUserPreference.isInitAssistantEmbedded();
    };

    static {
        DsdsseUserPreference.getInstance().addGroupChangeListener(
                DsdsseUserPreference.PREF_IA_EMBEDDED_KEY, GroupID.EMBEDDED_IA, groupChangeListener);
    }

    public static final JMenu findMenuBarItemByName(String menuText) {
        return appController.menuBar.findMenuBarItemByName(menuText);
    }

    public static final JMenuItem findSubMenuItemByName(String menuItemText, String subMenuItemText) {
        return appController.menuBar.findSubMenuItemByName(menuItemText, subMenuItemText);
    }

    /**
     *
     */
    static final void createInstance(AdfMenuBar menuBar) {
        if (appController.menuBar != null) {
            logger.severe("AppController instance already initialized !!!");
            return;
        }
        appController.menuBar = menuBar;
        appController.appStateModel = AppStateModel.getInstance();
        appController.mclnGraphViewEditor = DsdsseEnvironment.getMclnGraphViewEditor();
        appController.designSpaceView = DsdsseEnvironment.getDesignSpaceView();

        // default model is set as Model Creation
        appController.setDevelopmentMode();
//        appController.setSimulationMode();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(appController.keyEventDispatcher);
    }

    public static AppController getInstanceToGetMenuListener() {
        return appController;
    }

    public static AppController getInstance() {
        if (appController.menuBar == null) {
            logger.severe("AppController instance not yet initialized !!!");
        }
        return appController;
    }

    /**
     * @param iconName
     * @param tipText
     * @param enabled
     * @param menuActionListener
     * @return
     */
    public static JButton createIconButton(String iconName, boolean enabled, String tipText,
                                           ActionListener menuActionListener) {
        JButton button = BuildUtils.createIconButton(DsdsDseDesignSpaceHolderPanel.PREFIX + iconName, enabled, tipText,
                menuActionListener);
        return button;
    }

    /**
     * @param defaultIconName
     * @param selectedIconName
     * @param selected
     * @param enabled
     * @param itemListener
     * @return
     */
    public static JToggleButton createToggleIconButton(String defaultIconName, String selectedIconName,
                                                       boolean selected, boolean enabled, ItemListener itemListener) {
        JToggleButton toggleButton = BuildUtils.createToggleIconButton(DsdsDseDesignSpaceHolderPanel.PREFIX + defaultIconName,
                DsdsDseDesignSpaceHolderPanel.PREFIX + selectedIconName, selected, enabled, itemListener);
        return toggleButton;
    }

    private static final AdfEnableDisableAllActionGroup enableDisableAllActionGroup = new AdfEnableDisableAllActionGroup();

    public static final AdfEnableDisableAllActionGroup getEnableDisableAllActionGroup() {
        return enableDisableAllActionGroup;
    }

    private static final AdfEnableDisableAllActionGroup demoMenuActionGroup = new AdfEnableDisableAllActionGroup();

    public static final AdfEnableDisableAllActionGroup getDemoMenuActionGroup() {
        return demoMenuActionGroup;
    }

    private static final AdfToggleButtonActionGroup ADF_TOGGLE_BUTTON_ACTION_GROUP = new AdfToggleButtonActionGroup();

    public static final AdfToggleButtonActionGroup getToggleButtonActionGroup() {
        return ADF_TOGGLE_BUTTON_ACTION_GROUP;
    }

    //
    //   A p p   C o n t r o l l e r   I n s t a n c e
    //

    private AdfMenuBar menuBar;
    private AppStateModel appStateModel;
    private DesignSpaceView designSpaceView;
    private MclnGraphViewEditor mclnGraphViewEditor;

    /**
     * @param title
     * @return AbstractAction
     */
    public static final AdfBasicAction getUIAction(String title) {
        AdfBasicAction action = DseMenuAndToolbarBuilder.getUIAction(title);
        return action;
    }


    //
    //   M e n u   A c t i o n   L i s t e n e r
    //

    private final AdfMenuActionListener adfMenuActionListener = (ae) -> {
        String actionCommand = ae.getActionCommand();
        boolean done = processMenuOrToolbarCommand(actionCommand);
        return done;
    };

    //
    //   K e y   I n p u t   L i s t e n e r
    //

    private int pressedChar = 0;
    private KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED && keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                SwingUtilities.invokeLater(() -> {
                    PresentationRunner.cancelPresentation();
                });
                keyEvent.consume();
                return true;
            }

            // toggling
            // The code toggles view when Ctrl + Key pressed.
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED && keyEvent.isControlDown() && pressedChar == 0) {
                if (keyEvent.getKeyCode() == '+' || keyEvent.getKeyCode() == '=') {
                    pressedChar = keyEvent.getKeyCode();
                    DsdsseUserPreference.toggleProjectSpaceAxesVisibility();
                    designSpaceView.regenerateGraphView();
                } else if (keyEvent.getKeyCode() == '#' || keyEvent.getKeyCode() == '3') {
                    pressedChar = keyEvent.getKeyCode();
                    DsdsseUserPreference.toggleGridVisibility();
                    designSpaceView.regenerateGraphView();
                } else if (keyEvent.getKeyCode() == 'R' || keyEvent.getKeyCode() == 'r') {
                    pressedChar = keyEvent.getKeyCode();
                    DsdsseUserPreference.toggleProjectSpaceDetailsVisible();
                    designSpaceView.regenerateGraphView();
                }
                keyEvent.consume();
                return true;
            }
            // un-toggling
            // The code set view back when
            // a) only Ctrl released,
            // b) only Key released,
            // c) both Ctrl and Key released.
            if ((keyEvent.getID() == KeyEvent.KEY_LAST && !keyEvent.isControlDown() && pressedChar != 0 && keyEvent.getKeyCode() != pressedChar) ||
                    (keyEvent.getID() == KeyEvent.KEY_RELEASED && pressedChar != 0)) {

                if (pressedChar == '+' || pressedChar == '=') {
                    pressedChar = 0;
                    DsdsseUserPreference.toggleProjectSpaceAxesVisibility();
                    designSpaceView.regenerateGraphView();
                } else if (pressedChar == '#' || pressedChar == '3') {
                    pressedChar = 0;
                    DsdsseUserPreference.toggleGridVisibility();
                    designSpaceView.regenerateGraphView();

                } else if (pressedChar == 'R' || pressedChar == 'r') {
                    pressedChar = 0;
                    DsdsseUserPreference.toggleProjectSpaceDetailsVisible();
                    designSpaceView.regenerateGraphView();
                }
                keyEvent.consume();
                return true;
            }
            return false;
        }
    };

    //
    //  A p p   C o n t r o l l e r   I n s t a n c e
    //

    private AppController() {
    }

    AdfMenuActionListener getMenuListener() {
        return adfMenuActionListener;
    }

    private String lastMenuCommand;

    /*
        Basic Menu Disabling/Enabling riles.

        The application may run in two modes: Development and Simulation
        Up on start up application is set to Development mode
        There are: Print, Setup Preferences and Help that ideally should
        be always available to open assisting panels

         When in Development mode:
         1) Set Development Mode menu item and toolbar button is disabled.
         2) Set Simulation Mode menu item and button enabled.
         3) All Development items and buttons are enabled, and all Simulation buttons are disabled.

         There are three type of Menu Item and corresponding toolbar buttons:
         Enable Disable Button, Toggle Button and regular Java JButton.

         Enable Disable Button.
         1) When Menu item or corresponding toolbar button is pushed the button gets disabled.
         2) In addition to pushing button the button can be enabled or disabled from other
            application components.
         3) If the button is a member of a group of similar buttons, then push on some other button
             of the group will enabled pushed button.

         Unlike Enable Disable Button the Selectable Toggle Button, can be in either Unselected or Selected.
         This button works as Radio Button.
         The initial state is Unselected. When the button is pushed it is considered Selected.
         If Selected Button is clicked again it became Unselected again.
         Several Selectable Toggle Button may constitute a group.
         If some unselected button of the group is clicked and there is some selected button in the group
         The unselected button became selected and selected button became unselected.
         In addition selected button may be set unselected by other application components
         Some or all buttons in the group can be disabled and then again enabled by application components.

         Enable Disable Buttons are used to select menu item and start operation.
         When pushed the button became disabled, and when the operation is complete the button became enabled again.
         The is used in Project and Demo menu and as Set Development Mode and Set Simulation Mode.
         Also as Preferences Setup, Help and Init Assistant button.

         Selectable Toggle Button is used to set Development operation.
     */

    /**
     * @param cmd
     */
    private boolean processMenuOrToolbarCommand(String cmd) {

        // DestroyWelcomePanel if Open
        WelcomePanel.destroyWelcomePanel();

        lastMenuCommand = cmd;
        if (MENU_ITEM_NEW_PROJECT.equals(cmd)) {

            unselectCreationOperation();

            boolean projectSavedOrDiscarded =
                    saveOrDiscardProjectIfModified(SELECTED_OPERATION_IS_TO_DISCARD_CURRENT_PROJECT);
            if (!projectSavedOrDiscarded) {
                return true;
            }

            setDevelopmentMode();
            MclnProject mclnProject = DesignSpaceModel.getInstance().onCreateNewEmptyMclnProject();
            if (mclnProject == null) {
                // Project attributes not specified
                return true;
            }
            DesignSpaceModel.getInstance().resetMclnProject(mclnProject);
            MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
            MclnSimulationController.getInstance().setMclnModel(currentMclnModel);

        } else if (MENU_ITEM_CHANGE_ATTRIBUTES.equals(cmd)) {

            unselectCreationOperation();
            setDevelopmentMode();
            ProjectAttributes projectAttributes = DesignSpaceModel.getInstance().onChangeProjectAttributes(MclnProject.getInstance());
            if (projectAttributes == null) {
                // Project attributes not specified
                return true;
            }
            DesignSpaceModel.getInstance().resetMclnProjectUpOnAttributesModified(projectAttributes.getCopy());

        } else if (MENU_ITEM_RENAME_MODEL.equals(cmd)) {
            // finishing currently active operation
            unselectCreationOperation();

            DesignSpaceModel.getInstance().onRenameMclnModel();

        } else if (MENU_ITEM_CLEAR_DESIGN_SPACE.equals(cmd)) {

            clearDesignSpace();

        } else if (MENU_ITEM_OPEN_PROJECT.equals(cmd)) {

            unselectCreationOperation();

            boolean projectSavedOrDiscarded =
                    saveOrDiscardProjectIfModified(SELECTED_OPERATION_IS_TO_DISCARD_CURRENT_PROJECT);
            if (!projectSavedOrDiscarded) {
                return true;
            }

            setDevelopmentMode();
            DesignSpaceController.getInstance().retrieveMclnProject();

        } else if (MENU_ITEM_SAVE.equals(cmd)) {

            unselectCreationOperation();

            if (isInSimulationMode()) { // This might be used is this item is allowed in simulation mode
                MclnSimulationController.getInstance().setSimulationPaused();
                setSimulationMenuItemsAndButtonEnabled(false);
            } else {
                // finishing currently active operation
                unselectCreationOperation();
            }
            DesignSpaceController.getInstance().saveProject();
            if (isInSimulationMode()) {
                MclnSimulationController.getInstance().setSimulationResumed();
                restoreSimulationMenuItemsAndButtonEnabled();
            }

        } else if (MENU_ITEM_SAVE_AS.equals(cmd)) {

            unselectCreationOperation();

            if (isInSimulationMode()) {
                MclnSimulationController.getInstance().setSimulationPaused();
                setSimulationMenuItemsAndButtonEnabled(false);
            } else {
                // finishing currently active operation
                unselectCreationOperation();
            }
            DesignSpaceController.getInstance().saveProjectAs();

            if (isInSimulationMode()) {
                MclnSimulationController.getInstance().setSimulationResumed();
                restoreSimulationMenuItemsAndButtonEnabled();
            }

        } else if (MENU_ITEM_SHOW_PRINT_CONTENT.equals(cmd)) {


            return openPrintPreviewContent();

        } else if (MENU_ITEM_EXIT.equals(cmd)) {

            // Condition: Current editing operation must be canceled
            unselectCreationOperation();

            userClosesApplication();

        } else if (MENU_ITEM_GRAPH_VIEW.equals(cmd) || MENU_ITEM_MATRIX_VIEW.equals(cmd)) {
            switchToSelectedView(cmd);
//            switchToSelectedApplicationView(cmd);
        } else

            //
            //  E x a m p l e s
            //

            if (MENU_BASIC_BLOCK.equals(cmd) || MENU_LOGICAL_BLOCKS.equals(cmd) ||
                    MENU_TWO_RULES.equals(cmd) || MENU_THREE_RULES.equals(cmd) ||
                    MENU_TRIGGER.equals(cmd) || MENU_SINGLE_PROPERTY.equals(cmd) ||
                    MENU_TERNARY_COUNTER.equals(cmd) ||
                    MENU_MUT_EXCL.equals(cmd) || MENU_DIN_PHIL.equals(cmd)
                    ) {

                unselectCreationOperation();

                boolean projectSavedOrDiscarded =
                        saveOrDiscardProjectIfModified(SELECTED_OPERATION_IS_TO_DISCARD_CURRENT_PROJECT);
                if (!projectSavedOrDiscarded) {
                    return true;
                }

                // finishing currently active operation
                stopSimulationProcessAndClearSimulationPresentation();

//                setSimulationMode();
                if (appStateModel.isSimulationMode()) {
                    setDevelopmentMode();
                }

                DesignSpaceController.getInstance().onCreateDemoProject(cmd);
                // This call will create project backup as for demo project
                // at the moment the project is created, the model is yet empty.
                MclnProject.getInstance().backupProject();
                return true;

            } else

                //
                //   C r e a t i o n   M o d e   C o m m a n d s
                //

                if (MENU_ITEM_SET_DEVELOPMENT_MODE.equals(cmd)) {

//                    if (InitAssistantInterface.isInitAssistantUpAndRunning()) {
//                        if (!shutdownCurrentEditingOperationOrInitAssistantIfPossible()) {
//                            return false;
//                        }
//                        appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
//                    }
                    setDevelopmentMode();
                    return false;

                } else if (MENU_ITEM_CREATE_PROPERTIES.equals(cmd)) {
                    if (!isRequestedOperationAlreadySet(AppStateModel.Operation.CREATE_PROPERTIES)) {
                        appStateModel.setNewEditingOperation(AppStateModel.Operation.CREATE_PROPERTIES,
                                AppStateModel.OperationStep.PLACE_NODE);
                    } else {
                        AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
                    }

                } else if (MENU_ITEM_CREATE_CONDITIONS.equals(cmd)) {
                    if (!isRequestedOperationAlreadySet(AppStateModel.Operation.CREATE_CONDITIONS)) {
                        appStateModel.setNewEditingOperation(AppStateModel.Operation.CREATE_CONDITIONS,
                                AppStateModel.OperationStep.PLACE_CONDITION);
                    } else {
                        appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
                    }

                } else if (MENU_ITEM_CREATE_POLYLINE_ARCS.equals(cmd)) {
                    // Called twice when icon clicked to select or unselect.
                    // But it is not called when another icon of the group is clicked.
                    // Also is not called when icon is unselected via action.
                    setCreatingPolylineArcs();

                } else if (MENU_ITEM_CREATE_SPLINE_ARCS.equals(cmd)) {
                    // Called twice when icon clicked to select or unselect.
                    // But it is not called when another icon of the group is clicked.
                    // Also is not called when icon is unselected via action.
                    setCreatingSplineArcs();

                } else if (MENU_ITEM_CREATE_FRAGMENTS.equals(cmd)) {
                    // Called twice when icon clicked to select or unselect.
                    // But it is not called when another icon of the group is clicked.
                    // Also is not called when icon is unselected via action.
                    toggleFragmentCreation();

                } else

                    //  M o v e   M o d e l   O p e r a t i o n s

                    if (MENU_ITEM_MOVE_ELEMENTS.equals(cmd)) {
                        toggleMoveElements();

                    } else if (MENU_ITEM_MOVE_FRAGMENT.equals(cmd)) {
                        toggleMoveFragment();

                    } else if (MENU_ITEM_MOVE_MODEL.equals(cmd)) {
                        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.MOVE_MODEL)) {
                            appStateModel.setNewEditingOperation(AppStateModel.Operation.MOVE_MODEL,
                                    AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING);
                        } else {
                            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
                        }

                    } else if (MENU_ITEM_DELETE_ELEMENT.equals(cmd)) {
                        toggleDeleteElementOperation();
                    } else if (MENU_ITEM_LAUNCH_IA.equals(cmd)) {
                        // Called twice when icon clicked to select and unselect.
                        // But it is not called when another icon of the group is clicked.
                        // Also is not called when icon is unselected via action.
                        launchInitAssistant();
                        DseMenuMediator.disableInitAssistantOnMenuItemClicked();
                        return false;
                    } else

                        //
                        //   S i m u l a t i o n   M o d e   C o m m a n d s
                        //

//        if ("Discrete Dynamic Linear System".equals(cmd)) {
////            onDDLS();
//        } else if ("Discrete Dynamic Random System".equals(cmd)) {
////            onDDRS();
//        } else if ("Discrete Event Dynamic System".equals(cmd)) {
////            onDEDS();
//        }

                        if (MENU_ITEM_SET_SIMULATION_MODE.equals(cmd)) {
                            setSimulationMode();
                            return false; // button will stay disabled
                        } else if (MENU_ITEM_START_SIMULATION.equals(cmd)) {
                            onStartSimulation();
                        } else if (MENU_ITEM_STOP_SIMULATION.equals(cmd)) {
                            onStopSimulation();
                        } else if (MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP.equals(cmd)) {
                            onExecuteOneSimulationStep();
                        } else if (MENU_ITEM_RESET_SIMULATION.equals(cmd)) {
                            onResetSimulation();
                        } else

                            //
                            // Set up  menu request
                            //
                            if (MENU_ITEM_SHOW_SETUP_PANEL.equals(cmd)) {
                                return onShowSetup();
                            } else if (MENU_ITEM_HIDE_SETUP_PANEL.equals(cmd)) {
                                onHideSetup();
                            } else

                                //
                                //   Help menu requests MENU_ITEM_HOW_TO_USE_CREATION_OPERATIONS
                                //

                                if (MENU_ITEM_RUN_ALL_SHOWS_SEQUENTIALLY.equals(cmd)) {

                                    if (PresentationRunner.isDemoRunning()) {
                                        return true;
                                    }
                                    if (!AppController.getInstance().releaseEastSideSpaceIfPossible()) {
                                        return true;
                                    }
                                    if (AppStateModel.isSimulationMode()) {
                                        AppController.getInstance().setDevelopmentMode();
                                    }

                                    // creating new presentation project
                                    MclnProject mclnProject = DesignSpaceModel.getInstance().
                                            replaceCurrentProjectWithPresentationProject(
                                                    HowToUseCreateOperationsScript.SCRIPT_NAME);
                                    MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
                                    MclnSimulationController.getInstance().setMclnModel(currentMclnModel);

                                    java.util.List<PresentationScriptHandler> allScripts = new ArrayList();
                                    allScripts.add(
                                            HowToUseCreateOperationsScript.createHowToUseCreateOperationsScript());
                                    allScripts.add(
                                            HowToUseModificationOperationsScript.createHowToUseModificationOperationsScript());
                                    allScripts.add(
                                            HowToUseSimulationOperationsScript.createHowToRunModelUseSimulationScript());
                                    PresentationRunner.presentAllDemoScripts(allScripts);

                                } else if (MENU_ITEM_HOW_TO_USE_CREATION_OPERATIONS.equals(cmd)) {
                                    return startPresentationShow(HowToUseCreateOperationsScript.SCRIPT_NAME);

                                } else if (MENU_ITEM_HOW_USE_MODIFICATION_OPERATIONS.equals(cmd)) {
                                    return startPresentationShow(HowToUseModificationOperationsScript.SCRIPT_NAME);

                                } else if (MENU_ITEM_HOW_TO_RUN_MODEL_SIMULATION.equals(cmd)) {
                                    return startPresentationShow(HowToUseSimulationOperationsScript.SCRIPT_NAME);

                                } else if (MENU_ITEM_WHAT_IS_DSDS_DSE.equals(cmd)) {
                                    return onOpenHelpPanelButtonClicked();
                                } else if (MENU_ITEM_RUNNING_EXAMPLES.equals(cmd)) {
                                    onHelpRunningModelExamples();
                                } else if (MENU_ITEM_MENU_COMMANDS.equals(cmd)) {
                                    onHelpMenuCommands();
                                } else if (MENU_ITEM_WHAT_IS_CREATOR.equals(cmd)) {
                                    onHelpWhatIsGraphicalModelCreator();
                                } else if (MENU_ITEM_WHAT_IS_SIM_ENG.equals(cmd)) {
                                    onHelpWhatIsSimEngine();
                                } else if (MENU_ITEM_WHAT_IS_TD_IG.equals(cmd)) {
                                    onHelpWhatIsTDIG();
                                } else if (MENU_ITEM_WHAT_IS_SD_IG.equals(cmd)) {
                                    onHelpWhatIsSDIG();
                                } else if (MENU_ITEM_ABOUT.equals(cmd)) {
                                    DsdsseSplash.showAboutPopup(DsdsseMainFrame.getInstance());
                                } else {
                                    lastMenuCommand = null;
                                }
        // returning "true" makes button enabled
        return true;
    }

    final void userClosesApplicationWindow() {
        userClosesApplication();
    }

    public void userClosesApplication() {
        // Embedded and Running Init Assistant must be interrupted if possible
        // TODO
        // Currently "Exit" is disabled when IA is initializing.
        // But it make sense to disable it only when editing operation is in progress
        if (!shutdownInitAssistantIfPossible()) {
            // Initialization was not interrupted
            return;
        }

        boolean projectSavedOrDiscarded =
                saveOrDiscardProjectIfModified(SELECTED_OPERATION_IS_TO_DISCARD_CURRENT_PROJECT);
        if (!projectSavedOrDiscarded) {
            return;
        }

        System.exit(0);
    }

    /**
     * This method is preserved for possible use when menu "View" is used to
     * switch from one application main panel to another back and forth.
     * Currently menu bar "View" menu is used top switch Design Space content.
     *
     * @param selectedView
     */
    private void switchToSelectedApplicationView(String selectedView) {
        if (!selectedView.equals(MENU_ITEM_MATRIX_VIEW)) {
            return;
        }
        DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
        DsdsDseMatrixSpaceHolderPanel dsdsDseMatrixSpaceHolderPanel = DsdsDseMatrixSpaceHolderPanel.getInstance();
        mainFrame.setDsdsDseMatrixViewHolderPanel(dsdsDseMatrixSpaceHolderPanel);
    }

    /**
     *
     * @param selectedView
     */
    private void switchToSelectedView(String selectedView) {
        if (selectedView.equals(MENU_ITEM_GRAPH_VIEW)) {
            DesignSpaceGraphOrMatrixViewCardPanel.getInstance().switchToMclnGraphDesignerView();
        } else if(selectedView.equals(MENU_ITEM_MATRIX_VIEW)){
            DesignSpaceGraphOrMatrixViewCardPanel.getInstance().switchToMclnGraphMatrixView();
        }
    }

    //
    // Methods called from Menu Listener when it processes request to start operation
    //

    public void unselectCreationOperation() {
        AppStateModel.Operation currentOperation = appStateModel.getCurrentOperation();
        if (currentOperation.isCreationOperation()) {
            String menuItem = appStateModel.getCurrentOperation().getMenuItem();
            AdfBasicAction adfBasicAction = getUIAction(menuItem);
            if (adfBasicAction != null) {
                adfBasicAction.setSelected(false);
                AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
            }
        }
    }

    /**
     * Called:
     * When menu: Clear Design Space menu item is clicked
     */
    private final void clearDesignSpace() {

        unselectCreationOperation();

        //  Checking if cleaning makes sense or is possible

        DesignSpaceView designSpaceView = DesignSpaceView.getInstance();
        boolean designSpaceIsEmpty = designSpaceView.isDesignSpaceEmpty();
        if (designSpaceIsEmpty) {
            DsdsDseMessagesAndDialogs.showMessage(designSpaceView, "Clear Design Space operation",
                    AllMessages.DESIGN_SPACE_IS_EMPTY_MESSAGE.getText());
            return;
        }

        boolean projectSavedOrDiscarded =
                saveOrDiscardProjectIfModelModified(SELECTED_OPERATION_IS_TO_CLEAR_CURRENT_MODEL);
        if (!projectSavedOrDiscarded) {
            return;
        }

        // Cleaning
        MclnGraphModel.getInstance().clearCurrentMclnModel();

        // Although by this time project might be already backed up after
        // it was saved, we do this here again to backup cleaned project.
        MclnProject mclnProject = MclnProject.getInstance();
        mclnProject.resetTheProjectBackup();
    }

    //
    //   P r i n t i n g
    //

    /**
     *
     */
    private final boolean openPrintPreviewContent() {

        unselectCreationOperation();

        // The Print menu item is disabled when Init Assistant is initializing.
        // Hence, this method can only be called when Init Assistant is either
        // closed or in Front Page idling mode, So that it can be closed
        // without checking if Init Assistant can be interrupted
//        if (DsdsseUserPreference.isInitAssistantEmbedded() && InitAssistantInterface.isInitAssistantUpAndRunning()) {
        if (InitAssistantInterface.isInitAssistantUpAndRunning()) {
            InitAssistantInterface.shutDownInitAssistantUnconditionally();
        } else {
            initAssistantExcludedFromGroupAndDisabled();
        }

        // finishing currently active operation
        unselectCreationOperation();

        if (!DesignSpaceContentManager.isPrintPreviewContentWestPanel()) {
            AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
            DesignSpaceView.getInstance().switchToPrintPreviewContent();
            DseMenuMediator.openPrintContextMenuItemClickedAndDisabled();
            return false;
        }
        return true;
    }

    /**
     * Called when Print Preview Panel Cancel button button clicked
     */
    public final void onUserClosesPrintPreviewContent() {
        onHidePrintPreviewPanel();
        enableAndUnlockInitAssistant();
    }

    private final void onHidePrintPreviewPanel() {
        DesignSpaceView.getInstance().restoreDesignSpaceView();
        Action action = getUIAction(MENU_ITEM_SHOW_PRINT_CONTENT);
        if (action != null) {
            action.setEnabled(true);
        }
    }

    public void setOperationSelected(String menuItem, Boolean selected) {
        AdfBasicAction action = getUIAction(menuItem);
        if (action != null) {
            action.setSelected(selected);
        }
    }

    /**
     * C r e a t i n g   P o l y l i n e   A r c s
     * <p>
     * Called when menu item clicked
     */
    private final void setCreatingPolylineArcs() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.CREATING_POLYLINE_ARCS)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.CREATING_POLYLINE_ARCS,
                    AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
            enableOperation(MENU_ITEM_CREATE_POLYLINE_ARCS);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    /**
     * C r e a t i n g   S p l i n e   A r c s
     * <p>
     * Called when menu item clicked
     */
    private final void setCreatingSplineArcs() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.CREATING_SPLINE_ARCS)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.CREATING_SPLINE_ARCS,
                    AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
            enableOperation(MENU_ITEM_CREATE_SPLINE_ARCS);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    /**
     * C r e a t i n g   F r a g m e n t s
     * <p>
     * Called when menu item clicked
     */
    private final void toggleFragmentCreation() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.CREATE_FRAGMENTS)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.CREATE_FRAGMENTS,
                    AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY);
            enableOperation(MENU_ITEM_CREATE_FRAGMENTS);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    private final void toggleMoveFragment() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.MOVE_FRAGMENT)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.MOVE_FRAGMENT,
                    AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    private final void toggleMoveElements() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.MOVE_ELEMENTS)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.MOVE_ELEMENTS,
                    AppStateModel.OperationStep.SELECT_ELEMENT_TO_BE_MOVED);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    /**
     * D e l e t i o n   o f   m o d e l   e l e m e n t s
     * <p>
     * Called when menu item clicked
     */
    private void toggleDeleteElementOperation() {
        if (!isRequestedOperationAlreadySet(AppStateModel.Operation.DELETE_ELEMENT)) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.DELETE_ELEMENT,
                    AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED);
            enableOperation(MENU_ITEM_DELETE_ELEMENT);
        } else {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        }
    }

    //
    //   I n i t   A s s i s t a n t :   o p e n i n g  /  c l o s i n g
    //

    /**
     * Init Assistant
     * <p>
     * Called when menu item clicked
     */
    private void launchInitAssistant() {
        if (InitAssistantInterface.isInitAssistantUpAndRunning()) {
            return;
        }
        // Open Init Assistant
        DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
        InitAssistantInterface.createInitializationAssistant(mainFrame);
    }

    /**
     * The method called when user placed a model entity into Init Assistant
     * by either right mouse popup menu or clicking on the entity
     */
    public void initAssistantOpenForInitialization() {
        AdfBasicAction launchInitializationAssistantAction = getUIAction(MENU_ITEM_LAUNCH_IA);
        launchInitializationAssistantAction.setDisabledAndLock();
    }

    public void unlockInitAssistant() {
        AdfBasicAction launchInitializationAssistantAction = getUIAction(MENU_ITEM_LAUNCH_IA);
        launchInitializationAssistantAction.setDisabledStateUnlocked();
    }

    public void initAssistantExcludedFromGroupAndDisabled() {
        AdfBasicAction launchInitializationAssistantAction = getUIAction(MENU_ITEM_LAUNCH_IA);
        launchInitializationAssistantAction.setEnabled(false);
    }

    /**
     * Called when Print Preview Panel Cancel button button clicked
     */
    public void enableAndUnlockInitAssistant() {
        DseMenuMediator.closingPrintContextAndMenuItemIsEnabled();
    }

    /**
     * Called from outside of Shared Panel Space Group
     * by Preference Setup title bar Close button
     */
    public void onUserClosesPreferencesSetup() {
        onHideSetupPanel();
        AdfBasicAction preferenceSetupPanelAction = getUIAction(MENU_ITEM_SHOW_SETUP_PANEL);
        if (preferenceSetupPanelAction != null) {
            preferenceSetupPanelAction.setEnabled(true);
        }
    }

    /**
     * Called from outside of Shared Panel Space Group
     * by Help panel title bar Close button
     */
    public void onUserClosesHelpContent() {
        hideHelpPanel();
        AdfBasicAction helpContentPanelAction = getUIAction(MENU_ITEM_WHAT_IS_DSDS_DSE);
        if (helpContentPanelAction != null) {
            helpContentPanelAction.setEnabled(true);
        }
    }


    public void enableOperation(String menuItem) {
        Action action = getUIAction(menuItem);
        action.setEnabled(true);
    }

    /**
     * Called from App State Model when user selects new operation
     *
     * @param enable
     */
    public void enableDisableCurrentCommand(boolean enable) {
        AdfBasicAction actionToEnableDisable;
        actionToEnableDisable = getUIAction(MENU_ITEM_CREATE_PROPERTIES);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_CREATE_CONDITIONS);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_CREATE_SPLINE_ARCS);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_CREATE_POLYLINE_ARCS);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_CREATE_FRAGMENTS);
        actionToEnableDisable.setEnabled(enable);


        actionToEnableDisable = getUIAction(MENU_ITEM_MOVE_ELEMENTS);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_MOVE_FRAGMENT);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_MOVE_MODEL);
        actionToEnableDisable.setEnabled(enable);
        actionToEnableDisable = getUIAction(MENU_ITEM_DELETE_ELEMENT);
        actionToEnableDisable.setEnabled(enable);

        DseMenuMediator.enableOrDisableInitAssistantOnCreationStartedOrStopped(enable);

        enableDisableAllActionGroup.enableAllActions(enable);
        if (appStateModel.isDevelopmentMode()) {
            demoMenuActionGroup.enableAllActions(enable);
        }
    }

    /**
     *
     */
    void unselectActiveEditingOperationUpOnSimulationModeSelected() {
        AppStateModel.Operation currentOperation = AppStateModel.getInstance().getCurrentOperation();
        if (currentOperation == null || currentOperation == AppStateModel.Operation.NONE) {
            return;
        }
        String menuItem = currentOperation.getMenuItem();
        AdfBasicAction currentAction = getUIAction(menuItem);
        if (!(currentAction instanceof AdfToggleButtonAction)) {
            return;
        }
        AdfToggleButtonAction adfToggleButtonAction = (AdfToggleButtonAction) currentAction;
        adfToggleButtonAction.setSelected(false);
    }

    /**
     * @param model
     */
    private void updateAppInfoPanelModelSection(AppStateModel.Operation model) {
        if (DsdsseEnvironment.getCurrentMode() != DsdsseEnvironment.EXEC_MODE) {
            DsdsseEnvironment.setCurrentMode(DsdsseEnvironment.EDIT_MODE);
        }
        appStateModel.updateDemoMode(model);
    }

    private boolean isRequestedOperationAlreadySet(AppStateModel.Operation operation) {
        return appStateModel.isCurrentOperation(operation) && !appStateModel.getCurrentOperationStep().isCanceled();
    }

    //
    //   C r e a t i o n   M o d e
    //

    public void setDevelopmentMode() {
//        if (isInDevelopmentMode()) {
//            return;
//        }

        stopSimulationProcessAndClearSimulationPresentation();

        AdfBasicAction developmentModeAction = getUIAction(MENU_ITEM_SET_DEVELOPMENT_MODE);
        AdfBasicAction simulationModeEnabledAction = getUIAction(MENU_ITEM_SET_SIMULATION_MODE);
        developmentModeAction.setEnabled(false);
        simulationModeEnabledAction.setEnabled(true);

        // enable creation mode buttons
        AdfBasicAction createStatementsAction = getUIAction(MENU_ITEM_CREATE_PROPERTIES);
        AdfBasicAction createConditionsAction = getUIAction(MENU_ITEM_CREATE_CONDITIONS);
        AdfBasicAction createSplineArcsAction = getUIAction(MENU_ITEM_CREATE_SPLINE_ARCS);
        AdfBasicAction createPolylineArcsAction = getUIAction(MENU_ITEM_CREATE_POLYLINE_ARCS);
        AdfBasicAction createFragmentAction = getUIAction(MENU_ITEM_CREATE_FRAGMENTS);

        AdfBasicAction moveElementsAction = getUIAction(MENU_ITEM_MOVE_ELEMENTS);
        AdfBasicAction moveFragmentAction = getUIAction(MENU_ITEM_MOVE_FRAGMENT);
        AdfBasicAction moveModelAction = getUIAction(MENU_ITEM_MOVE_MODEL);
        AdfBasicAction deleteAction = getUIAction(MENU_ITEM_DELETE_ELEMENT);

        createStatementsAction.setEnabled(true);
        createFragmentAction.setEnabled(true);
        createConditionsAction.setEnabled(true);
        createSplineArcsAction.setEnabled(true);
        createPolylineArcsAction.setEnabled(true);

        moveElementsAction.setEnabled(true);
        moveFragmentAction.setEnabled(true);
        moveModelAction.setEnabled(true);
        deleteAction.setEnabled(true);

        DseMenuMediator.enableInitAssistant();

        // disable simulation mode items
        MclnSimulationController.getInstance().setSimulationDisabled();

//        simulationModeEnabledAction.setEnabled(true);

        // updating mode and operations info panel
        AppStateModel.setDevelopmentMode();
        AppStateModel.setCurrentSimulationOperation(AppStateModel.Operation.NONE);
        AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);

        DesignSpaceGraphOrMatrixViewCardPanel.getInstance().switchToMclnGraphDesignerView();
        DesignOrSimulationStatusPanelCardView.getInstance().switchToDesignStatusViewPanel();
    }

    //
    //   S i m u l a t i o n   M o d e
    //

    /**
     * S e t   S i m u l a t i o n   M o d e
     */
    private void setSimulationMode() {

        AdfBasicAction developmentModeAction = getUIAction(MENU_ITEM_SET_DEVELOPMENT_MODE);
        developmentModeAction.setEnabled(true);

        unselectActiveEditingOperationUpOnSimulationModeSelected();

        // disable creation mode items
        AdfBasicAction createStatementsAction = getUIAction(MENU_ITEM_CREATE_PROPERTIES);
        AdfBasicAction createConditionsAction = getUIAction(MENU_ITEM_CREATE_CONDITIONS);
        AdfBasicAction createSplineArcsAction = getUIAction(MENU_ITEM_CREATE_SPLINE_ARCS);
        AdfBasicAction createPolylineArcsAction = getUIAction(MENU_ITEM_CREATE_POLYLINE_ARCS);
        AdfBasicAction createFragmentAction = getUIAction(MENU_ITEM_CREATE_FRAGMENTS);

        AdfBasicAction moveElementsAction = getUIAction(MENU_ITEM_MOVE_ELEMENTS);
        AdfBasicAction moveFragmentAction = getUIAction(MENU_ITEM_MOVE_FRAGMENT);
        AdfBasicAction moveModelAction = getUIAction(MENU_ITEM_MOVE_MODEL);
        AdfBasicAction deleteAction = getUIAction(MENU_ITEM_DELETE_ELEMENT);
        AdfBasicAction launchInitializationAssistantAction = getUIAction(MENU_ITEM_LAUNCH_IA);

        createStatementsAction.setEnabled(false);
        createConditionsAction.setEnabled(false);
        createSplineArcsAction.setEnabled(false);
        createPolylineArcsAction.setEnabled(false);
        createFragmentAction.setEnabled(false);

        moveElementsAction.setEnabled(false);
        moveFragmentAction.setEnabled(false);
        moveModelAction.setEnabled(false);
        deleteAction.setEnabled(false);

        launchInitializationAssistantAction.setEnabled(false);
        launchInitializationAssistantAction.setExcludedFromGroup(true);

        // enable simulation mode items
        setSimulationMenuItemsAndButtonEnabled(true);
        MclnSimulationController.getInstance().setSimulationEnabled();

        // updating mode and operations info panel
        AppStateModel.setSimulationMode();
        AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
        AppStateModel.setCurrentSimulationOperation(AppStateModel.Operation.SIMULATION_ENABLED);

        DesignOrSimulationStatusPanelCardView.getInstance().switchToSimulationStatusViewPanel();
    }

    /**
     * S t a r t   S i m u l a t i o n
     */
    private void onStartSimulation() {
        Action startSimulationAction = getUIAction(MENU_ITEM_START_SIMULATION);
        Action stopSimulationAction = getUIAction(MENU_ITEM_STOP_SIMULATION);
        Action oneSimulationStepAction = getUIAction(MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP);
        Action resetSimulationAction = getUIAction(MENU_ITEM_RESET_SIMULATION);
        startSimulationAction.setEnabled(false);
        stopSimulationAction.setEnabled(true);
        oneSimulationStepAction.setEnabled(false);
        resetSimulationAction.setEnabled(true);


        MclnSimulationController.getInstance().setSimulationStarted();
        appStateModel.setCurrentSimulationOperation(AppStateModel.Operation.SIMULATION_STARTED);
    }

    /**
     * S t o p   S i m u l a t i o n
     */
    private void onStopSimulation() {
        MclnSimulationController.getInstance().setSimulationStopped();
        Action startSimulationAction = getUIAction(MENU_ITEM_START_SIMULATION);
        Action stopSimulationAction = getUIAction(MENU_ITEM_STOP_SIMULATION);
        Action oneSimulationStepAction = getUIAction(MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP);
        Action resetSimulationAction = getUIAction(MENU_ITEM_RESET_SIMULATION);
        startSimulationAction.setEnabled(true);
        stopSimulationAction.setEnabled(false);
        oneSimulationStepAction.setEnabled(true);
        resetSimulationAction.setEnabled(true);
        appStateModel.setCurrentSimulationOperation(AppStateModel.Operation.SIMULATION_STOPPED);
    }

    /**
     * R e s e t   S i m u l a t i o n
     */
    private void onResetSimulation() {
//        LogPanel.getInstance().clearLog();
        MclnSimulationController.getInstance().setSimulationReset();

        Action startSimulationAction = getUIAction(MENU_ITEM_START_SIMULATION);
        Action stopSimulationAction = getUIAction(MENU_ITEM_STOP_SIMULATION);
        Action oneSimulationStepAction = getUIAction(MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP);
        Action resetSimulationAction = getUIAction(MENU_ITEM_RESET_SIMULATION);
        startSimulationAction.setEnabled(true);
        stopSimulationAction.setEnabled(false);
        oneSimulationStepAction.setEnabled(true);
        resetSimulationAction.setEnabled(true);

        appStateModel.setCurrentSimulationOperation(AppStateModel.Operation.SIMULATION_RESET);
    }

    /**
     * O n e   S i m u l a t i o n   S t e p
     */
    private void onExecuteOneSimulationStep() {
        MclnSimulationController.getInstance().executeOneSimulationStep();
        appStateModel.setCurrentSimulationOperation(AppStateModel.Operation.SIMULATION_ONE_STEP);
    }

    //
    //   P r e f e r e n c e s   s e t u p   s u p p o r t
    //

    public boolean onShowSetup() {
        if (!shutdownEmbeddedRunningInitAssistantIfPossible()) {
            // Initialization was not interrupted
            return true;
        }
        togglePreferencesSetupPanel();
        return false;
    }

    /**
     * Called from menu MENU_ITEM_HIDE_SETUP_PANEL or corresponding toolbar
     * toggle button only when TOGGLE_MENU_AND_TOOLBAR_BUTTONS is true.
     */
    public void onHideSetup() {
        onHideSetupPanel();
    }


    private final void togglePreferencesSetupPanel() {
        PreferencesSetupPanel preferencesSetupPanel = PreferencesSetupPanel.getInstance();
        if (!DesignSpaceContentManager.isThisComponentEastPanel(preferencesSetupPanel)) {
            DesignSpaceContentManager.setEastPanel(preferencesSetupPanel);
            DseMenuMediator.openSetupPanelMenuItemClickedAndDisabled();
        }
    }

    private void onHideSetupPanel() {
        if (!DseMenuAndToolbarBuilder.TOGGLE_MENU_AND_TOOLBAR_BUTTONS) {
            DesignSpaceContentManager.hideEastPanel(PreferencesSetupPanel.getInstance());
        } else {
            DesignSpaceContentManager.hideEastPanel(PreferencesSetupPanel.getInstance());
            Action action = getUIAction(MENU_ITEM_SHOW_SETUP_PANEL);
            if (action != null) {
                action.setEnabled(true);
            }
            action = getUIAction(MENU_ITEM_HIDE_SETUP_PANEL);
            if (action != null) {
                action.setEnabled(false);
            }
        }
    }

    //
    //   H e l p   s u p p o r t
    //

    private boolean onOpenHelpPanelButtonClicked() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.WhatIsDSDSSE);
        if (!DesignSpaceContentManager.isThisComponentEastPanel(helpPanelHolder)) {
            return showHelpPanel(helpPanelHolder);
        }
        return false;
    }

    private void onHelpRunningModelExamples() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.RunningExamples);
        showHelpPanel(helpPanelHolder);
    }

    private void onHelpMenuCommands() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.MenuCommands);
        showHelpPanel(helpPanelHolder);
    }

    private void onHelpWhatIsGraphicalModelCreator() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.WhatIsModelCreator);
        showHelpPanel(helpPanelHolder);
    }

    private void onHelpWhatIsSimEngine() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.WhatIsSimEng);
        showHelpPanel(helpPanelHolder);
    }

    private void onHelpWhatIsTDIG() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.WhatIsTimeDrivenIG);
        showHelpPanel(helpPanelHolder);
    }

    private void onHelpWhatIsSDIG() {
        HelpPanelHolder helpPanelHolder = HelpPanelHolder.getInitializedInstance(HelpPanelHolder.HelpMenuItems.WhatIsStateDrivenIG);
        showHelpPanel(helpPanelHolder);
    }

    /**
     * @param helpPanelHolder
     */
    private boolean showHelpPanel(HelpPanelHolder helpPanelHolder) {
        if (!shutdownEmbeddedRunningInitAssistantIfPossible()) {
            // Initialization was not interrupted
            return true;
        }
        DesignSpaceContentManager.setEastPanel(helpPanelHolder);
        DseMenuMediator.helpMenuItemClickedAndDisabled();
        return false;
    }

    /**
     *
     */
    private void hideHelpPanel() {
        DesignSpaceContentManager.hideEastPanel(HelpPanelHolder.getInstance());
    }

    //
    //   U t i l i t y   m e t h o d s
    //

    public final boolean isInDevelopmentMode() {
        Action creationModeEnabledAction = getUIAction(MENU_ITEM_SET_DEVELOPMENT_MODE);
        boolean developmentMode = !creationModeEnabledAction.isEnabled();
        return developmentMode;
    }

    private final boolean isInSimulationMode() {
        return appStateModel.isSimulationMode();
    }

    /**
     *
     */
    private void stopSimulationProcessAndClearSimulationPresentation() {
//        LogPanel.getInstance().clearLog();
        // setSimulationReset will set model to initial state
//        onResetSimulation();
        MclnSimulationController.getInstance().setSimulationReset();

        MclnSimulationController.getInstance().clearSimulation();
        setSimulationMenuItemsAndButtonEnabled(false);
    }

    /**
     * This method is to be called to restore mode that was interrupted
     * by Save or Save All operation only.
     */
    private void restoreSimulationMenuItemsAndButtonEnabled() {
        setSimulationMenuItemsAndButtonEnabled(true);
    }

    /**
     * @param enabled
     */
    private void setSimulationMenuItemsAndButtonEnabled(boolean enabled) {
        Action startSimulationAction = getUIAction(MENU_ITEM_START_SIMULATION);
        Action stopSimulationAction = getUIAction(MENU_ITEM_STOP_SIMULATION);
        Action oneSimulationStepAction = getUIAction(MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP);
        Action resetSimulationAction = getUIAction(MENU_ITEM_RESET_SIMULATION);

        startSimulationAction.setEnabled(enabled);
        stopSimulationAction.setEnabled(false);
        oneSimulationStepAction.setEnabled(enabled);
        resetSimulationAction.setEnabled(enabled);
    }


    // Here are conditions to begin new menu operation:
    // 1) Current editing operation must be canceled and Initialization Assistant must be Shutdown
    // 2) Current editing operation must be canceled and Initialization Assistant is not initializing but may be running
    // 3) Embedded Initialization Assistant must be shutdown
    // 4) Current editing operation must be canceled
    //
    //

    // ================================================================================================================
    //  C l e a n u p   a f t e r   D e m o   P r e s e n t a t i o n   i s   c o m p l e t e  o r   c a n c e l l e d
    // ================================================================================================================

    public final void cleanupAfterDemoPresentationIsCompleteOrCanceled() {
        mclnGraphViewEditor.cleanupAfterDemoPresentationIsCompleteOrCanceled();

        if (AppStateModel.isSimulationMode()) {
            setDevelopmentMode();
        } else {
            unselectActiveEditingOperationUpOnSimulationModeSelected();
            AppStateModel.setNewEditingOperation(AppStateModel.Operation.NONE, AppStateModel.OperationStep.NONE);
            MclnGraphModel.getInstance().clearCurrentMclnModel();
        }

        DesignSpaceModel.getInstance().restoreStashedProject();
    }

    /**
     * @param parentComponent
     * @param header
     * @param message
     */
    public static void showMessageIfNotSilentMode(Component parentComponent, String header, String message) {
        if (DsdsseUserPreference.isUserPreferenceSilentCancellationPolicy()) {
            return;
        }
        DsdsDseMessagesAndDialogs.showMessage(parentComponent, header, message);
    }

    /**
     * @param scriptName
     * @return
     */
    public static boolean startPresentationShow(String scriptName) {
        if (PresentationRunner.isDemoRunning()) {
            return true;
        }

        AdfBasicAction adfBasicAction = getUIAction(MENU_ITEM_RUN_ALL_SHOWS_SEQUENTIALLY);
        if (adfBasicAction != null && !adfBasicAction.isEnabled()) {
            showMessageIfNotSilentMode(DesignSpaceView.getInstance().getMclnGraphDesignerView(),
                    "Launching Presentation Show", CANNOT_START_PRESENTATION_MESSAGE);
            return true;
        }

        if (!AppController.getInstance().releaseEastSideSpaceIfPossible()) {
            return true;
        }

        if (AppStateModel.isSimulationMode()) {
            AppController.getInstance().setDevelopmentMode();
        }

        // creating new presentation project
        MclnProject mclnProject = DesignSpaceModel.getInstance().
                replaceCurrentProjectWithPresentationProject(scriptName);

        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);

        PresentationScriptHandler createOperationsScript = null;
        switch (scriptName) {
            case HowToUseCreateOperationsScript.SCRIPT_NAME:
                createOperationsScript = HowToUseCreateOperationsScript.createHowToUseCreateOperationsScript();
                break;
            case HowToUseModificationOperationsScript.SCRIPT_NAME:
                createOperationsScript = HowToUseModificationOperationsScript.createHowToUseModificationOperationsScript();
                break;
            case HowToUseSimulationOperationsScript.SCRIPT_NAME:
                createOperationsScript = HowToUseSimulationOperationsScript.createHowToRunModelUseSimulationScript();
                break;
        }

        if (createOperationsScript == null) {
            return true;
        }
        PresentationRunner.presentDemo(createOperationsScript);
        return true;
    }

    //   C h e c k i n g   a n d   s a v i n g   P r o j e c t

    /**
     * projectChanged[0] -> boolean[1] = projectAttributesChanged && modelChanged[0]
     * projectChanged[1] -> boolean[1] = projectAttributesChanged
     * projectChanged[2] -> boolean[]  = modelChanged, where
     * <p>
     * modelChanged[0] - true when something changed = modelChanged[1] | modelChanged[2] |modelChanged[3]
     * modelChanged[1] - true when initialized
     * modelChanged[2] - true when structure changed
     * modelChanged[3] - true when model moved
     *
     * @return
     */
    private final boolean saveOrDiscardProjectIfModified(String title) {
        if (MclnProject.getInstance().isPresentationProject()) {
            // we do not want to ask user when running Presentation
            return true;
        }
        boolean[][] projectModified = MclnProject.getInstance().wasProjectModified();
        if (!projectModified[0][0]) {
            // project was not modified
            return true;
        }
        boolean userWantsToDiscardChanges = letUserToConfirmIfProjectShouldBeSaved(title, projectModified);
        if (!userWantsToDiscardChanges) {
            return false;
        }
        return userWantsToDiscardChanges;
    }

    /**
     * @param title
     * @return
     */
    private final boolean saveOrDiscardProjectIfModelModified(String title) {
        if (MclnProject.getInstance().isPresentationProject()) {
            // we do not want to ask user when running Presentation
            return true;
        }

        boolean[][] projectModified = new boolean[3][];
        boolean[] modelChanged = MclnProject.getInstance().wasModelModified();


        projectModified[0] = new boolean[]{modelChanged[0]};
        if (!projectModified[0][0]) {
            // model was not modified
            return true;
        }
        projectModified[1] = new boolean[]{false};
        projectModified[2] = modelChanged;

        boolean userWantsToDiscardChanges = letUserToConfirmIfProjectShouldBeSaved(title, projectModified);
        if (!userWantsToDiscardChanges) {
            return false;
        }
        return userWantsToDiscardChanges;
    }

    /**
     * @param title
     * @param projectModified
     * @return
     */
    private boolean letUserToConfirmIfProjectShouldBeSaved(String title, boolean[][] projectModified) {

        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("<html>").
                append("<div style=\" font-size:11px; font-weight: plain;    color:#550000; text-align:justify;   ").
                append("<br> \">").
                append("Here below are unsaved change(s) that will be lost if the Project is not saved.&nbsp;&nbsp;<br>");

        if (projectModified[0][0]) {
            stringBuilder.append("<ul>");
            if (projectModified[1].length == 1 && projectModified[1][0]) {
                stringBuilder.append("<li>Change in the Project's attributes.</li>");
            }

            if (projectModified[2] != null && projectModified[2][0]) {
                if (projectModified[2][1]) {
                    stringBuilder.append("<li>Unsaved model initializations.</li>");
                }
                if (projectModified[2][2]) {
                    stringBuilder.append("<li>Change in the Model structure.</li>");
                }
                if (projectModified[2][3]) {
                    stringBuilder.append("<li>Change in the Model or its fragment location.</li>");
                }
            }
            stringBuilder.append("</ul>");
            stringBuilder.append(" Do you want to save the Project ? ");
            stringBuilder.append("</div></html>");
        }
        String question = stringBuilder.toString();

        if (question != null) {
            Component background = DesignSpaceView.getInstance();
            int choice = DsdsDseMessagesAndDialogs.threeOptionsDialog(
                    background, question, title, SAVE_PROJECT_OPTIONS);
            if (choice == USER_CHOICE_DIALOG_CLOSED) {
                return false;
            } else if (choice == USER_CHOICE_SAVE_PROJECT) {
                // Save entity and OK to interrupt initialization
                boolean saved = DesignSpaceController.getInstance().saveProject();
                return saved;
            } else if (choice == USER_CHOICE_DISCARD_PROJECT) {
                // Discard modification and OK to interrupt initialization
                return true;
            } else if (choice == USER_CHOICE_CANCEL_OPERATION) {
                // Ignore request to interrupt initialization
                return false;
            }
        }
        return true;
    }

    //   C l o s i n g   I n i t   A s s i s t a n t

    /**
     * User closes Init Assistant
     *
     * @return
     */
    public boolean onUserClickedCloseStandaloneInitAssistantButton() {
        boolean okToInterrupt = InitAssistantInterface.canInitAssistantBeInterrupted(
                AllMessages.CAN_MODEL_INITIALIZATION_TO_BE_CANCELED_MESSAGE.getText());

        DseMenuMediator.enableInitAssistant();
        appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
                AppStateModel.OperationStep.INIT_ASSISTANT_CLOSED);
        return okToInterrupt;
    }

    /**
     * User closes Init Assistant
     *
     * @return
     */
    public boolean onUserClickedCloseEmbeddedInitAssistantButton() {
        boolean okToInterrupt = InitAssistantInterface.canInitAssistantBeInterrupted(
                AllMessages.CAN_MODEL_INITIALIZATION_TO_BE_CANCELED_MESSAGE.getText());
        if (!okToInterrupt) {
            return false;
        }
        InitAssistantInterface.shutDownInitAssistantUnconditionally();
        DseMenuMediator.enableInitAssistant();
        appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
                AppStateModel.OperationStep.INIT_ASSISTANT_CLOSED);
        return true;
    }

    //
    //   Replacing Property or Arc
    //

    /**
     * Called from Edit Popup Menu to place Property into Init Assistant
     *
     * @param mainFrame
     * @param mcLnPropertyView
     */
    public final void setInitAssistantToInitializeProperty(JFrame mainFrame, MclnPropertyView mcLnPropertyView) {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            DseMenuMediator.disableInitAssistantOnMenuItemClicked();
        }
        InitAssistantInterface.setInitAssistantToInitializeProperty(mainFrame, mcLnPropertyView);
        appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
                AppStateModel.OperationStep.INITIALIZING_PROPERTY_NODE);
    }


    /**
     * Called fom Edit Popup Menu to place Arc into Init Assistant
     *
     * @param mainFrame
     * @param mclnArcView
     */
    public final void setInitAssistantToInitializeArc(JFrame mainFrame, MclnArcView mclnArcView) {

        InitAssistantInterface.setInitAssistantToInitializeArc(mainFrame, (DesignerArcView) mclnArcView);

        AppStateModel.OperationStep arcInitializationStep;
        if (mclnArcView.isRecognizingArc()) {
            arcInitializationStep = AppStateModel.OperationStep.INITIALIZING_RECOGNIZING_ARC;
        } else {
            arcInitializationStep = AppStateModel.OperationStep.INITIALIZING_GENERATING_ARC;
        }
        appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION, arcInitializationStep);

        AppController.getInstance().initAssistantOpenForInitialization();

//        }
    }

    /**
     * Called when Presentation is starting
     *
     * @return
     */
    private boolean releaseEastSideSpaceIfPossible() {
        if (DesignSpaceContentManager.isPrintPreviewContentWestPanel()) {
            return false;
        }
        if (DsdsseUserPreference.isInitAssistantEmbedded() && InitAssistantInterface.isInitAssistantUpAndRunning()) {
            if (!shutdownInitAssistantIfPossible()) {
                return false;
            }
            appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION, AppStateModel.OperationStep.INIT_ASSISTANT_CLOSED);
            DseMenuMediator.enableInitAssistant(); // this is only difference with shutdownEmbeddedRunningInitAssistantIfPossible
        }
        AdfBasicAction preferenceSetupPanelAction = getUIAction(MENU_ITEM_SHOW_SETUP_PANEL);
        if (preferenceSetupPanelAction != null && !preferenceSetupPanelAction.isEnabled()) {
            preferenceSetupPanelAction.setEnabled(true);
        }
        AdfBasicAction helpContentPanelAction = getUIAction(MENU_ITEM_WHAT_IS_DSDS_DSE);
        if (helpContentPanelAction != null && !helpContentPanelAction.isEnabled()) {
            helpContentPanelAction.setEnabled(true);
        }
        DesignSpaceContentManager.clearEastSideSpace();
        return true;
    }

    //
    //    Release East Space for Setup or Help of Exit
    //

    /**
     * Is used to shutdown Init Assistant when opening Setup or Help Panel
     *
     * @return Created on 10/29/2017
     */
    private boolean shutdownEmbeddedRunningInitAssistantIfPossible() {
        if (!(DsdsseUserPreference.isInitAssistantEmbedded() && InitAssistantInterface.isInitAssistantUpAndRunning())) {
            return true;
        }
        if (shutdownInitAssistantIfPossible()) {
            appStateModel.setNewEditingOperation(AppStateModel.Operation.INITIALIZATION, AppStateModel.OperationStep.INIT_ASSISTANT_CLOSED);
            return true;
        }
        return false;
    }

    /**
     * The method shuts down Initialization Assistant if possible
     * Otherwise user is asked permission shutdown Initialization Assistant
     *
     * @return true when Initialization Assistant was shutdown, false otherwise.
     */
    private boolean shutdownInitAssistantIfPossible() {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            return true;
        }
        return InitAssistantInterface.shutdownInitAssistantIfPossible(
                AllMessages.CAN_MODEL_INITIALIZATION_TO_BE_CANCELED_MESSAGE.getText());
    }
}
