package com.mfa.report.service;

import com.mfa.report.model.Activity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    public byte[] createActivityExcel(List<Activity> activities) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Activities");

        // Créer la première ligne pour les en-têtes
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Désignation", "Tâche", "Tâche Prochaine", "Date", "Observation", "Prédiction", "Autre"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Ajouter les données des activités
        int rowNum = 1;
        for (Activity activity : activities) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(activity.getDescription());
            row.createCell(1).setCellValue(activity.getTaskList().get(0).getDescription());
            row.createCell(2).setCellValue(activity.getNexTaskList().get(0).getDescription());
            row.createCell(3).setCellValue(activity.getDueDatetime());
            row.createCell(4).setCellValue(activity.getObservation());
            row.createCell(5).setCellValue(activity.getPrediction());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
