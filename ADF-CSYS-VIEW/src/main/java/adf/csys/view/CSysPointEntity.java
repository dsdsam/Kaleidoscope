package adf.csys.view;

import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created by Admin on 9/1/2014.
 */
public class CSysPointEntity extends BasicCSysEntity {

    private double[] modelPnt = new double[3];
    private double[] cSysPnt = {0, 0, 0};

    protected double[] scrPnt = {0, 0, 0};
    protected int scrX, scrY;

    /**
     * @param parentCSys
     */
    public CSysPointEntity(CSysView parentCSys, double[] origPnt) {
        super(parentCSys);
        modelPnt = origPnt;
        VAlgebra.copyVec3(cSysPnt, origPnt);
    }

    /**
     * @param parentCSys
     * @param drawColor
     */
    public CSysPointEntity(CSysView parentCSys, double[] origPnt, Color drawColor) {
        super(parentCSys, drawColor);
        modelPnt = origPnt;
        VAlgebra.copyVec3(cSysPnt, origPnt);
    }

    //
    //   p l a c e m e n t
    //

    @Override
    public void updateEntityUponViewRedefined() {
        VAlgebra.copyVec3(modelPnt, cSysPnt);
    }

    public void updateModelPointUponViewRedefined() {
        VAlgebra.copyVec3(modelPnt, cSysPnt);
    }

    @Override
    public void moveEntityActivePointTo(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
    }

    @Override
    public void moveEntity(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
//        System.out.println("move Entity: " + this.getClass().getSimpleName() + " " + cSysPnt[0]);
    }

    /**
     * @return Returns the point1.
     */
    public double[] getModelPoint() {
        return VAlgebra.copyVec3(modelPnt);
    }

    public double[] getCSysPnt() {
        return cSysPnt;
    }

    public double[] getScrPnt() {
        return scrPnt;
    }


    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        switch (parentCSys.getProjection()) {

            case CSysView.ZOXProjection:
                scrX = scr0[0] + (int) (cSysPnt[0] * scale);
                scrY = scr0[2] + (int) (-cSysPnt[2] * scale);
                break;

            case CSysView.ZOYProjection:
                scrX = scr0[0] + (int) (cSysPnt[1] * scale);
                scrY = scr0[2] + (int) (-cSysPnt[2] * scale);
                break;

            default:
//                scrPnt[0] = scr0[0] + (int) (cSysPnt[0] * scale);
//                scrPnt[1] = scr0[1] + (int) (-cSysPnt[1] * scale);
//                scrX = (int) scrPnt[0];
//                scrY = (int) scrPnt[1];
                scrX = scr0[0] + (int) Math.rint(cSysPnt[0] * scale);
                scrY = scr0[1] + (int) Math.rint(-cSysPnt[1] * scale);
                scrPnt[0] = scrX;
                scrPnt[1] = scrY;
                break;
        }
    }

    /**
     * Transforms the entity according to the matrix provided
     *
     * @param mat43
     */
    @Override
    public void doTransformation(double[][] mat43) {
        VAlgebra.Mat43XPnt3(cSysPnt, mat43, modelPnt);
    }

    @Override
    public void doNextTransformation(double[][] mat43) {
        VAlgebra.Mat43XPnt3(cSysPnt, mat43, cSysPnt);
    }


    @Override
    public void draw(Graphics g) {
        if (hidden || clippedOff || parentCSys == null) {
            return;
        }
        g.drawLine(scrX, scrY, scrX, scrY);
    }
}
