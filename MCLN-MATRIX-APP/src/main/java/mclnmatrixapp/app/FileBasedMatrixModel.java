package mclnmatrixapp.app;

import adf.utils.FileIOUtils;
import mclnmatrix.model.*;

import java.util.*;

import static mclnmatrix.model.ModelFileUtils.convertCompactFormatToBaseFormat;

public class FileBasedMatrixModel extends MclnMatrixModel {

    private static final String FORMAT_TYPE_KEY = "FORMAT-TYPE";
    private static final String FORMAT_TYPE_STANDARD = "STANDARD";
    private static final String FORMAT_TYPE_STANDARD_SKIP = "STANDARD-SKIP";
    private static final String FORMAT_TYPE_COMPACT = "COMPACT";
    private static final String INITIAL_STATE_KEY = "INITIAL-STATE";
    private static final String PROPERTIES_KEY = "PROPERTIES";
    private static final String DEPENDENCY_KEY = "DEPENDENCY";
    private static final String RELATION_KEY = "RELATION";


    public static final synchronized FileBasedMatrixModel createInstance() {
        return new FileBasedMatrixModel();
    }

    private FileBasedMatrixModel() {

    }

    //
    //    R e t r i e v i n g   m o d e l   f r o m   f i l e   a n d
    //               c r e a t i n g   t h e   m o d e l
    //

    public final boolean createModelFromFile(String relFilePath) {
        List<String> relationsList = loadModel(relFilePath);
        if (relationsList == null) {
            return false;
        }
        createModelAccordingSpecificationType(relationsList);
        return true;
    }

    //
    //  ================   C r e a t i n g   M o d e l   R e l a t i o n s    ====================
    //
/*
    Initial chane: createModelFromFile List<String>
                       loadModel
                         \/
                createModelFromRelations
                         \/
               convertModelListToMvlDataModel

   Compact file conversion  loadModelFileAsListOfStrings
                                        \/
           Model File Utils convertCompactFormatToBaseFormat

  Latest: createModelFromFile List<String>
                     loadModel
                        \/
           createModelAccordingSpecificationType
           STANDARD: convertBasicFormatRelationListToModel

           STANDARD SKIP:

           COMPACT:
                    convertCompactFormatToBaseFormat
                    convertBasicFormatRelationListToModel
                    convertCorrespondenceListToMatrixModel(relations);



 */

    private List<String> loadModel(String relDirPath) {
        List<String> modelList = FileIOUtils.loadTxtFileAsListOfStringsFromClassPath(relDirPath);
//        if (modelList != null) {
//            for (String fileEntry : modelList) {
//                System.out.println("file entry " + fileEntry);
//            }
//        }
        return modelList;
    }

    boolean stateChangeHighlighted = true;

