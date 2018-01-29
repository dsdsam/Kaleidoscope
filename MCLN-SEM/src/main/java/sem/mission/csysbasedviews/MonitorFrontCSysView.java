package sem.mission.csysbasedviews;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Feb 19, 2012
 * Time: 5:49:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonitorFrontCSysView extends SeBasicCSysView {

    public MonitorFrontCSysView(double cSysX, double cSysY, double cSysWidth,
                                double cSysHeight, int viewPadding, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, options);
    }

    @Override
    public void modelChanged(double currentAngle, double[][] mat43) {
        repaint();
    }

//    @Override
//    protected void updatePresentationUponViewResized() {
//        System.out.println();
////        resetViewScaleAndRescaleEntityList();
//    }
}
