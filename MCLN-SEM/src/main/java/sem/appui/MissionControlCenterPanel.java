package sem.appui;

import sem.app.AppConstants;
import sem.mission.controlles.modelcontroller.actions.ActionPowerOnOff;
import sem.mission.controlles.modelcontroller.actions.SetVantagePpointAction;
import sem.appui.controls.*;
import sem.infrastructure.evdistributor.SemEventDistributor;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import adf.ui.UiUtils;
import adf.ui.components.borders.AlignedRoundedBorder;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 18, 2011
 * Time: 12:23:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MissionControlCenterPanel extends JPanel {

    public static final Dimension MISSION_CONTROL_BUTTON_SIZE = new Dimension(127, 24);
    private static final Dimension HEADER_SIZE = new Dimension(1, 14);
    private static final Dimension SETUP_HEADER_SIZE = new Dimension(1, 15);

    private static final Font ARIEL_PLAIN_12 = new Font("Arial", Font.PLAIN, 12);

    private static final Color FIRST_STATE_BORDER_COLOR = new Color(0xF0F0F0);
    private static final Color FIRST_STATE_INNER_BORDER_COLOR = Color.BLACK;
    private static final Color SECOND_STATE_BORDER_COLOR = FIRST_STATE_BORDER_COLOR;
    private static final Color SECOND_STATE_INNER_BORDER_COLOR = FIRST_STATE_INNER_BORDER_COLOR;

    private static final Color RESET_BUTTON_BACKGROUND_COLOR = new Color(128, 128, 0);

    private static final MissionControlCenterPanel MISSION_CONTROL_PANEL = new MissionControlCenterPanel();

    public static final MissionControlCenterPanel getInstance() {
        return MISSION_CONTROL_PANEL;
    }

    // Controls
//    private JLabel directionsHeader;
    private TitledBorder manualControlPanelTitledBorde;

    private TargetLocationPickupPanel targetLocationPickupPanel;
    private DirectionalNavigatonPanel directionalNavigatonPanel;
    private OperationButtonPanel operationButtonPanel;
    private List<BasicManualControlPanel> manualControlPanels = new ArrayList();


    private JLabel setupHeader;
    private RoundedCornersButton resetButton;
    private TwoStateRoundedCornersButton autoManualButton;
    private TwoStateRoundedCornersButton viewPositioningButton;
    private TwoStateRoundedCornersButton powerButton;

    /**
     *
     */
    private EventDistributionAdapter missionEventDistributionListener = new EventDistributionAdapter<Boolean,
            CallbackListener>() {

        @Override
        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, Boolean powerOn,
                                              CallbackListener callbackListener) {

            switch (eventId) {
                case POWER:
                    setInitialState(false, powerOn);
                    repaint();
                    viewPositioningButton.doClick();
                    break;
            }
        }
    };

    /**
     *
     */
    private MissionControlCenterPanel() {
        buildComponents();
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                missionEventDistributionListener);
    }

    /**
     *
     */
    private void buildComponents() {

        setBackground(new Color(0x000000));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        setLayout(new GridBagLayout());

        JPanel manualControlPanel = createManualControlPanel();
        add(manualControlPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//        directionsHeader = new JLabel("Directions", JLabel.CENTER);
//        directionsHeader.setBorder(new MatteBorder(0, 3, 0, 1, Color.GRAY));
//        directionsHeader.setFont(new Font("Dialog", Font.BOLD, 12));
//        directionsHeader.setOpaque(true);
//        directionsHeader.setBackground(Color.ORANGE);
//        Color sec1AColor = new Color(80, 252, 154);
//        directionsHeader.setBackground(sec1AColor);
//        directionsHeader.setForeground(Color.BLACK);
//        directionsHeader.setSize(HEADER_SIZE);
//        directionsHeader.setPreferredSize(HEADER_SIZE);
//        directionsHeader.setMinimumSize(HEADER_SIZE);
//        directionsHeader.setMaximumSize(HEADER_SIZE);
//        add(directionsHeader, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
//                GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
//
//        targetLocationPickupPanel = createTargetLocationPickupPanel();
//        add(targetLocationPickupPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.NONE, new Insets(15, 1, 0, 0), 0, 0));
//
//        directionalNavigatonPanel = createDirectioNavigationPane();
//        add(directionalNavigatonPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
//
//        operationButtonPanel = new OperationButtonPanel(this, new Dimension(1, 150));
//        add(operationButtonPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));

        setupHeader = new JLabel("S e t u p", JLabel.CENTER);
        setupHeader.setBorder(new MatteBorder(0, 3, 0, 1, Color.GRAY));
        setupHeader.setFont(new Font("Dialog", Font.BOLD, 11));
        setupHeader.setOpaque(true);
        setupHeader.setBackground(Color.ORANGE);
        setupHeader.setForeground(Color.BLACK);
        setupHeader.setSize(SETUP_HEADER_SIZE);
        setupHeader.setPreferredSize(SETUP_HEADER_SIZE);
        setupHeader.setMinimumSize(SETUP_HEADER_SIZE);
        setupHeader.setMaximumSize(SETUP_HEADER_SIZE);
        add(setupHeader, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(createMainButtonPanel(), new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));

        manualControlPanels.add(targetLocationPickupPanel);
        manualControlPanels.add(directionalNavigatonPanel);
        manualControlPanels.add(operationButtonPanel);
        setInitialState(true, false);
    }

    private JPanel createManualControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        Border border = createRoundedTitledPaddedBorder(3, 3, 5, 3, "  D i r e c t i o n s  ",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null, UiUtils.LINE_BORDER_DARK_COLOR,
                new Color(0x00AA00), 0, 5, 0, 5);
        panel.setBorder(border);

//        directionsHeader = new JLabel("Directions", JLabel.CENTER);
//        directionsHeader.setBorder(new MatteBorder(0, 3, 0, 1, Color.GRAY));
//        directionsHeader.setFont(new Font("Dialog", Font.BOLD, 12));
//        directionsHeader.setOpaque(true);
//        directionsHeader.setBackground(Color.ORANGE);
//        Color sec1AColor = new Color(80, 252, 154);
//        directionsHeader.setBackground(sec1AColor);
//        directionsHeader.setForeground(Color.BLACK);
//        directionsHeader.setSize(HEADER_SIZE);
//        directionsHeader.setPreferredSize(HEADER_SIZE);
//        directionsHeader.setMinimumSize(HEADER_SIZE);
//        directionsHeader.setMaximumSize(HEADER_SIZE);
//        panel.add(directionsHeader, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
//                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        targetLocationPickupPanel = createTargetLocationPickupPanel();
        panel.add(targetLocationPickupPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(7, 0, 0, 0), 0, 0));

        directionalNavigatonPanel = createDirectioNavigationPane();
        panel.add(directionalNavigatonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

        operationButtonPanel = new OperationButtonPanel(this, new Dimension(1, 150));
        panel.add(operationButtonPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

        return panel;
    }

    private Border createRoundedTitledPaddedBorder(int outerTop, int outerLeft,
                                                   int outerBottom, int outerRight,
                                                   String title, int titleJustification,
                                                   int titlePosition,
                                                   Font titleFont, Color innerBorderColor,
                                                   Color outerBorderColor,
                                                   Color titleColor,
                                                   int top, int left, int bottom, int right) {

        AlignedRoundedBorder alignedRoundedBorder = new AlignedRoundedBorder(2, 4,
                AlignedRoundedBorder.ROUNDING_BOTH, innerBorderColor, outerBorderColor);

        manualControlPanelTitledBorde = BorderFactory.createTitledBorder(
                alignedRoundedBorder, title, titleJustification,
                titlePosition,
                titleFont, titleColor);

        Border paddedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(outerTop, outerLeft, outerBottom, outerRight),
                manualControlPanelTitledBorde
        );
        Border compoundTitledBorder = BorderFactory.createCompoundBorder(paddedBorder,
                BorderFactory.createEmptyBorder(top, left, bottom, right));
        return compoundTitledBorder;
    }

    private final String MANUAL_CONTROL_PANEL = "MANUAL_CONTROL_PANEL";
    private final String AUTO_CONTROL_PANEL = "AUTO_CONTROL_PANEL";
    private JPanel manualOrAutoControlPanel;
    private CardLayout manualOrAutoControlPanelCardLayout;

    /**
     * @return SpaceExplorationControlPanel
     */
    private TargetLocationPickupPanel createTargetLocationPickupPanel() {
        Dimension size = new Dimension(1, 100);
        TargetLocationPickupPanel targetLocationPickupPanel = new TargetLocationPickupPanel(this);
        return targetLocationPickupPanel;
    }

    /**
     *
     */
    private DirectionalNavigatonPanel createDirectioNavigationPane() {
        Dimension size = new Dimension(110, 110);
        DirectionalNavigatonPanel panel = new DirectionalNavigatonPanel(this, size);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(3, 3, 2, 0)
        ));
        return panel;
    }

    /**
     *
     */
    private JPanel createMainButtonPanel() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        Dimension size = new Dimension(120, 76);
        panel.setSize(size);
        panel.setPreferredSize(size);
        panel.setMaximumSize(size);
        panel.setMinimumSize(size);

        panel.setLayout(new GridBagLayout());

        // Reset to Initial position
        resetButton = new RoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                (new Color(0xF0F0F0)), (new Color(128, 128, 0)),
                null, "Restart Exploration");
        resetButton.setFont(ARIEL_PLAIN_12);
        resetButton.setToolTipText(" Restarts exploration from original position ");
        resetButton.setOpaque(false);
        resetButton.setBlinkingColor(new Color(190, 120, 215));
        resetButton.setBlinkingColor(new Color(190, 190, 50));
        resetButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        resetButton.setSize(MISSION_CONTROL_BUTTON_SIZE);
        resetButton.setPreferredSize(MISSION_CONTROL_BUTTON_SIZE);
        resetButton.setMinimumSize(MISSION_CONTROL_BUTTON_SIZE);
        resetButton.setMaximumSize(MISSION_CONTROL_BUTTON_SIZE);

        resetButton.setForeground(new Color(0xF0F0F0));
        resetButton.setFocusPainted(false);
        resetButton.setBackground(RESET_BUTTON_BACKGROUND_COLOR);
        resetButton.setEnabled(false);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetButton.doBlink(150);
                SemEventDistributor.executeResetModel(resetButton, Boolean.TRUE, null);
            }
        });
        panel.add(resetButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));

        // McLN/Manual control mode
        Color abBC1 = new Color(0x0000AA); //Color.blue;
        Color abTC1 = Color.white;
        Color abBC2 = new Color(0x0000AA); //Color.blue;
        Color abTC2 = Color.white;
        autoManualButton = new TwoStateRoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                  AppConstants.MANUAL_CONTROL,AppConstants.MCLN_CONTROL,
                abBC1, abTC1, abBC2, abTC2, "Set Auto/Manual control mode.");
        autoManualButton.setFont(ARIEL_PLAIN_12);
        autoManualButton.setToolTipText(" Switches to Manual or McLN Control mode ");
        autoManualButton.setBlinkingColor(Color.BLUE);
        autoManualButton.setSize(MISSION_CONTROL_BUTTON_SIZE);
        autoManualButton.setPreferredSize(MISSION_CONTROL_BUTTON_SIZE);
        autoManualButton.setMinimumSize(MISSION_CONTROL_BUTTON_SIZE);
        autoManualButton.setMaximumSize(MISSION_CONTROL_BUTTON_SIZE);
        autoManualButton.setEnabled(false);
        autoManualButton.setFocusPainted(false);
        autoManualButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
