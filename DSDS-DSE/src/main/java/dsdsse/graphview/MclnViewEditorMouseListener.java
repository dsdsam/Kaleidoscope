package dsdsse.graphview;

import adf.onelinemessage.AdfOneLineMessageManager;
import adf.ui.components.panels.OneLineMessagePanel;
import adf.ui.tootippopup.AdfDetailedTooltipPopup;
import dsdsse.app.AppStateModel;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import mclnview.graphview.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Admin on 11/11/2015.
 */
public class MclnViewEditorMouseListener {

    private MclnViewEditorMouseListener mclnViewEditorMouseListener;

    private final MclnGraphDesignerView mclnGraphDesignerView;
    private final MclnGraphViewEditor mclnGraphViewEditor;
    private final SplineArcCreator splineArcCreator;
    private final PolylineArcCreator polylineArcCreator;

    private MclnGraphEntityView selectedMclnGraphEntityView;

    // ============================================================================================================
    //
    //                 M o u s e   L i s t e n e r
    //
    // ============================================================================================================

    private MouseAdapter mouseAdapter = new MouseAdapter() {

        // ============================================================================================================
        //                       M o u s e   P r e s s e d
        // ============================================================================================================

        @Override
        public void mousePressed(MouseEvent me) {
            int x = me.getX();
            int y = me.getY();

            /**
             * This section activates graph entity initialization if it is selected
             */
            boolean rmbPressed = isRMBPressed(me);

            // ========================================================================================================

            MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(x, y);

                /*
                   Creating and showing right mouse Simulator popup menu
                 */
            if (rmbPressed && AppStateModel.getInstance().isSimulationMode()) {
                if (mclnGraphEntityView instanceof MclnPropertyView) {
                    MclnPropertyView mcLnPropertyView = mclnGraphEntityView.toPropertyView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu = MclnGraphViewPopupMenu.getPopupMenu(mcLnPropertyView);
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
            if (rmbPressed && AppStateModel.getInstance().isDevelopmentMode() && mclnGraphEntityView != null &&
                    mclnGraphEntityView != mclnGraphViewEditor.mclnGraphEntityViewToBeDragged &&
                    initAssistantCanBeInitialised) {

                if (mclnGraphViewEditor.mclnGraphEntityViewToBeDragged != null) {
                    // RMB clicked on entity other than dragged -> complete dragging then show popup
                    mclnGraphViewEditor.mclnGraphEntityViewToBeDragged.toMclnGraphNodeView().movingNodeCompleted();
                    mclnGraphViewEditor.mclnGraphEntityViewToBeDragged = null;
                    mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
                }

                // opening Property Popup Menu
                if (mclnGraphEntityView instanceof MclnPropertyView) {
                    MclnPropertyView mcLnPropertyView = mclnGraphEntityView.toPropertyView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu =
                            MclnGraphViewPopupMenu.getPopupMenu(mcLnPropertyView);
                    if (mclnGraphViewPopupMenu == null) {
                        return;
                    }
                    mclnGraphViewPopupMenu.show(mclnGraphDesignerView, x, y);
                    me.consume();
                    return;
                }

                // opening Condition Popup Menu
                if (mclnGraphEntityView instanceof MclnConditionView) {
                    MclnConditionView mcLnConditionView = mclnGraphEntityView.toConditionView();
                    MclnGraphViewPopupMenu mclnGraphViewPopupMenu =
                            MclnGraphViewPopupMenu.getPopupMenu(mcLnConditionView);
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
            }

            // ========================================================================================================

            /*
                M o v i n g   g r a p h   e n t i t i e s
             */

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_ELEMENTS)

            {
                selectAndUnselectTheModelElementsToBeMoved(me, MouseEvent.MOUSE_PRESSED);
                me.consume();
                mclnGraphDesignerView.repaint();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT)

            {
                moveTheModelFragment(me, MouseEvent.MOUSE_PRESSED);
                me.consume();
                return;
            }

            try

            {
                if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_MODEL &&
                        mclnGraphViewEditor.currentOperationStep ==
                                AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING) {
                    me.consume();
                    MclnGraphNodeView mclnGraphViewNode = mclnGraphDesignerView.getGraphNodeAtCoordinates(x, y);
                    mclnGraphViewEditor.selectedMclnGraphViewNode = mclnGraphViewNode;
                    moveModel(me, MouseEvent.MOUSE_PRESSED);
                    me.consume();
                    return;
                }
            } catch (
                    Exception e)

            {
                e.printStackTrace();
            }

            // ========================================================================================================

            /*
                 Process Editing Operations
             */

            //            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_PROPERTIES ||
//                    mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS) {
//                mclnGraphDesignerView.snapToGridLine(me);
//            }
            processEditingOperationsUponMousePressed(me);
            me.consume();
        }

        // ============================================================================================================
        //                       M o u s e   R e l e a s e d
        // ============================================================================================================

        /**
         * @param me
         */
        @Override
        public void mouseReleased(MouseEvent me) {
//            System.out.println("MOUSE_RELEASED");

            if (AppStateModel.Operation.CREATING_POLYLINE_ARCS == mclnGraphViewEditor.currentOperation) {
                polylineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_RELEASED);
                me.consume();
                return;
            }
            if (AppStateModel.Operation.CREATING_SPLINE_ARCS == mclnGraphViewEditor.currentOperation) {
                splineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_RELEASED);
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_ELEMENTS) {
                moveTheModelElements(me, MouseEvent.MOUSE_RELEASED);
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
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

            //
            //   drugged   during creation i n d i v i d u a l   n o d e   released
            //
            if (mclnGraphViewEditor.mclnGraphEntityViewToBeDragged != null &&
                    (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_PROPERTIES ||
                            mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS)) {
                // Releasing dragged during creation individual node
                checkIfDraggedEntityNodeReleasedOverOtherEntity(mclnGraphViewEditor.mclnGraphEntityViewToBeDragged,
                        mclnGraphViewEditor.currentOperation);
            }

            //  // Releasing dragged node, if any
            if (mclnGraphViewEditor.dragging && (mclnGraphViewEditor.selectedMclnGraphViewNode != null)) {
                mclnGraphDesignerView.placeEntity(mclnGraphViewEditor.selectedMclnGraphViewNode);
                mclnGraphDesignerView.eraseAndPaintEntityWithConnectedEntities(mclnGraphViewEditor.selectedMclnGraphViewNode);
                mclnGraphViewEditor.selectedMclnGraphViewNode.setHighlighted(false);
                mclnGraphViewEditor.selectedMclnGraphViewNode = null;
                mclnGraphViewEditor.dragging = false;
            }
            me.consume();
        }

        // ============================================================================================================
        //                       M o u s e   C l i c k e d
        // ============================================================================================================

        /**
         * @param me
         */
        @Override
        public void mouseClicked(MouseEvent me) {
            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_FRAGMENT) {
                moveTheModelFragment(me, MouseEvent.MOUSE_CLICKED);
                me.consume();
                return;
            }
            me.consume();
        }

