package dsdsse.matrixview;

import adf.menu.AdfMenuActionListener;
import adf.menu.AdfMenuBar;
import dsdsse.app.*;
import dsdsse.designspace.DesignSpaceView;

import static dsdsse.app.AppController.MENU_ITEM_GRAPH_VIEW;
import static dsdsse.app.AppController.MENU_ITEM_MATRIX_VIEW;

public class MclnMatrixViewController {

    private static MclnMatrixViewController mclnMatrixViewController;

    public static MclnMatrixViewController createInstance() {
        assert mclnMatrixViewController == null : "Mcln Matrix View Controller is a singleton and already created";
        return mclnMatrixViewController = new MclnMatrixViewController();
    }

    public static MclnMatrixViewController getInstance() {
        assert mclnMatrixViewController != null : "Mcln Matrix View Controller is a singleton and not yet created";
        return mclnMatrixViewController;
    }

    //
    //   A p p   C o n t r o l l e r   I n s t a n c e
    //

    private AdfMenuBar menuBar;
    private AppStateModel appStateModel;
    private DesignSpaceView designSpaceView;

    //
    //   M e n u   A c t i o n   L i s t e n e r
    //

    private final AdfMenuActionListener adfMenuActionListener = (ae) -> {
        String actionCommand = ae.getActionCommand();
        boolean done = processMenuOrToolbarCommand(actionCommand);
        return done;
    };


    //
    //  A p p   C o n t r o l l e r   I n s t a n c e
    //

    private MclnMatrixViewController() {

    }


    public AdfMenuActionListener getMenuListener() {
        return adfMenuActionListener;
    }

    private String lastMenuCommand;

    /**
     * @param cmd
     */
    private boolean processMenuOrToolbarCommand(String cmd) {

        lastMenuCommand = cmd;
        if (AppController.MENU_ITEM_EXIT.equals(cmd)) {
            // Condition: Current editing operation must be canceled
            AppController.getInstance().unselectCreationOperation();
            AppController.getInstance().userClosesApplication();

        } else if (MENU_ITEM_GRAPH_VIEW.equals(cmd) || MENU_ITEM_MATRIX_VIEW.equals(cmd)) {
            switchToSelectedView(cmd);
        }

        // returning "true" makes button enabled
        return true;
    }

    private void switchToSelectedView(String selectedView) {
        if (!selectedView.equals(AppController.MENU_ITEM_GRAPH_VIEW)) {
            return;
        }
        DsdsseMainFrame mainFrame = DsdsseMainFrame.getInstance();
        DsdsDseDesignSpaceHolderPanel dsdsDseDesignSpaceHolderPanel = DsdsDseDesignSpaceHolderPanel.getInstance();
        mainFrame.setDsdsDseDesignerViewHolderPanel(dsdsDseDesignSpaceHolderPanel);
    }
}

