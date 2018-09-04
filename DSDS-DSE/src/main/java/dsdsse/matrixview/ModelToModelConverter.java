package dsdsse.matrixview;

import mcln.model.*;
import mcln.palette.MclnState;
import mclnmatrix.model.*;
import mclnview.graphview.MclnGraphModel;

import java.util.*;

public class ModelToModelConverter {

    static MclnBasedMatrixModel convertMclnModelToMatrixModel(MclnGraphModel mclnGraphModel, MclnModel mclnModel) {

        MclnBasedMatrixModel mclnBasedMatrixModel =
                MclnBasedMatrixModel.createMclnBasedMatrixModel(null, mclnGraphModel, mclnModel);

        //

        List<MclnStatement> orderedStatements = createOrderedStatementsList(mclnModel);
        mclnBasedMatrixModel.setOrderedStatements(orderedStatements);

        List<MclnCondition> orderedConditions = createOrderedConditionsList(mclnModel);
        mclnBasedMatrixModel.setOrderedConditions(orderedConditions);

        //

        // creating input state vector data
        List<VectorCell> inputVectorStateData = createVectorStateData(false, orderedStatements);
        mclnBasedMatrixModel.setInputStateVectorData(inputVectorStateData);

        // creating condition state vector data
        List<VectorCell> conditionVectorStateData = createVectorStateData(false, orderedConditions);
        mclnBasedMatrixModel.setConditionStateVectorData(conditionVectorStateData);

        // creating suggested state vector data
        List<VectorCell> suggestedVectorStateData = createVectorStateData(true, orderedStatements);
        mclnBasedMatrixModel.setSuggestedStateVectorData(suggestedVectorStateData);

        //

        Map<String, VectorCell> statementUidToInputVectorCellMap =
                createNodeUidToVectorCellMap(inputVectorStateData);
        mclnBasedMatrixModel.setStatementUidToInputVectorCellMap(statementUidToInputVectorCellMap);

        Map<String, VectorCell> conditionUidToConditionVectorCellMap =
                createNodeUidToVectorCellMap(conditionVectorStateData);
        mclnBasedMatrixModel.setConditionUidToConditionVectorCellMap(conditionUidToConditionVectorCellMap);

        Map<String, VectorCell> statementUidToSuggestedVectorCellMap =
                createNodeUidToVectorCellMap(suggestedVectorStateData);
        mclnBasedMatrixModel.setStatementUidToSuggestedVectorCellMap(statementUidToSuggestedVectorCellMap);

        // creating matrix

        List<Map<Integer, AndCell>> andMatrixConditions = convertMclnModelToAndMatrixModel(orderedStatements,
                orderedConditions);
        mclnBasedMatrixModel.setAndMatrixConditions(andMatrixConditions);

        List<Map<Integer, OrCell>> orMatrixConditions = convertMclnModelToOrMatrixModel(orderedStatements,
                orderedConditions);
        mclnBasedMatrixModel.setOrMatrixConditions(orMatrixConditions);

        mclnBasedMatrixModel.setAndMatrixDataModelForHorizontalLayout(
                new AndMatrixDataModel(true, andMatrixConditions));
        mclnBasedMatrixModel.setOrMatrixDataModelForHorizontalLayout(
                new OrMatrixDataModel(true, orMatrixConditions));
        mclnBasedMatrixModel.setAndMatrixDataModelForVerticalLayout(
                new AndMatrixDataModel(false, andMatrixConditions));
        mclnBasedMatrixModel.setOrMatrixDataModelForVertivalLayout(
                new OrMatrixDataModel(false, orMatrixConditions));

        return mclnBasedMatrixModel;
    }

    private static List<MclnStatement> createOrderedStatementsList(MclnModel mclnModel) {
        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        SortedSet<MclnStatement> setOfSortedStatements = new TreeSet(mclnStatements);
        List<MclnStatement> orderedStatements = new ArrayList();
        for (MclnStatement mclnStatement : mclnStatements) {
            orderedStatements.add(mclnStatement);
        }
        return orderedStatements;
    }

    private static List<MclnCondition> createOrderedConditionsList(MclnModel mclnModel) {
        List<MclnCondition> mclnConditions = mclnModel.getMclnConditions();
        SortedSet<MclnCondition> setOfSortedConditions = new TreeSet(mclnConditions);
        List<MclnCondition> orderedConditions = new ArrayList();
        for (MclnCondition mclnCondition : setOfSortedConditions) {
            orderedConditions.add(mclnCondition);
        }
        return orderedConditions;
    }

    private static void printList(String header, List<? extends MclnNode> listOfNodes) {
        System.out.println("\n" + header);
        for (MclnNode mclnNode : listOfNodes) {
            System.out.println("Node " + mclnNode.getUID());
        }
    }

