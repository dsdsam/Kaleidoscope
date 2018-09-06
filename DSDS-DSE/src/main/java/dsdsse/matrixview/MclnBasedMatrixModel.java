package dsdsse.matrixview;

import mcln.model.MclnCondition;
import mcln.model.MclnModel;
import mcln.model.MclnModelSimulationListener;
import mcln.model.MclnStatement;
import mcln.palette.MclnState;
import mclnmatrix.model.MclnMatrixModel;
import mclnmatrix.model.VectorCell;
import mclnmatrix.view.MclnMatrixView;
import mclnview.graphview.MclnGraphModel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MclnBasedMatrixModel extends MclnMatrixModel {

    static MclnBasedMatrixModel createMclnBasedMatrixModel(MclnDesignerMatrixView mclnDesignerMatrixView,
                                                           MclnGraphModel mclnGraphModel, MclnModel mclnModel) {
        return new MclnBasedMatrixModel(mclnDesignerMatrixView, mclnGraphModel, mclnModel);
    }

    //   I n s t a n c e

    private final List<MclnStatement> orderedStatements = new ArrayList();
    private final List<MclnCondition> orderedConditions = new ArrayList();

    private final Map<String, VectorCell> statementUidToInputVectorCellMap = new HashMap();
    private final Map<String, VectorCell> conditionUidToConditionVectorCellMap = new HashMap();
    private final Map<String, VectorCell> statementUidToSuggestedVectorCellMap = new HashMap();

    private final Map<String, String> statementUidToSuggestedStateMap = new HashMap();

    //
    //   MclnModelSimulationListener listens simulation process
    //

    private MclnModelSimulationListener mclnModelSimulationListener = new MclnModelSimulationListener() {

        @Override
        public void newPropertySuggestedStateInferred(MclnStatement mclnStatement) {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulatedSuggestedStateInferred(mclnStatement);
            } else {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        onSimulatedSuggestedStateInferred(mclnStatement);
                    });
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }

        @Override
        public void mclnModelStateChanged() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStateChange();
            } else {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        onSimulationStateChange();
                    });
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }

        @Override
        public void simulationStepExecuted() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStepExecuted();
            } else {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        onSimulationStepExecuted();
                    });
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }

        @Override
        public void mclnModelStateReset() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStateReset();
            } else {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        onSimulationStateReset();
                    });
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }
    };

    /**
     * @param mclnStatement
     */
    private void onSimulatedSuggestedStateInferred(MclnStatement mclnStatement) {
        MclnState mclnState = mclnStatement.getCurrentMclnState();
        String updatedState = mclnState.getHexColor();
        String statementUID = mclnStatement.getUID();
        statementUidToSuggestedStateMap.put(statementUID, updatedState);
    }

    /**
     *
     */
    private void onSimulationStateChange() {
//        System.out.println("MclnBasedMatrixModel onSimulationStateChange");
    }

    /**
     *
     */
    private void onSimulationStepExecuted() {

        // updating Input vector
        for (MclnStatement mclnStatement1 : orderedStatements) {
            MclnState statementMclnState = mclnStatement1.getCurrentMclnState();
            String statementCurrentValue = statementMclnState.getHexColor();
            VectorCell inputVectorCell = statementUidToInputVectorCellMap.get(mclnStatement1.getUID());
            inputVectorCell.setState(statementCurrentValue);
        }

        // updating Conditions vector
        for (MclnCondition mclnCondition : orderedConditions) {
            MclnState conditionMclnState = mclnCondition.getCurrentMclnState();
            String conditionCurrentValue = conditionMclnState.getHexColor();
            VectorCell conditionVectorCell = conditionUidToConditionVectorCellMap.get(mclnCondition.getUID());
            conditionVectorCell.setState(conditionCurrentValue);
        }

        //
        for (MclnStatement mclnStatement2 : orderedStatements) {
            String suggestedState = statementUidToSuggestedStateMap.get(mclnStatement2.getUID());
            VectorCell suggestedVectorCell = statementUidToSuggestedVectorCellMap.get(mclnStatement2.getUID());
            if (suggestedState == null) {
                suggestedVectorCell.setState("-");
            } else {
                suggestedVectorCell.setState(suggestedState);
            }
        }
        statementUidToSuggestedStateMap.clear();
    }

    /**
     *
     */
    private void onSimulationStateReset() {
        getInputStateVectorDataModel().setToInitialState();
        getConditionStateVectorDataModel().setToInitialState();
        getSuggestedStateVectorDataModel().setToInitialState();
        mclnMatrixView.repaint();
    }

    //

    private MclnDesignerMatrixView mclnDesignerMatrixView;
    MclnMatrixView mclnMatrixView;
    private final MclnGraphModel mclnGraphModel;
    private final MclnModel mclnModel;

    private MclnBasedMatrixModel(MclnDesignerMatrixView mclnDesignerMatrixView, MclnGraphModel mclnGraphModel,
                                 MclnModel mclnModel) {
        this.mclnDesignerMatrixView = mclnDesignerMatrixView;
        this.mclnGraphModel = mclnGraphModel;
        this.mclnModel = mclnModel;
        /*
               We call addListener to ask mclnGraphModel to add listener to new Mcln Model.
               It is removed when model is cleared
             */
        mclnGraphModel.addMclnModelSimulationListener(mclnModelSimulationListener);
    }

    public void setMclnDesignerMatrixView(MclnMatrixView mclnMatrixView) {
        this.mclnMatrixView = mclnMatrixView;
    }

    public void setOrderedStatements(List<MclnStatement> orderedStatements) {
        this.orderedStatements.addAll(orderedStatements);
    }

    public void setOrderedConditions(List<MclnCondition> orderedConditions) {
        this.orderedConditions.addAll(orderedConditions);
    }

    protected void setInputStateVectorData(List<VectorCell> inputStateVectorData) {
        getInputStateVectorDataModel().setVectorStateData(inputStateVectorData);
    }

    protected void setConditionStateVectorData(List<VectorCell> conditionStateVectorData) {
        getConditionStateVectorDataModel().setVectorStateData(conditionStateVectorData);
    }

    protected void setSuggestedStateVectorData(List<VectorCell> suggestedStateVectorData) {
        getSuggestedStateVectorDataModel().setVectorStateData(suggestedStateVectorData);
    }

    // Maps

    public void setStatementUidToInputVectorCellMap(Map<String, VectorCell> statementUidToInputVectorCellMap) {
        this.statementUidToInputVectorCellMap.putAll(statementUidToInputVectorCellMap);
    }

    public void setConditionUidToConditionVectorCellMap(Map<String, VectorCell> conditionUidToInputVectorCellMap) {
        this.conditionUidToConditionVectorCellMap.putAll(conditionUidToInputVectorCellMap);
    }

    public void setStatementUidToSuggestedVectorCellMap(Map<String, VectorCell> statementUidToSuggestedVectorCellMap) {
        this.statementUidToSuggestedVectorCellMap.putAll(statementUidToSuggestedVectorCellMap);
    }

    public Map<String, VectorCell> getStatementUidToInputVectorCellMap() {
        return statementUidToInputVectorCellMap;
    }

    public Map<String, VectorCell> getConditionUidToConditionVectorCellMap() {
        return conditionUidToConditionVectorCellMap;
    }

    public Map<String, VectorCell> getStatementUidToSuggestedVectorCellMap() {
        return statementUidToSuggestedVectorCellMap;
    }

}
