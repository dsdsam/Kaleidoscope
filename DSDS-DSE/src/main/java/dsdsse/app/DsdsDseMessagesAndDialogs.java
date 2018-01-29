package dsdsse.app;

import adf.app.AdfMessagesAndDialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 8/24/2017.
 */
public final class DsdsDseMessagesAndDialogs {

    private static Component mainFrame;

    private static int FONT_SIZE = 3;

    public static void setMainFrame(JFrame mainFrame) {
        DsdsDseMessagesAndDialogs.mainFrame = mainFrame;
    }

    // Arc creation message
    public static String MESSAGE_STRAIGHT_3POINT_ARC_UNDO = new StringBuilder().append("<html><div style=\"text-align:center; \">").
            append("<font  color=\"#020080\" size=\"" + FONT_SIZE + "\">").
            append("Designed Straight Arc is too short. Cannot fit the Arc Arrow.&nbsp;&nbsp;<br>").
            append("Please choose other solution or click Right Mouse Button (RMB) to undo previous step.&nbsp;&nbsp;&nbsp;").
            append("</font>").
            append("<br>").
            append("</html>").toString();

    // Arc creation message
    public static String MESSAGE_3POINT_ARC_UNDO = new StringBuilder().append("<html><div style=\"text-align:center; \">").
            append("<font  color=\"#020080\" size=\"" + FONT_SIZE + "\">").
            append("Designed Three Point Arc is too short. Cannot fit the Arc Arrow.&nbsp;&nbsp;<br>").
            append("Please choose other solution or click Right Mouse Button (RMB to undo previous step.&nbsp;&nbsp;&nbsp;").
            append("</font>").
            append("<br>").
            append("</html>").toString();

    // Arc creation message
    public static String MESSAGE_MULTI_KNOT_ARC_UNDO = new StringBuilder().append("<html><div style=\"text-align:center; \">").
            append("<font  color=\"#020080\" size=\"" + FONT_SIZE + "\">").
            append("Designed Multi-Knot Arc is too short. Cannot fit the Arc Arrow.&nbsp;&nbsp;<br>").
            append("Please choose other solution or click Right Mouse Button (RMB to undo previous step.&nbsp;&nbsp;&nbsp;").
            append("</font>").
            append("<br>").
            append("</html>").toString();

    //
    //   M e s s a g e s
    //

    public static void showMessage(Component parentComponent, String header, String message) {
        if (parentComponent == null) {
            parentComponent = mainFrame;
        }
        AdfMessagesAndDialogs.showMessagePopup(parentComponent, header, message);
    }

    public static void showWarning(Component parentComponent, String header, String message) {
        if (parentComponent == null) {
            parentComponent = mainFrame;
        }
        AdfMessagesAndDialogs.showWarningPopup(parentComponent, header, message);
    }

    //
    //   D i a l o g s
    //

    public static boolean showYesNoQuestion(Component parentComponent, String header, String message) {
        if (parentComponent == null) {
            parentComponent = mainFrame;
        }
        return AdfMessagesAndDialogs.showYesNoQuestion(parentComponent, header, message);
    }

    public static int threeOptionsDialog(Component parentComponent, String message, String title, String[] options) {
        return AdfMessagesAndDialogs.threeOptionsDialog(parentComponent, message, title,
                options, options[0]);
    }

    public static final String textEntryDialog(Component component, String title, String fieldMessage, String initialValue) {
        String reply = AdfMessagesAndDialogs.textEntryDialog(component, title, fieldMessage, initialValue);
        return reply != null ? reply : initialValue;
    }
}
