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

    private final List<String> prevVectorData = new ArrayList();
    private final List<String> vectorData = new ArrayList();
    private List<String> changeIndicatorVector;

    private VectorDataModel() {
    }

    public final boolean updateData(List<String> vectorValues) {
        if (prevVectorData.isEmpty()) {
            int vectorValuesSize = vectorValues.size();
            for (int i = 0; i < vectorValuesSize; i++) {
                vectorData.add("-");
            }
        }
        prevVectorData.clear();
        prevVectorData.addAll(vectorData);
        vectorData.clear();
        vectorData.addAll(vectorValues);

        int vectorValuesSize = vectorValues.size();
        System.out.println();
        for (int i = 0; i < vectorValuesSize; i++) {
            System.out.print(prevVectorData.get(i));
        }
        System.out.println();
        for (int i = 0; i < vectorValuesSize; i++) {
            System.out.print(vectorData.get(i));
        }
        int changeCounter = 0;
        for (int i = 0; i < vectorValuesSize; i++) {
            changeCounter += vectorData.get(i).equalsIgnoreCase(prevVectorData.get(i)) ? 0 : 1;
        }
        return changeCounter == vectorValuesSize;
    }

    public void setChangeIndicatorVector(List<String> changeIndicatorVector) {
        this.changeIndicatorVector = changeIndicatorVector;
    }

    public String getValue(int i) {
        if (i >= vectorData.size()) {
            return "-";
        }
        String stateValue = vectorData.get(i);
        return stateValue;
    }

    public boolean isValueChanged(int i) {
        return changeIndicatorVector.get(i).equalsIgnoreCase("!");
    }

    public final List<String> getVectorValues() {
        List<String> vectorValues = new ArrayList(vectorData);
        return vectorValues;
    }
}
