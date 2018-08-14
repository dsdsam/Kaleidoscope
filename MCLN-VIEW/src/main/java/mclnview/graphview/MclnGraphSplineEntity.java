package mclnview.graphview;

import adf.csys.view.CSysSplineEntity;
import adf.csys.view.CSysView;
import mcln.model.ArrowTipLocationPolicy;
import mcln.model.MclnArc;
import mcln.model.MclnSplineArc;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 6/16/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MclnGraphSplineEntity extends CSysSplineEntity implements Cloneable {

    private static final double ARROW_LENGTH = MclnArcArrow.DEFAULT_ARROW_LENGTH;
    private static final double GAP_BETWEEN_NODE_AND_ARROW = 5;

    private static final Color SPLINE_CREATION_COLOR = new Color(0xFF00FF);
    private static final Color SPLINE_DRAWING_COLOR = Color.GRAY;
    private static final Color CONVEX_SECTION_COLOR = new Color(0xFF0000);
    private static final Color FLAT_SECTION_COLOR = new Color(0x009900);

    private final MclnSplineArc mclnSplineArc;
    ;
    private MclnGraphNodeView inpNode;
    private MclnGraphNodeView outNode;

    private boolean highlightFlatSegments;
    private boolean selectingArrowTipLocation;
    private boolean highlightArcKnotsForArrowTipSelection;

    private int arrowKnobIndex = -1;
    private int arrowTipSplineIndex = -1;
    private MclnArcArrow mclnArcArrow;
    private Color arcMclnStateColor = Color.BLACK;

    private boolean knobSelected;


    //  ***********************************************************************************************
    //
    //   T h e   M e t h o d s   t o   C r e a t   A r c   v i a   G r a h i c a l   I n t e r f a c e
    //
    //  ***********************************************************************************************

    /**
     * Used when Arc is created interactively
     *
     * @param parentCSysView
     * @param inpNode
     */
    public MclnGraphSplineEntity(CSysView parentCSysView, MclnSplineArc mclnSplineArc, MclnGraphNodeView inpNode) {
        this(parentCSysView, mclnSplineArc, inpNode, null);
        setDrawColor(SPLINE_CREATION_COLOR);
    }

    //
    //  k n o t s   c r e a t i o n
    //

    void createFirstScrKnotMakeItActive(double[] scrPnt) {
        super.createFirstScrKnot(scrPnt);
        super.makeFirstKnotActive();
    }

    void addNextScrKnotAndMakeItActive(double[] scrPnt) {
        super.addNextScrKnot(scrPnt);
        super.makeLastKnotActive();
    }

    void addNextScrKnotAndMakePreviousKnotActive(double[] scrPnt) {
        super.addNextScrKnot(scrPnt);
        super.makePrevKnotActive();
    }

    @Override
    public int getNKnots() {
        return super.getNKnots();
    }

    @Override
    public void updateLastKnotLocation(double[] knotCSysPnt) {
        int lastKnotIndex = getNKnots() - 1;
        super.updateKnotCSysLocation(lastKnotIndex, knotCSysPnt);

    }

    /**
     * Editor sets Output Node right when it is picked up,
     * it does not wait until the creation is finished
     *
     * @param outNode
     */
    void splineCreationCompleted(MclnGraphNodeView outNode) {
        this.outNode = outNode;
        setDrawColor(SPLINE_DRAWING_COLOR);
    }

    //  *********************************************************************************************************
    //
    //   T h e   M e t h o d s   t o   I n s t a n t i a t e   A r c   W i t h   P r o g r a m m a t i c a l l y
    //                              P r o v i d e d    A t t r i b u t e s
    //
    //  *********************************************************************************************************

    /**
     * Called when Arc is created programmatically
     *
     * @param parentCSysView
     */
    public MclnGraphSplineEntity(CSysView parentCSysView, MclnSplineArc mclnSplineArc, MclnGraphNodeView inpNode, MclnGraphNodeView outNode) {
        super(parentCSysView);
        this.mclnSplineArc = mclnSplineArc;
        this.inpNode = inpNode;
        this.outNode = outNode;
    }

    //
    //  a r r o w   c r e a t i o n   a t t r i b u t e s
    //

    void setHighlightFlatSegments(boolean highlightFlatSegments) {
        this.highlightFlatSegments = highlightFlatSegments;
    }

    void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        this.selectingArrowTipLocation = selectingArrowTipLocation;
    }

    void destroyArcArrowUpOnUndoingArrowTipSelection() {
        arrowTipSplineIndex = -1;
        setArrow(null);
    }

    public void setHighlightArcKnotsForArrowTipSelection(boolean highlightArcKnotsForArrowTipSelection) {
        this.highlightArcKnotsForArrowTipSelection = highlightArcKnotsForArrowTipSelection;
    }

    public void setArrowTipSplineIndex(int arrowTipSplineIndex) {
        this.arrowTipSplineIndex = arrowTipSplineIndex;
    }

    private int getArrowTipSplineIndex() {
        return arrowTipSplineIndex;
    }

    //
    //   Arrow location indexes
    //

    void setArcKnobAt(int index) {
        arrowKnobIndex = index;
    }

    private int getArcKnobIndex() {
        return arrowKnobIndex;
    }

    public void setKnobSelected(boolean status) {
        this.knobSelected = status;
    }

    public double[] getScrOutlineCenter() {
        return mclnArcArrow.getScrOutlineCenter();
    }

    public void setArrowWatermarked(boolean watermarked) {
        mclnArcArrow.setWatermarked(watermarked);
    }

    void setArrowSelected(boolean arrowSelected) {
        mclnArcArrow.setSelected(arrowSelected);
    }

    public void setArrowHighlighted(boolean arrowHighlighted) {
        mclnArcArrow.setHighlighted(arrowHighlighted);
    }

    //  *********************************************************************************************************
    //
    //                                  M o v i n g   S p l i n e   K n o t s
    //                  a s   P a r t   o f   M o v e   E l e m e n t s   O p e r a t i o n
    //
    //  *********************************************************************************************************

    void restoreSplineFromModel() {
        // restoring knots
        List<double[]> knotCSysLocations = mclnSplineArc.getKnotCSysLocations();
        this.setSplineCSysKnots(knotCSysLocations);
        mclnArcArrow = repositionArcArrowAfterSplineReshaped(mclnSplineArc);
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
    }

    /**
     * @param point
     * @return
     */
    boolean selectAKnotUnderMouse(Point point) {
        int nPnts = getNKnots();
        selectedKnotIndex = super.findKnot(point.x, point.y);
        if (selectedKnotIndex <= 0 || selectedKnotIndex == (nPnts - 1)) {
            selectedKnotIndex = -1;
        }
        setSelectedKnotInd(selectedKnotIndex);
        return selectedKnotIndex != -1;
    }

    //   Following three methods below work when arc knots are moved
    //   They place Arrow on the arc thread

    void updateSplineKnotDragged(MclnArc mclnArc, Point mousePoint) {
        if (selectedKnotIndex < 0) {
            return;
        }
        double[] cSysPoint = {mousePoint.x, mousePoint.y, 0};
        super.moveSelectedKnotCSysPointTo(selectedKnotIndex, cSysPoint);

        mclnArcArrow = repositionArcArrowAfterSplineReshaped(mclnArc);
    }

    void updateArrowPositionWhenArcNodeMoved(MclnArc mclnArc) {
        mclnArcArrow = repositionArcArrowAfterSplineReshaped(mclnArc);
    }

    private MclnArcArrow repositionArcArrowAfterSplineReshaped(MclnArc mclnArc) {
        int arrowTipSplineIndex = getArrowTipSplineIndex();//mclnArc.getArrowTipSplineIndex();
        Color arcMclnStateColor = getArcMclnStateColor();
        boolean arrowSelected = mclnArcArrow.isArrowSelected();
        MclnArcArrow mclnArcArrow = buildArrowAtThePoint(arrowTipSplineIndex, arcMclnStateColor);
        // this line should be removed after I make arrow rotation without rectration
        mclnArcArrow.setSelected(arrowSelected);
        return mclnArcArrow;
    }

    //  *********************************************************************************************************
    //
    //                    M o v i n g   A r c   A s   a   P a r t   o f   a   F r a g m e n t
    //
    //  *********************************************************************************************************

    /**
     * @param translationVector
     */
    public void translate(double[] translationVector) {
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
     * @param translationVector
     */
    public void translateAndPaintSplineAtInterimLocation(Graphics g, double[] translationVector) {
        List<double[]> translatedSplineScrPoints = super.getTranslatedSplineScrPoints(translationVector);
        super.drawInterimSplineAccordingConditions(g, translatedSplineScrPoints);
        // move arrow should ne here
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.keySet();
    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationVector
     */
    public double[] takeFinalLocation(double[] translationVector) {
        double[] inpScrPnt = inpNode.getScrPnt();
        double[] outScrPnt = outNode.getScrPnt();
        super.takeFinalLocation(translationVector, inpScrPnt, outScrPnt);
        if (mclnArcArrow == null) {
            System.out.println("mclnArcArrow == null  " + inpNode.getUID());
        } else {
            mclnArcArrow.takeFinalLocation(translationVector);
        }
        return translationVector;
    }

    @Override
    public MclnGraphSplineEntity clone() {
        MclnGraphSplineEntity mcLnGraphSplineEntityClone = null;
        try {
            mcLnGraphSplineEntityClone = (MclnGraphSplineEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mcLnGraphSplineEntityClone;
        }
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

    //  *********************************************************************************************************
    //
    //                          S e t t i n g   d r a w i n g   a t t r i b u t e s
    //
    //  *********************************************************************************************************


    @Override
    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
        if (mclnArcArrow != null) {
            mclnArcArrow.setWatermarked(watermarked);
        }
    }

    @Override
    public void setPreSelected(boolean preSelected) {
        super.setSelected(preSelected);
        if (mclnArcArrow == null) {
            return;
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mclnArcArrow == null) {
            return;
        }
        mclnArcArrow.setSelected(selected);
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


    //  *********************************************************************************************************
    //
    //                                    D r a w i n g    S p l i n e
    //
    //  *********************************************************************************************************


    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
        if (mclnArcArrow != null) {
            mclnArcArrow.doCSysToScreenTransformation(scr0, scale);
        }
    }

    @Override
    public void drawPlainEntity(Graphics g) {
        drawSplineAccordingConditions(g, splineScrPoints);
    }

    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        List<double[]> translatedSplineScrPoints = super.getTranslatedSplineScrPoints();
        super.drawInterimSplineAccordingConditions(g, translatedSplineScrPoints);
    }


    /**
     * Main method to draw spline with knots
     *
     * @param g
     */
    @Override
    protected final void drawSplineAccordingConditions(Graphics g, List<double[]> splineScrPoints) {
        if (hidden || parentCSys == null) {
            return;
        }

        // Drawing spline line
        if (highlightFlatSegments) {
            MclnGraphViewEditorHelper.findSplineFlatSegments(splineScrPoints);
        }
        drawSpline(g, splineScrPoints);

        // Drawing knots and knob
        drawSplineKnots(g);

        // Highlighting selected knots (when creating arrow knob)
        if (selectingArrowTipLocation) {
            if (highlightArcKnotsForArrowTipSelection && highlightedKnots != null) {
//                highlightSplineSegment(g, highlightedKnots);
            }
        }

        // Drawing Arc Arrow
        if (mclnArcArrow != null) {
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }
    }

    /**
     * @param g
     */
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
            drawSpline(g, splineScrPoints);
            g2D.setStroke(currentStroke);
        }
        if (mclnArcArrow != null) {
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    /**
     * @param g
     */
    @Override
    protected final void drawSpline(Graphics g, List<double[]> splineThreadScrPoints) {

        int nKnots = getNKnots();
        if (nKnots < 1) {
            return;
        }

        int nPoints = splineThreadScrPoints.size();
        if (nPoints == 0) {
            return;
        }

        double[] curPnt = splineThreadScrPoints.get(0);

        int cx = (int) (curPnt[0] + 0.5);
        int cy = (int) (curPnt[1] + 0.5);

        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 1; i < nPoints; i++) {
            curPnt = splineThreadScrPoints.get(i);
            int nx = (int) (curPnt[0] + 0.5);
            int ny = (int) (curPnt[1] + 0.5);

            if (highlightFlatSegments) {
                double currentPointFlag = curPnt[2];
                if (currentPointFlag < 0.) {
                    g.setColor(CONVEX_SECTION_COLOR);
                } else {
                    g.setColor(FLAT_SECTION_COLOR);
                }
            } else {
                Color splineThreadColor = getThreadColor();
                g.setColor(splineThreadColor);
            }
            g.drawLine(cx, cy, nx, ny);
            cx = nx;
            cy = ny;
        }
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

    /**
     * Keep it for debugging
     *
     * @param g
     * @param highlightedKnots
     */
    private void highlightSplineSegment(Graphics g, double[][] highlightedKnots) {

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = g.getColor();

        g.setColor(Color.RED);
        int dotX = ((int) (highlightedKnots[0][0] + 0.5)) - 5;
        int dotY = ((int) (highlightedKnots[0][1] + 0.5)) - 5;
        g.drawOval(dotX, dotY, 10, 10);
//        g.drawRect(dotX, dotY, 10, 10);
        dotX = ((int) (highlightedKnots[1][0] + 0.5)) - 5;
        dotY = ((int) (highlightedKnots[1][1] + 0.5)) - 5;
        g.drawOval(dotX, dotY, 10, 10);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
        g.setColor(currentColor);
    }

    private Color getArrowColor() {
        Color color = DEFAULT_DRAWING_COLOR;

        if (selectingArrowTipLocation) {
            color = getHighlightColor();
        } else if (isWatermarked()) {
            color = getWatermarkColor();
            return color;
        }
//        else if (isHighlighted()) {
//            color = getHighlightColor();
//        } else if (isMouseHover()) {
//            color = getHighlightColor();
//        }
        return color;
    }

    private int[] highlightedKnotIndexes;
    private double[][] highlightedKnots;


    //  *********************************************************************************************************
    //
    //                          A r c   A r r o w   P o s i t i o n i n g   M e t h o d s
    //
    //  *********************************************************************************************************

    private void setArrow(MclnArcArrow mclnArcArrow) {
        if (mclnArcArrow == null) {
            System.out.println();
        }
        this.mclnArcArrow = mclnArcArrow;
    }

    /**
     * Is intended to be developed to semi-automatically
     * find arrow tip location for multi-knot arc
     *
     * @param x
     * @param y
     * @return
     */
    int getTwoMouseClosedKnotsAndFindArrowTipLocation(int x, int y) {

        highlightedKnots = null;
        this.arrowTipSplineIndex = -1;

        int arrowTipSplineScrIndex = -1;

        List<double[]> splineScrKnots = getSplineScrKnots();
        int[] highlightedKnotIndexes = SplineCurveAnalyser.getTwoMouseNearestKnots(x, y, splineScrKnots);
        if (highlightedKnotIndexes == null) {
            return arrowTipSplineScrIndex;
        }

        double[] firstKnot = splineScrKnots.get(highlightedKnotIndexes[0]);
        double[] secondKnot = splineScrKnots.get(highlightedKnotIndexes[1]);
        highlightedKnots = new double[][]{firstKnot, secondKnot};

        int arrowLength = MclnArcArrow.DEFAULT_ARROW_LENGTH;
        arrowTipSplineScrIndex = findArrowTipIndexNew(arrowLength, highlightedKnotIndexes);

        if (arrowTipSplineScrIndex >= 0) {
            Color arcMclnStateColor = getArcMclnStateColor();
            mclnArcArrow = buildArrowAtThePoint(arrowTipSplineScrIndex, arcMclnStateColor);
        } else {
            mclnArcArrow = null;
        }

        this.arrowTipSplineIndex = arrowTipSplineScrIndex;
        return arrowTipSplineScrIndex;
    }


    private int findArrowTipIndexNew(int arrowLength, int[] boundaryKnotIndexes) {

        int nKnots = getNKnots();
        if (nKnots < 3) {
            return -1;
        }

        int begKnotInd = boundaryKnotIndexes[0];
        int endKnotInd = boundaryKnotIndexes[1];

        int lastKnotIndex = nKnots - 1;
        double inpNodeRadius = (begKnotInd == 0) ? ((inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS) : 0;
        double outNodeRadius = (endKnotInd == lastKnotIndex) ? ((outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS) : 0;

        int where = 2;

        int arrowTipSplineScrIndex = findArrTipIndNew(begKnotInd, endKnotInd, arrowLength, inpNodeRadius, outNodeRadius, where);
        return arrowTipSplineScrIndex;
    }

    /**
     * @param begKnotInd
     * @param endKnotInd
     * @param arrLen
     * @param r1
     * @param r2
     * @param where
     * @return
     */
    private int findArrTipIndNew(int begKnotInd, int endKnotInd, double arrLen, double r1, double r2, int where) {
        /*
           Segments are small straight lines drawn between knots
         */
        int nSeg = getNSegments();

        List<double[]> splineScrKnots = getSplineScrKnots();

        int begSegInd = begKnotInd * nSeg;
        int endSegInd = endKnotInd * nSeg;

        double[] startScrPnt = getSplineScrPnt(begSegInd);
        double[] endScrPnt = getSplineScrPnt(endSegInd);

        double[] prevSegScrPnt = startScrPnt;
        double[] currSegScrPnt = null;
        int curSegInd = begSegInd + 1;

        int firstInd = -1, midInd = -1, lastInd = -1;

//
//  case 2:   // find first appropriate segment in middle section - between knots

        //
        // fond first index for arrow tip
        //
        double dist = 0;

        double distanceFromFirstKnotToArrowTip = 0;
        for (curSegInd = begSegInd + 1; curSegInd < endSegInd; curSegInd++) {
            currSegScrPnt = getSplineScrPntWithZ(curSegInd);
            double currentPointFlag = currSegScrPnt[2];
            currSegScrPnt[2] = 0.;
            double segLength = VAlgebra.distVec3(currSegScrPnt, prevSegScrPnt);
            distanceFromFirstKnotToArrowTip += segLength;

            // the distance from start point to arrow tip should
            // be longer then input node radius + arrow length

            prevSegScrPnt = currSegScrPnt;
            if (currentPointFlag < 0) {
                continue;
            }
            if (distanceFromFirstKnotToArrowTip > (r1 + arrLen)) {
                // first index from input Node that can be an Arrow tip
                firstInd = curSegInd;
            }
        }


        //
        // check if the arrow does not overlaps with output Node
        //
        dist = VAlgebra.distVec3(endScrPnt, currSegScrPnt);
        if (dist < r2) {
            return -1;
        }
        if (where == 2) {
            return firstInd;
        }
//
//  case 2:  find last appropriate segment ind
        curSegInd = endSegInd;
        currSegScrPnt = endScrPnt;
        dist = 0;
        for (int i = 0; i < nSeg; curSegInd--, i++) {
            currSegScrPnt = getSplineScrPnt(curSegInd);
            dist = VAlgebra.distVec3(endScrPnt, currSegScrPnt);
            if (dist > r2) {
                lastInd = curSegInd;
                break;
            }
        }
        dist = VAlgebra.distVec3(startScrPnt, currSegScrPnt);
        if (dist < r1 + arrLen) {
            return -1;
        }
        if (where == 2) {
            return lastInd;
        }
//
//  case 3:   find midl appropriate segment ind
        curSegInd = firstInd + (lastInd - firstInd) / 2;
//        System.out.println(" dist " + dist + "  " + r1 + arrLen + "  " + curSegInd);

        if (where == 3) {
            return curSegInd;
        } else {
            return -1;
        }
    }

    /**
     * The method is used during Arc creation to check if the Arc
     * thread length or shape allows to place the Arc Arrow on it.
     *
     * @return
     */
    boolean checkIfArrowTipCanBeFound() {
        System.out.println("checkIfArrowTipLocationCanBeFound ");

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        List<double[]> splineScrKnots = getSplineScrKnots();
        int nSeg = getNSegments();

        int begSegInd = 0;
        int endSegInd = ((splineScrKnots.size() - 1) * nSeg);
        double[] prevSplineScrPnt = getSplineScrPntWithZ(begSegInd);
        prevSplineScrPnt[2] = 0.;
        int curSegInd = begSegInd + 1;

        double[] outNodeScrPnt = outNode.getScrPnt();

        double distanceFromInpNodeToArrowTip = 0.;
        try {
            for (int i = 1; i < endSegInd; curSegInd++, i++) {
                System.out.println("ind i " + i + ", curSegInd " + curSegInd + ",   " + splineScrPoints.size());
                double[] currSplineScrPnt = getSplineScrPntWithZ(curSegInd);
                currSplineScrPnt[2] = 0.;

                double segLength = VAlgebra.distVec3(currSplineScrPnt, prevSplineScrPnt);
                distanceFromInpNodeToArrowTip += segLength;
                double distToOutNode = VAlgebra.distVec3(currSplineScrPnt, outNodeScrPnt);

                prevSplineScrPnt = currSplineScrPnt;
                double inpNodeCriticalDistance = distanceFromInpNodeToArrowTip -
                        (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW);

                // the distance from start point to arrow tip should
                // be longer then input node radius + arrow length + small gap;
                if (inpNodeCriticalDistance < 0.) {
                    System.out.println("Arrow is too close to input node ");
                    continue;
                }
                double outNodeCriticalDistance = distToOutNode - (outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW + 5);
                if (outNodeCriticalDistance < 0.) {
                    System.out.println("Arrow is too close to Out node ");
                    continue;
                }
                return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method is called from Mcln Spline Arc View and ultimately from
     * Spline Arc Creator class to let user to place Arc Arrow by moving
     * the mouse to desired a point. Called when arrow creation is complete.
     * Search range is from Arc Input Node to Arc Output Node
     *
     * @param x
     * @param y
     * @return
     */
    int findArrowTipIndexOnTheSplineForJustCreatedArc(int x, int y) {

        highlightedKnots = null;
        List<double[]> splineScrKnots = getSplineScrKnots();

        int arrowTipSplineScrIndex = -1;
        int firstKnotIndex = 0;
        int outputArcKnotIndex = splineScrKnots.size() - 1;
        highlightedKnotIndexes = new int[]{firstKnotIndex, outputArcKnotIndex};

        arrowTipSplineScrIndex = findArrowTipLocationUnderMouse(x, y, highlightedKnotIndexes);

        if (arrowTipSplineScrIndex >= 0) {
            Color arcMclnStateColor = getArcMclnStateColor();
            mclnArcArrow = buildArrowAtThePoint(arrowTipSplineScrIndex, arcMclnStateColor);
        } else {
            mclnArcArrow = null;
        }

        this.arrowTipSplineIndex = arrowTipSplineScrIndex;
        return arrowTipSplineScrIndex;
    }


    /**
     * @param x
     * @param y
     * @param boundaryKnotIndexes
     * @return
     */
    private int findArrowTipLocationUnderMouse(int x, int y, int[] boundaryKnotIndexes) {

        double[] currentMouseVec = {x, y, 0.};

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        int nSeg = getNSegments();

        int begKnotInd = boundaryKnotIndexes[0];
        int endKnotInd = boundaryKnotIndexes[1];

        int begSegInd = begKnotInd * nSeg;
        int endSegInd = (endKnotInd * nSeg) + 0;
        double[] prevSplineScrPnt = getSplineScrPntWithZ(begSegInd);
        prevSplineScrPnt[2] = 0.;
        int curSegInd = begSegInd + 1;

        double[] outNodeScrPnt = outNode.getScrPnt();

        int arrowTipSplineIndex = -1;
        double minDistance = Double.MAX_VALUE;
        double distanceFromInpNodeToArrowTip = 0.;
        List<double[]> splineScrPoints = getSplineScrPoints();
        try {
            for (int i = 1; i < endSegInd; curSegInd++, i++) {
                double[] currSplineScrPnt = getSplineScrPntWithZ(curSegInd);
                double currentPointFlag = currSplineScrPnt[2];
                currSplineScrPnt[2] = 0.;

                double segLength = VAlgebra.distVec3(currSplineScrPnt, prevSplineScrPnt);
                distanceFromInpNodeToArrowTip += segLength;
                double distToOutNode = VAlgebra.distVec3(currSplineScrPnt, outNodeScrPnt);

                prevSplineScrPnt = currSplineScrPnt;
//                if (currentPointFlag < 0) {
//                    continue;
//                }
                double inpNodeCriticalDistance = distanceFromInpNodeToArrowTip -
                        (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW);
                // the distance from start point to arrow tip should
                // be longer then input node radius + mclnArcArrow length + small gap;
//                System.out.println("ind " + i);
//                System.out.println("segLength " + segLength);
//                System.out.println("distanceFromInpNodeToArrowTip " + distanceFromInpNodeToArrowTip);
//                System.out.println("inpNodeRadius " + inpNodeRadius);
//                System.out.println("ARROW_LENGTH " + ARROW_LENGTH);
//                System.out.println("GAP_BETWEEN_NODE_AND_ARROW " + GAP_BETWEEN_NODE_AND_ARROW);
//                System.out.println("inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW " + (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW));
//                System.out.println("inpNodeCriticalDistance " + inpNodeCriticalDistance);
                if (inpNodeCriticalDistance < 0.) {
//                    System.out.println("Arrow is too close to input node ");
                    continue;
                }
                double outNodeCriticalDistance = distToOutNode - (outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW);
                if (outNodeCriticalDistance < 0.) {
                    continue;
                }
                double mouseToPointDist = VAlgebra.distVec3(currentMouseVec, currSplineScrPnt);
                if (mouseToPointDist < 200000 && mouseToPointDist < minDistance) {
                    minDistance = mouseToPointDist;
                    arrowTipSplineIndex = curSegInd;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrowTipSplineIndex;
    }


    /**
     * This method construct McLN Arrow
     *
     * @param arrowTipIndex
     * @param stateColor
     * @return
     */
    private MclnArcArrow buildArrowAtThePoint(int arrowTipIndex, Color stateColor) {

        if (arrowTipIndex < 0) {
            return null;
        }

        int length = MclnArcArrow.DEFAULT_ARROW_LENGTH;
        int width = MclnArcArrow.DEFAULT_ARROW_WIDTH;

        double[] arrTipScrPnt = getSplineScrPnt(arrowTipIndex);
        double[] arrEndScrPnt = getSplineScrPnt(arrowTipIndex - 1);
        double[] arrowScrDirection = VAlgebra.subVec3(arrEndScrPnt, arrTipScrPnt);
        double[] normalizedScrDirectionVector = VAlgebra.normalizeVec3(arrowScrDirection);
        double[] arrowTipLocation = getSplineScrPnt(arrowTipIndex);

        MclnArcArrow mclnArcArrow = new MclnArcArrow(parentCSys, length, width, normalizedScrDirectionVector,
                arrowTipLocation, stateColor);

        return mclnArcArrow;
    }


    /**
     * This method construct McLN Arrow
     * 1) when Demo-project created
     * 1) When Editor finished Arc creation
     * 2) When McLN Model is retrieved from XML-file
     *
     * @param parentCSys
     * @param stateColor
     * @param inpNode
     * @param optNode
     * @return
     */
    MclnArcArrow constructRetrievedArrow(MclnArc mclnArc, CSysView parentCSys, Color stateColor,
                                         MclnGraphNodeView inpNode, MclnGraphNodeView optNode) {
        setArrow(null);

        int length = MclnArcArrow.DEFAULT_ARROW_LENGTH;
        int width = MclnArcArrow.DEFAULT_ARROW_WIDTH;

        ArrowTipLocationPolicy arrowTipLocationPolicy = mclnArc.getArrowTipLocationPolicy();
        int arrowTipIndex;
        if (arrowTipLocationPolicy == ArrowTipLocationPolicy.DETERMINED_BY_USER) {
            arrowTipIndex = getArrowTipSplineIndex();
            if (arrowTipIndex <= 0) {
                return null;
            }
        } else {
            arrowTipIndex = findRetrievedArrowTipIndex(length, inpNode, optNode);
            if (arrowTipIndex <= 0) {
                length = MclnArcArrow.MEDIUM_ARROW_LENGTH;
                width = MclnArcArrow.MEDIUM_ARROW_WIDTH;
                arrowTipIndex = findRetrievedArrowTipIndex(length, inpNode, optNode);
            }
            if (arrowTipIndex <= 0) {
                length = MclnArcArrow.SMALL_ARROW_LENGTH;
                width = MclnArcArrow.SMALL_ARROW_WIDTH;
                arrowTipIndex = findRetrievedArrowTipIndex(length, inpNode, optNode);
            }
            if (arrowTipIndex <= 0) {
                return null;
            }
            mclnArc.setArrowTipSplineIndex(arrowTipIndex);
            setArrowTipSplineIndex(arrowTipIndex);
        }


        double[] arrowTipLocation = getSplineScrPnt(arrowTipIndex);
        double[] arrEndScrPnt = getSplineScrPnt(arrowTipIndex - 1);
        double[] arrowScrDirection = VAlgebra.subVec3(arrEndScrPnt, arrowTipLocation);
        double[] normalizedDirectionScrVector = VAlgebra.normalizeVec3(arrowScrDirection);

        MclnArcArrow mclnArcArrow = new MclnArcArrow(parentCSys, length, width, normalizedDirectionScrVector, arrowTipLocation,
                stateColor);
        setArrow(mclnArcArrow);
        return mclnArcArrow;
    }

    /*

     THis is the method that is used from ConstructRetrievedArrow method.

   Src Node            prev knpb                knob                      next knob                    Dst Node
                       prev knob index          knob index                next knob index
    */
    private int findRetrievedArrowTipIndex(int arrowLength, MclnGraphNodeView inpNode,
                                           MclnGraphNodeView optNode) {

        double dR = 3;

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;

        int knobIndex = getArcKnobIndex();
        int nKnots = getNKnots();
        int arrTipIndex = 0;
        if (nKnots >= 3) {

            double[] prevScrPnt = getScrKnot(knobIndex - 1);
            double[] knobScrPnt = getScrKnot(knobIndex);
            double dist1 = VAlgebra.distVec3(prevScrPnt, knobScrPnt);
            prevScrPnt = getScrKnot(knobIndex);
            knobScrPnt = getScrKnot(knobIndex + 1);
            double dist2 = VAlgebra.distVec3(prevScrPnt, knobScrPnt);

            int where = 0;

            if (dist2 - (dR + outNodeRadius) > arrowLength) {
                where = (dist2 - (dR + outNodeRadius) > 4 * arrowLength) ? 3 : 1;
                where = 1;
                arrTipIndex = findArrTipInd(knobIndex, knobIndex + 1, arrowLength,
                        dR, outNodeRadius, where);
            } else {
                if (dist1 - (dR + inpNodeRadius) < arrowLength) {
                    where = 3;
                    arrTipIndex = findArrTipInd(knobIndex - 1, knobIndex + 1, arrowLength,
                            dR, outNodeRadius, where);
                    return arrTipIndex;
                }

                where = (dist2 - (dR + inpNodeRadius) > 4 * arrowLength) ? 3 : 2;
                where = 2;
                arrTipIndex = findArrTipInd(knobIndex - 1, knobIndex, arrowLength,
                        inpNodeRadius, dR, where);
            }
//
            if (arrTipIndex < 0) {
                return -1;
            }
        }
        return arrTipIndex;
    }

    /**
     * The method is to copy all Arc's attributes to persistent data class - Mcln Arc
     * <p>
     * Called when Arc Creator finished
     * creating Arc
     * moving Arc elements
     *
     * @param mclnSplineArc
     */
    void storeArcPersistentAttributesIntoMclnArc(MclnSplineArc mclnSplineArc) {

        // What is this?
        int knobIndex = getArcKnobIndex();
        mclnSplineArc.setKnobIndex(knobIndex);

        // storing knots
        List<double[]> knotCSysLocations = this.getSplineCSysKnots();
        mclnSplineArc.setCSysKnots(knotCSysLocations);

//        List<double[]> knotCSysLocations = this.getKnotCSysLocations();
//        mclnSplineArc.setCSysKnots(knotCSysLocations);

        // storing calculated spline thread
        List<double[]> splineCSysPoints = getSplineCSysPoints();
        mclnSplineArc.setSplineCSysPoints(splineCSysPoints);

        // storing Arrow cSys points
        if (mclnArcArrow != null) {
            mclnSplineArc.setArrowTipSplineIndex(arrowTipSplineIndex);
            double[][] arrowCSysPoints = mclnArcArrow.getCSysPoints();
            mclnSplineArc.setArrowCSysPoints(arrowCSysPoints);
        }
    }
}
