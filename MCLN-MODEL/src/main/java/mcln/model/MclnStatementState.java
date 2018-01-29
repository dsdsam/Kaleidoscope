package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/18/14
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnStatementState implements Cloneable {

    public static final String MCLN_STATEMENT_STATE_TAG = "Mcln-Statement-State";
    public static final String MCLN_STATE_TAG = "Mcln-State";
    public static final String INTERPRETATION_TAG = "Interpretation";

    /**
     * Called from:
     * 1) Mcln Model Retriever
     * 2) AvailableMclnStatementStates
     *
     * @param mclnState
     * @param interpretation
     * @return
     */
    public static MclnStatementState createMclnStatementState(MclnState mclnState, String interpretation) {
        return new MclnStatementState(mclnState, null, interpretation);
    }

    /**
     * Called from:
     * 1) Mcln Model Retriever
     * 2) AvailableMclnStatementStates
     *
     * @param oppositeMclnStatementState
     * @param mclnState
     * @param interpretation
     * @return
     */
    public static MclnStatementState createMclnStatementState(MclnStatementState oppositeMclnStatementState,
                                                              MclnState mclnState, String interpretation) {
        return new MclnStatementState(mclnState, oppositeMclnStatementState, interpretation);
    }

    /**
     * Called from Initializer: Property Data Holder
     *
     * @param mclnState
     * @param interpretation
     * @return
     */
    public static MclnStatementState createMclnStatementStateNew(MclnState mclnState, String interpretation) {
        return new MclnStatementState(mclnState, interpretation, null);
    }

    /**
     * Called from Initializer: Property Data Holder
     *
     * @param mclnState
     * @param mclnStateInterpretation
     * @param oppositeMclnState
     * @param oppositeMclnStateInterpretation
     * @return
     */
    public static MclnStatementState createMclnStatementStateNew(MclnState mclnState,
                                                                 String mclnStateInterpretation,
                                                                 MclnState oppositeMclnState,
                                                                 String oppositeMclnStateInterpretation) {
        MclnStatementState oppositeMclnStatementState = new MclnStatementState(oppositeMclnState,
                oppositeMclnStateInterpretation);
        MclnStatementState mclnStatementState = new MclnStatementState(mclnState, mclnStateInterpretation,
                oppositeMclnStatementState);
        return mclnStatementState;
    }

//    public static MclnStatementState createMclnStatementState(MclnPaletteState mclnPaletteState, String interpretation) {
//        return new MclnStatementState(null, mclnPaletteState, interpretation);
//    }

    //
    //   I n s t a n c e
    //

//    private lastInputValue

    private final MclnState mclnState;
    private MclnStatementState oppositeMclnStatementState;
    private String interpretation = "";

    private MclnStatementState(MclnState mclnState, String interpretation) {
        this.mclnState = mclnState;
        this.interpretation = interpretation != null ? interpretation : "";
        oppositeMclnStatementState = null;
    }

    private MclnStatementState(MclnState mclnState, String interpretation,
                               MclnStatementState oppositeMclnStatementState) {
        this.mclnState = mclnState;
        this.interpretation = interpretation != null ? interpretation : "";

        this.oppositeMclnStatementState = oppositeMclnStatementState;
        if (oppositeMclnStatementState != null) {
            oppositeMclnStatementState.oppositeMclnStatementState = this;
        }
    }

    private MclnStatementState(MclnState mclnState, MclnStatementState oppositeMclnStatementState,
                               String interpretation) {
        this.mclnState = mclnState;
        this.interpretation = interpretation != null ? interpretation : "";

        this.oppositeMclnStatementState = oppositeMclnStatementState;
        if (oppositeMclnStatementState != null) {
            oppositeMclnStatementState.oppositeMclnStatementState = this;
        }
    }

    @Override
    public MclnStatementState clone() {
        try {
            return (MclnStatementState) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }

    //   A t t r i b u t e s

    public MclnState getMclnState() {
        return mclnState;
    }

    public boolean isOppositeState(int otherState) {
        return mclnState.isOppositeState(otherState);
    }


    public MclnStatementState getOppositeMclnStatementState() {
        return oppositeMclnStatementState;
    }

    public String getColorName() {
        return mclnState.getColorName();
    }

    public int getState() {
        return mclnState.getState();
    }

    public int getRGB() {
        return mclnState.getRGB();
    }

    public int getStateID() {
        return mclnState.getStateID();
    }

    public String getHexColor() {
        return mclnState.getHexColor();
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation != null ? interpretation : "";
    }

    public String getStateInterpretation() {
        return interpretation;
    }

    public String toView() {
        return mclnState.toView() + ", " + interpretation;
    }

    @Override
    public boolean equals(Object other) {

        if ((other instanceof MclnState)) {
            MclnState otherMclnState = (MclnState) other;
            return mclnState.getHexColor().equals(otherMclnState.getHexColor());
        }

        if ((other instanceof MclnStatementState)) {
            MclnStatementState otherMclnStatementState = (MclnStatementState) other;
            String thisStateInterpretation = getStateInterpretation();
            String otherStateInterpretation = otherMclnStatementState.getStateInterpretation();
            if(!thisStateInterpretation.equals(otherStateInterpretation)){
                return false;
            }
            return mclnState.getHexColor().equals(otherMclnStatementState.getHexColor());
        }

        return false;
    }

    public String toString() {
        return "Interpretation = " + interpretation +", MclnState = "+ mclnState.toString();
    }

    /**
     * @param stringBuilder
     * @return
     */
    public StringBuilder toXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_STATEMENT_STATE_TAG).append(">");
        mclnState.toXml(stringBuilder, MCLN_STATE_TAG);
        String interpretation = getStateInterpretation();
        stringBuilder.append("<").append(INTERPRETATION_TAG).append(">").
                append(interpretation).append("</").append(INTERPRETATION_TAG).append(">");
        stringBuilder.append("</").append(MCLN_STATEMENT_STATE_TAG).append(">");
        return stringBuilder;
    }


}
