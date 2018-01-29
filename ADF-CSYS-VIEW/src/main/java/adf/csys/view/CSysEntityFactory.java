/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

/**
 * @author xpadmin
 *
 */
public class CSysEntityFactory {

    public static CSysEntity createLineEntity(CSysView cSysView,  double x1, double y1, double x2, double y2 ){
        return new CSysLineEntity(cSysView, x1, y1, x2, y2 );
    }
}
