package mclnview.graphview;

import adf.csys.view.CSysSplineEntity;
import adf.csys.view.CSysView;
import mcln.model.ArrowTipLocationPolicy;
import mcln.model.MclnArc;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Admin on 11/15/2017.
 */
class MclnGraphSplineViewEntity extends CSysSplineEntity implements Cloneable {

    private static final double ARROW_LENGTH = MclnArcArrow.DEFAULT_ARROW_LENGTH;
    private static final double GAP_BETWEEN_NODE_AND_ARROW = 5;

    //    private static final Color CONVEX_SECTION_COLOR = new Color(0xFF0099);
//    private static final Color CONVEX_SECTION_COLOR = new Color(0xFF00CC);
//    private static final Color CONVEX_SECTION_COLOR = new Color(0xBA1F1F);
    private static final Color CONVEX_SECTION_COLOR = new Color(0xFF0000);
    //    private static final Color CONVEX_SECTION_COLOR = new Color(0xDA1616);
//    private static final Color FLAT_SECTION_COLOR = new Color(0x31A4B1);
    private static final Color FLAT_SECTION_COLOR = new Color(0x009900);

    private MclnGraphViewNode inpNode;
    private MclnGraphViewNode outNode;

    private boolean highlightFlatSegments;
    private boolean selectingArrowTipLocation;
    private boolean highlightArcKnotsForArrowTipSelection;

    private int arrowKnobIndex = -1;
    private int arrowBaseKnotIndex = -1;
    private int arrowTipSplineIndex = -1;
    private MclnArcArrow tentativeMclnArcArrow;
    private MclnArcArrow mclnArcArrow;

    private Color mclnArcStateColor = Color.BLACK;
    private boolean knobSelected;


    /**
     * Used by Editor
     *
     * @param parentCSysView
     * @param inpNode
     */
    public MclnGraphSplineViewEntity(CSysView parentCSysView, MclnGraphViewNode inpNode) {
        this(parentCSysView, inpNode, null);
    }

    /**
     * Called when McLN model retrieved
     *
     * @param parentCSysView
     */
    public MclnGraphSplineViewEntity(CSysView parentCSysView, MclnGraphViewNode inpNode, MclnGraphViewNode outNode) {
        super(parentCSysView);
        this.inpNode = inpNode;
        this.outNode = outNode;
    }

//    public void updateArrowColorUponInitialization(Color stateColor) {
//        mclnArcArrow.updateStateColorUponInitialization(stateColor);
//    }

