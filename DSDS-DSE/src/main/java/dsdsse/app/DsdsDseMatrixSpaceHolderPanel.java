package dsdsse.app;

import adf.menu.AdfMenuBar;
import dsdsse.designspace.DesignSpaceView;

import javax.swing.*;
import java.awt.*;

public class DsdsDseMatrixSpaceHolderPanel extends JPanel {

    private static DsdsDseMatrixSpaceHolderPanel dsdsDseMatrixSpaceHolderPanel;

    public static final synchronized DsdsDseMatrixSpaceHolderPanel createInstance() {
        assert dsdsDseMatrixSpaceHolderPanel == null : "Dsds Dse Matrix View Main Panel is a singleton and already created";
        return dsdsDseMatrixSpaceHolderPanel = new DsdsDseMatrixSpaceHolderPanel();
    }

    public static final DsdsDseMatrixSpaceHolderPanel getInstance() {
        assert dsdsDseMatrixSpaceHolderPanel != null : "Dsds Dse Matrix View Main Panel is a singleton and not yet created";
        return dsdsDseMatrixSpaceHolderPanel;
    }

    private DsdsDseMatrixSpaceHolderPanel() {
        super(new BorderLayout());
        setFocusable(true);
        setBackground(Color.WHITE);
//        AppMatrixViewMainPanel appMatrixViewMainPanel = AppMatrixViewMainPanel.createInstance();
//        add(appMatrixViewMainPanel, BorderLayout.CENTER);
    }

    public AdfMenuBar createAndReturnDsdsDseMatrixViewMenuBar() {
        return DseMenuAndToolbarBuilder.getInstance().buildMclnMatrixViewMenuBar();
    }

    void buildContents(DsdsseMainFrame dsdsseMainFrame) {
        DesignSpaceView designSpaceView = DesignSpaceView.getInstance();
        add(designSpaceView, BorderLayout.CENTER);
    }
}
