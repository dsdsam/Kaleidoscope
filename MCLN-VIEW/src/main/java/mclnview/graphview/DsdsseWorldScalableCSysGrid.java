package mclnview.graphview;

import adf.csys.view.CSysRectangleEntity;
import adf.csys.view.CSysView;
import adf.csys.view.DoubleRectangle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Admin on 11/14/2017.
 */
public class DsdsseWorldScalableCSysGrid extends CSysRectangleEntity {

    private final int INTERSECTION_LINE = 6;
    private final int HALF_OF_INTERSECTION_LINE = INTERSECTION_LINE / 2;

    public static final double DEFAULT_GRID_STEP = 1.0;
    public static final int DEFAULT_GRID_MARGIN = 10;
    public static final Color DEFAULT_GRID_COLOR = new Color(0xC0C0C0);

    private final CSysView parentCSysView;
    private final double gridCSysStep;
    private final int gridMargin;
    private final Color gridColor;

    private int scrX0 = 0;
    private int scrY0 = 0;
    private int gridScrStep;

    private boolean foolScreenGrid;
    private boolean showDesignSpaceEdges;
    private boolean showGrid;
    private boolean gridCreated;

    /**
     * Creates grid with default parameters and color
     *
     * @param parentCSysView
     */
    public DsdsseWorldScalableCSysGrid(CSysView parentCSysView) {
        this(parentCSysView, DEFAULT_GRID_STEP, DEFAULT_GRID_MARGIN, DEFAULT_GRID_COLOR);
    }

    /**
     * @param parentCSysView
     * @param gridCSysStep
     * @param gridMargin
     * @param gridColor
     */
    public DsdsseWorldScalableCSysGrid(CSysView parentCSysView, double gridCSysStep, int gridMargin,
                                       Color gridColor) {
        super(parentCSysView, parentCSysView.getCSysRectangle());
        this.parentCSysView = parentCSysView;
        this.gridCSysStep = gridCSysStep;
        this.gridMargin = gridMargin;
        this.gridColor = gridColor;

        foolScreenGrid = true;
        showDesignSpaceEdges = true;
        showGrid = true;
    }

    /**
     * Updates grid project rectangle after model rectangle changed
     */
    public void updateProjectRectangle() {
        DoubleRectangle projectCSysRectangle = parentCSysView.getCSysRectangle();
        updateCSysRectangle(projectCSysRectangle);
    }

    private final java.util.List<int[]> gridLineScrXCoordinates = new ArrayList();
    private final java.util.List<int[]> gridLineScrYCoordinates = new ArrayList();

    /**
     * Scales grid line coordinates
     *
     * @param scrX0
     * @param scrY0
     * @param minScale
     */
    private void initGrid(int scrX0, int scrY0, double minScale) {

        double scrGridCSysStep = gridCSysStep * minScale;
        if (scrGridCSysStep < 1) {
            gridCreated = false;
            return;
        }

        gridLineScrXCoordinates.clear();
        gridLineScrYCoordinates.clear();

        double lineCnt = 0;
        int rowYFrom0ToMin = scrY0;
        int rowYFrom0ToMax = scrY0;
        gridLineScrYCoordinates.add(new int[]{rowYFrom0ToMin, rowYFrom0ToMax});
        // moving from 0 to up and down
        while (rowYFrom0ToMin > gridMargin) {
            lineCnt++;
            int offset = (int) Math.rint(lineCnt * scrGridCSysStep);
            rowYFrom0ToMin = scrY0 - offset; // end of loop condition
            rowYFrom0ToMax = scrY0 + offset;
            gridLineScrYCoordinates.add(new int[]{rowYFrom0ToMin, rowYFrom0ToMax});
        }

        double columnCnt = 0;
        int columnXFrom0ToMin = scrX0;
        int columnXFrom0ToMax = scrX0;
        gridLineScrXCoordinates.add(new int[]{columnXFrom0ToMin, columnXFrom0ToMax});
        // moving from 0 to mim and max
        while (columnXFrom0ToMin > gridMargin) {
            columnCnt++;
            int offset = (int) Math.rint(columnCnt * scrGridCSysStep);
            columnXFrom0ToMin = scrX0 - offset; // end of loop condition
            columnXFrom0ToMax = scrX0 + offset;
            gridLineScrXCoordinates.add(new int[]{columnXFrom0ToMin, columnXFrom0ToMax});
        }
        gridCreated = true;
    }

