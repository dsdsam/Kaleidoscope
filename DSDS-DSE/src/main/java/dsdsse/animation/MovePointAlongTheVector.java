package dsdsse.animation;

import vw.valgebra.VAlgebra;

/**
 * Created by Admin on 6/1/2017.
 */
public class MovePointAlongTheVector {

    public static void main(String[] args) {

        double[] vec1 = VAlgebra.initVec3(5, 0, 0);
        double[] vec2 = VAlgebra.initVec3(15, 15, 0);
        double[] vec3 = VAlgebra.initVec3(0, 0, 0);

        double length2 = VAlgebra.vec3Len(vec2);
        double condition;
        double p = 0D;
        do {
            vec3 = VAlgebra.LinCom3(vec3, (1.D - p), vec1, p, vec2);
            int[] intVec = VAlgebra.doubleVec3ToInt(vec3);
            System.out.println("Vec " + intVec[0] + "   " + intVec[1] + "   " + intVec[2] + ",   % =" + p);
            double length1 = VAlgebra.vec3Len(vec3);
            p = p + 0.1;
            condition = Math.abs(length1 - length2);
        } while (condition > 0.1);
    }
}