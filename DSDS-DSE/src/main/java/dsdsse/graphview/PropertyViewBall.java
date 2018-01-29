package dsdsse.graphview;

import dsdsse.preferences.DsdsseUserPreference;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;

/**
 * Created by Admin on 9/28/2016.
 */
public class PropertyViewBall {

    private static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
//    private static final Color DEFAULT_BALL_COLOR = new Color(0xF0F0F0); //Color.LIGHT_GRAY;


    private static int preferenceRadius = 0;
    private static boolean displayMclnPropertyViewAs3DCircle;

    private static byte[] data;
    private static int hx;
    private static int hy;
    private static final int bgGrey = 208; // 192
    private static int maxR;

    /**
     * @param radius
     * @return
     */
    public static byte[] prepareDataWIthRadius(int radius) {
        PropertyViewBall.hx = radius / 2;
        PropertyViewBall.hy = radius / 2;
        int R = radius;
        int D = 2 * R + 1;
        int rPlusOne = R + 1;
        byte[] data = new byte[(D * D)];
        int mr = 0;
        try {
//        for (int Y = 2 * R; --Y >= 0; ) {
            for (int Y = 0; Y < D; Y++) {
//                int x0 = (int) (Math.sqrt(rPlusOne * rPlusOne - ((Y+1) - rPlusOne) * ((Y+1) - rPlusOne))   );
                int x0 = (int) (Math.sqrt(R * R - ((Y) - R) * ((Y) - R)) + 0.5);
                if (x0 == 0) {
                    x0++;
                    x0++;
                }
                if (Y == (R - 1) || Y == (R + 1)) {
                    x0++;
                }
//            int index = (Y * D + R - x0) + 1 + Y;
                int rowIndex = rPlusOne - x0 - 1;
                int index = Y * D + rowIndex;
//            for (int X = -x0 ; X < (x0); X++) {
                for (int X = -x0; X <= (x0); X++) {
                    int x = X + hx;
                    int y = Y - R + hy;
                    int r = (int) (Math.sqrt(x * x + y * y));
                    if (r > mr) {
                        mr = r;
                    }
                    data[index++] = r <= 0 ? 1 : (byte) r;
                }
            }
            maxR = mr;
        } catch (Exception e) {
            System.out.println();
        } finally {
            return data;
        }
    }

    //   I n i t i a l i z i n g   B a l l   d i s p l a y

    // setting radius
    public static void setBallSize(int radius) {
        PropertyViewBall.preferenceRadius = radius;
        PropertyViewBall.data = PropertyViewBall.prepareDataWIthRadius(radius);
    }

    // setting 3D/plain circle representation
    public static void setDisplayMclnPropertyViewAs3DCircle(boolean displayMclnPropertyViewAs3DCircle) {
        PropertyViewBall.displayMclnPropertyViewAs3DCircle = displayMclnPropertyViewAs3DCircle;
    }

    //
    //   I n s t a n c e
    //

    public static final PropertyViewBall createInstance(Color ballColor) {
        return new PropertyViewBall(ballColor);
    }

    private int displayRadius;
    private Color currentStateColor;
    private Image watermarkBall;
    private Image ballImage;

    /**
     * @param ballColor
     */
    private PropertyViewBall(Color ballColor) {
        setState(ballColor);
    }

    /**
     * @param ballColor
     */
    public void setState(Color ballColor) {
        displayRadius = preferenceRadius;
        ballImage = initBallImage(displayRadius, ballColor);
        watermarkBall = initBallImage(displayRadius, ballColor);
        this.currentStateColor = ballColor;
    }

