package dsdsse.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnArc;
import mcln.model.MclnNode;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 26, 2013
 * Time: 7:35:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MclnArcView extends MclnGraphEntityView<MclnArc> {

    private static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    private static final Color DEFAULT_ARC_COLOR = Color.LIGHT_GRAY;

    final MclnArc mclnArc; // arc data model

    MclnGraphDesignerView parentCSys;
    MclnState arcMclnState;
    MclnGraphViewNode inpNode;
    MclnGraphViewNode outNode;

    boolean straightArc;
//    private int selKnotInd = -1;

    //    MclnGraphSplineEntity mcLnGraphSplineEntity;
    MclnArcArrow mclnArcArrow;

    StringBuilder oneLineMessageBuilder = new StringBuilder();

    //
    //  C r e a t i o n   m e t h o d s
    //

    /**
     * Creates incomplete Arc with Input Node only - used when the arc is created by user
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     */
    MclnArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;

        this.mclnArc = mclnArc;
        this.inpNode = inpNode;

        // set arc to be a spline
        straightArc = false;
        arcMclnState = mclnArc.getArcMclnState();
    }

//    protected void initBeingCreatedArc(CSysView parentCSys, MclnGraphViewNode inpNode) {
//        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, inpNode);
//        double[] splineScrStartPoint = inpNode.getScrPnt();
//
//        createFirstScrKnotMakeItActive(splineScrStartPoint);
//        addNextScrKnotAndMakeItActive(splineScrStartPoint);
//
//        mcLnGraphSplineEntity.setSelected(true);
//
//        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
//        mcLnGraphSplineEntity.setArcColor(stateColor);
//    }

//    public void resetArcSpline() {
//        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, inpNode);
//        double[] splineScrStartPoint = inpNode.getScrPnt();
//
//        createFirstScrKnotMakeItActive(splineScrStartPoint);
//        addNextScrKnotAndMakeItActive(splineScrStartPoint);
//
//        mcLnGraphSplineEntity.setSelected(true);
//        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
//        mcLnGraphSplineEntity.setArcColor(stateColor);
//    }
//

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    abstract void setOutputNode(MclnGraphViewNode outNode);

    //    public void setOutputNode(MclnGraphViewNode outNode) {
//        this.outNode = outNode;
//        mcLnGraphSplineEntity.setOutputNode(outNode);
//    }

    /**
     *
     */
    abstract void finishArcCreation();

//      void finishArcCreation() {
//
//        // complete MCLN Arc creation
//        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
//        mclnArc.setOutNode(mclnArcOutputNode);
//        List<double[]> knotCSysLocations = this.getKnotCSysLocations();
//        mclnArc.setCSysKnots(knotCSysLocations);
//
//        arcMclnState = mclnArc.getArcMclnState();
//
//        // complete spline creation
//        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
//        mcLnGraphSplineEntity.setArcColor(stateColor);
//        mcLnGraphSplineEntity.calculate();
//
//        mclnArcArrow = mcLnGraphSplineEntity.editorFinishedSplineCreation(mclnArc);
//
////        mclnArcArrow = constructArrow(parentCSys, stateColor);
//
//        // storing spline cSys points and arrow cSys points for future xmlization
////        if (mclnArcArrow != null) {
////            List<double[]> splineCSysPoints = mclnGraphSplineViewEntity.getSplineCSysPoints();
////            double[][] arrowCSysPoints = mclnArcArrow.arrowScrPointsToCSysPoints(mclnArcArrow.getViewPoints());
////            mclnArc.setSplineCSysPoints(splineCSysPoints, arrowCSysPoints);
////        }
//    }

    /**
     * This constructor is used when arc model is available:
     * 1) When ArcView is created as part of Demo-project
     * 2) when McLN Fragment is created by Editor.
     * 3) When McLN recreated from saved model
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     * @param outNode
     */
    MclnArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode,
                MclnGraphViewNode outNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;
        this.mclnArc = mclnArc;
        this.inpNode = inpNode;
        this.outNode = outNode;
    }

