package dsdsse.designspace.initializer;

import dsdsse.preferences.DsdsseUserPreference;
import mcln.model.MclnStatement;
import mclnview.graphview.PropertyViewBall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Admin on 7/24/2016.
 */
class PropertyTestingPanel extends JPanel {

    public static final int RADIUS = 7;
    private static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    private static final Color DEFAULT_BALL_COLOR = Color.LIGHT_GRAY;//new Color(0xF0F0F0);


    //   I n s t a n c e

    private final GeneratorTestingPanel generatorTestingPanel;
    private final InitAssistantDataModel initAssistantDataModel;
    // 3D representation
    private final PropertyViewBall propertyViewBall;

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent me) {
            boolean presentingTimeDrivenGenerator = initAssistantDataModel.isSelectedProgramTimeDrivenProgram();
            boolean rightMouseButtonPressed = (me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0;
            if (presentingTimeDrivenGenerator || !rightMouseButtonPressed) {
                return;
            }
            SimulationPopupMenu simulationPopupMenu = SimulationPopupMenu.getPopupMenu(generatorTestingPanel,
                    initAssistantDataModel);
            int x = me.getX();
            int y = me.getY();
            simulationPopupMenu.show(PropertyTestingPanel.this, x, y);
            me.consume();
        }
    };

    PropertyTestingPanel(GeneratorTestingPanel generatorTestingPanel, InitAssistantDataModel initAssistantDataModel) {
        this.generatorTestingPanel = generatorTestingPanel;
        this.initAssistantDataModel = initAssistantDataModel;
        initAssistantDataModel.isSelectedProgramTimeDrivenProgram();
        setOpaque(true);
        setBackground(Color.WHITE);

        propertyViewBall = PropertyViewBall.createInstance(DEFAULT_BALL_COLOR);
        addMouseListener(mouseAdapter);
    }

    /**
     *
     */
    void destroyContents() {
        removeMouseListener(mouseAdapter);
    }

    void setPropertyState(Color stateColor) {
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

    //
    //   S i m u l a t i o n
    //

    private boolean simulateModelInput() {
        boolean inputGenerated = false;
        java.util.List<MclnStatement> mclnStatements = null;
        for (MclnStatement mclnStatementForProgramSimulation : mclnStatements) {
            String advancedState = ""; //mclnStatementForProgramSimulation.doInputSimulatingProgramStep();

            if (advancedState == null) {
                continue;
            }

            inputGenerated = true;
        }
        return inputGenerated;
    }

    String doInputSimulatingProgramStep() {
//        if (inputSimulatingProgram == null) {
//            return null;
//        }
//        boolean done = inputSimulatingProgram.executeStep();
//        if (done) {
//            return getUID() + ":" + getCurrentMclnState().toView();
//        }
        return null;
    }
}
