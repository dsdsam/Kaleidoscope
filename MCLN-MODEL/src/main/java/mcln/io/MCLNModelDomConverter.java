package mcln.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/3/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */

class MCLNModelDomConverter {

    /**
     *
     */
    static void modelToDOMRepresentation() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {

        }
        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Model");
        doc.appendChild(rootElement);

        // staff elements
//        int cellListSize = mclnModel.getCellListSize();
//        for (int i = 0; i < cellListSize; i++) {
//            DsdsseCell dsdsseCell = mclnModel.getCell(i);
//            Element cellElement = doc.createElement("DsdsseCell");
//            rootElement.appendChild(cellElement);
//
//            Element cellName = doc.createElement("cellName");
//            cellName.appendChild(doc.createTextNode(dsdsseCell.getSubject()));
//            cellElement.appendChild(cellName);
//        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException tce) {

        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(".\\Saved Models\\M C L N - Model.xml"));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        StreamResult result = new StreamResult(byteArrayOutputStream);


        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);
        try {
            transformer.transform(source, streamResult);
        } catch (TransformerException te) {

        }

//        String unformattedXML = byteArrayOutputStream.toString();
//        String formattedXML = prettyFormat0(unformattedXML, 2);

//        System.out.println(formattedXML);

        System.out.println("File saved!");
    }

    /**
     * @param input
     * @param indent
     * @return
     */
    static String prettyFormat0(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    /**
     * @param input
     * @param indent
     * @return
     */
    static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Throwable e) {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            } catch (Throwable t) {
                return input;
            }
        }
    }

//    public static void main(String[] args) {
//        MclnModel model = new MclnModel("M C L N  Model");
//        DsdsseCell dsdsseCell;
//        dsdsseCell = DsdsseCell.createInstance("C-01");
//        model.addCell(dsdsseCell);
//        dsdsseCell = DsdsseCell.createInstance("C-02");
//        model.addCell(dsdsseCell);
//        dsdsseCell = DsdsseCell.createInstance("C-03");
//        model.addCell(dsdsseCell);
//
//        MCLNModelDomConverter.modelToDOMRepresentation(model);
//    }
}

