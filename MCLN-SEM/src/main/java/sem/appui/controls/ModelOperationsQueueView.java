/*
 * Created on Aug 7, 2005
 *
 */
package sem.appui.controls;

import adf.ui.components.borders.AlignedRoundedBorder;
import adf.utils.BuildUtils;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.mission.controlles.modelcontroller.ModelOperationRequestQueue;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.MissionControlCenterPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xpadmin
 */
public class ModelOperationsQueueView extends JPanel {

    private final int MAX_ELEMENTS = 8;
    private final List<QueuedRequestView> ELEMENTS = new ArrayList();
    private final Dimension ITEM_SIZE = new Dimension(37, 37);
    private final Color BORDER_COLOR = new Color(0x008800);
    private final Color ACTIVE_BORDER_COLOR = new Color(0x00DD00);
    private final Color CURRENT_REQUEST_ACTIVE_BORDER_COLOR = new Color(0xCC0000);

    private final Dimension ENABLE_BUTTON_ITEM_SIZE = new Dimension(41, 41);
    private final Color ENABLE_BUTTON_FIRST_STATE_BORDER_COLOR = new Color(0x808080);
    private final Color ENABLE_BUTTON_FIRST_STATE_INNER_BORDER_COLOR = Color.RED; //Color.BLACK;
    private final Color ENABLE_BUTTON_SECOND_STATE_BORDER_COLOR = ENABLE_BUTTON_FIRST_STATE_BORDER_COLOR;
    private final Color ENABLE_BUTTON_SECOND_STATE_INNER_BORDER_COLOR = ENABLE_BUTTON_FIRST_STATE_INNER_BORDER_COLOR;
    private static final Color ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR = Color.BLACK;  //new Color(160,0,0);
    private static final Color ENABLE_BUTTON_SECOND_STATE_BACKROUND_COLOR = new Color(0, 110, 0);//Color.BLACK;
    private static final Color ENABLE_BUTTON_FIRST_STATE_FOREGROUND_COLOR = new Color(200, 0, 0);//Color.WHITE;
    private static final Color ENABLE_BUTTON_SECOND_STATE_FOREGROUND_COLOR = Color.WHITE;

//    private static final String ICON_NAME_TRANSITION = "ya223.png";
     private static final String ICON_NAME_TRANSITION = "blue-waves-4.png";
//    private static final String ICON_NAME_TRANSITION = "w.png";
//    private static final String ICON_NAME_TRANSITION = "wa223.png";

    public static final String PREFIX = "/sem-resources/images/directions/queue/";

    private Map<ModelMotionOperation, ImageIcon> operationToIconMap = new HashMap();

    {   // initialization

        operationToIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT,
                BuildUtils.getImageIcon(PREFIX + "spin.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE,
                BuildUtils.getImageIcon(PREFIX + "spin.gif"));

        operationToIconMap.put(ModelMotionOperation.OPERATION_TRANSMISSION,
                BuildUtils.getImageIcon(PREFIX + ICON_NAME_TRANSITION));

        operationToIconMap.put(ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT,
                BuildUtils.getImageIcon(PREFIX + "fw-to-the-point.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-fw.gif"));

        operationToIconMap.put(ModelMotionOperation.OPERATION_LEFT_TRACK_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-lf.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_MOVE_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-fw.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_RIGHT_TRACK_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-rf.gif"));

        operationToIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_RIGHT,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-lr.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_NONE, null);
//                AppHelper.getImageIcon(SPLASH_DIR_CLASS_PATH + "transparent-bg-stop.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_LEFT,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-rl.gif"));

        operationToIconMap.put(ModelMotionOperation.OPERATION_LEFT_TRACK_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-lb.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_MOVE_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-bw.gif"));
        operationToIconMap.put(ModelMotionOperation.OPERATION_RIGHT_TRACK_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "transparent-bg-rb.gif"));
    }

    private Map<ModelMotionOperation, ImageIcon> operationToSelectedIconMap = new HashMap();

