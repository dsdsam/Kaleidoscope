package dsdsse.animation;

import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;

/**
 * Created by Admin on 6/16/2017.
 */
public class HowToUseSimulationOperationsScript extends PresentationScriptHandler {

    public static final String SCRIPT_NAME = "How to Use Model Simulation Operations";

    public static final HowToUseSimulationOperationsScript createHowToRunModelUseSimulationScript() {
        MclnGraphDesignerView mclnGraphDesignerView = DesignSpaceView.getInstance().getMclnGraphDesignerView();
        return new HowToUseSimulationOperationsScript(mclnGraphDesignerView);
    }

    // Operations
    private static final String OPERATION_SIMULATION = "Simulation Operation";

    // Step actions and action attributes
    private static final String STEP_GENERATE_MODEL = "Generate Model";
    private static final String STEP_CLICK_MENU_BUTTON = "Click on menu button";
    private static final String STEP_CLICKING_START_BUTTON = "Click on Start button";
    private static final String STEP_CLICKING_STEP_BUTTON = "Click on Step button";
    private static final String STEP_CLICKING_RESET_BUTTON = "Click on Reset button";
    private static final String STEP_CLICKING_STOP_BUTTON = "Click on Stop button";

    //   ===============================================================================================================
    //   M e s s a g e s
    //   ===============================================================================================================

    private static final String HOW_TO_RUN_MODEL_SIMULATION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Presentation:</font>&nbsp; ").
            append("How to Run Model Simulation ").
            append("</div></html>").toString();

    private static final String SELECTING_MENUS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Three Rules</font> menu is selected and example model is generated.<br> ").
            append("Then <font color=#FFDD00>Set Simulation Mode</font> menu is selected. ").
            append("</div></html>").toString();

    private static final String CLICKING_ON_SIMULATE_ONE_STEP_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Step</font> simulation button is clicked 18 times <br>to execute 18 simulation steps. ").
            append("</div></html>").toString();

    private static final String CLICKING_ON_RESET_SIMULATION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Reset</font> simulation button is clicked <br>to reset model to initial state. ").
            append("</div></html>").toString();

    private static final String CLICKING_ON_START_SIMULATION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Start</font> simulation button is clicked <br>to start model auto-simulation process. ").
            append("</div></html>").toString();

    private static final String CLICKING_ON_STOP_SIMULATION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Stop</font> simulation button is clicked <br>to stop model auto-simulation process. ").
            append("</div></html>").toString();

    private static final String CLEANUP_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("Now the last selected operation is unselected and created model is destroyed. ").
            append("</div></html>").toString();

    //   ===============================================================================================================
    //   S c r i p t
    //   ===============================================================================================================

    private static final String[][] demoScriptData = {

            {SET_CURSOR_TO_HOME_LOCATION, "", ""},
            {PAUSE, "1", ""},
            {INFO_MESSAGE, HOW_TO_RUN_MODEL_SIMULATION_MESSAGE, SCRIPT_NAME_MESSAGE_BACKGROUND,
                    "0xFFFFFF", MESSAGE_EXPOSURE_SHORT, SKIP_STEP_FOR_INDIVIDUAL_SHOW},
            {PAUSE, "1", "", "", "", SKIP_STEP_FOR_INDIVIDUAL_SHOW},

            //   =======================================================================================================
            //   G e n e r a t i n g   e x a m p l e   m o d e l   a n d   s e t t i n g   s i m u l a t i o n   m o d e
            //   =======================================================================================================

            {INFO_MESSAGE, SELECTING_MENUS_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_MEDIUM},
            {PAUSE, "1", ""},
            {OPERATION_SIMULATION, STEP_GENERATE_MODEL, AppController.MENU_ITEM_EXAMPLES,
                    AppController.DEMO_PROJECT_THREE_RULES, "20"},
            {PAUSE, "2", ""},

            //   =======================================================================================================
            //   Using Simulation Control Buttons
            //   =======================================================================================================

            {OPERATION_SIMULATION, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_SIMULATION_MODE,
                    AppController.MENU_ITEM_SET_SIMULATION_MODE, "30"},
            {PAUSE, "1", ""},

            {INFO_MESSAGE, CLICKING_ON_SIMULATE_ONE_STEP_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_SIMULATION, STEP_CLICKING_STEP_BUTTON, AppController.MENU_ITEM_EXECUTE_ONE_SIMULATION_STEP, "18"},
            {PAUSE, "1", ""},

            {INFO_MESSAGE, CLICKING_ON_RESET_SIMULATION_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_SIMULATION, STEP_CLICKING_RESET_BUTTON, AppController.MENU_ITEM_RESET_SIMULATION, "1"},
            {PAUSE, "1", ""},

            {INFO_MESSAGE, CLICKING_ON_START_SIMULATION_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_SIMULATION, STEP_CLICKING_START_BUTTON, AppController.MENU_ITEM_START_SIMULATION, "1"},
            {PAUSE, "22", ""},

            {INFO_MESSAGE, CLICKING_ON_STOP_SIMULATION_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_SIMULATION, STEP_CLICKING_STOP_BUTTON, AppController.MENU_ITEM_STOP_SIMULATION, "1"},
            {PAUSE, "2", ""},

            //   ======================================================================================================
            //   Operation: Cleanup
            //   ======================================================================================================

            {INFO_MESSAGE, SET_DEVELOPMENT_MODE_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT
            },
            {PAUSE, "1", ""
            },
            {OPERATION_SIMULATION, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_DEVELOPMENT_MODE,
                    AppController.MENU_ITEM_SET_DEVELOPMENT_MODE, "100"},
            {PAUSE, "1", ""},

            //   ======================================================================================================
    };

