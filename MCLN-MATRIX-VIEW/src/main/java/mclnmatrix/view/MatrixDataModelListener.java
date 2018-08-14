package mclnmatrix.view;

import java.util.List;

public interface MatrixDataModelListener {

    void onConditionsVectorUpdated(List<String> conditions);

    void onSuggestedStatesVectorUpdate(List<String> conditions);

    void onInputVectorUpdate(List<String> conditions);

}
