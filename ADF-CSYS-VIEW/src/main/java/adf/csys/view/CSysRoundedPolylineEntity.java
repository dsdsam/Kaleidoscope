package adf.csys.view;

import vw.valgebra.VAlgebra;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


public class CSysRoundedPolylineEntity extends BasicCSysEntity {

    private static final Color POLY_LINE_LONGEST_SEGMENT_DRAWING_COLOR = new Color(0xDD0000);

    private static final double BISECTOR_SCALE = 1;
    private static final double STRAIGHT_SEGMENT_LENGTH = 1; // was 3
    private static final double TWO_STRAIGHT_SEGMENT_LENGTH = 2;  // was 5

    private final List<RoundedJoint> cSysPolyJoints = new ArrayList();
    private final List<int[][]> screenSegmentPoints = new ArrayList();
    private boolean creationComplete;
    private boolean paintLongestSegment = true;
    private int longestSegmentIndex;

    /**
     * @param parentCSysView
     */
    public CSysRoundedPolylineEntity(CSysView parentCSysView, Color lineColor) {
        super(parentCSysView, lineColor);
    }

    public int getJointsSize() {
        return cSysPolyJoints.size();
    }

    protected void createFirstScrKnot(double[] cSysPnt) {
        cSysPolyJoints.clear();
        screenSegmentPoints.clear();
        addPoint(cSysPnt);
    }

