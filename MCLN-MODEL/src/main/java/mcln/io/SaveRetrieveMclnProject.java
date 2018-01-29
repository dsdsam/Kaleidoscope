package mcln.io;

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

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/2/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
class SaveRetrieveMclnProject {

    private static final Dimension FILE_CHOOSER_SIZE = new Dimension(1000, 350);

    //    private static final String SAVE_MODEL_CHOOSER_TITLE = "Save Model: Please Select File Name";
    private static final String RETRIEVE_MODEL_CHOOSER_TITLE = "Retrieve Project: Please Select File Name";

    private static final String MCLN_MODEL_XML_FILE_EXTENSION = "mcln";

    private static SaveRetrieveMclnProject saveRetrieveMclnProject;


    /**
     * @return
     */
    static synchronized SaveRetrieveMclnProject getInstance() {
        assert saveRetrieveMclnProject != null : "Attempt to get SaveRetrieveMclnProject instance before it is created";
        if (saveRetrieveMclnProject == null) {
            throw new RuntimeException("Attempt to get SaveRetrieveMclnProject instance before it is created");
        }
        return saveRetrieveMclnProject;
    }

    //
    //   M c l n   M o d e l   S t o r a g e   i n s t a n c e
    //

    private String initialProjectName;

    private String projectName;


    private String defaultAbsoluteModelStorageDirectory;

    private String lastAbsoluteModelStorageDirectory;


    String getProjectName() {
        return projectName;
    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    static File getOpenFilePath(Component parent, String fileChooserTitle, String initialDirectory,
                                FileNameExtensionFilter fileFilter) {
        return getFileName(parent, fileChooserTitle, initialDirectory, fileFilter, true);
    }

    /**
     * @param fileChooserTitle
     * @param initialDirectory
     * @param fileFilter
     * @return
     */
    static File getSaveFilePath(Component parent, String fileChooserTitle, String initialDirectory,
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
        fileChooser.setPreferredSize(FILE_CHOOSER_SIZE);
        fileChooser.setMinimumSize(FILE_CHOOSER_SIZE);
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
     *
     */
    public MclnProject openProject(Component parent) {
        System.out.println("DesignSpaceModel.openProject");
        String absoluteModelStorageDirectory = defaultAbsoluteModelStorageDirectory;
        if (lastAbsoluteModelStorageDirectory != null) {
            absoluteModelStorageDirectory = lastAbsoluteModelStorageDirectory;
        }
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("MCLN Model", "main/java/mcln");
        File selectedPathToMclnModelFile = getOpenFilePath(parent, RETRIEVE_MODEL_CHOOSER_TITLE,
                absoluteModelStorageDirectory, fileFilter);

        String fileName = "";
        String selectedAbsoluteModelStorageDirectory;
        if (selectedPathToMclnModelFile.isDirectory()) {
            // message
            return null;
        }

        lastAbsoluteModelStorageDirectory = selectedPathToMclnModelFile.getParent();
        String fileNameWithExtension = selectedPathToMclnModelFile.getName();
        int index = fileNameWithExtension.indexOf(".");
        if (index != -1) {
            fileNameWithExtension = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
        }
        projectName = fileNameWithExtension;

        System.out.println("DesignSpaceModel.openProject: opening from dir " +
                lastAbsoluteModelStorageDirectory + "\n  file " + projectName);

        Document mclnAsDocument = readProject(selectedPathToMclnModelFile);

        MclnProject mclnProject = MclnModelRetriever.createMclnProjectFromXmlDom(mclnAsDocument);
        String formattedXmlString = documentToFormattedString(mclnAsDocument);
        System.out.println("DesignSpaceModel.openProject: " + formattedXmlString);
        System.out.println();
        return mclnProject;
    }

    /**
     * @param selectedPathToMclnModelFile
     */
    private Document readProject(File selectedPathToMclnModelFile) {
        System.out.println("DesignSpaceModel.readProject: " + selectedPathToMclnModelFile);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document mclnAsDocument = documentBuilder.parse(selectedPathToMclnModelFile);
            return mclnAsDocument;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    static final String format(String unformattedXml) {
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
    static final String documentToFormattedString(Document xmlDocument) {
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


    private static void testModelStorage() {
        SaveRetrieveMclnProject modelStorage = new SaveRetrieveMclnProject();
        modelStorage.openProject(new JFrame());
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                testModelStorage();
//            }
//        });
//    }

}

