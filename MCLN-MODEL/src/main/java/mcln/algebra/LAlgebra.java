package mcln.algebra;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/4/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class LAlgebra {
    /*
        -  "T"  //  MCL_POSITIVE
        -  "C"  //  MCL_CONTRAD
        -  "-"  //  MCL_NOTHING
        -  "U"  //  MCL_UNKNOWN
        -  "F"  //  MCL_NEGATIVE
    */

    private static final int RTS_PETRI_NET = -1;
    private static final int RTS_MODEL_NOT_DEFINED = 0;
    private static final int RTS_MULTI_COLOR_NET = 1;
    private static final int RTS_MONOCHROME_NET = 2;

    /* Molty Color Net variables */
    public static final int MCL_CORE_POWER = 4;
    public static final int MCL_CORE_MAX = ((int) Math.pow(2, MCL_CORE_POWER)) - 1;
    public static final int MCL_CORE_RANGE = 3;
    public static final int MCL_NOTHING = 1;
    public static final int MCL_CONTRAD = 2;
    public static final int MCL_UNKNOWN = 3;
    public static final int MCL_POSITIV = 4;
    public static final int MCL_NEGATIV = 5;
    public static final int NO_THRESHOLD = MCL_NOTHING;
    public static final int POSITIV_THRESHOLD = MCL_POSITIV;
    public static final int NEGATIV_THRESHOLD = MCL_NEGATIV;
/*
 static final int RGB_CORE_RANGE = 10;
 static final int RGB_CORE    =  0xFF;
 static final int RGB_NOTHING =  0x80;
 static final int RGB_CONTRAD =  0x81;
 static final int RGB_UNKNOWN =  0x82;
 static final int RGB_POSITIV =  0x83;
 static final int RGB_NEGATIV =  0x84;
*/

    private static final int mcDiscolTab[][] =
            {
//{MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
                    {MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
            };
    private static final int mcAdd1Tab[][] =
            {
//{MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
                    {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
                    {MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_UNKNOWN, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_POSITIV, MCL_CONTRAD, MCL_CONTRAD, MCL_POSITIV, MCL_CONTRAD},
                    {MCL_NEGATIV, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_NEGATIV},
            };
    private static final int mcColorTab[][] =
            {
//{MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
                    {MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING, MCL_NOTHING},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD, MCL_CONTRAD},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
                    {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_NEGATIV, MCL_POSITIV},
            };
    private static final int mcAdd2Tab[][] =
            {
//{MCL_NOTHING, MCL_CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
                    {MCL_NOTHING, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
                    {MCL_CONTRAD, MCL_CONTRAD, MCL_UNKNOWN, MCL_POSITIV, MCL_NEGATIV},
                    {MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN, MCL_UNKNOWN},
                    {MCL_POSITIV, MCL_POSITIV, MCL_UNKNOWN, MCL_POSITIV, MCL_UNKNOWN},
                    {MCL_NEGATIV, MCL_NEGATIV, MCL_UNKNOWN, MCL_UNKNOWN, MCL_NEGATIV},
            };
    // --------------------------------------------------------------------
    private static final int currentModel = RTS_MULTI_COLOR_NET;

    // ------------------------------
    public static boolean isColor(int state) {
        return (Math.abs(state) >= MCL_CORE_MAX);
    }

    // ------------------------------
    private static int sign(int x) {
        if (x == 0)
            return (x);
        else
            return (x / Math.abs(x));
    }

    // -------------------------------------------------
    public static String coreStateToStr(int state) {
        String stateStr;

        if (state == LAlgebra.MCL_NOTHING)
            stateStr = new String("NOTHING");
        else if (state == LAlgebra.MCL_CONTRAD)
            stateStr = new String("CONTRADICTION");
        else if (state == LAlgebra.MCL_UNKNOWN)
            stateStr = new String("UNKNOWN");
        else if (state == LAlgebra.MCL_POSITIV)
            stateStr = new String("POSITIVE");
        else if (state == LAlgebra.MCL_NEGATIV)
            stateStr = new String("NEGATIVE");
        else
            stateStr = new String("" + state);

        return (stateStr);
    }

    // ------------------------------
    public static void print(int x) {
        if (Math.abs(x) < MCL_CORE_MAX) {
            if (x == MCL_NOTHING)
                System.out.println("State is NOTHING");
            if (x == MCL_CONTRAD)
                System.out.println("State is CONTRADICTION");
            if (x == MCL_UNKNOWN)
                System.out.println("State is UNKNOWN");
            if (x == MCL_POSITIV)
                System.out.println("State is POSITIVE");
            if (x == MCL_NEGATIV)
                System.out.println("State is NEGATIVE");
        } else
            System.out.println("State is " + x);
    }

    // -------------------------------------------
    public static String int32toBinaryStr(int a) {
        String s = "";
        int mask = 1 << 31;
        for (int i = 0; i < 32; mask = mask >>> 1, i++)
            s = new String(s + (((a & mask) == mask) ? " 1" : " 0")
                    + ((((i + 1) % 4) == 0) ? "  " : ""));
        return s;
    }

    // -------------------------------------------
    public static int mcDiscoloring(int x1, int x2) {
        int res;
        if (isCore(x1) && isCore(x2))
            return (mcDiscolTab[x1][x2]);
        else if (isCore(x1) || isCore(x2)) {
            if ((x1 == MCL_NOTHING) || (x2 == MCL_NOTHING))
                return (MCL_NOTHING);
            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
                return (MCL_CONTRAD);
            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
                return (MCL_CONTRAD);
            if ((x1 == MCL_POSITIV) || (x2 == MCL_POSITIV))
                return (MCL_CONTRAD);
            if ((x1 == MCL_NEGATIV) || (x2 == MCL_NEGATIV))
                return (MCL_CONTRAD);
        } else if (Math.abs(x1) == Math.abs(x2)) {
            res = (((sign(x1) * sign(x2)) == 1)
                    ? MCL_POSITIV : MCL_NEGATIV);
            return (res);
        } else
            return (MCL_CONTRAD);

        return (MCL_CONTRAD);
    }

    // --------------------------------
    public static int mcAdd1(int x1, int x2) {
        if (isCore(x1) && isCore(x2))
            return (mcAdd1Tab[x1][x2]);
        else if (isCore(x1) || isCore(x2)) {
            if (x1 == MCL_NOTHING)
                return (x2);
            if (x2 == MCL_NOTHING)
                return (x1);

            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
                return (MCL_CONTRAD);
            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
                return (MCL_CONTRAD);

            if (x1 == MCL_POSITIV)
                if (x2 > 0)
                    return (MCL_POSITIV);
                else
                    return (MCL_CONTRAD);
            if (x2 == MCL_POSITIV)
                if (x1 > 0)
                    return (MCL_POSITIV);
                else
                    return (MCL_CONTRAD);

            if (x1 == MCL_NEGATIV)
                if (x2 < 0)
                    return (MCL_NEGATIV);
                else
                    return (MCL_CONTRAD);
            if (x2 == MCL_NEGATIV)
                if (x1 < 0)
                    return (MCL_NEGATIV);
                else
                    return (MCL_CONTRAD);
        } else if (Math.abs(x1) == Math.abs(x2)) {
            if (x1 > 0 && x2 > 0)
                return (MCL_POSITIV);
            else if (x1 < 0 && x2 < 0)
                return (MCL_NEGATIV);
            else
                return (MCL_CONTRAD);

        } else
            return (MCL_CONTRAD);

        return (MCL_CONTRAD);
    }

    // -------------------------------------------
    public static int mcColoring(int x1, int x2) {
        int res;
        if (isCore(x1) && isCore(x2))
            return (mcColorTab[x1][x2]);
        else if (isCore(x1) || isCore(x2)) {
            if ((x1 == MCL_NOTHING) || (x2 == MCL_NOTHING))
                return (MCL_NOTHING);
            if ((x1 == MCL_CONTRAD) || (x2 == MCL_CONTRAD))
                return (MCL_CONTRAD);
            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
                return (MCL_UNKNOWN);

            if (x1 == MCL_POSITIV)
                return (x2);
            if (x2 == MCL_POSITIV)
                return (x1);

            if (x1 == MCL_NEGATIV)
                return (-x2);
            if (x2 == MCL_NEGATIV)
                return (-x1);
        } else if (Math.abs(x1) == Math.abs(x2)) {
            res = (((sign(x1) * sign(x2))
                    == 1) ? x1 : -x1);
            return (res);
        } else
            return (MCL_UNKNOWN);

        return (MCL_UNKNOWN);
    }

    // --------------------------------
    public static int mcAdd2(int x1, int x2) {
        if (isCore(x1) && isCore(x2))
            return (mcAdd2Tab[x1][x2]);
        else if (isCore(x1) || isCore(x2)) {
            if (x1 == MCL_NOTHING)
                return (x2);
            if (x2 == MCL_NOTHING)
                return (x1);

            if (x1 == MCL_CONTRAD)
                return (x2);
            if (x2 == MCL_CONTRAD)
                return (x1);

            if ((x1 == MCL_UNKNOWN) || (x2 == MCL_UNKNOWN))
                return (MCL_UNKNOWN);

            if (x1 == MCL_POSITIV)
                if (x2 > 0)
                    return (MCL_POSITIV);
                else
                    return (MCL_UNKNOWN);

            if (x2 == MCL_POSITIV)
                if (x1 > 0)
                    return (MCL_POSITIV);
                else
                    return (MCL_UNKNOWN);

            if (x1 == MCL_NEGATIV)
                if (x2 < 0)
                    return (MCL_NEGATIV);
                else
                    return (MCL_UNKNOWN);
            if (x2 == MCL_NEGATIV)
                if (x1 < 0)
                    return (MCL_NEGATIV);
                else
                    return (MCL_UNKNOWN);
        } else if (Math.abs(x1) == Math.abs(x2)) {
            if (x1 > 0 && x2 > 0)
                return (x1);
            else if (x1 < 0 && x2 < 0)
                return (x1);
            else
                return (MCL_UNKNOWN);

        } else
            return (MCL_UNKNOWN);
        return (MCL_UNKNOWN);
    }

    // =====================================================
    public static int mpy1(int x1, int x2) {
        switch (currentModel) {
            case RTS_MULTI_COLOR_NET:
                return (mcDiscoloring(x1, x2));
            case RTS_MONOCHROME_NET:
                break;
            default:
                break;
        }
        return (MCL_NOTHING);
    }

    // ------------------------------------------
    public static int add1(int x1, int x2) {
        switch (currentModel) {
            case RTS_MULTI_COLOR_NET:
                return (mcAdd1(x1, x2));
            case RTS_MONOCHROME_NET:
                break;
            default:
                break;
        }
        return (MCL_NOTHING);
    }

    // ------------------------------------------
    public static int mpy2(int x1, int x2) {
        switch (currentModel) {
            case RTS_MULTI_COLOR_NET:
                return (mcColoring(x1, x2));
            case RTS_MONOCHROME_NET:
                break;
            default:
                break;
        }
        return (MCL_NOTHING);
    }

    // ------------------------------------------
    public static int add2(int x1, int x2) {
        switch (currentModel) {
            case RTS_MULTI_COLOR_NET:
                return (mcAdd2(x1, x2));
            case RTS_MONOCHROME_NET:
                break;
            default:
                break;
        }
        return (MCL_NOTHING);
    }

    public static void testMul1() {
        for (int i = 0; i < 17; i++)
            for (int j = 0; j < 17; j++) {

            }
    }

    // ------------------------------------------------------
 /*
 public static Color stateToColor( int i_state )
 {
  int colorInd[] = { 0, 0, 0 };
  int state, colorCnt = 0;
  Color color;
  boolean neg;



  if ( i_state < 0 )
  {
   neg = true;
   i_state = i_state * (-1);
  }
  else
   neg = false;

  if ( i_state < MCL_CORE_RANGE )
  {
    return(new Color( colorInd[0], colorInd[1],
                  colorInd[2] ));
  }

  state =  i_state - MCL_CORE_RANGE;

  if (colorCnt == state)
    return(new Color( colorInd[0], colorInd[1],
                  colorInd[2] ));

  for ( int i = 0; i < 5; i++ )
  {
   if ( i < 2 )
   {
    for ( int j = 0; j <= 1; j++ )
    {
      for ( int k = 0; k <= 1; k++ )
      {
       if (colorCnt == state)
       {
        if ( neg )
         color = new Color( 250 - k*250,
                            250 - j*250,
                            250 - i*250 );
        else
         color = new Color( k*250,
                             j*250,
                             i*250 );
        return(color);
       }
       colorCnt++;
      }
    }
   }
   else
   {
    for ( int j = 0; j <= 5; j++ )
    {
      for ( int k = 0; k <= 5; k++ )
      {
       if (colorCnt == state)
       {
        if ( neg )
         color = new Color( ( i-1 )*50,
                            250 - j*50,
                            250 - k*50 );
        else
         color = new Color( 250 - ( i-1 )*50,
                            j*50,
                            k*50 );
        return( color );
       }
       colorCnt++;
      }
    }
   }

  }
  return( new Color( colorInd[0], colorInd[1],
                     colorInd[2] ));
 }

*/
/*
 private static final int rgbDiscolTab[][] =
 {
 //{NOTHING, CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
 {RGB_NOTHING, RGB_NOTHING, RGB_NOTHING, RGB_NOTHING, RGB_NOTHING },
 {RGB_NOTHING, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
 {RGB_NOTHING, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
 {RGB_NOTHING, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
 {RGB_NOTHING, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
 };
private static final int rgbAdd1Tab[][] =
{
//{NOTHING, CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
{RGB_NOTHING, RGB_CONTRAD, RGB_UNKNOWN, RGB_POSITIV, RGB_NEGATIV },
{RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
{RGB_UNKNOWN, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
{RGB_POSITIV, RGB_CONTRAD, RGB_CONTRAD, RGB_POSITIV, RGB_CONTRAD },
{RGB_NEGATIV, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_NEGATIV },
};
private static final int rgbColorTab[][] =
{
//{NOTHING, CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
{RGB_NOTHING, RGB_NOTHING, RGB_NOTHING, RGB_NOTHING, RGB_NOTHING },
{RGB_NOTHING, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD, RGB_CONTRAD },
{RGB_NOTHING, RGB_CONTRAD, RGB_UNKNOWN, RGB_UNKNOWN, RGB_UNKNOWN },
{RGB_NOTHING, RGB_CONTRAD, RGB_UNKNOWN, RGB_POSITIV, RGB_NEGATIV },
{RGB_NOTHING, RGB_CONTRAD, RGB_UNKNOWN, RGB_NEGATIV, RGB_POSITIV },
};
private static final int rgbAdd2Tab[][] =
{
//{NOTHING, CONTRAD, UNKNOWN, POSITIVE, NEGATIVE },
{RGB_NOTHING, RGB_CONTRAD, RGB_UNKNOWN, RGB_POSITIV, RGB_NEGATIV },
{RGB_CONTRAD, RGB_CONTRAD, RGB_UNKNOWN, RGB_POSITIV, RGB_NEGATIV },
{RGB_UNKNOWN, RGB_UNKNOWN, RGB_UNKNOWN, RGB_UNKNOWN, RGB_UNKNOWN },
{RGB_POSITIV, RGB_POSITIV, RGB_UNKNOWN, RGB_POSITIV, RGB_UNKNOWN },
{RGB_NEGATIV, RGB_NEGATIV, RGB_UNKNOWN, RGB_UNKNOWN, RGB_NEGATIV },
};
*/
/*
     This functions represent four operations for
     simmetric oposit pairs model

*/
    public static int MAX_VAL = 0x0FFFFFFF;
    public static int VAL_MASK = 0x0FFFFFF0;

    public static int getNOTHING() {
        return MCL_NOTHING;
    }

    public static int getCONTRAD() {
        return MCL_CONTRAD;
    }

    public static int getUNKNOWN() {
        return MCL_UNKNOWN;
    }

    public static int getPOSITIV() {
        return MCL_POSITIV;
    }

    public static int getNEGATIV() {
        return MCL_NEGATIV;
    }


    public static boolean isCore(int val) {
        return (0 < val && val <= MCL_CORE_MAX);
    }

    public static boolean isValue(int val) {
        return !isCore(val);
    }

    // public static int notValue( int val )
//  { return -val; }
 /*
    This function return an oposit positiv value
    calculated as a complement to the diven one

    0  000 0000 0000 0000 0000 1111 0000 0000
    0  111 1111 1111 1111 1111 0000 1111 1111
 */
    public static int getOppositeValue(int val) {
        return ((val * (-1) - 1)) & VAL_MASK;
    }


    public static boolean isEqual(int val1, int val2) {
        return (val1 == val2);
    }

    public static boolean isOposit(int val1, int val2) {
//     System.out.println("isOposit "+val1 + "   "+val2+"   "+getOppositeValue( val2 ));
//     System.out.println(RtsLAlgebra.int32toBinaryStr( val1 ));
//     System.out.println(RtsLAlgebra.int32toBinaryStr( val2 ));
//     System.out.println(RtsLAlgebra.int32toBinaryStr( getOppositeValue( val2 ) ));
        return val1 == getOppositeValue(val2);
    }

    public static boolean isSameValue(int val1, int val2) {
        //  System.out.println(" "+val1 + "   "+val2+"   "+isEqual( val1, val2 ));
        //  System.out.println(" "+val1 + "   "+val2+"   "+isOposit( val1, val2 ));
        return isEqual(val1, val2) || isOposit(val1, val2);
    }

    /*
     public static boolean isEqual( int val1, int val2 )
      { return (val1 & 0xFFFFFF) == (val2 & 0xFFFFFF); }
     public static boolean isOposit( int val1, int val2 )
      { return (val1 & 0xFFFFFF) == notValue(val2); }
     public static boolean isSameValue( int val1, int val2 )
      { return isEqual( val1, val2 ) || isOposit( val1, val2 ); }
    */
    public static boolean isNOTHING(int val) {
        return (val == MCL_NOTHING);
    }

    public static boolean isCONTRAD(int val) {
        return (val == MCL_CONTRAD);
    }

    public static boolean isUNKNOWN(int val) {
        return (val == MCL_UNKNOWN);
    }

    public static boolean isPOSITIV(int val) {
        return (val == MCL_POSITIV);
    }

    public static boolean isNEGATIV(int val) {
        return (val == MCL_NEGATIV);
    }

    // -------------------------------------------
    public static int rgbColorToMclValue(int state) {
        return isCore(state) ? state : (state << MCL_CORE_POWER);
    }

    // -------------------------------------------
    public static int mclValueToRgbColor(int value) {
        return isCore(value) ? value : (value >> MCL_CORE_POWER);
    }

    // -------------------------------------------
    public static int mclDiscolorizing(int x1, int x2) {
        int res;
        if (isCore(x1) && isCore(x2)) {
// System.out.println(" core "+x1 + "   "+x2+"   ");
            return (mcDiscolTab[x1 - 1][x2 - 1]);
        } else if (isCore(x1) || isCore(x2)) {//  System.out.println(" one of core "+x1 + "   "+x2+"   ");
            if (isNOTHING(x1) || isNOTHING(x2))
                return (getNOTHING());
            if (isCONTRAD(x1) || isCONTRAD(x2))
                return (getCONTRAD());
            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getCONTRAD());
            if (isPOSITIV(x1) || isPOSITIV(x2))
                return (getCONTRAD());
            if (isNEGATIV(x1) || isNEGATIV(x2))
                return (getCONTRAD());
        } else if (isSameValue(x1, x2)) {
            res = isEqual(x1, x2) ? getPOSITIV() : getNEGATIV();
//   System.out.println(" "+x1 + "   "+x2+"   "+res);
            return (res);
        } else
            return (getCONTRAD());

        return (getCONTRAD());
    }

    // --------------------------------
    public static int mclAdd1(int x1, int x2) {
        if (isCore(x1) && isCore(x2))
            return (mcAdd1Tab[x1 - 1][x2 - 1]);
        else if (isCore(x1) || isCore(x2)) {
            if (isNOTHING(x1))
                return (x2);
            if (isNOTHING(x2))
                return (x1);

            if (isCONTRAD(x1) || isCONTRAD(x2))
                return (getCONTRAD());
            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getCONTRAD());
            if (isPOSITIV(x1) || isPOSITIV(x2))
                return (getCONTRAD());
            if (isNEGATIV(x1) || isNEGATIV(x2))
                return (getCONTRAD());

        } else if (isEqual(x1, x2)) { // System.out.println(" "+x1 + "   "+x2+"   ");
            return (x1);
        } else
            return (getCONTRAD());

        return (getCONTRAD());
    }

    // -------------------------------------------
    public static int mclColorizing(int x1, int x2) {
        int res;
        if (isCore(x1) && isCore(x2)) {
// System.out.println(" core "+x1 + "   "+x2+"   ");
            return (mcColorTab[x1 - 1][x2 - 1]);
        } else if (isCore(x1) || isCore(x2)) {
            if (isNOTHING(x1) || isNOTHING(x2))
                return (getNOTHING());
            if (isCONTRAD(x1) || isCONTRAD(x2))
                return (getCONTRAD());
            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getUNKNOWN());


            if (isPOSITIV(x1))
                return (x2);
            if (isPOSITIV(x2))
                return (x1);

            if (isNEGATIV(x1))
                return (getOppositeValue(x2));
            if (isNEGATIV(x2))
                return (getOppositeValue(x1));
        }

        return (getUNKNOWN());
    }

    // --------------------------------
    public static int mclAdd2(int x1, int x2) {
        if (isCore(x1) && isCore(x2))
            return (mcAdd2Tab[x1 - 1][x2 - 1]);
        else if (isCore(x1) || isCore(x2)) {
            if (isNOTHING(x1)) {
//   System.out.println("Add2  "+x2);
                return (x2);
            }
            if (isNOTHING(x2))
                return (x1);

            if (isCONTRAD(x1))
                return (x2);
            if (isCONTRAD(x2))
                return (x1);

            if (isUNKNOWN(x1) || isUNKNOWN(x2))
                return (getUNKNOWN());

            if (isPOSITIV(x1) || isPOSITIV(x2) ||
                    isNEGATIV(x1) || isNEGATIV(x1))
                return (getUNKNOWN());

        } else if (isEqual(x1, x2)) {
            return (x1);
        } else
            return (getUNKNOWN());
        return (getUNKNOWN());
    }

    public static void TextColorizing(int n) {
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
                int r = mclAdd2(k, l);

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
