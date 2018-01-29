package adf.ui.components.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;

import javax.swing.border.AbstractBorder;

public class AlignedRoundedBorder extends AbstractBorder {
    public static int ROUNDING_NONE = 0;

    public static int ROUNDING_LEFT = 1;

    public static int ROUNDING_RIGHT = 2;

    public static int ROUNDING_BOTH = 3;

    private static int[][][] BORDER_OR04 = {
            {{2, 0}, {2, -1}, {1, -1}, {1, -2}, {0, -2}},
            {{0, 2}, {1, 2}, {1, 1}, {2, 1}, {2, 0}},
            {{-2, 0}, {-2, 1}, {-1, 1}, {-1, 2}, {0, 2}},
            {{0, -2}, {-1, -2}, {-1, -1}, {-2, -1}, {-2, 0}}};

    private static int[][][] BORDER_IR04 = {
            {{3, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -3}},
            {{1, 3}, {2, 3}, {2, 2}, {3, 2}, {3, 1}},
            {{-3, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 3}},
            {{-1, -3}, {-2, -3}, {-2, -2}, {-3, -2}, {-3, -1}}};

    private static int[][][] BORDER_OR05 = {
            {{4, 0}, {3, -1}, {2, -1}, {1, -2}, {1, -3}, {0, -4}},
            {{0, 4}, {1, 3}, {1, 2}, {2, 1}, {3, 1}, {4, 0}},
            {{-4, 0}, {-3, 1}, {-2, 1}, {-1, 2}, {-1, 3}, {0, 4}},
            {{0, -4}, {-1, -3}, {-1, -2}, {-2, -1}, {-3, -1},
                    {-4, 0}}};

    private static int[][][] BORDER_IR05 = {
            {{4, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -4}},
            {{1, 4}, {2, 3}, {2, 2}, {3, 2}, {4, 1}},
            {{-4, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 4}},
            {{-1, -4}, {-2, -3}, {-2, -2}, {-3, -2}, {-4, -1}}};

    private int thickness;

    private int roundingRadius;

    private int roundingPolicy;

    private Color outerBorderColor;

    private Color innerBorderColor;

    private Polygon outerBorderPolygon;

    private Polygon innerBorderPolygon;

    private int currentWidth;

    private int currentHeight;

    /**
     * * *
     *
     * @param thickness        *
     * @param roundingRadius   *
     * @param roundingPolicy   *
     * @param outerBorderColor *
     * @param innerBorderColor
     */
    public AlignedRoundedBorder(int thickness, int roundingRadius,
                                int roundingPolicy, Color innerBorderColor, Color outerBorderColor) {
        this.thickness = 2;//thickness;
        this.roundingRadius = roundingRadius;
        this.roundingPolicy = roundingPolicy;
        this.innerBorderColor = innerBorderColor;
        this.outerBorderColor = outerBorderColor;
    }

    public void setInnerBorderColor(Color innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
    }


    public void setOuterBorderColor(Color outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
    }

    /**
     * * *
     *
     * @param c *
     * @return Insets
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    /**
     * * *
     *
     * @param c      *
     * @param insets *
     * @return Insets
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = thickness;
        insets.left = thickness;
        insets.bottom = thickness;
        insets.right = thickness;
        return insets;
    }

    /**
     * * *
     *
     * @param x      *
     * @param y      *
     * @param width  *
     * @param height *
     * @param border *
     * @param outer  *
     * @return Poligon
     */
    private Polygon buildShape(int x, int y, int width, int height,
                               int[][][] border, boolean outer) {
        Polygon borderPolygon = new Polygon();
        width--;
        height--;
        if (roundingPolicy == ROUNDING_LEFT || roundingPolicy == ROUNDING_BOTH) {
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + border[0][j][0], y + height
                        + border[0][j][1]);
            }
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon
                        .addPoint(x + border[1][j][0], y + border[1][j][1]);
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
        if (roundingPolicy == ROUNDING_RIGHT || roundingPolicy == ROUNDING_BOTH) {
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + width + border[2][j][0], y
                        + border[2][j][1]);
            }
            for (int j = 0; j < border[0].length; j++) {
                borderPolygon.addPoint(x + width + border[3][j][0], y + height
                        + border[3][j][1]);
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

    /**
     * * *
     *
     * @param c      *
     * @param g      *
     * @param x      *
     * @param y      *
     * @param width  *
     * @param height
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        if (currentWidth != width || currentHeight != height) {
            currentWidth = width;
            currentHeight = height;
            if (roundingRadius == 4) {
                outerBorderPolygon = buildShape(x, y, width, height, BORDER_OR04, true);
                innerBorderPolygon = buildShape(x, y, width, height, BORDER_IR04, false);
            }
            if (roundingRadius == 5) {
                outerBorderPolygon = buildShape(x, y, width, height, BORDER_OR05, true);
                innerBorderPolygon = buildShape(x, y, width, height, BORDER_IR05, false);
            }
        }

        if (innerBorderColor != null) {
            g.setColor(innerBorderColor);
            g.drawPolygon(innerBorderPolygon);
        }

         if (outerBorderColor != null) {
            g.setColor(outerBorderColor);
            g.drawPolygon(outerBorderPolygon);
        }

//
//
//        if (thickness >= 1) {
//            g.setColor(outerBorderColor);
//            g.drawPolygon(outerBorderPolygon);
//        }
//
//        if (thickness == 2) {
//            g.setColor(innerBorderColor);
//            g.drawPolygon(innerBorderPolygon);
//        }
    }
}
