package adf.onelinemessage;

import java.awt.*;

/**
 * Created by Admin on 9/10/2017.
 * <p>
 * AdfOneLineMessageManager manages singleton one line message panel
 * that is intended to be placed at the bottom of application window.
 */
public class AdfOneLineMessageManager {

    private static final OneLineMessagePanel oneLineMessagePanel = new OneLineMessagePanel();

    public static final OneLineMessagePanel getOneLineMessagePanelInstance() {
        return oneLineMessagePanel;
    }

    //   H a n d l i n g   W a r n i n g   M e s s a g e

    public static final void showWarningMessage(String message) {
        showWarningMessage(0, message, 0);
    }

    public static final void showWarningMessage(int panelHeight, String message, int textSize) {
        oneLineMessagePanel.showMessage(panelHeight, OneLineMessagePanel.WARNING, message, textSize);
    }

    //   H a n d l i n g   I n f o   M e s s a g e

    public static final void showInfoMessage(String message) {
        showInfoMessage(0, message, 0);
    }

    public static final void showInfoMessage(int panelHeight, String message, int textSize) {
        oneLineMessagePanel.showMessage(panelHeight, OneLineMessagePanel.INFO, message, textSize);
    }

    //  C l e a r i n g

    public static final void clearHighPriorityMessage() {
        oneLineMessagePanel.clearMessage(true, true);
    }

    public static final void clearMessage() {
        oneLineMessagePanel.clearMessage(false, false);
    }
}
