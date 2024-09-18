package com.mfa.report.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mfa.report.model.Activity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class PDFService {
    public byte[] createActivityPdf(List<Activity> activities) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();


        Paragraph header1 = new Paragraph("Premier En-tête");
        document.add(header1);


        Paragraph header2 = new Paragraph("Deuxième En-tête");
        document.add(header2);


        PdfPTable table = new PdfPTable(7);


        table.addCell("Désignation");
        table.addCell("Tâche");
        table.addCell("Tâche Prochaine");
        table.addCell("Date");
        table.addCell("Observation");
        table.addCell("Prédiction");
        table.addCell("Autre");

        for (Activity activity : activities) {
            table.addCell(activity.getDescription());
            table.addCell(activity.getTaskList().get(0).getDescription());
            table.addCell(activity.getNexTaskList().get(0).getDescription());
            table.addCell(activity.getDueDatetime().toString());
            table.addCell(activity.getObservation());
            table.addCell(activity.getPrediction());
        }

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }
}
