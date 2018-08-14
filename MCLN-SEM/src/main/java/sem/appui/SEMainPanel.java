/*
 * Created on Jul 31, 2005
 *
 */
package sem.appui;

import adf.csys.view.CSysView;
import adf.onelinemessage.AdfOneLineMessageManager;
import adf.ui.components.panels.DigitalClockPanel;
import adf.ui.components.panels.ImagePanel;
import adf.utils.BuildUtils;
import sem.app.SEMApp;
import sem.app.TransmissionPanelBuilder;
import sem.appui.components.panes.tree.ModelTreePanel;
import sem.appui.controls.ModelOperationsQueueView;
import sem.appui.controls.SpaceRotationControlPanel;
import sem.appui.controls.VerticalRotationPanel;
import sem.appui.controls.ViewRotationController;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelOperationRequestQueue;
import sem.mission.csysbasedviews.*;
import sem.mission.explorer.model.SpaceExplorerModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author xpadmin
 */
public class SEMainPanel extends JPanel {

    private final String CONTROL_PANEL = "CONTROL_PANEL";
    private final String MODEL_TREE_PANEL = "MODEL_TREE_PANEL";
    private final String SWITCH_TO_CONTROL_PANEL = "Switch to Control Panel";
    private final String SWITCH_TO_MODEL_TREE_PANEL = "Switch to Model Tree";

    private Dimension MODEL_OPERATION_QUEUE_VIEW_SIZE = new Dimension(50, 0);
    private Color BACKGROUND = UIManager.getColor("sem.app.ui.main.panel.background");

    private MainMonitorCSysView mainMonitorCSysCiew;
    private MonitorFrontCSysView monitorFrontCSysView;
    private SeBasicCSysView robotSeBasicCSys;
    private MonitorSideCSysView monitorSideCSysView;
    private SpaceRotationControlPanel spaceRotationControlPanel;
    private VerticalRotationPanel verticalRotationPanel;
    private MissionTelemetryPanel missionTelemetryPanel;
    private ModelTreePanel modelTreePanel;

    private ViewRotationControllerListener viewRotationControllerListener = new ViewRotationControllerListener() {

        /**
         * @param verticalTargetValue
         * @param increment
         * @return true when target is reached
         */
        public boolean onRotateVertilally(int verticalTargetValue, int increment) {
            increment = Math.abs(increment);
            JSlider slider = spaceRotationControlPanel.getSlider();
            int direction = verticalTargetValue - slider.getValue();
            if (direction == 0 || Math.abs(direction) <= increment) {
                slider.setValue(verticalTargetValue);
                return true;
            }
            increment = Math.abs(increment);
            if (direction > 0) {
                slider.setValue(slider.getValue() + increment);
            } else {
                slider.setValue(slider.getValue() - increment);
            }
            return false;
        }

        /**
         * @param horizontalTargetValue
         * @param increment
         * @return true when target is reached
         */
        public boolean onRotateHorizontally(int horizontalTargetValue, int increment) {
            increment = Math.abs(increment);
            JSlider slider = verticalRotationPanel.getSlider();
            int direction = horizontalTargetValue - slider.getValue();
            if (direction == 0 || Math.abs(direction) <= increment) {
                slider.setValue(horizontalTargetValue);
                return true;
            }

            if (direction > 0) {
                slider.setValue(slider.getValue() + increment);
            } else {
                slider.setValue(slider.getValue() - increment);
            }
            return false;
        }
    };

    private KeyboardFocusManager seKeyboardFocusManager = new DefaultKeyboardFocusManager() {
        public boolean dispatchKeyEvent(KeyEvent e) {

            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                JSlider slider = spaceRotationControlPanel.getSlider();
                if (!slider.hasFocus()) {
                    if (keyCode == KeyEvent.VK_UP) {
                        slider.setValue(slider.getValue() + 1);
                    } else if (keyCode == KeyEvent.VK_DOWN) {
                        slider.setValue(slider.getValue() - 1);
                    }
                    slider.requestFocusInWindow();
                }
            }

            if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
                JSlider slider = verticalRotationPanel.getSlider();
                if (!slider.hasFocus()) {
                    if (keyCode == KeyEvent.VK_RIGHT) {
                        slider.setValue(slider.getValue() + 1);
                    } else if (keyCode == KeyEvent.VK_LEFT) {
                        slider.setValue(slider.getValue() - 1);
                    }
                    slider.requestFocusInWindow();
                } else {
                    if ((keyCode == KeyEvent.VK_RIGHT) &&
                            (slider.getValue() >= 178)) {
                        slider.setValue(-180);
                    } else if ((keyCode == KeyEvent.VK_LEFT) &&
                            (slider.getValue() == -180)) {
                        slider.setValue(180);
                    }
                }
            }

