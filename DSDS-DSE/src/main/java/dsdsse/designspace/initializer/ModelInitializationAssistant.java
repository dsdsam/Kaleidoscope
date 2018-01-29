package dsdsse.designspace.initializer;

import adf.ui.components.dialogs.undecorated.AdfDialogClosingCallback;
import adf.ui.components.dialogs.undecorated.AdfUndecoratedDialog;
import dsdsse.app.*;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.graphview.MclnArcView;
import dsdsse.graphview.MclnPropertyView;
import dsdsse.designspace.DesignSpaceContentManager;
import mcln.model.*;
import mcln.palette.MclnPaletteFactory;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Admin on 3/1/2016.
 */
public final class ModelInitializationAssistant extends JPanel {

    private static final Logger logger = Logger.getLogger(ModelInitializationAssistant.class.getName());

    private static final String[] OPTIONS = {"Save Changes", "Discard Changes", "Continue Initializing"};

    private static final int USER_CHOICE_DIALOG_CLOSED = -1;
    private static final int USER_CHOICE_LEFT_BUTTON = 0;
    private static final int USER_CHOICE_MIDDLE_BUTTON = 1;
    private static final int USER_CHOICE_RIGHT_BUTTON = 2;

    private static final boolean WINDOW_MODALITY = false;

    private static final int IA_WIDTH = 600;
    private static final int IA_MIN_HEIGHT = 500;
    private static final int IA_PREF_HEIGHT = 500;
    private static final Dimension IA_MIN_SIZE = new Dimension(IA_WIDTH, IA_MIN_HEIGHT);
    private static final Dimension IA_PREF_SIZE = new Dimension(IA_WIDTH, IA_PREF_HEIGHT);

    private static Point lastLocationOnScreen;

    private final InitAssistantTitleBar initAssistantTitleBar;

    private JFrame mainFrame;
    private MclnPropertyView mclnPropertyView;
    private MclnArcView mclnArcView;
    private final InitAssistantDataModel initAssistantDataModel;
    private InitAssistantController initAssistantController;
    private AdfUndecoratedDialog adfUndecoratedDialog;

    //
    //   C r e a t i o n   o f   t h e   i n s t a n c e
    //