    /**
     * snapMouseToTheGrid method
     */
    void snapMouseToTheGrid(MouseEvent me) {

        int eps = gridScrStep / 2;
        int x = me.getX();
        int y = me.getY();

        double cSysX = parentCSysView.scrXScalarToCSys(x);
        double cSysY = parentCSysView.scrYScalarToCSys(y);

        cSysX += (cSysX >= 0) ? 0.5 * gridCSysStep : -0.5 * gridCSysStep;
        cSysY += (cSysY >= 0) ? 0.5 * gridCSysStep : -0.5 * gridCSysStep;

        double cSysGX = ((int) (cSysX / gridCSysStep)) * gridCSysStep;
        double cSysGY = ((int) (cSysY / gridCSysStep)) * gridCSysStep;

        int scrGX = parentCSysView.cSysXScalarToScr(cSysGX);
        int scrGY = parentCSysView.cSysYScalarToScr(cSysGY);

        int dx = scrGX - x;
        int dy = scrGY - y;

        if (Math.abs(dx) > eps) {
            dx = 0;
        }
        if (Math.abs(dy) > eps) {
            dy = 0;
        }
        me.translatePoint(dx, dy);
    }


    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        if (hidden || clippedOff || parentCSys == null) {
            return;
        }

        super.doCSysToScreenTransformation(scr0, scale);
        gridScrStep = parentCSysView.cSysScalarToScr(gridCSysStep);
        scrX0 = scr0[0];
        scrY0 = scr0[1];
        initGrid(scrX0, scrY0, scale);
    }

    /**
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        if (hidden || clippedOff || parentCSys == null) {
            return;
        }

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (showGrid && gridCreated) {
            drawProjectSpaceScaledSolidLineGrid(g, foolScreenGrid);
//        drawEntireScreeScaledCrossLineGrid(g, foolScreenGrid);
        }

//        if (showDesignSpaceEdges) {
//            drawProjectRectangle(g);
////        super.draw(g);
//        }


        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);

    }

    /**
     * Scales and draws grid in the boundaries of the Project Space
     *
     * @param g
     */
    private void drawProjectSpaceScaledSolidLineGrid(Graphics g, boolean foolScreenGrid) {

        int xMin;
        int xMax;
        int yMin;
        int yMax;

        if (foolScreenGrid) {
            Rectangle viewRect = parentCSysView.getBounds();
            // draw fool screen size grid
            xMin = gridMargin;
            xMax = viewRect.width - gridMargin;
            yMin = gridMargin;
            yMax = viewRect.height - gridMargin;
        } else {
            // draw project space size grid
            xMin = parentCSysView.projectSpaceViewXMin + gridMargin / 3;
            xMax = parentCSysView.projectSpaceViewXMax - gridMargin / 3;
            yMin = parentCSysView.projectSpaceViewYMin + gridMargin / 3;
            yMax = parentCSysView.projectSpaceViewYMax - gridMargin / 3;
        }

        g.setColor(gridColor);
        int xIndex = 0;
        int yIndex = 0;

        int[] xCoordinates = gridLineScrXCoordinates.get(xIndex);
        int columnXFrom0ToMin = xCoordinates[0];
        int columnXFrom0ToMax = xCoordinates[1];

        int[] yCoordinates = gridLineScrYCoordinates.get(yIndex);
        int rowYFrom0ToMin = yCoordinates[0];
        int rowYFrom0ToMax = yCoordinates[1];

        // horizontal lines
//        while (rowYFrom0ToMin > yMin) {
//            g.drawLine(xMin, rowYFrom0ToMin, xMax, rowYFrom0ToMin);
//            g.drawLine(xMin, rowYFrom0ToMax, xMax, rowYFrom0ToMax);
//
//            yIndex++;
//            yCoordinates = gridLineScrYCoordinates.get(yIndex);
//            rowYFrom0ToMin = yCoordinates[0]; // end of loop condition
//            rowYFrom0ToMax = yCoordinates[1];
//        }
//
//        // vertical lines
//        while (columnXFrom0ToMax < (xMax-1)) {
//            g.drawLine(columnXFrom0ToMin, yMin, columnXFrom0ToMin, yMax);
//            g.drawLine(columnXFrom0ToMax, yMin, columnXFrom0ToMax, yMax);
//
//            xIndex++;
//            xCoordinates = gridLineScrXCoordinates.get(xIndex);
//            columnXFrom0ToMin = xCoordinates[0]; // end of loop condition
//            columnXFrom0ToMax = xCoordinates[1];
//        }

        for (int i = 0; i < gridLineScrYCoordinates.size(); i++) {
            g.drawLine(xMin, rowYFrom0ToMin, xMax, rowYFrom0ToMin);
            g.drawLine(xMin, rowYFrom0ToMax, xMax, rowYFrom0ToMax);

            yCoordinates = gridLineScrYCoordinates.get(i);
            rowYFrom0ToMin = yCoordinates[0];
            rowYFrom0ToMax = yCoordinates[1];
        }
        for (int i = 0; i < gridLineScrXCoordinates.size(); i++) {
            g.drawLine(columnXFrom0ToMin, yMin, columnXFrom0ToMin, yMax);
            g.drawLine(columnXFrom0ToMax, yMin, columnXFrom0ToMax, yMax);

            xCoordinates = gridLineScrXCoordinates.get(i); // Somthig was wrong here
            columnXFrom0ToMin = xCoordinates[0];
            columnXFrom0ToMax = xCoordinates[1];
        }
    }


