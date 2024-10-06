package com.mfa.report.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FileService {
  private final MissionService missionService;
  private final DirectionService directionService;
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
      Document document = new Document(PageSize.A4.rotate(), 1f, 1f, 0f, 0f);
      PdfWriter.getInstance(document, byteArrayOutputStream);
      document.open();

      Paragraph title =
          new Paragraph(
              "COMPTE RENDU TRIMESTRIEL Mois de JUILLET",
              FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);
      document.add(Chunk.NEWLINE);

      // Create a table for missions and activities
      PdfPTable table = new PdfPTable(7); // 7 columns
      table.setWidthPercentage(100);

      // Add table headers
      addTableHeader(
          table,
          new String[] {
            "MISSIONS",
            "ACTIVITES",
            "PREVISIONS",
            "INDICATEUR DE PERFORMANCE",
            "REALISATION",
            "RECOMMENDATION",
            "OBSERVATION"
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
              PdfPCell missionCell =
                  new PdfPCell(new Phrase(missionDescription, fontUtils.toMissionTitle()));
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
                kpiCell.setBorderWidthTop(
                    0f); // Optional: you can manage KPI cell borders similarly if needed
                kpiCell.setBorderWidthBottom(
                    i == realizationCount - 1 ? 0f : 1f); // Same for KPI cell
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
          PdfPCell missionCell =
              new PdfPCell(new Phrase(missionDescription, fontUtils.toMissionTitle()));
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

  public byte[] createMissionReportExcel(List<Mission> missions, String dates) {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Create styles for the cells
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle titleCellStyle = createTitleCellStyle(workbook);
      CellStyle directionCellStyle = createDirectionCellStyle(workbook);
      CellStyle missionCellStyle = createMissionCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);

      // Group missions by directionId
      Map<String, List<Mission>> missionsByDirection = missions.stream()
              .collect(Collectors.groupingBy(mission -> mission.getDirection().getId()));

      // Track used sheet names to ensure uniqueness
      Set<String> usedSheetNames = new HashSet<>();

      // For each direction
      for (Map.Entry<String, List<Mission>> entry : missionsByDirection.entrySet()) {
        String directionId = entry.getKey();
        List<Mission> missionsForDirection = entry.getValue();
        String directionDescription = getDirectionDescriptionById(directionId);

        // Create a base sheet name from the direction ID (truncate to 25 chars to leave room for uniqueness counter)
        String baseSheetName = "Direction " + directionDescription;
        baseSheetName = truncateSheetName(baseSheetName, 25); // Truncate the name if necessary

        // Generate a unique sheet name
        String sheetName = makeUniqueSheetName(workbook, baseSheetName, usedSheetNames);

        // Create a new sheet for each direction
        Sheet sheet = workbook.createSheet(sheetName);

        // Create title, direction, and header rows
        createTitleRow(sheet, titleCellStyle, dates);
        createDirectionRow(sheet, directionCellStyle, directionDescription);
        createHeaderRow(sheet, headerCellStyle);

        // Add mission and activity data
        int rowIdx = 4;
        for (Mission mission : missionsForDirection) {
          rowIdx = addMissionData(sheet, rowIdx, mission, missionCellStyle, activityCellStyle);
        }

        // Auto-size columns for better readability
        autoSizeColumns(sheet);
      }

      // Write the workbook to the output stream
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String truncateSheetName(String sheetName, int maxLength) {
    // Truncate the name to a safe length for Excel sheets (31 characters is the limit)
    return sheetName.length() > maxLength ? sheetName.substring(0, maxLength) : sheetName;
  }

  private String makeUniqueSheetName(Workbook workbook, String baseName, Set<String> usedSheetNames) {
    String uniqueName = baseName;
    int counter = 1;

    // Ensure the sheet name is unique by appending a counter if needed
    while (usedSheetNames.contains(uniqueName) || workbook.getSheet(uniqueName) != null) {
      uniqueName = baseName + " (" + counter + ")";
      counter++;
    }

    usedSheetNames.add(uniqueName);
    return uniqueName;
  }

  private CellStyle createHeaderCellStyle(Workbook workbook) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontHeightInPoints((short) 12);

    CellStyle headerCellStyle = workbook.createCellStyle();
    headerCellStyle.setFont(headerFont);
    headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
    headerCellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    return headerCellStyle;
  }

  private CellStyle createTitleCellStyle(Workbook workbook) {
    Font titleFont = workbook.createFont();
    titleFont.setBold(true);
    titleFont.setFontHeightInPoints((short) 16);

    CellStyle titleCellStyle = workbook.createCellStyle();
    titleCellStyle.setFont(titleFont);
    titleCellStyle.setAlignment(HorizontalAlignment.CENTER);

    return titleCellStyle;
  }

  private CellStyle createDirectionCellStyle(Workbook workbook) {
    Font titleFont = workbook.createFont();
    titleFont.setBold(true);
    titleFont.setFontHeightInPoints((short) 16);

    CellStyle directionCellStyle = workbook.createCellStyle();
    directionCellStyle.setFont(titleFont);
    directionCellStyle.setAlignment(HorizontalAlignment.LEFT);

    return directionCellStyle;
  }

  private CellStyle createMissionCellStyle(Workbook workbook) {
    Font missionFont = workbook.createFont();
    missionFont.setBold(true);
    missionFont.setFontHeightInPoints((short) 15);

    CellStyle missionCellStyle = workbook.createCellStyle();
    missionCellStyle.setFont(missionFont);
    missionCellStyle.setAlignment(HorizontalAlignment.CENTER);
    missionCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    return missionCellStyle;
  }

  private CellStyle createActivityCellStyle(Workbook workbook) {
    Font activityFont = workbook.createFont();
    activityFont.setFontHeightInPoints((short) 13);

    CellStyle activityCellStyle = workbook.createCellStyle();
    activityCellStyle.setFont(activityFont);
    activityCellStyle.setAlignment(HorizontalAlignment.CENTER);
    activityCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    return activityCellStyle;
  }

  private void createTitleRow(Sheet sheet, CellStyle titleCellStyle, String dates) {
    Row titleRow = sheet.createRow(0);
    Cell titleCell = titleRow.createCell(2);
    titleCell.setCellValue("Mission Report - " + dates);
    titleCell.setCellStyle(titleCellStyle);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
  }

  private void createDirectionRow(Sheet sheet, CellStyle directionCellStyle, String directionName) {
    Row directionRow = sheet.createRow(1);
    Cell directionCell = directionRow.createCell(0);
    directionCell.setCellValue("Direction: " + directionName);
    directionCell.setCellStyle(directionCellStyle);
  }

  private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle) {
    Row headerRow = sheet.createRow(3);
    String[] headers = {
            "MISSIONS",
            "ACTIVITES",
            "PREVISIONS",
            "INDICATEUR DE PERFORMANCE",
            "REALISATION",
            "RECOMMENDATION",
            "OBSERVATION"
    };

    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerCellStyle);
    }
  }

  private int addMissionData(Sheet sheet, int rowIdx, Mission mission, CellStyle missionCellStyle, CellStyle activityCellStyle) {
    List<Activity> activities = mission.getActivity();
    int totalPerformanceIndicators = activities.stream().mapToInt(activity -> activity.getPerformanceRealization().size()).sum();

    for (int i = 0; i < activities.size(); i++) {
      Activity activity = activities.get(i);
      List<PerformanceRealization> realizations = activity.getPerformanceRealization();
      int realizationRows = realizations.size();

      for (int j = 0; j < realizationRows; j++) {
        Row row = sheet.createRow(rowIdx);

        // Merge mission cell if more than one row to merge
        if (rowIdx == 4 || (i == 0 && j == 0)) {
          if (totalPerformanceIndicators > 1) {
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + totalPerformanceIndicators - 1, 0, 0));
          }
          Cell missionCell = row.createCell(0);
          missionCell.setCellValue(mission.getDescription());
          missionCell.setCellStyle(missionCellStyle);
        }

        // Merge activity and forecast cells if more than one row to merge
        if (j == 0) {
          if (realizationRows > 1) {
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + realizationRows - 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + realizationRows - 1, 2, 2));
          }

          Cell activityCell = row.createCell(1);
          activityCell.setCellValue(activity.getDescription());
          activityCell.setCellStyle(activityCellStyle);

          Cell predictionCell = row.createCell(2);
          predictionCell.setCellValue(activity.getPrediction());
          predictionCell.setCellStyle(activityCellStyle);
        }

        // Complete the achievements
        PerformanceRealization realization = realizations.get(j);
        row.createCell(3).setCellValue(realization.getKPI());
        row.createCell(4).setCellValue(realization.getRealization());

        rowIdx++;
      }

      // Add recommendations and observation
      addRecommendationsAndObservation(sheet, rowIdx - 1, activity);
    }

    return rowIdx;
  }

  private void addRecommendationsAndObservation(Sheet sheet, int rowIdx, Activity activity) {
    List<Recommendation> recommendations = activity.getRecommendations();
    if (!recommendations.isEmpty()) {
      StringBuilder recommendationText = new StringBuilder();
      for (Recommendation recommendation : recommendations) {
        recommendationText.append("• ").append(recommendation.getDescription()).append("\n");
      }
      sheet.getRow(rowIdx).createCell(5).setCellValue(recommendationText.toString());
    } else {
      sheet.getRow(rowIdx).createCell(5).setCellValue("");
    }

    sheet.getRow(rowIdx).createCell(6).setCellValue(activity.getObservation());
  }

  private void autoSizeColumns(Sheet sheet) {
    for (int i = 0; i < 7; i++) {
      sheet.autoSizeColumn(i);
    }
    sheet.setColumnWidth(1, 10000);
    sheet.setColumnWidth(2, 10000);
  }

  private String getDirectionDescriptionById(String directionId) {
    return directionService.getDirectionById(directionId).getName();
  }

}
