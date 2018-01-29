package dsdsse.app;

import mcln.algebra.LAlgebra;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/4/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnPalette {
    public static final int CREATE_STATE = 0xC0C0C0;      // gray
    // static final int CREATE_STATE = 0xFF0000;      // gray
    //static final int NOT_CREATE_STATE = 0x0F0F0F; // durkgray
    static final int mclnPaletteMax = 10 * 2;
    public static final int RED = 0xFF0000;
    public static final int NOT_RED = 0x00FFFF;
    public static final int GREEN = 0x00FF00;
    public static final int NOT_GREEN = 0xFF00FF;
    public static final int BLUE = 0x0000FF;
    public static final int NOT_BLUE = 0xFFFF00;
    public static final int YELLOW = NOT_BLUE;
    public static final int NOT_YELLOW = BLUE;
    public static final int MAGENTA = NOT_GREEN;
    public static final int NOT_MAGENTA = GREEN;
    public static final int CYAN = NOT_RED;
    public static final int NOT_CYAN = RED;
    public static final int PINK = 0xFFAAAA;
    public static final int NOT_PINK = 0x005555;

    public static final int WHITE = 0xFFFFFF;
    public static final int VERY_LIGHT_CIAN = 0xCCFFFF;
    public static final int VERY_LIGHT_BLUE = 0xCCCCFF;
    public static final int VERY_LIGHT_MAGENTA = 0xCCFFCC;
    public static final int VERY_LIGHT_RED = 0xFFCCCC;
    public static final int VERY_LIGHT_YELLOW = 0xFFFFCC;


    public static final int DARK_RED = 0xCC0000;
    public static final int DARK_CIAN = 0x00CCCC;
    public static final int DARK_GREEN = 0x00CC00;
    public static final int DARK_MAGENTA = 0xCC00CC;
    public static final int DARK_BLUE = 0x0000CC;
    public static final int DARK_YELLOW = 0xCCCC00;

    public static final int VERY_DARK_CIAN = 0x006666;
    public static final int VERY_DARK_BLUE1 = 0x003366;
    public static final int VERY_DARK_BLUE2 = 0x000066;
    public static final int VERY_DARK_BLUE3 = 0x330066;
    public static final int VERY_DARK_MAGENTA = 0x660066;
    public static final int VERY_DARK_RED1 = 0x660033;
    public static final int VERY_DARK_RED2 = 0x660000;
    public static final int VERY_DARK_RED3 = 0x663300;
    public static final int VERY_DARK_YELLOW = 0x666600;
    public static final int VERY_DARK_GREEN1 = 0x336600;
    public static final int VERY_DARK_GREEN2 = 0x006600;
    public static final int VERY_DARK_GREEN3 = 0x006633;

    static int[][] mclnPalette = new int[21][3];
    static String rgbsColorName[] = new String[21];


    private static Map<Color, Integer> defaultColorPalette = new HashMap();
    private static Map<Integer, Color> defaultStatePalette = new HashMap();

    private static Map<Color, Integer> activeColorPalette = new HashMap();
    private static Map<Integer, Color> activeStatePalette = new HashMap();


    //
    // init default MclnStatePalette
    //

    /**
     *
     */
    public static void initPalette() {
        int[][] RGBs = initRGBs();
        Map[] defaultMaps = createDefaultColorPalette(RGBs);
        defaultColorPalette = defaultMaps[0];
        defaultStatePalette = defaultMaps[1];
        activeColorPalette = defaultColorPalette;
        activeStatePalette = defaultStatePalette;
    }

    /**
     * @param RGBs
     * @return
     */
    private static Map[] createDefaultColorPalette(int[][] RGBs) {
        Map<Color, Integer> colorPalette = new HashMap();
        Map<Integer, Color> statePalette = new HashMap();
        int state = 11;
        for (int[] rgb : RGBs) {
            Color color = new Color(rgb[0], rgb[1], rgb[2]);
            colorPalette.put(color, state);
            statePalette.put(state, color);
            state++;
        }
        return new Map[]{colorPalette, statePalette};
    }

    public static Integer getActiveState(Color color) {
        return activeColorPalette.get(color);
    }

    public static Color getActiveColor(Integer state) {
        return activeStatePalette.get(state);
    }

    /*
        private int getrgbsInd( int ind )
        {
         int i;

         if ( Math.abs(ind) < RtsLAlgebra.MCL_CORE_RANGE )
          return(0);

         if ( ind < 0 )
         {
          ind += RtsLAlgebra.MCL_CORE_RANGE;
          ind = (-2)*ind + 1;
         }
         else
         {
          ind -= RtsLAlgebra.MCL_CORE_RANGE;
          ind = ind*2;  // positive
         }

         return(ind);
        }
        */
// ---------------------------------------------------------
    private int _getrgbsInd(int ind) {
        int i;
/*
 i = ind - ind/2;
 if (i != 0)
  ind = (ind - 1)*2;  // positive
 else
  ind = (ind - 1)*2 +1;
*/
        return (ind - 1);
    }

    // --------------------------------------------------------------
/*
public void initrgbs()
{
 int ind, i, j, k;
 double r = 255, g = 255; b = 255;

 rgbs = new double[21][3];
 int ind = 0;
 for ( int i = 160; i< 250; i +=10 )
 for ( int j = 160; j< 250; j +=10 )
 for ( int k = 160; k< 250; k +=10 )
 {
  int r = 160; r< 250; r +=10 )
  for ( int g = 160; g< 250; g +=10 )
  for ( int b = 160; b< 250; b +=10 )
  rgbs[ind][0] = double r;
  rgbs[ind][1] = double g;
  rgbs[ind][2] = double b;
  ind
 }
*/
// --------------------------------------------------------------
    public static int[][] initRGBs() {
        int[][] RGBs = new int[21][3];
        RGBs[0][0] = 180;
        RGBs[0][1] = 180;
        RGBs[0][2] = 180;
        rgbsColorName[0] = "Light Gray";

        RGBs[1][0] = 128;
        RGBs[1][1] = 128;
        RGBs[1][2] = 128;
        rgbsColorName[1] = "Not Light Gray";
// red  1
        RGBs[2][0] = 255;
        RGBs[2][1] = 0;
        RGBs[2][2] = 0;
        rgbsColorName[2] = "Red";

        RGBs[3][0] = 128;
        RGBs[3][1] = 0;
        RGBs[3][2] = 0;
        rgbsColorName[3] = "Not Red";
// green  2
        RGBs[4][0] = 0;
        RGBs[4][1] = 255;
        RGBs[4][2] = 0;
        rgbsColorName[4] = "Green";

        RGBs[5][0] = 0;
        RGBs[5][1] = 125;
        RGBs[5][2] = 0;
        rgbsColorName[5] = "Not Green";
// blue   3
        RGBs[6][0] = 0;
        RGBs[6][1] = 0;
        RGBs[6][2] = 255;
        rgbsColorName[6] = "Blue";

        RGBs[7][0] = 0;
        RGBs[7][1] = 0;
        RGBs[7][2] = 125;
        rgbsColorName[7] = "Not Blue";
//       4
        RGBs[8][0] = 255;
        RGBs[8][1] = 255;
        RGBs[8][2] = 0;
        rgbsColorName[8] = "Yellow";

        RGBs[9][0] = 125;
        RGBs[9][1] = 125;
        RGBs[9][2] = 0;
        rgbsColorName[9] = "Not Yellow";
//       5
        RGBs[10][0] = 255;
        RGBs[10][1] = 0;
        RGBs[10][2] = 255;
        rgbsColorName[10] = "Magenta";

        RGBs[11][0] = 125;
        RGBs[11][1] = 0;
        RGBs[11][2] = 125;
        rgbsColorName[11] = "Not Magenta";
        //       6
        RGBs[12][0] = 0;
        RGBs[12][1] = 255;
        RGBs[12][2] = 255;
        rgbsColorName[12] = "Cian";

        RGBs[13][0] = 0;
        RGBs[13][1] = 125;
        RGBs[13][2] = 125;
        rgbsColorName[13] = "Not Cian";
        //       7
        RGBs[14][0] = 255;
        RGBs[14][1] = 150;
        RGBs[14][2] = 150;
        rgbsColorName[14] = "Not Red";

        RGBs[15][0] = 160;
        RGBs[15][1] = 80;
        RGBs[15][2] = 80;
        rgbsColorName[15] = "Not Red";

        return RGBs;
    }

    // ----------------------------
/*
public Color getColor( int ind )
{
 // System.out.println( "getColor "+ ind );
 if ( ind == 0 )
  return(Color.lightGray);

 if ( Math.abs(ind) < RtsLAlgebra.MCL_CORE_RANGE )
  return(Color.black);

 if ( Math.abs(ind) > 15 )
 {
// System.out.println( "getColor > 15 "+ (new Color( ind )).getRGB() );
  return(new Color( ind ));
 }
  ind = getrgbsInd( ind );
  Color color = new Color( mclnPalette[ind][0], mclnPalette[ind][1],
                           mclnPalette[ind][2] );
  return( color );
}
*/
// ----------------------------
/*
public String stateToColorStr( int ind )
{
 if ( Math.abs(ind) < RtsLAlgebra.MCL_CORE_RANGE )
  return "Core Color";

  ind = getMCLNPaletteInd( ind );
  return mclnPaletteColorName[ind];
}
*/
    private static Color getContradColor() {
        return new Color(0xB0B0B0);
    }

    private static Color getPositiveColor() {
        return Color.white;
    }

    private static Color getNegativeColor() {
        return new Color(0x000000);
    }

    private static Color getUndefinedColor() {
        return new Color(0xB0B0B0);
    }

    public static Color RgbStateToColorClass(int rgbState) {
        if (LAlgebra.isCore(rgbState)) {
            if (LAlgebra.isCONTRAD(rgbState))
                return getContradColor();
            if (LAlgebra.isPOSITIV(rgbState))
                return getPositiveColor();
            if (LAlgebra.isNEGATIV(rgbState))
                return getNegativeColor();

            return getUndefinedColor();
        }

        return new Color(rgbState);
    }

    // ---  for Color Chooser
    public static int ColorClassToRgbState(Color col) {
        return col.getRGB() & 0xFFFFFF;
    }

/*
public int ColorToState( Color col )
{
 int state = col.getRGB();
 return( state & 0xFFFFFF );
}
*/
}

