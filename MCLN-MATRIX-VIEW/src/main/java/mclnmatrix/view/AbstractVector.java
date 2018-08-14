package mclnmatrix.view;

import mclnmatrix.model.VectorDataModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractVector extends JPanel {

    static final Color CELL_BACKGROUND_COLOR = new Color(0xFFFFEE);

    private static final Border VECTOR_BORDER =
            new LineBorder(MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR, 1);

    private static final Border HORIZONTAL_VECTOR_CELL_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border VERTICAL_VECTOR_CELL_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);

    final VectorDataModel vectorDataModel;
    final int nElements;
    private final boolean horizontalVector;
    final java.util.List<JLabel> labelVector = new ArrayList();
    Dimension cellSize = new Dimension(MclnMatrixView.CELL_SIZE, MclnMatrixView.CELL_SIZE);

    /**
     * @param horizontal
     * @param vectorDataModel
     * @param nElements
     * @param width
     * @param height
     */
    AbstractVector(boolean horizontal, VectorDataModel vectorDataModel, int nElements, int width, int height) {
        horizontalVector = horizontal;
        this.vectorDataModel = vectorDataModel;
        this.nElements = nElements;
        if (horizontalVector) {
            setLayout(new GridLayout(1, 0));
        } else {
            setLayout(new GridLayout(0, 1));
        }

        setBorder(VECTOR_BORDER);
        setOpaque(true);
        setBackground(CELL_BACKGROUND_COLOR);
        Dimension dimSize = new Dimension(width, height);
        setPreferredSize(dimSize);
        setMinimumSize(dimSize);
        setMaximumSize(dimSize);
        initVector(nElements);
    }

    /**
     * @param nElements
     */
    private void initVector(int nElements) {
        for (int i = 0; i < nElements; i++) {
            String currentState = vectorDataModel.getValue(i);
            currentState = !currentState.equalsIgnoreCase("_") ? currentState : " ";
            CellLabel cellLabel = new CellLabel(currentState);
            cellLabel.setFont(MclnMatrixView.FONT);
            cellLabel.setPreferredSize(cellSize);
            cellLabel.setMinimumSize(cellSize);
            cellLabel.setMaximumSize(cellSize);
            labelVector.add(cellLabel);
            if (horizontalVector) {
                if (i != (nElements - 1)) {
                    cellLabel.setBorder(HORIZONTAL_VECTOR_CELL_BORDER);
                }
                add(cellLabel);
            } else {
                if (i != (nElements - 1)) {
                    cellLabel.setBorder(VERTICAL_VECTOR_CELL_BORDER);
                }
                add(cellLabel);
            }
        }
    }

      public void updateRecalculatedState(List<String> vectorValues){};
      public void updateRecalculatedState(List<String> vectorValues,  List<String> differenceVector){

      }

    private static class CellLabel extends JLabel {
        CellLabel(String value) {
            super(value, JLabel.CENTER);
            setBackground(CELL_BACKGROUND_COLOR);
            setOpaque(true);
        }
    }
}
