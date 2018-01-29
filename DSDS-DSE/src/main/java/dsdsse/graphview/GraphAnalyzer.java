package dsdsse.graphview;

import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 2/8/2016.
 */
final class GraphAnalyzer {

    static boolean findConnectingArcs(Set<MclnGraphViewNode> selectedNodesToBeMoved, Set<MclnArcView> arcsToBeMoved,
                                      Set<MclnArcView> connectingArcsThatWilBeDiscarded) {
        arcsToBeMoved.clear();
        connectingArcsThatWilBeDiscarded.clear();
        for(MclnGraphViewNode mclnGraphViewNode : selectedNodesToBeMoved){
            List<MclnArcView> inpArcList = mclnGraphViewNode.inpArcList;
            for(MclnArcView mclnArcView : inpArcList){
                MclnGraphViewNode mclnArcViewInpNode = mclnArcView.getInpNode();
                boolean contains = selectedNodesToBeMoved.contains(mclnArcViewInpNode);
                if(contains){
                    // the other end of arc is also selected -> adding it to the list
                    arcsToBeMoved.add(mclnArcView);
                }else{
                    connectingArcsThatWilBeDiscarded.add(mclnArcView);
                }
            }
            List<MclnArcView> outArcList = mclnGraphViewNode.outArcList;
            for(MclnArcView mclnArcView : outArcList){
                // getting arc's other end node
                MclnGraphViewNode mclnArcViewOutNode = mclnArcView.getOutNode();
                boolean contains = arcsToBeMoved.contains(mclnArcViewOutNode);
                if(!contains){
                    connectingArcsThatWilBeDiscarded.add(mclnArcView);
                }
            }
        }

        connectingArcsThatWilBeDiscarded.removeAll(arcsToBeMoved);

        for (MclnArcView mclnArcView : arcsToBeMoved) {
            mclnArcView.setWatermarked(false);
//            mclnGraphViewArc.setHighlighted(true);
//            mclnGraphViewArc.setSelected(true);
        }

        for (MclnArcView mclnArcView : connectingArcsThatWilBeDiscarded) {
            mclnArcView.setWatermarked(true);
        }

        return !arcsToBeMoved.isEmpty(); // true if arc(s) to be moved found
    }
}
