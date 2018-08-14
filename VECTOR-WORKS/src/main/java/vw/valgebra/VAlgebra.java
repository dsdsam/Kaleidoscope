/*
 * Created on Aug 8, 2005
 *
 */
package vw.valgebra;

/**
 * @author xpadmin
 */


public final class VAlgebra {

    public static final double ALMOST_ZERO = 0.0001;

    public static void Mat43XPnt3(double[] outputPnt, double[][] mat, double[] inputPnt) {
        mat43XPnt3(outputPnt, mat, inputPnt);
    }

    public static double[] mat43XPnt3(double[][] mat, double[] inputPnt) {
        double[] outVec = new double[3];
        return mat43XPnt3(outVec, mat, inputPnt);
    }

    /**
     * @param outVec
     * @param mat
     * @param inputPnt
     */
    public static double[] mat43XPnt3(double[] outVec, double[][] mat, double[] inputPnt) {

        if (outVec == null) {
            outVec = new double[3];
        }

        double x, y, z;
        x = mat[0][0] * inputPnt[0] + mat[0][1] * inputPnt[1] + mat[0][2] * inputPnt[2] + mat[3][0];
        y = mat[1][0] * inputPnt[0] + mat[1][1] * inputPnt[1] + mat[1][2] * inputPnt[2] + mat[3][1];
        z = mat[2][0] * inputPnt[0] + mat[2][1] * inputPnt[1] + mat[2][2] * inputPnt[2] + mat[3][2];

        outVec[0] = x;
        outVec[1] = y;
        outVec[2] = z;

        return outVec;
    }

    public static void xyMat43XPnt3(double[] outputPnt, double[][] mat, double[] inputPnt) {

        double x, y, z;
        x = mat[0][0] * inputPnt[0] + mat[0][1] * inputPnt[1] + mat[3][0];
        y = mat[1][0] * inputPnt[0] + mat[1][1] * inputPnt[1] + mat[3][1];

        outputPnt[0] = x;
        outputPnt[1] = y;
    }

    public static void xyVec3Translate(double[] output, double[] translatingVector, double[] input) {
        output[0] = input[0] + translatingVector[0];
        output[1] = input[1] + translatingVector[1];
    }
//
//    public static void updateMat43ZRotationPart(double mat43[][], double sin, double cos) {
//        mat43[0][0] += cos;
//        mat43[0][1] += -sin;
//        mat43[1][0] += sin;
//        mat43[1][1] += cos;
//    }

//    public static void initMat43TranslationPart(double mat43[][], double tVec[]) {
//        mat43[3][0] = tVec[0];
//        mat43[3][1] = tVec[1];
//        mat43[3][2] = tVec[2];
//    }

    /**
     * @param mat43
     * @param tVec
     */
//    public static void updateMat43Translate(double mat43[][], double tVec[]) {
//        mat43[3][0] += tVec[0];
//        mat43[3][1] += tVec[1];
//        mat43[3][2] += tVec[2];
//    }

    /**
     * @param mat
     */
    public static void printMat43(String header, double[][] mat) {
        System.out.println("\n" + header + "\n matrix:");
        System.out.println(mat[0][0] + "  " + mat[0][1] + "  " + mat[0][2]);
        System.out.println(mat[1][0] + "  " + mat[1][1] + "  " + mat[1][2]);
        System.out.println(mat[2][0] + "  " + mat[2][1] + "  " + mat[2][2]);
        System.out.println(mat[3][0] + "  " + mat[3][1] + "  " + mat[3][2]);
        System.out.println();
    }

    public static void printMat43(String header, int[][] mat) {
        System.out.println("\n" + header + "\n matrix:");
        System.out.println(mat[0][0] + "  " + mat[0][1] + "  " + mat[0][2]);
        System.out.println(mat[1][0] + "  " + mat[1][1] + "  " + mat[1][2]);
        System.out.println(mat[2][0] + "  " + mat[2][1] + "  " + mat[2][2]);
        System.out.println(mat[3][0] + "  " + mat[3][1] + "  " + mat[3][2]);
        System.out.println();
    }

