package dsdsse.history;

import adf.utils.RingStack;
import mcln.palette.MclnPaletteFactory;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/11/14
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimulationTracePanel extends JPanel {

    static int stateDotSize;

    private static final Color LINE_SEPARATOR_COLOR = new Color(0xD0D0D0);

    private int LINE_HEIGHT = ExecutionHistoryPanel.ROW_HEIGHT;
    private int HALF_LINE_HEIGHT = (LINE_HEIGHT / 2);


    private int lineNum;
    private final List<MclnPropertyView> presentedPropertyList = new ArrayList();

    /**
     *
     */
    public SimulationTracePanel() {
        setBorder(null);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    void setContents(List<MclnPropertyView> presentedPropertyList) {
        this.presentedPropertyList.clear();
        this.presentedPropertyList.addAll(presentedPropertyList);
        lineNum = presentedPropertyList.size();
        repaint();
    }


    public void simulationStepExecuted() {
        repaint();
    }

    public void traceLogCleared() {
        repaint();
    }

    //
    //     D i s p l a y
    //

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLines(g);
    }

    /**
     * @param g
     */
    private void drawLines(Graphics g) {

        Dimension panelSize = getSize();
        int panelWidth = panelSize.width;
        g.setColor(Color.ORANGE);

        int y = LINE_HEIGHT - 1;
        int stateY = HALF_LINE_HEIGHT;
//        int stateY2 = 2;
        int stateY2 = ((LINE_HEIGHT - 1) - (stateDotSize + 1)) / 2;
        for (int i = 0; i < lineNum; i++, y += LINE_HEIGHT, stateY += LINE_HEIGHT) {
            MclnPropertyView mcLnPropertyView = presentedPropertyList.get(i);
            RingStack ringStack = mcLnPropertyView.getHistory();
            String mclnPaletteName = mcLnPropertyView.getMclnStatesPaletteName();
            MclnStatesNewPalette mclnStatesNewPalette = MclnPaletteFactory.getPaletteByName(mclnPaletteName);
            int size = ringStack.getSize();
            int stateX = HALF_LINE_HEIGHT - 3;
            int stateX2 = 3;
            for (int j = 0; j < 95; j++, stateX += (HALF_LINE_HEIGHT + 2)) {
                MclnState mclnState = (MclnState) ringStack.getElementAt(j);
                if (mclnState == null) {
                    break;
                }
                int col = mclnState.getStateID();
                Color fillColor = new Color(col);//Color.RED;
                g.setColor(fillColor);
                g.fillOval(stateX2, stateY2, stateDotSize, stateDotSize);

                Graphics2D g2D = (Graphics2D) g;
                Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.GRAY);
                g.drawOval(stateX2, stateY2, stateDotSize, stateDotSize);
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
                stateX2 += LINE_HEIGHT;
            }
            stateY2 += LINE_HEIGHT;
            g.setColor(LINE_SEPARATOR_COLOR);
            g.drawLine(0, y, panelWidth, y);
        }
    }
}
