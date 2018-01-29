package mcln.palette;

import mcln.model.MclnAlgebra;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Admin on 9/30/2014.
 */
public class TwoShadesConfettiPalette implements MclnStatesNewPalette {

    static final String PALETTE_NAME = "Two Shades Confetti Palette";

    public static final int CREATION_STATE_ID = CreationStatePalette.CREATION_STATE_ID;

    private static final Color WHITE_COLOR = new Color(0xFFFFFF);
    public static final int WHITE_STATE_ID = WHITE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color BLACK_COLOR = new Color(0x000000);
    public  static final int BLACK_STATE_ID = BLACK_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GRAY_COLOR = new Color(0xCCCCCC);
    public    static final int GRAY_STATE_ID = GRAY_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_GRAY_COLOR = new Color(0xA5A5A5);
    public   static final int DARK_GRAY_STATE_ID = DARK_GRAY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color RED_COLOR = new Color(0xED1C24);
    public  static final int RED_STATE_ID = RED_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_RED_COLOR = new Color(0x800040);
    public  static final int DARK_RED_STATE_ID = DARK_RED_COLOR.getRGB() & 0xFFFFFF;

    private static final Color GREEN_COLOR = new Color(0x00FF00);
    public  static final int GREEN_STATE_ID = GREEN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_GREEN_COLOR = new Color(0x008040);
    public  static final int DARK_GREEN_STATE_ID = DARK_GREEN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BLUE_COLOR = new Color(0x0090FF);
    public   static final int BLUE_STATE_ID = BLUE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_BLUE_COLOR = new Color(0x0000FF);
    public   static final int DARK_BLUE_STATE_ID = DARK_BLUE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color MAGENTA_COLOR = new Color(0xFF00FF);
    public    static final int MAGENTA_STATE_ID = MAGENTA_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_MAGENTA_COLOR = new Color(0xCA00CA);
    public   static final int DARK_MAGENTA_STATE_ID = DARK_MAGENTA_COLOR.getRGB() & 0xFFFFFF;

    private static final Color PURPLE_COLOR = new Color(0xB66CFF);
    public   static final int PURPLE_STATE_ID = PURPLE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_PURPLE_COLOR = new Color(0x8000FF);
    public   static final int DARK_PURPLE_STATE_ID = DARK_PURPLE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color CYAN_COLOR = new Color(0x00FFFF);
    public     static final int CYAN_STATE_ID = CYAN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_CYAN_COLOR = new Color(0x0080C0);
    public   static final int DARK_CYAN_STATE_ID = DARK_CYAN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color YELLOW_COLOR = new Color(0xFFF200);
    public    static final int YELLOW_STATE_ID = YELLOW_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_YELLOW_COLOR = new Color(0x909000);
    public  static final int DARK_YELLOW_STATE_ID = DARK_YELLOW_COLOR.getRGB() & 0xFFFFFF;

    private static final Color BROWN_COLOR = new Color(0xce9b4e);
    public    static final int BROWN_STATE_ID = BROWN_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_BROWN_COLOR = new Color(0xB97A57);
    public   static final int DARK_BROWN_STATE_ID = DARK_BROWN_COLOR.getRGB() & 0xFFFFFF;

    private static final Color OLIVE_COLOR = new Color(0x61AFA7);
    public  static final int OLIVE_STATE_ID = OLIVE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_OLIVE_COLOR = new Color(0x408080);
    public   static final int DARK_OLIVE_STATE_ID = DARK_OLIVE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color PINK_COLOR = new Color(0xFFAEC9);
    public   static final int PINK_STATE_ID = PINK_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_PINK_COLOR = new Color(0x880015);
    public   static final int DARK_PINK_STATE_ID = DARK_PINK_COLOR.getRGB() & 0xFFFFFF;

    private static final Color ORANGE_COLOR = new Color(0xFF8000);
    public   static final int ORANGE_STATE_ID = ORANGE_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_ORANGE_COLOR = new Color(0xFF8000);
    public  static final int DARK_ORANGE_STATE_ID = DARK_ORANGE_COLOR.getRGB() & 0xFFFFFF;

    private static final Color CANARY_COLOR = new Color(0x96FE16);
    public    static final int CANARY_STATE_ID = CANARY_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_CANARY_COLOR = new Color(0xB5E61D);
    public    static final int DARK_CANARY_STATE_ID = DARK_CANARY_COLOR.getRGB() & 0xFFFFFF;

