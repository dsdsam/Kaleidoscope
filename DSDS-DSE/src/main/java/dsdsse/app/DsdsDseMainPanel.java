package dsdsse.app;

import adf.menu.AdfMenuBar;
import dsdsse.designspace.DesignSpaceView;

import javax.swing.*;
import java.awt.*;

public class DsdsDseMainPanel extends JPanel {

    private static DsdsDseMainPanel dsdsDseMainPanel ;

    public static DsdsDseMainPanel createInstance() {
        assert dsdsDseMainPanel == null : "Dsds Dse Main Panel is a singleton and already created";
        return dsdsDseMainPanel = new DsdsDseMainPanel();
    }

    public static DsdsDseMainPanel getInstance() {
        assert dsdsDseMainPanel != null : "Dsds Dse Main Panel is a singleton and not yet created";
        return dsdsDseMainPanel;
    }

    private DsdsDseMainPanel() {
        super(new BorderLayout());
        setFocusable(true);
        setBackground(Color.WHITE);
    }



//    public AdfMenuBar createAndReturnDsdsDseMatrixViewMenuBar(){
//        return DseMenuAndToolbarBuilder.getInstance().buildMclnMatrixViewMenuBar();
//    }
//
//    void buildContents(DsdsseMainFrame dsdsseMainFrame) {
//        DesignSpaceView designSpaceView = DesignSpaceView.getInstance();
//
//        add(designSpaceView, BorderLayout.CENTER);
//    }
}
