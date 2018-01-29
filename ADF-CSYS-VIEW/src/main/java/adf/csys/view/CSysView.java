/*
 * Created on Jul 31, 2005
 *
 */
package adf.csys.view;

import vw.valgebra.VAlgebra;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author xpadmin
 */

/**
 * The Coordinate System class. This class is a part of
 * Application Development Framework and intended to represent
 * the coordinate system and a set of drawn in the system
 * graphical entities on the screen.
 * <p>
 *
 * @author Vladimir Lakin
 * @version 1.2 05/06/03
 * @since 1.0
 */
public abstract class CSysView extends JComponent {

    protected static final Logger logger = Logger.getLogger(CSysView.class.getName());

    public static final int YOXProjection = 1;
    public static final int ZOXProjection = 2;
    public static final int ZOYProjection = 3;

    public static final int DRAW_NONE = 0;
    public static final int DRAW_AXIS = 1;
    public static final int DRAW_VALUES = 2;

    private Color background = Color.BLACK;

    private int projection = YOXProjection;

    private DoubleRectangle cSysViewRectangle;
    private double cSysX;
    private double projectSpaceX1;
    private double projectSpaceX2;
    private double projectSpaceWidth;
    private double projectSpaceHeight;
    private double cSysY;
    private double projectSpaceY1;
    private double projectSpaceY2;
    private double cSysWidth;
    private double cSysHeight;
    private double cSysX0;
    private double cSysY0;

    private int options;

    private double xScale;
    private double yScale;
    protected double minScale;

    private int scrX;
    private int scrX0;
    private int scrY;
    private int scrY0;
    protected int[] scr0 = new int[3];
    public int projectSpaceViewXMin = 0;
    public int projectSpaceViewYMin = 0;
    public int projectSpaceViewXMax = 0;
    public int projectSpaceViewYMax = 0;
    private boolean applyPadding;
    private int viewPadding;
    private int doublePadding;

    protected int projectSpaceViewWidth = 0;
    protected int projectSpaceViewHeight = 0;

    private Rectangle cSysScreenRect = new Rectangle();
    private Rectangle projectSpaceScrRectangle = new Rectangle();
    private Rectangle cSysAxisRect = new Rectangle();

    // axes line ends
    private int x_Axis_R_End, x_Axis_L_End;
    private int y_Axis_U_End, y_Axis_B_End;

    // grid
    private List<CSysEntity> gridEntityList;
    protected boolean gridVisible;
    private double gridStep;

    // world representation
    private List<CSysEntity> worldEntityList = new ArrayList();
    private List<BasicCSysEntity> worldAxesList = new ArrayList();

    // R o t a t i o n
    private double xRotationAngle;
    double zRotationAngle;
    protected final double[][] xRotatingMatrix = new double[4][3];
    protected final double[][] yRotatingMatrix = new double[4][3];
    protected final double[][] zRotatingMatrix = new double[4][3];
    protected final double[][] combinedRotatingMatrix = new double[4][3];
    private final double[][] perspectiveMatrix = new double[4][3];