    private static final Color LILAC_COLOR = new Color(0xBF6FBF);
    public  static final int LILAC_STATE_ID = LILAC_COLOR.getRGB() & 0xFFFFFF;
    private static final Color DARK_LILAC_COLOR = new Color(0xB355B3);
    public  static final int DARK_LILAC_STATE_ID = DARK_LILAC_COLOR.getRGB() & 0xFFFFFF;

    private static final List<MclnState> paletteAsList = new ArrayList();
    private static final Map<Integer, MclnState> mclnStatesPalette = new LinkedHashMap();

    private static final int getState(List<MclnState> paletteAsLis) {
        return MclnAlgebra.MCL_VALUE_BASE + paletteAsList.size();
    }

    static {
        paletteAsList.add(MclnState.createState("Creation", getState(paletteAsList), CREATION_STATE_ID));

        paletteAsList.add(MclnState.createState("White", getState(paletteAsList), WHITE_STATE_ID));
        paletteAsList.add(MclnState.createState("Black", getState(paletteAsList), BLACK_STATE_ID));

        paletteAsList.add(MclnState.createState("Gray", getState(paletteAsList), GRAY_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Gray", getState(paletteAsList), DARK_GRAY_STATE_ID));


        paletteAsList.add(MclnState.createState("Red", getState(paletteAsList), RED_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Red", getState(paletteAsList), DARK_RED_STATE_ID));

        paletteAsList.add(MclnState.createState("Green", getState(paletteAsList), GREEN_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Green",
                getState(paletteAsList), DARK_GREEN_STATE_ID));

        paletteAsList.add(MclnState.createState("Blue", getState(paletteAsList), BLUE_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Blue",
                getState(paletteAsList), DARK_BLUE_STATE_ID));

        paletteAsList.add(MclnState.createState("Magenta", getState(paletteAsList), MAGENTA_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Magenta",
                getState(paletteAsList), DARK_MAGENTA_STATE_ID));

        paletteAsList.add(MclnState.createState("Purple", getState(paletteAsList), PURPLE_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Purple",
                getState(paletteAsList), DARK_PURPLE_STATE_ID));

        paletteAsList.add(MclnState.createState("Cyan", getState(paletteAsList), CYAN_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Cyan",
                getState(paletteAsList), DARK_CYAN_STATE_ID));

        paletteAsList.add(MclnState.createState("Yellow", getState(paletteAsList), YELLOW_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Yellow",
                getState(paletteAsList), DARK_YELLOW_STATE_ID));

        paletteAsList.add(MclnState.createState("Brown", getState(paletteAsList), BROWN_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Brown",
                getState(paletteAsList), DARK_BROWN_STATE_ID));

        paletteAsList.add(MclnState.createState("Olive", getState(paletteAsList), OLIVE_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Olive",
                getState(paletteAsList), DARK_OLIVE_STATE_ID));

        paletteAsList.add(MclnState.createState("Canary", getState(paletteAsList), CANARY_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Canary",
                getState(paletteAsList), DARK_CANARY_STATE_ID));

        paletteAsList.add(MclnState.createState("Lilac", getState(paletteAsList), LILAC_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Lilac",
                getState(paletteAsList), DARK_LILAC_STATE_ID));

        paletteAsList.add(MclnState.createState("Pink", getState(paletteAsList), PINK_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Pink",
                getState(paletteAsList), DARK_PINK_STATE_ID));

        paletteAsList.add(MclnState.createState("Orange", getState(paletteAsList), ORANGE_STATE_ID));
        paletteAsList.add(MclnState.createState("Dark Orange",
                getState(paletteAsList), DARK_ORANGE_STATE_ID));

        for (MclnState mclnState : paletteAsList) {
            mclnStatesPalette.put(mclnState.getStateID(), mclnState);
        }
    }

    static TwoShadesConfettiPalette twoShadesConfettiPalette = new TwoShadesConfettiPalette();

    public static TwoShadesConfettiPalette getInstance() {
        return twoShadesConfettiPalette;
    }


    //   I n s t a n c e

    private TwoShadesConfettiPalette() {

    }

    @Override
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
