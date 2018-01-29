package dsdsse.designspace.initializer;

import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Created by Admin on 3/16/2016.
 */
final class InitAssistantDataModel {

    private static final Logger logger = Logger.getLogger(InitAssistantDataModel.class.getName());

    private static enum ModelEntity {Nothing, Property, Arc}

    static enum AttributeID {
        StatePaletteChosen, AllowedStatesChoiceChanged, ComponentName, PropertyName, AllowedStatementState, InitialState,
        HasProgram, TimeDrivenGeneratorSelected, RuleDrivenGeneratorSelected, ProgramHasPhase,
        ProgramStructureChanged, ProgramRowChanged, ArcState
    }

    //
    //   L i s t e n e r s
    //
    private final List<InitAssistantDataModelListener> initAssistantDataModelListeners = new CopyOnWriteArrayList();

    private ModelEntity entityBeingInitialized = ModelEntity.Nothing;
    private DataHolder dataHolder;

    //
    //   C r e a t i o n
    //

    InitAssistantDataModel() {
        entityBeingInitialized = ModelEntity.Nothing;
    }

    // Initializing with Property
    InitAssistantDataModel(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        entityBeingInitialized = ModelEntity.Property;
    }

    // Initializing with Arc
    InitAssistantDataModel(ArcDataHolder arcDataHolder) {
        this.dataHolder = arcDataHolder;
        entityBeingInitialized = ModelEntity.Arc;
    }

    void resetDataHolder(DataHolder dataHolder) {
        initAssistantDataModelListeners.clear();
        this.dataHolder = dataHolder;
        if (dataHolder instanceof PropertyDataHolder) {
            entityBeingInitialized = ModelEntity.Property;
        } else if (dataHolder instanceof ArcDataHolder) {
            entityBeingInitialized = ModelEntity.Arc;
        } else {
            entityBeingInitialized = ModelEntity.Nothing;
        }
    }

    final void addListener(InitAssistantDataModelListener adfInitAssistantDataModelListener) {
        initAssistantDataModelListeners.add(adfInitAssistantDataModelListener);
    }

    final void removeListener(InitAssistantDataModelListener adfInitAssistantDataModelListener) {
        initAssistantDataModelListeners.remove(adfInitAssistantDataModelListener);
    }

    final boolean isAssistantInitialized() {
        return entityBeingInitialized != ModelEntity.Nothing;
    }

    final boolean isInitializingProperty() {
        return entityBeingInitialized == ModelEntity.Property;
    }

    final boolean isInitializingArc() {
        return entityBeingInitialized == ModelEntity.Arc;
    }

    final void showMessage(String message) {
        fireShowMessageRequest(message);
    }

    final void showInfoMessage(String message) {
        fireShowMessageRequest(message);
    }

    final void showAndHideInfoMessage(String message) {
        fireShowAndHideMessageRequest(message);
    }

    //
    //   A t t r i b u t e s   S e t t e r s  a n d   G e t t e r s
    //

    public MclnStatement getMclnProperty() {
        return dataHolder.getMclnProperty();
    }

    public Polygon getArcArrowPoints() {
        return dataHolder.getArcArrowPoints();
    }

    public MclnArc getMclnArc() {
        return dataHolder.getMclnArc();
    }

    final List<MclnStatementState> getCurrentAllowedStatesList() {
        return dataHolder.getAllowedStatesList();
    }

    final boolean[] getPaletteSelectedStates() {
        return dataHolder.getPaletteSelectedStates();
    }

    final List<MclnState> geMclnStatesPaletteAsList() {
        return dataHolder.geMclnStatesPaletteAsList();
    }

    final boolean isPaletteModified() {
        return dataHolder.isPaletteModified();
    }

    boolean isPairsOfOppositeStatesPalette() {
        return dataHolder.isPairsOfOppositeStatesPalette();
    }

    //
    //  Program attributes
    //

    final boolean propertyHasProgram() {
        return dataHolder.propertyHasProgram();
    }

    boolean isSelectedProgramTimeDrivenProgram() {
        return dataHolder.isSelectedProgramTimeDrivenProgram();
    }