        // ============================================================================================================
        //                       M o u s e   D r a g g e d
        // ============================================================================================================

        @Override
        public void mouseDragged(MouseEvent me) {

            if ((mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATING_SPLINE_ARCS ||
                    mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATING_POLYLINE_ARCS) &&
                    mclnGraphViewEditor.currentOperationStep != AppStateModel.OperationStep.PICK_UP_ARC_INPUT_NODE) {
                me.consume();
                return;
            }

            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_ELEMENTS) {
                moveTheModelElements(me, MouseEvent.MOUSE_DRAGGED);
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
            if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_PROPERTIES ||
                    mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS) {
                mclnGraphViewEditor.dragMclnGraphPropertyOrConditionDuringCreation(me, mclnGraphViewEditor.currentOperation);
            }
            me.consume();
        }

        // ============================================================================================================
        //                       M o u s e   M o v e d
        // ============================================================================================================

        @Override
        public void mouseMoved(MouseEvent me) {

            MclnGraphEntityView mclnGraphEntityView = checkIfMouseHoverEntity(me.getX(), me.getY());
            if (mclnGraphEntityView != null) {
                if (
                    //
                    // Highlighting Property nodes on mouse hover
                    //
                        (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_FRAGMENTS &&
                                mclnGraphEntityView.isPropertyNode()) ||

//                                (
//                                        (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATING_POLYLINE_ARCS &&
//                                                (polylineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY ||
//                                                        polylineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION ||
//                                                        polylineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE
//                                                ) &&
//                                                ((polylineArcCreator.isArcInputNodeAProperty() && mclnGraphEntityView.isConditionNode()) ||
//                                                        (polylineArcCreator.isArcInputNodeACondition() && mclnGraphEntityView.isPropertyNode())
//                                                )
//                                        ) ||
//                                                (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATING_SPLINE_ARCS &&
//                                                        (splineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY ||
//                                                                splineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION ||
//                                                                splineArcCreator.currentOperationStep == AppStateModel.OperationStep.PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE
//                                                        )&&
//                                                        ((splineArcCreator.isArcInputNodeAProperty() && mclnGraphEntityView.isConditionNode()) ||
//                                                                (splineArcCreator.isArcInputNodeACondition() && mclnGraphEntityView.isPropertyNode())
//                                                        )
//                                                )
//                                ) ||

                                //
                                // Highlighting Property, Condition or Arcs on mouse hover
                                // when moving elements
                                mclnGraphViewEditor.currentOperation == AppStateModel.Operation.MOVE_ELEMENTS ||
                                //
                                // Highlighting Property or Condition nodes on mouse hover
                                // when creating (or moving) Property or Condition
                                ((mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_PROPERTIES ||
                                        mclnGraphViewEditor.currentOperation == AppStateModel.Operation.CREATE_CONDITIONS) &&
                                        me.isControlDown())
                    //&& mclnGraphEntityView.isMclnGraphNode())
                        ) {

//                    System.out.println("" + mclnGraphEntityView.getEntityTypeAsString());
                    mclnGraphEntityView.setMouseHover(true);
                    mclnGraphDesignerView.setMouseHoveredEntity(mclnGraphEntityView);
                }
            } else {
                mclnGraphDesignerView.setMouseHoveredEntity(null);
            }
            // when while menu was open and mouse moved, the arc
            // end and the mouse got different positions.
            if (AppStateModel.Operation.CREATING_POLYLINE_ARCS == mclnGraphViewEditor.currentOperation) {
                polylineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_MOVED);
            }
            if (AppStateModel.Operation.CREATING_SPLINE_ARCS == mclnGraphViewEditor.currentOperation) {
                splineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_MOVED);
            }
            me.consume();
        }

    };