//    Exception in thread "pool-3-thread-1" java.lang.IndexOutOfBoundsException: Index: 7, Size: 41
//    at java.util.ArrayList.rangeCheck(ArrayList.java:653)
//    at java.util.ArrayList.get(ArrayList.java:429)
//    at dsdsse.graphview.DsdsseWorldScalableCSysGrid.drawProjectSpaceScaledSolidLineGrid(DsdsseWorldScalableCSysGrid.java:274)
//    at dsdsse.graphview.DsdsseWorldScalableCSysGrid.draw(DsdsseWorldScalableCSysGrid.java:187)
//    at dsdsse.graphview.MclnGraphView.paintWorld(MclnGraphView.java:632)
//    at dsdsse.graphview.MclnGraphView.createEmptyScreenImage(MclnGraphView.java:603)
//    at dsdsse.graphview.MclnGraphView.createScreenImageAndDrawMclnGraphOnIt(MclnGraphView.java:546)
//    at dsdsse.graphview.MclnGraphView.createAndDisplayNewOffScreenImage(MclnGraphView.java:1352)
//    at dsdsse.graphview.MclnGraphView.access$1900(MclnGraphView.java:36)
//    at dsdsse.graphview.MclnGraphView$2.mclnModelCleared(MclnGraphView.java:222)
//    at dsdsse.designspace.mcln.model.mcln.MclnGraphModel.fireMclnModelCleared(MclnGraphModel.java:356)
//    at dsdsse.designspace.mcln.model.mcln.MclnGraphModel.clearCurrentMclnModel(MclnGraphModel.java:145)
//    at dsdsse.designspace.mcln.model.mcln.MclnGraphModel.resetMclnModel(MclnGraphModel.java:80)
//    at dsdsse.designspace.DesignSpaceModel.resetMclnProject(DesignSpaceModel.java:264)
//    at dsdsse.designspace.DesignSpaceModel.restoreStashedProject(DesignSpaceModel.java:249)
//    at dsdsse.animation.DemoRunner.lambda$new$5(DemoRunner.java:122)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
//    at java.lang.Thread.run(Thread.java:745)


    /**
     * 12/02/2015
     * Draws grid intersection lines in the boundaries of the CSys view with optional cell size and margins
     *
     * @param g
     */
    private void drawEntireScreeScaledCrossLineGrid(Graphics g, boolean foolScreenGrid) {

        int xMin;
        int yMin;

        if (foolScreenGrid) {
            // draw fool screen size grid
            xMin = gridMargin;
            yMin = gridMargin;
        } else {
            // draw project space size grid
            xMin = parentCSysView.projectSpaceViewXMin + gridMargin / 3;
            yMin = parentCSysView.projectSpaceViewYMin + gridMargin / 3;
        }

        g.setColor(gridColor);

        int yIndex = 0;
        int[] yCoordinates = gridLineScrYCoordinates.get(yIndex);
        int rowYFrom0ToMin = yCoordinates[0];
        int rowYFrom0ToMax = yCoordinates[1];

        // moving cSys 0 to min and max
        while (rowYFrom0ToMin > yMin) {

            int xIndex = 0;
            int[] xCoordinates = gridLineScrXCoordinates.get(xIndex);
            int columnXFrom0ToMin = xCoordinates[0];
            int columnXFrom0ToMax = xCoordinates[1];
//
            // crosses along the row
            while (columnXFrom0ToMin > xMin) {

                // drawing horizontal dash-lines
                int horizontalDashStart = columnXFrom0ToMin - HALF_OF_INTERSECTION_LINE;
                int horizontalDashEnd = horizontalDashStart + INTERSECTION_LINE;
                g.drawLine(horizontalDashStart, rowYFrom0ToMin, horizontalDashEnd, rowYFrom0ToMin);
                g.drawLine(horizontalDashStart, rowYFrom0ToMax, horizontalDashEnd, rowYFrom0ToMax);

                horizontalDashStart = columnXFrom0ToMax - HALF_OF_INTERSECTION_LINE;
                horizontalDashEnd = horizontalDashStart + INTERSECTION_LINE;
                g.drawLine(horizontalDashStart, rowYFrom0ToMin, horizontalDashEnd, rowYFrom0ToMin);
                g.drawLine(horizontalDashStart, rowYFrom0ToMax, horizontalDashEnd, rowYFrom0ToMax);


                // drawing cSys horizontal line mid dash
//                if (evenCell) {
//                    midDashStart = column - cellCenter - halfOfCenterDash;
//                    midDashEnd = column - cellCenter + halfOfCenterDash;
//                } else {
//                    midDashStart = column - cellCenter - (halfOfCenterDash - 0);
//                    midDashEnd = midDashStart + (centerDash - 1);
//                }
//                g.drawLine(midDashStart, row, midDashEnd, row);
//                int currentCellCenter = column + cellCenter;
//                g.drawLine(currentCellCenter - 2, row, currentCellCenter + 2, row);

                // drawing vertical dash-lines
                int verticalDashStart = rowYFrom0ToMin - HALF_OF_INTERSECTION_LINE;
                int verticalDashEnd = verticalDashStart + INTERSECTION_LINE;
                g.drawLine(columnXFrom0ToMin, verticalDashStart, columnXFrom0ToMin, verticalDashEnd);
                g.drawLine(columnXFrom0ToMax, verticalDashStart, columnXFrom0ToMax, verticalDashEnd);

                verticalDashStart = rowYFrom0ToMax - HALF_OF_INTERSECTION_LINE;
                verticalDashEnd = verticalDashStart + INTERSECTION_LINE;
                g.drawLine(columnXFrom0ToMin, verticalDashStart, columnXFrom0ToMin, verticalDashEnd);
                g.drawLine(columnXFrom0ToMax, verticalDashStart, columnXFrom0ToMax, verticalDashEnd);


                // drawing cSys vertical line mid dash
//                if (evenCell) {
//                    midDashStart = row - cellCenter - halfOfCenterDash;
//                    midDashEnd = row - cellCenter + halfOfCenterDash;
//                } else {
//                    midDashStart = row + cellCenter - (halfOfCenterDash - 1);
//                    midDashEnd = midDashStart + (centerDash - 1);
//                }
//                g.drawLine(column, midDashStart, column, midDashEnd);
//                currentCellCenter = row + cellCenter;
//                g.drawLine(column, currentCellCenter - 2, column, currentCellCenter + 2);

//                column += cellWidth;
//                curDashStart = column - HALF_OF_INTERSECTION_LINE;
//                curDashEnd = column + HALF_OF_INTERSECTION_LINE;

//                columnCnt++;
//                int step = (int) Math.rint(columnCnt * minScale);

                xIndex++;
                xCoordinates = gridLineScrXCoordinates.get(xIndex);
                columnXFrom0ToMin = xCoordinates[0]; // end of loop condition
                columnXFrom0ToMax = xCoordinates[1];
            }

            yIndex++;
            yCoordinates = gridLineScrYCoordinates.get(yIndex);
            rowYFrom0ToMin = yCoordinates[0];
            rowYFrom0ToMax = yCoordinates[1];
        }
    }

    /**
     * Scales and draws grid Project Space rectangle
     *
     * @param g
     */
