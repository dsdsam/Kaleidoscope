package dsdsse.designspace.initializer;

import dsdsse.graphview.MclnPropertyView;
import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 6/22/2016.
 */
class PropertyDataHolder extends DataHolder {

    /**
     * @param mclnStatesPalette
     * @param mclnPropertyView
     * @return
     */
    static PropertyDataHolder createPropertyDataHolder(MclnStatesNewPalette mclnStatesPalette,
                                                       MclnPropertyView mclnPropertyView) {
        return new PropertyDataHolder(mclnStatesPalette, mclnPropertyView);
    }


    // palette
    private boolean previousPaletteIsPairsOfState;

    // Property original Allowed States Palette
    private String originalAllowedStatesPaletteName;

    // palled that was used to initialize Allowed States
    private String currentAllowedStatesPaletteName;

    // palled that is selected but was not yet used to initialize Allowed States
    private String selectedMclnStatesPaletteName;

    private MclnStatesNewPalette mclnStatesPalette;
    private final List<MclnState> mclnStatesPaletteAsList = new ArrayList();
    private boolean[] selectedStates;

    // Property Attributes
    AvailableMclnStatementStates originalAvailableMclnStatementStates;
    private final List<MclnStatementState> originalAllowedStatesList = new ArrayList();
    private List<MclnStatementState> currentAllowedStatesList = new ArrayList();

    private MclnStatementState originalInitialMclnStatementState;
    private MclnStatementState selectedInitialMclnStatementState;

    private MclnStatementState programMclnStateCandidate;

    private final MclnStatement mclnStatement;

    private String originalComponentName = "";
    private String componentName = "";

    private String originalPropertyName = "";
    private String propertyName = "";

    private boolean originalPropertyHasProgram;
    private boolean selectedPropertyHasProgram;
    private boolean originalProgramIsTimeDrivenProgram;
    private boolean selectedProgramIsTimeDrivenProgram;

    private final List<ProgramStepData> originalProgramSteps = new ArrayList();
    private List<ProgramStepData> selectedProgramSteps = new ArrayList();
    private List<ProgramStepData> selectedTimeDrivenProgramSteps = new ArrayList();
    private List<ProgramStepData> selectedRuleDrivenProgramSteps = new ArrayList();

    private boolean originalProgramHasPhase;
    private boolean selectedProgramHasPhase;
    private boolean selectedTimeDrivenProgramHasPhase;
    private boolean selectedRuleDrivenProgramHasPhase;

