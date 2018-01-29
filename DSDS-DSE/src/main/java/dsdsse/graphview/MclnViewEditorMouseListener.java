package dsdsse.graphview;

import adf.onelinemessage.AdfOneLineMessageManager;
import adf.ui.components.panels.OneLineMessagePanel;
import adf.ui.tootippopup.AdfDetailedTooltipPopup;
import dsdsse.app.AppStateModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Admin on 11/11/2015.
 */
public class MclnViewEditorMouseListener {

    private MclnViewEditorMouseListener mclnViewEditorMouseListener;

    private final MclnGraphDesignerView mclnGraphDesignerView;
    private final MclnGraphViewEditor mclnGraphViewEditor;

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        /**
         * @param me
         */
        @Override
        public void mousePressed(MouseEvent me) {
//            System.out.println("MOUSE_PRESSED");

            int x = me.getX();
            int y = me.getY();

            /**
             * This section activate graph entity initialization if it is selected
             */
            boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
//            System.out.println("Mouse pressed " + me.getPoint());
//            boolean initAssistantUpAndRunning = InitAssistantInterface.isInitAssistantUpAndRunning();
//            boolean initAssistantCanBeInitialised = AppStateModel.getInstance().canInitAssistantBeInitialized();
//            if (!rightMouseButtonPressed && initAssistantUpAndRunning) {
//                if (!initAssistantCanBeInitialised ||
//                        AppStateModel.getInstance().isPrintToolActive()) {
//                    return;
//                }
//                MclnGraphEntityView mclnGraphEntityView = mclnGraphView.getGraphEntityAtCoordinates(x, y);
//                System.out.println("Entity  " + mclnGraphEntityView);
//                System.out.println("Entity  " + ((mclnGraphEntityView != null) ? mclnGraphEntityView.toString() : ""));
//                DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
//
//                if (mclnGraphEntityView instanceof MclnPropertyView) {
//                    InitAssistantInterface.resetInitAssistantToInitializeProperty(mainFrame,
//                            (MclnPropertyView) mclnGraphEntityView);
//                    AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
//                            AppStateModel.OperationStep.INITIALIZING_PROPERTY_NODE);
//                    me.consume();
//                    return;
//                } else if (mclnGraphEntityView instanceof MclnArcView) {
//                    MclnArcView mclnArcView = (MclnArcView) mclnGraphEntityView;
//                    InitAssistantInterface.resetInitAssistantToInitializeArc(mainFrame, mclnArcView);
//
//                    AppStateModel.OperationStep arcInitializationStep;
//                    if (mclnArcView.isRecognizingArc()) {
//                        arcInitializationStep = AppStateModel.OperationStep.INITIALIZING_RECOGNIZING_ARC;
//                    } else {
//                        arcInitializationStep = AppStateModel.OperationStep.INITIALIZING_GENERATING_ARC;
//                    }
//                    AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
//                            arcInitializationStep);
//                    me.consume();
//                    return;
//                }
//            }

            // ========================================================================================================

            /*
               Creating and showing right mouse Simulator popup menu
             */
            if (rightMouseButtonPressed && AppStateModel.getInstance().isSimulationMode()) {
                MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
                if (mclnGraphEntityView instanceof MclnPropertyView) {
                    MclnPropertyView mclnPropertyView = mclnGraphEntityView.toPropertyView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu = MclnGraphViewPopupMenu.getPopupMenu(mclnPropertyView);
                    if (mclnGraphViewPopupMenu == null) {
                        return;
                    }
                    mclnGraphViewPopupMenu.show(mclnGraphDesignerView, x, y);
                    me.consume();
                    return;
                }
            }

            // ========================================================================================================

             /*
               Creating and showing right mouse Creator popup menu
             */
            boolean initAssistantCanBeInitialised = AppStateModel.canInitAssistantBeInitialized();
            if (rightMouseButtonPressed && AppStateModel.getInstance().isDevelopmentMode() &&
                    initAssistantCanBeInitialised) {
//                    AppStateModel.canOperationBeInterrupted()) {

//                boolean initAssistantUpAndRunning = InitAssistantInterface.isInitAssistantUpAndRunning();
//                boolean initAssistantCanBeInitialised = AppStateModel.getInstance().canInitAssistantBeInitialized();

//                if (rightMouseButtonPressed && initAssistantCanBeInitialised) {

                MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);

                // opening Property Popup Menu
                if (mclnGraphEntityView instanceof MclnPropertyView) {
                    MclnPropertyView mclnPropertyView = mclnGraphEntityView.toPropertyView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu =
                            MclnGraphViewPopupMenu.getPopupMenu(mclnPropertyView);
                    if (mclnGraphViewPopupMenu == null) {
                        return;
                    }
                    mclnGraphViewPopupMenu.show(mclnGraphDesignerView, x, y);
                    me.consume();
                    return;
                }

                // opening Condition Popup Menu
                if (mclnGraphEntityView instanceof MclnConditionView) {
                    MclnConditionView mclnConditionView = mclnGraphEntityView.toConditionView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu =
                            MclnGraphViewPopupMenu.getPopupMenu(mclnConditionView);
                    if (mclnGraphViewPopupMenu == null) {
                        return;
                    }
                    mclnGraphViewPopupMenu.show(mclnGraphDesignerView, x, y);
                    me.consume();
                    return;
                }

                // opening Arc Popup Menu
                if (mclnGraphEntityView instanceof MclnArcView) {
                    MclnArcView mclnArcView = mclnGraphEntityView.toArcView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu =
                            EditorPopupMenu.getPopupMenu(mclnArcView);
                    if (mclnGraphViewPopupMenu == null) {
                        return;
                    }
                    mclnGraphViewPopupMenu.show(mclnGraphDesignerView, x, y);
                    me.consume();
                    return;
                }
//                }
            }

