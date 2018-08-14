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
    private boolean drawAsDot;

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
    public CSysPointEntity(CSysView parentCSys, double[] origPnt, Color drawColor, boolean drawAsDot) {
        super(parentCSys, drawColor);
        modelPnt = origPnt;
        VAlgebra.copyVec3(cSysPnt, origPnt);
        this.drawAsDot = drawAsDot;
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
    public void moveEntityActivePointTo(double[] cSysPnt) {
        this.cSysPnt = VAlgebra.copyVec3(cSysPnt);
    }

    @Override
    public void moveEntity(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
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

    public boolean isDot() {
        return drawAsDot;
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        doCSysToScreenTransformation(scr0, scale, cSysPnt);
    }

    @Override
    public int[] doCSysToScreenTransformation(int[] scr0, double scale, double[] cSysPnt) {
        int[] scrPoint = super.doCSysToScreenTransformation(scr0, scale, cSysPnt);
        scrX = scrPoint[0];
        scrY = scrPoint[1];
        return scrPoint;
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
    public void drawPlainEntity(Graphics g) {
        draw(g);
    }

    @Override
    public void draw(Graphics g) {
        if (hidden || clippedOff || parentCSys == null) {
            return;
        }

        g.setColor(getDrawColor());

        if (isDot()) {
            g.drawLine(scrX - 1, scrY + 1, scrX + 1, scrY + 1);
            g.drawLine(scrX - 1, scrY, scrX, scrY + 1);
            g.drawLine(scrX - 1, scrY - 1, scrX + 1, scrY - 1);
        } else {
            g.drawLine(scrX, scrY, scrX, scrY);
        }
    }
}
