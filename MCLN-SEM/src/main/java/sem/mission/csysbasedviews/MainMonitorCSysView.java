/*
 * Created on Sep 15, 2005
 *
 */
package sem.mission.csysbasedviews;

import java.awt.*;
import java.util.*;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelLineEntity;
import adf.csys.model.ModelPolyLineEntity;
import adf.csys.view.CSysEntity;
import adf.csys.view.BasicCSysEntity;

import javax.swing.*;

/**
 * @author xpadmin
 */
public class MainMonitorCSysView extends SeBasicCSysView {

//    private Color lineColor = new Color(0x003300);
//    private Color borderColor = new Color(0x006600);

    private Formatter formatter = new Formatter();


    public MainMonitorCSysView(double cSysX, double cSysY, double cSysWidth,
                               double cSysHeight, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, 1, options);
        this.setName("MainMonitorCSysView");
    }

//    public void doXRotation(double angle) {
//        paintWithoutImage = true;
//        super.doXRotation(angle);
//        paintWithoutImage = false;
//       // rebuildImageIfNeeded();
//    }

//    /**
//     * @param angle
//     */
//    public void doZRotation(double angle) {
//        paintWithoutImage = true;
//        super.doZRotation(angle);
//        paintWithoutImage = false;
////        rebuildImageIfNeeded();
//    }


    private boolean buildImage;
    private Image backBuffer;
//   private   Dimension backSize;
//    private int currentWidth = 0;
//    private int currentHeight = 0;


    private void rebuildImageIfNeeded() {
        Rectangle origRect = this.getBounds(); //g.getClipBounds();
//        System.out.println("origRect " + origRect.x + "  " + origRect.y + "  " + origRect.width + "  " + origRect.height);

        backBuffer = createImage(origRect.width, origRect.height);
//            System.out.println("Image  w " + backBuffer.getWidth(null) + ", h" + backBuffer.getHeight(null));
        Graphics backGC = backBuffer.getGraphics();
        backGC.setColor(Color.BLACK);
        backGC.fillRect(0, 0, origRect.width, origRect.height);
//        updateCSysEntList(combinedRotatingMatrix);
        paintWorld(backGC);
    }

