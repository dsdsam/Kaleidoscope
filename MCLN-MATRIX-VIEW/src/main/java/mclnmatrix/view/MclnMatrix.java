package mclnmatrix.view;

import mclnmatrix.model.MatrixDataModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MclnMatrix extends JPanel {

    private static final Color MATRIX_BACKGROUND_COLOR = new Color(0xFFFFEE);

    private static final Border MATRIX_BORDER =
            new LineBorder(MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR, 1);

    private static final Border ROW_CELL_BORDER =
            new MatteBorder(0, 0, 1, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border ROW_LAST_CELL_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border LAST_ROW_CELL_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border LAST_ROW_LAST_CELL_BORDER =
            new MatteBorder(0, 0, 0, 0, MATRIX_BACKGROUND_COLOR);

    private final MatrixDataModel matrixDataModel;
    Dimension cellSize = new Dimension(MclnMatrixView.CELL_SIZE,MclnMatrixView.CELL_SIZE);

    public MclnMatrix(MatrixDataModel matrixDataModel, int width, int height, int rows, int columns) {
        this.matrixDataModel = matrixDataModel;
        setLayout(new GridLayout(rows, columns));
        setBorder(MATRIX_BORDER);
        setOpaque(true);
        setBackground(MATRIX_BACKGROUND_COLOR);
        Dimension dimSize = new Dimension(width, height);
        setPreferredSize(dimSize);
        setMinimumSize(dimSize);
        setMinimumSize(dimSize);
        initMatrix(rows, columns);
    }

    private void initMatrix(int nRows, int nColumns) {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nColumns; j++) {
                String cellValue = matrixDataModel.getMatrixElement(i, j);
                cellValue = !cellValue.equalsIgnoreCase("_") ? cellValue : " ";
                CellLabel cellLabel = new CellLabel(cellValue);
                cellLabel.setFont(MclnMatrixView.FONT);
                cellLabel.setPreferredSize(cellSize);
                cellLabel.setMinimumSize(cellSize);
                cellLabel.setMaximumSize(cellSize);
                if (i != (nRows - 1)) {
                    if (j != (nColumns - 1)) {
                        cellLabel.setBorder(ROW_CELL_BORDER);
                    } else {
                        cellLabel.setBorder(ROW_LAST_CELL_BORDER);
                    }
                } else {
                    // last row
                    if (j != (nColumns - 1)) {
                        cellLabel.setBorder(LAST_ROW_CELL_BORDER);
                    } else {
                        // last cell
                        cellLabel.setBorder(LAST_ROW_LAST_CELL_BORDER);
                    }
                }
                add(cellLabel);
            }
        }
    }

    private static class CellLabel extends JLabel {
        private final String cellValue;

        CellLabel(String cellValue) {
            super(cellValue, JLabel.CENTER);
            this.cellValue = cellValue;
        }
    }
}
