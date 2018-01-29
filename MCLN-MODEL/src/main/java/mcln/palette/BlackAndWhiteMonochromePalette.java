package mcln.palette;

import mcln.model.MclnAlgebra;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Admin on 5/22/2016.
 */
class BlackAndWhiteMonochromePalette implements MclnStatesNewPalette {

    static final String PALETTE_NAME = "Black and White Monochrome Palette";

    private static final Color CREATION_COLOR = new Color(0xD0D0D0); //  it is here to have all colors in one place
    public static final int CREATION_STATE_ID = CreationStatePalette.CREATION_STATE_ID;

    private static final Color WHITE_COLOR = new Color(0xFFFFFF);
    public static final int WHITE_STATE_KEY = WHITE_COLOR.getRGB() & 0xFFFFFF;


    private static final Color JAVA_LIGHT_GRAY_COLOR = Color.LIGHT_GRAY; // 0xC0C0C0 == 192,192,192
    private static final Color LIGHT_GRAY_COLOR = new Color(0xB0B0B0);   // 0xB0B0B0 == 176,176,176
    public static final int LIGHT_GRAY_STATE_KEY = LIGHT_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color JAVA_GRAY_COLOR = Color.GRAY;             // 0x808080 == 128,128,128
    private static final Color GRAY_COLOR = new Color(0x909090);         // 0x909090 == 144,144,144
    public static final int GRAY_STATE_KEY = GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color JAVA_DARK_GRAY_COLOR = Color.DARK_GRAY;     // 0x404040 == 64,64,64
    private static final Color DARK_GRAY_COLOR = new Color(0x707070);      // 0x707070 == 112,112,112
    public static final int DARK_GRAY_STATE_KEY = DARK_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLACK_COLOR = new Color(0x000000);
    public static final int BLACK_STATE_KEY = BLACK_COLOR.getRGB() & 0xFFFFFF;

    private static final java.util.List<MclnState> paletteAsList = new ArrayList();
    private static final Map<Integer, MclnState> mclnStatesPalette = new LinkedHashMap();

    static {
        paletteAsList.add(MclnState.createState("Creation",
                paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, CREATION_STATE_ID));

        paletteAsList.add(MclnState.createState("White",
                paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, WHITE_STATE_KEY));

        paletteAsList.add(MclnState.createState("Black"
                , paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, LIGHT_GRAY_STATE_KEY));

        paletteAsList.add(MclnState.createState("Gray",
                paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, GRAY_STATE_KEY));

        paletteAsList.add(MclnState.createState("Dark Gray",
                paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, DARK_GRAY_STATE_KEY));

        paletteAsList.add(MclnState.createState("Black",
                paletteAsList.size() + MclnAlgebra.MCL_CORE_MAX + 1, BLACK_STATE_KEY));

        for (MclnState mclnState : paletteAsList) {
            mclnStatesPalette.put(mclnState.getStateID(), mclnState);
        }
    }

    private static BlackAndWhiteMonochromePalette blackAndWhiteMonochromePalette = new BlackAndWhiteMonochromePalette();

    public static BlackAndWhiteMonochromePalette getInstance() {
        return blackAndWhiteMonochromePalette;
    }


    //   I n s t a n c e

    private BlackAndWhiteMonochromePalette() {

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
