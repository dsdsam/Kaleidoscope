package mclnmatrix.model;

public abstract class MatrixDataModel {

    abstract public String getCellValue(int i, int j);

    abstract public MatrixCell getMatrixElement(int i, int j);
}
