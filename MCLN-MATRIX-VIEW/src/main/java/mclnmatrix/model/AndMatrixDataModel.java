package mclnmatrix.model;

import java.util.List;
import java.util.Map;

public class AndMatrixDataModel extends MatrixDataModel {

    private final boolean horizontalLayout;
    private final List<Map<Integer, AndCell>> andMatrixConditions;

    public AndMatrixDataModel(boolean horizontalLayout, List<Map<Integer, AndCell>> andMatrixConditions) {
        this.horizontalLayout = horizontalLayout;
        this.andMatrixConditions = andMatrixConditions;
    }

    @Override
    public String getCellValue(int i, int j) {
        MatrixCell matrixCell = getMatrixElement(i, j);
        return matrixCell != null ? matrixCell.getExpectedState() : "-";
    }

    @Override
    public MatrixCell getMatrixElement(int i, int j) {
        AndCell andCell;
        if (horizontalLayout) {
            Map<Integer, AndCell> rowCells = andMatrixConditions.get(i);
            andCell = rowCells.get(j);
        } else {
            Map<Integer, AndCell> rowCells = andMatrixConditions.get(j);
            andCell = rowCells.get(i);
        }
        return andCell;
    }
}
