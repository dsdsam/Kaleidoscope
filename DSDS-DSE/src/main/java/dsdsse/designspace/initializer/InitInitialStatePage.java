package dsdsse.designspace.initializer;

import dsdsse.app.DsdsseEnvironment;
import dsdsse.graphview.PropertyViewBall;
import dsdsse.preferences.DsdsseUserPreference;
import mcln.model.MclnStatementState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Admin on 3/17/2016.
 */
final class InitInitialStatePage extends JPanel {

    private static final Dimension INITIAL_STATE_LABEL_SIZE = new Dimension(70, 20);
    private static final Dimension INITIAL_STATE_COLOR_VIEW_SIZE = new Dimension(40, 20);
    private static final Dimension INITIAL_STATE_RGB_LABEL_SIZE = new Dimension(40, 20);
    private static final Dimension INITIAL_STATE_RGB_DISPLAY_SIZE = new Dimension(80, 20);

    private final InitAssistantController initAssistantController;
    private final InitAssistantDataModel initAssistantDataModel;

    private final JLabel initialStateLabel = new JLabel("Initial State:", JLabel.LEFT);
    private final JPanel stateColorDisplayPanel = new JPanel();
    private final JLabel rgbLabel = new JLabel("RGB:", JLabel.CENTER);
    private final JLabel rgbStateDisplay = new JLabel("", JLabel.CENTER);
    private PropertyViewPanel propertyViewPanel = new PropertyViewPanel();

    /**
     * Listens to Initial State selection mouse click
     */
    private final InitAssistantDataModelListener initAssistantDataModelListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
                switch (attributeID) {
                    case InitialState:
                        setInitialStatementState(initAssistantDataModel);
//                        MclnStatementState initialMclnStatementState =
//                                initAssistantDataModel.getInitialMclnStatementState();
//                        System.out.println(
//                                "NameAndStateSetupPanel - initialMclnState = " + initialMclnStatementState.toString());
//                        stateColorDisplayPanel.setBackground(new Color(initialMclnStatementState.getColor()));
//                        rgbStateDisplay.setText(initialMclnStatementState.getHexColor());
                        break;
                }
            };

    private final ControllerRequestListener controllerRequestListener =
            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.PageNavigationRequest operation,
             InitAssistantController.InitializationPage page) -> {
                switch (operation) {
                    case NextPage:
                        if (page == InitAssistantController.InitializationPage.initInitialStatePage) {
                            setInitialStatementState(initAssistantDataModel);
                        }
                        break;
                }
            };

    InitInitialStatePage(
            InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());
        this.initAssistantController = initAssistantController;
        this.initAssistantDataModel = initAssistantDataModel;

        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), BorderFactory.createEtchedBorder()
        );
        setBorder(border);

        setInitialStatementState(initAssistantDataModel);

        boolean hasProgram = initAssistantDataModel.propertyHasProgram();

        initContents(hasProgram);
        initAssistantDataModel.addListener(initAssistantDataModelListener);
        initAssistantController.addListener(controllerRequestListener);
    }

    //
    private void setInitialStatementState(InitAssistantDataModel initAssistantDataModel) {
        MclnStatementState initialMclnStatementState = initAssistantDataModel.getInitialMclnStatementState();
        if (initialMclnStatementState == null) {
            rgbStateDisplay.setText("0xFFFFFF");
            propertyViewPanel.setPropertyState(null);
        } else {
            Color selectedColor = new Color(initialMclnStatementState.getRGB());
            stateColorDisplayPanel.setBackground(selectedColor);
            rgbStateDisplay.setText(initialMclnStatementState.getHexColor());
            propertyViewPanel.setPropertyState(selectedColor);
        }
    }

    //
    private void initContents(boolean hasProgram) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(InitAssistantUIColorScheme.INPUT_PANEL_BACKGROUND);

        mainPanel.add(propertyViewPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(initSelectedStatePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 4), 0, 0));

        mainPanel.add(initHasProgramCheckBox(hasProgram), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel initSelectedStatePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // initial state label
        initialStateLabel.setPreferredSize(INITIAL_STATE_LABEL_SIZE);
        initialStateLabel.setMaximumSize(INITIAL_STATE_LABEL_SIZE);
        DsdsseEnvironment.setCompFontSize(initialStateLabel, Font.BOLD, 11);
        initialStateLabel.setForeground(Color.blue);
        mainPanel.add(initialStateLabel);

        // initial state color display
        stateColorDisplayPanel.setPreferredSize(INITIAL_STATE_COLOR_VIEW_SIZE);
        stateColorDisplayPanel.setMaximumSize(INITIAL_STATE_COLOR_VIEW_SIZE);
        stateColorDisplayPanel.setBorder(new CompoundBorder(new LineBorder(Color.black), new LineBorder(Color.WHITE)));
        mainPanel.add(stateColorDisplayPanel);

        // HEX state label
        rgbLabel.setPreferredSize(INITIAL_STATE_RGB_LABEL_SIZE);
        rgbLabel.setMaximumSize(INITIAL_STATE_RGB_LABEL_SIZE);
        DsdsseEnvironment.setCompFontSize(rgbLabel, Font.BOLD, 11);
        rgbLabel.setForeground(Color.blue);
        mainPanel.add(rgbLabel);

        // HEX state display
        rgbStateDisplay.setPreferredSize(INITIAL_STATE_RGB_DISPLAY_SIZE);
        rgbStateDisplay.setMaximumSize(INITIAL_STATE_RGB_DISPLAY_SIZE);
        rgbStateDisplay.setOpaque(true);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new LineBorder(Color.WHITE)),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
        );
        rgbStateDisplay.setBorder(border);
        rgbStateDisplay.setBackground(Color.BLACK);
        rgbStateDisplay.setForeground(Color.WHITE);
        mainPanel.add(rgbStateDisplay);

        return mainPanel;
    }


    private JCheckBox initHasProgramCheckBox(boolean hasProgram) {
        JCheckBox hasProgramCheckBox = new JCheckBox(
                " The property has Input Simulating Program ", hasProgram);
//        hasProgramCheckBox.setSelected(propertyHasProgram);
        DsdsseEnvironment.setCompFontSize(hasProgramCheckBox, Font.BOLD, 11);
        hasProgramCheckBox.setOpaque(false);
        hasProgramCheckBox.addItemListener(initAssistantController.getProgramCheckBoxItemListener());
        return hasProgramCheckBox;
    }

    private static class PropertyViewPanel extends JPanel {

        private static final Color DEFAULT_BALL_COLOR = Color.LIGHT_GRAY;
        // 3D representation
        private PropertyViewBall propertyViewBall;

        PropertyViewPanel() {
            setOpaque(true);
            setBackground(Color.WHITE);
//            ballCurrentColor = (mclnState != null) ? new Color(mclnState.getColor()) : DEFAULT_BALL_COLOR;
            propertyViewBall = PropertyViewBall.createInstance(DEFAULT_BALL_COLOR);
        }

        void setPropertyState(Color stateColor) {
            stateColor = stateColor == null ? DEFAULT_BALL_COLOR : stateColor;
            propertyViewBall.setState(stateColor);
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D) g;
            Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean status = DsdsseUserPreference.isPropertyView3D();
            propertyViewBall.setDisplayMclnPropertyViewAs3DCircle(status);

            Rectangle bounds = getBounds();
            int centerX = bounds.width / 2;
            int centerY = bounds.height / 2;

            propertyViewBall.draw(g, centerX, centerY, false);

            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
        }
    }
}
