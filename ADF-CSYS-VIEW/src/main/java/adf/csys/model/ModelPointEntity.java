package adf.csys.model;

import java.awt.*;

import vw.valgebra.VAlgebra;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 19, 2013
 * Time: 5:28:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelPointEntity extends BasicModelEntity {

    private double[] originalPoint;
    private double[] currentPoint;


    public ModelPointEntity(double[] point) {
        originalPoint = VAlgebra.copyVec3(point);
        currentPoint = VAlgebra.copyVec3(point);
    }

    public ModelPointEntity(double[] point, Color color) {
        super(color);
        originalPoint = VAlgebra.copyVec3(point);
        currentPoint = VAlgebra.copyVec3(point);
    }

    public void redefinePoints(double[] point) {
        definePoints(point);
    }

    protected void definePoints(double[] point) {
        this.originalPoint = (point != null) ? VAlgebra.copyVec3(point) : new double[]{0, 0, 0};
        this.currentPoint = (point != null) ? VAlgebra.copyVec3(point) : new double[]{0, 0, 0};
    }

    /**
     * @return Returns the pnt1.
     */
    public double[] getCurPoint() {
        return currentPoint;
    }

    /**
     * @param mat43
     */
    public void modelToWorldTransform(double[][] mat43) {
        VAlgebra.xyMat43XPnt3(currentPoint, mat43, originalPoint);
        this.modified = true;
    }

    public void modelTranslate(double[] translatingVector) {
        VAlgebra.xyVec3Translate(currentPoint, translatingVector, originalPoint);
        this.modified = true;
    }
}

