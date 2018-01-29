package mcln.model;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/10/14
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MclnAlgebra {

    /* Multi Color Net variables */
    static final int MCL_CORE_POWER = 4;
    public static final int MCL_CORE_MAX = ((int) Math.pow(2, MCL_CORE_POWER)) - 1;  // 15
    public static final int MCL_VALUE_BASE= ((int) Math.pow(2, MCL_CORE_POWER));
    static final int MCL_CORE_RANGE = 3;


    public static final int MCL_NOTHING = 1;
    public static final int MCL_CONTRAD = 2;
    public static final int MCL_UNKNOWN = 3;
    public static final int MCL_POSITIV = 4;
    public static final int MCL_NEGATIV = 5;


    static final int NO_THRESHOLD = MCL_NOTHING;
    static final int POSITIV_THRESHOLD = MCL_POSITIV;
    static final int NEGATIV_THRESHOLD = MCL_NEGATIV;

    private static final int mcDiscolTab[][] = {
//          {MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
            {MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING},
            {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
    };
    private static final int mcAdd1Tab[][] = {
//          {MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
            {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
            {MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_UNKNOWN, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_POSITIV, MCL_CONTRAD, MCL_CONTRAD, MCL_POSITIV, MCL_CONTRAD},
            {MCL_NEGATIV, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_NEGATIV},
    };
    private static final int mcColorTab[][] = {
//          {MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
            {MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING},
            {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN},
            {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
            {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_NEGATIV, MCL_POSITIV},
    };
    private static final int mcAdd2Tab[][] = {
//          {MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
            {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
            {MCL_CONTRAD, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
            {MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN},
            {MCL_POSITIV, MCL_POSITIV, MCL_UNKNOWN, MCL_POSITIV, MCL_UNKNOWN},
            {MCL_NEGATIV, MCL_NEGATIV, MCL_UNKNOWN, MCL_UNKNOWN, MCL_NEGATIV},
    };

    //
    //
    //

    static boolean isColor(int state) {
        return (Math.abs(state) >= MCL_CORE_MAX);
    }

    private static int sign(int x) {
        return (x == 0) ? x : (x / Math.abs(x));
    }

    static String coreStateToString(int state) {
        String stateStr;
        if (state == MclnAlgebra.MCL_NOTHING) {
            stateStr = "NOTHING";
        } else if (state == MclnAlgebra.MCL_CONTRAD) {
            stateStr = "CONTRADICTION";
        } else if (state == MclnAlgebra.MCL_UNKNOWN) {
            stateStr = "UNKNOWN";
        } else if (state == MclnAlgebra.MCL_POSITIV) {
            stateStr = "POSITIVE";
        } else if (state == MclnAlgebra.MCL_NEGATIV) {
            stateStr = "NEGATIVE";
        } else {
            stateStr = "State is NOTHING";
        }
        return stateStr;
    }

    static void print(int x) {
        if (Math.abs(x) < MCL_CORE_MAX) {
            System.out.println("State " + x + "is les than MCL CORE MAX " + MCL_CORE_MAX);
            return;
        }
        System.out.println("State " + x + " is " + coreStateToString(x));
    }

    static String int32toBinaryStr(int a) {
        int mask = 1 << 31;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 32; mask = mask >>> 1, i++)
            stringBuilder.append((((a & mask) == mask) ? " 1" : " 0")
                    + ((((i + 1) % 4) == 0) ? "  " : ""));
        return stringBuilder.toString();
    }

    static int MAX_VAL = 0x0FFFFFFF;
    static int VAL_MASK = 0x0FFFFFF0;

    static int getNOTHING() {
        return MCL_NOTHING;
    }

    static int getCONTRADICT() {
        return MCL_CONTRAD;
    }

    static int getUNKNOWN() {
        return MCL_UNKNOWN;
    }

    static int getPOSITIVE() {
        return MCL_POSITIV;
    }

    static int getNEGATIVE() {
        return MCL_NEGATIV;
    }

    static boolean isNOTHING(int val) {
        return (val == MCL_NOTHING);
    }

    static boolean isCONTRADICT(int val) {
        return (val == MCL_CONTRAD);
    }

    static boolean isUNKNOWN(int val) {
        return (val == MCL_UNKNOWN);
    }

    static boolean isPOSITIVE(int val) {
        return (val == MCL_POSITIV);
    }

    static boolean isNEGATIVE(int val) {
        return (val == MCL_NEGATIV);
    }


    static boolean isCore(int mclnValue) {
        return 0 < mclnValue && mclnValue <= MCL_CORE_MAX;
    }

    static boolean isValue(int val) {
        return !isCore(val);
    }

    // static int notValue( int val )
//  { return -val; }
 /*
    This function return an oposit positiv value
    calculated as a complement to the given one

    0  000 0000 0000 0000 0000 1111 0000 0000
    0  111 1111 1111 1111 1111 0000 1111 1111
 */
    static int getOppositeValue(int val) {
        return -val;
    }

    static boolean areValuesEqual(int val1, int val2) {
        return valuesAreEqual(val1, val2) || valuesAreOpposite(val1, val2);
    }

    static boolean valuesAreEqual(int val1, int val2) {
        return val1 == val2;
    }

    static boolean valuesAreOpposite(int val1, int val2) {
        return val1 == getOppositeValue(val2);
    }

//    static int mcDiscolor(int x1, int x2) {
//
//        if (isCore(x1) && isCore(x2)) {
//            return (mcDiscolTab[x1][x2]);
//        }
//
//        if (isCore(x1) || isCore(x2)) {
//            if ((x1 == MCL_NOTHING) || (x2 == MCL_NOTHING))
//                return MCL_NOTHING;
//            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
//                return MCL_CONTRAD;
//            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
//                return MCL_CONTRAD;
//            if ((x1 == MCL_POSITIV) || (x2 == MCL_POSITIV))
//                return MCL_CONTRAD;
//            if ((x1 == MCL_NEGATIV) || (x2 == MCL_NEGATIV))
//                return MCL_CONTRAD;
//
//            return MCL_CONTRAD;
//
//        } else if (Math.abs(x1) == Math.abs(x2)) {
//            return ((sign(x1) * sign(x2)) == 1) ? MCL_POSITIV : MCL_NEGATIV;
//        }
//
//        return MCL_CONTRAD;
//    }

    /**
     * @param x1
     * @param x2
     * @return
     */
    public static int discolorize(int x1, int x2) {

        if (isCore(x1) && isCore(x2)) {
//            System.out.println(" core "+x1 + "   "+x2+"   ");
            return (mcDiscolTab[x1 - 1][x2 - 1]);
        }

        if (isCore(x1) || isCore(x2)) {
            //  System.out.println(" one of core "+x1 + "   "+x2+"   ");
            if (isNOTHING(x1) || isNOTHING(x2)) {
                return (getNOTHING());
            } else if (isCONTRADICT(x1) || isCONTRADICT(x2)) {
                return (getCONTRADICT());
            } else if (isUNKNOWN(x1) || isUNKNOWN(x2)) {
                return (getCONTRADICT());
            } else if (isPOSITIVE(x1) || isPOSITIVE(x2)) {
                return (getCONTRADICT());
            } else if (isNEGATIVE(x1) || isNEGATIVE(x2)) {
                return (getCONTRADICT());
            } else {
                throw new RuntimeException("Unknown Core x1 or x2 value x1 = " + x1 + ", x2 = " + x2);
            }
        }

        if (areValuesEqual(x1, x2)) {
            int res = valuesAreEqual(x1, x2) ? getPOSITIVE() : getNEGATIVE();
//            System.out.println(" "+x1 + "   "+x2+"   "+res);
            return res;
        }

        // values are not same, like A and B
        return getCONTRADICT();
    }

//    static int mcAdd1(int x1, int x2) {
//        if (isCore(x1) && isCore(x2))
//            return (mcAdd1Tab[x1][x2]);
//        else if (isCore(x1) || isCore(x2)) {
//            if (x1 == MCL_NOTHING)
//                return (x2);
//            if (x2 == MCL_NOTHING)
//                return (x1);
//
//            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
//                return (MCL_CONTRAD);
//            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
//                return (MCL_CONTRAD);
//
//            if (x1 == MCL_POSITIV)
//                if (x2 > 0)
//                    return (MCL_POSITIV);
//                else
//                    return (MCL_CONTRAD);
//            if (x2 == MCL_POSITIV)
//                if (x1 > 0)
//                    return (MCL_POSITIV);
//                else
//                    return (MCL_CONTRAD);
//
//            if (x1 == MCL_NEGATIV)
//                if (x2 < 0)
//                    return (MCL_NEGATIV);
//                else
//                    return (MCL_CONTRAD);
//            if (x2 == MCL_NEGATIV)
//                if (x1 < 0)
//                    return (MCL_NEGATIV);
//                else
//                    return (MCL_CONTRAD);
//        } else if (Math.abs(x1) == Math.abs(x2)) {
//            if (x1 > 0 && x2 > 0)
//                return (MCL_POSITIV);
//            else if (x1 < 0 && x2 < 0)
//                return (MCL_NEGATIV);
//            else
//                return (MCL_CONTRAD);
//
//        } else
//            return (MCL_CONTRAD);
//
//        return (MCL_CONTRAD);
//    }

    public static int mclConjunction(int x1, int x2) {
        if (isCore(x1) && isCore(x2)) {
            return (mcAdd1Tab[x1 - 1][x2 - 1]);
        } else if (isCore(x1) || isCore(x2)) {
            if (isNOTHING(x1))
                return (x2);
            if (isNOTHING(x2))
                return (x1);

            if (isCONTRADICT(x1) || isCONTRADICT(x2))
                return (getCONTRADICT());
            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getCONTRADICT());
            if (isPOSITIVE(x1) || isPOSITIVE(x2))
                return (getCONTRADICT());
            if (isNEGATIVE(x1) || isNEGATIVE(x2))
                return (getCONTRADICT());

        } else if (valuesAreEqual(x1, x2)) { // System.out.println(" "+x1 + "   "+x2+"   ");
            return (x1);
        } else {
            return (getCONTRADICT());
        }
        return (getCONTRADICT());
    }


//    static int mcColoring(int x1, int x2) {
//        int res;
//        if (isCore(x1) && isCore(x2))
//            return (mcColorTab[x1][x2]);
//        else if (isCore(x1) || isCore(x2)) {
//            if ((x1 == MCL_NOTHING) || (x2 == MCL_NOTHING))
//                return (MCL_NOTHING);
//            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
//                return (MCL_CONTRAD);
//            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
//                return (MCL_UNKNOWN);
//
//            if (x1 == MCL_POSITIV)
//                return (x2);
//            if (x2 == MCL_POSITIV)
//                return (x1);
//
//            if (x1 == MCL_NEGATIV)
//                return (-x2);
//            if (x2 == MCL_NEGATIV)
//                return (-x1);
//        } else if (Math.abs(x1) == Math.abs(x2)) {
//            res = (((sign(x1) * sign(x2))
//                    == 1) ? x1 : -x1);
//            return (res);
//        } else
//            return (MCL_UNKNOWN);
//
//        return (MCL_UNKNOWN);
//    }

    public static int colorize(int x1, int x2) {
        if (isCore(x1) && isCore(x2)) {
//            System.out.println(" core " + x1 + "   " + x2 + "   ");
            return (mcColorTab[x1 - 1][x2 - 1]);
        }

        if (isCore(x1) || isCore(x2)) {
//            System.out.println(" one is core " + x1 + "   " + x2 + "   ");
            if (isNOTHING(x1) || isNOTHING(x2)) {
                return getNOTHING();
            } else if (isCONTRADICT(x1) || isCONTRADICT(x2)) {
                return getCONTRADICT();
            } else if (isUNKNOWN(x1) || isUNKNOWN(x2)) {
                return getUNKNOWN();
            }

            if (isPOSITIVE(x1)) {
                return x2;
            }
            if (isPOSITIVE(x2)) {
                return x1;
            }

            if (isNEGATIVE(x1)) {
                return getOppositeValue(x2);
            }
            if (isNEGATIVE(x2)) {
                return getOppositeValue(x1);
            }
        } else {
            throw new RuntimeException("Unknown Core x1 or x2 value x1 = " + x1 + ", x2 = " + x2);
        }


        return getUNKNOWN();
    }


//    static int mcAdd2(int x1, int x2) {
//        if (isCore(x1) && isCore(x2))
//            return (mcAdd2Tab[x1][x2]);
//        else if (isCore(x1) || isCore(x2)) {
//            if (x1 == MCL_NOTHING)
//                return (x2);
//            if (x2 == MCL_NOTHING)
//                return (x1);
//
//            if (x1 == MCL_CONTRAD)
//                return (x2);
//            if (x2 == MCL_CONTRAD)
//                return (x1);
//
//            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
//                return (MCL_UNKNOWN);
//
//            if (x1 == MCL_POSITIV)
//                if (x2 > 0)
//                    return (MCL_POSITIV);
//                else
//                    return (MCL_UNKNOWN);
//
//            if (x2 == MCL_POSITIV)
//                if (x1 > 0)
//                    return (MCL_POSITIV);
//                else
//                    return (MCL_UNKNOWN);
//
//            if (x1 == MCL_NEGATIV)
//                if (x2 < 0)
//                    return (MCL_NEGATIV);
//                else
//                    return (MCL_UNKNOWN);
//            if (x2 == MCL_NEGATIV)
//                if (x1 < 0)
//                    return (MCL_NEGATIV);
//                else
//                    return (MCL_UNKNOWN);
//        } else if (Math.abs(x1) == Math.abs(x2)) {
//            if (x1 > 0 && x2 > 0)
//                return (x1);
//            else if (x1 < 0 && x2 < 0)
//                return (x1);
//            else
//                return (MCL_UNKNOWN);
//
//        } else
//            return (MCL_UNKNOWN);
//        return (MCL_UNKNOWN);
//    }

    public static int mclnDisjunction(int x1, int x2) {
        if (isCore(x1) && isCore(x2)) {
            return (mcAdd2Tab[x1 - 1][x2 - 1]);
        }

        if (isCore(x1) || isCore(x2)) {
            if (isNOTHING(x1)) {
                return (x2);
            }
            if (isNOTHING(x2))
                return (x1);

            if (isCONTRADICT(x1))
                return (x2);
            if (isCONTRADICT(x2))
                return (x1);

            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getUNKNOWN());

            if (isPOSITIVE(x1) || isPOSITIVE(x2) ||
                    isNEGATIVE(x1) || isNEGATIVE(x1))
                return (getUNKNOWN());

        } else if (valuesAreEqual(x1, x2)) {
            return (x1);
        } else
            return (getUNKNOWN());
        return (getUNKNOWN());
    }

    static int rgbColorToMclValue(int state) {
        return isCore(state) ? state : (state << MCL_CORE_POWER);
    }

    static int mclValueToRgbColor(int value) {
        return isCore(value) ? value : (value >> MCL_CORE_POWER);
    }

    static void TextColorizing(int n) {
        char[] a = {'-', '0', 'U', 'T', 'F', 'A', 'B', 'C', 'D', 'E', 'G'};
        int[] v = {
                rgbColorToMclValue(6), getOppositeValue(rgbColorToMclValue(6)),
                rgbColorToMclValue(7), getOppositeValue(rgbColorToMclValue(7)),
                rgbColorToMclValue(8), getOppositeValue(rgbColorToMclValue(8))};
             /*
              int r = mclDiscolorizing(
      rgbColorToMclValue(v[0]), rgbColorToMclValue(v[1]) );
      */
        //     boolean r = isOposit(
        //     rgbColorToMclValue(v[0]), rgbColorToMclValue(v[1]) );
        //     System.out.print(v[1]+"   "+v[1]+"   "+r);
        //     int r = getOpositValue( getOpositValue( v[1] ) );
        //   System.out.print(v[1]+" "+r);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int k = (i < 5) ? i + 1 : v[i - 5];
                int l = (j < 5) ? j + 1 : v[j - 5];
//  System.out.println("i "+i+"/"+k);
//  System.out.println("j "+j+"/"+l);
                // k = 48; l = -48;

                // int r = mclDiscolorizing( k, l );
                //   int r = mclAdd1( k, l );
                // int r = mclColorizing( k, l );
                int r = mclnDisjunction(k, l);

                int r2, rr;
                if (r < MCL_CORE_MAX)
                    rr = r;
                else {
                    if (r > v[4])
                        r2 = getOppositeValue(r);
                    else
                        r2 = r;
                    rr = mclValueToRgbColor(r2);
                }
                char x;

                if (Math.abs(rr) <= 5)


                    x = a[rr - 1];
                else {
                    x = '!';
                    int ii = 5 + (rr - 6) * 2;
                    int jj = 5 + ((rr - 6) * 2) + 1;
                    x = (r > v[4]) ? a[jj] : a[ii];
//  System.out.println(" "+r+"   "+rr);

                    //   System.out.print(rr+"/"+ii+"  ");
                }

//   System.out.print(" "+x);
            }
// System.out.println();
        }
    }

}
