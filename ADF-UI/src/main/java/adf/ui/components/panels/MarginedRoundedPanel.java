package adf.ui.components.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Th e class  enhances  the  JPanel  with
 * capability to have margines and rounded
 * cornerns.
 *
 * @author vlakin
 */
public class MarginedRoundedPanel extends JPanel {

    public static int ROUND_UPPER_LEFT = 1;
    public static int ROUND_UPPER_RIGHT = 2;
    public static int ROUND_LOWER_LEFT = 4;
    public static int ROUND_LOWER_RIGHT = 8;

    public static int ROUND_ALL_CORNERS = ROUND_UPPER_LEFT | ROUND_UPPER_RIGHT | ROUND_LOWER_RIGHT | ROUND_LOWER_LEFT;
    public static int ROUND_LEFT_SIDE = ROUND_UPPER_LEFT | ROUND_LOWER_LEFT;
    public static int ROUND_BOTTOM_SIDE = ROUND_LOWER_RIGHT | ROUND_LOWER_LEFT;

    private int topMargin;
    private int leftMargin;
    private int bottomMargin;
    private int rightMargin;

    private int innerWidth;
    private int innerHeight;

    private boolean externalAreaOpaque = true;

    private int roundingPolicy;
    private int thikness;

    private Color outerBorderColor;
    private Color innerBorderColor;
    private Paint backgroundTexture;

    private Color innerBackground;
    private int roundingRadius = 9;

    private ArrayList clippingPpoints = new ArrayList();
    private ArrayList borderPoints = new ArrayList();

    protected Polygon clippingPolygon;
    private int[] borderXPpoints;
    private int[] borderYPpoints;

    private Polygon borderPolygon;
    private boolean overlapCildrenBorder;

    private Border roundedBorder;

