package dsdsse.designspace;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import mcln.model.MclnModelRetriever;
import mcln.model.MclnProject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/1/14
 * Time: 7:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectStorage {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String APPLICATION_INSTALLATION_DIRECTORY = System.getProperty("user.dir");

    private static final Dimension FILE_CHOOSER_SIZE = new Dimension(700, 350);

    private static final String SAVE_PROJECT_AS_CHOOSER_TITLE = "Save Project As: Please Select File Name";
    private static final String RETRIEVE_MODEL_CHOOSER_TITLE = "Retrieve Project: Please Select File Name";

    private static final String MCLN_MODEL_XML_FILE_EXTENSION = "mcln";

    private static final String TRACE_LOG_RELATIVE_DIR_PATH = FILE_SEPARATOR + "Mcln Trace Log Storage";

    private static ProjectStorage mclnModelStorage;

    /**
     * @param directoryName
     * @return
     */
    public static synchronized ProjectStorage createInstance(String directoryName) {
        mclnModelStorage = new ProjectStorage(directoryName);
        return mclnModelStorage;
    }

    /**
     * @return
     */
    public static synchronized ProjectStorage getInstance() {
        assert mclnModelStorage != null : "Attempt to get SaveRetrieveMclnProject instance before it is created";
        if (mclnModelStorage == null) {
            throw new RuntimeException("Attempt to get SaveRetrieveMclnProject instance before it is created");
        }
        return mclnModelStorage;
    }

    //
    //   M c l n   M o d e l   S t o r a g e   i n s t a n c e
    //

    private String defaultAbsoluteModelStorageDirectory;

    /**
     * @param directoryName
     */
    private ProjectStorage(String directoryName) {
        defaultAbsoluteModelStorageDirectory = APPLICATION_INSTALLATION_DIRECTORY + FILE_SEPARATOR + directoryName;
        File modelStorageDirectory = new File(defaultAbsoluteModelStorageDirectory);
        boolean exists = modelStorageDirectory.exists();
        if (!exists) {
            try {
                modelStorageDirectory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Reads Mcln Project XML. File name is selected via Open File Dialog.
     * Suggested file name in dialog set as MCLN project name.
     */
    MclnProject openMclnProject(Component parent) {
        MclnProject mclnProject = MclnProject.getInstance();
        String absoluteModelStorageDirectory = defaultAbsoluteModelStorageDirectory;
        String suggestedProjectFileName = "";
        if (mclnProject.wasProjectSavedOrRetrieved()) {
            absoluteModelStorageDirectory = mclnProject.getLastAbsoluteModelStorageDirectory();
            suggestedProjectFileName = mclnProject.getLastSavedOrRetrievedProjectFileName();
        }

        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("MCLN Project", MCLN_MODEL_XML_FILE_EXTENSION);
        // asking user to chose directory & select file name
        File selectedPathToMclnModelFile = getOpenFilePath(parent, RETRIEVE_MODEL_CHOOSER_TITLE,
                absoluteModelStorageDirectory, fileFilter, suggestedProjectFileName);

        String selectedAbsoluteModelStorageDirectory;
        if (selectedPathToMclnModelFile == null) {
            // the selection was cancelled
            return null;
        }
        if (selectedPathToMclnModelFile.isDirectory()) {
            // This case never happens because when user selects directory
            // the "select file dialog" switches goes into the directory
            selectedAbsoluteModelStorageDirectory = selectedPathToMclnModelFile.getAbsolutePath();
        } else {
            selectedAbsoluteModelStorageDirectory = selectedPathToMclnModelFile.getParent();
            String selectedFileName = selectedPathToMclnModelFile.getName();
            int index = selectedFileName.indexOf(".");
            if (index != -1) {
                selectedFileName = selectedFileName.substring(0, index);
            }
            suggestedProjectFileName = selectedFileName;
        }

        Document mclnAsDocument = MclnModelRetriever.readMclnProjectXml(selectedPathToMclnModelFile);

        mclnProject = MclnModelRetriever.createMclnProjectFromXmlDom(mclnAsDocument);

        mclnProject.setLastAbsoluteModelStorageDirectory(selectedAbsoluteModelStorageDirectory);
        mclnProject.setLastSavedOrRetrievedProjectFileName(suggestedProjectFileName);

//        String formattedXmlString = documentToFormattedString(mclnAsDocument);
//        System.out.println("DesignSpaceModel.openProject: " + formattedXmlString);
//        System.out.println();
        return mclnProject;
    }

    /**
     * Saves MCLN project into File named as project
     */
    boolean saveProject(Component parent, MclnProject mclnProject) {
        if (!mclnProject.wasProjectSavedOrRetrieved()) {
            return saveProjectAs(parent, mclnProject);
        }
        String absoluteModelStorageDirectory = mclnProject.getLastAbsoluteModelStorageDirectory();
        String suggestedProjectFileName = mclnProject.getLastSavedOrRetrievedProjectFileName();
        return saveProject(mclnProject, absoluteModelStorageDirectory, suggestedProjectFileName);
    }

    /**
     * Saves MCLN project. File name is selected via Open File Dialog
     * Suggested file name in dialog set as MCLN project name.
     */
    boolean saveProjectAs(Component parent, MclnProject mclnProject) {
        String absoluteModelStorageDirectory = defaultAbsoluteModelStorageDirectory;
        if (mclnProject.wasProjectSavedOrRetrieved()) {
            absoluteModelStorageDirectory = mclnProject.getLastAbsoluteModelStorageDirectory();
        }

        String suggestedProjectFileName = mclnProject.getLastSavedOrRetrievedProjectFileName();

        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("MCLN Model", MCLN_MODEL_XML_FILE_EXTENSION);
        // asking user to chose directory & provide file name
        File selectedPathToModelDirectoryOrFile = getSaveFilePathViaDialog(parent, SAVE_PROJECT_AS_CHOOSER_TITLE,
                absoluteModelStorageDirectory, fileFilter, suggestedProjectFileName);

        String selectedAbsoluteModelStorageDirectory;
        if (selectedPathToModelDirectoryOrFile == null) {
            // the selection was cancelled
            return false;
        }
        if (selectedPathToModelDirectoryOrFile.isDirectory()) {
            // This case never happens because when user selects directory
            // the "select file dialog" switches goes into the directory
            selectedAbsoluteModelStorageDirectory = selectedPathToModelDirectoryOrFile.getAbsolutePath();
        } else {
            selectedAbsoluteModelStorageDirectory = selectedPathToModelDirectoryOrFile.getParent();
            String selectedFileName = selectedPathToModelDirectoryOrFile.getName();
            int index = selectedFileName.indexOf(".");
            if (index != -1) {
                selectedFileName = selectedFileName.substring(0, index);
            }
            suggestedProjectFileName = selectedFileName;
        }
        mclnProject.setLastAbsoluteModelStorageDirectory(selectedAbsoluteModelStorageDirectory);
        mclnProject.setLastSavedOrRetrievedProjectFileName(suggestedProjectFileName);

        return saveProject(mclnProject, selectedAbsoluteModelStorageDirectory, suggestedProjectFileName);
    }

    /**
     * @param mclnProject
     * @param absoluteModelStorageDirectory
     * @param projectFileName
     */
    private boolean saveProject(MclnProject mclnProject, String absoluteModelStorageDirectory, String projectFileName) {

        File modelStorageDirectory = new File(absoluteModelStorageDirectory);
        boolean exists = modelStorageDirectory.exists();
        if (!exists) {
            try {
                modelStorageDirectory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        System.out.println("DesignSpaceModel.saveProject to location : save to dir " +
                absoluteModelStorageDirectory + "\n  file " + projectFileName);

        String projectXml = mclnProject.toXml();
        System.out.println("XML=" + projectXml);
        projectXml = format(projectXml);

        // project file name stored with standard extension
        String absolutePathToProjectFile = absoluteModelStorageDirectory + FILE_SEPARATOR +
                projectFileName + "." + MCLN_MODEL_XML_FILE_EXTENSION;
        File projectFile = new File(absolutePathToProjectFile);
        if (projectFile.exists()) {
            try {
                projectFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(absolutePathToProjectFile));
            printWriter.println(projectXml);
            mclnProject.resetTheProjectBackup();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    private static File getOpenFilePath(Component parent, String fileChooserTitle, String initialDirectory,
                                        FileNameExtensionFilter fileFilter, String suggestedProjectFileName) {
        return getFileNameViaDialog(parent, fileChooserTitle, initialDirectory, fileFilter, suggestedProjectFileName, true);
    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    private static File getSaveFilePathViaDialog(Component parent, String fileChooserTitle, String initialDirectory,
                                                 FileNameExtensionFilter fileFilter, String suggestedProjectFileName) {
        return getFileNameViaDialog(parent, fileChooserTitle, initialDirectory, fileFilter, suggestedProjectFileName, false);
    }

    /**
     * @param parent
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @param suggestedProjectFileName
     * @param openFilePath
     * @return
     */
    private static File getFileNameViaDialog(Component parent, String fileChooserTitle, String initialDirectory,
                                             FileNameExtensionFilter fileFilter, String suggestedProjectFileName,
                                             boolean openFilePath) {

        File currentDirectory = new File(initialDirectory);
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setPreferredSize(FILE_CHOOSER_SIZE);
        fileChooser.setDialogTitle(fileChooserTitle);

        //Add a custom file filter and disable the default
        if (fileFilter != null) {
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(fileFilter);
        }

        int returnVal;
        if (openFilePath) {
            File file;
            if (suggestedProjectFileName.isEmpty()) {
                file = new File(suggestedProjectFileName);
            } else {
                file = new File(suggestedProjectFileName + "." + MCLN_MODEL_XML_FILE_EXTENSION);
            }
            fileChooser.setSelectedFile(file);
            returnVal = fileChooser.showOpenDialog(parent);
        } else {
            fileChooser.setSelectedFile(new File(suggestedProjectFileName + "." + MCLN_MODEL_XML_FILE_EXTENSION));
            returnVal = fileChooser.showSaveDialog(parent);
        }
        //Process the result
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        currentDirectory = fileChooser.getSelectedFile();
        return currentDirectory;
    }

    public static final String format(String unformattedXml) {
        Document xmlDocument = xmlToDocument(unformattedXml);
        String formattedXmlString = documentToFormattedString(xmlDocument);
        return formattedXmlString;
    }

    /**
     * This function converts String XML to Document object
     *
     * @param xml
     * @return
     */
    private static final Document xmlToDocument(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            return db.parse(inputSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param xmlDocument
     * @return
     */
    public static final String documentToFormattedString(Document xmlDocument) {
        try {
            OutputFormat format = new OutputFormat(xmlDocument);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(xmlDocument);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean saveTraceLog(String modelName, String fileName, List<String> traceLogList) {

        String absoluteModelStorageDirectory = APPLICATION_INSTALLATION_DIRECTORY + TRACE_LOG_RELATIVE_DIR_PATH;
        File traceLogStorageDirectory = new File(absoluteModelStorageDirectory);
        boolean exists = traceLogStorageDirectory.exists();
        if (!exists) {
            try {
                traceLogStorageDirectory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

//        System.out.println("DesignSpaceModel.saveTraceLog to location : save to dir " +
//                absoluteModelStorageDirectory + "\n  file " + TRACE_LOG_FILE_NAME);
        String absolutePathToTraceLogFile = absoluteModelStorageDirectory + fileName;
        File traceLogFile = new File(absolutePathToTraceLogFile);
        if (traceLogFile.exists()) {
            try {
                traceLogFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(absolutePathToTraceLogFile));
            String modelNameHeader = "@MODEL-NAME : " + modelName;
            printWriter.println(modelNameHeader);
            for (String entry : traceLogList) {
                printWriter.println(entry);
            }
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }

    //
    // testing
    //

    private static void testModelStorage() {
        ProjectStorage modelStorage = new ProjectStorage("");
        modelStorage.openMclnProject(new JFrame());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                testModelStorage();
            }
        });
    }

}


