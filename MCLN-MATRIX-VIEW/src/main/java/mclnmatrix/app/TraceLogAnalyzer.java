package mclnmatrix.app;

import adf.utils.FileIOUtils;

import javax.swing.*;
import java.util.*;

public class TraceLogAnalyzer {

    private static final String SIMPLE_MODEL_RELATIVE_FILE_PATH = "/relation/tracelogs/Basic Block Trace Log.txt";

    private static final String MODEL_NAME_KEY = "MODEL-NAME";
    private static final String PROPERTY_NAME_LIST_KEY = "PROPERTY-NAME-LIST";
    private static final String EOF_KEY = "- End of Trace Log -";

    private static String modelName;
    private static final List<String> propertyNameList = new ArrayList();
    private static final List<List<String>> relationsList = new ArrayList();

    //
    //    R e t r i e v i n g   m o d e l   f r o m   f i l e   a n d
    //               c r e a t i n g   t h e   m o d e l
    //

    private static final boolean retrieveAndProcessLogFile(String relFilePath) {
        List<String> traceLogList = loadLogFile(relFilePath);
        if (traceLogList == null) {
            return false;
        }
        boolean success = extractFileHeaders(traceLogList);
        if (!success) {
            return false;
        }

        System.out.println("\n//\n//  Splitting to Trace Log pairs\n//\n");
        TraceLogCorrespondenceBuilder.traceLogToCorrespondence(modelName, propertyNameList,
                TraceLogAnalyzer.relationsList);
        return success;
    }

    /**
     * @param traceLogList
     * @return
     */
    private static final boolean extractFileHeaders(List<String> traceLogList) {

        List<List<String>> relationsList = new ArrayList();
        List<String> reversedRelationsList = new ArrayList();
        for (String entry : traceLogList) {
            if (entry == null || entry.trim().contains("#") || entry.trim().contains(EOF_KEY)) {
                // comment found, skipping it
                continue;
            }
            if (entry.trim().charAt(0) == '@') { // header found

                String header = entry.substring(1);
                String[] keyValuePair = header.split(":");
                String key = keyValuePair == null || keyValuePair.length != 2 || keyValuePair[0] == null ?
                        null : keyValuePair[0].trim();

                if (key == null) {
                    System.out.println("Invalid header: " + header);
                    System.out.println("Keyword " + key + " is null.");
                    System.out.println();
                    return false;
                }
                String value = keyValuePair[1] == null ? null : keyValuePair[1].trim();
                if (value == null || value.isEmpty()) {
                    System.out.println("Invalid header: " + header);
                    System.out.println("Value \'" + value + "\' is not recognized as valid Model Name!");
                    System.out.println();
                    return false;
                }
                if (modelName == null) {
                    modelName = extractModelNameHeader(key, value);
                    if (modelName.isEmpty()) {
                        return false;
                    }
                    continue;
                }

                if (propertyNameList.isEmpty()) {
                    List<String> propertyNameList = extractPropertyNamesHeader(key, value);
                    if (propertyNameList == null) {
                        return false;
                    }

                    TraceLogAnalyzer.propertyNameList.addAll(propertyNameList);
                    continue;
                }
            }

            // processing trace log itself
            reversedRelationsList.add(entry);
        }

        // Reverse file
        String[] tmpArray = reversedRelationsList.toArray(new String[reversedRelationsList.size()]);
//        int reversedRelationsListSize = reversedRelationsList.size() / 2;
//        for (int i = 0; i < reversedRelationsListSize; i++) {
//            String tmpData = tmpArray[i];
//            tmpArray[i] = tmpArray[tmpArray.length - 1 - i];
//            tmpArray[tmpArray.length - 1 - i] = tmpData;
//        }
        for (String states : tmpArray) {
            String lineNumber = states.substring(0,states.indexOf(")")+1);
            states = states.substring(states.indexOf(")")+1,states.length() ).trim();
            System.out.println("Reversed relation "+states);
            String[] statesArray = states.split(",");
            List<String> stateAsList = new ArrayList();
            for (String state : statesArray) {
                stateAsList.add(state == null ? "" : state.trim());
            }
            addToStatesToSymbolsMap(stateAsList);
            TraceLogAnalyzer.relationsList.add(stateAsList);
        }
        printStatesToSymbolsMap(statesToSymbolsMap);
        return true;
    }

