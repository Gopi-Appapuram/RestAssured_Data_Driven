package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    public static List<LinkedHashMap<String, String>> loadDSheetData(String filename, String sheetName) throws IOException, InvalidFormatException {
        String filePath = "src/test/resources/TestData/" + filename + ".xlsx";
        List<LinkedHashMap<String, String>> excelData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                sheet = workbook.createSheet(sheetName); // Create sheet if not exists
            }

            Row headerRow = sheet.getRow(0); // Assuming the first row contains headers
            List<String> columnNames = new ArrayList<>();
            for (Cell cell : headerRow) {
                columnNames.add(cell.getStringCellValue());
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                for (int col = 0; col < columnNames.size(); col++) {
                    Cell cell = currentRow.getCell(col);
                    String cellValue = new DataFormatter().formatCellValue(cell);
                    data.put(columnNames.get(col), cellValue);
                }
                excelData.add(data);
            }
        }
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
    /*public static void writeResponseToOutputFile(List<LinkedHashMap<String, String>> ddtcs, String outputFilename, String sheetName) throws IOException, InvalidFormatException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\" + outputFilename + ".xlsx";


        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                sheet = workbook.createSheet(sheetName); // Create sheet if not exists
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
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    String columnName = headerRow.getCell(i).getStringCellValue();
                    String value = testcase.get(columnName);
                    Cell cell = row.createCell(i);
                    cell.setCellValue(value);
                }
            }

            // Write the workbook to the output file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }*/

    /**
     * Writes data to the specified Excel file and sheet.
     *
     * @param data           The list of maps containing data to be written.
     * @param outputFilename The path to the output Excel file.
     * @param sheetName      The name of the sheet where data will be written.
     * @throws IOException If an I/O error occurs while writing to the Excel file.
     */
    public static void writeDataToExcel(List<LinkedHashMap<String, String>> data, String outputFilename, String sheetName) throws IOException, InvalidFormatException {

        String uniqueDirPath = System.getProperty("user.dir") + "\\DataDrivenResults_" + DateFormater.formatPresentDateTime();
        // Create the directory if it does not exist
        Files.createDirectories(Paths.get(uniqueDirPath));

        // Construct the file path with the unique directory
        String filePath = uniqueDirPath + "\\" + outputFilename + ".xlsx";

        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;

        if (file.exists()) {
            file.delete();
            workbook = new XSSFWorkbook(); // Create a new workbook if file does not exist
            sheet = workbook.createSheet(sheetName);
        } else {
            workbook = new XSSFWorkbook(); // Create a new workbook if file does not exist
            sheet = workbook.createSheet(sheetName);
        }

        if (sheet == null) {
            sheet = workbook.createSheet(sheetName); // Create sheet if it does not exist
        }

        // Write headers
        if (!data.isEmpty()) {
            Row headerRow = sheet.createRow(0);
            LinkedHashMap<String, String> firstData = data.get(0);
            int columnIdx = 0;
            for (String key : firstData.keySet()) {
                Cell cell = headerRow.createCell(columnIdx++);
                cell.setCellValue(key);
            }
        }

        // Write data
        int rowIndex = 1;
        for (LinkedHashMap<String, String> map : data) {
            Row row = sheet.createRow(rowIndex++);
            int cellIdx = 0;
            for (String key : map.keySet()) {
                row.createCell(cellIdx++).setCellValue(map.get(key));
            }
        }

        // Apply styles and auto-size columns (optional, implement as needed)
        applyStylesAndAutoSizeColumns(sheet, workbook);

        // Write the workbook to the output file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * Applies styles and auto-sizes columns for the sheet.
     *
     * @param sheet    The sheet to apply styles to.
     * @param workbook The workbook containing the sheet.
     */
    private static void applyStylesAndAutoSizeColumns(Sheet sheet, Workbook workbook) {
        int rowLength = sheet.getLastRowNum();
        for (int i = 0; i <= rowLength; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                CellStyle style = createCellStyle(workbook);
                if (i == 0) {
                    applyColorToCell(style, "SKY_BLUE");
                } else{
                    applyConditionalFormatting(cell, style);
                }
                sheet.autoSizeColumn(j);
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * Creates a cell style with default settings.
     *
     * @param workbook The workbook to create the cell style for.
     * @return The created cell style.
     */
    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        return style;
    }

    /**
     * Applies conditional formatting to the cell based on its value.
     *
     * @param cell  The cell to apply formatting to.
     * @param style The cell style to apply.
     */
    private static void applyConditionalFormatting(Cell cell, CellStyle style) {
        String passColorName = "GREEN";
        String failColorName = "RED";

        if (cell.getStringCellValue().equalsIgnoreCase("pass")) {
            applyColorToCell(style, passColorName);
        } else if (cell.getStringCellValue().equalsIgnoreCase("fail")) {
            applyColorToCell(style, failColorName);
        }
    }

    /**
     * Applies the specified color to the cell style.
     *
     * @param style       The cell style to apply the color to.
     * @param colorName   The name of the color to apply.
     */
    private static void applyColorToCell(CellStyle style, String colorName) {
        try {
            IndexedColors indexedColor = IndexedColors.valueOf(colorName);
            short colorIndex = indexedColor.getIndex();
            style.setFillForegroundColor(colorIndex);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = new XSSFFont();
            font.setBold(true);
            style.setFont(font);
        } catch (IllegalArgumentException e) {
            System.out.println("Color name not found: " + colorName);
        }
    }
}