    private ComponentAdapter componentAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            Dimension currentSize = getSize();
            if (currentSize.width <= 0) {
                // return if panel is not visible on the screen
                return;
            }
            resetViewScaleAndRescaleEntityList();
            updatePresentationUponViewResized();
            // this repaint is important to update this view after holding
            // panel content changed and layout rearranged component sizes
            CSysView.this.repaint();
        }

        public void componentShown(ComponentEvent e) {
            Dimension currentSize = getSize();
            if (currentSize.width <= 0) {
                // return if panel is not visible on the screen
                return;
            }
            resetViewScaleAndRescaleEntityList();
            updatePresentationUponViewResized();
            // this repaint is important to update this view after holding
            // panel content changed and layout rearranged component sizes
            CSysView.this.repaint();
        }
    };

    /**
     * @param cSysX
     * @param cSysY
     * @param cSysWidth
     * @param cSysHeight
     * @param options
     */
    public CSysView(double cSysX, double cSysY, double cSysWidth, double cSysHeight, int viewPadding, int options) {
        setBorder(null);
        DoubleRectangle cSysViewRectangle = new DoubleRectangle(cSysX, cSysY, cSysWidth, cSysHeight);
        this.setBackground(background);
        init(cSysViewRectangle, options);
        this.addComponentListener(componentAdapter);
        this.viewPadding = viewPadding;
        this.doublePadding = viewPadding + viewPadding;
    }

    /**
     * Added for debugging to to put break point and intercept call
     *
     * @param border
     */
    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    /**
     * Called from extending class when model cSys rectangle changed
     *
     * @param cSysViewRectangle
     */
    protected void updateViewRectangle(DoubleRectangle cSysViewRectangle) {
        init(cSysViewRectangle.getCopy(), options);           // calling private method
        // will update extending class
        onProjectCSysRectangleReset();                    // calling protected method
        // will rescale view data of this class and update entities
        resetViewScaleAndRescaleEntityList();              // calling private method
    }

    /**
     * @param cSysViewRectangle
     * @param options
     */
    private void init(DoubleRectangle cSysViewRectangle, int options) {
        this.cSysViewRectangle = cSysViewRectangle;
        this.cSysX = cSysViewRectangle.getX();
        projectSpaceX1 = cSysX;
        this.cSysWidth = cSysViewRectangle.getWidth();
        this.projectSpaceX2 = projectSpaceX1 + cSysWidth;

        this.cSysY = cSysViewRectangle.getY();
        projectSpaceY1 = cSysY;
        this.cSysHeight = cSysViewRectangle.getHeight();
        this.projectSpaceY2 = projectSpaceY1 - cSysHeight;

        projectSpaceWidth = projectSpaceX2 - projectSpaceX1;
        projectSpaceHeight = projectSpaceY1 - projectSpaceY2;

        this.options = options;


        VAlgebra.initPitchXRotMat43(xRotatingMatrix, 0, null);
//        VAlgebra.printMat43(xRotatingMatrix);
        VAlgebra.initZRotMat43(zRotatingMatrix, 0, null);
//        VAlgebra.printMat43(zRotatingMatrix);
        VAlgebra.initZRotMat43(combinedRotatingMatrix, 0, null);
//        VAlgebra.printMat43(combinedRotatingMatrix);
        VAlgebra.initPerspectiveMat43(perspectiveMatrix);
//        perspectiveMatrix
//        logger.info(" projectSpaceX1     = " + projectSpaceX1);
//        logger.info(" projectSpaceY1     = " + projectSpaceY1);
//        logger.info(" projectSpaceX2     = " + projectSpaceX2);
//        logger.info(" projectSpaceY2     = " + projectSpaceY2);
//        logger.info(" projectSpaceWidth  = " + projectSpaceWidth);
//        logger.info(" projectSpaceHeight = " + projectSpaceHeight);
    }

    /**
     * Exists to update overriding class
     * May be overridden
     */
    protected void onProjectCSysRectangleReset() {

    }

    /**
     *
     */
    private void resetViewScaleAndRescaleEntityList() {

        // prepare position
        Rectangle rect = getBounds();
        cSysScreenRect.setBounds(0, 0, rect.width - doublePadding, rect.height - doublePadding);
//        cSysScreenRect.grow(-padding, -padding);
        cSysAxisRect.setBounds(0, 0, rect.width, rect.height);
        cSysAxisRect.grow(-80, -80);

        // rescale
        xScale = (cSysScreenRect.width) / cSysWidth;
        yScale = (cSysScreenRect.height) / cSysHeight;
//        System.out.println("prepareCSys: cSysScreenRect x= "+cSysScreenRect.x+" y= "+cSysScreenRect.y
//                +" width= "+cSysScreenRect.width+" height= "+cSysScreenRect.height);
        if (xScale < yScale) {
            minScale = xScale;
            gridStep = cSysWidth;
        } else {
            minScale = yScale;
            gridStep = cSysHeight;
        }
//        minScale = minScale * 0.95;
//        minScale = minScale * 0.4;
//        System.out.println("prepareCSys: Scale x= "+xScale+" y= "+yScale+" minScale= "+minScale);
        // count X0 position
        if (cSysX < 0 && 0 < (cSysX + cSysWidth)) {
            cSysX0 = 0;
            scrX0 = (int) Math.rint((cSysX0 - cSysX) * xScale + viewPadding);
            //       scr_X0 = cSysRect.x + (int)( (world_X0 - world_X_Min) * xScale);
        } else {
            cSysX0 = cSysX;
            scrX0 = 0;
        }
        scr0[0] = scrX0;

        // count Y0 position
        if (cSysY > 0 && (cSysY - cSysHeight) < 0) {
            cSysY0 = 0;
            scrY0 = (int) Math.rint(((cSysY) - cSysY0) * yScale + viewPadding);
            //       scr_Y0 = cSysRect.y + (int)( (world_Y_Max -  world_Y0) * yScale);
        } else {
            cSysY0 = cSysY;
            scrY0 = (int) Math.rint((cSysHeight) * yScale + viewPadding);
        }
//        if (cSysY < 0 && (cSysY + cSysHeight) > 0) {
//            cSysY0 = 0;
//            scrY0 = (int) Math.rint((((cSysY + cSysHeight) - cSysY0) * yScale));
//            //       scr_Y0 = cSysRect.y + (int)( (world_Y_Max -  world_Y0) * yScale);
//        } else {
//            cSysY0 = cSysY;
//            scrY0 = (int) Math.rint(((cSysHeight) * yScale));
//        }
        scr0[1] = scrY0;
        scr0[2] = scrY0;

//        System.out.println("prepareCSys: cSysX0= "+cSysX0+" cSysY0= "+cSysY0);
//        System.out.println("prepareCSys: scrX0= "+scrX0+" scrY0= "+scrY0);
        x_Axis_L_End = cSysAxisRect.x;
        x_Axis_R_End = cSysAxisRect.x + cSysAxisRect.width - 1;
        y_Axis_B_End = cSysAxisRect.y + cSysAxisRect.height - 1;
        y_Axis_U_End = cSysAxisRect.y;

        projectSpaceViewXMin = cSysXScalarToScr(projectSpaceX1);
        projectSpaceViewYMin = cSysYScalarToScr(projectSpaceY1);
        projectSpaceViewXMax = cSysXScalarToScr(projectSpaceX2);
        projectSpaceViewYMax = cSysYScalarToScr(projectSpaceY2);

        projectSpaceViewWidth = projectSpaceViewXMax - projectSpaceViewXMin;
        projectSpaceViewHeight = projectSpaceViewYMax - projectSpaceViewYMin;

        projectSpaceScrRectangle = new Rectangle(projectSpaceViewXMin, projectSpaceViewYMin,
                projectSpaceViewWidth, projectSpaceViewHeight);

        updateCSysEntList(combinedRotatingMatrix);
    }

    public boolean isPointInsideProjectRectangle(int x, int y) {
        boolean projectSpaceScrRectangleContainsPoint = projectSpaceScrRectangle.contains(x, y);
        return projectSpaceScrRectangleContainsPoint;
    }


    public int[] getCSysScrZeroVector() {
        int[] zeroVec = {scrY0, scrY0, 0};
        return zeroVec;
    }


    public DoubleRectangle getCSysRectangle() {
        return cSysViewRectangle.getCopy();
    }

    public double scrScalarToCSys(double scrScalar) {
        return scrScalar / minScale;
    }

    public int cSysScalarToScr(double cSysScalar) {
        return (int) Math.rint(cSysScalar * minScale);
    }

    //
    //   int to double / double to int conversion
    //

    public int[][] doubleMatX3ToIntMatX3(double[][] doubleMat) {
        int[][] intMat = new int[doubleMat.length][];
        for (int i = 0; i < doubleMat.length; i++) {
            double[] currentPoint = doubleMat[i];
            intMat[i] = doubleVec3ToInt(currentPoint);
        }
        return intMat;
    }

    /**
     * @param doubleVec
     * @return
     */
    public int[] doubleVec3ToInt(double[] doubleVec) {
        int intVec[] = new int[3];
        return doubleVec3ToInt(intVec, doubleVec);
    }

    public int[] doubleVec3ToInt(int[] intVec, double[] doubleVec) {
        intVec[0] = (int) Math.rint(doubleVec[0]);
        intVec[1] = (int) Math.rint(doubleVec[1]);
        intVec[2] = (int) Math.rint(doubleVec[2]);
        return intVec;
    }

    /**
     * @param intVec
     * @return
     */
    public double[] intVec3ToDouble(int[] intVec) {
        double doubleVec[] = new double[3];
        return intVec3ToDouble(doubleVec, intVec);
    }

    public double[] intVec3ToDouble(double doubleVec[], int intVec[]) {
        doubleVec[0] = intVec[0];
        doubleVec[1] = intVec[1];
        doubleVec[2] = intVec[2];
        return doubleVec;
    }

    //   S c a l e

    public double[] scaleScrPntToCSysPnt(double[] screenPoint) {
        return scaleScrPntToCSysPnt(null, screenPoint);
    }

    public double[] scaleScrPntToCSysPnt(double[] cSysPoint, double[] screenPoint) {
        if (cSysPoint == null) {
            cSysPoint = new double[3];
        }
        cSysPoint[0] = screenPoint[0] / minScale;
        cSysPoint[1] = screenPoint[1] / minScale;
        cSysPoint[2] = 0d;
        return cSysPoint;
    }

    public double[] scaleCSysPntToScrPnt(double[] cSysPoint) {
        return scaleCSysPntToScrPnt(null, cSysPoint);
    }

    public double[] scaleCSysPntToScrPnt(double[] screenPoint, double[] cSysPoint) {
        if (screenPoint == null) {
            screenPoint = new double[3];
        }
        screenPoint[0] = cSysPoint[0] * minScale;
        screenPoint[1] = cSysPoint[1] * minScale;
        screenPoint[2] = 0d;
        return screenPoint;
    }

    //
    //   Scr to CSys / CSys to scr conversion
    //

    public double[] screenPointToCSysPoint(int x, int y) {
        double[] cSysPoint = new double[3];
        cSysPoint[0] = (x - scrX0) / minScale;
        cSysPoint[1] = (scrY0 - y) / minScale;
        cSysPoint[2] = 0d;
        return cSysPoint;
    }

    /**
     * @param screenPoint
     * @return
     */
    public double[] screenPointToCSysPoint(double[] screenPoint) {
        return screenPointToCSysPoint(null, screenPoint);
    }

    /**
     * @param cSysPoint
     * @param screenPoint
     * @return
     */
    public double[] screenPointToCSysPoint(double[] cSysPoint, double[] screenPoint) {
        if (cSysPoint == null) {
            cSysPoint = new double[3];
        }
        cSysPoint[0] = (screenPoint[0] - scrX0) / minScale;
        cSysPoint[1] = (scrY0 - screenPoint[1]) / minScale;
        cSysPoint[2] = 0d;
        return cSysPoint;
    }


    public double[] cSysPointToScreenPoint(double[] cSysPoint) {
        return cSysPointToScreenPoint(null, cSysPoint);
    }

    public double[] cSysPointToScreenPoint(double[] screenPoint, double[] cSysPoint) {
        if (screenPoint == null) {
            screenPoint = new double[3];
        }
        screenPoint[0] = scrX0 + cSysPoint[0] * minScale;
        screenPoint[1] = scrY0 - cSysPoint[1] * minScale;
        screenPoint[2] = 0d;
        return screenPoint;
    }

    //
    //   ============================================
    //

    /**
     * @return Returns the projection.
     */
    public int getProjection() {
        return projection;
    }

    /**
     * @param projection The projection to set.
     */
    public void setProjection(int projection) {
        this.projection = projection;
    }

    /**
     * @return true when YoX Projection
     */
    public boolean isYOXProjection() {
        return projection == YOXProjection;
    }

    /**
     * @return true when ZoX Projection
     */
    public boolean isZOXProjection() {
        return projection == ZOXProjection;
    }

    /**
     * @return true when ZoY Projection
     */
    public boolean isZOYProjection() {
        return projection == ZOYProjection;
    }

    /**
     * @return Returns the gridVisible.
     */
    public boolean isGridVisible() {
        return gridVisible;
    }

    /**
     * @param gridVisible The gridVisible to set.
     */
    public void setGridVisible(boolean gridVisible) {
        this.gridVisible = gridVisible;
        if (gridVisible && gridEntityList == null) {
            gridEntityList = new ArrayList();
            createGrid();
        }
    }

    //
    //   building world, axes and model representation
    //

    /**
     *
     */
    protected void addWorldEntity(BasicCSysEntity basicCSysEntity) {
        worldEntityList.add(basicCSysEntity);
        basicCSysEntity.setParentCSys(this);
    }

    private void addWorldAxis(BasicCSysEntity basicCSysEntity) {
        worldAxesList.add(basicCSysEntity);
        basicCSysEntity.setParentCSys(this);
    }

    /**
     *
     */
    public double getCurX() {
        return 0D;
    }

    public double getCurY() {
        return 0D;
    }

    public int cSysXScalarToScr(double x) {
        return (int) Math.rint(scrX0 + x * minScale);
    }

    public int cSysYScalarToScr(double y) {
        return (int) Math.rint(scrY0 - y * minScale);
    }

    public int scrXScalarToCSys(int x) {
        return (int) Math.rint((x - scrX0) / minScale);
    }

    public int scrYScalarToCSys(int y) {
        return (int) Math.rint((scrY0 - y) / minScale);
    }

    /**
     * will be overridden
     */
    protected void updatePresentationUponViewResized() {

    }


    //    P A I N T I N G

    /**
     *
     */
    public void drawCSysLine(Graphics g, double x1, double y1, double x2, double y2) {
        int scr_x1, scr_y1;
        int scr_x2, scr_y2;

        scr_x1 = scrX0 + (int) (x1 * minScale);
        scr_y1 = scrY0 + (int) (-y1 * minScale);
        scr_x2 = scrX0 + (int) (x2 * minScale);
        scr_y2 = scrY0 + (int) (-y2 * minScale);
        g.drawLine(scr_x1, scr_y1, scr_x2, scr_y2);
    }

    public void drawCSysPoint(Graphics g, double x, double y) {
        drawCSysLine(g, x, y, x, y);
    }

    /**
     * @param g
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawCSysEntLine(Graphics g, float x1, float y1,
                                float x2, float y2) {
        int scr_x1, scr_y1;
        int scr_x2, scr_y2;
        scr_x1 = scrX0 + (int) (x1 * yScale);
        scr_y1 = scrY0 + (int) (-y1 * yScale);
        scr_x2 = scrX0 + (int) (x2 * yScale);
        scr_y2 = scrY0 + (int) (-y2 * yScale);
        g.drawLine(scr_x1, scr_y1, scr_x2, scr_y2);
    }

    public void drawCSysLineEntity(Graphics g, double[] pnt1, double[] pnt2) {
        int scr_x1, scr_y1;
        int scr_x2, scr_y2;
        scr_x1 = scrX0 + (int) (pnt1[0] * xScale);
        scr_y1 = scrY0 + (int) (-pnt1[1] * xScale);
        scr_x2 = scrX0 + (int) (pnt2[0] * xScale);
        scr_y2 = scrY0 + (int) (-pnt2[1] * xScale);
        g.drawLine(scr_x1, scr_y1, scr_x2, scr_y2);
    }

    /**
     * @param g
     * @param pnt
     * @param radius
     */
    public void drawCSysEntCircle(Graphics g, double[] pnt, double radius) {
        int scr_x1, scr_y1;
        int scr_x2, scr_y2;
        scr_x1 = scrX0 + (int) ((pnt[0] - radius) * xScale);
        scr_y1 = scrY0 + (int) (-(pnt[1] + radius) * xScale);
        scr_x2 = (int) (radius * 2 * xScale);
        scr_y2 = (int) (radius * 2 * xScale);
        //g.fillOval( scr_x1, scr_y1, scr_x2, scr_y2 );
        g.drawOval(scr_x1, scr_y1, scr_x2, scr_y2);
    }

    /**
     * @param g
     * @param wrdPnts
     * @param wrdX
     * @param wrdY
     * @param n
     */
    public void drawCSysEntRegion(Graphics g, double[][] wrdPnts,
                                  int[] wrdX, int[] wrdY, int n) {
        for (int i = 0; i < n; i++) {
            wrdX[i] = scrX0 + (int) (wrdPnts[i][0] * xScale);
            wrdY[i] = scrY0 + (int) (-wrdPnts[i][1] * xScale);
        }
        g.fillPolygon(wrdX, wrdY, n);
    }

    // A X I S

    public void createAxis(Color[] axesColors) {
        if (axesColors == null) {
            return;
        }
        CSysLineEntity cSysLineEntity;

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{5, 0, 0});
        cSysLineEntity.setDrawColor(axesColors[0]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{-5, 0, 0});
        cSysLineEntity.setDrawColor(axesColors[1]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 5, 0});
        cSysLineEntity.setDrawColor(axesColors[2]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, -5, 0});
        cSysLineEntity.setDrawColor(axesColors[3]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, 5});
        cSysLineEntity.setDrawColor(axesColors[4]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);

        cSysLineEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, -5});
        cSysLineEntity.setDrawColor(axesColors[5]);
        addWorldEntity(cSysLineEntity);
        addWorldAxis(cSysLineEntity);
    }

    // G R I D

