package mclnview.graphview.interfaces;

import mcln.model.MclnArc;
import mcln.model.MclnCondition;
import mcln.model.MclnNode;
import mcln.model.MclnStatement;
import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnConditionView;
import mclnview.graphview.MclnGraphViewModel;
import mclnview.graphview.MclnPropertyView;

/**
 * Created by Admin on 11/15/2017.
 */
public interface MclnModelStructureChangedListener {
    public MclnPropertyView mclnStatementAdded(MclnStatement mclnStatement);

    public void mclnStatementRemoved(MclnStatement mclnStatement);

    public MclnConditionView mclnConditionAdded(MclnCondition mclnCondition);

    public void mclnConditionRemoved(MclnCondition mclnCondition);

    public MclnArcView incompleteMclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc);

//    public void arcCreationCompleted(MclnGraphViewArc mclnGraphViewArc);

    public MclnArcView mclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc);

    public void mclnArcRemoved(MclnArc mclnArc);

    public void mclnModelViewRectangleUpdated(MclnGraphViewModel mclnGraphModel);
}
