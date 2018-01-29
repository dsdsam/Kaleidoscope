package sem.appui.controls;

import adf.utils.BuildUtils;
import sem.infrastructure.OperationRequestStatus;
import sem.appui.controller.BasicOperationRequest;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.appui.MissionControlCenterPanel;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;
import sem.infrastructure.evdistributor.SemEventDistributor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 11, 2011
 * Time: 8:06:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoveButtonPanel extends JPanel {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String PREFIX = "/sem-resources/images/directions/";

    private static final Color FIRST_STATE_OUTER_BORDER_COLOR = new Color(0x008800);
    private static final Color FIRST_STATE_INNER_BORDER_COLOR = Color.BLACK;
    private static final Color SECOND_STATE_OUTER_BORDER_COLOR = new Color(0x00DD00);
    private static final Color SECOND_STATE_INNER_BORDER_COLOR = Color.BLACK;

//    private static final Color FIRST_STATE_START_STOP_BORDER_COLOR = Color.LIGHT_GRAY;

    private static final Color FIRST_STATE_BACKROUND_COLOR = new Color(50, 150, 50);
    private static final Color SECOND_STATE_BACKROUND_COLOR = new Color(50, 220, 50);
    private static final Color START_STOP_BACKROUND_COLOR = new Color(200, 50, 50);

    private List<RoundedCornersButton> buttonsGroup = new ArrayList();

    private TwoStateRoundedCornersButton leftTrackWorwardButton;
    private TwoStateRoundedCornersButton forwardButton;
    private TwoStateRoundedCornersButton rightTrackForwardButton;
    private TwoStateRoundedCornersButton leftRotationButton;
    private RoundedCornersButton stopButton;
    private TwoStateRoundedCornersButton rightRotationButton;
    private TwoStateRoundedCornersButton leftTrackBackwardButton;
    private TwoStateRoundedCornersButton backwardButton;
    private TwoStateRoundedCornersButton rightTrackBackwardButton;

    private MissionControlCenterPanel missionControlCenterPanel;
//    private OperationButtonPanel operationButtonPanel;
//    private SpaceExplorerModel spaceExplorerModel;
    private ModelController modelController;

    private final ServiceExecutionListener serviceExecutionListener = new ServiceExecutionListener() {
        public void executionDone(BasicOperationRequest<?, ?> operationRequest) {
            OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
            ModelMotionOperation requestedOperation = (ModelMotionOperation) operationRequest.getRequestedOperation();
//            System.out.println("MoveButtonPanel: Operation " + requestedOperation + " Done with status = " + operationRequestStatus);

            /**
             * Current Operation Should be return to initial state when not from the queue
             */
//            operationButtonPanel.cancelOperation();
            cancelAllOperation();
//            finishOperationExecution();
        }
    };


    /**
     * @param size
     */
    public MoveButtonPanel(MissionControlCenterPanel missionControlCenterPanel, Dimension size) {
        this.missionControlCenterPanel = missionControlCenterPanel;
        modelController = ModelController.getInstance();
        setSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setOpaque(false);
        setLayout(new GridLayout(3, 3));
        createButtons();
    }

    /**
     *
     */
    private final void createButtons() {

        leftTrackWorwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-lf.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-lf.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Left track forward.");
        leftTrackWorwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_LEFT_TRACK_FORWARD,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(leftTrackWorwardButton);


        forwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-fw.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-fw.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Forward.");

        forwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                double[] distanceToTheTargetLocation = new double[]{10};
                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.MOVE_FORWARD,
                        twoStateRoundedCornersButton, distanceToTheTargetLocation, serviceExecutionListener);

            }
        });
        addButton(forwardButton);


        rightTrackForwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-rf.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-rf.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Right track forward.");
        rightTrackForwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_RIGHT_TRACK_FORWARD,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(rightTrackForwardButton);


        leftRotationButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-lr.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-lr.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Right rotation.");
        leftRotationButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_SPIN_TO_THE_RIGHT,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(leftRotationButton);


        stopButton = new RoundedCornersButton(Color.BLACK, 2, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_OUTER_BORDER_COLOR,
//       new Color(0xB0B0B0),
                Color.BLACK,
                // FIRST_STATE_INNER_BORDER_COLOR,
                null, "Stop");

