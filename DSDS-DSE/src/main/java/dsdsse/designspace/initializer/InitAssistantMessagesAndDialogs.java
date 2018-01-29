package dsdsse.designspace.initializer;

import dsdsse.app.DsdsDseMessagesAndDialogs;

import java.awt.*;

/**
 * Created by Admin on 5/19/2016.
 */
public class InitAssistantMessagesAndDialogs {

    static String MESSAGE_AVAILABLE_STATES_NOT_SELECTED = "Available States not Selected";

    private static Component popupParentComponent;

    public static void setPopupParentComponent(Component popupParentComponent) {
        InitAssistantMessagesAndDialogs.popupParentComponent = popupParentComponent;
    }

    private static int FONT_SIZE = 3;
    static String MESSAGE_TEMPLATE = new StringBuilder().append("<html><p>").
            append("<font  color=\"#020080\" size=\"" + FONT_SIZE + "\">").
            append("#M1#&nbsp;&nbsp;").
            append("<br>#M1#&nbsp;&nbsp;").
            append("</font>").
            append("<br>").
            append("</p></html>").toString();

    public static final String IS_IT_OK_TO_SHUTDOWN_INIT_ASSISTANT =
            "<html><div style=\"text-align:center; \">" +
                    "Selected Operation requires to close Initialization Assistant.&nbsp;<br>" +
                    "Is it OK to cancel current initialization ?" +
                    "</div></html>";

    public static final String USER_WANTS_TO_CLOSE_INIT_ASSISTANT =
            "<html><div style=\"text-align:center; \">" +
                    "Initialization Assistant is currently initializing model component.&nbsp;<br>" +
                    "Is it OK to cancel initialization ?" +
                    "</div></html>";

}
