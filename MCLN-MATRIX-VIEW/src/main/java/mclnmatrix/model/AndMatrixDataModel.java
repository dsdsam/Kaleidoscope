package mclnmatrix.model;

import java.util.List;
import java.util.Map;

public class AndMatrixDataModel extends MatrixDataModel {

    private final boolean horizontalLayout;
    private final List<AndCell> andMatrixProperties;
    private final List<Map<Integer, AndCell>> andMatrixConditions;

    public AndMatrixDataModel(boolean horizontalLayout,
                              List<AndCell> andMatrixProperties, List<Map<Integer, AndCell>> andMatrixConditions) {
        this.horizontalLayout = horizontalLayout;
        this.andMatrixProperties = andMatrixProperties;
        this.andMatrixConditions = andMatrixConditions;
    }

    public String getMatrixElement(int i, int j) {
        if(horizontalLayout) {
            Map<Integer, AndCell> rowCells = andMatrixConditions.get(i);
            AndCell andCell = rowCells.get(j);
            if (andCell != null) {
                return andCell.getExpectedState();
            } else {
                return "-";
            }
        }else{
            Map<Integer, AndCell> rowCells = andMatrixConditions.get(j);
            AndCell andCell = rowCells.get(i);
            if (andCell != null) {
                return andCell.getExpectedState();
            } else {
                return "-";
            }
        }
    }

    /**
     * Rows are Properties
     * Columns are conditions
     *
     * @param i
     * @param j
     * @return
     */
//    public String getMatrixElementFor(int i, int j) {
//
//    }
}
