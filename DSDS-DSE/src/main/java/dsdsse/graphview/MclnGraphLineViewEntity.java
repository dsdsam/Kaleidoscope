package dsdsse.graphview;

import adf.csys.model.ModelLineEntity;
import adf.csys.view.CSysView;
import adf.csys.view.CSysLineEntity;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 6/5/13
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
class MclnGraphLineViewEntity extends CSysLineEntity {

    private ModelLineEntity modelLineEntity;

    private double[] cSysLineStart;
    private double[] cSysLineEnd;


    public MclnGraphLineViewEntity(CSysView cSysView, double[] cSysLineStart, double[] cSysLineEnd) {
        super(cSysView, cSysLineStart, cSysLineEnd);
        this.cSysLineStart = cSysLineStart;
        this.cSysLineEnd = cSysLineEnd;
    }

    /**
     * @param modelLineEntity
     */
    public MclnGraphLineViewEntity(CSysView cSysView, ModelLineEntity modelLineEntity) {
        super(cSysView, modelLineEntity.getCurLineHead(), modelLineEntity.getCurLineEnd());
        this.modelLineEntity = modelLineEntity;
    }

    /**
     * @return Returns the point1.
     */
    public double[] getPoint1() {
        return modelLineEntity.getCurLineHead();
    }

    /**
     * @return Returns the point1.
     */
    public double[] getPoint2() {
        return modelLineEntity.getCurLineEnd();
    }

    // not Override
    public void updatePlacement(double[] cSysLineStart, double[] cSysLineEnd) {
        this.cSysLineStart = (cSysLineStart != null) ? cSysLineStart : getPoint1();
        this.cSysLineEnd = (cSysLineEnd != null) ? cSysLineEnd : getPoint2();
//        System.out.println("MclnGraphLineViewEntity: updatePlacement   " + lineStart);
//        System.out.println("MclnGraphLineViewEntity: updatePlacement   " + lineEnd);
        super.update(this.cSysLineStart, this.cSysLineEnd);
    }

    @Override
    public void updateUponModelRedefined() {
        init(modelLineEntity.getCurLineHead(), modelLineEntity.getCurLineEnd());
        this.setDrawColor(modelLineEntity.getColor());
//        if(modelLineEntity.getColor() == Color.GRAY){
//            System.out.println(modelLineEntity.getEntityId());
//        }
    }
}
