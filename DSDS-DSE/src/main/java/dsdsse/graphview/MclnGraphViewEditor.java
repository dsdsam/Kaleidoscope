package dsdsse.graphview;


import dsdsse.app.AppStateModel;
import dsdsse.app.AppStateModelListener;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import dsdsse.designspace.DesignSpaceModel;
import mcln.model.*;
import mclnview.graphview.*;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:49:16 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MclnGraphViewEditor {

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
    AppStateModel.Operation currentOperation = AppStateModel.Operation.NONE;
    AppStateModel.OperationStep currentOperationStep;

    private MclnEditorModel mclnEditorModel;

    // fragment creation data
    private MclnPropertyView curFragmentInpNode;
    private MclnPropertyView curFragmentOutNode;
    private MclnConditionView mcLnConditionView;
    private MclnArcView conditionInpArcView;
    private MclnArcView conditionOutArcView;

    // arc creation data
    private MclnGraphNodeView currentArcInputNode;
    private MclnGraphNodeView currentArcOutputNode;
    private MclnSplineArcView currentSplineArc;

    // Dragging Operations data
    boolean dragging;
    MclnGraphNodeView selectedMclnGraphViewNode;

    // moving graph fragment
    MclnGraphNodeView mclnGraphViewNodeToBeMoved;
    final Set<MclnGraphNodeView> selectedMclnNodesToBeMoved = new LinkedHashSet();
    private final Stack<MclnGraphNodeView> selectedMclnNodesToBeMovedStack = new Stack();
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
     * Currently menu blocking mechanism is used to allow any started
     * operation to be finished. Left for possible future usage
     *
     * @param currentOperation
     * @param currentOperationStep
     * @return
     */
//    public void terminateCurrentOperation(AppStateModel.Operation currentOperation,
//                                          AppStateModel.OperationStep currentOperationStep) {
//
//        if (currentOperation.isCreatingArcs()) {
//            switch (currentOperationStep) {
//                case PICK_UP_ARC_INPUT_NODE:
//                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
//                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
//                case PICK_UP_ARC_ONLY_KNOT:
//                case PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION:
//                case PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE:
//                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
//                    terminateArcCreation();
//                    break;
//            }
//        } else if (currentOperation.isCreatingFragments()) {
//            switch (currentOperationStep) {
//                case PICK_UP_FIRST_PROPERTY:
//                case PICK_UP_SECOND_PROPERTY:
//                case PLACE_CONDITION:
//                    terminateFragmentCreation();
//                    break;
//            }
//        } else if (currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
//            for (MclnGraphEntityView mclnGraphEntityView : selectedMclnNodesToBeMoved) {
//                mclnGraphEntityView.setSelected(false);
//                mclnGraphDesignerView.repaintImageAndSpriteEntities();
//            }
//        }
//    }

    /**
     * Terminating Arc creation operation
     */
//    private void terminateArcCreation() {
//        if (currentArcInputNode != null) {
//            currentArcInputNode.setSelected(false);
//        }
//        if (currentArcOutputNode != null) {
//            currentArcOutputNode.setSelected(false);
//        }
//
//        // removing arc from model and graph view
//        if (currentSplineArc != null) {
//            MclnArc mclnArc = currentSplineArc.getTheElementModel();
//            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
//        }
//
//        currentArcInputNode = null;
//        currentArcOutputNode = null;
//        currentSplineArc = null;
//
//        currentOperationStep = AppStateModel.OperationStep.CANCELED;
//        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.CANCELED);
//    }

    /**
     * Terminating Fragment creation operation
     */
//    private void terminateFragmentCreation() {
//
//
//        if (curFragmentInpNode != null) {
//            curFragmentInpNode.setSelected(false);
//            mclnGraphDesignerView.paintEntityOnly(curFragmentInpNode);
//        }
//        if (curFragmentOutNode != null) {
//            curFragmentOutNode.setSelected(false);
//            mclnGraphDesignerView.paintEntityOnly(curFragmentOutNode);
//        }
//
//        // removing arcs from model and graph view
//        if (conditionInpArcView != null) {
//            MclnArc mclnArc = conditionInpArcView.getTheElementModel();
//            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
//        }
//
//        if (conditionOutArcView != null) {
//            MclnArc mclnArc = conditionOutArcView.getTheElementModel();
//            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
//        }
//
//        if (mclnConditionView != null) {
//            MclnCondition mclnCondition = mclnConditionView.getMclnCondition();
//            mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);
//        }
//
//        curFragmentInpNode = null;
//        curFragmentOutNode = null;
//        mclnConditionView = null;
//        conditionInpArcView = null;
//        conditionOutArcView = null;
//    }

    /**
     * @param me
     * @return
     */
    final boolean isRMBPressed(MouseEvent me) {
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
        return rightMouseButtonPressed;
    }

    //
    //   P r o c e s s i n g    C r e a t i o n    O p e r a t i o n s
    //
    MclnGraphEntityView mclnGraphEntityViewToBeDragged;
    DesignerNodeView designerNodeViewToBeDragged;

    /**
     * @param me
     * @param operation
     */
    final void createMclnGraphPropertyOrCondition(MouseEvent me, AppStateModel.Operation operation) {

        if (isRMBPressed(me)) {
            if (mclnGraphEntityViewToBeDragged == null) {
                return;
            }
            // There is a node selected to be dragged. This code cancels moving
            // by returning node to its original location, and un-selects it.
            mclnGraphEntityViewToBeDragged.toMclnGraphNodeView().restoreBackupCSysLocation();
            mclnGraphEntityViewToBeDragged.toMclnGraphNodeView().movingNodeCompleted();
            mclnGraphDesignerView.setMouseHoveredEntity(null);
            mclnGraphEntityViewToBeDragged = null;
            mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
            return;
        }

        // create Node if empty place was picked
        int x = me.getX();
        int y = me.getY();
        MclnGraphEntityView mclnGraphEntityView = getSomethingSelected(x, y);

        if (mclnGraphEntityViewToBeDragged == null) {
            // As selected for dragging entity is null, new Node should be created if empty space clicked
            if (mclnGraphEntityView == null) {
                mclnGraphDesignerView.snapToGridLine(me);
                double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(me.getX(), me.getY());
                if (operation == AppStateModel.Operation.CREATE_PROPERTIES) {
                    createNewStatement(cSysPoint);
                } else {
                    createNewCondition(cSysPoint);
                }
            } else {
                // a node was clicked for dragging
                if (me.isControlDown() && mclnGraphEntityView.isMclnGraphNode()) {
                    mclnGraphEntityView.toMclnGraphNodeView().startMoving();
                    mclnGraphEntityViewToBeDragged = mclnGraphEntityView;
                    mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnGraphEntityView);
                } else {
                    if (me.isControlDown()) {
                        // An Arc entity was clicked to be moved. Show warning.
                        DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Operation: " + operation,
                                "The Arcs cannot be moved while executing operation: \"" +
                                        operation + "\" !");
                    } else {
                        // Existing entity was clicked as a point for node creation. Show warning
                        String nodeType = (operation == AppStateModel.Operation.CREATE_PROPERTIES) ? "Property: " : "Condition";
                        DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Operation " + operation,
                                "The " + nodeType + " node cannot be placed on the top of other entity \"" +
                                        mclnGraphEntityView.getEntityTypeAsString() + "\" !");
                    }
                }
            }
            return;
        } else {
            // There exist a node selected for dragging
            if (mclnGraphEntityView == null) {
                // empty space clicked -> cancel dragging
                mclnGraphEntityViewToBeDragged.toMclnGraphNodeView().movingNodeCompleted();
                mclnGraphEntityViewToBeDragged = null;
                mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
            } else {
                if (mclnGraphEntityView == mclnGraphEntityViewToBeDragged) {
                    // When moving Node, it is OK to click on selected node again.
                    // This means use selected Node and then pressed it to start dragging it.
                    // Do nothing.
                    System.out.println();
                } else {
                    // Another node selected -> unselect previously selected node
                    // and set newly selected for dragging

                    mclnGraphEntityViewToBeDragged.toMclnGraphNodeView().movingNodeCompleted();
                    mclnGraphEntityViewToBeDragged = null;
                    mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);

                    if (mclnGraphEntityView.isMclnGraphNode()) {
                        mclnGraphEntityView.toMclnGraphNodeView().startMoving();
                        mclnGraphEntityViewToBeDragged = mclnGraphEntityView;
                        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnGraphEntityView);
                    } else {
//                        if (me.isControlDown()) {
                        //  An Arc entity was clicked to be moved. Show warning.
                        DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Operation: " + operation,
                                "The Arcs cannot be moved while executing operation: \"" +
                                        operation + "\" !");
                        System.out.println();
//                        } else {
//                            Existing entity was clicked as a point for node creation. Show warning
//                            String nodeType = (operation == AppStateModel.Operation.CREATE_PROPERTIES) ? "Property: " : "Condition";
//                            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Operation " + operation,
//                                    "The " + nodeType + " node cannot be placed on the top of other entity \"" +
//                                            mclnGraphEntityView.getEntityTypeAsString() + "\" !");
//                        }
                    }
                }
            }
        }

    }

    final void dragMclnGraphPropertyOrConditionDuringCreation(MouseEvent me, AppStateModel.Operation operation) {
        if (mclnGraphViewEditor.mclnGraphEntityViewToBeDragged != null) {
            MclnGraphNodeView mclnGraphViewNode = mclnGraphEntityViewToBeDragged.toMclnGraphNodeView();
            mclnGraphDesignerView.snapToGridLine(me);
            mclnGraphViewNode.druggingSelectedNode(me.getX(), me.getY());
            mclnGraphDesignerView.repaint();
        }
    }

    //
    //   Creating   Node - Arc - Node   fragment
    //

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
            mcLnConditionView = createNewCondition(cSysPoint);

            // creating the fragment's arcs models and views

            conditionInpArcView = mclnGraphModel.createCompleteMclnArcAndUpdateView(
                    ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION,
                    curFragmentInpNode, mcLnConditionView);

            conditionOutArcView = mclnGraphModel.createCompleteMclnArcAndUpdateView(
                    ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION,
                    mcLnConditionView, curFragmentOutNode);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_FIRST_PROPERTY);

            curFragmentInpNode.setSelected(false);
            curFragmentOutNode.setSelected(false);
            mcLnConditionView.setMouseHover(false);

            curFragmentInpNode = null;
            curFragmentOutNode = null;
            mcLnConditionView = null;
            conditionInpArcView = null;
            conditionOutArcView = null;

            mclnGraphDesignerView.fragmentCreationComplete();
        }
    }

    private boolean isSomethingSelected(int x, int y) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
        return mclnGraphEntityView != null;
    }

    MclnGraphEntityView getSomethingSelected(int x, int y) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
        return mclnGraphEntityView;
    }

    private MclnGraphNodeView getNodeSelected(int x, int y) {
        MclnGraphNodeView mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
        return mclnGraphViewNode;
    }

    private boolean isNodeSelected(int x, int y) {
        MclnGraphNodeView mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
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
        MclnPropertyView mcLnPropertyView = mclnGraphDesignerView.getPropertyNodeAtCoordinates(x, y);
        return mcLnPropertyView;
    }


