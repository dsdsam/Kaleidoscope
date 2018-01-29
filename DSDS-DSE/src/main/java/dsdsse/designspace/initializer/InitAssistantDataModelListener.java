package dsdsse.designspace.initializer;

/**
 * Created by Admin on 4/23/2016.
 */
interface InitAssistantDataModelListener {

    void onInitAssistantDataModelChanged(InitAssistantDataModel initAssistantDataModel,
                                         InitAssistantDataModel.AttributeID attributeID,
                                         boolean initialized);

    default String buildStateInterpretation(String componentName, String propertyName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(componentName).append(" ").append(propertyName);
        return stringBuilder.toString();
    }

    default void showMessage(String message) {
    }

    default void showAndHideMessage(String message) {
    }
}
