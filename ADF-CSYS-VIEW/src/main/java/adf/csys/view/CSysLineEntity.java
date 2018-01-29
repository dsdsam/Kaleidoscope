/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * @author xpadmin
 */
public class CSysLineEntity extends BasicCSysEntity {

    private double[] modelPnt1 = new double[3];
    private double[] modelPnt2 = new double[3];
    private double[] cSysPnt1 = {0, 0, 0};
    private double[] cSysPnt2 = {0, 0, 0};

    protected int scrX1, scrY1;
    protected int scrX2, scrY2;


    public CSysLineEntity(CSysView parentCSys) {
        super(parentCSys);
    }

    /**
     * This constructor creates line entity initialized by model
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public CSysLineEntity(CSysView parentCSysView, double x1, double y1, double x2, double y2) {
        super(parentCSysView);
        VAlgebra.initVec3(modelPnt1, x1, y1, 0);
        VAlgebra.initVec3(modelPnt2, x2, y2, 0);
        VAlgebra.copyVec3(cSysPnt1, modelPnt1);
        VAlgebra.copyVec3(cSysPnt2, modelPnt2);
    }

    /**
     * This constructor creates line entity backed by model
     *
     * @param p1
     * @param p2
     */
    public CSysLineEntity(CSysView parentCSysView, double[] p1, double[] p2) {
        super(parentCSysView);
        init(p1, p2);
    }

    /**
     * @param lineHead
     * @param lineEnd
     */
    protected void update(double[] lineHead, double[] lineEnd) {
        init(lineHead, lineEnd);
    }

    protected void init(double[] lineHead, double[] lineEnd) {
        modelPnt1 = lineHead;
        modelPnt2 = lineEnd;
        VAlgebra.copyVec3(cSysPnt1, modelPnt1);
        VAlgebra.copyVec3(cSysPnt2, modelPnt2);
    }

    /**
     * @return Returns the point1.
     */
    public double[] getPoint1() {
        return modelPnt1;
    }

    /**
     * @return Returns the point2.
     */
    public double[] getPoint2() {
        return modelPnt2;
    }