//    private void drawProjectRectangle(Graphics g) {
//
//        g.setColor(Color.RED);
//        // h
//        g.drawLine(
//                parentCSysView.projectSpaceViewXMin, parentCSysView.projectSpaceViewYMin,
//                parentCSysView.projectSpaceViewXMin, parentCSysView.projectSpaceViewYMax);
//
//        g.drawLine(
//                parentCSysView.projectSpaceViewXMax, parentCSysView.projectSpaceViewYMin,
//                parentCSysView.projectSpaceViewXMax, parentCSysView.projectSpaceViewYMax);
//
//        // v
//        g.drawLine(
//                parentCSysView.projectSpaceViewXMin, parentCSysView.projectSpaceViewYMin,
//                parentCSysView.projectSpaceViewXMax, parentCSysView.projectSpaceViewYMin);
//        g.drawLine(
//                parentCSysView.projectSpaceViewXMin, parentCSysView.projectSpaceViewYMax,
//                parentCSysView.projectSpaceViewXMax, parentCSysView.projectSpaceViewYMax);
//    }

//    /**
//     *
//     * @param g
//     */
//    private void drawProjectSpaceCoordinates(Graphics g) {
//
//        g.setColor(Color.DARK_GRAY);
//
//        Font   font = StandardFonts.FONT_MONOSPACED_BOLD_10;
//
//        g.setFont(font);
//
//        int coordinatesPadHeight = 14;
//
//        String text = "";
//        DoubleRectangle projectSpaceRect =  parentCSysView.getCSysRectangle();
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("(").append(projectSpaceRect.getX()).append(" , ").
//                append(projectSpaceRect.getY()).append(")");
//        text = stringBuilder.toString();
//        int width = g.getFontMetrics(font).stringWidth(text);
//        int coordinatesPadWidth = width;
//
//        Color COORDINATE_PAD_BACKGROUND = new Color(0xFFFFFF);
//        g.setColor(COORDINATE_PAD_BACKGROUND);
//        g.fillRect(parentCSysView.projectSpaceViewXMin, parentCSysView.projectSpaceViewYMin - coordinatesPadHeight ,
//                coordinatesPadWidth, coordinatesPadHeight);
//
//        g.setColor(Color.DARK_GRAY);
//        g.drawString(text, parentCSysView.projectSpaceViewXMin,
//                parentCSysView.projectSpaceViewYMin - 7);
//
//
//
//        stringBuilder = new StringBuilder();
//        stringBuilder.append("(").append(projectSpaceRect.getRightX()).append(" , ").
//                append(projectSpaceRect.getLowerY()).append(")");
//        text = stringBuilder.toString();
//        width = g.getFontMetrics(font).stringWidth(text);
//        coordinatesPadWidth = width;
//
//        g.setColor(COORDINATE_PAD_BACKGROUND);
//        g.fillRect(parentCSysView.projectSpaceViewXMax - coordinatesPadWidth , parentCSysView.projectSpaceViewYMax + 3,
//                coordinatesPadWidth, coordinatesPadHeight);
//
//        g.setColor(Color.DARK_GRAY);
//        g.drawString(text, parentCSysView.projectSpaceViewXMax - coordinatesPadWidth +1 ,
//                parentCSysView.projectSpaceViewYMax + coordinatesPadHeight  );
//
//    }



    // ========================================================================
    //       O L D   G R I D S

    protected void drawDottedCSysGrid(Graphics g) {

        Dimension d = parentCSys.getSize();
        int curDashStart;
        int curDashEnd;
//        initGrid();

//        g.setColor(Color.lightGray);
//        double curX = (int) (projSpaceXMin / gridStep);
//        int curScrX;
//        while (curX < projSpaceXMax) {
//            curScrX = cSysXToScrX(curX);
//            for (curDashStart = 0, curDashEnd = HALF_DASH; curDashStart < d.height;) {
//                g.drawLine(curScrX, curDashStart, curScrX, curDashEnd);
//                curDashStart = curDashEnd + GAP;
//                curDashEnd = curDashStart + DASH;
//            }
//            curX += gridStep;
//        }
//
//        double curY = (int) (projSpaceYMin / gridStep);
//        int curScrY;
//        while (curY < projSpaceYMax) {
//            curScrY = cSysYToScrY(curY);
//            for (curDashStart = 0, curDashEnd = HALF_DASH; curDashStart < d.width;) {
//                g.drawLine(curDashStart, curScrY, curDashEnd, curScrY);
//                curDashStart = curDashEnd + GAP;
//                curDashEnd = curDashStart + DASH;
//            }
//            curY += gridStep;
//        }
    }


    /**
     * drawSimpleGrid method
     */