            // ========================================================================================================

            /*
                M o v i n g   g r a p h   e n t i t i e s
             */

            //  prepare graph component for to be dragged
            MclnGraphViewNode mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
            mclnGraphViewEditor.selectedMclnGraphViewNode = mclnGraphViewNode;

//            if (rightMouseButtonPressed) {
//                if (mclnGraphViewEditor.currentOperation != AppStateModel.Operation.CREATE_ARCS ||
//                        mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE) {
//                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu = MclnGraphViewPopupMenu.getInstance();
//                    mclnGraphViewPopupMenu.initPopup();
//                    mclnGraphViewPopupMenu.show(mclnGraphView, x, y);
//                    me.consume();
//                    return;
//                }
//            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
                moveTheModelFragment(me, MouseEvent.MOUSE_PRESSED);
                me.consume();
                return;
            }

            try {
                if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_MODEL &&
                        mclnGraphViewEditor.currentOperationStep ==
                                AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING) {
                    me.consume();
                    moveModel(me, MouseEvent.MOUSE_PRESSED);
                    me.consume();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ========================================================================================================

            /*
                 Process Editing Operations
             */

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_NODES ||
                    mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS) {
                mclnGraphDesignerView.snapToGridLine(me);
            }
            processEditingOperationsUponMousePressed(me);
            me.consume();
        }

        /**
         * Processing dragging selected entity operation
         *
         * @param me
         */
        @Override
        public void mouseReleased(MouseEvent me) {
//            System.out.println("MOUSE_RELEASED");

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
//                    && mclnGraphViewEditor.currentOperationStep ==
//                    AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE) {
                moveTheModelFragment(me, MouseEvent.MOUSE_RELEASED);
                me.consume();
                return;
            }

            try {
                if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_MODEL &&
                        mclnGraphViewEditor.currentOperationStep ==
                                AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE) {
                    moveModel(me, MouseEvent.MOUSE_RELEASED);
                    me.consume();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//        if (menuOpen)
//            return;
//        switch (currentOperation) {
//            case MOVE_ELEMENT:
//                moveAGroupOfNodes(me, MouseEvent.MOUSE_RELEASED);
//                break;
//            case MOVE_MODEL:
//                moveModel(me, MouseEvent.MOUSE_RELEASED);
//                break;
//        }
//        me.consume();
//        refreshInfoPanel();
//    }

            if (!mclnGraphViewEditor.dragging || (mclnGraphViewEditor.selectedMclnGraphViewNode == null)) {
                me.consume();
                return;
            }

            mclnGraphDesignerView.placeEntity(mclnGraphViewEditor.selectedMclnGraphViewNode);
            mclnGraphDesignerView.eraseAndPaintEntityWithConnectedEntities(mclnGraphViewEditor.selectedMclnGraphViewNode);
            mclnGraphViewEditor.selectedMclnGraphViewNode.setHighlighted(false);
//            selectedMclnGraphViewNode.updateUponModelRedefined();
            mclnGraphViewEditor.selectedMclnGraphViewNode = null;
            mclnGraphViewEditor.dragging = false;
            me.consume();
        }

        /**
         * @param me
         */
        @Override
        public void mouseClicked(MouseEvent me) {
//            System.out.println("M O U S E  C L I C K E D");
            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
                moveTheModelFragment(me, MouseEvent.MOUSE_CLICKED);
                me.consume();
                return;
            }
