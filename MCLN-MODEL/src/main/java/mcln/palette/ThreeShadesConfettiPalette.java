package mcln.palette;

import mcln.model.MclnAlgebra;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Admin on 10/2/2014.
 */
public class ThreeShadesConfettiPalette implements MclnStatesNewPalette {

    static final String PALETTE_NAME = "Three Shades Confetti Palette";

    public static final int CREATION_STATE_ID = CreationStatePalette.CREATION_STATE_ID;
    private static final Color WHITE_COLOR = new Color(0xFFFFFF);
    public static final int WHITE_STATE = WHITE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color BLACK_COLOR = new Color(0x000000);
    public static final int BLACK_STATE = BLACK_COLOR.getRGB() & 0xFFFFFF;

    private static final Color LIGHT_GRAY_COLOR = new Color(0xDDDDDD);
    public static final int LIGHT_GRAY_STATE = LIGHT_GRAY_COLOR.getRGB() & 0xFFFFFF;
    private static final Color GRAY_COLOR = new Color(0xCCCCCC);
    public static final int GRAY_STATE = GRAY_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_GRAY_COLOR = new Color(0x808080);
    public static final int DARK_GRAY_STATE = DARK_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color RED_COLOR = new Color(0xFF0000);
    public static final int RED_STATE = RED_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_RED_COLOR = new Color(0xCC0033);
    public static final int MID_RED_STATE = MID_RED_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_RED_COLOR = new Color(0x800040);
    public static final int DARK_RED_STATE = DARK_RED_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GREEN_COLOR = new Color(0x00FF00);
    public static final int GREEN_STATE = GREEN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_GREEN_COLOR = new Color(0x94D352);
    public static final int MID_GREEN_STATE = MID_GREEN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_GREEN_COLOR = new Color(0x008040);
    public static final int DARK_GREEN_STATE = DARK_GREEN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLUE_COLOR = new Color(0x0000FF);
    public static final int BLUE_STATE = BLUE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_BLUE_COLOR = new Color(0x0080FF);
    public static final int MID_BLUE_STATE = MID_BLUE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_BLUE_COLOR = new Color(0x0020BB);
    public static final int DARK_BLUE_STATE = DARK_BLUE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color MAGENTA_COLOR = new Color(0xFF00FF);
    public static final int MAGENTA_STATE = MAGENTA_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_MAGENTA_COLOR = new Color(0x9900FF);
    public static final int MID_MAGENTA_STATE = MID_MAGENTA_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_MAGENTA_COLOR = new Color(0x4000DD);
    public static final int DARK_MAGENTA_STATE = DARK_MAGENTA_COLOR.getRGB() & 0xFFFFFF;

    private static final Color CIAN_COLOR = new Color(0x00FFFF);
    public static final int CYAN_STATE = CIAN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_CYAN_COLOR = new Color(0x31A4B1);
    public static final int MID_CYAN_STATE = MID_CYAN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_CYAN_COLOR = new Color(0x2C5463);
    public static final int DARK_CYAN_STATE = DARK_CYAN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color YELLOW_COLOR = new Color(0xFFFF00);
    public static final int YELLOW_STATE = YELLOW_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_YELLOW_COLOR = new Color(0xFFC800);
    public static final int MID_YELLOW_STATE = MID_YELLOW_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_YELLOW_COLOR = new Color(0x4000DD);
    public static final int DARK_YELLOW_STATE = DARK_YELLOW_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BROWN_COLOR = new Color(0xdec79f);
    public static final int BROWN_STATE = BROWN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_BROWN_COLOR = new Color(0xce9b4e);
    public static final int MID_BROWN_STATE = MID_BROWN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_BROWN_COLOR = new Color(0xc27101);
    public static final int DARK_BROWN_STATE = DARK_BROWN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color SWAMP_COLOR = new Color(0xc8c8aa);
    public static final int SWAMP_STATE = SWAMP_COLOR.getRGB() & 0xFFFFFF;
    private static final Color MID_SWAMP_COLOR = new Color(0x999966);
    public static final int MID_SWAMP_STATE = MID_SWAMP_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_SWAMP_COLOR = new Color(0xe4e640);
    public static final int DARK_SWAMP_STATE = DARK_SWAMP_COLOR.getRGB() & 0xFFFFFF;

    private static final Color PINK_COLOR = new Color(0xFF7FBF);
    public static final int PINK_STATE = PINK_COLOR.getRGB() & 0xFFFFFF;

