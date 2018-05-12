package dsdsse.graphview;

import adf.csys.view.CSysRoundedPolylineEntity;
import adf.csys.view.CSysView;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.List;

public class MclnGraphPolylineEntity extends CSysRoundedPolylineEntity {

    private static final double GAP_BETWEEN_NODE_AND_ARROW = 8;

    private static final Color POLY_LINE_CREATION_COLOR = new Color(0xFF0000);
    private static final Color POLY_LINE_DRAWING_COLOR = Color.GRAY;

//    private static final Color POLY_LINE_DRAWING_COLORD = new Color(0xFF8000);
    // private static final Color POLY_LINE_LONGEST_SEGMENT_DRAWING_COLOR = new Color(0x0000FF);

    private MclnGraphViewNode inpNode;
    private MclnGraphViewNode outNode;
    private Color arcMclnStateColor;
    private MclnArcArrow mclnArcArrow;

    MclnGraphPolylineEntity(CSysView parentCSys, MclnGraphViewNode inpNode) {
        super(parentCSys, POLY_LINE_CREATION_COLOR);
        this.inpNode = inpNode;
    }

    public void polylineCreationCompleted(MclnGraphViewNode outNode) {
        this.outNode = outNode;
        setDrawColor(POLY_LINE_DRAWING_COLOR);
        super.polylineCreationCompleted();
    }

    void arcCreationCompleted() {

    }

    /**
     * Called when McLN model retrieved
     *
     * @param parentCSysView
     */
    public MclnGraphPolylineEntity(CSysView parentCSysView, MclnGraphViewNode inpNode, MclnGraphViewNode outNode) {
        super(parentCSysView, POLY_LINE_DRAWING_COLOR);
        this.inpNode = inpNode;
        this.outNode = outNode;
    }

    public void setHighlightLongestSegment(boolean highlightLongestSegment) {
        super.setHighlightLongestSegment(highlightLongestSegment);
    }

    public void setArcMclnStateColor(Color arcMclnStateColor) {
        this.arcMclnStateColor = arcMclnStateColor;
    }

    //
    //  K n o t s   c r e a t i o n
    //

    void createFirstCSysKnot(double[] cSysPnt) {
        super.createFirstScrKnot(cSysPnt);
    }

    void addNextCSysKnot(double[] cSysPnt) {
        super.addPoint(cSysPnt);

    }

    public boolean updateLastPoint(double[] jointPoint) {
        return super.updateLastPoint(jointPoint);
    }

//    void addNextScrKnotAndMakePreviousKnotActive(double[] scrPnt) {
//        super.addNextScrKnot(scrPnt);
//        super.makePrevKnotActive();
//    }

    //
    //   Placing Arc Arrow on the polyline
    //

    private int[] highlightedKnotIndexes;
    private double[][] highlightedKnots;

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

    /**
     * @param boundaryKnotIndexes
     * @return
     */
    private boolean checkIfArrowTipLocationCanBeFound(int[] boundaryKnotIndexes) {
        System.out.println("checkIfArrowTipLocationCanBeFound ");

//        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ?
//                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
//        double outNodeRadius = (outNode instanceof MclnPropertyView) ?
//                MclnPropertyView.RADIUS : MclnConditionView.SCREEN_RADIUS;
//
//        int nSeg = getNSegments();
//
//        int begKnotInd = boundaryKnotIndexes[0];
//        int endKnotInd = boundaryKnotIndexes[1];
//
//        int begSegInd = begKnotInd * nSeg;
//        int endSegInd = (endKnotInd * nSeg) + 0;
//        double[] prevSplineScrPnt = getSplineScrPntWithZ(begSegInd);
//        prevSplineScrPnt[2] = 0.;
//        int curSegInd = begSegInd + 1;
//
//        double[] outNodeScrPnt = outNode.getScrPnt();
//
//        double distanceFromInpNodeToArrowTip = 0.;
//        try {
//            for (int i = 1; i < endSegInd; curSegInd++, i++) {
//                System.out.println("ind i " + i+", curSegInd "+curSegInd+",   "+splineScrPoints.size());
//                double[] currSplineScrPnt = getSplineScrPntWithZ(curSegInd);
//                currSplineScrPnt[2] = 0.;
//
//                double segLength = VAlgebra.distVec3(currSplineScrPnt, prevSplineScrPnt);
//                distanceFromInpNodeToArrowTip += segLength;
//                double distToOutNode = VAlgebra.distVec3(currSplineScrPnt, outNodeScrPnt);
//
//                prevSplineScrPnt = currSplineScrPnt;
//                double inpNodeCriticalDistance = distanceFromInpNodeToArrowTip -
//                        (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW);
//                // the distance from start point to arrow tip should
//                // be longer then input node radius + arrow length + small gap;
////                System.out.println("ind " + i);
////                System.out.println("segLength " + segLength);
////                System.out.println("distanceFromInpNodeToArrowTip " + distanceFromInpNodeToArrowTip);
////                System.out.println("inpNodeRadius " + inpNodeRadius);
////                System.out.println("ARROW_LENGTH " + ARROW_LENGTH);
////                System.out.println("GAP_BETWEEN_NODE_AND_ARROW " + GAP_BETWEEN_NODE_AND_ARROW);
////                System.out.println("inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW " + (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW));
////                System.out.println("inpNodeCriticalDistance " + inpNodeCriticalDistance);
//                if (inpNodeCriticalDistance < 0.) {
//                    System.out.println("Arrow is too close to input node ");
//                    continue;
//                }
//                double outNodeCriticalDistance = distToOutNode - (outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW+5);
//                if (outNodeCriticalDistance < 0.) {
//                    System.out.println("Arrow is too close to Out node ");
//                    continue;
//                }
//                return true;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return false;
    }

