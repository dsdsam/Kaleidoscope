package dsdsse.graphview;

import dsdsse.app.AppStateModel;
import dsdsse.app.AppStateModelListener;
import dsdsse.designspace.DesignSpaceView;
import mcln.model.ArrowTipLocationPolicy;
import mcln.model.MclnArc;
import mclnview.graphview.MclnGraphEntityView;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnGraphNodeView;
import mclnview.graphview.MclnPolylineArcView;
import vw.valgebra.VAlgebra;

import java.awt.event.MouseEvent;

public class PolylineArcCreator {

    //
    //
    //

    private MclnGraphDesignerView mclnGraphDesignerView;
    private final MclnGraphViewEditor mclnGraphViewEditor;

    AppStateModel.OperationStep previousArcCreationOperationStep;
    AppStateModel.Operation currentOperation = AppStateModel.Operation.NONE;
    AppStateModel.OperationStep currentOperationStep;

    // Arc stuff
    private MclnGraphNodeView currentArcInputNode;
    private MclnGraphNodeView currentArcOutputNode;
    private MclnPolylineArcView mclnPolylineArcView;
    private boolean userSelectsArrowLocation = true;

    private final AppStateModelListener appStateModelListener = new AppStateModelListener() {
        @Override
        public void stateChanged() {
            setEditorOperationAndStep(AppStateModel.getCurrentOperation(), AppStateModel.getCurrentOperationStep());
        }

        /**
         * Called from App Model State when model state has changed
         *
         * @param operation
         * @param operationStep
         */
        private void setEditorOperationAndStep(AppStateModel.Operation operation, AppStateModel.OperationStep operationStep) {

            if (PolylineArcCreator.this.currentOperation == operation && PolylineArcCreator.this.currentOperationStep == operationStep) {
                return;
            }

//        setModelMoved(false);
//        clearLocalGraphFragmentMovingCollections();
            PolylineArcCreator.this.currentOperation = operation;
            PolylineArcCreator.this.currentOperationStep = operationStep;
        }
    };


