package dsdsse.graphview;

import dsdsse.designspace.executor.MclnSimulationController;
import mcln.model.MclnStatement;
import mcln.model.MclnStatementState;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Admin on 8/7/2016.
 */
public class SimulatorPopupMenu extends MclnGraphViewPopupMenu {

    //
    //   I n s t a n c e
    //

    private MclnPropertyView mclnPropertyView;
    private MclnArcView mclnArcView;

    ActionListener propertyPopupMenuItemActionListener = (ActionEvent e) -> {
        System.out.println("Item selected " + e.getActionCommand());
        JMenuItem menuItem = (JMenuItem) e.getSource();
        ColoredSquare coloredSquare = (ColoredSquare) menuItem.getIcon();
        MclnStatementState mclnStatementState = coloredSquare.getMclnStatementState();

        MclnStatement mclnStatement = mclnPropertyView.getTheElementModel();
        String uid = mclnStatement.getUID();
        MclnState mclnState = mclnStatementState.getMclnState();
        MclnSimulationController.processRMBPopupMenuUserInput(uid, mclnState);
    };

    SimulatorPopupMenu(MclnPropertyView mclnPropertyView) {
        this.mclnPropertyView = mclnPropertyView;
        List<MclnStatementState> allowedStatesList = mclnPropertyView.getAllowedStatesList();
        initSimulatorPopupMenu(allowedStatesList, propertyPopupMenuItemActionListener);
    }

    private void initSimulatorPopupMenu(List<MclnStatementState> allowedStatesList,
                                        ActionListener stateSelectionPopupMenuItemActionListener) {
        for (MclnStatementState mclnStatementState : allowedStatesList) {
            int stateID = mclnStatementState.getStateID();
            Color itemColor = new Color(mclnStatementState.getRGB());
            ColoredSquare coloredSquare = ColoredSquare.createInstance(mclnStatementState, stateID, itemColor);
            String interpretation = mclnStatementState.getStateInterpretation();
            interpretation = interpretation.replace("$", " - ");
            JMenuItem menuItem = new JMenuItem(interpretation);
            menuItem.setHorizontalAlignment(JButton.LEADING);
            menuItem.setIcon(coloredSquare);
            menuItem.setForeground(Color.BLACK);
            menuItem.setUI(new BasicMenuItemUI() {
                {
                    selectionBackground = Color.ORANGE;
                    selectionForeground = Color.DARK_GRAY;
                }
            });
            menuItem.addActionListener(stateSelectionPopupMenuItemActionListener);
            add(menuItem);
        }
    }
}
