package dsdsse.app;

import adf.app.AdfMessagesAndDialogs;
import adf.mainframe.AdfMainFrame;
import adf.menu.AdfMenuBar;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 1, 2013
 * Time: 2:42:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DsdsseMainFrame extends AdfMainFrame {

    private static final Logger logger = Logger.getLogger(DsdsseMainFrame.class.getName());

    private static DsdsseMainFrame dsdsseMainFrame;

    public static final synchronized DsdsseMainFrame createMainFrame(){
        assert dsdsseMainFrame == null : "Dsds Dse Dsdsse Main Frame is a singleton and already created";
        return dsdsseMainFrame = new DsdsseMainFrame();
    }

    public static final DsdsseMainFrame getInstance() {
        assert dsdsseMainFrame != null : "Dsds Dse Dsdsse Main Frame is a singleton and not yet created";
        return dsdsseMainFrame;
    }

    //
    //   I n s t a n c e
    //

    private DsdsDseMainPanel dsdsDseMainPanel;
    private JPanel oneLineMessagePanel;

    private DsdsseMainFrame() {
        AdfMessagesAndDialogs.setMainFrame(dsdsseMainFrame);
        DsdsDseMessagesAndDialogs.setMainFrame(dsdsseMainFrame);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

    public final void setDsdsDseMainPanel(DsdsDseMainPanel dsdsDseMainPanel) {
        this.dsdsDseMainPanel = dsdsDseMainPanel;
        getContentPane().add(dsdsDseMainPanel);
    }

    public final void setOneLineMessagePanel(JPanel oneLineMessagePanel) {
        this.oneLineMessagePanel = oneLineMessagePanel;
    }

    public final void setDsdsDseDesignerViewHolderPanel(DsdsDseDesignSpaceHolderPanel dsdsDseDesignSpaceHolderPanel){
        dsdsDseMainPanel.removeAll();
        AdfMenuBar menuBar = dsdsDseDesignSpaceHolderPanel.createAndReturnDsdsDseMenuBar();
        setJMenuBar(menuBar);
        dsdsDseMainPanel.add(dsdsDseDesignSpaceHolderPanel, BorderLayout.CENTER);
        dsdsDseMainPanel.add(oneLineMessagePanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public final void setDsdsDseMatrixViewHolderPanel(DsdsDseMatrixSpaceHolderPanel dsdsDseMatrixSpaceHolderPanel){
        dsdsDseMainPanel.removeAll();
        AdfMenuBar menuBar = dsdsDseMatrixSpaceHolderPanel.createAndReturnDsdsDseMatrixViewMenuBar();
        setJMenuBar(menuBar);
        dsdsDseMainPanel.add(dsdsDseMatrixSpaceHolderPanel, BorderLayout.CENTER);
        dsdsDseMainPanel.add(oneLineMessagePanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
