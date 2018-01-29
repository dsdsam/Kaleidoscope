/*
 * Created on Aug 6, 2005
 *
 */
package adf.csys.model;

import java.awt.Color;


/**
 * @author xpadmin
 */
public class ModelEntityFactory {

    public static ModelLineEntity createLineEntity(double[] pnt1, double[] pnt2, Color color) {
        return new ModelLineEntity(pnt1, pnt2, color);
    }

     public static ModelPointEntity createPointEntity(double[] point, Color color) {
        return new ModelPointEntity(point, color);
    }

    public static ModelPointEntity createMclnModelArcEntity(double[] point, Color color) {
        return new ModelPointEntity(point, color);
    }


}