//    /**
//     * This constructor is used when arc model is available:
//     * 1) When ArcView is created as part of Demo-project
//     * 2) when McLN Fragment is created by Editor.
//     * 3) When McLN recreated from saved model
//     *
//     * @param parentCSys
//     * @param mclnArc
//     * @param inpNode
//     * @param outNode
//     */
//    MclnArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode,
//                MclnGraphViewNode outNode) {
//        super(parentCSys, mclnArc);
//        this.parentCSys = parentCSys;
//        this.mclnArc = mclnArc;
//        this.inpNode = inpNode;
//        this.outNode = outNode;
//
//        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, inpNode, outNode);
//
//        arcMclnState = mclnArc.getArcMclnState();
//        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
//        mcLnGraphSplineEntity.setArcColor(stateColor);
//        mcLnGraphSplineEntity.setMclnArcStateColor(stateColor);
//
//        List<double[]> knotCSysLocations = mclnArc.getKnotCSysLocations();
//
//        int arrowTipSplineIndex = mclnArc.getArrowTipSplineIndex();
//        mcLnGraphSplineEntity.setArrowTipSplineIndex(arrowTipSplineIndex);
//
//        if (knotCSysLocations.size() == 2) {
////            new Exception("Arrow with two knots created").printStackTrace();
//            double[] midPoint = calculateKnobLocation(0, false, knotCSysLocations.get(0), knotCSysLocations.get(1));
//            knotCSysLocations.add(1, midPoint);
//            setKnobIndex(1);
//        } else {
//            setKnobIndex(mclnArc.getKnobIndex());
//        }
//        if (knotCSysLocations.size() > 3) {
////            System.out.println();
//        }
//        mcLnGraphSplineEntity.setSplineCSysKnots(knotCSysLocations);
//
//        setDrawKnots(false);
//        setSelected(false);
//        setSplineThreadSelected(false);
//        setAllKnotsSelected(false);
//        mcLnGraphSplineEntity.setNoActiveKnots();
//
//        mcLnGraphSplineEntity.calculate();
//
//        MclnArcArrow mclnArcArrow = mcLnGraphSplineEntity.constructRetrievedArrow(mclnArc, parentCSys, stateColor, inpNode, outNode);
//        this.mclnArcArrow = mclnArcArrow;
//        mcLnGraphSplineEntity.finishSplineCreation(mclnArc, mclnArcArrow);
//    }

    public Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation) {
        return mclnArcArrow.buildTriangleArrow(arrowLength, arrowWidth, arrowTipScrLocation);
    }

    @Override
    public MclnArc getTheElementModel() {
        return mclnArc;
    }

    public final boolean isRecognizingArc() {
        return inpNode != null && (inpNode instanceof MclnPropertyView);
    }

    public final boolean isGeneratingArc() {
        return inpNode != null && (inpNode instanceof MclnConditionView);
    }

    boolean isConnected() {
        return inpNode != null && outNode != null;
    }

    boolean isInitialized() {
        return true;
    }

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    public void repaintArrowUponInitialization() {
        MclnState arcMclnState = mclnArc.getArcMclnState();
        mclnArcArrow.updateStateColorUponInitialization(arcMclnState.getStateID());
        parentCSys.drawArcAndConnectedEntitiesOnTheImageAndCallRepaint(this);
    }

    public String getUID() {
        return mclnArc.getUID();
    }

    public MclnGraphViewNode getInpNode() {
        return inpNode;
    }

    public MclnGraphViewNode getOutNode() {
        return outNode;
    }

    public MclnArcView clone() {
        MclnArcView mclnArcViewClone = null;
        try {
            mclnArcViewClone = (MclnArcView) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mclnArcViewClone;
        }
    }

    //    //
//    //  E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
//    //
//
    abstract void setHighlightFlatSegments(boolean highlightFlatSegments);
//    void setHighlightFlatSegments(boolean highlightFlatSegments) {
//        mcLnGraphSplineEntity.setHighlightFlatSegments(highlightFlatSegments);
//    }
//
//    void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
//        mcLnGraphSplineEntity.setSelectingArrowTipLocation(selectingArrowTipLocation);
//    }
//
//    void destroyArcArrowUpOnUndoingArrowTipSelection() {
//        mcLnGraphSplineEntity.destroyArcArrowUpOnUndoingArrowTipSelection();
//    }
//
//    public void setHighlightArcKnotsForArrowTipSelection(boolean highlightArcKnotsForArrowTipSelection) {
//        this.mcLnGraphSplineEntity.setHighlightArcKnotsForArrowTipSelection(highlightArcKnotsForArrowTipSelection);
//    }

