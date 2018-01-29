package dsdsse.graphview;

import vw.valgebra.VAlgebra;

import java.util.List;

/**
 * Created by Admin on 3/6/2016.
 */
public class MclnGraphViewEditorHelper {

    private static final double EPS = 0.006D;

    static void findSplineFlatSegments(List<double[]> splineScrPoints) {
        int nPoints = splineScrPoints.size();
        if (nPoints < 3) {
            return;
        }
        double[] prevPoint = splineScrPoints.get(0);
        double[] currPoint = splineScrPoints.get(1);
        double[] seg1 = VAlgebra.subVec3(currPoint, prevPoint);
        prevPoint = currPoint;

        for (int i = 2; i < nPoints; i++) {
            currPoint = splineScrPoints.get(i);
            double[] prevPointCopy = VAlgebra.copyVec3(prevPoint);
            prevPointCopy[2] = 0;
            currPoint[2] = 0;
            double[] seg2 = VAlgebra.subVec3(currPoint, prevPointCopy);

            double dot = VAlgebra.dot3(seg1, seg2);
            double len1 = VAlgebra.vec2Len(seg1);
            double len2 = VAlgebra.vec2Len(seg2);
            double cos = dot / (len1 * len2);
            if (Math.abs(cos - 1.) > EPS) {
                currPoint[2] = -1;
            }
            seg1 = seg2;
            prevPoint = currPoint;
        }
    }
}