    /**
     * @param ballColor
     * @return
     */
    private Image initBallImage(int displayRadius, Color ballColor) {
        byte red[] = new byte[256];
        red[0] = (byte) bgGrey;
        byte green[] = new byte[256];
        green[0] = (byte) bgGrey;
        byte blue[] = new byte[256];
        blue[0] = (byte) bgGrey;

        int Rl = ballColor.getRed();
        int Gl = ballColor.getGreen();
        int Bl = ballColor.getBlue();

        for (int i = maxR; i >= 1; --i) {
            float d = (float) i / maxR;
            red[i] = (byte) blend(blend(Rl, 255, d), bgGrey, 1);
            green[i] = (byte) blend(blend(Gl, 255, d), bgGrey, 1);
            blue[i] = (byte) blend(blend(Bl, 255, d), bgGrey, 1);
        }
        IndexColorModel model = new IndexColorModel(8, maxR + 1, red, green, blue, 0);
        int D = 2 * displayRadius + 1;
        return ballImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(D, D, model, data, 0, D));
    }

    private final int blend(int fg, int bg, float fgfactor) {
        return (int) (bg + (fg - bg) * fgfactor);
    }

    /**
     * @param g
     * @param scrX
     * @param scrY
     */
    public void draw(Graphics g, int scrX, int scrY, boolean watermarked) {

        if (displayRadius != preferenceRadius) {
            displayRadius = preferenceRadius;
            ballImage = initBallImage(displayRadius, currentStateColor);
            watermarkBall = initBallImage(displayRadius, currentStateColor);
        }

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (displayMclnPropertyViewAs3DCircle) {
            if (watermarked) {
                drawWatermarkBoll(g, scrX, scrY, displayRadius);
            } else {
                draw3DBoll(g, scrX, scrY, displayRadius);
            }
            g2D.setColor(Color.GRAY);
        } else {
            cSysFillScrCircle(g, currentStateColor, scrX, scrY, displayRadius);
            g2D.setColor(Color.GRAY);
        }

//        parentCSys.csysDrawScrCircle(g, Color.GRAY, scrX, scrY, radius);
        g.drawOval(scrX - displayRadius, scrY - displayRadius, 2 * displayRadius, 2 * displayRadius);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    private void draw3DBoll(Graphics g, int x, int y, int r) {
        int size = (2 * r) + 1;
        x = x - (size >> 1);
        y = y - (size >> 1);
        g.drawImage(ballImage, x, y, size, size, null);
    }

    private void drawWatermarkBoll(Graphics g, int x, int y, int r) {
        int size = 2 * r;
        x = x - (size >> 1) + 1;
        y = y - (size >> 1) + 1;
        g.drawImage(watermarkBall, x, y, size - 1, size - 1, null);
    }

    private void paintBorder(Graphics g, int scrX, int scrY) {

//        g.setColor(getCircleColor());
//        g.drawOval(scrX - RADIUS, scrY - RADIUS, 2 * RADIUS, 2 * RADIUS);
//
//        if (!(isPreSelected() || isSelected())) {
//            return;
//        }
//
//        Graphics2D g2D = (Graphics2D) g;
//        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
//        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        Stroke currentStroke = g2D.getStroke();
//        g2D.setStroke(new BasicStroke(1));
//
//        if (isPreSelected() && !isSelected()) {
//            g2D.setColor(getPreSelectedColor());
//        }
//        if (!isPreSelected() && isSelected()) {
//            g2D.setColor(getSelectedColor());
//        }
//        if (isPreSelected() && isSelected()) {
//            g2D.setColor(getSelectedColor());
//        }
//        g.drawOval(scrX - SELECTED_CIRCLE_RADIUS, scrY - SELECTED_CIRCLE_RADIUS, 2 * SELECTED_CIRCLE_RADIUS,
//                2 * SELECTED_CIRCLE_RADIUS);
//
//        g2D.setStroke(currentStroke);
//        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    /**
     * @param g
     * @param drawColor
     * @param scrX
     * @param scrY
     * @param scrRadius
     */
    public void csysDrawScrCircle(Graphics g, Color drawColor, int scrX, int scrY, int scrRadius) {
        g.setColor(drawColor);
        Graphics2D gg = (Graphics2D) g;
//        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        circle(gg, false, scrX, scrY, scrRadius);
    }

    /**
     * @param g
     * @param fillColor
     * @param scrX
     * @param scrY
     * @param scrRadius
     */
    public void cSysFillScrCircle(Graphics g, Color fillColor,
                                  int scrX, int scrY, int scrRadius) {
        g.setColor(fillColor);
        Graphics2D gg = (Graphics2D) g;
        circle(gg, true, scrX, scrY, scrRadius);
//        int x1 = scrX - scrRadius;
//        int y1 = scrY - scrRadius;
//        int x2 = (scrRadius << 1) + 1;
//        int y2 = (scrRadius << 1) + 1;
//        g.setColor(fillColor);
//        g.fillOval(x1, y1, x2, y2);
    }

    /**
     * Circle is the Bresenham's algorithm for a scan converted circle
     *
     * @param g
     * @param x0
     * @param y0
     * @param r
     */
    private void circle(Graphics g, boolean fill, int x0, int y0, int r) {
        int dx, dy;
        float d;
        dx = 0;
        dy = r;
        d = 5 / 4 - r;
        if (fill) {
            fillPoints(x0, y0, dx, dy, g);
        } else {
            plotPoints(x0, y0, dx, dy, g);
        }
        while (dy > dx) {
            if (d < 0) {
                d = d + 2 * dx + 3;
            } else {
                d = d + 2 * (dx - dy) + 5;
                dy--;
            }
            dx++;
            if (fill) {
                fillPoints(x0, y0, dx, dy, g);
            } else {
                plotPoints(x0, y0, dx, dy, g);
            }
        }
    }

    /**
     * @param x0
     * @param y0
     * @param dx
     * @param dy
     * @param g
     */
    private void plotPoints(int x0, int y0, int dx, int dy, Graphics g) {
        g.drawLine(x0 + dx, y0 + dy, x0 + dx, y0 + dy);
        g.drawLine(x0 + dy, y0 + dx, x0 + dy, y0 + dx);
        g.drawLine(x0 + dy, y0 - dx, x0 + dy, y0 - dx);
        g.drawLine(x0 + dx, y0 - dy, x0 + dx, y0 - dy);
        g.drawLine(x0 - dx, y0 - dy, x0 - dx, y0 - dy);
        g.drawLine(x0 - dy, y0 - dx, x0 - dy, y0 - dx);
        g.drawLine(x0 - dy, y0 + dx, x0 - dy, y0 + dx);
        g.drawLine(x0 - dx, y0 + dy, x0 - dx, y0 + dy);
    }

    private void fillPoints(int x0, int y0, int dx, int dy, Graphics g) {
        int x1 = x0 + dx;
        int y1 = y0 + dy;

        int x2 = x0 + dy;
        int y2 = y0 + dx;

        int x3 = x0 + dy;
        int y3 = y0 - dx;

        int x4 = x0 + dx;
        int y4 = y0 - dy;

        int x5 = x0 - dx;
        int y5 = y0 - dy;

        int x6 = x0 - dy;
        int y6 = y0 + dx;

        int x7 = x0 - dy;
        int y7 = y0 - dx;

        int x8 = x0 - dx;
        int y8 = y0 + dy;

        g.drawLine(x1, y1, x8, y8);
        g.drawLine(x2, y2, x6, y6);
        g.drawLine(x3, y3, x7, y7);
        g.drawLine(x4, y4, x5, y5);
    }
}
