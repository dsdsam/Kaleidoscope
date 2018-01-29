package mcln.palettes.confetti;

import mcln.palette.MclnPaletteState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 10/30/2014.
 */
public class MclnStatesConfettiPalette extends ConfettiPaletteColorDefinitions implements ConfettiStatePaletteKeys {

    private static final String OPPOSITE_PREFIX = "Not ";

    private static final String[][] statesSpec = {
            {MCLN_STATE_CREATION, String.valueOf(MCLN_STATE_CREATION_COLOR_DEF)},
            {MCLN_STATE_WHITE, String.valueOf(MCLN_STATE_WHITE_COLOR_DEF)},
            {MCLN_STATE_BLACK, String.valueOf(MCLN_STATE_BLACK_COLOR_DEF)},

            {MCLN_STATE_GRAY, String.valueOf(MCLN_STATE_GRAY_COLOR_DEF)},
            {MCLN_STATE_DARK_GRAY, String.valueOf(MCLN_STATE_DARK_GRAY_COLOR_DEF)},

            {MCLN_STATE_RED, String.valueOf(MCLN_STATE_RED_COLOR_DEF)},
            {MCLN_STATE_DARK_RED, String.valueOf(MCLN_STATE_DARK_RED_COLOR_DEF)},

            {MCLN_STATE_GREEN, String.valueOf(MCLN_STATE_GREEN_COLOR_DEF)},
            {MCLN_STATE_DARK_GREEN, String.valueOf(MCLN_STATE_DARK_GREEN_COLOR_DEF)},

            {MCLN_STATE_BLUE, String.valueOf(MCLN_STATE_BLUE_COLOR_DEF)},
            {MCLN_STATE_DARK_BLUE, String.valueOf(MCLN_STATE_DARK_BLUE_COLOR_DEF)},

            {MCLN_STATE_MAGENTA, String.valueOf(MCLN_STATE_MAGENTA_COLOR_DEF)},
            {MCLN_STATE_DARK_MAGENTA, String.valueOf(MCLN_STATE_DARK_MAGENTA_COLOR_DEF)},
    };

    private static final Map<String, MclnPaletteState> mclnStatementStatesPalette = new HashMap();

    static {
        for (int i = 0; i < statesSpec.length; i++) {
            String colorName = statesSpec[i][0];
            String hexColor = statesSpec[i][1];
            MclnPaletteState paletteState = MclnPaletteState.createState(null, colorName, i, hexColor);
            mclnStatementStatesPalette.put(colorName, paletteState);

            String oppositeColorName = OPPOSITE_PREFIX + colorName;
            String oppositeHexColor = hexColor;
            MclnPaletteState oppositePaletteState = MclnPaletteState.createState(paletteState, oppositeColorName, -i,
                    oppositeHexColor);
            mclnStatementStatesPalette.put(oppositeColorName, oppositePaletteState);
        }
    }

    public static final MclnPaletteState getState(String colorName) {
        return mclnStatementStatesPalette.get(colorName);
    }
}
