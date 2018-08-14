package dsdsse.app;

import adf.menu.AdfMenuBar;
import adf.onelinemessage.AdfOneLineMessageManager;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.matrixview.MclnMatrixViewController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:51:46 PM
 * To change this template use File | Settings | File Templates.
 */
public final class DSDSSEAppBuilder {

    /**
     * builds application objects
     *
     * @param dsdsseMainFrame
     */
    public void build(DsdsseMainFrame dsdsseMainFrame) {

        DsdsDseMainPanel dsdsDseMainPanel = DsdsDseMainPanel.createInstance();
        dsdsseMainFrame.setDsdsDseMainPanel(dsdsDseMainPanel);
        JPanel panel = AdfOneLineMessageManager.getOneLineMessagePanelInstance();
        dsdsseMainFrame.setOneLineMessagePanel(panel);

        DsdsDseMatrixSpaceHolderPanel.createInstance();
        MclnMatrixViewController.createInstance();

        // init DSDS DSE Main Panel

        DsdsDseDesignSpaceHolderPanel dsdsDseDesignSpaceHolderPanel = DsdsDseDesignSpaceHolderPanel.createInstance();
        AdfMenuBar menuBar = dsdsDseDesignSpaceHolderPanel.createAndReturnDsdsDseMenuBar();
        dsdsseMainFrame.setJMenuBar(menuBar);

        // Populating Design Space content

        dsdsDseDesignSpaceHolderPanel.add(DseMenuAndToolbarBuilder.initToolBar(), BorderLayout.NORTH);
        DesignSpaceView designSpaceView = DesignSpaceView.getInstance();
        dsdsDseDesignSpaceHolderPanel.add(designSpaceView, BorderLayout.CENTER);
        dsdsseMainFrame.setDsdsDseDesignerViewHolderPanel(dsdsDseDesignSpaceHolderPanel);

        AppController.createInstance(menuBar);
    }
}