    private final boolean createModelAccordingSpecificationType(List<String> relationsList) {
        String formatType = "";
        String initialState = "";
        List<VectorCell> inputVectorStateData = new ArrayList();
        List<String> modelList = new ArrayList();
        boolean success = false;
        for (String entry : relationsList) {
            if (entry == null || entry.trim().contains("#")) {
                continue;
            }
            if (entry.trim().charAt(0) == '@') {
                String header = entry.substring(1);
                String[] keyValuePair = header.split(":");
                String key = keyValuePair == null || keyValuePair.length != 2 || keyValuePair[0] == null ?
                        null : keyValuePair[0].trim();

                if (key == null) {
//                    System.out.println("Invalid header: " + header);
//                    System.out.println("Keyword " + key + " is null.");
//                    System.out.println();
                    return false;
                }

                //   P a r s i n g   F o r m a t   T y p e

                if (!formatType.isEmpty()) {
                    if (key.equalsIgnoreCase(FORMAT_TYPE_KEY)) {
//                        System.out.println("Invalid header: " + header);
//                        System.out.println("Keyword " + key + " found, but Format Type " + formatType + " already specified.");
//                        System.out.println();
                        return false;
                    }
                } else {
                    if (!key.equalsIgnoreCase(FORMAT_TYPE_KEY)) {
//                        System.out.println("Invalid header: " + header);
//                        System.out.println("Keyword " + FORMAT_TYPE_KEY + "expected.");
//                        System.out.println();
                        return false;
                    }
                    String value = keyValuePair[1] == null ? null : keyValuePair[1].trim();
                    if (value == null || !value.equalsIgnoreCase(FORMAT_TYPE_STANDARD) &&
                            !value.equalsIgnoreCase(FORMAT_TYPE_STANDARD_SKIP) &&
                            !value.equalsIgnoreCase(FORMAT_TYPE_COMPACT)) {
//                        System.out.println("Invalid header: " + header);
//                        System.out.println("Value " + value + " is not recognized as valid Format Type!");
//                        System.out.println();
                        return false;
                    }
                    formatType = value;
                    continue;
                }

                //   P a r s i n g   I n i t i a l   S t a t e

                if (!initialState.isEmpty()) {
                    if (key.equalsIgnoreCase(INITIAL_STATE_KEY)) {
//                        System.out.println("Invalid header: " + header);
//                        System.out.println("Keyword " + key + " found, but initial State " + initialState + " already processed.");
//                        System.out.println();
                        return false;
                    }
                } else {
                    if (!key.equalsIgnoreCase(INITIAL_STATE_KEY)) {
//                        System.out.println("Invalid header: " + header);
//                        System.out.println("Keyword " + INITIAL_STATE_KEY + "expected.");
//                        System.out.println();
                        return false;
                    }
                    String value = keyValuePair[1] == null ? null : keyValuePair[1].trim();
                    inputVectorStateData = createInputVectorStateData(value);
//                    InitialStateDataModel  initialStateDataModel = parseInitialState(value);
                    if (inputVectorStateData.isEmpty()) {
//                        System.out.println("Parsing Initial State list was not successful!");
//                        System.out.println();
                        return false;
                    }
                    initialState = value;
//                    initialStateValues = value;
                    continue;
                }
            }

            //   C r e a t i n g   M o d e l   R e l a t i o n s   L i s t

            modelList.add(entry);
        }


        //   B u i l d i n g   t h e   m o d e l   a c c o r d i n g   t o
        //            s p e c i f i e d   F o r m a t   T y p e

        List<Correspondence> relations = new ArrayList();

        if (formatType.equalsIgnoreCase(FORMAT_TYPE_STANDARD)) {
            relations = convertStandardFormatSpecToCorrespondenceList(modelList);
            if (relations.isEmpty()) {
//                System.out.println(FORMAT_TYPE_KEY + ": " + FORMAT_TYPE_STANDARD + "The Model was not created!");
//                System.out.println();
                return false;
            }
            stateChangeHighlighted = true;
        } else if (formatType.equalsIgnoreCase(FORMAT_TYPE_STANDARD_SKIP)) {
            relations = convertStandardFormatSpecToCorrespondenceList(modelList);
            if (relations.isEmpty()) {
//                System.out.println(FORMAT_TYPE_KEY + ": " + FORMAT_TYPE_STANDARD_SKIP + "The Model was not created!");
//                System.out.println();
                return false;
            }
            stateChangeHighlighted = true;
        } else if (formatType.equalsIgnoreCase(FORMAT_TYPE_COMPACT)) {
            List<String> standardFormatSpec = convertCompactFormatToBaseFormat(modelList);
            relations = convertStandardFormatSpecToCorrespondenceList(standardFormatSpec);
            if (relations.isEmpty()) {
//                System.out.println(FORMAT_TYPE_KEY + ": " + FORMAT_TYPE_COMPACT + "The Model was not created!");
//                System.out.println();
                return false;
            }
            stateChangeHighlighted = false;
        }

        // creating input vector data
        setInputStateVectorData(inputVectorStateData);

        // this method builds model and defines property and condition sizes
        convertCorrespondenceListToMatrixModel(relations);


        // creating suggested state vector data
        List<VectorCell> suggestedVectorStateDataModel = createVectorStateData(getPropertySize());
        setSuggestedStateVectorData(suggestedVectorStateDataModel);

        // creating condition state vector data
        List<VectorCell> conditionVectorStateDataModel = createVectorStateData(getConditionSize());
        setConditionStateVectorData(conditionVectorStateDataModel);

        andMatrixDataModelForHorizontalLayout =
                new AndMatrixDataModel(true, andMatrixConditions);
        orMatrixDataModelForHorizontalLayout =
                new OrMatrixDataModel(true, orMatrixConditions);
        andMatrixDataModelForVerticalLayout =
                new AndMatrixDataModel(false, andMatrixConditions);
        orMatrixDataModelForVertivalLayout =
                new OrMatrixDataModel(false, orMatrixConditions);
        return true;
    }

