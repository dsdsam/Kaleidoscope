package adf.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/2/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemUtilities {

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
}
