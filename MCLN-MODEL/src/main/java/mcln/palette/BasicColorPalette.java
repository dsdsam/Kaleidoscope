package mcln.palette;

import mcln.model.MclnAlgebra;

import java.awt.*;
import java.util.*;

/**
 * Created by Admin on 5/22/2016.
 */
public class BasicColorPalette implements MclnStatesNewPalette {

    public static final String PALETTE_NAME = "Basic Colors Palette";

    public static final int CREATION_STATE_ID = CreationStatePalette.CREATION_STATE_ID;

    private static final Color WHITE_COLOR = new Color(0xFFFFFF);
    public static final int WHITE_STATE_ID = WHITE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color LIGHT_GRAY_COLOR = new Color(0xB0B0B0);
    public static final int LIGHT_GRAY_STATE_ID = LIGHT_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GRAY_COLOR = new Color(0x909090);
    public static final int GRAY_STATE_ID = GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color DARK_GRAY_COLOR = new Color(0x707070);
    public static final int DARK_GRAY_STATE_ID = DARK_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLACK_COLOR = new Color(0x000000);
    public static final int BLACK_STATE_ID = BLACK_COLOR.getRGB() & 0xFFFFFF;

    private static final Color RED_COLOR = new Color(0xFF0000);
    public static final int RED_STATE_ID = RED_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GREEN_COLOR = new Color(0x00D000);
    public static final int GREEN_STATE_ID = GREEN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLUE_COLOR = new Color(0x0000FF);
    public static final int BLUE_STATE_ID = BLUE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color CIAN_COLOR = new Color(0x00FFFF);
    public static final int CYAN_STATE_ID = CIAN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color MAGENTA_COLOR = new Color(0xFF00FF);
    public static final int MAGENTA_STATE_ID = MAGENTA_COLOR.getRGB() & 0xFFFFFF;

    private static final Color YELLOW_COLOR = new Color(0xFFFF00);
    public static final int YELLOW_STATE_ID = YELLOW_COLOR.getRGB() & 0xFFFFFF;

    private static final java.util.List<MclnState> paletteAsList = new ArrayList();
    private static final Map<Integer, MclnState> mclnStatesPalette = new LinkedHashMap();

    static {
        paletteAsList.add(MclnState.createState("Creation",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), CREATION_STATE_ID));

        paletteAsList.add(MclnState.createState("White",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), WHITE_STATE_ID));

        paletteAsList.add(MclnState.createState("Black",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), LIGHT_GRAY_STATE_ID));

        paletteAsList.add(MclnState.createState("Gray",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), GRAY_STATE_ID));

        paletteAsList.add(MclnState.createState("Dark Gray",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), DARK_GRAY_STATE_ID));

        paletteAsList.add(MclnState.createState("Black",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), BLACK_STATE_ID));


        paletteAsList.add(MclnState.createState("Red",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), RED_STATE_ID));

        paletteAsList.add(MclnState.createState("Green",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), GREEN_STATE_ID));

        paletteAsList.add(MclnState.createState("Blue",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), BLUE_STATE_ID));

        paletteAsList.add(MclnState.createState("Cyan",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), CYAN_STATE_ID));

        paletteAsList.add(MclnState.createState("Magenta",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), MAGENTA_STATE_ID));

        paletteAsList.add(MclnState.createState("Yellow",
                MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size(), YELLOW_STATE_ID));

        for (MclnState mclnState : paletteAsList) {
            mclnStatesPalette.put(mclnState.getStateID(), mclnState);
        }
    }

    private static BasicColorPalette basicColorPalette = new BasicColorPalette();

    public static BasicColorPalette getInstance() {
        return basicColorPalette;
    }


    //   I n s t a n c e

    private BasicColorPalette() {

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
    public java.util.List<MclnState> getPaletteAsList() {
        return paletteAsList;
    }
}
