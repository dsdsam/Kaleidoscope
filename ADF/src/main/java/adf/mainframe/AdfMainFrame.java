/*
 * Created on Jul 22, 2005
 *
 */
package adf.mainframe;

import adf.app.AdfApp;
import adf.app.AdfMessagesAndDialogs;
import adf.app.AppManifestAttributes;
import adf.app.ConfigProperties;
import adf.utils.BuildUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xpadmin
 */
public class AdfMainFrame extends JFrame {

    protected Dimension curSize = new Dimension(0, 0); // is used for resizing

    private static AdfMainFrame adfMainFrame;

    protected static AdfMainFrame getInstance() {
        return adfMainFrame;
    }

    /**
     *
     */
    public AdfMainFrame() {
        adfMainFrame = this;
        AdfMessagesAndDialogs.setMainFrame(adfMainFrame);
        // activeBackground
        UIManager.put("adfActiveCaption", new Color(0, 0, 64));
        // activeForeground
        UIManager.put("adfActiveCaptionText", Color.BLUE);
        // activeShadow
        UIManager.put("AdfActiveCaptionBorder", Color.RED);
        ImageIcon imageIcon = BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "logo.gif");
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            setIconImage(image);
        }

        UIManager.put("AdfCloseButton", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_close.png"));
        UIManager.put("AdfCloseInactineIcon", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_close_inactive.png"));
        UIManager.put("AdfMinimizeButton", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_minimize.png"));
        UIManager.put("AdfMinimizeButton.Inactive.Icon", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_minimize_inactive.png"));
        UIManager.put("AdfMaximizeButton", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_maximize.png"));
        UIManager.put("AdfMaximizeButton.Inactive.Icon", BuildUtils.getImageIcon(AdfApp.BASE_IMAGE_LOCATION + "adf_maximize_inactive.png"));

        setFocusable(true);
    }

    /**
     *
     */
    public Dimension initMainFrame(double percent) {
        setTitle(System.getProperty(ConfigProperties.APP_TITLE_KEY) + " - " + AppManifestAttributes.getAppVersion());

        // set Main Frame size
        Toolkit tk = getToolkit();
        Dimension screenSize = tk.getScreenSize();

        int height = (int) (((float) screenSize.height) * percent);
        int portion = height / 3;

        curSize.width = (portion * 4);
        curSize.height = portion * 3;// - 60;

//        if (screenSize.width == 1024 && screenSize.height == 768) {
//            curSize.width = screenSize.width;
//            curSize.height = screenSize.height;
//            pack();
//            setSize(curSize);
//            this.setMinimumSize(new Dimension(300, 500));
//            return new Dimension(0, 0);
//        }
//
//        if (screenSize.width > 640) {
////            int height = (int) (((float) screenSize.height) * percent);
////            int portion = height / 3;
//            int width = 1024;
//            int portion = width / 4;
//            curSize.width = (portion * 4);
//            curSize.height = portion * 3;// - 60;
//        }


        setSize(curSize.width, curSize.height);
        this.setMinimumSize(new Dimension(600, 400));
        return new Dimension(screenSize.width - curSize.width, screenSize.height - curSize.height);
    }

}
