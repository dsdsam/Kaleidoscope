package mcln.model;

import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 4/1/14
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class AvailableMclnStatementStates {

    static final String AVAILABLE_STATEMENT_STATES_TAG = "Available-Statement-States";
    static final String AVAILABLE_STATE_TAG = "Available-State";

    /**
     * This method is called to create AvailableMclnStatementStates from Editor and for demo
     *
     * @param availableStatesData
     * @param initialState
     * @return
     */
    public static AvailableMclnStatementStates createAvailableStatementStates(MclnStatesNewPalette mclnStateNewPalette,
                                                                              Object[][] availableStatesData,
                                                                              MclnState initialState) {
        return createInstance(mclnStateNewPalette, availableStatesData, initialState);
    }

    /**
     * This method is called to create AvailableMclnStatementStates for demo
     *
     * @param mclnStateNewPalette
     * @param availableStatesData
     * @param initialState
     * @return
     */
    public static AvailableMclnStatementStates createAvailableSymmetricStatementStates(
            MclnStatesNewPalette mclnStateNewPalette, Object[][] availableStatesData, MclnState initialState) {
        return createInstance(mclnStateNewPalette, availableStatesData, initialState);
    }

    /**
     * @param mclnStateNewPalette
     * @param availableStatesData
     * @param initialState
     * @return
     */
    private static AvailableMclnStatementStates createInstance(MclnStatesNewPalette mclnStateNewPalette,
                                                               Object[][] availableStatesData,
                                                               MclnState initialState) {

        MclnStatementState initialMclnStatementState =
                MclnStatementState.createMclnStatementState(initialState, "$ is in Initial State");

        Map<Integer, MclnStatementState> availableMclnStatementStates = new LinkedHashMap();
//        List<MclnState> propertyStatesAsList = new ArrayList();

        availableMclnStatementStates.put(initialState.getStateID(), initialMclnStatementState);
//        propertyStatesAsList.add(mclnInitialStatementState);

        if (availableStatesData == null || availableStatesData.length == 0 || availableStatesData[0].length == 0) {
            return new AvailableMclnStatementStates(mclnStateNewPalette.getPaletteName(), initialMclnStatementState,
                    availableMclnStatementStates);
        }

        try {

            for (Object[] entryPair : availableStatesData) {
                MclnState mclnState = (MclnState) entryPair[0];
                String interpretation = (String) entryPair[1];
                MclnStatementState mclnStatementState =
                        MclnStatementState.createMclnStatementState(mclnState, interpretation);

                availableMclnStatementStates.put(mclnStatementState.getStateID(), mclnStatementState);
//                propertyStatesAsList.add(mclnStatementState);
            }

            // adding opposite states
            if (mclnStateNewPalette != null && mclnStateNewPalette.isPairsOfOppositeStatesPalette()) {
                for (Object[] entryPair : availableStatesData) {
                    MclnState mclnState = (MclnState) entryPair[0];
                    if (mclnState.getState() <= 0) {
                        continue;
                    }

                    MclnState oppositeMclnState = mclnState.getOppositeMclnState();
                    if (oppositeMclnState == null) {
                        // no opposite state
                        continue;
                    }

                    int oppositeStateID = oppositeMclnState.getStateID();
                    MclnStatementState oppositeStatementState = availableMclnStatementStates.get(oppositeStateID);
                    if (oppositeStatementState != null) {
                        continue;
                    }
                    MclnStatementState mclnStatementState = availableMclnStatementStates.get(mclnState.getStateID());

//                    MclnState oppositeMclnState = mclnStateNewPalette.getState(oppositeState);
                    String colorName = oppositeMclnState.getColorName();
                    String oppositeColorName = "Not " + mclnState.getColorName();
                    String interpretation = "$ is " + colorName + " (" + oppositeColorName + ")";
                    oppositeStatementState = MclnStatementState.createMclnStatementState(mclnStatementState,
                            oppositeMclnState, interpretation);
                    availableMclnStatementStates.put(oppositeStatementState.getStateID(), oppositeStatementState);
//                    propertyStatesAsList.add(oppositeStatementState);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            availableMclnStatementStates.clear();
        }
        return new AvailableMclnStatementStates(mclnStateNewPalette.getPaletteName(), initialMclnStatementState,
                availableMclnStatementStates);
    }

    /**
     * Called from GUI Model Initializer Assistant
     *
     * @param availableStatesList
     * @param initialMclnStatementState
     * @return
     */
    public static AvailableMclnStatementStates createAvailableStatementStates(
            String mclnStatesPaletteName, List<MclnStatementState> availableStatesList,
            MclnStatementState initialMclnStatementState) {

        Map<Integer, MclnStatementState> availableMclnStatementStates = new LinkedHashMap();
        for (MclnStatementState mclnStatementState : availableStatesList) {
            availableMclnStatementStates.put(mclnStatementState.getStateID(), mclnStatementState);
        }

        return new AvailableMclnStatementStates(mclnStatesPaletteName, initialMclnStatementState,
                availableMclnStatementStates);
    }


    //
    //   I n s t a n c e
    //

    private String mclnStatesPaletteName;
    private final MclnStatementState initialMclnStatementState;
    private final Map<Integer, MclnStatementState> stateIdToMclnStatementStatesMap;
    private final List<MclnStatementState> propertyStatesAsList = new ArrayList();

    /**
     * This constructor is used when model is created by editor or demo builder
     *
     * @param stateIdToMclnStatementStatesMap
     * @param initialMclnStatementState
     */
    AvailableMclnStatementStates(String mclnStatesPaletteName, MclnStatementState initialMclnStatementState,
                                         Map<Integer, MclnStatementState> stateIdToMclnStatementStatesMap) {
        this.mclnStatesPaletteName = mclnStatesPaletteName != null ? mclnStatesPaletteName : "";
        this.initialMclnStatementState = initialMclnStatementState;
        this.stateIdToMclnStatementStatesMap = stateIdToMclnStatementStatesMap;
        propertyStatesAsList.addAll(stateIdToMclnStatementStatesMap.values());
    }

//    /**
//     * This constructor is used when model recreated from XML
//     *
//     * @param availableMclnStatementStates
//     */
//    AvailableMclnStatementStates(Map<Integer, MclnStatementState> availableMclnStatementStates,
//                                 String mclnStatesPaletteName, MclnStatementState initialMclnStatementState) {
//        this.mclnStatesPaletteName = mclnStatesPaletteName != null ? mclnStatesPaletteName : "";
//        this.availableMclnStatementStates = availableMclnStatementStates;
//        this.initialMclnStatementState = null;
//    }

    public String getMclnStatesPaletteName() {
        return mclnStatesPaletteName;
    }

    public List<MclnStatementState> getPropertyStatesAsList() {
        return new ArrayList(propertyStatesAsList);
    }


    MclnStatementState getInitialMclnStatementState() {
        return initialMclnStatementState;
    }

    int size() {
        return stateIdToMclnStatementStatesMap.size();
    }

    public MclnStatementState getMclnStatementState(Integer stateID) {
        return stateIdToMclnStatementStatesMap.get(stateID);
    }

    public MclnState getMclnState(Integer stateID) {
        return stateIdToMclnStatementStatesMap.get(stateID).getMclnState();
    }

    /**
     * @param stringBuilder
     * @return
     */
    StringBuilder toXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(AVAILABLE_STATEMENT_STATES_TAG).append(">");

        // available states
        Collection<MclnStatementState> mclnStatementStates = stateIdToMclnStatementStatesMap.values();
        for (MclnStatementState mclnStatementState : mclnStatementStates) {
            mclnStatementState.toXml(stringBuilder);
        }

        stringBuilder.append("</").append(AVAILABLE_STATEMENT_STATES_TAG).append(">");
        return stringBuilder;
    }
}
