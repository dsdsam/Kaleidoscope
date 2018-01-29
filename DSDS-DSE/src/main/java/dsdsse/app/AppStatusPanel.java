package dsdsse.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 8, 2013
 * Time: 3:23:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppStatusPanel extends JPanel {

    private static final Color PANEL_BACKGROUND_COLOR = new Color(0xE0E0E0);
    private static final Font font = new Font("Arial", Font.BOLD, 11);
    private static final Font plainFont = new Font("Arial", Font.PLAIN, 11);
    private static final Color BLUE_COLOR = Color.BLUE;
    private static final int width = 60;
    private static final int height = 21;

    private static enum LabelAttributes {

        MODEL_LABEL("  Model:  ", 50, AppStatusPanel.height, AppStatusPanel.font,
                AppStatusPanel.BLUE_COLOR, Color.WHITE),
        MODEL_VALUE("None", 90, AppStatusPanel.height, AppStatusPanel.plainFont,
                Color.WHITE, Color.BLACK),

        MODE_LABEL("  Mode:  ", 50, AppStatusPanel.height, AppStatusPanel.font,
                AppStatusPanel.BLUE_COLOR, Color.WHITE),
        MODE_VALUE(AppStateModel.getInstance().getMode().getText(), 80, AppStatusPanel.height, AppStatusPanel.plainFont,
                Color.WHITE, Color.BLACK),

        OPERATION_LABEL("  Operation:  ", 80, AppStatusPanel.height, AppStatusPanel.font,
                AppStatusPanel.BLUE_COLOR, Color.WHITE),
        OPERATION_VALUE(AppStateModel.getInstance().getCurrentOperation().getText(), 90, AppStatusPanel.height, AppStatusPanel.plainFont,
                Color.WHITE, Color.BLACK),

        OPERATION_STEP_LABEL("  Step:  ", 50, AppStatusPanel.height, AppStatusPanel.font,
                AppStatusPanel.BLUE_COLOR, Color.WHITE),
        OPERATION_STEP_VALUE(AppStateModel.getInstance().getCurrentOperationStep().getText(), 90, AppStatusPanel.height, AppStatusPanel.plainFont,
                Color.WHITE, Color.BLACK),

        SIMULATION_TICKS_LABEL("  Ticks:  ", 50, AppStatusPanel.height, AppStatusPanel.font,
                AppStatusPanel.BLUE_COLOR, Color.WHITE),
        SIMULATION_TICKS_VALUE(AppStateModel.getInstance().getCurrentOperationStep().getText(), 90, AppStatusPanel.height, AppStatusPanel.plainFont,
                Color.WHITE, Color.BLACK);

        private String text;
        private int width;
        private int height;
        private Font font;
        private Color background;
        private Color foreground;

        private LabelAttributes(String text, int width, int height, Font font, Color background, Color foreground) {
            this.text = text;
            this.width = width;
            this.height = height;
            this.font = font;
            this.background = background;
            this.foreground = foreground;
        }
    }

    private static final JLabel createJLabel(LabelAttributes labelAttributes, boolean valueName) {
        JLabel label;
        if (valueName) {
            label = new JLabel(labelAttributes.text, JLabel.CENTER);
        } else {
            label = new JLabel(labelAttributes.text, JLabel.LEFT);
        }
        label.setOpaque(valueName);
        label.setBorder(new MatteBorder(2, 4, 2, 4, PANEL_BACKGROUND_COLOR));
//        Dimension size = new Dimension(labelAttributes.width, labelAttributes.height);
//        label.setSize(size);
//        label.setMinimumSize(size);
//        label.setMaximumSize(size);
//        label.setPreferredSize(size);
        label.setFont(labelAttributes.font);
        label.setBackground(labelAttributes.background);
        label.setForeground(labelAttributes.foreground);
        return label;
    }

    private final JLabel modelNameLabel =
            AppStatusPanel.createJLabel(LabelAttributes.MODEL_LABEL, true);
    private final JLabel modelValueLabel =
            AppStatusPanel.createJLabel(LabelAttributes.MODEL_VALUE, false);

    private final JLabel modeNameLabel =
            AppStatusPanel.createJLabel(LabelAttributes.MODE_LABEL, true);
    private final JLabel modeValueLabel =
            AppStatusPanel.createJLabel(LabelAttributes.MODE_VALUE, false);

    private final JLabel operationNameLabel =
            AppStatusPanel.createJLabel(LabelAttributes.OPERATION_LABEL, true);
    private final JLabel operationValueLabel =
            AppStatusPanel.createJLabel(LabelAttributes.OPERATION_VALUE, false);

    private final JLabel operationStepNameLabel =
            AppStatusPanel.createJLabel(LabelAttributes.OPERATION_STEP_LABEL, true);
    private final JLabel operationStepValueLabel =
            AppStatusPanel.createJLabel(LabelAttributes.OPERATION_STEP_VALUE, false);

    private final JLabel simulationTicksNameLabel =
            AppStatusPanel.createJLabel(LabelAttributes.SIMULATION_TICKS_LABEL, true);
    private final JLabel simulationTicksValueLabel =
            AppStatusPanel.createJLabel(LabelAttributes.SIMULATION_TICKS_VALUE, false);

    /**
     *
     */
    public AppStatusPanel() {
        super(new GridBagLayout());
        setBackground(PANEL_BACKGROUND_COLOR);

        Border lineBorder = new MatteBorder(0,0,1,0,Color.GRAY);
        setBorder(lineBorder);

        add(modelNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(modelValueLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(modeNameLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(modeValueLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(operationNameLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(operationValueLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(operationStepNameLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(operationStepValueLabel, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(simulationTicksNameLabel, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(simulationTicksValueLabel, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        JPanel placeHolder = new JPanel();
        placeHolder.setOpaque(false);
        add(placeHolder, new GridBagConstraints(10, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));

        AppStateModel.setAppStatusPanel(this);
    }

    public void updateModelName(String modelName) {
        modelValueLabel.setText(modelName);
    }

    public void updateMode() {
        modeValueLabel.setText(AppStateModel.getInstance().getMode().getText());
        if (AppStateModel.getInstance().isDevelopmentMode()) {
            operationNameLabel.setVisible(true);
            operationValueLabel.setVisible(true);
            operationStepNameLabel.setVisible(true);
            operationStepValueLabel.setVisible(true);
            simulationTicksNameLabel.setVisible(false);
            simulationTicksValueLabel.setVisible(false);
        } else {
            operationNameLabel.setVisible(false);
            operationValueLabel.setVisible(false);
            operationStepNameLabel.setVisible(false);
            operationStepValueLabel.setVisible(false);
            simulationTicksNameLabel.setVisible(true);
            simulationTicksValueLabel.setVisible(true);
        }
        invalidate();
        repaint();
    }

    public void updateStatus() {
        operationValueLabel.setText(AppStateModel.getInstance().getCurrentOperation().getText());
        if (AppStateModel.getInstance().isDevelopmentMode()) {
            operationStepValueLabel.setText(AppStateModel.getInstance().getCurrentOperationStep().getText());
        } else {
            simulationTicksValueLabel.setText(AppStateModel.getInstance().getSimulationTicks());
        }
        invalidate();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 53);

        AppStatusPanel appStatusPanel = new AppStatusPanel();
        frame.add(appStatusPanel);
        frame.setVisible(true);
    }


}
