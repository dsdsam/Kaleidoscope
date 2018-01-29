package dsdsse.designspace.initializer;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Admin on 2/16/2017.
 */
class InitAssistantViewBuilder {

    private static final int TOP_PANEL_HEIGHT = 200;
    private static final Dimension TOP_PANEL_SIZE = new Dimension(1, TOP_PANEL_HEIGHT);
    private static final Dimension INPUT_PANEL_SIZE = new Dimension(1, TOP_PANEL_HEIGHT);
    private static final Dimension NOTE_PANEL_SIZE = new Dimension(280, TOP_PANEL_HEIGHT);

    /**
     * Access point
     *
     * @param initAssistant
     * @param initAssistantController
     * @param initAssistantDataModel
     */
    static final void buildViewLayout(ModelInitializationAssistant initAssistant,
                                      InitAssistantController initAssistantController,
                                      InitAssistantDataModel initAssistantDataModel) {

        if (!initAssistantDataModel.isAssistantInitialized()) {
            initWelcomePageLayout(initAssistant, initAssistantController);
        } else {
            buildInitializationLayout(initAssistant, initAssistantController, initAssistantDataModel);
        }
        initAssistant.revalidate();
    }

    /**
     * @param initAssistant
     * @param initAssistantController
     */
    private static final void initWelcomePageLayout(ModelInitializationAssistant initAssistant,
                                                    InitAssistantController initAssistantController) {

        initAssistant.removeAll();

        JPanel initAssistantContentPanel = new JPanel(new GridBagLayout());
//        initAssistantController.addListener(initAssistant.getEndInitializationRequestListener());
//        InitAssistantButtonsPanel initAssistantButtonsPanel =
//                new InitAssistantButtonsPanel(initAssistantController.getCancelAction());
//        initAssistantController.addListener(initAssistantButtonsPanel.getControllerRequestListener());
        initAssistantContentPanel.setLayout(new BorderLayout());
        initAssistant.setLayout(new GridBagLayout());
        initAssistantContentPanel.add(InitAssistantFrontPagePanel.getInstance());

        initAssistant.add(initAssistantContentPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

//        initAssistant.add(initAssistantButtonsPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
//                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * @param initAssistant
     * @param initAssistantController
     * @param initAssistantDataModel
     * @return
     */
    private static final void buildInitializationLayout(ModelInitializationAssistant initAssistant,
                                                        InitAssistantController initAssistantController,
                                                        InitAssistantDataModel initAssistantDataModel) {

        initAssistant.removeAll();

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(0xEEEEEE)); //238
        topPanel.setBackground(Color.RED);

        Color MCLN_STATE_CREATION_COLOR = new Color(0xC0C0C0);
        String HEX = String.format("0x%X", (MCLN_STATE_CREATION_COLOR.getRGB() & 0xFFFFFF));

        topPanel.setPreferredSize(TOP_PANEL_SIZE);
        topPanel.setMinimumSize(TOP_PANEL_SIZE);
        topPanel.setMaximumSize(TOP_PANEL_SIZE);

//        JPanel notePanel = new JPanel(new BorderLayout());
//        notePanel.setOpaque(true);
//        notePanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
//        notePanel.setBackground(new Color(0xFFFFEE));
//        notePanel.setPreferredSize(NOTE_PANEL_SIZE);
//        notePanel.setMinimumSize(NOTE_PANEL_SIZE);
//        notePanel.setMaximumSize(NOTE_PANEL_SIZE);

        NoteAreaHolderPanel noteAreaHolderPanel;

        JPanel inputAreaPanel = new JPanel(new BorderLayout());
        inputAreaPanel.setOpaque(true);
        inputAreaPanel.setBackground(new Color(0xD2D2D2));
        inputAreaPanel.setPreferredSize(INPUT_PANEL_SIZE);
        inputAreaPanel.setMinimumSize(INPUT_PANEL_SIZE);
        inputAreaPanel.setMaximumSize(INPUT_PANEL_SIZE);


        // C r e a t i n g   p a g e s

        // creating init Assistant Note Area Holder Panel

        BillBoardPanel billBoardPanel = new BillBoardPanel(initAssistantController, initAssistantDataModel);
        GeneratorTestingPanel generatorTestingPanel =
                new GeneratorTestingPanel(initAssistantController, initAssistantDataModel);
        noteAreaHolderPanel = new NoteAreaHolderPanel(
                initAssistantController, initAssistantDataModel, billBoardPanel, generatorTestingPanel);
        noteAreaHolderPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        noteAreaHolderPanel.setBackground(new Color(0xFFFFEE));
        noteAreaHolderPanel.setPreferredSize(NOTE_PANEL_SIZE);
        noteAreaHolderPanel.setMinimumSize(NOTE_PANEL_SIZE);
        noteAreaHolderPanel.setMaximumSize(NOTE_PANEL_SIZE);


//        notePanel.add(billBoardPanel, BorderLayout.CENTER);

        InitAssistantFrontPagePanel initAssistantFrontPagePanel = InitAssistantFrontPagePanel.getInstance();

        InitAllowedStatesPage initAllowedStatesPage =
                new InitAllowedStatesPage(initAssistantController, initAssistantDataModel);


        // creating cards panel

        InitAssistantCardsPanel initAssistantCardsPanel;
        InitInitialStatePage initInitialStatePage;
        if (initAssistantDataModel.isInitializingProperty()) {

            InitAllowedStatesInterpretationsPage initAllowedStatesInterpretationsPage =
                    new InitAllowedStatesInterpretationsPage(initAssistantDataModel);

            initInitialStatePage = new InitInitialStatePage(initAssistantController,
                    initAssistantDataModel);

            InputGeneratorSetupPanel inputGeneratorSetupPanel = new InputGeneratorSetupPanel(initAssistantController,
                    initAssistantDataModel);


            initAssistantCardsPanel = new InitAssistantCardsPanel(initAllowedStatesPage,
                    initAllowedStatesInterpretationsPage, initInitialStatePage, inputGeneratorSetupPanel);
        } else {

            ArcStateSetupPanel arcStateSetupPanel = new ArcStateSetupPanel(initAssistantController,
                    initAssistantDataModel);


            initAssistantCardsPanel = new InitAssistantCardsPanel(initAllowedStatesPage, arcStateSetupPanel);
        }

        // layout components

        initAssistantCardsPanel.setPreferredSize(INPUT_PANEL_SIZE);
        initAssistantCardsPanel.setMinimumSize(INPUT_PANEL_SIZE);
        initAssistantCardsPanel.setMaximumSize(INPUT_PANEL_SIZE);

//        initAssistantController.addListener(initAssistant.getEndInitializationRequestListener());
        initAssistantController.addListener(initAssistantCardsPanel.getControllerRequestListener());

        topPanel.add(noteAreaHolderPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        topPanel.add(initAssistantCardsPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


        AllowedStatesTablePanel allowedStatesTablePanel =
                new AllowedStatesTablePanel(initAssistantController, initAssistantDataModel);

        InitAssistantButtonsPanel initAssistantButtonsPanel = new InitAssistantButtonsPanel(initAssistantDataModel,
                initAssistantController.getPrevPageAction(), initAssistantController.getNextPageAction(),
                initAssistantController.getSaveAction(), initAssistantController.getCancelAction());
        initAssistantController.addListener(initAssistantButtonsPanel.getControllerRequestListener());


        // inserting panels
//        modelInitializationAssistant.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        initAssistant.setLayout(new GridBagLayout());
//
        initAssistant.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        initAssistant.add(allowedStatesTablePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        initAssistant.add(initAssistantButtonsPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    }
}
