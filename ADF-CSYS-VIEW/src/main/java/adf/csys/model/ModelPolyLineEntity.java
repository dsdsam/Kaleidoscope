/*
 * Created on Aug 17, 2005
 *
 */
package adf.csys.model;

import vw.valgebra.VAlgebra;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xpadmin
 */
public class ModelPolyLineEntity extends BasicModelEntity {

    private List<Point3d> polyLine = new ArrayList<Point3d>();
    private boolean closed;

    public ModelPolyLineEntity(boolean closed) {
        this.closed = closed;
    }

    public ModelPolyLineEntity(Color color, boolean closed) {
        super(color);
        this.closed = closed;
    }

    /**
     * @param i
     * @return
     */
    public boolean isVisible(int i) {
        return polyLine.get(i).isVisible();
    }

    /**
     * @return Returns the closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed The closed to set.
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * @param point
     */
    public void addPoint(double[] point) {
        polyLine.add(createPoint3d(point));
    }

    /**
     * @param i
     * @return  double[]
     */
    public double[] getPoint(int i) {
        return polyLine.get(i).getPoint();
    }

    /**
     * @return Returns the polyline.
     */
    public List<Point3d> getPolyLine() {
        return polyLine;
    }

    /**
     * @param polyLine The polyline to set.
     */
    public void setPolyLine(List polyLine) {
        this.polyLine = polyLine;
    }

    /**
     * @param point
     * @return Point3d
     */
    private Point3d createPoint3d(double[] point) {
        double[] copyPnt = new double[3];
        copyPnt[0] = point[0];
        copyPnt[1] = point[1];
        copyPnt[2] = point[2];
        return new Point3d(copyPnt);
    }

    /**
     * @param mat43
     */
    public void modelToWorldTransform(double[][] mat43) {
        for (int i = 0; i < polyLine.size(); i++) {
            polyLine.get(i).modelToWorldTransform(mat43);
        }
        this.modified = true;
//        System.out.println("curPnt1 "+((Point3d)polyline.get(0)).getPoint()[0]);
    }

    public void modelTranslate(double[] translationVector) {
        for (int i = 0; i < polyLine.size(); i++) {
            polyLine.get(i).modelTranslate(translationVector);
        }
        this.modified = true;
    }

    /**
     * @author xpadmin
     */
    private class Point3d {

        private double[] orgPoint;
        private double[] curPoint;
        private boolean visible = true;


        Point3d(double[] point) {
            this.orgPoint = new double[]{point[0], point[1], point[2]};;
            this.curPoint = new double[]{point[0], point[1], point[2]};
        }

        /**
         * @return Returns the visible.
         */
        public boolean isVisible() {
            return visible;
        }

        /**
         * @param visible The visible to set.
         */
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        /**
         * @return Returns the point.
         */
        public double[] getPoint() {
            return curPoint;
        }

        /**
         * @param mat43
         */
        public void modelToWorldTransform(double[][] mat43) {
            VAlgebra.Mat43XPnt3(curPoint, mat43, orgPoint);
        }

        public void modelTranslate(double[] translationVector) {
            VAlgebra.xyVec3Translate(curPoint, translationVector, orgPoint);
        }
    }


}
