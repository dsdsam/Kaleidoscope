package mcln.palette;

import mcln.model.MclnAlgebra;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 4/1/14
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PairsOfOppositeStatesPalette implements MclnStatesNewPalette {

    static final String PALETTE_NAME = "Pairs of Opposite States Palette";

    public static final int CREATION_STATE_ID = CreationStatePalette.CREATION_STATE_ID;
    public static final int NOT_CREATION_STATE_ID = CreationStatePalette.NOT_CREATION_STATE_ID;

    private static final Color GRAY_COLOR = new Color(0x606060);
    public static final int GRAY_STATE = GRAY_COLOR.getRGB() & 0xFFFFFF;
    private static final Color NOT_GRAY_COLOR = new Color(0x9F9F9F);
    public static final int NOT_GRAY_STATE = NOT_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color RED_COLOR = new Color(0xFF0000);
    public static final int RED_STATE = RED_COLOR.getRGB() & 0xFFFFFF;
    private static final Color NOT_RED_COLOR = new Color(0x00FFFF);
    public static final int NOT_RED_STATE = NOT_RED_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GREEN_COLOR = new Color(0x00FF00);
    public static final int GREEN_STATE = GREEN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color NOT_GREEN_COLOR = new Color(0xFF00FF);
    public static final int NOT_GREEN_STATE = NOT_GREEN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLUE_COLOR = new Color(0x0000FF);
    public static final int BLUE_STATE = BLUE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color NOT_BLUE_COLOR = new Color(0xFFFF00);
    public static final int NOT_BLUE_STATE = NOT_BLUE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color PINK_COLOR = new Color(0xFF7FBF);
    public static final int PINK_STATE = PINK_COLOR.getRGB() & 0xFFFFFF;
    private static final Color NOT_PINK_COLOR = new Color(0x008040);
    public static final int NOT_PINK_STATE = NOT_PINK_COLOR.getRGB() & 0xFFFFFF;

    private static final List<MclnState> paletteAsList = new ArrayList();
    private static final Map<Integer, MclnState> mclnStatesPalette = new LinkedHashMap();

    private final static int paletteSizeToMclnState(List<MclnState> mclnStatesPaletteAsList) {
        int state = MclnAlgebra.MCL_VALUE_BASE + mclnStatesPaletteAsList.size() ;
        return state;
    }

    static {
        paletteAsList.add(MclnState.createState("Creation", paletteSizeToMclnState(paletteAsList), CREATION_STATE_ID));
        MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Creation", NOT_CREATION_STATE_ID);

        paletteAsList.add(MclnState.createState("Gray", paletteSizeToMclnState(paletteAsList), GRAY_STATE));
        MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Gray", NOT_GRAY_STATE);

        paletteAsList.add(MclnState.createState("Red", paletteSizeToMclnState(paletteAsList), RED_STATE));
         MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Red", NOT_RED_STATE);

        paletteAsList.add(MclnState.createState("Green", paletteSizeToMclnState(paletteAsList), GREEN_STATE));
         MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Green", NOT_GREEN_STATE);

        paletteAsList.add(MclnState.createState("Blue", paletteSizeToMclnState(paletteAsList), BLUE_STATE));
         MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Blue", NOT_BLUE_STATE);

        paletteAsList.add(MclnState.createState("Pink", paletteSizeToMclnState(paletteAsList), PINK_STATE));
         MclnState.createOppositeMclnState(paletteAsList.get(paletteAsList.size() - 1),
                "Opposite to Pink", NOT_PINK_STATE);

        for (MclnState mclnState : paletteAsList) {
            mclnStatesPalette.put(mclnState.getStateID(), mclnState);
        }
    }

    static PairsOfOppositeStatesPalette pairsOfOppositeStatesPalette = new PairsOfOppositeStatesPalette();

    public static PairsOfOppositeStatesPalette getInstance() {
        return pairsOfOppositeStatesPalette;
    }


    //   I n s t a n c e

    private PairsOfOppositeStatesPalette() {

    }

    @Override
    public String getPaletteName() {
        return PALETTE_NAME;
    }

    @Override
    public boolean isPairsOfOppositeStatesPalette() {
        return true;
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
