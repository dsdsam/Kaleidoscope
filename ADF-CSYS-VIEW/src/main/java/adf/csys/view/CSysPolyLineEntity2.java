package adf.csys.view;

import adf.csys.model.ModelPolyLineEntity;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created by Admin on 5/17/2016
 */
public class CSysPolyLineEntity2 extends BasicCSysEntity {

    // rectangle model coordinates.
    private double[] modelUpperLeftCorner = new double[3];
    private double[] modelLowerRightCorner = new double[3];

    // rectangle cSys coordinates
    // these are transformed model coordinates
    // SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    private double[] cSysUpperLeftCorner = {0, 0, 0};
    private double[] cSysLowerRightCorner = {0, 0, 0};

    // these are transformed model coordinates projected on screen
    protected int[] scrUpperLeftCorner = {0, 0, 0};
    protected int[] scrLowerRightCorner = {0, 0, 0};

    /**
     * This constructor creates rectangle entity initialized with model coordinates
     *
     * @param parentCSysView
     * @param doubleRectangle
     */
    public CSysPolyLineEntity2(CSysView parentCSysView, DoubleRectangle doubleRectangle) {
        this(parentCSysView, doubleRectangle.getX(), doubleRectangle.getY(),
                doubleRectangle.getRightX(), doubleRectangle.getLowerY());
    }

    public CSysPolyLineEntity2(CSysView parentCSysView, ModelPolyLineEntity modelPolyLineEntity) {
        super(parentCSysView);
    }

    /**
     * This constructor creates rectangle entity initialized with model coordinates
     *
     * @param parentCSysView
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public CSysPolyLineEntity2(CSysView parentCSysView, double x1, double y1, double x2, double y2) {
        super(parentCSysView);
        initCSysRectangle(  x1,   y1,   x2,   y2);
    }

    protected void updateCSysRectangle(DoubleRectangle cSysRectangle) {
        initCSysRectangle(cSysRectangle.getX(), cSysRectangle.getY(),
                cSysRectangle.getRightX(), cSysRectangle.getLowerY());
    }

    private void initCSysRectangle(double x1, double y1, double x2, double y2){
        VAlgebra.initVec3(modelUpperLeftCorner, x1, y1, 0);
        VAlgebra.initVec3(modelLowerRightCorner, x2, y2, 0);
        VAlgebra.copyVec3(cSysUpperLeftCorner, modelUpperLeftCorner);
        VAlgebra.copyVec3(cSysLowerRightCorner, modelLowerRightCorner);
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        switch (parentCSys.getProjection()) {

            case CSysView.ZOXProjection:
                scrUpperLeftCorner[0] = scr0[0] + (int) (cSysUpperLeftCorner[0] * scale);
                scrUpperLeftCorner[2] = scr0[2] + (int) (-cSysUpperLeftCorner[2] * scale);
                scrLowerRightCorner[0] = scr0[0] + (int) (cSysLowerRightCorner[0] * scale);
                scrLowerRightCorner[2] = scr0[2] + (int) (-cSysLowerRightCorner[2] * scale);
                break;

            case CSysView.ZOYProjection:
                scrUpperLeftCorner[0] = scr0[0] + (int) (cSysUpperLeftCorner[1] * scale);
                scrUpperLeftCorner[2] = scr0[2] + (int) (-cSysUpperLeftCorner[2] * scale);
                scrLowerRightCorner[0] = scr0[0] + (int) (cSysLowerRightCorner[1] * scale);
                scrLowerRightCorner[2] = scr0[2] + (int) (-cSysLowerRightCorner[2] * scale);
                break;

            default: // XoY projection
//                scrUpperLeftCorner[0] = scr0[0] + (int) (cSysUpperLeftCorner[0] * scale);
//                scrUpperLeftCorner[1] = scr0[1] + (int) (-cSysUpperLeftCorner[1] * scale);
//                scrLowerRightCorner[0] = scr0[0] + (int) (cSysLowerRightCorner[0] * scale);
//                scrLowerRightCorner[1] = scr0[1] + (int) (-cSysLowerRightCorner[1] * scale);

                scrUpperLeftCorner[0] = (int) Math.rint(scr0[0] + cSysUpperLeftCorner[0] * scale);
                scrUpperLeftCorner[1] = (int) Math.rint(scr0[1] - cSysUpperLeftCorner[1] * scale);
                scrLowerRightCorner[0] = (int) Math.rint(scr0[0] + cSysLowerRightCorner[0] * scale);
                scrLowerRightCorner[1] = (int) Math.rint(scr0[1] - cSysLowerRightCorner[1] * scale);
                break;
        }
    }

    @Override
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.drawLine(scrUpperLeftCorner[0], scrUpperLeftCorner[1], scrLowerRightCorner[0], scrUpperLeftCorner[1]);
        g.drawLine(scrLowerRightCorner[0], scrUpperLeftCorner[1], scrLowerRightCorner[0], scrLowerRightCorner[1]);
        g.drawLine(scrLowerRightCorner[0], scrLowerRightCorner[1], scrUpperLeftCorner[0], scrLowerRightCorner[1]);
        g.drawLine(scrUpperLeftCorner[0], scrLowerRightCorner[1], scrUpperLeftCorner[0], scrUpperLeftCorner[1]);
        g.setColor(c);
    }
//
}