//    private void drawSimpleGrid(Graphics g) {
//        Dimension d = parentCSys.getNKnots();
//        int curDashStart;
//        int curDashEnd;
//        initGrid();
//
//        g.setColor(Color.lightGray);
//        int xCurInd = xStartInd;
//        for (int i = 0; i < gridNCols; i++) {
//            curDashStart = 0;
//            curDashEnd = HALF_DASH;
//            for (int k = 0; k < 2 * gridNRows - 2; k++) {
//                g.drawLine(xCurInd, curDashStart, xCurInd, curDashEnd);
//                curDashStart = ((k + 1) * cellHeight) / 2 - HALF_DASH;
//                curDashEnd = curDashStart + DASH;
//            }
//            xCurInd += cellWidth;
//        }
//
//        int yCurInd = yStartInd;
//        for (int j = 0; j < gridNRows; j++) {
//            curDashStart = 0;
//            curDashEnd = HALF_DASH;
//            for (int k = 0; k < 2 * gridNCols - 2; k++) {
//                g.drawLine(curDashStart, yCurInd, curDashEnd, yCurInd);
//                curDashStart = ((k + 1) * cellWidth) / 2 - HALF_DASH;
//                curDashEnd = curDashStart + DASH;
//            }
//            yCurInd += cellHeight;
//        }
//    }


    /**
     * snapToTheNearestIntersection method
     */
    protected void snapToTheNearestIntersection(MouseEvent e) {


//        int dx, dy;
//        int x = e.getX();
//        int y = e.getY();
//        double cSysX = cSysScrXToWorldX(x);
//        double cSysY = cSysScrYToWorldY(y);
//        cSysX += (cSysX >= 0) ? 0.5 * gridStep : -0.5 * gridStep;
//        cSysY += (cSysY >= 0) ? 0.5 * gridStep : -0.5 * gridStep;
//
//        double cSysGX = ((int) (cSysX / gridStep)) * gridStep;
//        double cSysGY = ((int) (cSysY / gridStep)) * gridStep;
//        int scrGX = cSysXToScrX(cSysGX);
//        int scrGY = cSysYToScrY(cSysGY);
//        dx = scrGX - x;
//        dy = scrGY - y;
//        e.translatePoint(dx, dy);
    }
// ----------------------------------------------------------------


}
