package mclnmatrix.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MclnMatrixModel {

    protected final Map<String, Property> propertyNameToPropertyMap = new HashMap();

    protected final List<Map<Integer, AndCell>> andMatrixConditions = new ArrayList();
    protected final List<Map<Integer, OrCell>> orMatrixConditions = new ArrayList();

    protected final List<Property> properties = new ArrayList();

    protected VectorDataModel inputStateVectorDataModel = VectorDataModel.createInstance();
    protected VectorDataModel suggestedStateVectorDataModel = VectorDataModel.createInstance();
    protected VectorDataModel conditionStateVectorDataModel = VectorDataModel.createInstance();

    protected Map<Integer, AndCell> conjunction;
    protected AndMatrixDataModel andMatrixDataModelForHorizontalLayout;
    protected AndMatrixDataModel andMatrixDataModelForVerticalLayout;

    protected String effectedPropertyName;
    protected Map<Integer, OrCell> disjunction;
    protected OrMatrixDataModel orMatrixDataModelForHorizontalLayout;
    protected OrMatrixDataModel orMatrixDataModelForVertivalLayout;

    public MclnMatrixModel() {
    }

    public boolean isModelEmpty() {
        return propertyNameToPropertyMap.isEmpty() && andMatrixConditions.isEmpty();
    }

    //
    //   G e t t i n g   m o d e l   d a t a
    //

    public int getPropertySize() {
        return inputStateVectorDataModel.vectorSize();
    }

    public int getConditionSize() {
        return andMatrixConditions.size();
    }

    protected void setInputStateVectorData(List<VectorCell> inputStateVectorData) {
        this.inputStateVectorDataModel.setVectorStateData(inputStateVectorData);
    }

    public VectorDataModel getInputStateVectorDataModel() {
        return inputStateVectorDataModel;
    }

    protected void setSuggestedStateVectorData(List<VectorCell> suggestedStateVectorData) {
        this.suggestedStateVectorDataModel.setVectorStateData(suggestedStateVectorData);
    }

    public VectorDataModel getSuggestedStateVectorDataModel() {
        return suggestedStateVectorDataModel;
    }

    protected void setConditionStateVectorData(List<VectorCell> conditionStateVectorData) {
        this.conditionStateVectorDataModel.setVectorStateData(conditionStateVectorData);
    }

    public VectorDataModel getConditionStateVectorDataModel() {
        return conditionStateVectorDataModel;
    }

    //
    //   Matrix getters and setters
    //

    public void setAndMatrixConditions(List<Map<Integer, AndCell>> andMatrixConditions) {
        this.andMatrixConditions.clear();
        this.andMatrixConditions.addAll(andMatrixConditions);
    }

    public void setOrMatrixConditions(List<Map<Integer, OrCell>> orMatrixConditions) {
        this.orMatrixConditions.clear();
        this.orMatrixConditions.addAll(orMatrixConditions);
    }

    public void setAndMatrixDataModelForHorizontalLayout(AndMatrixDataModel andMatrixDataModelForHorizontalLayout) {
        this.andMatrixDataModelForHorizontalLayout = andMatrixDataModelForHorizontalLayout;
    }

    public void setOrMatrixDataModelForHorizontalLayout(OrMatrixDataModel orMatrixDataModelForHorizontalLayout) {
        this.orMatrixDataModelForHorizontalLayout = orMatrixDataModelForHorizontalLayout;
    }

    public void setAndMatrixDataModelForVerticalLayout(AndMatrixDataModel andMatrixDataModelForVerticalLayout) {
        this.andMatrixDataModelForVerticalLayout = andMatrixDataModelForVerticalLayout;
    }

    public void setOrMatrixDataModelForVertivalLayout(OrMatrixDataModel orMatrixDataModelForVertivalLayout) {
        this.orMatrixDataModelForVertivalLayout = orMatrixDataModelForVertivalLayout;
    }

    public AndMatrixDataModel getAndMatrixDataModelForHorizontalLayout() {
        return andMatrixDataModelForHorizontalLayout;
    }

    public OrMatrixDataModel getOrMatrixDataModelForHorizontalLayout() {
        return orMatrixDataModelForHorizontalLayout;
    }

    public AndMatrixDataModel getAndMatrixDataModelForVerticalLayout() {
        return andMatrixDataModelForVerticalLayout;
    }

    public OrMatrixDataModel getOrMatrixDataModelForVerticalLayout() {
        return orMatrixDataModelForVertivalLayout;
    }

}