    AdfDialogClosingCallback adfDialogClosingCallback = new AdfDialogClosingCallback() {
        @Override
        public void windowClosing(WindowEvent e) {
            initAssistantController.onInitAssistanceWindowClosing();
            InitAssistantMessagesAndDialogs.setPopupParentComponent(null);
            if (adfUndecoratedDialog != null) {
                lastLocationOnScreen = adfUndecoratedDialog.getLocationOnScreen();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
            InitAssistantInterface.cleanupOnInitAssistantClosed();
            adfUndecoratedDialog = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n\n");
            stringBuilder.append("*********************************************************************\n");
            stringBuilder.append("*   I n i t i a l i z a t i o n   A s s i s t a n t   C l o s e d   *\n");
            stringBuilder.append("*********************************************************************");
            logger.severe(stringBuilder.toString());

        }
    };

    EndInitializationRequestListener endInitializationRequestListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.EndInitializationRequest requestID) -> {
                switch (requestID) {
                    case Cancel:
                        if (initAssistantDataModel.isInitializingProperty()) {
                            resetInitAssistantToFrontPageIfPossible(
                                    AllMessages.CANCELLING_PROPERTY_IN_INIT_ASSISTANT_QUESTION.getText());
                        }
                        if (initAssistantDataModel.isInitializingArc()) {
                            resetInitAssistantToFrontPageIfPossible(
                                    AllMessages.CANCELLING_ARC_IN_ASSISTANT_QUESTION.getText());
                        }
//                        if (canInitializationBeCanceled()) {
//                            resetToFrontPageView();
//                            AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
//                                    AppStateModel.OperationStep.INIT_ASSISTANT_IS_IDLING);
//                        }
                        break;
                    case Save:
                        System.out.println("Save ! Save ! " + requestID);
                        initializeSelectedElement();
                        resetToFrontPageView();
                        AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
                                AppStateModel.OperationStep.INIT_ASSISTANT_IS_IDLING);
                        break;
                }
            };


    //
    //   C r e a t i o n
    //

    /**
     *
     */
    ModelInitializationAssistant() {
        mclnPropertyView = null;
        mclnArcView = null;
        initAssistantDataModel = new InitAssistantDataModel();
        initAssistantTitleBar = DsdsseUserPreference.isInitAssistantEmbedded() ? null : new InitAssistantTitleBar();
    }

    /**
     *
     */
    void initWelcomeView() {
        mclnPropertyView = null;
        mclnArcView = null;

        // creating InitAssistantDataModel and InitAssistantController
        initAssistantDataModel.resetDataHolder(null);
        initAssistantController = new InitAssistantController(initAssistantDataModel);
        initAssistantController.addListener(endInitializationRequestListener);

        InitAssistantViewBuilder.buildViewLayout(this, initAssistantController, initAssistantDataModel);
        initAssistantController.setInitialPage();
    }


    /**
     * Resets Init Assistant to Initial Front Page after initialization process finished or canceled
     */
    public void resetToFrontPageView() {
        initWelcomeView();
        // Updating Title
        if (DsdsseUserPreference.isInitAssistantEmbedded()) {
            EmbeddedAssistantHolderPanel embeddedAssistantHolderPanel = EmbeddedAssistantHolderPanel.getInstance();
            embeddedAssistantHolderPanel.extendTitle("Brief Introduction");
        } else {
            initAssistantTitleBar.extendHeaderText("Brief Introduction");
        }
        AppController.getInstance().unlockInitAssistant();
    }

    /**
     * @param mclnPropertyView
     */
    void initPropertyView(MclnPropertyView mclnPropertyView) {
        this.mclnPropertyView = mclnPropertyView;
        mclnArcView = null;
        MclnStatement mclnProperty = mclnPropertyView.getTheElementModel();
        String mclnStatesPaletteName = mclnProperty.getMclnStatesPaletteName();
        MclnStatesNewPalette mclnStatesPalette = MclnPaletteFactory.getPaletteByName(mclnStatesPaletteName);

        // creating InitAssistantDataModel and InitAssistantController
        PropertyDataHolder propertyDataHolder =
                PropertyDataHolder.createPropertyDataHolder(mclnStatesPalette, mclnPropertyView);
        initAssistantDataModel.resetDataHolder(propertyDataHolder);// = new InitAssistantDataModel(propertyDataHolder);
        initAssistantController = new InitAssistantController(initAssistantDataModel);
        initAssistantController.addListener(endInitializationRequestListener);

        InitAssistantViewBuilder.buildViewLayout(this, initAssistantController, initAssistantDataModel);
        initAssistantController.setInitialPage();
    }

    /**
     * @param mclnPropertyView
     */
    public void resetToInitializePropertyView(MclnPropertyView mclnPropertyView) {
        initPropertyView(mclnPropertyView);
        // Updating Title
        if (DsdsseUserPreference.isInitAssistantEmbedded()) {
            EmbeddedAssistantHolderPanel embeddedAssistantHolderPanel = EmbeddedAssistantHolderPanel.getInstance();
            embeddedAssistantHolderPanel.extendTitle("Property", mclnPropertyView.getUID());
        } else {
            initAssistantTitleBar.extendHeaderText("Property", mclnPropertyView.getUID());
        }
    }


    /**
     * @param mclnArcView
     */
    void initArcView(MclnArcView mclnArcView) {
        mclnPropertyView = null;
        this.mclnArcView = mclnArcView;
        MclnStatement mclnProperty;
        if (mclnArcView.getInpNode() instanceof MclnPropertyView) {
            MclnPropertyView inputPropertyView = mclnArcView.getInpNode().toPropertyView();
            mclnProperty = inputPropertyView.getTheElementModel();
        } else {
            MclnPropertyView outputPropertyView = mclnArcView.getOutNode().toPropertyView();
            mclnProperty = outputPropertyView.getTheElementModel();
        }
        String mclnStatesPaletteName = mclnProperty.getMclnStatesPaletteName();
        MclnStatesNewPalette mclnStatesPalette = MclnPaletteFactory.getPaletteByName(mclnStatesPaletteName);

        // creating InitAssistantDataModel and InitAssistantController
        ArcDataHolder arcDataHolder = ArcDataHolder.createArcDataHolder(mclnStatesPalette, mclnProperty, mclnArcView);
        initAssistantDataModel.resetDataHolder(arcDataHolder);
        initAssistantController = new InitAssistantController(initAssistantDataModel);
        initAssistantController.addListener(endInitializationRequestListener);

        InitAssistantViewBuilder.buildViewLayout(this, initAssistantController, initAssistantDataModel);
        initAssistantController.setInitialPage();
    }

    /**
     * @param mclnArcView
     */
    void resetToInitializeArcView(MclnArcView mclnArcView) {
        initArcView(mclnArcView);
        // Updating Title
        if (DsdsseUserPreference.isInitAssistantEmbedded()) {
            EmbeddedAssistantHolderPanel embeddedAssistantHolderPanel = EmbeddedAssistantHolderPanel.getInstance();
            embeddedAssistantHolderPanel.extendTitle("Arc", mclnArcView.getUID());
        } else {
            initAssistantTitleBar.extendHeaderText("Arc", mclnArcView.getUID());
        }
    }

    InitAssistantDataModel getInitAssistantDataModel() {
        return initAssistantDataModel;
    }

    public InitAssistantController getInitAssistantController() {
        return initAssistantController;
    }

    /**
     * @param mainFrame
     */
    void showInitializationAssistant(JFrame mainFrame) {
        if (DsdsseUserPreference.isInitAssistantEmbedded()) {
            initEmbeddedAssistant(IA_MIN_SIZE, IA_PREF_SIZE);
        } else {
            initAndOpenWizard(mainFrame, WINDOW_MODALITY, IA_MIN_SIZE, IA_PREF_SIZE);
        }
    }

    private void initEmbeddedAssistant(Dimension minSize, Dimension prefSize) {

        EmbeddedAssistantHolderPanel embeddedAssistantHolderPanel = EmbeddedAssistantHolderPanel.createInstance(this);

        if (initAssistantDataModel.isInitializingProperty()) {
            String uID = initAssistantDataModel.getMclnProperty().getUID();
            embeddedAssistantHolderPanel.extendTitle("Property", uID);
        } else if (initAssistantDataModel.isInitializingArc()) {
            String uID = initAssistantDataModel.getMclnArc().getUID();
            embeddedAssistantHolderPanel.extendTitle("Arc", uID);
        } else {
            embeddedAssistantHolderPanel.extendTitle("Brief Introduction");
        }

        this.setMinimumSize(minSize);
        this.setPreferredSize(prefSize);
        DesignSpaceContentManager.setEastPanel(embeddedAssistantHolderPanel);
    }

    /**
     * @param mainFrame
     * @param modal
     * @param prefSize
     */
    private void initAndOpenWizard(JFrame mainFrame, boolean modal, Dimension minSize, Dimension prefSize) {


        adfUndecoratedDialog = new InitAssistantUndecoratedDialog(mainFrame, modal, true, initAssistantTitleBar,
                adfDialogClosingCallback);
        InitAssistantMessagesAndDialogs.setPopupParentComponent(adfUndecoratedDialog);

        adfUndecoratedDialog.setMinimumSize(minSize);
        adfUndecoratedDialog.setPreferredSize(prefSize);

        adfUndecoratedDialog.addAppPanel(this);
//        adfUndecoratedDialog.getContentPane().setBackground(InitAssistantFactory.CONTAINER_PANEL_BACKGROUND);

//        // adding popup menu
//        AdfUndecoratedDialogPopupMenu adfUndecoratedDialogPopupMenu = new AdfUndecoratedDialogPopupMenu(adfUndecoratedDialog);
//        adfUndecoratedDialogPopupMenu.setLightWeightPopupEnabled(false);
//        ((JComponent) adfUndecoratedDialog.getContentPane()).setComponentPopupMenu(adfUndecoratedDialogPopupMenu);

        bringUp(mainFrame, adfUndecoratedDialog);

        //  extending header with element name and UID

        if (initAssistantDataModel.isInitializingProperty()) {
            String uID = initAssistantDataModel.getMclnProperty().getUID();
            initAssistantTitleBar.extendHeaderText("Property", uID);
        } else if (initAssistantDataModel.isInitializingArc()) {
            String uID = initAssistantDataModel.getMclnArc().getUID();
            initAssistantTitleBar.extendHeaderText("Arc", uID);
        } else {
            initAssistantTitleBar.extendHeaderText("Brief Introduction");
        }
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
    }

    /**
     * @param mainFrame
     * @param adfUndecoratedDialog
     */
    private void bringUp(JFrame mainFrame, AdfUndecoratedDialog adfUndecoratedDialog) {
        this.mainFrame = mainFrame;
        if (lastLocationOnScreen != null) {
            adfUndecoratedDialog.setLocation(lastLocationOnScreen);
        } else {
            Dimension appSize = mainFrame.getSize();
            Dimension assistantSize = adfUndecoratedDialog.getSize();
            int x = (int) ((appSize.getWidth() - assistantSize.getWidth()) / 2);
            int y = (int) ((appSize.getHeight() - assistantSize.getHeight()) / 2);
            if (x >= 0 && y >= 0) {
                adfUndecoratedDialog.setLocationRelativeTo(mainFrame);
            } else {
                adfUndecoratedDialog.setLocationRelativeTo(null);
            }
        }
        adfUndecoratedDialog.bringUp();
    }

    //
    //   I n i t i a l i z a t i o n   c o m p l e t i o n   m e t h o d s
    //

    //
    // The methods is called when selected entity initialization is complete
    // It populates the entity with initialization attributes entered,
    // Then entity is saved into MclnModel and updated on the screen.
    //

    void initializeSelectedElement() {
        if (initAssistantDataModel.isInitializingProperty()) {
            populatePropertyFromDataModel(initAssistantDataModel.getMclnProperty());
        } else if (initAssistantDataModel.isInitializingArc()) {
            populateArcFromDataModel(initAssistantDataModel.getMclnArc());
        }
    }

    /**
     * @param mclnStatement
     */
    private boolean populatePropertyFromDataModel(MclnStatement mclnStatement) {

        mclnStatement.setRuntimeInitializationUpdatedFlag();

        String componentName = initAssistantDataModel.getComponentName();
        String propertyName = initAssistantDataModel.getPropertyName();

        mclnStatement.setSubject(componentName);
        mclnStatement.setPropertyName(propertyName);

        // updating Available Mcln Statement States

        MclnStatesNewPalette mclnStateNewPalette = initAssistantDataModel.getMclnStatesPalette();
        List<MclnStatementState> propertyAllowedStatesAsList = initAssistantDataModel.getCurrentAllowedStatesList();
        MclnStatementState initialMclnStatementState = initAssistantDataModel.getInitialMclnStatementState();

        AvailableMclnStatementStates availableMclnStatementStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStateNewPalette.getPaletteName(),
                        propertyAllowedStatesAsList, initialMclnStatementState);

        mclnStatement.setAvailableMclnStatementStates(availableMclnStatementStates);

        // updating Input Generating Program

        boolean propertyHasProgram = initAssistantDataModel.propertyHasProgram();
        if (!propertyHasProgram) {
            MclnProject.removeInputGeneratingProgram(mclnStatement);
            // repainting Property view
            mclnPropertyView.repaintPropertyUponInitialization();
            return true;
        }

        List<ProgramStepData> programStepData = initAssistantDataModel.getSelectedProgramSteps();
        if (programStepData == null) {
            initAssistantDataModel.showInfoMessage("Program steps not created");
            return false;
        }

        boolean programHasPhase = initAssistantDataModel.getSelectedProgramHasPhase();
        MclnProject.updateInputGeneratingProgram(mclnStatement, programHasPhase,
                initAssistantDataModel.isSelectedProgramTimeDrivenProgram(), programStepData);

        // repainting Property view
        mclnPropertyView.repaintPropertyUponInitialization();
        return true;
    }

    private void populateArcFromDataModel(MclnArc mclnArc) {

        mclnArc.setRuntimeInitializationUpdatedFlag();

        MclnState selectedArcMclnState = initAssistantDataModel.getArcSelectedMclnState();
        mclnArc.setArcMclnState(selectedArcMclnState);
        // repainting Arc Arrow view
        mclnArcView.repaintArrowUponInitialization();

    }


    //
    //   R e s e t   or   S h u t d o w n   I n i t i a l i z a t i o n   A s s i s t a n t
    //

    /*
       Here are the cases when Init Assistant initialization can be interrupted.
       1) User may click button Cancel on the Control Buttons Panel.
          This may happen when Init Assistant is either Standalone or Embedded.
          Result: Init Assistant is set to Front Page If user is OK to Save or
          Discard change. Otherwise initialization continues.

       2) User may click "Close" button on the Header of Init Assistant panel.
          This may happen when Init Assistant is either Standalone or Embedded.
          Result: Init Assistant is closed If user is OK to Save or Discard change.
          Otherwise initialization continues.

       3) 2 cases when Init Assistant is embedded. User is allowed to click Settings,
          Help or Exit Menu/Icons when Init Assistant is initializing or in Front Page state.
          Case 1: User clicks Menu/Icons when Init Assistant is initializing.
          Result: Init Assistant is Closed If user OK to Save or Discard change.
          Otherwise initialization continues.
          Case 2: User clicks Menu/Icons when Init Assistant is in Front Page state.
          Result: Init Assistant is Closed unconditionally.

       4) 2 cases when Init Assistant is Standalone.
          Case 1: User may click Exit menu when Init Assistant is initializing.
          Result: Init Assistant is Closed If user OK to Save or
          Discard change. Otherwise initialization continues.
          Case 2: User clicks Exit menu when Init Assistant is in Front Page state.
          Result: Init Assistant is Closed unconditionally.

          Cases 2, 3 and 4 are similar.

       5) User might want to replace currently initialised component
          with another



          Operations that affect IA only
1)       Cancel – SA and EB (inside IA)  Check and Front

2)       Close – SA and EB   (Con – INT - IA)  Check and Destroy

3)       Replace – SA and EB (Con – INT - IA)
         Check canInitAssistantBeInterrupted and Replace

4)       Release right space – EB (Con – INT - IA) Check and Destroy + action (open setup or help)

5)       Close  to switch to Print – case 2 + action (switch to print layout)\
         Print can be open only when embedded IA is in Front Page state

6)       Exit - case 2 + action (close app)

7)       Switch to Sim - case 2 + action (Switch)
         Switching to simulation mode is allowed only when Init Assistant is not initializing.
         Open idling Init Assistant will not be closed when switching to Sim mode.

8)  shutdownInitAssistantIfPossible called to release the East Panel Space when Presentation is started

The operations may be executed only at particular states;

Opening or Replacing is done via RMB because LMB is used by operation
It can be done between dev operations because
When operation is started IA icon and RMB IA item are disabled.
In reverse: When initialization is started all dev operations are disabled
     */

    public boolean canInitAssistantBeInterrupted(String message) {
        if (isInitAssistantInitializingAndEntityIsModified() &&
                DsdsseUserPreference.isUserPreferenceConfirmationPolicy()) {
            Component background = DesignSpaceView.getInstance();
            int choice = DsdsDseMessagesAndDialogs.threeOptionsDialog(background, message, "Confirmation", OPTIONS);
            if (choice == USER_CHOICE_DIALOG_CLOSED) {
                return false;
            } else if (choice == USER_CHOICE_LEFT_BUTTON) {
                // Save entity and OK to interrupt initialization
                initializeSelectedElement();
                return true;
            } else if (choice == USER_CHOICE_MIDDLE_BUTTON) {
                // Discard modification and OK to interrupt initialization
                return true;
            } else if (choice == USER_CHOICE_RIGHT_BUTTON) {
                // Ignore request to interrupt initialization
                return false;
            }
        }
        return true;
    }

    /**
     * The method is used by Cancel button only
     * <p>
     * The method is used to interrupt initialization and set
     * Init Assistant into idling Front Page mode.
     * <p>
     * The method resets Initialization Assistant to front page if possible.
     * Otherwise user is asked permission to interrupt or continue initialization.
     *
     * @return true when Initialization Assistant was reset, false otherwise.
     */
    private boolean resetInitAssistantToFrontPageIfPossible(String question) {
        if (!canInitAssistantBeInterrupted(question)) {
            return false;
        }
        resetToFrontPageView();
        AppStateModel.getInstance().setNewEditingOperation(AppStateModel.Operation.INITIALIZATION,
                AppStateModel.OperationStep.INIT_ASSISTANT_IS_IDLING);
        return true;
    }

    /**
     *
     */
    final void shutDownInitAssistantUnconditionally() {
        if (DsdsseUserPreference.isInitAssistantEmbedded()) {
            EmbeddedAssistantHolderPanel.destroyInstance();
        } else {
            adfUndecoratedDialog.fadeOutAndDestroy();
        }
    }

    //   M o d i f i c a t i o n   c h e c k

    private boolean isInitAssistantInitializingAndEntityIsModified() {
        return (mclnPropertyView != null || mclnArcView != null) && isEntityModified();
    }

    private boolean isEntityModified() {
        boolean modified = initAssistantDataModel.isEntityModified();
        return modified;
    }
}
