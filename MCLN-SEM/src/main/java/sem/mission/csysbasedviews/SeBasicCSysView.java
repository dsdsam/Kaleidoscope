/*
 * Created on Aug 2, 2005
 *
 */
package sem.mission.csysbasedviews;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import adf.csys.model.ModelPolyLineEntity;
import adf.csys.view.CSysView;
import sem.mission.explorer.model.SpaceExplorerModel;
import sem.mission.controlles.modelcontroller.ModelController;
import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelLineEntity;
import adf.csys.model.ModelChangeListener;
import adf.csys.view.BasicCSysEntity;
import adf.csys.view.CSysLineEntity;
import adf.csys.view.CSysEntity;

/**
 * @author xpadmin
 */
public class SeBasicCSysView extends CSysView implements ModelChangeListener {

    private final Color[] axesColors =
            {
                    (new Color(0xFFFF00)), (new Color(0x777700)), (new Color(0x00FFFF)),
                    (new Color(0x007777)), (new Color(0xFF00FF)), (new Color(0x770077)),
            };

    protected SpaceExplorerModel spaceExplorerModel;

    private CSysEntity[] csysModelEntityArray = new CSysEntity[0];

    public SeBasicCSysView(double cSysX, double cSysY, double cSysWidth, double cSysHeight, int viewPadding,
                           int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, options);
        createAxis(axesColors);
    }

    /**
     * @param spaceExplorerModel
     */
    // no override
    public void buildRepresentation(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;
        buildWorldRepresentation();
        buildModelRepresentation();
    }

    /**
     *
     */
    // no override
    protected void buildWorldRepresentation() {
        List<BasicModelEntity> worldModelEntityList = spaceExplorerModel.getWorldEntityList();
        for (int i = 0; i < worldModelEntityList.size(); i++) {

            BasicModelEntity basicModelEntity = worldModelEntityList.get(i);

            if (basicModelEntity instanceof ModelLineEntity) {
                SeCSysLineEntity cSysLineEntity = new SeCSysLineEntity(this, (ModelLineEntity) basicModelEntity);
                cSysLineEntity.setDrawColor(basicModelEntity.getColor());
                addWorldEntity(cSysLineEntity);
            } else if (basicModelEntity instanceof ModelPolyLineEntity) {
                SeCSysViewPolyLineEntity seCSysViewPolylineEntity =
                        new SeCSysViewPolyLineEntity(this, (ModelPolyLineEntity) basicModelEntity);
                seCSysViewPolylineEntity.setDrawColor(basicModelEntity.getColor());
                addWorldEntity(seCSysViewPolylineEntity);
            }

        }
        createAxis(axesColors);
        updateCSysEntList(combinedRotatingMatrix);
    }


    /**
     *
     *
     */
    // no override
    protected void buildModelRepresentation() {
        BasicModelEntity[] modelEntityArray = spaceExplorerModel.getAssemblyEntityArray();
        List<BasicCSysEntity> csysModelEntityList = new ArrayList();
        for (int i = 0; i < modelEntityArray.length; i++) {
            BasicModelEntity basicModelEntity = modelEntityArray[i];
            if (basicModelEntity.isInvisible()) {
                continue;
            }
            BasicCSysEntity basicCSysEntity = null;
            if (basicModelEntity instanceof ModelLineEntity) {
                basicCSysEntity = new SeCSysLineEntity(this, (ModelLineEntity) basicModelEntity);
                basicCSysEntity.setDrawColor(basicModelEntity.getColor());
//                addModelEntity(cSysLineEntity);
            } else if (basicModelEntity instanceof ModelPolyLineEntity) {
                basicCSysEntity = new SeCSysViewPolyLineEntity(this, (ModelPolyLineEntity) basicModelEntity);
                basicCSysEntity.setDrawColor(basicModelEntity.getColor());
//                addModelEntity(seCSysViewPolylineEntity);
            }
            basicCSysEntity.setParentCSys(this);
            csysModelEntityList.add(basicCSysEntity);
        }
        this.setCsysModelEntityArray(csysModelEntityList.toArray(new BasicCSysEntity[csysModelEntityList.size()]));
        updateModelEntList(combinedRotatingMatrix);
    }

    public void setCsysModelEntityArray(BasicCSysEntity[] csysModelEntityArray) {
        this.csysModelEntityArray = csysModelEntityArray;
    }

    // A X I S
    protected void createAxis(java.util.List<CSysEntity> viewWorldEntityList) {
        BasicCSysEntity basicCSysEntity;

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{5, 0, 0});
        basicCSysEntity.setDrawColor(new Color(0xFFFF00));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{-5, 0, 0});
        basicCSysEntity.setDrawColor(new Color(0x777700));
        viewWorldEntityList.add(basicCSysEntity);


        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 5, 0});
        basicCSysEntity.setDrawColor(new Color(0x00FFFF));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, -5, 0});
        basicCSysEntity.setDrawColor(new Color(0x007777));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, 5});
        basicCSysEntity.setDrawColor(new Color(0xFF00FF));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, -5});
        basicCSysEntity.setDrawColor(new Color(0x770077));
        viewWorldEntityList.add(basicCSysEntity);
    }

    //      public void repaint(){
