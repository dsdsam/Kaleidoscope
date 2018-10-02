package adf.utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/5/2017.
 */
public class FileIOUtils {

     /*
     This class contains method to read and write txt files
        1) from/to classpath
        2) from/to file system

     Naming convention:

       fileName - just the name of the file

       absDirPath - the absolute directory where the file is located
       absFilePath - absolute path that includes fileDir/fileName

       dirClassPath - the relative path to the location of the file e.g. /dir/dir
       fileClassPath - the relative path to a file e.g. /package/.../package/fileName
    */

    public static final List<String> loadTxtFileFromUserDirAsListAsItIs(String fileClassPath) {
        String dirPath = System.getProperty("user.dir");
        String absFilePath = dirPath + fileClassPath;
        List<String> fileAslist = loadTxtFileAsListOfStrings(absFilePath, true);
        return fileAslist;
    }

    public static final List<String> loadTxtFileFromUserDirAsListSkipEmptyLines(String fileClassPath) {
        String dirPath = System.getProperty("user.dir");
        String absFilePath = dirPath + fileClassPath;
        List<String> fileAslist = loadTxtFileAsListOfStrings(absFilePath, false);
        return fileAslist;
    }

    /**
     * @param fileClassPath
     * @return
     */
    public static final String loadTxtFileFromClassPathAsStringAsItIs(String fileClassPath) {
        List<String> listOfLines = loadTxtFileAsListOfStringsFromClassPathAsItIs(fileClassPath);
        String file = ListOfLinesToString(listOfLines);
        return file;
    }

    public static final String loadTxtFileFromClassPathAsStringSkipEmptyLines(String fileClassPath) {
        List<String> listOfLines = loadTxtFileAsListOfStringsFromClassPathSkipEmptyLines(fileClassPath);
        String file = ListOfLinesToString(listOfLines);
        return file;
    }

    /**
     * @param fileClassPath
     * @return
     */
    public static final List<String> loadTxtFileAsListOfStringsFromClassPathAsItIs(String fileClassPath) {
        List<String> loadedFile = new ArrayList();
        URL fileURL = FileIOUtils.class.getResource(fileClassPath);
        if (fileURL == null) {
            return loadedFile;
        }
        String absFilePath = fileURL.getFile();
        loadedFile = loadTxtFileAsListOfStrings(absFilePath, true);
        return loadedFile;
    }

    /**
     * @param fileClassPath
     * @return
     */
    public static final List<String> loadTxtFileAsListOfStringsFromClassPathSkipEmptyLines(String fileClassPath) {
        List<String> loadedFile = new ArrayList();
        URL fileURL = FileIOUtils.class.getResource(fileClassPath);
        if (fileURL == null) {
            return loadedFile;
        }
        String absFilePath = fileURL.getFile();
        loadedFile = loadTxtFileAsListOfStrings(absFilePath, false);
        return loadedFile;
    }


    /**
     * When writing to classpath the classpath should exist, otherwise it will not be found
     *
     * @param fileClassPath
     * @param fileToWrite
     * @return
     */
    public static final boolean writeListOfStringsAsTxtFileToClassPath(String fileClassPath, List<String> fileToWrite) {
        URL fileURL = FileIOUtils.class.getResource(fileClassPath);
        if (fileURL == null) {
            return false;
        }
        String filePath = fileURL.getFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String fileLine : fileToWrite) {
                System.out.println("writing line: " + fileLine);
                writer.println(fileLine);
            }
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    /**
     * This method writes file relative to User Dir
     *
     * @param fileClassPath
     * @param fileToWrite
     * @return
     */
    public static final boolean writeTxtFileToUserDir(String fileClassPath, List<String> fileToWrite) {
        URL fileURL = FileIOUtils.class.getResource(fileClassPath);
        if (fileURL == null) {
            return false;
        }
        String dirPath = System.getProperty("user.dir");
        String filePath = dirPath + fileClassPath;
        File file = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!file.exists()) {
            boolean created = file.mkdirs();
            if (!created) {
                return false;
            }
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String fileLine : fileToWrite) {
//                System.out.println("writing line: " + fileLine);
                writer.println(fileLine);
            }
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    //
    //  E x c e l   f i l e   l o a d e r  (works with POI)
    //

    /**
     * Loads specified Excel file from file system as list of Strings. Column
     * values withing each string are separated by provided char separator
     *
     * @param selectedPathToFixingRatesFile
     * @param columnSeparator
     * @return
     */
//    public static final List<String> loadExcelFile(File selectedPathToFixingRatesFile, char columnSeparator) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        List<String> readExcelRows = new ArrayList();
//        Workbook workbook = null;
//
//        //Create the input stream from the xlsx/xls file
//
//        try (FileInputStream fis = new FileInputStream(selectedPathToFixingRatesFile)) {
//            //Create Workbook instance for xlsx/xls file input stream
//            String fileName = selectedPathToFixingRatesFile.getAbsolutePath();
//            if (fileName.toLowerCase().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(fis);
//            } else if (fileName.toLowerCase().endsWith("xls")) {
//                workbook = new HSSFWorkbook(fis);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //Get the number of sheets in the xlsx file
//        int numberOfSheets = workbook.getNumberOfSheets();
//
//        //loop through each of the sheets
//        for (int i = 0; i < numberOfSheets; i++) {
//            //Get the nth sheet from the workbook
//            Sheet sheet = workbook.getSheetAt(i);
//
//            //every sheet has rows, iterate over them
//            Iterator<Row> rowIterator = sheet.iterator();
//            while (rowIterator.hasNext()) {
//                String value = "";
//
//                //Get the row object
//                Row row = rowIterator.next();
//
//                //Every row has columns, get the column iterator and iterate over them
//                Iterator<Cell> cellIterator = row.cellIterator();
//                int columnIndex = 0;
////                List<String> readExcelRow = new ArrayList();
//                stringBuilder.delete(0, stringBuilder.length());
//                while (cellIterator.hasNext()) {
//
//                    //Get the Cell object
//                    Cell cell = cellIterator.next();
//                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                        value = cell.getStringCellValue().trim();
//                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                        double rate = cell.getNumericCellValue();
//                        value = String.valueOf(rate);
//                    } else {
//                        new Exception("Unknown Cell type " + cell.getCellType()).printStackTrace();
//                    }
//
//                    stringBuilder.append(value).append(columnSeparator);
////                    readExcelRow.add(value);
//                    columnIndex++;
//                }
//                readExcelRows.add(stringBuilder.toString());
//            }
//        }
//        return readExcelRows;
//    }

    /**
     * Loads specified text file from file system as list of Strings
     *
     * @return
     */
    private static final List<String> loadTxtFileAsListOfStrings(String absFilePath, boolean preservEmptyLines) {
        String fileName = absFilePath.substring(0, absFilePath.lastIndexOf("/"));
        File file = new File(absFilePath.substring(0, absFilePath.lastIndexOf("/")));
        if (!file.exists()) {
            return null;
        }
        List<String> loadedFile = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(absFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!preservEmptyLines && line.trim().isEmpty()) {
                    continue;
                } else {
                    loadedFile.add(line);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException ioe) {
        }
        return loadedFile;
    }

    /**
     * This method converts list of strings to single string
     *
     * @param listOfLines
     * @return
     */
    private static final String ListOfLinesToString(List<String> listOfLines) {
        if (listOfLines == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : listOfLines) {
            sb.append(line);
        }
        return sb.toString();
    }

}
