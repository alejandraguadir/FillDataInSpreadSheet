package com.automation.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDuplicator implements Task {

    private String filePath;
    private String sheetName;
    private String outputFilePath;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelDuplicator() {
        this.filePath = System.getenv("INPUT_FILE");
        this.sheetName = System.getenv("EXCEL_SHEET");
        this.outputFilePath = System.getenv("OUTPUT_FILE");
    }

    public void loadWorkbook() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            this.workbook = new XSSFWorkbook(fis);
            if (sheetName != null && !sheetName.isEmpty()) {
                this.sheet = workbook.getSheet(sheetName);
            } else {
                this.sheet = workbook.getSheetAt(0);
            }
        }
    }

    public List<List<Object>> readSheet() {
        List<List<Object>> data = new ArrayList<>();
        for (Row row : sheet) {
            List<Object> rowData = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowData.add(cell.getBooleanCellValue());
                        break;
                    default:
                        rowData.add(null);
                }
            }
            data.add(rowData);
        }
        return data;
    }

    public void duplicateData(List<List<Object>> data) {
        int maxRow = sheet.getLastRowNum() + 1;
        int maxCol = sheet.getRow(0).getLastCellNum();
        int dataIndex = 0;

        for (int rowIndex = 0; rowIndex < maxRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            for (int colIndex = 0; colIndex < maxCol; colIndex++) {
                Cell cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.createCell(colIndex);
                }
                List<Object> rowData = data.get(dataIndex / maxCol);
                Object value = rowData.get(dataIndex % maxCol);
                setCellValue(cell, value);
                dataIndex++;
                if (dataIndex >= data.size() * maxCol) {
                    dataIndex = 0;
                }
            }
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue("");
        }
    }

    public void saveWorkbook() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            workbook.write(fos);
        }
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        System.out.println(filePath);
        System.out.println(outputFilePath);
        System.out.println(sheetName);

        // Cargar la hoja de cálculo y duplicar los datos
        try {
            loadWorkbook();
            List<List<Object>> data = readSheet();
            duplicateData(data);
            saveWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static ExcelDuplicator excelDuplicator() {
        return new ExcelDuplicator();
    }
}