    private List<VectorCell> createInputVectorStateData(String initialStateEntry) {
        if (initialStateEntry == null || initialStateEntry.isEmpty()) {
            return null;
        }
        List<VectorCell> inputVectorStateData = new ArrayList();
        String[] initialStatesPairsAsArray = initialStateEntry.split(",");
        for (int i = 0; i < initialStatesPairsAsArray.length; i++) {
            String[] initialStatesPair = initialStatesPairsAsArray[i].split("=");
            String propertyName = initialStatesPair[0].trim();
            String initialState = initialStatesPair[1].trim();
            VectorCell vectorCell = VectorCell.createCell(i, propertyName, initialState);
            inputVectorStateData.add(vectorCell);
        }
        return inputVectorStateData;
    }

    static List<VectorCell> createVectorStateData(int listSize) {
        List<VectorCell> vectorData = new ArrayList();
        for (int i = 0; i < listSize; i++) {
            VectorCell vectorCell = VectorCell.createCell(i, "", "-");
            vectorData.add(vectorCell);
        }
        return vectorData;
    }

    /**
     * @param modelList
     * @return
     */
    private List<Correspondence> convertStandardFormatSpecToCorrespondenceList(List<String> modelList) {

        int rowIndex = -1;
        List<Correspondence> relations = new ArrayList();

        for (String entry : modelList) {
            if (entry == null || entry.trim().contains("#")) {
                continue;
            }
            if (entry.contains(PROPERTIES_KEY)) {
                String header = entry;
                String[] propertyNames = header.split(":");
                String[] propertyNamesArray = propertyNames[1].split(",");
                for (int i = 0; i < propertyNamesArray.length; i++) {
                    String propertyName = propertyNamesArray[i].trim();
                    Property property = Property.createProperty(i, propertyName);
                    properties.add(property);
                    propertyNameToPropertyMap.put(propertyName, property);
                }
                continue;
            }

            //
            // parsing dependency relation
            //

            Correspondence newCorrespondence = Correspondence.createInstance();

            conjunction = new HashMap();
            rowIndex++;
            String[] relationParts = entry.split("!");

            String suggestedStatePart = relationParts[0].replace(" ", "");
            String[] variableSuggestedStatePairs = suggestedStatePart.split(",");
            for (String variableSuggestedStatePair : variableSuggestedStatePairs) {
                String[] nameValuePairAsArray = variableSuggestedStatePair.split("=");
                newCorrespondence.addConclusionPair(nameValuePairAsArray[0], nameValuePairAsArray[1]);
            }

            String conditionPart = relationParts[1].replace(" ", "");
            String[] conditionPartPairs = conditionPart.split(",");
            for (String conditionPartPair : conditionPartPairs) {
                String[] nameValuePairAsArray = conditionPartPair.split("=");
                newCorrespondence.addPremisePair(nameValuePairAsArray[0], nameValuePairAsArray[1]);
            }

            boolean existingCorrespondenceHasSamePremisesAsNewOne = false;
            int relationsCurrentSize = relations.size();
            if (relationsCurrentSize >= 1) {
                for (int i = 0; i < relationsCurrentSize; i++) {
                    Correspondence existingCurrentCorrespondence = relations.get(i);
                    existingCorrespondenceHasSamePremisesAsNewOne = existingCurrentCorrespondence.hasEqualPremises(newCorrespondence);
                    if (!existingCorrespondenceHasSamePremisesAsNewOne) {
                        continue;
                    }
                    // existing correspondence has same premises as new correspondence
                    // check if new correspondence has existing conclusion
                    List<VariableValuePair> conclusionsToAdd =
                            existingCurrentCorrespondence.areThereNewConclusionsToBeAdded(newCorrespondence);
                    if (conclusionsToAdd.isEmpty()) {
                        // found correspondence that has same premise and existing conclusion
                        // it should be ignored
                        break;
                    }
                    // Augment conclusions in existing correspondence
                    // with the conclusions from new correspondence.
                    // And discard new correspondence after jumping out of the loop.
                    existingCurrentCorrespondence.addAllConclusionPairs(conclusionsToAdd);
                    break;
                }
            }
            if (!existingCorrespondenceHasSamePremisesAsNewOne) {
                relations.add(newCorrespondence);
            }
        }
//        printCorrespondenceList(relations);
        return relations;
    }

