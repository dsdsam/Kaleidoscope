package dsdsse.animation;

import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.graphview.MclnGraphViewNode;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created by Admin on 6/17/2017.
 */
public class HowToUseModificationOperationsScript extends PresentationScriptHandler {

    public static final String SCRIPT_NAME = "How to Use Model Modification Operations";

    public static final HowToUseModificationOperationsScript createHowToUseModificationOperationsScript() {
        MclnGraphDesignerView mclnGraphDesignerView = DesignSpaceView.getInstance().getMclnGraphDesignerView();
        return new HowToUseModificationOperationsScript(mclnGraphDesignerView);
    }

    // Operations
    private static final String OPERATION_GENERATE_MODEL = "Generate Model";
    private static final String OPERATION_MOVE_FRAGMENTS = "Move Fragments";
    private static final String OPERATION_MOVE_MODEL = "Move Model";
    private static final String OPERATION_DELETE_MODEL_ELEMENTS = "Delete Model Elements";

    // Step actions and action attributes
    private static final String STEP_CLICK_MENU_BUTTON = "Click menu button";
    private static final String STEP_CLICK_TOOLBAR_BUTTON = "Click toolbar button";
    private static final String STEP_PICK_UP_ELEMENT_TO_MOVE = "Pick up element to move";
    private static final String STEP_CLICK_ON_CURRENT_POINT_AGAIN = "Click on current point again";
    private static final String STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION = "Move mouse to specified location";
    private static final String STEP_PICK_UP_NODE_TO_DELETE = "Pick up node to delete";
    private static final String STEP_PICK_UP_ARC_TO_DELETE = "Pick up arc to delete";

    //   ===============================================================================================================
    //   M e s s a g e s
    //   ===============================================================================================================

    private static final String HOW_TO_USE_MODIFICATION_OPERATIONS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Presentation:</font>&nbsp; ").
            append("How to Use Model Modification Operations ").
            append("</div></html>").toString();

    private static final String GENERATING_MODEL_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Three Rules</font> menu is selected <br>and example model is generated. ").
            append("</div></html>").toString();

    private static final String MOVING_MODEL_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Move Entire Model</font> menu is selected and <br>four model translations presented. ").
            append("</div></html>").toString();

    private static final String MOVING_FRAGMENT_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Move Model Fragments</font> menu is selected.<br> ").
            append("Then a fragment of the model is highlighted and moved apart of the model. ").
            append("</div></html>").toString();

    private static final String DELETING_MODEL_ELEMENTS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Delete Model Elements</font> menu is selected <br>and some nodes and arcs are deleted. ").
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

            //   ======================================================================================================
            //   Message: How to Use Model Modification Operations
            //   ======================================================================================================

            {SET_CURSOR_TO_HOME_LOCATION, "", ""},
            {PAUSE, "1", ""},
            {INFO_MESSAGE, HOW_TO_USE_MODIFICATION_OPERATIONS_MESSAGE, SCRIPT_NAME_MESSAGE_BACKGROUND,
                    "0xFFFFFF", MESSAGE_EXPOSURE_SHORT, SKIP_STEP_FOR_INDIVIDUAL_SHOW},
            {PAUSE, "1", "", "", "", SKIP_STEP_FOR_INDIVIDUAL_SHOW},

            //   ======================================================================================================
            //   Operation: Set Development mode
            //   ======================================================================================================

            {INFO_MESSAGE, SET_DEVELOPMENT_MODE_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {SET_DEVELOPMENT_MODE, STEP_CLICK_TOOLBAR_BUTTON, AppController.MENU_ITEM_SET_DEVELOPMENT_MODE, "1"},
            {PAUSE, "1", ""},

            //   =======================================================================================================
            //   G e n e r a t i n g   e x a m p l e   m o d e l
            //   =======================================================================================================

            {INFO_MESSAGE, GENERATING_MODEL_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_GENERATE_MODEL, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_EXAMPLES,
                    AppController.DEMO_PROJECT_THREE_RULES},
            {PAUSE, "1", ""},

            //   =======================================================================================================
            //   M o v i n g   m o d e l
            //   =======================================================================================================

            {INFO_MESSAGE, MOVING_MODEL_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_MODIFICATION,
                    AppController.MENU_ITEM_MOVE_MODEL},
            {PAUSE, "1", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-4:-10", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "5:-5", "DRAG"},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-13:-0", "DRAG"},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-13:-19", "DRAG"},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-4:-10", "DRAG"},
            {PAUSE, "2", ""},

            //   =======================================================================================================
            //   M o v i n g   f r a g m e n t
            //   =======================================================================================================

            // Generating model fragments
            {INFO_MESSAGE, MOVING_FRAGMENT_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_MODIFICATION,
                    AppController.MENU_ITEM_MOVE_FRAGMENT},
            {PAUSE, "1", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_PICK_UP_ELEMENT_TO_MOVE, "S-0000004", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_PICK_UP_ELEMENT_TO_MOVE, "S-0000003", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_PICK_UP_ELEMENT_TO_MOVE, "S-0000002", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_PICK_UP_ELEMENT_TO_MOVE, "C-0000001", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-11:5", "DRAG"},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-11:15", "DRAG"},
            {OPERATION_MOVE_FRAGMENTS, STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION, "-8:14", ""},
            {OPERATION_MOVE_FRAGMENTS, STEP_CLICK_ON_CURRENT_POINT_AGAIN, "", ""},
            {PAUSE, "2", ""},