    /**
     * @param mclnGraphViewEditor
     */
    PolylineArcCreator(MclnGraphViewEditor mclnGraphViewEditor) {
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
                        createPolylineArcInstance(currentArcInputNode);
                    }
                    break;
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
                    pickupArcNextJointOrOutputNode(me);
                    break;
                case PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION:
                    checkRMBOrUpdateArrowLocationAndFinishArcCreation(me, userSelectsArrowLocation);
                    break;
            }

        } else if (mouseEventType == MouseEvent.MOUSE_MOVED) {
            switch (currentOperationStep) {
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY:
                case PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION:
                    mclnGraphDesignerView.snapToGridLine(me);
                    moveArcActivePoint(me);
                    break;
                case PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION:
                    findArrowTipIndexOnThePolyline(me, userSelectsArrowLocation);
                    break;
            }
            me.consume();
        } else if (mouseEventType == MouseEvent.MOUSE_RELEASED) {

        }
    }

    private void createPolylineArcInstance(MclnGraphNodeView currentArcInputNode) {

        // Statement or Condition was selected - make one-Node arc
        mclnPolylineArcView = MclnGraphModel.getInstance().createIncompleteMclnPolylineArcAndUpdateView(
                ArrowTipLocationPolicy.DETERMINED_BY_USER, currentArcInputNode);

        mclnPolylineArcView.setUnderConstruction(true);

        currentArcInputNode.setSelected(true);
        mclnGraphDesignerView.setArcInputNodeWhileCreatingArc(currentArcInputNode);
        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnPolylineArcView);

        currentArcOutputNode = null;
        if (currentArcInputNode.isPropertyNode()) {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
        } else {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
        }
        AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
        return;
    }

    /**
     * Moving active point
     *
     * @param me
     */
    private void moveArcActivePoint(MouseEvent me) {

        double[] screenPoint = VAlgebra.initVec3(me.getX(), me.getY(), 0);

        /**
         * This section places mouse right in the middle of potential output node
         * when mouse hovers a node of type other than input node
         */
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(me.getX(), me.getY());
        if (mclnGraphEntityView != null) {
            if (currentArcInputNode != null && ((currentArcInputNode.isPropertyNode() && mclnGraphEntityView.isConditionNode())
                    || (currentArcInputNode.isConditionNode() && mclnGraphEntityView.isPropertyNode()))) {
                screenPoint = mclnGraphEntityView.toMclnGraphNodeView().getScrPnt();
                mclnGraphEntityView.setMouseHover(true);
                mclnGraphDesignerView.setMouseHoveredEntity(mclnGraphEntityView);
            } else {
                mclnGraphDesignerView.setMouseHoveredEntity(null);
            }
        }
//        if (mclnGraphEntityView != null && mclnGraphEntityView.isMouseHover()) {
//            if (currentArcInputNode != null && ((currentArcInputNode.isPropertyNode() && mclnGraphEntityView.isConditionNode())
//                    || (currentArcInputNode.isConditionNode() && mclnGraphEntityView.isPropertyNode() )) ) {
//                screenPoint = mclnGraphEntityView.toMclnGraphNodeView().getScrPnt();
//            }
//        }

        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(screenPoint);
        boolean updated = mclnPolylineArcView.updateLastPoint(cSysPoint);
        mclnGraphDesignerView.repaint();
    }

    private void pickupArcNextJointOrOutputNode(MouseEvent me) {

        //   U n d o i n g

        if (isRMBPressed(me)) {
            boolean hasMoreSegments = removeLastSegment();
            if (hasMoreSegments) {
                mclnGraphDesignerView.repaint();
            } else {
                cancelArcCreation();
            }
            me.consume();
            return;
        }

        // Adding next knot

        int scrX = me.getX();
        int scrY = me.getY();
        int scrZ = 0;

        if (!checkIfOutputNodePicked(scrX, scrY)) {
            addNextJoint(scrX, scrY);
        }

        mclnGraphDesignerView.repaint();
    }

    private void cancelArcCreation() {

        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);

        MclnArc mclnArc = mclnPolylineArcView.getTheElementModel();
        MclnGraphModel.getInstance().removeMclnArcAndUpdateView(mclnArc);

        currentArcInputNode.setSelected(false);

        currentArcInputNode = null;
        currentArcOutputNode = null;
        mclnPolylineArcView = null;

        currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
    }

    private void addNextJoint(int scrX, int scrY) {
        double[] screenPoint = VAlgebra.initVec3(scrX, scrY, 0);
        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(screenPoint);
        MclnPolylineArcView mclnPolylineArcView = this.mclnPolylineArcView;
        mclnPolylineArcView.addNextCSysKnot(cSysPoint);

    }

    /**
     * @param scrX
     * @param scrY
     * @return
     */
    private boolean checkIfOutputNodePicked(int scrX, int scrY) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphViewEditor.getSomethingSelected(scrX, scrY);
        // try if node or condition picked
        if (mclnGraphEntityView == null) {
            return false;
        }
        if (!mclnGraphViewEditor.isSelectedOutputNodeAllowed(mclnGraphEntityView)) {
            return false;
        }

        // second node was piked

        // setting arc presentation

        currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
        mclnPolylineArcView.setOutputNode(currentArcOutputNode);
        mclnGraphDesignerView.setMouseHoveredEntity(null);
        currentArcInputNode.setSelected(true);
        currentArcOutputNode.setSelected(true);

        mclnPolylineArcView.setSelectingArrowTipLocation(true);
        mclnGraphDesignerView.repaint();

        previousArcCreationOperationStep = AppStateModel.getInstance().getCurrentOperationStep();
        currentOperationStep = AppStateModel.OperationStep.PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION);

        return true;
    }

    /**
     * @param me
     * @return
     */
    private boolean findArrowTipIndexOnThePolyline(MouseEvent me, boolean userSelectsArrowLocation) {
        int x = me.getX();
        int y = me.getY();
        boolean arrowLocationFound = mclnPolylineArcView.findArrowTipIndexOnThePolyline(x, y, userSelectsArrowLocation);
        mclnGraphDesignerView.repaint();
        return arrowLocationFound;
    }

    /**
     * Method is called after arc output node is selected
     *
     * @param me
     * @param userSelectsArrowLocation
     */
    private void checkRMBOrUpdateArrowLocationAndFinishArcCreation(MouseEvent me, boolean userSelectsArrowLocation) {

        if (isRMBPressed(me)) {
            // cancel placing arrow, unselect selected output node and go
            // back to creating knots or selecting another output node

            mclnPolylineArcView.unselectSelectedOutputNode();
            double[] scrPoint = {me.getX(), me.getY(), 0};
            double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(scrPoint);
            boolean updated = mclnPolylineArcView.updateLastPoint(cSysPoint);

            currentArcOutputNode.setSelected(false);
            mclnPolylineArcView.setSelectingArrowTipLocation(false);

            currentArcOutputNode = null;
            if (currentArcInputNode.isPropertyNode()) {
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
            } else {
                currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
            }
            AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);
            me.consume();
            mclnGraphDesignerView.repaint();
            return;
        }

        // This call is needed as user may click on just created knot or output node
        // without moving the mose. In this case arrowTipSplineScrIndex is not found
        // and arrow is not shown
        boolean arrowLocationFound = findArrowTipIndexOnThePolyline(me, userSelectsArrowLocation);
        // checking if user selects Arrow location
        if (!(userSelectsArrowLocation && arrowLocationFound)) {
            // Wrong selection - ignore the pick
            return;
        }
        finishArcCreationAndAddItToMclnModel();
    }

    /**
     * This method completes arc creation
     */
    private void finishArcCreationAndAddItToMclnModel() {

        // reset nodes
        currentArcInputNode.setSelected(false);
        currentArcOutputNode.setSelected(false);

        // resetting arc presentation
        mclnPolylineArcView.setDrawKnots(false);
        mclnPolylineArcView.setSelected(false);
        mclnPolylineArcView.setThreadSelected(false);
        mclnPolylineArcView.setAllKnotsSelected(false);
        mclnPolylineArcView.setSelectedKnotInd(-1);

        mclnPolylineArcView.arcInteractiveCreationFinished();

        currentArcInputNode.outArcList.add(mclnPolylineArcView);
        currentArcOutputNode.inpArcList.add(mclnPolylineArcView);

        mclnPolylineArcView.setUnderConstruction(false);

        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);

        currentArcInputNode = null;
        currentArcOutputNode = null;
        mclnPolylineArcView = null;

        currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
    }

    private final boolean isRMBPressed(MouseEvent me) {
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
        return rightMouseButtonPressed;
    }

    private boolean removeLastSegment() {
        boolean hasMoreSegments = mclnPolylineArcView.removeLastSegment();
        return hasMoreSegments;
    }
}