    /**
     * @param g
     */
    public void draw(Graphics g) {

        if (hidden || clippedOff || parentCSys == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
//        System.out.println("parentCSys = " + parentCSys.getName() +
//                " scrX1 = " + scrX1 + " scrY1 = " + scrY1 + " scrX2 = " + scrX2 + " scrY2 = " + scrY2);
        g.drawLine(scrX1, scrY1, scrX2, scrY2);
    }


    /**
     *
     */
//    @Override
//    public void draw(Graphics g, int[] scr0, double scale) {
//
//        if (hidden || clippedOff || parentCSys == null) {
//            return;
//        }
//
////        System.out.println("s0 = " + scr0[0] + " s1 = " + scr0[1] + " s2 = " + scr0[2]);
////        System.out.println("parentCSys = " + parentCSys.getName() +
////                " scrX1 = " + scrX1 + " scrY1 = " + scrY1 + " scrX2 = " + scrX2 + " scrY2 = " + scrY2);
//        g.drawLine(scrX1, scrY1, scrX2, scrY2);
//    }

    /**
     *
     */
    /*
    public void viewPointClipAndTransform( double clippingPlane, double[][] mat43 ){
        cSysPnt1 = VAlgebra.copyVec3( getPoint1() );
        cSysPnt2 = VAlgebra.copyVec3( getPoint2() );

//        currentPnt1 = VAlgebra.copyVec3( getPoint1() );
//        currentPnt2 = VAlgebra.copyVec3( getPoint2() );
        transform( mat43 );
        doXClipping( clippingPlane );
    }
    */


    /**
     * Transforms the entity according to the matrix provided
     *
     * @param mat43
     */
    public void doTransformation(double[][] mat43) {
        VAlgebra.Mat43XPnt3(cSysPnt1, mat43, modelPnt1);
        VAlgebra.Mat43XPnt3(cSysPnt2, mat43, modelPnt2);
    }

    public void doNextTransformation(double[][] mat43) {
        VAlgebra.Mat43XPnt3(cSysPnt1, mat43, cSysPnt1);
        VAlgebra.Mat43XPnt3(cSysPnt2, mat43, cSysPnt2);
    }

    /**
     * @param clippingPlane
     */
    public void doXClipping(double clippingPlane) {
//      System.out.println("doXClipping: Begin "+clippingPlane);
//      double[] point1;
//      double[] point2;

//        double[] point1 = VAlgebra.copyVec3(cSysPnt1);
//        double[] point2 = VAlgebra.copyVec3(cSysPnt2);

        double[] point1 = cSysPnt1;
        double[] point2 = cSysPnt2;

//      double[] point1 = VAlgebra.copyVec3( getPoint1() );
//      double[] point2 = VAlgebra.copyVec3( getPoint2() );

//      VAlgebra.printVec3( point1 );
//      VAlgebra.printVec3( point2 );
        clippingPlane = 0;
        double[] referencePoint = {clippingPlane, 0, 0};
        double[] normalVector = {1, 0, 0};
        double[] tmpVector1 = VAlgebra.subVec3(point1, referencePoint);
        double[] tmpVector2 = VAlgebra.subVec3(point2, referencePoint);
//      System.out.println("doXClipping: vectors");
//      VAlgebra.printVec3(tmpVector1);
//      VAlgebra.printVec3(tmpVector2);
        double dotProduct1 = VAlgebra.dot3(normalVector, tmpVector1);
        double dotProduct2 = VAlgebra.dot3(normalVector, tmpVector2);
//      System.out.println("doXClipping: dots"+dotProduct1+"  "+dotProduct2);
        if (((dotProduct1 * dotProduct2) > 0)) {
            if (dotProduct1 < 0) {
                clippedOff = true;
            }
            return;
        }
        clippedOff = false;
        double param = Math.abs(dotProduct1) /
                (Math.abs(dotProduct1) + Math.abs(dotProduct2));
//      System.out.println("doXClipping: param"+param);
//      double[] clippePoint;
        if (dotProduct1 < 0) {
//          clippePoint = VAlgebra.copyVec3( cSysPnt1 );
            cSysPnt1 = VAlgebra.copyVec3(cSysPnt1, VAlgebra.addVec3(point1, VAlgebra.scaleVec3(
                    VAlgebra.subVec3(point2, point1), param)));
//            cSysPnt2 = VAlgebra.copyVec3(point2);
//          System.out.println("doXClipping: new left pnt");
//          VAlgebra.printVec3(cSysPnt1);
//          VAlgebra.printVec3(cSysPnt2);
        } else {
//            cSysPnt1 = VAlgebra.copyVec3(point1);
            cSysPnt2 = VAlgebra.copyVec3(cSysPnt2, VAlgebra.addVec3(point1, VAlgebra.scaleVec3(
                    VAlgebra.subVec3(point2, point1), param)));
//          System.out.println("doXClipping: new rigght pnt");
//          VAlgebra.printVec3(cSysPnt1);
//          VAlgebra.printVec3(cSysPnt2);
        }
    }


    /**
     *
     */
    @Override
    public void doProspectiveDistortion() {

        switch (parentCSys.getProjection()) {
            case CSysView.YOXProjection:
                cSysPnt1[0] = (cSysPnt1[0] * (1 + ((cSysPnt1[2]) / 600)));
                cSysPnt1[1] = (cSysPnt1[1] * (1 + ((cSysPnt1[2]) / 600)));
                cSysPnt2[0] = (cSysPnt2[0] * ((1 + (cSysPnt2[2]) / 600)));
                cSysPnt2[1] = (cSysPnt2[1] * ((1 + (cSysPnt2[2]) / 600)));
//            cSysPnt1[0] = (cSysPnt1[0]/cSysPnt1[2])*640+320;
//            cSysPnt1[1] = (cSysPnt1[1]/cSysPnt1[2])*480+240;
                break;

            case CSysView.ZOXProjection:
                break;

            case CSysView.ZOYProjection:
                cSysPnt1[1] = (cSysPnt1[1] * (1 + ((cSysPnt1[0]) / 600)));
                cSysPnt1[2] = (cSysPnt1[2] * (1 + ((cSysPnt1[0]) / 600)));
                cSysPnt2[1] = (cSysPnt2[1] * ((1 + (cSysPnt2[0]) / 600)));
                cSysPnt2[2] = (cSysPnt2[2] * ((1 + (cSysPnt2[0]) / 600)));
                //        cSysPnt1[0] = (cSysPnt1[0]/cSysPnt1[2])*640+320;
                //        cSysPnt1[1] = (cSysPnt1[1]/cSysPnt1[2])*480+240;
                break;
        }
    }

    public void doCSysToScreenTransformation(int[] scr0, double scale) {

        switch (parentCSys.getProjection()) {

            case CSysView.ZOXProjection:
                scrX1 = scr0[0] + (int) (cSysPnt1[0] * scale);
                scrY1 = scr0[2] + (int) (-cSysPnt1[2] * scale);
                scrX2 = scr0[0] + (int) (cSysPnt2[0] * scale);
                scrY2 = scr0[2] + (int) (-cSysPnt2[2] * scale);
//            System.out.println("XOZ: a1 = "+point1[0]+" a2 = "+point1[2]+" b1 = "+point2[0]+" b2 = "+point2[2]);
                break;

            case CSysView.ZOYProjection:
                scrX1 = scr0[0] + (int) (cSysPnt1[1] * scale);
                scrY1 = scr0[2] + (int) (-cSysPnt1[2] * scale);
                scrX2 = scr0[0] + (int) (cSysPnt2[1] * scale);
                scrY2 = scr0[2] + (int) (-cSysPnt2[2] * scale);
                break;

            default: // XoY projection
                scrX1 = scr0[0] + (int) (cSysPnt1[0] * scale);
                scrY1 = scr0[1] + (int) (-cSysPnt1[1] * scale);
                scrX2 = scr0[0] + (int) (cSysPnt2[0] * scale);
                scrY2 = scr0[1] + (int) (-cSysPnt2[1] * scale);
//            System.out.println("a1 = "+cSysPnt1[0]+" a2 =" + ""+cSysPnt1[1]+" b1 = "+cSysPnt2[0]+" b2 = "+cSysPnt2[1]);
//            System.out.println("s1 = "+scrX1+" s2 = "+scrY1+" b1 = "+scrX2+" b2 = "+scrY2);
                break;
        }
    }

}
