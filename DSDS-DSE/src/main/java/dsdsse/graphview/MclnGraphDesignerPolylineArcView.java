package dsdsse.graphview;

import mcln.model.MclnArc;
import mclnview.graphview.MclnGraphNodeView;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnPolylineArcView;

import java.awt.*;

class MclnGraphDesignerPolylineArcView extends MclnPolylineArcView implements DesignerArcView{

    private final MclnGraphDesignerView mclnGraphDesignerView;

    //  ***********************************************************************************************
    //
    //   T h e   M e t h o d s   t o   C r e a t   A r c   v i a   G r a h i c a l   I n t e r f a c e
    //
    //  ***********************************************************************************************

    /**
     * Creates incomplete Arc with Input Node only - used when the arc is created by user
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     */
    MclnGraphDesignerPolylineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(parentCSys, mclnArc, inpNode);
        this.mclnGraphDesignerView = parentCSys;
    }

    MclnGraphDesignerPolylineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode,
                        MclnGraphNodeView outNode) {
        super(parentCSys, mclnArc, inpNode, outNode);
        this.mclnGraphDesignerView = parentCSys;
    }

    //  *********************************************************************************************************
    //
    //          I n i t i a l i z i n g   A r c   b y   I n i t i a l i z a t i o n   A s s i s t a n t
    //
    //  *********************************************************************************************************

    @Override
    public Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation) {
        return mclnGraphPolylineEntity.buildTriangleArrow(arrowLength, arrowWidth, arrowTipScrLocation);
    }

    @Override
    public double[] getScrOutlineCenter() {
        return mclnGraphPolylineEntity.getScrOutlineCenter();
    }

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    @Override
    public void repaintArrowUponInitialization() {
        arcMclnState = mclnPolylineArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mclnGraphPolylineEntity.setArcMclnStateColor(stateColor);
        mclnGraphPolylineEntity.updateStateColorUponInitialization(arcMclnState);
        mclnGraphDesignerView.drawArcAndConnectedEntitiesOnTheImageAndCallRepaint(this);
    }


}
