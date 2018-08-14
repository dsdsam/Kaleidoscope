package dsdsse.designspace.initializer;

import dsdsse.app.DsdsseEnvironment;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Admin on 3/17/2016.
 */
final class ArcStateSetupPanel extends JPanel {

    private static final Dimension INITIAL_STATE_LABEL_SIZE = new Dimension(120, 20);
    private static final Dimension INITIAL_STATE_RGB_DISPLAY_SIZE = new Dimension(80, 20);

    private static final String INIT_RECOGNIZING_ARC = "Recognizing Arc RGB:";
    private static final String INIT_GENERATING_ARC = "Generating Arc RGB:";

    private Color arrowColor = Color.WHITE;
    private final JLabel initialStateLabel = new JLabel("", JLabel.LEFT);
    private final JLabel rgbStateDisplay = new JLabel("", JLabel.CENTER);

    private final ArcViewPanel arcViewPanel;

    private final InitAssistantDataModelListener initAssistantDataModelListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
                switch (attributeID) {
                    case ArcState:
                        MclnState arcSelectedMclnState = initAssistantDataModel.getArcSelectedMclnState();
                        setArcSelectedMclnState(arcSelectedMclnState);
                        break;
                }
            };

    //
    //   C r e a t i o n
    //

    ArcStateSetupPanel(InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), BorderFactory.createEtchedBorder()
        );
        setBorder(border);

        Polygon arcArrowPoints = initAssistantDataModel.getArcArrowPoints();
        arcViewPanel = new ArcViewPanel(arcArrowPoints);
        MclnState arcSelectedMclnState = initAssistantDataModel.getArcSelectedMclnState();
        setArcSelectedMclnState(arcSelectedMclnState);

        if (initAssistantDataModel.isRecognizingArc()) {
            initialStateLabel.setText(INIT_RECOGNIZING_ARC);
        } else {
            initialStateLabel.setText(INIT_GENERATING_ARC);
        }

        initContents();
        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    /**
     * @param mclnState
     */
    private void setArcSelectedMclnState(MclnState mclnState) {
        if (mclnState == null) {
            rgbStateDisplay.setText("0xFFFFFF");
            arrowColor = Color.WHITE;
        } else {
            arrowColor = new Color(mclnState.getRGB());
            rgbStateDisplay.setText(mclnState.getHexColor());
        }
        repaint();
    }

    private void initContents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(InitAssistantUIColorScheme.INPUT_PANEL_BACKGROUND);

        mainPanel.add(arcViewPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // initial state RGB panel
        mainPanel.add(initSelectedStatePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel initSelectedStatePanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        // initial state label
        initialStateLabel.setPreferredSize(INITIAL_STATE_LABEL_SIZE);
        initialStateLabel.setMaximumSize(INITIAL_STATE_LABEL_SIZE);
        DsdsseEnvironment.setCompFontSize(initialStateLabel, Font.BOLD, 11);
        initialStateLabel.setForeground(Color.BLUE);
        mainPanel.add(initialStateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(Box.createHorizontalStrut(15), new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        // RGB state display
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
        mainPanel.add(rgbStateDisplay, new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(rgbStateDisplay);
        return mainPanel;
    }

    /**
     * Represents McLN Arc Arrow
     */
    private class ArcViewPanel extends JPanel {
        private Polygon arcArrowPoints;

        ArcViewPanel(Polygon arcArrowPoints) {
            this.arcArrowPoints = arcArrowPoints;
            setOpaque(true);
            setBackground(Color.WHITE);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            drawArrow(g);
        }

        private void drawArrow(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Polygon newPolygon = new Polygon(arcArrowPoints.xpoints, arcArrowPoints.ypoints, arcArrowPoints.npoints);
            Rectangle rectangle = newPolygon.getBounds();
            newPolygon.translate(-rectangle.x, -rectangle.y);

            Rectangle panelBounds = getBounds();
            int midX = panelBounds.width / 2;
            int midY = panelBounds.height / 2;
            newPolygon.translate(midX, midY);

            Rectangle arrowOutline = newPolygon.getBounds();
            newPolygon.translate(-((arrowOutline.width / 2)), -((arrowOutline.height / 2)));

            g2D.setColor(arrowColor);
            g2D.fill(newPolygon);
            g2D.setColor(Color.DARK_GRAY);
            g2D.draw(newPolygon);

            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
        }
    }
}
