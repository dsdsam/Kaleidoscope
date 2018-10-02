package mclnmatrixapp.app;

import adf.utils.FileIOUtils;

import java.util.ArrayList;
import java.util.List;

public class ModelFileUtils {

    private static final String FILE_FORMAT_KEY = "FORMAT-TYPE";
    private static final String FILE_FORMAT_TYPE = "COMPACT";
    private static final String STATE_VARIABLES_KEY = "STATE-VARIABLES";

    private static final String MODEL_IN_COMPACT_FORMAT_RELATIVE_FILE_PATH = "/relation/model001InCompactFormat .txt";
    private static final String MODEL_IN_BASE_FORMAT_RELATIVE_FILE_PATH = "/relation/model001InBaseFormat .txt";


    public static List<String> loadModelFileAsListOfStrings(String relDirPath) {
        List<String> modelList = FileIOUtils.loadTxtFileFromUserDirAsListSkipEmptyLines(relDirPath);
        if (modelList != null) {
            for (String fileEntry : modelList) {
                System.out.println("file entry " + fileEntry);
            }
        }
        return modelList;
    }

    public static List<String> convertCompactFormatToBaseFormat(List<String> modelFileInCompactFormat) {
        List<String> modelFileInBaseFormat = new ArrayList();


//        String stateVariableList = STATE_VARIABLES_KEY + " : ";
//        for (int i = 0; i < compactFormatFileSize; i++) {
//            String divider = i < (compactFormatFileSize - 1) ? "," : "";
//            String stateVariableEntry = "v" + i + divider;
//            stateVariableList = stateVariableList.concat(stateVariableEntry);
//        }
        String properties = modelFileInCompactFormat.get(0);
        modelFileInBaseFormat.add(properties);

        int compactFormatFileSize = modelFileInCompactFormat.size();
        String prevBaseFormatFileEntry = "";
        for (int i = 1; i < compactFormatFileSize; i++) {
            String fileEntry = modelFileInCompactFormat.get(i);
            if (fileEntry == null || fileEntry.trim().contains("#")) {
                continue;
            }
//            fileEntry = fileEntry.replace("_", " ");
            System.out.println("Compact file entry " + fileEntry);
            String baseFormatFileEntry = "";
            int fileEntryLength = fileEntry.length();
            for (int j = 0; j < fileEntryLength; j++) {
                char singleSymbol = fileEntry.charAt(j);
                String divider = j < (fileEntryLength - 1) ? "," : "";
                String baseFormatEntry = "v" + (j+1) + "=" + singleSymbol + divider;
                baseFormatFileEntry = baseFormatFileEntry.concat(baseFormatEntry);
            }
            System.out.println("formatted compact file entry " + baseFormatFileEntry);
            if (i > 1) {
                String relation = baseFormatFileEntry + " ! " + prevBaseFormatFileEntry;
                modelFileInBaseFormat.add(relation);
            }
            prevBaseFormatFileEntry = baseFormatFileEntry;
        }
        for (String baseFormatFileEntry : modelFileInBaseFormat) {
            System.out.println("Base format file entry " + baseFormatFileEntry);
        }
        return modelFileInBaseFormat;
    }


    public static void main(String[] args) {
        String relFilePath = MODEL_IN_COMPACT_FORMAT_RELATIVE_FILE_PATH;
        String outputFileRelFilePath = MODEL_IN_BASE_FORMAT_RELATIVE_FILE_PATH;

        // Loading model as list of strings
        List<String> modelFileAsListOfStrings = loadModelFileAsListOfStrings(relFilePath);
        if (modelFileAsListOfStrings == null) {
            System.out.println("Model file is not loaded from: " + relFilePath);
            System.exit(0);
        }

        if (modelFileAsListOfStrings.isEmpty()) {
            System.out.println("Loaded model file " + relFilePath + " is empty.");
            System.exit(0);
        }

        String formatType = modelFileAsListOfStrings.get(0);
        String[] keyValueArray = formatType.split(":");
        if (keyValueArray.length != 2 || !keyValueArray[0].trim().equalsIgnoreCase(FILE_FORMAT_KEY)) {
            System.out.println("Loaded model file " + relFilePath + " has incorrect file format definition: " + formatType);
            System.exit(0);
        }

        // removing File Format Definition
        List<String> modelFileData = new ArrayList();
        for (int i = 1; i < modelFileAsListOfStrings.size(); i++) {
            String fileEntry = modelFileAsListOfStrings.get(i);
            modelFileData.add(fileEntry);
        }

        //
        // processing model file according type
        //

        if (keyValueArray[1].trim().equalsIgnoreCase(FILE_FORMAT_TYPE)) {
            List<String> modelFileInBaseFormat = convertCompactFormatToBaseFormat(modelFileData);
            FileIOUtils.writeTxtFileToUserDir(MODEL_IN_BASE_FORMAT_RELATIVE_FILE_PATH, modelFileInBaseFormat);
        }
    }
}