    private void convertCorrespondenceListToMatrixModel(List<Correspondence> relations) {
        int relationsSize = relations.size();
//        System.out.println("\n Converting relations: " + relationsSize);
        int correspondenceCounter = 1;
        int rowIndex = -1;

        for (Correspondence correspondence : relations) {
            rowIndex++;
            Map<Integer, OrCell> disjunction = new HashMap();
            for (VariableValuePair variableValuePair : correspondence.conclusions) {
//                System.out.println("Conclusions: " + variableValuePair.toString());
                String variableName = variableValuePair.getVariableName();
                int variableIndex = variableValuePair.getIndex();
                String generatedState = variableValuePair.getValue();
                OrCell orCell = OrCell.createOrCell(rowIndex, variableIndex, variableName, generatedState);
                disjunction.put(variableIndex, orCell);
            }
            orMatrixConditions.add(disjunction);

            Map<Integer, AndCell> conjunction = new HashMap();
            for (VariableValuePair variableValuePair : correspondence.premises) {
//                System.out.println("Premises: " + variableValuePair.toString());
                String variableName = variableValuePair.getVariableName();
                int variableIndex = variableValuePair.getIndex();
                String expectedState = variableValuePair.getValue();
                AndCell andCell = AndCell.createAndCell(rowIndex, variableIndex, variableName, expectedState);
                conjunction.put(variableIndex, andCell);
            }
            andMatrixConditions.add(conjunction);
        }
    }

    // ================================================================================================================
    //              I n i t i a l l y   c r e a t e d   m e t h o d s
    // ================================================================================================================


    /**
     * Initial Model building method
     *
     * @param relationsList
     * @return
     */
    private final boolean createModelFromRelations(List<String> relationsList) {
        boolean success = convertModelListToMvlDataModel(relationsList);
        if (success) {
            andMatrixDataModelForHorizontalLayout =
                    new AndMatrixDataModel(true, andMatrixConditions);
            orMatrixDataModelForHorizontalLayout =
                    new OrMatrixDataModel(true, orMatrixConditions);
            andMatrixDataModelForVerticalLayout =
                    new AndMatrixDataModel(false, andMatrixConditions);
            orMatrixDataModelForVertivalLayout =
                    new OrMatrixDataModel(false, orMatrixConditions);
        }
        return true;
    }