//                boolean manualControl = !twoStateRoundedCornersButton.isInSecondState();
//                SemEventDistributor.setManualControl(autoManualButton, (manualControl ? Boolean.FALSE : Boolean.TRUE), null);

                boolean manualControl = twoStateRoundedCornersButton.isInSecondState();
                SemEventDistributor.setManualControl(autoManualButton, (manualControl ? Boolean.TRUE : Boolean.FALSE), null);
            }
        });
        panel.add(autoManualButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));

        // 3D view
        Color vpBC1 = new Color(0x008888);
        Color vpTC1 = Color.white;
        Color vpBC2 = new Color(0x008888);
        Color vpTC2 = Color.white;
        viewPositioningButton = new TwoStateRoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                "3D View", "H o m e", vpBC1, vpTC1, vpBC2, vpTC2, "Position View for observation.");
        viewPositioningButton.setBlinkingColor(new Color(0x00CCCC));
        viewPositioningButton.setFont(ARIEL_PLAIN_12);
        viewPositioningButton.setToolTipText(" Positions Explorer to 3D or 2D view ");
        viewPositioningButton.setSize(MISSION_CONTROL_BUTTON_SIZE);
        viewPositioningButton.setPreferredSize(MISSION_CONTROL_BUTTON_SIZE);
        viewPositioningButton.setMinimumSize(MISSION_CONTROL_BUTTON_SIZE);
        viewPositioningButton.setMaximumSize(MISSION_CONTROL_BUTTON_SIZE);
        viewPositioningButton.setEnabled(false);
        viewPositioningButton.setFocusPainted(false);
        viewPositioningButton.addThisButtonActionListener(new SetVantagePpointAction(viewPositioningButton));
        panel.add(viewPositioningButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));

        // Panels divider line
        JLabel devider = new JLabel("", JLabel.CENTER);
        Dimension DEVIDER_SIZE = new Dimension(1, 2);
        devider.setSize(DEVIDER_SIZE);
        devider.setPreferredSize(DEVIDER_SIZE);
        devider.setMinimumSize(DEVIDER_SIZE);
        devider.setMaximumSize(DEVIDER_SIZE);
        devider.setBackground(Color.ORANGE);
        devider.setOpaque(true);
        panel.add(devider, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 3), 0, 0));

        // Space holder
        JPanel spaceHolderPanel = new JPanel();
        spaceHolderPanel.setOpaque(false);
        panel.add(spaceHolderPanel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // Power/Shutdown
        Color sbBC1 = new Color(130, 0, 0);
        Color sbTC1 = Color.white;
        Color sbBC2 = new Color(200, 0, 0);
        Color sbTC2 = Color.white;
        powerButton = new TwoStateRoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                "Power", "Shutdown", sbBC2, sbTC2, sbBC2, sbTC2, "Set power On/Off.");
        powerButton.setFont(ARIEL_PLAIN_12);
        powerButton.setToolTipText(" Sets Exploration Mission power On/Off ");
        powerButton.setSize(MISSION_CONTROL_BUTTON_SIZE);
        powerButton.setPreferredSize(MISSION_CONTROL_BUTTON_SIZE);
        powerButton.setMinimumSize(MISSION_CONTROL_BUTTON_SIZE);
        powerButton.setMaximumSize(MISSION_CONTROL_BUTTON_SIZE);
        powerButton.setBlinkingColor(Color.RED);
        powerButton.setEnabled(true);
        powerButton.addThisButtonActionListener(new ActionPowerOnOff(this, powerButton));
        panel.add(powerButton, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, new Insets(7, 10, 0, 10), 0, 0));

        return panel;
    }

    /**
     * 
     */
    public void switchToManualControlPanel() {
        manualOrAutoControlPanelCardLayout.show(manualOrAutoControlPanel, MANUAL_CONTROL_PANEL);
    }

    public void switchToAutoControlPanel() {
        manualOrAutoControlPanelCardLayout.show(manualOrAutoControlPanel, AUTO_CONTROL_PANEL);
    }

    /**
     * @param initializing
     * @param status
     */
    private void setInitialState(boolean initializing, boolean status) {
        Color sec1AColor = new Color(244, 227, 94);

        //   directionsHeader.setEnabled(status);
//        directionsHeader.setBackground(status ? sec1AColor : Color.LIGHT_GRAY);
        manualControlPanelTitledBorde.setTitleColor(status ? UiUtils.LINE_BORDER_BRIGHT_COLOR : UiUtils.LINE_BORDER_DARK_COLOR);
        setupHeader.setEnabled(status);
        setupHeader.setBackground(status ? sec1AColor : Color.LIGHT_GRAY);

        resetButton.setBackground(status ? new Color(220, 220, 0) : Color.LIGHT_GRAY);
        resetButton.setBackground(status ? new Color(0, 0, 0) : new Color(0, 0, 0));
        resetButton.setBackground(status ? RESET_BUTTON_BACKGROUND_COLOR : RESET_BUTTON_BACKGROUND_COLOR);
        resetButton.setEnabled(status);

        autoManualButton.setBackground(new Color(0x0000AA));
        if (initializing) {
            autoManualButton.setFirstStateCurrent();
        }
        autoManualButton.setEnabled(status);

        viewPositioningButton.setBackground(status ? new Color(0x0000AA) : Color.LIGHT_GRAY);
        viewPositioningButton.setFirstStateCurrent();
        viewPositioningButton.setEnabled(status);
    }

    public void clickPowerOnButton(){
        powerButton.doClick();
    }

}