//           if(this.getParent() != null){
//                  super.repaint();
//          Thread.dumpStack();
//           }
//      }
    private boolean recreateSurface = true;

    @Override
    public void paint(Graphics g) {

        Rectangle rect = g.getClipBounds();
        g.setColor(Color.BLACK);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

//        Rectangle rect2 = this.getBounds();
//        if (recreateSurface) {
//            if(rect2.width != 0){
//
//                paintWorld(g);
//                 recreateSurface = false;
//            }
//        }

//        ((Graphics2D) g).drawImage(bufferedImage, 100, 100, Color.RED, null);
//        paintWorld(g);
        paintModel(g);
    }

    /**
     * @param g
     */
    protected void paintModel(Graphics g) {
        for (int i = 0; i < csysModelEntityArray.length; i++) {
            CSysEntity cSysEntity = csysModelEntityArray[i];
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    @Override
//        public void paint(Graphics g) {
//        super.paint(g);
//        paintWorld(g);

//        paintModel(g);
//        if (true) {
//            paintGrid(g);
//        }
//    }
//    public void paint(Graphics g) {
////    minScale = minScale * 0.6;
//        super.paint(g);
//
//    }

    public double getCurX() {
        return spaceExplorerModel.getCurX();
    }

    public double getCurY() {
        return spaceExplorerModel.getCurY();
    }

    public double getDirection() {
        return spaceExplorerModel.getDirection();
    }


    public double getTargetDirection() {
        return ModelController.getInstance().getTargetDirection();
    }


    public void modelRedefined(BasicModelEntity basicModelEntity) {

    }


    /**
     * @param mat43
     */
    public void updateModelEntList(double[][] mat43) {
        updateCSysEntityArray(mat43, csysModelEntityArray);
//        for (int i = 0; i < csysModelEntityArray.length; i++) {
//            BasicCSysEntity basicCSysEntity = (BasicCSysEntity)csysModelEntityArray[i];
////            obj.initTransformation();
//            basicCSysEntity.doTransformation(mat43);
//            basicCSysEntity.doPrespectiveDistortion();
//            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
//        Rectangle rect = getBounds();
//        rect.x = 0;
//        rect.y = 0;
////         System.out.println("MainMonitorCSysView.updateCSysEntList:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
//        this.paintImmediately(rect);

//        repaint();

    }


    @Override
    public void updateCSysEntList(double[][] mat43) {

        updateCSysWorldEntList(mat43, this.getWorldEntityList());
        updateCSysWorldEntList(mat43, this.getWorldAxesList());
        updateCSysEntityArray(mat43, csysModelEntityArray);
//        for (int i = 0; i < csysModelEntityArray.length; i++) {
//            CSysEntity cSysEntity = csysModelEntityArray[i];
////            obj.initTransformation();
//            cSysEntity.doTransformation(mat43);
//            cSysEntity.doPrespectiveDistortion();
//            cSysEntity.doCSysToScreenTransformation(scr0, minScale);
//        }
    }

    //
    // Implementation of ModelChangeListener inteface
    //

    public void updateWorldEntityUponModelRedefined(BasicModelEntity basicModelEntity) {
        List<CSysEntity> worldEntityList = this.getWorldEntityList();
        for (int i = 0; i < worldEntityList.size(); i++) {
            CSysEntity cSysEntity = worldEntityList.get(i);
            cSysEntity.updateUponModelRedefined();
        }

        updateCSysEntList(combinedRotatingMatrix);
    }


    public void modelChanged(double currentAngle, double[][] mat43) {
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWwWWWWWWWW");
//        Thread.dumpStack();
        updateModelEntList(combinedRotatingMatrix);
    }
}