            //   =======================================================================================================
            //   D e l e t i n g   m o d e l   e l e m e n t s
            //   =======================================================================================================

            // Deleting model elements
            {INFO_MESSAGE, DELETING_MODEL_ELEMENTS_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF",
                    MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_MODIFICATION,
                    AppController.MENU_ITEM_DELETE_ELEMENT},
            {PAUSE, "1", ""},

            // deleting Properties
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_NODE_TO_DELETE, "S-0000008", ""},
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_NODE_TO_DELETE, "S-0000010", ""},
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_NODE_TO_DELETE, "S-0000009", ""},
            // deleting arcs
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_ARC_TO_DELETE, "A-0000005", "ARC"},
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_ARC_TO_DELETE, "A-0000009", "ARC"},
            // deleting Conditions
            {OPERATION_DELETE_MODEL_ELEMENTS, STEP_PICK_UP_NODE_TO_DELETE, "C-0000002", ""},
            {PAUSE, "2", ""},

            //   ======================================================================================================
    };


    private PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback;

    /**
     * @param mclnGraphDesignerView
     */
    HowToUseModificationOperationsScript(MclnGraphDesignerView mclnGraphDesignerView) {
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

            case HowToUseCreateOperationsScript.SET_DEVELOPMENT_MODE:
                if (AppController.getInstance().isInDevelopmentMode()) {
                    scriptStepCompletionFeedback.done();
                    break;
                }
                executeOperationSetDevelopmentMode(scriptItem);
                break;

            case SET_CURSOR_TO_HOME_LOCATION:
                executeOperationSetCursorToHomeLocation(scriptItem);
                break;

            case OPERATION_GENERATE_MODEL:
                executeOperationMoveFragment(scriptItem);
                break;

            case OPERATION_MOVE_FRAGMENTS:
                executeOperationMoveFragment(scriptItem);
                break;

            case OPERATION_DELETE_MODEL_ELEMENTS:
                deleteModelElementsOperationExecutor(scriptItem);
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
    //   M o v e   m o d e l   o r   m o d e l   f r a g m e n t s   a n i m a t o r
    //

    private void executeOperationMoveFragment(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_ELEMENT_TO_MOVE.equals(scriptItem.getStepAction())) {
            currentTickExecutor = findAndPickUpMclnGraphNodeViewExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICK_ON_CURRENT_POINT_AGAIN.equals(scriptItem.getStepAction())) {
            currentTickExecutor = clickOnCurrentPointExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_MOVE_MOUSE_TO_SPECIFIED_LOCATION.equals(scriptItem.getStepAction())) {
            currentTickExecutor = moveMouseToSpecifiedLocationExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    //
    //   M o v e   m o d e l   o r   m o d e l   f r a g m e n t s   a n i m a t o r
    //

    private void deleteModelElementsOperationExecutor(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_NODE_TO_DELETE.equals(scriptItem.getStepAction())) {
            currentTickExecutor = findPickUpAndDeleteGraphEntityExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_ARC_TO_DELETE.equals(scriptItem.getStepAction())) {
            currentTickExecutor = findAndPickUpMclnGraphArcViewExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICK_ON_CURRENT_POINT_AGAIN.equals(scriptItem.getStepAction())) {
            currentTickExecutor = clickOnCurrentPointExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    //
    //   Find and Pick Up NcLN Graph Node
    //

    private MclnGraphViewNode mclnGraphNode;
    private int[] nodeIntScrLocation;
    AnimatingTickExecutor findPickUpAndDeleteGraphEntityExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String inputNodeID = scriptItem.getActionAttribute1();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                mclnGraphNode = mclnGraphDesignerView.getMclnNodeByID(inputNodeID);
                if (mclnGraphNode != null) {

                    double[] nodeScrLocation = mclnGraphNode.getScrPnt();
                    int[] nodeIntViewLocation = mclnGraphDesignerView.doubleVec3ToInt(nodeScrLocation);
                    Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();
                    int[] viewLocationOnScreenVec = new int[]{viewLocationOnScreen.x, viewLocationOnScreen.y, 0};
                    nodeIntScrLocation = VAlgebra.addVec3(viewLocationOnScreenVec, nodeIntViewLocation);


                    double[] currentScreenPoint = new double[]{nodeIntScrLocation[0], nodeIntScrLocation[1], 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = currentScreenPoint;

                    nextTickToExecute++;
                }
                break;
            case 2:
                /*
                    Clicking on Property Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;
            case 3:
                /*
                    Clicking one more time
                 */
                doMouseClick();
                nextTickToExecute = 100;
                break;
            case 100:
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

//    MclnGraphViewNode mclnGraphFirstNode;
//    int[] firstNodeIntScrLocation;
//    MclnGraphViewNode mclnGraphSecondNode;
//    int[] secondNodeIntScrLocation;

    protected void executeOperationSetDevelopmentMode(ScriptItem scriptItem) {
        if (STEP_CLICK_TOOLBAR_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = clickOnToolBarButtonExecutor;
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
