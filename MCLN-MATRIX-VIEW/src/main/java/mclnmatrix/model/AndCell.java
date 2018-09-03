package mclnmatrix.model;

/**
 * This is the model of the And matrix cell
 */
public class AndCell extends MatrixCell {

    public static AndCell createAndCell(int i, int j, String propertyName, String expectedState) {
        return new AndCell(null, i, j, propertyName, expectedState);
    }

    public static AndCell createAndCell(Object source, int i, int j, String propertyName, String expectedState) {
        return new AndCell(source, i, j, propertyName, expectedState);
    }

    private final Object source;
    private final int i;
    private final int j;
    private final String propertyName;
    private final String expectedState;

    AndCell(Object source, int i, int j, String propertyName, String expectedState) {
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
