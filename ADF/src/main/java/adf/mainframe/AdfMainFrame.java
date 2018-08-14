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
import java.util.logging.Logger;

/**
 * @author xpadmin
 */
public class AdfMainFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(AdfMainFrame.class.getName());

    private static final Dimension FRAME_DEFAULT_MINIMUM_SIZE = new Dimension(600, 400);

    private static AdfMainFrame adfMainFrame;

    public static AdfMainFrame createMainFrame() {
        if (adfMainFrame != null) {
            logger.severe("Adf Main Frame instance already created !!!");
            return adfMainFrame;
        }
        return adfMainFrame = new AdfMainFrame();
    }

    protected static AdfMainFrame getInstance() {
        return adfMainFrame;
    }

    //
    //   I n s t a n c e
    //

    protected AdfMainFrame() {
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
    public void initMainFrame() {
        setTitle(System.getProperty(ConfigProperties.APP_TITLE_KEY) + " - " + AppManifestAttributes.getAppVersion());
    }

    /**
     * Called to set frame size based on provided occupancy percent
     * Valid argument value is: 0 < occupancyPercent < 1
     * @param occupancyPercent
     */
    public void initFrameSize(double occupancyPercent) {

        if (occupancyPercent <= 0) {
            occupancyPercent = 0.50;
        }
        if (occupancyPercent >= 1) {
            occupancyPercent = 0.90;
        }

        Toolkit tk = getToolkit();
        Dimension screenSize = tk.getScreenSize();

        int height = (int) (((float) screenSize.height) * occupancyPercent);
        int portion = height / 9;

        int finalWidth = (portion * 16);
        int finalHeight = portion * 9;

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

//       System.out.println( "curSize.width = " + curSize.width );
//       System.out.println( "curSize.height = " + curSize.height );
//       initTest();

        setSize(finalWidth, finalHeight);
        this.setMinimumSize(FRAME_DEFAULT_MINIMUM_SIZE);
    }
}
