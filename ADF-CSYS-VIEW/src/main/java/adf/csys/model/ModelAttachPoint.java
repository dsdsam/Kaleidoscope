package adf.csys.model;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 14, 2013
 * Time: 10:14:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelAttachPoint extends BasicModelEntity {


    public ModelAttachPoint(double[] attachPoint) {
        super(null);
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColor(Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modelToWorldTransform(double[][] mat43) {

    }
}
