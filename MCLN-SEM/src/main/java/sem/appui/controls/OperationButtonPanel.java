package sem.appui.controls;

import sem.mission.controlles.modelcontroller.ModelController;
import sem.appui.MissionControlCenterPanel;
import sem.infrastructure.evdistributor.SemEventDistributor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 20, 2011
 * Time: 11:07:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OperationButtonPanel extends BasicManualControlPanel {

    protected Dimension MANUAL_CONTROL_BUTTON_SIZE = new Dimension(131, 24);
    private static final Font ARIEL_PLAIN_12 = new Font("Arial", Font.PLAIN, 12);

    private static final Color FIRST_STATE_BORDER_COLOR = new Color(0xF0F0F0);
    private static final Color FIRST_STATE_INNER_BORDER_COLOR = Color.BLACK;
    private static final Color SECOND_STATE_BORDER_COLOR = FIRST_STATE_BORDER_COLOR;
    private static final Color SECOND_STATE_INNER_BORDER_COLOR = FIRST_STATE_INNER_BORDER_COLOR;

    private Color RESET_BUTTON_BACKGROUND_COLOR = new Color(128, 128, 0);

    private MoveButtonPanel moveButtonPanel;
    private TwoStateRoundedCornersButton pauseResumeButton;
    private RoundedCornersButton resetButton;

    /**
     * @param missionControlCenterPanel
     * @param size
     */
    public OperationButtonPanel(MissionControlCenterPanel missionControlCenterPanel, Dimension size) {
        super(missionControlCenterPanel, size);
//        this.setOpaque(true);
//        this.setBackground(Color.CYAN);
//        setBorder(new EmptyBorder(0, 3, 0, 3));
        this.setLayout(new GridBagLayout());

        Dimension moveButtonPanelSize = new Dimension(123, 123);
//          Dimension moveButtonPanelSize = new Dimension(126, 126);
        moveButtonPanel = new MoveButtonPanel(missionControlCenterPanel, moveButtonPanelSize);
        moveButtonPanel.setForeground(Color.black);

        add(moveButtonPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        Color FIRST_STATE_BORDER_COLOR = new Color(0x008800);
        Color SECOND_STATE_OUTER_BORDER_COLOR = new Color(0x00DD00);
        Color abBC1 = Color.BLACK;  //new Color(0x0000AA); //Color.blue;
        Color abTC1 = Color.white;
        Color abBC2 = Color.BLACK; //new Color(0x0000AA); //Color.blue;
        Color abTC2 = Color.white;

        pauseResumeButton = new TwoStateRoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                "P a u s e", "R e s u m e", abBC1, abTC1, abBC2, abTC2, "Set Auto/Manual control mode.");
        pauseResumeButton.setBlinkingColor(new Color(0x006600));
        pauseResumeButton.setSize(MANUAL_CONTROL_BUTTON_SIZE);
        pauseResumeButton.setPreferredSize(MANUAL_CONTROL_BUTTON_SIZE);
        pauseResumeButton.setMinimumSize(MANUAL_CONTROL_BUTTON_SIZE);
        pauseResumeButton.setMaximumSize(MANUAL_CONTROL_BUTTON_SIZE);
        pauseResumeButton.setEnabled(false);
        pauseResumeButton.setFocusPainted(false);
        pauseResumeButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                ModelController.getInstance().onPauseResume(twoStateRoundedCornersButton.isInSecondState());
            }
        });
        add(pauseResumeButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));


        resetButton = new RoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                (new Color(0xF0F0F0)), (new Color(128, 128, 0)),
                null, "Starting Position");
        resetButton.setFont(ARIEL_PLAIN_12);
        resetButton.setToolTipText("Reset SE to original position.");
        resetButton.setOpaque(false);
        resetButton.setBlinkingColor(new Color(190, 120, 215));
        resetButton.setBlinkingColor(new Color(190, 190, 50));
        resetButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        resetButton.setSize(MANUAL_CONTROL_BUTTON_SIZE);
        resetButton.setPreferredSize(MANUAL_CONTROL_BUTTON_SIZE);
        resetButton.setMinimumSize(MANUAL_CONTROL_BUTTON_SIZE);
        resetButton.setMaximumSize(MANUAL_CONTROL_BUTTON_SIZE);

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
//        add(resetButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    }


    /**
     * Called When GUI State Model Event Distributed to this component
     */
    @Override
    protected void panelEnergized(boolean status) {

        setEnabled(status);

        pauseResumeButton.setBackground(status ? new Color(0x0000AA) : Color.LIGHT_GRAY);
        pauseResumeButton.setFirstStateCurrent();
        pauseResumeButton.setEnabled(status);
        pauseResumeButton.setEnabled(status && isManualControlOn());

        resetButton.setBackground(status ? new Color(220, 220, 0) : Color.LIGHT_GRAY);
        resetButton.setBackground(status ? new Color(0, 0, 0) : new Color(0, 0, 0));
        resetButton.setBackground(status ? RESET_BUTTON_BACKGROUND_COLOR : RESET_BUTTON_BACKGROUND_COLOR);
        resetButton.setEnabled(status);

        moveButtonPanel.stateChanged(status);
        repaint();
    }

}