    @Override
    public MclnGraphSplineViewEntity clone() {
        MclnGraphSplineViewEntity mclnGraphSplineViewEntityClone = null;
        try {
            mclnGraphSplineViewEntityClone = (MclnGraphSplineViewEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mclnGraphSplineViewEntityClone;
        }
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

    void addFinalScrKnotAndSetNoActiveKnots(double[] scrPnt) {
        super.addNextScrKnot(scrPnt);
        super.setNoActiveKnots();
    }

    /**
     * Editor sets Output Node right when it is picked up,
     * it does not wait until the creation is finished
     *
     * @param outNode
     */
    void setOutputNode(MclnGraphViewNode outNode) {
        this.outNode = outNode;
    }

    //
    //  a r r o w   c r e a t i o n
    //

    void setHighlightFlatSegments(boolean highlightFlatSegments) {
        this.highlightFlatSegments = highlightFlatSegments;
    }

    void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        this.selectingArrowTipLocation = selectingArrowTipLocation;
    }

    void destroyArcArrowUpOnUndoingArrowTipSelection() {
        arrowTipSplineIndex = -1;
        tentativeMclnArcArrow = null;
        mclnArcArrow = null;
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

    public Color getMclnArcStateColor() {
        return mclnArcStateColor;
    }

    public void setMclnArcStateColor(Color mclnArcStateColor) {
        this.mclnArcStateColor = mclnArcStateColor;
    }

    //
    //   Arrow location indexes
    //

    void setArrowBaseKnotIndexAt(int index) {
        arrowBaseKnotIndex = index;
    }

    int getArrowBaseKnotIndexAt() {
        return arrowBaseKnotIndex;
    }

    void setArcKnobAt(int index) {
        arrowKnobIndex = index;
    }

    private int getArcKnobIndex() {
        return arrowKnobIndex;
    }

    public void setKnobSelected(boolean status) {
        this.knobSelected = status;
    }

    private boolean isKnobSelected() {
        return knobSelected;
    }

    MclnArcArrow editorFinishedSplineCreation(MclnArc mclnArc) {
        finishSplineCreation(mclnArc, tentativeMclnArcArrow);
        tentativeMclnArcArrow = null;
        return mclnArcArrow;
    }

    /**
     * Called when Editor finished Acr creation
     */
    void finishSplineCreation(MclnArc mclnArc, MclnArcArrow mclnArcArrow) {

        int knobIndex = getArcKnobIndex();
        mclnArc.setKnobIndex(knobIndex);

        this.mclnArcArrow = mclnArcArrow;


        // storing spline cSys points and arrow cSys points for future storing as xml
        if (mclnArcArrow != null) {
            java.util.List<double[]> splineCSysPoints = getSplineCSysPoints();
            double[][] arrowCSysPoints = mclnArcArrow.getCSysPoints();
            mclnArc.setSplineCSysPoints(splineCSysPoints, arrowCSysPoints);

            mclnArc.setArrowTipSplineIndex(arrowTipSplineIndex);
        }
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
        if (mclnArcArrow != null) {
            mclnArcArrow.doCSysToScreenTransformation(scr0, scale);
        }
    }

    @Override
    public int getNKnots() {
        return super.getNKnots();
    }

    @Override
    public int findKnot(int x, int y) {
        return super.findKnot(x, y);
    }

    public void setArrowWatermarked(boolean watermarked) {
        mclnArcArrow.setWatermarked(watermarked);
    }

    public void setArrowSelected(boolean arrowSelected) {
        mclnArcArrow.setSelected(arrowSelected);
    }

    public void setArrowHighlighted(boolean arrowHighlighted) {
        mclnArcArrow.setHighlighted(arrowHighlighted);
    }

    //
    //   M o v i n g   t h e   S p l i n e
    //

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
//        System.out.println("9999999999999999999999999999999");
        java.util.List<double[]> translatedSplineScrPoints = super.getTranslatedSplineScrPoints(translationVector);
        super.drawInterimSplineAccordingConditions(g, translatedSplineScrPoints);

        // move arrow should ne here
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.keySet();
    }

//    public void paintSplineAtInterimLocation(Graphics g, double[] translationVector) {
//        List<double[]> translatedSplineScrPoints = super.getTranslatedSplineScrPoints(translationVector);
//        super.drawInterimSpline(g, translatedSplineScrPoints);
//
//        // move arrow should ne here
//    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationVector
     */
    public double[] takeFinalLocation(double[] translationVector) {
        double[] intScrPnt = inpNode.getScrPnt();
        double[] outScrPnt = outNode.getScrPnt();
        super.takeFinalLocation(translationVector, intScrPnt, outScrPnt);

        if (mclnArcArrow == null) {
            System.out.println("mclnArcArrow == null  " + inpNode.getUID());
        } else {
            mclnArcArrow.takeFinalLocation(translationVector);
        }
        return translationVector;
    }


//    public void resetToOriginalLocation(   ){
//        super.doCSysToScreenTransformation(scr0, scale);
//        if (mclnArcArrow != null) {
//            mclnArcArrow.doCSysToScreenTransformation(scr0, scale);
//        }
//    }

    //
    //   d r a w i n g   a t t r i b u t e   s e t t i n g s
    //

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
        mclnArcArrow.setPreSelected(preSelected);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mclnArcArrow == null) {
            return;
        }
        mclnArcArrow.setSelected(selected);
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        if (mclnArcArrow == null) {
            return false;
        }
        return mclnArcArrow.isMouseHover(x, y);
    }

    //
    //     D r a w    S p l i n e
    //

    @Override
    public void drawPlainEntity(Graphics g) {
        drawSplineAccordingConditions(g, splineScrPoints);
    }

    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        java.util.List<double[]> translatedSplineScrPoints = super.getTranslatedSplineScrPoints();
        super.drawInterimSplineAccordingConditions(g, translatedSplineScrPoints);
    }


    /**
     * Main method to draw spline with knots
     *
     * @param g
     */
    @Override
    protected final void drawSplineAccordingConditions(Graphics g, java.util.List<double[]> splineScrPoints) {

        if (hidden || parentCSys == null) {
            return;
        }

        //
        //     Drawing spline line
        //

        if (highlightFlatSegments) {
//            MclnGraphViewEditorHelper.findSplineFlatSegments(splineScrPoints);
        }
        drawSpline(g, splineScrPoints);

        //
        //     Drawing knots and knob
        //

        drawSplineKnots(g);

        //
        //     Highlighting selected knots (when creating arrow knob)
        //

        if (selectingArrowTipLocation) {
            if (highlightArcKnotsForArrowTipSelection && highlightedKnots != null) {
//                highlightSplineSegment(g, highlightedKnots);
            }
            if (tentativeMclnArcArrow != null) {
                tentativeMclnArcArrow.drawArrow(g, selectingArrowTipLocation);
                mclnArcArrow = tentativeMclnArcArrow;
            }
            return;
        }

        if (mclnArcArrow != null) {
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }
    }

    public void paintExtras(Graphics g) {

        if (!(isWatermarked() || isHighlightedOrSelected())) {
            return;
        }

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke currentStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(1));
//        g2D.setColor(getSelectedColor());
        drawSpline(g, splineScrPoints);
        g2D.setStroke(currentStroke);

        if (mclnArcArrow != null) {
//            mclnArcArrow.setWatermarked(isWatermarked());
            mclnArcArrow.drawArrow(g, selectingArrowTipLocation);
        }

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);

    }

    /**
     * @param g
     */
    @Override
    protected final void drawSpline(Graphics g, java.util.List<double[]> splineThreadScrPoints) {

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

        Object currentSetting = null;
//        if (highlightFlatSegments) {
////            currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
////            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        } else {
//            currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
//            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        }
        currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


//        boolean flipFlag = false;
        for (int i = 1; i < nPoints; i++) {
            curPnt = splineThreadScrPoints.get(i);
//            VAlgebra.printVec3(curPnt);
            int nx = (int) (curPnt[0] + 0.5);
            int ny = (int) (curPnt[1] + 0.5);

            if (highlightFlatSegments) {
                double currentPointFlag = curPnt[2];
//                System.out.println("drawSpline " + i + "  " + nx + "    " + ny + " COLOR   INDICATOR  " + curPnt[2]);
                if (currentPointFlag < 0.) {
//                    System.out.println("highlighting " + CONVEX_SECTION_COLOR);
                    g.setColor(CONVEX_SECTION_COLOR);
                } else {
//                    System.out.println("normal  " + FLAT_SECTION_COLOR);
                    g.setColor(FLAT_SECTION_COLOR);
                }
            } else {
                Color splineThreadColor = getThreadColor();
                g.setColor(splineThreadColor);
            }

            g.drawLine(cx, cy, nx, ny);
            cx = nx;
            cy = ny;
//            flipFlag ^= true;
        }

//        if (highlightFlatSegments) {
//            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
//        }
    }

    @Override
    protected Color getThreadColor() {
        return Color.LIGHT_GRAY;
    }


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


    /**
     * Is intended to be developed to semi-automatically
     * find arrow tip location for multi-knot arc
     *
     * @return
     */