//            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_MODEL && mclnGraphViewEditor.currentOperationStep ==
//                    AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING) {
//                moveModel(me, MouseEvent.MOUSE_CLICKED);
//                me.consume();
//                return;
//            }
            me.consume();
        }

        /**
         * D r a g i n g   g r a p h   n o d e
         *
         * @param me
         */
        @Override
        public void mouseDragged(MouseEvent me) {
//            System.out.println("MOUSE_DRAGGED");


            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_ARCS &&
                    mclnGraphViewEditor.currentOperationStep != AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE) {
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
                moveTheModelFragment(me, MouseEvent.MOUSE_DRAGGED);
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_MODEL && mclnGraphViewEditor.currentOperationStep ==
                    AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE) {
//                mclnGraphView.snapToGridLine(me);
                moveModel(me, MouseEvent.MOUSE_DRAGGED);
                me.consume();
                return;
            }

            //
            //   d r a g g i n g   i n d i v i d u a l   n o d e
            //
            if (mclnGraphViewEditor.currentOperation != AppStateModel.Operation.CREATE_NODES &&
                    mclnGraphViewEditor.currentOperation != AppStateModel.Operation.CREATE_CONDITIONS) {
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.selectedMclnGraphViewNode != null) {
                if (!mclnGraphViewEditor.dragging) {
                    mclnGraphViewEditor.selectedMclnGraphViewNode.setWatermarked(true);
                    mclnGraphViewEditor.selectedMclnGraphViewNode.setMouseHover(false);
                    mclnGraphDesignerView.paintEntityOnly(mclnGraphViewEditor.selectedMclnGraphViewNode);
                    mclnGraphViewEditor.selectedMclnGraphViewNode.setWatermarked(false);
                    mclnGraphViewEditor.dragging = true;
                } else {
                    //
                    //   paint dragged individual entity on the screen at mouse tip
                    //
//                    if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_NODES ||
//                            mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS) {
//
//                    }
                    if (mclnGraphViewEditor.snapTranslationVector) {
                        mclnGraphDesignerView.snapToGridLine(me);
                    }
                    mclnGraphDesignerView.snapToGridLine(me);

                    mclnGraphDesignerView.paintEntityOnlyOnTheScreenAtPoint(me.getX(), me.getY(),
                            mclnGraphViewEditor.selectedMclnGraphViewNode);
                }
                /*
                   I commented this call out because I do not know if it is needed
                 */
//                mclnGraphViewEditor.cleanUpArcCreation();
            }
            me.consume();
        }

        @Override
        public void mouseMoved(MouseEvent me) {

            MclnGraphEntityView mclnGraphEntityView = checkIfMouseHoverEntity(me.getX(), me.getY());
            if (mclnGraphEntityView != null) {
                boolean ctrlDown = me.isControlDown();
                boolean shiftDown = me.isShiftDown();
                if ((ctrlDown && shiftDown)) {
//                    System.out.println("ctrlDown & shiftDown");
                    me.consume();
                    return;
                }
            }

            // when while menu was open and mouse moved, the arc
            // end and the mouse got different positions.
            if (AppStateModel.Operation.CREATE_ARCS == mclnGraphViewEditor.currentOperation) {
                mclnGraphViewEditor.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_MOVED);
            }
            me.consume();
        }

    };

    /**
     *
     */
    public MclnViewEditorMouseListener(MclnGraphDesignerView mclnGraphDesignerView, MclnGraphViewEditor mclnGraphViewEditor) {
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        this.mclnGraphViewEditor = mclnGraphViewEditor;
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    //
    //   Processing   M o u s e   P r e s s e d   Event
    //

    private final boolean isRMBPressed(MouseEvent me) {
        boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
        return rightMouseButtonPressed;
    }

    final void processEditingOperationsUponMousePressed(MouseEvent me) {
//        System.out.println("Process Mouse Pressed: Current Operation is \"" + mclnGraphViewEditor.currentOperation + "\"");
//        System.out.println("Process Mouse Pressed: Current Step      is \"" + mclnGraphViewEditor.currentOperationStep + "\"");
        if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.NONE) {
            return;
        }

        switch (mclnGraphViewEditor.currentOperation) {
            case CREATE_NODES:
                if (!isRMBPressed(me)) {
                    mclnGraphViewEditor.createMclnStatement(me);
                }
                break;
            case CREATE_CONDITIONS:
                if (!isRMBPressed(me)) {
                    mclnGraphViewEditor.createMclnGraphCondition(me, mclnGraphViewEditor.currentOperation);
                }
                break;
            case CREATE_ARCS:
                mclnGraphViewEditor.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_PRESSED);
                break;
            case CREATE_FRAGMENTS:
                mclnGraphViewEditor.createConditionFragment(me);
                break;
//            case SET_STATE:
//                if (objUnderMouse != null && (objUnderMouse.isCell() ||
//                        objUnderMouse.isArc())) {
//                    setupDialog(objUnderMouse);
//                }
//
//                break;
//            case RUN_MODEL:
//                break;
//
//            case MOVE_ELEMENT:
//                moveAGroupOfNodes(me, MouseEvent.MOUSE_PRESSED);
//                break;
//            case MOVE_MODEL:
//                moveModel(me, MouseEvent.MOUSE_PRESSED);
//                break;
            case DELETE_ELEMENT:
                mclnGraphViewEditor.processDeleteElement(me);
                break;
            default:
                break;
        }
    }

    //
    //   Processing   M o u s e   M o v e d   Event
    //

    final MclnGraphEntityView checkIfMouseHoverEntity(int x, int y) {
        MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);
