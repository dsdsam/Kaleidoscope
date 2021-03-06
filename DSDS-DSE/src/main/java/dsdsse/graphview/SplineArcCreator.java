package dsdsse.graphview;

import dsdsse.app.AppStateModel;
import dsdsse.app.AppStateModelListener;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import dsdsse.designspace.DesignSpaceView;
import mcln.model.ArrowTipLocationPolicy;
import mcln.model.MclnArc;
import mclnview.graphview.*;
import vw.valgebra.VAlgebra;

import java.awt.event.MouseEvent;

public class SplineArcCreator {

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

    private MclnGraphDesignerView mclnGraphDesignerView;
    private final MclnGraphViewEditor mclnGraphViewEditor;

    AppStateModel.OperationStep previousArcCreationOperationStep;
    AppStateModel.Operation currentOperation = AppStateModel.Operation.NONE;
    AppStateModel.OperationStep currentOperationStep;

    // Arc stuff
    private MclnGraphNodeView currentArcInputNode;
    private MclnGraphNodeView currentArcOutputNode;
    private MclnSplineArcView mclnSplineArcView;
    private boolean userSelectsArrowLocation = true;
    private int arrowTipSplineScrIndex;

    private final AppStateModelListener appStateModelListener = () -> {
        setEditorOperationAndStep(AppStateModel.getCurrentOperation(), AppStateModel.getCurrentOperationStep());
    };

    /**
     * @param mclnGraphViewEditor
     */
    SplineArcCreator(MclnGraphViewEditor mclnGraphViewEditor) {
        this.mclnGraphViewEditor = mclnGraphViewEditor;
        AppStateModel.getInstance().setAppStateModelListener(appStateModelListener);
        mclnGraphDesignerView = DesignSpaceView.getInstance().getMclnGraphDesignerView();
    }

    boolean isArcInputNodeAProperty() {
        return currentArcInputNode != null && currentArcInputNode.isPropertyNode();
    }

    boolean isArcInputNodeACondition() {
        return currentArcInputNode != null && currentArcInputNode.isConditionNode();
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
        this.currentOperation = operation;
        this.currentOperationStep = operationStep;
    }

    /**
     * @param me
     * @param operation
     * @param mouseEventType
     */
    final void processArcCreation(MouseEvent me, AppStateModel.Operation operation, int mouseEventType) {

        if (mouseEventType == MouseEvent.MOUSE_PRESSED) {
            switch (currentOperationStep) {
                case PICK_UP_ARC_INPUT_NODE:
                    currentArcInputNode = mclnGraphViewEditor.pickUpArcInputNode(me);
                    if (currentArcInputNode != null) {
                        createSplineArcInstance(currentArcInputNode);
                    }
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
                    // This call is needed as user may click on just created knot or output node
                    // without moving the mose. In this case arrowTipSplineScrIndex is not found
                    // and arrow is not shown
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSplineForJustCreatedArc(me, true);
                    takeThreeKnotArcArrowTipIndexAndFinishArcCreation(me);
                    break;
                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
                    // This call is needed as user may click on just created knot or output node
                    // without moving the mose. In this case arrowTipSplineScrIndex is not found
                    // and arrow is not shown
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSplineForJustCreatedArc(me, true);
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
                    moveArcActivePoint(me);
                    break;
                case PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION:
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSplineForJustCreatedArc(me, true);
                    break;
                case PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION:
                    arrowTipSplineScrIndex = findArrowTipIndexOnTheSplineForJustCreatedArc(me, true);
                    break;
            }
        }
    }

    /**
     * @param currentArcInputNode
     */
    private void createSplineArcInstance(MclnGraphNodeView currentArcInputNode) {

        // Statement or Condition was selected - make one-Node arc
        mclnSplineArcView =
                DesignSpaceView.getInstance().getMclnGraphModel().createIncompleteMclnSplineArcAndUpdateView(
                        ArrowTipLocationPolicy.DETERMINED_BY_USER, currentArcInputNode);

        mclnSplineArcView.setUnderConstruction(true);

        mclnSplineArcView.setHighlightFlatSegments(false);

        currentArcInputNode.setSelected(true);
        mclnGraphDesignerView.setArcInputNodeWhileCreatingArc(currentArcInputNode);

        // resetting arc presentation
        mclnSplineArcView.setSelected(true);
        mclnSplineArcView.setThreadSelected(true);
        mclnSplineArcView.setDrawKnots(true);
        mclnSplineArcView.setAllKnotsSelected(true);

        currentArcOutputNode = null;
        if (currentArcInputNode.isPropertyNode()) {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
        } else {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
        }
        AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
        return;
    }