    {   // initialization

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT,
                BuildUtils.getImageIcon(PREFIX + "spin.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE,
                BuildUtils.getImageIcon(PREFIX + "spin.gif"));

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_TRANSMISSION,
                BuildUtils.getImageIcon(PREFIX + ICON_NAME_TRANSITION));

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT,
                BuildUtils.getImageIcon(PREFIX + "selected-fw-to-the-point.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP,
                BuildUtils.getImageIcon(PREFIX + "selected-fw.gif"));

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_LEFT_TRACK_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-lf.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_MOVE_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-fw.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_RIGHT_TRACK_FORWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-rf.gif"));

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_RIGHT,
                BuildUtils.getImageIcon(PREFIX + "selected-lr.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_NONE, null);
//                AppHelper.getImageIcon(SPLASH_DIR_CLASS_PATH + "transparent-bg-stop.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_SPIN_TO_THE_LEFT,
                BuildUtils.getImageIcon(PREFIX + "selected-rl.gif"));

        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_LEFT_TRACK_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-lb.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_MOVE_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-bw.gif"));
        operationToSelectedIconMap.put(ModelMotionOperation.OPERATION_RIGHT_TRACK_BACKWARD,
                BuildUtils.getImageIcon(PREFIX + "selected-rb.gif"));
    }


    private final QueuedRequestView currentlyExecutedOperationView;
    private final TwoStateRoundedCornersButton enableButton;
    private boolean enableButtonCurrentlyInFirstState = true;
    private MissionControlCenterPanel missionControlCenterPanel;
    private final ModelController modelController;

    private boolean powerOn;
    private boolean queueOn = true;
    ModelOperationRequestQueue modelOperationRequestQueue;

    /**
     * @param missionControlCenterPanel
     * @param size
     */
    public ModelOperationsQueueView(ModelOperationRequestQueue modelOperationRequestQueue,
                                    MissionControlCenterPanel missionControlCenterPanel, Dimension size) {
        this.modelOperationRequestQueue = modelOperationRequestQueue;
        setSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setOpaque(false);
        this.missionControlCenterPanel = missionControlCenterPanel;
        this.setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        this.setOpaque(true);

        currentlyExecutedOperationView =
                new QueuedRequestView(operationToIconMap, operationToSelectedIconMap,
                        BORDER_COLOR, CURRENT_REQUEST_ACTIVE_BORDER_COLOR);
        currentlyExecutedOperationView.setPreferredSize(ITEM_SIZE);
        AlignedRoundedBorder alignedRoundedBorder = new AlignedRoundedBorder(2, 4, AlignedRoundedBorder.ROUNDING_BOTH,
//                BORDER_COLOR,
                null, Color.BLACK);
        currentlyExecutedOperationView.setBorder(alignedRoundedBorder);
        add(currentlyExecutedOperationView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE,
                new Insets(15, 0, 10, 0), 0, 0));

        int yLocation = 1;
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            QueuedRequestView queueRequestView =
                    new QueuedRequestView(operationToIconMap, operationToSelectedIconMap,
                            BORDER_COLOR, ACTIVE_BORDER_COLOR);
            queueRequestView.setPreferredSize(ITEM_SIZE);
            alignedRoundedBorder = new AlignedRoundedBorder(2, 4,
                    AlignedRoundedBorder.ROUNDING_BOTH,
//                    BORDER_COLOR,
                    null, Color.BLACK);
            queueRequestView.setBorder(alignedRoundedBorder);
            add(queueRequestView, new GridBagConstraints(0, yLocation++, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(6, 0, 0, 0), 0, 0));

            ELEMENTS.add(queueRequestView);
        }

        enableButton = new TwoStateRoundedCornersButton(Color.BLACK, 1, 4, DirectionSetupButton.ROUNDING_ALL,
                ENABLE_BUTTON_FIRST_STATE_BORDER_COLOR, null,
                ENABLE_BUTTON_SECOND_STATE_BORDER_COLOR, null,
                null, null,
                "Off", "On",
                ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR, ENABLE_BUTTON_FIRST_STATE_FOREGROUND_COLOR,
                ENABLE_BUTTON_SECOND_STATE_BACKROUND_COLOR, ENABLE_BUTTON_SECOND_STATE_FOREGROUND_COLOR,
                "Enable/Disable Operation Request Queue.");
        enableButton.setFont(new Font("Monospace", Font.BOLD, 12));
        enableButton.setSize(ENABLE_BUTTON_ITEM_SIZE);
        enableButton.setPreferredSize(ENABLE_BUTTON_ITEM_SIZE);
        enableButton.setMinimumSize(ENABLE_BUTTON_ITEM_SIZE);
        enableButton.setMaximumSize(ENABLE_BUTTON_ITEM_SIZE);

        boolean modelOperationsQueueOn = this.modelOperationRequestQueue.isQueueOn();
        enableButton.setBackground(modelOperationsQueueOn ?
                ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR : ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR);

        if (!modelOperationsQueueOn) {
            enableButton.setFirstStateCurrent();
        } else {
            enableButton.setSecondStateCurrent();
        }


        enableButton.setEnabled(false);
        enableButton.setFocusPainted(false);
        enableButton.addThisButtonActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoStateRoundedCornersButton enableButton = (TwoStateRoundedCornersButton) e.getSource();
                boolean queueSwitchedOn = enableButton.isInSecondState();
                // First we start or stop queue
                ModelOperationsQueueView.this.modelOperationRequestQueue.setQueueOn(queueSwitchedOn);
                // then if Gueue was stopped
                if (!queueSwitchedOn) {
                    // We stop the operation execution that might still be running
                    ModelController.getInstance().stopAllOperation();
                }
            }
        });