    public void creationCompleted() {
        if (cSysPolyJoints.size() >= 2) {
            RoundedJoint prevRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 2);
            double[] prevSegmentJointCSysPoint = prevRoundedJoint.getCSysPnt();
            RoundedJoint lastRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 1);
            double[] lastSegmentJointCSysPoint = lastRoundedJoint.getCSysPnt();
            double distFromPrevToThisJointCSysPoint =
                    VAlgebra.distVec3(prevSegmentJointCSysPoint, lastSegmentJointCSysPoint);
            if (distFromPrevToThisJointCSysPoint < 1) {
                removeLastPoint();
            }
        }
        creationComplete = true;
        findAndHighlightLongestSegment();
    }

    /**
     *
     */
    void findAndHighlightLongestSegment() {
        if (cSysPolyJoints.size() < 2) {
            return;
        }
        int size = cSysPolyJoints.size();
        RoundedJoint prevRoundedJoint = cSysPolyJoints.get(0);
        int maxLengthSegmentIndex = 0;
        double maxSegmentLength = 0;
        for (int i = 1; i < size; i++) {
            RoundedJoint currentRoundedJoint = cSysPolyJoints.get(i);
            double[] segmentVector = VAlgebra.subVec3(prevRoundedJoint.getCSysPnt(), currentRoundedJoint.getCSysPnt());
            double segmentLength = VAlgebra.vec3Len(segmentVector);
            if (segmentLength > maxSegmentLength) {
                maxSegmentLength = segmentLength;
                maxLengthSegmentIndex = i - 1;
            }
            prevRoundedJoint = currentRoundedJoint;
        }
        this.longestSegmentIndex = maxLengthSegmentIndex;
    }


    /**
     * The argument CSysPoint is the JointPoint and the  end of Segment
     *
     * @param cSysPoint
     */
    public void addPoint(double[] cSysPoint) {
        RoundedJoint prevRoundedJoint = null;
        boolean shortSegmentBothEndsAreNotRounded = false;
        boolean prevSegmentIsSemiShortWithStartingRounded = false;
        boolean prevSegmentIsSemiShortWithEndingRounded = false;

        // By calling this method we finish current segment
        // cewation and start new segment
        // first we calculate current segment length
        // end prepare flags that indicate segment type
        if (cSysPolyJoints.size() >= 2) {
            prevRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 2);

            double[] prevSegmentJointCSysPoint = prevRoundedJoint.getCSysPnt();
            double[] thisSegmentJointCSysPoint = cSysPoint;
            double distFromPrevToThisJointCSysPoint =
                    VAlgebra.distVec3(prevSegmentJointCSysPoint, thisSegmentJointCSysPoint);

            if (distFromPrevToThisJointCSysPoint < STRAIGHT_SEGMENT_LENGTH) {
                shortSegmentBothEndsAreNotRounded = true;
            } else if (distFromPrevToThisJointCSysPoint < TWO_STRAIGHT_SEGMENT_LENGTH) {
                prevSegmentIsSemiShortWithEndingRounded = prevRoundedJoint.isShortSegmentBothEndsAreNotRounded();
                prevSegmentIsSemiShortWithStartingRounded = !prevSegmentIsSemiShortWithEndingRounded;
            }
        }
        if (cSysPolyJoints.size() > 0) {
            prevRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 1);
            prevRoundedJoint.setShortSegmentBothEndsAreNotRounded(shortSegmentBothEndsAreNotRounded);
        }
        boolean straightSegment = cSysPolyJoints.size() > 0;
        RoundedJoint roundedJoint =
                new RoundedJoint(parentCSys, prevRoundedJoint, cSysPoint, getDrawColor(), straightSegment, true);
        roundedJoint.setPrevSegmentIsSemiShortWithStartingRounded(prevSegmentIsSemiShortWithStartingRounded);
        cSysPolyJoints.add(roundedJoint);
        if (cSysPolyJoints.size() >= 3) {
            recalculateAllJoints();
            doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale);
        }
    }


    /**
     * This method update last point after it was moved
     * The last pont could be the second, the third and so on.
     *
     * @param jointPoint
     * @return
     */
    protected boolean updateLastPoint(double[] jointPoint) {
        RoundedJoint lastRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 1);
        lastRoundedJoint.updateLastPoint(jointPoint);

        boolean straightSegment = true;
        double[] prevSegmentJointCSysPoint = lastRoundedJoint.getPrevSegmentJointCSysPoint();
        if (prevSegmentJointCSysPoint != null) {
            double[] thisSegmentJointCSysPoint = lastRoundedJoint.getCSysPnt();
            double distFromPrevToThisJointCSysPoint =
                    VAlgebra.distVec3(prevSegmentJointCSysPoint, thisSegmentJointCSysPoint);

            straightSegment = distFromPrevToThisJointCSysPoint < STRAIGHT_SEGMENT_LENGTH;
            lastRoundedJoint.setStraightSegment(straightSegment);
            boolean semiStraightSegment = distFromPrevToThisJointCSysPoint > STRAIGHT_SEGMENT_LENGTH &&
                    distFromPrevToThisJointCSysPoint < TWO_STRAIGHT_SEGMENT_LENGTH;
            lastRoundedJoint.setSemiStraightSegment(semiStraightSegment);
        }

        lastRoundedJoint.calculateRoundingScreenPoints();
        if (straightSegment || cSysPolyJoints.size() <= 2) {
            // second point can be moved
            // but its joint cannot be recalculated
            doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale);
            return false;
        }
        recalculateAllJoints();
        doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale);
        return true;
    }

    /**
     * Called when poly-line creation is completed to remove dragged
     * last point under  mouse tip  when it is not needed because it
     * is very close to previous joint point.
     *
     * @return
     */
    private boolean removeLastPoint() {
        if (cSysPolyJoints.size() == 2) {
            cSysPolyJoints.clear();
            screenSegmentPoints.clear();
            return false;
        } else {
            cSysPolyJoints.remove(cSysPolyJoints.size() - 1);
            return true;
        }
    }

    /**
     * Called when lastly created joint needs
     * to be removed - as undo operation.
     * The dragged last under mouse tip point
     * is then connected to joint previous to
     * the removed one.
     *
     * @return
     */
    public boolean removeLastSegment() {
        if (cSysPolyJoints.size() == 2) {
            cSysPolyJoints.clear();
            screenSegmentPoints.clear();
            return false;
        } else {
            RoundedJoint removedLastRoundedJoint = cSysPolyJoints.remove(cSysPolyJoints.size() - 1);
            double[] jointPoint = removedLastRoundedJoint.getCSysPnt();
            updateLastPoint(jointPoint);
            return true;
        }
    }

    public RoundedJoint getJointUnderMouse(Point p) {
        int size = cSysPolyJoints.size();
        for (int i = 0; i < size; i++) {
            RoundedJoint roundedJoint = cSysPolyJoints.get(i);
            if (roundedJoint.isJointUnderMouse(p)) {
                return roundedJoint;
            }
        }
        return null;
    }

    public double[] getPoint(int i) {
        return cSysPolyJoints.get(i).getCSysPnt();
    }

    public void updatePolylineOnJointDragged(RoundedJoint roundedJoint, Point p) {
        roundedJoint.updateJointPointOnJointMoved(p);
        if (cSysPolyJoints.size() >= 3) {
            recalculateAllJoints();
        }
        findAndHighlightLongestSegment();
    }

    /**
     * Called when a joint is added, moved, or removed
     */
    private void recalculateAllJoints() {
        int size = cSysPolyJoints.size();
        for (int i = 2; i < size; i++) {
            RoundedJoint roundedJoint = cSysPolyJoints.get(i);
            roundedJoint.calculateRounding();
        }
    }

    //
    //
    //   T r a n s f o r m a t i o n s
    //

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        screenSegmentPoints.clear();
        int size = cSysPolyJoints.size();
        if (size == 0) {
            return;
        }

        for (int i = 2; i < size; i++) {
            RoundedJoint roundedJoint = cSysPolyJoints.get(i);
            roundedJoint.calculateRounding();
            roundedJoint.calculateRoundingScreenPoints();
        }

        RoundedJoint prevRoundedJoint = cSysPolyJoints.get(0);
        for (int i = 1; i < size; i++) {
            RoundedJoint currRoundedJoint = cSysPolyJoints.get(i);
            prevRoundedJoint.calculateRoundingScreenPoints();
            double[] prevSegmentJointCSysPoint = prevRoundedJoint.getCSysPnt();
            double[] currSegmentJointCSysPoint = currRoundedJoint.getCSysPnt();

            double[] prevSegTangentCSysPoint = null;
            double[] currSegTangentCSysPoint = null;
            int[] prevScrPoint;
            int[] currScrPoint;
            currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
            if (i == 1) { // first segment
                prevScrPoint = super.doCSysToScreenTransformation(scr0, scale, prevSegmentJointCSysPoint);
//                currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
                int[][] segScrPnt = new int[][]{prevScrPoint, currScrPoint, currScrPoint};
                screenSegmentPoints.add(segScrPnt);

            } else { // other segments

                double distFromPrevToThisJointCSysPoint =
                        VAlgebra.distVec3(prevSegmentJointCSysPoint, currSegmentJointCSysPoint);

                if (distFromPrevToThisJointCSysPoint < STRAIGHT_SEGMENT_LENGTH) {
                    // last joint point represents last segment, it is always straight
                    prevScrPoint = super.doCSysToScreenTransformation(scr0, scale, prevSegmentJointCSysPoint);
                    currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
                    int[][] segScrPnt = new int[][]{prevScrPoint, currScrPoint, currScrPoint};
                    screenSegmentPoints.add(segScrPnt);

                } else if (currRoundedJoint.isPrevSegmentIsSemiShortWithStartingRounded()) {
                    /*
                       We are here when prev segment does not have enough length to
                       fit both Starting and Ending rounding. So, as it has its Starting
                       point rounded, but its Ending point not rounded, this segment
                       does not make its Start point rounded.
                     */
                    prevScrPoint = super.doCSysToScreenTransformation(scr0, scale, prevSegmentJointCSysPoint);
                    currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
                    int[][] segScrPnt = new int[][]{prevScrPoint, currScrPoint, currScrPoint};
                    screenSegmentPoints.add(segScrPnt);

                } else if (prevRoundedJoint.isShortSegmentBothEndsAreNotRounded() || currRoundedJoint.isStraightSegment()) {
                    prevScrPoint = super.doCSysToScreenTransformation(scr0, scale, prevSegmentJointCSysPoint);
                    currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
                    int[][] segScrPnt = new int[][]{prevScrPoint, currScrPoint, currScrPoint};
                    screenSegmentPoints.add(segScrPnt);

                } else {

                    // do rounding, adding tangent points to screen points array

                    prevSegTangentCSysPoint = currRoundedJoint.getPrevSegmentTangentCSysPoint();
                    if (prevSegTangentCSysPoint == null) {
                        prevSegTangentCSysPoint = prevSegmentJointCSysPoint;
                    }
                    int[] prevSegPrevScrPoint = super.doCSysToScreenTransformation(scr0, scale, prevSegTangentCSysPoint);
                    int[][] prevSegScrPoint = screenSegmentPoints.get(screenSegmentPoints.size() - 1);
                    prevSegScrPoint[1] = prevSegPrevScrPoint;

                    currSegTangentCSysPoint = currRoundedJoint.getThisSegmentTangentCSysPoint();
                    if (currSegTangentCSysPoint == null) {
                        currSegTangentCSysPoint = prevSegmentJointCSysPoint;
                    }
                    prevScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegTangentCSysPoint);
                    currScrPoint = super.doCSysToScreenTransformation(scr0, scale, currSegmentJointCSysPoint);
                    int[][] segScrPnt = new int[][]{prevScrPoint, currScrPoint, currScrPoint};
                    screenSegmentPoints.add(segScrPnt);
                }
            }
            prevRoundedJoint = currRoundedJoint;
        }
    }

    //
    //   D r a w i n g
    //

    public void drawPlainEntity(Graphics g) {
        draw(g);
    }

    @Override
    public void draw(Graphics g) {
        if (parentCSys == null || screenSegmentPoints.size() < 1) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) g;
        // preserving current rendering key
        Object oldRenderingKey = graphics2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        // setting antialias
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        Stroke stroke = new BasicStroke(1f);
//        graphics2D.setStroke(stroke);

        Color roundColor = getCurrentColor();
        g.setColor(roundColor);

        // drawing segments
        int[][] prevSegmentScrPoints = screenSegmentPoints.get(0);
        for (int i = 0; i < screenSegmentPoints.size(); i++) {
            int[][] currentSegmentScrPoints = screenSegmentPoints.get(i);
            RoundedJoint currentRoundedJoint = cSysPolyJoints.get(i);

            // highlighting last short segment while creating
            if (!isSelected() && !creationComplete) {
                if (cSysPolyJoints.size() >= 2 && i == (screenSegmentPoints.size() - 1)) {
                    RoundedJoint lastRoundedJoint = cSysPolyJoints.get(cSysPolyJoints.size() - 1);
                    if (lastRoundedJoint.isStraightSegment()) {
                        g.setColor(Color.GREEN);
                    }
                    if (lastRoundedJoint.isSemiStraightSegment()) {
                        g.setColor(Color.RED);
                    }
                }
            }
            if (paintLongestSegment && creationComplete && longestSegmentIndex == i) {
                g.setColor(POLY_LINE_LONGEST_SEGMENT_DRAWING_COLOR);
            }

            // drawing segment in specified drawing color or length indicating color (green/reg)
            g.drawLine(currentSegmentScrPoints[0][0], currentSegmentScrPoints[0][1],
                    currentSegmentScrPoints[1][0], currentSegmentScrPoints[1][1]);
//            Shape segment = new Line2D.Double(currentSegmentScrPoints[0][0], currentSegmentScrPoints[0][1],
//                    currentSegmentScrPoints[1][0], currentSegmentScrPoints[1][1]);
//            graphics2D.draw(segment);

            // drawing lines from tangent to joint points
//            if (isSelected() && creationComplete) {
//                drawHiddenLines(graphics2D, prevSegmentScrPoints, currentSegmentScrPoints);
//                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldRenderingKey);
//                boolean selected = currentRoundedJoint.isSelected();
//                Color dotColor = selected ? Color.MAGENTA : Color.BLUE;
//                drawKnot(g, prevSegmentScrPoints[2][0], prevSegmentScrPoints[2][1], dotColor);
//                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            }

            prevSegmentScrPoints = currentSegmentScrPoints;
            g.setColor(roundColor);
        }
        // Draw rounding
