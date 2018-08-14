package dsdsse.animation;

import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;
import mclnview.graphview.MclnGraphNodeView;
import mclnview.graphview.MclnPropertyView;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created by Admin on 6/15/2017.
 */
public class HowToUseCreateOperationsScript extends PresentationScriptHandler {

    public static final String SCRIPT_NAME = "How to Use Model Creation Operations";

    public static final HowToUseCreateOperationsScript createHowToUseCreateOperationsScript() {
        MclnGraphDesignerView mclnGraphDesignerView = DesignSpaceView.getInstance().getMclnGraphDesignerView();
        return new HowToUseCreateOperationsScript(mclnGraphDesignerView);
    }

    // Operations
    private static final String OPERATION_CREATE_PROPERTIES = "Create Property";
    private static final String OPERATION_CREATE_CONDITIONS = "Create Conditions";
    private static final String OPERATION_CREATE_ARCS = "Create Arcs";
    private static final String OPERATION_CREATE_FRAGMENTS = "Create Fragments";

    // Step actions and action attributes
    private static final String STEP_CLICK_MENU_BUTTON = "Click menu button";
    private static final String STEP_PICK_UP_PROPERTY_LOCATION = "Pick up Property location";
    private static final String STEP_PICK_UP_CONDITION_LOCATION = "Pick up Condition location";
    private static final String STEP_CLICK_TOOLBAR_BUTTON = "Click toolbar button";
    private static final String STEP_CREATE_FIRST_ARC = "Create first arc";
    private static final String STEP_CREATE_SECOND_ARC = "Create second arc";


    private static final String STEP_FIND_FIRST_PROPERTY_LOCATION = "Pick up first Property location";
    private static final String STEP_FIND_SECOND_PROPERTY_LOCATION = "Pick up second Property location";

    //   ===============================================================================================================
    //   M e s s a g e s
    //   ===============================================================================================================

    private static final String HOW_TO_USE_CREATE_OPERATIONS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Presentation:</font>&nbsp; ").
            append("How to Use Model Creation Operations ").
            append("</div></html>").toString();

    private static final String CREATING_PROPERTIES_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Create Property Nodes</font> menu is selected <br>and four Property nodes are created. ").
            append("</div></html>").toString();

    private static final String CREATING_CONDITIONS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Create Condition Nodes</font> menu is selected <br>and one Condition node is created. ").
            append("</div></html>").toString();

    private static final String CREATING_ARCS_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Create Arcs</font> menu is selected <br>and four Arcs are created. ").
            append("</div></html>").toString();

    private static final String CREATING_FRAGMENT_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("<font color=#FFDD00>Create Fragments</font> menu is selected. <br>A fragment of two Properties and one Condition is created. ").
            append("</div></html>").toString();

