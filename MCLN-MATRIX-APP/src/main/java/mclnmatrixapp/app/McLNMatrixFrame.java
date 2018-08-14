package mclnmatrixapp.app;

import adf.app.AdfMessagesAndDialogs;
import adf.mainframe.AdfMainFrame;

import javax.swing.*;
import java.util.logging.Logger;

public class McLNMatrixFrame extends AdfMainFrame {

    private static final Logger logger = Logger.getLogger(McLNMatrixFrame.class.getName());

    private static McLNMatrixFrame mclnMatrixFrame;

    public static McLNMatrixFrame createMainFrame() {
        if (mclnMatrixFrame != null) {
            logger.severe("McLN Matrix Frame instance already created !!!");
            return mclnMatrixFrame;
        }
        mclnMatrixFrame = new McLNMatrixFrame();
        return mclnMatrixFrame;
    }

    public static McLNMatrixFrame getInstance() {
        return mclnMatrixFrame;
    }

    //
    //   I n s t a n c e
    //

    private McLNMatrixFrame() {
        mclnMatrixFrame = this;
        AdfMessagesAndDialogs.setMainFrame(this);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

    }

}

