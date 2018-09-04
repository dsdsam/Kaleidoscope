package mclnmatrix.model;

import mclnmatrix.view.BasicCellLabel;

public class VectorCell {

    public static VectorCell createCell(int i, String propertyName, String initialState) {
        return new VectorCell(null, i, propertyName, initialState);
    }

    public static VectorCell createCell(Object source, int i, String propertyName, String initialState) {
        return new VectorCell(source, i, propertyName, initialState);
    }

    private final Object source;
    private final int index;
    private final String propertyName;
    private final String initialState;
    private String state;
    private boolean stateChanged;

    private BasicCellLabel basicCellLabel;

    private VectorCell(Object source, int index, String propertyName, String initialState) {
        this.source = source;
        this.index = index;
        this.propertyName = propertyName;
        this.initialState = initialState;
        this.state = initialState;
    }

    final void setInitialState() {
        this.state = initialState;
        stateChanged = false;
        if (basicCellLabel == null) {
            return;
        }
        basicCellLabel.setColor(initialState);
    }

    public void setCellLabel(BasicCellLabel basicCellLabel) {
        this.basicCellLabel = basicCellLabel;
        basicCellLabel.setColor(initialState);
    }

    public Object getSource() {
        return source;
    }

    public void setState(String state) {
        stateChanged = !this.state.equalsIgnoreCase(state);
        this.state = state;
        if (basicCellLabel == null) {
            return;
        }
        basicCellLabel.setColor(state);
        basicCellLabel.repaint();
    }

    public boolean isStateChanged() {
        return stateChanged;
    }

    public String getState() {
        return state;
    }
}
