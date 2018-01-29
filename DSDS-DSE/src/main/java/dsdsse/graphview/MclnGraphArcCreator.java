package dsdsse.graphview;

import dsdsse.app.AppStateModel;

/**
 * Created by Admin on 8/24/2017.
 */
public class MclnGraphArcCreator {

   void threePointArcUnDoStraightChoice(MclnGraphDesignerView mclnGraphDesignerView, MclnArcView mclnArcView, MclnGraphViewNode arcInputNode,
                                        MclnGraphViewNode arcOutputNode) {

        // Undo operation if RMB pressed

       mclnArcView.resetArcSpline();

//            currentArc.removeOutputNode();
//            currentArc.deletePoint(2);
////            currentArc.deletePoint(1);
//            currentArc.setSelectedKnotInd(-1);
//
//            double[] splineScrStartPoint = currentArcInputNode.getScrPnt();
//
//            currentArc. createFirstScrKnotMakeItActive(splineScrStartPoint);
//            addNextScrKnotAndMakeItActive(splineScrStartPoint);

        // resetting arc presentation
       mclnArcView.removeOutputNode();
       mclnArcView.setSelected(true);
       mclnArcView.setSplineThreadSelected(true);
       mclnArcView.setDrawKnots(true);
       mclnArcView.setAllKnotsSelected(false);

        // unselect output node
       arcOutputNode.setSelected(false);
//            mclnGraphView.paintEntityOnly(currentArcOutputNode);
       arcOutputNode = null;

//            mclnGraphView.paintEntityOnlyOnTheScreen(currentArc);
        mclnGraphDesignerView.repaintImageAndSpriteEntities();
//
        AppStateModel.OperationStep currentOperationStep;
        if (arcInputNode.isPropertyNode()) {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION;
        } else {
            currentOperationStep = AppStateModel.OperationStep.PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY;
        }
        AppStateModel.getInstance().setCurrentOperationStep(currentOperationStep);

//        mclnGraphView.paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(x, y, currentArc);
        return;
    }
}
