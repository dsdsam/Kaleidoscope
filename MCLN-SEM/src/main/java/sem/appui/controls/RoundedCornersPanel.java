package sem.appui.controls;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 12, 2011
 * Time: 7:58:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundedCornersPanel extends JPanel {
     public static int ROUNDING_NONE = 0;
    public static int ROUNDING_LEFT = 1;
    public static int ROUNDING_RIGHT = 2;
    public static int ROUNDING_BOTH = 3;

    static int[][][] BORDER_OR04 = {
            {{2, 0}, {2, -1}, {1, -1}, {1, -2}, {0, -2}},
            {{0, 2}, {1, 2}, {1, 1}, {2, 1}, {2, 0}},
            {{-2, 0}, {-2, 1}, {-1, 1}, {-1, 2}, {0, 2}},
            {{0, -2}, {-1, -2}, {-1, -1}, {-2, -1}, {-2, 0}}};

    static int[][][] BORDER_IR04 = {
            {{3, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -3}},
            {{1, 3}, {2, 3}, {2, 2}, {3, 2}, {3, 1}},
            {{-3, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 3}},
            {{-1, -3}, {-2, -3}, {-2, -2}, {-3, -2}, {-3, -1}}};

    static int[][][] BORDER_OR05 = {
            {{4, 0}, {3, -1}, {2, -1}, {1, -2}, {1, -3}, {0, -4}},
            {{0, 4}, {1, 3}, {1, 2}, {2, 1}, {3, 1}, {4, 0}},
            {{-4, 0}, {-3, 1}, {-2, 1}, {-1, 2}, {-1, 3}, {0, 4}},
            {{0, -4}, {-1, -3}, {-1, -2}, {-2, -1}, {-3, -1},
                    {-4, 0}}};

    static int[][][] BORDER_IR05 = {
            {{4, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -4}},
            {{1, 4}, {2, 3}, {2, 2}, {3, 2}, {4, 1}},
            {{-4, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 4}},
            {{-1, -4}, {-2, -3}, {-2, -2}, {-3, -2}, {-4, -1}}};

       private int thickness;
    private int roundingRadius;
    private int roundingPolicy;
    private Color outerBorderColor;
    private Polygon outerClipPolygon;
    private Polygon outerBorderPolygon;
    private boolean externalAreaOpaque = true;
    private Color externalBackground = Color.LIGHT_GRAY;
    private Insets externalInsets = new Insets(0, 0, 0, 0);
    private Color innerBorderColor;
    private Polygon innerBorderPolygon;
    private boolean internalAreaOpaque = true;
    private Color internalBackground = Color.LIGHT_GRAY;
    private Insets internalInsets = new Insets(0, 0, 0, 0);
    private int currentWidth;
    private int currentHeight;

    public RoundedCornersPanel() {
    }

    public RoundedCornersPanel(int thickness, int roundingRadius,
                               int roundingPolicy, Color outerBorderColor, Color innerBorderColor) {
        this.thickness = thickness;
        this.roundingRadius = roundingRadius;
        this.roundingPolicy = roundingPolicy;
        this.outerBorderColor = outerBorderColor;
        this.innerBorderColor = innerBorderColor;
        this.setOpaque(false);
    }

    public void setProperties(int thickness, int roundingRadius,
                              int roundingPolicy, Color outerBorderColor, Color innerBorderColor) {
        this.thickness = thickness;
        this.roundingRadius = roundingRadius;
        this.roundingPolicy = roundingPolicy;
        this.outerBorderColor = outerBorderColor;
        this.innerBorderColor = innerBorderColor;
        this.setOpaque(false);
    }

    public void setRoundingPolicy(int roundingPolicy) {
        this.roundingPolicy = roundingPolicy;
    }

    public void setRoundingRadius(int roundingRadius) {
        this.roundingRadius = roundingRadius;
    }

    // E x t e r n a l   A r e a

    public boolean isExternalAreaOpaque() {
        return externalAreaOpaque;
    }

    public void setExternalAreaOpaque(boolean externalAreaOpaque) {
        this.externalAreaOpaque = externalAreaOpaque;
    }

    public void setExternalBackground(Color externalBackground) {
        this.externalBackground = externalBackground;
    }

    public void setExternalInsets(int top, int left, int bottom, int right) {
        externalInsets = new Insets(top, left, bottom, right);
        super.setBorder(new EmptyBorder(externalInsets.top + internalInsets.top,
                externalInsets.left + internalInsets.left,
                externalInsets.bottom + internalInsets.bottom,
                externalInsets.right + internalInsets.right));
    }

    public void setOuterBorderColor(Color outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
    }

    // I n t e r n a l   A r e a

    public boolean isInternalAreaOpaque() {
        return internalAreaOpaque;
    }

    public void setInternalAreaOpaque(boolean internalAreaOpaque) {
        this.internalAreaOpaque = internalAreaOpaque;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setInternalBackground(Color internalBackground) {
        this.internalBackground = internalBackground;
    }

    public void setInternalInsets(int top, int left, int bottom, int right) {
        internalInsets = new Insets(top, left, bottom, right);
        super.setBorder(new EmptyBorder(externalInsets.top + internalInsets.top,
                externalInsets.left + internalInsets.left,
                externalInsets.bottom + internalInsets.bottom,
                externalInsets.right + internalInsets.right));
    }

    public void setInnerBorderColor(Color innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
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
//        width--;
        height--;
        if (roundingPolicy == ROUNDING_LEFT || roundingPolicy == ROUNDING_BOTH) {
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
        if (roundingPolicy == ROUNDING_RIGHT || roundingPolicy == ROUNDING_BOTH) {
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

    public void paint(Graphics g) {

        if (roundingRadius == 0) {
            super.paint(g);
            return;
        }

        Rectangle origRect = g.getClipBounds();
//           System.out.println("origRect " + origRect.x + "  " + origRect.y + "  " + origRect.width + "  " + origRect.height);
        if (origRect.width < 50) {
            return;
        }

        if (externalAreaOpaque) {
            g.setColor(externalBackground);
            g.fillRect(origRect.x, origRect.y, origRect.width, origRect.height);
        }

        int x = origRect.x;
        int y = origRect.y;
        int width = origRect.width - 1;
        int height = origRect.height;


        x += externalInsets.left;
        y += externalInsets.right;
        width -= (externalInsets.left + externalInsets.right);
        height -= (externalInsets.top + externalInsets.bottom);

        if (currentWidth != width || currentHeight != height) {
            currentWidth = width;
            currentHeight = height;
            if (roundingRadius == 4) {
                outerClipPolygon = buildShape(x, y, width, height,
                        BORDER_OR04, true);
                outerBorderPolygon = buildShape(x, y, width - 1, height - 1,
                        BORDER_OR04, true);
//                if (thickness > 1) {
                innerBorderPolygon = buildShape(x, y, width - 1, height - 1,
                        BORDER_IR04, false);
//                }
            }
            if (roundingRadius == 5) {
                outerClipPolygon = buildShape(x, y, width, height,
                        BORDER_OR04, false);
                outerBorderPolygon = buildShape(x, y, width - 1, height - 1,
                        BORDER_OR05, true);
//                if (thickness > 1) {
                innerBorderPolygon = buildShape(x, y, width - 1, height - 1,
                        BORDER_IR05, false);
//                }
            }
        }
        Shape origClipShape = g.getClip();
        g.setClip(outerClipPolygon);
//        super.paint(g);
//        g.setClip(origClipShape);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (thickness >= 1) {
            g.setColor(outerBorderColor);
            g.drawPolygon(outerBorderPolygon);
        }
        if (thickness == 2) {
            g.setColor(innerBorderColor);
            g.drawPolygon(innerBorderPolygon);
        }
    }

    /**
     * @param args
     */
    public static void main(String args[]) {

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(600, 600);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.setLayout(new BorderLayout());
        Color BORDER_COLOR = Color.WHITE;//new Color(0x00DD00);
        RoundedCornersPanel roundedCornersPanel  = new RoundedCornersPanel(1, 5,
                ROUNDING_BOTH, Color.YELLOW, Color.RED);


        roundedCornersPanel.setBackground(new Color(0x00AA00));
        Dimension buttonSize = new Dimension(37, 37);
        roundedCornersPanel.setSize(buttonSize);

//        roundedCornersButton.setProperties(1, 4, DirectionSetupButton.ROUNDING_ALL, BORDER_COLOR, Color.RED);

        mainPanel.add(roundedCornersPanel);

        JFrame frame = new JFrame("Testing Frame");
        frame.setSize(600, 600);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
