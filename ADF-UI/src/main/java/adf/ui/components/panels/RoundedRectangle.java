package adf.ui.components.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RoundedRectangle extends Polygon {

    public static int ROUND_UPPER_LEFT = 1;
    public static int ROUND_UPPER_RIGHT = 2;
    public static int ROUND_LOWER_LEFT = 4;
    public static int ROUND_LOWER_RIGHT = 8;

    public static int ROUND_ALL = ROUND_UPPER_LEFT | ROUND_UPPER_RIGHT |
            ROUND_LOWER_LEFT | ROUND_LOWER_RIGHT;
    public static int ROUND_LEFT_SIDE = ROUND_UPPER_LEFT | ROUND_LOWER_LEFT;

    private ArrayList clippingPpoints = new ArrayList();
    private ArrayList borderPoints = new ArrayList();
    private Polygon borderPolygon;

    //    private Polygon clippingPolygon;
    private int[] borderXPpoints;
    private int[] borderYPpoints;

    private int roundingPolicy;
    private int roundingRadius = 7;
    private Color innerBorderColor = new Color(0x123456);

    private int rectWidth;
    private int rectHeight;

    public RoundedRectangle() {

    }

    /**
     * @param roundingPolicy
     */
    public void setRoundingPolicy(int roundingPolicy) {
        this.roundingPolicy = roundingPolicy;
    }

    public void updateSize(int rectWidth, int rectHeight) {
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        createClipPolygon();
    }

    private void createBorderPolygon() {
        boolean leftToRight = false;
        boolean topToDown = false;
        boolean goNorth = false;
        boolean goWest = true;
        borderXPpoints = new int[clippingPpoints.size()];
        borderYPpoints = new int[clippingPpoints.size()];

        for (int i = 0; i < clippingPpoints.size() - 1; i++) {
            Point pnt = (Point) clippingPpoints.get(i);
            Point nextPnt = (Point) clippingPpoints.get(i + 1);
//            System.out.println( "creating border x= " + pnt.x + "  y= " + pnt.y + " next:  " + nextPnt.x + "  y= " + nextPnt.y + "  " + i );
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

//            System.out.println( "new      border x= " + borderXPpoints[i] + "  y= " + borderYPpoints[i] + "  " + i );
            if (i == clippingPpoints.size() - 2) {
                borderXPpoints[i + 1] = pnt.x - 1;
                borderYPpoints[i + 1] = pnt.y - 1;
            }
        }

        borderPolygon = new Polygon(borderXPpoints, borderYPpoints, borderXPpoints.length);
    }

    private void createClipPolygon() {
        clippingPpoints.clear();
        borderPoints.clear();

        if ((roundingPolicy & ROUND_UPPER_LEFT) != 0) {
            circle(clippingPpoints, roundingRadius,
                    roundingRadius, roundingRadius, 0);
        } else {
            clippingPpoints.add(new Point(0, 0));
        }

        if ((roundingPolicy & ROUND_UPPER_RIGHT) != 0) {
            circle(clippingPpoints, rectWidth - roundingRadius - 1,
                    roundingRadius, roundingRadius, 1);
        } else {
            clippingPpoints.add(new Point(rectWidth - 1, 0));
        }

        if ((roundingPolicy & ROUND_LOWER_RIGHT) != 0) {
            circle(clippingPpoints, rectWidth - roundingRadius - 1,
                    rectHeight - roundingRadius - 1, roundingRadius, 2);
        } else {
            clippingPpoints.add(new Point(rectWidth - 1, rectHeight - 1));
        }

        if ((roundingPolicy & ROUND_LOWER_LEFT) != 0) {
            circle(clippingPpoints, roundingRadius,
                    rectHeight - roundingRadius - 1, roundingRadius, 3);
        } else {
            clippingPpoints.add(new Point(0, rectHeight - 1));
        }


        int[] clippingXPpoints = new int[clippingPpoints.size()];
        int[] clippingYPpoints = new int[clippingPpoints.size()];

        reset();
        for (int i = 0; i < clippingPpoints.size(); i++) {
            Point pnt = (Point) clippingPpoints.get(i);
            clippingXPpoints[i] = pnt.x;
            clippingYPpoints[i] = pnt.y;
            addPoint(10 + pnt.x, 10 + pnt.y);
        }
//        clippingPolygon = new Polygon( clippingXPpoints, clippingYPpoints, clippingPpoints.size() );
//        if ( innerBorderColor != null ){
//            createBorderPolygon();
//        }
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
    private void plotPoints(ArrayList clippingPoints, ArrayList clippingPoints2,
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

    private void circle(ArrayList clippingPoints, int x0, int y0, int r, int quote) {
        int x, y;
        float d;
        x = 0;
        y = r;
        d = 5 / 4 - r;
        ArrayList tmpArrayList = new ArrayList();
        while (y > x) {
            if (d < 0) {
                d = d + 2 * x + 3;
                x++;
            } else {
                d = d + 2 * (x - y) + 0;
                x++;
                y--;
            }
            plotPoints(clippingPoints, tmpArrayList, x0, y0, x, y, quote);
        }

        int arrLastInd = tmpArrayList.size() - 1;
        for (int i = 0; i <= arrLastInd; i++) {
//            System.out.println( "s= " + arrLastInd + "  i= " + i );
            Point pnt = (Point) tmpArrayList.get(arrLastInd - i);
            clippingPoints.add(pnt);
        }


    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(240, 100);

        JPanel panel = new TestPanel();

        panel.setBackground(new Color(0x123456));

        mainFrame.add(panel);

        mainFrame.setVisible(true);

    }

    static class TestPanel extends JPanel {

        RoundedRectangle roundedRectangle = new RoundedRectangle();

        public TestPanel() {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            roundedRectangle.setRoundingPolicy(RoundedRectangle.ROUND_LEFT_SIDE);
        }


        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(new Color(0x0000AA));
            roundedRectangle.updateSize(60, 60);
//            g.fillRect( 10, 10, 30, 30 );
            Graphics2D g2 = (Graphics2D) g;

            Paint paint = Patterns.getMediumTexture();
            g2.setPaint(paint);
            g2.fill(roundedRectangle);
        }
    }

}
