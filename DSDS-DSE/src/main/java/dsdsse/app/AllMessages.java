package dsdsse.app;

/**
 * Created by Admin on 6/27/2017.
 */
public enum AllMessages {
    CAN_MODEL_INITIALIZATION_TO_BE_CANCELED_MESSAGE(
            "<html><div style=\"text-align:center; \">" +
            "Selected operation requires to cancel started initialization.&nbsp;&nbsp;<br>" +
            "Is it OK to cancel initialization ?" +
            "</div></html>"),

     USER_WANTS_TO_REPLACE_COMPONENT_IN_INIT_ASSISTANT(
            "<html><div style=\"text-align:center; \">" +
            "Init Assistant is currently initializing another component.&nbsp;<br>" +
            "Is it OK to cancel initialization ?" +
            "</div></html>"),

    CANCELLING_PROPERTY_IN_INIT_ASSISTANT_QUESTION("There is modified Property in the Init Assistant. Please make a choice."),
    CANCELLING_ARC_IN_ASSISTANT_QUESTION("There is modified Arc in the Init Assistant. Please make a choice."),
    CLEAR_DESIGN_SPACE_QUESTION("There is a project in the Design Space. Please make a choice."),
    DESIGN_SPACE_IS_EMPTY_MESSAGE("The Design Space is currently empty.  Nothing to clean.  "),
    DESIGN_SPACE_PRESENTATION_IS_BEING_INTERRUPTED_MESSAGE(
            "The Presentation is going to be interrupter after current step completed.  "),
    DESIGN_SPACE_PRESENTATION_INTERRUPTED_MESSAGE("The Presentation Show has been interrupted. Show Ended.  ");



    // I n s t a n c e

    private String text;

    AllMessages(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