    /**
     * This is initially created converter that converts text relations list to model.
     * It does not do ant pre processing
     *
     * @param modelList
     * @return
     */
    private final boolean convertModelListToMvlDataModel(List<String> modelList) {

        int rowIndex = -1;
        disjunction = new HashMap();
        for (String entry : modelList) {
            if (entry == null || entry.trim().contains("#")) {
                continue;
            }
            if (entry != null && entry.contains(PROPERTIES_KEY)) {
                String header = entry;
                String[] propertyNames = header.split(":");
                String[] propertyNamesArray = propertyNames[1].split(",");
                for (int i = 0; i < propertyNamesArray.length; i++) {
                    String propertyName = propertyNamesArray[i].trim();
                    Property property = Property.createProperty(i, propertyName);
                    properties.add(property);
                    propertyNameToPropertyMap.put(propertyName, property);
                }
                continue;
            }

            // splitting file to dependency lists

//            List<List<String>> dependencies = new ArrayList();
            List<Property> dependenciesProperties = new ArrayList();
            if (entry != null && entry.contains(DEPENDENCY_KEY)) {
                if (rowIndex >= 0) {
//                    orMatrixProperties.add(disjunction);
                }


                String[] dependencyArray = entry.split(":");
                String[] dependencyRelationHeader = dependencyArray[1].split("!");
                effectedPropertyName = dependencyRelationHeader[0].trim();
                String[] fromPropertyNames = dependencyRelationHeader[1].split(",");
                for (int i = 0; i < fromPropertyNames.length; i++) {
                    String propertyName = fromPropertyNames[i];
                    Property property = propertyNameToPropertyMap.get(propertyName);
                    dependenciesProperties.add(property);
                }

                continue;
            }

            // parsing dependency relation
            conjunction = new HashMap();
            rowIndex++;
            String[] relationParts = entry.split("!");
            String suggestedState = relationParts[0].trim();

            Property orMatrixProperty = propertyNameToPropertyMap.get(effectedPropertyName);
            int orMatrixColumnIndex = orMatrixProperty.getColumnIndex();
            OrCell orCell = OrCell.createOrCell(rowIndex, orMatrixColumnIndex, effectedPropertyName, suggestedState);
            disjunction.put(orMatrixColumnIndex, orCell);
            orMatrixConditions.add(disjunction);
            disjunction = new HashMap();

            String[] situationPropertyNameValuePairs = relationParts[1].split(",");
            for (int i = 0; i < situationPropertyNameValuePairs.length; i++) {
                String situationPropertyNameValuePair = situationPropertyNameValuePairs[i];
                String[] situationPropertyNameValuePairAsArray = situationPropertyNameValuePair.split("=");
                String propertyName = situationPropertyNameValuePairAsArray[0].trim();
                String expectedState = situationPropertyNameValuePairAsArray[1].trim();
                dependenciesProperties.add(Property.createProperty(i, propertyName));
                Property andMatrixProperty = propertyNameToPropertyMap.get(propertyName);
                int andMatrixColumnIndex = andMatrixProperty.getColumnIndex();
                AndCell andCell = AndCell.createAndCell(rowIndex, andMatrixColumnIndex, propertyName, expectedState);
                conjunction.put(andMatrixColumnIndex, andCell);
            }
            andMatrixConditions.add(conjunction);
        }


        return true;
    }

    /**
     * Prints relation internal representation
     */
    protected void printCorrespondenceList(List<Correspondence> relations) {
        System.out.println("\n Relations: ");
        int correspondenceCounter = 1;
        for (Correspondence correspondence : relations) {
            int lineCounter = 0;
            System.out.print("" + correspondenceCounter + ")  ");
            for (VariableValuePair variableValuePair : correspondence.conclusions) {
                System.out.print(variableValuePair.toString());
                if (lineCounter != correspondence.conclusions.size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print(" => ");
                }
                lineCounter++;
            }
            lineCounter = 0;
            for (VariableValuePair variableValuePair : correspondence.premises) {
                System.out.print(variableValuePair.toString());
                if (lineCounter != correspondence.premises.size() - 1) {
                    System.out.print(", ");
                }
                lineCounter++;
            }
            System.out.println("");
            correspondenceCounter++;
        }
    }
}

// ====================================================================================================================
//                                C l a s s e s
// ====================================================================================================================

