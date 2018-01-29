package sem.mission.csysbasedviews;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Feb 19, 2012
 * Time: 5:49:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonitorSideCSysView extends SeBasicCSysView {

    public MonitorSideCSysView(double cSysX, double cSysY, double cSysWidth,
                               double cSysHeight, int viewPadding, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, options);
    }

    public void modelChanged(double currentAngle, double[][] mat43) {
        repaint();
    }
}