    static List<VectorCell> createVectorStateData(boolean setEmptyInitialState,
                                                  List<? extends MclnNode> orderedMclnNodes) {
        List<VectorCell> vectorData = new ArrayList();
        int listSize = orderedMclnNodes.size();
        for (int i = 0; i < listSize; i++) {
            MclnNode mclnNode = orderedMclnNodes.get(i);
            String initialState;
            if (setEmptyInitialState) {
                initialState = "-";
            } else {
                MclnState mclnState = mclnNode.getInitialMclnState();
                initialState = mclnState.getHexColor();
            }
            VectorCell vectorCell = VectorCell.createCell(mclnNode, i, "", initialState);
            vectorData.add(vectorCell);
        }
        return vectorData;
    }

    /**
     * @param orderedStatements
     * @param orderedConditions
     * @return
     */
    private static List<Map<Integer, AndCell>> convertMclnModelToAndMatrixModel(List<MclnStatement> orderedStatements,
                                                                                List<MclnCondition> orderedConditions) {
        List<Map<Integer, AndCell>> andMatrixConditions = new ArrayList();

        // loop through matrix rows (conditions)
        int conditionsSize = orderedConditions.size();
        for (int i = 0; i < conditionsSize; i++) {
            MclnCondition mclnCondition = orderedConditions.get(i);
            List<MclnArc<MclnStatement, MclnCondition>> conditionInboundArcs = mclnCondition.getClonedInboundArcs();

            // loop through matrix columns (statements)
            Map<Integer, AndCell> conjunction = new HashMap();
            int statementsSize = orderedStatements.size();
            for (int j = 0; j < statementsSize; j++) {
                MclnStatement mclnStatement = orderedStatements.get(j);

                // find the Arc that goes from current Statement to current Condition
                String columnStatementUid = mclnStatement.getUID();
                for (MclnArc<MclnStatement, MclnCondition> conditionInboundArc : conditionInboundArcs) {
                    MclnStatement mclnArcInputStatement = conditionInboundArc.getInpNode();
                    String arcInputStatementUid = mclnArcInputStatement.getUID();
                    if (columnStatementUid.equalsIgnoreCase(arcInputStatementUid)) {
                        MclnState mclnState = conditionInboundArc.getArcMclnState();
                        String expectedState = mclnState.getHexColor();
                        AndCell andCell = AndCell.createAndCell(conditionInboundArc, i, j, columnStatementUid, expectedState);
                        conjunction.put(j, andCell);
                    }
                }
            }
            andMatrixConditions.add(conjunction);
        }
        return andMatrixConditions;
    }

    private static List<Map<Integer, OrCell>> convertMclnModelToOrMatrixModel(List<MclnStatement> orderedStatements,
                                                                              List<MclnCondition> orderedConditions) {
        List<Map<Integer, OrCell>> orMatrixConditions = new ArrayList();

        // loop through matrix rows (conditions)
        int conditionsSize = orderedConditions.size();
        for (int i = 0; i < conditionsSize; i++) {
            MclnCondition mclnCondition = orderedConditions.get(i);
            List<MclnArc<MclnCondition, MclnStatement>> conditionOutboundArcs = mclnCondition.getClonedOutboundArcs();

            // loop through matrix columns (statements)
            Map<Integer, OrCell> disjunction = new HashMap();
            int statementsSize = orderedStatements.size();
            for (int j = 0; j < statementsSize; j++) {
                MclnStatement mclnStatement = orderedStatements.get(j);

                // find the Arc that goes from current Condition to current Statement
                String columnStatementUid = mclnStatement.getUID();
                for (MclnArc<MclnCondition, MclnStatement> conditionOutboundArc : conditionOutboundArcs) {
                    MclnStatement mclnArcOutputStatement = conditionOutboundArc.getOutNode();
                    String arcOutputStatementUid = mclnArcOutputStatement.getUID();
                    if (columnStatementUid.equalsIgnoreCase(arcOutputStatementUid)) {
                        MclnState mclnState = conditionOutboundArc.getArcMclnState();
                        String expectedState = mclnState.getHexColor();
                        OrCell orCell = OrCell.createOrCell(conditionOutboundArc, i, j, columnStatementUid, expectedState);
                        disjunction.put(j, orCell);
                    }
                }
            }
            orMatrixConditions.add(disjunction);
        }
        return orMatrixConditions;
    }

    private static Map<String, VectorCell> createNodeUidToVectorCellMap(List<VectorCell> vectorStateData) {
        Map<String, VectorCell> nodeUidToVectorCellMap = new HashMap();
        int vectorSize = vectorStateData.size();
        for (int i = 0; i < vectorSize; i++) {
            VectorCell vectorCell = vectorStateData.get(i);
            MclnNode mclnStatement = (MclnNode) vectorCell.getSource();
            nodeUidToVectorCellMap.put(mclnStatement.getUID(), vectorCell);
        }
        return nodeUidToVectorCellMap;
    }
}
