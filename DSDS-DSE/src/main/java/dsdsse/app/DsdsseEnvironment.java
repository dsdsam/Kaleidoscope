package dsdsse.app;

import adf.app.AdfEnv;
import adf.app.AdfMessagesAndDialogs;
import adf.app.AppManifestAttributes;
import adf.utils.BuildUtils;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphViewEditor;
import mclnview.graphview.MclnGraphModel;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 29, 2013
 * Time: 10:24:31 PM
 * To change this template use File | Settings | File Templates.
 */

public final class DsdsseEnvironment extends AdfEnv implements DSDSSEConstants {

    public static final int NO_MODE = -1;
    public static final int EDIT_MODE = 1;
    public static final int EXEC_MODE = 2;

    public static final String MCLN_PROJECT_STORAGE_DIRECTORY_NAME = "Mcln Project Storage";

    private static final String APP_FRAME_TITLE =
            " Discrete Symbolic Dynamical Systems Development & Simulating Environment";

    //
    // Component access
    //

    private static volatile MclnGraphModel mclnGraphModel;
    private static volatile MclnGraphViewEditor mclnGraphViewEditor;
    private static volatile DesignSpaceView designSpaceView;


    public static String getAppInitialFrameTitle() {
        String appVersion = AppManifestAttributes.getAppVersion();
        return APP_FRAME_TITLE + " - " + appVersion;
    }

    public static MclnGraphModel getMclnGraphModel() {
        return mclnGraphModel;
    }

    public static void setMclnModel(MclnGraphModel mclnGraphModel) {
        DsdsseEnvironment.mclnGraphModel = mclnGraphModel;
    }

    public static DesignSpaceView getDesignSpaceView() {
        return designSpaceView;
    }

    public static void setDesignSpaceView(DesignSpaceView designSpaceView) {
        DsdsseEnvironment.designSpaceView = designSpaceView;
    }

    public static MclnGraphViewEditor getMclnGraphViewEditor() {
        return mclnGraphViewEditor;
    }

    public static void setMclnGraphViewEditor(MclnGraphViewEditor mclnGraphViewEditor) {
        DsdsseEnvironment.mclnGraphViewEditor = mclnGraphViewEditor;
    }

    // Strings

    public static int curMode = NO_MODE;

// =================   C o n s t r u c t i n g   ===================

    private DsdsseEnvironment() {

    }

    // ===========================================================
    //   c o n v e n i e n c e    m e t h o d s
    // ===========================================================

//    public static void showMessagePopup(char type, String header, String message) {
//        messageDlg(dsdsseMainFrame, type, header, message);
//    }

    public static void showMessagePopup(Component parentComponent, char type, String header, String message) {
        AdfMessagesAndDialogs.showMessagePopup(parentComponent, header, message);
    }


    public static void setCurrentMode(int newMode) {
        curMode = newMode;
    }


    public static int getCurrentMode() {
        return curMode;
    }


    public static void setCompFontSize(java.awt.Component comp, int style, int fontSize) {
        BuildUtils.resetComponentFont(comp, style, fontSize);
    }
}