    public void setSelectedProgramSteps(List<ProgramStepData> selectedProgramSteps) {
        dataHolder.setSelectedProgramSteps(selectedProgramSteps);
        fireAdfDataModelChanged(AttributeID.ProgramStructureChanged, true);
    }

    List<ProgramStepData> getSelectedProgramSteps() {
        return dataHolder.getSelectedProgramSteps();
    }


    public String getMclnStatesPaletteName() {
        return dataHolder.getMclnStatesPaletteName();
    }

    public MclnStatesNewPalette getMclnStatesPalette() {
        return dataHolder.getMclnStatesPalette();
    }

    void setMclnStatePalette(MclnStatesNewPalette mclnStatesPalette) {
        dataHolder.setMclnStatePalette(mclnStatesPalette);
        fireAdfDataModelChanged(AttributeID.StatePaletteChosen, true);

        if (!isSelectedAllowedStatesListValid()) {
            fireAdfDataModelChanged(InitAssistantDataModel.AttributeID.AllowedStatesChoiceChanged, false);
        } else {
            fireAdfDataModelChanged(InitAssistantDataModel.AttributeID.AllowedStatesChoiceChanged, true);
        }
    }

    boolean isSelectedAllowedStatesListValid() {
        return dataHolder.isSelectedAllowedStatesListValid();
    }

    /**
     * Called when Assistant is going to show Property Allowed States Table filled
     * with states based on current selection in MclnStatesPaletteTableModel
     *
     * @param paletteSelectedAllowedStatesList
     */
    void updateCurrentAllowedStatesList(List<MclnState> paletteSelectedAllowedStatesList) {

        if (!dataHolder.updateCurrentAllowedStatesList(paletteSelectedAllowedStatesList)) {
            fireAdfDataModelChanged(AttributeID.AllowedStatesChoiceChanged, false);
            showInfoMessage(InitAssistantMessagesAndDialogs.MESSAGE_AVAILABLE_STATES_NOT_SELECTED);
        } else {
            fireAdfDataModelChanged(AttributeID.AllowedStatesChoiceChanged, true);
        }
    }

    /**
     * @param mclnStateID
     * @return
     */
    MclnStatementState getMclnStatementStateByMclnStateID(Integer mclnStateID) {
        return dataHolder.getMclnStatementStateByMclnStateID(mclnStateID);
    }


    final String getComponentName() {
        return dataHolder.getComponentName();
    }

    final void setComponentName(String componentName) {
        dataHolder.setComponentName(componentName);
        fireAdfDataModelChanged(AttributeID.ComponentName, true);
    }

    final String getPropertyName() {
        return dataHolder.getPropertyName();
    }

    final void setPropertyName(String propertyName) {
        dataHolder.setPropertyName(propertyName);
        fireAdfDataModelChanged(AttributeID.PropertyName, true);
    }

    final void allowedStatementStatesUpdated() {
        fireAdfDataModelChanged(AttributeID.AllowedStatementState, true);
    }

    public MclnStatementState getInitialMclnStatementState() {
        return dataHolder.getInitialMclnStatementState();
    }

    public MclnStatementState getMclnStatementStateByIndex(int index) {
        if (index >= dataHolder.getAllowedStatesList().size()) {
            showInfoMessage("Requested Mcln Statement State index exceeded list size ");
            return null;
        }
        return dataHolder.getMclnStatementStateByIndex(index);
    }

    final void setProgramMclnStateCandidate(MclnStatementState programMclnStateCandidate) {
        dataHolder.setProgramMclnStateCandidate(programMclnStateCandidate);
    }

    public MclnStatementState getProgramMclnStateCandidate() {
        return dataHolder.getProgramMclnStateCandidate();
    }

    final void setInitialMclnState(MclnStatementState updatedInitialMclnStatementState) {
        dataHolder.setInitialMclnState(updatedInitialMclnStatementState);
        fireAdfDataModelChanged(AttributeID.InitialState, dataHolder.getInitialMclnStatementState() != null);
    }

    final void setPropertyHasProgram(boolean hasProgram) {
        dataHolder.setPropertyHasProgram(hasProgram);
        fireAdfDataModelChanged(AttributeID.HasProgram, hasProgram);
    }

