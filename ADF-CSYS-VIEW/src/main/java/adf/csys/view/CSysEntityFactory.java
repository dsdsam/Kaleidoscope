/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

import java.awt.*;

/**
 * @author xpadmin
 */
public class CSysEntityFactory {

    public static CSysPointEntity createPointEntity(CSysView cSysView, double[] point, Color drawColor) {
        return new CSysPointEntity(cSysView, point, drawColor, true);
    }

    public static CSysLineEntity createLineEntity(CSysView cSysView, double x1, double y1, double x2, double y2) {
        return new CSysLineEntity(cSysView, x1, y1, x2, y2);
    }
}