//    //    K n o t s   a n d   s p l i n e   c r e a t i o n
//
//    private void createFirstScrKnotMakeItActive(double[] scrPnt) {
//        mcLnGraphSplineEntity.createFirstScrKnotMakeItActive(scrPnt);
//    }
//
//    void addNextScrKnotAndMakeItActive(double x, double y) {
//        double[] scrPnt = {x, y, 0};
//        addNextScrKnotAndMakeItActive(scrPnt);
//    }
//
//    private void addNextScrKnotAndMakeItActive(double[] scrPnt) {
//        mcLnGraphSplineEntity.addNextScrKnotAndMakeItActive(scrPnt);
//    }
//
//    void addNextScrKnotAndMakePreviousKnotActive(double x, double y) {
//        double[] scrPnt = {x, y, 0};
//        mcLnGraphSplineEntity.addNextScrKnotAndMakePreviousKnotActive(scrPnt);
//    }
//
//    @Override
//    public void moveEntityActivePointTo(int x, int y) {
//        double[] activePointInCSysCoordinates = parentCSys.screenPointToCSysPoint(x, y);
//        moveEntityActivePointTo(activePointInCSysCoordinates);
//    }
//
//    @Override
//    public void moveEntityActivePointTo(double[] activePointInCSysCoordinates) {
////        System.out.println("MclnGraphArcViewEntity (mclnGraph Spline ViewEntity): updateArcPlacement   " +
////                activePointInCSysCoordinates[0]);
//        mcLnGraphSplineEntity.moveEntityActivePointTo(activePointInCSysCoordinates);
//    }
//
//    public int deleteLastPoint() {
//        int nPnts = mcLnGraphSplineEntity.deleteLastCSysKnot();
//        return nPnts;
//    }
//
//    public void removeOutputNode() {
//        setOutputNode(null);
//        updatePosition();
////        mclnGraphSplineViewEntity.setColor(Color.red);
//    }

    //    //
//    //   D r a w i n g   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
//    //
//
    abstract void setDrawKnots(boolean status);

    //
//    //   W a t e r m a r k
//
//    @Override
//    public void setWatermarked(boolean watermarked) {
//        this.watermarked = watermarked;
//        mcLnGraphSplineEntity.setWatermarked(watermarked);
//    }
//
    abstract public void setArrowWatermarked(boolean watermarked) ;
//    public void setArrowWatermarked(boolean watermarked) {
//        mcLnGraphSplineEntity.setArrowWatermarked(watermarked);
//    }
//
//    //   P r e - S e l e c t i o n   a n d   S e l e c t i o n
//
//    @Override
//    public void setPreSelected(boolean preSelected) {
//        mcLnGraphSplineEntity.setSelected(preSelected);
//    }
//
//    @Override
//    public void setSelected(boolean status) {
//        mcLnGraphSplineEntity.setSelected(status);
//    }
//
//    public void setArrowSelected(boolean watermarked) {
//        mcLnGraphSplineEntity.setArrowSelected(watermarked);
//    }
//
//    public boolean isSelected() {
//        return mcLnGraphSplineEntity.isSelected();
//    }
//
    abstract public void setAllKnotsSelected(boolean status);

    //
//    public void setKnobSelected(boolean status) {
//        mcLnGraphSplineEntity.setKnobSelected(status);
//    }
//
    abstract void setSplineThreadSelected(boolean status);
