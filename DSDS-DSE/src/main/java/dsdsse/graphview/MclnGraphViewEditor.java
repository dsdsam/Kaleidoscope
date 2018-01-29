package dsdsse.graphview;


import dsdsse.app.AppStateModel;
import dsdsse.app.AppStateModelListener;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.*;
import vw.valgebra.VAlgebra;

import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:49:16 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MclnGraphViewEditor {

    public static final int CREATE_STATE = 0xC0C0C0;
    public static final int NO_THRESHOLD = 0;

    private static MclnGraphViewEditor mclnGraphViewEditor;

    /**
     * @param designSpaceModel
     * @param mclnGraphModel
     * @param mclnGraphDesignerView
     * @return
     */
    public static synchronized MclnGraphViewEditor createInstance(DesignSpaceModel designSpaceModel,
                                                                  MclnGraphModel mclnGraphModel,
                                                                  MclnGraphDesignerView mclnGraphDesignerView) {
        if (MclnGraphViewEditor.mclnGraphViewEditor != null) {
            throw new RuntimeException(
                    "Attempt to create singleton MclnGraphViewEditor instance after it is already created ");
        }
        MclnGraphViewEditor.mclnGraphViewEditor =
                new MclnGraphViewEditor(designSpaceModel, mclnGraphModel, mclnGraphDesignerView);
        return MclnGraphViewEditor.mclnGraphViewEditor;
    }

    /**
     * @return
     */
    public static synchronized MclnGraphViewEditor getInstance() {
        if (MclnGraphViewEditor.mclnGraphViewEditor == null) {
            throw new RuntimeException(
                    "Attempt to get singleton MclnGraphViewEditor instance before it is created ");
        }
        return MclnGraphViewEditor.mclnGraphViewEditor;
    }

    private final ArrowTipLocationPolicy arrowTipLocationPolicy =
            ArrowTipLocationPolicy.DETERMINED_BY_USER;

    private final MclnGraphModel mclnGraphModel;
    private final MclnGraphDesignerView mclnGraphDesignerView;
    AppStateModel.OperationStep previousArcCreationOperationStep;
    AppStateModel.Operation currentOperation = AppStateModel.Operation.NONE;
    AppStateModel.OperationStep currentOperationStep;

    private MclnEditorModel mclnEditorModel;

    // fragment creation data
    private MclnPropertyView curFragmentInpNode;
    private MclnPropertyView curFragmentOutNode;
    private MclnConditionView mclnConditionView;
    private MclnArcView conditionInpArcView;
    private MclnArcView conditionOutArcView;

    // arc creation data
    private MclnGraphViewNode currentArcInputNode;
    private MclnGraphViewNode currentArcOutputNode;
    private MclnArcView currentArc;

    // Dragging Operations data
    boolean dragging;
    MclnGraphViewNode selectedMclnGraphViewNode;
    MclnGraphEntityView currentlyHighlightedMclnGraphEntity;

    // moving graph fragment
    MclnGraphViewNode mclnGraphViewNodeToBeMoved;
    final Set<MclnGraphViewNode> selectedMclnNodesToBeMoved = new LinkedHashSet();
    private final Stack<MclnGraphViewNode> selectedMclnNodesToBeMovedStack = new Stack();
    private final Set<MclnArcView> selectedMclnArcsToBeMoved = new HashSet<>();
    private final Set<MclnArcView> selectedMclnArcsToBeMovedCloned = new HashSet<>();
    private final Set<MclnArcView> selectedArcsThatWillBeDiscarded = new HashSet<>();

    // dragging entire model
    private double translVecStartPnt[] = {0, 0, 0};
    private double translVecEndPnt[] = {0, 0, 0};
    private double translationVec[] = {0, 0, 0};
    boolean snapTranslationVector = true;
    private boolean modelMoved;

    public void setModelMoved(boolean modelMoved) {
        this.modelMoved = modelMoved;
    }

    boolean showDetailsTooltip = false;

    // indicates that current editing operation takes more
    // then one step and can not be interrupted by RMB
    private boolean editingLocked;

    private MclnProject mclnProject;

    private boolean userSelectsArrowLocation = true;
    private int arrowTipSplineScrIndex;

    private final AppStateModelListener appStateModelListener = () -> {
        setEditorOperationAndStep(AppStateModel.getCurrentOperation(), AppStateModel.getCurrentOperationStep());
    };

    /**
     * THe only constructor to instantiate MCLN Editor
     *
     * @param designSpaceModel
     * @param mclnGraphModel
     * @param mclnGraphDesignerView
     */
    private MclnGraphViewEditor(DesignSpaceModel designSpaceModel, MclnGraphModel mclnGraphModel, MclnGraphDesignerView mclnGraphDesignerView) {
        this.mclnGraphModel = mclnGraphModel;
        this.mclnGraphDesignerView = mclnGraphDesignerView;

        mclnProject = designSpaceModel.getMclnProject();
        mclnEditorModel = new MclnEditorModel(this);

        AppStateModel.getInstance().setAppStateModelListener(appStateModelListener);
    }

    /**
     * @param state
     */
    private void setEditingLocked(boolean state) {
        editingLocked = state;
    }

    /**
     * Called from App Model State when model state has changed
     *
     * @param operation
     * @param operationStep
     */
    private void setEditorOperationAndStep(AppStateModel.Operation operation, AppStateModel.OperationStep operationStep) {

        if (this.currentOperation == operation && this.currentOperationStep == operationStep) {
            return;
        }

        setModelMoved(false);
        clearLocalGraphFragmentMovingCollections();
        this.currentOperation = operation;
        this.currentOperationStep = operationStep;
    }

    //
    //   P r o c e s s i n g    T e r m i n a t i o n   O p e r a t i o n s
    //

    /**
     * @param currentOperation
     * @param currentOperationStep
     * @return
     */
    public void terminateCurrentOperation(AppStateModel.Operation currentOperation,
                                          AppStateModel.OperationStep currentOperationStep) {

        if (currentOperation.isCreatingArcs()) {
            switch (currentOperationStep) {
                case PICK_UP_ARC_INPUT_NODE:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
                case PICK_UP_ARC_ONLY_KNOT:
                case PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION:
                case PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE:
                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
                    terminateArcCreation();
                    break;
            }
        } else if (currentOperation.isCreatingFragments()) {
            switch (currentOperationStep) {
                case PICK_UP_FIRST_PROPERTY:
                case PICK_UP_SECOND_PROPERTY:
                case PLACE_CONDITION:
                    terminateFragmentCreation();
                    break;
            }
        } else if (currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
            for (MclnGraphEntityView mclnGraphEntityView : selectedMclnNodesToBeMoved) {
                mclnGraphEntityView.setSelected(false);
                mclnGraphDesignerView.repaintImageAndSpriteEntities();
            }
        }
    }

    /**
     * Terminating Arc creation operation
     */
    private void terminateArcCreation() {
        if (currentArcInputNode != null) {
            currentArcInputNode.setSelected(false);
        }
        if (currentArcOutputNode != null) {
            currentArcOutputNode.setSelected(false);
        }

        // removing arc from model and graph view
        if (currentArc != null) {
            MclnArc mclnArc = currentArc.getMclnArc();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
        }

        currentArcInputNode = null;
        currentArcOutputNode = null;
        currentArc = null;

        currentOperationStep = AppStateModel.OperationStep.CANCELED;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.CANCELED);
    }

    /**
     * Terminating Fragment creation operation
     */
    private void terminateFragmentCreation() {


        if (curFragmentInpNode != null) {
            curFragmentInpNode.setSelected(false);
            mclnGraphDesignerView.paintEntityOnly(curFragmentInpNode);
        }
        if (curFragmentOutNode != null) {
            curFragmentOutNode.setSelected(false);
            mclnGraphDesignerView.paintEntityOnly(curFragmentOutNode);
        }

        // removing arcs from model and graph view
        if (conditionInpArcView != null) {
            MclnArc mclnArc = conditionInpArcView.getMclnArc();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
        }

        if (conditionOutArcView != null) {
            MclnArc mclnArc = conditionOutArcView.getMclnArc();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
        }

        if (mclnConditionView != null) {
            MclnCondition mclnCondition = mclnConditionView.getMclnCondition();
            mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);
        }

        curFragmentInpNode = null;
        curFragmentOutNode = null;
        mclnConditionView = null;
        conditionInpArcView = null;
        conditionOutArcView = null;
    }

    /**
     * @param me
     * @return
     */
    private final boolean isRMBPressed(MouseEvent me) {
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
        return rightMouseButtonPressed;
    }

    //
    //   P r o c e s s i n g    C r e a t i o n    O p e r a t i o n s
    //

    /**
     * @param me
     * @return
     */
    final void createMclnStatement(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();

        // create Node if empty place was picked
        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
        if (mclnGraphEntityView != null) {
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Property Nodes",
                    "The Property node cannot be placed on the top of other entity \"" +
                            mclnGraphEntityView.getEntityTypeAsString() + "\" !");
            return;
        }

        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(x, y);
        createNewStatement(cSysPoint);
    }

    /**
     * @param me
     * @param operation
     */
    final void createMclnGraphCondition(MouseEvent me, AppStateModel.Operation operation) {

        int x = me.getX();
        int y = me.getY();

        // create Node if empty place was picked
        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
        if (mclnGraphEntityView != null) {
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Condition Nodes",
                    "The Condition node cannot be placed on the top of other entity \"" +
                            mclnGraphEntityView.getEntityTypeAsString() + "\" !");
            return;
        }

        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(x, y);
        createNewCondition(cSysPoint);
    }

     /* Creating the fragment

         inpArcList[i]   --> InputNode   outNodeList[i]
                        /                /
               -> Arc --                /
              /                        /
         inpArcList[i]   --> Tran <-- /  outNodeList[i]
                        /                /
               -> Arc --                /
              /                        /
        inpArcList[i]   OutputNode <--/  outNodeList[i]

     */

    /**
     * Creating MCLN Fragment
     *
     * @param me
     * @return
     */
    final void createConditionFragment(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;

        // Reset process if Right Mouse Button pressed
        if (rightMouseButtonPressed) {
            if (currentOperationStep != AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY) {
                if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_SECOND_PROPERTY) {

                    curFragmentInpNode.setSelected(false);
                    mclnGraphDesignerView.paintEntityOnly(curFragmentInpNode);
                    curFragmentInpNode = null;

                } else if (currentOperationStep == AppStateModel.OperationStep.PLACE_CONDITION) {

                    curFragmentInpNode.setSelected(false);
                    mclnGraphDesignerView.paintEntityOnly(curFragmentInpNode);

                    curFragmentOutNode.setSelected(false);
                    mclnGraphDesignerView.paintEntityOnly(curFragmentOutNode);

                    curFragmentInpNode = null;
                    curFragmentOutNode = null;
                }
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY;
                AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY);
            }
            return;
        }

        String FRAGMENT_CREATION_MESSAGE_1 =
                "\" while both selected for creation the fragment nodes are supposed to be Property nodes. Selection ignored.  ";
        String FRAGMENT_CREATION_MESSAGE_2 =
                "Selected for creation the fragment input and output nodes can not be same Property node. Selection ignored.  ";

        if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY) {

            MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
            if (mclnGraphEntityView == null) {
                return;
            }
            if (!mclnGraphEntityView.isPropertyNode()) {
                String message = "Selected element is \"" + mclnGraphEntityView.getEntityTypeAsString() +
                        FRAGMENT_CREATION_MESSAGE_1;
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Model Fragments", message);
                return;
            }
            curFragmentInpNode = mclnGraphEntityView.toPropertyView();

            curFragmentInpNode.setSelected(true);
            mclnGraphDesignerView.setCurFragmentInpNodeToPaintExtras(curFragmentInpNode);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_SECOND_PROPERTY;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_SECOND_PROPERTY);
            return;

        } else if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_SECOND_PROPERTY) {

            MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
            if (mclnGraphEntityView == null) {
                return;
            }
            if (!mclnGraphEntityView.isPropertyNode()) {
                String message = "Selected element is \"" + mclnGraphEntityView.getEntityTypeAsString() +
                        FRAGMENT_CREATION_MESSAGE_1;
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Model Fragments", message);
                return;
            }
            if (curFragmentInpNode == mclnGraphEntityView) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Model Fragments", FRAGMENT_CREATION_MESSAGE_2);
                return;
            }
            curFragmentOutNode = mclnGraphEntityView.toPropertyView();

            curFragmentOutNode.setSelected(true);
            mclnGraphDesignerView.setCurFragmentOutNodeToPaintExtras(curFragmentOutNode);
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_CONDITION_LOCATION;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_CONDITION_LOCATION);
            return;

        } else if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_CONDITION_LOCATION) {

            //
            // creating condition and associated arcs
            //

            MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
            // do nothing if existing entity was picked
            if (mclnGraphEntityView != null) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Model Fragments",
                        "The Condition node cannot be placed on the top of other entity \"" +
                                mclnGraphEntityView.getEntityTypeAsString() + "\" !");
                return;
            }

            double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(x, y);
            mclnConditionView = createNewCondition(cSysPoint);

            // creating the fragment's arcs models and views

            conditionInpArcView = mclnGraphModel.createCompleteMclnArcAndUpdateView(
                    ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION,
                    curFragmentInpNode, mclnConditionView);

            conditionOutArcView = mclnGraphModel.createCompleteMclnArcAndUpdateView(
                    ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION,
                    mclnConditionView, curFragmentOutNode);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY);

            curFragmentInpNode.setSelected(false);
            curFragmentOutNode.setSelected(false);
            mclnConditionView.setMouseHover(false);

            curFragmentInpNode = null;
            curFragmentOutNode = null;
            mclnConditionView = null;
            conditionInpArcView = null;
            conditionOutArcView = null;

            mclnGraphDesignerView.fragmentCreationComplete();
        }
    }

    private boolean isSomethingSelected(int x, int y) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
        return mclnGraphEntityView != null;
    }

    private MclnGraphEntityView getSomethingSelected(int x, int y) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
        return mclnGraphEntityView;
    }

    private MclnGraphViewNode getNodeSelected(int x, int y) {
        MclnGraphViewNode mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
        return mclnGraphViewNode;
    }

    private boolean isNodeSelected(int x, int y) {
        MclnGraphViewNode mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
        return mclnGraphViewNode != null;
    }

    private boolean isArcSelected(int x, int y) {
        MclnArcView mclnArcView = mclnGraphDesignerView.getArcAtCoordinates(x, y);
        return mclnArcView != null;
    }

    private MclnArcView getArcSelected(int x, int y) {
        MclnArcView mclnArcView = mclnGraphDesignerView.getArcAtCoordinates(x, y);
        return mclnArcView;
    }

    private MclnPropertyView isPropertyNodeSelected(int x, int y) {
        MclnPropertyView mclnPropertyView = mclnGraphDesignerView.getPropertyNodeAtCoordinates(x, y);
        return mclnPropertyView;
    }

    //
    //                            T h e   C r e a t i o n   o f   t h e   A r c s
    //
    /*
                                     +----------------------------------------+
                                     |        PICK_UP_ARC_INPUT_NODE          |
                                     +----------------------------------------+
                                                       |
                                                       |
                                     +----------------------------------------+
                        +------------| PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_NODE  |---------+
                        |            +----------------------------------------+         |
                        |                                                               |
                        |                                                               |
          +----------------------------+                        +----------------------------------------+
          | PICK_UP_ARC_ONLY_KNOT      |--------+      +--------|  PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE  |
          +----------------------------+        |      |        +----------------------------------------+
                                                |      |
                                                |      |
                                                |      |        +----------------------------------------+
                                                |      |   +----|  PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION  |
                                                |      |   |    +----------------------------------------+
                                                |      |   |
                                 +------------------------------------------------+
                                 |  finish Arc Creation And Add It To Mcln Model  |
                                 +------------------------------------------------+

     */

    /**
     * @param me
     * @param operation
     * @param mouseEventType
     */
    final void processArcCreation(MouseEvent me, AppStateModel.Operation operation, int mouseEventType) {

        if (mouseEventType == MouseEvent.MOUSE_PRESSED) {
//            System.out.println("processArcCreation: " + currentOperationStep);
            switch (currentOperationStep) {
                case PICK_UP_ARC_INPUT_NODE:
                    pickUpArcInputNode(me);
                    break;
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
                    pickUpArcFirstKnotOrOutputNode(me);
                    break;
                case PICK_UP_ARC_ONLY_KNOT:
                    pickUpArcOnlyKnotOrOutputNodeAgain(me);
                    break;
                case PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE:
                    pickUpArcNextKnotOrOutputNode(me);
                    break;
                case PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION:
                    // This call is needed as user may click on just created knot
                    // without moving the mose
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSpline(me, true);
                    takeThreeKnotArcArrowTipIndexAndFinishArcCreation(me);
                    break;
                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
                    takeMultiKnotArcArrowTipIndexAndFinishArcCreation(me);
                    break;
            }
        } else if (mouseEventType == MouseEvent.MOUSE_MOVED) {
//            System.out.println("Mouse moving x = " + me.getX() + ",  y = " + me.getY());
            switch (currentOperationStep) {
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
                case PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE:
                case PICK_UP_ARC_ONLY_KNOT:
//                    System.out.println("Mouse moving x = " + me.getX() + ",  y = " + me.getY());
                    moveArcActivePoint(me);
                    break;
                case PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION:
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSpline(me, true);
                    break;
                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSpline(me, true);
                    break;
            }
        }
    }

    /**
     * @param me
     */
    private void moveArcActivePoint(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
//        System.out.println("moveArcActivePoint");
    }

    /**
     * @param me
     */
    private void pickUpArcInputNode(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        if (((currentArcInputNode = getNodeSelected(x, y)) == null)) {
            return;
        }

        // Statement or Condition was selected - make one-Node arc
        currentArc = mclnGraphModel.createIncompleteMclnArcAndUpdateView(arrowTipLocationPolicy, currentArcInputNode);

        currentArc.setUnderConstruction(true);

        currentArc.setHighlightFlatSegments(false);

        currentArcInputNode.setSelected(true);
        mclnGraphDesignerView.setArcInputNodeWhileCreatingArc(currentArcInputNode);

        // resetting arc presentation
        currentArc.setSelected(true);
        currentArc.setSplineThreadSelected(true);
        currentArc.setDrawKnots(true);
        currentArc.setAllKnotsSelected(false);

        currentArcOutputNode = null;
        if (currentArcInputNode.isPropertyNode()) {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
        } else {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
        }
        AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
        return;
    }