//        add(enableButton, new GridBagConstraints(0, yLocation, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));


        JPanel placeHolderPanel = new JPanel();
        placeHolderPanel.setOpaque(false);
        add(placeHolderPanel, new GridBagConstraints(0, 20, 1, 1, 0.0, 1.0,
                GridBagConstraints.SOUTH, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));

        modelController = ModelController.getInstance();

        // adding this View Panel as queue listener
        modelOperationRequestQueue.addModelOperationRequestQueueListener(modelOperationsQueueListener);
    }

    /**
     *
     */
    private static class QueuedRequestView extends JLabel {

        private Map<ModelMotionOperation, ImageIcon> operationToIconMap;
        private Map<ModelMotionOperation, ImageIcon> operationToSelectedIconMap;
        private Color disabledBorderColor = Color.DARK_GRAY;//new Color(0xaaaaaa);
        private Color inactiveBorderColor;
        private Color activeBorderColor;
        private Color currentBorderColor;
        private AlignedRoundedBorder alignedRoundedBorder;
        private ModelMotionOperation modelMotionOperation = ModelMotionOperation.OPERATION_NONE;

        QueuedRequestView(Map<ModelMotionOperation, ImageIcon> operationToIconMap,
                          Map<ModelMotionOperation, ImageIcon> operationToSelectedIconMap,
                          Color inactiveBorderColor, Color activeBorderColor) {
            super("", JLabel.CENTER);
            this.operationToIconMap = operationToIconMap;
            this.operationToSelectedIconMap = operationToSelectedIconMap;
            this.inactiveBorderColor = inactiveBorderColor;
            this.activeBorderColor = activeBorderColor;
            currentBorderColor = disabledBorderColor;
        }


        public final void setBorder(AlignedRoundedBorder alignedRoundedBorder) {
            this.alignedRoundedBorder = alignedRoundedBorder;
            this.alignedRoundedBorder.setOuterBorderColor(currentBorderColor);
            super.setBorder(alignedRoundedBorder);
            setEnabled(false);
        }

        @Override
        public final void setEnabled(boolean status) {
            if (status) {
                currentBorderColor = inactiveBorderColor;
            } else {
                currentBorderColor = disabledBorderColor;
            }
            this.alignedRoundedBorder.setOuterBorderColor(currentBorderColor);
            super.setEnabled(status);
        }

        public final void setOperation(ModelMotionOperation modelMotionOperation, boolean selected) {
            this.modelMotionOperation = modelMotionOperation;
            ImageIcon imageIcon;
            if (selected) {
                imageIcon = operationToSelectedIconMap.get(this.modelMotionOperation);
            } else {
                imageIcon = operationToIconMap.get(this.modelMotionOperation);
            }
            setIcon(imageIcon);
//            System.out.println("QueueItenView.setOperation:" + modelMotionOperation + ",  imageIcon = " + imageIcon);
            if (modelMotionOperation != ModelMotionOperation.OPERATION_NONE) {
                currentBorderColor = activeBorderColor;
            } else {
                currentBorderColor = inactiveBorderColor;
            }
            this.alignedRoundedBorder.setOuterBorderColor(currentBorderColor);
            repaint();
        }
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////////////////////////
     * Q U E U E     L I S T E N E R
     * /////////////////////////////////////////////////////////////////////////////////////////////////////
     */
    private ModelOperationsQueueListener modelOperationsQueueListener = new ModelOperationsQueueListener() {

        /**
         * @param modelOperationRequest
         * @param modelOperationRequestsInTheQueue
         *
         */
        public void modelOperationRequestEnqueued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                  ModelOperationRequest[] modelOperationRequestsInTheQueue) {
//        System.out.println("ModelQueueView.modelOperationRequestEnqueued XXXXXXXXXXXXXXXXXXXXXXX " +
//                modelOperationRequest.getRequestedOperation() + ",  " + modelOperationRequestsInTheQueue.length);
            updateQueueView(modelOperationRequestsInTheQueue);
        }

        /**
         * @param modelOperationRequest
         * @param modelOperationRequestsInTheQueue
         *
         */
        public void modelOperationRequestDequeued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                  ModelOperationRequest[] modelOperationRequestsInTheQueue) {
//        System.out.println("ModelQueueView.modelOperationRequestDequeued YYYYYYYYYYYYYYYYYYYY " +
//                modelOperationRequest.getRequestedOperation() + ",  " + modelOperationRequestsInTheQueue.length);
//        enableButton.setEnabled(false);
            ModelMotionOperation actualMotionOperation = modelOperationRequest.getActualOperation();
            updateCurrentOperation(actualMotionOperation);
            updateQueueView(modelOperationRequestsInTheQueue);
        }

        /**
         * @param modelOperationRequest
         * @param modelOperationRequestsInTheQueue
         *
         */
        public void modelOperationRequestInterrupted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                     ModelOperationRequest[] modelOperationRequestsInTheQueue) {
//        System.out.println("ModelQueueView.modelOperationRequestInterrupted IIIIIIIIIIIIIIIIIIIIII " +
//                modelOperationRequest.getRequestedOperation() + ",  " + modelOperationRequestsInTheQueue.length);
//        getIconForOperation(modelOperationRequest);
        }

        /**
         * @param modelOperationRequest
         * @param modelOperationRequestsInTheQueue
         *
         */
        public void modelOperationRequestExecuted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                  ModelOperationRequest[] modelOperationRequestsInTheQueue) {
//        System.out.println("ModelQueueView.modelOperationRequestExecuted ZZZZZZZZZZZZZZZZZZZZ " +
//                modelOperationRequest.getRequestedOperation() + ",  " + modelOperationRequestsInTheQueue.length);
//        enableButton.setEnabled(true);
            updateCurrentOperation(ModelMotionOperation.OPERATION_NONE);
        }


        public void modelOperationsQueuePowerOn(boolean powerOn, ModelOperationRequest[] modelOperationRequestsInTheQueue) {
            ModelOperationsQueueView.this.powerOn = powerOn;
            enableButton.setEnabled(powerOn);
            enableButton.setBackground(powerOn ?
                    ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR : ENABLE_BUTTON_FIRST_STATE_BACKROUND_COLOR);
//        System.out.println("Queue View modelOperationRequestExecuted  "+powerOn );

            if (!queueOn) {
                enableButton.setFirstStateCurrent();
            } else {
                enableButton.setSecondStateCurrent();
            }

            boolean enable = powerOn && queueOn;

            if (!enable) {
                updateCurrentOperation(ModelMotionOperation.OPERATION_NONE);
                updateQueueView(modelOperationRequestsInTheQueue);
            }
            enableQueue(enable);
        }


        public void modelOperationsQueueStatusOn(boolean queueOn, ModelOperationRequest[] modelOperationRequestsInTheQueue) {
            ModelOperationsQueueView.this.queueOn = queueOn;
            if (!queueOn) {
                enableButton.setFirstStateCurrent();
            } else {
                enableButton.setSecondStateCurrent();
            }


            boolean enable = powerOn && queueOn;

            if (!enable) {
                updateCurrentOperation(ModelMotionOperation.OPERATION_NONE);
                updateQueueView(modelOperationRequestsInTheQueue);
            }
            enableQueue(enable);
        }

        public void modelOperationsQueueCleard(ModelOperationRequest[] modelOperationRequestsInTheQueue) {
            updateCurrentOperation(ModelMotionOperation.OPERATION_NONE);
            updateQueueView(modelOperationRequestsInTheQueue);
        }

        /**
         * @param status
         */
        private void enableQueue(boolean status) {
            setEnabled(status);
            // set current operation view enabled/disabled
            currentlyExecutedOperationView.setEnabled(status);
            // set all queued operations view enabled/disabled
            for (int i = 0; i < MAX_ELEMENTS; i++) {
                QueuedRequestView queueRequestView = ELEMENTS.get(i);
                queueRequestView.setEnabled(status);
            }
        }

        /**
         * @param modelMotionOperation
         */
        private void updateCurrentOperation(ModelMotionOperation modelMotionOperation) {
//         ModelMotionOperation actualMotionOperation = modelOperationRequest.getActualOperation();
//        System.out.println("ModelQueueView.updateCurrentlyExecutedOperation: actualMotionOperation = "
//                + actualMotionOperation);
            currentlyExecutedOperationView.setOperation(modelMotionOperation, true);
        }

        /**
         * @param modelOperationRequestsInQueue
         */
        private void updateQueueView(ModelOperationRequest[] modelOperationRequestsInQueue) {
            int queueCurrentSize = modelOperationRequestsInQueue.length;
//            System.out.println("ModelQueueView.updateQueueView:  queueCurrentSize = " + queueCurrentSize);
            for (int i = 0; i < MAX_ELEMENTS; i++) {
                QueuedRequestView currentQueueRequestView = ELEMENTS.get(i);
                if (i < queueCurrentSize) {
                    ModelOperationRequest<ModelMotionOperation, Double> modelOperationRequest =
                            modelOperationRequestsInQueue[i];
                    ModelMotionOperation actualMotionOperation = modelOperationRequest.getActualOperation();
                    if (actualMotionOperation == null) {
                        actualMotionOperation = modelOperationRequest.getRequestedOperation();
                    }
                    currentQueueRequestView.setOperation(actualMotionOperation, false);
                } else {
                    currentQueueRequestView.setOperation(ModelMotionOperation.OPERATION_NONE, false);
                }
            }
        }
    };
}