    public static String vecToString(double[] vec) {
        String string = "" + vec[0] + "  " + vec[1] + "  " + vec[2];
        return string;
    }

    /**
     * @param vec
     */
    public static void printVec3(double[] vec) {
        System.out.println("printVec3: " + vecToString(vec));
    }

    /**
     * @param outputMat
     * @param inputMat
     */
    public static void copyMat43(double[][] outputMat, double[][] inputMat) {
        copyVec3(outputMat[0], inputMat[0]);
        copyVec3(outputMat[1], inputMat[1]);
        copyVec3(outputMat[2], inputMat[2]);
        copyVec3(outputMat[3], inputMat[3]);
    }

    private final static double[][] trf43Xtrf43TmpMatrix = new double[4][3];

    /**
     * @param oMat
     * @param Mat1
     * @param Mat2
     */
    public static void trf43Xtrf43(double[][] oMat, double[][] Mat1, double[][] Mat2) {

        trf43Xtrf43TmpMatrix[0][0] = Mat1[0][0] * Mat2[0][0] + Mat1[0][1] * Mat2[1][0] + Mat1[0][2] * Mat2[2][0];
        trf43Xtrf43TmpMatrix[0][1] = Mat1[0][0] * Mat2[0][1] + Mat1[0][1] * Mat2[1][1] + Mat1[0][2] * Mat2[2][1];
        trf43Xtrf43TmpMatrix[0][2] = Mat1[0][0] * Mat2[0][2] + Mat1[0][1] * Mat2[1][2] + Mat1[0][2] * Mat2[2][2];

        trf43Xtrf43TmpMatrix[1][0] = Mat1[1][0] * Mat2[0][0] + Mat1[1][1] * Mat2[1][0] + Mat1[1][2] * Mat2[2][0];
        trf43Xtrf43TmpMatrix[1][1] = Mat1[1][0] * Mat2[0][1] + Mat1[1][1] * Mat2[1][1] + Mat1[1][2] * Mat2[2][1];
        trf43Xtrf43TmpMatrix[1][2] = Mat1[1][0] * Mat2[0][2] + Mat1[1][1] * Mat2[1][2] + Mat1[1][2] * Mat2[2][2];

        trf43Xtrf43TmpMatrix[2][0] = Mat1[2][0] * Mat2[0][0] + Mat1[2][1] * Mat2[1][0] + Mat1[2][2] * Mat2[2][0];
        trf43Xtrf43TmpMatrix[2][1] = Mat1[2][0] * Mat2[0][1] + Mat1[2][1] * Mat2[1][1] + Mat1[2][2] * Mat2[2][1];
        trf43Xtrf43TmpMatrix[2][2] = Mat1[2][0] * Mat2[0][2] + Mat1[2][1] * Mat2[1][2] + Mat1[2][2] * Mat2[2][2];

        trf43Xtrf43TmpMatrix[3][0] = Mat1[0][0] * Mat2[3][0] + Mat1[0][1] * Mat2[3][1] + Mat1[0][2] * Mat2[3][2] + Mat1[3][0];

        trf43Xtrf43TmpMatrix[3][1] = Mat1[1][0] * Mat2[3][0] + Mat1[1][1] * Mat2[3][1] + Mat1[1][2] * Mat2[3][2] + Mat1[3][1];

        trf43Xtrf43TmpMatrix[3][2] = Mat1[2][0] * Mat2[3][0] + Mat1[2][1] * Mat2[3][1] + Mat1[2][2] * Mat2[3][2] + Mat1[3][2];

        copyVec3(oMat[0], trf43Xtrf43TmpMatrix[0]);
        copyVec3(oMat[1], trf43Xtrf43TmpMatrix[1]);
        copyVec3(oMat[2], trf43Xtrf43TmpMatrix[2]);
        copyVec3(oMat[3], trf43Xtrf43TmpMatrix[3]);
    }