            if (keyCode == KeyEvent.VK_F1) {
                ModelOperationRequestQueue.getModelOperationRequestQueue().checkIfQueueOK();
            }
            return super.dispatchKeyEvent(e);
        }
    };

    private ChangeListener spaceRotationSliderListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int angle = (int) source.getValue();
                mainMonitorCSysCiew.doXRotation(angle);
                mainMonitorCSysCiew.repaint();
            }
        }
    };

    private ChangeListener horizontalSliderListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int angle = (int) source.getValue();
                mainMonitorCSysCiew.doZRotation(angle);
                mainMonitorCSysCiew.repaint();
            }
        }
    };

    public SEMainPanel() {
        System.out.println("SEMainPanel start");
        KeyboardFocusManager.setCurrentKeyboardFocusManager(seKeyboardFocusManager);
        setBackground(BACKGROUND);
        System.out.println("SEMainPanel initContent start");
        initContent();
        System.out.println("SEMainPanel initContent ready");
        ViewRotationController.getInstance().addViewRotationControllerListener(viewRotationControllerListener);
    }

    // ===============    I n i t i a l i z a t i o n    ===============

    private void initContent() {

        setLayout(new BorderLayout());
        setBorder(null);

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                createMainControlPanel(), createMainDisplayPanel());
        verticalSplitPane.setDividerLocation(-1);
        verticalSplitPane.setDividerSize(2);


        add(verticalSplitPane, BorderLayout.CENTER);

        add(initMessagePanel(), BorderLayout.SOUTH);
    }


    /**
     * @return JPanel
     */
    private JPanel createMainControlPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
