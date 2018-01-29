package dsdsse.welcome;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/30/2017.
 */
public class RoundedRectangle {

    public static int UPPER_LEFT = 1 << 16;
    public static int UPPER_RIGHT = 2 << 16;
    public static int LOWER_LEFT = 4 << 16;
    public static int LOWER_RIGHT = 8 << 16;
    private static int CORNER_MASK = 15 << 16;

    public static int ROUND_UPPER_LEFT = 1;
    public static int ROUND_UPPER_RIGHT = 2;
    public static int ROUND_LOWER_LEFT = 4;
    public static int ROUND_LOWER_RIGHT = 8;

    public static int DEFAULT_RADIUS = 10;

    public static int ROUND_NONE = UPPER_LEFT | UPPER_RIGHT | LOWER_LEFT | LOWER_RIGHT;

    public static int ROUND_ALL =
            UPPER_LEFT | ROUND_UPPER_LEFT | UPPER_RIGHT | ROUND_UPPER_RIGHT |
                    LOWER_LEFT | ROUND_LOWER_LEFT | LOWER_RIGHT | ROUND_LOWER_RIGHT;
    public static int ROUND_UPPER_SIDE =
            UPPER_LEFT | ROUND_UPPER_LEFT | UPPER_RIGHT | ROUND_UPPER_RIGHT |
                    LOWER_LEFT | LOWER_RIGHT;
    public static int ROUND_LEFT_SIDE = UPPER_LEFT | ROUND_UPPER_LEFT | LOWER_LEFT | ROUND_LOWER_LEFT |
            UPPER_RIGHT | LOWER_RIGHT;

    public static int ROUND_BOTTOM_SIDE = UPPER_LEFT | UPPER_RIGHT | LOWER_LEFT | ROUND_LOWER_LEFT |
            LOWER_RIGHT | ROUND_LOWER_RIGHT;

    private ArrayList clippingPoints = new ArrayList();
    private ArrayList borderPoints = new ArrayList();
    private final List tmpPointsList = new ArrayList();
    private Polygon borderPolygon;

    public Polygon clippingPolygon;
    private int[] borderXPpoints;
    private int[] borderYPpoints;

    private int roundingPolicy;
    private final int roundingRadius;

    private Insets insets = new Insets(0, 0, 0, 0);
    private int rectWidth;
    private int rectHeight;

    public RoundedRectangle() {
        this(DEFAULT_RADIUS, ROUND_ALL);
    }

    public RoundedRectangle(int radius, int roundingPolicy) {
        roundingRadius = radius;
        roundingPolicy = roundingPolicy;
    }

    /**
     * @param roundingPolicy
     */
    public void setRoundingPolicy(int roundingPolicy) {
        this.roundingPolicy = roundingPolicy;
    }

    public void updateSize(Insets insets, int rectWidth, int rectHeight) {
        this.insets = insets;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        createClipPolygon();
    }

    private void createBorderPolygon() {
        boolean leftToRight = false;
        boolean topToDown = false;
        boolean goNorth = false;
        boolean goWest = true;
        borderXPpoints = new int[clippingPoints.size()];
        borderYPpoints = new int[clippingPoints.size()];

        for (int i = 0; i < clippingPoints.size() - 1; i++) {
            Point pnt = (Point) clippingPoints.get(i);
            Point nextPnt = (Point) clippingPoints.get(i + 1);
            if (nextPnt.x < pnt.x) { // go west
                if (nextPnt.y < pnt.y) { // go north-west
                    if (!goNorth) {
                        borderXPpoints[i] = pnt.x;
                        borderYPpoints[i] = pnt.y - 1;
                    } else {
                        borderXPpoints[i] = pnt.x + 1;
                        borderYPpoints[i] = pnt.y;
                    }

                } else if (nextPnt.y == pnt.y) {  // go westt
                    borderXPpoints[i] = pnt.x;
                    borderYPpoints[i] = pnt.y - 1;
                    goWest = true;

                } else {                           // go south-west
                    if (!goWest) {
                        borderXPpoints[i] = pnt.x - 1;
                        borderYPpoints[i] = pnt.y;
                    } else {
                        borderXPpoints[i] = pnt.x;
                        borderYPpoints[i] = pnt.y - 1;
                    }
                }

            } else if (nextPnt.x > pnt.x) { // go east
                if (nextPnt.y < pnt.y) {          // go north-east
                    if (goWest) {
                        borderXPpoints[i] = pnt.x + 1;
                        borderYPpoints[i] = pnt.y;
                    } else {
                        borderXPpoints[i] = pnt.x;
                        borderYPpoints[i] = pnt.y + 1;
                    }

                } else if (nextPnt.y == pnt.y) {  // go east
                    borderXPpoints[i] = pnt.x;
                    borderYPpoints[i] = pnt.y + 1;
                    goWest = false;
                } else {                            // go south-east
                    if (goNorth) {
                        borderXPpoints[i] = pnt.x;
                        borderYPpoints[i] = pnt.y + 1;
                    } else {
                        borderXPpoints[i] = pnt.x - 1;
                        borderYPpoints[i] = pnt.y;
                    }
                }
            }

            if (nextPnt.x == pnt.x) { // go north
                if (nextPnt.y < pnt.y) { // go north
                    borderXPpoints[i] = pnt.x + 1;
                    borderYPpoints[i] = pnt.y;
                    goNorth = true;
                } else { // go south
                    borderXPpoints[i] = pnt.x - 1;
                    borderYPpoints[i] = pnt.y;
                    goNorth = false;
                }
            }

            if (i == clippingPoints.size() - 2) {
                borderXPpoints[i + 1] = pnt.x - 1;
                borderYPpoints[i + 1] = pnt.y - 1;
            }
        }

        borderPolygon = new Polygon(borderXPpoints, borderYPpoints, borderXPpoints.length);
    }

