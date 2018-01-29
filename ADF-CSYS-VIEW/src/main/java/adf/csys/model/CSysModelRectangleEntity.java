package adf.csys.model;

import java.awt.*;

/**
 * Created by Admin on 8/24/2014.
 */
public class CSysModelRectangleEntity extends BasicModelEntity {

    private double[] orgLineStart;
    private double[] orgLineEnd;

    private double[] curLineStart;
    private double[] curLineEnd;

//    private CSysLineEntity cSysLineEntity;

    /**
     * @param lineHead
     * @param lineEnd
     * @param color
     */
    public CSysModelRectangleEntity(double[] lineHead, double[] lineEnd, Color color) {
        super(color);
        definePoints(lineHead, lineEnd);
    }

    private void definePoints(double[] lineStart, double[] lineEnd) {
        this.orgLineStart = (lineStart != null) ? new double[]{lineStart[0], lineStart[1], lineStart[2]} : new double[]{0, 0, 0};
        this.orgLineEnd = (lineEnd != null) ? new double[]{lineEnd[0], lineEnd[1], lineEnd[2]} : new double[]{0, 0, 0};
        this.curLineStart = (lineStart != null) ? new double[]{lineStart[0], lineStart[1], lineStart[2]} : new double[]{0, 0, 0};
        this.curLineEnd = (lineEnd != null) ? new double[]{lineEnd[0], lineEnd[1], lineEnd[2]} : new double[]{0, 0, 0};
//        if (cSysLineEntity != null) {
//            cSysLineEntity.updateUponModelRedefined();
//        }
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