    private ComponentAdapter componentAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            recreateClippingShape();
        }

        public void componentShown(ComponentEvent e) {
            recreateClippingShape();
        }
    };

    /**
     * Constructs default instance
     */
    public MarginedRoundedPanel() {
        this(ROUND_LOWER_LEFT);
        this.setBorderProperty(1, 9, true, Color.GRAY, null);
        this.setOpaque(false);
        this.setExternalAreaOpaque(true);
        this.setInnerBackground(new Color(25, 49, 64));
        this.setBackground(new Color(88, 128, 149));
        this.setLayout(new BorderLayout());
    }

    /**
     * Construdts instance with provided rounding policy
     */
    public MarginedRoundedPanel(int roundingPolicy) {
        innerBackground = getBackground();
        setRoundingPolicy(roundingPolicy);
        setBorder(null);
        this.addComponentListener(componentAdapter);
        recreateClippingShape();
    }

    /**
     * @param roundingPolicy
     */
    public void setRoundingPolicy(int roundingPolicy) {
        this.roundingPolicy = roundingPolicy;
        updateInnerArea();
    }

    /**
     * @return
     */
    public int getRoundingPolicy() {
        return roundingPolicy;
    }

    /**
     * @param topMargin
     */
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        updateInnerArea();
    }

    /**
     * @return topMargin
     */
    public int getTopMargin() {
        return topMargin;
    }

    /**
     * @param leftMargin
     */
    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        updateInnerArea();
    }

    /**
     * @return leftMargin
     */
    public int getLeftMargin() {
        return leftMargin;
    }

    /**
     * @param bottomMargin
     */
    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        updateInnerArea();
    }

    /**
     * @return bottomMargin
     */
    public int getBottomMargin() {
        return bottomMargin;
    }

    /**
     * @param rightMargin
     */
    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        updateInnerArea();
    }

    /**
     * @return rightMargin
     */
    public int getRightMargin() {
        return rightMargin;
    }

    /**
     * @param topMargin
     * @param leftMargin
     * @param bottomMargin
     * @param rightMargin
     * @throws IOException
     */
    public void setMargins(int topMargin, int leftMargin, int bottomMargin,
                           int rightMargin) {
        this.topMargin = topMargin;
        this.leftMargin = leftMargin;
        this.bottomMargin = bottomMargin;
        this.rightMargin = rightMargin;
        updateInnerArea();
    }

    /**
     * @return backgroundTexture
     */
    public Paint getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * @param backgroundTexture
     */
    public void setBackgroundTexture(Paint backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    /**
     * @param thikness
     * @param roundingRadius
     * @param overlapCildrenBorder
     * @param outerBorderColor
     * @param innerBorderColor
     */
    public void setBorderProperty(int thikness, int roundingRadius, boolean overlapCildrenBorder,
                                  Color outerBorderColor, Color innerBorderColor) {
        this.thikness = thikness;
        this.roundingRadius = roundingRadius;
        this.overlapCildrenBorder = overlapCildrenBorder;
        if (!overlapCildrenBorder) {
            roundedBorder = BorderFactory.createEmptyBorder(thikness, thikness,
                    (thikness == 0) ? 0 : thikness - 1, thikness);
        }

        Border border = getBorder();
        super.setBorder(BorderFactory.createCompoundBorder(roundedBorder, border));

        this.outerBorderColor = outerBorderColor;
        this.innerBorderColor = innerBorderColor;
    }

    /**
     *
     */
    public void setBorder(Border border) {
        if (roundedBorder != null) {
            if (border != null) {
                super.setBorder(BorderFactory.createCompoundBorder(roundedBorder, border));
            } else {
                super.setBorder(roundedBorder);
            }
        } else {
            super.setBorder(border);
        }
    }

    /**
     * @param state
     */
    public void setExternalAreaOpaque(boolean state) {
        externalAreaOpaque = state;
    }

    /**
     * @return
     */
    public boolean isExternalAreaOpaque() {
        return externalAreaOpaque;
    }

    /**
     * @param background
     */
    public void setInnerBackground(Color background) {
        innerBackground = background;
    }

    /**
     * @return
     */
    public Color getInnerBackground() {
        return innerBackground;
    }

    /**
     * @return
     */
    public Insets getMargins() {
        return new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
    }

    /**
     *
     */
    public Insets getInsets() {
        Insets insets = super.getInsets();
        return new Insets(topMargin + insets.top, leftMargin + insets.left,
                bottomMargin + insets.bottom, rightMargin + insets.right);
    }

    /**
     *
     *
     */
    private void recreateClippingShape() {
        updateInnerArea();
        repaint();
    }

    /**
     *
     *
     */
    private void updateInnerArea() {
        innerWidth = getWidth() - leftMargin - rightMargin;
        innerWidth = innerWidth < 0 ? 0 : innerWidth;
        innerHeight = getHeight() - topMargin - bottomMargin;
        innerHeight = innerHeight < 0 ? 0 : innerHeight;
        createClippingPolygon();
    }

    /**
     *
     *
     */
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

    /**
     *
     *
     */
    private void createClippingPolygon() {
        clippingPpoints.clear();
        borderPoints.clear();

        if ((roundingPolicy & ROUND_UPPER_LEFT) != 0) {
            createRoundedCorner(clippingPpoints, leftMargin + roundingRadius,
                    topMargin + roundingRadius, roundingRadius, 0);
        } else {
            clippingPpoints.add(new Point(leftMargin, topMargin));
        }

        if ((roundingPolicy & ROUND_UPPER_RIGHT) != 0) {
            createRoundedCorner(clippingPpoints, leftMargin +
                            innerWidth - roundingRadius - 1,
                    topMargin + roundingRadius, roundingRadius, 1);
        } else {
            clippingPpoints.add(new Point(leftMargin + innerWidth - 1, topMargin));
        }

        if ((roundingPolicy & ROUND_LOWER_RIGHT) != 0) {
            createRoundedCorner(clippingPpoints, leftMargin +
                            innerWidth - roundingRadius - 1,
                    topMargin + innerHeight - roundingRadius - 1, roundingRadius, 2);
        } else {
            clippingPpoints.add(new Point(leftMargin + innerWidth - 1, topMargin + innerHeight - 1));
        }

        if ((roundingPolicy & ROUND_LOWER_LEFT) != 0) {
            createRoundedCorner(clippingPpoints, leftMargin + roundingRadius,
                    topMargin + innerHeight - roundingRadius - 1, roundingRadius, 3);
        } else {
            clippingPpoints.add(new Point(leftMargin, topMargin + innerHeight - 1));
        }


        int[] clippingXPpoints = new int[clippingPpoints.size()];
        int[] clippingYPpoints = new int[clippingPpoints.size()];
        for (int i = 0; i < clippingPpoints.size(); i++) {
            Point pnt = (Point) clippingPpoints.get(i);
            clippingXPpoints[i] = pnt.x;
            clippingYPpoints[i] = pnt.y;
        }
        clippingPolygon = new Polygon(clippingXPpoints, clippingYPpoints, clippingPpoints.size());
        if (innerBorderColor != null) {
            createBorderPolygon();
        }
    }

    /**
     * @param clippingPoints
     * @param x0
     * @param y0
     * @param r
     * @param quote
     */
    private void createRoundedCorner(ArrayList clippingPoints, int x0, int y0, int r, int quote) {
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

    /*
      public void circle(int x0, int y0, int r, Graphics g){
    int x,y;
    float d;
    x=0;
    y=r;
    d=5/4-r;
    plotPoints(x0,y0,x,y,g);

    while (y>x){
      if (d<0) {
        d=d+2*x+3;
        x++;
      }else {
        d=d+2*(x-y)+5;
        x++;
        y--;
      }
      plotPoints(x0,y0,x,y,g);
    }
  }
     */

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

    /**
     *
     */
    protected void paintBorder(Graphics g) {

        if (isExternalAreaOpaque()) {

            Graphics2D g2 = (Graphics2D) g;
            Border border = getBorder();
            if (border != null) {
                Rectangle rect = g.getClipRect();
                if (thikness > 0) {
                    g.setClip(clippingPolygon);
                }

                border.paintBorder(this, g, leftMargin, topMargin,
                        innerWidth, innerHeight - 1);
                g.setClip(rect);
            }

            if (thikness > 0) {
                if (outerBorderColor != null) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(outerBorderColor);
                    g.drawPolygon(clippingPolygon);
                }

                if (innerBorderColor != null) {
                    g.setColor(innerBorderColor);
                    g.drawPolygon(borderPolygon);
                }
            }

        }

/*
        if (thikness > 0 ){
            if (outerBorderColor != null){
                g.setColor( outerBorderColor );
                g.drawPolygon( clippingPolygon );
            }
            if (innerBorderColor != null){
                g.setColor( innerBorderColor );
                g.drawPolygon(borderPolygon);
            }
        }else{
            Border border = getBorder();
            if ( border != null ){
                border.paintBorder( this, g, leftMargin, topMargin,
                    innerWidth, innerHeight );
            }
        }
 */
    }
//    public void paint(Graphics g){
//        super.paint(g);
//    }

    /**
     *
     */
    protected void paintComponent(Graphics g) {
       /*
        * super of paint(Graphics g ) method paints:
        * 1. paintComponent
        * 2. paintBorder
        * 3. paintChildren
        */
        super.paintComponent(g);

        if (isExternalAreaOpaque()) {
            g.setColor(innerBackground);
            if (backgroundTexture != null) {
                ((Graphics2D) g).setPaint(backgroundTexture);
            } else

            if (innerBackground != null) {
                g.setColor(innerBackground);
            }

            g.fillPolygon(clippingPolygon);
            g.drawPolygon(clippingPolygon);
        }
    }

    /**
     *
     */
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (overlapCildrenBorder) {
            paintBorder(g);
        }
    }

    /**
     *
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame("MarginedRoundedPanel");
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(200, 54);
        mainPanel.setBackground(Color.YELLOW);
        mainPanel.setLayout(new GridLayout(0, 1));

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        MarginedRoundedPanel borderedPanel = null;

        borderedPanel = new MarginedRoundedPanel(ROUND_LEFT_SIDE);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(1, 9, true, Color.GRAY, null);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground( Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());
        JLabel label1 = new JLabel("  Test");
//        label.setOpaque( true );
//        label.setSize( 200, 50 );
//        label.setPreferredSize( new Dimension( 200, 50 ));
//        label.setBackground( Color.CYAN );
//        label.setVerticalAlignment( JLabel.CENTER );
        label1.setHorizontalAlignment(JLabel.LEFT);
        borderedPanel.add(label1, BorderLayout.CENTER);
        mainPanel.add(borderedPanel);

        borderedPanel = new MarginedRoundedPanel(ROUND_UPPER_LEFT);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(0, 9, true, Color.orange, Color.orange);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground(new Color(223, 232, 242));
        borderedPanel.setInnerBackground(Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());

        mainPanel.add(borderedPanel);

        // =================================
        borderedPanel = new MarginedRoundedPanel(ROUND_UPPER_RIGHT);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(0, 9, true, Color.orange, Color.orange);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground(new Color(223, 232, 242));
        borderedPanel.setInnerBackground(Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());

        mainPanel.add(borderedPanel);


        // --------------------------------
        borderedPanel = new MarginedRoundedPanel(ROUND_LOWER_RIGHT);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(0, 9, true, Color.orange, Color.orange);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground(new Color(223, 232, 242));
        borderedPanel.setInnerBackground(Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());

        mainPanel.add(borderedPanel);

        // --------------------------------
        borderedPanel = new MarginedRoundedPanel(ROUND_LOWER_LEFT);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(0, 9, true, Color.orange, Color.orange);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground(new Color(223, 232, 242));
        borderedPanel.setInnerBackground(Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());

        mainPanel.add(borderedPanel);


        // --------------------------------
        borderedPanel = new MarginedRoundedPanel(ROUND_LOWER_LEFT | ROUND_LOWER_RIGHT);
        borderedPanel.setLayout(new GridLayout());
//        borderedPanel.setBorder( BorderFactory.createMatteBorder(6,6,6,6, Color.red) );
        borderedPanel.setBorderProperty(0, 9, true, Color.orange, Color.orange);
        borderedPanel.setPreferredSize(new Dimension(200, 54));
        //twRoundedPanel.setSize( 200, 100 );
        borderedPanel.setOpaque(false);
        borderedPanel.setExternalAreaOpaque(true);
        borderedPanel.setInnerBackground(new Color(223, 232, 242));
        borderedPanel.setInnerBackground(Color.BLACK);
        borderedPanel.setBackground(new Color(88, 128, 149));
        borderedPanel.setMargins(3, 3, 3, 3);
        System.out.println(" after borderedPanel.setMargins");
        borderedPanel.setLayout(new BorderLayout());

        mainPanel.add(borderedPanel);
/*
        JLabel label2 = new JLabel("  Test2");
        label2.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label2, BorderLayout.SOUTH );
        mainPanel.add(borderedPanel);

        // ================ 1  1 ===================
        borderedPanel = new MarginedRoundedPanel( ROUNDING_BOTH );
        borderedPanel.setLayout( new GridLayout( 0, 1 ) );
        borderedPanel.setBackground( new Color( 88, 128, 149 ) );
        borderedPanel.setInnerBackground( new Color( 88, 128, 149 ) );
        borderedPanel.setMargins( 3, 3, 3, 3 );
        borderedPanel.setBorderProperty( 2, 9, true, new Color( 185, 120, 202 ), new Color( 185, 120, 202 ) );
//        insetPanel.setBorder( BorderFactory.createMatteBorder( 1,1,1,1, new Color( 88, 128, 149) ));
        borderedPanel.setPreferredSize( new Dimension( 200, 54 ) );
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
        MarginedRoundedPanel innerBorderedPanel1 = new MarginedRoundedPanel( ROUNDING_BOTH );
        innerBorderedPanel1.setLayout( new BorderLayout() );
        innerBorderedPanel1.setBorder( new EmptyBorder( 0,5,0,5 ) );
        innerBorderedPanel1.setBorderProperty( 0, 9, false, null, null );
        innerBorderedPanel1.setMargins( 0, 0, 1, 0 );
        innerBorderedPanel1.setOpaque( true );
        innerBorderedPanel1.setInnerBackground( new Color( 223, 232, 242 ) );
        innerBorderedPanel1.setPreferredSize( new Dimension( 200, 54 ) );
        innerBorderedPanel1.setOpaque( false );
        innerBorderedPanel1.setRoundedAreaOpaque( true );
        borderedPanel.add( innerBorderedPanel1 );

        MarginedRoundedPanel innerBorderedPanel2 = new MarginedRoundedPanel( ROUNDING_BOTH );
        innerBorderedPanel2.setLayout( new BorderLayout() );
        innerBorderedPanel2.setBorder( new EmptyBorder( 0,5,0,5 ) );
        innerBorderedPanel2.setBorderProperty( 0, 9, false, null, null );
        innerBorderedPanel2.setMargins( 1, 0, 0, 0 );
        innerBorderedPanel2.setOpaque( true );
        innerBorderedPanel2.setInnerBackground( new Color( 223, 232, 242 ) );
        innerBorderedPanel2.setPreferredSize( new Dimension( 200, 54 ) );
        innerBorderedPanel2.setOpaque( false );
        innerBorderedPanel2.setRoundedAreaOpaque( true );
        borderedPanel.add( innerBorderedPanel2 );

        JLabel label3 = new JLabel("Test3");
        label3.setHorizontalAlignment( JLabel.LEFT );
        innerBorderedPanel1.add( label3, BorderLayout.WEST );

        label3 = new JLabel("Test4");
        label3.setHorizontalAlignment( JLabel.LEFT );
        innerBorderedPanel2.add( label3, BorderLayout.WEST );
        mainPanel.add( borderedPanel );

        // ================222222222====================
        borderedPanel = new MarginedRoundedPanel(ROUNDING_BOTH);
        borderedPanel.setLayout( new GridLayout(0,1) );
        borderedPanel.setBackground( new Color( 88, 128, 149) );
        borderedPanel.setInnerBackground( new Color( 25, 49, 64 ) );
        borderedPanel.setBorderProperty( 2, 9, false, new Color( 25, 49, 64 ), new Color( 25, 49, 64 ) );
        borderedPanel.setMargins(3,3,3,3);

        borderedPanel.setBorder( BorderFactory.createMatteBorder( 4,4,4,4, Color.magenta ));
        borderedPanel.setPreferredSize( new Dimension( 200, 54 ));
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
        JLabel label4 = new JLabel("  Test3");
        label4.setForeground(Color.white);
        label4.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label4, BorderLayout.CENTER );
        label4 = new JLabel("   Test4");
        label4.setForeground(Color.white);
        label4.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label4, BorderLayout.SOUTH );
        mainPanel.add(borderedPanel);

        // ==============333333333=========================
        borderedPanel = new MarginedRoundedPanel(ROUNDING_BOTH);
        borderedPanel.setLayout( new GridLayout(0,1) );
        borderedPanel.setBackground( new Color(155, 179, 201) );
        borderedPanel.setInnerBackground( new Color(155, 179, 201) );
        borderedPanel.setMargins(3,3,3,3);
        borderedPanel.setBorderProperty( 2, 9, false, new Color( 68, 100, 131 ), new Color( 223, 232, 242) );

//        borderedPanel.setBorder( BorderFactory.createMatteBorder( 2,2,2,2, new Color( 88, 128, 149) ));
        borderedPanel.setPreferredSize( new Dimension( 200, 34 ));
        //twRoundedPanel.setSize( 200, 100 ); new Color( 223, 232, 242)
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
//        JTextField textField = new JTextField();
//        textField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        label3 = new JLabel("  Test4");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.WEST );
        label3 = new JLabel("  Test5");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.WEST );
        mainPanel.add(borderedPanel);

        // ==============333 1111=========================
        borderedPanel = new MarginedRoundedPanel(ROUNDING_BOTH);
        borderedPanel.setLayout( new GridLayout(0,1) );
        borderedPanel.setBackground( new Color( 25, 49, 64 ) );
        borderedPanel.setInnerBackground( new Color(155, 179, 201) );
        borderedPanel.setMargins(3,3,3,3);
        borderedPanel.setBorderProperty( 2, 9, false, new Color( 68, 100, 131 ), new Color( 223, 232, 242) );
//        borderedPanel.setBorder( BorderFactory.createMatteBorder( 2,2,2,2, new Color( 88, 128, 149) ));
        borderedPanel.setPreferredSize( new Dimension( 200, 34 ));
        //twRoundedPanel.setSize( 200, 100 ); new Color( 223, 232, 242)
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
        label3 = new JLabel("  Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.CENTER );
        label3 = new JLabel("  Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.SOUTH );
        mainPanel.add(borderedPanel);
        // ==============333 222=========================
        borderedPanel = new MarginedRoundedPanel(ROUNDING_BOTH);
        borderedPanel.setLayout( new GridLayout(0,1) );
        borderedPanel.setBackground( new Color( 25, 49, 64 ) );
        borderedPanel.setInnerBackground( new Color( 25, 49, 64 ) );
        borderedPanel.setMargins(3,3,3,3);
        borderedPanel.setBorderProperty( 1, 9, false, new Color( 68, 100, 131 ), null );
//        borderedPanel.setBorder( BorderFactory.createMatteBorder( 2,2,2,2, new Color( 88, 128, 149) ));
        borderedPanel.setPreferredSize( new Dimension( 200, 34 ));
        //twRoundedPanel.setSize( 200, 100 ); new Color( 223, 232, 242)
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
        label3 = new JLabel("  Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.CENTER );
        label3 = new JLabel("  Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.SOUTH );
        mainPanel.add(borderedPanel);

        // ==============4=======================
        borderedPanel = new MarginedRoundedPanel(ROUNDING_RIGHT);
        borderedPanel.setLayout( new GridLayout(0,1) );
        borderedPanel.setBackground( new Color( 88, 128, 149) );
        borderedPanel.setInnerBackground( new Color( 223, 232, 242) );
        borderedPanel.setMargins(3,3,3,3);
        borderedPanel.setBorderProperty( 1, 9, false, null, null );
        borderedPanel.setBorder( BorderFactory.createEmptyBorder( 3,3,3,3 ));
        borderedPanel.setPreferredSize( new Dimension( 200, 34 ));
        //twRoundedPanel.setSize( 200, 100 ); new Color( 223, 232, 242)
        borderedPanel.setOpaque( true );
        borderedPanel.setRoundedAreaOpaque( true );
        label3 = new JLabel("Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.CENTER );
        label3 = new JLabel("Test");
        label3.setHorizontalAlignment( JLabel.LEFT );
        borderedPanel.add( label3, BorderLayout.SOUTH );
        mainPanel.add(borderedPanel);

        // ==============5=======================
         borderedPanel = new MarginedRoundedPanel(ROUNDING_LEFT);
         borderedPanel.setLayout( new GridLayout(0,1) );
         borderedPanel.setBackground( new Color( 88, 128, 149) );
         borderedPanel.setInnerBackground( new Color( 25, 49, 64 ) );
         borderedPanel.setMargins(3,3,3,0);
         borderedPanel.setBorderProperty( 0, 9, false, null, null );
//        borderedPanel.setBorder( BorderFactory.createMatteBorder( 2,2,2,2, new Color( 88, 128, 149) ));
         borderedPanel.setPreferredSize( new Dimension( 200, 34 ));
         //twRoundedPanel.setSize( 200, 100 ); new Color( 223, 232, 242)
         borderedPanel.setOpaque( true );
         borderedPanel.setRoundedAreaOpaque( true );
         label3 = new JLabel( "Test" );
         label3.setForeground( Color.white );
         label3.setHorizontalAlignment( JLabel.LEFT );
         borderedPanel.add( label3, BorderLayout.CENTER );
         label3 = new JLabel( "Test" );
         label3.setForeground( Color.white );
         label3.setHorizontalAlignment( JLabel.LEFT );
         borderedPanel.add( label3, BorderLayout.SOUTH );
*/

        //      frame.getContentPane().setLayout( new BorderLayout() );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}