    /**
     * @param mclnStatesPalette
     * @param mclnPropertyView
     */
    private PropertyDataHolder(MclnStatesNewPalette mclnStatesPalette,
                               MclnPropertyView mclnPropertyView) {
        assert mclnPropertyView != null : "mclnPropertyView must not be null";

        // palette
        this.mclnStatesPalette = mclnStatesPalette;
        originalAllowedStatesPaletteName = mclnStatesPalette.getPaletteName();
        currentAllowedStatesPaletteName = originalAllowedStatesPaletteName;
        selectedMclnStatesPaletteName = originalAllowedStatesPaletteName;
        mclnStatesPaletteAsList.addAll(mclnStatesPalette.getPaletteAsList());

        // property
        this.mclnStatement = mclnPropertyView.getTheElementModel();

        // property attributes
        originalAvailableMclnStatementStates = mclnStatement.getAvailableMclnStatementStates();
        originalAllowedStatesList.addAll(mclnStatement.getPropertyStatesAsList());

        originalComponentName = mclnStatement.getSubject();
        componentName = originalComponentName;
        originalPropertyName = mclnStatement.getPropertyName();
        propertyName = originalPropertyName;

        originalInitialMclnStatementState = mclnStatement.getInitialMclnStatementState();
        selectedInitialMclnStatementState = originalInitialMclnStatementState;

        // preparing flag "has program"
        originalPropertyHasProgram = mclnStatement.hasInputGeneratingProgram();
        selectedPropertyHasProgram = originalPropertyHasProgram;

        // preparing program type statement
        if (originalPropertyHasProgram) {
            originalProgramIsTimeDrivenProgram = mclnStatement.isTimeDrivenProgram();
            selectedProgramIsTimeDrivenProgram = originalProgramIsTimeDrivenProgram;
        } else {
            originalProgramIsTimeDrivenProgram = false;
            selectedProgramIsTimeDrivenProgram = true;
        }


        // preparing program steps
        if (originalPropertyHasProgram) {
            originalProgramHasPhase = false;
            if (isSelectedProgramTimeDrivenProgram()) {
                originalProgramHasPhase = mclnStatement.theProgramHasPhase();
                if (!originalProgramHasPhase) {
                    originalProgramSteps.add(new ProgramStepData(true));
                }
            }
        }


        List<ProgramStepData> existingSteps = new ArrayList(originalProgramSteps);
        existingSteps.addAll(mclnStatement.getProgramData());
        originalProgramSteps.clear();
        for( int i = 0; i < existingSteps.size(); i++){
            ProgramStepData programStepData = existingSteps.get(i);
            originalProgramSteps.add(programStepData.clone());
        }
        selectedProgramSteps = new ArrayList(existingSteps);

        if (originalProgramIsTimeDrivenProgram) {
            selectedTimeDrivenProgramSteps = selectedProgramSteps;
        } else {
            selectedRuleDrivenProgramSteps = selectedProgramSteps;
        }

        if (originalPropertyHasProgram) {
            // preparing flag "program has phase"
            selectedProgramHasPhase = originalProgramHasPhase;
            if (originalProgramIsTimeDrivenProgram) {
                selectedTimeDrivenProgramHasPhase = selectedProgramHasPhase;
            } else {
                selectedRuleDrivenProgramHasPhase = selectedProgramHasPhase;
            }
        }

//        currentAllowedStatesList = new ArrayList( );
//        for( int i = 0; i < originalAllowedStatesList.size(); i++){
//            MclnStatementState originalMclnStatementState = originalAllowedStatesList.get(i);
//            MclnStatementState clonedOriginalMclnStatementState = originalMclnStatementState.clone();
//            currentAllowedStatesList.add(clonedOriginalMclnStatementState);
//        }
//
//        for (MclnStatementState mclnStatementState : currentAllowedStatesList) {
//            mclnStateIdToMclnStatementStatesMap.put(mclnStatementState.getStateID(), mclnStatementState);
//        }
        selectedStates = preparePaletteSelectedStates();
    }

    @Override
    public MclnStatement getMclnProperty() {
        return mclnStatement;
    }

    /**
     * Called when the holder created and when Palette reselected
     *
     * @return
     */
    private boolean[] preparePaletteSelectedStates() {
        int paletteSize = mclnStatesPaletteAsList.size();
        boolean[] selectedStates = new boolean[paletteSize];

        // return empty allowed states selection if palette is different from the one was used to create
        // The Property Allowed States List
        // This may happen when user moves back and forth between Assistant's pages

        if (selectedMclnStatesPaletteName.equalsIgnoreCase(originalAllowedStatesPaletteName)) {
            for (int i = 0; i < paletteSize; i++) {
                MclnState mclnState = mclnStatesPaletteAsList.get(i);
                selectedStates[i] = originalAllowedStatesList.contains(mclnState);
            }
//            currentAllowedStatesList = new ArrayList(originalAllowedStatesList);
            currentAllowedStatesList = new ArrayList( );
            for( int i = 0; i < originalAllowedStatesList.size(); i++){
                MclnStatementState originalMclnStatementState = originalAllowedStatesList.get(i);
                MclnStatementState clonedOriginalMclnStatementState = originalMclnStatementState.clone();
                 currentAllowedStatesList.add(clonedOriginalMclnStatementState);
            }

            mclnStateIdToMclnStatementStatesMap.clear();
            for (MclnStatementState mclnStatementState : currentAllowedStatesList) {
                mclnStateIdToMclnStatementStatesMap.put(mclnStatementState.getStateID(), mclnStatementState);
            }

            return selectedStates;

        }

        if (selectedMclnStatesPaletteName.equalsIgnoreCase(currentAllowedStatesPaletteName)) {
            for (int i = 0; i < paletteSize; i++) {
                MclnState mclnState = mclnStatesPaletteAsList.get(i);
                selectedStates[i] = currentAllowedStatesList.contains(mclnState);
            }
            return selectedStates;
        }

        return selectedStates;
    }