    private static final String CLEANUP_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\" size=\"6\">Agenda:</font> ").
            append("Now the last selected operation is unselected and created model is destroyed. ").
            append("</div></html>").toString();

    //   ==============================================================================================================
    //   S c r i p t
    //   ==============================================================================================================

    private static final String[][] demoScriptData = {

            //   ======================================================================================================
            //   Message: How to Use Model Creation Operations
            //   ======================================================================================================

            {SET_CURSOR_TO_HOME_LOCATION, "", ""},
            {PAUSE, "1", ""},
            {INFO_MESSAGE, HOW_TO_USE_CREATE_OPERATIONS_MESSAGE, SCRIPT_NAME_MESSAGE_BACKGROUND,
                    "0xFFFFFF", MESSAGE_EXPOSURE_SHORT, SKIP_STEP_FOR_INDIVIDUAL_SHOW},
            {PAUSE, "1", "", "", "", SKIP_STEP_FOR_INDIVIDUAL_SHOW},

            //   ======================================================================================================
            //   Operation: Set Development mode
            //   ======================================================================================================

//            {INFO_MESSAGE, SET_DEVELOPMENT_MODE_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT
//            },
//            {PAUSE, "1", ""
//            },
//            {SET_DEVELOPMENT_MODE, STEP_CLICK_TOOLBAR_BUTTON, AppController.MENU_ITEM_SET_DEVELOPMENT_MODE, "1"
//            },
//            {PAUSE, "1", ""
//            },

            //   ======================================================================================================
            //   Operation: Create Properties
            //   ======================================================================================================

            {PAUSE, "1", ""},
            {INFO_MESSAGE, CREATING_PROPERTIES_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_CREATE_PROPERTIES, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_CREATION_MODE,
                    AppController.MENU_ITEM_CREATE_PROPERTIES},
            {OPERATION_CREATE_PROPERTIES, STEP_PICK_UP_PROPERTY_LOCATION, "FIRST", "-5:4"},
            {OPERATION_CREATE_PROPERTIES, STEP_PICK_UP_PROPERTY_LOCATION, "SECOND", "5:4"},
            {OPERATION_CREATE_PROPERTIES, STEP_PICK_UP_PROPERTY_LOCATION, "THIRD", "-8:-4"},
            {OPERATION_CREATE_PROPERTIES, STEP_PICK_UP_PROPERTY_LOCATION, "FOURTH", "8:-4"},
            {PAUSE, "1", ""},

            //   ======================================================================================================
            //   Operation: Create Conditions
            //   ======================================================================================================

            {INFO_MESSAGE, CREATING_CONDITIONS_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_CREATE_CONDITIONS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_CREATION_MODE,
                    AppController.MENU_ITEM_CREATE_CONDITIONS},
            {OPERATION_CREATE_CONDITIONS, STEP_PICK_UP_CONDITION_LOCATION, "FIRST", "0:10"},
            {PAUSE, "1", ""},

            //   ======================================================================================================
            //   Operation: Creating Arcs
            //   ======================================================================================================

            {INFO_MESSAGE, CREATING_ARCS_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_CREATE_ARCS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_CREATION_MODE,
                    AppController.MENU_ITEM_CREATE_SPLINE_ARCS},
            {OPERATION_CREATE_ARCS, STEP_CREATE_FIRST_ARC, "S-0000002", "C-0000001"},
            {OPERATION_CREATE_ARCS, STEP_CREATE_SECOND_ARC, "S-0000002", "C-0000001", },
            {OPERATION_CREATE_ARCS, STEP_CREATE_FIRST_ARC, "C-0000001", "S-0000001"},
            {OPERATION_CREATE_ARCS, STEP_CREATE_SECOND_ARC, "C-0000001", "S-0000001"},
            {PAUSE, "1", ""},

            //   ======================================================================================================
            //   Operation: Creating Fragments
            //   ======================================================================================================

            {INFO_MESSAGE, CREATING_FRAGMENT_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_SHORT},
            {PAUSE, "1", ""},
            {OPERATION_CREATE_FRAGMENTS, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_CREATION_MODE,
                    AppController.MENU_ITEM_CREATE_FRAGMENTS},
            {OPERATION_CREATE_FRAGMENTS, STEP_FIND_FIRST_PROPERTY_LOCATION, "S-0000004", ""},
            {OPERATION_CREATE_FRAGMENTS, STEP_FIND_SECOND_PROPERTY_LOCATION, "S-0000003", ""},
            {OPERATION_CREATE_FRAGMENTS, STEP_PICK_UP_CONDITION_LOCATION, "FRAGMENT CONDITION", "0:-10"},
            {PAUSE, "2", ""},
            //   ======================================================================================================
    };

    //
    //
    //

    private PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback;

    /**
     * @param mclnGraphDesignerView
     */
    HowToUseCreateOperationsScript(MclnGraphDesignerView mclnGraphDesignerView) {
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

    @Override
    boolean isIntroductory() {
        return true;
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

            case SET_DEVELOPMENT_MODE:
                if (AppController.getInstance().isInDevelopmentMode()) {
                    scriptStepCompletionFeedback.done();
                    break;
                }
                executeOperationSetDevelopmentMode(scriptItem);
                break;

            case SET_CURSOR_TO_HOME_LOCATION:
                executeOperationSetCursorToHomeLocation(scriptItem);
                break;

            case OPERATION_CREATE_PROPERTIES:
                executeOperationCreateProperty(scriptItem);
                break;

            case OPERATION_CREATE_CONDITIONS:
                executeOperationCreateCondition(scriptItem);
                break;

            case OPERATION_CREATE_ARCS:
                executeOperationCreateArc(scriptItem);
                break;

            case OPERATION_CREATE_FRAGMENTS:
                executeOperationCreateFragment(scriptItem);
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
    //   P r o p e r t y   c r e a t i o n   a n i m a t o r
    //

    private void executeOperationCreateProperty(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_PROPERTY_LOCATION.equals(scriptItem.getStepAction())) {
            currentTickExecutor = moveToAndClickOnExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    //
    //   C o n d i t i o n   c r e a t i o n   a n i m a t o r
    //

    private void executeOperationCreateCondition(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_CONDITION_LOCATION.equals(scriptItem.getStepAction())) {
            currentTickExecutor = moveToAndClickOnExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    //
    //   A r c   c r e a t i o n   a n i m a t o r
    //

    MclnGraphNodeView mclnGraphFirstNode;
    int[] firstNodeIntScrLocation;
    MclnGraphNodeView mclnGraphSecondNode;
    int[] secondNodeIntScrLocation;

    private void executeOperationCreateArc(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CREATE_FIRST_ARC.equals(scriptItem.getStepAction())) {
            currentTickExecutor = creatingFirstArcsFromPropertyExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CREATE_SECOND_ARC.equals(scriptItem.getStepAction())) {
            currentTickExecutor = creatingSecondArcsFromPropertyExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }


    //
    //   Creating Arcs
    //
    MclnGraphNodeView mclnGraphViewInputNode;
    MclnGraphNodeView mclnGraphViewOutputNode;
    int[] inpNodeIntScrLocation;
    int[] outNodeIntScrLocation;
    //    int[] intVec = {0, 0, 0};
    AnimatingTickExecutor creatingFirstArcsFromPropertyExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String inpNodeID = scriptItem.getActionAttribute1();
        String outNodeID = scriptItem.getActionAttribute2();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                if (executionCanceled) {
                    nextTickToExecute = 100;
                    break;
                }
                mclnGraphViewInputNode = mclnGraphDesignerView.getMclnNodeByID(inpNodeID);
                if (mclnGraphViewInputNode != null) {
//
                    double[] nodeScrLocation = mclnGraphViewInputNode.getScrPnt();
                    int[] nodeIntViewLocation = mclnGraphDesignerView.doubleVec3ToInt(nodeScrLocation);
                    Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();
                    int[] viewLocationOnScreenVec = new int[]{viewLocationOnScreen.x, viewLocationOnScreen.y, 0};
                    inpNodeIntScrLocation = VAlgebra.addVec3(viewLocationOnScreenVec, nodeIntViewLocation);

                    double[] currentScreenPoint = new double[]{inpNodeIntScrLocation[0], inpNodeIntScrLocation[1], 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = currentScreenPoint;

                    nextTickToExecute++;
                }

                break;
            case 2:
                if (executionCanceled) {
                    nextTickToExecute = 100;
                    break;
                }
                /*
                    Clicking on Property Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;

            case 3:
                /*
                    Finding Condition node and moving to the node.
                 */
                mclnGraphViewOutputNode = mclnGraphDesignerView.getMclnNodeByID(outNodeID);
                if (mclnGraphViewOutputNode != null) {
                    double[] nodeScrLocation = mclnGraphViewOutputNode.getScrPnt();
                    int[] nodeIntViewLocation = mclnGraphDesignerView.doubleVec3ToInt(nodeScrLocation);
                    Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();
                    int[] viewLocationOnScreenVec = new int[]{viewLocationOnScreen.x, viewLocationOnScreen.y, 0};
                    outNodeIntScrLocation = VAlgebra.addVec3(viewLocationOnScreenVec, nodeIntViewLocation);

                    double[] currentScreenPoint = new double[]{outNodeIntScrLocation[0], outNodeIntScrLocation[1], 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, ARC_CREATION_DELAY);
                    lastScreenPoint = currentScreenPoint;
                    nextTickToExecute++;
                }
                break;
            case 4:
                /*
                    Clicking on Condition Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;
            case 5:
                /*
                    Creating three point arc
                 */
                doMouseClick();
                nextTickToExecute++;
                break;

            case 6:
                /*
                    Creating Arrow location
                 */
                int[] pnt = VAlgebra.linCom3(0.40D, inpNodeIntScrLocation, 0.60D, outNodeIntScrLocation);

                double[] currentScreenPoint = new double[]{pnt[0], pnt[1], 0};
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, ARC_CREATION_DELAY);
                lastScreenPoint = currentScreenPoint;
                nextTickToExecute++;
                break;
            case 7:
                /*
                    First Arc created.
                 */
                doMouseClick();
                nextTickToExecute = 100;
                break;

            case 100:
                /*
                    Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    /*
       Creating second Arc. Moving to Property Node.
    */
    AnimatingTickExecutor creatingSecondArcsFromPropertyExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                if (executionCanceled) {
                    nextTickToExecute = 100;
                    break;
                }
               /*
                    Moving to Property Node.
                */
                double[] currentScreenPoint = new double[]{inpNodeIntScrLocation[0], inpNodeIntScrLocation[1], 0};
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                lastScreenPoint = currentScreenPoint;
                nextTickToExecute++;
                break;
            case 2:
                if (executionCanceled) {
                    nextTickToExecute = 100;
                    break;
                }
                /*
                    Clicking on Property Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;
            case 3:
                /*
                    Moving to output Node.
                 */
                currentScreenPoint = new double[]{outNodeIntScrLocation[0], outNodeIntScrLocation[1], 0};
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, ARC_CREATION_DELAY);
                lastScreenPoint = currentScreenPoint;
                nextTickToExecute++;
                break;
            case 4:
                /*
                    Clicking on output Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;
            case 5:
                /*
                    Creating three point arc
                 */
                double[] zVec = {0, 0, -1};
                double[] inpNodeDblScrLocation = VAlgebra.intVec3ToDouble(inpNodeIntScrLocation);
                double[] outNodeDblScrLocation = VAlgebra.intVec3ToDouble(outNodeIntScrLocation);
                double[] inpToOutVector = VAlgebra.subVec3(outNodeDblScrLocation, inpNodeDblScrLocation);
                double[] inpToOutDirVec = VAlgebra.normalizeVec3(inpToOutVector);
                double[] normalDir = VAlgebra.cross3(inpToOutDirVec, zVec);
                double[] knobLocation = VAlgebra.scaleVec3(normalDir, 60);

                double[] midPointVec = VAlgebra.linCom3((1.D - 0.5), inpNodeDblScrLocation, 0.5D, outNodeDblScrLocation);
                knobLocation = VAlgebra.addVec3(midPointVec, knobLocation);
                moveCursorFromPointAToPointB(lastScreenPoint, knobLocation, ARC_CREATION_DELAY);
                lastScreenPoint = knobLocation;
                nextTickToExecute++;
                break;
            case 6:
                doMouseClick();
                double[] arrowLocatingVector;
                if(mclnGraphViewInputNode instanceof MclnPropertyView){
                    arrowLocatingVector = VAlgebra.initVec3(lastScreenPoint[0] + 60, lastScreenPoint[1] + 30, 0);
                }else {
                    arrowLocatingVector = VAlgebra.initVec3(lastScreenPoint[0] - 60, lastScreenPoint[1] + 30, 0);
                }
                moveCursorFromPointAToPointB(lastScreenPoint, arrowLocatingVector, ARC_CREATION_DELAY);
                lastScreenPoint = arrowLocatingVector;
                nextTickToExecute++;
                break;
            case 7:
                doMouseClick();
                nextTickToExecute = 100;
                break;
            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    //
    //   F r a g m e n t   c r e a t i o n   a n i m a t o r
    //

    private void executeOperationCreateFragment(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_FIND_FIRST_PROPERTY_LOCATION.equals(scriptItem.getStepAction())) {
            currentTickExecutor = findAndPickUpMclnGraphNodeViewExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_FIND_SECOND_PROPERTY_LOCATION.equals(scriptItem.getStepAction())) {

            mclnGraphFirstNode = mclnGraphNode;
            firstNodeIntScrLocation = nodeIntScrLocation;

            currentTickExecutor = findAndPickUpMclnGraphNodeViewExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_PICK_UP_CONDITION_LOCATION.equals(scriptItem.getStepAction())) {

            mclnGraphSecondNode = mclnGraphNode;
            secondNodeIntScrLocation = nodeIntScrLocation;

            currentTickExecutor = moveToAndClickOnExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    protected void executeOperationSetDevelopmentMode(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else if (STEP_CLICK_TOOLBAR_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = clickOnToolBarButtonExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }

    public String toString() {
        return SCRIPT_NAME;
    }
}