    /**
     * @param mat43
     * @param angle
     * @param tVec
     * @return initialized matrix mat43
     */
    public static double[][] initMat43(double mat43[][], double angle, double tVec[]) {

        double rad = (Math.PI * angle) / 180d;
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        if (angle == 0) {
            mat43[0][0] = 1d;
            mat43[0][1] = 0d;
            mat43[1][0] = 0d;
            mat43[1][1] = 1d;
        } else {
            mat43[0][0] = cos;
            mat43[0][1] = -sin;
            mat43[1][0] = sin;
            mat43[1][1] = cos;
        }

        mat43[0][2] = 0d;
        mat43[1][2] = 0d;
        mat43[2][0] = 0d;
        mat43[2][1] = 0d;
        mat43[2][2] = 1d;

        if (tVec == null) {
            mat43[3][0] = 0;
            mat43[3][1] = 0;
            mat43[3][2] = 0;
        } else {
            mat43[3][0] = tVec[0];
            mat43[3][1] = tVec[1];
            mat43[3][2] = tVec[2];
        }
        return mat43;
    }

    /**
     * @param mat43
     * @param directionVector
     * @param tVec
     * @return
     */
    public static double[][] initMat43(double mat43[][], double[] directionVector, double tVec[]) {

        if (directionVector == null) {
            mat43[0][0] = 1d;
            mat43[0][1] = 0d;
            mat43[1][0] = 0d;
            mat43[1][1] = 1d;
        } else {
            mat43[0][0] = directionVector[0];
            mat43[0][1] = -directionVector[1];
            mat43[1][0] = directionVector[1];
            mat43[1][1] = directionVector[0];
        }

        mat43[0][2] = 0d;
        mat43[1][2] = 0d;
        mat43[2][0] = 0d;
        mat43[2][1] = 0d;
        mat43[2][2] = 1d;

        if (tVec == null) {
            mat43[3][0] = 0;
            mat43[3][1] = 0;
            mat43[3][2] = 0;
        } else {
            mat43[3][0] = tVec[0];
            mat43[3][1] = tVec[1];
            mat43[3][2] = tVec[2];
        }
        return mat43;
    }


//  public static double[][] initMat43( double[][] mat, double angle,  double[] tVec ){
//
//        double rad = (Math.PI*angle)/180d;
//        double c =  Math.cos(rad);
//        double s =  Math.sin(rad);
//
//        if ( angle == 0 ){
//            mat[0][0] =  1;  mat[0][1] =  0;   mat[0][2] =  0;
//            mat[1][0] =  0;  mat[1][1] =  1;   mat[1][2] =  0;
//            mat[2][0] =  0;  mat[2][1] =  0;   mat[2][2] =  1;
//        }else{
//            mat[0][0] =  c;  mat[0][1] =  s;   mat[0][2] =  0;
//            mat[1][0] = -s;  mat[1][1] =  c;   mat[1][2] =  0;
//            mat[2][0] =  0;  mat[2][1] =  0;   mat[2][2] =  1;
//        }
//
//        if (tVec == null ){
//            mat[3][0] = 0;   mat[3][1] =  0;   mat[3][2] = 0;
//        }else{
//            mat[3][0] = tVec[0];  mat[3][1] = tVec[1]; mat[3][2] = tVec[2];
//        }
//        return mat;
//    }


