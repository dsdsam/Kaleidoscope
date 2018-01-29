package dsdsse.app;

import adf.menu.AdfMenuBar;
import adf.onelinemessage.AdfOneLineMessageManager;
import dsdsse.designspace.DesignSpaceView;

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

    private DsdsseMainFrame dsdsseMainFrame;

    /**
     * builds application objects
     *
     * @param dsdsseMainFrame
     */
    public void build(DsdsseMainFrame dsdsseMainFrame) {
        this.dsdsseMainFrame = dsdsseMainFrame;
        DsdsseMainPanel dsdsseMainPanel = new DsdsseMainPanel();
//
//
////        MissionGuiStateModel.getInstance().addMissionGuiModelListener(modelController);
////
//        BasicMenuToolbarController defaultMenuToolbarController = seMainPanel.createController(mainFrame);
//        JMenuBar menuBar = defaultMenuToolbarController.initMenu();
//        mainFrame.setJMenuBar(menuBar);

        AdfMenuBar menuBar = DseMenuAndToolbarBuilder.getInstance().buildMenuBar();
        dsdsseMainFrame.setJMenuBar(menuBar);
//
//        dsdsseMainFrame.getRootPane().setBorder(null);
        Container contentPane = dsdsseMainFrame.getContentPane();

//        dsdsseMainPanel.setDoubleBuffered(false);
//        CardLayout cardLayout = new CardLayout();
//        contentPane.setLayout(cardLayout);
        contentPane.add(dsdsseMainPanel);
//        dsdsseMainFrame.addTopLevelPanel(DsdsseMainFrame.MAIN_PANEL_DSDSSE, dsdsseMainPanel);

        // init main panel

        dsdsseMainPanel.setBackground(Color.WHITE);

        DsdsseEnvironment.putMainPanel(this);

        dsdsseMainPanel.add(DseMenuAndToolbarBuilder.initToolBar(), BorderLayout.NORTH);

        DesignSpaceView designSpaceView = DesignSpaceView.getInstance();

//        mainModelView.setLayout(new GridBagLayout());
//
//        AppStatusPanel appStatusPanel = new AppStatusPanel();
//        mainModelView.add(appStatusPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//
//        DesignSpaceModel designSpaseModel = DesignSpaceModel.getInstance();
//        MclnGraphModel mclnModel = new MclnGraphModel("MCLN Model");
//
//        MclnGraphView mclnCSysView = new MclnGraphView(-50, -30, 100, 60, CSys.DRAW_AXIS);
//
//        DesignSpaceView mclnModelView = new DesignSpaceView(designSpaseModel, mclnModel, mclnCSysView);
//        mainModelView.add(mclnModelView, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
//                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        dsdsseMainPanel.add(designSpaceView, BorderLayout.CENTER);

        JPanel panel = AdfOneLineMessageManager.getOneLineMessagePanelInstance();
//        panel.setPreferredSize(new Dimension(0, 24));
        dsdsseMainPanel.add(panel, BorderLayout.SOUTH);

//        dsdsseMainPanel.initGlassPane(mclnModelView);
        AppController.initInstance(menuBar);
//        AppStateModel.getInstance().setMode(AppStateModel.Mode.DEVELOPMENT);
    }
}