    private static final Color ORANGE_COLOR = new Color(0xFF9900);
    public static final int ORANGE_STATE = ORANGE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color CANARY_COLOR = new Color(0xBFFF00);
    public static final int CANARY_STATE = CANARY_COLOR.getRGB() & 0xFFFFFF;

    private static final List<MclnState> paletteAsList = new ArrayList();
    private static final Map<Integer, MclnState> mclnStatesPalette = new LinkedHashMap();
    private static final Map<String, MclnState> colorHexKeyToMclnStatesPalette = new LinkedHashMap();

   static {
        paletteAsList.add(MclnState.createState("Creation",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), CREATION_STATE_ID));

        paletteAsList.add(MclnState.createState("White",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), WHITE_STATE));
        paletteAsList.add(MclnState.createState("Black",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), BLACK_STATE));

        paletteAsList.add(MclnState.createState("Light Gray",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), LIGHT_GRAY_STATE));
        paletteAsList.add(MclnState.createState("Medium Gray",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), GRAY_STATE));
        paletteAsList.add(MclnState.createState("Dark Gray",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_GRAY_STATE));

        paletteAsList.add(MclnState.createState("Red",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), RED_STATE));
        paletteAsList.add(MclnState.createState("Medium Red",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_RED_STATE));
        paletteAsList.add(MclnState.createState("Dark Red",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_RED_STATE));

        paletteAsList.add(MclnState.createState("Green",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), GREEN_STATE));
        paletteAsList.add(MclnState.createState("Medium Green",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_GREEN_STATE));
        paletteAsList.add(MclnState.createState("Dark Green",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_GREEN_STATE));

        paletteAsList.add(MclnState.createState("Blue",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), BLUE_STATE));
        paletteAsList.add(MclnState.createState("Medium Blue",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_BLUE_STATE));
        paletteAsList.add(MclnState.createState("Dark Blue",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_BLUE_STATE));

        paletteAsList.add(MclnState.createState("Purple",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MAGENTA_STATE));
        paletteAsList.add(MclnState.createState("Medium Purple",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_MAGENTA_STATE));
        paletteAsList.add(MclnState.createState("Dark Purple",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_MAGENTA_STATE));

        paletteAsList.add(MclnState.createState("Cyan",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), CYAN_STATE));
        paletteAsList.add(MclnState.createState("Medium Cyan",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_CYAN_STATE));
        paletteAsList.add(MclnState.createState("Dark Cyan",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_CYAN_STATE));

        paletteAsList.add(MclnState.createState("Yellow",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), YELLOW_STATE));
        paletteAsList.add(MclnState.createState("Medium Yellow",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_YELLOW_STATE));
        paletteAsList.add(MclnState.createState("Dark Yellow",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_YELLOW_STATE));

        paletteAsList.add(MclnState.createState("Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), BROWN_STATE));
        paletteAsList.add(MclnState.createState("Medium Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_BROWN_STATE));
        paletteAsList.add(MclnState.createState("Dark Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_BROWN_STATE));

        paletteAsList.add(MclnState.createState("Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), SWAMP_STATE));
        paletteAsList.add(MclnState.createState("Medium Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MID_SWAMP_STATE));
        paletteAsList.add(MclnState.createState("Dark Brown",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_SWAMP_STATE));

        paletteAsList.add(MclnState.createState("Pink",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), PINK_STATE));
        paletteAsList.add(MclnState.createState("Orange",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), ORANGE_STATE));
        paletteAsList.add(MclnState.createState("Canary",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), CANARY_STATE));

        for (MclnState mclnState : paletteAsList) {
            mclnStatesPalette.put(mclnState.getStateID(), mclnState);
            colorHexKeyToMclnStatesPalette.put(mclnState.getHexColor(), mclnState);
        }
    }

    private static final ThreeShadesConfettiPalette THREE_STATES_PALETTE = new ThreeShadesConfettiPalette();

    public static ThreeShadesConfettiPalette getInstance() {
        return THREE_STATES_PALETTE;
    }


    //   I n s t a n c e

    private ThreeShadesConfettiPalette() {

    }

    public String getPaletteName() {
        return PALETTE_NAME;
    }

    @Override
    public boolean isPairsOfOppositeStatesPalette() {
        return false;
    }

    @Override
    public MclnState getMclnState(Integer stateValue) {
        return mclnStatesPalette.get(stateValue);
    }

    @Override
    public List<MclnState> getPaletteAsList() {
        return paletteAsList;
    }
}