//
//    //   H i g h l i g h t i n g
//
//    @Override
//    public void setHighlighted(boolean highlighted) {
//        mcLnGraphSplineEntity.setHighlighted(highlighted);
//    }
//
//    public void setArrowHighlighted(boolean highlighted1) {
//        mcLnGraphSplineEntity.setArrowHighlighted(highlighted1);
//    }
//
//    @Override
//    public void setHighlightColor(Color highlightedColor) {
//        mcLnGraphSplineEntity.setHighlightColor(highlightedColor);
//    }
//
//    @Override
//    public boolean isMouseHover(int x, int y) {
//        return mcLnGraphSplineEntity.isMouseHover(x, y);
//    }
//
//    public boolean isKnotPicked(int x, int y) {
//
////            if (MCLNMedia.drawArcKnobAsArrow  &&
////                    !(parentCSys.currentOperation == parentCSys.MOVE_ELEMENT  ||
////                            parentCSys.currentOperation == parentCSys.DELETE_ELEMENT ||
////                            parentCSys.currentOperation == parentCSys.CREATE_ARCS))
////                return (arrow.isSelected( x, y ));
//
//        int nPnts = mcLnGraphSplineEntity.getNKnots();
//        selKnotInd = mcLnGraphSplineEntity.findKnot(x, y);
//
//
////System.out.println("isSelected  "+selKnotInd);
//        if (selKnotInd <= 0 || selKnotInd == (nPnts - 1)) {
//            selKnotInd = -1;
//        }
//        mcLnGraphSplineEntity.setSelectedKnotInd(selKnotInd);
//        return selKnotInd != -1;
//
////            return  mclnGraphSplineViewEntity.isKnobSelected(  x,   y);
//    }
//
//    //   M i s c
//
//    public void setDrawKnotBoxesFlag(boolean status) {
//        mcLnGraphSplineEntity.setDrawKnotBoxesFlag(status);
//    }
//
//    @Override
//    public void setHidden(boolean hidden) {
//        this.hidden = hidden;
//        mcLnGraphSplineEntity.setHidden(hidden);
//    }

    /**
     * @return
     */
    public MclnArc getMclnArc() {
        return mclnArc;
    }


//    /**
//     * @param statementRadius
//     * @param drawKnobAsArrow
//     * @param point01
//     * @param point2
//     * @return
//     */
//    private double[] calculateKnobLocation(double statementRadius, boolean drawKnobAsArrow,
//                                           double point01[], double point2[]) {
//
//        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ? 8 : 0;
//        double outNodeRadius = (outNode instanceof MclnPropertyView) ? 8 : 0;
//
//        double[] alongVec = new double[3];
//        double[] dirVec = new double[3];
//        double[] radVec = new double[3];
//        double[] newVec = new double[3];
//        double[] knobLocation = {0., 0., 0.};
//        double factRadius;
//
//        if (statementRadius != 0) {
////            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) inpNode).RADIUS);
//            VAlgebra.subVec3(alongVec, point01, point2);
//            VAlgebra.normalizeVec3(alongVec, dirVec);
//            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
//            VAlgebra.subVec3(alongVec, alongVec, radVec);
//            VAlgebra.addVec3(newVec, point2, alongVec);
//            if (drawKnobAsArrow) {
//                VAlgebra.LinCom3(knobLocation, 0.25, point2, 0.75, newVec);
//            } else {
//                VAlgebra.LinCom3(knobLocation, 0.50, point2, 0.50, newVec);
//            }
//        } else {
////            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) outNode).RADIUS);
//            VAlgebra.subVec3(alongVec, point2, point01);
//            VAlgebra.normalizeVec3(alongVec, dirVec);
//            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
//            VAlgebra.subVec3(alongVec, alongVec, radVec);
//            VAlgebra.addVec3(newVec, point01, alongVec);
//            if (drawKnobAsArrow) {
//                VAlgebra.LinCom3(knobLocation, 0.75, point01, 0.25, newVec);
//            } else {
//                VAlgebra.LinCom3(knobLocation, 0.50, point01, 0.50, newVec);
//            }
//        }
//        return knobLocation;
//    }

