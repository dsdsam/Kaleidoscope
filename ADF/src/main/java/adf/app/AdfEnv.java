package adf.app;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Holds the application environment
 */
public class AdfEnv extends AppProperties {

    public static final Logger logger = Logger.getLogger(AdfEnv.class.getName());

    // UI Manager keys

    public static final String MAIN_PANEL_BACKGROUND = "app.ui.main.panel.background";
    public static final String SPLASH_WINDOW_KEY = "splash.window";

    public static String BASE_IMAGE_LOCATION = "/app_icons/";
    public static String APP_ICON_IMAGES = "/adf-resources/images/";

    public static final char TYPE_MESSAGE = 'M';
    public static final char TYPE_WARNING = 'W';

    private static final String MAIN_FRAME_KEY = "main.frame";

    //
    //   M a i n   C o m p o n e n t s   A c c e s s
    //

    public static void putMainFrame(Object obj) {
        put(MAIN_FRAME_KEY, obj);
    }

    public static JFrame getMainFrame() {
        Object value = get(MAIN_FRAME_KEY);
        if (!(value instanceof JFrame)) {
            throw new RuntimeException("Main Frame is is not a JFrame");
        }
        return (JFrame) value;
    }


    // ============================================================
    //  M e s s a g e     s u p p o r t
    // ------------------------------------------------------------

    public static final boolean yesNoDlg(Component parentComponent, String header, String mess) {
        int n = JOptionPane.showConfirmDialog(parentComponent, mess, header,
                JOptionPane.YES_NO_OPTION);
        return (n == 0);
    }

    /**
     *
     */
    private static void setCompFontSize(java.awt.Component comp, int style, int fontSize) {
        int fStyle = (style == -1) ? Font.PLAIN : style;
        int fSize = (fontSize == -1) ? 12 : fontSize;
        comp.setFont(new Font(comp.getFont().getName(), fStyle, fSize));
    }


    /**
     *
     */
    public static JButton makeButton(JButton button, ImageIcon icon,
                                     String text, String command,
                                     String tipText, Dimension size,
                                     Insets shrinkWrap,
                                     int style, int fontSize,
                                     ActionListener actionListener) {
        if (button == null) {
            button = new JButton();
        }
        if (icon != null) {
            button.setIcon(icon);
        }
        if (text != null) {
            button.setText(text);
        }
        if (command != null) {
            button.setActionCommand(command);
        }
        if (tipText != null) {
            button.setToolTipText(tipText);
        }
        if (size != null) {
            button.setPreferredSize(size);
            //      button.setMaximumSize( size );
            //      button.setMaximumSize( size );
        }

        button.setMinimumSize(new Dimension(0, 0));
        //    button.setMaximumSize( new Dimension( 0,0 ) );

        if (shrinkWrap != null) {
            button.setMargin(shrinkWrap);
        }
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }

        setCompFontSize(button, style, fontSize);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.TOP);
        return button;
    }

    /**
     *
     */

    public static ImageIcon getImageIconOld(String fileName) {
        URL url;
        ImageIcon imageIcon;

        url = ClassLoader.getSystemResource("app_icons/" + fileName);
        if (url != null) {
            //      System.out.println("Not on the page URL= "+url.toString());
            //      System.out.println("Not on the pge URL= "+url.toExternalForm());
            imageIcon = new ImageIcon(url);
        } else {
//        System.out.println(
//        "AdfEnv.getImageIcon: Not on the page URL for a file "+
//        "\""+fileName+"\" is null");
            String iconPath = INSTALLATION_DIRECTORY + FILE_SEPARATOR + "app_icons" + FILE_SEPARATOR + fileName;
//       System.out.println(">>"+iconPath);
//        appendToErrorLogFile(
//        "AdfEnv.getImageIcon: Not on the page URL for a file "+
//        "\""+fileName+"\" is null");

            return new ImageIcon(iconPath);
        }

        return imageIcon;
    }

    /**
     *
     */
    public static void waitKbdHit() {
        BufferedReader kbdInp;

        System.out.println("Hit kbd to continue.");
        InputStreamReader inpStreamReader =
                new InputStreamReader(System.in);
        try {
            inpStreamReader.read();
        } catch (IOException e) {
            System.out.println("Error reading Kbd");
        }

    }


    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    public static File getOpenFilePath(Component parent, String fileChooserTitle, String initialDirectory,
                                       FileNameExtensionFilter fileFilter) {
        return getFileName(parent, fileChooserTitle, initialDirectory, fileFilter, true);
    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    public static File getSaveFilePath(Component parent, String fileChooserTitle, String initialDirectory,
                                       FileNameExtensionFilter fileFilter) {
        return getFileName(parent, fileChooserTitle, initialDirectory, fileFilter, false);
    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @param openFilePath
     * @return
     */
    private static File getFileName(Component parent, String fileChooserTitle, String initialDirectory,
                                    FileNameExtensionFilter fileFilter, boolean openFilePath) {

        File currentDirectory = new File(initialDirectory);
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setDialogTitle(fileChooserTitle);

        //Add a custom file filter and disable the default
        if (fileFilter != null) {
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(fileFilter);
        }

        int returnVal;
        if (openFilePath) {
            returnVal = fileChooser.showOpenDialog(parent);
        } else {
            returnVal = fileChooser.showSaveDialog(parent);
        }
        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = fileChooser.getSelectedFile();
        }
        return currentDirectory;
    }


    /**
     * @param fontName
     * @return
     */
    public static final boolean loadAndRegisterFont(String fontName) {
        // family Times New Roman
        // family Calibri
        try {
            InputStream fontInputStream = AdfEnv.class.getResourceAsStream("/fonts-ttf/" + fontName);
            Font myCreatedFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
//            myCreatedFont = myCreatedFont.deriveFont(18f);
            String name = myCreatedFont.getFontName();
            String createdFontName = myCreatedFont.getFontName();
            String family = myCreatedFont.getFamily();
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(myCreatedFont);
            return true;
        } catch (FontFormatException ffe) {

        } catch (IOException ioe) {

        }
        return false;
    }
}