//    int getTwoMouseClosedKnotsAndFindArrowTipLocation(int x, int y) {
//
//        highlightedKnots = null;
//        this.arrowTipSplineIndex = -1;
//
//        int arrowTipSplineScrIndex = -1;
//
//        java.util.List<double[]> splineScrKnots = getSplineScrKnots();
//        int[] highlightedKnotIndexes = SplineCurveAnalyser.getTwoMouseNearestKnots(x, y, splineScrKnots);
//        if (highlightedKnotIndexes == null) {
//            return arrowTipSplineScrIndex;
//        }
//
//        double[] firstKnot = splineScrKnots.get(highlightedKnotIndexes[0]);
//        double[] secondKnot = splineScrKnots.get(highlightedKnotIndexes[1]);
//        highlightedKnots = new double[][]{firstKnot, secondKnot};
//
//        int arrowLength = MclnArcArrow.DEFAULT_ARROW_LENGTH;
//        arrowTipSplineScrIndex = findArrowTipIndexNew(arrowLength, highlightedKnotIndexes);
//
//        if (arrowTipSplineScrIndex >= 0) {
//            Color arcMclnStateColor = getArcMclnStateColor();
//            tentativeMclnArcArrow = buildArrowAtThePoint(arrowTipSplineScrIndex, arcMclnStateColor);
//        } else {
//            tentativeMclnArcArrow = null;
//        }
//
//        this.arrowTipSplineIndex = arrowTipSplineScrIndex;
//        return arrowTipSplineScrIndex;
//    }


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

        java.util.List<double[]> splineScrKnots = getSplineScrKnots();

        int begSegInd = begKnotInd * nSeg;
        int endSegInd = endKnotInd * nSeg;

        double[] startScrPnt = getSplineScrPnt(begSegInd);
        double[] endScrPnt = getSplineScrPnt(endSegInd);

        double[] prevSegScrPnt = startScrPnt;
        double[] currSegScrPnt = null;
        int curSegInd = begSegInd + 1;

        int arrowTipIndex = -1;

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

    boolean checkIfArrowTipCanBeFound( ) {
        java.util.List<double[]> splineScrKnots = getSplineScrKnots();

        int firstKnotIndex = 0;
        int outputArcKnotIndex = splineScrKnots.size() - 1;
        int[] highlightedKnotIndexes = new int[]{firstKnotIndex, outputArcKnotIndex};

        boolean arrowTipIndexFound  = checkIfArrowTipLocationCanBeFound(  highlightedKnotIndexes);
        return arrowTipIndexFound;
    }

    /**
     * @param boundaryKnotIndexes
     * @return
     */
    private boolean checkIfArrowTipLocationCanBeFound( int[] boundaryKnotIndexes) {
        System.out.println("checkIfArrowTipLocationCanBeFound " );

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

        double distanceFromInpNodeToArrowTip = 0.;
        try {
            for (int i = 1; i < endSegInd; curSegInd++, i++) {
                System.out.println("ind i " + i+", curSegInd "+curSegInd+",   "+splineScrPoints.size());
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
//                System.out.println("ind " + i);
//                System.out.println("segLength " + segLength);
//                System.out.println("distanceFromInpNodeToArrowTip " + distanceFromInpNodeToArrowTip);
//                System.out.println("inpNodeRadius " + inpNodeRadius);
//                System.out.println("ARROW_LENGTH " + ARROW_LENGTH);
//                System.out.println("GAP_BETWEEN_NODE_AND_ARROW " + GAP_BETWEEN_NODE_AND_ARROW);
//                System.out.println("inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW " + (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW));
//                System.out.println("inpNodeCriticalDistance " + inpNodeCriticalDistance);
                if (inpNodeCriticalDistance < 0.) {
                    System.out.println("Arrow is too close to input node ");
                    continue;
                }
                double outNodeCriticalDistance = distToOutNode - (outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW+5);
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

//    private int calculateArcLen

    /**
     * Called when user selects arrow tip
     * Search range is from Arc Input Node to Arc Output Node
     *
     * @param x
     * @param y
     * @return
     */
    int findArrowTipIndexOnTheSpline(int x, int y) {

        highlightedKnots = null;
        java.util.List<double[]> splineScrKnots = getSplineScrKnots();

        int arrowTipSplineScrIndex = -1;
        int firstKnotIndex = 0;
        int outputArcKnotIndex = splineScrKnots.size() - 1;
        highlightedKnotIndexes = new int[]{firstKnotIndex, outputArcKnotIndex};

        arrowTipSplineScrIndex = findArrowTipLocationUnderMouse(x, y, highlightedKnotIndexes);

        if (arrowTipSplineScrIndex >= 0) {
            Color arcMclnStateColor = getArcMclnStateColor();
            tentativeMclnArcArrow = buildArrowAtThePoint(arrowTipSplineScrIndex, arcMclnStateColor);
        } else {
            tentativeMclnArcArrow = null;
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
        java.util.List<double[]> splineScrPoints = getSplineScrPoints();
        try {
//            System.out.println("\n");
//            double[] scrPnt = splineScrPoints.get(2);
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
                // be longer then input node radius + arrow length + small gap;
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
                double outNodeCriticalDistance = distToOutNode - ( outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW);
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
                                         MclnGraphViewNode inpNode, MclnGraphViewNode optNode) {
        this.mclnArcArrow = null;

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
        }


        double[] arrowTipLocation = getSplineScrPnt(arrowTipIndex);
        double[] arrEndScrPnt = getSplineScrPnt(arrowTipIndex - 1);
        double[] arrowScrDirection = VAlgebra.subVec3(arrEndScrPnt, arrowTipLocation);
        double[] normalizedDirectionScrVector = VAlgebra.normalizeVec3(arrowScrDirection);

        mclnArcArrow = new MclnArcArrow(parentCSys, length, width, normalizedDirectionScrVector, arrowTipLocation,
                stateColor);
        return mclnArcArrow;
    }

    /*


   Src Node            prev knpb                knob                      next knob                    Dst Node
                       prev knob index          knob index                next knob index
    */
    private int findRetrievedArrowTipIndex(int arrowLength, MclnGraphViewNode inpNode,
                                           MclnGraphViewNode optNode) {

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
//                    return -1;
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
}