//    /**
//     * @param me
//     */
//    private void moveArcActivePoint(MouseEvent me) {
//        int x = me.getX();
//        int y = me.getY();
//        mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
////        System.out.println("moveArcActivePoint");
//    }

    /**
     * @param me
     */
    MclnGraphNodeView pickUpArcInputNode(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        if (((currentArcInputNode = getNodeSelected(x, y)) == null)) {
            return null;
        }
        return currentArcInputNode;
    }

    /**
     * @param mclnGraphEntityView
     * @return
     */
    boolean isSelectedOutputNodeAllowed(MclnGraphEntityView mclnGraphEntityView) {
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
     * @param cSysPoint
     */
    private MclnPropertyView createNewStatement(double[] cSysPoint) {
        MclnPropertyView mcLnPropertyView = mclnGraphModel.createMclnStatementAndUpdateView(cSysPoint);
        return mcLnPropertyView;
    }

    private MclnConditionView createNewCondition(double[] cSysPoint) {
        MclnConditionView mcLnConditionView = mclnGraphModel.createMclnConditionAndUpdateView(cSysPoint);
        return mcLnConditionView;
    }


    //
    //   M o v i n g   t h e   g r a p h   f r a g m e n t
    //

    boolean isMouseHoveringFragmentNode(MouseEvent me) {

        int mouseX = me.getX();
        int mouseY = me.getY();

        MclnGraphNodeView tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
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
        MclnGraphNodeView tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
        if (tmpMclnGraphViewNode == null) {
            return false;
        }

        // selecting leading node
        if (selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode)) {

            for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
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
                clonedMclnArcView.backupCurrentState();
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
        MclnGraphNodeView tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);

        MclnGraphNodeView mclnGraphViewNodeToUnselect;
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
        MclnGraphNodeView tmpMclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(mouseX, mouseY);
        if (tmpMclnGraphViewNode == null) {
            return false;
        }

        if (!selectedMclnNodesToBeMoved.contains(tmpMclnGraphViewNode)) {
            return false;
        }

        for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
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
        for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
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
            MclnArc mclnArc = mclnArcView.getTheElementModel();
            mclnArc.disconnectFromInputAndOutputNodes();
            mclnGraphModel.removeMclnArcAndUpdateView(mclnArc); // the model will update view
        }

        List<String> nodeUIDs = new ArrayList();
        for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
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
        mclnGraphDesignerView.takeFinalLocationThenRecreateImageAndRepaintItOnTheScreen(nodeUIDs, arcUIDs, finalLocationScaledCScrVector);
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

        int mouseX = me.getX();
        int mouseY = me.getY();

        if (currentOperationStep == AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED) {
            if (mclnGraphViewEntityToBeDeleted == null && isRMBPressed(me)) {
                return;
            }
            mclnGraphViewEntityToBeDeleted = mclnGraphDesignerView.getGraphEntityAtCoordinates(mouseX, mouseY);
            if (mclnGraphViewEntityToBeDeleted == null) {
                return;
            }
            mclnGraphViewEntityToBeDeleted.prepareForDeletion();
            mclnGraphDesignerView.highlightEntityViewToBeDeleted(mclnGraphViewEntityToBeDeleted);

            currentOperationStep = AppStateModel.OperationStep.REMOVE_SELECTED_ELEMENT;
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
            return;
        }

        if (currentOperationStep == AppStateModel.OperationStep.REMOVE_SELECTED_ELEMENT) {
            // un-selecting selected element
            if (isRMBPressed(me)) {
                if (mclnGraphViewEntityToBeDeleted != null) {
                    mclnGraphViewEntityToBeDeleted.cancelDeletion();
                    mclnGraphViewEntityToBeDeleted = null;
                    mclnGraphDesignerView.highlightEntityViewToBeDeleted(null);
                    currentOperationStep = AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED;
                    AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
                }
                return;
            }

            MclnGraphEntityView anotherEntitySelectedToBeDeleted = mclnGraphDesignerView.getGraphEntityAtCoordinates(mouseX, mouseY);
            if (anotherEntitySelectedToBeDeleted != null &&
                    mclnGraphViewEntityToBeDeleted != anotherEntitySelectedToBeDeleted) {
                // User selected another entity to be deleted, cancel
                // previously selected and prepare newly selected
                mclnGraphViewEntityToBeDeleted.cancelDeletion();
                mclnGraphViewEntityToBeDeleted = null;
                mclnGraphDesignerView.highlightEntityViewToBeDeleted(null);
                mclnGraphViewEntityToBeDeleted = anotherEntitySelectedToBeDeleted;
                mclnGraphViewEntityToBeDeleted.prepareForDeletion();
                mclnGraphDesignerView.highlightEntityViewToBeDeleted(mclnGraphViewEntityToBeDeleted);
                return;
            }

            mclnGraphViewEntityToBeDeleted.setSelected(false);
            MclnGraphModel mclnGraphModel = MclnGraphModel.getInstance();

            if (mclnGraphViewEntityToBeDeleted instanceof MclnPropertyView) {
                MclnPropertyView mcLnPropertyView = mclnGraphViewEntityToBeDeleted.toPropertyView();
                MclnStatement mclnStatement = mcLnPropertyView.getTheElementModel();
                mclnGraphModel.removeMclnStatementAndUpdateView(mclnStatement);

            } else if (mclnGraphViewEntityToBeDeleted instanceof MclnConditionView) {
                MclnConditionView mcLnConditionView = mclnGraphViewEntityToBeDeleted.toConditionView();
                MclnCondition mclnCondition = mcLnConditionView.getTheElementModel();
                mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);

            } else if (mclnGraphViewEntityToBeDeleted instanceof MclnArcView) {
                MclnArcView mclnArcView = mclnGraphViewEntityToBeDeleted.toArcView();
                MclnArc mclnArc = mclnArcView.getTheElementModel();
                mclnGraphModel.removeMclnArcAndUpdateView(mclnArc);
            }

            mclnGraphViewEntityToBeDeleted = null;
            selectedMclnGraphViewNode = null;

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ELEMENT_TO_BE_DELETED;
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
        }
    }

    /**
     * @param elementToBeDeleted
     */
    void processUserDeletesElementViaPopup(MclnGraphEntityView elementToBeDeleted) {
        elementToBeDeleted.setSelected(false);
        MclnGraphModel mclnGraphModel = MclnGraphModel.getInstance();

        if (elementToBeDeleted instanceof MclnPropertyView) {
            MclnPropertyView mcLnPropertyView = (MclnPropertyView) elementToBeDeleted;
            MclnStatement mclnStatement = mcLnPropertyView.getTheElementModel();
            mclnGraphModel.removeMclnStatementAndUpdateView(mclnStatement);

        } else if (elementToBeDeleted instanceof MclnConditionView) {
            MclnConditionView mcLnConditionView = (MclnConditionView) elementToBeDeleted;
            MclnCondition mclnCondition = mcLnConditionView.getTheElementModel();
            mclnGraphModel.removeMclnConditionAndUpdateView(mclnCondition);

        } else if (elementToBeDeleted instanceof MclnArcView) {
            MclnArcView mclnArcView = (MclnArcView) elementToBeDeleted;
            MclnArc mclnArc = mclnArcView.getTheElementModel();
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

        // fragment creation data
        curFragmentInpNode = null;
        curFragmentOutNode = null;
        mcLnConditionView = null;
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
