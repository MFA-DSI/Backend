package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.controller.utils.LocalDateUtils;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.FileService;
import com.mfa.report.service.MissionService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class FileController {
    private final FileService fileService;
    private final MissionService missionService;
    private final MissionMapper missionMapper;
    private final DirectionService directionService;
    private  final ActivityService activityService;
    private final LocalDateUtils localDateUtils;
    private final DirectionValidator directionValidator;


    @PostMapping("/activity/export/excel")
    public void exportExcel(@RequestBody List<String> ids, HttpServletResponse response) throws IOException {
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
    public ResponseEntity<byte[]> getActivityReportWeekly(@RequestParam String directionId, @RequestParam LocalDate date, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) throws IOException {
        List<Activity> activities = activityService.getActivitiesForWeek(date,directionId,page,pageSize);
        byte[] excelBytes = fileService.createActivityReportExcel("DSI",date,activities);
            String filename = "file-activities";
        log.info(String.valueOf(activities.size()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=activities.xlsx")
                .contentLength(excelBytes.length)
                .body(excelBytes);
    }


    @PostMapping("/mission/export/pdf")
    public ResponseEntity<byte[]> generateMissionPdf( @RequestBody List<String> missionIds) {
        List<Mission> missions = missionService.findMissionsByIds(missionIds);

        if (missions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Step 2: Generate the PDF
        byte[] pdfBytes = fileService.createMissionReport(missions,"Juillet");

        // Step 3: Set HTTP Headers and return PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "mission_report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/mission/export/excel")
    public ResponseEntity<byte[]> generateExcel(@RequestBody List<String> missionIds) throws IOException {
        List<Mission> missions = missionService.findMissionsByIds(missionIds);
        log.info(String.valueOf(missions.size()));
        byte[] resource = fileService.createMissionReportExcel(missions);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=missions.xlsx")
                .contentLength(resource.length)
                .body(resource);
    }


    // TODO: change this to activity display
    @PostMapping("/mydirection/mission/export/week/excel")
    public ResponseEntity<byte[]> generateWeekReport(@RequestParam String directionId, @RequestParam LocalDate date, @RequestParam int pageSize) throws IOException {
        String reportTitle = localDateUtils.generateReportTitleForWeek(date);
        LocalDate endDate = date.plusDays(5);
        log.info(reportTitle);
       Direction direction = directionService.getDirectionById(directionId);
        List<Activity> activities = activityService.getActivitiesForWeek(date, directionId, 1, pageSize);
        byte[] resource = fileService.createActivityReportExcel(direction.getName(), date,activities);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String fileName = localDateUtils.formatDateRange(date,endDate)+" -CR ACTIVITES HEBDOMADAIRES"+".xlsx";

        log.info(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }

    @PostMapping("/mydirection/mission/export/monthly/excel")
    public ResponseEntity<byte[]> generateMonthReport(@RequestParam String directionId, @RequestParam int year,@RequestParam int month, @RequestParam int pageSize) throws IOException {
        // TODO: verify user and make a validation if not responsible
        String monthName = "mois "+ localDateUtils.getMonthName(month);

        Direction direction = directionService.getDirectionById(directionId);
        List<Mission> missions = missionService.getMissionActivitiesForMonth(year,month, directionId, 1, pageSize);
        byte[] resource = fileService.createMissionReportExcelForDirection(missions, monthName,directionId);
        String fileName = direction.getName() + "+mission+ - mois du+" + monthName+ " " + year+ ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }

    @PostMapping("/mydirection/mission/export/quarter/excel")
    public ResponseEntity<byte[]> generateQuarterReport(@RequestParam String directionId, @RequestParam int year,@RequestParam int quarter, @RequestParam int pageSize) throws IOException {
        String quarterNumber = localDateUtils.getQuarterName(quarter)+" "+year;
        Direction direction = directionService.getDirectionById(directionId);
        List<Mission> missions = missionService.getMissionActivitiesForQuarter(year,quarter, directionId, 1, pageSize);
        byte[] resource = fileService.createMissionReportExcelForDirection(missions, quarterNumber,directionId);
        String fileName = direction.getName()  + "+mission du+" + quarterNumber+ "du " + year+ ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }

    
}
