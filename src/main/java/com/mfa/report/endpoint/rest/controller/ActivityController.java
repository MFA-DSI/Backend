package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.ActivityService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.mfa.report.service.DirectionService;
import com.mfa.report.service.MissionService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class ActivityController {
  private final ActivityService activityService;
  private final ActivityMapper mapper;

  private final MissionService missionService;
  private final DirectionService directionService;

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
  public List<ActivityDTO> getAllActivities(@RequestParam(defaultValue = "1")  int page, @RequestParam(defaultValue = "15") int pageSize) {
    return activityService.getActivities(page,pageSize).getContent().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }


  @GetMapping("/activity/all")
  public List<ActivityDTO> getAllActivitiesByDirectionId(@RequestParam String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize){
    return activityService.getActivitiesByDirectionId(directionId,page,pageSize).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activity")
  public ActivityDTO getActivityById(@RequestParam String activityId){
    return mapper.toDomain(activityService.getActivityById(activityId));
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
}