    /**
     * @param mat
     * @param angle
     * @param t
     */
    public static void initPitchXRotMat43(double mat[][], double angle, double t[]) {

        double rad = (Math.PI * angle) / 180d;
        double c = Math.cos(rad);
        double s = Math.sin(rad);

        if (angle == 0) {
            mat[0][0] = 1;
            mat[0][1] = 0;
            mat[0][2] = 0;
            mat[1][0] = 0;
            mat[1][1] = 1;
            mat[1][2] = 0;
            mat[2][0] = 0;
            mat[2][1] = 0;
            mat[2][2] = 1;
        } else {
            mat[0][0] = 1;
            mat[0][1] = 0;
            mat[0][2] = 0;
            mat[1][0] = 0;
            mat[1][1] = c;
            mat[1][2] = -s;
            mat[2][0] = 0;
            mat[2][1] = s;
            mat[2][2] = c;
        }

        if (t == null) {
            mat[3][0] = 0;
            mat[3][1] = 0;
            mat[3][2] = 0;
        } else {
            mat[3][0] = t[0];
            mat[3][1] = t[1];
            mat[3][2] = t[2];
        }
    }

    /**
     * @param mat
     * @param angle
     * @param t
     */
    public static void initYawYRotMat43(double mat[][], double angle, double t[]) {
        double rad = (Math.PI * angle) / 180d;
        double c = Math.cos(rad);
        double s = Math.sin(rad);

        if (angle == 0) {
            mat[0][0] = 1;
            mat[0][1] = 0;
            mat[0][2] = 0;
            mat[1][0] = 0;
            mat[1][1] = 1;
            mat[1][2] = 0;
            mat[2][0] = 0;
            mat[2][1] = 0;
            mat[2][2] = 1;
        } else {
            mat[0][0] = c;
            mat[0][1] = 0;
            mat[0][2] = s;
            mat[1][0] = 0;
            mat[1][1] = 1;
            mat[1][2] = 0;
            mat[2][0] = -s;
            mat[2][1] = 0;
            mat[2][2] = c;
        }

        if (t == null) {
            mat[3][0] = 0;
            mat[3][1] = 0;
            mat[3][2] = 0;
        } else {
            mat[3][0] = t[0];
            mat[3][1] = t[1];
            mat[3][2] = t[2];
        }
    }


    /**
     * @param mat
     * @param angle
     * @param t
     */
    public static void initZRotMat43(double mat[][], double angle, double t[]) {

        double rad = (Math.PI * angle) / 180d;
        double c = Math.cos(rad);
        double s = Math.sin(rad);

        if (angle == 0) {
            mat[0][0] = 1;
            mat[0][1] = 0;
            mat[0][2] = 0;
            mat[1][0] = 0;
            mat[1][1] = 1;
            mat[1][2] = 0;
            mat[2][0] = 0;
            mat[2][1] = 0;
            mat[2][2] = 1;
        } else {
            mat[0][0] = c;
            mat[0][1] = -s;
            mat[0][2] = 0;
            mat[1][0] = s;
            mat[1][1] = c;
            mat[1][2] = 0;
            mat[2][0] = 0;
            mat[2][1] = 0;
            mat[2][2] = 1;
        }

        if (t == null) {
            mat[3][0] = 0;
            mat[3][1] = 0;
            mat[3][2] = 0;
        } else {
            mat[3][0] = t[0];
            mat[3][1] = t[1];
            mat[3][2] = t[2];
        }
    }

    public static void initPerspectiveMat43(double[][] mat) {

        mat[0][0] = 0.8;
        mat[0][1] = 0;
        mat[0][2] = 0;

        mat[1][0] = 0;
        mat[1][1] = 0.7;
        mat[1][2] = 0;

        mat[2][0] = 0;
        mat[2][1] = 0;
        mat[2][2] = 1;

        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 5;

    }

    //
    //   V e c t o r   o p e r a t i o n s
    //

    public static int[] createIntVec3() {
        return initIntVec3(null, 0, 0, 0);
    }

    public static int[] initIntVec3(int x, int y, int z) {
        return initIntVec3(null, x, y, z);
    }

    public static int[] initIntVec3(int[] vec, int x, int y, int z) {
        if (vec == null) {
            vec = new int[3];
        }
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
        return vec;
    }

    public static int[] doubleVec3ToIntVec3(double[] fromVec) {
        return doubleVec3ToIntVec3(null, fromVec);
    }

