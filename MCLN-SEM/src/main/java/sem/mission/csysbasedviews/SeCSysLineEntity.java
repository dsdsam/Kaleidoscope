/*
 * Created on Aug 6, 2005
 *
 */
package sem.mission.csysbasedviews;

import adf.csys.model.ModelLineEntity;
import adf.csys.view.CSysLineEntity;
import adf.csys.view.CSysView;

import java.awt.*;

/**
 * @author xpadmin
 */
public class SeCSysLineEntity extends CSysLineEntity {

    private ModelLineEntity modelLineEntity;

    /**
     * @param modelLineEntity
     */
    public SeCSysLineEntity(CSysView cSysView, ModelLineEntity modelLineEntity) {
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

    @Override
    public void updateUponModelRedefined() {
        init(modelLineEntity.getCurLineHead(), modelLineEntity.getCurLineEnd());
        this.setDrawColor(modelLineEntity.getColor());
//        if(modelLineEntity.getColor() == Color.GRAY){
//            System.out.println(modelLineEntity.getEntityId());
//        }
    }

    public void draw(Graphics g) {
        super.draw(g);
    }


//    /**
//     *
//     */
//    @Override
//    public void draw(Graphics g, int[] scr0, double scale) {
//        super.draw(g, scr0, scale);
//    }

}
