package mclnview.graphview;

import mcln.model.MclnArc;
import mcln.model.MclnCondition;
import mcln.model.MclnNode;
import mcln.model.MclnStatement;

/**
 * Created by Admin on 11/14/2017.
 */
public final class MclnGraphVewFactory {

    //
    //   S t a t e m e n t   c r e a t i o n
    //

    public static MclnPropertyView createMclnGraphViewStatement(MclnGraphView parentCSys,
                                                                MclnStatement mclnStatement) {
        MclnPropertyView mclnPropertyView = new MclnPropertyView(parentCSys, mclnStatement);
        return mclnPropertyView;
    }

    //
    //   C o n d i t i o n   c r e a t i o n
    //

    public static MclnConditionView createMclnGraphViewCondition(MclnGraphView parentCSys,
                                                                 MclnCondition mclnCondition) {
        MclnConditionView mclnConditionView = new MclnConditionView(parentCSys, mclnCondition);
        return mclnConditionView;
    }

    //
    //   A r c   c r e a t i o n
    //

//    /**
//     * called by Editor after arc input node selected
//     *
//     * @param parentCSys
//     * @param arcName
//     * @param knob
//     * @param radius
//     * @param initState
//     * @param threshold
//     * @param inpNode
//     * @return
//     */
//    public static MclnGraphViewArc createIncompleteMclnGraphArcViewForEditor(
//            ArrowTipLocationPolicy arrowTipLocationPolicy, MclnGraphView parentCSys, String arcName,
//            double knob[], double radius, int initState, int threshold, MclnGraphViewPointEntity inpNode) {
//
//        MclnProject currentMclnProject = MclnProject.getCurrentMclnProject();
//
//        MclnNode mclnArcInputNode = inpNode.getTheElementModel();
//        List<double[]> knotCSysLocations = new ArrayList();
//        MclnArc<MclnNode, MclnNode> mclnArc = currentMclnProject.createIncompleteMclnArc(arrowTipLocationPolicy, knotCSysLocations,
//                mclnArcInputNode);
//
//        MclnGraphViewArc mclnGraphViewArc = null;
////                new MclnGraphViewArc(parentCSys, mclnArc,
////                arcName,
////                knob, radius,
////                initState, threshold,
////                inpNode);
//
//        return mclnGraphViewArc;
//    }

    public static MclnArcView createNewIncompleteMclnGraphArcViewForEditor(MclnGraphView parentCSys,
                                                                           MclnArc<MclnNode, MclnNode> mclnArc,
                                                                           MclnGraphViewNode inpNode) {
        MclnArcView mclnArcView = new MclnArcView(parentCSys, mclnArc, inpNode);
        return mclnArcView;
    }

//    /**
//     * This constructor is used to create arc of simple Fragment
//     *
//     * @param parentCSys
//     * @param arcName
//     * @param knobScrPoint
//     * @param inpNode
//     * @param outNode
//     * @return
//     */
//    static final MclnGraphViewArc createCompleteStraightMclnGraphArcViewForEditor(
//            ArrowTipLocationPolicy arrowTipLocationPolicy, MclnGraphView parentCSys, String arcName,
//            double[] knobScrPoint, MclnGraphViewPointEntity inpNode, MclnGraphViewPointEntity outNode) {
//
//        //
//        // Create Mcln Arc model
//        //
//
//        MclnNode mclnArcInputNode = inpNode.getTheElementModel();
//        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
//
//        List<double[]> knotCSysLocations = new ArrayList();
//        knotCSysLocations.add(mclnArcInputNode.getCSysLocation());
//        double[] knobCSysLocation = parentCSys.screenPointToCSysPoint(knobScrPoint);
//        knotCSysLocations.add(knobCSysLocation);
//        knotCSysLocations.add(mclnArcOutputNode.getCSysLocation());
//
//        MclnProject currentMclnProject = MclnProject.getCurrentMclnProject();
//        MclnArc mclnArc = currentMclnProject.createStraightMclnArc(arrowTipLocationPolicy, knotCSysLocations,
//                mclnArcInputNode, mclnArcOutputNode);
//        mclnArc.setKnobIndex(1);
//
//        //
//        // Create Mcln Arc model view
//        //
//
//        MclnGraphViewArc mclnGraphViewArc = createMclnGraphViewArc(parentCSys, mclnArc, inpNode, outNode);
//        return mclnGraphViewArc;
//    }

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
                                                     MclnGraphViewNode inpNode,
                                                     MclnGraphViewNode outNode) {

        MclnArcView mclnArcView = new MclnArcView(parentCSys, mclnArc, inpNode, outNode);
        return mclnArcView;
    }
}