//    private boolean canKnotBeCreated(MclnGraphEntityView mclnGraphEntityView) {
//        if (mclnGraphEntityView != null) {
//            AdfEnv.messageDlg(mclnGraphView, 'M', "Creation connecting Arc knots",
//                    "Arc knot can not be placed on the top of another knot or any other graph entity. Please, select another location. ");
//            return false;
//        }
//        return true;
//    }

    /**
     * @param me
     */
    private void pickUpArcFirstKnotOrOutputNode(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();

        if (isRMBPressed(me)) { // done

            // Undo first step - unselect first node

            currentArcInputNode.setSelected(false);
            mclnGraphDesignerView.paintEntityOnly(currentArcInputNode);

            MclnArc mclnArc = currentArc.getMclnArc();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);

            currentArcInputNode = null;
            currentArcOutputNode = null;
            currentArc = null;

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
            return;
        }

        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
        // try if node or condition picked
        if (mclnGraphEntityView != null) {
            if (!isSelectedOutputNodeAllowed(mclnGraphEntityView)) {
                return;
            }
            // setting arc presentation
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(true);
            currentArc.setDrawKnots(true);
            currentArc.setAllKnotsSelected(false);
            currentArc.setSelectedKnotInd(-1);

            // second node was piked
            currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                    mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
            currentArcOutputNode.setSelected(true);
            currentArc.setOutputNode(currentArcOutputNode);

            //
            // intermediate knot will be moved
            //

            double[] outNodeScrPoint = currentArcOutputNode.getScrPnt();
            currentArc.addNextScrKnotAndMakePreviousKnotActive(outNodeScrPoint[0], outNodeScrPoint[1]);

            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(currentArc);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_ONLY_KNOT;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_ONLY_KNOT);
            System.out.println("MclnGraphViewEditor: second node was piked");

        } else {

            // the arc first knot location piked

            // setting arc presentation
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(true);
            currentArc.setDrawKnots(true);
            currentArc.setAllKnotsSelected(false);
            currentArc.setSelectedKnotInd(-1);

            currentArc.addNextScrKnotAndMakeItActive(x, y);
            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(currentArc);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE);
            System.out.println("MclnGraphViewEditor: first knot was piked");
        }
    }

    /**
     * @param me
     */
    private void pickUpArcOnlyKnotOrOutputNodeAgain(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();

        if (isRMBPressed(me)) {  // done

            // Undo operation if RMB pressed

            currentArc.resetArcSpline();

            // resetting arc presentation
            currentArc.removeOutputNode();
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(true);
            currentArc.setDrawKnots(true);
            currentArc.setAllKnotsSelected(false);

            // unselect output node
            currentArcOutputNode.setSelected(false);
            currentArcOutputNode = null;

            mclnGraphDesignerView.repaintImageAndSpriteEntities();
//
            if (currentArcInputNode.isPropertyNode()) {
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
            } else {
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
            }
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);

            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
            return;
        }

        // check if the graph node is picked
        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
        // try if node or condition picked
        if (mclnGraphEntityView != null) {

            // ignore selection if other than output node picked
            if (mclnGraphEntityView != currentArcOutputNode) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                        "At this step either an empty space to create arc knot or already selected output node can be clicked. Selection ignored. ");
                return;
            }

            //
            // other node picked again -> make straight three point arc
            //

            double[] middleKnotScrLocation = calculateKnobScrLocation(currentArcInputNode, currentArcOutputNode, true);
            currentArc.makeThreePointArc(middleKnotScrLocation);
            boolean arrowTipFound = checkIfArrowTipCanBeFound();
            if (!arrowTipFound) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                        DsdsDseMessagesAndDialogs.MESSAGE_STRAIGHT_3POINT_ARC_UNDO);
