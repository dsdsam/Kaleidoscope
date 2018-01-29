package dsdsse.designspace.initializer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 3/16/2016.
 */
final class InitAssistantCardsPanel extends JPanel {

    private static final int PANEL_HEIGHT = 90;
    private static final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

    private final Color backgroundColor = new Color(0xFFFF00);

    private CardLayout cardLayout;
    private final InitAllowedStatesPage initAllowedStatesPage;
    private final InitAllowedStatesInterpretationsPage initAllowedStatesInterpretationsPage;
    private final InitInitialStatePage initInitialStatePage;
    private final InputGeneratorSetupPanel inputGeneratorSetupPanel;
    private final ArcStateSetupPanel arcStateSetupPanel;

    private final ControllerRequestListener controllerRequestListener =
            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.PageNavigationRequest operation,
             InitAssistantController.InitializationPage pageID) -> {
                switch (operation) {
                    case InitialPage:
                    case NextPage:
                        if (pageID == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
                            cardLayout.show(InitAssistantCardsPanel.this, InitAssistantController.PALETTE_PAGE_ID);
                        } else if (pageID == InitAssistantController.InitializationPage.InitInterpretationPage) {
                            cardLayout.show(InitAssistantCardsPanel.this,
                                    InitAssistantController.INIT_INTERPRETATION_PAGE_ID);
                        } else if (pageID == InitAssistantController.InitializationPage.initInitialStatePage) {
                            cardLayout.show(InitAssistantCardsPanel.this,
                                    InitAssistantController.INIT_INITIAL_STATE_PAGE_ID);
                        } else if (pageID == InitAssistantController.InitializationPage.InitInputGeneratorPage) {
                            cardLayout.show(InitAssistantCardsPanel.this,
                                    InitAssistantController.INPUT_GENERATOR_PAGE_ID);
                        } else if (pageID == InitAssistantController.InitializationPage.InitArcStatePage) {
                            cardLayout.show(InitAssistantCardsPanel.this, InitAssistantController.ARC_STATE_PAGE_ID);
                        }
                        break;
                }

            };


//    private final ControllerRequestListener controllerRequestListener =
//            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
//             InitAssistantController.ControllerRequest operation,
//             InitAssistantController.InitializationPage nextPage) -> {
//                switch (operation) {
//                    case Next:
//                        if (nextPage == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
//                            cardLayout.next(InitAssistantCardsPanel.this);
//                        } else if (nextPage == InitAssistantController.InitializationPage.InitArcStatePage) {
//                            cardLayout.show(InitAssistantCardsPanel.this, ArcStateSetupPanel.ARC_STATE_PAGE_ID);
//                        } else {
//                            cardLayout.next(InitAssistantCardsPanel.this);
//                        }
//                        break;
//                    case Prev:
//                        cardLayout.previous(InitAssistantCardsPanel.this);
//                        break;
//                }
//
//            };

    public ControllerRequestListener getControllerRequestListener() {
        return controllerRequestListener;
    }

    //   C r e a t i o n

    InitAssistantCardsPanel(InitAllowedStatesPage initAllowedStatesPage,
                            InitAllowedStatesInterpretationsPage initAllowedStatesInterpretationsPage,
                            InitInitialStatePage initInitialStatePage,
                            InputGeneratorSetupPanel inputGeneratorSetupPanel ) {
        this(true, initAllowedStatesPage, initAllowedStatesInterpretationsPage, initInitialStatePage,
                inputGeneratorSetupPanel, null);
    }

    InitAssistantCardsPanel(InitAllowedStatesPage initAllowedStatesPage, ArcStateSetupPanel arcStateSetupPanel) {
        this(false, initAllowedStatesPage, null, null, null, arcStateSetupPanel);
    }

    /**
     * @param initializingProperty
     * @param initAllowedStatesPage
     * @param initAllowedStatesInterpretationsPage
     * @param initInitialStatePage
     * @param inputGeneratorSetupPanel
     * @param arcStateSetupPanel
     */
    private InitAssistantCardsPanel(boolean initializingProperty, InitAllowedStatesPage initAllowedStatesPage,
                                    InitAllowedStatesInterpretationsPage initAllowedStatesInterpretationsPage,
                                    InitInitialStatePage initInitialStatePage,
                                    InputGeneratorSetupPanel inputGeneratorSetupPanel,
                                    ArcStateSetupPanel arcStateSetupPanel) {
        this.initAllowedStatesPage = initAllowedStatesPage;
        this.initAllowedStatesInterpretationsPage = initAllowedStatesInterpretationsPage;
        this.initInitialStatePage = initInitialStatePage;
        this.inputGeneratorSetupPanel = inputGeneratorSetupPanel;
        this.arcStateSetupPanel = arcStateSetupPanel;

        setBackground(backgroundColor);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        if (initializingProperty) {
        add(initAllowedStatesPage, InitAssistantController.PALETTE_PAGE_ID);
        add(initAllowedStatesInterpretationsPage, InitAssistantController.INIT_INTERPRETATION_PAGE_ID);
        add(initInitialStatePage, InitAssistantController.INIT_INITIAL_STATE_PAGE_ID);
        add(inputGeneratorSetupPanel, InitAssistantController.INPUT_GENERATOR_PAGE_ID);
        } else {
        add(arcStateSetupPanel, InitAssistantController.ARC_STATE_PAGE_ID);
        }

        if (initializingProperty) {
            cardLayout.show(InitAssistantCardsPanel.this, InitAssistantController.PALETTE_PAGE_ID);
        } else {
            cardLayout.show(InitAssistantCardsPanel.this, InitAssistantController.ARC_STATE_PAGE_ID);
        }


        cardLayout.first(this);

        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);
        setPreferredSize(PANEL_SIZE);
    }
}
