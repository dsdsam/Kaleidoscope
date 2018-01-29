package adf.csys.model;

import java.awt.*;

import vw.valgebra.VAlgebra;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 20, 2011
 * Time: 8:35:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelEntityPoint extends BasicModelEntity {

    private final double[] orgPnt;
    private double[] curPnt;

    public ModelEntityPoint(double x, double y, double z) {
        super(Color.BLACK);
        this.orgPnt = new double[]{x, y, z};
        this.curPnt = new double[]{x, y, z};
//        this.x = x;
//        this.y = y;
    }

    private double x;

    private double y;

    public double getX() {
        return curPnt[0];
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return curPnt[1];
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getVector() {
        return curPnt;
    }

//    public double getXasLong() {
//        return x;
//    }
//
//    public double getYasLong() {
//        return y;
//    }

    public void modelToWorldTransform(double[][] mat43) {
        VAlgebra.Mat43XPnt3(curPnt, mat43, orgPnt);
        this.modified = true;
    }

//    public void modelTranslate(double[] translationVector){
//        VAlgebra.xyVec3Translate(curPnt, translationVector, orgPnt);
//        this.modified = true;
//    }
}
