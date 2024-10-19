package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.controller.utils.LocalDateUtils;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.FileService;
import com.mfa.report.service.MissionService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;
    private final MissionService missionService;
    private final MissionMapper missionMapper;
    private final DirectionService directionService;
    private final LocalDateUtils localDateUtils;
    private final DirectionValidator directionValidator;

    @PostMapping("/mydirection/mission/export/week/excel")
    public ResponseEntity<byte[]> generateWeekReport(@RequestParam String directionId, @RequestParam LocalDate date, @RequestParam int pageSize) throws IOException {
        String reportTitle = localDateUtils.generateReportTitleForWeek(date);
       Direction direction = directionService.getDirectionById(directionId);
        List<Mission> missions = missionService.getMissionActivitiesForWeek(date, directionId, 1, pageSize);
        byte[] resource = fileService.createMissionReportExcel(missions, "Juillet");
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String fileName = direction.getName() + " Activités + - semaine du+" + formattedDate + ".xlsx";

        log.info(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }



    @PostMapping("/mydirection/mission/export/monthly/excel")
    public ResponseEntity<byte[]> generateMonthReport(@RequestParam String directionId, @RequestParam int year,@RequestParam int month, @RequestParam int pageSize,@RequestParam String userId) throws IOException {
        // TODO: verify user and make a validation if not responsible
        String monthName = localDateUtils.getMonthName(month);
        List<Mission> missions = missionService.getMissionActivitiesForMonth(year,month, directionId, 1, pageSize);
        byte[] resource = fileService.createMissionReportExcel(missions, monthName);
        String fileName = missions.get(0).getDirection().getName() + "+mission+ - mois du+" + monthName+ " " + year+ ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }

    @PostMapping("/mydirection/mission/export/quarter/excel")
    public ResponseEntity<byte[]> generateQuarterReport(@RequestParam String directionId, @RequestParam int year,@RequestParam int quarter, @RequestParam int pageSize,@RequestParam String userId) throws IOException {
        String quarterNumber = localDateUtils.getQuarterName(quarter);
        List<Mission> missions = missionService.getMissionActivitiesForQuarter(year,quarter, directionId, 1, pageSize);
        byte[] resource = fileService.createMissionReportExcel(missions, quarterNumber);
        String fileName = missions.get(0).getDirection().getName() + "+mission du+" + quarterNumber+ "du " + year+ ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(resource.length)
                .body(resource);
    }

    
}