//        buttonsGroup.addElement(stopButton);
        int fontSize = 11;
        Font font = stopButton.getFont();
        stopButton.setFont(new Font("Serif", Font.PLAIN, fontSize));
//        stopButton.setFont(new Font(font.getName(), Font.BOLD, fontSize));
        stopButton.setForeground(new Color(0xF0F0F0));

        stopButton.setToolTipText("Stop operation.");
        stopButton.setBlinkingColor(Color.RED);
        stopButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
//        stopButton.setGroup(buttonsGroup);
        Dimension resetButtonSize = new Dimension(1, 31);
        stopButton.setSize(resetButtonSize);
        stopButton.setPreferredSize(resetButtonSize);
        stopButton.setBackground(new Color(170, 0, 0));
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.CANCEL_OPERATION_EXECUTION,
                        null, null, serviceExecutionListener);
            }
        });
        add(stopButton);


        rightRotationButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-rl.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-rl.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Left rotation.");
        rightRotationButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_SPIN_TO_THE_LEFT,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(rightRotationButton);


        leftTrackBackwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-lb.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-lb.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Left track backward.");
        leftTrackBackwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_LEFT_TRACK_BACKWARD,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(leftTrackBackwardButton);


        backwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-bw.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-bw.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Backward.");
        backwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                double[] distanceToTheTargetLocation = new double[]{-10};
                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.MOVE_BACKWARD,
                        twoStateRoundedCornersButton, distanceToTheTargetLocation, serviceExecutionListener);
            }
        });
        addButton(backwardButton);


        rightTrackBackwardButton = new DirectionSetupButton(BuildUtils.getImageIcon(PREFIX + "transparent-bg-rb.gif"),
                BuildUtils.getImageIcon(PREFIX + "selected-rb.gif"),
                FIRST_STATE_OUTER_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_OUTER_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                FIRST_STATE_BACKROUND_COLOR, SECOND_STATE_BACKROUND_COLOR, "Right track backward.");
        rightTrackBackwardButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton twoStateRoundedCornersButton = (TwoStateRoundedCornersButton) e.getSource();
                twoStateRoundedCornersButton.setSecondStateCurrent();

                SemEventDistributor.distributeGuiStateModelEventToListeners(
                        SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                        SemEventDistributor.EventId.OPERATION_RIGHT_TRACK_BACKWARD,
                        twoStateRoundedCornersButton, null, serviceExecutionListener);
            }
        });
        addButton(rightTrackBackwardButton);
    }

    /**
     * @param button
     */
    private final void setupButton(RoundedCornersButton button) {
        int fontSize = 8;
        Font font = button.getFont();
        button.setFont(new Font(font.getName(), Font.BOLD, fontSize));
//        button.setGroup(buttonsGroup);
        button.setFocusPainted(false);
    }

    /**
     * @param button
     */
    public void addButton(RoundedCornersButton button) {
        add(button);
        buttonsGroup.add(button);
        setupButton(button);
    }

    /**
     *
     *
     */
    void stateChanged(boolean status) {
        setEnabled(status);
        RoundedCornersButton button;
        for (int i = 0; i < buttonsGroup.size(); i++) {
            button = buttonsGroup.get(i);
            button.setEnabled(status);
            if (button instanceof TwoStateRoundedCornersButton) {
                ((TwoStateRoundedCornersButton) button).setFirstStateCurrent();
            }
        }
        stopButton.setEnabled(status);
        repaint();
    }

    /**
     * called when other panal activated operation
     */
    private void cancelAllOperation() {
//        ModelController.getInstance().stopAllOperation();
        if (buttonsGroup.isEmpty()) {
            return;
        }
        for (RoundedCornersButton button : buttonsGroup) {
            button.reset();
            button.repaint();
        }
    }

}
