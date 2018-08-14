package dsdsse.graphview;

import mcln.model.MclnArc;
import mclnview.graphview.MclnGraphNodeView;

import java.awt.*;

public interface DesignerArcView {

    String getUID();

    MclnArc getTheElementModel();

    MclnGraphNodeView getInpNode();

    MclnGraphNodeView getOutNode();

    //  *********************************************************************************************************
    //
    //          I n i t i a l i z i n g   A r c   b y   I n i t i a l i z a t i o n   A s s i s t a n t
    //
    //  *********************************************************************************************************

    Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation);

    double[] getScrOutlineCenter();

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    void repaintArrowUponInitialization();

}
