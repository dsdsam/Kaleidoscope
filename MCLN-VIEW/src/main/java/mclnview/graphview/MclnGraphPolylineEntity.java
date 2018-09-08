package mclnview.graphview;

import adf.csys.view.CSysRoundedPolylineEntity;
import adf.csys.view.CSysView;
import mcln.model.MclnArc;
import mcln.model.MclnPolylineArc;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.List;

public final class MclnGraphPolylineEntity extends CSysRoundedPolylineEntity implements Cloneable {

    private static final double GAP_BETWEEN_NODE_AND_ARROW = 8;

    private static final Color POLY_LINE_CREATION_COLOR = new Color(0xFF00FF);
    private static final Color POLY_LINE_DRAWING_COLOR = Color.GRAY;

    //
    //   I n s t a n c e
    //

    private final MclnArc mclnArc;

    /**
     * Both, the flag and the method below are added for debugging
     *
     * @param thisIsClone
     */
    private boolean thisIsClone;
    private final MclnGraphView mclnGraphView;

    public void setThisIsClone(boolean thisIsClone) {
        this.thisIsClone = thisIsClone;
    }

    private MclnGraphNodeView inpNode;
    private MclnGraphNodeView outNode;
    private Color arcMclnStateColor;

    // arrow
    private boolean selectingArrowTipLocation;
    private double[] arrowTipCSysLocation = {0, 0, 0};
    private int[] arrowTipScrLocation = {0, 0, 0};
    private int arrowSegmentIndex = -1;
    private MclnArcArrow mclnArcArrow;


    //  ***********************************************************************************************
    //
    //   T h e   M e t h o d s   t o   C r e a t   A r c   v i a   G r a h i c a l   I n t e r f a c e
    //
    //  ***********************************************************************************************

    public MclnGraphPolylineEntity(MclnGraphView mclnGraphView, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(mclnGraphView, POLY_LINE_CREATION_COLOR);
        this.mclnGraphView = mclnGraphView;
        this.mclnArc = mclnArc;
        this.inpNode = inpNode;
    }

    public void polylineCreationCompleted(MclnGraphNodeView outNode) {
        this.outNode = outNode;
        setDrawColor(POLY_LINE_DRAWING_COLOR);
        super.polylineCreationCompleted();
    }


    //  *********************************************************************************************************
    //
    //   T h e   M e t h o d s   t o   I n s t a n t i a t e   A r c   W i t h   P r o g r a m m a t i c a l l y
    //                              P r o v i d e d    A t t r i b u t e s
    //
    //  *********************************************************************************************************

    /**
     * Called when McLN model retrieved
     *
     * @param mclnGraphView
     * @param mclnArc
     * @param knotCSysLocations
     * @param arrowSegmentIndex
     * @param arrowTipCSysLocation
     * @param inpNode
     * @param outNode
     */
    public MclnGraphPolylineEntity(MclnGraphView mclnGraphView, MclnArc mclnArc, List<double[]> knotCSysLocations,
                                   int arrowSegmentIndex, double[] arrowTipCSysLocation, MclnGraphNodeView inpNode, MclnGraphNodeView outNode) {
        super(mclnGraphView, POLY_LINE_DRAWING_COLOR);
        this.mclnGraphView = mclnGraphView;
        this.mclnArc = mclnArc;
        this.arrowSegmentIndex = arrowSegmentIndex;
        this.arrowTipCSysLocation = arrowTipCSysLocation;

        this.inpNode = inpNode;
        this.outNode = outNode;
    }

    /**
     * Is used to recreate Joints upon retrieval
     *
     * @param knotCSysLocations
     */
    void createJointPointsAndArrow(List<double[]> knotCSysLocations) {
        super.recreateJointPoints(knotCSysLocations);
        Color arcMclnStateColor = getArcMclnStateColor();
        this.arrowTipScrLocation = parentCSys.doubleVec3ToInt(parentCSys.cSysPointToScreenPoint(arrowTipCSysLocation));
        positionArrowOnTheArcThread(arrowSegmentIndex, arrowTipScrLocation, arcMclnStateColor);
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
    }


    //  *********************************************************************************************************
    //
    //          I n i t i a l i z i n g   A r c   b y   I n i t i a l i z a t i o n   A s s i s t a n t
    //
    //  *********************************************************************************************************