    public static int[] doubleVec3ToIntVec3(int[] toVec, double[] fromVec) {
        if (toVec == null) {
            toVec = new int[3];
        }
        toVec[0] = (int) Math.rint(fromVec[0]);
        toVec[1] = (int) Math.rint(fromVec[1]);
        toVec[2] = (int) Math.rint(fromVec[2]);
        return toVec;
    }

    //

    public static double[] createVec3() {
        return initVec3(null, 0, 0, 0);
    }

    public static double[] initVec3(double x, double y, double z) {
        return initVec3(null, x, y, z);
    }

    public static double[] initVec3(double vec[], double x, double y, double z) {
        if (vec == null) {
            vec = new double[3];
        }
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
        return vec;
    }

    //

    /**
     * @param angle
     * @param z
     * @return point on
     */
    public static double[] createPointOnCircle(double r, double angle, double z) {
        double rad = (Math.PI * angle) / 180d;
        double[] vec = {r * Math.cos(rad), r * Math.sin(rad), z};
        return vec;
    }

    public static double[] createPointOnCircleAndTranslate(double r, double angle, double x, double y, double z) {
        double rad = (Math.PI * angle) / 180d;
        double[] vec = {r * Math.cos(rad), r * Math.sin(rad), 0};
        return translateVec(vec, x, y, z);
    }

    /**
     * @param fromVec
     * @return new 3-D vector
     */
    public static double[] copyVec3(double[] fromVec) {
        if (fromVec == null) {
            new Exception("From Vector Not Provided").printStackTrace();
            fromVec = new double[3];
        }
        return copyVec3(null, fromVec);
    }

    /**
     * @param toVec
     * @param fromVec
     * @return toVec if it is not null otherwise new 3-D vector
     */
    public static double[] copyVec3(double[] toVec, double[] fromVec) {
        if (toVec == null) {
//            new Exception("Result Vector Not Provided").printStackTrace();
            toVec = new double[3];
        }
        toVec[0] = fromVec[0];
        toVec[1] = fromVec[1];
        toVec[2] = fromVec[2];
        return toVec;
    }

    public static int[] copyIntVec3(int[] fromVec) {
        if (fromVec == null) {
            new Exception("From Vector Not Provided").printStackTrace();
            fromVec = new int[3];
        }
        return copyIntVec3(null, fromVec);
    }

    /**
     * @param toVec
     * @param fromVec
     * @return toVec if it is not null otherwise new 3-D vector
     */
    public static int[] copyIntVec3(int[] toVec, int[] fromVec) {
        if (toVec == null) {
//            new Exception("Result Vector Not Provided").printStackTrace();
            toVec = new int[3];
        }
        toVec[0] = fromVec[0];
        toVec[1] = fromVec[1];
        toVec[2] = fromVec[2];
        return toVec;
    }


    /**
     * @param vec3
     * @param vec1
     * @param vec2
     */
    public static double[] addVec3(double[] vec3, double[] vec1, double[] vec2) {
        if (vec3 == null) {
            new Exception("Result Vector Not Provided").printStackTrace();
            vec3 = new double[3];
        }
        vec3[0] = vec1[0] + vec2[0];
        vec3[1] = vec1[1] + vec2[1];
        vec3[2] = vec1[2] + vec2[2];
        return vec3;
    }

    /**
     * @param vec1
     * @param vec2
     * @return new 3-D vector
     */
    public static double[] addVec3(double[] vec1, double[] vec2) {
        double vec3[] = new double[3];
        vec3[0] = vec1[0] + vec2[0];
        vec3[1] = vec1[1] + vec2[1];
        vec3[2] = vec1[2] + vec2[2];
        return vec3;
    }

    /**
     * @param vec3
     * @param vec1
     * @param vec2
     * @return
     */
    public static int[] addVec3(int[] vec3, int[] vec1, int[] vec2) {
        if (vec3 == null) {
            new Exception("Result Vector Not Provided").printStackTrace();
            vec3 = new int[3];
        }
        vec3[0] = vec1[0] + vec2[0];
        vec3[1] = vec1[1] + vec2[1];
        vec3[2] = vec1[2] + vec2[2];
        return vec3;
    }

