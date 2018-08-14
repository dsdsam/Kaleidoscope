package mclnmatrixapp.appview;

import adf.ui.laf.button.Adf3DButton;
import adf.utils.StandardFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationControlButton extends Adf3DButton {

    private final ActionListener simulationControlsListener;

    private final ActionListener buttonActionListener = (e) -> {
        processRequestEven(e);
    };

    SimulationControlButton(String text, ActionListener simulationControlsListener) {
        super(text);
        addActionListener(buttonActionListener);
        this.simulationControlsListener = simulationControlsListener;

        Dimension buttonSize = new Dimension(200, 23);
        setPreferredSize(buttonSize);
        setMinimumSize(buttonSize);
        setMaximumSize(buttonSize);
        setFocusPainted(false);
        setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
    }

    private void processRequestEven(ActionEvent e) {
        e.setSource(e.getActionCommand());
        simulationControlsListener.actionPerformed(e);
    }

}
