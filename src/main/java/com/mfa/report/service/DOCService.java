package com.mfa.report.service;

import com.mfa.report.model.Activity;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class DOCService {
    public byte[] createActivityDoc(List<Activity> activities) throws IOException {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        XWPFParagraph header1 = document.createParagraph();
        header1.createRun().setText("Premier En-tête");


        XWPFParagraph header2 = document.createParagraph();
        header2.createRun().setText("Deuxième En-tête");

        // Créer un tableau pour les activités
        XWPFTable table = document.createTable();


        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("Désignation");
        headerRow.addNewTableCell().setText("Tâche");
        headerRow.addNewTableCell().setText("Tâche Prochaine");
        headerRow.addNewTableCell().setText("Date");
        headerRow.addNewTableCell().setText("Observation");
        headerRow.addNewTableCell().setText("Prédiction");
        headerRow.addNewTableCell().setText("Autre");

        for (Activity activity : activities) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(activity.getDescription());
            row.getCell(1).setText(activity.getTaskList().get(0).getDescription());
            row.getCell(2).setText(activity.getNexTaskList().get(0).getDescription());
            row.getCell(3).setText(activity.getDueDatetime().toString());
            row.getCell(4).setText(activity.getObservation());
            row.getCell(5).setText(activity.getPrediction());
        }

        document.write(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}