//    void setKnobIndex(int knobIndex) {
//        mcLnGraphSplineEntity.setArcKnobAt(knobIndex);
//    }
//
//    /**
//     * @param scrKnobLocation
//     */
//    void makeThreePointArc(double[] scrKnobLocation) {
//        double[] cSysKnobLocation = parentCSys.screenPointToCSysPoint(null, scrKnobLocation);
////        setKnobIndex(1);
//        mcLnGraphSplineEntity.updateKnotCSysLocation(1, cSysKnobLocation);
//    }
//
//    private void updatePosition() {
//        // after the arc is moved, it is not more straight.
//        straightArc = false;
//        updateEnds();
//        mcLnGraphSplineEntity.calculate();
//    }
//
//    public void updateActiveScrPoint(double x, double y) {
//        mcLnGraphSplineEntity.updateActiveScrPoint(x, y, 0);
//        straightArc = false;
//    }
//
//    public void updateActiveScrPoint(double x, double y, double z) {
//        mcLnGraphSplineEntity.updateActiveScrPoint(x, y, 0);
//    }


    void updateEnds() {
//        double alongVec[] = {0, 0, 0};
//        double dirVec[] = {0, 0, 0};
//        double radVec[] = {0, 0, 0};
//        double scrPnt[] = {0, 0, 0};
//
//        if (inpNode.type == RtsMVNObject.TRANSITION) {
//            boolean inputObj = true;
//            RtsVAlgebra.copyVec3(end1,
//                    choosTransitionEnd(((RtsMCN_Transition) inpNode), inputObj));
//        } else {
//            RtsVAlgebra.initVec3(end1, inpNode.scrBase[0],
//                    inpNode.scrBase[1], 0);
//            nodeScrRad = ((RtsMCN_Node) inpNode).getScrRad();
//        }
//        spline.csysUpdateFirstScrPoint(end1);
//
//        if (outNode == null)
//            return;
//
//        if (outNode.type == RtsMVNObject.TRANSITION) {
//            boolean inputObj = false;
//            RtsVAlgebra.copyVec3(end2,
//                    choosTransitionEnd(((RtsMCN_Transition) outNode), inputObj));
//        } else {
//            RtsVAlgebra.initVec3(end2, outNode.scrBase[0],
//                    outNode.scrBase[1], 0);
//            nodeScrRad = ((RtsMCN_Node) outNode).getScrRad();
//        }
//        spline.csysUpdateLastScrPoint(end2);

    }

    //
    //   D e l e t i o n
    //

    //    public void deletePoint(int ind) {
//        mcLnGraphSplineEntity.deleteCSysKnot(ind);
//    }
//
//    //
//    //  C S y s   r e l a t e d   t r a n s f o r m a t i o n s
//    //
//
//    @Override
//    public void moveEntity(int x, int y) {
////        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
////        System.out.println("move Entity: " + this.getClass().getSimpleName() + " " + cSysPnt[0]);
//    }
//
//    // not Override
//    public void placeEntity(int[] scr0, double scale) {
//    }
//
//    @Override
//    public void doCSysToScreenTransformation(int[] scr0, double scale) {
//        mcLnGraphSplineEntity.doCSysToScreenTransformation(scr0, scale);
//    }
//
//    //
//    //  M o v i n g   t h e   A r c
//    //
//
//    @Override
//    public void translate(double[] translationVector) {
//        mcLnGraphSplineEntity.translate(translationVector);
//    }
//
//    /**
//     * Called when fragment is being moved to paint its last translated phase
//     *
//     * @param g
//     * @param translationVector
//     */
//    @Override
    abstract public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector);
//
////    public void takeFinalLocation(double[] translationVector) {
////        mclnGraphSplineViewEntity.takeFinalLocation(translationVector);
////        List<double[]> knotCSysLocations = mclnGraphSplineViewEntity.getSplineCSysKnots();
////        mclnArc.setCSysKnots(knotCSysLocations);
////    }
//
//    /**
//     * method is used by Move Graph Fragment and Move Entire Model features
//     *
//     * @param translationVector
//     */
//    @Override
//    public void takeFinalLocation(double[] translationVector) {
//        mcLnGraphSplineEntity.takeFinalLocation(translationVector);
//        List<double[]> knotCSysLocations = mcLnGraphSplineEntity.getSplineCSysKnots();
//        mclnArc.setCSysKnots(knotCSysLocations);
//    }
//
//    @Override
//    public void resetToOriginalLocation() {
//
//    }


    //    private int arcKnobInd = -1;
    // Drawing conditions
//    private boolean drawArcKnob = false;
    private boolean drawKnotBoxes = false;
    private boolean drawArrow = true;
//    private int selectedKnotIndex;

//    public int getNKnots() {
//        return 2; //spline.getNKnots();
//    }


//    /**
//     * @param ind
//     */
//    public void setArcKnobAt(int ind) {
//        mclnGraphSplineViewEntity.setArcKnobAt(ind);
////        arcKnobInd = ind;
////        drawArcKnob = ind != -1;
//    }

//    public void setSelectedKnotInd(int ind) {
//        selKnotInd = ind;
//        mcLnGraphSplineEntity.setSelectedKnotInd(ind);
//    }
//
//    public int getSelectedKnotIndex() {
//        return mcLnGraphSplineEntity.getSelectedKnotIndex();
//    }
//
//    public int getNumberOfKnots() {
//        return mcLnGraphSplineEntity.getNKnots();
//    }
//
//
//    //
//    //
//    //
//
//    public   List<double[]> getKnotCSysLocations() {
//        return mcLnGraphSplineEntity.getSplineCSysKnots();
//    }

    public double[] getScrOutlineCenter() {
        return mclnArcArrow.getScrOutlineCenter();
    }


