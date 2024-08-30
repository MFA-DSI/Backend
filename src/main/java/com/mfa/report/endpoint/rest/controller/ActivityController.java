package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    return activityService.getActivitiesForWeek(weekStartDate, directionId).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/month")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #month")
  public List<ActivityDTO> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam String directionId) {
    return activityService.getActivitiesForMonth(year, month, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/quarter")
  @Cacheable(value = "activity", key = "#directionId+ ':' + #quarter")
  public List<ActivityDTO> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam String directionId) {
    return activityService.getActivitiesForQuarter(year, quarter, directionId).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/activities/all")
  public List<ActivityDTO> getAllActivities() {
    return activityService.getActivities().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }
}
