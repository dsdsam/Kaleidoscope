package mclnmatrix.model;

/*
    This is the model of Or matrix cell
 */
public class OrCell extends MatrixCell {

    public static OrCell createOrCell(int i, int j, String propertyName, String expectedState) {
        return new OrCell(null, i, j, propertyName, expectedState);
    }

    public static OrCell createOrCell(Object source, int i, int j, String propertyName, String expectedState) {
        return new OrCell(source, i, j, propertyName, expectedState);
    }

    private final Object source;
    private final int i;
    private final int j;
    private final String propertyName;
    private final String expectedState;

    OrCell(Object source, int i, int j, String propertyName, String expectedState) {
        this.source = source;
        this.i = i;
        this.j = j;
        this.propertyName = propertyName;
        this.expectedState = expectedState;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String getExpectedState() {
        return expectedState;
    }
}