    //
    //   A t t r i b u t e s   S e t t e r s  a n d   G e t t e r s
    //

    //   Palette related attributes

    public String getMclnStatesPaletteName() {
        return currentAllowedStatesPaletteName;
    }

    public MclnStatesNewPalette getMclnStatesPalette() {
        return mclnStatesPalette;
    }

    @Override
    final boolean isPaletteModified() {
        return originalAllowedStatesPaletteName.equalsIgnoreCase(currentAllowedStatesPaletteName);
    }

    @Override
    boolean isPairsOfOppositeStatesPalette() {
        return mclnStatesPalette.isPairsOfOppositeStatesPalette();
    }

    @Override
    void setMclnStatePalette(MclnStatesNewPalette mclnStatesPalette) {
        previousPaletteIsPairsOfState = this.mclnStatesPalette.isPairsOfOppositeStatesPalette();
        this.mclnStatesPalette = mclnStatesPalette;
        selectedMclnStatesPaletteName = mclnStatesPalette.getPaletteName();

        mclnStatesPaletteAsList.clear();
        mclnStatesPaletteAsList.addAll(mclnStatesPalette.getPaletteAsList());
//        selectedStates = new boolean[mclnStatesPalette.getPaletteAsList().size()];
        selectedStates = preparePaletteSelectedStates();

        if (!isSelectedAllowedStatesListValid()) {
            currentAllowedStatesList.clear();
        }
//        currentAllowedStatesList.clear();
    }

    @Override
    List<MclnState> geMclnStatesPaletteAsList() {
        return mclnStatesPaletteAsList;
    }

    @Override
    boolean[] getPaletteSelectedStates() {
        return selectedStates;
    }

    //   Allowed States related attributes

    private final Map<Integer, MclnStatementState> mclnStateIdToMclnStatementStatesMap = new HashMap();

    /**
     * Called when Assistant is going to show Allowed States Table
     * but filled as nothing was selected from States Palette
     *
     * @param paletteSelectedAllowedStatesList
     */
    @Override
    public boolean updateCurrentAllowedStatesList(List<MclnState> paletteSelectedAllowedStatesList) {

        if (!isSelectedAllowedStatesListValid(paletteSelectedAllowedStatesList)) {
            return false;
        }

        this.currentAllowedStatesList = new ArrayList();
        mclnStateIdToMclnStatementStatesMap.clear();
        for (MclnState paletteMclnState : paletteSelectedAllowedStatesList) {
//                MclnStatementState currentlySelectedMclnStatementState =
//                        originalAvailableMclnStatementStates.getMclnStatementState(paletteMclnState.getStateID());
//                if (currentlySelectedMclnStatementState != null) {
//                    currentAllowedStatesList.add(currentlySelectedMclnStatementState);
//                } else {

            if (!isPairsOfOppositeStatesPalette()) {
                MclnStatementState mclnStatementState =
                        MclnStatementState.createMclnStatementStateNew(paletteMclnState,
                                paletteMclnState.getColorName());
                currentAllowedStatesList.add(mclnStatementState);
                mclnStateIdToMclnStatementStatesMap.put(mclnStatementState.getStateID(), mclnStatementState);
            } else {
                MclnState oppositeMclnState = paletteMclnState.getOppositeMclnState();
                MclnStatementState mclnStatementState =
                        MclnStatementState.createMclnStatementStateNew(paletteMclnState,
                                paletteMclnState.getColorName(),
                                oppositeMclnState,
                                (oppositeMclnState != null ? oppositeMclnState.getColorName() : ""));

                MclnStatementState oppositeMclnStatementState =
                        mclnStatementState.getOppositeMclnStatementState();

                currentAllowedStatesList.add(mclnStatementState);
                currentAllowedStatesList.add(oppositeMclnStatementState);
                mclnStateIdToMclnStatementStatesMap.put(mclnStatementState.getStateID(), mclnStatementState);
                mclnStateIdToMclnStatementStatesMap.put(oppositeMclnStatementState.getStateID(),
                        oppositeMclnStatementState);
            }

        }

        // this assignment states the selected palette was used to create Property Allowed States list
        currentAllowedStatesPaletteName = selectedMclnStatesPaletteName;
        return true;
    }

