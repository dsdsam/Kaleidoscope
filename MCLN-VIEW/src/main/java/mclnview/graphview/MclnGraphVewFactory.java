package mclnview.graphview;

import mcln.model.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 5/29/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MclnGraphVewFactory {

    //
    //   S t a t e m e n t   c r e a t i o n
    //

    public static MclnPropertyView createMclnGraphViewStatement(MclnGraphView parentCSys,
                                                                MclnStatement mclnStatement) {
        MclnPropertyView mcLnPropertyView = new MclnPropertyView(parentCSys, mclnStatement);
        return mcLnPropertyView;
    }

    //
    //   C o n d i t i o n   c r e a t i o n
    //

    public static MclnConditionView createMclnGraphViewCondition(MclnGraphView parentCSys,
                                                                 MclnCondition mclnCondition) {
        MclnConditionView mcLnConditionView = new MclnConditionView(parentCSys, mclnCondition);
        return mcLnConditionView;
    }

    //
    //   A r c   c r e a t i o n
    //

    public static MclnArcView createNewIncompleteMclnGraphArcViewForEditor(MclnGraphView parentCSys,
                                                                           MclnArc<MclnNode, MclnNode> mclnArc,
                                                                           MclnGraphNodeView inpNode) {
        MclnArcView mclnArcView = new MclnSplineArcView(parentCSys, mclnArc, inpNode);
        return mclnArcView;
    }

    public static MclnPolylineArcView createNewIncompleteMclnGraphPolylineArcViewForEditor(MclnGraphView parentCSys,
                                                                                           MclnArc<MclnNode, MclnNode> mclnArc,
                                                                                           MclnGraphNodeView inpNode) {
        MclnPolylineArcView mclnPolylineArcView = new MclnPolylineArcView(parentCSys, mclnArc, inpNode);
        return mclnPolylineArcView;
    }

    /**
     * Called when arc is created with model, programmatically or retrieved from saved model
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     * @param outNode
     * @return
     */
    public static MclnArcView createMclnGraphViewArc(MclnGraphView parentCSys, MclnArc mclnArc,
                                                     MclnGraphNodeView inpNode,
                                                     MclnGraphNodeView outNode) {
        MclnArcView mclnArcView;
        if (mclnArc != null && mclnArc instanceof MclnPolylineArc) {
            mclnArcView = new MclnPolylineArcView(parentCSys, mclnArc, inpNode, outNode);
        } else {
            mclnArcView = new MclnSplineArcView(parentCSys, mclnArc, inpNode, outNode);
        }
        return mclnArcView;
    }
}
