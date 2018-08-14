package dsdsse.graphview;

import mcln.model.*;
import mclnview.graphview.*;

public class MclnGraphDesignerViewFactory {

    //
    //   S t a t e m e n t   c r e a t i o n
    //

    public static McLnGraphDesignerPropertyView createMclnGraphDesignerPropertyView(MclnGraphDesignerView mclnGraphDesignerView,
                                                                                    MclnStatement mclnStatement) {
        McLnGraphDesignerPropertyView mcLnGraphDesignerPropertyView =
                new McLnGraphDesignerPropertyView(mclnGraphDesignerView, mclnStatement);
        return mcLnGraphDesignerPropertyView;
    }

    //
    //   C o n d i t i o n   c r e a t i o n
    //

    public static MclnGraphDesignerConditionView createMclnGraphDesignerConditionView(MclnGraphDesignerView mclnGraphDesignerView,
                                                                                      MclnCondition mclnCondition) {
        MclnGraphDesignerConditionView mclnGraphDesignerConditionView =
                new MclnGraphDesignerConditionView(mclnGraphDesignerView, mclnCondition);
        return mclnGraphDesignerConditionView;
    }

    //
    //   A r c   c r e a t i o n
    //

    public static MclnGraphDesignerPolylineArcView createIncompleteMclnGraphDesignerPolylineArcView(MclnGraphDesignerView mclnGraphDesignerView,
                                                                                       MclnArc<MclnNode, MclnNode> mclnArc,
                                                                                       MclnGraphNodeView inpNode) {
        MclnGraphDesignerPolylineArcView mclnGraphDesignerPolylineArcView =
                new MclnGraphDesignerPolylineArcView(mclnGraphDesignerView, mclnArc, inpNode);
        return mclnGraphDesignerPolylineArcView;
    }

    public static MclnGraphDesignerSplineArcView createIncompleteMclnGraphDesignerSplineArcView(MclnGraphDesignerView mclnGraphDesignerView,
                                                                             MclnArc<MclnNode, MclnNode> mclnArc,
                                                                             MclnGraphNodeView inpNode) {
        MclnGraphDesignerSplineArcView mclnGraphDesignerSplineArcView =
                new MclnGraphDesignerSplineArcView(mclnGraphDesignerView, mclnArc, inpNode);
        return mclnGraphDesignerSplineArcView;
    }

    /**
     * Called when arc is created with model, programmatically or retrieved from saved model
     *
     * @param mclnGraphDesignerView
     * @param mclnArc
     * @param inpNode
     * @param outNode
     * @return
     */
    public static MclnArcView createMclnGraphDesignerArcView(MclnGraphDesignerView mclnGraphDesignerView, MclnArc mclnArc,
                                                             MclnGraphNodeView inpNode,
                                                             MclnGraphNodeView outNode) {
        MclnArcView mclnArcView;
        if (mclnArc != null && mclnArc instanceof MclnPolylineArc) {
            mclnArcView = new MclnGraphDesignerPolylineArcView(mclnGraphDesignerView, mclnArc, inpNode, outNode);
        } else {
            mclnArcView = new MclnGraphDesignerSplineArcView(mclnGraphDesignerView, mclnArc, inpNode, outNode);
        }
        return mclnArcView;
    }

}