    /**
     *
     */
    private void createClipPolygon() {
        clippingPoints.clear();
        borderPoints.clear();

        if ((roundingPolicy & CORNER_MASK & UPPER_LEFT) == UPPER_LEFT) {
            if ((roundingPolicy & ROUND_UPPER_LEFT) != 0) {
                createRoundedCorner(clippingPoints, insets.left + roundingRadius, insets.top + roundingRadius, roundingRadius, 0);
            } else {
                clippingPoints.add(new Point(insets.left, insets.top));
            }
        }

        if ((roundingPolicy & CORNER_MASK & UPPER_RIGHT) == UPPER_RIGHT) {
            if ((roundingPolicy & ROUND_UPPER_RIGHT) != 0) {
                createRoundedCorner(clippingPoints, rectWidth - insets.right - roundingRadius,
                        insets.top + roundingRadius, roundingRadius, 1);
            } else {
                clippingPoints.add(new Point(rectWidth - insets.right, insets.top));
            }
        }

        if ((roundingPolicy & CORNER_MASK & LOWER_RIGHT) == LOWER_RIGHT) {
            if ((roundingPolicy & ROUND_LOWER_RIGHT) != 0) {
                createRoundedCorner(clippingPoints, rectWidth - insets.right - roundingRadius,
                        rectHeight - insets.bottom - roundingRadius, roundingRadius, 2);
            } else {
                clippingPoints.add(new Point(rectWidth - insets.right, rectHeight - insets.bottom));
            }
        }

        if ((roundingPolicy & CORNER_MASK & LOWER_LEFT) == LOWER_LEFT) {
            if ((roundingPolicy & ROUND_LOWER_LEFT) != 0) {
                createRoundedCorner(clippingPoints, insets.left + roundingRadius,
                        rectHeight - insets.bottom - roundingRadius, roundingRadius, 3);
            } else {
                clippingPoints.add(new Point(insets.left, rectHeight - insets.bottom));
            }
        }


        int[] clippingXPpoints = new int[clippingPoints.size()];
        int[] clippingYPpoints = new int[clippingPoints.size()];

        for (int i = 0; i < clippingPoints.size(); i++) {
            Point pnt = (Point) clippingPoints.get(i);
            clippingXPpoints[i] = pnt.x;
            clippingYPpoints[i] = pnt.y;
        }


        clippingPolygon = new Polygon(clippingXPpoints, clippingYPpoints, clippingPoints.size());
    }


    private void createRoundedCorner(List clippingPoints, int x0, int y0, int r, int quote) {
        int x, y;
        float d;
        x = 0;
        y = r;
        d = 5 / 4 - r;
        tmpPointsList.clear();
        while (y > x) {
            if (d < 0) {
                d = d + 2 * x + 3;
                x++;
            } else {
                d = d + 2 * (x - y) + 0;
                x++;
                y--;
            }
            plotPoints(clippingPoints, tmpPointsList, x0, y0, x, y, quote);
        }

        int arrLastInd = tmpPointsList.size() - 1;
        for (int i = 0; i <= arrLastInd; i++) {
            Point pnt = (Point) tmpPointsList.get(arrLastInd - i);
            clippingPoints.add(pnt);
        }
    }

    /**
     * @param clippingPoints
     * @param clippingPoints2
     * @param x0
     * @param y0
     * @param x
     * @param y
     * @param quot
     */
    private void plotPoints(List clippingPoints, List clippingPoints2,
                            int x0, int y0, int x, int y, int quot) {
        switch (quot) {
            case 0: // upper left
                clippingPoints2.add(new Point(x0 - x, y0 - y));
                clippingPoints.add(new Point(x0 - y, y0 - x));
                break;

            case 1: // upper right
                clippingPoints2.add(new Point(x0 + y, y0 - x));
                clippingPoints.add(new Point(x0 + x, y0 - y));
                break;

            case 2: // lower right
                clippingPoints2.add(new Point(x0 + x, y0 + y));
                clippingPoints.add(new Point(x0 + y, y0 + x));
                break;

            case 3: // lower left
                clippingPoints2.add(new Point(x0 - y, y0 + x));
                clippingPoints.add(new Point(x0 - x, y0 + y));
                break;
        }
    }

}