    public Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation) {
        return mclnArcArrow.buildTriangleArrow(arrowLength, arrowWidth, arrowTipScrLocation);
    }

    public void setArcMclnStateColor(Color arcMclnStateColor) {
        this.arcMclnStateColor = arcMclnStateColor;
    }

    public Color getArcMclnStateColor() {
        return arcMclnStateColor;
    }

    public void updateStateColorUponInitialization(MclnState arcMclnState) {
        mclnArcArrow.updateStateColorUponInitialization(arcMclnState.getStateID());
    }

    public double[] getScrOutlineCenter() {
        return mclnArcArrow.getScrOutlineCenter();
    }

    @Override
    public void setHighlightLongestSegment(boolean highlightLongestSegment) {
        super.setHighlightLongestSegment(highlightLongestSegment);
    }


    //
    //  K n o t s   c r e a t i o n
    //

    public void createFirstCSysKnot(double[] cSysPnt) {
        super.createFirstScrKnot(cSysPnt);
    }

    public void addNextCSysKnot(double[] cSysPnt) {
        super.addPoint(cSysPnt);

    }

    public boolean updateLastPoint(double[] jointPoint) {
        return super.updateLastPoint(jointPoint);
    }

    void unselectSelectedOutputNode() {
        outNode = null;

        // arrow
        setSelectingArrowTipLocation(false);
        arrowTipCSysLocation = new double[]{0, 0, 0};
        arrowTipScrLocation = new int[]{0, 0, 0};
        arrowSegmentIndex = -1;
        mclnArcArrow = null;
    }

    public int getNKnots() {
        return super.getJointsSize();
    }

    //
    //   Creating Arc Arrow and placing it on the polyline
    //

    void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        this.selectingArrowTipLocation = selectingArrowTipLocation;
        super.setHighlightLongestSegment(selectingArrowTipLocation);
    }

    boolean checkIfArrowTipCanBeFound() {
//        List<double[]> splineScrKnots = getScreenSegmentPoints();
//
//        int firstKnotIndex = 0;
//        int outputArcKnotIndex = splineScrKnots.size() - 1;
//        int[] highlightedKnotIndexes = new int[]{firstKnotIndex, outputArcKnotIndex};
//
//        boolean arrowTipIndexFound  = checkIfArrowTipLocationCanBeFound(  highlightedKnotIndexes);
//        return arrowTipIndexFound;
        return false;
    }

    //
    //   U s e r   C r e a t e s   A r c   A r r o w
    //

    /**
     * Called when user selects arrow tip
     * Search range is from Arc Input Node to Arc Output Node
     *
     * @param x
     * @param y
     * @return
     */
    boolean findArrowTipIndexOnThePolyline(int x, int y) {

        List<int[][]> screenSegmentPoints = getScreenSegmentPoints();
        arrowSegmentIndex = findArrowTipLocationUnderMouse(x, y, screenSegmentPoints);
        if (arrowSegmentIndex < 0) {
            return false;
        }
        Color arcMclnStateColor = getArcMclnStateColor();
        positionArrowOnTheArcThread(arrowSegmentIndex, arrowTipScrLocation, arcMclnStateColor);
        return true;
    }

    private int findArrowTipLocationUnderMouse(int x, int y, List<int[][]> screenSegmentPoints) {

        arrowTipCSysLocation = null;
        arrowTipScrLocation = null;

        double[] currentMouseVec = {x, y, 0.};

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        int arrowSegmentIndex = -1;
        double minDistance = Double.MAX_VALUE;
        double mouseToPointDist;
        double[] minLocationInsideSegment = null;
        int nOfSegments = screenSegmentPoints.size();
        int lastSegmentIndex = nOfSegments - 1;
        for (int i = 0; i < nOfSegments; i++) {
            int[][] segmentPoints = screenSegmentPoints.get(i);
            double[] segmentStartPoint = {segmentPoints[0][0], segmentPoints[0][1], 0.};
            double[] segmentEndPoint = {segmentPoints[1][0], segmentPoints[1][1], 0.};

            double[] segmentVector;
            double[] segmentNormalizedVector;
            double[] nodeRadiusVector;

            segmentVector = VAlgebra.subVec3(segmentEndPoint, segmentStartPoint);
            segmentNormalizedVector = VAlgebra.normalizeVec3(segmentVector);

            // both "if"s will be invoked in case when arc is straight, that is it
            // has only one segment and hence i equal 0 is its first and last index
            if (i == 0) {
                nodeRadiusVector = VAlgebra.scaleVec3((inpNodeRadius + GAP_BETWEEN_NODE_AND_ARROW + MclnArcArrow.LARGE_ARROW_LENGTH), segmentNormalizedVector);
                segmentStartPoint = VAlgebra.addVec3(segmentStartPoint, nodeRadiusVector);
            }
            if (i == lastSegmentIndex) {
                nodeRadiusVector = VAlgebra.scaleVec3(outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW, segmentNormalizedVector);
                segmentEndPoint = VAlgebra.subVec3(segmentEndPoint, nodeRadiusVector);
            }

            boolean searchingInsideSegment = true;
            double step = 0.001;
            double slidingPercent = 0.;
            while (searchingInsideSegment) {
                double[] locationInsideSegment = VAlgebra.linCom3((1.0 - slidingPercent),
                        segmentStartPoint, slidingPercent, segmentEndPoint);
                mouseToPointDist = VAlgebra.distVec3(currentMouseVec, locationInsideSegment);
                if (mouseToPointDist < 200000 && mouseToPointDist < minDistance) {
                    minDistance = mouseToPointDist;
                    minLocationInsideSegment = locationInsideSegment;
                    arrowSegmentIndex = i;
                }
                slidingPercent += step;
                searchingInsideSegment = slidingPercent <= 1.0;
            }
        }

        if (minLocationInsideSegment == null) {
            arrowTipCSysLocation = null;
            arrowTipScrLocation = null;
            return -1;
        }
        arrowTipCSysLocation = parentCSys.screenPointToCSysPoint(minLocationInsideSegment);
        arrowTipScrLocation = parentCSys.doubleVec3ToInt(minLocationInsideSegment);
        return arrowSegmentIndex;
    }

    /**
     * @return
     */
    boolean findArrowTipLocationAtTheMiddleOfTheSegment() {
        if (arrowSegmentIndex < 0) {
            return false;
        }
        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        List<int[][]> screenSegmentPoints = getScreenSegmentPoints();
        int nOfSegments = screenSegmentPoints.size();
        int lastSegmentIndex = nOfSegments - 1;
        int[][] arrowSegmentPoints = screenSegmentPoints.get(arrowSegmentIndex);
        double[] segmentStartPoint = {arrowSegmentPoints[0][0], arrowSegmentPoints[0][1], 0.};
        double[] segmentEndPoint = {arrowSegmentPoints[1][0], arrowSegmentPoints[1][1], 0.};
        double[] segmentVector = VAlgebra.subVec3(segmentEndPoint, segmentStartPoint);
        double[] segmentNormalizedVector = VAlgebra.normalizeVec3(segmentVector);

        double[] nodeRadiusVector;
        if (arrowSegmentIndex == 0) {
            nodeRadiusVector = VAlgebra.scaleVec3((inpNodeRadius + GAP_BETWEEN_NODE_AND_ARROW + MclnArcArrow.LARGE_ARROW_LENGTH), segmentNormalizedVector);
            segmentStartPoint = VAlgebra.addVec3(segmentStartPoint, nodeRadiusVector);

        } else if (arrowSegmentIndex == lastSegmentIndex) {
            nodeRadiusVector = VAlgebra.scaleVec3(outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW, segmentNormalizedVector);
            segmentEndPoint = VAlgebra.subVec3(segmentEndPoint, nodeRadiusVector);
        }

        double[] locationInsideSegment = VAlgebra.linCom3(0.5, segmentStartPoint, 0.5, segmentEndPoint);
        double[] arrowLengthVector = VAlgebra.scaleVec3(MclnArcArrow.LARGE_ARROW_LENGTH / 2, segmentNormalizedVector);
        double[] arrowTipInTheMidOfSegmentLocation = VAlgebra.addVec3(locationInsideSegment, arrowLengthVector);

        arrowTipScrLocation = parentCSys.doubleVec3ToInt(arrowTipInTheMidOfSegmentLocation);
        arrowTipCSysLocation = parentCSys.screenPointToCSysPoint(arrowTipInTheMidOfSegmentLocation);

        Color arcMclnStateColor = getArcMclnStateColor();
        positionArrowOnTheArcThread(arrowSegmentIndex, arrowTipScrLocation, arcMclnStateColor);

        return true;
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
        if (mclnArcArrow != null) {
            mclnArcArrow.doCSysToScreenTransformation(scr0, scale);
        }
    }

    public void setMouseHover(boolean mouseHover) {
        if (mclnArcArrow == null) {
            return;
        }
        super.setMouseHover(mouseHover);
        mclnArcArrow.setMouseHover(mouseHover);
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        if (mclnArcArrow == null) {
            return false;
        }
        return mclnArcArrow.isMouseHover(x, y);
    }

    public void setDrawKnots(boolean status) {
        super.setDrawKnots(status);
    }

    void setArrowSelected(boolean arrowSelected) {
        mclnArcArrow.setSelected(arrowSelected);
    }

    //  *********************************************************************************************************
    //
    //                                   M o v i n g   A r c   K n o t s   O r
    //                 T h e   A r c   A s   a   P a r t   o f   a   M o d e l   o r   F r a g m e n t
    //
    //  *********************************************************************************************************


    protected void backupJointPoints() {
        super.backupJointPoints();
    }

    //
    //   M o v i n g   k n o t s
    //

    private RoundedJoint selectedRoundedJoint;

    boolean selectAKnotUnderMouse(Point mousePoint) {
        RoundedJoint roundedJoint = super.getJointUnderMouse(mousePoint);
        if (roundedJoint == null) {
            /*
               If user clicks not on knob - un-select selected knob if selected
             */
            if (selectedRoundedJoint != null) {
                selectedRoundedJoint.setSelected(false);
                selectedRoundedJoint = null;
            } else if (isSelected()) {
//                 setSelected(false);
            }
            return false;
        }

        // clicking the same knot again is OK
        // it will merely reselected

        if (selectedRoundedJoint != null) {
            selectedRoundedJoint.setSelected(false);
        }
        selectedRoundedJoint = roundedJoint;
        selectedRoundedJoint.setSelected(true);
        return true;
    }

    void updatePolylineKnotDragged(Point mousePoint) {
        if (selectedRoundedJoint != null) {
            super.updatePolylineOnJointDragged(selectedRoundedJoint, mousePoint);
            findArrowTipLocationAtTheMiddleOfTheSegment();
        }
    }

    /**
     * called to restore Arrow upon modification cancelled
     *
     * @param mclnPolylineArc
     */
    void restoreBackupJointPointsUponModificationCancelled(MclnPolylineArc mclnPolylineArc) {
        super.restoreJointPoints();
        findArrowTipLocationAtTheMiddleOfTheSegment();
    }

    //
    //   T r a n s l a t i n g   A r c
    //

    @Override
    public MclnGraphPolylineEntity clone() {
        MclnGraphPolylineEntity mclnGraphPolylineEntityClone = null;
        try {
            mclnGraphPolylineEntityClone = (MclnGraphPolylineEntity) super.clone();
            mclnGraphPolylineEntityClone.setThisIsClone(true);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mclnGraphPolylineEntityClone;
        }
    }

    /**
     * @param translationVector
     */
    void translate(double[] translationVector) {
        if (mclnArcArrow == null) {
            System.out.println("mclnArcArrow == null  " + inpNode.getUID());
        } else {
            mclnArcArrow.translate(translationVector);
        }
    }

    /**
     * method is called by Move Graph Fragment feature
     *
     * @param g
     * @param scrTranslationVector
     */
    void translateAndPaintSplineAtInterimLocation(Graphics g, double[] scrTranslationVector) {
        //      System.out.println("9999999999999999999999999999999");
        double[] cSysTranslationVector = parentCSys.scaleScrPntToCSysPntRightly(scrTranslationVector);
        super.doTranslationStep(cSysTranslationVector);

//        if (mclnArcArrow == null) {
//            System.out.println("mclnArcArrow == null  " + inpNode.getUID());
//        } else {
//            mclnArcArrow.takeFinalLocation(scrTranslationVector);
//        }
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
        super.draw(g);
    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationScrVector
     */
    double[] takeFinalLocation(double[] translationScrVector) {
        double[] inpCSysPnt = inpNode.getCSysPnt();
        double[] outCSysPnt = outNode.getCSysPnt();
        double[] cSysTranslationVector = parentCSys.scaleScrPntToCSysPntRightly(translationScrVector);
        super.takeFinalLocation(cSysTranslationVector, inpCSysPnt, outCSysPnt);

        if (mclnArcArrow == null) {
            System.out.println("mclnArcArrow == null  " + inpNode.getUID());
        } else {
            mclnArcArrow.takeFinalLocation(translationScrVector);
            arrowTipCSysLocation = VAlgebra.translateToNewVec(arrowTipCSysLocation, cSysTranslationVector);
        }
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
        return translationScrVector;
    }

    /**
     * @param cSysPnt
     */
    public void updateFirstKnotLocation(double[] cSysPnt) {
        super.updateFirstPolylineJoint(cSysPnt);
    }

    public void updateLastKnotLocation(double[] cSysPnt) {
        super.updateLastPolylineJoint(cSysPnt);
    }
    //  *********************************************************************************************************
    //
    //                                    D r a w i n g    P o l y l i n e
    //
    //  *********************************************************************************************************

    public Color getCurrentColor() {
        if (isSelected()) {
            return getSelectedColor();
        }
        return getDrawColor();
    }

    /**
     * @param g
     */
    public void drawPlainEntity(Graphics g) {
        super.drawPlainEntity(g);
        if (arrowTipScrLocation != null && mclnArcArrow != null) {
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }
    }

    public void paintExtras(Graphics g) {
        if (!(isWatermarked() || isHighlightedOrSelected() || isMouseHover())) {
            return;
        }
        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isMouseHover()) {
            Stroke currentStroke = g2D.getStroke();
            g2D.setStroke(new BasicStroke(1));
            super.drawPlainEntity(g);
            g2D.setStroke(currentStroke);
        }
        if (mclnArcArrow != null) {
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    @Override
    protected Color getThreadColor() {
        Color color = DEFAULT_DRAWING_COLOR;

        if (isUnderConstruction()) {
            color = getCreationColor();
            return color;
        }

        if (isWatermarked()) {
            color = getWatermarkColor();
            return color;
        }

        if (isHighlighted()) {
            color = getHighlightColor();
        } else if (selected && threadSelected) {
            color = getSelectedColor();
        } else if (selected) {
            color = getSelectedColor();
        }
        return color;
    }

    //  *********************************************************************************************************
    //
    //                          A r c   A r r o w   P o s i t i o n i n g   M e t h o d s
    //
    //  *********************************************************************************************************

    private void positionArrowOnTheArcThread(int arrowSegmentIndex, int[] arrowTipScrLocation, Color stateColor) {

        if (arrowSegmentIndex < 0) {
            return;
        }

        int[][] arrowSegmentPoints = getSegmentPoints(arrowSegmentIndex);
        double[] segmentStartPoint = {arrowSegmentPoints[0][0], arrowSegmentPoints[0][1], 0.};
        double[] segmentEndPoint = {arrowSegmentPoints[1][0], arrowSegmentPoints[1][1], 0.};

        double[] arrowSegmentDirection = VAlgebra.subVec3(segmentStartPoint, segmentEndPoint);
        double[] normalizedScrDirectionVector = VAlgebra.normalizeVec3(arrowSegmentDirection);
        double[] arrowScrTipLocation = {arrowTipScrLocation[0], arrowTipScrLocation[1], 0.};

        if (mclnArcArrow == null) {
            mclnArcArrow = MclnArcArrow.createMclnArcArrow(mclnGraphView, normalizedScrDirectionVector,
                    arrowScrTipLocation, stateColor);
        } else {
            mclnArcArrow.repositionArrowLocation(normalizedScrDirectionVector, arrowScrTipLocation);
        }
    }

    /**
     * The method is to copy all Arc's attributes to persistent data class - Mcln Arc
     * <p>
     * Called when Arc Creator finished
     * creating Arc
     * moving Arc elements
     *
     * @param mclnPolylineArc
     */
    void storeArcPersistentAttributesIntoMclnArc(MclnPolylineArc mclnPolylineArc) {

        mclnPolylineArc.setArrowSegmentIndex(arrowSegmentIndex);
        mclnPolylineArc.setArrowTipCSysLocation(arrowTipCSysLocation);
        mclnPolylineArc.setArrowTipScrLocation(arrowTipScrLocation);

        // storing polyline Joint cSys points for future storing as xml
        List<double[]> knotCSysLocations = getJointPoints();
        mclnPolylineArc.setCSysKnots(knotCSysLocations);

        // storing arrow cSys points for future storing as xml
        if (mclnArcArrow != null) {
            double[][] arrowCSysPoints = mclnArcArrow.getCSysPoints();
            mclnPolylineArc.setArrowCSysPoints(arrowCSysPoints);
        }
    }
}