//    public void createGrid() {
//          Color borderColor = new Color(0x666666);
//          CSysLineEntity cSysLineEntity;
//
//          cSysLineEntity = new CSysLineEntity( -2, 2, 2, -2 );
//          gridEntityList.add(cSysLineEntity);
//
//          cSysLineEntity =
//              new CSysLineEntity(this, (double)cSysX, (double)cSysY, (double)cSysX + (double)cSysWidth, (double)cSysY );
//          gridEntityList.add(cSysLineEntity);
//
////        cSysX;
////        private double ;
////        private double cSysWidth;
////        private double cSysHeight;
//
//          cSysLineEntity =
//              new CSysLineEntity( 1, 0, 1, cSysScreenRect.height );
//          gridEntityList.add(cSysLineEntity);
//
//
//          for( int i = 0; i < 10; i++ ){
//              cSysLineEntity =
//                  new CSysLineEntity( cSysX, cSysY+cSysHeight-i, cSysX + cSysWidth, cSysY+cSysHeight-i );
//              gridEntityList.add(cSysLineEntity);
//          }
//
//          for( int i = 0; i < 11; i++ ){
//              cSysLineEntity =
//                  new CSysLineEntity( cSysX+i, cSysY+cSysHeight, cSysX+i, cSysY );
//              gridEntityList.add(cSysLineEntity);
//
//
//      }


    public void createGrid() {
        Color borderColor = new Color(0x666666);
//        Color borderColor = new Color(0x0000FF);
        CSysLineEntity cSysLineEntity;
        /*
        cSysLineEntity = new CSysLineEntity( -2, 2, 2, -2 );
        gridEntityList.add(cSysLineEntity);

        cSysLineEntity =
            new CSysLineEntity( cSysX, cSysY, cSysX + cSysWidth, cSysY );
        gridEntityList.add(cSysLineEntity);
        */
//        cSysX;
//        private double ;
//        private double cSysWidth;
//        private double cSysHeight;
        /*
        cSysLineEntity =
            new CSysLineEntity( 1, 0, 1, cSysScreenRect.height );
        gridEntityList.add(cSysLineEntity);
        */
        /*
        for( int i = 0; i < 10; i++ ){
            cSysLineEntity =
                new CSysLineEntity( cSysX, cSysY+cSysHeight-i, cSysX + cSysWidth, cSysY+cSysHeight-i );
            gridEntityList.add(cSysLineEntity);
        }

        for( int i = 0; i < 11; i++ ){
            cSysLineEntity =
                new CSysLineEntity( cSysX+i, cSysY+cSysHeight, cSysX+i, cSysY );
            gridEntityList.add(cSysLineEntity);
        }*/
        double[] p1 = new double[]{-1, -2, 0};
        double[] p2 = new double[]{1, 2, 0};
        cSysLineEntity = new CSysLineEntity(this, p1, p2);
        addGridEntity(cSysLineEntity);

        double i = 0;
        while (i < cSysHeight + 1) {

//            cSysLineEntity =
//                new CSysLineEntity( cSysX, cSysY+cSysHeight-i,
//                        cSysX + cSysWidth, cSysY+cSysHeight-i );

            cSysLineEntity = new CSysLineEntity(this, new double[]{cSysX, cSysY + cSysHeight - i, 0},
                    new double[]{cSysX + cSysWidth, cSysY + cSysHeight - i, 0});
            if (i == 0) {
                cSysLineEntity.setDrawColor(borderColor);
            }
            addGridEntity(cSysLineEntity);
            i++;
        }

        cSysLineEntity.setDrawColor(borderColor);

        i = 0;
        while (i < cSysWidth + 1) {
//            cSysLineEntity =
//                new CSysLineEntity( cSysX+i, cSysY+cSysHeight, cSysX+i, cSysY );
            cSysLineEntity = new CSysLineEntity(this, new double[]{cSysX + i, cSysY + cSysHeight, 0},
                    new double[]{cSysX + i, cSysY, 0});
            if (i == 0) {
                cSysLineEntity.setDrawColor(borderColor);
            }
            addGridEntity(cSysLineEntity);
            i++;
        }

        cSysLineEntity.setDrawColor(borderColor);

    }

    /**
     * @param basicCSysEntity
     */
    private void addGridEntity(BasicCSysEntity basicCSysEntity) {
        basicCSysEntity.setParentCSys(this);
        gridEntityList.add(basicCSysEntity);
    }


    // V I E W   R O T A T I O N S   A N D   S C A L I N G

    public void doXRotation(double angle) {
        xRotationAngle = angle;
        VAlgebra.initPitchXRotMat43(xRotatingMatrix, -xRotationAngle, null);
        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, xRotatingMatrix, zRotatingMatrix);
        updateCSysEntList(combinedRotatingMatrix);
    }

    /**
     *
     */
    public void doYRotation(double angle) {
        xRotationAngle = angle;
        VAlgebra.initYawYRotMat43(yRotatingMatrix, angle, null);
        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, yRotatingMatrix, zRotatingMatrix);
        updateCSysEntList(yRotatingMatrix);
    }

    /**
     * @param angle
     */
    public void doZRotation(double angle) {
        zRotationAngle = angle;
        VAlgebra.initZRotMat43(zRotatingMatrix, -zRotationAngle, null);
        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, xRotatingMatrix, zRotatingMatrix);
        updateCSysEntList(combinedRotatingMatrix);
    }

    //  T R A N S F O R M A T I O N S

    /**
     * @return
     */
    public List<CSysEntity> getWorldEntityList() {
        return worldEntityList;
    }

    /**
     * @return
     */
    public List<BasicCSysEntity> getWorldAxesList() {
        return worldAxesList;
    }

    //
    //   s c a l i n g   a n d   t r a n s f o r m a t i o n s
    //

    public abstract void updateCSysEntList(double[][] mat43);

    /**
     * @param entityArray
     */
    protected void scaleEntArray(CSysEntity[] entityArray) {
        for (int i = 0; i < entityArray.length; i++) {
            CSysEntity basicCSysEntity = entityArray[i];
            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
        }
    }

    /**
     * @param entityCollection
     */
    protected void scaleEntityCollection(Collection<? extends BasicCSysEntity> entityCollection) {
        for (BasicCSysEntity basicCSysEntity : entityCollection) {
            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
        }
    }

    /**
     * @param mat43
     * @param worldEntityList
     */
    protected void updateCSysWorldEntList(double[][] mat43, List worldEntityList) {
        int nOfEntities = worldEntityList.size();
        for (int i = 0; i < nOfEntities; i++) {
            BasicCSysEntity basicCSysEntity = (BasicCSysEntity) worldEntityList.get(i);
            basicCSysEntity.doTransformation(mat43);
            basicCSysEntity.doProspectiveDistortion();
            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
        }
    }

    /**
     * Updates world entity list upon view rotated or re-sized
     *
     * @param mat43
     * @param entityArray, this could be: axis, static world or model entity
     */
    protected void updateCSysEntityArray(double[][] mat43, CSysEntity[] entityArray) {
        for (int i = 0; i < entityArray.length; i++) {
            BasicCSysEntity basicCSysEntity = (BasicCSysEntity) entityArray[i];
            basicCSysEntity.doTransformation(mat43);
            basicCSysEntity.doProspectiveDistortion();
            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
        }
    }

    //
    //   p a i n t i n g   w o r l d   a n d   a x e s
    //

    /**
     * @param g
     */
    protected void paintWorld(Graphics g) {
        int nElements = worldEntityList.size();
        for (int i = 0; i < nElements; i++) {
            CSysLineEntity cSysEntity = (CSysLineEntity) worldEntityList.get(i);
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    /**
     * @param g
     */
    protected void paintAxes(Graphics g) {
        int nOfAxes = worldAxesList.size();
        for (int i = 0; i < nOfAxes; i++) {
            CSysLineEntity cSysEntity = (CSysLineEntity) worldAxesList.get(i);
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    //
    //   p a i n t i n g   p r i m i t i v e s
    //

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

    private void recordPoints(int x0, int y0, int dx, int dy, Graphics g) {
        int[] xyCoordinates = new int[0];
        int x;
        int y;
        System.out.println("\n");
        System.out.println("x0 = " + x0 + ",  y0 = " + y0 + "       dx = " + dx + ",  dy = " + dy);
        int x1 = x0 + dx;
        int y1 = y0 + dy;
        System.out.println("" + x1 + "  " + y1);

        int x2 = x0 + dy;
        int y2 = y0 + dx;
        System.out.println("" + x2 + "  " + y2);

        int x3 = x0 + dy;
        int y3 = y0 - dx;
        System.out.println("" + x3 + "  " + y3);

        int x4 = x0 + dx;
        int y4 = y0 - dy;
        System.out.println("" + x4 + "  " + y4);
// ----------------------------
        int x5 = x0 - dx;
        int y5 = y0 - dy;
        System.out.println("" + x5 + "  " + y5);

        int x6 = x0 - dy;
        int y6 = y0 + dx;
        System.out.println("" + x6 + "  " + y6);

        int x7 = x0 - dy;
        int y7 = y0 - dx;
        System.out.println("" + x7 + "  " + y7);

        int x8 = x0 - dx;
        int y8 = y0 + dy;
        System.out.println("" + x8 + "  " + y8);


//        g.drawLine(x1, y1, x1, y1);
//        g.drawLine(x2, y2, x2, y2);
//        g.drawLine(x3, y3, x3, y3);
//        g.drawLine(x4, y4, x4, y4);
//        g.drawLine(x5, y5, x5, y5);
//        g.drawLine(x6, y6, x6, y6);
//        g.drawLine(x7, y7, x7, y7);
//        g.drawLine(x8, y8, x8, y8);
        g.setColor(Color.RED);
        g.drawLine(x1, y1, x8, y8);
        g.drawLine(x2, y2, x6, y6);
        g.drawLine(x3, y3, x7, y7);
        g.drawLine(x4, y4, x5, y5);

//        g.drawLine(x5, y5, x5, y5);
//        g.drawLine(x6, y6, x6, y6);
//        g.drawLine(x7, y7, x7, y7);
//        g.drawLine(x8, y8, x8, y8);
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x1, y1);
        g.drawLine(x2, y2, x2, y2);
        g.drawLine(x3, y3, x3, y3);
        g.drawLine(x4, y4, x4, y4);
        g.drawLine(x5, y5, x5, y5);
        g.drawLine(x6, y6, x6, y6);
        g.drawLine(x7, y7, x7, y7);
        g.drawLine(x8, y8, x8, y8);
        System.out.println("");
    }

    /**
     * This method is used for runtime vectot painting on screen
     */
    public void paintVector(double[] scrStart, double[] acrEnd) {
        Graphics g = getGraphics();
        int[] intStart = VAlgebra.doubleVec3ToInt(scrStart);
        int[] intEnd = VAlgebra.doubleVec3ToInt(acrEnd);
        g.setColor(Color.RED);
        g.drawLine(intStart[0], intStart[1], intEnd[0], intEnd[1]);
    }

    /**
     * THis method is used for debugging
     */
    public void paintVector(double[] vec) {
        Graphics g = getGraphics();
        int[] intVec = new int[3];
        VAlgebra.doubleVec3ToInt(intVec, vec);
        g.setColor(Color.RED);
        g.drawLine(100, 100, intVec[0] + 200, intVec[1] + 200);
        paintImmediately(getBounds());
    }
}