// ============================================================================================================
//
//                 M o u s e   L i s t e n e r   P r o c e s s o r   I n s t a n c e
//
// ============================================================================================================

    public MclnViewEditorMouseListener(MclnGraphDesignerView mclnGraphDesignerView, MclnGraphViewEditor mclnGraphViewEditor) {
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        this.mclnGraphViewEditor = mclnGraphViewEditor;
        splineArcCreator = new SplineArcCreator(mclnGraphViewEditor);
        polylineArcCreator = new PolylineArcCreator(mclnGraphViewEditor);
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

    private final void processEditingOperationsUponMousePressed(MouseEvent me) {
        if (mclnGraphViewEditor.currentOperation == AppStateModel.Operation.NONE) {
            return;
        }

        switch (mclnGraphViewEditor.currentOperation) {
            case CREATE_PROPERTIES:
            case CREATE_CONDITIONS:
                mclnGraphViewEditor.createMclnGraphPropertyOrCondition(me, mclnGraphViewEditor.currentOperation);
                break;
            case CREATING_SPLINE_ARCS:
                splineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_PRESSED);
                break;
            case CREATING_POLYLINE_ARCS:
                polylineArcCreator.processArcCreation(me, mclnGraphViewEditor.currentOperation, MouseEvent.MOUSE_PRESSED);
                break;
            case CREATE_FRAGMENTS:
                mclnGraphViewEditor.createConditionFragment(me);
                break;
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
        if (mclnGraphEntityView != null) {

            // show one line message
            String message = mclnGraphEntityView.getOneLineInfoMessage();
            AdfOneLineMessageManager.showInfoMessage(message);

            if (mclnGraphViewEditor.showDetailsTooltip) {
                String detailedTooltip = mclnGraphEntityView.getTooltip();
                AdfDetailedTooltipPopup.showTooltip(x, y, detailedTooltip);
            }

        } else {
            OneLineMessagePanel oneLineMessagePanel = OneLineMessagePanel.getInstance();
            oneLineMessagePanel.clearMessage();
            AdfOneLineMessageManager.clearMessage();

            AdfDetailedTooltipPopup.hideTooltip();
        }
        return mclnGraphEntityView;
    }

//  *********************************************************************************************************
//
//                                M o v i n g   M o d e l   E l e m e n t s
//
//  *********************************************************************************************************

    private void selectAndUnselectTheModelElementsToBeMoved(MouseEvent me, int eventType) {

        if (eventType != MouseEvent.MOUSE_PRESSED) {
            return;
        }

        if (isRMBPressed(me)) {
            if (selectedMclnGraphEntityView == null) {
                return;
            }
            if (selectedMclnGraphEntityView instanceof MclnArcView) {
                if (mclnGraphViewEditor.currentOperationStep ==
                        AppStateModel.OperationStep.ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING) {
                    selectedMclnGraphEntityView.toArcView().restoreBackupCSysKnots();
                    selectedMclnGraphEntityView.toArcView().movingArcCompleted();
                    mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
                }
            } else if (selectedMclnGraphEntityView instanceof MclnGraphNodeView) {
                if (mclnGraphViewEditor.currentOperationStep ==
                        AppStateModel.OperationStep.ELEMENT_PROPERTY_SELECTED_START_DRAGGING ||
                        mclnGraphViewEditor.currentOperationStep ==
                                AppStateModel.OperationStep.ELEMENT_CONDITION_SELECTED_START_DRAGGING) {
                    selectedMclnGraphEntityView.toMclnGraphNodeView().restoreBackupCSysLocation();
                    selectedMclnGraphEntityView.toMclnGraphNodeView().movingNodeCompleted();
                    mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
                }
            } else {
                return;
            }
            mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.SELECT_ELEMENT_TO_BE_MOVED;
            AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
            return;
        }

        //
        //   L e f t   M o u s e   B u t t o n   C o m m a n d s
        //

        if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.SELECT_ELEMENT_TO_BE_MOVED) {
            MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(me.getX(), me.getY());
            if (mclnGraphEntityView == null) {
                return;
            }
            if (mclnGraphEntityView instanceof MclnArcView) {
                // first press on arc arrow to selects it
                mclnGraphEntityView.toArcView().startMoving();
                selectedMclnGraphEntityView = mclnGraphEntityView;
                mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnGraphEntityView);

                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING;
                AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);

            } else if (mclnGraphEntityView instanceof MclnGraphNodeView) {
                // Mcln Graph Node selected
                System.out.println("Node selected - Start moving " + mclnGraphEntityView.getEntityTypeAsString());
                mclnGraphEntityView.toMclnGraphNodeView().startMoving();
                selectedMclnGraphEntityView = mclnGraphEntityView;
                mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnGraphEntityView);

                if (mclnGraphEntityView instanceof MclnPropertyView) {
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_PROPERTY_SELECTED_START_DRAGGING;
                } else {
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_CONDITION_SELECTED_START_DRAGGING;
                }
                AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
            }

        } else if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING) {
            MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(me.getX(), me.getY());
            if (mclnGraphEntityView == null) {
                // we are here because click was not on this or other arc arrow
                // this means click might be on the knot
                boolean selected = selectedMclnGraphEntityView.toArcView().selectAKnotUnderMouse(me.getPoint());
                if (selected) {
                    mclnGraphDesignerView.repaint();  // repaint is needed as the knot may be reselected
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_ARC_DRAGGING_KNOT;
                    AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                } else {
                    userStopsMovingEntity();
                }

            } else if (mclnGraphEntityView == selectedMclnGraphEntityView) {
                // we are here because same arc arrow was clicked second time
                // second press on the arc arrow un-selects selected arc
                userStopsMovingEntity();
                //  selectedMclnGraphEntityView = null;
            } else {
                // user cliched on other Arc to select it. Newly selected Arc knots will be moved
                userSelectedAnotherEntityToMove(mclnGraphEntityView);
            }


            // Moving Node
        } else if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_PROPERTY_SELECTED_START_DRAGGING ||
                mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_CONDITION_SELECTED_START_DRAGGING) {
            MclnGraphEntityView mclnGraphEntityView = mclnGraphDesignerView.getGraphEntityAtCoordinates(me.getX(), me.getY());
            if (mclnGraphEntityView == null) {
                // we are here because click was not on this or other entity
                // this means user wants to stop moving node
                userStopsMovingEntity();

            } else if (mclnGraphEntityView == selectedMclnGraphEntityView) {
                // When moving Node, it OK to click on selected node again.
                // This means use clicked on a Node to select, and then
                // pressed it to start dragging it.
            } else {
                // user cliched on other Node or Arc to select it. Newly selected Node or Arc knots will be moved
                userSelectedAnotherEntityToMove(mclnGraphEntityView);
            }
        }
    }

    /**
     *
     **/
    private void userSelectedAnotherEntityToMove(MclnGraphEntityView mclnGraphEntityView) {
        if (selectedMclnGraphEntityView instanceof MclnArcView) {
            selectedMclnGraphEntityView.toArcView().movingArcCompleted();
        } else {
            selectedMclnGraphEntityView.toMclnGraphNodeView().movingNodeCompleted();
        }
        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);

        if (mclnGraphEntityView instanceof MclnArcView) {
            mclnGraphEntityView.toArcView().startMoving();
            mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING;
        } else {

            mclnGraphEntityView.toMclnGraphNodeView().startMoving();
            if (mclnGraphEntityView instanceof MclnPropertyView) {
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_PROPERTY_SELECTED_START_DRAGGING;
            } else {
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_CONDITION_SELECTED_START_DRAGGING;
            }
        }
        AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
        selectedMclnGraphEntityView = mclnGraphEntityView;
        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(mclnGraphEntityView);
    }

    /**
     *
     **/
    private void userStopsMovingEntity() {
        // we are here because same entity was clicked second time
        // second press on the entity un-selects selected entity
        if (selectedMclnGraphEntityView instanceof MclnArcView) {
            selectedMclnGraphEntityView.toArcView().movingArcCompleted();
        } else {
            selectedMclnGraphEntityView.toMclnGraphNodeView().movingNodeCompleted();
        }
        selectedMclnGraphEntityView = null;
        mclnGraphDesignerView.makeGraphEntityToBeASpritePaintedOnTheScreenOnly(null);
        mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.SELECT_ELEMENT_TO_BE_MOVED;
        AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
    }

    /**
     *
     **/
    void moveTheModelElements(MouseEvent me, int eventType) {
        if (eventType == MouseEvent.MOUSE_DRAGGED) {
            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_PROPERTY_SELECTED_START_DRAGGING ||
                    mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_CONDITION_SELECTED_START_DRAGGING) {

                MclnGraphNodeView mclnGraphViewNode = selectedMclnGraphEntityView.toMclnGraphNodeView();
                mclnGraphDesignerView.snapToGridLine(me);
                mclnGraphViewNode.druggingSelectedNode(me.getX(), me.getY());

            } else if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_ARC_DRAGGING_KNOT) {
                MclnArcView mclnArcView = selectedMclnGraphEntityView.toArcView();
                mclnGraphDesignerView.snapToGridLine(me);
                mclnArcView.druggingSelectedArc(me.getPoint());
            }
            mclnGraphDesignerView.repaint();
            return;
        }

        // mouse button released while dragging
        if (eventType == MouseEvent.MOUSE_RELEASED) {
            if (selectedMclnGraphEntityView instanceof MclnArcView) {
                if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.ELEMENT_ARC_DRAGGING_KNOT) {
                    mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING;
                    AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                }
            } else {
                // Mouse released while gragging Node entity
                if (selectedMclnGraphEntityView != null) {
                    //
                    //  We are here because mouse was release while dragging entity as part of Move Elements operation
                    //  Checking if mouse was released while dragged entity is over other entity.
                    //  If yes. move dragged entity to last location before dragging started.
                    //
                    checkIfDraggedEntityNodeReleasedOverOtherEntity(selectedMclnGraphEntityView,
                            mclnGraphViewEditor.currentOperation);
                }

            }
            return;
        }
    }

    private final void checkIfDraggedEntityNodeReleasedOverOtherEntity(MclnGraphEntityView selectedMclnGraphEntityView,
                                                                       AppStateModel.Operation operation) {
        if (selectedMclnGraphEntityView == null) {
            return;
        }

        // check if dragged node released on the top of some entity
        int[] scrVec = selectedMclnGraphEntityView.toMclnGraphNodeView().getScrVec();
        int x = scrVec[0];
        int y = scrVec[1];
        MclnGraphEntityView otherMclnGraphEntityView =
                mclnGraphDesignerView.getOtherThanThisEntityAtCoordinates(selectedMclnGraphEntityView, x, y);

        if (otherMclnGraphEntityView == null) {
            // Preserve last location for possible rollback
            selectedMclnGraphEntityView.toMclnGraphNodeView().preserveLastLocation();
        } else {
            // Returning dragged node to its previous position
            selectedMclnGraphEntityView.toMclnGraphNodeView().moveToLastLocation();
            entityCannotBePlacedOverOtherEntity(selectedMclnGraphEntityView, otherMclnGraphEntityView, operation);
        }
    }

    private void entityCannotBePlacedOverOtherEntity(MclnGraphEntityView thisEntity, MclnGraphEntityView otherEntity,
                                                     AppStateModel.Operation operation) {
        String nodeType = thisEntity.getEntityTypeAsString();
        String cannotReleaseNodeOnTHeTopOfEntityMessage =
                new StringBuilder().append("<html><div style=\"text-align:center; \">").
                        append("<font  color=\"#020080\" size=\"" + 3 + "\">").
                        append(nodeType + " being dragged cannot be placed over other entity ").
                        append("(" + otherEntity.getEntityTypeAsString() + ")&nbsp;&nbsp;!&nbsp;&nbsp;&nbsp;<br>").
                        append("Please restart dragging.&nbsp;&nbsp;&nbsp;").
                        append("</font>").
                        append("<br>").
                        append("</html>").toString();
        DsdsDseMessagesAndDialogs.showWarning(mclnGraphDesignerView, "Operation " + operation,
                cannotReleaseNodeOnTHeTopOfEntityMessage);
    }


