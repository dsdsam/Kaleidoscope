package dsdsse.designspace.initializer;

import mcln.palette.MclnPaletteFactory;
import mcln.palette.MclnStatesNewPalette;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Admin on 3/16/2016.
 */
final class InitAssistantController {

    static final String WELCOME_PAGE_ID = "Welcome Page";
    static final String PALETTE_PAGE_ID = "Palette Page";
    static final String INIT_INTERPRETATION_PAGE_ID = "Init Interpretation Page";
    static final String INIT_INITIAL_STATE_PAGE_ID = "Init Initial State Page";
    static final String INPUT_GENERATOR_PAGE_ID = "Input Generator Page";
    static final String ARC_STATE_PAGE_ID = "Arc State Page";

    static enum EndInitializationRequest {Save, Cancel, InitAssistantClosing}

    static enum ControllerRequest {Next, Prev}

    static enum PageNavigationRequest {InitialPage, LeavingPage, NextPage}

    static enum InitializationPage {
        WelcomePage,
        PaletteAndAllowedStatesPage, InitInterpretationPage, initInitialStatePage, InitInputGeneratorPage,
        InitArcStatePage
    }

    static enum NoteID {
        IntroductoryNote, ToggleContext,
        SelectAllowedStates, InitInterpretation, initInitialState,
        InitTimeDrivenGenerator, InitRuleDrivenGenerator, PropertyCompleted, InitArcState
    }

    //
    //   I n s t a n c e
    //

    private InitAssistantDataModel initAssistantDataModel;
    private final Action prevPageAction = new PrevPageAction();
    private final Action nextPageAction = new NextPageAction();
    private final Action saveAction = new SaveAction();
    private final Action cancelAction = new CancelAction();
    private InitializationPage currentPage = InitializationPage.PaletteAndAllowedStatesPage;
    private final Action testGeneratorAction = new TestGeneratorAction();
    private final Action generatorTypeSelectionAction = new GeneratorTypeSelectionAction();

    //
    //   C r e a t i o n
    //

    /**
     * @param initAssistantDataModel
     */
    InitAssistantController(InitAssistantDataModel initAssistantDataModel) {
        this.initAssistantDataModel = initAssistantDataModel;
        if (initAssistantDataModel.isInitializingProperty()) {
            currentPage = InitializationPage.PaletteAndAllowedStatesPage;
        } else if (initAssistantDataModel.isInitializingArc()) {
            currentPage = InitializationPage.InitArcStatePage;
        } else {
            currentPage = InitializationPage.WelcomePage;
        }
    }

    /**
     *
     */
    void shutDownInitAssistant() {
        fireInitAssistantEndingInitializationRequest(EndInitializationRequest.Cancel);
    }

    /**
     *
     */
    void onInitAssistanceWindowClosing() {
        fireInitAssistantEndingInitializationRequest(EndInitializationRequest.InitAssistantClosing);
    }

    //
    //   L i s t e n e r s
    //

    private final List<ControllerRequestListener> controllerRequestListeners = new CopyOnWriteArrayList();
    private final List<EndInitializationRequestListener> endInitializationRequestListeners = new CopyOnWriteArrayList();
    private final List<ShowNoteRequestListener> showNoteRequestListeners = new CopyOnWriteArrayList();


