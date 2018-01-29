package mcln.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/28/2015.
 */
public class MclnArcPolyLineEntity {

    private final List<double[]> polyLinePoints = new ArrayList();

    public MclnArcPolyLineEntity(){

    }

    public MclnArcPolyLineEntity(double[][] polyLinePoints) {
        this.polyLinePoints.clear();
        for (double[] point : polyLinePoints) {
            this.polyLinePoints.add(point);
        }
    }

    public MclnArcPolyLineEntity(List<double[]> polyLinePoints) {
        this.polyLinePoints.clear();
        this.polyLinePoints.addAll(polyLinePoints);
    }

    public List<double[]> getPolyLinePoints() {
        return polyLinePoints;
    }

//    public void setPolyLinePoints(double[][] polyLinePoints) {
//        this.polyLinePoints.clear();
//        for (double[] point : polyLinePoints) {
//            this.polyLinePoints.add(point);
//        }
//    }
//
//    public void setPolyLinePoints(List<double[]> polyLinePoints) {
//        this.polyLinePoints.clear();
//        this.polyLinePoints.addAll(polyLinePoints);
//    }
}