//        g.setColor(Color.GRAY);
        for (RoundedJoint roundedJoint : cSysPolyJoints) {
            if ((!(roundedJoint.prevRoundedJoint != null &&
                    roundedJoint.prevRoundedJoint.isShortSegmentBothEndsAreNotRounded())) &&
                    !roundedJoint.isPrevSegmentIsSemiShortWithStartingRounded()) {
                roundedJoint.drawPlainEntity(g);
            }
        }

        // restoring old rendering key
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldRenderingKey);
    }

    /**
     * @param g
     * @param prevSegment
     * @param segment
     */
    private void drawHiddenLines(Graphics2D g, int[][] prevSegment, int[][] segment) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(segment[1][0], segment[1][1], segment[2][0], segment[2][1]);
        if (prevSegment != null) {
            g.drawLine(segment[0][0], segment[0][1], prevSegment[2][0], prevSegment[2][1]);
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

    // ----------------------------------------------------------------------
    //                        R o u n d e d   J o i n t
    //   This class makes poly line to have segment connection rounded points
    // ----------------------------------------------------------------------

    public static class RoundedJoint extends CSysPointEntity {

        private static final double TWO_PI = Math.PI + Math.PI;
        private static final int SCREEN_OUTLINE_HALF_SIZE = 6;

        private final RoundedJoint prevRoundedJoint;

        // rounding calculation data

        private double[] v1;
        private double[] v2;
        private double[] prevSegmentDirection;
        private double[] thisSegmentDirection;
        private double[] prevSegmentTangentCSysPoint;
        private double[] thisSegmentTangentCSysPoint;
        private double[] bisectorPoint;
        private double roundingRadius;

        private List<double[]> roundingCSysPointsList = new ArrayList();
        private List<int[]> roundingScrPointsList = new ArrayList();
        private final List<int[]> screenCenterAnTangPoints = new ArrayList();
        private boolean straightSegment;
        private boolean semiStraightSegment;
        private boolean shortSegmentBothEndsAreNotRounded;
        private boolean prevSegmentIsSemiShortWithStartingRounded;
        private Rectangle scrOutline = new Rectangle(0, 0, 0, 0);

        /**
         * @param parentCSys
         * @param prevRoundedJoint
         * @param origPnt
         * @param drawColor
         * @param drawAsDot
         */
        RoundedJoint(CSysView parentCSys, RoundedJoint prevRoundedJoint, double[] origPnt,
                     Color drawColor, boolean straightSegment, boolean drawAsDot) {
            super(parentCSys, origPnt, drawColor, drawAsDot);
            this.prevRoundedJoint = prevRoundedJoint;
            this.straightSegment = straightSegment;
        }

        public boolean isStraightSegment() {
            return straightSegment;
        }

        /**
         * @param straightSegment
         */
        public void setStraightSegment(boolean straightSegment) {
            this.straightSegment = straightSegment;
        }

        public boolean isSemiStraightSegment() {
            return semiStraightSegment;
        }

        /**
         * This flag is used to highlight active segment when its length is R < L < 2R
         *
         * @param semiStraightSegment
         */
        public void setSemiStraightSegment(boolean semiStraightSegment) {
            this.semiStraightSegment = semiStraightSegment;
        }

        public boolean isShortSegmentBothEndsAreNotRounded() {
            return shortSegmentBothEndsAreNotRounded;
        }

        public boolean isPrevSegmentShort() {
            return prevRoundedJoint != null ? prevRoundedJoint.isShortSegmentBothEndsAreNotRounded() : false;
        }

        public void setShortSegmentBothEndsAreNotRounded(boolean straightSegment) {
            this.shortSegmentBothEndsAreNotRounded = straightSegment;
        }

        /**
         * The flag is checked when making decision
         * to round or not this segment start joint
         *
         * @return
         */
        public boolean isPrevSegmentIsSemiShortWithStartingRounded() {
            return prevSegmentIsSemiShortWithStartingRounded;
        }

        /**
         * This flag is set when fragment creation is finalized by adding new segment ending point.
         * This flag means that previous segment is semi-short - can fit its rounding
         * at the beginning of segment. So, this segment should not
         * make rounding at the end of prev and the beginning this segment
         *
         * @param prevSegmentIsSemiShortWithStartingRounded
         */
        public void setPrevSegmentIsSemiShortWithStartingRounded(boolean prevSegmentIsSemiShortWithStartingRounded) {
            this.prevSegmentIsSemiShortWithStartingRounded = prevSegmentIsSemiShortWithStartingRounded;
        }

        void updateLastPoint(double[] cSysPoint) {
            moveEntityActivePointTo(cSysPoint);
            int[] scrPoint = super.doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale, cSysPoint);
            scrOutline = calculateScreenOutline(scrPoint);
        }

        private void updateJointPointOnJointMoved(Point p) {
            int[] scrPoint = {p.x, p.y, 0};
            double[] cSysPoint = parentCSys.screenPointToCSysPoint(p.x, p.y);
            moveEntityActivePointTo(cSysPoint);
            scrOutline = calculateScreenOutline(scrPoint);
        }

        boolean isJointUnderMouse(Point mousePoint) {
            return scrOutline.contains(mousePoint);
        }

        public double[] getPrevSegmentJointCSysPoint() {
            return prevRoundedJoint != null ? prevRoundedJoint.getCSysPnt() : null;
        }

        public double[] getPrevSegmentTangentCSysPoint() {
            return prevSegmentTangentCSysPoint;
        }

        public double[] getThisSegmentTangentCSysPoint() {
            return thisSegmentTangentCSysPoint;
        }

        void calculateRoundingScreenPoints() {
            doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale);
        }

        @Override
        public void doCSysToScreenTransformation(int[] scr0, double scale) {
            roundingScrPointsList.clear();
            for (double[] currentCSysPoint : roundingCSysPointsList) {
                int[] scrPnt = super.doCSysToScreenTransformation(scr0, scale, currentCSysPoint);
                roundingScrPointsList.add(scrPnt);
            }

            // calculating outline
            double[] cSysPoint = getCSysPnt();
            int[] scrPoint = super.doCSysToScreenTransformation(scr0, scale, cSysPoint);
            calculateScreenOutline(scrPoint);
        }

        @Override
        public void drawPlainEntity(Graphics g) {
            draw(g);
        }

        @Override
        public void draw(Graphics g) {
            // this is used for debugging
//            for (int i = 0; i < screenCenterAnTangPoints.size(); i++) {
//                int[] scrPoint = screenCenterAnTangPoints.get(i);
//                g.drawLine(scrPoint[0] - 1, scrPoint[1], scrPoint[0] + 1, scrPoint[1]);
//                g.drawLine(scrPoint[0], scrPoint[1] - 1, scrPoint[0], scrPoint[1] + 1);
//                g.drawLine(scrPoint[0], scrPoint[1], scrPoint[0], scrPoint[1]);
//            }
            // paint joint point outline
//            g.setColor(Color.RED);
//            g.drawRect(scrOutline.x, scrOutline.y, scrOutline.width, scrOutline.height);

            if (roundingScrPointsList.size() == 0) {
                return;
            }

            //
            //  draw rounding
            //

            Color roundColor = getCurrentColor();
            g.setColor(roundColor);
            int[] prevPoint = roundingScrPointsList.get(0);
            for (int i = 1; i < roundingScrPointsList.size(); i++) {
                int[] currentPoint = roundingScrPointsList.get(i);
                g.drawLine(prevPoint[0], prevPoint[1], currentPoint[0], currentPoint[1]);
                prevPoint = currentPoint;
            }
        }

        private Rectangle calculateScreenOutline(int[] scrPoint) {
            Rectangle scrOutline = new Rectangle(scrPoint[0] - SCREEN_OUTLINE_HALF_SIZE, scrPoint[1] - SCREEN_OUTLINE_HALF_SIZE,
                    2 * SCREEN_OUTLINE_HALF_SIZE, 2 * SCREEN_OUTLINE_HALF_SIZE);
            return scrOutline;
        }
        //
        //   C a l c u l a t i n g   r o u n d i n g
        //

        void calculateRounding() {
            double[] prevSegmentJointCSysPoint = getPrevSegmentJointCSysPoint();
            double[] thisSegmentJointCSysPoint = getCSysPnt();
            double distFromPrevToThisJointCSysPoint =
                    VAlgebra.distVec3(prevSegmentJointCSysPoint, thisSegmentJointCSysPoint);
            if (distFromPrevToThisJointCSysPoint < STRAIGHT_SEGMENT_LENGTH) {
                roundingScrPointsList.clear();
                roundingCSysPointsList.clear();
                return;
            }

            calculateJointVectors();
            roundingRadius = calculateTangentPoints(v1, v2);
            calculateArcPoints(prevSegmentTangentCSysPoint, thisSegmentTangentCSysPoint, bisectorPoint, roundingRadius);
        }

        //   private stuff

        private void calculateJointVectors() {
            double[] prevSegmentStartCSysPnt = prevRoundedJoint.prevRoundedJoint.getCSysPnt();
            double[] prevSegmentEndCSysPnt = prevRoundedJoint.getCSysPnt();
            double[] thisSegmentEndCSysPnt = getCSysPnt();
            v1 = VAlgebra.subVec3(prevSegmentEndCSysPnt, prevSegmentStartCSysPnt);
            v2 = VAlgebra.subVec3(thisSegmentEndCSysPnt, prevSegmentEndCSysPnt);
        }

        //
        //   Calculating Tangent Points
        //

        private double calculateTangentPoints(double[] prevSegmentVector, double[] thisSegmentVector) {

            double[] cSysPnt = prevRoundedJoint.getCSysPnt();
            double dot;

            screenCenterAnTangPoints.clear();
            // Calculating prev segment direction
            prevSegmentDirection = VAlgebra.normalizeVec3(prevSegmentVector);
            // Calculating this segment direction
            thisSegmentDirection = VAlgebra.normalizeVec3(thisSegmentVector);

            // Calculating bisector point
            double[] bisectorDirection = VAlgebra.linCom3(-0.5, prevSegmentDirection, 0.5, thisSegmentDirection);
            bisectorDirection = VAlgebra.normalizeVec3(bisectorDirection);
            double[] bisectorVector = VAlgebra.scaleVec3(BISECTOR_SCALE, bisectorDirection);
            bisectorPoint = VAlgebra.addVec3(cSysPnt, bisectorVector);

            // calculating prev segment tangent point
            dot = VAlgebra.dot3(bisectorVector, prevSegmentDirection);
            double[] prevSegmentTangentCSysVector = VAlgebra.scaleVec3(dot, prevSegmentDirection);
            prevSegmentTangentCSysPoint = VAlgebra.addVec3(cSysPnt, prevSegmentTangentCSysVector);

            // calculating this segment tangent point
            dot = VAlgebra.dot3(bisectorVector, thisSegmentDirection);
            double[] thisSegmentTangentCSysVector = VAlgebra.scaleVec3(dot, thisSegmentDirection);
            thisSegmentTangentCSysPoint = VAlgebra.addVec3(cSysPnt, thisSegmentTangentCSysVector);

            // calculating radius
            double[] startingRadiusVector = VAlgebra.subVec3(prevSegmentTangentCSysPoint, bisectorPoint);
            double roundingRadius = VAlgebra.vec3Len(startingRadiusVector);
            return roundingRadius;
        }

        //
        //   Calculating Rounding Points
        //

        private boolean calculateArcPoints(double[] prevSegmentTangentCSysPoint, double[] thisSegmentTangentCSysPoint,
                                           double[] circleCenterPoint, double radius) {
            double[] prevFromCenterToTangentPointVector = VAlgebra.subVec3(prevSegmentTangentCSysPoint, circleCenterPoint);
            double[] thisFromCenterToTangentPointVector = VAlgebra.subVec3(thisSegmentTangentCSysPoint, circleCenterPoint);

            double prevFromCenterToTangentPointVectorAngle =
                    Math.atan2(prevFromCenterToTangentPointVector[1], prevFromCenterToTangentPointVector[0]);
            double thisFromCenterToTangentPointVectorAngle =
                    Math.atan2(thisFromCenterToTangentPointVector[1], thisFromCenterToTangentPointVector[0]);

            prevFromCenterToTangentPointVectorAngle += TWO_PI;
            thisFromCenterToTangentPointVectorAngle += TWO_PI;

            boolean angleSwapped = false;
            if (prevFromCenterToTangentPointVectorAngle > thisFromCenterToTangentPointVectorAngle) {
                angleSwapped = true;
            }
            double minAngle = Math.min(prevFromCenterToTangentPointVectorAngle, thisFromCenterToTangentPointVectorAngle);
            double maxAngle = Math.max(prevFromCenterToTangentPointVectorAngle, thisFromCenterToTangentPointVectorAngle);

            boolean check = false;
            if ((maxAngle - minAngle) >= Math.PI) {
                double tmp = maxAngle;
                maxAngle = minAngle;
                minAngle = tmp - 2 * Math.PI;
                check = true;
            }

            double delta = Math.PI / 90;
            roundingCSysPointsList.clear();
            for (double i = minAngle; i < maxAngle - (delta * 2); i += delta) {
                double currentAngle = i;
                double arcX = circleCenterPoint[0] + radius * Math.cos(currentAngle);
                double arcY = circleCenterPoint[1] + radius * Math.sin(currentAngle);
                double[] circlePoint = VAlgebra.initVec3(arcX, arcY, 0);
                roundingCSysPointsList.add(circlePoint);
            }
            if (check) {
                if (!angleSwapped) {
                    roundingCSysPointsList.add(VAlgebra.copyVec3(prevSegmentTangentCSysPoint));
                } else {
                    roundingCSysPointsList.add(VAlgebra.copyVec3(thisSegmentTangentCSysPoint));
                }
            } else {
                if (angleSwapped) {
                    roundingCSysPointsList.add(VAlgebra.copyVec3(prevSegmentTangentCSysPoint));
                } else {
                    roundingCSysPointsList.add(VAlgebra.copyVec3(thisSegmentTangentCSysPoint));
                }
            }
            return true;
        }
    }
}