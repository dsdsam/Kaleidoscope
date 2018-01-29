package adf.app;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 2/8/2016.
 */
public final class AdfMessagesAndDialogs {

    public static final char TYPE_MESSAGE = 'M';
    public static final char TYPE_WARNING = 'W';

    private static Component mainFrame;

    public static void setMainFrame(JFrame mainFrame) {
        AdfMessagesAndDialogs.mainFrame = mainFrame;
    }

    //
    //   M e s s a g e s
    //

    // Info message popup
    public static void showMessagePopup(Component parentComponent, String header, String message) {
        if(parentComponent == null) {
            parentComponent = mainFrame;
        }
        showMessagePopup(parentComponent, TYPE_MESSAGE, header, message);
    }

    // Warning message popup
    public static void showWarningPopup(Component parentComponent, String header, String message) {
        if(parentComponent == null) {
            parentComponent = mainFrame;
        }
        showMessagePopup(parentComponent, TYPE_WARNING, header, message);
    }

    //
    //   D i a l o g s
    //

    public static boolean showYesNoQuestion(Component parentComponent, String header, String question) {
        if(parentComponent == null) {
            parentComponent = mainFrame;
        }
        return AdfEnv.yesNoDlg(parentComponent, header, question);
    }

    public static final int threeOptionsDialog(Component parentComponent, String message, String title,
                                               Object[] options, Object initialOption) throws HeadlessException {
        int choice = JOptionPane.showOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, initialOption);
        return choice;
    }

    public static final String textEntryDialog(Component component, String title, String fieldMessage, String initialValue) {
        String reply = (String) JOptionPane.showInputDialog(component,
                fieldMessage, title, JOptionPane.PLAIN_MESSAGE, null, null, initialValue);
        return reply != null ? reply : initialValue;
    }

    //
    //   P r i v a t e s
    //

    private static void showMessagePopup(Component parentComponent, char type, String header, String message) {
        if(parentComponent == null) {
            parentComponent = mainFrame;
        }
        if (message == null) {
            JOptionPane.showMessageDialog(parentComponent, "The message is not provided");
            return;
        }

        if (type == 'M' || type == 'm') {
            if (header == null) {
                JOptionPane.showMessageDialog(parentComponent, message);
            } else {
                JOptionPane.showMessageDialog(parentComponent, message, header, JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        if (type == 'W' || type == 'w') {
            if (header == null) {
                JOptionPane.showMessageDialog(parentComponent, message, header, JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentComponent, message, header, JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
    }
}