    private void moveArcActivePoint(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();

        /**
         * This section places mouse right in the middle of potential output node
         * when mouse hovers a node of type other than input node
         */
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(me.getX(), me.getY());
        if (mclnGraphEntityView != null) {
            if (currentArcInputNode != null && ((currentArcInputNode.isPropertyNode() && mclnGraphEntityView.isConditionNode())
                    || (currentArcInputNode.isConditionNode() && mclnGraphEntityView.isPropertyNode()))) {
                double[] screenPoint = mclnGraphEntityView.toMclnGraphNodeView().getScrPnt();
                mclnGraphEntityView.setMouseHover(true);
                mclnGraphDesignerView.setMouseHoveredEntity(mclnGraphEntityView);
                int[] scrIntPoint = mclnGraphDesignerView.doubleVec3ToInt(screenPoint);
//                System.out.println("B " + me.getX() + "  " + me.getY());
                x = scrIntPoint[0];
                y = scrIntPoint[1];
            } else {
                mclnGraphDesignerView.setMouseHoveredEntity(null);
            }
        }
        mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, mclnSplineArcView);
    }

    /**
     * @param me
     */
    private void pickUpArcFirstKnotOrOutputNode(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();

        if (mclnGraphViewEditor.isRMBPressed(me)) { // done

            // Undo first step - unselect first node

            currentArcInputNode.setSelected(false);

            mclnGraphDesignerView.paintEntityOnly(currentArcInputNode);

            MclnArc mclnArc = mclnSplineArcView.getTheElementModel();
            DesignSpaceView.getInstance().getMclnGraphModel().removeMclnArcAndUpdateView(mclnArc);

            currentArcInputNode = null;
            currentArcOutputNode = null;
            mclnSplineArcView = null;

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
            return;
        }

        MclnGraphEntityView mclnGraphEntityView = mclnGraphViewEditor.getSomethingSelected(x, y);
        // try if node or condition picked
        if (mclnGraphEntityView != null) {
            if (!mclnGraphViewEditor.isSelectedOutputNodeAllowed(mclnGraphEntityView)) {
                return;
            }
            // setting arc presentation
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(true);
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setAllKnotsSelected(false);
            mclnSplineArcView.setSelectedKnotInd(-1);

            // second node was piked
            currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                    mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
            currentArcOutputNode.setSelected(true);
            mclnSplineArcView.setOutputNode(currentArcOutputNode);

            //
            // intermediate knot will be moved
            //

            double[] outNodeScrPoint = currentArcOutputNode.getScrPnt();
            mclnSplineArcView.addNextScrKnotAndMakePreviousKnotActive(outNodeScrPoint[0], outNodeScrPoint[1]);

            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(mclnSplineArcView);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_ONLY_KNOT;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_ONLY_KNOT);
            System.out.println("MclnGraphViewEditor: second node was piked");

        } else {

            // the arc first knot location piked

            // setting arc presentation
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(true);
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setAllKnotsSelected(false);
            mclnSplineArcView.setSelectedKnotInd(-1);

            mclnSplineArcView.addNextScrKnotAndMakeItActive(x, y);
            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(mclnSplineArcView);

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

        if (mclnGraphViewEditor.isRMBPressed(me)) {  // done

            // Undo operation if RMB pressed

            mclnSplineArcView.resetArcSpline();

            // resetting arc presentation
            mclnSplineArcView.removeOutputNode();
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(true);
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setAllKnotsSelected(false);

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

            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, mclnSplineArcView);
            return;
        }

        // check if the graph node is picked
        MclnGraphEntityView mclnGraphEntityView = mclnGraphViewEditor.getSomethingSelected(x, y);
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
            mclnSplineArcView.makeThreePointArc(middleKnotScrLocation);
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
        mclnSplineArcView.setSelected(true);
        mclnSplineArcView.setThreadSelected(true);
        mclnSplineArcView.setDrawKnots(true);
        mclnSplineArcView.setAllKnotsSelected(false);

        mclnSplineArcView.setHighlightFlatSegments(true);
        mclnSplineArcView.setSelectingArrowTipLocation(true);
        mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(true);

        previousArcCreationOperationStep = AppStateModel.getInstance().getCurrentOperationStep();
        currentOperationStep = AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION);
    }

    /**
     * @param inpNode
     * @param outNode
     * @param drawKnobAsArrow
     * @return
     */
    private static double[] calculateKnobScrLocation(MclnGraphNodeView inpNode, MclnGraphNodeView outNode,
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

            int nPnts = mclnSplineArcView.deleteLastPoint();
            if (nPnts == 2) {
                if (currentArcInputNode.isPropertyNode()) {
                    currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
                } else {
                    currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
                }
                AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
            }
            mclnSplineArcView.setThreadSelected(true);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, mclnSplineArcView);
            return;
        }

        //
        //  do the step
        //

        MclnGraphEntityView mclnGraphEntityView = mclnGraphViewEditor.getSomethingSelected(x, y);
        System.out.println("getSomethingSelected mclnGraphEntityView " + mclnGraphEntityView);
        // try if node or condition picked
        if (mclnGraphEntityView == null) {

            //
            // Knot location piked
            //

            mclnSplineArcView.setThreadSelected(true);
            mclnSplineArcView.addNextScrKnotAndMakeItActive(x, y);
            mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(mclnSplineArcView);
            return;
        }

        // wrong node selected
        if (!mclnGraphViewEditor.isSelectedOutputNodeAllowed(mclnGraphEntityView)) {
            return;
        }

        //
        // The Node was selected - take the arc Knob location
        //

        currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
        currentArcOutputNode.setSelected(true);
        mclnSplineArcView.setOutputNode(currentArcOutputNode);

        // setting output node cSys point as the arc last knot location
        double[] nodeCSysLocation = currentArcOutputNode.getCSysPnt();
        mclnSplineArcView.updateLastKnotLocation(nodeCSysLocation);
        mclnGraphDesignerView.repaint();

        boolean arrowTipFound = checkIfArrowTipCanBeFound();
        int nPnts = mclnSplineArcView.getNumberOfKnots();
        if (!arrowTipFound) {
            String message = nPnts == 3 ?
                    DsdsDseMessagesAndDialogs.MESSAGE_3POINT_ARC_UNDO :
                    DsdsDseMessagesAndDialogs.MESSAGE_MULTI_KNOT_ARC_UNDO;
            DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Creating Connecting Arcs", message);

            // remove just added output node
            currentArcOutputNode.setSelected(false);
            mclnSplineArcView.removeOutputNode();
            currentArcOutputNode = null;
            return;
        }

        if (nPnts == 3) {

            //
            // Three knots arc created. Find arrow tip location
            //

            // resetting arc presentation
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(false);
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setAllKnotsSelected(false);

            mclnSplineArcView.setSelectingArrowTipLocation(true);
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(true);
            mclnSplineArcView.setHighlightFlatSegments(true);

            previousArcCreationOperationStep = AppStateModel.getInstance().getCurrentOperationStep();
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION);

        } else {

            //
            // Multi knot arc created.
            //

            // resetting arc presentation
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(false);
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setAllKnotsSelected(false);

            mclnSplineArcView.setSelectingArrowTipLocation(true);
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(true);
            mclnSplineArcView.setHighlightFlatSegments(true);

            currentOperationStep = AppStateModel.OperationStep.PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION);
        }

    }

    /**
     * Called at the end of Arc creation steps to let user to select the Arc Arrow location on the spline
     *
     * @param me
     * @return
     */
    private int findArrowTipIndexOnTheSplineForJustCreatedArc(MouseEvent me, boolean userSelectsArrowLocation) {
        int x = me.getX();
        int y = me.getY();
        int arrowTipSplineScrIndex =
                mclnSplineArcView.findArrowTipIndexOnTheSplineForJustCreatedArc(x, y, userSelectsArrowLocation);
        mclnGraphDesignerView.paintArcArrowWhileIsBeingCreatedOnScreen(mclnSplineArcView);
        return arrowTipSplineScrIndex;
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
                mclnSplineArcView.removeOutputNode();
                currentArcOutputNode = null;
            }

            currentOperationStep = previousArcCreationOperationStep;
            AppStateModel.getInstance().setCurrentOperationStep(previousArcCreationOperationStep);

            mclnSplineArcView.setDrawKnotBoxes(false);
            mclnGraphDesignerView.eraseArcOnlyFromImageAndCallRepaint(mclnSplineArcView);

            // resetting arc presentation
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(true);
            mclnSplineArcView.setAllKnotsSelected(false);

            mclnSplineArcView.setSelectingArrowTipLocation(false);
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(false);
            mclnSplineArcView.setHighlightFlatSegments(false);

            mclnSplineArcView.destroyArcArrowUpOnUndoingArrowTipSelection();
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(false);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, mclnSplineArcView);
            return;
        }

        // Analyzing where the pick was done

        // checking if user selects Arrow location
        // and mouse is at the user selected location
        if (!userSelectsArrowLocation || arrowTipSplineScrIndex < 0) {
            // Wrong selection - ignore the pick
            return;
        }

        mclnSplineArcView.setSelectedKnotInd(-1);
        finishDesignedArcCreationAndAddItToMclnModel();
    }

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

            mclnSplineArcView.removeOutputNode();
            currentArcOutputNode = null;
            mclnSplineArcView.setDrawKnotBoxes(false);
            mclnGraphDesignerView.eraseArcOnlyFromImageAndCallRepaint(mclnSplineArcView);

            // resetting arc presentation
            mclnSplineArcView.setDrawKnots(true);
            mclnSplineArcView.setSelected(true);
            mclnSplineArcView.setThreadSelected(false);
            mclnSplineArcView.setAllKnotsSelected(false);

            mclnSplineArcView.setSelectingArrowTipLocation(false);
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(false);
            mclnSplineArcView.setHighlightFlatSegments(false);

            mclnSplineArcView.destroyArcArrowUpOnUndoingArrowTipSelection();
            mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(false);
            mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, mclnSplineArcView);
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
        finishDesignedArcCreationAndAddItToMclnModel();
    }

    /**
     * @param arrowTipSplineScrIndex
     */
    private void setUserSelectedArrowTipSplineScrIndex(int arrowTipSplineScrIndex) {
        mclnSplineArcView.setKnobIndex(arrowTipSplineScrIndex);
        mclnSplineArcView.setSelectedKnotInd(-1);
    }

    /**
     * Called:
     * 1) when straight or curved arc with only one knot (knob) arc designed in steps:
     * a) input node picked - output node picked - only knot (knob) location picked
     * b) input node picked - output node picked - output node picked again (straight arc created)
     * c) input node picked - nly knot (knob) location picked - output node picked
     * 2) after multi-knot arc designed and knob location selected
     */
    private void finishDesignedArcCreationAndAddItToMclnModel() {

        mclnSplineArcView.setSelectingArrowTipLocation(false);
        mclnSplineArcView.setHighlightArcKnotsForArrowTipSelection(false);
        mclnSplineArcView.setHighlightFlatSegments(false);

        // reset nodes
        currentArcInputNode.setSelected(false);
        currentArcOutputNode.setSelected(false);

        // resetting arc presentation
        mclnSplineArcView.setDrawKnots(false);
        mclnSplineArcView.setSelected(false);
        mclnSplineArcView.setThreadSelected(false);
        mclnSplineArcView.setAllKnotsSelected(false);
        mclnSplineArcView.setSelectedKnotInd(-1);

        mclnSplineArcView.arcInteractiveCreationFinished();

        currentArcInputNode.outArcList.add(mclnSplineArcView);
        currentArcOutputNode.inpArcList.add(mclnSplineArcView);

        mclnSplineArcView.setUnderConstruction(false);

//        mclnGraphModel.registerCompletedMclnArcAndUpdateView(currentArc);
        // remove temporary arc from graph image and paint complete arc (with arrow) on the graph image
        mclnGraphDesignerView.eraseAndPaintArcViewWithConnectedEntities(mclnSplineArcView);

        currentArcInputNode = null;
        currentArcOutputNode = null;
        mclnSplineArcView = null;
        currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
    }

    /**
     * The method is used during Arc creation to check if the Arc
     * thread length or shape allows to place the Arc Arrow on it.
     *
     * @return
     */
    boolean checkIfArrowTipCanBeFound() {
        boolean arrowTipFound = mclnSplineArcView.checkIfArrowTipCanBeFound();
        return arrowTipFound;
    }
}