    /**
     * @param mclnStateID
     * @return
     */
    MclnStatementState getMclnStatementStateByMclnStateID(Integer mclnStateID) {
        return mclnStateIdToMclnStatementStatesMap.get(mclnStateID);
    }

    @Override
    public List<MclnStatementState> getAllowedStatesList() {
        return currentAllowedStatesList;
    }

    //
    //   Input generating program related attributes
    //

    boolean isSelectedProgramTimeDrivenProgram() {
        return selectedProgramIsTimeDrivenProgram;
    }

    public void setSelectedProgramSteps(List<ProgramStepData> selectedProgramSteps) {
        this.selectedProgramSteps = selectedProgramSteps;
    }

    List<ProgramStepData> getSelectedProgramSteps() {
        return selectedProgramSteps;
    }


    //


    final String getComponentName() {
        return componentName;
    }

    final void setComponentName(String componentName) {
        this.componentName = componentName != null ? componentName : "";
        System.out.println(" setComponentName " + componentName);
    }

    final String getPropertyName() {
        return propertyName;
    }

    final void setPropertyName(String propertyName) {
        this.propertyName = propertyName != null ? propertyName : "";
        System.out.println(" setPropertyName " + propertyName);
    }


    public MclnStatementState getInitialMclnStatementState() {
        return selectedInitialMclnStatementState;
    }

    public MclnStatementState getMclnStatementStateByIndex(int index) {
//        if (index >= currentAllowedStatesList.size()) {
//            showInfoMessage("Requested Mcln Statement State index exceeded list size ");
//            return null;
//        }
        return currentAllowedStatesList.get(index);
    }

    final void setProgramMclnStateCandidate(MclnStatementState programMclnStateCandidate) {
        this.programMclnStateCandidate = programMclnStateCandidate;
    }

    public MclnStatementState getProgramMclnStateCandidate() {
        return programMclnStateCandidate;
    }

    final void setInitialMclnState(MclnStatementState updatedInitialMclnStatementState) {
        this.selectedInitialMclnStatementState = updatedInitialMclnStatementState;
        System.out.println(" setInitialMclnState " + selectedInitialMclnStatementState.toString());
    }

    final boolean propertyHasProgram() {
        return selectedPropertyHasProgram;
    }

    final void setPropertyHasProgram(boolean hasProgram) {
        this.selectedPropertyHasProgram = hasProgram;
        if (hasProgram) {
            selectedProgramIsTimeDrivenProgram = true;
        }
    }

    final boolean getSelectedProgramHasPhase() {
        return selectedProgramHasPhase;
    }

    final void setSelectedProgramHasPhase(boolean selectedProgramHasPhase) {
        this.selectedProgramHasPhase = selectedProgramHasPhase;
        if (isSelectedProgramTimeDrivenProgram()) {
            selectedTimeDrivenProgramHasPhase = selectedProgramHasPhase;
            if(!selectedProgramHasPhase){
                ProgramStepData programStepData =   selectedProgramSteps.get(0);
                programStepData.setConditionState(null);
                programStepData.setGeneratedState(null);
                programStepData.setTicks("-1");
            }
        } else {
            selectedRuleDrivenProgramHasPhase = selectedProgramHasPhase;
        }
    }