//        System.out.println("checkIfMouseHoverEntity: ");
        if (mclnGraphEntityView != null) {
//             System.out.println("checkIfMouseHoverEntity: "+mclnGraphViewEntity.getClass().getSimpleName());
//            if (popupMenu != null) {
//                return;
//            }
//            if (currentOperation == RUN_MODEL && !obj.isCell())
//                return;
//            if (objUnderMouse != null && objUnderMouse == obj) {
//                return;
//            }

            // unselect
//            if (objUnderMouse != null) {
//                objUnderMouse.setHighlighted(false);
//                objUnderMouse.redrawMyselfOnly();
//                objUnderMouse = null;
//            }
//            if (selectedMclnGraphViewNode != null) {
//               selectedMclnGraphViewNode.placeEntity();
//            }
//            mclnGraphViewEntity.setHighlighted(true);
            mclnGraphEntityView.setMouseHover(true);
//            mclnGraphView.paintEntityOnlyOnTheScreen(mclnGraphEntityView);
//            mclnGraphView.redisplayOffScreenImageAndPaintExtrasOnTheScreen();

            mclnGraphViewEditor.currentlyHighlightedMclnGraphEntity = mclnGraphEntityView;

            // show one line message
            String message = mclnGraphEntityView.getOneLineInfoMessage();
            AdfOneLineMessageManager.showInfoMessage(message);
//            AdfOneLineMessageManager.showWarningMessage(message);


            if (mclnGraphViewEditor.showDetailsTooltip) {
                String detailedTooltip = mclnGraphEntityView.getTooltip();
                AdfDetailedTooltipPopup.showTooltip(x, y, detailedTooltip);
            }
//            mclnGraphView.setToolTipText(tooltip);
//            mclnGraphViewEntity.drawEntityOnly();
//            objUnderMouse = obj;
        } else {
//            if (popupMenu != null) {
//                return;
//            }
            if (mclnGraphViewEditor.currentlyHighlightedMclnGraphEntity != null) {
                mclnGraphViewEditor.currentlyHighlightedMclnGraphEntity.setMouseHover(false);
//                mclnGraphView.paintEntityOnlyOnTheScreen(mclnGraphViewEditor.currentlyHighlightedMclnGraphEntity);
//                mclnGraphView.redisplayOffScreenImageAndPaintExtrasOnTheScreen();
                mclnGraphViewEditor.currentlyHighlightedMclnGraphEntity = null;

                OneLineMessagePanel oneLineMessagePanel = OneLineMessagePanel.getInstance();
                oneLineMessagePanel.clearMessage();
                AdfOneLineMessageManager.clearMessage();

//                mclnGraphView.setToolTipText(null);
                AdfDetailedTooltipPopup.hideTooltip();
            }
        }
        return mclnGraphEntityView;
    }

    //
    //  ============================ M O V I N G ============================
    //

    private void moveTheModelFragment(MouseEvent me, int eventType) {
//        System.out.println("moveAGroupOfNodes:  MOUSE Event");
//        AppStateModel.getInstance().printState();
//        System.out.println("moveAGroupOfNodes " + eventType+" selected nodes "+mclnGraphViewEditor.selectedMclnNodesToBeMoved.size());


        if (eventType == MouseEvent.MOUSE_PRESSED) {

            //
            //   R i g h t   M o u s e   B u t t o n   C o m m a n d s
            //

            if (isRMBPressed(me)) {

                if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED) {
//                    mclnGraphViewEditor.unselectSelectedNode(me);
                    return;
                }

                if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE) {
                    mclnGraphViewEditor.unselectSelectedNode(me);
                    if (mclnGraphViewEditor.selectedMclnNodesToBeMoved.size() == 0) {
                        mclnGraphViewEditor.undoFragmentMoved();
                        mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED;
                        AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                    }
                    return;
                }

                if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED) {
                    mclnGraphViewEditor.undoFragmentMoved();
                    AppStateModel.OperationStep currentOperationStep = AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED;
                    mclnGraphViewEditor.currentOperationStep = currentOperationStep;
                    AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.MOVE_FRAGMENT, currentOperationStep);
                    return;
                }
                return;
            }

            //
            //   L e f t   M o u s e   B u t t o n   C o m m a n d s
            //

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED) {
                boolean okToDrug = mclnGraphViewEditor.selectAFragmentNodeToBeMoved(me);
//                System.out.println("Selected node: N O D E  SELECTED " + okToDrug);
                if (mclnGraphViewEditor.selectedMclnNodesToBeMoved.size() != 1) {
                    return;
                }
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE;
                AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE) {
                boolean okToDrug = mclnGraphViewEditor.selectAFragmentNodeToBeMoved(me);
//                System.out.println("Selected node: N O D E  SELECTED " + okToDrug);
//                if (!okToDrug) {
//                    return;
//                }
//                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED;
//                AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED);
                return;
            }

//            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED) {
//                mclnGraphViewEditor.resetLeadingNode(me);
//                // this call will repaint Translation Vector
//                mclnGraphViewEditor.moveSelectedModelFragment(me);
//                return;
//            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED) {
                if (mclnGraphViewEditor.isMouseHoveringFragmentNode(me)) {
                    mclnGraphViewEditor.resetLeadingNode(me);
                } else {
                    mclnGraphViewEditor.takeTheGroupFinalLocation();
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED;
                    AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                }
                return;
            }

            return;
        }


        // make next move
        if (eventType == MouseEvent.MOUSE_DRAGGED) {

            if (isRMBPressed(me)) {
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE) {
                boolean fragmentMoved = mclnGraphViewEditor.moveSelectedModelFragment(me);
                if (fragmentMoved) {
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED;
                    AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED);
                }
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED) {
                boolean fragmentMoved = mclnGraphViewEditor.moveSelectedModelFragment(me);
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED) {
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED;
                AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED);
                mclnGraphViewEditor.moveSelectedModelFragment(me);
                return;
            }

            return;
        }

        // take final location
        if (eventType == MouseEvent.MOUSE_RELEASED) {

            if (isRMBPressed(me)) {
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_IS_BEING_DRAGGED) {
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED;
                AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED);
                mclnGraphViewEditor.pauseDraggingModelFragment(me);
                return;
            }

            return;
        }

        // select element
        if (eventType == MouseEvent.MOUSE_CLICKED) {

            if (isRMBPressed(me)) {
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.MODEL_SEGMENT_DRAGGING_IS_PAUSED) {
                if (!mclnGraphViewEditor.isMouseHoveringFragmentNode(me)) {
                    mclnGraphViewEditor.takeTheGroupFinalLocation();
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED;
                    AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                }
                return;
            }

            return;
        }

    }