//                threePointArcUnDoStraightChoice(me, mclnGraphView, currentArc, currentArcInputNode, currentArcOutputNode);
                return;
            }
        } else {
            boolean arrowTipFound = checkIfArrowTipCanBeFound();
            if (!arrowTipFound) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                        DsdsDseMessagesAndDialogs.MESSAGE_3POINT_ARC_UNDO);
                return;
            }
        }
        //
        // Three knots arc created. User selects arrow tip location
        // Creation schema:
        // a) Click on Input Arc
        // b) Click on Output Arc
        // c) Click on location of only knot
        // d) Creation completed

        // resetting arc presentation
        currentArc.setSelected(true);
        currentArc.setSplineThreadSelected(true);
        currentArc.setDrawKnots(true);
        currentArc.setAllKnotsSelected(false);

        currentArc.setHighlightFlatSegments(true);
        currentArc.setSelectingArrowTipLocation(true);
        currentArc.setHighlightArcKnotsForArrowTipSelection(true);

        previousArcCreationOperationStep = AppStateModel.getInstance().getCurrentOperationStep();
        currentOperationStep = AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION);
    }


    /**
     * @param me
     */
    private void pickUpArcNextKnotOrOutputNode(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;

        //
        // Undo operation if RMB pressed
        //
        if (rightMouseButtonPressed) {  // done

            int nPnts = currentArc.deleteLastPoint();
            if (nPnts == 2) {
                if (currentArcInputNode.isPropertyNode()) {
                    currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
                } else {
                    currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
                }
                AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
            }
            currentArc.setSplineThreadSelected(true);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
            return;
        }

        //
        //  do the step
        //

        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);
        // try if node or condition picked
        if (mclnGraphEntityView == null) {

            //
            // Knot location piked
            //

            currentArc.setSplineThreadSelected(true);
//            System.out.println(" Knot location piked  ");
            currentArc.addNextScrKnotAndMakeItActive(x, y);
            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(currentArc);
            return;
        }

        // wrong node selected
        if (!isSelectedOutputNodeAllowed(mclnGraphEntityView)) {
            return;
        }

        //
        // The Node was selected - take the arc Knob location
        //

        currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
        currentArcOutputNode.setSelected(true);
        currentArc.setOutputNode(currentArcOutputNode);

        int nPnts = currentArc.getNumberOfKnots();

        boolean arrowTipFound = checkIfArrowTipCanBeFound();
        if (!arrowTipFound) {
            String message = nPnts == 3 ?
                    DsdsDseMessagesAndDialogs.MESSAGE_3POINT_ARC_UNDO :
                    DsdsDseMessagesAndDialogs.MESSAGE_MULTI_KNOT_ARC_UNDO;
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs", message);

            // remove just added output node
            currentArcOutputNode.setSelected(false);
            currentArc.removeOutputNode();
            currentArcOutputNode = null;
            return;
        }

        if (nPnts == 3) {

            //
            // Three knots arc created. Find arrow tip location
            //

            // resetting arc presentation
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(false);
            currentArc.setDrawKnots(true);
            currentArc.setAllKnotsSelected(false);

            currentArc.setSelectingArrowTipLocation(true);
            currentArc.setHighlightArcKnotsForArrowTipSelection(true);
            currentArc.setHighlightFlatSegments(true);

            previousArcCreationOperationStep = AppStateModel.getInstance().getCurrentOperationStep();
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION);

        } else {

            //
            // Multi knot arc created.
            //

            // resetting arc presentation
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(false);
            currentArc.setDrawKnots(true);
            currentArc.setAllKnotsSelected(false);

            currentArc.setSelectingArrowTipLocation(true);
            currentArc.setHighlightArcKnotsForArrowTipSelection(true);
            currentArc.setHighlightFlatSegments(true);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION);
        }

    }

    /**
     * @param me
     */
    private void takeThreeKnotArcArrowTipIndexAndFinishArcCreation(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;

        //
        // Undo operation if RMB pressed
        //
        if (rightMouseButtonPressed) {  // done

            if (previousArcCreationOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_ONLY_KNOT) {

                // do nothing here

            } else if (previousArcCreationOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE) {
                currentArcOutputNode.setSelected(false);
                currentArc.removeOutputNode();
                currentArcOutputNode = null;
            }

            currentOperationStep = previousArcCreationOperationStep;
            AppStateModel.getInstance().setCurrentOperationStep(previousArcCreationOperationStep);

            currentArc.setDrawKnotBoxesFlag(false);
            mclnGraphDesignerView.eraseArcOnlyFromImageAndCallRepaint(currentArc);

            // resetting arc presentation
            currentArc.setDrawKnots(true);
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(true);
            currentArc.setAllKnotsSelected(false);

            currentArc.setSelectingArrowTipLocation(false);
            currentArc.setHighlightArcKnotsForArrowTipSelection(false);
            currentArc.setHighlightFlatSegments(false);

            currentArc.destroyArcArrowUpOnUndoingArrowTipSelection();
            currentArc.setHighlightArcKnotsForArrowTipSelection(false);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
            return;
        }

        // Analyzing where the pick was done

        // checking if user selects Arrow location
        // and mouse is at the user selected location
        if (!userSelectsArrowLocation || arrowTipSplineScrIndex < 0) {
            // Wrong selection - ignore the pick
            return;
        }

        currentArc.setSelectedKnotInd(-1);
        finishArcCreationAndAddItToMclnModel();
    }

    /**
     * @return
     */
    private boolean checkIfArrowTipCanBeFound() {
        boolean arrowTipFound = currentArc.checkIfArrowTipCanBeFound();
        return arrowTipFound;
    }


    /**
     * @param me
     * @return
     */
    private int findArrowTipIndexOnTheSpline(MouseEvent me, boolean userSelectsArrowLocation) {
        int x = me.getX();
        int y = me.getY();
        int arrowTipSplineScrIndex = currentArc.findArrowTipIndexOnTheSpline(x, y, userSelectsArrowLocation);
        mclnGraphDesignerView.paintArcArrowWhileIsBeingCreatedOnScreen(currentArc);
        return arrowTipSplineScrIndex;
    }

    /**
     *
     */
    /**
     * @param me
     */
    private void takeMultiKnotArcArrowTipIndexAndFinishArcCreation(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;

        //
        // Undo operation if RMB pressed
        //

        if (rightMouseButtonPressed) {   // done

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE);

            currentArcOutputNode.setSelected(false);

            currentArc.removeOutputNode();
            currentArcOutputNode = null;
            currentArc.setDrawKnotBoxesFlag(false);
            mclnGraphDesignerView.eraseArcOnlyFromImageAndCallRepaint(currentArc);

            // resetting arc presentation
            currentArc.setDrawKnots(true);
            currentArc.setSelected(true);
            currentArc.setSplineThreadSelected(false);
            currentArc.setAllKnotsSelected(false);

            currentArc.setSelectingArrowTipLocation(false);
            currentArc.setHighlightArcKnotsForArrowTipSelection(false);
            currentArc.setHighlightFlatSegments(false);

            currentArc.destroyArcArrowUpOnUndoingArrowTipSelection();
            currentArc.setHighlightArcKnotsForArrowTipSelection(false);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
            return;
        }

        // Analyzing where the pick was done

        // checking if user selects Arrow location
        // and mouse is at the user selected location
        if (!userSelectsArrowLocation || arrowTipSplineScrIndex < 0) {
            // Wrong selection - ignore the pick
            return;
        }

        // user selected Arrow location
        setUserSelectedArrowTipSplineScrIndex(arrowTipSplineScrIndex);
        finishArcCreationAndAddItToMclnModel();
    }

    /**
     * @param autoSetKnob
     */
    private void takeKnobAndFinishArcCreation(boolean autoSetKnob) {
        if (autoSetKnob) {
            int nPnts = currentArc.getNumberOfKnots();
            int knobInd = ((nPnts - 2) / 2) + 1;
            currentArc.setKnobIndex(knobInd);
        } else {
            currentArc.setKnobIndex(currentArc.getSelectedKnotIndex());
            currentArc.setSelectedKnotInd(-1);
        }
    }

    /**
     * @param arrowTipSplineScrIndex
     */
    private void setUserSelectedArrowTipSplineScrIndex(int arrowTipSplineScrIndex) {
        currentArc.setKnobIndex(arrowTipSplineScrIndex);
        currentArc.setSelectedKnotInd(-1);
    }

    /**
     * Called:
     * 1) when straight or curved arc with only one knot (knob) arc designed in steps:
     * a) input node picked - output node picked - only knot (knob) location picked
     * b) input node picked - output node picked - output node picked again (straight arc created)
     * c) input node picked - nly knot (knob) location picked - output node picked
     * 2) after multi-knot arc designed and knob location selected
     */
    private void finishArcCreationAndAddItToMclnModel() {

        currentArc.setSelectingArrowTipLocation(false);
        currentArc.setHighlightArcKnotsForArrowTipSelection(false);
        currentArc.setHighlightFlatSegments(false);

        // reset nodes
        currentArcInputNode.setSelected(false);
        currentArcOutputNode.setSelected(false);

        // resetting arc presentation
        currentArc.setDrawKnots(false);
        currentArc.setSelected(false);
        currentArc.setSplineThreadSelected(false);
        currentArc.setAllKnotsSelected(false);
        currentArc.setSelectedKnotInd(-1);

        currentArc.finishArcCreation();

        // updating MclnArc (model)
//        MclnArc mclnArc = currentArc.getMclnArc();
//        MclnGraphModel.getInstance().addMclnArc(mclnArc);

        currentArcInputNode.outArcList.add(currentArc);
        currentArcOutputNode.inpArcList.add(currentArc);

        currentArc.setUnderConstruction(false);

//        mclnGraphModel.registerCompletedMclnArcAndUpdateView(currentArc);
        // remove temporary arc from graph image and paint complete arc (with arrow) on the graph image
        mclnGraphDesignerView.eraseAndPaintArcViewWithConnectedEntities(currentArc);

        currentArcInputNode = null;
        currentArcOutputNode = null;
        currentArc = null;
        setEditingLocked(false);
        currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
    }

    /**
     * @param mclnGraphEntityView
     * @return
     */
    private boolean isSelectedOutputNodeAllowed(MclnGraphEntityView mclnGraphEntityView) {
        if (mclnGraphEntityView.isArc()) {
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                    "Arc can not take another Arc as its input or output node. Selection ignored. ");
            return false;
        }
        if (currentArcInputNode.isPropertyNode()) {
            if (mclnGraphEntityView.isPropertyNode()) {
                DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                        "Selected Arc input node is Property node. Therefore its output node is supposed to be Condition node. Selection ignored. ");
                return false;
            }
        } else if (mclnGraphEntityView.isConditionNode()) {
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs",
                    "Selected Arc input node is Condition node. Therefore its output node is supposed to be Property node. Selection ignored. ");
            return false;
        }
        return true;
    }

    /**
     * @param inpNode
     * @param outNode
     * @param drawKnobAsArrow
     * @return
     */
    private static double[] calculateKnobScrLocation(MclnGraphViewNode inpNode, MclnGraphViewNode outNode,
                                                     boolean drawKnobAsArrow) {

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        double[] arcVec = new double[3];
        double[] dirVec = new double[3];
        double[] inpNodeRadVec = new double[3];
        double[] outNodeRadVec = new double[3];
        double[] newVec = new double[3];
        double[] knobLocation = {0., 0., 0.};

        double end1[] = inpNode.getScrPnt();
        double end2[] = outNode.getScrPnt();
        VAlgebra.subVec3(arcVec, end1, end2);
        VAlgebra.normalizeVec3(arcVec, dirVec);

        VAlgebra.scaleVec3(inpNodeRadVec, inpNodeRadius, dirVec);
        VAlgebra.subVec3(arcVec, arcVec, inpNodeRadVec);
        VAlgebra.scaleVec3(outNodeRadVec, outNodeRadius, dirVec);
        VAlgebra.subVec3(arcVec, arcVec, outNodeRadVec);

        VAlgebra.addVec3(newVec, end2, arcVec);
        if (drawKnobAsArrow) {
            VAlgebra.LinCom3(knobLocation, 0.25, end2, 0.75, newVec);
        } else {
            VAlgebra.LinCom3(knobLocation, 0.50, end2, 0.50, newVec);
        }
        return knobLocation;
    }

    /**
     * @param cSysPoint
     */
    private MclnPropertyView createNewStatement(double[] cSysPoint) {
        MclnPropertyView mclnPropertyView = mclnGraphModel.createMclnStatementAndUpdateView(cSysPoint);
//        mclnPropertyView.setMouseHover(true);
        return mclnPropertyView;
    }

    private MclnConditionView createNewCondition(double[] cSysPoint) {
        MclnConditionView mclnConditionView = mclnGraphModel.createMclnConditionAndUpdateView(cSysPoint);
//        mclnConditionView.setMouseHover(true);
        return mclnConditionView;
    }


    //
    //   M o v i n g   t h e   g r a p h   f r a g m e n t
    //

    boolean isMouseHoveringFragmentNode(MouseEvent me) {

        int mouseX = me.getX();
        int mouseY = me.getY();

        MclnGraphViewNode tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
        if (tmpMclnGraphViewNode == null) {
            return false;
        }

        return selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode);
    }

    /**
     * @param me
     */
    boolean selectAFragmentNodeToBeMoved(MouseEvent me) {

        setModelMoved(false);

        int mouseX = me.getX();
        int mouseY = me.getY();
        MclnGraphViewNode tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
        if (tmpMclnGraphViewNode == null) {
            return false;
        }

        // selecting leading node
        if (selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode)) {

            for (MclnGraphViewNode mclnGraphViewNode : selectedMclnNodesToBeMoved) {
                mclnGraphViewNode.setSelected(false);
            }

            mclnGraphViewNodeToBeMoved = tmpMclnGraphViewNode;

            mclnGraphViewNodeToBeMoved.setSelected(true);

            // repaint selected nodes and arcs
            mclnGraphDesignerView.paintTheModelFragmentOnTheScreen(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMovedCloned,
                    selectedArcsThatWillBeDiscarded);

            double[] scrPnt = mclnGraphViewNodeToBeMoved.getScrPnt();
            VAlgebra.initVec3(translVecStartPnt, scrPnt[0], scrPnt[1], 0d);
            System.out.println("selectAFragmentNodeToBeMoved " + " selected nodes " + selectedMclnNodesToBeMoved.size());
            return true;
        }

        //   adding new Node

        tmpMclnGraphViewNode.setPreSelected(true);
        selectedMclnNodesToBeMoved.add(tmpMclnGraphViewNode);
        selectedMclnNodesToBeMovedStack.push(tmpMclnGraphViewNode);

        GraphAnalyzer.findConnectingArcs(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMoved,
                selectedArcsThatWillBeDiscarded);

        // cloning selected arcs to be moved
        selectedMclnArcsToBeMovedCloned.clear();
        if (selectedMclnArcsToBeMoved.size() > 0) {
            for (MclnArcView mclnArcView : selectedMclnArcsToBeMoved) {
                MclnArcView clonedMclnArcView = mclnArcView.clone();
                selectedMclnArcsToBeMovedCloned.add(clonedMclnArcView);
            }
        }

        // repaint selected nodes and arcs
        mclnGraphDesignerView.paintTheModelFragmentOnTheScreen(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMovedCloned,
                selectedArcsThatWillBeDiscarded);

        return false;
    }

    //
    //  D o   U n d o
    //

    void unselectSelectedNode(MouseEvent me) {
        if (selectedMclnNodesToBeMovedStack.isEmpty()) {
            return;
        }
        int mouseX = me.getX();
        int mouseY = me.getY();
        MclnGraphViewNode tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);

        MclnGraphViewNode mclnGraphViewNodeToUnselect;
        if (tmpMclnGraphViewNode != null && selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode)) {
            boolean removed = selectedMclnNodesToBeMovedStack.remove(tmpMclnGraphViewNode);
            if (!removed) {
                new Exception("Something is wrong, the node " + tmpMclnGraphViewNode.toString() +
                        " should be unselected").printStackTrace();
            }
            mclnGraphViewNodeToUnselect = tmpMclnGraphViewNode;
        } else {
            mclnGraphViewNodeToUnselect = selectedMclnNodesToBeMovedStack.pop();
        }

        selectedMclnNodesToBeMoved.remove(mclnGraphViewNodeToUnselect);
        mclnGraphViewNodeToUnselect.setHighlighted(false);
        mclnGraphViewNodeToUnselect.setPreSelected(false);
        mclnGraphViewNodeToUnselect.setSelected(false);

        // resetting fragment connected arcs as new set of fragment connected arcs will be create
        for (MclnArcView mclnArcView : selectedArcsThatWillBeDiscarded) {
            mclnArcView.setWatermarked(false);
        }
        // here new set of fragment connected arcs will be create
        GraphAnalyzer.findConnectingArcs(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMoved,
                selectedArcsThatWillBeDiscarded);

        // cloning selected arcs
        selectedMclnArcsToBeMovedCloned.clear();
        if (selectedMclnArcsToBeMoved.size() > 0) {
            for (MclnArcView mclnArcView : selectedMclnArcsToBeMoved) {
                MclnArcView mclnArcViewCloned = mclnArcView.clone();
                selectedMclnArcsToBeMovedCloned.add(mclnArcViewCloned);
            }
        }

        // repaint selected nodes and arcs
        mclnGraphDesignerView.paintTheModelFragmentOnTheScreen(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMovedCloned,
                selectedArcsThatWillBeDiscarded);
    }

    /**
     *
     */
    void undoFragmentMoved() {

        VAlgebra.initVec3(translationVec, 0, 0, 0);
        System.out.println("takeTheGroupFinalLocation " + " selected nodes " + selectedMclnNodesToBeMoved.size());

//        for (MclnGraphViewArc mclnGraphViewArc : selectedMclnArcsToBeMoved) {
//            mclnGraphViewArc.setHighlighted(false);
//            mclnGraphViewArc.setPreSelected(false);
//            mclnGraphViewArc.setSelected(false);
//            mclnGraphViewArc.setArrowHighlighted(false);
//            mclnGraphViewArc.resetToOriginalLocation();
//        }

        for (MclnArcView mclnArcView : selectedArcsThatWillBeDiscarded) {
            mclnArcView.setHighlighted(false);
            mclnArcView.setPreSelected(false);
            mclnArcView.setSelected(false);
            mclnArcView.setWatermarked(false);
            mclnArcView.setArrowWatermarked(false);
        }

        for (MclnGraphEntityView mclnGraphEntityView : selectedMclnNodesToBeMoved) {
            mclnGraphEntityView.setHighlighted(false);
            mclnGraphEntityView.setPreSelected(false);
            mclnGraphEntityView.setSelected(false);
            mclnGraphEntityView.setMouseHover(false);
            mclnGraphEntityView.resetToOriginalLocation();
        }

        mclnGraphDesignerView.clearInterimEntityCollectionsAndRecreateOffScreenImage();

        clearLocalGraphFragmentMovingCollections();
    }

    /**
     * Called to reset moved fragment leading node when the fragment is paused at interim location
     *
     * @param me
     * @return
     */
    boolean resetLeadingNode(MouseEvent me) {
//        modelMoved = false;
        int mouseX = me.getX();
        int mouseY = me.getY();
        MclnGraphViewNode tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
        if (tmpMclnGraphViewNode == null) {
            return false;
        }

        if (!selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode)) {
            return false;
        }

        for (MclnGraphViewNode mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.setSelected(false);
        }
        mclnGraphViewNodeToBeMoved = tmpMclnGraphViewNode;

        mclnGraphViewNodeToBeMoved.setSelected(true);

        // repaint selected nodes and arcs
        mclnGraphDesignerView.paintTheModelFragmentOnTheScreen(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMovedCloned,
                selectedArcsThatWillBeDiscarded);

        // update translation vector on the screen

        double[] scrPnt = mclnGraphViewNodeToBeMoved.getScrPnt();
        VAlgebra.initVec3(translVecStartPnt, scrPnt[0], scrPnt[1], 0d);
        VAlgebra.initVec3(translVecEndPnt, me.getX(), me.getY(), 0d);
        mclnGraphDesignerView.paintVector(translVecStartPnt, translVecEndPnt);

        return true;
    }

    /**
     * @param me
     */
    boolean moveSelectedModelFragment(MouseEvent me) {
        if (selectedMclnNodesToBeMoved.size() == 0 || mclnGraphViewNodeToBeMoved == null) {
            setModelMoved(false);
            return false;
        }

        if (snapTranslationVector) {
            mclnGraphDesignerView.snapToGridLine(me);
        }

        VAlgebra.initVec3(translVecEndPnt, me.getX(), me.getY(), 0d);
        VAlgebra.subVec3(translationVec, translVecEndPnt, translVecStartPnt);

        double translationVecLength = VAlgebra.vec2Len(translationVec);
        if (translationVecLength <= 15) {
            setModelMoved(false);
            return false;
        }

        mclnGraphDesignerView.translateGraphFragmentBeingMovedAndPaintOnTheScreen(translationVec, selectedMclnNodesToBeMoved,
                selectedMclnArcsToBeMovedCloned, selectedArcsThatWillBeDiscarded);

        // this call we paint Translation Vector
        mclnGraphDesignerView.paintVector(translVecStartPnt, translVecEndPnt);
        setModelMoved(true);
        return true;
    }

    /**
     * @param me
     */
    void pauseDraggingModelFragment(MouseEvent me) {
//        for (MclnGraphViewArc mclnGraphViewArc : selectedMclnArcsToBeMoved) {
//            mclnGraphViewArc.setSelected(false);
//        }
        for (MclnGraphViewNode mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.setSelected(false);
        }
        mclnGraphViewNodeToBeMoved.setSelected(true);
    }

    /**
     *
     */
    void takeTheGroupFinalLocation() {

        if (!modelMoved) {
            return;
        }

        // the deletion of the fragment connecting arcs
        for (MclnArcView mclnArcView : selectedArcsThatWillBeDiscarded) {

            mclnArcView.setHighlighted(false);
            mclnArcView.setPreSelected(false);
            mclnArcView.setSelected(false);
            mclnArcView.setWatermarked(false);
            mclnArcView.setArrowWatermarked(false);

            mclnArcView.disconnectFromInputAndOutputNodes();
            MclnArc mclnArc = mclnArcView.getMclnArc();
            mclnArc.disconnectFromInputAndOutputNodes();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc); // the model will update view
        }

        List<String> nodeUIDs = new ArrayList();
        for (MclnGraphViewNode mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.setHighlighted(false);
            mclnGraphViewNode.setPreSelected(false);
            mclnGraphViewNode.setSelected(false);
            mclnGraphViewNode.setMouseHover(false);
            nodeUIDs.add(mclnGraphViewNode.getUID());
        }

        List<String> arcUIDs = new ArrayList();
        for (MclnArcView mclnArcView : selectedMclnArcsToBeMoved) {
            arcUIDs.add(mclnArcView.getUID());
        }
        double[] finalLocationScaledCScrVector = mclnGraphViewNodeToBeMoved.getFinalLocationScaledScrVector();
        mclnGraphDesignerView.takeFinalLocationAndPaintTheGraphOnTheScreen(nodeUIDs, arcUIDs, finalLocationScaledCScrVector);
        System.out.println("takeTheGroupFinalLocation " + " selected nodes " + selectedMclnNodesToBeMoved.size());

        clearLocalGraphFragmentMovingCollections();
        setModelMoved(false);
    }

    private void clearLocalGraphFragmentMovingCollections() {
        mclnGraphViewNodeToBeMoved = null;
        selectedMclnNodesToBeMovedStack.clear();
        selectedMclnNodesToBeMoved.clear();
        selectedMclnArcsToBeMoved.clear();
        selectedMclnArcsToBeMovedCloned.clear();
        selectedArcsThatWillBeDiscarded.clear();
    }

    //
    //   M o v i n g   e n t i r e   g r a p h
    //

    /**
     * @param me
     */
    void prepareEntireModelToBeMoved(MouseEvent me) {
        setModelMoved(false);

        // Save start point of translating vector
        if (selectedMclnGraphViewNode != null) {
            double[] scrPnt = selectedMclnGraphViewNode.getScrPnt();
            VAlgebra.initVec3(translVecStartPnt, scrPnt[0], scrPnt[1], 0d);
        } else {
            VAlgebra.initVec3(translVecStartPnt, me.getX(), me.getY(), 0d);
        }
        mclnGraphDesignerView.createEmptyOffScreenImageAndModelSpriteView();
    }

    /**
     * @param me
     */
    void moveModelStep(MouseEvent me) {
        if (selectedMclnGraphViewNode != null && snapTranslationVector) {
            mclnGraphDesignerView.snapToGridLine(me);
        }
        VAlgebra.initVec3(translVecEndPnt, me.getX(), me.getY(), 0d);
        VAlgebra.subVec3(translationVec, translVecEndPnt, translVecStartPnt);

        double translationVecLength = VAlgebra.vec2Len(translationVec);
        if (translationVecLength <= 15) {
            setModelMoved(false);
            return;
        }

        mclnGraphDesignerView.translateAndPaintTheGraphOnTheScreen(translationVec);
        mclnGraphDesignerView.paintVector(translVecStartPnt, translVecEndPnt);
        setModelMoved(true);
    }

    /**
     *
     */
    boolean takeTheModelViewFinalLocation() {
        if (!modelMoved) {
            mclnGraphDesignerView.resetModelViewAsUserDidNotStartDragging();
            return false;
        }
        mclnGraphDesignerView.takeMovedModelFinalLocationAndRecreateOffScreenImage(translationVec);
        setModelMoved(false);
        return true;
    }

    //=================================================================================================================

    //
    //   D e l e t e   E n t i t y   O p e r a t i o n
    //

    private MclnGraphEntityView mclnGraphViewEntityToBeDeleted;

    void processDeleteElement(MouseEvent me) {

        System.out.println("Processing: Delete Element");
//        DsdsseEnvironment.showMessagePopup(DsdsseEnvironment.TYPE_WARNING, "Operation: Delete Element", "Message");
        int mouseX = me.getX();
        int mouseY = me.getY();

        if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED) {
            System.out.println("deleteElement: " + currentOperationStep);
            if (mclnGraphViewEntityToBeDeleted == null && isRMBPressed(me)) {
                return;
            }

            mclnGraphViewEntityToBeDeleted = mclnGraphDesignerView.getGraphEntityAtCoordinates(mouseX, mouseY);
            if (mclnGraphViewEntityToBeDeleted == null) {
                return;
            }
            System.out.println("\"mclnGraphViewEntityToBeDeleted selected");
            mclnGraphViewEntityToBeDeleted.setSelected(true);
            mclnGraphDesignerView.highlightEntityViewToBeDeleted(mclnGraphViewEntityToBeDeleted);

            currentOperationStep = AppStateModel.OperationStep.REMOVE_SELECTED_ELEMENT;
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
            return;
        }

        if (currentOperationStep == AppStateModel.OperationStep.REMOVE_SELECTED_ELEMENT) {

            System.out.println("deleteElement: " + currentOperationStep);
            // un-selecting selected element
            if (mclnGraphViewEntityToBeDeleted != null && isRMBPressed(me)) {
                mclnGraphViewEntityToBeDeleted.setSelected(false);
                mclnGraphViewEntityToBeDeleted = null;
                mclnGraphDesignerView.highlightEntityViewToBeDeleted(mclnGraphViewEntityToBeDeleted);
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED;
                AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
                return;
            }

            MclnGraphEntityView mclnGraphViewEntityToConfirmDeletion =
                    mclnGraphDesignerView.getGraphEntityAtCoordinates(mouseX, mouseY);
            if (mclnGraphViewEntityToBeDeleted != mclnGraphViewEntityToConfirmDeletion) {
                return;
            }

            mclnGraphViewEntityToBeDeleted.setSelected(false);
            MclnGraphModel mclnGraphModel = MclnGraphModel.getInstance();

            if (mclnGraphViewEntityToBeDeleted instanceof MclnPropertyView) {
                MclnPropertyView mclnPropertyView = (MclnPropertyView) mclnGraphViewEntityToBeDeleted;
                MclnStatement mclnStatement = mclnPropertyView.getTheElementModel();
                mclnGraphModel.removeMclnStatementAndUpdateView(mclnStatement);

            } else if (mclnGraphViewEntityToBeDeleted instanceof MclnConditionView) {
                MclnConditionView mclnConditionView = (MclnConditionView) mclnGraphViewEntityToBeDeleted;
                MclnCondition mclnCondition = mclnConditionView.getTheElementModel();
                mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);

            } else if (mclnGraphViewEntityToBeDeleted instanceof MclnArcView) {
                MclnArcView mclnArcView = (MclnArcView) mclnGraphViewEntityToBeDeleted;
                MclnArc mclnArc = mclnArcView.getMclnArc();
                mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
            }

            mclnGraphViewEntityToBeDeleted = null;
            selectedMclnGraphViewNode = null;
            currentlyHighlightedMclnGraphEntity = null;

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED;
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
        }
    }

    void processUserDeletesElementViaPopup(MclnGraphEntityView elementToBeDeleted) {
        processElementDeletion(elementToBeDeleted);
    }


    private void processElementDeletion(MclnGraphEntityView elementToBeDeleted) {
        elementToBeDeleted.setSelected(false);
        MclnGraphModel mclnGraphModel = MclnGraphModel.getInstance();

        if (elementToBeDeleted instanceof MclnPropertyView) {
            MclnPropertyView mclnPropertyView = (MclnPropertyView) elementToBeDeleted;
            MclnStatement mclnStatement = mclnPropertyView.getTheElementModel();
            mclnGraphModel.removeMclnStatementAndUpdateView(mclnStatement);

        } else if (elementToBeDeleted instanceof MclnConditionView) {
            MclnConditionView mclnConditionView = (MclnConditionView) elementToBeDeleted;
            MclnCondition mclnCondition = mclnConditionView.getTheElementModel();
            mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);

        } else if (elementToBeDeleted instanceof MclnArcView) {
            MclnArcView mclnArcView = (MclnArcView) elementToBeDeleted;
            MclnArc mclnArc = mclnArcView.getMclnArc();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
        }
    }

    // ================================================================================================================
    //  C l e a n u p   a f t e r   D e m o   P r e s e n t a t i o n   i s   c o m p l e t e  o r   c a n c e l l e d
    // ================================================================================================================

    public final void cleanupAfterDemoPresentationIsCompleteOrCanceled() {

        mclnGraphDesignerView.cleanupAfterDemoPresentationCanceled();

        // Arc creation data
        currentArcInputNode = null;
        currentArcOutputNode = null;
        currentArc = null;

        // fragment creation data
        curFragmentInpNode = null;
        curFragmentOutNode = null;
        mclnConditionView = null;
        conditionInpArcView = null;
        conditionOutArcView = null;

        // fragment moving data
        mclnGraphViewNodeToBeMoved = null;
        selectedMclnNodesToBeMoved.clear();
        selectedMclnNodesToBeMovedStack.clear();
        selectedMclnArcsToBeMoved.clear();
        selectedMclnArcsToBeMovedCloned.clear();
        selectedArcsThatWillBeDiscarded.clear();
    }
}
