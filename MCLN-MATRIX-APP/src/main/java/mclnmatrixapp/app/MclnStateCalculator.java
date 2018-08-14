package mclnmatrixapp.app;

import adf.app.AdfEnv;
import mclnmatrix.app.AoSUtils;
import mclnmatrix.model.*;
import mclnmatrix.view.MatrixDataModelListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MclnStateCalculator {

    private static int DELAY = 1000;

    private MatrixDataModelListener matrixDataModelListener;

    private MclnMatrixModel mclnMatrixModel;
    private int propertiesSize;
    private int conditionsSize;
    private mclnmatrix.model.InitialStateDataModel InitialStateDataModel;
    private VectorDataModel inputStateVectorDataModel;
    private VectorDataModel conditionVectorDataModel;
    private VectorDataModel suggestedStateVectorDataModel;
    private AndMatrixDataModel andMatrixDataModel;
    private OrMatrixDataModel orMatrixDataModel;

    private final Timer timer;
    private ActionListener timerTickListener = (e) -> {
        System.out.println("tick");
        calculateStateToStateTransition();
        AdfEnv.getMainFrame().repaint();
    };

    public MclnStateCalculator(MclnMatrixModel mclnMatrixModel) {
        setCurrentModel(mclnMatrixModel);
        timer = new Timer(DELAY, timerTickListener);
    }

    final void setMatrixDataModelListener(MatrixDataModelListener matrixDataModelListener){
        this.matrixDataModelListener = matrixDataModelListener;
    }

    public void setCurrentModel(MclnMatrixModel mclnMatrixModel) {
        this.mclnMatrixModel = mclnMatrixModel;
        propertiesSize = mclnMatrixModel.getPropertySize();
        conditionsSize = mclnMatrixModel.getConditionSize();

        InitialStateDataModel = mclnMatrixModel.getInitialStateDataModel();
        inputStateVectorDataModel = mclnMatrixModel.getInputStateVectorDataModel();
        conditionVectorDataModel = mclnMatrixModel.getConditionVectorDataModel();
        suggestedStateVectorDataModel = mclnMatrixModel.getSuggestedStateVectorDataModel();

        andMatrixDataModel = mclnMatrixModel.getAndMatrixDataModelForHorizontalLayout();
        orMatrixDataModel = mclnMatrixModel.getOrMatrixDataModelForHorizontalLayout();
    }

    /**
     *
     */
   public void startSimulation() {
        timer.start();
    }

    /**
     *
     */
    public void stopSimulation() {
        timer.stop();
    }

    /**
     *
     */
    public void calculateStateToStateTransition() {
        System.out.println("Executing One Simulation step");
        List<String> conditions = multiplyAMDMatrixByPropertyStateVector(conditionsSize, propertiesSize);
        conditionVectorDataModel.updateData(conditions);
//        MatrixViewUiBuilder.conditionsVector.updateRecalculatedState(conditions);
        matrixDataModelListener.onConditionsVectorUpdated(conditions);

        List<String> suggestedStateValues = MultiplyORMatrixByConditionStateVector(conditionsSize, propertiesSize);
        suggestedStateVectorDataModel.updateData(suggestedStateValues);

//        MatrixViewUiBuilder.suggestedStatesVector.updateRecalculatedState(suggestedStateValues);
        matrixDataModelListener.onSuggestedStatesVectorUpdate(suggestedStateValues);

        List<String> currentInputVectorValues = inputStateVectorDataModel.getVectorValues();
        List<String> updatedStateVectorValues = applySuggestedStates(currentInputVectorValues, suggestedStateValues);
        inputStateVectorDataModel.updateData(updatedStateVectorValues);
        List<String> changeIndicatorVector = calculateDifference(updatedStateVectorValues, suggestedStateValues);
        inputStateVectorDataModel.setChangeIndicatorVector(changeIndicatorVector);
//        MatrixViewUiBuilder.inputVector.updateRecalculatedState(updatedStateVectorValues);
        matrixDataModelListener.onInputVectorUpdate(updatedStateVectorValues);
    }

    List<String> calculateDifference(List<String> currentInput, List<String> suggestedStates) {
        List<String> transformedStateValues = new ArrayList();
        int currentInputSize = currentInput.size();
        for (int i = 0; i < currentInputSize; i++) {
            String currentState = currentInput.get(i);
            String suggestedState = suggestedStates.get(i);
            String transformedState = AoSUtils.equals(currentState, suggestedState);
            transformedStateValues.add(transformedState.toString());
        }
        return transformedStateValues;
    }

    /**
     * Here the rows are filled with the combinations of the expected model state
     */
    private List<String> multiplyAMDMatrixByPropertyStateVector(int conditionsSize, int propertiesSize) {
        System.out.println("Multiplying AMD Matrix by Property State Vector");
        List<String> conditions = new ArrayList();
        for (int i = 0; i < conditionsSize; i++) {
            String conjunction = "-";
            for (int j = 0; j < propertiesSize; j++) {
                String stateOfJ = inputStateVectorDataModel.getValue(j);
                String andOfIJ = andMatrixDataModel.getMatrixElement(i, j);
                String result = AoSUtils.equals(andOfIJ, stateOfJ);
                conjunction = AoSUtils.conjunction(conjunction, result);
            }
            conditions.add(conjunction);
        }
        for (int i = 0; i < conditionsSize; i++) {
            System.out.println("Calculated Conjunction is " + conditions.get(i));
        }
        return conditions;
    }

    /**
     * Here the rows are filled with the combinations of the suggested model state
     */
    List<String> MultiplyORMatrixByConditionStateVector(int conditionsSize, int propertiesSize) {
        System.out.println("Multiplying OR Matrix by Condition State Vector");
        List<String> suggestedStateValues = new ArrayList();
        for (int i = 0; i < propertiesSize; i++) {
            String disjunction = "-";
            for (int j = 0; j < conditionsSize; j++) {
                String conditionOfJ = conditionVectorDataModel.getValue(j);
                String orOfIJ = orMatrixDataModel.getMatrixElement(j, i);
                System.out.println("Calculating Result State  i = " + i + " j =  " + j + "   " + orOfIJ);
                String suggestedState = AoSUtils.production(orOfIJ, conditionOfJ);
                disjunction = AoSUtils.disjunction(disjunction, suggestedState);
            }
            suggestedStateValues.add(disjunction);
        }
        for (int i = 0; i < propertiesSize; i++) {
            System.out.println("Calculated Disjunction is " + suggestedStateValues.get(i));
        }
        return suggestedStateValues;
    }

    private List<String> applySuggestedStates(List<String> currentInput, List<String> suggestedStates) {
        List<String> transformedStateValues = new ArrayList();
        int currentInputSize = currentInput.size();
        for (int i = 0; i < currentInputSize; i++) {
            String currentState = currentInput.get(i);
            String suggestedState = suggestedStates.get(i);
            String transformedState = AoSUtils.application(currentState, suggestedState);
            transformedStateValues.add(transformedState.toString());
        }
        return transformedStateValues;
    }
}