    private static Map<String, String> statesToSymbolsMap = new HashMap();
    private static String[] arrayOfSymbols = AoSUtils.getSSetAsArray();
    private static int currentSymbolIndex = 0;

    private static final void addToStatesToSymbolsMap(List<String> stateAsList) {
        for (String propertyState : stateAsList) {
            String setSymbol = statesToSymbolsMap.get(propertyState);
            if (setSymbol == null) {
                String setSymbolFromSet = arrayOfSymbols[currentSymbolIndex++];
                statesToSymbolsMap.put(propertyState, setSymbolFromSet);
            }
        }
    }

    private static final void printStatesToSymbolsMap(Map<String, String> statesToSymbolsMap) {
        Set<String> keys = statesToSymbolsMap.keySet();
        int ind = 0;
        for (String key : keys) {
            String setSymbol = statesToSymbolsMap.get(key);
            System.out.println("State to Symbols " + ind + "  " + key + " ==> " + setSymbol);
        }
    }

    /**
     * @param nameKey
     * @param value
     * @return
     */
    private static final String extractModelNameHeader(String nameKey, String value) {
        //   E x t r a c t i n g   M o d e l   N a m e

        if (modelName != null) {
            if (nameKey.equalsIgnoreCase(MODEL_NAME_KEY)) {
                System.out.println("Invalid header key: " + nameKey);
                System.out.println("Keyword " + nameKey + " found, but Model Name " + modelName + " already specified.");
                System.out.println();
                return "";
            }
        } else if (!nameKey.equalsIgnoreCase(MODEL_NAME_KEY)) {
            System.out.println("Invalid header key: " + nameKey);
            System.out.println("Keyword " + MODEL_NAME_KEY + " is expected.");
            System.out.println();
            return "";
        }
        return value;
    }

    /**
     * @param propertyNameListKey
     * @param value
     * @return
     */
    private static final List<String> extractPropertyNamesHeader(String propertyNameListKey, String value) {
        //   E x t r a c t i n g   M o d e l   N a m e
        List<String> propertyNameList = new ArrayList();
        if (!propertyNameList.isEmpty()) {
            if (propertyNameListKey.equalsIgnoreCase(PROPERTY_NAME_LIST_KEY)) {
                System.out.println("Invalid header key: " + propertyNameListKey);
                System.out.println("Keyword " + propertyNameListKey + " found, but Property Name List already specified.");
                System.out.println();
                return null;
            }
        } else if (!propertyNameListKey.equalsIgnoreCase(PROPERTY_NAME_LIST_KEY)) {
            System.out.println("Invalid header key: " + propertyNameListKey);
            System.out.println("Keyword " + PROPERTY_NAME_LIST_KEY + " is expected.");
            System.out.println();
            return null;
        }
        String[] propertyNames = value.split(",");
        for (String propertyName : propertyNames) {
            propertyNameList.add(propertyName == null ? "" : propertyName.trim());
        }
        return propertyNameList;
    }

    /**
     * @param relDirPath
     * @return
     */
    private static final List<String> loadLogFile(String relDirPath) {
        List<String> modelList = FileIOUtils.loadTxtFileFromUserDirAsList(relDirPath);
        if (modelList != null) {
            for (String fileEntry : modelList) {
                System.out.println("file entry " + fileEntry);
            }
        }
        return modelList;
    }