//    private final void setKnotCSysLocations(List<double[]> knotCSysLocations) {
//        mclnGraphSplineViewEntity.setKnotCSysLocations(knotCSysLocations);
//        int index = 0;
//        for (double[] cSysPnt : knotCSysLocations) {
//            double[] scrPnt = parentCSys.cSysPointToScreenPoint(null, cSysPnt);
//            mclnGraphSplineViewEntity.addScrKnot(index, scrPnt);
//            index++;
//        }
//
//    }

    //
    //   D r a w i n g   A r c
    //

//    @Override
//    public void drawPlainEntity(Graphics g) {
//        mcLnGraphSplineEntity.drawPlainEntity(g);
//    }
//
//    @Override
//    public void newDrawEntityAndConnectedEntity(Graphics g, boolean paintExtras) {
//        // draw arc first
//        mcLnGraphSplineEntity.drawPlainEntity(g);
//        if (paintExtras) {
//            paintExtras(g);
//        }
//        // draw nodes on the top
//        drawConnectedEntities(g, paintExtras);
//    }
//
//    @Override
//    public void drawEntityOnlyAtInterimLocation(Graphics g) {
//        mcLnGraphSplineEntity.drawEntityOnlyAtInterimLocation(g);
//    }
//
//    @Override
//    public void drawEntityAndConnectedEntityExtras(Graphics g, boolean paintExtras) {
//        // draw arc first
//        mcLnGraphSplineEntity.drawPlainEntity(g);
//        if (paintExtras) {
//            paintExtras(g);
//        }
//        // draw nodes on the top
//        drawConnectedEntities(g, paintExtras);
//    }

    /**
     * draws connected entity + extras if paintExtras is true
     *
     * @param g
     * @param paintExtras
     */
    void drawConnectedEntities(Graphics g, boolean paintExtras) {
//        if (inpNode != null) {
        inpNode.drawPlainEntity(g);
        if (paintExtras) {
            inpNode.paintExtras(g);
        }
//        }
        if (outNode != null) {
            outNode.drawPlainEntity(g);
            if (paintExtras) {
                outNode.paintExtras(g);
            }
        }
    }

