package mclnmatrix.view;

import adf.ui.laf.button.Adf3DButton;
import adf.utils.StandardFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewLayoutToggleButton extends Adf3DButton {

    private final String orgFirstStateText;
    private final String orgSecondStateText;
    private String firstStateText;
    private String secondStateText;
    private final ActionListener toggleRequestActionListener;
    private boolean inFirstState = true;

    private final ActionListener buttonActionListener = (e) -> {
        processToggleEven(e);
    };

    public ViewLayoutToggleButton(String firstStateText, String secondStateText, ActionListener toggleRequestActionListener) {
        super(secondStateText);
        this.orgFirstStateText = firstStateText;
        this.orgSecondStateText = secondStateText;
        this.firstStateText = firstStateText;
        this.secondStateText = secondStateText;
        this.toggleRequestActionListener = toggleRequestActionListener;
        setFocusPainted(false);
        addActionListener(buttonActionListener);

        Dimension buttonSize = new Dimension(200, 23);
        setPreferredSize(buttonSize);
        setMinimumSize(buttonSize);
        setMaximumSize(buttonSize);
        setFocusPainted(false);
        setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
    }

    public void resetDefaultLayout(boolean suggestedLayoutIsHorizontal) {
        inFirstState = true;
        if (suggestedLayoutIsHorizontal) {
            this.firstStateText = orgFirstStateText;
            this.secondStateText = orgSecondStateText;
            setText(secondStateText);
        } else {
            this.firstStateText = orgSecondStateText;
            this.secondStateText = orgFirstStateText;
            setText(secondStateText);
        }
    }

    private void processToggleEven(ActionEvent e) {
        if (inFirstState) {
            setText(firstStateText);
            e.setSource(secondStateText);
        } else {
            setText(secondStateText);
            e.setSource(firstStateText);
        }
        toggleRequestActionListener.actionPerformed(e);
        inFirstState ^= true;
    }
}
