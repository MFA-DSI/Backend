package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.service.ActivityService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class ActivityController {
  private final ActivityService activityService;
  private final ActivityMapper mapper;

  @GetMapping("/activities/week")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #weekStartDate")
  public List<ActivityDTO> getActivitiesForWeek(
      @RequestParam LocalDate weekStartDate, @RequestParam String directionId) {
    return activityService.getActivitiesForWeek(weekStartDate, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/month")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #month + ':' + #year")
  public List<ActivityDTO> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam String directionId) {
    return activityService.getActivitiesForMonth(year, month, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/quarter")
  @Cacheable(value = "activity", key = "#directionId + ':' + #quarter + ':' + #year")
  public List<ActivityDTO> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam String directionId) {
    return activityService.getActivitiesForQuarter(year, quarter, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/all")
  public List<ActivityDTO> getAllActivities(@RequestParam  int page, @RequestParam int pageSize) {
    return activityService.getActivities(page,pageSize).getContent().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }


  @GetMapping("/activity/all")
  public List<ActivityDTO> getAllActivitiesByDirectionId(@RequestParam String directionId){
    return activityService.getActivitiesByDirectionId(directionId).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activity")
  public ActivityDTO getActivityById(@RequestParam String activityId){
    return mapper.toDomain(activityService.getActivityById(activityId));
  }
}
