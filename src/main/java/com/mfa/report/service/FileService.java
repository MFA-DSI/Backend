package com.mfa.report.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;
import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.model.Recommendation;
import com.mfa.report.service.utils.FontUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FileService {
  private final MissionService missionService;
  private final FontUtils fontUtils;

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
      //    table.addCell(activity.getTaskList().get(0).getDescription());
      //  table.addCell(activity.getNexTaskList().get(0).getDescription());
      table.addCell(activity.getDueDatetime().toString());
      table.addCell(activity.getObservation());
      table.addCell(activity.getPrediction());
    }

    document.add(table);
    document.close();

    return outputStream.toByteArray();
  }

  public byte[] createMissionReport(List<Mission> missions) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      Document document = new Document(PageSize.A4.rotate());
      PdfWriter.getInstance(document, byteArrayOutputStream);
      document.open();

      Paragraph title = new Paragraph("COMPTE RENDU TRIMESTRIEL Mois de JUILLET", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);
      document.add(Chunk.NEWLINE);

      // Create a table for missions and activities
      PdfPTable table = new PdfPTable(7); // 7 columns
      table.setWidthPercentage(100);

      // Add table headers
      addTableHeader(table, new String[] {
              "MISSIONS", "ACTIVITES", "PREVISIONS", "INDICATEUR DE PERFORMANCE",
              "REALISATION", "RECOMMENDATION", "OBSERVATION"
      });

      // Iterate through each mission
      for (Mission mission : missions) {
        String missionDescription = mission.getDescription();
        boolean isFirstActivity = true;
        int activityCount = mission.getActivity().size();

        // Check if the mission has activities
        if (activityCount > 0) {
          for (Activity activity : mission.getActivity()) {
            // Add the mission description only once for the first activity
            if (isFirstActivity) {
              PdfPCell missionCell = new PdfPCell(new Phrase(missionDescription, fontUtils.toMissionTitle()));
              missionCell.setRowspan(activityCount);
              missionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
              missionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
              table.addCell(missionCell);
              isFirstActivity = false;
            }

            // Add activity details
            table.addCell(activity.getDescription());
            table.addCell(activity.getPrediction());

            if (!activity.getPerformanceRealization().isEmpty()) {
              // Table for Performance Realizations
              PdfPTable realizationTable = new PdfPTable(1);
              realizationTable.setWidthPercentage(104); // Adjusted to match the first table

              // Table for KPIs
              PdfPTable kpiTable = new PdfPTable(1);
              kpiTable.setWidthPercentage(104);
              int realizationCount = activity.getPerformanceRealization().size();
              List<PerformanceRealization> realizations = activity.getPerformanceRealization();

              for (int i = 0; i < realizationCount; i++) {
                PerformanceRealization realization = realizations.get(i);

                // Create realization cell
                PdfPCell realizationCell = new PdfPCell(new Phrase(realization.getRealization()));
                realizationCell.setBorderWidthTop(0f);
                // Set border bottom to 1f only for the last element
                realizationCell.setBorderWidthBottom(i == realizationCount - 1 ? 0f : 1f);
                realizationTable.addCell(realizationCell);

                // Create KPI cell
                PdfPCell kpiCell = new PdfPCell(new Phrase(String.valueOf(realization.getKPI())));
                kpiCell.setBorderWidthTop(0f); // Optional: you can manage KPI cell borders similarly if needed
                kpiCell.setBorderWidthBottom(i == realizationCount - 1 ? 0f : 1f); // Same for KPI cell
                kpiTable.addCell(kpiCell);
              }

              // Add both tables to the main table
              PdfPCell realizationContainer = new PdfPCell();
              realizationContainer.addElement(realizationTable);
              table.addCell(realizationContainer);

              PdfPCell kpiContainer = new PdfPCell();
              kpiContainer.addElement(kpiTable);
              table.addCell(kpiContainer);
            } else {
              table.addCell(""); // Empty cell for no realization
              table.addCell(""); // Empty cell for no KPI
            }

            // Add recommendations if available
            if (!activity.getRecommendations().isEmpty()) {
              PdfPCell recommendationCell = new PdfPCell();
              Paragraph recommendationContent = new Paragraph();
              for (Recommendation recommendation : activity.getRecommendations()) {
                recommendationContent.add("• " + recommendation.getDescription() + "\n");
              }
              recommendationCell.addElement(recommendationContent);
              table.addCell(recommendationCell);
            } else {
              table.addCell("Aucune recommandation");
            }

            // Add observations
            table.addCell(activity.getObservation());
          }
        } else {
          // Add mission even if no activities
          PdfPCell missionCell = new PdfPCell(new Phrase(missionDescription, fontUtils.toMissionTitle()));
          missionCell.setColspan(7); // Span across all columns if no activities
          missionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          missionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
          table.addCell(missionCell);
        }
      }

      // Add the table to the document
      document.add(table);
      document.close();
      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void addTableHeader(PdfPTable table, String[] headers) {
    for (String header : headers) {
      PdfPCell headerCell = new PdfPCell(new Phrase(header));
      headerCell.setBackgroundColor(BaseColor.GREEN.darker());
      headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(headerCell);
    }
  }

  public ByteArrayResource createExcelFile(List<String> ids) throws IOException {
    List<Mission> missions = missionService.findMissionsByIds(ids);
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Missions");

    // Create header row
    Row headerRow = sheet.createRow(0); // Ligne d'entête au tout début
    String[] headers = {
      "MISSIONS",
      "ACTIVITES",
      "PREVISIONS",
      "INDICATEUR DE PERFORMANCE",
      "REALISATION",
      "RECOMMANDATION",
      "OBSERVATION"
    };

    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(createHeaderCellStyle(workbook));
    }

    // Data rows
    int rowIndex = 1; // Ligne de données commence après les entêtes
    for (Mission mission : missions) {
      String missionDescription = mission.getDescription();
      boolean isFirstActivity = true;

      // Si pas d'activités, créer une ligne vide avec juste la mission
      if (mission.getActivity().isEmpty()) {
        Row dataRow = sheet.createRow(rowIndex++);
        dataRow.createCell(0).setCellValue(missionDescription);
        for (int j = 1; j < headers.length; j++) {
          dataRow.createCell(j).setCellValue(""); // Remplir les colonnes vides
        }
      }

      for (Activity activity : mission.getActivity()) {
        Row dataRow = sheet.createRow(rowIndex++);

        // Ajouter la description de la mission seulement pour la première activité
        if (isFirstActivity) {
          dataRow.createCell(0).setCellValue(missionDescription);
          isFirstActivity = false;
        } else {
          dataRow
              .createCell(0)
              .setCellValue(""); // Cellule vide pour les autres lignes de la même mission
        }

        dataRow
            .createCell(1)
            .setCellValue(activity.getDescription() != null ? activity.getDescription() : "");
        dataRow
            .createCell(2)
            .setCellValue(activity.getPrediction() != null ? activity.getPrediction() : "");

        // Concaténer les indicateurs de performance (KPI, réalisation, type)
        String performanceIndicators =
            activity.getPerformanceRealization().stream()
                .map(
                    perf ->
                        String.format(
                            "KPI: %d, Réalisation: %s, Type: %s",
                            perf.getKPI(), perf.getRealization(), perf.getRealizationType()))
                .collect(Collectors.joining("; ")); // Séparé par "; " pour chaque indicateur

        dataRow.createCell(3).setCellValue(performanceIndicators);

        // Concaténer les réalisations
        String realizations =
            activity.getPerformanceRealization().stream()
                .map(PerformanceRealization::getRealization)
                .collect(Collectors.joining("; ")); // Séparer par "; " pour chaque réalisation

        dataRow.createCell(4).setCellValue(realizations);

        // Concaténer les recommandations
        String recommendations =
            activity.getRecommendations().stream()
                .map(
                    rec ->
                        String.format(
                            "Description: %s, Date: %s, Approuvée: %s",
                            rec.getDescription(),
                            rec.getCreationDatetime() != null
                                ? rec.getCreationDatetime()
                                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                : "N/A",
                            rec.isApproved() ? "Oui" : "Non"))
                .collect(Collectors.joining("; ")); // Séparé par "; " pour chaque recommandation

        dataRow.createCell(5).setCellValue(recommendations);

        dataRow
            .createCell(6)
            .setCellValue(activity.getObservation() != null ? activity.getObservation() : "");
      }
    }

    // Auto-size columns
    for (int i = 0; i < headers.length; i++) {
      sheet.autoSizeColumn(i);
    }

    // Write to output stream
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();

    return new ByteArrayResource(outputStream.toByteArray());
  }

  private CellStyle createHeaderCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 12);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }
}