    private final ItemListener paletteSelectionComboBoxItemListener = (e) -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedPaletteName = (String) e.getItem();
            selectedPaletteName = selectedPaletteName.replace("(default)", "").trim();
            MclnStatesNewPalette mclnStatesPalette = MclnPaletteFactory.getPaletteByName(selectedPaletteName);
            initAssistantDataModel.setMclnStatePalette(mclnStatesPalette);
        }
    };

    //   B u t t o n   L i s t e n e r s

    private class NextPageAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            doStateMachineTransition(ControllerRequest.Next);
        }
    }

    private class PrevPageAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            doStateMachineTransition(ControllerRequest.Prev);
        }
    }

    private class CancelAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            fireInitAssistantEndingInitializationRequest(EndInitializationRequest.Cancel);
        }
    }

    private class SaveAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            fireInitAssistantEndingInitializationRequest(EndInitializationRequest.Save);
        }
    }

    private final ItemListener programCheckBoxItemListener = (e) -> {
        initAssistantDataModel.setPropertyHasProgram(e.getStateChange() == ItemEvent.SELECTED);
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                    nextButton.setText(nextStr);
//                    propertyHasProgram = true;
//                    notePanel.setNote(2, step2of3Str);
//                    notePanel.repaint();
//            } else {
//                    nextButton.setText(applyStr);
//                    propertyHasProgram = false;
//                    notePanel.setNote(2, step2of3Str);
//                    notePanel.repaint();
//            }
    };

    /**
     * activated when Start/Stop Testing button clicked
     */
    private class TestGeneratorAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            fireShowNoteRequest(NoteID.ToggleContext);
        }
    }

    private class GeneratorTypeSelectionAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // switching current selection in Data Model
            initAssistantDataModel.swapProgramsOnTypeReselected();
            String actionCommand = e.getActionCommand();
            if (actionCommand.contains("Time Driven")) {
                fireShowNoteRequest(NoteID.InitTimeDrivenGenerator);
            } else if (actionCommand.contains("State Driven")) {
                fireShowNoteRequest(NoteID.InitRuleDrivenGenerator);
            }
        }
    }

    final void addListener(ControllerRequestListener controllerRequestListener) {
        controllerRequestListeners.add(controllerRequestListener);
    }

    final void addListener(EndInitializationRequestListener endInitializationRequestListener) {
        endInitializationRequestListeners.add(endInitializationRequestListener);
    }

    final void addListener(ShowNoteRequestListener showNoteRequestListener) {
        showNoteRequestListeners.add(showNoteRequestListener);
    }

    public Action getPrevPageAction() {
        return prevPageAction;
    }

    public Action getNextPageAction() {
        return nextPageAction;
    }

    public Action getSaveAction() {
        return saveAction;
    }

    public Action getCancelAction() {
        return cancelAction;
    }

    public ItemListener getPaletteSelectionComboBoxItemListener() {
        return paletteSelectionComboBoxItemListener;
    }

    public ItemListener getProgramCheckBoxItemListener() {
        return programCheckBoxItemListener;
    }

    public Action getTestGeneratorAction() {
        return testGeneratorAction;
    }

    public Action getGeneratorTypeSelectionAction() {
        return generatorTypeSelectionAction;
    }

    //
    //   S t a t e   t r a n s i t i o n s
    //

    void setInitialPage() {
        if (!initAssistantDataModel.isAssistantInitialized()) {
            firePageNavigationRequest(PageNavigationRequest.InitialPage, InitializationPage.WelcomePage);
            fireShowNoteRequest(NoteID.IntroductoryNote);
        } else if (initAssistantDataModel.isInitializingProperty()) {
//            firePageNavigationRequest(PageNavigationRequest.InitialPage, InitializationPage.WelcomePage);
//            fireShowNoteRequest(NoteID.IntroductoryNote);
            firePageNavigationRequest(PageNavigationRequest.InitialPage, InitializationPage.PaletteAndAllowedStatesPage);
            fireShowNoteRequest(NoteID.SelectAllowedStates);
        } else if (initAssistantDataModel.isInitializingArc()) {
//            firePageNavigationRequest(PageNavigationRequest.InitialPage, InitializationPage.WelcomePage);
//            fireShowNoteRequest(NoteID.IntroductoryNote);
            firePageNavigationRequest(PageNavigationRequest.InitialPage, InitializationPage.InitArcStatePage);
            fireShowNoteRequest(NoteID.InitArcState);
        }
    }

    /**
     * Steps State Machine provides steps transition logic and invokes actions
     *
     * @param direction
     */
    private void doStateMachineTransition(ControllerRequest direction) {

        firePageNavigationRequest(PageNavigationRequest.LeavingPage, currentPage);

        switch (currentPage) {
            case PaletteAndAllowedStatesPage:
                if (direction == ControllerRequest.Next) {
                    if (initAssistantDataModel.isInitializingProperty()) {
                        if (!initAssistantDataModel.isSelectedAllowedStatesListValid()) {
//                            initAssistantDataModel.fireAdfDataModelChanged(InitAssistantDataModel.AttributeID.AllowedStatesChoiceChanged, false);
//                            initAssistantDataModel.showInfoMessage(
//                                    InitAssistantMessagesAndDialogs.MESSAGE_AVAILABLE_STATES_NOT_SELECTED);
                            return;
                        }
//                        if (!initAssistantDataModel.updateCurrentAllowedStatesListOnPaletItemSelected(paletteSelectedAllowedStatesList)) {
//                            fireAdfDataModelChanged(AttributeID.AllowedStatesChoiceChanged, false);
//                            showInfoMessage(InitAssistantMessagesAndDialogs.MESSAGE_AVAILABLE_STATES_NOT_SELECTED);
//                           return;
//                        }
//                        if (!initAssistantDataModel.isPageValid(InitializationPage.PaletteAndAllowedStatesPage)) {
//                            InitAssistantMessagesAndDialogs.
//                                    showMessagePopup(InitAssistantMessagesAndDialogs.MESSAGE_AVAILABLE_STATES_NOT_SELECTED);
//                            return;
//                        }
                        currentPage = InitializationPage.InitInterpretationPage;
                        fireShowNoteRequest(NoteID.InitInterpretation);

                    } else if (initAssistantDataModel.isInitializingArc()) {
                        currentPage = InitializationPage.InitArcStatePage;
                        fireShowNoteRequest(NoteID.InitArcState);
                    }
                    firePageNavigationRequest(PageNavigationRequest.NextPage, currentPage);

                }
                break;
            case InitInterpretationPage:
                if (direction == ControllerRequest.Next) {
                    currentPage = InitializationPage.initInitialStatePage;
                    fireShowNoteRequest(NoteID.initInitialState);
                } else {
                    currentPage = InitializationPage.PaletteAndAllowedStatesPage;
                    fireShowNoteRequest(NoteID.SelectAllowedStates);
                }
                firePageNavigationRequest(PageNavigationRequest.NextPage, currentPage);
                break;
            case initInitialStatePage:
                if (direction == ControllerRequest.Next) {
                    currentPage = InitializationPage.InitInputGeneratorPage;
                    fireShowNoteRequest(NoteID.InitTimeDrivenGenerator);
                } else {
                    currentPage = InitializationPage.InitInterpretationPage;
                    fireShowNoteRequest(NoteID.InitInterpretation);
                }
                firePageNavigationRequest(PageNavigationRequest.NextPage, currentPage);
                break;
            case InitInputGeneratorPage:
                if (direction == ControllerRequest.Prev) {
                    currentPage = InitializationPage.initInitialStatePage;
                    fireShowNoteRequest(NoteID.initInitialState);
                    firePageNavigationRequest(PageNavigationRequest.NextPage, currentPage);
                }
                break;
            case InitArcStatePage:
                if (direction == ControllerRequest.Prev) {
                    currentPage = InitializationPage.PaletteAndAllowedStatesPage;
                    firePageNavigationRequest(PageNavigationRequest.NextPage, currentPage);
                }
                break;
        }
    }

    final boolean isCurrentStepInitInterpretationPage() {
        return currentPage == InitializationPage.InitInterpretationPage;
    }

    final boolean isCurrentStepInitialState() {
        return currentPage == InitializationPage.initInitialStatePage;
    }

    final boolean isCurrentStepInitInputGeneratingProgramPage() {
        return currentPage == InitializationPage.InitInputGeneratorPage;
    }

    final boolean isCurrentStepInitArcState() {
        return currentPage == InitializationPage.InitArcStatePage;
    }

    //
    //   E v e n t   f i r e
    //

    private final void firePageNavigationRequest(PageNavigationRequest operation, InitializationPage attribute) {
        for (ControllerRequestListener controllerRequestListener : controllerRequestListeners) {
            controllerRequestListener.onControllerRequest(this, initAssistantDataModel, operation, attribute);
        }
    }

    private final void fireInitAssistantEndingInitializationRequest(EndInitializationRequest requestID) {
        for (EndInitializationRequestListener endInitializationRequestListener : endInitializationRequestListeners) {
            endInitializationRequestListener.onISaveOrCancelControllerRequest(initAssistantDataModel, requestID);
        }
    }

    private final void fireShowNoteRequest(NoteID noteID) {
        for (ShowNoteRequestListener showNoteRequestListener : showNoteRequestListeners) {
            showNoteRequestListener.showNote(noteID);
        }
    }
}
