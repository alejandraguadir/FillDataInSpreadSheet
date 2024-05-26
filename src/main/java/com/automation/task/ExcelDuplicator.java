package com.automation.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDuplicator implements Task {

    private String filePath;
    private String outputFilePath;
    private Workbook workbook;

    public ExcelDuplicator() {
        this.filePath = System.getenv("INPUT_FILE");
        this.outputFilePath = System.getenv("OUTPUT_FILE");
    }

    public void loadWorkbook() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            this.workbook = new XSSFWorkbook(fis);
        }
    }

    public List<List<Object>> readSheet() {
        List<List<Object>> data = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet is used
        for (Row row : sheet) {
            List<Object> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(getCellValue(cell));
            }
            data.add(rowData);
        }
        return data;
    }

    public void duplicateData(List<List<Object>> data) {
        Sheet sheet = workbook.getSheetAt(0); // Hoja de cálculo de entrada

        // Obtener la última fila en la hoja de cálculo de salida
        int lastRowNum = sheet.getLastRowNum();

        // Duplicar los datos hasta que la última fila de la hoja de cálculo de salida llegue a la fila 1048575
        while (lastRowNum < 1000) { // Última fila - 1
            Row lastRow = sheet.getRow(lastRowNum);
            Row newRow = sheet.createRow(lastRowNum + 1); // Crear una nueva fila después de la última fila
            for (int i = 0; i < lastRow.getLastCellNum(); i++) {
                Cell lastCell = lastRow.getCell(i);
                Cell newCell = newRow.createCell(i);
                if (lastCell != null) {
                    Object cellValue = getCellValue(lastCell);
                    // Conversión explícita de tipo
                    if (cellValue instanceof String) {
                        newCell.setCellValue((String) cellValue);
                    } else if (cellValue instanceof Double) {
                        newCell.setCellValue((Double) cellValue);
                    } else if (cellValue instanceof Boolean) {
                        newCell.setCellValue((Boolean) cellValue);
                    }
                }
            }
            lastRowNum++;
        }
    }


    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
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

