package mclnmatrix.model;

import java.util.List;
import java.util.Map;

public class OrMatrixDataModel extends MatrixDataModel {

    private final boolean horizontalLayout;
    private final List<Map<Integer, OrCell>> orMatrixConditions;

    public OrMatrixDataModel(boolean horizontalLayout, List<Map<Integer, OrCell>> orMatrixConditions) {
        this.horizontalLayout = horizontalLayout;
        this.orMatrixConditions = orMatrixConditions;
    }

    @Override
    public String getCellValue(int i, int j) {
        MatrixCell matrixCell = getMatrixElement(i, j);
        return matrixCell != null ? matrixCell.getExpectedState() : "-";
    }

    @Override
    public MatrixCell getMatrixElement(int i, int j) {
        OrCell orCell;
        if (horizontalLayout) {
            Map<Integer, OrCell> rowCells = orMatrixConditions.get(i);
            orCell = rowCells.get(j);
        } else {
            Map<Integer, OrCell> rowCells = orMatrixConditions.get(j);
            orCell = rowCells.get(i);
        }
        return orCell;
    }
}
