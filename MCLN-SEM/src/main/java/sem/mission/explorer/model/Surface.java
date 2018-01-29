/*
 * Created on Sep 11, 2005
 *
 */
package sem.mission.explorer.model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelEntityFactory;
import adf.csys.model.ModelLineEntity;
import vw.valgebra.VAlgebra;

/**
 * @author xpadmin
 */
public class Surface extends BasicSeModelNode {

    private double x;
    private double y;
    private double width;
    private double height;
    private Rectangle outlineRect = new Rectangle();
    private final double[] directionTip = {0, 0, 0};
    private final double[] directionEnd = {0, 0, 0};
    private final Color selectedDirectionColor = Color.YELLOW;
    private final double[][] doubleOutlineRect = new double[2][2];
    private final ModelLineEntity selectedDirection =
            ModelEntityFactory.createLineEntity(directionTip, directionEnd, selectedDirectionColor);

    private ArrayList entityList;

    public Surface(String name, String nodeId, double x, double y, double width, double height) {
        super(name, nodeId);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        outlineRect = new Rectangle((int) x, (int) y, (int) width, (int) height);
        outlineRect.grow(-1, -1);
        build();
    }

    public void build() {
        Color lineColor = new Color(0x333333);
        Color borderColor = new Color(0x666666);
        BasicModelEntity basicModelEntiry = null;
        ArrayList border = new ArrayList();
        double i = 0;

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        while (i < height + 1) {
            double currentY = y + height - i;
            minY = Math.min(minY, currentY);
            maxY = Math.max(maxY, currentY);
            double[] linePnt1 = {x, currentY, 0};
            double[] linePnt2 = {x + width, currentY, 0};
            basicModelEntiry = ModelEntityFactory.createLineEntity(linePnt1, linePnt2, lineColor);
            if (i == 0 || (i >= height)) {
                basicModelEntiry.setColor(borderColor);
                border.add(basicModelEntiry);
            } else {
                addPartEntity(basicModelEntiry);
            }
            i++;
        }


        i = 0;
        while (i < width + 1) {
            double currentX = x + i;
            minX = Math.min(minX, currentX);
            maxX = Math.max(maxY, currentX);
            double[] linePnt1 = {currentX, y + height, 0};
            double[] linePnt2 = {currentX, y, 0};
            basicModelEntiry = ModelEntityFactory.createLineEntity(linePnt1, linePnt2, lineColor);
            if (i == 0 || ((i) >= width)) {
                basicModelEntiry.setColor(borderColor);
                border.add(basicModelEntiry);
            } else {
                addPartEntity(basicModelEntiry);
            }
            i++;
        }
        doubleOutlineRect[0][0] = minX + 1.0;
        doubleOutlineRect[0][1] = minY + 1.0;
        doubleOutlineRect[1][0] = maxX - 1.0;
        doubleOutlineRect[1][1] = maxY - 1.0;

//        basicModelEntiry.setColor( borderColor );
        addRartEntityList(border);
//        modelEntityList.addAll(border);
        selectedDirection.setEntityId("Selected Direction");
//        modelEntityList.add(selectedDirection);
        addPartEntity(selectedDirection);
    }

    public boolean containsPoint(double[] point) {
        return VAlgebra.outlineContainsPoint(doubleOutlineRect, point);
    }

    public boolean contains(int x, int y) {
        int minX = outlineRect.x;
        int maxX = outlineRect.x + outlineRect.width;
        int minY = outlineRect.y;
        int maxY = outlineRect.y + outlineRect.height;
//        System.out.println("Serfice Rect minX = " + minX + ", minY = " + minY + ", maxX = " + maxX + ", maxY = " + maxY);
//        System.out.println(" x = " + x + ", y = " + y + ", inside " + outlineRect.contains(x, y));
        return outlineRect.contains(x, y);
    }

    /**
     * @param lineHead
     * @param lineEnd
     * @param selectedDirectionColor
     */
    public BasicModelEntity updateSelectedDirectionVector(double[] lineHead, double[] lineEnd,
                                                          Color selectedDirectionColor, boolean visible) {
        selectedDirection.redefinePoints(lineHead, lineEnd);
        selectedDirection.setColor(selectedDirectionColor);
        selectedDirection.setVisible(visible);
        return selectedDirection;
    }
}
