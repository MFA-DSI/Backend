package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.controller.utils.LocalDateUtils;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.FileService;
import com.mfa.report.service.MissionService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(
    value = {"*"},
    exposedHeaders = {"Content-Disposition"})
public class FileController {
  private final FileService fileService;
  private final MissionService missionService;
  private final MissionMapper missionMapper;
  private final DirectionService directionService;
  private final ActivityService activityService;
  private final LocalDateUtils localDateUtils;
  private final DirectionValidator directionValidator;

  @PostMapping("/activity/export/excel")
  public void exportExcel(@RequestBody List<String> ids, HttpServletResponse response)
      throws IOException {
    List<Activity> activities = activityService.getActivitiesByIds(ids);
    byte[] excelBytes = fileService.createActivityExcel(activities);

    log.info(String.valueOf(activities.size()));
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=activities.xlsx");
    response.setContentLength(excelBytes.length);
    response.getOutputStream().write(excelBytes);
    response.getOutputStream().flush();
  }

  @PostMapping("/activity/report/excel")
  public ResponseEntity<byte[]> getActivityReportWeekly(
      @RequestParam String directionId,
      @RequestParam LocalDate date,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize)
      throws IOException {
    List<Activity> activities =
        activityService.getActivitiesForWeek(date, directionId, page, pageSize);
    byte[] excelBytes = fileService.createActivityReportExcel("DSI", date, activities);
    String filename = "file-activities";
    log.info(String.valueOf(activities.size()));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=activities.xlsx")
        .contentLength(excelBytes.length)
        .body(excelBytes);
  }

  @PostMapping("/mission/export/pdf")
  public ResponseEntity<byte[]> generateMissionPdf(@RequestBody List<String> missionIds) {
    List<Mission> missions = missionService.findMissionsByIds(missionIds);

    if (missions.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Step 2: Generate the PDF
    byte[] pdfBytes = fileService.createMissionReport(missions, "Juillet");

    // Step 3: Set HTTP Headers and return PDF
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("filename", "mission_report.pdf");

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
  }

  @PostMapping("/mission/export/excel")
  public ResponseEntity<byte[]> generateExcel(@RequestBody List<String> missionIds)
      throws IOException {
    List<Mission> missions = missionService.findMissionsByIds(missionIds);
    Set<String> directions = missions.stream()
            .map(mission -> mission.getDirection().getName()) // Récupère le nom de chaque direction
            .collect(Collectors.toSet()); // Utilise un Set pour éviter les doublons

    // Construire le nom du fichier en fonction des directions
    String filename = "missions_" + String.join(" - ", directions) + ".xlsx";
    byte[] resource = fileService.createMissionReportExcel(missions);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename)
        .contentLength(resource.length)
        .body(resource);
  }

  // TODO: change this to activity display
  @PostMapping("/mydirection/mission/export/week/excel")
  public ResponseEntity<byte[]> generateWeekReport(
          @RequestParam String directionId, @RequestParam LocalDate date, @RequestParam int pageSize)
          throws IOException {

    String reportTitle = localDateUtils.generateReportTitleForWeek(date);
    LocalDate endDate = date.plusDays(6);
    log.info(reportTitle);

    Direction direction = directionService.getDirectionById(directionId);
    List<Direction> subDirection = directionService.getSubDirectionByDirectionId(directionId);

    byte[] resource;
    if (subDirection.isEmpty()) {
      // Pas de sous-directions, génère un rapport pour la direction seule
      List<Activity> activities = activityService.getActivitiesForWeek(date, directionId, 1, pageSize);
      resource = fileService.createActivityReportExcel(direction.getName(), date, activities);
    } else {
      // Génère un rapport pour la direction principale et ses sous-directions
      List<Activity> mainActivities = activityService.getActivitiesForWeek(date, directionId, 1, pageSize);
      List<Direction> subDirections = directionService.getSubDirectionByDirectionId(directionId);
      resource =  fileService.createActivityReportExcelWithSubDirections(direction.getId(), date, mainActivities);
    }

    String fileName = localDateUtils.formatDateRange(date, endDate) + " - CR ACTIVITES HEBDOMADAIRES - "+direction.getAcronym() + ".xlsx";
    log.info(fileName);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .contentLength(resource.length)
            .body(resource);
  }

  @PostMapping("/mydirection/mission/export/monthly/excel")
  public ResponseEntity<byte[]> generateMonthReport(
          @RequestParam String directionId,
          @RequestParam int year,
          @RequestParam int month,
          @RequestParam int pageSize)
          throws IOException {
    // TODO: verify user and make a validation if not responsible
    String monthName = localDateUtils.getMonthName(month);

    Direction direction = directionService.getDirectionById(directionId);
    List<Mission> missions =
            missionService.getMissionActivitiesForMonth(year, month, directionId, 1, pageSize);
    byte[] resource =
            fileService.createMissionReportExcelForDirection(missions, monthName, directionId);
    String fileName =
            "BILAN MENSUEL des activités de la "
                    + direction.getName()
                    + " - mois du "
                    + monthName
                    + " "
                    + year
                    + ".xlsx";
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .contentLength(resource.length)
            .body(resource);
  }


  @PostMapping("/mydirection/mission/export/quarter/excel")
  public ResponseEntity<byte[]> generateQuarterReport(
      @RequestParam String directionId,
      @RequestParam int year,
      @RequestParam int quarter,
      @RequestParam int pageSize)
      throws IOException {
    String quarterNumber = localDateUtils.getQuarterName(quarter);
    Direction direction = directionService.getDirectionById(directionId);
    List<Mission> missions =
        missionService.getMissionActivitiesForQuarter(year, quarter, directionId, 1, pageSize);
    byte[] resource =
        fileService.createMissionReportExcelForDirection(missions, quarterNumber, directionId);
    String fileName =
        "BILAN des activités de la "
            + direction.getName()
            + " - "
            + quarterNumber
            + " de l'année "
            + year
            + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .contentLength(resource.length)
        .body(resource);
  }
}
