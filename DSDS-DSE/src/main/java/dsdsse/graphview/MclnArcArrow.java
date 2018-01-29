package dsdsse.graphview;

import adf.csys.view.CSysView;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/22/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnArcArrow {

    protected static final Color DEFAULT_WATERMARK_COLOR = new Color(0xDDDDDD);
    protected static final Color DEFAULT_WATERMARK_BORDER_COLOR = new Color(0xBBBBBB);

    private static final Color ARROW_CREATION_FILL_COLOR = new Color(0xBBBBBB);
    private static final Color ARROW_BORDER_COLOR = Color.GRAY;//new Color(0x777777);

    private static final Color ARROW_SELECTED_FILLING_COLOR = new Color(0xEEEEEE);//new Color(0xE0E0E0);
    private static final Color ARROW_SELECTED_BORDER_COLOR = Color.MAGENTA;//Color.BLACK;//new Color(0xFF00FF);

    private static final Color ARROW_HIGHLIGHTED_FILLING_COLOR = new Color(0xEEEEEE);
    private static final Color ARROW_HIGHLIGHTED_BORDER_COLOR = Color.RED;

    public static final int DEFAULT_ARROW_LENGTH = 14;
    public static final int DEFAULT_ARROW_WIDTH = 5;

    public static final int MEDIUM_ARROW_LENGTH = 10;
    public static final int MEDIUM_ARROW_WIDTH = 4;

    public static final int SMALL_ARROW_LENGTH = 7;
    public static final int SMALL_ARROW_WIDTH = 3;

    /*

                                --  *  1. ( length, -w )
                           --       |
                      --            |
    0. ( 0,0 )    *                 *  2. ( length, 0 )
                      --            |
                           --       |
                                --  *   3. ( length, +w  )

  */

    public static enum Representation {TRIANGLE_ARROW, KNOB_ARROW, BIRD_ARROW}


    private static final int NOT_SET = 0;

    private static final double bal = 7;
    private static final double baw = 3;

    private CSysView parentCSys;
    private int arrowLength, arrowWidth;

    private double[][] triangleArrow = {
            {0, 0, 0,},
//            {DEFAULT_ARROW_LENGTH / 2, 0, 0,},
            {DEFAULT_ARROW_LENGTH, -DEFAULT_ARROW_WIDTH, 0,},
            {DEFAULT_ARROW_LENGTH, +DEFAULT_ARROW_WIDTH, 0,},
            {0, 0, 0,}};

    private double[][] knobArrow = {
            {0, 0, 0,},
            {-DEFAULT_ARROW_LENGTH, 0, 0,},
            {-DEFAULT_ARROW_LENGTH, -DEFAULT_ARROW_WIDTH, 0,},
            {-DEFAULT_ARROW_LENGTH, +DEFAULT_ARROW_WIDTH, 0,}};

    private double[][] birdArrow = {
            {0, 0, 0,},
            {-bal, 0, 0,},
            {-bal, -baw, 0,},
            {-bal, +baw, 0,}};

    private double[][] arrow = {
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,}};

    private int[][] scrArrow = {
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,}};

    private double[][] placement = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1},
            {100, 100, 0}};

    private double[][] scrPoints = {
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,}};

    private double[][] cSysPoints = {
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,},
            {0, 0, 0,}};

    private Representation knobRepresentation = Representation.TRIANGLE_ARROW;

    // outline in screen coordinates
    private int scrOutlineMinX;
    private int scrOutlineMinY;
    private int scrOutlineMaxX;
    private int scrOutlineMaxY;

    private boolean arrowHighlighted;
    private boolean arrowPreSelected;
    private boolean arrowSelected;

    private Polygon arcKnob;
    private double[] directionVector;
    private final double[] arrowTipScrLocation;
    private final double[] arrowTipCSysLocation;

    private Color stateColor;

    // used when spline is dragged
    private double[] translationVector = {0, 0, 0};

    public MclnArcArrow(CSysView cSysView, int arrowLength, int arrowWidth, double[] directionVector,
                        double[] arrowTipScrLocation, Color stateColor) {
        this.parentCSys = cSysView;
        this.arrowLength = arrowLength;
        this.arrowWidth = arrowWidth;
        this.directionVector = directionVector;
        this.stateColor = stateColor;

        this.arrowTipScrLocation = arrowTipScrLocation;
        arrowTipCSysLocation = parentCSys.screenPointToCSysPoint(arrowTipScrLocation);

        arcKnob = initTriangleArrow(arrowLength, arrowWidth, directionVector, arrowTipScrLocation);
        cSysPoints = arrowScrPointsToCSysPoints(scrPoints);
    }

    double[] getScrOutlineCenter() {
        double[] outlineCenter = {0, 0, 0};
        outlineCenter[0] = outline.x + outline.width / 2;
        outlineCenter[1] = outline.y + outline.height / 2;
        return outlineCenter;
    }

    public void updateStateColorUponInitialization(int stateID) {
        this.stateColor = new Color(stateID);
    }

    double[][] getCSysPoints() {
        return cSysPoints;
    }

    /**
     * @param arrowLength
     * @param arrowWidth
     * @param directionVector
     * @param arrowTipScrLocation
     * @return
     */
    private Polygon initTriangleArrow(double arrowLength, double arrowWidth, double[] directionVector,
                                      double[] arrowTipScrLocation) {
        triangleArrow[0][0] = 0;
        triangleArrow[0][1] = 0;
        triangleArrow[1][0] = arrowLength;
        triangleArrow[1][1] = -arrowWidth;
        triangleArrow[2][0] = arrowLength;
        triangleArrow[2][1] = arrowWidth;

        VAlgebra.initMat43(placement, directionVector, arrowTipScrLocation);
        scrPoints = placeArrowOnScr(triangleArrow, placement);
        Polygon knob = scrPointsMatrixToPolygon(scrPoints, true);
        return knob;
    }

    /**
     * This method is used by Initialization Assistant
     *
     * @param arrowLength
     * @param arrowWidth
     * @param arrowTipScrLocation
     * @return
     */
    public Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] arrowTipScrLocation) {
        Polygon arcKnob = buildTriangleArrow(arrowLength, arrowWidth, directionVector, arrowTipScrLocation);
        return arcKnob;
    }

    /**
     * The method is used by Initialization Assistant
     *
     * @param arrowLength
     * @param arrowWidth
     * @param directionVector
     * @param arrowTipScrLocation
     * @return
     */
    private Polygon buildTriangleArrow(double arrowLength, double arrowWidth, double[] directionVector,
                                       double[] arrowTipScrLocation) {

        double[][] triangleArrow = new double[4][3];
        triangleArrow[0][0] = 0;
        triangleArrow[0][1] = 0;
        triangleArrow[1][0] = arrowLength;
        triangleArrow[1][1] = -arrowWidth;
        triangleArrow[2][0] = arrowLength;
        triangleArrow[2][1] = arrowWidth;

        double[][] placement = new double[4][3];
        VAlgebra.initMat43(placement, directionVector, arrowTipScrLocation);
        double[][] scrPoints = placeArrowOnScr(triangleArrow, placement);
        Polygon knob = scrPointsMatrixToPolygon(scrPoints, false);
        return knob;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public boolean isMouseHover(int x, int y) {
        return outline.contains(x, y);
    }

    /**
     * @param knob
     * @param placement
     * @return
     */
    private double[][] placeArrowOnScr(double[][] knob, double[][] placement) {
        double[][] scrKnobLocations = new double[4][3];
        for (int i = 0; i < placement.length; i++) {
            scrKnobLocations[i] = VAlgebra.mat43XPnt3(placement, knob[i]);
        }
        return scrKnobLocations;
    }

    protected int[][] placeArrowOnScr(int[][] scrKnobLocations, double[][] knob, double[][] placement) {
        for (int i = 0; i < placement.length; i++) {
            double[] scrPoint = VAlgebra.mat43XPnt3(placement, knob[i]);
            scrKnobLocations[i] = doubleVec3ToInt(scrKnobLocations[i], scrPoint);
        }
        return scrKnobLocations;
    }


    private int[] doubleVec3ToInt(int vec1[], double vec2[]) {
        vec1[0] = (int) Math.rint(vec2[0]);
        vec1[1] = (int) Math.rint(vec2[1]);
        vec1[2] = (int) Math.rint(vec2[2]);
        return vec1;
    }

    Rectangle outline = new Rectangle();

    //
    //   d r a w i n g   a t t r i b u t e   s e t t i n g s
    //

    public void setPreSelected(boolean preSelected) {
        this.arrowPreSelected = preSelected;
    }

    public void setSelected(boolean arrowSelected) {
        this.arrowSelected = arrowSelected;
    }

    public void setHighlighted(boolean arrowHighlighted) {
        this.arrowHighlighted = arrowHighlighted;
    }

    //
    //   d r a w i n g
    //

    private boolean watermarked;

    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
    }

    public void translate(double[] translationVector) {
        this.translationVector = translationVector;
    }

    public void takeFinalLocation(double[] translationVector) {
        VAlgebra.translateVec(arrowTipScrLocation, translationVector);
        parentCSys.screenPointToCSysPoint(arrowTipCSysLocation, arrowTipScrLocation);
        arcKnob = initTriangleArrow((int) arrowLength, (int) arrowWidth, directionVector, arrowTipScrLocation);
        arcKnob = scrPointsMatrixToPolygon(scrPoints, true);
        cSysPoints = arrowScrPointsToCSysPoints(scrPoints);
    }

    /**
     * @param g
     */

    void drawArrow(Graphics g, boolean selectingArrowTipLocation) {
        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (watermarked) {
            g2D.setColor(DEFAULT_WATERMARK_COLOR);
        } else if (selectingArrowTipLocation) {
            g2D.setColor(Color.WHITE);
//            g2D.setColor(stateColor);
        } else if (arrowSelected) {
            g2D.setColor(ARROW_SELECTED_FILLING_COLOR);
        } else if (arrowHighlighted) {
            g2D.setColor(ARROW_HIGHLIGHTED_FILLING_COLOR);
        } else {
            g2D.setColor(stateColor);
        }
        g2D.fill(arcKnob);

        if (watermarked) {
            g2D.setColor(DEFAULT_WATERMARK_BORDER_COLOR);
        } else if (arrowSelected) {
            g.setColor(ARROW_SELECTED_BORDER_COLOR);
        } else if (arrowHighlighted) {
            g.setColor(ARROW_HIGHLIGHTED_BORDER_COLOR);
        } else {
            g.setColor(ARROW_BORDER_COLOR);
        }
        g2D.draw(arcKnob);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    /**
     *
     */
    private double[][] arrowScrPointsToCSysPoints(double[][] scrPoints) {
        double[][] cSysPoints = scrPoints.clone();
        for (int i = 0; i < scrPoints.length; i++) {
            cSysPoints[i] = parentCSys.screenPointToCSysPoint(scrPoints[i]);
        }
        return cSysPoints;
    }

    /**
     * @param scr0
     * @param scale
     */
    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        parentCSys.cSysPointToScreenPoint(arrowTipScrLocation, arrowTipCSysLocation);


//        for (int i = 0; i < cSysPoints.length; i++) {
//            double[] currentSCysPoint = cSysPoints[i];
//            double[] currentScrPoint = scrPoints[i];
//            currentScrPoint[0] = scr0[0] + (int) (currentSCysPoint[0] * scale);
//            currentScrPoint[1] = scr0[1] + (int) (-currentSCysPoint[1] * scale);
//            currentScrPoint[2] = 0;
//        }

//        int[][] scrKnobLocations = parentCSys.doubleMatX3ToIntMatX3(scrPoints);

        // calculate Outline();
//        arcKnob = scrPointsMatrixToPolygon(scrPoints);
//        arcKnob.addPoint(scrKnobLocations[0][0], scrKnobLocations[0][1]);
//        arcKnob.addPoint(scrKnobLocations[1][0], scrKnobLocations[1][1]);
//        arcKnob.addPoint(scrKnobLocations[2][0], scrKnobLocations[2][1]);
        arcKnob = initTriangleArrow(arrowLength, arrowWidth, directionVector, arrowTipScrLocation);
    }

    private Polygon scrPointsMatrixToPolygon(double[][] scrPoints, boolean calculateOutline) {
        int[][] scrKnobLocations = parentCSys.doubleMatX3ToIntMatX3(scrPoints);
        if (calculateOutline) {
            // calculate Outline();
            calculateOutline(scrKnobLocations);
        }
        Polygon arcKnob = new Polygon();
        arcKnob.addPoint(scrKnobLocations[0][0], scrKnobLocations[0][1]);
        arcKnob.addPoint(scrKnobLocations[1][0], scrKnobLocations[1][1]);
        arcKnob.addPoint(scrKnobLocations[2][0], scrKnobLocations[2][1]);
        return arcKnob;
    }

    /**
     * @param scrKnobLocations
     */
    private void calculateOutline(int[][] scrKnobLocations) {
        int curX, curY;

        scrOutlineMinX = scrOutlineMaxX = scrKnobLocations[0][0];
        scrOutlineMinY = scrOutlineMaxY = scrKnobLocations[0][1];
        for (int i = 1; i < scrArrow.length; i++) {
            curX = scrKnobLocations[i][0];
            curY = scrKnobLocations[i][1];
            scrOutlineMinX = Math.min(scrOutlineMinX, curX);
            scrOutlineMaxX = Math.max(scrOutlineMaxX, curX);
            scrOutlineMinY = Math.min(scrOutlineMinY, curY);
            scrOutlineMaxY = Math.max(scrOutlineMaxY, curY);
        }

        outline.x = scrOutlineMinX;
        outline.y = scrOutlineMinY;
        outline.width = (scrOutlineMaxX - outline.x);
        outline.height = (scrOutlineMaxY - outline.y);
        outline.grow(2, 2);
    }
}

