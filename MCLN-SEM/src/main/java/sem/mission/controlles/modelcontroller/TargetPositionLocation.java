package sem.mission.controlles.modelcontroller;

import vw.VectorPoint;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 29, 2011
 * Time: 10:08:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetPositionLocation extends VectorPoint {

    double distance;

    public TargetPositionLocation() {
        super();
    }

    public TargetPositionLocation(double x, double y) {
        super(x, y);
        distance = Math.sqrt(x * x + y * y);
    }

    public double getDistance() {
        return distance;
    }

}
