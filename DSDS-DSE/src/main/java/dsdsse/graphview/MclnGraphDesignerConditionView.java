package dsdsse.graphview;

import mcln.model.MclnCondition;
import mclnview.graphview.MclnConditionView;
import mclnview.graphview.MclnGraphView;

class MclnGraphDesignerConditionView extends MclnConditionView implements DesignerNodeView {

    MclnGraphDesignerConditionView(MclnGraphView parentCSys, MclnCondition mclnCondition) {
        super(parentCSys, mclnCondition);
    }
}
