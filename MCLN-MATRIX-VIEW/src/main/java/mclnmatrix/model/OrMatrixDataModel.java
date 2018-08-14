package mclnmatrix.model;

import java.util.List;
import java.util.Map;

public class OrMatrixDataModel extends MatrixDataModel{

    private final boolean horizontalLayout;
    private final List<Map<Integer, OrCell>> orMatrixProperties;
    private final List<Map<Integer, OrCell>> orMatrixConditions;

    public OrMatrixDataModel(boolean horizontalLayout,
                             List<Map<Integer, OrCell>> orMatrixProperties, List<Map<Integer, OrCell>> orMatrixConditions) {
        this.horizontalLayout = horizontalLayout;
        this.orMatrixProperties = orMatrixProperties;
        this.orMatrixConditions = orMatrixConditions;
    }

    public String getMatrixElement(int i, int j) {
        if(horizontalLayout) {
//            if (i >= orMatrixProperties.size()) {
//                return "~";
//            }
            Map<Integer, OrCell> rowCells = orMatrixConditions.get(i);
            if (rowCells == null) {
                return "+";
            }
            OrCell orCell = rowCells.get(j);
            if (orCell != null) {
                return orCell.getExpectedState();
            } else {
                return "-";
            }
        } else{
            /**
             * Rows are Properties
             * Columns are conditions
             */
//            if (j >= orMatrixProperties.size()) {
//                return "~";
//            }
            Map<Integer, OrCell> rowCells = orMatrixConditions.get(j);
            if (rowCells == null) {
                return "+";
            }
            OrCell andCell = rowCells.get(i);
            if (andCell != null) {
                return andCell.getExpectedState();
            } else {
                return "-";
            }
//            return "-";
        }
//        if(orMatrixProperties.size() >= j){
//            return "~";
//        }
//        Map<Integer, OrCell> columnCells = orMatrixProperties.get(j);
//        if(columnCells ==  null){
//            return "+";
//        }
//        OrCell orCell = columnCells.get(i);
//        if (orCell != null) {
//            return orCell.getExpectedState();
//        } else
//            return "-";
    }
}
