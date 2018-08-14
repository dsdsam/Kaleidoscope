package mclnmatrix.model;

import java.util.ArrayList;
import java.util.List;

public class InitialStateDataModel {

    static InitialStateDataModel createInstance(List<StateVectorCell> initialStateDataModel) {
        assert initialStateDataModel != null && !initialStateDataModel.isEmpty() :
                "Provided argument initialStateDataModel is eithet null ir empty list";
        return new InitialStateDataModel(initialStateDataModel);
    }

    private final List<StateVectorCell> initialStateCells;
    private final List<String> initialStateData = new ArrayList();;

    private InitialStateDataModel(List<StateVectorCell> initialStateCells) {
        this.initialStateCells = initialStateCells;
        for (StateVectorCell stateVectorCell : initialStateCells) {
            initialStateData.add(stateVectorCell.getState());
        }
    }

    public boolean isEmpty(){
        return initialStateData.isEmpty();
    }

    public void populateInpitVectorDataModel(VectorDataModel vectorDataModel) {
        vectorDataModel.updateData(initialStateData);
    }
}