    final boolean getSelectedProgramHasPhase() {
        return dataHolder.getSelectedProgramHasPhase();
    }

    final void setSelectedProgramHasPhase(boolean selectedProgramHasPhase) {
        dataHolder.setSelectedProgramHasPhase(selectedProgramHasPhase);
        fireAdfDataModelChanged(AttributeID.ProgramHasPhase, selectedProgramHasPhase);
    }

    final void swapProgramsOnTypeReselected() {
        dataHolder.swapProgramsOnTypeReselected();
        if (dataHolder.isSelectedProgramTimeDrivenProgram()) {
            fireAdfDataModelChanged(AttributeID.TimeDrivenGeneratorSelected, true);
        } else {
            fireAdfDataModelChanged(AttributeID.RuleDrivenGeneratorSelected, true);
        }
    }

    //
    //   Arc attributes getters and setters
    //

    boolean isRecognizingArc() {
        return dataHolder.isRecognizingArc();
    }

    final void setArcSelectedMclnState(MclnState selectedArcMclnState) {
        dataHolder.setArcSelectedMclnState(selectedArcMclnState);
        fireAdfDataModelChanged(AttributeID.ArcState, selectedArcMclnState != null);
    }

    final MclnState getArcSelectedMclnState() {
        return dataHolder.getArcSelectedMclnState();
    }

    // Calling listeners

    final void fireProgramRowChanged() {
        fireAdfDataModelChanged(AttributeID.ProgramRowChanged, true);
    }

    /*
       The Button Panel is one of the listeners of this call.
       When called, it will check if initialized entity is modified
       and then enable or disable Apply change button correspondingly.
     */
    final void fireAdfDataModelChanged(AttributeID attributeID, boolean initialized) {
        for (InitAssistantDataModelListener initAssistantDataModelListener : initAssistantDataModelListeners) {
            initAssistantDataModelListener.onInitAssistantDataModelChanged(this, attributeID, initialized);
        }
    }

    private final void fireShowMessageRequest(String message) {
        for (InitAssistantDataModelListener initAssistantDataModelListener : initAssistantDataModelListeners) {
            initAssistantDataModelListener.showMessage(message);
        }
    }

    private final void fireShowAndHideMessageRequest(String message) {
        for (InitAssistantDataModelListener initAssistantDataModelListener : initAssistantDataModelListeners) {
            initAssistantDataModelListener.showAndHideMessage(message);
        }
    }

    //   M o d i f i c a t i o n   c h e c k

    boolean isEntityModified() {
        return dataHolder.isEntityModified();
    }


//    boolean isSelectedAllowedStatesListValid(List<MclnState> paletteSelectedAllowedStatesList) {
//        return paletteSelectedAllowedStatesList != null && paletteSelectedAllowedStatesList.size() != 0;
//    }

    // Finalization

//    private boolean isPropertyAllowedStatesListModified() {
//        return true;
//    }

//    boolean isInitializationComplete() {
//        boolean initialized = false;
//
//        // checking if Allowed States changed
//        boolean allowedStatesChanged = isAllowedStatesChanged();
//        // checking if Initial State changed
//        boolean initialStateChanged = isInitialStateChanged();
//
//
//        return initialized;
//    }

//    /**
//     * Checking if allowed states changed
//     *
//     * @return
//     */
//    private boolean isAllowedStatesChanged() {
//
//        boolean sizeChanged = currentAllowedStatesList.size() != originalAllowedStatesList.size();
//        if (sizeChanged) {
//            return sizeChanged;
//        }
//
//        // checking if content changed
//        int statesSize = originalAllowedStatesList.size();
//        boolean contentChanged = false;
//        for (int i = 0; i < statesSize; i++) {
//            MclnStatementState mclnState = currentAllowedStatesList.get(i);
//            if (!originalAllowedStatesList.contains(mclnState)) {
//                return true;
//            }
//        }
//        return false;
//    }

//    private boolean isInitialStateChanged() {
//        return (originalInitialMclnStatementState == null && selectedInitialMclnStatementState != null) ||
//                (selectedInitialMclnStatementState != originalInitialMclnStatementState);
//    }
}
