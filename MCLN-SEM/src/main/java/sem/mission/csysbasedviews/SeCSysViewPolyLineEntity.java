/*
 * Created on Aug 17, 2005
 *
 */
package sem.mission.csysbasedviews;

import adf.csys.model.ModelPolyLineEntity;
import adf.csys.view.CSysPolyLineEntity;
import adf.csys.view.CSysView;

/**
 * @author xpadmin
 *
 */
public class SeCSysViewPolyLineEntity extends CSysPolyLineEntity {

    /**
     * 
     * @param modelPolyLineEntity
     */
    SeCSysViewPolyLineEntity(CSysView cSysView, ModelPolyLineEntity modelPolyLineEntity){
        super(cSysView, modelPolyLineEntity);
    }
}
