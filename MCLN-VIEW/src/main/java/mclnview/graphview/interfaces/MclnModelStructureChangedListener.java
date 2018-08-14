package mclnview.graphview.interfaces;

import mcln.model.MclnArc;
import mcln.model.MclnCondition;
import mcln.model.MclnNode;
import mcln.model.MclnStatement;
import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnConditionView;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnPropertyView;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/1/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnModelStructureChangedListener {

    public MclnPropertyView mclnStatementAdded(MclnStatement mclnStatement);

    public void mclnStatementRemoved(MclnStatement mclnStatement);

    public MclnConditionView mclnConditionAdded(MclnCondition mclnCondition);

    public void mclnConditionRemoved(MclnCondition mclnCondition);

    public MclnArcView incompleteMclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc);

    public MclnArcView mclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc);

    public void mclnArcRemoved(MclnArc mclnArc);

    public void mclnModelViewRectangleUpdated(MclnGraphModel mclnGraphModel);
}