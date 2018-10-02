package mclnmatrix.view;

import mclnmatrix.model.VectorCell;
import mclnmatrix.model.VectorDataModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractVector extends JPanel {

    static final Color CELL_FOREGROUND_COLOR = Color.DARK_GRAY;
    static final Color CELL_BACKGROUND_COLOR = new Color(0xFFFFE8);
    private static final Dimension CELL_SIZE = new Dimension(MclnMatrixView.CELL_SIZE, MclnMatrixView.CELL_SIZE);

    private static final Border VECTOR_BORDER =
            new MatteBorder(1, 1, 0, 0, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);

    private static final Border HORIZONTAL_VECTOR_CELL_GRIP_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border HORIZONTAL_VECTOR_CELL_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);
    private Border HORIZONTAL_VECTOR_ANY_CELL_BORDER =
            BorderFactory.createCompoundBorder(HORIZONTAL_VECTOR_CELL_BORDER, HORIZONTAL_VECTOR_CELL_GRIP_BORDER);

    private static final Border VERTICAL_VECTOR_CELL_GRIP_BORDER =
            new MatteBorder(0, 0, 1, 0, MclnMatrixView.MATRIX_AND_VECTOR_GRID_COLOR);
    private static final Border VERTICAL_VECTOR_CELL_BORDER =
            new MatteBorder(0, 0, 0, 1, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);
    private Border VERTICAL_VECTOR_ANY_CELL_BORDER =
            BorderFactory.createCompoundBorder(VERTICAL_VECTOR_CELL_BORDER, VERTICAL_VECTOR_CELL_GRIP_BORDER);

    private static final Border VECTOR_LOWER_RIGHT_CELL_BORDER =
            new MatteBorder(0, 0, 1, 1, MclnMatrixView.MATRIX_AND_VECTOR_BORDER_COLOR);

    //   I n s t a n c e

    protected final int nElements;
    protected final VectorDataModel vectorDataModel;
    protected final List<BasicCellLabel> vectorCells = new ArrayList();

    private final boolean horizontalVector;

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

        setOpaque(true);
        setBackground(CELL_BACKGROUND_COLOR);

        setLayout(new GridBagLayout());
        setBorder(VECTOR_BORDER);

        Dimension dimSize;
        if (horizontalVector) {
            dimSize = new Dimension(MclnMatrixView.CELL_SIZE * nElements + 1,
                    MclnMatrixView.CELL_SIZE + 1);
        } else {
            dimSize = new Dimension(MclnMatrixView.CELL_SIZE + 1,
                    MclnMatrixView.CELL_SIZE * nElements + 1);
        }
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
            VectorCell vectorCell = vectorDataModel.getVectorCell(i);
            CellLabel cellLabel = new CellLabel(vectorCell);
            cellLabel.setFont(MclnMatrixView.FONT);

            cellLabel.setPreferredSize(CELL_SIZE);
            cellLabel.setMinimumSize(CELL_SIZE);
            cellLabel.setMaximumSize(CELL_SIZE);
            vectorCells.add(cellLabel);

            if (horizontalVector) {
                if (i != (nElements - 1)) {
                    cellLabel.setBorder(HORIZONTAL_VECTOR_ANY_CELL_BORDER);
                } else {
                    cellLabel.setBorder(VECTOR_LOWER_RIGHT_CELL_BORDER);
                }
                add(cellLabel,
                        new GridBagConstraints(i, 0, 1, 1, 0, 0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));

            } else {
                if (i != (nElements - 1)) {
                    cellLabel.setBorder(VERTICAL_VECTOR_ANY_CELL_BORDER);
                } else {
                    cellLabel.setBorder(VECTOR_LOWER_RIGHT_CELL_BORDER);
                }
                add(cellLabel,
                        new GridBagConstraints(0, i, 1, 1, 0, 0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));
            }
        }
    }

    public void updateRecalculatedState(List<String> vectorValues) {
    }

    public void updateRecalculatedState(List<String> vectorValues, List<String> differenceVector) {
    }

    public BasicCellLabel isMouseHoverVectorCell(MouseEvent me) {
        Component c = me.getComponent();
        Point mousePointInLocalCoordinates = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), getParent());
        Rectangle rect = getBounds();
        if (!rect.contains(mousePointInLocalCoordinates)) {
            return null;
        }

        mousePointInLocalCoordinates = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), this);
        int vectorSize = vectorCells.size();
        for (int i = 0; i < vectorSize; i++) {
            BasicCellLabel basicCellLabel = vectorCells.get(i);
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

        private final VectorCell vectorCell;

        CellLabel(VectorCell vectorCell) {
            super(vectorCell.getSource(), vectorCell.getState());
            this.vectorCell = vectorCell;
            vectorCell.setCellLabel(this);
        }

        @Override
        boolean isStateChanged() {
            return vectorCell != null ? vectorCell.isStateChanged() : false;
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
}