//    //
//    //   O L D   p a i n t i n g
//    //
//
//    @Override
//    public void draw(Graphics g) {
//        if (isHidden()) {
//            return;
//        }
//        g.setColor(getDrawColor());
//        mcLnGraphSplineEntity.draw(g);
//        if (mclnArcArrow != null) {
////            mclnArcArrow.drawKnob(g, Color.GREEN);
//        }
//    }

    @Override
    public void paintExtras(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    public void paintExtrasAtInterimLocation(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    abstract void paintExtrasAtGivenScreenLocation(Graphics g);

//    private void paintExtrasAtGivenScreenLocation(Graphics g) {
//        mcLnGraphSplineEntity.paintExtras(g);
//        if (inpNode != null) {
//            inpNode.drawPlainEntity(g);
//        }
//        if (outNode != null) {
//            outNode.drawPlainEntity(g);
//        }
//    }


    //
    //   p l a c e m e n t
    //

    abstract void updateArcPlacement(int[] scr0, double scale);
//    public void updateArcPlacement(int[] scr0, double scale) {
////        VAlgebra.copyVec3(modelPnt, worldPnt);
////        System.out.println("MclnGraphArcViewEntity: updateArcPlacement");
//        double[] inpNodeCSysPnt = inpNode.getCSysPnt();
//        double[] outNodeCSysPnt = outNode.getCSysPnt();
////            System.out.println("MclnGraphArcViewEntity: updateArcPlacement   " + inpNodeCSysPnt[0]);
////            System.out.println("MclnGraphArcViewEntity: updateArcPlacement   " + outNodeCSysPnt[0]);
//        mcLnGraphSplineEntity.updateFirstKnotLocation(inpNodeCSysPnt);
//        mcLnGraphSplineEntity.updateLastKnotLocation(outNodeCSysPnt);
//
//        mcLnGraphSplineEntity.doCSysToScreenTransformation(scr0, scale);
//    }


    public String getTooltip() {
        if (mclnArc == null) {
            return null;
        }
        return mclnArc.getUID();
    }

    abstract boolean checkIfArrowTipCanBeFound();

//    boolean checkIfArrowTipCanBeFound() {
//        boolean arrowTipFound = mcLnGraphSplineEntity.checkIfArrowTipCanBeFound();
//        return arrowTipFound;
//    }
//
//    /**
//     * @param x
//     * @param y
//     * @param userSelectsArrowLocation
//     * @return
//     */
//    int findArrowTipIndexOnTheSpline(int x, int y, boolean userSelectsArrowLocation) {
//        int arrowTipSplineScrIndex = -1;
//        if (userSelectsArrowLocation) {
//            arrowTipSplineScrIndex = mcLnGraphSplineEntity.findArrowTipIndexOnTheSpline(x, y);
//        } else {
//            arrowTipSplineScrIndex = mcLnGraphSplineEntity.getTwoMouseClosedKnotsAndFindArrowTipLocation(x, y);
//        }
//        return arrowTipSplineScrIndex;
//    }

    //
    //   M e t h o d s   t o   d i s c o n n e c t   t h e   A r c   f r o m   t h e   g r a p h
    //

    /**
     * Called when a node removed from the graph
     *
     * @param nodeToDisconnectFrom
     */
    void disconnectFromNode(MclnGraphViewNode nodeToDisconnectFrom) {
        if (nodeToDisconnectFrom == inpNode) {
            inpNode = null;
        }
        if (nodeToDisconnectFrom == outNode) {
            outNode = null;
        }
    }

    /**
     * Called when this arc is disconnected from its input and output nodes
     */
    void disconnectFromInputAndOutputNodes() {
        if (inpNode != null) {
            inpNode.removeOutputArc(this);
        }
        if (outNode != null) {
            outNode.removeInputArc(this);
        }
        inpNode = null;
        outNode = null;
    }

    abstract public String getOneLineInfoMessage();

//    @Override
//    public String getOneLineInfoMessage() {
//        if (inpNode == null || outNode == null) {
//            return "Arc creation is not complete.";
//        }
//        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());
//
//        MclnPropertyView mclnPropertyView;
//        MclnConditionView mclnConditionView;
//        MclnArc mclnArc = getMclnArc();
//
//        if (mclnArc.isRuntimeInitializationUpdatedFlag()) {
//            oneLineMessageBuilder.append("* ");
//        }
//
//        if (inpNode instanceof MclnPropertyView) {
//            mclnPropertyView = inpNode.toPropertyView();
//            oneLineMessageBuilder.append("Recognizing Arc: [");
//            oneLineMessageBuilder.append(" ID: " + getUID());
//
//            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
//            oneLineMessageBuilder.append(", Expected State = ");
//            oneLineMessageBuilder.append(expectedState);
//            oneLineMessageBuilder.append(" ]");
//
//            MclnState inpState = mclnPropertyView.getCurrentState();
//            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
//            oneLineMessageBuilder.append("  Produced output: ");
//            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
//            oneLineMessageBuilder.append("  =  P(" + expectedState);
//            oneLineMessageBuilder.append(", ");
//            oneLineMessageBuilder.append(inpState.getStateName());
//            oneLineMessageBuilder.append(");");
//        } else {
//            mclnConditionView = inpNode.toConditionView();
//            oneLineMessageBuilder.append("Generating Arc: [");
//            oneLineMessageBuilder.append(" ID: " + getUID());
//
//            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
//            oneLineMessageBuilder.append(", Generated State = ");
//            oneLineMessageBuilder.append(generatedState);
//            oneLineMessageBuilder.append(" ]");
//
//            MclnState inpState = mclnConditionView.getCurrentState();
//            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
//            oneLineMessageBuilder.append("  Produced output: ");
//            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
//            oneLineMessageBuilder.append("  =  G(" + generatedState);
//            oneLineMessageBuilder.append(", ");
//            oneLineMessageBuilder.append(inpState.getStateName());
//            oneLineMessageBuilder.append(");");
//        }
//
//
////        oneLineMessageBuilder.append(", Calculated State = ");
//
//        return oneLineMessageBuilder.toString();
//    }


}