    /**
     * @param vec1
     * @param vec2
     * @return new 3-D vector
     */
    public static int[] addVec3(int[] vec1, int[] vec2) {
        int vec3[] = new int[3];
        vec3[0] = vec1[0] + vec2[0];
        vec3[1] = vec1[1] + vec2[1];
        vec3[2] = vec1[2] + vec2[2];
        return vec3;
    }

    /**
     * @param vec1
     * @param vec2
     * @param a
     * @param b
     * @param c
     */
    public static void addCoord3(double vec1[], double vec2[],
                                 double a, double b, double c) {
        vec1[0] = vec2[0] + a;
        vec1[1] = vec2[1] + b;
        vec1[2] = vec2[2] + c;
    }

    /**
     * @param vec1
     * @param vec2
     * @return new 3-D vector
     */
    public static double[] subVec3(double[] vec1, double[] vec2) {
        return subVec3(null, vec1, vec2);
    }

    /**
     * @param vec3
     * @param vec1
     * @param vec2
     * @return vec3 if it is not null otherwise new 3-D vector
     */
    public static double[] subVec3(double[] vec3, double[] vec1, double[] vec2) {
        if (vec3 == null) {
            vec3 = new double[3];
        }
        vec3[0] = vec1[0] - vec2[0];
        vec3[1] = vec1[1] - vec2[1];
        vec3[2] = vec1[2] - vec2[2];
        return vec3;
    }


    /**
     * @param vec1
     * @param vec2
     * @return dot
     */
    public static double dot3(double vec1[], double vec2[]) {
        return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
    }

    /**
     * @param vec
     * @return the length of the vector
     */
    public static double vec2Len(double[] vec) {
        double sum = vec[0] * vec[0] + vec[1] * vec[1];
        double length = Math.sqrt(sum);
        return length;
    }

