package dsdsse.animation;

/**
 * Created by Admin on 5/26/2017.
 */
class ScriptItem {
    private final String operation;
    private final String stepAction;
    private final String actionAttribute1;
    private final String actionAttribute2;
    private final String actionAttribute3;
    private final String actionAttribute4;

    ScriptItem(String operation, String stepAction, String actionAttribute1, String actionAttribute2,
               String actionAttribute3, String actionAttribute4) {
        this.operation = operation;
        this.stepAction = stepAction;
        this.actionAttribute1 = actionAttribute1;
        this.actionAttribute2 = actionAttribute2;
        this.actionAttribute3 = actionAttribute3;
        this.actionAttribute4 = actionAttribute4;
    }

    String getOperation() {
        return operation;
    }

    String getStepAction() {
        return stepAction;
    }

    String getMessageText() {
        return stepAction;
    }

    String getActionAttribute1() {
        return actionAttribute1;
    }

    String getMessageBackground() { return actionAttribute1; }

    String getMessageForeground() {
        return actionAttribute2;
    }

    String getActionAttribute2() { return actionAttribute2; }

    String getActionAttribute3() {
        return actionAttribute3;
    }

    String getMessageTime() {
        return actionAttribute3;
    }

    String getNewYCoordinate() {
        return actionAttribute3;
    }

    String getMessageType() {
        return actionAttribute4;
    }

    public String toString() {
        return "Operation: \"" + operation +
                "\",  Step action: \"" + stepAction +
                "\",  Action attribute-1: \"" + actionAttribute1 +
                "\",  Action attribute-2: \"" + actionAttribute2 + "\"";

    }
}