    //   S t a r t i n g   T h e   A p p l i c a t i o n

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                boolean success = retrieveAndProcessLogFile(SIMPLE_MODEL_RELATIVE_FILE_PATH);
                if (!success) {
                    System.out.println("TraceLogAnalyzer.main: Log File was not retrieved ! ");
                    return;
                }

//                FileIOUtils.writeTxtFileToUserDir(relativeLocation, dataToFile);
            } catch (Throwable e) {
                System.out.println("TraceLogAnalyzer.main: Throwable caught ! " + e.toString());
                System.out.println("TraceLogAnalyzer.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}

//class Dependency {
//
//    public static Dependency createInstance(List<String>[] logStatesAtStep) {
//        return new Dependency(logStatesAtStep);
//    }
//
////    final SortedSet<VariableValuePair> premises = new TreeSet();
////    final List<VariableValuePair> premisesAsList = new ArrayList(premises);
////    final SortedSet<VariableValuePair> conclusions = new TreeSet();
////    final List<VariableValuePair> conclusionsAsList = new ArrayList(premises);
//
//    private final List<String>[] logStatesAtStep;
//    List<String> results;
//
//    private Dependency(List<String>[] logStatesAtStep) {
//        this.logStatesAtStep = logStatesAtStep;
//        whatHasChangedAfterTransition(logStatesAtStep);
//        System.out.println();
//    }
//
//    private   void whatHasChangedAfterTransition(List<String>[] logStatesAtStep) {
//
////            String prevStateString = stateListToString(stepPair[0]);
////            String nextStateString = stateListToString(stepPair[1]);
////            System.out.println("Prev: " + prevStateString);
////            System.out.println("Next: " + nextStateString);
////            System.out.println("");
//
//        List<String> prprState = logStatesAtStep[0];
//        List<String> prevState = logStatesAtStep[1];
//        List<String> nextState = logStatesAtStep[2];
//         results = new ArrayList();
//        for (int i = 0; i < prevState.size(); i++) {
//            String prevProperty = prevState.get(i);
//            String nextProperty = nextState.get(i);
//            String result = AoSUtils.applied(prevProperty, nextProperty);
//            results.add(result);
//        }
//    }
//
//    void printStepStatesDependency() {
//        String prevPrevStateString = stateListToString(logStatesAtStep[0]);
//        String prevStateString = stateListToString(logStatesAtStep[1]);
//        String nextStateString = stateListToString(logStatesAtStep[2]);
////        System.out.println("Dependency pair " + cnt++);
//        System.out.println("PrPr: " + prevPrevStateString);
//        System.out.println("Prev: " + prevStateString);
//        System.out.println("Next: " + nextStateString);
////            if(stepPair[2] != null){
////                String newStateString = stateListToString(stepPair[2]);
////                System.out.println("repl: " + newStateString);
////            }
//    }
//
//    private static String stateListToString(List<String> states) {
//        StringBuilder sb = new StringBuilder();
//        for (String state : states) {
//            if (sb.length() != 0) {
//                sb.append(",");
//            }
//            sb.append(state);
//        }
//        String allStatesString = sb.toString();
//        return sb.toString();
//    }
//
////    final void addPremisePair(String variableName, String value) {
////        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
////        addPremisePair(variableValuePair);
////    }
////
////    final void addPremisePair(VariableValuePair variableValuePair) {
////        premises.add(variableValuePair);
////        premisesAsList.clear();
////        premisesAsList.addAll(premises);
////    }
////
////    final void addConclusionPair(String variableName, String value) {
////        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
////        addConclusionPair(variableValuePair);
////    }
////
////    final void addConclusionPair(VariableValuePair variableValuePair) {
////        conclusions.add(variableValuePair);
////        // this should be done after each addition to have conclusionsAsList sorted
////        conclusionsAsList.clear();
////        conclusionsAsList.addAll(conclusions);
////    }
////
////    final void addAllConclusionPairs(List<VariableValuePair> variableValuePairs) {
////        conclusions.addAll(variableValuePairs);
////        // this should be done after each addition to have conclusionsAsList sorted
////        conclusionsAsList.clear();
////        conclusionsAsList.addAll(conclusions);
////    }
////
////    public final boolean hasEqualPremises(Correspondence otherCorrespondence) {
////        int premisesSize = premisesAsList.size();
////        if (premisesSize != otherCorrespondence.premisesAsList.size()) {
////            return false;
////        }
////        for (int i = 0; i < premisesSize; i++) {
////            VariableValuePair variableValuePair = premisesAsList.get(i);
////            VariableValuePair otherVariableValuePair = otherCorrespondence.premisesAsList.get(i);
////            boolean same = variableValuePair.equals(otherVariableValuePair);
////            if (!same) {
////                return false;
////            }
////        }
////        return true;
////    }
////
////    public final List<VariableValuePair> areThereNewConclusionsToBeAdded(Correspondence newCorrespondence) {
////        List<VariableValuePair> conclusionsToAdd = new ArrayList();
////        int otherConclusionsSize = newCorrespondence.conclusions.size();
////        for (int i = 0; i < otherConclusionsSize; i++) {
////            VariableValuePair newConclusionVariableValuePair = newCorrespondence.conclusionsAsList.get(i);
////            boolean exist = conclusions.contains(newConclusionVariableValuePair);
////            if (!exist) {
////                conclusionsToAdd.add(newConclusionVariableValuePair);
////            }
////        }
////        return conclusionsToAdd;
////    }
//
//
//}
