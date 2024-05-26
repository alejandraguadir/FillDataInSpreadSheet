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

    public void duplicateData(List<List<Object>> data, int totalRowsToPopulate) {
        Sheet sheet = workbook.getSheetAt(0); // Hoja de cálculo de entrada
        int numRowsInput = data.size(); // Número de filas en el conjunto de datos de entrada
        int numRowsOutput = sheet.getLastRowNum() + 1; // Número de filas actualmente en la hoja de cálculo de salida
        int numRowsToAdd = totalRowsToPopulate - numRowsOutput; // Número de filas a agregar a la hoja de cálculo de salida

        while (numRowsToAdd > 0) {
            for (int i = 0; i < numRowsInput && numRowsToAdd > 0; i++) {
                Row inputRow = sheet.getRow(i); // Obtener la fila del conjunto de datos de entrada
                Row newRow = sheet.createRow(numRowsOutput++); // Crear una nueva fila en la hoja de cálculo de salida
                for (int j = 0; j < inputRow.getLastCellNum(); j++) {
                    Cell inputCell = inputRow.getCell(j); // Obtener la celda de la fila del conjunto de datos de entrada
                    Cell newCell = newRow.createCell(j); // Crear una nueva celda en la fila de salida
                    if (inputCell != null) {
                        Object cellValue = getCellValue(inputCell);
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
                numRowsToAdd--;
            }
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
            // Obtener la cantidad de filas a poblar de una variable de entorno llamada "NUM_ROWS_TO_POPULATE"
            int totalRowsToPopulate = Integer.parseInt(System.getenv("NUM_ROWS"));
            duplicateData(data, totalRowsToPopulate);
            saveWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExcelDuplicator excelDuplicator() {
        return new ExcelDuplicator();
    }
}
