package dsdsse.designspace.initializer;

import mcln.model.MclnArc;
import mcln.model.MclnStatement;
import mcln.model.MclnStatementState;
import mcln.model.ProgramStepData;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import java.awt.*;
import java.util.List;

/**
 * Created by Admin on 6/24/2016.
 */
class DataHolder {

    private final String UNSUPPORTED_OPERATION_EXCEPTION_TEXT =
            "This operation must be defined in one of the extending classes";

    private final UnsupportedOperationException unsupportedOperationException =
            new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_TEXT);

    List<MclnStatementState> getAllowedStatesList() {
        throw unsupportedOperationException;
    }

    boolean[] getPaletteSelectedStates() {
        throw unsupportedOperationException;
    }

    List<MclnState> geMclnStatesPaletteAsList() {
        throw unsupportedOperationException;
    }

    //
    //   A t t r i b u t e s   S e t t e r s  a n d   G e t t e r s
    //

//    boolean[] preparePaletteSelectedStates() {
//        throw unsupportedOperationException;
//    }

    boolean isRecognizingArc() {
        throw unsupportedOperationException;
    }

    boolean isPaletteModified() {
        throw unsupportedOperationException;
    }

    boolean isPairsOfOppositeStatesPalette() {
        throw unsupportedOperationException;
    }

    //
    //
    //

    boolean isSelectedProgramTimeDrivenProgram() {
        throw unsupportedOperationException;
    }

    void setSelectedProgramSteps(List<ProgramStepData> selectedProgramSteps) {
        throw unsupportedOperationException;
    }

    List<ProgramStepData> getSelectedProgramSteps() {
        throw unsupportedOperationException;
    }


    //

    MclnStatement getMclnProperty() {
        throw unsupportedOperationException;
    }

    String getMclnStatesPaletteName() {
        throw unsupportedOperationException;
    }

    MclnStatesNewPalette getMclnStatesPalette() {
        throw unsupportedOperationException;
    }

    void setMclnStatePalette(MclnStatesNewPalette mclnStatesPalette) {
        throw unsupportedOperationException;
    }

    boolean isSelectedAllowedStatesListValid() {
        throw unsupportedOperationException;
    }

    boolean isSelectedAllowedStatesListValid(List<MclnState> paletteSelectedAllowedStatesList) {
        throw unsupportedOperationException;
    }

    /**
     * Called when Assistant is going to show Property Allowed States Table filled
     * with states based on current selection in MclnStatesPaletteTableModel
     *
     * @param paletteSelectedAllowedStatesList
     */
    boolean updateCurrentAllowedStatesList(List<MclnState> paletteSelectedAllowedStatesList) {
        throw unsupportedOperationException;
    }

    MclnStatementState getMclnStatementStateByMclnStateID(Integer mclnStateID) {
        throw unsupportedOperationException;
    }

    String getComponentName() {
        throw unsupportedOperationException;
    }

    void setComponentName(String componentName) {
        throw unsupportedOperationException;
    }

    String getPropertyName() {
        throw unsupportedOperationException;
    }

    void setPropertyName(String propertyName) {
        throw unsupportedOperationException;
    }

    MclnStatementState getInitialMclnStatementState() {
        throw unsupportedOperationException;
    }

    MclnStatementState getMclnStatementStateByIndex(int index) {
        throw unsupportedOperationException;
    }

    void setProgramMclnStateCandidate(MclnStatementState programMclnStateCandidate) {
        throw unsupportedOperationException;
    }

    MclnStatementState getProgramMclnStateCandidate() {
        throw unsupportedOperationException;
    }

    void setInitialMclnState(MclnStatementState updatedInitialMclnStatementState) {
        throw unsupportedOperationException;
    }

    boolean propertyHasProgram() {
        throw unsupportedOperationException;
    }

    void setPropertyHasProgram(boolean hasProgram) {
        throw unsupportedOperationException;
    }

    boolean getSelectedProgramHasPhase() {
        throw unsupportedOperationException;
    }

    void setSelectedProgramHasPhase(boolean selectedProgramHasPhase) {
        throw unsupportedOperationException;
    }

    void swapProgramsOnTypeReselected() {
        throw unsupportedOperationException;
    }

    //
    //   Arc attributes getters and setters
    //

    public Polygon getArcArrowPoints() {
        throw unsupportedOperationException;
    }

    MclnArc getMclnArc() {
        throw unsupportedOperationException;
    }

    List<MclnStatementState> getArcAllowedStatesList() {
        throw unsupportedOperationException;
    }

    void setArcSelectedMclnState(MclnState selectedArcMclnState) {
        throw unsupportedOperationException;
    }

    MclnState getOriginalArcMclnState() {
        throw unsupportedOperationException;
    }

    MclnState getArcSelectedMclnState() {
        throw unsupportedOperationException;
    }

    boolean isEntityModified() {
        throw unsupportedOperationException;
    }
}
