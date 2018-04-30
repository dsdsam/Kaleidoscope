package dsdsse.graphview;

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
public class MclnSplineArcView extends MclnGraphEntityView<MclnArc> implements Cloneable {

    private static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    private static final Color DEFAULT_ARC_COLOR = Color.LIGHT_GRAY;

    private final MclnArc mclnArc; // arc data model

    private MclnGraphDesignerView parentCSys;
    private MclnState arcMclnState;
    private MclnGraphViewNode inpNode;
    private MclnGraphViewNode outNode;

    private boolean straightArc;
    private int selKnotInd = -1;

    private MclnGraphSplineEntity mclnGraphSplineViewEntity;
    private MclnArcArrow mclnArcArrow;

    private StringBuilder oneLineMessageBuilder = new StringBuilder();

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
    MclnSplineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;
//        this.knobLocation = knobLocation;
//        this.radius = radius;
//        this.initState = initState;
//        this.threshold = threshold;

        this.mclnArc = mclnArc;
        this.inpNode = inpNode;

        // set arc to be a spline
        straightArc = false;
        mclnGraphSplineViewEntity = new MclnGraphSplineEntity(parentCSys, inpNode);
        double[] splineScrStartPoint = inpNode.getScrPnt();

        createFirstScrKnotMakeItActive(splineScrStartPoint);
        addNextScrKnotAndMakeItActive(splineScrStartPoint);

        mclnGraphSplineViewEntity.setSelected(true);

        arcMclnState = mclnArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
        mclnGraphSplineViewEntity.setArcColor(stateColor);
    }

    public void resetArcSpline() {
        mclnGraphSplineViewEntity = new MclnGraphSplineEntity(parentCSys, inpNode);
        double[] splineScrStartPoint = inpNode.getScrPnt();

        createFirstScrKnotMakeItActive(splineScrStartPoint);
        addNextScrKnotAndMakeItActive(splineScrStartPoint);

        mclnGraphSplineViewEntity.setSelected(true);
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
        mclnGraphSplineViewEntity.setArcColor(stateColor);
    }

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    public void setOutputNode(MclnGraphViewNode outNode) {
        this.outNode = outNode;
        mclnGraphSplineViewEntity.setOutputNode(outNode);
    }

    /**
     *
     */
    public void finishArcCreation() {

        // complete MCLN Arc creation
        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
        mclnArc.setOutNode(mclnArcOutputNode);
        List<double[]> knotCSysLocations = this.getKnotCSysLocations();
        mclnArc.setCSysKnots(knotCSysLocations);

        arcMclnState = mclnArc.getArcMclnState();

        // complete spline creation
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
        mclnGraphSplineViewEntity.setArcColor(stateColor);
        mclnGraphSplineViewEntity.calculate();

        mclnArcArrow = mclnGraphSplineViewEntity.editorFinishedSplineCreation(mclnArc);

//        mclnArcArrow = constructArrow(parentCSys, stateColor);

        // storing spline cSys points and arrow cSys points for future xmlization
//        if (mclnArcArrow != null) {
//            List<double[]> splineCSysPoints = mclnGraphSplineViewEntity.getSplineCSysPoints();
//            double[][] arrowCSysPoints = mclnArcArrow.arrowScrPointsToCSysPoints(mclnArcArrow.getViewPoints());
//            mclnArc.setSplineCSysPoints(splineCSysPoints, arrowCSysPoints);
//        }
    }

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
    MclnSplineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode,
                MclnGraphViewNode outNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;
        this.mclnArc = mclnArc;
