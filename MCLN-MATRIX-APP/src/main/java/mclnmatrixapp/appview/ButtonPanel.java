package mclnmatrixapp.appview;

import adf.utils.StandardFonts;
import mclnmatrix.view.MclnMatrixView;
import mclnmatrix.view.ViewLayoutToggleButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    private static final Dimension SIMULATION_BUTTON_SIZE = new Dimension(150, 24);

    public static final String START_SIMULATION_REQUEST = "Start Simulation";
    public static final String STOP_SIMULATION_REQUEST = "Stop Simulation";
    public static final String ONE_SIMULATION_STEP_REQUEST = "One Simulation Step";
    public static final String RESET_SIMULATION_REQUEST = "Reset Simulation";

    private ViewLayoutToggleButton viewLayoutButton;

    public ButtonPanel(ActionListener toggleRequestActionListener, ActionListener simulationControlsListener) {
        super(new GridBagLayout());
        initLayout(toggleRequestActionListener, simulationControlsListener);
        setBorder(new LineBorder(Color.GRAY));
    }

    public void resetDefaultLayout(boolean suggestedLayoutIsHorizontal) {
        viewLayoutButton.resetDefaultLayout(suggestedLayoutIsHorizontal);
    }

    /**
     * @param toggleRequestActionListener
     * @param simulationControlsListener
     */
    private void initLayout(ActionListener toggleRequestActionListener, ActionListener simulationControlsListener) {

        JButton startStopSimulationButton = new ViewLayoutToggleButton(STOP_SIMULATION_REQUEST, START_SIMULATION_REQUEST,
                simulationControlsListener);
        startStopSimulationButton.setFocusPainted(false);
        startStopSimulationButton.setPreferredSize(SIMULATION_BUTTON_SIZE);
        startStopSimulationButton.setMinimumSize(SIMULATION_BUTTON_SIZE);
        startStopSimulationButton.setMaximumSize(SIMULATION_BUTTON_SIZE);
        add(startStopSimulationButton,
                new GridBagConstraints(0, 0, 1, 1, 0, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 20, 0, 0), 0, 0));

        //
        JButton simulationStepButton = new SimulationControlButton(ONE_SIMULATION_STEP_REQUEST, simulationControlsListener);
        simulationStepButton.setFocusPainted(false);
        simulationStepButton.setPreferredSize(SIMULATION_BUTTON_SIZE);
        simulationStepButton.setMinimumSize(SIMULATION_BUTTON_SIZE);
        simulationStepButton.setMaximumSize(SIMULATION_BUTTON_SIZE);
        add(simulationStepButton,
                new GridBagConstraints(1, 0, 1, 1, 0, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
        //
        JButton resetSimulationButton = new SimulationControlButton(RESET_SIMULATION_REQUEST, simulationControlsListener);
        resetSimulationButton.setFocusPainted(false);
        resetSimulationButton.setPreferredSize(SIMULATION_BUTTON_SIZE);
        resetSimulationButton.setMinimumSize(SIMULATION_BUTTON_SIZE);
        resetSimulationButton.setMaximumSize(SIMULATION_BUTTON_SIZE);
        add(resetSimulationButton,
                new GridBagConstraints(2, 0, 1, 1, 0, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 20), 0, 0));
        //
        viewLayoutButton = new ViewLayoutToggleButton(MclnMatrixView.HORIZONTAL_LAYOUT_REQUEST,
                MclnMatrixView.VERTICAL_LAYOUT_REQUEST, toggleRequestActionListener);
        add(viewLayoutButton,
                new GridBagConstraints(1, 1, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(5, 0, 5, 0), 0, 0));
    }
}
