package mathematics.spline;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 6/16/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathSpline3D {
    static final int E_NO_ERROR = 0;
    static final int E_CONVERGENCE = 1;
    static final int E_ZERO_LENGTH = 2;
    static final int E_CANT_SOLVE = 3;

    static final double PRECISION_EPSILON = 0.0000000000001;   //1.0e-13
    static final double PID2 = 1.57079632679489661923;
    static final double TWOPI = Math.PI + Math.PI;
    static final double JACOBI_EPS = 1.0e-30;
    static final double JACOBI_ZERO = 1.0e-14;

    static final double ALMOST_ZERO = 0.000000001;
    static final double HUGE_DOUBLE = (1.0 / ALMOST_ZERO);

    // tan cond
    static final int SPL_TAN_NONE = (0x0 << 0);
    static final int SPL_TAN_START = (0x1 << 0);
    static final int SPL_TAN_END = (0x1 << 1);
    public static final int SPL_TAN_BOTH = (SPL_TAN_START | SPL_TAN_END);
    static final int SPL_TAN_PERIODIC = (0x1 << 2);
    static final int SPL_FLAT_START = (0x1 << 3);
    static final int SPL_FLAT_END = (0x1 << 4);
    static final int SPL_FLAT_BOTH = (SPL_FLAT_START | SPL_FLAT_END);
    static final int SPL_TAN_INVALID = (0x1 << 5);
    static final int SPL_TAN_QUAD_START = (0x1 << 6);
    static final int SPL_TAN_QUAD_END = (0x1 << 7);
    static final int SPL_TAN_ARC_START = (0x1 << 8);
    static final int SPL_TAN_ARC_END = (0x1 << 9);
    static final int SPL_TAN_ARC_BOTH = (SPL_TAN_ARC_START | SPL_TAN_ARC_END);
    static final int SPL_TAN_QUAD_BOTH = (SPL_TAN_QUAD_START | SPL_TAN_QUAD_END);
    static final int SPL_TAN_CORNER = 5;

    // curv cond
    static final int SPL_CURV_NONE = 0x0;
    static final int SPL_CURV_START = 0x1;
    static final int SPL_CURV_END = 0x2;
    static final int SPL_CURV_BOTH = (SPL_CURV_START | SPL_CURV_END);

    // Whitch derivatives do not calculate in interpolate_splsrf()
    static final int SPL_U_DERIV = 0x1;
    static final int SPL_V_DERIV = 0x2;
    static final int SPL_UV_DERIV = 0x4;

    // Conditions for derivatives of Coons surface along boundaries
    static final int COONS_FREE = 0;
    static final int COONS_TAN_PLN = 1;
    static final int COONS_NRM_PLN = 2;


    // parametrization
    static final int CHORD_PARAM = 2;
    static final int KEEP_PARAM = 3;
    static final int FOLEY_PARAM = 4;

    /**
     * @param list
     * @return
     */
    private static double[][] knotsListToArray(List<double[]> list) {
        int size = list.size();
        double[][] knots = new double[size][3];
        for (int i = 0; i < size; i++) {
            knots[i] = list.get(i);
        }
        return knots;
    }

    //
    //   I n s t a n c e
    //

    private int tan_cond;                // Type of boundary conditions used.

    // points

    // array of spline knots
    private static int INIT_MAX_KNOTS = 5;
    private static int INCREASE_KNOTS = 5;

    //    private final List<double[]> splKnotsList = new ArrayList();
    private double[][] splKnots;   // knots in CSys coordinates
    private double[] params;       // parameter values
    private double[][] tangts;     // derivatives in interpolation points

    // draw
    private double[] drawParams;    // parameter values
//    private double[][] drawTangts;    // derivatives in interpolation points

    // Tesselation of spline segments for display/selection purposes
    private final int SPLNSEG_GRTESS = 15;

    private int pntRectW2 = 3, pntRectW = 2 * pntRectW2;
    private int pntRectH2 = 3, pntRectH = 2 * pntRectH2;

    private final double eps = 0.00001;
    private final double eps_sqr = eps * eps;
    private boolean interpDone = false;
    private boolean eval_done = false;


// ===============  C o n s t r u c t o r s  ===================

    public MathSpline3D() {
        tan_cond = SPL_TAN_NONE;
        splKnots = new double[0][3];
    }

    /**
     *
     */
    public MathSpline3D(int tanCond, int initialArrSize, int splArrIncrement) {
        tan_cond = tanCond;
        INIT_MAX_KNOTS = (initialArrSize != -1) ? initialArrSize : INIT_MAX_KNOTS;
        INCREASE_KNOTS = (splArrIncrement != -1) ? splArrIncrement : INCREASE_KNOTS;
        splKnots = new double[0][3];
    }

    public int getNSegments() {
        return SPLNSEG_GRTESS;
    }

    public void clearKnots() {
        splKnots = new double[0][3];
    }

    public int getNKnots() {
        return splKnots.length;
    }

    public double[][] getKnots() {
        return splKnots;
    }

    public boolean canAddKnot() {
        int nKnots = splKnots.length;
        if (nKnots < 2) {
            return true;
        }

        if (dist_sqr(splKnots[nKnots - 2], splKnots[nKnots - 1]) < eps_sqr) {
            System.out.println("addKnot NEAR current index " + (nKnots - 1) + " current size = " + nKnots);
            return false;
        } else {
            return true;
        }
    }

    public void updateKnot(int ind, int x, int y) {
        double[] knot = {x, y, 0};
        updateKnot(ind, knot);
    }

    public void updateKnot(int ind, double[] knot) {
        if (ind < 0 || ind >= splKnots.length) {
            ind = splKnots.length - 1;
        }
//        System.out.println("Update ####### " + ind + "   " + splKnots.length);
        copyVector(knot, splKnots[ind]);
        interpolate();
    }

    public void setKnots(List<double[]> splineCSysKnots) {
        splKnots = knotsListToArray(splineCSysKnots);
        interpolate();
    }


// input is in screen coordinates

    public void translateSpline(double[] translationVector) {
        int nKnots = splKnots.length;
        for (int i = 0; i < nKnots; i++) {
            double[] curKnot = splKnots[i];
            addVec3(curKnot, curKnot, translationVector);
        }
        interpolate();
    }


    public double[] getKnotClone(int ind) {
        if (ind < 0 || ind >= splKnots.length) {
            return null;
        }
        double[] splKnot = splKnots[ind];
        double[] knot = {splKnot[0], splKnot[1], splKnot[2]};
        return knot;
    }

    /**
     *
     */
    public List<double[]> sketchSpline(List<double[]> splCSysPoints) {
        drawParams = params;
        return sketchSpline(splKnots, tangts, splCSysPoints, -1, -1);
    }


    // ===================================
    //  I N T E R P O L A T E
    // ===================================
// INPUT:
//  if tan_cond == SPL_TAN_NONE or
//     tan_cond == SPL_TAN_PERIODIC
//  then
//    only array of interpolant points is set.
//    otherwise
//    appropriate elements of p_spline->tangts
//    is to be set (only directions of tangents
//    are important/)
//
// OUTPUT: arrays of parameters and
//         derivatives are set  */
//
    private final void interpolate() {
        interpolate(splKnots);
    }

    private boolean interpolate(double[][] splKnots) {

        interpDone = false;
        int nPnts = splKnots.length;

        if (nPnts < 2) {
            return (false);
        }
//        if (nPnts > 0) {
        if (dist_sqr(splKnots[nPnts - 2], splKnots[nPnts - 1]) < eps_sqr) {
            return false;
        }
//        }

        // set tangent condition
        tangts = new double[nPnts][3];
        params = new double[nPnts];

        // Must set the directions of the tangents for interpolate
        if ((tan_cond & SPL_TAN_START) != 0) {
            vsub3(splKnots[1], splKnots[0], tangts[0]);
        }
        if ((tan_cond & SPL_TAN_END) != 0) {
            vsub3(splKnots[nPnts - 1], splKnots[nPnts - 2], tangts[nPnts - 1]);
        }
        Double doub0 = new Double(tangts[nPnts - 1][0]);
        Double doub1 = new Double(tangts[nPnts - 1][1]);
        Double doub2 = new Double(tangts[nPnts - 1][2]);
        if (doub0.isNaN() || doub1.isNaN() || doub2.isNaN()) {
            System.out.println();
        }
        prepareSplineToCompute(splKnots, SPL_CURV_NONE, CHORD_PARAM, null, null);

        /* Compute spline derivatives at knotes */
        computeSpline(nPnts, 1, 3, splKnots, 1, params, tan_cond, tangts);

        interpDone = true;
        eval_done = false;
        return true;
    }

// ===================================
//  prepare_spline_to_compute
// ===================================
//  curv_cond,
//  parametrization    could be KEEP_PARAM, CHORD_PARAM or FOLEY_PARAM

    private void prepareSplineToCompute(double[][] points, int curv_cond, int parametrization,
                                        double der2_st[], double der2_end[]) {

        int nPnts = points.length;

        int ii, jj, cond;

        double d1, d2, bb;
        double[][] matrix = new double[3][3];
        double[][] p = new double[3][3];
        double[][] p_t = new double[3][3];

        double[] aver = new double[3];
        double[] lyambd = new double[3];
        double[] delt1 = new double[3];

        double[] Nelson_1;
        double[] Nelson_2;
        double[] Theta;

        double Nel_sqrt_1, Nel_sqrt, Nel_sqrt_11, hh1 = 0, hh2 = 0,
                hh, delt,
                dot1, lngth;

        if (nPnts < 2)
            return;

        if (parametrization == KEEP_PARAM) /* Normalize end velocities */
            /* {
         if (curv_cond & SPL_CURV_START)
           {
           d1 = sqrt (dot(p_spln->tangts[0], p_spln->tangts[0]));
           if (d1 * HUGE_DOUBLE < ALMOST_ZERO)
            copy_vector(NULL, der2_st);
           else
            scvec(1/d1, der2_st, der2_st);
           }
         if (curv_cond & SPL_CURV_END)
           {
            d2= sqrt (dot(p_spln->tangts[npnt-1], p_spln->tangts[npnt-1]));
            if (d2 * HUGE_DOUBLE < ALMOST_ZERO)
             copy_vector(NULL, der2_end);
            else
             scvec(1/d2, der2_end, der2_end);
            }*/

            return;
// }

        // Provide correct space to store derivatives and parameters
        cond = tan_cond;

/*
 ii = tangts.size() - nPnts;
 if( ii < 1-nPnts && (cond & SPL_TAN_START))
    return;
 if( ii < 0       && (cond & SPL_TAN_END))
    return;

 if ( ii > 0 )
   tangts = new double[nPnts];
 else if(ii < 0) xar_extend(&p_spln->tangts, -ii);
*/

        tangts = new double[nPnts][3];

/*
 ii = params.size() - nPnts;
 if( ii > 0)     xar_shrink(&p_spln->params,  ii);
 else if(ii < 0) xar_extend(&p_spln->params, -ii);
*/
        params = new double[nPnts];

        // Set parameter values at knotes

        params[0] = 0.0;

        if ((parametrization & CHORD_PARAM) == CHORD_PARAM ||
                (parametrization == FOLEY_PARAM && nPnts < 3)) {
            for (ii = 1; ii < nPnts; ii++)
                params[ii] = params[ii - 1] + dist(points[ii], points[ii - 1]);

            // Normalize end velocities

            if ((cond & SPL_TAN_START) != 0) {
                if ((curv_cond & SPL_CURV_START) != 0) {
                    d1 = Math.sqrt(dot(tangts[0], tangts[0]));
                    if (d1 * HUGE_DOUBLE < ALMOST_ZERO)
                        copyVector(null, der2_st);
                    else
                        scvec(1 / d1 / d1, der2_st, der2_st);
                }
                normalize_vector(tangts[0], tangts[0]);
            }

            if ((cond & SPL_TAN_END) != 0) {
                if ((curv_cond & SPL_CURV_END) != 0) {
                    d2 = Math.sqrt(dot(tangts[nPnts - 1], tangts[nPnts - 1]));
                    if (d2 * HUGE_DOUBLE < ALMOST_ZERO)
                        copyVector(null, der2_end);
                    else
                        scvec(1 / d2 / d2, der2_end, der2_end);
                }
                normalize_vector(tangts[nPnts - 1], tangts[nPnts - 1]);
            }

        } else if (parametrization == FOLEY_PARAM) {
            ibMatrix(points, matrix, aver);

            jacobi3(matrix, lyambd, p);

            transpose_matrix(3, 3, p, p_t);
            for (ii = 0; ii < 3; ii++)
                if (lyambd[ii] < ALMOST_ZERO)
                    lyambd[ii] = 1.0;

            for (ii = 0; ii < 3; ii++)
                for (jj = 0; jj < 3; jj++)
                    p[ii][jj] = p[ii][jj] / lyambd[ii];


            matrix_multiply(3, 3, 3, p_t, p, matrix);

            Nelson_1 = new double[nPnts - 1];
            Nelson_2 = new double[nPnts - 2];
            Theta = new double[nPnts - 1];

            makeNelsons(matrix, points, 1, nPnts - 1, Nelson_1);

            makeNelsons(matrix, points, 2, nPnts - 2, Nelson_2);

            for (ii = 1; ii < nPnts - 1; ii++) {
                bb = 0.5 * (Nelson_1[ii - 1] + Nelson_1[ii] - Nelson_2[ii - 1]) /
                        Math.sqrt(Nelson_1[ii - 1]) / Math.sqrt(Nelson_1[ii]);

                if (bb < -1.0)
                    bb = -1.0;
                if (bb > 1.0)
                    bb = 1.0;

                Theta[ii] = Math.PI - spg_acos(bb);

                if (Theta[ii] > PID2 - PRECISION_EPSILON)
                    Theta[ii] = PID2;
            }

            for (ii = 0; ii < nPnts - 1; ii++) {
                Nel_sqrt_1 = Math.sqrt(Nelson_1[ii - 1]);
                Nel_sqrt = Math.sqrt(Nelson_1[ii]);
                Nel_sqrt_11 = Math.sqrt(Nelson_1[ii + 1]);

                if (ii != 0)
                    hh2 = 1.5 * Theta[ii] * Nel_sqrt_1 / (Nel_sqrt + Nel_sqrt_1);
                if (ii != nPnts - 2)
                    hh1 = 1.5 * Theta[ii + 1] * Nel_sqrt_11 / (Nel_sqrt + Nel_sqrt_11);
                if (ii == 0)
                    hh = Nel_sqrt * (1.0 + hh1);
                else if (ii == nPnts - 2)
                    hh = Nel_sqrt * (1.0 + hh2);
                else
                    hh = Nel_sqrt * (1.0 + hh1 + hh2);

                params[ii + 1] = params[ii] + hh;
            }

            // Normalize end velocities


            if ((cond & SPL_TAN_START) != 0) {
                vsub3(points[1], points[0], delt1);
                dot1 = dot(delt1, delt1);
                lngth = vectorLength(tangts[0]);

                normalize_vector(tangts[0], tangts[0]);
                delt = Math.sqrt(dot1) / params[1];  /* ! */
                scvec(delt, tangts[0], tangts[0]);
                if ((curv_cond & SPL_CURV_START) != 0)
                    scvec(delt * delt / lngth / lngth, der2_st, der2_st);
            }

            if ((cond & SPL_TAN_END) != 0) {
                vsub3(points[nPnts - 2], points[nPnts - 1], delt1);
                dot1 = dot(delt1, delt1);
                lngth = vectorLength(tangts[nPnts - 1]);

                normalize_vector(tangts[nPnts - 1], tangts[nPnts - 1]);
                delt = Math.sqrt(dot1) / (params[nPnts - 1] - params[nPnts - 2]);  /* ! */
                scvec(delt, tangts[nPnts - 1], tangts[nPnts - 1]);
                if ((curv_cond & SPL_CURV_END) != 0)
                    scvec(delt * delt / lngth / lngth, der2_end, der2_end);
            }
        }
    }

// ------------------------------------------------------------------
//   Computes a set of NVAL-dimensional splines
//
//   int     ncol;              Number of columns - number of points
//   int     nrow;              Number of rows    ==1
//   int     nval;              Number of values in each node  == 3
//                              (spline dimension)
//   double  fcn[];             Function values (input)
//   int     col_row;           1 - spline in column direction == TRUE
//                              0 - spline in row direction
//   double  params[];          Parameter values
//   int     tan_cond;          Boundary conditions flag
//   double  der[];             Derivative values (output)
//
//  Note: "fcn" and "der" arrays are really three dimensional arrays
//        of the type array[ncol][nrow][nval]. They are only declared
//        as one dimensional because of "C" limitations.
//

    private void computeSpline(int ncol, int nrow, int nval, double[][] splKnots, int col_row, double params[],
                               int tan_cond, double der[][]) {

        double aux0, under[], above[], diago[], matrix[];
        int npar;

        npar = (col_row != 0) ? ncol : nrow;

        if (npar < 2) {
            return;
        } else if (npar == 2 && tan_cond == SPL_TAN_NONE)      /* Straight line */ {
            aux0 = 1.0 / (params[1] - params[0]);
            linComSplKnots(ncol, nrow, nval, col_row,
                    aux0, splKnots, 1, -aux0, splKnots, 0, der, 1);
            copySplKnot(ncol, nrow, nval, col_row, der, 1, der, 0);
            return;
        }

        // Provide space for 3-diagonal matrix

// matrix = new double[ 3 * npar ];
        under = new double[npar];
        above = new double[npar];
        diago = new double[npar];

        // Set all but the first and the last equations ,
        // use the space for derivatives to hold all right-hand sides

        makeMatrixAndRightVector(ncol, nrow, nval, col_row, params, splKnots, der,
                above, diago, under);

        // Set boundary conditions ( first and last equations )
        boundary_conditions_for_spline(ncol, nrow, nval, col_row, tan_cond,
                params, splKnots, der, diago, above, under);

        // Solve systems of modified 3-diagonal equations simultaneously
        solve_3_diago_lin(ncol, nrow, nval, under, diago, above, col_row,
                (tan_cond != SPL_TAN_PERIODIC), der);

    }

/*
    ===============================
     A U X I L I A R Y   F U N C S
    ===============================
*/
/*
   double     matrix[3][3];
   Pnt_3d     *p_1, *p_2;
   int        nn;

   double     *Nelson;
*/

    private void makeNelsons(double matrix[][], double[][] splKnots, int ind,
                             int nn, double nelson[]) {
        int ii, jj, kk;
        double delta[] = new double[3];
        double sum;

        for (ii = 0; ii < nn; ii++) {
            nelson[ii] = 0.0;

            vsub3(splKnots[ind + ii], splKnots[ii], delta);

            for (jj = 0; jj < 3; jj++) {
                sum = 0.0;
                for (kk = 0; kk < 3; kk++) {
                    sum += (delta[kk] * matrix[kk][jj]);
                }
                nelson[ii] += (sum * delta[jj]);
            }
        }
    }

// -----------------------------------------------------------

    private double[] vsub3(double a[], double b[], double c[]) {
        c[0] = a[0] - b[0];
        c[1] = a[1] - b[1];
        c[2] = a[2] - b[2];
        return c;
    }
// -----------------------------------------------------------

    public double[] addVec3(double c[], double a[], double b[]) {
        c[0] = a[0] + b[0];
        c[1] = a[1] + b[1];
        c[2] = a[2] + b[2];
        return c;
    }
// ----------------------------------------------------------

    private double vectorLength(double vector[]) {
        double max, v0, v1, v2;

        v0 = Math.abs(vector[0]);
        v1 = Math.abs(vector[1]);
        v2 = Math.abs(vector[2]);
        max = (v0 > v1) ? ((v0 > v2) ? v0 : v2) : ((v1 > v2) ? v1 : v2);

        if (max == 0.0)
            return (0.0);

        v0 /= max;
        v1 /= max;
        v2 /= max;

        return (max * Math.sqrt(v0 * v0 + v1 * v1 + v2 * v2));
    }

// ----------------------------------------------------------

    private double
    dist(double pnt1[], double pnt2[]) {
        double aux, diff0, diff1, diff2;

        diff0 = pnt1[0] - pnt2[0];
        diff1 = pnt1[1] - pnt2[1];
        diff2 = pnt1[2] - pnt2[2];

        aux = diff0 * diff0 + diff1 * diff1 + diff2 * diff2;
        aux = Math.sqrt(aux);

        return (aux);
    }
// ---------------------------------------------------------
// Return the square of the distance from vec1 to vec2.

    double dist_sqr(double vec1[], double vec2[]) {
        double diff0, diff1, diff2;

        diff0 = vec2[0] - vec1[0];
        diff1 = vec2[1] - vec1[1];
        diff2 = vec2[2] - vec1[2];

        return (diff0 * diff0 + diff1 * diff1 + diff2 * diff2);
    }

// ----------------------------------------------------------

    private int
    normalize_vector(double vector2[], double vector1[]) {
        double len;

        len = vectorLength(vector1);

        if (len < ALMOST_ZERO) {
            vector2[0] = vector2[1] = vector2[2] = 0.0;
            return (E_ZERO_LENGTH);
        }

        vector2[0] = vector1[0] / len;
        vector2[1] = vector1[1] / len;
        vector2[2] = vector1[2] / len;

        return (E_NO_ERROR);
    }

    /**
     * @param fromVector
     * @param toVector
     */
    private void copyVector(double[] fromVector, double[] toVector) {

        if (fromVector == null)
            toVector[0] = toVector[1] = toVector[2] = 0.0;
        else {
            toVector[0] = fromVector[0];
            toVector[1] = fromVector[1];
            toVector[2] = fromVector[2];
        }
    }

    /**
     * @param scalar
     * @param vector
     * @param result
     * @return
     */
    private double[] scvec(double scalar, double vector[], double result[]) {
        result[0] = scalar * vector[0];
        result[1] = scalar * vector[1];
        result[2] = scalar * vector[2];

        return (result);
    }

// -----------------------------------------------------------

    private double dot(double a[], double b[]) {
        return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
    }
// -----------------------------------------------------------

    private void cross(double a[], double b[], double c[]) {
        double d0, d1;

        d0 = a[1] * b[2] - a[2] * b[1];
        d1 = a[2] * b[0] - a[0] * b[2];

        c[2] = a[0] * b[1] - a[1] * b[0];
        c[0] = d0;
        c[1] = d1;
    }
// -----------------------------------------------------------

    private double[] lincom(double scalar_a, double vector_a[], double scalar_b,
                            double vector_b[], double result[])

    {
        result[0] = scalar_a * vector_a[0] + scalar_b * vector_b[0];
        result[1] = scalar_a * vector_a[1] + scalar_b * vector_b[1];
        result[2] = scalar_a * vector_a[2] + scalar_b * vector_b[2];

        return (result);
    }

// -----------------------------------------------------------

    private void vadd(double v1[], double v2[], double v3[]) {
        v3[0] = v2[0] + v1[0];
        v3[1] = v2[1] + v1[1];
        v3[2] = v2[2] + v1[2];
    }

// -----------------------------------------------------------

    private void linComSplKnots(int ncol, int nrow, int nval, int col_row,
                                double a, double[][] pnt1, int num1,
                                double b, double[][] pnt2, int num2,
                                double pnt[][], int num) {
        int idx0, idx1, idx2;
/*
  int     row, idx, n_row, idx_start, idx_start1, idx_start2, idx_inc,
          ind0, ind1, ind2, idx0, idx1, idx2;

  n_row = (col_row != 0) ? nrow : ncol;
  idx_start = (col_row != 0) ? num * nval : num * ncol * nval;
  idx_start1 = (col_row != 0) ? num1 * nval : num1 * ncol * nval;
  idx_start2 = (col_row != 0) ? num2 * nval : num2 * ncol * nval;
  idx_inc = (col_row != 0) ? ncol * nval : nval;

  for (row = 0, ind0 = idx_start, ind1 = idx_start1, ind2 = idx_start2;
     row < n_row; row ++, ind0 += idx_inc, ind1 += idx_inc, ind2 += idx_inc)
  for (idx = 0, idx0 = ind0, idx1 = ind1, idx2 = ind2; idx < nval;
       idx ++, idx0 ++, idx1 ++, idx2 ++)
    pnt[idx0] = a * pnt1[idx1] + b * pnt2[idx2];
*/

//  System.out.println("Lin knots "+num+" "+num1+"  "+num2+"  "+nval);
        idx0 = (col_row != 0) ? num * nval : num * ncol * nval;
        idx1 = (col_row != 0) ? num1 * nval : num1 * ncol * nval;
        idx2 = (col_row != 0) ? num2 * nval : num2 * ncol * nval;
//System.out.println("Lin ind "+idx0+" "+idx1+"  "+idx2);
/*
  pnt[idx0][0] = a * pnt1[idx1][0] + b * pnt2[idx2][0];
  pnt[idx0][1] = a * pnt1[idx1][1] + b * pnt2[idx2][1];
  pnt[idx0][2] = a * pnt1[idx1][2] + b * pnt2[idx2][2];
*/
        pnt[num][0] = a * pnt1[num1][0] + b * pnt2[num2][0];
        pnt[num][1] = a * pnt1[num1][1] + b * pnt2[num2][1];
        pnt[num][2] = a * pnt1[num1][2] + b * pnt2[num2][2];

    }

    // -----------------------------------------------------------
                       /* pnt1 - from, pnt - to */
    private void copySplKnot(int ncol, int nrow, int nval, int col_row,
                             double pnt1[][], int num1, double pnt[][], int num) {
        int idx0, idx1;

/*
  int row, idx, n_row, idx_start, idx_start1, idx_inc, ind0, ind1, idx0,
      idx1;

  n_row = (col_row) ? nrow : ncol;
  idx_start = (col_row) ? num * nval : num * ncol * nval;
  idx_start1 = (col_row) ? num1 * nval : num1 * ncol * nval;
  idx_inc = (col_row) ? ncol * nval : nval;

  for (row = 0, ind0 = idx_start, ind1 = idx_start1; row < n_row;
       row ++, ind0 += idx_inc, ind1 += idx_inc)
  for (idx = 0, idx0 = ind0, idx1 = ind1; idx < nval;
       idx ++, idx0 ++, idx1 ++)
       pnt[idx0] = pnt1[idx1];
*/

        idx0 = (col_row != 0) ? num * nval : num * ncol * nval;
        idx1 = (col_row != 0) ? num1 * nval : num1 * ncol * nval;

        pnt[num][0] = pnt1[num1][0];
        pnt[num][1] = pnt1[num1][1];
        pnt[num][2] = pnt1[num1][2];
    }

    /**
     * compute matrix for optimal plane determination
     *
     * @param splKnots
     * @param matrix
     * @param aver
     */
    private void ibMatrix(double[][] splKnots, double matrix[][], double aver[]) {
        int num = splKnots.length;
        int i, j, k;
        double sum[] = {0, 0, 0};

        for (j = 0; j < 3; j++) {
            sum[j] = 0.;
            for (k = 0; k < 3; k++)
                matrix[j][k] = 0.;

            for (i = 0; i < num; i++)
                sum[j] += splKnots[i][j];

            aver[j] = sum[j] / ((double) num);
        }
        for (i = 0; i < num; i++) {
            double[] knot = splKnots[i];
            for (j = 0; j < 3; j++) {
                for (k = 0; k < 3; k++) {
                    matrix[j][k] += (knot[j] - aver[j]) * (knot[k] - aver[k]);
                }
            }
        }
    }

// -----------------------------------------------------------
// INPUT:
//     double    a[3][3], s, tau;
//     int       i, j, k, l;

    private void jacobi_rotate(double a[][], int i, int j, int k, int l,
                               double s, double tau) {
        double g, h;

        g = a[i][j];
        h = a[k][l];
        a[i][j] = g - s * (h + g * tau);
        a[k][l] = h + s * (g - h * tau);
    }

// -----------------------------------------------------------
//     /* INPUT: */
//     double    a[3][3];    /* Initial matrix. On output, elements of a above
//                              the diagonal are destroyed. */
//     /* OUTPUT: */
//     double    d[3];       /* Eigenvalues of a. */
//     double    v[3][3];    /* Matrix whose columns contain the normalized
//                              eigenvectors of a. */

    private int jacobi3(double a[][], double d[], double v[][]) {
        int i, j, ip, iq;
        double tresh, theta, tau, t, sm, s, h, g, c,
                b[] = {0, 0, 0}, z[] = {0, 0, 0};

        for (ip = 0; ip < 3; ip++) {
            b[ip] = d[ip] = a[ip][ip];
            z[ip] = 0.0;
            for (iq = 0; iq < 3; iq++)
                v[ip][iq] = 0.0;
            v[ip][ip] = 1.0;
        }

        for (i = 0; i < 50; i++) {
            sm = Math.abs(a[0][1]) + Math.abs(a[0][2]) + Math.abs(a[1][2]);
            if (sm < JACOBI_EPS)
                return (E_NO_ERROR);
            if (i < 4)
                tresh = 0.02 * sm;
            else
                tresh = 0.0;
            for (ip = 0; ip < 2; ip++) {
                for (iq = ip + 1; iq < 3; iq++) {
                    g = 100. * Math.abs(a[ip][iq]);

                    if (i > 4 &&
                            Math.abs(d[ip]) + g == Math.abs(d[ip]) &&
                            Math.abs(d[iq]) + g == Math.abs(d[iq]))
                        a[ip][iq] = 0.0;
                    else if (Math.abs(a[ip][iq]) > tresh) {
                        h = d[iq] - d[ip];
                        if (Math.abs(h) + g == Math.abs(h))
                            t = a[ip][iq] / h;
                        else {
                            theta = 0.5 * h / a[ip][iq];
                            if (Math.abs(theta) < JACOBI_ZERO)
                                theta = 0.0;
                            t = 1.0 / (Math.abs(theta) +
                                    Math.sqrt(1.0 + theta * theta));
                            if (theta < 0.0)
                                t = -t;
                        }
                        c = 1.0 / Math.sqrt(1.0 + t * t);
                        s = t * c;
                        tau = s / (1.0 + c);
                        h = t * a[ip][iq];
                        z[ip] -= h;
                        z[iq] += h;
                        d[ip] -= h;
                        d[iq] += h;
                        a[ip][iq] = 0.0;
                        for (j = 0; j < ip; j++)
                            jacobi_rotate(a, j, ip, j, iq, s, tau);
                        for (j = ip + 1; j < iq; j++)
                            jacobi_rotate(a, ip, j, j, iq, s, tau);
                        for (j = iq + 1; j < 3; j++)
                            jacobi_rotate(a, ip, j, iq, j, s, tau);
                        for (j = 0; j < 3; j++)
                            jacobi_rotate(v, j, ip, j, iq, s, tau);
                    }
                }
            }
            for (ip = 0; ip < 3; ip++)

            {
                b[ip] += z[ip];
                d[ip] = b[ip];
                z[ip] = 0.0;
            }
        }
        normalize_matrix(v);
        return (E_CONVERGENCE);
    }

// -----------------------------------------------------------
/*
   Transpose the m by n matrix[m][n] and return the result in
   transpose matrix[n][m].
   This routine assumes that matrix and tranpose are different!
*/

    private void transpose_matrix(int m, int n, double matrix[][], double transpose[][]) {
        int i, j;

        for (i = 0; i < m; i++) {
            for (j = 0; j < n; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }

    }  // end of transpose_matrix

// -------------------------------------------------------
// double loc_sys[4][3];   Matrix to extract data from.
//                         == NULL iff unit
//                         trasfromation is needed.
//        x_vec[3]

    private void get_vectors(double loc_sys[][], double x_vec[], double y_vec[],
                             double z_vec[], double o_pnt[]) {
        if (loc_sys != null) {
            if (x_vec != null) {
                x_vec[0] = loc_sys[0][0];
                x_vec[1] = loc_sys[1][0];
                x_vec[2] = loc_sys[2][0];
            }
            if (y_vec != null) {
                y_vec[0] = loc_sys[0][1];
                y_vec[1] = loc_sys[1][1];
                y_vec[2] = loc_sys[2][1];
            }
            if (z_vec != null) {
                z_vec[0] = loc_sys[0][2];
                z_vec[1] = loc_sys[1][2];
                z_vec[2] = loc_sys[2][2];
            }
            if (o_pnt != null) {
                o_pnt[0] = loc_sys[3][0];
                o_pnt[1] = loc_sys[3][1];
                o_pnt[2] = loc_sys[3][2];
            }
        } else {
            if (x_vec != null) {
                x_vec[0] = 1.0;
                x_vec[1] = 0.0;
                x_vec[2] = 0.0;
            }
            if (y_vec != null)

            {
                y_vec[0] = 0.0;
                y_vec[1] = 1.0;
                y_vec[2] = 0.0;
            }
            if (z_vec != null) {
                z_vec[0] = 0.0;
                z_vec[1] = 0.0;
                z_vec[2] = 1.0;
            }
            if (o_pnt != null) {
                o_pnt[0] = 0.0;
                o_pnt[1] = 0.0;
                o_pnt[2] = 0.0;
            }
        }
    }
// -----------------------------------------------------------
// Put components of local system.
// loc_sys[4][3];/* Matrix to put data into.
// Note: NULL may be passed instead of any of:
//    x_vec , y_vec, z_vec, o_pnt.
//    In this case corresponding elements in
//    loc_sys WILL NOT BE SET
//

    private void put_vectors(double loc_sys[][], double x_vec[], double y_vec[],
                             double z_vec[], double o_pnt[]) {

        if (x_vec != null) {
            loc_sys[0][0] = x_vec[0];
            loc_sys[1][0] = x_vec[1];
            loc_sys[2][0] = x_vec[2];
        }
        if (y_vec != null) {
            loc_sys[0][1] = y_vec[0];
            loc_sys[1][1] = y_vec[1];
            loc_sys[2][1] = y_vec[2];
        }
        if (z_vec != null) {
            loc_sys[0][2] = z_vec[0];
            loc_sys[1][2] = z_vec[1];
            loc_sys[2][2] = z_vec[2];
        }
        if (o_pnt != null) {
            loc_sys[3][0] = o_pnt[0];
            loc_sys[3][1] = o_pnt[1];
            loc_sys[3][2] = o_pnt[2];
        }
    }

// -----------------------------------------------------------

    private int normalize_matrix(double loc_sys[][]) {
        double x_vec[] = {0, 0, 0};
        double y_vec[] = {0, 0, 0};
        double z_vec[] = {0, 0, 0};
        int ierror;

        get_vectors(loc_sys, x_vec, y_vec, z_vec, null);
        ierror = normalize_vector(x_vec, x_vec);
        if (ierror == E_NO_ERROR)
            ierror = normalize_vector(y_vec, y_vec);
        if (ierror == E_NO_ERROR)
            ierror = normalize_vector(z_vec, z_vec);

        if (ierror != E_NO_ERROR)
            return (ierror);

        put_vectors(loc_sys, x_vec, y_vec, z_vec, null);

        return (E_NO_ERROR);
    }

// -----------------------------------------------------------
/*
   Multiply the m by n matrix mat1 and the n by p matrix mat2 to produce
     the m by p matrix prod.
   This routine assumes that mat1, mat2, and prod are all different!
   As usual matrices are stored as arrays and numbered as
    0, 1, 2, 3,..., n-1
    n, n+1,.......,2n-1
    ...
double  *mat1;   [m][n]
double  *mat2;   [n][p]
double  *prod;   [m][p]
*/

    private void
    matrix_multiply(int m, int n, int p, double mat1[][],
                    double mat2[][],
                    double prod[][]) {
        double sum;
        int i, j, k;

        for (i = 0; i < m; i++)
            for (j = 0; j < p; j++) {
                sum = 0;
                for (k = 0; k < n; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                prod[i][j] = sum;
            }

    }  // end of matrix_multiply

// -----------------------------------------------------------

    private double
    spg_acos(double cosine) {
        if ((cosine > 1.0) && ((cosine - 1.0) < PRECISION_EPSILON))
            cosine = 1.0;
        else if ((cosine < -1.0) && ((-cosine - 1.0) < PRECISION_EPSILON))
            cosine = -1.0;

        return (Math.acos(cosine));
    }
// -----------------------------------------------------------
/*
   Set all but the first and the last equations ,
   use the space for derivatives to hold all right-hand sides
*/

    private void makeMatrixAndRightVector(int ncol, int nrow, int nval, int col_row,
                                          double params[], double[][] splKnots, double der[][],
                                          double above[], double diago[], double under[]) {

        double inv_step_0, inv_step_1, aux0, aux1;
        int ii, npar;

        if (col_row != 0)
            npar = ncol - 1;
        else
            npar = nrow - 1;

        if (npar < 2)
            return;

        inv_step_0 = 1.0 / (params[1] - params[0]);

        for (ii = 1; ii < npar; ii++) {
            inv_step_1 = 1.0 / (params[ii + 1] - params[ii]);

            under[ii] += inv_step_0;
            above[ii] += inv_step_1;
            diago[ii] += 2.0 * (inv_step_0 + inv_step_1);

            aux0 = inv_step_0 * inv_step_0;
            aux1 = inv_step_1 * inv_step_1;

            linComSplKnots(ncol, nrow, nval, col_row,
                    aux1, splKnots, ii + 1, -aux1, splKnots, ii, der, ii);
            linComSplKnots(ncol, nrow, nval, col_row,
                    1.0, der, ii, aux0, splKnots, ii, der, ii);
            linComSplKnots(ncol, nrow, nval, col_row,
                    3.0, der, ii, -3.0 * aux0, splKnots, ii - 1, der, ii);
            inv_step_0 = inv_step_1;
        }
    }
// -----------------------------------------------------------
/*
   double       pnt_0[3], pnt_1[3], pnt_2[3];
   double       *p_arc_rad, arc_ctr[3];
   double       v0[3], v1[3];    x and y unit vectors.  May be
                                  NULL if data is not desired.
   double       *arc_angle;      NULL if data is not desired
*/

    private int
    arc_3_points(double pnt_0[], double pnt_1[], double pnt_2[],
                 double arc_ctr[], double v0[], double v1[],
                 double rad_and_angle[]) {
        double vec_a[] = {0, 0, 0}, vec_b[] = {0, 0, 0}, vec_c[] = {0, 0, 0},
                a_dot_b, a_x_b_sq,
                a_x_b[] = {0, 0, 0},
                rad, ang, c0, c1,
                p_ctr[] = {0, 0, 0}, x_vec[] = {0, 0, 0}, y_vec[] = {0, 0, 0};
        double len_a, len_b,
                unit_a[] = {0, 0, 0}, unit_b[] = {0, 0, 0},
                center[] = {0, 0, 0}, vec_x[] = {0, 0, 0},
                vec_y[] = {0, 0, 0};


        vsub3(pnt_1, pnt_0, vec_a);
        if ((len_a = vectorLength(vec_a)) != 0.0)
            scvec(1.0 / len_a, vec_a, unit_a);

        vsub3(pnt_2, pnt_0, vec_b);
        if ((len_b = vectorLength(vec_b)) != 0.0)
            scvec(1.0 / len_b, vec_b, unit_b);

        if (len_a != 0.0 && len_b != 0.0) {
            cross(unit_a, unit_b, a_x_b);
            a_x_b_sq = dot(a_x_b, a_x_b);
            a_dot_b = dot(unit_a, unit_b);
        } else {
            a_x_b_sq = 0.0;
            a_dot_b = 0.0;
        }

        if (len_a == 0.0 || len_b == 0.0 || a_x_b_sq < 1.0e-10) {
            /* straight line        */
            rad = 10e+10;
            ang = len_b / (rad);
            if (a_dot_b < 0.0 || len_b == 0.0)
                ang = TWOPI - ang;
            if (rad_and_angle != null) {
                rad_and_angle[0] = rad;
                rad_and_angle[1] = ang;
            }
            return (1);
        }

        c0 = Math.sqrt(a_x_b_sq);
        rad = vectorLength(vsub3(vec_a, vec_b, vec_c)) / (2.0 * c0);
        if (rad_and_angle != null)
            rad_and_angle[0] = rad;
        scvec(1.0 / c0, a_x_b, a_x_b);

        c0 = 0.5 * (1.0 - a_dot_b * len_b / len_a) / a_x_b_sq;
        c1 = 0.5 * (1.0 - a_dot_b * len_a / len_b) / a_x_b_sq;
        lincom(c0, vec_a, c1, vec_b, vec_c);
        p_ctr = arc_ctr != null ? arc_ctr : center;
        vadd(pnt_0, vec_c, p_ctr);
        x_vec = v0 != null ? v0 : vec_x;
        scvec(-1.0 / rad, vec_c, x_vec);
        y_vec = v1 != null ? v1 : vec_y;
        cross(a_x_b, x_vec, y_vec);

        if (rad_and_angle != null) {
            vsub3(pnt_2, p_ctr, vec_c);
            c0 = dot(vec_c, x_vec);
            c1 = dot(vec_c, y_vec);
            if (c0 == 0.0 && c1 == 0.0) {
                /* just a point */
                rad = 10e+10;
                ang = len_b / (rad);
                if (a_dot_b < 0.0 || len_b == 0.0)
                    ang = TWOPI - ang;
                rad_and_angle[0] = rad;
                rad_and_angle[1] = ang;
                return (2);
            }
            ang = Math.atan2(c1, c0);
            if (ang < 0.0)
                ang += TWOPI;
            rad_and_angle[1] = ang;
        }
        return (0);
    }

    /**
     * @param ncol
     * @param nrow
     * @param nval
     * @param col_row
     * @param tan_cond
     * @param params
     * @param fcn
     * @param der
     * @param diago
     * @param above
     * @param under
     * @return
     */
    private int boundary_conditions_for_spline(int ncol, int nrow, int nval, int col_row,
                                               int tan_cond,
                                               double params[], double[][] fcn,
                                               double der[][], double diago[],
                                               double above[], double under[]) {
        double step_0, step_1, aux0, aux1;
        int i, npar, npar_1, eq_set[] = {0, 0};
/*
         idx_inc, ind_inc, n_row,
         ind0, ind1, idx0, idx1, idx2;
*/
        double rad_and_angle[] = {0, 0};

        npar = ncol;
        npar_1 = npar - 1;
// System.out.println("boundary_conditions_for_spline   "+npar+"  "+tan_cond);
/* Set boundary conditions ( first and last equations ) */

        eq_set[0] = eq_set[1] = 0;

        if (tan_cond == SPL_TAN_PERIODIC)      /* First and second derivatives */ {                                       /* in the first and last knots  */
            /* are to be equal. */
//System.out.println("boundary_conditions_for_spline   eSPL_TAN_PERIODIC "+npar);
            diago[0] = 1.0;
            above[0] = 0.0;
            under[0] = -1.0;
            aux0 = 1.0 / (params[1] - params[0]);
            aux1 = 1.0 / (params[npar_1] - params[npar - 2]);
            diago[npar_1] = 2.0 * (aux0 + aux1);
            above[npar_1] = aux0;
            under[npar_1] = aux1;

            aux0 = 3.0 * aux0 * aux0;
            aux1 = 3.0 * aux1 * aux1;

            /*  D[0] = 0.0;                                              */
            /*  D[npar - 1] = ((f[1] - f[0]) * aux0 +                    */
            /*                 (f[npar - 1] - f[npar - 2]) * aux1) */
/*
        for ( row = 0, ind0 = 0, ind1 = npar_1 * ind_inc;
              row < n_row;
              row ++, ind0 += idx_inc, ind1 += idx_inc)
        for (idx = 0, idx0 = ind0, idx1 = ind1; idx < nval;
                  idx ++, idx0 ++, idx1 ++)
             {
              der[idx0] = 0.0;
              der[idx1] = ((fcn[idx0 + ind_inc] - fcn[idx0]) * aux0 +
                           (fcn[idx1] - fcn[idx1 - ind_inc]) * aux1 );
            }
*/
            for (i = 0; i < nval; i++) {
                der[0][i] = 0.0;
                der[npar_1][i] = ((fcn[1][i] - fcn[0][i]) * aux0 +
                        (fcn[npar_1][i] - fcn[npar_1 - 1][i]) * aux1);
            }

            eq_set[0] = eq_set[1] = 1;
        } else if (npar == 3 && tan_cond == SPL_TAN_NONE) {       /* Parabolic interpolation third derivatives == 0.0     */
//System.out.println("boundary_conditions_for_spline Parabolic "+npar);
            diago[0] = above[0] = -1.0;
            diago[2] = under[2] = 1.0;
            under[0] = above[2] = 0.0;
            aux0 = 2.0 / (params[1] - params[0]);
            aux1 = 2.0 / (params[2] - params[1]);

            /*   D[0] = (f[0] - f[1]) * aux0 */
            /*   D[2] = (f[2] - f[1]) * aux1 */
/*
        for (row = 0, ind0 = 0;
             row < n_row;
             row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0, idx1 = ind0 + ind_inc + ind_inc;
                  idx < nval; idx ++, idx0 ++, idx1 ++)
                  {
                  der[idx0][] = (fcn[idx0][] - fcn[idx0 + ind_inc][]) * aux0;
                  der[idx1][] = (fcn[idx1][] - fcn[idx0 + ind_inc][]) * aux1;
                  }
*/
            for (i = 0; i < nval; i++) {
                der[0][i] = (fcn[0][i] - fcn[1][i]) * aux0;
                der[2][i] = (fcn[2][i] - fcn[1][i]) * aux1;
            }

            eq_set[0] = eq_set[1] = 1;
        } else if (npar == 2 && tan_cond == SPL_TAN_START) {
            /* Add condition at non-tangent end to ensure it's a parabola. */

            aux0 = 2.0 / (params[1] - params[0]);
            diago[1] = under[1] = 1.0;
            above[1] = 0.0;
            eq_set[1] = 1;

            /*  D[1] = (f[1] - f[0]) * aux0  */
/*
        for (row = 0, ind0 = ind_inc; row < n_row; row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                  der[idx0] = (fcn[idx0] - fcn[idx0 - ind_inc]) * aux0;
*/
            for (i = 0; i < nval; i++)
                der[1][i] = (fcn[1][i] - fcn[0][i]) * aux0;
        } else if (npar == 2 && tan_cond == SPL_TAN_END) {
            /* Add condition at non-tangent start to ensure it's a parabola. */
//System.out.println("boundary_conditions_for_spline non-tangent "+npar);
            aux0 = 2.0 / (params[1] - params[0]);
            diago[0] = above[0] = -1.0;
            under[0] = 0.0;
            eq_set[0] = 1;

            /*  D[0] = (f[0] - f[1]) * aux0  */
/*
        for (row = 0, ind0 = 0; row < n_row; row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                  der[idx0] = (fcn[idx0] - fcn[idx0 + ind_inc]) * aux0;
*/
            for (i = 0; i < nval; i++)
                der[0][i] = (fcn[0][i] - fcn[1][i]) * aux0;
        }
//System.out.println("boundary_conditions_for_spline   -------- "+npar);
        if ((tan_cond & SPL_TAN_START) != 0) {                       /* Preserve derivatives at the start    */
            diago[0] = 1.0;
            under[0] = above[0] = 0.0;
            eq_set[0] = 1;
//System.out.println("boundary_conditions_for_spline   SPL_TAN_START "+npar);
        } else if ((tan_cond & SPL_FLAT_START) != 0) {
//System.out.println("boundary_conditions_for_spline   SPL_FLAT_START "+npar);
            aux0 = 3.0 / (params[1] - params[0]);
            diago[0] = 2.0;
            above[0] = 1.0;
            under[0] = 0.0;

            /* D[0] = (f[1] - f[0]) * aux0  */
/*
        for (row = 0, ind0 = 0; row < n_row; row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                  der[idx0] = (fcn[idx0 + ind_inc] - fcn[idx0]) * aux0;
*/

            for (i = 0; i < nval; i++)
                der[0][i] = (fcn[1][i] - fcn[0][i]) * aux0;

            eq_set[0] = 1;
        } else if ((tan_cond & SPL_TAN_QUAD_START) != 0) {
//System.out.println("boundary_conditions_for_spline SPL_TAN_QUAD_START  "+npar);
            aux0 = 1.0 / (params[1] - params[0]);
            aux1 = 1.0 / (params[2] - params[0]);
            diago[0] = 1.0;
            under[0] = above[0] = 0.0;

            /* D[0] = ((f[1] - f[0]) * aux0 * aux0 - (f[2] - f[0]) * aux1 * aux1)
        / (aux0 - aux1) */
/*
        for (row = 0, ind0 = 0; row < n_row; row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                 der[idx0] = ((fcn[idx0 + ind_inc] - fcn[idx0]) * aux0 * aux0 -
                              (fcn[idx0 + ind_inc + ind_inc] - fcn[idx0]) *
                              aux1 * aux1) / (aux0 - aux1);
*/

            for (i = 0; i < nval; i++)
                der[0][i] = ((fcn[1][i] - fcn[0][i]) * aux0 * aux0 -
                        (fcn[2][i] - fcn[0][i]) * aux1 * aux1) / (aux0 - aux1);

            eq_set[0] = 1;
        } else if ((tan_cond & SPL_TAN_ARC_START) != 0) {
//System.out.println("boundary_conditions_for_spline SPL_TAN_ARC_START  "+npar);
/*
   The derivative at point 0 is calculated as a tangent vector to
   an arc passing through the first 3 points of the spline. The value (up
   to the sign is calculated as follows

        d(f) = r d(angle) ==> d(f)/d(t) = r d(angle)/d(t) (r is a constant)

   then assuming that d(angle)/d(t) is a constant we get

                d(f)/d(t) = r delta(angle) / delta(t)
*/
            double p0[] = {0, 0, 0},
                    p1[] = {0, 0, 0},
                    p2[] = {0, 0, 0},
                    c[] = {0, 0, 0};    /* the first 3 points of the spline
                                         and the center of the arc they form */
            /* vectors from the center of the arc to
    the points 0 and 2 of the spline */
            double c_p0[] = {0, 0, 0};

            /* vectors between points */
            double p0_p1[] = {0, 0, 0}, p0_p2[] = {0, 0, 0};

            double N[] = {0, 0, 0}; /* normal of the plane of the arc */

            /* the vector tangent to the arc at p0 */
            double tang_vec[] = {0, 0, 0};
            //     double r; /*the radius of the arc*/
            //     double angle; /*the angle of the arc*/

            diago[0] = 1.0;
            under[0] = above[0] = 0.0;
/*
        for (row = 0, ind0 = 0; row < n_row; row ++, ind0 += idx_inc)
        {
*/
            for (i = 0; i < nval; i++) {
                p0[i] = fcn[0][i];
                p1[i] = fcn[1][i];
                p2[i] = fcn[2][i];
            }

            if (arc_3_points(p0, p1, p2, c, null, null, rad_and_angle) != 0) {
                /* either points are too close one to another or
       the radius is too big -- use linar approximation */

                for (i = 0; i < nval; i++) {
                    der[0][i] = (p1[i] - p0[i]) / (params[1] - params[0]);
                }
            } else {
                /* calculate normal to the plane */

                vsub3(p0, c, c_p0);
                vsub3(p1, p0, p0_p1);
                vsub3(p2, p0, p0_p2);
                cross(p0_p1, p0_p2, N); /* the vectors aren't collinear */

                /* want vector pointing inside the spline */

                cross(N, c_p0, tang_vec);
                normalize_vector(tang_vec, tang_vec);

                for (i = 0; i < nval; i++) {
                    der[0][i] = tang_vec[i] * rad_and_angle[0] * rad_and_angle[1] /
                            (params[2] - params[0]);
                }
            }
/*
    }
*/
            eq_set[0] = 1;
        } else if (eq_set[0] == 0) {                                   /* No third derivative discontinuity
                                        across second parameter knot.*/
//System.out.println("boundary_conditions_for_spline   eq_set[0] "+npar);
            step_0 = params[1] - params[0];
            step_1 = params[2] - params[1];
            aux0 = 1.0 / (step_0 * step_0);
            aux1 = 1.0 / (step_1 * step_1);
            diago[0] = under[1] + step_1 * aux0;
            above[0] = diago[1] + step_1 * (aux0 - aux1);
            under[0] = 0.0;

            aux0 *= 2.0;
            aux1 *= 2.0;

            /* D[0] = D[1] + step_1 *
    ((f[1] - f[2]) * aux1 / step_1 -
     (f[0] - f[1]) * aux0 / step_0) */
            /* Do not replace aux1 / step_1 by aux1 and aux0 / step_0 by step_0 */
/*
        for (row = 0, ind0 = 0; row < n_row; row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0, idx1 = ind0 + ind_inc,
                  idx2 = ind0 + ind_inc + ind_inc;
                  idx < nval; idx ++, idx0 ++, idx1 ++, idx2 ++)
                  der[idx0] = der[idx1] + step_1 *
                                 ((fcn[idx1] - fcn[idx2]) * aux1 / step_1
                                  - (fcn[idx0] - fcn[idx1]) * aux0/step_0);
*/

            for (i = 0; i < nval; i++)
                der[0][i] = der[1][i] + step_1 *
                        ((fcn[1][i] - fcn[2][i]) * aux1 / step_1
                                - (fcn[0][i] - fcn[1][i]) * aux0 / step_0);

            eq_set[0] = 1;
        }

        if ((tan_cond & SPL_TAN_END) != 0) {                       /* Preserve derivatives at the end      */
            diago[npar_1] = 1.0;
            under[npar_1] = above[npar_1] = 0.0;
            eq_set[1] = 1;
        } else if ((tan_cond & SPL_FLAT_END) != 0) {
            aux1 = 3.0 / (params[npar_1] - params[npar - 2]);
            diago[npar_1] = 2.0;
            above[npar_1] = 0.0;
            under[npar_1] = 1.0;

            /* D[npar - 1] = (f[npar - 1] - f[npar - 2]) * aux1;   */
/*
        for (row = 0, ind0 = npar_1 * ind_inc; row < n_row;
             row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                  der[idx0] = (fcn[idx0] - fcn[idx0 - ind_inc]) * aux1;
*/
            for (i = 0; i < nval; i++)
                der[npar_1][i] = (fcn[npar_1][i] - fcn[npar_1 - 1][i]) * aux1;

            eq_set[1] = 1;
        } else if ((tan_cond & SPL_TAN_QUAD_END) != 0) {
            aux0 = 1.0 / (params[npar_1] - params[npar - 2]);
            aux1 = 1.0 / (params[npar_1] - params[npar - 3]);
            diago[npar_1] = 1.0;
            above[npar_1] = under[npar_1] = 0.0;

            /* D[npar - 1] = ((f[npar - 1] - f[npar - 2]) * aux0 * aux0 -
         (f[npar - 1] - f[npar - 3]) * aux1 * aux1) /
         (aux0 - aux1) */
/*
        for (row = 0, ind0 = npar_1 * ind_inc; row < n_row;
             row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0; idx < nval; idx ++, idx0 ++)
                 der[idx0] = ((fcn[idx0] - fcn[idx0 - ind_inc]) * aux0 * aux0 -
                              (fcn[idx0] - fcn[idx0 - ind_inc - ind_inc]) *
                              aux1 * aux1) / (aux0 - aux1);
*/
            for (i = 0; i < nval; i++)
                der[npar_1][i] = ((fcn[npar_1][i] - fcn[npar_1 - 1][i]) * aux0 * aux0 -
                        (fcn[npar_1][i] - fcn[npar_1 - 2][i]) * aux1 * aux1)
                        / (aux0 - aux1);
            eq_set[1] = 1;
        } else if ((tan_cond & SPL_TAN_ARC_END) != 0) {

/* The derivative at the endpoint is calculated as a tangent vector to
   an arc passing through the last 3 points of the spline. The value (up
   to the sign is calculated as follows
        d(f) = r d(angle) ==> d(f)/d(t) = r d(angle)/d(t) (r is a constant)

   then assuming that d(angle)/d(t) is a constant we get

                d(f)/d(t) = r delta(angle) / delta(t)
*/
            double p0[] = {0, 0, 0},
                    p1[] = {0, 0, 0},
                    p2[] = {0, 0, 0},
                    c[] = {0, 0, 0};      /* the last 3 points of the spline
                                      starting from the end and the
                                      center of the arc they form */
            /* vectors from the center of the arc to
    the points 0 and 2 of the spline */
            double c_p0[] = {0, 0, 0};

            /* vectors between points */
            double p0_p1[] = {0, 0, 0}, p0_p2[] = {0, 0, 0};

            double N[] = {0, 0, 0}; /* normal of the plane of the arc */

            double tang_vec[] = {0, 0, 0}; /* the vector tangent to the arc at p0 */
            // double rad_and_angle[0]; the radius of the arc
            // double rad_and_angle[1]; the angle of the arc

            diago[0] = 1.0;
            under[0] = above[0] = 0.0;
/*
        for (row = 0, ind0 = npar_1 * ind_inc; row < n_row;
                      row ++, ind0 += idx_inc)
        {
*/
            for (i = 0; i < nval; i++) {
                p0[i] = fcn[npar_1][i];
                p1[i] = fcn[npar_1 - 1][i];
                p2[i] = fcn[npar_1 - 2][i];
            }

            if (arc_3_points(p0, p1, p2, c, null, null, rad_and_angle)
                    != 0) {
                /* either points are too close one to another or
       the radius is too big -- use linar approximation */
                for (i = 0; i < nval; i++) {
                    der[npar_1][i] = (p0[i] - p1[i]) / (params[npar_1] - params[npar - 2]);

                }
            } else {
                /* calculate normal to the plane */

                vsub3(p0, c, c_p0);
                vsub3(p1, p0, p0_p1);
                vsub3(p2, p0, p0_p2);
                cross(p0_p1, p0_p2, N); /* the vectors aren't collinear */

                /* want vector pointing outside the spline */

                cross(c_p0, N, tang_vec);
                normalize_vector(tang_vec, tang_vec);


                for (i = 0; i < nval; i++) {
                    der[npar_1][i] = tang_vec[i] * rad_and_angle[0] *
                            rad_and_angle[1] /
                            (params[npar_1] - params[npar - 3]);
                }
            }
            /*  } */
            eq_set[1] = 1;
        } else if (eq_set[1] == 0) {                              /* No third derivative discontinuity
                                   across next to last parameter knot.*/
//System.out.println("boundary_conditions_for_spline   eq_set[1] "+npar);
            step_0 = params[npar - 2] - params[npar - 3];
            step_1 = params[npar_1] - params[npar - 2];
            aux0 = 1.0 / (step_0 * step_0);
            aux1 = 1.0 / (step_1 * step_1);
            under[npar_1] = diago[npar - 2] - step_0 * (aux0 - aux1);
            diago[npar_1] = above[npar - 2] + step_0 * aux1;
            above[npar_1] = 0.0;

            aux1 *= 2.0;
            aux0 *= 2.0;

            /*  D[npar - 1] = D[npar - 2] - step_0 *
((f[npar - 2] - f[npar - 1]) * aux1 / step_1 -
(f[npar - 3] - f[npar - 2]) * aux0 / step_0)  */
            /* Do not replace aux1 / step_1 by aux1 and aux0 / step_0 by step_0 */
/*
        for (row = 0, ind0 = npar_1 * ind_inc; row < n_row;
             row ++, ind0 += idx_inc)
             for (idx = 0, idx0 = ind0, idx1 = ind0 - ind_inc,
                  idx2 = idx1 - ind_inc;
                  idx < nval; idx ++, idx0 ++, idx1 ++, idx2 ++)
                  der[idx0] = der[idx1] - step_0 *
                                    ((fcn[idx1] - fcn[idx0]) * aux1 / step_1
                                   - (fcn[idx2] - fcn[idx1]) * aux0 / step_0);
*/
            for (i = 0; i < nval; i++)
                der[npar_1][i] = der[npar - 2][i] - step_0 *
                        ((fcn[npar - 2][i] - fcn[npar - 1][i]) * aux1 / step_1
                                - (fcn[npar - 3][i] - fcn[npar - 2][i]) * aux0 / step_0);

            eq_set[1] = 1;
//  System.out.println("der["+npar_1+"] = "+der[npar_1][0]+"   "
//  +der[npar_1][1]+"   "+der[npar_1][2]);
        }


        return (E_NO_ERROR);
    }

// -----------------------------------------------------------
/*
  ======================================
          solve_3_diago_lin
  ======================================
*/
/* -------------------------------------------------------------------- */
/* Solve linear systems of 3-diagonal equations simultaneously  */


//     INPUT:
//
//     int nCol;            Number of columns
//     int nRow;           /Number of rows
//     int nVal;            Number of values in each node
//     double under[], diago[], above[];
//     3 diagonales of the matrix
//     int col_row;         1 - calculation in column direction
//     0 - calculation in row direction
//
//     boolean diag_flag;    true  - matrix has non-zero elements only on 3 main diagonals
//     (u[0] == a[n-1] == 0);
//     false - matrix has u[0] and/or a[n-1] not egual to zero.
//
//     INPUT-OUTPUT:
//
//     double res[];           Right-hand sides (input),
//     result (output)

    /* Note: "res" is really three dimensional array of the type
            array[ncol][nrow][nval]. It is only declared
            as one dimensional because of "C" limitations.  */
// -----------------------------------------------------------------------
    private int
    solve_3_diago_lin(int ncol, int nrow, int nval,
                      double under[], double diago[],
                      double above[], int col_row, boolean diag_flag,
                      double res[][]) {
        int i, npar, idx, row, ii, ncol_nval, n_row, npar_1_idx,
                idx0, idx1, idxi, idxj, ii1;

        ncol_nval = ncol * nval;

        npar = ncol;
        n_row = nrow;
// idx_inc = ncol_nval;
// ind_inc = nval;
        npar_1_idx = (npar - 1);
//System.out.println("Solve dag 0    "+ncol);
/*
  d[0]   a[0]    0      0      0                    u[0]    x[0]     y[0]
  u[1]   d[1]   a[1]    0      0                     0      x[1]     y[1]
         u[2]   d[2]   a[2]    0                     0      x[2]     y[2]
                u[3]   d[3]   a[3]                   0      x[3]     y[3]
                       . . . . . . . . .
                                                          *        =
                                . . . . . . .
   0      0      0      .     u[n-3] d[n-3] a[n-3]  0      x[n-3]   y[n-3]
   0      0      0      .      0     u[n-2] d[n-2] a[n-2]  x[n-2]   y[n-2]
   0    a[n-1]   0      .      0      0     u[n-1] d[n-1]  x[n-1]   y[n-1]
*/

        // Elimination of matrix elements under the diagonal
        for (ii = 0; ii < npar - 1; ii++) {
//System.out.println("Solve dag loop 0  "+ii+"   "+ncol);
            /* Divide by diagonal element and
   eliminate next element under the diagonal    */

            if (diago[ii] == 0.0)
                return (E_CANT_SOLVE);

            above[ii] /= diago[ii];
            if (!diag_flag)
                under[ii] /= diago[ii];

            /*  X[ii] =     X[ii] / diago[ii]                */
            /*  X[ii + 1] = X[ii + 1] - X[ii] * under[ii+1]  */
/*
    for (row = 0, idx0 = idx_start; row < n_row; row ++, idx0 += idx_inc )
         for (idx = 0, idxi = idx0; idx < nval; idx ++, idxi ++)
              {
               res[idxi][i] /= diago[ii];
               res[idxi + ind_inc][i] -= (res[idxi] * under[ii + 1]);
              }
*/
            for (i = 0; i < nval; i++) {
                res[ii][i] /= diago[ii];
                res[ii + 1][i] -= (res[ii][i] * under[ii + 1]);
            }
//System.out.println("Solve dag loop 1  "+ii+"   "+ncol);

            diago[ii + 1] -= (above[ii] * under[ii + 1]);

            if (diag_flag)
                continue;

            under[ii + 1] = -under[ii] * under[ii + 1];

            if (ii == npar - 3) {
                above[npar - 2] += under[npar - 2];
                under[npar - 2] = 0.0;
            }

            if (ii == 0 || ii == npar - 2)
                continue;

            /* Eliminate next element in the last row       */

            diago[npar - 1] -= (under[ii] * above[npar - 1]);

            /*  X[npar - 1] = X[npar - 1] - X[ii] * above[npar-1]   */
/*
    for (row = 0, idx0 = npar_1_idx, idx1 = idx_start; row < n_row;
         row ++, idx0 += idx_inc, idx1 += idx_inc )
         for (idx = 0, idxi = idx0, idxj = idx1; idx < nval;
              idx ++, idxi ++, idxj ++)
              res[idxi] -= (res[idxj] * above[npar-1]);
*/

            for (i = 0; i < nval; i++) {
                res[npar - 1][i] -= (res[ii][i] * above[npar - 1]);
            }

            above[npar - 1] = -above[ii] * above[npar - 1];

            if (ii == npar - 3) {
                under[npar - 1] += above[npar - 1];
                above[npar - 1] = 0.0;
            }
        }

//System.out.println("Solve dag 1  "+ncol);
        // Now the system of equations looks like:
/*
   1     a[0]    0      0      0                    u[0]    x[0]     y[0]
   0      1     a[1]    0      0                    u[1]    x[1]     y[1]
   0      0      1     a[2]    0                    u[2]    x[2]     y[2]
                 0      1     a[3]                  u[3]    x[3]     y[3]
                       . . . . . . . . .
                                                          *        =
                                . . . . . . .
   0      0      0      .      0      1     a[n-3] u[n-3]  x[n-3]   y[n-3]
   0      0      0      .      0      0      1     a[n-2]  x[n-2]   y[n-2]
   0      0      0      .      0      0      0     d[n-1]  x[n-1]   y[n-1]
*/

/* Substitution, finding solution       */

/*  X[npar - 1] = X[npar - 1] / diago[npar - 1] */

        if (n_row > 0 && nval > 0 && diago[npar - 1] == 0.0)
            return (E_CANT_SOLVE);
/*
  for (row = 0, idx0 = npar_1_idx; row < n_row; row ++, idx0 += idx_inc)
     for (idx = 0, idxi = idx0; idx < nval; idx ++, idxi ++)
          res[idxi] /= diago[npar - 1];
*/
//System.out.println("Solve dag 2  "+ncol);
        for (i = 0; i < nval; i++)
            res[npar - 1][i] /= diago[npar - 1];

/*  for (ii = npar - 2; ii >= 0; ii --)                                   */
/*       X[ii] = X[ii] - X[ii + 1] * above[ii]                            */
/*                       or                                               */
/*       X[ii] = X[ii] - X[ii + 1] * above[ii] - X[npar - 1] * under[ii]; */
/*
 for (ii = npar - 2, ii1 = idx_start - ind_inc; ii >= 0;
      ii --, ii1 -= ind_inc)
 for (row = 0, idx0 = ii1, idx1 = npar_1_idx; row < n_row;
      row ++, idx0 += idx_inc, idx1 += idx_inc)
 for (idx = 0, idxi = idx0, idxj = idx1; idx < nval;
      idx ++, idxi ++, idxj ++)
 {
   if (diag_flag)
     res[idxi] -= (res[idxi + ind_inc] * above[ii]);
   else
   res[idxi] -= (res[idxi + ind_inc] * above[ii] + res[idxj] * under[ii]);
 }
*/
        for (ii = npar - 2; ii >= 0; ii--)
            for (i = 0; i < nval; i++) {
                if (diag_flag)
                    res[ii][i] -= (res[ii + 1][i] * above[ii]);
                else
                    res[ii][i] -= (res[ii + 1][i] * above[ii] + res[npar - 1][i] * under[ii]);
            }

        return (E_NO_ERROR);
    }

    /*
    // ---------------------------------------------------
    //draw_spline_(p_spline, K_NOT_USED, K_NOT_USED, FALSE);
    // ============================================================
    //    D r a w   s p l i n e
    // ============================================================
    // Draws several segments of a spline
    //int     seg_sta;         Index of first segment to draw
    //int     seg_end;         Index of last segment to draw
    //                          iff < 0 than to the end
    //int     dealloc;         dealloc the array if not already done
    */
/*
  diag = dist(outline[0], outline[1]);
  eps = 0.0001 * diag;

entity_to_polyline_farr(entity, eps, 0.0, 1.0, null, points,
                                     MAX_LIMIT_PNTS);
spl_to_polyln((Spline *)p_entity, eps, t0, t1, TRUE,
                                  params_arr, pnts_arr, n_max_pnts);
int spl_to_polyln(Spline *p_spline, double eps,
                            double in_t0, double in_t1, int norm = true,
                            double *params_arr, Pnt_3d *pnts_arr, int n_max)

*/
    public void printSplineKnots(double[][] splKnots) {
        int nKnots = splKnots.length;
        System.out.println("\n Spline Knots,  n = " + nKnots);
        for (int i = 0; i < nKnots; i++) {
            double[] splKnot = splKnots[i];
            System.out.println("splKnots i= " + i + ",  " + splKnot[0] + ",  " + splKnot[1] + ",  " + splKnot[2]);
        }
        System.out.println("\n");
    }

// ---------------------------------------------------------

    private List<double[]> sketchSpline(double[][] drawKnots, double[][] drawTangts,
                                        List<double[]> splCSysPoints, int seg_sta, int seg_end) {
        int nDrawKnots = drawKnots.length;
        int id, flag;
        double spln_pnt[] = {0, 0, 0};
        double parm, step;
        int i;
        int seg;
//        printSplineKnots();
// Tesselation of spline segments for display/selection purposes
// System.out.println("sketch_spline: start");


        if (nDrawKnots < 2 || interpDone == false || eval_done) {
            if (nDrawKnots == 1) {
                double[] firstPnt = new double[3];
                splCSysPoints.clear();
                copyVector(splKnots[0], firstPnt);
                splCSysPoints.add(firstPnt);
            }
            return splCSysPoints;
        }

// System.out.println("sketch_spline:========= nDrawKnots   "+n_spln_pnts);
        if (seg_sta < 0) {
            seg_sta = 0;
        }
        if (seg_end < 0 || seg_end > nDrawKnots - 2) {
            seg_end = nDrawKnots - 2;
        }
        if (seg_sta > seg_end) {
            return splCSysPoints;
        }

        id = SPLNSEG_GRTESS * seg_sta;
// System.out.println("sketch_spline: 1  "+seg_sta+"  "+seg_end+"  "+id);
        splCSysPoints.clear();

        // first calculate the points that are needed to be calculated
        for (seg = seg_sta; seg <= seg_end; seg++) {
            parm = drawParams[seg];
            step = (drawParams[seg + 1] - drawParams[seg]) /
                    ((double) SPLNSEG_GRTESS);
            flag = (seg == seg_sta) ? 0 : 1;
            if (flag == 0) {
                parm -= step;
            }

            for (int cnt = flag - 1; cnt < SPLNSEG_GRTESS; cnt++) {
                parm += step;
                spln_pnt = new double[3];
                evaluateSpline(drawKnots, drawTangts, parm, false, spln_pnt, null, null);
//   System.out.println("sketchSpline eval "+id + spln_pnt[0]+"    "+spln_pnt[1]);
                splCSysPoints.add(spln_pnt);
                id++;
            }
        }
// System.out.println("sketch_spline:==================== Done  "+n_spln_pnts);
        eval_done = true;
        return splCSysPoints;
    }

/*
#define DOT4(A,B) (A[0]*B[0] + A[1]*B[1] + A[2]*B[2] + A[3]*B[3])
#define DOT5(A,B) (A[0]*B[0] + A[1]*B[1] + A[2]*B[2] + A[3]*B[3] + A[4]*B[4])
#define DOT6(A,B) (A[0]*B[0] + A[1]*B[1] + A[2]*B[2] + A[3]*B[3] + A[4]*B[4]

                   A[5]*B[5])
*/
// --------------------------------------------------------------
/* Evaluate spline, its first and second derivatives            */

//double  param;          /* spline parameter                     */
//int     norm;           /* flag indicating if param is normalized
//                           (0.0 start of the spline  1.0 - end )
//                           == TRUE      normalized
//                           == FALSE     not normalized          */
//double  *der0;          /* output: spline coordinates
//                                may be null if not interested   */
//double  *der1;          /* output: first derivative,
//                                may be null if not interested   */
//double  *der2;          /* output: second derivative
//                                may be null if not interested   */

    private void evaluateSpline(double[][] drawKnots, double[][] drawTangts,
                                double param, boolean norm, double der0[], double der1[], double der2[]) {

        int nPnts = drawKnots.length;
        int ii, ind, less, more;
        double del_tot, del_seg, par;
        double h0[] = {0, 0, 0, 0},
                h1[] = {0, 0, 0, 0},
                h2[] = {0, 0, 0, 0},
                ph0[], ph1[], ph2[];


        if (nPnts < 2)
            return;

        // create ordinary array of 3D points

        del_tot = (drawParams[nPnts - 1] - drawParams[0]);

        // Use unnormalized parameters for the whole spline

        if (norm)
            par = param * del_tot + drawParams[0];
        else
            par = param;

        if (tan_cond == SPL_TAN_PERIODIC)  // Bring parameter into the range
        {
            while (par > drawParams[0])
                par -= del_tot;
            while (par < drawParams[0])
                par += del_tot;
        }

        // Use bynary search to find proper spline segment

        less = 0;
        more = nPnts - 2;
        for (ind = (less + more) / 2; less < more; ind = (less + more) / 2) {
            if (ind >= nPnts) {
//   System.out.println("rval "+  ind+"  "+nDrawKnots);
            }
            if (par < drawParams[ind])
                more = ind - 1;
            else if (par >= drawParams[ind + 1])
                less = ind + 1;
            else
                less = more = ind;
        }

        del_seg = drawParams[ind + 1] - drawParams[ind];
        if (del_seg == 0.0)
            del_seg = 1.0;

        // Use normalized segment parameter

        par = (par - drawParams[ind]) / del_seg;

        ph0 = der0 == null ? null : h0;
        ph1 = der1 == null ? null : h1;
        ph2 = der2 == null ? null : h2;
        hermite3(par, ph0, ph1, ph2);

        for (ii = 0; ii < 3; ii++) {
            double[] coeff = {0, 0, 0, 0};
            coeff[0] = drawKnots[ind][ii];
            coeff[1] = drawKnots[ind + 1][ii];
            coeff[2] = drawTangts[ind][ii] * del_seg;
            coeff[3] = drawTangts[ind + 1][ii] * del_seg;

            Double doub0 = new Double(coeff[2]);
            Double doub1 = new Double(coeff[3]);
            if (doub0.isNaN() || doub1.isNaN()) {
                System.out.println();
            }
/*
 System.out.println("rval "+  tangts[ind][ii]+"   "+del_seg);
 System.out.println("rval "+  tangts[ind+1][ii]+"   "+del_seg);
 System.out.println("rval "+ coeff[0]+"    "+
                                 coeff[1]+"    "+
                                 coeff[2]+"    "+
                                 coeff[3]);
*/
            // Hermite interpolation of cubic polinomial

            if (der0 != null) {
                der0[ii] = coeff[0] * h0[0] + coeff[1] * h0[1] + coeff[2] * h0[2] + coeff[3] * h0[3];

// System.out.println("rval "+ der0[ii]);
//System.out.println("rval "+ der0[ii]+"  "+h0[0]+"  "+h0[1]+"   "+
//h0[2]+"  "+h0[4]);
            }

            if (der1 != null) {
                der1[ii] = coeff[0] * h1[0] + coeff[1] * h1[1] + coeff[2] * h1[2] + coeff[3] * h1[3];
                der1[ii] /= del_seg;
                if (norm)
                    der1[ii] *= del_tot;
            }

            if (der2 != null) {
                der2[ii] = coeff[0] * h2[0] + coeff[1] * h2[1] + coeff[2] * h2[2] + coeff[3] * h2[3];
                der2[ii] /= (del_seg * del_seg);
                if (norm) {
                    der2[ii] *= (del_tot * del_tot);
                }
            }
        }
    }
// ---------------------------------------------------------------
// Evaluate Hiermite's cubic interpolation functions.
//
// double  p0;     /* parameter value          */
// double  der0[4];
// double  der1[4];    /* may be null if not interested    */
// double  der2[4];    /* may be null if not interested    */
//

    private void
    hermite3(double p0, double der0[], double der1[], double der2[]) {

        double p1 = 1.0 - p0;
        double pp = p0 * p1;
        double dd = p1 - p0;

        // Hermitian cubic polinomials satisfy

// Index[0] - fcn(1.0) = der(0.0) = der(1.0) = 0.0,  fcn(0.0) = 1.0
// Index[1] - fcn(0.0) = der(0.0) = der(1.0) = 0.0,  fcn(1.0) = 1.0
// Index[2] - fcn(0.0) = fcn(1.0) = der(1.0) = 0.0,  der(0.0) = 1.0
// Index[1] - fcn(0.0) = fcn(1.0) = der(0.0) = 0.0,  der(1.0) = 1.0


        if (der0 != null) {
            der0[2] = p1 * pp;
            der0[3] = -p0 * pp;
            der0[0] = 2.0 * der0[2] + p1 * p1;
            der0[1] = 1.0 - der0[0];
        }

        if (der1 != null) {
            der1[0] = -6.0 * pp;
            der1[1] = -der1[0];
            der1[2] = p1 - 3.0 * pp;
            der1[3] = der1[2] - dd;
        }

        if (der2 != null) {
            der2[0] = -6.0 * dd;
            der2[1] = -der2[0];
            der2[2] = -1.0 - 3.0 * dd;
            der2[3] = der2[2] + 2.0;
        }
    }

// ===================================
/*
static void waitKbdHit()
{
 BufferedReader kbdInp;

 System.out.println("Hit kbd to continue.");
 InputStreamReader inpStreamReader =
                      new InputStreamReader(System.in);
  try
  {
   inpStreamReader.read();
  } catch (IOException e)
   {
    System.out.println( "Error reading Kbd" );
   }

} // End of waitKbdHit
*/
}