//        this.UID = mclnArc.getUID();
        this.inpNode = inpNode;
        this.outNode = outNode;

        mclnGraphSplineViewEntity = new MclnGraphSplineEntity(parentCSys, inpNode, outNode);

        arcMclnState = mclnArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_COLOR;
        mclnGraphSplineViewEntity.setArcColor(stateColor);
        mclnGraphSplineViewEntity.setMclnArcStateColor(stateColor);

        List<double[]> knotCSysLocations = mclnArc.getKnotCSysLocations();

        int arrowTipSplineIndex = mclnArc.getArrowTipSplineIndex();
        mclnGraphSplineViewEntity.setArrowTipSplineIndex(arrowTipSplineIndex);

        if (knotCSysLocations.size() == 2) {
//            new Exception("Arrow with two knots created").printStackTrace();
            double[] midPoint = calculateKnobLocation(0, false, knotCSysLocations.get(0), knotCSysLocations.get(1));
            knotCSysLocations.add(1, midPoint);
            setKnobIndex(1);
        } else {
            setKnobIndex(mclnArc.getKnobIndex());
        }
        if (knotCSysLocations.size() > 3) {
//            System.out.println();
        }
        mclnGraphSplineViewEntity.setSplineCSysKnots(knotCSysLocations);

        setDrawKnots(false);
        setSelected(false);
        setSplineThreadSelected(false);
        setAllKnotsSelected(false);
        mclnGraphSplineViewEntity.setNoActiveKnots();

        mclnGraphSplineViewEntity.calculate();

        MclnArcArrow mclnArcArrow = mclnGraphSplineViewEntity.constructRetrievedArrow(mclnArc, parentCSys, stateColor, inpNode, outNode);
        this.mclnArcArrow = mclnArcArrow;
        mclnGraphSplineViewEntity.finishSplineCreation(mclnArc, mclnArcArrow);
    }

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

    @Override
    public MclnSplineArcView clone() {
        MclnSplineArcView mclnArcViewClone = null;
        try {
            mclnArcViewClone = (MclnSplineArcView) super.clone();
            mclnArcViewClone.mclnGraphSplineViewEntity = this.mclnGraphSplineViewEntity.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mclnArcViewClone;
        }
    }

    //
    //  E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    void setHighlightFlatSegments(boolean highlightFlatSegments) {
        mclnGraphSplineViewEntity.setHighlightFlatSegments(highlightFlatSegments);
    }

    void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        mclnGraphSplineViewEntity.setSelectingArrowTipLocation(selectingArrowTipLocation);
    }

    void destroyArcArrowUpOnUndoingArrowTipSelection() {
        mclnGraphSplineViewEntity.destroyArcArrowUpOnUndoingArrowTipSelection();
    }

    public void setHighlightArcKnotsForArrowTipSelection(boolean highlightArcKnotsForArrowTipSelection) {
        this.mclnGraphSplineViewEntity.setHighlightArcKnotsForArrowTipSelection(highlightArcKnotsForArrowTipSelection);
    }

    //    K n o t s   a n d   s p l i n e   c r e a t i o n

    private void createFirstScrKnotMakeItActive(double[] scrPnt) {
        mclnGraphSplineViewEntity.createFirstScrKnotMakeItActive(scrPnt);
    }

    void addNextScrKnotAndMakeItActive(double x, double y) {
        double[] scrPnt = {x, y, 0};
        addNextScrKnotAndMakeItActive(scrPnt);
    }

    private void addNextScrKnotAndMakeItActive(double[] scrPnt) {
        mclnGraphSplineViewEntity.addNextScrKnotAndMakeItActive(scrPnt);
    }

    void addNextScrKnotAndMakePreviousKnotActive(double x, double y) {
        double[] scrPnt = {x, y, 0};
        mclnGraphSplineViewEntity.addNextScrKnotAndMakePreviousKnotActive(scrPnt);
    }

    @Override
    public void moveEntityActivePointTo(int x, int y) {
        double[] activePointInCSysCoordinates = parentCSys.screenPointToCSysPoint(x, y);
        moveEntityActivePointTo(activePointInCSysCoordinates);
    }

    @Override
    public void moveEntityActivePointTo(double[] activePointInCSysCoordinates) {
//        System.out.println("MclnGraphArcViewEntity (mclnGraph Spline ViewEntity): updateArcPlacement   " +
//                activePointInCSysCoordinates[0]);
        mclnGraphSplineViewEntity.moveEntityActivePointTo(activePointInCSysCoordinates);
    }

    public int deleteLastPoint() {
        int nPnts = mclnGraphSplineViewEntity.deleteLastCSysKnot();
        return nPnts;
    }

    public void removeOutputNode() {
        setOutputNode(null);
        updatePosition();
//        mclnGraphSplineViewEntity.setColor(Color.red);
    }

    //
    //   D r a w i n g   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    public void setDrawKnots(boolean status) {
        mclnGraphSplineViewEntity.setDrawKnots(status);
    }

    //   W a t e r m a r k

    @Override
    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
        mclnGraphSplineViewEntity.setWatermarked(watermarked);
    }

    public void setArrowWatermarked(boolean watermarked) {
        mclnGraphSplineViewEntity.setArrowWatermarked(watermarked);
    }

    //   P r e - S e l e c t i o n   a n d   S e l e c t i o n

    @Override
    public void setPreSelected(boolean preSelected) {
        mclnGraphSplineViewEntity.setSelected(preSelected);
    }

    @Override
    public void setSelected(boolean status) {
        mclnGraphSplineViewEntity.setSelected(status);
    }

    public void setArrowSelected(boolean watermarked) {
        mclnGraphSplineViewEntity.setArrowSelected(watermarked);
    }

    public boolean isSelected() {
        return mclnGraphSplineViewEntity.isSelected();
    }

    public void setAllKnotsSelected(boolean status) {
        mclnGraphSplineViewEntity.setAllKnotsSelected(status);
    }

    public void setKnobSelected(boolean status) {
        mclnGraphSplineViewEntity.setKnobSelected(status);
    }

    public void setSplineThreadSelected(boolean status) {
        mclnGraphSplineViewEntity.setThreadSelected(status);
    }

    //   H i g h l i g h t i n g

    @Override
    public void setHighlighted(boolean highlighted) {
        mclnGraphSplineViewEntity.setHighlighted(highlighted);
    }

    public void setArrowHighlighted(boolean highlighted1) {
        mclnGraphSplineViewEntity.setArrowHighlighted(highlighted1);
    }

    @Override
    public void setHighlightColor(Color highlightedColor) {
        mclnGraphSplineViewEntity.setHighlightColor(highlightedColor);
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        return mclnGraphSplineViewEntity.isMouseHover(x, y);
    }

    public boolean isKnotPicked(int x, int y) {

//            if (MCLNMedia.drawArcKnobAsArrow  &&
//                    !(parentCSys.currentOperation == parentCSys.MOVE_ELEMENT  ||
//                            parentCSys.currentOperation == parentCSys.DELETE_ELEMENT ||
//                            parentCSys.currentOperation == parentCSys.CREATE_ARCS))
//                return (arrow.isSelected( x, y ));

        int nPnts = mclnGraphSplineViewEntity.getNKnots();
        selKnotInd = mclnGraphSplineViewEntity.findKnot(x, y);


//System.out.println("isSelected  "+selKnotInd);
        if (selKnotInd <= 0 || selKnotInd == (nPnts - 1)) {
            selKnotInd = -1;
        }
        mclnGraphSplineViewEntity.setSelectedKnotInd(selKnotInd);
        return selKnotInd != -1;

//            return  mclnGraphSplineViewEntity.isKnobSelected(  x,   y);
    }

    //   M i s c

    public void setDrawKnotBoxesFlag(boolean status) {
        mclnGraphSplineViewEntity.setDrawKnotBoxesFlag(status);
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        mclnGraphSplineViewEntity.setHidden(hidden);
    }

    /**
     * @return
     */
    public MclnArc getMclnArc() {
        return mclnArc;
    }


    /**
     * @param statementRadius
     * @param drawKnobAsArrow
     * @param point01
     * @param point2
     * @return
     */
    private double[] calculateKnobLocation(double statementRadius, boolean drawKnobAsArrow,
                                           double point01[], double point2[]) {

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ? 8 : 0;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ? 8 : 0;

        double[] alongVec = new double[3];
        double[] dirVec = new double[3];
        double[] radVec = new double[3];
        double[] newVec = new double[3];
        double[] knobLocation = {0., 0., 0.};
        double factRadius;

        if (statementRadius != 0) {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) inpNode).RADIUS);
            VAlgebra.subVec3(alongVec, point01, point2);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point2, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.25, point2, 0.75, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point2, 0.50, newVec);
            }
        } else {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) outNode).RADIUS);
            VAlgebra.subVec3(alongVec, point2, point01);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point01, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.75, point01, 0.25, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point01, 0.50, newVec);
            }
        }
        return knobLocation;
    }

    void setKnobIndex(int knobIndex) {
        mclnGraphSplineViewEntity.setArcKnobAt(knobIndex);
    }

    /**
     * @param scrKnobLocation
     */
    void makeThreePointArc(double[] scrKnobLocation) {
        double[] cSysKnobLocation = parentCSys.screenPointToCSysPoint(null, scrKnobLocation);
//        setKnobIndex(1);
        mclnGraphSplineViewEntity.updateKnotCSysLocation(1, cSysKnobLocation);
    }

    private void updatePosition() {
        // after the arc is moved, it is not more straight.
        straightArc = false;
        updateEnds();
        mclnGraphSplineViewEntity.calculate();
    }

    public void updateActiveScrPoint(double x, double y) {
        mclnGraphSplineViewEntity.updateActiveScrPoint(x, y, 0);
        straightArc = false;
    }

    public void updateActiveScrPoint(double x, double y, double z) {
        mclnGraphSplineViewEntity.updateActiveScrPoint(x, y, 0);
    }


    private void updateEnds() {
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

    public void deletePoint(int ind) {
        mclnGraphSplineViewEntity.deleteCSysKnot(ind);
    }

    //
    //  C S y s   r e l a t e d   t r a n s f o r m a t i o n s
    //

    @Override
    public void moveEntity(int x, int y) {
//        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
//        System.out.println("move Entity: " + this.getClass().getSimpleName() + " " + cSysPnt[0]);
    }

    // not Override
    public void placeEntity(int[] scr0, double scale) {
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        mclnGraphSplineViewEntity.doCSysToScreenTransformation(scr0, scale);
    }

    //
    //  M o v i n g   t h e   A r c
    //

    @Override
    public void translate(double[] translationVector) {
        mclnGraphSplineViewEntity.translate(translationVector);
    }

    /**
     * Called when fragment is being moved to paint its last translated phase
     *
     * @param g
     * @param translationVector
     */
    @Override
    public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector) {
        mclnGraphSplineViewEntity.translateAndPaintSplineAtInterimLocation(g, translationVector);
    }

