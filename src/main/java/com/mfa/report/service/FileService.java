package com.mfa.report.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mfa.report.endpoint.rest.controller.utils.LocalDateUtils;
import com.mfa.report.model.*;
import com.mfa.report.service.utils.FontUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@AllArgsConstructor
public class FileService {
  private final MissionService missionService;
  private final DirectionService directionService;
  private final ActivityService activityService;
  private final FontUtils fontUtils;
  private final LocalDateUtils dateUtils;
  private final TemplateEngine templateEngine;

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

  public byte[] createMissionReport(List<Mission> missions, String month) {
    Context context = new Context();
    context.setVariable("missions", missions != null ? missions : Collections.emptyList());
    context.setVariable("month", month);

    String htmlContent = templateEngine.process("missionReport", context);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      HtmlConverter.convertToPdf(htmlContent, outputStream);
      return outputStream.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public byte[] createMissionReportExcelForDirection(List<Mission> missions, String dates, String directionId) {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Create styles for the cells
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle titleCellStyle = createTitleCellStyle(workbook);
      CellStyle directionCellStyle = createDirectionCellStyle(workbook);
      CellStyle missionCellStyle = createMissionCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);
      CellStyle noActivityCellStyle = createNoActivityCellStyle(workbook); // Style for the no activity message

      // Filter missions by the provided directionId
      List<Mission> missionsForDirection = missions.stream()
              .filter(mission -> mission.getDirection().getId().equals(directionId))
              .collect(Collectors.toList());

      // Create a sheet for the direction
      String directionDescription = getDirectionDescriptionById(directionId);
      String sheetName = "Direction " + directionDescription;
      sheetName = truncateSheetName(sheetName, 25); // Truncate the name if necessary

      // Create a new sheet
      Sheet sheet = workbook.createSheet(sheetName);

      // Create title, direction, and header rows
      createTitleRow(sheet, titleCellStyle, dates);
      createDirectionRow(sheet, directionCellStyle, directionDescription);
      createHeaderRow(sheet, headerCellStyle);

      // Add mission and activity data or no activity message
      int rowIdx = 4;
      if (missionsForDirection.isEmpty()) {
        Row noActivityRow = sheet.createRow(rowIdx);
        Cell noActivityCell = noActivityRow.createCell(0);
        noActivityCell.setCellValue("Aucune activité cette semaine");
        noActivityCell.setCellStyle(noActivityCellStyle); // Apply custom style if needed
      } else {
        for (Mission mission : missionsForDirection) {
          rowIdx = addMissionData(sheet, rowIdx, mission, missionCellStyle, activityCellStyle);
        }
      }

      // Auto-size columns for better readability
      autoSizeColumns(sheet);

      // Write the workbook to the output stream
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public byte[] createMissionReportExcelForDirectionWithSubDirections(List<Mission> missions, String dates, String directionId) {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Création des styles pour les cellules
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle titleCellStyle = createTitleCellStyle(workbook);
      CellStyle directionCellStyle = createDirectionCellStyle(workbook);
      CellStyle missionCellStyle = createMissionCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);
      CellStyle noActivityCellStyle = createNoActivityCellStyle(workbook);

      if (missions == null) {
        throw new IllegalArgumentException("La liste des missions ne peut pas être nulle.");
      }

      // Filtrer les missions de la direction principale
      List<Mission> missionsForDirection = missions.stream()
              .filter(mission -> mission.getDirection() != null && mission.getDirection().getId().equals(directionId))
              .collect(Collectors.toList());

      // Création d'une feuille pour la direction principale
      String directionDescription = getDirectionDescriptionById(directionId);
      String sheetName = "Direction " + (directionDescription != null ? directionDescription : "Inconnue");
      sheetName = truncateSheetName(sheetName, 25);
      sheetName = getUniqueSheetName(workbook, sheetName); // Assure un nom unique

      Sheet sheet = workbook.createSheet(sheetName);
      createTitleRow(sheet, titleCellStyle, dates);
      createDirectionRow(sheet, directionCellStyle, directionDescription);
      createHeaderRow(sheet, headerCellStyle);

      int rowIdx = 4;
      if (missionsForDirection.isEmpty()) {
        Row noActivityRow = sheet.createRow(rowIdx);
        Cell noActivityCell = noActivityRow.createCell(0);
        noActivityCell.setCellValue("Aucune activité cette semaine");
        noActivityCell.setCellStyle(noActivityCellStyle);
      } else {
        for (Mission mission : missionsForDirection) {
          rowIdx = addMissionData(sheet, rowIdx, mission, missionCellStyle, activityCellStyle);
        }
      }

      // Traiter les sous-directions
      List<Direction> subDirections = directionService.getSubDirectionByDirectionId(directionId);
      if (subDirections != null) {
        for (Direction subDirection : subDirections) {
          List<Mission> missionsForSubDirection = missions.stream()
                  .filter(mission -> mission.getDirection() != null && mission.getDirection().getId().equals(subDirection.getId()))
                  .collect(Collectors.toList());

          String subDirectionSheetName = "Direction " + (subDirection.getAcronym() != null ? subDirection.getAcronym() : "Inconnue");
          subDirectionSheetName = truncateSheetName(subDirectionSheetName, 25);
          subDirectionSheetName = getUniqueSheetName(workbook, subDirectionSheetName); // Assure un nom unique

          Sheet subDirectionSheet = workbook.createSheet(subDirectionSheetName);
          createTitleRow(subDirectionSheet, titleCellStyle, dates);
          createDirectionRow(subDirectionSheet, directionCellStyle, subDirection.getName());
          createHeaderRow(subDirectionSheet, headerCellStyle);

          rowIdx = 4;
          if (missionsForSubDirection.isEmpty()) {
            Row noActivityRow = subDirectionSheet.createRow(rowIdx);
            Cell noActivityCell = noActivityRow.createCell(0);
            noActivityCell.setCellValue("Aucune activité cette semaine");
            noActivityCell.setCellStyle(noActivityCellStyle);
          } else {
            for (Mission mission : missionsForSubDirection) {
              rowIdx = addMissionData(subDirectionSheet, rowIdx, mission, missionCellStyle, activityCellStyle);
            }
          }
          autoSizeColumns(subDirectionSheet);
        }
      }

      // Auto-size des colonnes pour la direction principale
      autoSizeColumns(sheet);

      // Ecriture du classeur dans le flux de sortie
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Génère un nom de feuille unique en ajoutant un suffixe numérique si nécessaire.
   */
  private String getUniqueSheetName(XSSFWorkbook workbook, String baseName) {
    String sheetName = baseName;
    int index = 1;
    while (workbook.getSheet(sheetName) != null) {
      sheetName = baseName + " (" + index + ")";
      index++;
    }
    return sheetName;
  }

  public byte[] createActivityExcel(List<Activity> activities) throws IOException {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Create cell styles (customize as needed)
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);

      // Enable word wrap and left alignment for activity and task cells
      activityCellStyle.setWrapText(true);
      activityCellStyle.setAlignment(HorizontalAlignment.LEFT);

      // Enable word wrap for header cell style as well
      headerCellStyle.setWrapText(true);
      headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

      // Create a sheet
      Sheet sheet = workbook.createSheet("Activités des directions");

      // Create header row with increased height for better readability
      Row headerRow = sheet.createRow(0);
      headerRow.setHeightInPoints(40); // Increase header row height for wrapping

      String[] headers = {
              "ACTIVITÉS MENSUELLES PRÉVUES",
              "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES",
              "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE",
              "OBSERVATIONS",
              "PRÉVISION"
      };
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerCellStyle);
      }

      // Populate activities and related lists
      int rowIdx = 1;
      for (Activity activity : activities) {
        // Row for the main activity information
        Row activityRow = sheet.createRow(rowIdx++);

        // Activity description
        activityRow.createCell(0).setCellValue(activity.getDescription());

        // Join task descriptions with newline character for realized weekly tasks
        String realizedTasks = activity.getTaskList().stream()
                .map(task -> "- " + task.getDescription())
                .collect(Collectors.joining("\n"));
        Cell taskCell = activityRow.createCell(1);
        taskCell.setCellValue(realizedTasks);
        taskCell.setCellStyle(activityCellStyle);

        // Join next task descriptions with newline character for next week's tasks
        String nextWeekTasks = activity.getNexTaskList().stream()
                .map(nextTask -> "- " + nextTask.getDescription())
                .collect(Collectors.joining("\n"));
        Cell nextTaskCell = activityRow.createCell(2);
        nextTaskCell.setCellValue(nextWeekTasks);
        nextTaskCell.setCellStyle(activityCellStyle);

        // Observations and predictions
        Cell observationCell = activityRow.createCell(3);
        observationCell.setCellValue(activity.getObservation());
        observationCell.setCellStyle(activityCellStyle);

        Cell predictionCell = activityRow.createCell(4);
        predictionCell.setCellValue(activity.getPrediction());
        predictionCell.setCellStyle(activityCellStyle);

        // Set left alignment and wrapping for all cells in the main row
        for (int i = 0; i < headers.length; i++) {
          Cell cell = activityRow.getCell(i);
          if (cell == null) {
            cell = activityRow.createCell(i);
          }
          cell.setCellStyle(activityCellStyle);
        }
      }

      // Auto-size columns for readability
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }

      // Write the workbook to the output stream
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    }
  }

  public byte[] createActivityReportExcel(String directionName, LocalDate startDate, List<Activity> activities) throws IOException {
    LocalDate endDate = startDate.plusDays(6);
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Create cell styles
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);
      activityCellStyle.setWrapText(true);
      activityCellStyle.setAlignment(HorizontalAlignment.LEFT);

      // Enable word wrap for header cell style as well
      headerCellStyle.setWrapText(true);
      headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

      // Styles for direction and title rows
      CellStyle titleStyle = workbook.createCellStyle();
      Font titleFont = workbook.createFont();
      titleFont.setBold(true);
      titleFont.setFontHeightInPoints((short) 16);  // Title font size
      titleStyle.setFont(titleFont);

      CellStyle subtitleStyle = workbook.createCellStyle();
      Font subtitleFont = workbook.createFont();
      subtitleFont.setBold(true);
      subtitleFont.setFontHeightInPoints((short) 14);  // Subtitle font size
      subtitleStyle.setFont(subtitleFont);
      subtitleStyle.setAlignment(HorizontalAlignment.CENTER);

      // Create a sheet
      Sheet sheet = workbook.createSheet("Activities");

      // Add direction name at the top
      Row directionRow = sheet.createRow(0);
      Cell directionCell = directionRow.createCell(0);
      directionCell.setCellValue("Direction: " + directionName);
      directionCell.setCellStyle(titleStyle);

      // Merge cells for direction title for a more centered display
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

      // Add report title with date range on the second row
      Row titleRow = sheet.createRow(1);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("COMPTE RENDU DES ACTIVITES HEBDOMADAIRES DE LA " + directionName + " DU " + dateUtils.formatDate(startDate)  + " AU " + dateUtils.formatDate(endDate));
      titleCell.setCellStyle(subtitleStyle);

      // Merge cells for the title row
      sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

      // Create header row for the table
      Row headerRow = sheet.createRow(3);
      headerRow.setHeightInPoints(40); // Increase header row height for readability

      String[] headers = {
              "ACTIVITÉS MENSUELLES PRÉVUES",
              "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES",
              "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE",
              "OBSERVATIONS",
              "PRÉVISION"
      };

      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerCellStyle);
      }

      // Populate activities and related lists
      int rowIdx = 4;
      for (Activity activity : activities) {
        Row activityRow = sheet.createRow(rowIdx++);

        // Activity description
        activityRow.createCell(0).setCellValue(activity.getDescription());

        // Realized weekly tasks
        String realizedTasks = activity.getTaskList().stream()
                .map(task -> "- " + task.getDescription())
                .collect(Collectors.joining("\n"));
        Cell taskCell = activityRow.createCell(1);
        taskCell.setCellValue(realizedTasks);
        taskCell.setCellStyle(activityCellStyle);

        // Next week's tasks
        String nextWeekTasks = activity.getNexTaskList().stream()
                .map(nextTask -> "- " + nextTask.getDescription())
                .collect(Collectors.joining("\n"));
        Cell nextTaskCell = activityRow.createCell(2);
        nextTaskCell.setCellValue(nextWeekTasks);
        nextTaskCell.setCellStyle(activityCellStyle);

        // Observations and predictions
        Cell observationCell = activityRow.createCell(3);
        observationCell.setCellValue(activity.getObservation());
        observationCell.setCellStyle(activityCellStyle);

        Cell predictionCell = activityRow.createCell(4);
        predictionCell.setCellValue(activity.getPrediction());
        predictionCell.setCellStyle(activityCellStyle);
      }

      // Auto-size columns for readability
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }

      // Write the workbook to the output stream
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    }
  }


  public byte[] createActivityReportExcelWithSubDirections(String mainDirectionId, LocalDate startDate, List<Activity> allActivities) throws IOException {
    LocalDate endDate = startDate.plusDays(6);

    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      // Define cell styles
      CellStyle headerCellStyle = createHeaderCellStyle(workbook);
      CellStyle titleCellStyle = createTitleCellStyle(workbook);
      CellStyle activityCellStyle = createActivityCellStyle(workbook);
      CellStyle noActivityCellStyle = createNoActivityCellStyle(workbook);



      // Filter activities for the main direction
      List<Activity> mainDirectionActivities = allActivities.stream()
              .filter(activity -> activity.getMission().getDirection() != null && activity.getMission().getDirection().getId().equals(mainDirectionId))
              .collect(Collectors.toList());

      // Create a sheet for the main direction
      String mainDirectionName = getDirectionDescriptionById(mainDirectionId);
      String sheetName = "Direction " + (mainDirectionName != null ? mainDirectionName : "Inconnue");
      sheetName = truncateSheetName(sheetName, 25);
      sheetName = getUniqueSheetName(workbook, sheetName); // Ensure unique name


      activityCellStyle.setWrapText(true);
      activityCellStyle.setAlignment(HorizontalAlignment.LEFT);

      // Enable word wrap for header cell style as well
      headerCellStyle.setWrapText(true);
      headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

      // Styles for direction and title rows
      CellStyle titleStyle = workbook.createCellStyle();
      Font titleFont = workbook.createFont();
      titleFont.setBold(true);
      titleFont.setFontHeightInPoints((short) 16);  // Title font size
      titleStyle.setFont(titleFont);

      CellStyle subtitleStyle = workbook.createCellStyle();
      Font subtitleFont = workbook.createFont();
      subtitleFont.setBold(true);
      subtitleFont.setFontHeightInPoints((short) 14);  // Subtitle font size
      subtitleStyle.setFont(subtitleFont);
      subtitleStyle.setAlignment(HorizontalAlignment.CENTER);

      // Create a sheet
      Sheet sheet = workbook.createSheet("Activities");

      // Add direction name at the top
      Row directionRow = sheet.createRow(0);
      Cell directionCell = directionRow.createCell(0);
      directionCell.setCellValue("Direction: " + mainDirectionName);
      directionCell.setCellStyle(titleStyle);

      // Merge cells for direction title for a more centered display
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

      // Add report title with date range on the second row
      Row titleRow = sheet.createRow(1);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("COMPTE RENDU DES ACTIVITES HEBDOMADAIRES DE LA " + mainDirectionName + " DU " + dateUtils.formatDate(startDate)  + " AU " + dateUtils.formatDate(endDate));
      titleCell.setCellStyle(subtitleStyle);

      // Merge cells for the title row
      sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

      // Create header row for the table
      Row headerRow = sheet.createRow(3);
      headerRow.setHeightInPoints(40); // Increase header row height for readability

      String[] headers = {
              "ACTIVITÉS MENSUELLES PRÉVUES",
              "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES",
              "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE",
              "OBSERVATIONS",
              "PRÉVISION"
      };

      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerCellStyle);
      }

      // Populate activities and related lists
      int rowIdx = 4;
      for (Activity activity : mainDirectionActivities) {
        Row activityRow = sheet.createRow(rowIdx++);

        // Activity description
        activityRow.createCell(0).setCellValue(activity.getDescription());

        // Realized weekly tasks
        String realizedTasks = activity.getTaskList().stream()
                .map(task -> "- " + task.getDescription())
                .collect(Collectors.joining("\n"));
        Cell taskCell = activityRow.createCell(1);
        taskCell.setCellValue(realizedTasks);
        taskCell.setCellStyle(activityCellStyle);

        // Next week's tasks
        String nextWeekTasks = activity.getNexTaskList().stream()
                .map(nextTask -> "- " + nextTask.getDescription())
                .collect(Collectors.joining("\n"));
        Cell nextTaskCell = activityRow.createCell(2);
        nextTaskCell.setCellValue(nextWeekTasks);
        nextTaskCell.setCellStyle(activityCellStyle);

        // Observations and predictions
        Cell observationCell = activityRow.createCell(3);
        observationCell.setCellValue(activity.getObservation());
        observationCell.setCellStyle(activityCellStyle);

        Cell predictionCell = activityRow.createCell(4);
        predictionCell.setCellValue(activity.getPrediction());
        predictionCell.setCellStyle(activityCellStyle);
      }

      // Auto-size columns for readability
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }



      // Handle sub-directions
      List<Direction> subDirections = directionService.getSubDirectionByDirectionId(mainDirectionId);
      if (subDirections != null) {
        for (Direction subDirection : subDirections) {
          List<Activity> subDirectionActivities = activityService.getActivitiesForWeek(startDate,subDirection.getId(),1,100);

          String directionName = getDirectionDescriptionById(subDirection.getId());
          String sheetSubDirection = "Direction " + (mainDirectionName != null ? mainDirectionName : "Inconnue");
          sheetName = truncateSheetName(sheetName, 25);
          sheetName = getUniqueSheetName(workbook, sheetName); // Ensure unique name


          activityCellStyle.setWrapText(true);
          activityCellStyle.setAlignment(HorizontalAlignment.LEFT);

          // Enable word wrap for header cell style as well
          headerCellStyle.setWrapText(true);
          headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

          // Styles for direction and title rows
          CellStyle titleStyleSub = workbook.createCellStyle();
          Font titleFontSub = workbook.createFont();
          titleFont.setBold(true);
          titleFont.setFontHeightInPoints((short) 16);  // Title font size
          titleStyle.setFont(titleFont);

          CellStyle subtitleStyleSub = workbook.createCellStyle();
          Font subtitleSub = workbook.createFont();
          subtitleSub.setBold(true);
          subtitleFont.setFontHeightInPoints((short) 14);  // Subtitle font size
          subtitleStyle.setFont(subtitleFont);
          subtitleStyle.setAlignment(HorizontalAlignment.CENTER);

          // Create a sheet
          Sheet subsheet = workbook.createSheet("Activities");

          // Add direction name at the top
          Row subDirectionRow = sheet.createRow(0);
          Cell subDirectionCell = directionRow.createCell(0);
          directionCell.setCellValue("Direction: " + directionName);
          directionCell.setCellStyle(titleStyle);

          // Merge cells for direction title for a more centered display
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

          // Add report title with date range on the second row
          Row titleRowSub = sheet.createRow(1);
          Cell titleCellSub = titleRow.createCell(0);
          titleCellSub.setCellValue("COMPTE RENDU DES ACTIVITES HEBDOMADAIRES DE LA " + directionName + " DU " + dateUtils.formatDate(startDate)  + " AU " + dateUtils.formatDate(endDate));
          titleCellSub.setCellStyle(subtitleStyle);

          // Merge cells for the title row
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

          // Create header row for the table
          Row headerRowSub = sheet.createRow(3);
          headerRowSub.setHeightInPoints(40); // Increase header row height for readability

          String[] subheaders = {
                  "ACTIVITÉS MENSUELLES PRÉVUES",
                  "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES",
                  "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE",
                  "OBSERVATIONS",
                  "PRÉVISION"
          };

          for (int i = 0; i < subheaders.length; i++) {
            Cell cell = subDirectionRow.createCell(i);
            cell.setCellValue(subheaders[i]);
            cell.setCellStyle(headerCellStyle);
          }

          // Populate activities and related lists
          for (Activity activity : mainDirectionActivities) {
            Row activityRow = sheet.createRow(rowIdx++);

            // Activity description
            activityRow.createCell(0).setCellValue(activity.getDescription());

            // Realized weekly tasks
            String realizedTasks = activity.getTaskList().stream()
                    .map(task -> "- " + task.getDescription())
                    .collect(Collectors.joining("\n"));
            Cell taskCell = activityRow.createCell(1);
            taskCell.setCellValue(realizedTasks);
            taskCell.setCellStyle(activityCellStyle);

            // Next week's tasks
            String nextWeekTasks = activity.getNexTaskList().stream()
                    .map(nextTask -> "- " + nextTask.getDescription())
                    .collect(Collectors.joining("\n"));
            Cell nextTaskCell = activityRow.createCell(2);
            nextTaskCell.setCellValue(nextWeekTasks);
            nextTaskCell.setCellStyle(activityCellStyle);

            // Observations and predictions
            Cell observationCell = activityRow.createCell(3);
            observationCell.setCellValue(activity.getObservation());
            observationCell.setCellStyle(activityCellStyle);

            Cell predictionCell = activityRow.createCell(4);
            predictionCell.setCellValue(activity.getPrediction());
            predictionCell.setCellStyle(activityCellStyle);
          }

          // Auto-size columns for readability
          for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
          }
        }
      }

      // Auto-size columns for the main direction sheet
      autoSizeColumns(sheet);

      // Write workbook to output stream
      workbook.write(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private int generateActivityTable(Sheet sheet, String directionName, LocalDate startDate, LocalDate endDate, List<Activity> activities, int startRow, CellStyle titleStyle, CellStyle headerCellStyle, CellStyle activityCellStyle) {
    // Title for direction and date range
    Row titleRow = sheet.createRow(startRow++);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("COMPTE RENDU DES ACTIVITES HEBDOMADAIRES DE LA " + directionName + " DU " + startDate + " AU " + endDate);
    titleCell.setCellStyle(titleStyle);
    sheet.addMergedRegion(new CellRangeAddress(startRow - 1, startRow - 1, 0, 4));

    // Header row
    Row headerRow = sheet.createRow(startRow++);
    String[] headers = {"ACTIVITÉS MENSUELLES PRÉVUES", "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES", "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE", "OBSERVATIONS", "PRÉVISION"};
    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerCellStyle);
    }

    // Populate activities for the direction
    for (Activity activity : activities) {
      Row activityRow = sheet.createRow(startRow++);
      activityRow.createCell(0).setCellValue(activity.getDescription());

      Cell taskCell = activityRow.createCell(1);
      taskCell.setCellValue(formatTasks(activity.getTaskList()));
      taskCell.setCellStyle(activityCellStyle);

      Cell nextTaskCell = activityRow.createCell(2);
      nextTaskCell.setCellValue(formatNextTasks(activity.getNexTaskList()));
      nextTaskCell.setCellStyle(activityCellStyle);

      activityRow.createCell(3).setCellValue(activity.getObservation());
      activityRow.createCell(4).setCellValue(activity.getPrediction());
    }

    // Set column widths
    sheet.setColumnWidth(0, 8000);  // Adjust width as needed
    for (int i = 1; i < headers.length; i++) {
      sheet.autoSizeColumn(i);
    }

    return startRow;
  }

  private int addActivityData(Sheet sheet, int rowIdx, Activity activity, CellStyle activityCellStyle) {
    // Create a new row for the activity
    Row activityRow = sheet.createRow(rowIdx);

    // Fill in the cells for each piece of data related to the activity
    int colIdx = 0;

    // Assuming Activity has fields: designation, task, nextTask, date, observation, prediction, etc.
    Cell designationCell = activityRow.createCell(colIdx++);
    designationCell.setCellValue(activity.getMission().getDescription());
    designationCell.setCellStyle(activityCellStyle);

    Cell activityRowCell = activityRow.createCell(colIdx++);
    activityRowCell.setCellValue(activity.getDescription());
    activityRowCell.setCellStyle(activityCellStyle);








    String realizedTasks = activity.getTaskList().stream()
            .map(task -> "- " + task.getDescription())
            .collect(Collectors.joining("\n"));
    Cell taskCell = activityRow.createCell(1);
    taskCell.setCellValue(realizedTasks);
    taskCell.setCellStyle(activityCellStyle);

    // Next week's tasks
    String nextWeekTasks = activity.getNexTaskList().stream()
            .map(nextTask -> "- " + nextTask.getDescription())
            .collect(Collectors.joining("\n"));
    Cell nextTaskCell = activityRow.createCell(2);
    nextTaskCell.setCellValue(nextWeekTasks);
    nextTaskCell.setCellStyle(activityCellStyle);

    List<PerformanceRealization> performanceRealization = activity.getPerformanceRealization();

    if (performanceRealization != null && !performanceRealization.isEmpty()) {
      for (PerformanceRealization performanceRealization1 : performanceRealization) {
        Row recommendationRow = sheet.createRow(++rowIdx); // Move to next row for each recommendation

        int recColIdx = colIdx; // Start after activity fields
        Cell performanceIndicatorCell = recommendationRow.createCell(recColIdx++);
        performanceIndicatorCell.setCellValue(performanceRealization1.getKPI());
        performanceIndicatorCell.setCellStyle(activityCellStyle);

        Cell realizationCell = recommendationRow.createCell(recColIdx++);
        realizationCell.setCellValue(performanceRealization1.getRealization());
        realizationCell.setCellStyle(activityCellStyle);

        // Add any other Recommendation details as needed
      }
    } else {
      // If no recommendations, leave row with activity details only
      rowIdx++;
    }

    Cell observationCell = activityRow.createCell(colIdx++);
    observationCell.setCellValue(activity.getObservation());
    observationCell.setCellStyle(activityCellStyle);

    Cell predictionCell = activityRow.createCell(colIdx++);
    predictionCell.setCellValue(activity.getPrediction());
    predictionCell.setCellStyle(activityCellStyle);

    // Continue for other fields in Activity if needed...

    // Return the next row index to allow the caller to continue adding rows sequentially
    return rowIdx + 1;
  }


  private String formatTasks(List<Task> tasks) {
    return tasks.stream()
            .map(task -> "- " + task.getDescription())  // Ajoute la description de chaque tâche
            .collect(Collectors.joining("\n"));
  }
  private String formatNextTasks(List<NextTask> tasks) {
    return tasks.stream()
            .map(task -> "- " + task.getDescription())
            .collect(Collectors.joining("\n"));
  }


  private int generateDirectionReportSection(Sheet sheet, int startRowIdx, String directionName, LocalDate startDate, LocalDate endDate, List<Activity> activities, CellStyle titleStyle, CellStyle subtitleStyle, CellStyle headerCellStyle, CellStyle activityCellStyle) {
    // Add direction name at the top
    Row directionRow = sheet.createRow(startRowIdx++);
    Cell directionCell = directionRow.createCell(0);
    directionCell.setCellValue("Direction: " + directionName);
    directionCell.setCellStyle(titleStyle);
    sheet.addMergedRegion(new CellRangeAddress(startRowIdx - 1, startRowIdx - 1, 0, 4));

    // Add report title with date range
    Row titleRow = sheet.createRow(startRowIdx++);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("COMPTE RENDU DES ACTIVITES HEBDOMADAIRES DE LA " + directionName + " DU " + dateUtils.formatDate(startDate) + " AU " + dateUtils.formatDate(endDate));
    titleCell.setCellStyle(subtitleStyle);
    sheet.addMergedRegion(new CellRangeAddress(startRowIdx - 1, startRowIdx - 1, 0, 4));

    // Create header row
    Row headerRow = sheet.createRow(startRowIdx++);
    String[] headers = {"ACTIVITÉS MENSUELLES PRÉVUES", "SOUS-ACTIVITÉS RÉALISÉES (OU TÂCHES) HEBDOMADAIRES", "SOUS-ACTIVITÉS À RÉALISER POUR LA SEMAINE PROCHAINE", "OBSERVATIONS", "PRÉVISION"};
    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerCellStyle);
    }

    // Populate rows with activity data
    for (Activity activity : activities) {
      Row activityRow = sheet.createRow(startRowIdx++);
      activityRow.createCell(0).setCellValue(activity.getDescription());

      Cell taskCell = activityRow.createCell(1);
      taskCell.setCellValue(formatTasks(activity.getTaskList()));
      taskCell.setCellStyle(activityCellStyle);

      Cell nextTaskCell = activityRow.createCell(2);
      nextTaskCell.setCellValue(formatNextTasks(activity.getNexTaskList()));
      nextTaskCell.setCellStyle(activityCellStyle);

      activityRow.createCell(3).setCellValue(activity.getObservation());
      activityRow.createCell(4).setCellValue(activity.getPrediction());
    }
    return startRowIdx + 1; // Return the next row index after section
  }


  // Assume this method is defined to create a style for the no activity message
  private CellStyle createNoActivityCellStyle(XSSFWorkbook workbook) {
    CellStyle style = workbook.createCellStyle();
    // Add any specific styling, e.g., font size, color, etc.
    return style;
  }



  private CellStyle createTaskCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    // Customize style for task rows
    return style;
  }

  public byte[] createMissionReportExcel(List<Mission> missions) {
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
        createTitleRow(sheet, titleCellStyle, "inter-direction");
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
    headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
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
    titleCell.setCellValue("Rapport des activités  " + dates);
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
