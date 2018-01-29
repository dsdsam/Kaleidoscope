package dsdsse.app;

import adf.app.AdfMessagesAndDialogs;
import adf.mainframe.AdfMainFrame;

import javax.swing.*;
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

    public static final String MAIN_PANEL_DSDSSE = "MAIN_PANEL_DSDSSE";

    private static DsdsseMainFrame dsdsseMainFrame;

    /**
     *
     * @return
     */
    public static DsdsseMainFrame createMainFrame(){
        if(dsdsseMainFrame != null){
            logger.severe("DsdsseMainFrame instance already created !!!");
            return dsdsseMainFrame ;
        }
        return new DsdsseMainFrame();
    }

    /**
     *
     * @return
     */
    public static DsdsseMainFrame getInstance() {
        return dsdsseMainFrame;
    }

    //   I n s t a n c e

//    private Map<String, JPanel> panelMap = new HashMap();


    private DsdsseMainFrame() {
        dsdsseMainFrame = this;
        AdfMessagesAndDialogs.setMainFrame(dsdsseMainFrame);
        DsdsDseMessagesAndDialogs.setMainFrame(dsdsseMainFrame);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

//    public void addTopLevelPanel(String IdKey, JPanel unit) {
//        panelMap.put(IdKey, unit);
//    }
}
