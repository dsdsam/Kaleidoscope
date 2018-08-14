package dsdsse.graphview;

import mcln.model.MclnArc;
import mclnview.graphview.MclnGraphNodeView;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnSplineArcView;

import java.awt.*;

class MclnGraphDesignerSplineArcView extends MclnSplineArcView implements DesignerArcView{

    private final MclnGraphDesignerView mclnGraphDesignerView;

    //  ***********************************************************************************************
    //
    //   T h e   M e t h o d s   t o   C r e a t   A r c   v i a   G r a h i c a l   I n t e r f a c e
    //
    //  ***********************************************************************************************


    MclnGraphDesignerSplineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(parentCSys, mclnArc, inpNode);
        this.mclnGraphDesignerView = parentCSys;
    }

    MclnGraphDesignerSplineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode,
                      MclnGraphNodeView outNode) {
        super(parentCSys, mclnArc, inpNode, outNode);
        this.mclnGraphDesignerView = parentCSys;
    }

    public MclnGraphNodeView getInpNode() {
        return inpNode;
    }

    public MclnGraphNodeView getOutNode() {
        return outNode;
    }

    //  *********************************************************************************************************
    //
    //          I n i t i a l i z i n g   A r c   b y   I n i t i a l i z a t i o n   A s s i s t a n t
    //
    //  *********************************************************************************************************

    @Override
    public Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation) {
        return mcLnGraphSplineEntity.buildTriangleArrow(arrowLength, arrowWidth, arrowTipScrLocation);
    }

    @Override
    public double[] getScrOutlineCenter() {
        return mcLnGraphSplineEntity.getScrOutlineCenter();
    }

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    @Override
    public void repaintArrowUponInitialization() {
        arcMclnState = mclnSplineArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);
        mcLnGraphSplineEntity.updateStateColorUponInitialization(arcMclnState);
        mclnGraphDesignerView.drawArcAndConnectedEntitiesOnTheImageAndCallRepaint(this);
    }

}
