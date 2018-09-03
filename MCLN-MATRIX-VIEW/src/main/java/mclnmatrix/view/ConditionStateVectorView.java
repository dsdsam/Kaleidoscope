package mclnmatrix.view;

import mclnmatrix.model.VectorDataModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConditionStateVectorView extends AbstractVector {

    private static final Color HIGHLIGHTED_BACKGROUND_COLOR = new Color(0x77FFFF);

    public ConditionStateVectorView(boolean horizontal, VectorDataModel vectorDataModel, int nElements, int width, int height) {
        super(horizontal, vectorDataModel, nElements, width, height);
    }

    @Override
    public void updateRecalculatedState(List<String> vectorValues) {
        for (int i = 0; i < nElements; i++) {
            JLabel label = vectorCells.get(i);
            String cellValue = vectorValues.get(i);
            label.setText(cellValue);
            boolean recognized = cellValue.equalsIgnoreCase("!");
            if (recognized) {
                label.setBackground(HIGHLIGHTED_BACKGROUND_COLOR);
            } else {
                label.setBackground(CELL_BACKGROUND_COLOR);
            }
        }
        repaint();
    }
}