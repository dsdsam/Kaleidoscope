package dsdsse.animation;

import vw.valgebra.VAlgebra;

/**
 * Created by Admin on 6/19/2017.
 */
public class Calc {

    /*
    Find angle φ between BC and AB using any "angle between two planar vectors" algorithm you find (there are many ways).
Calculate the arc inclusive angle ψ with

ψ = 2*arcsin(cot(φ/2))
Calculate the distance s along BC where the arc ends

s = r*cot(φ/2)
If the direction of BC is e_BC=(ex,ey) and the normal is n_BC=(ey,-ex) then the end of the arc M is

(mx,my) = (bx,by) + s*(ex,ey)
And the circle center is

(kx,ky) = (mx,my) + r*(nx,ny)
Now take N=4 angle increments to rotate point M about K to get your green points

i-th point: i=1..4

gx = kx + (mx-kx)*cos((i/4)*ψ)+(my-ky)*sin((i/4)*ψ)

gy = ky - (mx-kx)*sin((i/4)*ψ)+(my-ky)*cos((i/4)*ψ)
     */

    public static void calculate() {
        double r = 1;
        double[] p1 = {10, 0, 0};
        double[] p2 = {0, 0, 0};
        double[] p3 = {10, 10, 0};
//        double[] p3 = {0, -10, 0};
//
//        double[] p1 = {3, 4, 0};
//        double[] p2 = {4, 3, 0};
//        double[] p3 = {0, -10, 0};

        double dotProdP1P2 = VAlgebra.dot3(p1, p3);
        double magP1 = VAlgebra.vec3Len(p1);
        double magP2 = VAlgebra.vec3Len(p3);
        double cosAlpha = dotProdP1P2 / (magP1 * magP2);
        double alphaRad = Math.acos(cosAlpha);
        double alphaDegree = Math.toDegrees(alphaRad);
        double halfOfAlpha = alphaDegree / 2.;

        double c = r / Math.sin(alphaRad / 2.);
        double b0 = c * Math.cos(alphaRad / 2.);
        double b1 = r * ( Math.cos(alphaRad / 2.) / Math.sin(alphaRad / 2.));
        double lengthFromP2ToM = r / Math.tan(alphaRad / 2.);

        double[] vecFromP2ToP3 = VAlgebra.subVec3(p3, p2);
        double[] dirFromP2ToP3 = VAlgebra.normalizeVec3(vecFromP2ToP3);
        double[] t1 = VAlgebra.addVec3(p2, VAlgebra.scaleVec3(lengthFromP2ToM, dirFromP2ToP3));
        double[] zDir = {0,0,1};
        double[] normToDirFromP2ToP3 = VAlgebra.cross3(dirFromP2ToP3, zDir);
        double[] center = VAlgebra.addVec3(t1, VAlgebra.scaleVec3(r, normToDirFromP2ToP3));

        // case 2
        double[] dirToCenter = VAlgebra.normalizeVec3(center);
        double distFromP2ToM = 1;
        double[] vecFromP2ToP3V2 = VAlgebra.subVec3(p3, p2);
        double[] dirFromP2ToP3V2 = VAlgebra.normalizeVec3(vecFromP2ToP3V2);
        double[] t1V1 = VAlgebra.addVec3(p2, VAlgebra.scaleVec3(distFromP2ToM, dirFromP2ToP3));

        double distFromP2ToK = distFromP2ToM /  Math.cos(alphaRad / 2.);
        double[] k0 = VAlgebra.addVec3(p2, VAlgebra.scaleVec3(distFromP2ToK, dirToCenter));


//        double[] center2 = VAlgebra.addVec3(t1V1, VAlgebra.scaleVec3(r, normToDirFromP2ToP3));



        double distFromMToK0 = distFromP2ToM *  Math.sin(alphaRad / 2.);
        double distFromK0ToK = distFromMToK0 *  Math.tan(alphaRad / 2.);

        double distFromP2ToK0 = distFromP2ToM *  Math.cos(alphaRad / 2.);
        double distFromP2ToK2 = distFromP2ToK0 + distFromK0ToK;

        double[] k02 = VAlgebra.addVec3(p2, VAlgebra.scaleVec3(distFromP2ToK2, dirToCenter));

        double firstNum = 0;
        double secondNum = 0;

//        double r = 0;
        // 1.
        double x = 2;
        double y = 3;
        double radius = Math.hypot(x, y);
        double alpha = Math.toDegrees(Math.atan2(y, x));
        double cot = (Math.toRadians(Math.cos(alpha / 2)) / Math.toRadians(Math.sin(alpha / 2)));
        double con = (Math.toRadians(Math.cos(firstNum))/Math.toRadians(Math.sin(firstNum)));
        // 2.

        double pci = 2 * Math.asin(cot);

        // 3
        double s = r * cot;


    }

    public static void main(String[] args) {
        Calc.calculate();
    }
}