//    /**
//     * @param mat43
//     */
//    public void updateCSysEntList(double[][] mat43) {
//
//        int numEntities;
//
//        numEntities = worldEntityList.size();
//        for (int i = 0; i < numEntities; i++) {
//            CSysEntity cSysEntity = (CSysEntity) worldEntityList.get(i);
//            cSysEntity.initTransformation();
//            cSysEntity.doTransformation(mat43);
//            cSysEntity.doPrespectiveDistortion();
//            cSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
//
//        numEntities = modelEntityList.size();
//        for (int i = 0; i < numEntities; i++) {
//            CSysEntity obj = (CSysEntity) modelEntityList.get(i);
//            obj.initTransformation();
//            obj.doTransformation(mat43);
//            obj.doPrespectiveDistortion();
//            obj.doCSysToScreenTransformation(scr0, minScale);
//        }
//
//        Rectangle rect = getBounds();
//        rect.x = 0;
//        rect.y = 0;
////         System.out.println("MainMonitorCSysView.updateCSysEntList:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
////        this.paintImmediately(rect);
//        repaint();
//    }
//
//    @Override
//    protected void rescaleEntityList() {
//        super.rescaleEntityList();
////        rebuildImageIfNeeded();
//    }

    boolean paintWithoutImage;

    @Override
    public void paint(Graphics g) {
        Rectangle rect = getBounds();
//        System.out.println("MainMonitorCSysView.paint:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
        g.setColor(Color.BLACK);
//        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, rect.width, rect.height);
//        g.fillRect(rect.x, rect.y, rect.width, rect.height);
//        Image serfaceImage = rebuildImageIfNeeded();
//        if (paintWithoutImage) {
        paintWorld(g);
//        } else if (backBuffer != null) {
//            g.drawImage(backBuffer, 0, 0, null);
//        }

//        if (backBuffer == null) {
//            newBackBuffer();
//        } else {
////             Graphics backGC = backBuffer.getGraphics();
////            backGC.setColor(Color.RED);
////            backGC.clearRect(0, 0, rect.width,
////                    rect.height);
////            paintWorld(backGC);
//            g.drawImage(backBuffer, 100, 0, null);
//        }
//        super.paint(g);
//        paintWorld(g);
        paintModel(g);
        drawInfo(g);
    }


    private Color textColor = new Color(0x008800);
    private Color digitsColor = new Color(0x00AA00);

    private void drawInfo(Graphics g) {
        Dimension size = getSize();
        int base = size.height;// - 4;
        int xyLoc = 2;
        int angLoc = 200;

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));

        g.setColor(textColor);
        g.drawString("Location X:", xyLoc, base - 13);
        g.drawString("Location Y:", xyLoc, base);

        g.drawString("Target   :   ", angLoc, base - 13);
        g.drawString("Direction:   ", angLoc, base);


        String location;
        g.setColor(digitsColor);
        location = String.format("%6.2f", getCurX());
        g.drawString(location, xyLoc + 110, base - 13);
        location = String.format("%6.2f", getCurY());
        g.drawString(location, xyLoc + 110, base);

        String direction = String.format("%6.2f", getTargetDirection() / 100);
        g.drawString(direction, angLoc + 110, base - 13);
        location = String.format("%6.2f", getDirection());
        g.drawString(location, angLoc + 110, base);

    }

    /*
    public void buildRepresentation( SpaceExplorerModel spaceExplorerModel ){
        super.buildRepresentation( spaceExplorerModel );
        setProjection(CSys.ZOYProjection);
        VAlgebra.initYawYRotMat43( yRotatingMatrix, 0, null );
        VAlgebra.initYawYRotMat43( zRotatingMatrix, 0, null );
        doYYRotation( 15 );
        doZRotation( 180 );

    }
    */
    /*
   public void buildRepresentation( SpaceExplorerModel spaceExplorerModel ){
       super.buildRepresentation( spaceExplorerModel );
       setProjection(CSys.ZOYProjection);
       VAlgebra.initYawYRotMat43( yRotatingMatrix, 0, null );
       VAlgebra.initYawYRotMat43( zRotatingMatrix, 0, null );

       doYYRotation( 15 );
       doZRotation( 180 );

   }
    */
    /**
     *
     */
    /*
    public void doYYRotation( double angle ){
        VAlgebra.initYawYRotMat43( yRotatingMatrix, angle, null );
        VAlgebra.trf43Xtrf43( combinedRotatingMatrix,
                yRotatingMatrix, zRotatingMatrix );
//        VAlgebra.printMat43(combinedRotatingMatrix);
//        VAlgebra.initPerspectiveMat43( perspectiveMatrix );
//        VAlgebra.trf43Xtrf43( combinedRotatingMatrix,
//                 combinedRotatingMatrix, perspectiveMatrix );
        updateCSysEntList(combinedRotatingMatrix);
    }
    */

    /**
     *
     */
    /*
    public void doZRotation( double angle ){
//        double[][] zRotatingMatrix = new double[4][3];
        VAlgebra.initZRotMat43( zRotatingMatrix, angle, null );
        VAlgebra.trf43Xtrf43( combinedRotatingMatrix,
                yRotatingMatrix, zRotatingMatrix );
        double[][] perspectiveMatrix = new double[4][3];
        updateCSysEntList(combinedRotatingMatrix);
    }
 */

    //  T R A N S F O R M A T I O N S
    /*
    private void updateCSysEntListForViewPoint( double currentAngle, double[][] mat43 ){

        double[] trVec = VAlgebra.initVec3(
                new double[3], mat43[3][0]*-1, mat43[3][1]*-1, mat43[3][2]*-1 );
        double[][] mat = VAlgebra.initMat43( new double[4][3], 0, trVec );
//        System.out.println("!!!^^^^^^^^^^^^^^^^^^^^^^^^^ "+currentAngle);
//        VAlgebra.printMat43(mat);
        double[][] applMat = VAlgebra.initMat43( new double[4][3], currentAngle, null );
//        applMat = VAlgebra.initMat43( new double[4][3], currentAngle, trVec );
        VAlgebra.trf43Xtrf43( applMat, applMat, mat );
        VAlgebra.trf43Xtrf43( applMat, applMat, combinedRotatingMatrix );

//        VAlgebra.trf43Xtrf43( applMat, combinedRotatingMatrix, applMat );
        int numEntities = worldEntityList.size();
//        double normalToClippingPlane = {mat43[3][0]};
        for( int i=0; i < numEntities; i++ ){
            CSysEntity obj = (CSysEntity) worldEntityList.get(i);
//            obj.viewPointClipAndTransform( 0, applMat );

        }

        repaint();
    }
    */
    /*
    private void doXClipping( double clippingPlane, double[][] mat43 ){
        int numEntities = worldEntityList.size();
        for( int i=0; i < numEntities; i++ ){
            CSysEntity obj = (CSysEntity) worldEntityList.get(i);
            obj.viewPointClipAndTransform( clippingPlane, mat43 );

        }
    }
    */
    // Implementation of ModelChangeListener inteface
    /*
       public void modelChanged( double currentAngle, double[][] mat43 ){
   //        System.out.println("MainMonitorCSysView: ^^^^^^^^^^^^^^^^^^^^^^^^^ "+currentAngle);
           updateCSysEntListForViewPoint( currentAngle, mat43 );
   //        doXClipping( mat43[3][0] );

   //        VAlgebra.printMat43(mat43);

       }
    */
    private double xRotationAngle;
    private double zRotationAngle;
    private CSysEntity[] viewWorldEntityArray = new CSysEntity[0];
    private CSysEntity[] viewModelEntityArray = new CSysEntity[0];
    //  T R A N S F O R M A T I O N S

    /**
     *
     */
    @Override
    protected void buildWorldRepresentation() {
        java.util.List<BasicModelEntity> worldModelEntityList = spaceExplorerModel.getWorldEntityList();
        java.util.List<CSysEntity> viewWorldEntityList = new ArrayList();
        for (int i = 0; i < worldModelEntityList.size(); i++) {

            BasicModelEntity basicModelEntity = worldModelEntityList.get(i);

            CSysEntity cSysEntity = null;
            if (basicModelEntity instanceof ModelLineEntity) {
                cSysEntity = new SeCSysLineEntity(this, (ModelLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(basicModelEntity.getColor());
//                addWorldEntity(cSysLineEntity);
            } else if (basicModelEntity instanceof ModelPolyLineEntity) {
                cSysEntity = new SeCSysViewPolyLineEntity(this, (ModelPolyLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(basicModelEntity.getColor());
//                addWorldEntity(seCSysViewPolylineEntity);
            }
            viewWorldEntityList.add(cSysEntity);
        }

        createAxis(viewWorldEntityList);
        viewWorldEntityArray = viewWorldEntityList.toArray(new BasicCSysEntity[viewWorldEntityList.size()]);
        updateCSysEntList(combinedRotatingMatrix);
    }

//    // A X I S
//    private void createAxis(java.util.List<BasicCSysEntity> viewWorldEntityList) {
//        BasicCSysEntity basicCSysEntity;
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{5, 0, 0});
//        basicCSysEntity.setDrawColor(new Color(0xFFFF00));
//        viewWorldEntityList.add(basicCSysEntity);
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{-5, 0, 0});
//        basicCSysEntity.setDrawColor(new Color(0x777700));
//        viewWorldEntityList.add(basicCSysEntity);
//
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 5, 0});
//        basicCSysEntity.setDrawColor(new Color(0x00FFFF));
//        viewWorldEntityList.add(basicCSysEntity);
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, -5, 0});
//        basicCSysEntity.setDrawColor(new Color(0x007777));
//        viewWorldEntityList.add(basicCSysEntity);
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, 5});
//        basicCSysEntity.setDrawColor(new Color(0xFF00FF));
//        viewWorldEntityList.add(basicCSysEntity);
//
//        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, -5});
//        basicCSysEntity.setDrawColor(new Color(0x770077));
//        viewWorldEntityList.add(basicCSysEntity);
//    }

    /**
     *
     *
     */
    @Override
    protected void buildModelRepresentation() {
        BasicModelEntity[] modelEntityArray = spaceExplorerModel.getAssemblyEntityArray();
        java.util.List<CSysEntity> csysModelEntityList = new ArrayList();
        for (int i = 0; i < modelEntityArray.length; i++) {
            BasicModelEntity basicModelEntity = modelEntityArray[i];
            if (basicModelEntity.isInvisible()) {
                continue;
            }
            CSysEntity cSysEntity = null;
            if (basicModelEntity instanceof ModelLineEntity) {
                cSysEntity = new SeCSysLineEntity(this, (ModelLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(basicModelEntity.getColor());
//                addModelEntity(cSysLineEntity);
            } else if (basicModelEntity instanceof ModelPolyLineEntity) {
                cSysEntity = new SeCSysViewPolyLineEntity(this, (ModelPolyLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(basicModelEntity.getColor());
//                addModelEntity(seCSysViewPolylineEntity);
            }
            csysModelEntityList.add(cSysEntity);
        }
        viewModelEntityArray = csysModelEntityList.toArray(new BasicCSysEntity[csysModelEntityList.size()]);
        updateModelEntList(combinedRotatingMatrix);
    }

//    public void doXRotation(double angle) {
////        double deltaAngle = angle - xRotationAngle;
//        xRotationAngle = angle;
//        VAlgebra.initPitchXRotMat43(xRotatingMatrix, -xRotationAngle, null);
//        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, xRotatingMatrix, zRotatingMatrix);
////        VAlgebra.printMat43(combinedRotatingMatrix);
////        double[][] perspectiveMatrix = new double[4][3];
////        VAlgebra.initPerspectiveMat43( perspectiveMatrix );
////        VAlgebra.trf43Xtrf43( combinedRotatingMatrix,
////                 combinedRotatingMatrix, perspectiveMatrix );
//        updateCSysEntList(combinedRotatingMatrix);
//    }

//    public void doYRotation(double angle) {
////        double deltaAngle = angle - xRotationAngle;
//        xRotationAngle = angle;
//        VAlgebra.initYawYRotMat43(yRotatingMatrix, angle, null);
//        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, yRotatingMatrix, zRotatingMatrix);
////        VAlgebra.printMat43(combinedRotatingMatrix);
////        double[][] perspectiveMatrix = new double[4][3];
////        VAlgebra.initPerspectiveMat43( perspectiveMatrix );
////        VAlgebra.trf43Xtrf43( combinedRotatingMatrix,
////                 combinedRotatingMatrix, perspectiveMatrix );
//        updateCSysEntList(yRotatingMatrix);
//    }

    @Override
    public void updateCSysEntList(double[][] mat43) {
        this.updateCSysEntityArray(mat43, viewWorldEntityArray);
//        for (int i = 0; i < viewWorldEntityArray.length; i++) {
//            CSysEntity cSysEntity = viewWorldEntityArray[i];
//            cSysEntity.doTransformation(mat43);
//            cSysEntity.doPrespectiveDistortion();
//            cSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
        this.updateCSysEntityArray(mat43, viewModelEntityArray);
//        for (int i = 0; i < viewModelEntityArray.length; i++) {
//            CSysEntity cSysEntity = viewModelEntityArray[i];
//            cSysEntity.doTransformation(mat43);
//            cSysEntity.doPrespectiveDistortion();
//            cSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
    }

    @Override
    public void updateModelEntList(double[][] mat43) {
        updateCSysEntityArray(mat43, viewModelEntityArray);
//        for (int i = 0; i < viewModelEntityArray.length; i++) {
//            CSysEntity cSysEntity = viewModelEntityArray[i];
//            cSysEntity.doTransformation(mat43);
//            cSysEntity.doPrespectiveDistortion();
//            cSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
//        Rectangle rect = getBounds();
//        rect.x = 0;
//        rect.y = 0;
////         System.out.println("MainMonitorCSysView.updateCSysEntList:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
//        this.paintImmediately(rect);

//        repaint();

    }

    @Override
    protected void paintWorld(Graphics g) {
        for (int i = 0; i < viewWorldEntityArray.length; i++) {
            CSysEntity cSysEntity = viewWorldEntityArray[i];
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    /**
     * @param g
     */
    @Override
    protected void paintModel(Graphics g) {
        for (int i = 0; i < viewModelEntityArray.length; i++) {
            CSysEntity cSysEntity = viewModelEntityArray[i];
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    /**
     * Implementation of ModelChangeListener inteface
     *
     * @param currentAngle
     * @param mat43
     */
    @Override
    public void modelChanged(double currentAngle, double[][] mat43) {
        Runnable runnable = new Runnable() {
            public void run() {
                updateModelEntList(combinedRotatingMatrix);
                Rectangle rect = getBounds();
                rect.x = 0;
                rect.y = 0;
//         System.out.println("MainMonitorCSysView.updateCSysEntList:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
//                MainMonitorCSysView.this.paintImmediately(rect);
                repaint();
            }
        };
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }

//        System.out.println("isEventDispatchThread "+SwingUtilities.isEventDispatchThread());
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }

//        updateModelEntList(combinedRotatingMatrix);
//        if (backBuffer == null) {
//            buildImage = true;
//        }
    }

    @Override
    public void modelRedefined(BasicModelEntity basicModelEntity) {
        updateWorldEntityUponModelRedefined(basicModelEntity);
    }


}
