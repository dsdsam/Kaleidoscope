package dsdsse.graphview;

import dsdsse.app.AppStateModel;
import dsdsse.app.AppStateModelListener;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.ArrowTipLocationPolicy;
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
    private MclnGraphViewNode currentArcInputNode;
    private MclnGraphViewNode currentArcOutputNode;
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
        } else if (mouseEventType == MouseEvent.MOUSE_RELEASED) {

        }
    }

    private void createPolylineArcInstance(MclnGraphViewNode currentArcInputNode) {

        // Statement or Condition was selected - make one-Node arc
        mclnPolylineArcView = MclnGraphModel.getInstance().createIncompleteMclnPolylineArcAndUpdateView(
                ArrowTipLocationPolicy.DETERMINED_BY_USER, currentArcInputNode);

//        mclnPolylineArcView.setUnderConstruction(true);
//        mclnPolylineArcView.setUnderConstruction(false);

        currentArcInputNode.setSelected(true);
        mclnGraphDesignerView.setArcInputNodeWhileCreatingArc(currentArcInputNode);

        // resetting arc presentation
//        mclnPolylineArcView.setSelected(true);
        mclnPolylineArcView.setThreadSelected(true);
        mclnPolylineArcView.setDrawKnots(true);
        mclnPolylineArcView.setAllKnotsSelected(false);

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
//        if (polylineCreated) {
//            return;
//        }
//        setCrossHairs(me);
//        if (polylinePointCounter < 2) {
//            return;
//        }
        mclnGraphDesignerView.regenerateGraphView();
        int scrX = me.getX();
        int scrY = me.getY();
        int scrZ = 0;
        double[] screenPoint = VAlgebra.initVec3(scrX, scrY, scrZ);
//        System.out.println("scrX = " + scrX + ", scrY = " + scrY);
        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(screenPoint);
//        MclnPolylineArcView mclnPolylineArcView =   mclnPolylineArcView;
        boolean updated = mclnPolylineArcView.updateLastPoint(cSysPoint);
        //  System.out.println("cSysPoint X = " + cSysPoint[0] + ", cSysPoint Y = " + cSysPoint[1] + "   " + updated);
//        mclnGraphView.repaint();
//        if (!updated) {
//            return;
//        }
        mclnGraphDesignerView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(scrX, scrY, mclnPolylineArcView);
//        mclnGraphDesignerView.regenerateGraphView();
//        mclnGraphDesignerView.repaint();
    }

    private void pickupArcNextJointOrOutputNode(MouseEvent me) {

        if (isRMBPressed(me)) {
            removeLastSegment();
            mclnGraphDesignerView.repaint();
            me.consume();
            return;
        }

        int scrX = me.getX();
        int scrY = me.getY();
        int scrZ = 0;

        if (!checkIfOutputNodePicked(scrX, scrY)) {
            addNextJoint(scrX, scrY);
        }

        mclnGraphDesignerView.repaint();
//        regenerateView();

        //            if (me.isControlDown()) {
//
//                cSysRoundedPolylineEntity.creationCompleted();
//                mclnGraphView.regenerateGraphView();
//                mclnGraphView.repaint();
//                me.consume();
//                return;
//            }

//            if (isRMBPressed(me)) {
//                removeLastSegment();
//                mclnGraphView.regenerateGraphView();
//                mclnGraphView.repaint();
//                me.consume();
//                return;
//            }
    }

    private void addNextJoint(int scrX, int scrY) {
        double[] screenPoint = VAlgebra.initVec3(scrX, scrY, 0);
        double[] cSysPoint = mclnGraphDesignerView.screenPointToCSysPoint(screenPoint);
        MclnPolylineArcView mclnPolylineArcView = this.mclnPolylineArcView;
        mclnPolylineArcView.addNextCSysKnot(cSysPoint);

    }

    /**
     *
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
//        mclnPolylineArcView.setSelected(true);
//        mclnPolylineArcView.setDrawKnots(true);
//        mclnPolylineArcView.setAllKnotsSelected(false);

        currentArcOutputNode = currentArcInputNode.isPropertyNode() ?
                mclnGraphEntityView.toConditionView() : mclnGraphEntityView.toPropertyView();
        mclnPolylineArcView.setOutputNode(currentArcOutputNode);
        currentArcOutputNode.setSelected(true);

        // I commented this out because I found in one case the Condition node
        // was not placed on the vertical line precisely.
//        double[] cSysPoint = currentArcOutputNode.getCSysPnt();
//        boolean updated = mclnPolylineArcView.updateLastPoint(cSysPoint);
//        System.out.println("cSysPoint X = " + cSysPoint[0] + ", cSysPoint Y = " + cSysPoint[1] + "   " + updated);


        mclnGraphDesignerView.paintArcAndConnectedEntityOnScreen(mclnPolylineArcView);
        // regenerateView();
//        currentOperationStep = PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION;
//        AppStateModel.getInstance().setCurrentOperationStep(PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION);
//        System.out.println("MclnGraphViewEditor: second node was piked");

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
        mclnGraphDesignerView.paintArcArrowWhileIsBeingCreatedOnScreen(mclnPolylineArcView);
        return arrowLocationFound;
    }

    /**
     * @param me
     * @param userSelectsArrowLocation
     */
    private void checkRMBOrUpdateArrowLocationAndFinishArcCreation(MouseEvent me, boolean userSelectsArrowLocation) {

        //
        //   RMB check should be here
        //

        // This call is needed as user may click on just created knot
        // without moving the mose
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

        //  mclnPolylineArcView.setSelectingArrowTipLocation(false);
        //mclnPolylineArcView.setHighlightArcKnotsForArrowTipSelection(false);

        // reset nodes
        currentArcInputNode.setSelected(false);
        currentArcOutputNode.setSelected(false);

        // resetting arc presentation
        mclnPolylineArcView.setDrawKnots(false);
        mclnPolylineArcView.setSelected(false);
        mclnPolylineArcView.setThreadSelected(false);
        mclnPolylineArcView.setAllKnotsSelected(false);
        mclnPolylineArcView.setSelectedKnotInd(-1);

        mclnPolylineArcView.finishArcCreation();

        // updating MclnArc (model)
//        MclnArc mclnArc = currentArc.getMclnArc();
//        MclnGraphModel.getInstance().addMclnArc(mclnArc);

        currentArcInputNode.outArcList.add(mclnPolylineArcView);
        currentArcOutputNode.inpArcList.add(mclnPolylineArcView);

        mclnPolylineArcView.setUnderConstruction(false);

//        mclnGraphModel.registerCompletedMclnArcAndUpdateView(currentArc);
        // remove temporary arc from graph image and paint complete arc (with arrow) on the graph image
        mclnGraphDesignerView.eraseAndPaintArcViewWithConnectedEntities(mclnPolylineArcView);

        currentArcInputNode = null;
        currentArcOutputNode = null;
        mclnPolylineArcView = null;

        currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE;
        AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE);
    }

//    private void regenerateView() {
//        mclnGraphDesignerView.regenerateGraphView();
//        mclnGraphDesignerView.repaint();
//    }

    private final boolean isRMBPressed(MouseEvent me) {
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
        return rightMouseButtonPressed;
    }

    private boolean removeLastSegment() {
        boolean hasMoreSegments = mclnPolylineArcView.removeLastSegment();
//        polylinePointCounter = hasMoreSegments ? --polylinePointCounter : 0;
        return true;
    }

    /**
     * @param arrowTipSplineScrIndex
     */
    private void setUserSelectedArrowTipSplineScrIndex(int arrowTipSplineScrIndex) {
//        mclnPolylineArcView.setKnobIndex(arrowTipSplineScrIndex);
//        mclnPolylineArcView.setSelectedKnotInd(-1);
    }
}
