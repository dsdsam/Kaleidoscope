package sem.mission.explorer.model;

import adf.csys.model.ModelLineEntity;

import java.awt.*;

import vw.valgebra.VAlgebra;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 6, 2011
 * Time: 11:11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModelPosition extends ModelLineEntity {

    private double[] directionVector = new double[3];

    ModelPosition(double[] pnt1, double[] pnt2, Color color) {
        super(pnt1, pnt2, color);
    }

    public double[] getCurrentLocationVector() {
        return getCurLineHead();
    }

    public double[] getCurrentDirectionVector() {
        double[] curPnt1 = getCurLineHead();
        double[] curPnt2 = getCurLineEnd();
//        System.out.println("Model Position direction = " + curPnt1[0] + "   " + curPnt1[1]);
//        System.out.println("Model Position direction = " + curPnt2[0] + "   " + curPnt2[1]);
        VAlgebra.subVec3(directionVector, getCurLineEnd(), getCurLineHead());
        return directionVector;
    }
}
