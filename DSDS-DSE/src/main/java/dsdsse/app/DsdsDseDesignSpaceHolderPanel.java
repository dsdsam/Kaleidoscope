package dsdsse.app;

//import adf.menu.AdfMenuBar;

import javax.swing.*;
import java.awt.*;

import adf.menu.AdfMenuBar;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 30, 2013
 * Time: 9:45:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DsdsDseDesignSpaceHolderPanel extends JPanel {

    public static String PREFIX = "/dsdsse-resources/images/app-icons/";
    public static String HELP_PREFIX = "/dsdsse-resources/images/splash/";

    private static DsdsDseDesignSpaceHolderPanel dsdsDseDesignSpaceHolderPanel;

    public static final synchronized DsdsDseDesignSpaceHolderPanel createInstance() {
        assert dsdsDseDesignSpaceHolderPanel == null : "Dsds Dse Designer View Main Panel is a singleton and already created";
        return dsdsDseDesignSpaceHolderPanel = new DsdsDseDesignSpaceHolderPanel();
    }

    public static final DsdsDseDesignSpaceHolderPanel getInstance() {
        assert dsdsDseDesignSpaceHolderPanel != null : "Dsds Dse Designer View Main Panel is a singleton and not yet created";
        return dsdsDseDesignSpaceHolderPanel;
    }

    //
    //   I n s t a n c e
    //

    private DsdsDseDesignSpaceHolderPanel() {
        super(new BorderLayout());
        setFocusable(true);
        setBackground(Color.WHITE);
    }

    public AdfMenuBar createAndReturnDsdsDseMenuBar(){
        return DseMenuAndToolbarBuilder.getInstance().buildMenuBar();
    }
}
