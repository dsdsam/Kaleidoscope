package mclnmatrix.view;

import mclnmatrix.model.MatrixCell;
import mclnmatrix.model.MatrixDataModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MclnMatrix extends JPanel {

    private static final Color MATRIX_BACKGROUND_COLOR = new Color(0xFFFFE7);

    private static final Border MATRIX_BORDER =
            new MatteBorder(1, 1, 0, 0, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);

    private static final Border ANY_BUT_LAST_CELL_BORDER =
            new MatteBorder(0, 0, 1, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);

    private static final Border ROW_LAST_CELL_DIVIDER_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border ROW_LAST_CELL_MATRIX_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);
    private Border ROW_LAST_CELL_BORDER =
            BorderFactory.createCompoundBorder(ROW_LAST_CELL_MATRIX_BORDER, ROW_LAST_CELL_DIVIDER_BORDER);

    private static final Border COLUMN_LAST_CELL_DIVIDER_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border COLUMN_LAST_CELL_MATRIX_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);
    private Border COLUMN_LAST_CELL_BORDER =
            BorderFactory.createCompoundBorder(COLUMN_LAST_CELL_DIVIDER_BORDER, COLUMN_LAST_CELL_MATRIX_BORDER);

    private static final Border MATRIX_LOWER_RIGHT_CELL_BORDER =
            new MatteBorder(0, 0, 1, 1, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);

    //
    //   I n s t a n c e
    //

    private final MatrixDataModel matrixDataModel;
    private final List<BasicCellLabel> matrixCells = new ArrayList();
    private final Dimension cellSize = new Dimension(MclnMatrixView.CELL_SIZE, MclnMatrixView.CELL_SIZE);

    MclnMatrix(MatrixDataModel matrixDataModel, int width, int height, int rows, int columns) {
        this.matrixDataModel = matrixDataModel;
        setLayout(new GridBagLayout());
        setBorder(MATRIX_BORDER);
        setOpaque(true);
        setBackground(MATRIX_BACKGROUND_COLOR);

        Dimension dimSize;
        dimSize = new Dimension(MclnMatrixView.CELL_SIZE * columns + 1,
                MclnMatrixView.CELL_SIZE * rows + 1);
        setPreferredSize(dimSize);
        setMinimumSize(dimSize);
        setMinimumSize(dimSize);
        initMatrix(rows, columns);
    }

    private void initMatrix(int nRows, int nColumns) {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nColumns; j++) {
                MatrixCell andCell = matrixDataModel.getMatrixElement(i, j);

                CellLabel cellLabel = new CellLabel(andCell);
                cellLabel.setFont(MclnMatrixView.FONT);
                cellLabel.setPreferredSize(cellSize);
                cellLabel.setMinimumSize(cellSize);
                cellLabel.setMaximumSize(cellSize);
                matrixCells.add(cellLabel);

                if (!(i == (nRows - 1) && j == (nColumns - 1))) {
                    if (i != (nRows - 1) && j != (nColumns - 1)) {
                        cellLabel.setBorder(ANY_BUT_LAST_CELL_BORDER);
                    } else if (j == (nColumns - 1)) {
                        cellLabel.setBorder(ROW_LAST_CELL_BORDER);
                    } else {
                        cellLabel.setBorder(COLUMN_LAST_CELL_BORDER);
                    }
                } else {
                    // last cell
                    cellLabel.setBorder(MATRIX_LOWER_RIGHT_CELL_BORDER);
                }
                add(cellLabel,
                        new GridBagConstraints(j, i, 1, 1, 0, 0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));
            }
        }
    }

    /**
     * @param me
     * @return
     */
    public BasicCellLabel isMouseHoverMatrixCell(MouseEvent me) {
        Point mousePointInLocalCoordinates = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), getParent());
        Rectangle rect = getBounds();
        if (!rect.contains(mousePointInLocalCoordinates)) {
            return null;
        }

        mousePointInLocalCoordinates = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), this);
        int matrixCellsSize = matrixCells.size();
        for (int i = 0; i < matrixCellsSize; i++) {
            BasicCellLabel basicCellLabel = matrixCells.get(i);
            boolean yes = basicCellLabel.isMouseHover(mousePointInLocalCoordinates);
            if (yes) {
                return basicCellLabel;
            }
        }
        return null;
    }

    /**
     *
     */
    private static class CellLabel extends BasicCellLabel {

        CellLabel(MatrixCell matrixCell) {
            super((matrixCell == null ? null : matrixCell.getSource()),
                    (matrixCell == null ? "-" : (matrixCell.getExpectedState().equalsIgnoreCase("_") ?
                            " " : matrixCell.getExpectedState())));
        }
    }
}