//        if (eventType == MouseEvent.MOUSE_PRESSED) {
////            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_NODES ) {
////
////            }
//
//            if (mclnGraphViewEditor.currentOperationStep ==
//                    AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE) {
//                //                if (isRMBPicked(me)) {
////                    if (canRestoreLocation && moveObj != null) {
////                        moveObj.resetStateAndRedraw();
////                        moveObj.restoreLocation();
////                        canRestoreLocation = false;
////                        currentOperationStep = TAKE_ELEMENT;
////                        repaint();
////                    } else {
////                        // System.out.println("moveAGroupOfNodes");
////                        set_ActionAndOperation(SET_STATE, DO_NOTHING);
////                        setEditingLocked(false);
////                    }
////                    moveObj = null;
////                    return;
////                }
//
////                if (canRestoreLocation) {
////                    canRestoreLocation = false;
////                    // setEditingLocked( false );
////                }
//                MclnGraphEntityView mclnEntityViewToMove = mclnGraphView.getGraphNodeAtCoordinates(me.getX(), me.getY());
////        System.out.println("checkIfMouseHoverEntity: ");
//                if (mclnEntityViewToMove != null) {
//
//                }
//
//            }
//        }
//                // if (selObj != null)
//                // {// the object was selected, but new operation is activated
//                // so, reset selected obj
//                // selObj.resetStateAndRedraw();
//                // selObj.updateDisplay();
//                // selObj = null;
//                // }
//                if ((moveObj = isSomethingSelected(me)) != null) {
//		    /*
//		     * if (selObj.type == RtsMVNObject.NODE)
//		     * System.out.println("moveAGroupOfNodes " +"NODE NODE NODE "); if
//		     * (selObj.type == RtsMVNObject.TRANSITION)
//		     * System.out.println("moveAGroupOfNodes "
//		     * +"TRANSITION TRANSITION TRANSITION "); if (selObj.type ==
//		     * RtsMVNObject.ARC) System.out.println(selObj.name +
//		     * " moveAGroupOfNodes " +"ARC ARC ARC ");
//		     */
//                    currentOperationStep = TAKE_ELEMENT_NEW_PLACE;
//                    moveObj.setStateSelectedAndRedraw();
//                    moveObj.saveLocation();
//                    // repaint();
//                    return;
//                }
//            } // end of if (currentOperationStep == TAKE_ELEMENT)
//        } // end of if (eventType == MouseEvent.MOUSE_PRESSED


//    private boolean canRestoreLocation;

    //
    //   M o v i n g   m o d e l   a s   a   w h o l e
    //

//    boolean moveFinished = true;

    private void moveModel(MouseEvent me, int eventType) {

        // initiate dragging
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE);
            mclnGraphViewEditor.prepareEntireModelToBeMoved(me);

//            System.out.println("moveModel MOUSE_PRESSED");
            return;
        }

        // make next move
        if (eventType == MouseEvent.MOUSE_DRAGGED) {
//            System.out.println("MOUSE_DRAGGED");
            mclnGraphViewEditor.moveModelStep(me);
//            canRestoreLocation = true;
//            mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE;
//            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE);
            return;
        }

        // take final location
        if (eventType == MouseEvent.MOUSE_RELEASED) {
//            System.out.println("MOUSE_RELEASED");
            mclnGraphViewEditor.takeTheModelViewFinalLocation();
            mclnGraphViewEditor.currentOperationStep =
                    AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING;
            AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
            return;
        }
    }


    //    private void moveModel(MouseEvent me, int eventType) {
//        boolean rmbPressed = false;
//        // System.out.println("moveModel" );
//        if ((me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
//            rmbPressed = true;
//
//        // --------------
//        if (currentOperationStep == TAKE_MODEL) {
//            if (eventType == MouseEvent.MOUSE_PRESSED && rmbPressed == false) {
//                if (canRestoreLocation)
//                    canRestoreLocation = false;
//
//                prepareEntireModelToBeMoved(me);
//                currentOperationStep = TAKE_MODEL_NEW_PLACE;
//                return;
//            }
//

//
//        } // End of if (currentOperationStep == TAKE_MODEL)
//
//        // --------------
//        if (currentOperationStep == TAKE_MODEL_NEW_PLACE) {
//            if (eventType == MouseEvent.MOUSE_DRAGGED) {
//                moveModelStep(me, false);
//                // System.out.println("moveModel " +"call ChangePosition ");
//                canRestoreLocation = true;
//                return;
//            }
//
//            // accept new location
//            if (eventType == MouseEvent.MOUSE_RELEASED && rmbPressed == false) {
//                moveModelFinish(me, true);
//                setProjModified();
//                currentOperationStep = TAKE_MODEL;
//                repaint();
//                return;
//            }
//
//        } // End of if (currentOperationStep == TAKE_MODEL_NEW_PLACE)
//
//    } // end of moveModel
}



