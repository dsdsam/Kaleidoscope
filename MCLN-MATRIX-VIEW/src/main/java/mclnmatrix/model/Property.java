package mclnmatrix.model;

public class Property {

    public static Property createProperty(int ID, String propertyName) {
        return new Property(ID, propertyName);
    }

    private final int columnIndex;
    private final String propertyName;

   private Property(int columnIndex, String propertyName) {
        this.columnIndex = columnIndex;
        this.propertyName = propertyName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
