package dsdsse.demo;

import vw.valgebra.VAlgebra;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/4/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicDemoCreator {

    private static double[] calculateKnobLocation(double statementRadius, boolean drawKnobAsArrow,
                                                  double point01[], double point2[]) {
        double[] alongVec = new double[3];
        double[] dirVec = new double[3];
        double[] radVec = new double[3];
        double[] newVec = new double[3];
        double[] knobLocation = {0., 0., 0.};
        double factRadius;

        if (statementRadius != 0) {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) inpNode).RADIUS);
            VAlgebra.subVec3(alongVec, point01, point2);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point2, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.25, point2, 0.75, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point2, 0.50, newVec);
            }
        } else {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) outNode).RADIUS);
            VAlgebra.subVec3(alongVec, point2, point01);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point01, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.75, point01, 0.25, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point01, 0.50, newVec);
            }
        }
        return knobLocation;
    }
}
