package mcln.palette;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/14/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnPaletteFactory {

    public static final String BLACK_AND_WHITE_MONOCHROME_PALETTE = BlackAndWhiteMonochromePalette.PALETTE_NAME;
    public static final String BASIC_COLORS_PALETTE = BasicColorPalette.PALETTE_NAME;
    public static final String THREE_SHADES_CONFETTI_PALETTE = ThreeShadesConfettiPalette.PALETTE_NAME;
    public static final String TWO_SHADES_CONFETTI_PALETTE = TwoShadesConfettiPalette.PALETTE_NAME;
    public static final String PAIRS_OF_OPPOSITE_STATES_PALETTE = PairsOfOppositeStatesPalette.PALETTE_NAME;

    //   P a l e t t e s

    private static BlackAndWhiteMonochromePalette blackAndWhiteMonochromePalette =
            BlackAndWhiteMonochromePalette.getInstance();

    private static BasicColorPalette basicColorPalette = BasicColorPalette.getInstance();

    private static ThreeShadesConfettiPalette threeShadesConfettiPalette = ThreeShadesConfettiPalette.getInstance();

    private static TwoShadesConfettiPalette twoShadesConfettiPalette = TwoShadesConfettiPalette.getInstance();

    private static PairsOfOppositeStatesPalette pairsOfOppositeStatesPalette =
            PairsOfOppositeStatesPalette.getInstance();

    // Palettes Map
    private static final Map<String, MclnStatesNewPalette> mclnStatesPaletteNameToPalette = new LinkedHashMap<>();

    static {
        mclnStatesPaletteNameToPalette.put(BLACK_AND_WHITE_MONOCHROME_PALETTE, blackAndWhiteMonochromePalette);
        mclnStatesPaletteNameToPalette.put(BASIC_COLORS_PALETTE, basicColorPalette);
        mclnStatesPaletteNameToPalette.put(TWO_SHADES_CONFETTI_PALETTE, twoShadesConfettiPalette);
        mclnStatesPaletteNameToPalette.put(THREE_SHADES_CONFETTI_PALETTE, threeShadesConfettiPalette);
        mclnStatesPaletteNameToPalette.put(PAIRS_OF_OPPOSITE_STATES_PALETTE, pairsOfOppositeStatesPalette);
    }


    //
    //   G e t t e r s
    //

    public static final MclnStatesNewPalette getPaletteByName(String mclnStatesPaletteName) {
        return mclnStatesPaletteNameToPalette.get(mclnStatesPaletteName);
    }

}