    private PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback;

    /**
     * @param mclnGraphDesignerView
     */
    HowToUseSimulationOperationsScript(MclnGraphDesignerView mclnGraphDesignerView) {
        super(mclnGraphDesignerView);
        initScript();
    }

    private void initScript() {
        scriptScanIndex = 0;
        demoScript.clear();
        for (int i = 0; i < demoScriptData.length; i++) {
            String[] parameters = demoScriptData[i];
            String operation = parameters[0];
            String stepAction = parameters[1];
            String actionAttribute1 = parameters[2];
            String actionAttribute2 = parameters.length <= 3 ? "" : parameters[3];
            String actionAttribute3 = parameters.length <= 4 ? "" : parameters[4];
            String actionAttribute4 = parameters.length <= 5 ? "" : parameters[5];
            ScriptItem scriptItem = new ScriptItem(operation, stepAction, actionAttribute1,
                    actionAttribute2, actionAttribute3, actionAttribute4);
            demoScript.add(scriptItem);
        }
    }

    @Override
    String getScriptName() {
        return SCRIPT_NAME;
    }

    public void setScriptStepCompletionFeedback(PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback) {
        this.scriptStepCompletionFeedback = scriptStepCompletionFeedback;
        super.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
    }

    void printScript() {
        System.out.println("\nPrinting Demo Script: ");
        System.out.println();
        for (ScriptItem scriptItem : demoScript) {
            System.out.println("Demo Script Item created: " + scriptItem.toString());
        }
    }


    //
    //   S c r i p t   S t e p   E x e c u t o r
    //

    void executeScriptStep(ScriptItem scriptItem) {

        switch (scriptItem.getOperation()) {
            case INFO_MESSAGE:
            case WARNING_MESSAGE:
                presentMessage(scriptItem);
                break;

            case SET_FULL_SCREEN_MODE:
                executeOperationSetFullScreenMode(scriptItem);
                break;

            case SET_CURSOR_TO_HOME_LOCATION:
                executeOperationSetCursorToHomeLocation(scriptItem);
                break;

            case OPERATION_SIMULATION:
                executeOperationGenerateModel(scriptItem);
                break;

            case PAUSE:
                pause(scriptItem);
                break;

            default:
                Thread.dumpStack();
                break;
        }
    }

    //
    //   S i m u l a t i o n   P r e s e n t a t i o n   S t e p s   E x e c u t o r
    //

    private void executeOperationGenerateModel(ScriptItem scriptItem) {
        if (STEP_GENERATE_MODEL.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICKING_STEP_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = usingSimulationControlButtonsExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICKING_START_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = usingSimulationControlButtonsExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICKING_RESET_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = usingSimulationControlButtonsExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICKING_STOP_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = usingSimulationControlButtonsExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    @Override
    public String toString() {
        return SCRIPT_NAME;
    }
}
