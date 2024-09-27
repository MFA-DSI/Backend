package com.mfa.report.endpoint.rest.controller;

import com.itextpdf.text.DocumentException;
import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mfa.report.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class ActivityController {
  private final ActivityService activityService;
  private final ActivityMapper mapper;

  private final MissionService missionService;
  private final DirectionService directionService;
  private final PDFService pdfService;
  private final DOCService docService;
  private final ExcelService excelService;
  private  final AssociatedEntitiesService associatedEntitiesService;

  private final DirectionValidator directionValidator;



  @GetMapping("/activities/week")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #weekStartDate")
  public List<ActivityDTO> getActivitiesForWeek(
      @RequestParam LocalDate weekStartDate, @RequestParam(required = false) String directionId,@RequestParam(defaultValue ="1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return activityService.getActivitiesForWeek(weekStartDate, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/month")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #month + ':' + #year")
  public List<ActivityDTO> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam(required = false) String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return activityService.getActivitiesForMonth(year, month, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/quarter")
  @Cacheable(value = "activity", key = "#directionId + ':' + #quarter + ':' + #year")
  public List<ActivityDTO> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam(required = false) String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return activityService.getActivitiesForQuarter(year, quarter, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/all")
  public List<ActivityDTO> getAllActivities(@RequestParam(defaultValue = "1")  int page, @RequestParam(defaultValue = "15",name = "page_size") int pageSize) {
    return activityService.getActivities(page,pageSize).getContent().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }


  @GetMapping("/activity/direction")
  public List<ActivityDTO> getAllActivitiesByDirectionId(@RequestParam String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15",name = "page_size") int pageSize){
    return activityService.getActivitiesByDirectionId(directionId,page,pageSize).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activity")
  public ActivityDTO getActivityById(@RequestParam String id){
    return mapper.toDomain(activityService.getActivityById(id));
  }
  @DeleteMapping("/activity/delete")
  public ResponseEntity<String> deleteActivity(
          @RequestParam(name = "activityId") String activityId,
          @RequestParam(name = "userId") String userId) {


    Activity existingActivity = activityService.getActivityById(activityId);
    if (existingActivity == null) {
      return ResponseEntity.notFound().build();
    }


    Mission associatedMission = existingActivity.getMission();
    if (associatedMission == null) {
      return ResponseEntity.badRequest().body("no mission is attached to this activity");
    }

    Direction associatedDirection = associatedMission.getDirection();
    if (associatedDirection == null) {
      return ResponseEntity.badRequest().body(null);
    }
    directionValidator.acceptUser(associatedDirection, userId);


    activityService.deleteActivity(existingActivity);
    return ResponseEntity.ok("Activity deleted successfully"); // Return 204 No Content
  }


  @PutMapping("/activity/update")
  @Transactional
  public ActivityDTO saveNewActivity(@RequestBody ActivityDTO activityDTO) {

    Activity activity = activityService.getActivityById(activityDTO.getId());
    activity.setDueDatetime(activityDTO.getDueDatetime());
    activity.setPrediction(activityDTO.getPrediction());
    activity.setObservation(activityDTO.getObservation());
    activity.setDescription(activityDTO.getDescription());
    Activity savedActivity = activityService.crUpdateActivity(activity);

    return mapper.toDomain(savedActivity);
  }

  @GetMapping("/activity/export/pdf")
  public void exportPdf(@RequestParam List<String> ids, HttpServletResponse response) throws IOException, DocumentException {
    List<Activity> activities = activityService.getActivitiesByIds(ids);
    byte[] pdfBytes = pdfService.createActivityPdf(activities);

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=activities.pdf");
    response.getOutputStream().write(pdfBytes);
    response.getOutputStream().flush();
  }

  @GetMapping("/activity/export/doc")
  public ResponseEntity<byte[]> exportDoc(@RequestParam List<String> ids, HttpServletResponse response) throws IOException {
    List<Activity> activities = activityService.getActivitiesByIds(ids);
    byte[] docBytes = docService.createActivityDoc(activities);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "document.docx");
    headers.setContentLength(docBytes.length);

    return ResponseEntity.ok()
            .headers(headers)
            .body(docBytes);
}
  @GetMapping("/activity/export/excel")
  public void exportExcel(@RequestParam List<String> ids, HttpServletResponse response) throws IOException {
    List<Activity> activities = activityService.getActivitiesByIds(ids);
    byte[] excelBytes = excelService.createActivityExcel(activities);

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=activities.xlsx");
    response.setContentLength(excelBytes.length);
    response.getOutputStream().write(excelBytes);
    response.getOutputStream().flush();
  }




}