    final void swapProgramsOnTypeReselected() {
        if (isSelectedProgramTimeDrivenProgram()) {
            selectedProgramSteps = selectedRuleDrivenProgramSteps;
            selectedProgramHasPhase = selectedRuleDrivenProgramHasPhase;
            selectedProgramIsTimeDrivenProgram = false;
        } else {
            selectedProgramSteps = selectedTimeDrivenProgramSteps;
            selectedProgramHasPhase = selectedTimeDrivenProgramHasPhase;
            selectedProgramIsTimeDrivenProgram = true;
        }
    }

    //   V a l i d a t i o n

    @Override
    boolean isSelectedAllowedStatesListValid() {
        if (selectedStates == null) {
            return false;
        }
        for (boolean selected : selectedStates) {
            if (selected) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isSelectedAllowedStatesListValid(List<MclnState> paletteSelectedAllowedStatesList) {
        return paletteSelectedAllowedStatesList != null && paletteSelectedAllowedStatesList.size() != 0;
    }

    boolean isInitializationComplete() {
        boolean initialized = false;

        // checking if Allowed States changed
        boolean allowedStatesChanged = wasAllowedStatesChanged();
        // checking if Initial State changed
        boolean initialStateChanged = wasInitialStateChanged();

        // checking if initial state is element of Allowed States


        return initialized;
    }

    //
    //   M o d i f i c a t i o n   c h e c k
    //

    @Override
    boolean isEntityModified() {

        if (isPaletteChanged()) {
            return true;
        }

        if (isSubjectChanged()) {
            return true;
        }

        if (wasAllowedStatesChanged()) {
            return true;
        }

        if (wasInitialStateChanged()) {
            return true;
        }

        if (wasPropertyProgramChanged()) {
            return true;
        }

        return false;
    }

    private boolean isPaletteChanged() {
        boolean nameChanged = originalAllowedStatesPaletteName != null && currentAllowedStatesPaletteName != null &&
                !originalAllowedStatesPaletteName.equals(currentAllowedStatesPaletteName);
        return nameChanged;
    }

    private boolean isSubjectChanged() {
        boolean subjectChanged =
                !originalComponentName.equals(componentName) || !originalPropertyName.equals(propertyName);
        return subjectChanged;
    }

    /**
     * Checking if allowed states changed
     *
     * @return
     */
    private boolean wasAllowedStatesChanged() {

        boolean sizeChanged = currentAllowedStatesList.size() != originalAllowedStatesList.size();
        if (sizeChanged) {
            return true;
        }

        // checking if content changed
        int statesSize = originalAllowedStatesList.size();
        for (int i = 0; i < statesSize; i++) {
            MclnStatementState currentMclnStatementState = currentAllowedStatesList.get(i);
            boolean contains = originalAllowedStatesList.contains(currentMclnStatementState);
            if (!originalAllowedStatesList.contains(currentMclnStatementState)) {
                return true;
            }
        }
        return false;
    }

    private boolean wasInitialStateChanged() {
        return (originalInitialMclnStatementState == null && selectedInitialMclnStatementState != null) ||
                (selectedInitialMclnStatementState != originalInitialMclnStatementState);
    }

//
//    private boolean originalPropertyHasProgram;
//    private boolean selectedPropertyHasProgram;
//    private boolean originalProgramIsTimeDrivenProgram;
//    private boolean selectedProgramIsTimeDrivenProgram;
//
//    private final List<ProgramStepData> originalProgramSteps = new ArrayList();
//    private List<ProgramStepData> selectedProgramSteps = new ArrayList();
//    private List<ProgramStepData> selectedTimeDrivenProgramSteps = new ArrayList();
//    private List<ProgramStepData> selectedRuleDrivenProgramSteps = new ArrayList();
//
//    private boolean originalProgramHasPhase;
//    private boolean selectedProgramHasPhase;
//    private boolean selectedTimeDrivenProgramHasPhase;
//    private boolean selectedRuleDrivenProgramHasPhase;

    private boolean wasPropertyProgramChanged() {
        boolean programWasAddedToOrRemovedFromProperty = wasProgramAddedToOrRemovedFromProperty();
        if (programWasAddedToOrRemovedFromProperty) {
            // The program was either added to or removed from the Property
            // hence there is change
            return true;
        }

        // The program was not added to or removed from the Property

        if (!originalPropertyHasProgram) {
            // The property did not have program before modification
            // so no change
            return false;
        }

        // the program existed before, hence it might be changed

        boolean existingProgramWasChanged = wasExistingProgramChanged();
        return existingProgramWasChanged;
    }

    /**
     * @return
     */
    private boolean wasProgramAddedToOrRemovedFromProperty() {
        boolean flagPropertyHasProgramChanged = originalPropertyHasProgram != selectedPropertyHasProgram;
        if (!originalPropertyHasProgram) {
            // The property did not have program before modification
            if (!flagPropertyHasProgramChanged) {
                // and program was not added to property, so no change
                return false;
            } else {
                // but program was added to property, hence there is change
                return true;
            }

        } else {
            // The property had program before modification
            if (!flagPropertyHasProgramChanged) {
                // and program was not removed from property, so no change
                return false;
            } else {
                // but program was removed from property, hence there is change
                return true;
            }
        }
    }

    /**
     * @return
     */
    private boolean wasExistingProgramChanged() {
        boolean existingProgramTypeWasChanged = wasExistingProgramTypeChanged();
        if (existingProgramTypeWasChanged) {
            return true;
        }

        boolean phaseSelectionChanged = originalProgramHasPhase != selectedProgramHasPhase;
        if (phaseSelectionChanged) {
            return true;
        }

        int originalProgramSize = originalProgramSteps.size();
        int selectedProgramStepsSize = selectedProgramSteps.size();
        boolean programStructureChanged = originalProgramSize != selectedProgramStepsSize;
        if (programStructureChanged) {
            return true;
        }

        boolean equal = false;
        for (int i = 0; i < originalProgramSize; i++) {
            ProgramStepData originalProgramStep = originalProgramSteps.get(i);
            ProgramStepData selectedProgramStep = selectedProgramSteps.get(i);
            equal |= !originalProgramStep.isEqual(selectedProgramStep);
        }


//        if (selectedProgramIsTimeDrivenProgram) {
//            if (originalProgramSize != selectedTimeDrivenProgramSteps.size()) {
//                return true;
//            }
//            for (int i = 0; i < originalProgramSize; i++) {
//                ProgramStepData originalProgramStep = originalProgramSteps.get(i);
//                ProgramStepData selectedTimeDrivenProgramStep = selectedTimeDrivenProgramSteps.get(i);
//                equal |= !originalProgramStep.equals(selectedTimeDrivenProgramStep);
//            }
//        } else {
//            if (originalProgramSize != selectedRuleDrivenProgramSteps.size()) {
//                return true;
//            }
//            for (int i = 0; i < originalProgramSize; i++) {
//                ProgramStepData originalProgramStep = originalProgramSteps.get(i);
//                ProgramStepData selectedTimeDrivenProgramStep = selectedRuleDrivenProgramSteps.get(i);
//                equal |= !originalProgramStep.equals(selectedTimeDrivenProgramStep);
//            }
//        }
        return equal;
    }

    /**
     * @return
     */
    private boolean wasExistingProgramTypeChanged() {
        return originalProgramIsTimeDrivenProgram != selectedProgramIsTimeDrivenProgram;
    }
}
