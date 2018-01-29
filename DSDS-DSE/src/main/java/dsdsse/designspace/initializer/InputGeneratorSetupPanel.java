package dsdsse.designspace.initializer;

import adf.utils.BuildUtils;
import mcln.model.ProgramStepData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Admin on 3/17/2016.
 */
final class InputGeneratorSetupPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(InputGeneratorSetupPanel.class.getName());

    private final String STATE_DRIVEN = "- State Driven";
    private final String TIME_DRIVEN = "- Time Driven";

    private static final String APPEND = "Append";
    private static final String INSERT = "Insert";
    private static final String REMOVE = "Remove";
    private static final String START_TESTING = "Start Testing";
    private static final String STOP_TESTING = "Stop Testing";

    //
    //   I n s t a n c e
    //

    private final InitAssistantController initAssistantController;
    private final InitAssistantDataModel initAssistantDataModel;

    private JButton appendButton;
    private JButton insertButton;
    private JButton removeButton;

    private JButton startStopTestButton;

    protected JTable table = null;
    protected JScrollPane scrollPanel = null;
    protected JViewport viewPort = null;

    protected int rowHeight = 0;    // set at init time
    protected int rowOffset = 0;
    protected int nVisibleRows = 0;    // set at init time

    protected int firstVisibleIndex = 0;
    protected int lastVisibleIndex = 6;
    protected boolean progUpdating = false;


    private final JRadioButton tdprb = new JRadioButton(TIME_DRIVEN);
    private final JRadioButton sdprb = new JRadioButton(STATE_DRIVEN);

    //    private boolean phaseNeeded;
    private JLabel phaseValue;
    private JCheckBox phaseCheckBox;
    private JLabel stepNumberValue;
    private JLabel cmpleteValue;

    private Color attrValueColor = new Color(0x000000);

    private final JPanel tablePanel = new JPanel(new BorderLayout());
    private TimeDrivenProgramTablePanel<ProgramStepData> timeDrivenProgramTablePanel;
    private RuleDrivenProgramTablePanel<ProgramStepData> ruleDrivenProgramTablePanel;
    private AbstractProgramTablePanel currentProgramTablePanel;


    private static final Color PANEL_BACKGROUND = new Color(0xDDDDDD);

    private final ControllerRequestListener controllerRequestListener =
            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.PageNavigationRequest operation,
             InitAssistantController.InitializationPage page) -> {
                switch (operation) {
                    case NextPage:
                        if (page == InitAssistantController.InitializationPage.InitInputGeneratorPage) {
                            System.out.println("InitInputGeneratorPage");
                            populateInputGeneratingProgramWithData();
                        }
                        break;
                }
            };


    /**
     * Called when generator type changed
     */
    private final InitAssistantDataModelListener initAssistantDataModelListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
                switch (attributeID) {
                    case TimeDrivenGeneratorSelected:
                        resetProgramPanel(timeDrivenProgramTablePanel);
                        break;
                    case RuleDrivenGeneratorSelected:
                        resetProgramPanel(ruleDrivenProgramTablePanel);
                        break;
                }
            };

    InputGeneratorSetupPanel(
            InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel) {
        super(new GridBagLayout());
//        setOpaque(true);
//        setBackground(PANEL_BACKGROUND);

        this.initAssistantController = initAssistantController;
        this.initAssistantDataModel = initAssistantDataModel;

        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.INPUT_PANEL_BACKGROUND);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(15, 15, 15, 15, InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND),
                BorderFactory.createEtchedBorder()
        );
        setBorder(border);

        initContents();
        initAssistantController.addListener(controllerRequestListener);
        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    private void initContents() {

        timeDrivenProgramTablePanel = new TimeDrivenProgramTablePanel(initAssistantDataModel);
        ruleDrivenProgramTablePanel = new RuleDrivenProgramTablePanel(initAssistantDataModel);
        if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
            currentProgramTablePanel = timeDrivenProgramTablePanel;
        } else {
            currentProgramTablePanel = ruleDrivenProgramTablePanel;
        }

        tablePanel.add(currentProgramTablePanel, BorderLayout.CENTER);
        add(tablePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        JPanel progTableButtonPanel = initProgTabButtonPanel();
        add(progTableButtonPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));


        JPanel progTypeSelectionPanel = initProgTypeSelectionPanel();
        add(progTypeSelectionPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

//        JPanel nextButtonPanel = new JPanel( new GridBagLayout() );
//        Dimension size = new Dimension( 1, 20 );
//        nextButtonPanel.setPreferredSize( size );
//        nextButtonPanel.setMaximumSize( size );
//        nextButtonPanel.setMinimumSize( size );
//        nextButtonPanel.setLayout( new GridBagLayout() );
//        nextButtonPanel.setOpaque(false);
//        nextButtonPanel.setBackground( Color.BLACK );
//
//        Dimension buttonSize = new Dimension( 50, 15 );
//
//
//
//
//        add( nextButtonPanel, new GridBagConstraints( 0, 2, 2, 1, 1.0, 0.0,
//                GridBagConstraints.EAST , GridBagConstraints.HORIZONTAL,
//                new Insets(0,0,0,0), 0, 0 ));
    }

    private JPanel initProgTabButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
