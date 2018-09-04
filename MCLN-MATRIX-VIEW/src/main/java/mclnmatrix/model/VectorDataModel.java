package mclnmatrix.model;

import java.util.ArrayList;
import java.util.List;

public class VectorDataModel {

    public static VectorDataModel createInstance() {
        return new VectorDataModel();
    }

    //
    //   I n s t a n c e
    //

    private final List<VectorCell> vectorStateData = new ArrayList();
    private final List<String> prevVectorData = new ArrayList();
    private List<String> changeIndicatorVector;

    private VectorDataModel() {
    }

    public int vectorSize() {
        return vectorStateData.size();
    }

    public void setVectorStateData(List<VectorCell> vectorStateData) {
        assert vectorStateData != null && !vectorStateData.isEmpty() :
                "Provided argument vectorStateData is either null ir empty list";
        this.vectorStateData.clear();
        for (VectorCell vectorCell : vectorStateData) {
            this.vectorStateData.add(vectorCell);
        }
    }

    public final void setToInitialState() {
        prevVectorData.clear();
        for (int i = 0; i < vectorStateData.size(); i++) {
            VectorCell vectorCell = vectorStateData.get(i);
            vectorCell.setInitialState();
        }
    }

    public final boolean updateData(List<String> vectorValues) {
        prevVectorData.clear();
        for (int i = 0; i < vectorValues.size(); i++) {
            String value = vectorValues.get(i);
            value = value.equalsIgnoreCase("0") ? "-" : value;
            VectorCell vectorCell = vectorStateData.get(i);
            prevVectorData.add(vectorCell.getState());
            vectorCell.setState(value);
        }
        int vectorValuesSize = vectorValues.size();
        int changeCounter = 0;
        for (int i = 0; i < vectorValuesSize; i++) {
            changeCounter += vectorValues.get(i).equalsIgnoreCase(prevVectorData.get(i)) ? 0 : 1;
        }
        return changeCounter == vectorValuesSize;
    }

    public void setChangeIndicatorVector(List<String> changeIndicatorVector) {
        this.changeIndicatorVector = changeIndicatorVector;
    }

    public VectorCell getVectorCell(int i) {
        return vectorStateData.get(i);
    }

    public String getValue(int i) {
        if (i >= vectorStateData.size()) {
            return "-";
        }
        VectorCell vectorCell = vectorStateData.get(i);
        String stateValue = vectorCell.getState();
        return stateValue;
    }

    public boolean isValueChanged(int i) {
        return changeIndicatorVector.get(i).equalsIgnoreCase("!");
    }

    public final List<String> getVectorValues() {
        List<String> vectorValues = new ArrayList();
        for (int i = 0; i < vectorStateData.size(); i++) {
            VectorCell vectorCell = vectorStateData.get(i);
            vectorValues.add(vectorCell.getState());
        }
        return vectorValues;
    }
}
