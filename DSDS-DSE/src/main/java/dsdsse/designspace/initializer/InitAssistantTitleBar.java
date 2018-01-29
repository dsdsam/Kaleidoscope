package dsdsse.designspace.initializer;

import adf.ui.components.dialogs.undecorated.AdfUndecoratedDialogTitleBar;

import java.awt.*;

/**
 * Created by Admin on 3/16/2016.
 */
final class InitAssistantTitleBar extends AdfUndecoratedDialogTitleBar {

    private static final int HEADER_PANEL_HEIGHT = 30;
    private static final Dimension HEADER_PANEL_SIZE = new Dimension(1, HEADER_PANEL_HEIGHT);
    private static final String PLAIN_HEADER_TEXT = "Initialization Assistant";
    private static final String HEADER_SUFFIX = " - Initializing ";

    private static final float DARK_COLOR_PERCENT = 60;
    private static final Color HEADER_BACKGROUND = new Color(0x008200);
    private static final Color HEADER_BACKGROUND_MAY_BE = new Color(0x01743A);
    private static final Color HEADER_FOREGROUND = Color.WHITE;

    InitAssistantTitleBar() {
        super("/dsdsse-resources/images/",PLAIN_HEADER_TEXT, HEADER_BACKGROUND, HEADER_FOREGROUND);
        setOpaque(false);
        setMinimumSize(HEADER_PANEL_SIZE);
        setMaximumSize(HEADER_PANEL_SIZE);
        setPreferredSize(HEADER_PANEL_SIZE);
    }

    void extendHeaderText(String text) {
        String extendedHeaderText = PLAIN_HEADER_TEXT + ":  " + text;
        super.setHeaderText(extendedHeaderText);
    }

    void extendHeaderText(String entity, String entityID) {
        String extendedHeaderText = PLAIN_HEADER_TEXT + HEADER_SUFFIX + entity + ": " + entityID;
        super.setHeaderText(extendedHeaderText);
    }

    void resetToPlainHeaderText() {
        super.setHeaderText(PLAIN_HEADER_TEXT);
    }
}