//        Border empty = BorderFactory.createEmptyBorder(3, 3, 3, 3);
//        panel.setBorder(empty);
        Dimension size = new Dimension(90, 0);
        panel.setMinimumSize(size);
        panel.setPreferredSize(size);

        int fontSize = 10;
        int buttonWidth = 90;
        int buttonHeight = 15;

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 2));
        infoPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftPanel.setLayout(new GridLayout(0, 1));
        // makeLabelAndAddToPanel( leftPanel, "Phase:", attrNameColor,
        // JLabel.LEFT );
        JLabel stepsLabel = BuildUtils.makeLabelAndAddToPanel(leftPanel, "Steps:",
                InitAssistantUIColorScheme.DISABLED_TEXT_COLOR, JLabel.LEFT);
        stepsLabel.setEnabled(false);
        JLabel completeLabel = BuildUtils.makeLabelAndAddToPanel(leftPanel, "Complete:",
                InitAssistantUIColorScheme.DISABLED_TEXT_COLOR, JLabel.LEFT);
        completeLabel.setEnabled(false);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        rightPanel.setLayout(new GridLayout(0, 1));

        // phaseValue =
        // makeLabelAndAddToPanel( rightPanel, "", attrValueColor, JLabel.LEFT
        // );

        stepNumberValue = BuildUtils.makeLabelAndAddToPanel(rightPanel, "", attrValueColor, JLabel.LEFT);
        cmpleteValue = BuildUtils.makeLabelAndAddToPanel(rightPanel, "", attrValueColor, JLabel.LEFT);

        infoPanel.add(leftPanel, BorderLayout.WEST);
        infoPanel.add(rightPanel, BorderLayout.CENTER);

        // b u t t o n   p a n e l   c r e a t i o n

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // // System.out.println( "JCheckBox phaseNeeded "+phaseNeeded);
        boolean selectedProgramHasPhase = initAssistantDataModel.getSelectedProgramHasPhase();
        phaseCheckBox = new JCheckBox(" - Has Phase", selectedProgramHasPhase);
        phaseCheckBox.setBorder(null);
        phaseCheckBox.setForeground(InitAssistantUIColorScheme.CONTROLS_FOREGROUND_COLOR);
        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);
        phaseCheckBox.setPreferredSize(buttonSize);
        phaseCheckBox.setMaximumSize(buttonSize);
        phaseCheckBox.setMinimumSize(buttonSize);
        phaseCheckBox.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // phaseCheckBox.setSelected( propertyHasProgram );
        BuildUtils.resetComponentFont(phaseCheckBox, Font.PLAIN, fontSize);
        phaseCheckBox.setOpaque(false);
        phaseCheckBox.setFocusPainted(false);

        phaseCheckBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                initAssistantDataModel.setSelectedProgramHasPhase(true);
            } else {
                initAssistantDataModel.setSelectedProgramHasPhase(false);
            }
            currentProgramTablePanel.repaint();
        });

        buttonPanel.add(phaseCheckBox);
        //
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 1)));
        appendButton = new JButton(APPEND);
        appendButton.setMnemonic(KeyEvent.VK_A);
        appendButton.setPreferredSize(buttonSize);
        appendButton.setMaximumSize(buttonSize);
        appendButton.setMinimumSize(buttonSize);
        BuildUtils.resetComponentFont(appendButton, Font.PLAIN, fontSize);
        appendButton.setAlignmentX(RIGHT_ALIGNMENT);
        appendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("progTableAppendButton");
                currentProgramTablePanel.appendEmptyRow();
