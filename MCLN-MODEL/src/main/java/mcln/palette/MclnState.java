package mcln.palette;

import mcln.algebra.LAlgebra;
import mcln.model.MclnAlgebra;
import mcln.model.MclnStatementState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/14/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnState implements Cloneable {

    public static final int MCL_CORE_MAX = 10;

    public static final MclnMetaState EMPTY_STATE =
            MclnMetaState.createMetaState("NDEF", MclnAlgebra.MCL_NOTHING, MclnMetaState.CORE_NOTHING_COLOR);
    public static final MclnMetaState CORE_STATE_NOTHING =
            MclnMetaState.createMetaState("NOTH", MclnAlgebra.MCL_NOTHING, MclnMetaState.CORE_NOTHING_COLOR);
    public static final MclnMetaState CORE_STATE_POSITIVE =
            MclnMetaState.createMetaState("GOOD", MclnAlgebra.MCL_POSITIV, MclnMetaState.CORE_POSITIVE_COLOR);
    public static final MclnMetaState CORE_STATE_NEGATIVE =
            MclnMetaState.createMetaState("OPST", MclnAlgebra.MCL_NEGATIV, MclnMetaState.CORE_NEGATIVE_COLOR);
    public static final MclnMetaState CORE_STATE_CONTRADICTION =
            MclnMetaState.createMetaState("FAIL", MclnAlgebra.MCL_CONTRAD, MclnMetaState.CORE_CONTRADICT_COLOR);
    public static final MclnMetaState CORE_STATE_UNKNOWN =
            MclnMetaState.createMetaState("MANY", LAlgebra.MCL_UNKNOWN, MclnMetaState.CORE_UNKNOWN_COLOR);

    /**
     * @param strIndex
     * @return
     */
    public static int hexStringToInt(String strIndex) {
        int intValue = 0;
        try {
            intValue = Integer.decode(strIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intValue;
    }

    /**
     * @param otherState
     * @return
     */
    private static final MclnState mclnAlgebraCoreStateToMclnState(int otherState) {
        switch (otherState) {
            case 1:
                return CORE_STATE_NOTHING;
            case 2:
                return CORE_STATE_CONTRADICTION;
            case 3:
                return CORE_STATE_UNKNOWN;
            case 4:
                return CORE_STATE_POSITIVE;
            case 5:
                return CORE_STATE_NEGATIVE;
            default:
                throw new RuntimeException("Unknown other state " + otherState);
        }
    }

    /**
     * Called from model retriever
     *
     * @param name
     * @param state
     * @param hexColor
     * @return
     */
    public static MclnState createState(String name, int state, String hexColor) {
        return new MclnState(null, name, state, hexColor);
    }

    public static MclnState createState(String name, int state, int stateID) {
        return new MclnState(null, name, state, stateID);
    }

    public static MclnState createState(MclnState oppositeState, String name, int state, String color) {
        return new MclnState(oppositeState, name, state, color);
    }

    /**
     * Method is used to create other Mcln State for Pairs Of States Palette
     *
     * @param thisMclnState
     * @param oppositeMclnStateName
     * @param oppositeMclnStateID
     * @return
     */
    public static MclnState createOppositeMclnState(MclnState thisMclnState, String oppositeMclnStateName,
                                                    int oppositeMclnStateID) {
        return new MclnState(thisMclnState, oppositeMclnStateName, oppositeMclnStateID);
    }

    //
    // i n s t a n c e
    //

    private MclnState oppositeMclnState;

    private final String stateName;
    private final int state;
    private final int stateID;
    private final String hexColor;
    private final int oppositeState;

    protected MclnState() {
        stateName = "";
        state = 0;
        this.oppositeState = state;
        stateID = 0;
        hexColor = "0xFFFFFF";
    }

    /**
     * @param oppositeMclnState
     * @param colorName
     * @param state
     * @param hexColor
     */
    protected MclnState(MclnState oppositeMclnState, String colorName, int state, String hexColor) {
        this.oppositeMclnState = oppositeMclnState;
        this.stateName = colorName;
        this.state = state;
        this.oppositeState = -state;
        this.stateID = hexStringToInt(hexColor);
        this.hexColor = hexColor;
        if (oppositeMclnState != null) {
            oppositeMclnState.oppositeMclnState = this;
        }
    }

    protected MclnState(MclnState oppositeMclnState, String colorName, int state, int stateID) {
        this.oppositeMclnState = oppositeMclnState;
        this.stateName = colorName;
        this.state = state;
        this.oppositeState = -state;
        this.stateID = stateID;
        this.hexColor = String.format("0x%06X", (0xFFFFFF & stateID));
        if (oppositeMclnState != null) {
            oppositeMclnState.oppositeMclnState = this;
        }
    }

    /**
     * Constructs is used to create other Mcln State for Pairs Of States Palette
     *
     * @param oppositeMclnState
     * @param colorName
     * @param stateID
     */
    protected MclnState(MclnState oppositeMclnState, String colorName, int stateID) {
        assert oppositeMclnState != null : "Opposite Mcln State cannot be null";
        this.oppositeMclnState = oppositeMclnState;
        this.stateName = colorName;
        this.state = (-1) * oppositeMclnState.getState();
        this.oppositeState = -state;
        this.stateID = stateID;
        this.hexColor = String.format("0x%06X", (0xFFFFFF & stateID));
        if (oppositeMclnState != null) {
            oppositeMclnState.oppositeMclnState = this;
        }
    }

    public MclnState clone() {
        MclnState mclnState = null;
        try {
            mclnState = (MclnState) super.clone();
        } finally {
            return mclnState;
        }
    }

    public MclnPaletteState toMclnPaletteState() {
        MclnPaletteState mclnPaletteOppositeState = null;
        if (oppositeMclnState != null) {
            mclnPaletteOppositeState = MclnPaletteState.createState(oppositeMclnState.stateName,
                    oppositeMclnState.state, oppositeMclnState.hexColor);
        }
        return MclnPaletteState.createState(mclnPaletteOppositeState, stateName, state, hexColor);
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

    public String getColorName() {
        return stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateInterpretation() {
        return stateName;
    }

    // S t a t e   c h e c k e r s

    public boolean isStateNothing() {
        return this == CORE_STATE_NOTHING;
    }

    public boolean isStateContradiction() {
        return this == CORE_STATE_CONTRADICTION;
    }

    public boolean isStateUnknown() {
        return this == CORE_STATE_UNKNOWN;
    }

    public boolean isSituationRecognised() {
        return isStatePositive() || isStateNegative();
    }

    public boolean isStatePositive() {
        return this == CORE_STATE_POSITIVE;
    }

    public boolean isStateNegative() {
        return this == CORE_STATE_NEGATIVE;
    }

    public MclnState getOppositeMclnState() {
        return oppositeMclnState;
    }

    //
    //   Operations
    //

    public MclnState applyDiscolorOperation(MclnState mclnState) {
        MclnState newMclnState = null;
        try {
            int newState = MclnAlgebra.discolorize(getState(), mclnState.getState());
            newMclnState = mclnAlgebraCoreStateToMclnState(newState);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return newMclnState;
        }

    }

    public MclnState applyConjunctionOperation(MclnState mclnState) {
        MclnState newMclnState = null;
        try {
            int newState = MclnAlgebra.mclConjunction(getState(), mclnState.getState());
            newMclnState = mclnAlgebraCoreStateToMclnState(newState);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return newMclnState;
        }
    }

    public MclnState applyColorizeOperation(MclnState otherMclnState) {
//        System.out.println("applyColorizeOperation: thisMclnState = " + toString());
//        System.out.println("applyColorizeOperation: otherMclnState = " + otherMclnState.toString());
        int newState = MclnAlgebra.colorize(getState(), otherMclnState.getState());
//        System.out.println("applyColorizeOperation: newState = " + otherMclnState.toString());

        if (isSameState(newState)) {
            return this;
        }
        if (isOppositeState(newState)) {
            return getOppositeMclnState();
        }
        MclnState newMclnState = mclnAlgebraCoreStateToMclnState(newState);
        return newMclnState;
    }

    /**
     * @param otherMclnState
     * @return
     */
    public MclnState applyDisjunctionOperation(MclnState otherMclnState) {
//        System.out.println("applyDisjunctionOperation: thisMclnState = " + toString());
//        System.out.println("applyDisjunctionOperation: otherMclnState = " + otherMclnState.toString());
        int newState = MclnAlgebra.mclnDisjunction(getState(), otherMclnState.getState());
        if (isCore(newState)) {
            return mclnAlgebraCoreStateToMclnState(newState);
        }
        MclnState newMclnState = (!isCore()) ? this : otherMclnState;
        return newMclnState;
    }

    private boolean isCore() {
        int state = getState();
        return 0 < state && state <= MCL_CORE_MAX;
    }

    private boolean isCore(int state) {
        return 0 < state && state <= MCL_CORE_MAX;
    }

    private boolean isSameState(int otherState) {
        return getState() == otherState;
    }

    public boolean isOppositeState(int otherState) {
        return oppositeState == otherState;
    }

    public String toView() {
        return stateName + ", " + state + ", " + hexColor;
    }

    //
    //
    //

    public boolean equals(Object other) {

        if ((other instanceof MclnState)) {
            MclnState otherMclnState = (MclnState) other;
            return hexColor.equals(otherMclnState.hexColor);
        }

        if ((other instanceof MclnStatementState)) {
            MclnStatementState otherMclnStatementState = (MclnStatementState) other;
            return hexColor.equals(otherMclnStatementState.getHexColor());
        }

        return false;
    }

    public String toString() {
        return getClass().getSimpleName() +
                "  state name = " + stateName + ", color = " + hexColor + ",  state = " + state +
                ";\n    opposite state: " + (oppositeMclnState != null ?
                oppositeMclnState.stateName +
                        ", color = " + oppositeMclnState.hexColor +
                        ",  state = " + oppositeMclnState.state :
                "null" + "\n");
    }

    /**
     * @param stringBuilder
     * @param tag
     * @return
     */
    public StringBuilder toXml(StringBuilder stringBuilder, String tag) {
        stringBuilder.append("<").append(tag).append(">").append(getColorName()).append(", ").
                append(getState()).append(", ").append(getHexColor()).append("</").append(tag).append(">");
        return stringBuilder;
    }

}