//        JPanel panel = new ImagePanel(AdfEnv.getImageIcon( "12.png" ));
        panel.setLayout(new GridBagLayout());
        Dimension size = new Dimension(330, 0);
        panel.setSize(size);
        panel.setPreferredSize(size);
        panel.setMaximumSize(size);
        panel.setMinimumSize(size);

        panel.add(SubjectNamePanel.getSingleton(),
                new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

        panel.add(initTabPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        MclnControllerHolderPanel mclnGraphViewHolderPanel = MclnControllerHolderPanel.getSingleton();
        panel.add(mclnGraphViewHolderPanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        panel.add(initClockPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        panel.add(Box.createHorizontalGlue(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    private JPanel createMainDisplayPanel() {
        JPanel mainDisplayPane = new JPanel();
        mainDisplayPane.setBorder(null);
        mainDisplayPane.setLayout(new GridBagLayout());
        mainDisplayPane.setMinimumSize(new Dimension(300, 0));


        JPanel missionPanel = new JPanel();
        missionPanel.setBorder(null);
        missionPanel.setLayout(new GridBagLayout());
        Dimension size = new Dimension(300, 0);
        missionPanel.setMinimumSize(size);

        missionPanel.add(PredicatePanel.getSingleton(),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

        ModelOperationRequestQueue modelOperationRequestQueue = ModelController.getInstance().getModelOperationRequestQueue();
        ModelOperationsQueueView modelOperationQueueView = new ModelOperationsQueueView(modelOperationRequestQueue,
                MissionControlCenterPanel.getInstance(), MODEL_OPERATION_QUEUE_VIEW_SIZE);

        missionPanel.add(initCSysViews(modelOperationQueueView), new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        mainDisplayPane.add(missionPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        return mainDisplayPane;
    }

    private JPanel initTabPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Dimension size = new Dimension(150, 40);
        panel.setMaximumSize(size);
        panel.setPreferredSize(size);
        panel.setMinimumSize(size);

        final CardLayout multiViewPanelCardLayout = new CardLayout();
        final JPanel multiViewPanel = new JPanel(multiViewPanelCardLayout);

        MissionControlCenterPanel missionControlCenterPanel = MissionControlCenterPanel.getInstance();
        multiViewPanel.add(missionControlCenterPanel, CONTROL_PANEL);

//        SEMTreePanel modelTree = (SEMTreePanel)initTreePanel();
        Border border = BorderFactory.createLoweredBevelBorder();
        //     modelTree.setBorder( border );
        JPanel modelTreePanel = createModelTreePanel();
        multiViewPanel.add(modelTreePanel, MODEL_TREE_PANEL);

        multiViewPanelCardLayout.show(multiViewPanel, CONTROL_PANEL);
        panel.add(multiViewPanel, BorderLayout.CENTER);
        multiViewPanelCardLayout.layoutContainer(multiViewPanel);

        // Panel switch button
        final JButton switchPanelButton = new JButton(SWITCH_TO_MODEL_TREE_PANEL);
        switchPanelButton.setFocusPainted(false);
        switchPanelButton.setBorder(BorderFactory.createEtchedBorder());
        switchPanelButton.setFont(new Font(switchPanelButton.getName(), Font.PLAIN, 9));
        switchPanelButton.setActionCommand(MODEL_TREE_PANEL);
        switchPanelButton.setText(SWITCH_TO_MODEL_TREE_PANEL);
        switchPanelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if (command.equalsIgnoreCase(CONTROL_PANEL)) {
                    System.out.println("CONTROL_PANEL");
                    multiViewPanelCardLayout.show(multiViewPanel, CONTROL_PANEL);
                    switchPanelButton.setActionCommand(MODEL_TREE_PANEL);
                    switchPanelButton.setText(SWITCH_TO_MODEL_TREE_PANEL);
                } else {
                    System.out.println("MODEL_TREE_PANEL");
                    multiViewPanelCardLayout.show(multiViewPanel, MODEL_TREE_PANEL);
                    switchPanelButton.setActionCommand(CONTROL_PANEL);
                    switchPanelButton.setText(SWITCH_TO_CONTROL_PANEL);
                }
            }
        });

        panel.add(switchPanelButton, BorderLayout.SOUTH);
        return panel;
    }

    /**
     *
     */
    private JPanel createModelTreePanel() {
        modelTreePanel = new ModelTreePanel(BuildUtils.getImageIcon(SEMApp.ICONS_DIR_CLASS_PATH + "vvdark_bg.png"));
        return modelTreePanel;
    }

    /**
     * @return
     */
    private JPanel initClockPanel() {
//        JPanel panel = new JPanel();
        JPanel panel = new ImagePanel(BuildUtils.getImageIcon(SEMApp.ICONS_DIR_CLASS_PATH + "12.png"));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                new Color(0xDDDDDD), Color.LIGHT_GRAY, Color.GRAY, Color.GRAY));
        panel.setLayout(new BorderLayout());
        panel.setLayout(new GridBagLayout());
        Dimension size = new Dimension(140, 33);

        DigitalClockPanel clPanel = new DigitalClockPanel();
        clPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        clPanel.setPreferredSize(size);
        clPanel.setMaximumSize(size);
        clPanel.setMinimumSize(size);
        panel.add(clPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        return panel;
    }

    /**
     *
     */
    private JPanel initCSysViews(ModelOperationsQueueView modelOperationQueueView) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        panel.setLayout(new GridBagLayout());

        panel.add(initMainViewPanel(modelOperationQueueView), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        JPanel lowPanel = new JPanel();
        lowPanel.setPreferredSize(new Dimension(0, 100));
        lowPanel.setLayout(new GridBagLayout());

        lowPanel.add(initFrontViewPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        lowPanel.add(initRobotViewPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        lowPanel.add(initSideViewPanel(), new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        missionTelemetryPanel = initMissionTelemetryPanel();
        if (missionTelemetryPanel != null) {
            lowPanel.add(missionTelemetryPanel, new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        panel.add(lowPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    /**
     *
     */
    private JPanel initMainViewPanel(ModelOperationsQueueView modelOperationQueueView) {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setLayout(new GridBagLayout());

        panel.add(modelOperationQueueView, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        mainMonitorCSysCiew = new MainMonitorCSysView(-50, 30, 100, 60, CSysView.DRAW_AXIS);
        mainMonitorCSysCiew.setProjection(CSysView.YOXProjection);
        panel.add(mainMonitorCSysCiew, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));

        spaceRotationControlPanel = new SpaceRotationControlPanel();
        spaceRotationControlPanel.addSliderChangeListener(spaceRotationSliderListener);
        panel.add(spaceRotationControlPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        Dimension spaceHolderPanelPreferredSize = new Dimension(50, 50);
        JPanel leftSpaceHolderPanel = new JPanel();
        leftSpaceHolderPanel.setBackground(Color.BLACK);
        leftSpaceHolderPanel.setPreferredSize(spaceHolderPanelPreferredSize);
        panel.add(leftSpaceHolderPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        verticalRotationPanel = new VerticalRotationPanel();
        verticalRotationPanel.addSliderChangeListener(horizontalSliderListener);
        panel.add(verticalRotationPanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        JPanel rightSpaceHolderPanel = new JPanel();
        rightSpaceHolderPanel.setBackground(Color.BLACK);
        rightSpaceHolderPanel.setPreferredSize(spaceHolderPanelPreferredSize);
        panel.add(rightSpaceHolderPanel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    /**
     *
     */
    private JPanel initFrontViewPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(130, 0));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        monitorFrontCSysView = new MonitorFrontCSysView(-10, -4, 20, 16, 2, CSysView.DRAW_AXIS);
        monitorFrontCSysView.setProjection(CSysView.ZOYProjection);
        panel.add(monitorFrontCSysView, BorderLayout.CENTER);
        return panel;
    }

    /**
     *
     */
    private JPanel initRobotViewPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0x990000))));

        robotSeBasicCSys = new RobotView(-2, 0, 4, 1, 0, CSysView.DRAW_AXIS);
        panel.add(robotSeBasicCSys, BorderLayout.CENTER);

        main.add(panel, BorderLayout.CENTER);
        return main;
    }

    /**
     *
     */
    private JPanel initSideViewPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(130, 0));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        monitorSideCSysView = new MonitorSideCSysView(-10, -4, 20, 16, 2, CSysView.DRAW_AXIS);
        monitorSideCSysView.setProjection(CSysView.ZOXProjection);
        panel.add(monitorSideCSysView, BorderLayout.CENTER);
        return panel;
    }

    /**
     * @return MissionTelemetryPanel
     */
    private MissionTelemetryPanel initMissionTelemetryPanel() {
        TransmissionPanelBuilder transmissionPanelBuilder = TransmissionPanelBuilder.getInstance();
        MissionTelemetryPanel missionTelemetryPanel = transmissionPanelBuilder.buildEmbeddedMissionTelemetryPanel();
        return missionTelemetryPanel;
    }

    // -------------------------------------------------------------
    private JPanel initMessagePanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JPanel panel = AdfOneLineMessageManager.getOneLineMessagePanelInstance();
        panel.setPreferredSize(new Dimension(0, 20));
        panel.setMinimumSize(new Dimension(0, 20));
        bottomPanel.add(panel, BorderLayout.SOUTH);

        return bottomPanel;
    }

//  ===============    E n d  o f   I n i t i a l i z a t i o n    ===============

//  ===============    =========================================   ===============

    public void buildModelRepresentation(SpaceExplorerModel spaceExplorerModel) {

        mainMonitorCSysCiew.buildRepresentation(spaceExplorerModel);
        monitorFrontCSysView.buildRepresentation(spaceExplorerModel);
        robotSeBasicCSys.buildRepresentation(spaceExplorerModel);
        monitorSideCSysView.buildRepresentation(spaceExplorerModel);

        spaceExplorerModel.addModelChangeListener(mainMonitorCSysCiew);
        spaceExplorerModel.addModelChangeListener(robotSeBasicCSys);

        modelTreePanel.buildModelRepresentation(spaceExplorerModel);
    }

}