    public static double vec3Len(double[] vec) {
        double sum = vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2];
        double length = Math.sqrt(sum);
        return length;
    }

    /**
     * @param vec
     * @return true when vector length is olmost zero
     */
    public static boolean isZeroVec3(double vec[]) {
        return (Math.abs(vec[0]) < ALMOST_ZERO && Math.abs(vec[1]) < ALMOST_ZERO && Math.abs(vec[2]) < ALMOST_ZERO);
    }

    public static double[] normalizeVec3(double[] vecToNormalize) {
        double[] normalizedVec = new double[3];
        return normalizeVec3(vecToNormalize, normalizedVec);
    }

    /**
     * @param vecToNormalize
     * @param normalizedVec
     */
    public static double[] normalizeVec3(double[] vecToNormalize, double[] normalizedVec) {
        double len = vec3Len(vecToNormalize);
        if (len < ALMOST_ZERO) {
            normalizedVec[0] = normalizedVec[1] = normalizedVec[2] = 0.0;
            return normalizedVec;
        }
        normalizedVec[0] = vecToNormalize[0] / len;
        normalizedVec[1] = vecToNormalize[1] / len;
        normalizedVec[2] = vecToNormalize[2] / len;
        return normalizedVec;
    }


    public static double[] scaleVec3(double vec1[], double scale) {
        return scaleVec3(null, scale, vec1);
    }

    public static double[] scaleVec3(double scale, double[] vec1) {
        return scaleVec3(null, scale, vec1);
    }

    /**
     * @param vec2
     * @param scale
     * @param vec1
     */
    public static double[] scaleVec3(double[] vec2, double scale, double[] vec1) {
        if (vec2 == null) {
            vec2 = new double[3];
        }
        vec2[0] = scale * vec1[0];
        vec2[1] = scale * vec1[1];
        vec2[2] = scale * vec1[2];
        return vec2;
    }


    public static double[] transformVec3(double[] vec, double angle, double[] tl) {
        return trformVec3(vec, angle, tl);
    }

    public static int[] translateIntVec(int[] vec1, int[] vec2) {
        vec1[0] = vec1[0] + vec2[0];
        vec1[1] = vec1[1] + vec2[1];
        vec1[2] = vec1[2] + vec2[2];
        return vec1;
    }

    public static double[] translateVec(double[] vec, double x, double y, double z) {
        vec[0] = vec[0] + x;
        vec[1] = vec[1] + y;
        vec[2] = vec[2] + z;
        return vec;
    }

    public static double[] translateVec(double[] vec1, double[] vec2) {
        vec1[0] = vec1[0] + vec2[0];
        vec1[1] = vec1[1] + vec2[1];
        vec1[2] = vec1[2] + vec2[2];
        return vec1;
    }

    public static double[] translateToNewVec(double[] vec1, double[] vec2) {
        double[] translatedVec = new double[3];
        translatedVec[0] = vec1[0] + vec2[0];
        translatedVec[1] = vec1[1] + vec2[1];
        translatedVec[2] = vec1[2] + vec2[2];
        return translatedVec;
    }

    /**
     * @param vec
     * @param angle
     * @param tl
     */
    public static double[] trformVec3(double[] vec, double angle, double[] tl) {
        double mat[][] = new double[4][3];
        initZRotMat43(mat, angle, tl);
        mat43XPnt3(vec, mat, vec);
        return vec;
    }

    public static double[] linCom3(double scale01, double[] vec1,
                                   double scale02, double[] vec2) {
        return LinCom3(null, scale01, vec1, scale02, vec2);
    }

    /**
     * @param vec3
     * @param scale01
     * @param vec1
     * @param scale02
     * @param vec2
     */
    public static double[] LinCom3(double[] vec3, double scale01,
                                   double[] vec1, double scale02,
                                   double[] vec2) {
        if (vec3 == null) {
            vec3 = new double[3];
        }

        vec3[0] = vec1[0] * scale01 + vec2[0] * scale02;
        vec3[1] = vec1[1] * scale01 + vec2[1] * scale02;
        vec3[2] = vec1[2] * scale01 + vec2[2] * scale02;
        return vec3;
    }

    public static int[] linCom3(double scale01, int[] vec1,
                                double scale02, int[] vec2) {
        return linCom3(null, scale01, vec1, scale02, vec2);
    }

    public static int[] linCom3(int[] vec3,
                                double scale01, int[] vec1,
                                double scale02, int[] vec2) {
        if (vec3 == null) {
            vec3 = new int[3];
        }

        vec3[0] = (int) Math.rint(vec1[0] * scale01 + vec2[0] * scale02);
        vec3[1] = (int) Math.rint(vec1[1] * scale01 + vec2[1] * scale02);
        vec3[2] = (int) Math.rint(vec1[2] * scale01 + vec2[2] * scale02);
        return vec3;
    }

    public static double[] cross3(double[] vec1, double[] vec2) {
        double[] vec3 = new double[3];
        return cross3(vec3, vec1, vec2);
    }

    /**
     * @param vec3
     * @param vec1
     * @param vec2
     */
    public static double[] cross3(double[] vec3, double[] vec1, double[] vec2) {
        if (vec3 == null) {
            vec3 = new double[3];
        }
        vec3[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
        vec3[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
        vec3[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
        return vec3;
    }

    /**
     * @param vec1
     * @param vec2
     * @return
     */
    public static double distVec3(double[] vec1, double[] vec2) {
        double[] tmpVec = subVec3(vec1, vec2);
        double length = vec3Len(tmpVec);

//        double diff0 = vec1[0] - vec2[0];
//        double diff1 = vec1[1] - vec2[1];
//        double diff2 = vec1[2] - vec2[2];
//        double aux = diff0 * diff0 + diff1 * diff1 + diff2 * diff2;
//        aux = Math.sqrt(aux);

        return length;
    }

    public static int[] doubleVec3ToInt(double fromVec[]) {
        return doubleVec3ToInt(null, fromVec);
    }

    public static int[] doubleVec3ToInt(int toVec[], double fromVec[]) {
        if (toVec == null) {
            toVec = new int[3];
        }
        toVec[0] = (int) Math.rint(fromVec[0]);
        toVec[1] = (int) Math.rint(fromVec[1]);
        toVec[2] = (int) Math.rint(fromVec[2]);
        return toVec;
    }

    /**
     * @param fromVec
     * @return
     */
    public static double[] intVec3ToDouble(int fromVec[]) {
        return intVec3ToDouble(null, fromVec);
    }

    /**
     * @param toVec
     * @param fromVec
     * @return
     */
    public static double[] intVec3ToDouble(double toVec[], int fromVec[]) {
        if (toVec == null) {
            toVec = new double[3];
        }
        toVec[0] = fromVec[0];
        toVec[1] = fromVec[1];
        toVec[2] = fromVec[2];
        return toVec;
    }

    // ===============  O u t l i n e  ===============================
    private static final double[][] transformOutlinePoints = new double[4][3];

    public static void transformOutline(double[][] outOutline,
                                        double[][] trf,
                                        double[][] inOutline) {
        /*outline   points
          *  1      1  2

          0  *      0  3
        */

        copyVec3(transformOutlinePoints[0], inOutline[0]);
        initVec3(transformOutlinePoints[1], inOutline[0][0], inOutline[1][1], 0);
        copyVec3(transformOutlinePoints[2], inOutline[1]);
        initVec3(transformOutlinePoints[3], inOutline[1][0], inOutline[0][1], 0);

        for (int i = 0; i < 4; i++)
            mat43XPnt3(transformOutlinePoints[i], trf, transformOutlinePoints[i]);

        double minX = Math.min(Math.min(transformOutlinePoints[0][0], transformOutlinePoints[1][0]),
                Math.min(transformOutlinePoints[2][0], transformOutlinePoints[3][0]));
        double maxX = Math.max(Math.max(transformOutlinePoints[0][0], transformOutlinePoints[1][0]),
                Math.max(transformOutlinePoints[2][0], transformOutlinePoints[3][0]));
        double minY = Math.min(Math.min(transformOutlinePoints[0][1], transformOutlinePoints[1][1]),
                Math.min(transformOutlinePoints[2][1], transformOutlinePoints[3][1]));
        double maxY = Math.max(Math.max(transformOutlinePoints[0][1], transformOutlinePoints[1][1]),
                Math.max(transformOutlinePoints[2][1], transformOutlinePoints[3][1]));

        initVec3(outOutline[0], minX, minY, 0);
        initVec3(outOutline[1], maxX, maxY, 0);
    }

    /**
     * @param outline1
     * @param outline2
     * @return true when contains
     */
    public static boolean outlineContainsOutline(double[][] outline1,
                                                 double[][] outline2) {
        // checks if oultline 1 contains outline 2 compleatly
        if (outline2[0][0] > outline1[0][0] &&
                outline2[0][1] > outline1[0][1] &&
                outline2[1][0] < outline1[1][0] &&
                outline2[1][1] < outline1[1][1])
            return true;

        return false;
    }

    /**
     * @param outline
     * @param pnt
     * @return true when contains
     */
    public static boolean outlineContainsPoint(double[][] outline,
                                               double[] pnt) {
        if (pnt[0] > outline[0][0] && pnt[1] > outline[0][1] &&
                pnt[0] < outline[1][0] && pnt[1] < outline[1][1])
            return true;

        return false;
    }

    /**
     * @param outline
     */
    public static void printOutline(double outline[][]) {
        System.out.println("Outline Rectangle:");
        System.out.println(outline[1][0] + "  " + outline[1][1] + "  " + outline[1][2]);
        System.out.println(outline[0][0] + "  " + outline[0][1] + "  " + outline[0][2]);
        System.out.println();
    }

}

