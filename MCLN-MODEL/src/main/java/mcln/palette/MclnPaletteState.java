package mcln.palette;

/**
 * Created by Admin on 10/28/2014.
 */
public class MclnPaletteState {

    public static final MclnPaletteState createState(String colorName, int state, String hexColor) {
        return new MclnPaletteState(null, colorName, state, hexColor);
    }

    public static final MclnPaletteState createState(MclnPaletteState oppositeState, String colorName, int state,
                                                     String hexColor) {
        return new MclnPaletteState(oppositeState, colorName, state, hexColor);
    }


    /**
     * @param strIndex
     * @return
     */
    private static int stringToInt(String strIndex) {
        int intValue = 0;
        try {
            intValue = Integer.decode(strIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intValue;
    }

    //
    // i n s t a n c e
    //

    private MclnPaletteState oppositeMclnPaletteState;
    private final String colorName;
    private final int state;
    private final int oppositeState;
    private final int stateID;
    private final String hexColor;


    private MclnPaletteState(MclnPaletteState oppositeMclnPaletteState, String colorName, int state, String hexColor) {
        this.oppositeMclnPaletteState = oppositeMclnPaletteState;
        this.colorName = colorName;
        this.state = state;
        this.oppositeState = state * (-1);
        this.stateID = stringToInt(hexColor);
        this.hexColor = hexColor;
        if (oppositeMclnPaletteState != null) {
            oppositeMclnPaletteState.oppositeMclnPaletteState = this;
        }
    }

    public boolean isOppositeState(int otherState) {
        return oppositeState == otherState;
    }

    public String getColorName() {
        return colorName;
    }

    public int getStateID() {
        return stateID;
    }

    public int getState() {
        return state;
    }

    public int getRGB() {
        return stateID;
    }

    public String getHexColor() {
        return hexColor;
    }

    public String toView() {
        return colorName + ", " + state + ", " + hexColor;
    }

    public String toString() {
        return getClass().getSimpleName() +
                "  color name = " + colorName +
                ", color = " + hexColor +
                ",  state = " + state +
                ";    opposite state: " + (oppositeMclnPaletteState != null ?
                oppositeMclnPaletteState.colorName +
                        ", color = " + oppositeMclnPaletteState.hexColor +
                        ",  state = " + oppositeMclnPaletteState.state :
                "null");
    }

}
