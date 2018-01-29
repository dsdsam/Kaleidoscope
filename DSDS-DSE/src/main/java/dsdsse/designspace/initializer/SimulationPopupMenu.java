package dsdsse.designspace.initializer;

import mcln.model.MclnStatementState;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Admin on 7/25/2016.
 */
public class SimulationPopupMenu extends JPopupMenu {

    public static SimulationPopupMenu getPopupMenu(GeneratorTestingPanel generatorTestingPanel,
                                                   InitAssistantDataModel initAssistantDataModel) {
        return new SimulationPopupMenu(generatorTestingPanel, initAssistantDataModel);
    }

    private final GeneratorTestingPanel generatorTestingPanel;

    private SimulationPopupMenu(GeneratorTestingPanel generatorTestingPanel, InitAssistantDataModel initAssistantDataModel) {
        this.generatorTestingPanel = generatorTestingPanel;
        setLightWeightPopupEnabled(false);
//        setBorder(BorderFactory.createRaisedSoftBevelBorder());

        ActionListener stateSelectionPopupMenuItemActionListener = (e) -> {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            ColoredSquare coloredSquare = (ColoredSquare) menuItem.getIcon();
            int stateID = coloredSquare.getStateID();
            Color itemColor = coloredSquare.getItemColor();
            this.generatorTestingPanel.updateMclnStatementStateFromPopup(  stateID);;
        };

        initPopupMenu(initAssistantDataModel, stateSelectionPopupMenuItemActionListener);
    }

    private void initPopupMenu(InitAssistantDataModel initAssistantDataModel,
                               ActionListener stateSelectionPopupMenuItemActionListener) {
        List<MclnStatementState> allowedStatesList = initAssistantDataModel.getCurrentAllowedStatesList();
        for (MclnStatementState mclnStatementState : allowedStatesList) {
            int stateID = mclnStatementState.getStateID();
            Color itemColor = new Color(mclnStatementState.getRGB());
            ColoredSquare coloredSquare = ColoredSquare.createInstance(stateID, itemColor);
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

    private static class ColoredSquare implements Icon {
        static ColoredSquare createInstance(int stateID, Color itemColor) {
            return new ColoredSquare(stateID, itemColor);
        }

        private final int stateID;
        private final Color itemColor;

        public ColoredSquare(int stateID, Color itemColor) {
            this.stateID = stateID;
            this.itemColor = itemColor;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            g.setColor(itemColor);
            g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
            g.setColor(oldColor);
        }

        public int getStateID() {
            return stateID;
        }

        public Color getItemColor() {
            return itemColor;
        }

        public int getIconWidth() {
            return 12;
        }

        public int getIconHeight() {
            return 12;
        }
    }
}