//                currentProgramTablePanel.getD
//                initAssistantDataModel.updateCurrentAllowedStatesListOnPaletItemSelected();
            }
        });

        appendButton.setFocusPainted(false);
        buttonPanel.add(appendButton);

        // creating Insert button
        insertButton = new JButton(INSERT);
        insertButton.setMnemonic(KeyEvent.VK_I);
        insertButton.setPreferredSize(buttonSize);
        insertButton.setMaximumSize(buttonSize);
        insertButton.setMinimumSize(buttonSize);
        BuildUtils.resetComponentFont(insertButton, Font.PLAIN, fontSize);
        insertButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("progTableInsertButton");
                currentProgramTablePanel.insertEmptyRow();
            }
        });
        insertButton.setFocusPainted(false);
        buttonPanel.add(insertButton);

        // creating Remove button
        removeButton = new JButton(REMOVE);
        removeButton.setMnemonic(KeyEvent.VK_R);
        removeButton.setPreferredSize(buttonSize);
        removeButton.setMaximumSize(buttonSize);
        removeButton.setMinimumSize(buttonSize);
        BuildUtils.resetComponentFont(removeButton, Font.PLAIN, fontSize);
        removeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("progTableRemoveButton");
                currentProgramTablePanel.removeRow();
            }
        });
        removeButton.setFocusPainted(false);
        buttonPanel.add(removeButton);

        buttonPanel.add(Box.createVerticalStrut(3));

        // creating Test button
          startStopTestButton = new JButton(START_TESTING);
        startStopTestButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        startStopTestButton.setBackground(Color.YELLOW);
        startStopTestButton.setMnemonic(KeyEvent.VK_R);
        startStopTestButton.setPreferredSize(buttonSize);
        startStopTestButton.setMaximumSize(buttonSize);
        startStopTestButton.setMinimumSize(buttonSize);
        BuildUtils.resetComponentFont(startStopTestButton, Font.PLAIN, fontSize);
        startStopTestButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        startStopTestButton.setFocusPainted(false);
        startStopTestButton.addActionListener(initAssistantController.getTestGeneratorAction());
        startStopTestButton.addActionListener((e) -> {
            if (startStopTestButton.getText().equals(START_TESTING)) {
                startStopTestButton.setText(STOP_TESTING);
            } else {
                startStopTestButton.setText(START_TESTING);
            }
        });

        buttonPanel.add(startStopTestButton);

        panel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 3, 0), 0, 0));
        return panel;
    }

    private JPanel initProgTypeSelectionPanel() {

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(true);
        mainPanel.setBackground(PANEL_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        mainPanel.setBackground(new Color(0xC0C0C0));

        mainPanel.setPreferredSize(new Dimension(300, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        Border etched = BorderFactory.createEtchedBorder();
//        mainPanel.setBorder(etched);
//        mainPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(3,0,0,0),
//                BorderFactory.createEtchedBorder(new Color(0x808080), new Color(0x505050)) ));
        mainPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        int fontSize = 10;

        JPanel lblPanel = new JPanel();
        lblPanel.setOpaque(false);
        // lblPanel.setBackground(new Color(0x909090));
        lblPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        lblPanel.setPreferredSize(new Dimension(30, 18));
        lblPanel.setLayout(new BorderLayout());
        // lblPanel.setOpaque(false);

        // JLabel prgChooserlab = new JLabel(" Program Type:");
        // prgChooserlab.setForeground(Color.white);
        // prgChooserlab.setBackground(new Color(0x909090));
        // lblPanel.add(prgChooserlab, BorderLayout.CENTER );
        // mainPanel.add( lblPanel );
        JLabel lab = BuildUtils.makeLabelAndAddToPanel(lblPanel, "Program Type:",
                Color.DARK_GRAY, JLabel.CENTER);
        BuildUtils.resetComponentFont(lab, Font.BOLD, 10);
        mainPanel.add(lblPanel);

//        mainPanel.add(Box.createRigidArea(new Dimension(4, 0)));
        Action generatorTypeSelectionAction =   initAssistantController.getGeneratorTypeSelectionAction();

        BuildUtils.resetComponentFont(tdprb, Font.PLAIN, fontSize);
        tdprb.setForeground(InitAssistantUIColorScheme.CONTROLS_FOREGROUND_COLOR);
        tdprb.setOpaque(false);
        tdprb.setMnemonic(KeyEvent.VK_T);
        tdprb.setFocusPainted(false);
        tdprb.addActionListener(generatorTypeSelectionAction);
        mainPanel.add(tdprb);

        mainPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        BuildUtils.resetComponentFont(sdprb, Font.PLAIN, fontSize);
        sdprb.setForeground(InitAssistantUIColorScheme.CONTROLS_FOREGROUND_COLOR);
        sdprb.setOpaque(false);
        sdprb.setMnemonic(KeyEvent.VK_S);
        sdprb.setFocusPainted(false);
        sdprb.addActionListener(generatorTypeSelectionAction);
        mainPanel.add(sdprb);

        mainPanel.add(Box.createRigidArea(new Dimension(6, 0)));

        tdprb.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(tdprb);
        group.add(sdprb);

        /*
         * insertButton = new JButton( insertStr ); setButtonFontSize(
         * insertButton, Font.PLAIN, fontSize ); insertButton.addActionListener(
         * this ); insertButton.setPreferredSize(new Dimension(90, 15));
         * insertButton.setMaximumSize(new Dimension(90, 15));
         * insertButton.setMinimumSize(new Dimension(90, 15)); mainPanel.add(
         * insertButton );
         *
         * removeButton = new JButton( removeStr ); setButtonFontSize(
         * removeButton, Font.PLAIN, fontSize ); removeButton.addActionListener(
         * this ); removeButton.setPreferredSize(new Dimension(90, 15));
         * removeButton.setMaximumSize(new Dimension(90, 15));
         * removeButton.setMinimumSize(new Dimension(90, 15)); mainPanel.add(
         * removeButton );
         */
        return mainPanel;
    }

    private void resetProgramPanel(AbstractProgramTablePanel programTablePanel) {

        tablePanel.remove(currentProgramTablePanel);
        currentProgramTablePanel = programTablePanel;
        tablePanel.add(currentProgramTablePanel, BorderLayout.CENTER);

        // updating view
        boolean selectedProgramHasPhase = initAssistantDataModel.getSelectedProgramHasPhase();
        phaseCheckBox.setSelected(selectedProgramHasPhase);
        populateControls();

        this.revalidate();
        phaseCheckBox.revalidate();
        currentProgramTablePanel.revalidate();
        currentProgramTablePanel.repaint();
    }

//    private boolean showingStateDrivenProgram;
//
//    private boolean isCurrentTableTimeDrivenProgram() {
//        return currentProgramTablePanel instanceof TimeDrivenProgramTablePanel;
//    }

    /**
     * implements FactSetupInputPanel interface
     */
//    public void setActive(boolean active) {
//        if (active) {
//            this.setBorder(AbstractFactSetupPanel.HIGHLIGHT_BORDER);
//            this.setEnabled(true);
//            tdprb.setEnabled(true);
//            sdprb.setEnabled(true);
//        } else {
//            this.setBorder(AbstractFactSetupPanel.REGULAR_BORDER);
//            this.setEnabled(false);
//            tdprb.setEnabled(false);
//            sdprb.setEnabled(false);
//        }
//        repaint();
//    }
    private void populateInputGeneratingProgramWithData() {
        List<ProgramStepData> selectedProgramSteps = initAssistantDataModel.getSelectedProgramSteps();
        if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
            timeDrivenProgramTablePanel.setData(selectedProgramSteps);
        } else {
            ruleDrivenProgramTablePanel.setData(selectedProgramSteps);
        }
        populateControls();
        startStopTestButton.setText(START_TESTING);
    }

    private void populateControls() {
        boolean selectedProgramHasPhase = initAssistantDataModel.getSelectedProgramHasPhase();
        if (currentProgramTablePanel instanceof TimeDrivenProgramTablePanel) {
            phaseCheckBox.setEnabled(true);
            phaseCheckBox.setSelected(selectedProgramHasPhase);
            tdprb.setSelected(true);
        } else {
            phaseCheckBox.setEnabled(false);
            phaseCheckBox.setSelected(false);
            sdprb.setSelected(true);
        }
    }

}
