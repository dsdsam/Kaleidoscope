package sem.appui.components;

import sem.appui.controls.Roundable;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 12, 2011
 * Time: 7:43:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundedRectangleShape extends Polygon implements Roundable {


    private int roundingPolicy;
    private final int[][][] shapeCorners;
    private final boolean outer;


    public RoundedRectangleShape(int[][][] shapeCorners, boolean outer,
                                 int roundingPolicy) {
        this.shapeCorners = shapeCorners;
        this.outer = outer;
        this.roundingPolicy = roundingPolicy;
    }

    public void setSize(int x, int y, int width, int height) {
        Polygon polygon = buildShape(x, y, width, height, shapeCorners, outer);
        this.xpoints = polygon.xpoints;
        this.ypoints = polygon.ypoints;
        this.npoints = polygon.npoints;
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param border
     * @param outer
     * @return Polygon
     */
    private Polygon buildShape(int x, int y, int width, int height,
                               int[][][] border, boolean outer) {
        Polygon borderPolygon = new Polygon();


        height++;
        if (roundingPolicy == ROUNDING_TOP) {
            borderPolygon.addPoint(x, y + height);
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + border[1][j][0], y + border[1][j][1]);
            }
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + width + border[2][j][0], y + border[2][j][1]);
            }
            borderPolygon.addPoint(x + width, y + height);
            return borderPolygon;
        }
        if (roundingPolicy == ROUNDING_LEFT || roundingPolicy == ROUNDING_ALL) {
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + border[0][j][0], y + height
                        + border[0][j][1]);
            }
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + border[1][j][0], y + border[1][j][1]);
            }
        } else {
            if (outer) {
                borderPolygon.addPoint(x, y + height);
                borderPolygon.addPoint(x, y);
            } else {
                borderPolygon.addPoint(x + 1, y + height - 1);
                borderPolygon.addPoint(x + 1, y + 1);
            }
        }
        if (roundingPolicy == ROUNDING_RIGHT || roundingPolicy == ROUNDING_ALL) {
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + width + border[2][j][0], y
                        + border[2][j][1]);
            }
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + width + border[3][j][0], y + height + border[3][j][1]);
            }
        } else {
            if (outer) {
                borderPolygon.addPoint(x + width, y);
                borderPolygon.addPoint(x + width, y + height);
            } else {
                borderPolygon.addPoint(x + width - 1, y + 1);
                borderPolygon.addPoint(x + width - 1, y + height - 1);
            }
        }
        return borderPolygon;
    }
}