//============== N O V I N G  ==================================

//    private void prepareOperation(int oldWhatToDo, int oldOperationStep) {
//        // System.out.println(" OldwhatToDo: "+oldWhatToDo + " Oldoperation: "+
//        // oldOperationStep);
//        if (oldWhatToDo == MOVE_ELEMENT || oldWhatToDo == DELETE_ELEMENT) {
//            eraseArcKnots();
//            parentView.repaint();
//            setEditingLocked(false);
//            // mclnMedia.setCreateScreenImage( true );
//        }
//        // System.out.println(" currentOperation: "+currentOperation + " operation: "+
//        // currentOperationStep);
//        if (currentOperation == MOVE_ELEMENT || currentOperation == DELETE_ELEMENT) {
//            drawArcKnots();
//            parentView.repaint();
//            setEditingLocked(true);
//        }
//    }
//
//    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    // M O V E E L E M E N T O P E R A T I O N S
//    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    //
//    private void startMoveElement(RtsMVNObject obj) {
//        boolean withDependensies = true;
//        boolean xorMode = true;
//
//        Graphics curGraphics = getScrImgGraphics();
//        Color bgColor = getBackground();
//        curGraphics.setColor(bgColor);
//        if (obj.type == RtsMVNObject.ARC)
//            obj.eraseWithChildren(bgColor, 0);
//        else
//            obj.eraseWithChildren(bgColor, 1);
//        // waitKbdHit();
//        // obj.drawWithDepend( curGraphics, withDependensies, xorMode );
//        redisplayOffScreenImageAndPaintExtrasOnTheScreen();
//    }
//
//    // --------------------------------------------------------
//    private void proceedMoveElement(RtsMVNObject obj) {
//        boolean withDependensies = true;
//        boolean xorMode = true;
//
//        // Graphics curGraphics = getGraphics();
//        // obj.drawWithDepend( curGraphics, withDependensies, xorMode, false );
//        if (obj.type == RtsMVNObject.ARC)
//            obj.redrawWithChildren(xorMode, 0);
//        else
//            obj.redrawWithChildren(xorMode, 1);
//        // waitKbdHit();
//    }
//
//    /*-------------------------------------------------------*/
//    private synchronized void moveElement(MouseEvent me, int eventType) {
//        // System.out.println( "moveElement" );
//
//        // Reset process if Right Mouse
//        // Button is pressed
//        if (eventType == MouseEvent.MOUSE_PRESSED) {
//            if (currentOperationStep == TAKE_ELEMENT) {
//                if (isRMBPicked(me)) {
//                    if (canRestoreLocation && moveObj != null) {
//                        moveObj.resetStateAndRedraw();
//                        moveObj.restoreLocation();
//                        canRestoreLocation = false;
//                        currentOperationStep = TAKE_ELEMENT;
//                        repaint();
//                    } else {
//                        // System.out.println("moveElement");
//                        set_ActionAndOperation(SET_STATE, DO_NOTHING);
//                        setEditingLocked(false);
//                    }
//                    moveObj = null;
//                    return;
//                }
//
//                if (canRestoreLocation) {
//                    canRestoreLocation = false;
//                    // setEditingLocked( false );
//                }
//
//                // if (selObj != null)
//                // {// the object was selected, but new operation is activated
//                // so, reset selected obj
//                // selObj.resetStateAndRedraw();
//                // selObj.updateDisplay();
//                // selObj = null;
//                // }
//                if ((moveObj = isSomethingSelected(me)) != null) {
//		    /*
//		     * if (selObj.type == RtsMVNObject.NODE)
//		     * System.out.println("moveElement " +"NODE NODE NODE "); if
//		     * (selObj.type == RtsMVNObject.TRANSITION)
//		     * System.out.println("moveElement "
//		     * +"TRANSITION TRANSITION TRANSITION "); if (selObj.type ==
//		     * RtsMVNObject.ARC) System.out.println(selObj.name +
//		     * " moveElement " +"ARC ARC ARC ");
//		     */
//                    currentOperationStep = TAKE_ELEMENT_NEW_PLACE;
//                    moveObj.setStateSelectedAndRedraw();
//                    moveObj.saveLocation();
//                    // repaint();
//                    return;
//                }
//            } // end of if (currentOperationStep == TAKE_ELEMENT)
//        } // end of if (eventType == MouseEvent.MOUSE_PRESSED )
//
//	/*
//	 * if (eventType == MouseEvent.MOUSE_MOVED) { if (currentOperationStep ==
//	 * TAKE_ELEMENT_NEW_PLACE) { System.out.println("moveElement "
//	 * +"call ChangePosition "); selObj.changePosition(me);
//	 * setProjModified(); repaint(); return; } } // End of if (eventType ==
//	 * MouseEvent.MOUSE_MOVED)
//	 */
//        if (eventType == MouseEvent.MOUSE_DRAGGED) {
//            if (currentOperationStep == TAKE_ELEMENT_NEW_PLACE) {
//                if (moveStage == 0) {
//                    startMoveElement(moveObj);
//                    moveStage = 1;
//                } else {
//                    proceedMoveElement(moveObj);
//                    // System.out.println("moveElement "
//                    // +"call ChangePosition ");
//                }
//                snapMousePointToTheGrid(me, false);
//                moveObj.changePosition(me);
//                proceedMoveElement(moveObj);
//                canRestoreLocation = true;
//                setProjModified();
//                setEditingLocked(true);
//                // redisplayOffScreenImageAndPaintExtrasOnTheScreen();
//                return;
//            }
//        } // End of if (eventType == MouseEvent.MOUSE_DRAGGED)
//
//        if (eventType == MouseEvent.MOUSE_RELEASED) {
//            if (isRMBPicked(me))
//                return;
//
//            if (currentOperationStep == TAKE_ELEMENT_NEW_PLACE) {
//                // movement is done
//                if (moveObj != null) {
//                    proceedMoveElement(moveObj);
//                    // System.out.println("moveElement " +"MOUSE_RELEASED" );
//
//                    moveObj.resetStateAndRedraw();
//
//                    // selObj.redrawWithDep( true );
//                    if (moveObj.type == RtsMVNObject.ARC) {
//                        // ((RtsMCN_Arc)selObj).setDrawKnotsFlag( true );
//                        moveObj.redrawWithChildren(1);
//                        // ((RtsMCN_Arc)selObj).setDrawKnotsFlag( false );
//                    } else
//                        moveObj.redrawWithChildren(2);
//
//                }
//                // repaint();
//                moveStage = 0;
//                // moveObj = null;
//                currentOperationStep = TAKE_ELEMENT;
//                return;
//            }
//
//        } // End of if (eventType == MouseEvent.MOUSE_RELEASED)
//
//    } // end of moveElement
//    // -----------------------------------------------------------
//    // M o v e M o d e l
//    // -----------------------------------------------------------
//
//    public synchronized void drawProjOnMoveModel()
//    // public synchronized void paint( Graphics g )
//    {
//        Graphics g = getGraphics();
//        Dimension d = getSize();
//        Graphics gg;
//        // System.out.println("drawProjOnMoveModel ");
//        // System.out.println("paintComponent ");
//        if (mclnMedia.createScreenImage) {
//
//            // System.out.println("paint Display");
//            if (offscreen != null) {
//                offscreen.flush();
//                offgraphics.dispose();
//            }
//            offscreen = createImage(d.width, d.height);
//            offscreensize = d;
//            offgraphics = offscreen.getGraphics();
//	    /*
//	     * int numObjects = objects.size(); if ( numObjects > 0 ) { String
//	     * mess = "R  e  p  a  i  n  t  i  n  g    .  .  ."; showMessage(
//	     * mess ); }
//	     */
//            offgraphics.setFont(getFont());
//            offgraphics.setColor(Color.white);
//            offgraphics.fillRect(0, 0, d.width, d.height);
//            g.setColor(Color.white);
//            prepareCSys();
//            // super.paintComponent( g );
//            gg = offgraphics;
//            mclnMedia.createScreenImage = false;
//            // updateMCNObjectsPosition();
//
//            gg = offgraphics;
//            super.paintComponent(gg);
//
//        }
//        // drawGrid( gg );
//        g.drawImage(offscreen, 0, 0, null);
//        drawProj(g);
//        // g.drawImage(offscreen, 0, 0, null);
//        // eraseMessage();
//    }
//
//    // ---------------------------------------------------------
//    private void prepareEntireModelToBeMoved(MouseEvent me) {
//        drawOnMoveModel = true;
//        snapMousePointToTheGrid(me, true);
//        // Save start point of translating vector
//        translVecStartX = me.getX();
//        translVecStartY = me.getY();
//        double cx = cSysScrXToWorldX(me.getX());
//        double cy = cSysScrYToWorldY(me.getY());
//        RtsVAlgebra.initVec3(translVecStartPnt, cx, cy, 0d);
//
//        // Save original location
//        int numObjects = objects.size();
//        for (int i = 0; i < numObjects; i++) {
//            RtsMVNObject obj = (RtsMVNObject) objects.elementAt(i);
//            obj.saveLocation();
//
//            // obj.setStateSelected();
//        }
//        // mclnMedia.setCreateScreenImage();
//        // repaint();
//        mclnMedia.createScreenImage = true;
//        drawProjOnMoveModel();
//    }
//
//    // ---------------------------------------------------------
//    private void moveModelStep(MouseEvent me, boolean moveAll) {
//        int orgDx, orgDy;
//        // snapMousePointToTheGrid( me, false );
//        snapMousePointToTheGrid(me, true);
//        double cx = cSysScrXToWorldX(me.getX());
//        double cy = cSysScrYToWorldY(me.getY());
//        double translVecEndPnt[] = { 0, 0, 0 };
//        double translationVec[] = { 0, 0, 0 };
//
//        RtsVAlgebra.initVec3(translVecEndPnt, cx, cy, 0d);
//        RtsVAlgebra.subVec3(translationVec, translVecEndPnt, translVecStartPnt);
//
//	/*
//	 * { Graphics g = getGraphics(); // offgraphics.translate( me.getX(),
//	 * me.getY() ); // g.drawImage(offscreen, (int)translationVec[0], //
//	 * (int) translationVec[1], null); // redisplayOffScreenImageAndPaintExtrasOnTheScreen(); orgDx = me.getX() -
//	 * translVecStartX; orgDy = me.getY() - translVecStartY;
//	 *
//	 * // g.drawImage(offscreen, me.getX(), me.getY(), null);
//	 *
//	 *
//	 * // boolean gridOn = isGridOn(); // setGridOn( false ); Dimension d =
//	 * getSize(); g.setColor( Color.white ); // super.paintComponent( g );
//	 *
//	 * // g.clearRect( -d.width, -d.height, d.width, d.height ); //
//	 * g.clearRect( 0, 0, d.width, d.height ); g.fillRect( 0, 0, d.width,
//	 * d.height );
//	 *
//	 *
//	 *
//	 * g.drawImage(offscreen, orgDx, orgDy, null); // setGridOn( gridOn ); }
//	 */
//
//        int numObjects = objects.size();
//        for (int i = 0; i < numObjects; i++) {
//            RtsMVNObject obj = (RtsMVNObject) objects.elementAt(i);
//            if (moveAll || obj.type == RtsMVNObject.NODE)
//                obj.changePosition(translationVec);
//        }
//        drawProjOnMoveModel();
//    }
//
//    // ---------------------------------------------------------
//    private void moveModelFinish(MouseEvent me, boolean moveAll) {
//        int orgDx, orgDy;
//        // snapMousePointToTheGrid( me, false );
//        snapMousePointToTheGrid(me, true);
//        double cx = cSysScrXToWorldX(me.getX());
//        double cy = cSysScrYToWorldY(me.getY());
//        double translVecEndPnt[] = { 0, 0, 0 };
//        double translationVec[] = { 0, 0, 0 };
//
//        RtsVAlgebra.initVec3(translVecEndPnt, cx, cy, 0d);
//        RtsVAlgebra.subVec3(translationVec, translVecEndPnt, translVecStartPnt);
//	/*
//	 * { Graphics g = getGraphics(); // offgraphics.translate( me.getX(),
//	 * me.getY() ); // g.drawImage(offscreen, (int)translationVec[0], //
//	 * (int) translationVec[1], null); // redisplayOffScreenImageAndPaintExtrasOnTheScreen(); orgDx = me.getX() -
//	 * translVecStartX; orgDy = me.getY() - translVecStartY; //
//	 * g.drawImage(offscreen, me.getX(), me.getY(), null);
//	 * g.drawImage(offscreen, orgDx, orgDy, null); }
//	 */
//        int numObjects = objects.size();
//        for (int i = 0; i < numObjects; i++) {
//            RtsMVNObject obj = (RtsMVNObject) objects.elementAt(i);
//            if (moveAll || obj.type == RtsMVNObject.NODE)
//                obj.changePosition(translationVec);
//        }
//
//        drawOnMoveModel = false;
//    }
//
//    // ------------------------------------------------
//    private void moveModel(MouseEvent me, int eventType) {
//        boolean rmbPressed = false;
//        // System.out.println("moveModel" );
//        if ((me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
//            rmbPressed = true;
//
//        // --------------
//        if (currentOperationStep == TAKE_MODEL) {
//            if (eventType == MouseEvent.MOUSE_PRESSED && rmbPressed == false) {
//                if (canRestoreLocation)
//                    canRestoreLocation = false;
//
//                prepareEntireModelToBeMoved(me);
//                currentOperationStep = TAKE_MODEL_NEW_PLACE;
//                return;
//            }
//
//            // Undo last Move
//            if (eventType == MouseEvent.MOUSE_PRESSED && rmbPressed == true) {
//                if (canRestoreLocation) {
//                    int numObjects = objects.size();
//                    for (int i = 0; i < numObjects; i++) {
//                        RtsMVNObject obj = (RtsMVNObject) objects.elementAt(i);
//                        obj.restoreLocation();
//                        // obj.resetState();
//                    }
//                    canRestoreLocation = false;
//                    currentOperationStep = TAKE_MODEL;
//
//                    setProjModified();
//                    repaint();
//                    // mclnMedia.setCreateScreenImage();
//                    // repaint();
//                }
//                return;
//            }
//
//        } // End of if (currentOperationStep == TAKE_MODEL)
//
//        // --------------
//        if (currentOperationStep == TAKE_MODEL_NEW_PLACE) {
//            if (eventType == MouseEvent.MOUSE_DRAGGED) {
//                moveModelStep(me, false);
//                // System.out.println("moveModel " +"call ChangePosition ");
//                canRestoreLocation = true;
//                return;
//            }
//
//            // accept new location
//            if (eventType == MouseEvent.MOUSE_RELEASED && rmbPressed == false) {
//                moveModelFinish(me, true);
//                setProjModified();
//                currentOperationStep = TAKE_MODEL;
//                repaint();
//                return;
//            }
//
//        } // End of if (currentOperationStep == TAKE_MODEL_NEW_PLACE)
//
//    } // end of moveModel


