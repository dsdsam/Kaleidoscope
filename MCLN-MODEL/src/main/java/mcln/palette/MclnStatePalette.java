package mcln.palette;

import mcln.model.MclnAlgebra;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/14/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnStatePalette {

    String CORE_NOTHING_COLOR = "0xC0C0C0";
    String CORE_CONTRADICT_COLOR = "0xAAAAAA";
    String CORE_UNKNOWN_COLOR = "0xC0C0C0";
    String CORE_POSITIVE_COLOR = "0xFFFFFF";
    String CORE_NEGATIVE_COLOR = "0xFFFFFF";

    String CREATION = "0xC0C0C0";
    String NOT_CREATION = "0x0B0B0B";
    String WHITE = "0xFFFFFF";
    String BLACK = "0x000000";

    String GRAY = "0xCCCCCC";
    String NOT_GRAY = "0x333333";
    String MID_GRAY = "0xBBBBBB";
    String DARK_GRAY = "0xAAAAAA";

    String RED = "0xFF0000";
    String NOT_RED = "0x00FFFF";
    String MID_RED = "0xCC0033";
    String DARK_RED = "0x800040";

    String GREEN = "0x00FF00";
    String NOT_GREEN = "0xFF00FF";
    String MID_GREEN = "0x94D352";
    String DARK_GREEN = "0x008040";

    String BLUE = "0x0000FF";
    String NOT_BLUE = "0xFFFF00";
    String MID_BLUE = "0x0040FF";
    String DARK_BLUE = "0x0020BB";

    String PURPLE = "0xFF00FF";
    String NOT_PURPLE = GREEN;
    String MID_PURPLE = "0x9900FF";
    String DARK_PURPLE = "0x4000DD";

    String CYAN = "0x00FFFF";
    String NOT_CYAN = RED;
    String MID_CYAN = "0x31A4B1";
    String DARK_CYAN = "0x2C5463";

    String YELLOW = "0xFFFF00";
    String NOT_YELLOW = BLUE;
    String MID_YELLOW = "0x9900FF";
    String DARK_YELLOW = "0x4000DD";

    String BROWN = "0xdec79f";
    String MID_BROWN = "0xce9b4e";
    String DARK_BROWN = "0xc27101";

    String SWAMP = "0xc8c8aa";
    String MID_SWAMP = "0x999966";
    String DARK_SWAMP = "0xe4e640";

    String PINK = "0xFF7FBF";
    String NOT_PINK = "0x005555";
    String ORANGE = "0xFF9900";
    String CANARY = "0xBFFF00";

    int MCLN_MIN_STATE = MclnAlgebra.MCL_CORE_MAX + 1;

    MclnState MCLN_CREATION_STATE = MclnState.createState("Creation", MCLN_MIN_STATE, CREATION);
    MclnState MCLN_NOT_CREATION_STATE = MclnState.createState(MCLN_CREATION_STATE, "Not Creation", -MCLN_MIN_STATE,
            NOT_CREATION);
//
//    MclnState MCLN_CREATION_STATE = MclnState.createState("Creation", MCLN_MIN_STATE, CREATION);
//    MclnState MCLN_NOT_CREATION_STATE = MclnState.createState(MCLN_CREATION_STATE, "Not Creation", -MCLN_MIN_STATE, NOT_CREATION);
//
//    MclnState MCLN_STATE_GRAY = MclnState.createState("Gray", (MCLN_MIN_STATE + 1) , GRAY);
//    MclnState MCLN_STATE_NOT_GRAY = MclnState.createState(MCLN_STATE_GRAY, "Not Gray", -(MCLN_MIN_STATE + 1), NOT_GRAY);
//
//    MclnState MCLN_STATE_RED = MclnState.createState("Red", (MCLN_MIN_STATE + 2), RED);
//    MclnState MCLN_STATE_NOT_RED = MclnState.createState(MCLN_STATE_RED, "Not Red", -(MCLN_MIN_STATE + 2), NOT_RED);
//
//    MclnState MCLN_STATE_GREEN = MclnState.createState("Green", (MCLN_MIN_STATE + 3), GREEN);
//    MclnState MCLN_STATE_NOT_GREEN = MclnState.createState(MCLN_STATE_GREEN, "Not Green", -(MCLN_MIN_STATE + 3), NOT_GREEN);
//
//    MclnState MCLN_STATE_BLUE = MclnState.createState("Blue", (MCLN_MIN_STATE + 4), BLUE);
//    MclnState MCLN_STATE_NOT_BLUE = MclnState.createState(MCLN_STATE_BLUE, "Not Blue", -(MCLN_MIN_STATE + 4), NOT_BLUE);
//
//    MclnState MCLN_STATE_DARK_BLUE = MclnState.createState("Dark blue", (MCLN_MIN_STATE + 5), DARK_BLUE);
//    MclnState MCLN_STATE_NOT_DARK_BLUE = MclnState.createState(MCLN_STATE_DARK_BLUE, "Not Dark blue", -(MCLN_MIN_STATE + 5), NOT_DARK_BLUE);
//
//    MclnState MCLN_STATE_PINK = MclnState.createState("Pink", (MCLN_MIN_STATE + 6), PINK);
//    MclnState MCLN_STATE_NOT_PINK = MclnState.createState(MCLN_STATE_PINK, "Not Pink", -(MCLN_MIN_STATE + 6), NOT_PINK);
//
//    MclnState MCLN_STATE_DARK_GREEN = MclnState.createState("Dark Brown", (MCLN_MIN_STATE + 7), DARK_GREEN);

    public List<MclnState> getAvailableStates();

    /**
     * @param color
     * @return
     */
    public MclnState getState(String color);

//       String CREATION_COLOR = "0xC0C0C0";
//      MclnState CREATION_MCLN_STATE = MclnState.createState("Creation State", 0, CREATION_COLOR);

//      int mclnPaletteMax = 10 * 2;
//       int RED = 0xFF0000;
//       int NOT_RED = 0x00FFFF;
//       int GREEN = 0x00FF00;
//       int NOT_GREEN = 0xFF00FF;
//       int BLUE = 0x0000FF;
//       int NOT_BLUE = 0xFFFF00;
//       int YELLOW = NOT_BLUE;
//       int NOT_YELLOW = BLUE;
//       int MAGENTA = NOT_GREEN;
//       int NOT_MAGENTA = GREEN;
//       int CYAN = NOT_RED;
//       int NOT_CYAN = RED;
//       int PINK = 0xFFAAAA;
//       int NOT_PINK = 0x005555;
//
//       int WHITE = 0xFFFFFF;
//       int VERY_LIGHT_CIAN = 0xCCFFFF;
//       int VERY_LIGHT_BLUE = 0xCCCCFF;
//       int VERY_LIGHT_MAGENTA = 0xCCFFCC;
//       int VERY_LIGHT_RED = 0xFFCCCC;
//       int VERY_LIGHT_YELLOW = 0xFFFFCC;
//
//
//       int DARK_RED = 0xCC0000;
//       int DARK_CIAN = 0x00CCCC;
//       int DARK_GREEN = 0x00CC00;
//       int DARK_MAGENTA = 0xCC00CC;
//       int DARK_BLUE = 0x0000CC;
//       int DARK_YELLOW = 0xCCCC00;
//
//       int VERY_DARK_CIAN = 0x006666;
//       int VERY_DARK_BLUE1 = 0x003366;
//       int VERY_DARK_BLUE2 = 0x000066;
//       int VERY_DARK_BLUE3 = 0x330066;
//       int VERY_DARK_MAGENTA = 0x660066;
//       int VERY_DARK_RED1 = 0x660033;
//       int VERY_DARK_RED2 = 0x660000;
//       int VERY_DARK_RED3 = 0x663300;
//       int VERY_DARK_YELLOW = 0x666600;
//       int VERY_DARK_GREEN1 = 0x336600;
//       int VERY_DARK_GREEN2 = 0x006600;
//       int VERY_DARK_GREEN3 = 0x006633;
//
//
//
//     MclnState getState(Integer color);
//
////      Integer getActiveState(Color color) {
////        return activeColorPalette.get(color);
////    }
////
////      Color getActiveColor(Integer state) {
////        return activeStatePalette.get(state);
////    }

}
