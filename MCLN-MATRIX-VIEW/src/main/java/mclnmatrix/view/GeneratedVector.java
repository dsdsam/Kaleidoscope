package mclnmatrix.view;

import mclnmatrix.app.AoSUtils;
import mclnmatrix.model.VectorDataModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GeneratedVector extends AbstractVector {

    private static final Color HIGHLIGHTED_BACKGROUND_COLOR = Color.ORANGE;

    public GeneratedVector(boolean horizontal, VectorDataModel vectorDataModel, int nElements, int width, int height) {
        super(horizontal, vectorDataModel, nElements, width, height);
    }

    @Override
    public void updateRecalculatedState(List<String> vectorValues) {
        for (int i = 0; i < nElements; i++) {
            JLabel label = labelVector.get(i);
            String cellValue = vectorValues.get(i);
            cellValue = !cellValue.equalsIgnoreCase("_") ? cellValue : " ";
            label.setText(cellValue);

            if (  AoSUtils.isStateSymbol(cellValue)) {
                label.setBackground(HIGHLIGHTED_BACKGROUND_COLOR);
            } else {
                label.setBackground(CELL_BACKGROUND_COLOR);
            }
        }
        repaint();
    }

    /*
       A 0 0 #
       M N G H
       A N G H

       0 B 0 #
       A N G H
       A B G H

       M N G H
       A B G H
       A B - -
     */
}
