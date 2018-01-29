/*
 * Created on Aug 6, 2005
 *
 */
package adf.csys.model;

import java.awt.Color;

import vw.valgebra.VAlgebra;

/**
 * @author xpadmin
 */
public class ModelLineEntity extends BasicModelEntity {

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
    public ModelLineEntity(double[] lineHead, double[] lineEnd, Color color) {
        super(color);
        definePoints(lineHead, lineEnd);
    }

    public void redefinePoints(double[] lineStart, double[] lineEnd) {
        definePoints(lineStart, lineEnd);
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

//    public void setCSysLineEntity(CSysLineEntity cSysLineEntity) {
//        this.cSysLineEntity = cSysLineEntity;
//    }


    /**
     * @return Returns the pnt1.
     */
    public double[] getCurLineHead() {
        return curLineStart;
    }

    /**
     * @return Returns the pnt2.
     */
    public double[] getCurLineEnd() {
        return curLineEnd;
    }

    /**
     * @param mat43
     */
    public void modelToWorldTransform(double[][] mat43) {
        VAlgebra.xyMat43XPnt3(curLineStart, mat43, orgLineStart);
        VAlgebra.xyMat43XPnt3(curLineEnd, mat43, orgLineEnd);
        this.modified = true;
//        VAlgebra.printMat43(mat43);
//        System.out.println("curPnt1 "+curPnt1[0]+"   "+orgPnt1[0]);
    }

//    public void modelToWorldTransform(double[][] mat43) {
//        VAlgebra.Mat43XPnt3(curLineHead, mat43, orgLineHead);
//        VAlgebra.Mat43XPnt3(curLineEnd, mat43, orgLineEnd);
//        this.modified = true;
//    }

    public void modelTranslate(double[] translatingVector) {
        VAlgebra.xyVec3Translate(curLineStart, translatingVector, orgLineStart);
        VAlgebra.xyVec3Translate(curLineEnd, translatingVector, orgLineEnd);
        this.modified = true;
    }
}
