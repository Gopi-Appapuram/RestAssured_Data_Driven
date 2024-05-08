package Utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading from and writing to Excel files.
 */
public class ExcelDataDriven {

    /**
     * Loads data from the specified Excel file and sheet into a list of hash maps.
     *
     * @param filename  The path to the Excel file.
     * @param sheetName The name of the sheet from which data will be loaded.
     * @return A list of hash maps containing the loaded data.
     * @throws IOException If an I/O error occurs while reading the Excel file.
     */
    public static List<HashMap<String, String>> loadDSheetData(String filename, String sheetName) throws IOException {
        String filePath = "src/test/resources/TestData/" + filename + ".xlsx";
        List<Map<String, String>> data = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);

        Sheet sheet = workbook.getSheet(sheetName); // Assuming the data is in the first sheet


        Row headerRow = sheet.getRow(0); // Assuming the first row contains headers
        List<String> columnName = new ArrayList<>();
        for (Cell cell : headerRow) {
            columnName.add(cell.getStringCellValue());
        }


        int numCols = headerRow.getLastCellNum();
        int numRows = sheet.getLastRowNum();

        /*for (int colIndex = 0; colIndex < numCols; colIndex++) {
            Cell headerCell = headerRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String header = headerCell.getStringCellValue().trim();
            headers.put(String.valueOf(colIndex + 1), header);
        }
        System.out.println(headers);*/

        List<HashMap<String, String>> excelData = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= numRows; rowIndex++) {

            Row currentRow = sheet.getRow(rowIndex);
            HashMap<String, String> Data = new HashMap<>();
            for (int col = 0; col < columnName.size(); col++) {
                Cell cell = currentRow.getCell(col);
                String cellValue = new DataFormatter().formatCellValue(cell);
                String colName = columnName.get(col);
                Data.put(colName, cellValue);

            }
            excelData.add(Data);

           /* Map<String, String> rowMap = new HashMap<>();
            for (int colIndex = 0; colIndex < numCols; colIndex++) {
                Cell headerCell = headerRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell currentCell = currentRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String header = headerCell.getStringCellValue().trim();
                String value = "";
                switch (currentCell.getCellType()) {
                    case STRING:
                        value = currentCell.getStringCellValue().trim();
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(currentCell)) {
                            value = currentCell.getLocalDateTimeCellValue().toString();
                        } else {
                            value = Double.toString(currentCell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        value = Boolean.toString(currentCell.getBooleanCellValue());
                        break;
                    case BLANK:
                        value = "";
                        break;
                    default:
                        value = "";
                        break;
                }
                rowMap.put(header, value);
            }
            data.add(rowMap);*/
        }

        fis.close();
        workbook.close();
        // System.out.println("Excel data: "+excelData);
        return excelData;
    }

    /**
     * Writes response data to the specified Excel file and sheet.
     *
     * @param ddtcs          The list of maps containing response data.
     * @param outputFilename The path to the output Excel file.
     * @param sheetName      The name of the sheet where data will be written.
     * @throws IOException If an I/O error occurs while writing to the Excel file.
     */
    public static void writeResponseToOutputFile(List<Map<String, String>> ddtcs, String outputFilename, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(outputFilename);
        Workbook workbook = WorkbookFactory.create(fis);

        Sheet sheet = workbook.getSheet(sheetName);

        // If the sheet does not exist, create a new one
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        // Create header row
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;
        for (String columnName : ddtcs.get(0).keySet()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(columnName);
        }

        // Write data from ddtcs map to Excel sheet
        int rowNum = 1; // Start from the second row as the first row is for headers
        for (Map<String, String> testcase : ddtcs) {
            Row row = sheet.createRow(rowNum++);
            // Iterate over each column in the row
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String columnName = headerRow.getCell(i).getStringCellValue();
                String value = testcase.get(columnName);
                Cell cell = row.createCell(i);
                cell.setCellValue(value);
            }
        }

        // Write the workbook to the output file
        try (FileOutputStream fos = new FileOutputStream(outputFilename)) {
            workbook.write(fos);
        }
    }


    /*public static void writeCustomResponse(String outputFilename, String sheetName, String headerName, String value) throws IOException {
        try (FileInputStream fis = new FileInputStream(outputFilename);
             Workbook workbook = WorkbookFactory.create(fis);
             FileOutputStream fos = new FileOutputStream(outputFilename)) {

            Sheet sheet = workbook.getSheet(sheetName);

            // If the sheet does not exist, create a new one
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            // Find the column index of the given header name
            int columnIndex = findColumnIndex(sheet, headerName);

            // If the header doesn't exist, create it at the end of the header row
            if (columnIndex == -1) {
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    headerRow = sheet.createRow(0);
                }
                columnIndex = headerRow.getLastCellNum(); // Append new header at the end
                Cell headerCell = headerRow.createCell(columnIndex);
                headerCell.setCellValue(headerName);
            }
            int rowIndex = 1;
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            Cell cell = row.createCell(columnIndex);
            cell.setCellValue(value);

            // Write the workbook back to the output file
            workbook.write(fos);
        }
    }*/

    /*private static int findColumnIndex(Sheet sheet, String headerName) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && cell.getStringCellValue().equalsIgnoreCase(headerName)) {
                    return i;
                }
            }
        }
        return -1; // Header not found
    }*/


    /**
     * Writes data to the specified Excel file and sheet.
     *
     * @param outputFilename The path to the output Excel file.
     * @param sheetName      The name of the sheet where data will be written.
     * @param data           A hash map containing the data to be written.
     * @throws IOException If an I/O error occurs while writing to the Excel file.
     */
    public static void writeDataToExcel(String outputFilename, String sheetName, HashMap<String, String> data) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(true);
             FileInputStream fis = new FileInputStream(outputFilename)) {

            Sheet sheet = workbook.getSheet(sheetName);

            // Write headers
            Row headerRow = sheet.createRow(0);
            int columnIdx = 0;
            for (String key : data.keySet()) {
                Cell cell = headerRow.createCell(columnIdx++);
                cell.setCellValue(key);
            }

            // Write data
            int rowIndex = 1;
            for (HashMap.Entry<String, String> entry : data.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                int lastCell = row.getLastCellNum();
                for (int i = 0; i <= lastCell; i++) {
                    row.createCell(i).setCellValue(entry.getValue());
                }

            }

            // Write the workbook back to the output file
            try (FileOutputStream fos = new FileOutputStream(outputFilename)) {
                workbook.write(fos);
            }
        }
    }


}
