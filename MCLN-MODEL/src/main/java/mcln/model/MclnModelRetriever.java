package mcln.model;

import mcln.palette.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 8/30/13
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnModelRetriever {

    /**
     * Called when Mcln Statement is being created from XML with Input Simulating Program
     *
     * @param uid
     * @return
     */
    private static final MclnStatement createMclnStatement(String uid, String subject, String propertyName,
                                                           boolean hasInputGenerator,
                                                           AvailableMclnStatementStates availableMclnStatementStates,
                                                           double[] cSysLocation,
                                                           MclnStatementState initialMclnStatementState,
                                                           InputSimulatingProgram inputSimulatingProgram) {
        MclnStatement mclnStatement = new MclnStatement(uid, subject, propertyName, hasInputGenerator,
                availableMclnStatementStates, cSysLocation, initialMclnStatementState, inputSimulatingProgram);
        return mclnStatement;
    }

    //
    //  C o n d i t i o n s
    //

    /**
     * Called when Mcln Condition is being created from XML
     *
     * @param uid
     * @return
     */
    private static synchronized final MclnCondition createMclnCondition(String uid) {
        MclnCondition mclnCondition = new MclnCondition(uid);
        return mclnCondition;
    }

    //
    //   A r c
    //

    /**
     * Called when Mcln Arc is being created from XML
     *
     * @param uid
     * @return
     */
    private static synchronized final <InpNodeType, OutNodeType> MclnArc createMclnArc(
            ArrowTipLocationPolicy arrowTipLocationPolicy, String uid, List<double[]> knotCSysLocations,
            MclnState arcMclnState, InpNodeType inpNode, OutNodeType outNode) {

        MclnArc<InpNodeType, OutNodeType> mclnArc = new MclnArc(arrowTipLocationPolicy, uid, knotCSysLocations,
                arcMclnState, inpNode, outNode);

        return mclnArc;
    }


    //
    //   M c l n   M o d e l   R e t r i e v a l
    //

    public static Document readMclnProjectXml(File selectedPathToMclnModelFile) {
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

    public static Document readMclnProjectXml(InputStream inputStream) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document mclnAsDocument = documentBuilder.parse(inputStream);
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

    /**
     * @param mclnAsXmlDocument
     * @return
     */
    public static MclnProject createMclnProjectFromXmlDom(Document mclnAsXmlDocument) {

        Map<String, MclnStatement> statementIdToMclnStatementMap = new HashMap();
        Map<String, MclnCondition> conditionIdToMclnConditionMap = new HashMap();

        DOMConfiguration docConfig = mclnAsXmlDocument.getDomConfig();
        docConfig.setParameter("infoset", Boolean.TRUE);

        mclnAsXmlDocument.normalizeDocument();
//        List<MclnModel> mclnModelList = processXmlDocument(mclnAsDocument);
//        MclnModel mclnModel = mclnModelList.get(0);
//        System.out.println("Recreated MclnModel:\n" + mclnModel.toXml());
//        System.out.println();
//    }
//
//    private static List<MclnModel> processXmlDocument(Document doc) {

        MclnStatement currentMclnStatement = null;
        mclnAsXmlDocument.getDocumentElement().normalize();
        Element rootElement = mclnAsXmlDocument.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        System.out.println("Root name " + rootNodeName);


        String PROJECT_TAG_NAME = "Mcln-Project";

        NodeList listOfMclnProjects = mclnAsXmlDocument.getElementsByTagName(PROJECT_TAG_NAME);
        int nOfProjects = listOfMclnProjects.getLength();
        System.out.println("Information on all Mcln projects, number is " + nOfProjects);

        if (nOfProjects != 1) {
            return null;
        }

        Node projectNode = listOfMclnProjects.item(0);
        NamedNodeMap namedNodeMap = projectNode.getAttributes();
        Node projectNameNode = namedNodeMap.getNamedItem("project-name");
        String projectName = projectNameNode.getNodeValue();


        String MODEL_TAG_NAME = "Mcln-Model";

        NodeList listOfMclnModels = mclnAsXmlDocument.getElementsByTagName(MODEL_TAG_NAME);
        int nOfModels = listOfMclnModels.getLength();
        System.out.println("Information on all Mcln Models, number is " + nOfModels);

        List<MclnModel> mclnModelList = new ArrayList();

        MclnModel mclnModel = null;
        for (int i = 0; i < nOfModels; i++) {
            Node node = listOfMclnModels.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element mclnModelElement = (Element) node;


                List<String[]> strRectangle = getXYLocationList(mclnModelElement,
                        MclnModel.MCLN_MODEL_RECTANGLE_TAG, MclnModel.MCLN_MODEL_RECTANGLE_CORNER_TAG);
                if (strRectangle == null) {
                    return null;
                }

                int currentCorner = 0;
                double[][] outline = new double[2][];
                for (String[] strXYLocation : strRectangle) {
                    double[] xyLocation = stringCSysLocationToDouble(strXYLocation[0], strXYLocation[1]);
                    outline[currentCorner++] = xyLocation;
                }

                mclnModel = MclnModel.createInstance(mclnModelElement.getTagName(), "M-0001",
                        outline[0][0], outline[0][1], outline[1][0], outline[1][1]);

//                mclnModel.setCSysRectangle(outline);

                //
                //  C r e a t i n g   M c l n   E l e m e n t s
                //

                NodeList listOfMclnStatements =
                        mclnModelElement.getElementsByTagName(MclnStatement.MCLN_STATEMENT_XML_TAG);
                int nOfStatements = listOfMclnStatements.getLength();
                System.out.println("Information on all Mcln Statements, number is " + nOfStatements);
                for (int j = 0; j < nOfStatements; j++) {
                    Element mclnStatementElement = (Element) listOfMclnStatements.item(j);
                    MclnStatement mclnStatement = domElementToMclnStatement(mclnStatementElement);
                    System.out.println("Reconstructed  Mcln Statements, uid is " + mclnStatement.getUID());
                    System.out.println("Reconstructed  Mcln Statements, xml: \n" + mclnStatement.toXml());
                    System.out.println();
                    mclnModel.addMclnStatement(mclnStatement);
                    statementIdToMclnStatementMap.put(mclnStatement.getUID(), mclnStatement);
                }

                NodeList listOfMclnConditions =
                        mclnModelElement.getElementsByTagName(MclnCondition.MCLN_CONDITION_XML_TAG);
                int nOfConditions = listOfMclnConditions.getLength();
                System.out.println("Information on all Mcln Conditions, number is " + nOfStatements);
                for (int j = 0; j < nOfConditions; j++) {
                    Element mclnConditionElement = (Element) listOfMclnConditions.item(j);
                    MclnCondition mclnCondition = domElementToMclnCondition(mclnConditionElement);
                    System.out.println("Reconstructed  Mcln Condition, uid is " + mclnCondition.getUID());
                    System.out.println("Reconstructed  Mcln Condition, xml: \n" + mclnCondition.toXml());
                    System.out.println();
                    mclnModel.addMclnCondition(mclnCondition);
                    conditionIdToMclnConditionMap.put(mclnCondition.getUID(), mclnCondition);
                }

                NodeList listOfMclnArcs = mclnModelElement.getElementsByTagName(MclnArc.MCLN_ARC_XML_TAG);
                int nOfArcs = listOfMclnArcs.getLength();
                System.out.println("Information on all Mcln Arcs, number is " + nOfArcs);
                for (int j = 0; j < nOfArcs; j++) {
                    Element mclnArcElement = (Element) listOfMclnArcs.item(j);
                    MclnArc mclnArc = domElementToMclnArc(mclnArcElement, statementIdToMclnStatementMap,
                            conditionIdToMclnConditionMap);
                    System.out.println("Reconstructed  Mcln Arc, uid is " + mclnArc.getUID());
                    System.out.println("Reconstructed  Mcln Arc, xml: \n" + mclnArc.toXml());
                    System.out.println();
                    mclnModel.addMclnArc(mclnArc);
                }

                mclnModelList.add(mclnModel);
            }

        }

        //
        // Creating and populating project
        //

        MclnProject mclnProject = MclnProject.createRetrievedMclnProject(projectName, mclnModel);

        return mclnProject;
    }

    /**
     * @param mclnModelAsXmlDocument
     * @return
     */
//    public static MclnModel buildMclnModelFromXmlDom(String sourceXml, Document mclnModelAsXmlDocument) {
//
//        Map<String, MclnStatement> nodeIdToMclnStatementMap = new HashMap();
//        Map<String, MclnCondition> nodeIdToMclnConditionMap = new HashMap();
//
//        System.out.println("MclnModelFactory: buildMclnModel ");
//
//        DOMConfiguration docConfig = mclnModelAsXmlDocument.getDomConfig();
//        docConfig.setParameter("infoset", Boolean.TRUE);
//
//        mclnModelAsXmlDocument.normalizeDocument();
//
//        mclnModelAsXmlDocument.getDocumentElement().normalize();
//        Element rootElement = mclnModelAsXmlDocument.getDocumentElement();
//        String rootNodeName = rootElement.getNodeName();
//        System.out.println("Root name " + rootNodeName);
//
//        String MODEL_TAG_NAME = "Mcln-Model";
//
//        NodeList listOfMclnModels = mclnModelAsXmlDocument.getElementsByTagName(MODEL_TAG_NAME);
//        int nOfModels = listOfMclnModels.getLength();
//        System.out.println("Information on all Mcln Models, number is " + nOfModels);
//
//        Node node = listOfMclnModels.item(0);
//        Element mclnModelElement = (Element) node;
//
//
//        List<String[]> strRectangle = getXYLocationList(mclnModelElement,
//                MclnModel.MCLN_MODEL_RECTANGLE_TAG, MclnModel.MCLN_MODEL_RECTANGLE_CORNER_TAG);
//        if (strRectangle == null) {
//            return null;
//        }
//
//        int currentCorner = 0;
//        double[][] outline = new double[2][];
//        for (String[] strXYLocation : strRectangle) {
//            double[] xyLocation = stringCSysLocationToDouble(strXYLocation[0], strXYLocation[1]);
//            outline[currentCorner++] = xyLocation;
//        }
//        MclnModel mclnModel = MclnModel.createInstance(mclnModelElement.getTagName(), "M-0001",
//                outline[0][0], outline[0][1], outline[1][0], outline[1][1]);
////        MclnModel mclnModel = new MclnModel(sourceXml, mclnModelElement.getTagName(), "M-0001",
////                outline[0][0], outline[0][1], outline[1][0], outline[1][1]);
//
//
//        //
//        //  C r e a t i n g   M c l n   E l e m e n t s
//        //
//
//        NodeList listOfMclnStatements =
//                mclnModelElement.getElementsByTagName(MclnStatement.MCLN_STATEMENT_XML_TAG);
//        int nOfStatements = listOfMclnStatements.getLength();
//        System.out.println("Information on all Mcln Statements, number is " + nOfStatements);
//        for (int j = 0; j < nOfStatements; j++) {
//            Element mclnStatementElement = (Element) listOfMclnStatements.item(j);
//            MclnStatement mclnStatement = domElementToMclnStatement(mclnStatementElement);
//            System.out.println("Reconstructed  Mcln Statements, uid is " + mclnStatement.getStatementUID());
//            System.out.println("Reconstructed  Mcln Statements, xml: \n" + mclnStatement.toXml());
//            System.out.println();
//            mclnModel.addMclnStatement(mclnStatement);
//            nodeIdToMclnStatementMap.put(mclnStatement.getUID(), mclnStatement);
//        }
//
//        NodeList listOfMclnConditions =
//                mclnModelElement.getElementsByTagName(MclnCondition.MCLN_CONDITION_XML_TAG);
//        int nOfConditions = listOfMclnConditions.getLength();
//        System.out.println("Information on all Mcln Conditions, number is " + nOfStatements);
//        for (int j = 0; j < nOfConditions; j++) {
//            Element mclnConditionElement = (Element) listOfMclnConditions.item(j);
//            MclnCondition mclnCondition = domElementToMclnCondition(mclnConditionElement);
//            System.out.println("Reconstructed  Mcln Condition, uid is " + mclnCondition.getConditionID());
//            System.out.println("Reconstructed  Mcln Condition, xml: \n" + mclnCondition.toXml());
//            System.out.println();
//            mclnModel.addMclnCondition(mclnCondition);
//            nodeIdToMclnConditionMap.put(mclnCondition.getUID(), mclnCondition);
//        }
//
//        NodeList listOfMclnArcs = mclnModelElement.getElementsByTagName(MclnArc.MCLN_ARC_XML_TAG);
//        int nOfArcs = listOfMclnArcs.getLength();
//        System.out.println("Information on all Mcln Arcs, number is " + nOfArcs);
//        for (int j = 0; j < nOfArcs; j++) {
//            Element mclnArcElement = (Element) listOfMclnArcs.item(j);
//            MclnArc mclnArc = domElementToMclnArc(mclnArcElement, nodeIdToMclnStatementMap, nodeIdToMclnConditionMap);
//            System.out.println("Reconstructed  Mcln Arc, uid is " + mclnArc.getUID());
//            System.out.println("Reconstructed  Mcln Arc, xml: \n" + mclnArc.toXml());
//            System.out.println();
//            mclnModel.addMclnArc(mclnArc);
//        }
//
//        return mclnModel;
//    }

    /**
     * @param mclnStatementElement
     * @return
     */
    private static MclnStatement domElementToMclnStatement(Element mclnStatementElement) {

//        NodeList statementChildNodes = mclnStatementElement.getChildNodes();
        NamedNodeMap namedNodeMap = mclnStatementElement.getAttributes();
        Node mclnStatesPaletteNameNode = namedNodeMap.getNamedItem(MclnStatement.MCLN_PALETTE_ATTRIBUTE_NAME);
        String mclnStatesPaletteName = mclnStatesPaletteNameNode.getNodeValue();
        mclnStatesPaletteName = mclnStatesPaletteName != null ? mclnStatesPaletteName : "";

        boolean hasInputGenerator = false;
        Node mclnStateHasInputGeneratorNode = namedNodeMap.getNamedItem(MclnStatement.MCLN_GENERATOR_ATTRIBUTE_NAME);
        if (mclnStateHasInputGeneratorNode != null) {
            String mclnStatementHasInputGenerator = mclnStateHasInputGeneratorNode.getNodeValue();
            hasInputGenerator = mclnStatementHasInputGenerator != null ?
                    stringToBoolean(mclnStatementHasInputGenerator) : false;
        }
        String uid = getElementProperty(mclnStatementElement, MclnStatement.MCLN_STATEMENT_UID_TAG);
//        MclnState mclnState = ThreeShadesConfettiPalette.MCLN_CREATION_STATE;
        System.out.println("Statement ID is: " + uid);
        if (uid == null) {
            return null;
        }

        String subject = getElementProperty(mclnStatementElement, MclnStatement.MCLN_STATEMENT_SUBJECT_TAG);
        System.out.println("Statement subject is: " + subject);
        if (subject == null) {
            subject = "Not defined";
        }

        String propertyName = getElementProperty(mclnStatementElement, MclnStatement.MCLN_STATEMENT_PROPERTY_NAME_TAG);
        System.out.println("Statement property name is: " + propertyName);
        if (propertyName == null) {
            propertyName = "";
        }

        String xLocation = getElementProperty(mclnStatementElement, "X-Location");
        System.out.println("Statement X location is: " + xLocation);
        if (xLocation == null) {
            return null;
        }
        String yLocation = getElementProperty(mclnStatementElement, "Y-Location");
        System.out.println("Statement Y location is: " + yLocation);
        if (yLocation == null) {
            return null;
        }

        double[] cSysLocation = stringCSysLocationToDouble(xLocation, yLocation);
        if (cSysLocation == null) {
            return null;
        }

        MclnStatementState defaultInitialMclnStatementState =
                MclnStatementState.createMclnStatementState(MclnStatePalette.MCLN_CREATION_STATE,
                        MclnStatePalette.MCLN_CREATION_STATE.getColorName());

        MclnStatementState initialMclnStatementState = getInitialStatementState(mclnStatementElement,
                defaultInitialMclnStatementState,
                MclnStatement.MCLN_STATEMENT_INITIAL_STATE_TAG, MclnStatementState.MCLN_STATEMENT_STATE_TAG,
                MclnStatementState.MCLN_STATE_TAG, MclnStatementState.INTERPRETATION_TAG);

        AvailableMclnStatementStates availableMclnStatementStates =
                recreateAvailableStatementStatesFromXml(mclnStatementElement, mclnStatesPaletteName,
                        initialMclnStatementState, AvailableMclnStatementStates.AVAILABLE_STATEMENT_STATES_TAG,
                        MclnStatementState.MCLN_STATEMENT_STATE_TAG, MclnStatementState.MCLN_STATE_TAG,
                        MclnStatementState.INTERPRETATION_TAG);

        System.out.println("availableStatesPalette size: " + availableMclnStatementStates.size());

        String simulationLog = getElementProperty(mclnStatementElement, MclnStatement.MCLN_STATEMENT_SIMULATION_LOG_TAG);
        System.out.println("Statement simulation log: " + simulationLog);

        InputSimulatingProgram inputSimulatingProgram = getInputSimulatingProgram(mclnStatementElement,
                InputSimulatingProgram.MCLN_INPUT_SIMULATING_PROGRAM_TAG, ProgramStep.MCLN_PROGRAM_STEP_TAG,
                availableMclnStatementStates);

        MclnStatement mclnStatement = MclnModelRetriever.createMclnStatement(uid, subject, propertyName,
                hasInputGenerator, availableMclnStatementStates, cSysLocation, initialMclnStatementState,
                inputSimulatingProgram);

        // this value is optional
        if (simulationLog != null && (simulationLog.equals("true") || simulationLog.equals("false"))) {
            mclnStatement.setSimulationLog(simulationLog.equals("true") ? true : false);
        }

//        mclnStatement.setSubject(subject);
        System.out.println();
//        NodeList csysLocation = mclnStatementElement.getElementsByTagName("CSysLocation");
//        Element idElement = (Element) idElementLst.item(0);
//        NodeList idChildNodeList = idElement.getChildNodes();
//        String id =   idChildNodeList.item(0).getNodeValue();
//        System.out.println("Statement ID is: " + id);
        return mclnStatement;
    }

    /**
     * @param mclnElement
     * @param availableStatementStatesTag
     * @param availableStatementStateTag
     * @param mclnStateTag
     * @param interpretationTag
     * @return
     */
    private static AvailableMclnStatementStates recreateAvailableStatementStatesFromXml(
            Element mclnElement, String mclnStatesPaletteName, MclnStatementState initialMclnStatementState,
            String availableStatementStatesTag, String availableStatementStateTag, String mclnStateTag,
            String interpretationTag) {

        AvailableMclnStatementStates emptyAvailableMclnStatementStates = new AvailableMclnStatementStates(
                mclnStatesPaletteName, initialMclnStatementState, new LinkedHashMap<>());

        if (!mclnElement.hasChildNodes()) {
            return emptyAvailableMclnStatementStates;
        }

        NodeList availableStatementStateElementList = mclnElement.getElementsByTagName(availableStatementStatesTag);
        if (availableStatementStateElementList == null || availableStatementStateElementList.getLength() < 1) {
            return emptyAvailableMclnStatementStates;
        }

        Element availableStatementStatesElement = (Element) availableStatementStateElementList.item(0);
        NodeList availableStatementStateChildNodeList = availableStatementStatesElement.getElementsByTagName(availableStatementStateTag);
        if (availableStatementStateChildNodeList == null || availableStatementStateChildNodeList.getLength() < 1) {
            return emptyAvailableMclnStatementStates;
        }

        int nStates = availableStatementStateChildNodeList.getLength();

        System.out.println("Information on Statement all states:, number is " + nStates);

        List<String[]> oppositeStatePropertyList = new ArrayList();
        List<String> interpretationList = new ArrayList();
        Map<Integer, MclnStatementState> availableMclnStatementStatesMap = new LinkedHashMap<>();
        for (int i = 0; i < nStates; i++) {
            Element availableStatementStateElement = (Element) availableStatementStateChildNodeList.item(i);

            // parsing interpretation
            NodeList interpretationElementChildList = availableStatementStateElement.getElementsByTagName(interpretationTag);
            if (interpretationElementChildList == null || interpretationElementChildList.getLength() < 1) {
                return emptyAvailableMclnStatementStates;
            }
            String interpretation = "No interpretation";
            Element interpretationElement = (Element) interpretationElementChildList.item(0);
            Node node = interpretationElement.getFirstChild();
            if (node != null) {
                interpretation = node.getNodeValue();
            }

            // parsing MclnStates
            NodeList statementStateElementChildList = availableStatementStateElement.getElementsByTagName(mclnStateTag);
            if (statementStateElementChildList == null || statementStateElementChildList.getLength() < 1) {
                return emptyAvailableMclnStatementStates;
            }
            Element mclnStateElement = (Element) statementStateElementChildList.item(0);
            node = mclnStateElement.getFirstChild();
            String stateProperties = node.getNodeValue();
            if (stateProperties != null) {
                String[] statePropertyArray = stateProperties.split(",");
                int state = stringToInt((statePropertyArray[1] != null) ? statePropertyArray[1].trim() : "");
                if (state > 0) {
                    String meaning = (statePropertyArray[0] != null) ? statePropertyArray[0].trim() : "";
                    String hexColor = (statePropertyArray[2] != null) ? statePropertyArray[2].trim() : "0x000000";
                    MclnState mclnState = MclnState.createState(meaning, state, hexColor);
                    MclnStatementState mclnStatementState = MclnStatementState.createMclnStatementState(mclnState, interpretation);
                    availableMclnStatementStatesMap.put(mclnState.getStateID(), mclnStatementState);
                } else {
                    oppositeStatePropertyList.add(statePropertyArray);
                    interpretationList.add(interpretation);
                }
            }
        }

        // processing opposite states
        int nOppositeStates = oppositeStatePropertyList.size();
        for (int i = 0; i < nOppositeStates; i++) {
            String[] oppositeStatePropertyArray = oppositeStatePropertyList.get(i);
            String meaning = (oppositeStatePropertyArray[0] != null) ? oppositeStatePropertyArray[0].trim() : "";
            int oppositeState = stringToInt((oppositeStatePropertyArray[1] != null) ? oppositeStatePropertyArray[1].trim() : "");
            String color = (oppositeStatePropertyArray[2] != null) ? oppositeStatePropertyArray[2].trim() : "0x000000";

            MclnState oppositeMclnState;
            MclnStatementState oppositeMclnStatementState;
            String interpretation = interpretationList.get(i);

            // getting state that is opposite to opposite
            int state = -oppositeState;
            MclnStatementState mclnStatementState = availableMclnStatementStatesMap.get(state);
            if (mclnStatementState != null) {
                oppositeMclnState = MclnState.createState(mclnStatementState.getMclnState(), meaning, oppositeState, color);
                oppositeMclnStatementState = MclnStatementState.createMclnStatementState(mclnStatementState,
                        oppositeMclnState, interpretation);
            } else {
                oppositeMclnState = MclnState.createState(meaning, state, color);
                oppositeMclnStatementState = MclnStatementState.createMclnStatementState(oppositeMclnState, interpretation);
                new Exception("Negative state without positive state " + oppositeMclnState.toString()).printStackTrace();
            }
            availableMclnStatementStatesMap.put(oppositeState, oppositeMclnStatementState);
        }
        AvailableMclnStatementStates availableMclnStatementStates = new AvailableMclnStatementStates(
                mclnStatesPaletteName, initialMclnStatementState, availableMclnStatementStatesMap);
        return availableMclnStatementStates;
    }

    /**
     * @param mclnElement
     * @param availableStatementStatesTag
     * @param availableStatementStateTag
     * @param mclnStateTag
     * @param interpretationTag
     * @return
     */
    private static MclnStatementState getInitialStatementState(Element mclnElement,
                                                               MclnStatementState defaultMclnStatementState,
                                                               String availableStatementStatesTag,
                                                               String availableStatementStateTag,
                                                               String mclnStateTag,
                                                               String interpretationTag) {


        MclnStatementState initialMclnStatementState = defaultMclnStatementState;

        if (!mclnElement.hasChildNodes()) {
            return initialMclnStatementState;
        }
        NodeList initialStatementStateElementList = mclnElement.getElementsByTagName(availableStatementStatesTag);
        if (initialStatementStateElementList == null || initialStatementStateElementList.getLength() < 1) {
            return initialMclnStatementState;
        }
        Element initialStatementStatesElement = (Element) initialStatementStateElementList.item(0);
        NodeList initialStatementStateChildNodeList = initialStatementStatesElement.getElementsByTagName(availableStatementStateTag);
        if (initialStatementStateChildNodeList == null || initialStatementStateChildNodeList.getLength() < 1) {
            return initialMclnStatementState;
        }
        int nStates = initialStatementStateChildNodeList.getLength();
        System.out.println("Information on Statement all initial states:, number is " + nStates);

        List<String[]> oppositeStatePropertyList = new ArrayList();
        List<String> interpretationList = new ArrayList();
        for (int i = 0; i < nStates; i++) {
            Element initialStatementStateElement = (Element) initialStatementStateChildNodeList.item(i);

            // parsing interpretation
            NodeList interpretationElementChildList = initialStatementStateElement.getElementsByTagName(interpretationTag);
            if (interpretationElementChildList == null || interpretationElementChildList.getLength() < 1) {
                return initialMclnStatementState;
            }
            String interpretation = "No interpretation";
            Element interpretationElement = (Element) interpretationElementChildList.item(0);
            Node node = interpretationElement.getFirstChild();
            if (node != null) {
                interpretation = node.getNodeValue();
            }

            // parsing MclnStates
            NodeList statementStateElementChildList = initialStatementStateElement.getElementsByTagName(mclnStateTag);
            if (statementStateElementChildList == null || statementStateElementChildList.getLength() < 1) {
                return initialMclnStatementState;
            }
            Element mclnStateElement = (Element) statementStateElementChildList.item(0);
            node = mclnStateElement.getFirstChild();
            String stateProperties = node.getNodeValue();
            if (stateProperties != null) {
                String[] statePropertyArray = stateProperties.split(",");
                int state = stringToInt((statePropertyArray[1] != null) ? statePropertyArray[1].trim() : "");
                if (state > 0) {
                    String meaning = (statePropertyArray[0] != null) ? statePropertyArray[0].trim() : "";
                    String color = (statePropertyArray[2] != null) ? statePropertyArray[2].trim() : "0x000000";
                    MclnState mclnState = MclnState.createState(meaning, state, color);
                    MclnStatementState mclnStatementState = MclnStatementState.createMclnStatementState(mclnState, interpretation);
                    initialMclnStatementState = mclnStatementState;
                } else {
                    oppositeStatePropertyList.add(statePropertyArray);
                    interpretationList.add(interpretation);
                }
            }
        }

        // processing opposite states
        int nOppositeStates = oppositeStatePropertyList.size();
//        for (int i = 0; i < nOppositeStates; i++) {
//            String[] statePropertyArray = oppositeStatePropertyList.get(i);
//            String meaning = (statePropertyArray[0] != null) ? statePropertyArray[0].trim() : "";
//            int state = stringToInt((statePropertyArray[1] != null) ? statePropertyArray[1].trim() : "");
//            String color = (statePropertyArray[2] != null) ? statePropertyArray[2].trim() : "0x000000";
//            int oppositeState = -state;
//            MclnStatementState oppositeMclnStatementState = availableMclnStatementStatesMap.get(oppositeState);
//            MclnState mclnState;
//            if (oppositeMclnStatementState != null) {
//                mclnState = MclnState.createState(oppositeMclnStatementState.getMclnState(), meaning, state, color);
//            } else {
//                mclnState = MclnState.createState(meaning, state, color);
//                new Exception("Negative state without positive state " + mclnState.toString()).printStackTrace();
//            }
//
//            MclnStatementState mclnStatementState = MclnStatementState.createMclnStatementState(mclnState, interpretationList.get(i));
//            availableMclnStatementStatesMap.put(state, mclnStatementState);
//        }

        return initialMclnStatementState;
    }

    /**
     * @param mclnStatementElement
     * @param inputSimulatingProgramTagName
     * @param programStepTagName
     * @return
     */
    private static InputSimulatingProgram getInputSimulatingProgram(Element mclnStatementElement,
                                                                    String inputSimulatingProgramTagName,
                                                                    String programStepTagName,
                                                                    AvailableMclnStatementStates availableMclnStatementStates) {

        InputSimulatingProgram inputSimulatingProgram = null;

        if (!mclnStatementElement.hasChildNodes()) {
            return inputSimulatingProgram;
        }
        NodeList programsElementList = mclnStatementElement.getElementsByTagName(inputSimulatingProgramTagName);
        if (programsElementList.getLength() < 1) {
            return inputSimulatingProgram;
        }
        Element programElement = (Element) programsElementList.item(0);
        String programName = programElement.getAttribute("name");
        boolean creatingTimeDrivenProgram = programName.equalsIgnoreCase("TimeDrivenProgram");
        System.out.println("InputSimulatingProgram is \"" + programName + "\"");

        NodeList programStepsElementList = programElement.getElementsByTagName(programStepTagName);
        if (programStepsElementList == null) {
            return inputSimulatingProgram;
        }
        int nSteps = programStepsElementList.getLength();
        System.out.println("Program " + programName + " has " + nSteps + " steps");
        List<ProgramStep> programStepsList = new ArrayList();
        for (int j = 0; j < nSteps; j++) {
            Element programStepElement = (Element) programStepsElementList.item(j);
            ProgramStep programStep = programStepElementToProgramState(creatingTimeDrivenProgram, programStepElement,
                    availableMclnStatementStates);
            programStepsList.add(programStep);
        }
        if (creatingTimeDrivenProgram) {
            inputSimulatingProgram = new TimeDrivenProgram(programStepsList);
        } else {
            inputSimulatingProgram = new StateDrivenProgram(programStepsList);
        }
        return inputSimulatingProgram;
    }

    /**
     * @param stateProperties
     * @return
     */
    private static MclnState statePropertiesToState(String stateProperties,
                                                    AvailableMclnStatementStates availableMclnStatementStates) {
        String[] statePropertiesArray = stateProperties.split(",");
        String meaning = (statePropertiesArray[0] != null) ? statePropertiesArray[0].trim() : "";
        int state = stringToInt((statePropertiesArray[1] != null) ? statePropertiesArray[1].trim() : "");
        String color = (statePropertiesArray[2] != null) ? statePropertiesArray[2].trim() : "0x000000";

        MclnState mclnState = null;
        int stateID = stringToInt(color);
        MclnStatementState mclnStatementState = availableMclnStatementStates.getMclnStatementState(stateID);
        if (mclnStatementState != null) {
            mclnState = mclnStatementState.getMclnState();
        }
        if (mclnState == null) {
            mclnState = MclnStatePalette.MCLN_CREATION_STATE;
        }
//        String oppositeMeaning = (statePropertiesArray[3] != null) ? statePropertiesArray[3].trim() : "";
//        int oppositeState = stringToInt((statePropertiesArray[4] != null) ? statePropertiesArray[4].trim() : "");
//        String oppositeColor = (statePropertiesArray[5] != null) ? statePropertiesArray[5].trim() : "0x000000";
//        MclnState oppositeMclnState = MclnState.createState(mclnState,
//                oppositeMeaning, oppositeState, oppositeColor);
        return mclnState;
    }

    /**
     * @param creatingTimeDrivenProgram
     * @param programStepElement
     * @return
     */
    private static ProgramStep programStepElementToProgramState(boolean creatingTimeDrivenProgram,
                                                                Element programStepElement,
                                                                AvailableMclnStatementStates availableMclnStatementStates) {

        boolean generatorStepHasPhase = false;
        NamedNodeMap namedNodeMap = programStepElement.getAttributes();
        Node generatorStepHasPhaseNode = namedNodeMap.getNamedItem(ProgramStep.MCLN_PROGRAM_ATTRIBUTE_IS_PHASE_KEY);
        if (generatorStepHasPhaseNode != null) {
            String generatorStepHasPhaseStrValue = generatorStepHasPhaseNode.getNodeValue();
            generatorStepHasPhase = generatorStepHasPhaseStrValue != null ?
                    stringToBoolean(generatorStepHasPhaseStrValue) : false;
        }

        String strTicks = getElementProperty(programStepElement, ProgramStep.MCLN_TICKS_TAG);
        int ticks = stringToInt(strTicks);
        if (ticks == 0) {
            return null;
        }

        String strStateData;
        ProgramStep programStep;

        strStateData = getElementProperty(programStepElement, ProgramStep.MCLN_GENERATED_STATE_TAG);
        if (strStateData == null) {
            return null;
        }
        MclnState generatedState = statePropertiesToState(strStateData, availableMclnStatementStates);

        if (creatingTimeDrivenProgram) {
            programStep = new TimeProgramStep(generatorStepHasPhase, ticks, generatedState);
        } else {
            strStateData = getElementProperty(programStepElement, ProgramStep.MCLN_EXPECTED_STATE_TAG);
            if (strStateData == null) {
                return null;
            }
            MclnState expectedState = statePropertiesToState(strStateData, availableMclnStatementStates);
            programStep = new StateProgramStep(expectedState, ticks, generatedState);
        }
        return programStep;
    }

    private static MclnCondition domElementToMclnCondition(Element mclnConditionElement) {
        String uid = getElementProperty(mclnConditionElement, MclnCondition.MCLN_CONDITION_UID_TAG);
        System.out.println("Statement ID is: " + uid);
        if (uid == null) {
            return null;
        }
        MclnCondition mclnCondition = MclnModelRetriever.createMclnCondition(uid);

        String xLocation = getElementProperty(mclnConditionElement, "X-Location");
        System.out.println("Statement X location is: " + xLocation);
        if (xLocation == null) {
            return null;
        }
        String yLocation = getElementProperty(mclnConditionElement, "Y-Location");
        System.out.println("Statement Y location is: " + yLocation);
        if (yLocation == null) {
            return null;
        }

        double[] cSysLocation = stringCSysLocationToDouble(xLocation, yLocation);
        if (cSysLocation == null) {
            return null;
        }
        mclnCondition.setCSysLocation(cSysLocation);
        System.out.println();
        return mclnCondition;
    }

    /**
     * @param mclnArcElement
     * @return
     */
    private static MclnArc domElementToMclnArc(Element mclnArcElement,
                                               Map<String, MclnStatement> nodeIdToMclnStatementMap,
                                               Map<String, MclnCondition> nodeIdToMclnConditionMap) {
        String uid = getElementProperty(mclnArcElement, MclnArc.MCLN_ARC_UID_TAG);
        System.out.println("Arc UID is: " + uid);
        if (uid == null) {
            return null;
        }

        String nodeUIDs = getElementProperty(mclnArcElement, MclnArc.MCLN_ARC_NODES_UID_TAG);
        System.out.println("Arc node UIDs: " + nodeUIDs);
        if (nodeUIDs == null) {
            return null;
        }

        String nodeUID[] = nodeUIDs.split(":");
        if (nodeUID[0] == null || nodeUID[1] == null) {
            return null;
        }

        MclnStatement mclnStatement;
        MclnNode mclnNode = nodeIdToMclnStatementMap.get(nodeUID[0].trim());
        if (mclnNode != null) {
            // arc from statement to condition
            mclnStatement = nodeIdToMclnStatementMap.get(nodeUID[0].trim());
        } else {
            // arc from condition to statement
            mclnStatement = nodeIdToMclnStatementMap.get(nodeUID[1].trim());
        }
        AvailableMclnStatementStates availableMclnStatementStates = mclnStatement.getAvailableMclnStatementStates();
        MclnState mclnState = null;
        String stateProperties = (String) getElementProperty(mclnArcElement, MclnArc.MCLN_ARC_STATE_TAG);
        if (stateProperties != null) {
            mclnState = statePropertiesToState(stateProperties, availableMclnStatementStates);
        }

        List<String[]> strXYLocations = getXYLocationList(mclnArcElement, MclnArc.MCLN_ARC_KNOTS_TAG,
                MclnArc.MCLN_ARC_KNOT_LOCATION_TAG);
        System.out.println("Statement XY location is: " + strXYLocations);
        if (strXYLocations == null || strXYLocations.size() == 0) {
            return null;
        }
        int nKnots = strXYLocations.size();
        List<double[]> knotLocations = new ArrayList();
        for (String[] strXYLocation : strXYLocations) {
            double[] xyLocation = stringCSysLocationToDouble(strXYLocation[0], strXYLocation[1]);
            knotLocations.add(xyLocation);
        }

        String arcArrowTipIndex = getElementProperty(mclnArcElement, MclnArc.MCLN_ARC_ARROW_TIP_INDEX_TAG);
        int arrowTipIndex = -1;
        if (arcArrowTipIndex != null) {
            arrowTipIndex = stringToInt(arcArrowTipIndex);
//            mclnArc.setArrowTipSplineIndex(arrowTipIndex);
        }

        String knobStrIndex = getElementProperty(mclnArcElement, MclnArc.MCLN_ARC_KNOB_INDEX_TAG);
        int knobIndex = -1;
        if (knobStrIndex != null) {
            knobIndex = stringToInt(knobStrIndex);
//            mclnArc.setKnobIndex(knobIndex);
        }

        ArrowTipLocationPolicy arrowTipLocationPolicy = getArrowTipLocationPolicy(arrowTipIndex, knobIndex);

        MclnArc mclnArc;
        MclnState arcMclnState;
        if (mclnNode != null) {
            // arc from statement to condition
//              mclnStatement = nodeIdToMclnStatementMap.get(nodeUID[0].trim());
            MclnCondition mclnCondition = nodeIdToMclnConditionMap.get(nodeUID[1].trim());
            mclnArc = MclnModelRetriever.createMclnArc(arrowTipLocationPolicy, uid, knotLocations, mclnState,
                    mclnStatement, mclnCondition);
//            mclnStatement.addOutArc(mclnArc);
//            mclnCondition.addInpArc(mclnArc);
        } else {
            // arc from condition to statement
            MclnCondition mclnCondition = nodeIdToMclnConditionMap.get(nodeUID[0].trim());
//              mclnStatement = nodeIdToMclnStatementMap.get(nodeUID[1].trim());
            mclnArc = MclnModelRetriever.createMclnArc(arrowTipLocationPolicy, uid, knotLocations, mclnState,
                    mclnCondition, mclnStatement);
//            mclnCondition.addOutArc(mclnArc);
//            mclnStatement.addInpArc(mclnArc);
        }

        mclnArc.setArrowTipSplineIndex(arrowTipIndex);
        mclnArc.setKnobIndex(knobIndex);
//
//        double[] cSysLocation = stringCSysLocationToDouble(xLocation, yLocation);
//        if (cSysLocation == null) {
//            return null;
//        }
//        mclnArc.setCSysLocation(cSysLocation);
        System.out.println();
        return mclnArc;
    }

    private static ArrowTipLocationPolicy getArrowTipLocationPolicy(int arrowTipIndex, int knobIndex) {
        if (arrowTipIndex > 0 && knobIndex <= 0) {
            return ArrowTipLocationPolicy.DETERMINED_BY_USER;
        } else if (arrowTipIndex > 0 && knobIndex > 0) {
            return ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_ARROW_TIP_OFFSET;
        } else if (arrowTipIndex <= 0 && knobIndex > 0) {
            return ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;
        } else {
            return ArrowTipLocationPolicy.DEFAULT_ARROW_TIP_LOCATION;
        }
    }

    private static List<String[]> getXYLocationList(Element mclnElement, String propertyListTagName, String propertyTagName) {
        if (!mclnElement.hasChildNodes()) {
            return null;
        }
        List<String[]> resultList = new ArrayList();
        NodeList propertyElementList = mclnElement.getElementsByTagName(propertyListTagName);
        if (propertyElementList.getLength() < 1) {
            return null;
        }

        Element propertyElement = (Element) propertyElementList.item(0);
        NodeList propertyChildNodeList = propertyElement.getElementsByTagName(propertyTagName);
        if (propertyChildNodeList.getLength() < 1) {
            return null;
        }

        int nOfKnots = propertyChildNodeList.getLength();
        System.out.println("Information on all   Arc knots, number is " + nOfKnots);
        for (int j = 0; j < nOfKnots; j++) {
            Element mclnArcKnotElement = (Element) propertyChildNodeList.item(j);
            Node node = mclnArcKnotElement.getFirstChild();
            String value = node.getNodeValue();
            if (value != null) {
                String[] xyLocation = value.split(":");
                resultList.add(xyLocation);
            }
        }
        return resultList;
    }


    private static String getElementProperty(Element mclnStatementElement, String propertyTagName) {
        if (!mclnStatementElement.hasChildNodes()) {
            return null;
        }

        NodeList propertyElementList = mclnStatementElement.getElementsByTagName(propertyTagName);
        if (propertyElementList.getLength() < 1) {
            return null;
        }

        Element propertyElement = (Element) propertyElementList.item(0);
        NodeList propertyChildNodeList = propertyElement.getChildNodes();
        if (propertyChildNodeList.getLength() < 1) {
            return null;
        }
        String value = propertyChildNodeList.item(0).getNodeValue();
        System.out.println("Property value is: " + value);
        return value;
    }

    private static double[] stringCSysLocationToDouble(String xLocation, String yLocation) {
        double[] cSysLocation;
        try {
            xLocation = xLocation.replace(",", ".");
            yLocation = yLocation.replace(",", ".");
            cSysLocation = new double[]{Double.parseDouble(xLocation), Double.parseDouble(yLocation), 0D};
        } catch (Exception e) {
            cSysLocation = null;
        }
        return cSysLocation;
    }

    private static int stringToInt(String strIndex) {
        int intValue = 0;
        try {
            intValue = Integer.decode(strIndex);
        } catch (Exception e) {

        }
        return intValue;
    }

    private static boolean stringToBoolean(String strBoolean) {
        boolean booleanValue = false;
        try {
            booleanValue = Boolean.valueOf(strBoolean);
        } catch (Exception e) {

        }
        return booleanValue;
    }

//    private static double stringToDouble(String value){
//        double doubleValue;
//        try{
//           doubleValue = Double.parseDouble(value);
//        }  catch(Exception e){
//            doubleValue = 0.0D;
//        }
//        return doubleValue;
//    }


}
