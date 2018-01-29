package dsdsse.graphview;

import vw.valgebra.VAlgebra;

import java.util.List;

/**
 * Created by Admin on 12/12/2015.
 */
class SplineCurveAnalyser {

    public static final double EPS = 0.05D;

    static void findSplineFlatSegments(List<double[]> splineScrPoints) {
        int nPoints = splineScrPoints.size();
        if (nPoints < 3) {
            return;
        }
        double[] prevPoint = splineScrPoints.get(0);
        double[] currPoint = splineScrPoints.get(1);
        double dot = VAlgebra.dot3(prevPoint, currPoint);
        double l1 = VAlgebra.vec2Len(prevPoint);
        double l2 = VAlgebra.vec2Len(currPoint);
        double prevCos = dot / (l1 * l2);

        for (int i = 2; i < nPoints; i++) {
            currPoint = splineScrPoints.get(i);
            dot = VAlgebra.dot3(prevPoint, currPoint);
            l1 = VAlgebra.vec2Len(prevPoint);
            l2 = VAlgebra.vec2Len(currPoint);
            double cos = dot / (l1 * l2);
            if (Math.abs(prevCos - 1.) < EPS && Math.abs(cos - 1.) < EPS) {
                currPoint[2] = 1;
//                System.out.println("Flat section segment " + 1);
            }
            prevCos = cos;
            prevPoint = currPoint;
        }
    }

    static void test() {
        System.out.println("Test");
////        double[][] data = {
//                {0,1,0},
//                {1,1,0},
//                {1,2,0},
//                {2,2,0},
////        };
        double[][] data = {
                {1, 0, 0},
                {0, 1, 0},
                {1, 0, 0},
                {1, 0, 0},
                {1, 1, 0},
        };
        double[] p0 = data[0];
        double[] p1 = data[1];
        double[] p2 = data[2];
        for (int i = 0; i < data.length - 1; i++) {
            double dot = VAlgebra.dot3(p0, p1);
            double l1 = VAlgebra.vec2Len(p0);
            double l2 = VAlgebra.vec2Len(p1);
            double cos = dot / (l1 * l2);

            p0 = data[i + 1];
            p1 = data[i + 2];
//            p2 = data[i + 3];
        }
//
    }

    private static double[] currentMouseVec = {0., 0., 0.};
    private static double[] mouseToKnotVec = {0., 0., 0.};

    static int[] getTwoMouseNearestKnots(int x, int y, List<double[]> splineScrKnots ) {
//        System.out.println("SplineCurveAnalyser.getTwoMouseNearestKnots: x = " + x + ",  y = " + y);
        currentMouseVec[0] = x;
        currentMouseVec[1] = y;
        double[] firstKnot = {0, 0, 0};
        double[] secondKnot = {0, 0, 0};
        int nKnots = splineScrKnots.size();
        if (nKnots < 3) {
            return null;
        }

        double minDistance = Double.MAX_VALUE;
        int minDistanceKnotIndex = -1;
        for (int i = 1; i < nKnots - 1; i++) {

            double[] currKnot = splineScrKnots.get(i);
            mouseToKnotVec = VAlgebra.subVec3(mouseToKnotVec, currKnot, currentMouseVec);
            double mouseToKnotDist = VAlgebra.vec2Len(mouseToKnotVec);
//            System.out.println("mouseToKnotDist " + mouseToKnotDist);
            double absDistance = Math.abs(mouseToKnotDist);
            // absDistance< 5. &&
            if (absDistance < minDistance) {
                minDistance = absDistance;
                minDistanceKnotIndex = i;
                firstKnot = currKnot;
            }
//            dot = VAlgebra.dot3(prevPoint, currPoint);
//            l1 = VAlgebra.vec2Len(prevPoint);
//            l2 = VAlgebra.vec2Len(currPoint);
//            double cos = dot / (l1 * l2);
//            if (Math.abs(prevCos - 1.) < EPS && Math.abs(cos - 1.) < EPS) {
//                currPoint[2] = 1;
//                System.out.println("Flat section segment " + 1);
//            }
//            prevCos = cos;
//            prevPoint = currPoint;
        }
//        System.out.println("mouseToKnot Min Dist " + minDistance+", index "+minDistanceKnotIndex);
        double firstMinDistance = minDistance;
        int firstMInDistanceKnotIndex = minDistanceKnotIndex;
//        System.out.println("mouseToKnot Min Dist " + minDistance+", index "+minDistanceKnotIndex);

        minDistance = Double.MAX_VALUE;
        minDistanceKnotIndex = -1;
        for (int i = 1; i < nKnots - 1; i++) {
            if (i == firstMInDistanceKnotIndex || ((i != firstMInDistanceKnotIndex - 1) && (i != (firstMInDistanceKnotIndex + 1)))) {
                continue;
            }
            currentMouseVec[0] = x;
            currentMouseVec[1] = y;
            double[] currKnot = splineScrKnots.get(i);
            mouseToKnotVec = VAlgebra.subVec3(mouseToKnotVec, currKnot, currentMouseVec);
            double mouseToKnotDist = VAlgebra.vec2Len(mouseToKnotVec);
//            System.out.println("mouseToKnotDist " + mouseToKnotDist);
            double absDistance = Math.abs(mouseToKnotDist);
            // absDistance< 5. &&
            if (absDistance < minDistance) {
                minDistance = absDistance;
                minDistanceKnotIndex = i;
                secondKnot = currKnot;
            }
//            dot = VAlgebra.dot3(prevPoint, currPoint);
//            l1 = VAlgebra.vec2Len(prevPoint);
//            l2 = VAlgebra.vec2Len(currPoint);
//            double cos = dot / (l1 * l2);
//            if (Math.abs(prevCos - 1.) < EPS && Math.abs(cos - 1.) < EPS) {
//                currPoint[2] = 1;
//                System.out.println("Flat section segment " + 1);
//            }
//            prevCos = cos;
//            prevPoint = currPoint;
        }
        double secondMinDistance = minDistance;
        int secondMInDistanceKnotIndex = minDistanceKnotIndex;
//        System.out.println("first mouseToKnot Min Dist " + firstMinDistance + ", index " + firstMInDistanceKnotIndex);
//        System.out.println("second mouseToKnot Min Dist " + secondMinDistance + ", index " + secondMInDistanceKnotIndex);
        if (secondMInDistanceKnotIndex < 0) {
            return null;
        }
//        double[][] selectedKnots = new double[][]{firstKnot, secondKnot};
        int[] selectedKnots;
        if(firstMInDistanceKnotIndex < secondMInDistanceKnotIndex) {
              selectedKnots = new int[]{firstMInDistanceKnotIndex, secondMInDistanceKnotIndex};
        }else{
            selectedKnots = new int[]{secondMInDistanceKnotIndex, firstMInDistanceKnotIndex};
        }
        return selectedKnots;
    }
}