//    public void takeFinalLocation(double[] translationVector) {
//        mclnGraphSplineViewEntity.takeFinalLocation(translationVector);
//        List<double[]> knotCSysLocations = mclnGraphSplineViewEntity.getSplineCSysKnots();
//        mclnArc.setCSysKnots(knotCSysLocations);
//    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationVector
     */
    @Override
    public void takeFinalLocation(double[] translationVector) {
        mclnGraphSplineViewEntity.takeFinalLocation(translationVector);
        List<double[]> knotCSysLocations = mclnGraphSplineViewEntity.getSplineCSysKnots();
        mclnArc.setCSysKnots(knotCSysLocations);
    }

    @Override
    public void resetToOriginalLocation() {

    }


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

    public void setSelectedKnotInd(int ind) {
        selKnotInd = ind;
        mclnGraphSplineViewEntity.setSelectedKnotInd(ind);
    }

    public int getSelectedKnotIndex() {
        return mclnGraphSplineViewEntity.getSelectedKnotIndex();
    }

    public int getNumberOfKnots() {
        return mclnGraphSplineViewEntity.getNKnots();
    }


    //
    //
    //

    public final List<double[]> getKnotCSysLocations() {
        return mclnGraphSplineViewEntity.getSplineCSysKnots();
    }

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

    @Override
    public void drawPlainEntity(Graphics g) {
        mclnGraphSplineViewEntity.drawPlainEntity(g);
    }

    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean paintExtras) {
        // draw arc first
        mclnGraphSplineViewEntity.drawPlainEntity(g);
        if (paintExtras) {
            paintExtras(g);
        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    @Override
    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        mclnGraphSplineViewEntity.drawEntityOnlyAtInterimLocation(g);
    }

    @Override
    public void drawEntityAndConnectedEntityExtras(Graphics g, boolean paintExtras) {
        // draw arc first
        mclnGraphSplineViewEntity.drawPlainEntity(g);
        if (paintExtras) {
            paintExtras(g);
        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    /**
     * draws connected entity + extras if paintExtras is true
     *
     * @param g
     * @param paintExtras
     */
    private void drawConnectedEntities(Graphics g, boolean paintExtras) {
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

    //
    //   O L D   p a i n t i n g
    //

    @Override
    public void draw(Graphics g) {
        if (isHidden()) {
            return;
        }
        g.setColor(getDrawColor());
        mclnGraphSplineViewEntity.draw(g);
        if (mclnArcArrow != null) {
//            mclnArcArrow.drawKnob(g, Color.GREEN);
        }
    }

    @Override
    public void paintExtras(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    public void paintExtrasAtInterimLocation(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    private void paintExtrasAtGivenScreenLocation(Graphics g) {
        mclnGraphSplineViewEntity.paintExtras(g);
        if (inpNode != null) {
            inpNode.drawPlainEntity(g);
        }
        if (outNode != null) {
            outNode.drawPlainEntity(g);
        }
    }


//    public boolean isKnobSelected(int x, int y) {
//        if (drawKnobAsArrow) {
////  System.out.println("Arrow selection "+arcKnobInd);
//            return false;//(arrow.isSelected( me ));
//        } else if (arcKnobInd != -1) {
//            int nPnts = getNKnots();
//            double knobPnt[] = {0., 0., 0.};
//            double csysKnobPnt[] = {0., 0., 0.};
//
//            knobPnt = copyKnotFromKnotArr(arcKnobInd);
//            int selKnotInd = findKnot(x, y);
//            //System.out.println("isSelected  "+selKnotInd);
//            return selKnotInd == arcKnobInd;
//        }
//        return (false);
//    }

    //
    //   p l a c e m e n t
    //

    public void updateArcPlacement(int[] scr0, double scale) {
//        VAlgebra.copyVec3(modelPnt, worldPnt);
//        System.out.println("MclnGraphArcViewEntity: updateArcPlacement");
        double[] inpNodeCSysPnt = inpNode.getCSysPnt();
        double[] outNodeCSysPnt = outNode.getCSysPnt();
//            System.out.println("MclnGraphArcViewEntity: updateArcPlacement   " + inpNodeCSysPnt[0]);
//            System.out.println("MclnGraphArcViewEntity: updateArcPlacement   " + outNodeCSysPnt[0]);
        mclnGraphSplineViewEntity.updateFirstKnotLocation(inpNodeCSysPnt);
        mclnGraphSplineViewEntity.updateLastKnotLocation(outNodeCSysPnt);

        mclnGraphSplineViewEntity.doCSysToScreenTransformation(scr0, scale);
    }


//    public void prepareToXMLize() {
//        List<double[]> splineCSysPoints = mclnGraphSplineViewEntity.getSplineCSysPoints();
//        double[][] arrowScrPoints = mclnArcKnob.getViewPoints();
//        List<double[]> arrowCSysPoints = new ArrayList<>();
//        for (int i = 0; i < arrowScrPoints.length; i++) {
//            double[] currentScrPoint = arrowScrPoints[i];
//            arrowCSysPoints.add(parentCSys.screenPointToCSysPoint(currentScrPoint));
//        }
//        mclnArc.setSplineCSysPoints(splineCSysPoints, arrowCSysPoints);
//
//    }

    public String getTooltip() {
        if (mclnArc == null) {
            return null;
        }
        return mclnArc.getUID();
    }


    boolean checkIfArrowTipCanBeFound() {
        boolean arrowTipFound = mclnGraphSplineViewEntity.checkIfArrowTipCanBeFound();
        return arrowTipFound;
    }

    /**
     * @param x
     * @param y
     * @param userSelectsArrowLocation
     * @return
     */
    int findArrowTipIndexOnTheSpline(int x, int y, boolean userSelectsArrowLocation) {
        int arrowTipSplineScrIndex = -1;
        if (userSelectsArrowLocation) {
            arrowTipSplineScrIndex = mclnGraphSplineViewEntity.findArrowTipIndexOnTheSpline(x, y);
        } else {
            arrowTipSplineScrIndex = mclnGraphSplineViewEntity.getTwoMouseClosedKnotsAndFindArrowTipLocation(x, y);
        }
        return arrowTipSplineScrIndex;
    }

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

    @Override
    public String getOneLineInfoMessage() {
        if (inpNode == null || outNode == null) {
            return "Arc creation is not complete.";
        }
        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());

        MclnPropertyView mclnPropertyView;
        MclnConditionView mclnConditionView;
        MclnArc mclnArc = getMclnArc();

        if (mclnArc.isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }

        if (inpNode instanceof MclnPropertyView) {
            mclnPropertyView = inpNode.toPropertyView();
            oneLineMessageBuilder.append("Recognizing Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Expected State = ");
            oneLineMessageBuilder.append(expectedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mclnPropertyView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  P(" + expectedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        } else {
            mclnConditionView = inpNode.toConditionView();
            oneLineMessageBuilder.append("Generating Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Generated State = ");
            oneLineMessageBuilder.append(generatedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mclnConditionView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  G(" + generatedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        }


//        oneLineMessageBuilder.append(", Calculated State = ");

        return oneLineMessageBuilder.toString();
    }


}