//  **************************************************************************************************************
//
//                                M o v i n g   M o d e l   F r a g m e n t
//
//  **************************************************************************************************************

    private void moveTheModelFragment(MouseEvent me, int eventType) {

        if (eventType == MouseEvent.MOUSE_PRESSED) {

            //
            //   R i g h t   M o u s e   B u t t o n   C o m m a n d s
            //

            if (isRMBPressed(me)) {

                if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.START_SELECTING_NODES_TO_BE_MOVED) {
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
                if (mclnGraphViewEditor.selectedMclnNodesToBeMoved.size() != 1) {
                    return;
                }
                mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE;
                AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
                return;
            }

            if (mclnGraphViewEditor.currentOperationStep == AppStateModel.OperationStep.SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE) {
                boolean okToDrug = mclnGraphViewEditor.selectAFragmentNodeToBeMoved(me);
                return;
            }

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

//  **************************************************************************************************************
//
//                                M o v i n g   m o d e l   a s   a   w h o l e
//
//  **************************************************************************************************************

    private void moveModel(MouseEvent me, int eventType) {

        // initiate dragging
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            mclnGraphViewEditor.currentOperationStep = AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE;
            AppStateModel.getInstance().setCurrentOperationStep(AppStateModel.OperationStep.DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE);
            mclnGraphViewEditor.prepareEntireModelToBeMoved(me);
            return;
        }

        // make next move
        if (eventType == MouseEvent.MOUSE_DRAGGED) {
            mclnGraphViewEditor.moveModelStep(me);
            return;
        }

        // take final location
        if (eventType == MouseEvent.MOUSE_RELEASED) {
            mclnGraphViewEditor.takeTheModelViewFinalLocation();
            mclnGraphViewEditor.currentOperationStep =
                    AppStateModel.OperationStep.CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING;
            AppStateModel.getInstance().setCurrentOperationStep(mclnGraphViewEditor.currentOperationStep);
            return;
        }
    }
}