class Correspondence {
    public static Correspondence createInstance() {
        return new Correspondence();
    }

    public final SortedSet<VariableValuePair> premises = new TreeSet();
    final List<VariableValuePair> premisesAsList = new ArrayList(premises);
    public final SortedSet<VariableValuePair> conclusions = new TreeSet();
    final List<VariableValuePair> conclusionsAsList = new ArrayList(premises);

    private Correspondence() {

    }

    public final void addPremisePair(String variableName, String value) {
        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
        addPremisePair(variableValuePair);
    }

    private final void addPremisePair(VariableValuePair variableValuePair) {
        premises.add(variableValuePair);
        premisesAsList.clear();
        premisesAsList.addAll(premises);
    }

    public final void addConclusionPair(String variableName, String value) {
        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
        addConclusionPair(variableValuePair);
    }

    final void addConclusionPair(VariableValuePair variableValuePair) {
        conclusions.add(variableValuePair);
        // this should be done after each addition to have conclusionsAsList sorted
        conclusionsAsList.clear();
        conclusionsAsList.addAll(conclusions);
    }

    public final void addAllConclusionPairs(List<VariableValuePair> variableValuePairs) {
        conclusions.addAll(variableValuePairs);
        // this should be done after each addition to have conclusionsAsList sorted
        conclusionsAsList.clear();
        conclusionsAsList.addAll(conclusions);
    }

    public final boolean hasEqualPremises(Correspondence otherCorrespondence) {
        int premisesSize = premisesAsList.size();
        if (premisesSize != otherCorrespondence.premisesAsList.size()) {
            return false;
        }
        for (int i = 0; i < premisesSize; i++) {
            VariableValuePair variableValuePair = premisesAsList.get(i);
            VariableValuePair otherVariableValuePair = otherCorrespondence.premisesAsList.get(i);
            boolean same = variableValuePair.equals(otherVariableValuePair);
            if (!same) {
                return false;
            }
        }
        return true;
    }

    public final List<VariableValuePair> areThereNewConclusionsToBeAdded(Correspondence newCorrespondence) {
        List<VariableValuePair> conclusionsToAdd = new ArrayList();
        int otherConclusionsSize = newCorrespondence.conclusions.size();
        for (int i = 0; i < otherConclusionsSize; i++) {
            VariableValuePair newConclusionVariableValuePair = newCorrespondence.conclusionsAsList.get(i);
            boolean exist = conclusions.contains(newConclusionVariableValuePair);
            if (!exist) {
                conclusionsToAdd.add(newConclusionVariableValuePair);
            }
        }
        return conclusionsToAdd;
    }
}

class VariableValuePair implements Comparable<VariableValuePair> {

    public static final VariableValuePair creteInstance(String variableName, String value) {
        return new VariableValuePair(variableName, value);
    }

    private int stringToInt(String strIndex) {
        int index = -1;
        try {
            index = Integer.parseInt(strIndex);
        } finally {
            return index;
        }
    }

    private final String variableName;
    private final int index;
    private final String value;

    private VariableValuePair(String variableName, String value) {
        assert (variableName != null && value != null) : "Illegal input: " + variableName + " or " + value;
        this.variableName = variableName;
        index = stringToInt(variableName.substring(1)) - 1;
        this.value = value;
    }

    public final String getVariableName() {
        return variableName;
    }

    public int getIndex() {
        return index;
    }

    public final String getValue() {
        return value;
    }

    public int compareTo(VariableValuePair otherObject) {
        return index - otherObject.index;
    }

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof VariableValuePair)) {
            return false;
        }
        VariableValuePair otherVariableValuePair = (VariableValuePair) otherObject;
        boolean nameIsSame = variableName.equalsIgnoreCase(otherVariableValuePair.variableName);
        boolean valueIsSame = value.equalsIgnoreCase(otherVariableValuePair.value);
        return nameIsSame && valueIsSame;
    }

    public String toString() {
        return variableName + "(" + value + ")";
    }
}










