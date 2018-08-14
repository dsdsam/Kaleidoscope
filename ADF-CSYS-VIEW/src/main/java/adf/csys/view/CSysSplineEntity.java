package adf.csys.view;


import mathematics.spline.MathSpline3D;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 6/16/13
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSysSplineEntity extends BasicCSysEntity {

    protected static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    protected static final Color DEFAULT_KNOT_COLOR = Color.DARK_GRAY;

    private MathSpline3D mathSpline3D = new MathSpline3D(MathSpline3D.SPL_TAN_BOTH, -1, -1);
    private CSysView parentCSysView;

    // cSys knots
    private final List<double[]> splineCSysKnots = new ArrayList();
    // cSys spline lines
    private final List<double[]> splineCSysPoints = new ArrayList<>();

    // scr knots
    private List<double[]> splineScrKnots = new ArrayList();
    // scr spline lines
    protected List<double[]> splineScrPoints = new ArrayList();


    // of screen points
    // Creation
    private final double eps = 0.00001;
    private final double eps_sqr = eps * eps;

    // Drawing constants
    private final int dotR = 2, dotD = 2 * dotR;
    private final int pntRectW2 = 5, pntRectW = 2 * pntRectW2;
    private final int pntRectH2 = 5, pntRectH = 2 * pntRectH2;

    // Display
    private Color knotColor = Color.BLACK;

    protected int selectedKnotIndex = -1;
    private int activeKnotInd = -1;  // used upon creation

    // knots
    private boolean drawKnots = true;
    private boolean allKnotsSelected;
    private boolean drawKnotBoxes;

    // thread
    protected boolean threadSelected;

    //
    //    C o n s t r u c t i n g
    //

    public CSysSplineEntity(CSysView parentCSysView) {
        super(parentCSysView);
        this.parentCSysView = parentCSysView;
    }

    @Override
    public CSysSplineEntity clone() throws CloneNotSupportedException {
        CSysSplineEntity cSysSplineEntityClone = null;
        try {
            cSysSplineEntityClone = (CSysSplineEntity) super.clone();

            cSysSplineEntityClone.splineScrKnots = new ArrayList();
            int size = this.splineScrKnots.size();
            for (int i = 0; i < size; i++) {
                cSysSplineEntityClone.splineScrKnots.add(VAlgebra.copyVec3(this.splineScrKnots.get(i)));
            }

            cSysSplineEntityClone.splineScrPoints = new ArrayList();
            size = this.splineScrPoints.size();
            for (int i = 0; i < size; i++) {
                cSysSplineEntityClone.splineScrPoints.add(VAlgebra.copyVec3(this.splineScrPoints.get(i)));
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw e;
        } finally {
            return cSysSplineEntityClone;
        }
    }

    //
    //  k n o t s    c r e a t i o n
    //

    protected void createFirstScrKnot(double[] scrPnt) {
        splineCSysKnots.clear();
        addScrKnot(scrPnt);
    }

    protected void addNextScrKnot(double[] scrPnt) {
        addScrKnot(scrPnt);
    }

    public void makeFirstKnotActive() {
        activeKnotInd = 0;
    }

    public void makeLastKnotActive() {
        activeKnotInd = mathSpline3D.getNKnots() - 1;
    }

    public void makePrevKnotActive() {
        activeKnotInd = mathSpline3D.getNKnots() - 2;
    }

    public void setNoActiveKnots() {
        activeKnotInd = -1;
    }

    /**
     * @param scrPnt
     */
    private void addScrKnot(double[] scrPnt) {
        if (splineScrKnots.size() >= 2) {
            double[] lastScrKnot = splineScrKnots.get(splineScrKnots.size() - 1);
            double[] prevScrKnot = splineScrKnots.get(splineScrKnots.size() - 2);
            double[] clonedLastScrKnot = VAlgebra.initVec3(lastScrKnot[0], lastScrKnot[1], 0);
            double[] clonedPrevScrKnot = VAlgebra.initVec3(prevScrKnot[0], prevScrKnot[1], 0);
            double dist = VAlgebra.distVec3(clonedPrevScrKnot, clonedLastScrKnot);
            if (dist < 5) {
//                System.out.println("CSysSplineEntity Ignoring knot location " + dist);
                return;
            }
        }
        double[] cSysPnt = parentCSys.screenPointToCSysPoint(scrPnt);
        splineCSysKnots.add(cSysPnt);
        mathSpline3D.setKnots(splineCSysKnots);
        calculate();
    }

    protected List<double[]> getSplineScrKnots() {
        return splineScrKnots;
    }

    protected List<double[]> getSplineScrPoints() {
        return splineScrPoints;
    }


    //
    //  k n o t    d e l e t i o n
    //

    public void deleteCSysKnot(int ind) {
        int nKnots = splineCSysKnots.size();
        if (nKnots <= 0 || ind < 0 && ind >= nKnots) {
            return;
        }
        splineCSysKnots.remove(ind);
        mathSpline3D.setKnots(splineCSysKnots);
    }

    public int deleteLastCSysKnot() {
        int nKnots = getNKnots();
        if (nKnots <= 0) {
            return 0;
        }

        if (nKnots > 2) {
            deleteCSysKnot(nKnots - 2);

        } else {
            deleteCSysKnot(nKnots - 1);
            deleteCSysKnot(0);
        }

        activeKnotInd = getNKnots() - 1;
        return getNKnots();
    }

    public void deleteAllCSysPoints() {
        mathSpline3D.clearKnots();
        activeKnotInd = -1;
    }

    //
    //    W o r k i n g    w i t h     k n o t s
    //

    public void setDrawKnots(boolean status) {
        drawKnots = status;
    }

    public void setDrawKnotBoxes(boolean status) {
        drawKnotBoxes = status;
    }

    public void setAllKnotsSelected(boolean status) {
        this.allKnotsSelected = status;
    }

    public final List<double[]> getSplineCSysKnots() {
        return new ArrayList(splineCSysKnots);
    }

    public final void setSplineCSysKnots(List<double[]> splineCSysKnots) {
        this.splineCSysKnots.clear();
        this.splineCSysKnots.addAll(splineCSysKnots);
        mathSpline3D.setKnots(this.splineCSysKnots);
        calculate();
    }


    protected int getNKnots() {
        return mathSpline3D.getNKnots();
    }

    //
    //    u p d a t e   o p e r a t i o n s
    //

    public void updateActiveScrPoint(double x, double y, double z) {
        double[] scrPnt = {x, y, z};
        moveEntityActivePointTo(scrPnt);
    }

    /**
     * @param cSysPnt
     */

    public void moveEntityActivePointTo(double[] cSysPnt) {
        updateKnotCSysLocation(activeKnotInd, cSysPnt);
    }

    /**
     * @param cSysPnt
     */
    public void updateFirstKnotLocation(double[] cSysPnt) {
        updateKnotCSysLocation(0, cSysPnt);
    }

    /**
     * @param cSysPnt
     */
    public void updateLastKnotLocation(double[] cSysPnt) {
        int lastKnotIndex = getNKnots() - 1;
        if (lastKnotIndex < 1) {
            return;
        }
        updateKnotCSysLocation(lastKnotIndex, cSysPnt);
    }

    public void moveSelectedKnotCSysPointTo(int selectedKnotIndex, double[] scrPnt) {
        double[] cSysPnt = parentCSys.screenPointToCSysPoint(scrPnt);
        updateKnotCSysLocation(selectedKnotIndex, cSysPnt);
    }

    /**
     * @param ind
     * @param cSysPnt
     */
    public void updateKnotCSysLocation(int ind, double[] cSysPnt) {
        if (ind < 0 || ind >= getNKnots()) {
            return;
        }
//        splineCSysKnots.set(ind, cSysPnt);
        mathSpline3D.updateKnot(ind, cSysPnt); // !!!!!!!!!
        calculate();
    }

    //
    //   c a l c u l a t i o n
    //

    /**
     * This method should be called after spline knots initialized
     * or new knots added
     */
    public void calculate() {
//        mathSpline3D.interpolate();
//        System.out.println("calculate.Spline Entity  cSys points " + splineCSysPoints.size());
//        splineCSysPoints = mathSpline3D.sketchSpline(splineCSysPoints);
        mathSpline3D.sketchSpline(splineCSysPoints);
        sketchScrSpline();
    }

    /**
     *
     */
    public void sketchScrSpline() {
//        splineCSysPoints = mathSpline3D.sketchSpline(splineCSysPoints);
//        splineScrPoints.clear();
//        int nOfCSysPoints = splineCSysPoints.size();
//        System.out.println("sketchScrSpline.Spline Entity cSys points " + splineCSysPoints.size());
//        for (int i = 0; i < nOfCSysPoints; i++) {
//            splineScrPoints.add(new double[]{0, 0, 0});
//        }
        doCSysToScreenTransformation(parentCSys.scr0, parentCSys.minScale);
    }


    public void setThreadSelected(boolean threadSelected) {
        this.threadSelected = threadSelected;
    }


    protected int findKnot(int x, int y) {
        Point curPnt;
        double[] scrPnt = new double[3];
        for (int i = 0; i < getNKnots(); i++) {
            double[] splineCSysKnot = splineCSysKnots.get(i);
            scrPnt = parentCSys.cSysPointToScreenPoint(scrPnt, splineCSysKnot);
            curPnt = new Point((int) (scrPnt[0] + 0.5) - pntRectW2, (int) (scrPnt[1] + 0.5) - pntRectH2);
            Rectangle rect = new Rectangle(curPnt, new Dimension(pntRectW, pntRectH));
            if (rect.contains(x, y))
                return (i);
        }
        return (-1);
    }

    /**
     * @param ind
     * @return
     */
    public double[] getScrKnot(int ind) {
        double[] cSysKnot = getCSysKnot(ind);
        if (cSysKnot == null) {
            return cSysKnot;
        }
        double[] scrPnt = parentCSys.cSysPointToScreenPoint(null, cSysKnot);
        return scrPnt;
    }

    /**
     * @param ind
     * @return
     */
    public double[] getCSysKnot(int ind) {
        if (ind < 0 || ind >= getNKnots()) {
            return null;
        }
        return mathSpline3D.getKnotClone(ind);
    }


    /**
     *
     */
    public void printCSysSpline() {
        int nKnots = splineCSysKnots.size();
        System.out.println("n = " + nKnots);
        for (int i = 0; i < nKnots; i++) {
            double[] curKnot = splineCSysKnots.get(i);
            System.out.println("curKnot.x = " + curKnot[0] + ",   curKnot.y = " + curKnot[1]);
        }
    }

    //
    //  d r a g g i n g   s p l i n e
    //
    private final List<double[]> translatedSplineScrPoints = new ArrayList();

    public List<double[]> getTranslatedSplineScrPoints(double[] translationVector) {

        translatedSplineScrPoints.clear();
        int size = splineScrPoints.size();
        for (int i = 0; i < size; i++) {
            double[] splineScrPoint = splineScrPoints.get(i);
            double[] translatedSplineScrPoint = VAlgebra.copyVec3(splineScrPoint);
            VAlgebra.translateVec(translatedSplineScrPoint, translationVector);
            translatedSplineScrPoints.add(translatedSplineScrPoint);
        }
        return translatedSplineScrPoints;
    }

    public List<double[]> getTranslatedSplineScrPoints() {
        return translatedSplineScrPoints;
    }

    protected void drawInterimSplineAccordingConditions(Graphics g, List<double[]> translatedSplineScrPoints) {
        drawSplineAccordingConditions(g, translatedSplineScrPoints);
    }

    public void takeFinalLocation(double[] translationVector, double[] intScrPnt, double[] outScrPnt) {

        List<double[]> translatedSplineCSysKnots = new ArrayList();
        List<double[]> translatedSplineScrKnots = new ArrayList();

        int size = splineScrKnots.size();
        for (int i = 0; i < size; i++) {
            double[] splineScrKnot;
            if (i == 0) {
                splineScrKnot = intScrPnt;
            } else if (i == (size - 1)) {
                splineScrKnot = outScrPnt;
            } else {
                splineScrKnot = splineScrKnots.get(i);
            }
            double[] translatedSplineScrKnot = VAlgebra.copyVec3(splineScrKnot);
            VAlgebra.translateVec(translatedSplineScrKnot, translationVector);
            translatedSplineScrKnots.add(translatedSplineScrKnot);
            double[] translatedSplineCSysKnot = parentCSysView.screenPointToCSysPoint(translatedSplineScrKnot);
            translatedSplineCSysKnots.add(translatedSplineCSysKnot);
        }

        splineScrKnots.clear();
        splineScrKnots.addAll(translatedSplineScrKnots);
        splineCSysKnots.clear();
        splineCSysKnots.addAll(translatedSplineCSysKnots);
        mathSpline3D.setKnots(splineCSysKnots);
        calculate();
    }

    //
    //     D r a w    S p l i n e
    //

    protected void drawSpline(Graphics g, List<double[]> splineThreadScrPoints) {

        int nKnots = getNKnots();
        if (nKnots < 1) {
            return;
        }

        int nPoints = splineThreadScrPoints.size();
        if (nPoints == 0) {
            return;
        }

        if (isWatermarked()) {
            g.setColor(watermarkColor);
        }

        double[] curPnt = splineThreadScrPoints.get(0);
        int cx = (int) (curPnt[0] + 0.5);
        int cy = (int) (curPnt[1] + 0.5);

        for (int i = 1; i < nPoints; i++) {
            curPnt = splineThreadScrPoints.get(i);
            int nx = (int) (curPnt[0] + 0.5);
            int ny = (int) (curPnt[1] + 0.5);
            g.drawLine(cx, cy, nx, ny);
            cx = nx;
            cy = ny;
        }
    }

    public double[] getSplineScrPntWithZ(int ind) {
        int nPnts = splineScrPoints.size();
        if (ind < 0 || ind > nPnts - 1)
            return null;

        double[] scrPnt = splineScrPoints.get(ind);
        double[] retPnt = {scrPnt[0], scrPnt[1], scrPnt[2]};
        return retPnt;
    }


    public double[] getSplineScrPnt(int ind) {
        int nPnts = splineScrPoints.size();
        if (ind < 0 || ind > nPnts - 1)
            return null;

        double[] scrPnt = splineScrPoints.get(ind);
        double[] retPnt = {scrPnt[0], scrPnt[1], 0.};
        return retPnt;
    }

    //
    // b a s i c   C S y s   s t u f f
    //


    @Override
    public void draw(Graphics g) {
//        if (parentCSys.whatToDo == parentCSys.MOVE_ELEMENT ||
//                parentCSys.whatToDo == parentCSys.DELETE_ELEMENT) {

//            drawKnotBoxes = true;
//        } else if (isHighlighted()) {
//            drawKnots = false;
//        }
//        drawSplineAccordingConditions(g, true, false, drawKnots, drawKnotBoxes);
        drawSplineAccordingConditions(g, splineScrPoints);
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        int nOfCSysPoints = splineCSysPoints.size();
//        System.out.println("scale.Spline Entity  " + nOfCSysPoints);
        if (nOfCSysPoints < 2) {
            return;
        }

        double[] doubleCSys0 = VAlgebra.intVec3ToDouble(scr0);
//        System.out.println("SCR " + nOfPoints);

//        if (splineScrPoints.size() != nOfCSysPoints) {
        splineScrPoints.clear();
//        System.out.println("scale.Spline Entity  " + nOfCSysPoints);
        for (int i = 0; i < nOfCSysPoints; i++) {
            splineScrPoints.add(new double[]{0, 0, 0});
        }
//        }
        for (int i = 0; i < nOfCSysPoints; i++) {
            double[] currentCSysPoint = splineCSysPoints.get(i);
            double[] currentScrPoint = splineScrPoints.get(i);
            VAlgebra.copyVec3(currentScrPoint, currentCSysPoint);
            VAlgebra.scaleVec3(currentScrPoint, scale, currentScrPoint);
            currentScrPoint[1] *= -1;
            VAlgebra.translateVec(currentScrPoint, doubleCSys0);
        }

        splineScrKnots.clear();
        for (double[] knotCSysLocation : splineCSysKnots) {
            double[] knotScrLocation = VAlgebra.copyVec3(knotCSysLocation);
            knotScrLocation = VAlgebra.scaleVec3(knotScrLocation, scale, knotScrLocation);
            knotScrLocation[1] *= -1;
            VAlgebra.addVec3(knotScrLocation, doubleCSys0, knotScrLocation);
            splineScrKnots.add(knotScrLocation);
        }
    }

    /**
     * Main method to draw spline with knots
     *
     * @param g
     */
    protected void drawSplineAccordingConditions(Graphics g, List<double[]> splineScrPoints) {

        calculate();

        //
        //     Draw spline line only
        //

        Color splineThreadColor = getThreadColor();
        g.setColor(splineThreadColor);
        drawSpline(g, splineScrPoints);

        //
        //     Draw knots
        //
        if (drawKnots) {
            drawSplineKnots(g);
        }
    }

    /**
     * @param g
     */
    protected void drawSplineKnots(Graphics g) {
        if (!drawKnots) {
            return;
        }
        double[] scrPnt;
        int lastInnerKnotIndex = splineScrKnots.size() - 1;
        g.setColor(knotColor);

        for (int i = 1; i < lastInnerKnotIndex; i++) {
            scrPnt = splineScrKnots.get(i);
            // do not draw knot where knob is located
//            if (((!drawKnobAsArrow && i != arcKnobInd) || drawKnobAsArrow)) {
            Color knotColor = getKnotColor(i);
            int dotX = ((int) (scrPnt[0] + 0.5));
            int dotY = ((int) (scrPnt[1] + 0.5));
            Color dotColor = Color.BLACK; // creation co;or
            if (allKnotsSelected) {
                dotColor = selectedKnotIndex == i ? Color.MAGENTA : Color.BLUE;  // moving color
            }
            drawBigKnob(g, dotX, dotY, dotColor, Color.WHITE);
//            }

            // draw boxes
            if (drawKnotBoxes) {
//                dotColor = selectedKnotIndex == i ? Color.MAGENTA : Color.BLUE;  // moving color
                g.setColor(dotColor);
                int kx = dotX - pntRectW2;
                int ky = dotY - pntRectH2;
                g.drawRect(kx, ky, pntRectW, pntRectH);
            }
        }
    }

    /**
     * @return
     */
    protected Color getThreadColor() {
        return DEFAULT_DRAWING_COLOR;
    }

    /**
     * @param knotIndex
     * @return
     */
    private Color getKnotColor(int knotIndex) {
        Color color;
        if (selected && (allKnotsSelected || (selectedKnotIndex != -1 && knotIndex == selectedKnotIndex))) {
            color = getHighlightColor();
        } else {
            color = isHighlighted() ? getHighlightColor() : DEFAULT_KNOT_COLOR;
        }
        return color;
    }

    /**
     * @param g
     * @param x
     * @param y
     * @param fillColor
     * @param knobOutlineColor
     */
    private void drawBigKnob(Graphics g, int x, int y, Color fillColor, Color knobOutlineColor) {
        g.setColor(knobOutlineColor);
        g.drawLine(x - 1, y - 3, x + 1, y - 3);
        g.drawLine(x - 2, y - 2, x + 2, y - 2);
        g.drawLine(x - 3, y - 1, x + 3, y - 1);
        g.drawLine(x - 3, y, x + 3, y);
        g.drawLine(x - 3, y + 1, x + 3, y + 1);
        g.drawLine(x - 2, y + 2, x + 2, y + 2);
        g.drawLine(x - 1, y + 3, x + 1, y + 3);
        drawKnot(g, x, y, fillColor);
    }

    /**
     * @param g
     * @param x
     * @param y
     * @param fillColor
     */
    private void drawKnot(Graphics g, int x, int y, Color fillColor) {
        g.setColor(fillColor);
        g.drawLine(x - 1, y - 2, x + 1, y - 2);
        g.drawLine(x - 2, y - 1, x + 2, y - 1);
        g.drawLine(x - 2, y, x + 2, y);
        g.drawLine(x - 2, y + 1, x + 2, y + 1);
        g.drawLine(x - 1, y + 2, x + 1, y + 2);
    }

    public int getSelectedKnotIndex() {
        return selectedKnotIndex;
    }

    public void setSelectedKnotInd(int ind) {
        selectedKnotIndex = ind;
        activeKnotInd = ind;
    }

    protected int getNSegments() {
        return mathSpline3D.getNSegments();
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
    public int findArrTipInd(int begKnotInd, int endKnotInd,
                             double arrLen, double r1, double r2,
                             int where) {
        int nSeg = mathSpline3D.getNSegments();

        double dist;
        int begSegInd = begKnotInd * nSeg;
        int endSegInd = endKnotInd * nSeg;

        int firstInd = -1, midInd = -1, lastInd = -1;
        double[] startScrPnt = getSplineScrPnt(begSegInd);
        double[] endScrPnt = getSplineScrPnt(endSegInd);
//
//  case 1:   // find first appropriate segment ind
        int curSegInd = begSegInd + 1;
        double[] curScrPnt = startScrPnt;
        dist = 0;
        for (int i = 0; i < nSeg; curSegInd++, i++) {
            curScrPnt = getSplineScrPnt(curSegInd);
            dist = VAlgebra.distVec3(startScrPnt, curScrPnt);
            if (dist > r1 + arrLen) {
                firstInd = curSegInd;
                break;
            }
        }
        dist = VAlgebra.distVec3(endScrPnt, curScrPnt);
        if (dist < r2) {
            return -1;
        }
        if (where == 1) {
            return firstInd;
        }
//
//  case 2:  find last appropriate segment ind
        curSegInd = endSegInd;
        curScrPnt = endScrPnt;
        dist = 0;
        for (int i = 0; i < nSeg; curSegInd--, i++) {
            curScrPnt = getSplineScrPnt(curSegInd);
            dist = VAlgebra.distVec3(endScrPnt, curScrPnt);
            if (dist > r2) {
                lastInd = curSegInd;
                break;
            }
        }
        dist = VAlgebra.distVec3(startScrPnt, curScrPnt);
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
     * This methis is used to set to ?
     *
     * @return
     */
    public List<double[]> getSplineCSysPoints() {
        return splineCSysPoints;
    }


}
