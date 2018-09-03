package mclnmatrix.view;

import javax.swing.*;
import java.awt.*;

import static mclnmatrix.view.AbstractVector.CELL_BACKGROUND_COLOR;

public class BasicCellLabel extends JLabel {

    private Object source;
    private String cellValue;
    private Color stateColor;

    BasicCellLabel(Object source, String cellValue) {
        super(cellValue.equalsIgnoreCase("_") ? "  " : cellValue, JLabel.CENTER);
        this.source = source;
        setColor(cellValue);
        setBackground(CELL_BACKGROUND_COLOR);
        setOpaque(true);
    }

    /*
        Is overridden in Abstract Vector class
     */
    public boolean isEmptyState() {
        return cellValue.equalsIgnoreCase("-");
    }

    /*
       This method is called:
       1) To set initial state when label is created.
       2) From VectorCell to update this label state when model is changed
     */
    public void setColor(String value) {
        cellValue = value.equalsIgnoreCase("_") ? "  " : value;
        boolean thisIsColor = cellValue.indexOf("0x") > -1;
        if (thisIsColor) {
            super.setText("");
            int intColor = Integer.decode(cellValue);
            stateColor = new Color(intColor);
        } else {
            stateColor = null;
            super.setText(cellValue);
        }
    }

    boolean isStateChanged() {
        return false;
    }

    public Object getSource() {
        return source;
    }

    public boolean isMouseHover(Point mousePointInLocalCoordinates) {
        Rectangle rect = getBounds();
        return rect.contains(mousePointInLocalCoordinates);
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stateColor != null) {
            drawState(g);
        }
    }

    private void drawState(Graphics g) {
        Dimension cellSize = getSize();
        int cellWidth = cellSize.width;
        int cellHeight = cellSize.height;

        int stateDotSize = 8;
        int squareSize = 11;
        int stateX1 = ((cellWidth - squareSize) / 2);
        int stateY1 = (((cellHeight - 1) - (squareSize)) / 2);
        int stateX2 = ((cellWidth - stateDotSize) / 2) - 1;
        int stateY2 = (((cellHeight - 1) - (stateDotSize + 1)) / 2);

        g.setColor(stateColor);
        if (isStateChanged()) {
            g.fill3DRect(stateX1, stateY1, squareSize, squareSize, true);
        } else {
            g.fillOval(stateX2, stateY2, stateDotSize, stateDotSize);
            Graphics2D g2D = (Graphics2D) g;
            Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.GRAY);
            g.drawOval(stateX2, stateY2, stateDotSize, stateDotSize);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
        }
    }

    @Override
    public void setText(String text) {
        if (this.getIcon() == null) {
            super.setText(text);
        } else {
            super.setText("");
        }
    }
}