    //
    //   U s e r   C r e a t e s   A r c   A r r o w
    //

    private int[] mouseTipScrLocation = {0, 0, 0};
    private int arrowSegmentIndex = -1;

    /**
     * Called when user selects arrow tip
     * Search range is from Arc Input Node to Arc Output Node
     *
     * @param x
     * @param y
     * @return
     */
    boolean findArrowTipIndexOnThePolyline(int x, int y) {

        highlightedKnots = null;
        List<int[][]> screenSegmentPoints = getScreenSegmentPoints();
        arrowSegmentIndex = findArrowTipLocationUnderMouse(x, y, screenSegmentPoints);
        if (arrowSegmentIndex > -1) {
            Color arcMclnStateColor = getArcMclnStateColor();
            mclnArcArrow = buildArrowAtThePoint(arrowSegmentIndex, mouseTipScrLocation, arcMclnStateColor);
        } else {
            mclnArcArrow = null;

        }
        return mclnArcArrow != null;
    }

    private int findArrowTipLocationUnderMouse(int x, int y, List<int[][]> screenSegmentPoints) {

        mouseTipScrLocation = null;
        int length = MclnArcArrow.DEFAULT_ARROW_LENGTH;
        int width = MclnArcArrow.DEFAULT_ARROW_WIDTH;

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
            if (i == 0) {
                segmentVector = VAlgebra.subVec3(segmentEndPoint, segmentStartPoint);
                segmentNormalizedVector = VAlgebra.normalizeVec3(segmentVector);
                nodeRadiusVector = VAlgebra.scaleVec3((inpNodeRadius + GAP_BETWEEN_NODE_AND_ARROW + MclnArcArrow.DEFAULT_ARROW_LENGTH), segmentNormalizedVector);
                segmentStartPoint = VAlgebra.addVec3(segmentStartPoint, nodeRadiusVector);

            } else if (i == lastSegmentIndex) {
                segmentVector = VAlgebra.subVec3(segmentEndPoint, segmentStartPoint);
                segmentNormalizedVector = VAlgebra.normalizeVec3(segmentVector);
                nodeRadiusVector = VAlgebra.scaleVec3(outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW, segmentNormalizedVector);
                segmentEndPoint = VAlgebra.subVec3(segmentEndPoint, nodeRadiusVector);
//                System.out.println("Dist to end "+outNodeRadius);  GAP_BETWEEN_NODE_AND_ARROW
            }

            boolean searchingInsideSegment = true;
            double step = 0.1;
            double slidingPercent = 0.;
            System.out.println("\n\n");
            while (searchingInsideSegment) {
                double[] locationInsideSegment = VAlgebra.linCom3(slidingPercent,
                        segmentStartPoint, (1.0 - slidingPercent), segmentEndPoint);
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
            mouseTipScrLocation = null;
            return -1;
        }
        mouseTipScrLocation = parentCSys.doubleVec3ToInt(minLocationInsideSegment);
        return arrowSegmentIndex;
    }

    /**
     *
     * @return
     */
    private boolean findArrowTipLocationAtTheMiddleOfTheSegment() {
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
            nodeRadiusVector = VAlgebra.scaleVec3((inpNodeRadius + GAP_BETWEEN_NODE_AND_ARROW + MclnArcArrow.DEFAULT_ARROW_LENGTH), segmentNormalizedVector);
            segmentStartPoint = VAlgebra.addVec3(segmentStartPoint, nodeRadiusVector);

        } else if (arrowSegmentIndex == lastSegmentIndex) {
            nodeRadiusVector = VAlgebra.scaleVec3(outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW, segmentNormalizedVector);
            segmentEndPoint = VAlgebra.subVec3(segmentEndPoint, nodeRadiusVector);
//                System.out.println("Dist to end "+outNodeRadius);  GAP_BETWEEN_NODE_AND_ARROW
        }

