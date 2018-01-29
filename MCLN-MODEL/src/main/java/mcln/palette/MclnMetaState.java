package mcln.palette;

/**
 * Created by Admin on 10/28/2014.
 */
public final class MclnMetaState extends MclnState {

    public static MclnMetaState createMetaState(String name, int state, String color) {
        return new MclnMetaState(null, name, state, color);
    }


    static String CORE_NOTHING_COLOR = "0xC0C0C0";
    static String CORE_CONTRADICT_COLOR = "0xAAAAAA";
    static String CORE_UNKNOWN_COLOR = "0xC0C0C0";
    static String CORE_POSITIVE_COLOR = "0xFFFFFF";
    static String CORE_NEGATIVE_COLOR = "0xFFFFFF";

    //
    // i n s t a n c e
    //

    private MclnMetaState oppositeMclnMetaState;
    private final String colorName;
    private final int state;
    private final int color;
    private final String hexColor;


    private MclnMetaState(MclnMetaState oppositeMclnMetaState, String colorName, int state, String hexColor) {
        super(oppositeMclnMetaState, colorName, state, hexColor);
        this.oppositeMclnMetaState = oppositeMclnMetaState;
        this.colorName = colorName;
        this.state = state;
        this.color = hexStringToInt(hexColor);
        this.hexColor = hexColor;
        if (oppositeMclnMetaState != null) {
            oppositeMclnMetaState.oppositeMclnMetaState = this;
        }
    }

//    private MclnMetaState(MclnMetaState oppositeMclnMetaState, String colorName, int state, int color) {
//        super(oppositeMclnMetaState, colorName, state, color);
//        this.oppositeMclnMetaState = oppositeMclnMetaState;
//        this.colorName = colorName;
//        this.state = state;
//        this.color = color;
//        this.hexColor = String.format("0x%06X", (0xFFFFFF & color));
//        if (oppositeMclnMetaState != null) {
//            oppositeMclnMetaState.oppositeMclnMetaState = this;
//        }
//    }

    @Override
    public String getColorName() {
        return colorName;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public int getStateID() {
        return color;
    }

    @Override
    public String getHexColor() {
        return hexColor;
    }

    @Override
    public String getStateInterpretation() {
        return colorName;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "  color name = " + colorName + ", color = " + hexColor + ",  state = " + state +
                ";   opposite state: No opposite state" + "\n";
    }
}