        double[] locationInsideSegment = VAlgebra.linCom3(0.5, segmentStartPoint, 0.5, segmentEndPoint);
        double[] arrowLengthVector = VAlgebra.scaleVec3(MclnArcArrow.DEFAULT_ARROW_LENGTH / 2, segmentNormalizedVector);
        double[] arrowTipInTheMidOfSegmentLocation = VAlgebra.addVec3(locationInsideSegment, arrowLengthVector);


        mouseTipScrLocation = parentCSys.doubleVec3ToInt(locationInsideSegment);


        Color arcMclnStateColor = getArcMclnStateColor();
        mclnArcArrow = buildArrowAtThePoint(arrowSegmentIndex, mouseTipScrLocation, arcMclnStateColor);


        return true;
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
        if (mclnArcArrow != null) {
            mclnArcArrow.doCSysToScreenTransformation(scr0, scale);
        }
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

    //
    //   M o v i n g   k n o t s
    //

    private RoundedJoint selectedRoundedJoint;

    protected void backupJointPoints() {
        super.backupJointPoints();
    }

    protected void restoreJointPoints() {
        super.restoreJointPoints();
    }

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
            //  mclnArcArrow.placeArrowOnScr();
        }
    }

    //
    //   D r a w i n g
    //

    public void drawPlainEntity(Graphics g) {
        super.drawPlainEntity(g);

        if (mouseTipScrLocation != null) {
            drawKnot(g, mouseTipScrLocation[0], mouseTipScrLocation[1], Color.MAGENTA);

            //
            //     Highlighting selected knots (when creating arrow knob)
            //

//            if (selectingArrowTipLocation) {
//                if (highlightArcKnotsForArrowTipSelection && highlightedKnots != null) {
////                highlightSplineSegment(g, highlightedKnots);
//                }
//                if (tentativeMclnArcArrow != null) {
//                    tentativeMclnArcArrow.drawArrow(g, selectingArrowTipLocation);
//                    mclnArcArrow = tentativeMclnArcArrow;
//                }
//                return;
//            }

            if (mclnArcArrow != null) {
                mclnArcArrow.drawArrow(g, true);
            }
        }
    }

    private void drawKnot(Graphics g, int x, int y, Color fillColor) {
        g.setColor(fillColor);
        g.drawLine(x - 1, y - 2, x + 1, y - 2);
        g.drawLine(x - 2, y - 1, x + 2, y - 1);
        g.drawLine(x - 2, y, x + 2, y);
        g.drawLine(x - 2, y + 1, x + 2, y + 1);
        g.drawLine(x - 1, y + 2, x + 1, y + 2);
    }

    public Color getArcMclnStateColor() {
        return arcMclnStateColor;
    }

    /**
     * This method construct McLN Arrow
     *
     * @param arrowSegmentIndex
     * @param stateColor
     * @return
     */
    private MclnArcArrow buildArrowAtThePoint(int arrowSegmentIndex, int[] mouseTipScrLocation, Color stateColor) {

        if (arrowSegmentIndex < 0) {
            return null;
        }

        int length = MclnArcArrow.DEFAULT_ARROW_LENGTH;
        int width = MclnArcArrow.DEFAULT_ARROW_WIDTH;

        int[][] arrowSegmentPoints = getSegmentPoints(arrowSegmentIndex);
        double[] segmentStartPoint = {arrowSegmentPoints[0][0], arrowSegmentPoints[0][1], 0.};
        double[] segmentEndPoint = {arrowSegmentPoints[1][0], arrowSegmentPoints[1][1], 0.};

        double[] arrowSegmentDirection = VAlgebra.subVec3(segmentEndPoint, segmentStartPoint);
        double[] normalizedScrDirectionVector = VAlgebra.normalizeVec3(arrowSegmentDirection);
        double[] arrowTipLocation = {mouseTipScrLocation[0], mouseTipScrLocation[1], 0.};

//        if(){
//            double distanceFromInpNodeToArrowTip = 0.;
//
//                    double distToOutNode = VAlgebra.distVec3(currSplineScrPnt, outNodeScrPnt);
//
//                    double inpNodeCriticalDistance = distanceFromInpNodeToArrowTip -
//                            (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW);
//                    // the distance from start point to arrow tip should
//                    // be longer then input node radius + arrow length + small gap;
////                System.out.println("ind " + i);
////                System.out.println("segLength " + segLength);
////                System.out.println("distanceFromInpNodeToArrowTip " + distanceFromInpNodeToArrowTip);
////                System.out.println("inpNodeRadius " + inpNodeRadius);
////                System.out.println("ARROW_LENGTH " + ARROW_LENGTH);
////                System.out.println("GAP_BETWEEN_NODE_AND_ARROW " + GAP_BETWEEN_NODE_AND_ARROW);
////                System.out.println("inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW " + (inpNodeRadius + ARROW_LENGTH + GAP_BETWEEN_NODE_AND_ARROW));
////                System.out.println("inpNodeCriticalDistance " + inpNodeCriticalDistance);
//                    if (inpNodeCriticalDistance < 0.) {
//                        System.out.println("Arrow is too close to input node ");
//                        continue;
//                    }
//                    double outNodeCriticalDistance = distToOutNode - (outNodeRadius + GAP_BETWEEN_NODE_AND_ARROW+5);
//                    if (outNodeCriticalDistance < 0.) {
//                        System.out.println("Arrow is too close to Out node ");
//                        continue;
//                    }
//        }

        MclnArcArrow mclnArcArrow = new MclnArcArrow(parentCSys, length, width, normalizedScrDirectionVector,
                arrowTipLocation, stateColor);

        return mclnArcArrow;
    }

    private void findSegmentClosestToMouseOLD(int x, int y, List<int[][]> screenSegmentPoints) {

        mouseTipScrLocation = null;

        double[] currentMouseVec = {x, y, 0.};

        int segmentIndex = -1;
        double minDistance = Double.MAX_VALUE;
        boolean startPointIsCloser = false;
        double mouseToPointDist;
        int nOfSegments = screenSegmentPoints.size();
        for (int i = 0; i < nOfSegments; i++) {
            int[][] segmentPoints = screenSegmentPoints.get(i);
            double[] segmentStartPoint = {segmentPoints[0][0], segmentPoints[0][1], 0.};
            double[] segmentEndPoint = {segmentPoints[1][0], segmentPoints[1][1], 0.};

            int tmpIndex = -1;

            mouseToPointDist = VAlgebra.distVec3(currentMouseVec, segmentStartPoint);
            if (mouseToPointDist < 200000 && mouseToPointDist < minDistance) {
                minDistance = mouseToPointDist;
                tmpIndex = i;
                startPointIsCloser = true;
            }

            mouseToPointDist = VAlgebra.distVec3(currentMouseVec, segmentEndPoint);
            if (mouseToPointDist < 200000 && mouseToPointDist < minDistance) {
                minDistance = mouseToPointDist;
                tmpIndex = i;
                startPointIsCloser = false;
            }

            if (tmpIndex >= 0) {
                segmentIndex = tmpIndex;
            }
        }

        mouseTipScrLocation = null;
        if (segmentIndex < 0) {
//            System.out.println("Min distance NOT found");
            return;
        }

//        System.out.println("Min distance found: index " + segmentIndex + ",  startPointIsCloser " + startPointIsCloser);
        int[][] segmentPoints = screenSegmentPoints.get(segmentIndex);
        int locationPointIndex = startPointIsCloser ? 0 : 1;
        mouseTipScrLocation = segmentPoints[locationPointIndex];

        segmentPoints = screenSegmentPoints.get(segmentIndex);
        double[] segmentStartPoint = {segmentPoints[0][0], segmentPoints[0][1], 0.};
        double[] segmentEndPoint = {segmentPoints[1][0], segmentPoints[1][1], 0.};

        double[] minLocationInsideSegment = null;
        boolean searchingInsideSegment = true;
        double step = 0.1;
        double slidingPercent = 0.;
        System.out.println("\n\n");
        while (searchingInsideSegment) {
            double[] locationInsideSegment = VAlgebra.linCom3(slidingPercent, segmentStartPoint, (1.0 - slidingPercent), segmentEndPoint);
            mouseToPointDist = VAlgebra.distVec3(currentMouseVec, locationInsideSegment);
            if (mouseToPointDist < 200000 && mouseToPointDist < minDistance) {
                minDistance = mouseToPointDist;
                minLocationInsideSegment = locationInsideSegment;
            }
//            System.out.println("Min local distance found: index " + slidingPercent + ",  mouseToPointDist " + mouseToPointDist +"    "+minDistance);
            slidingPercent += step;
            searchingInsideSegment = slidingPercent <= 1.0;
        }
        if (minLocationInsideSegment == null) {
//            System.out.println("Local Min not found");
            mouseTipScrLocation = null;
            return;
        }

        mouseTipScrLocation = parentCSys.doubleVec3ToInt(minLocationInsideSegment);
    }
}
