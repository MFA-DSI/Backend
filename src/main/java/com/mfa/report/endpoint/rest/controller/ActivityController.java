package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.model.Activity;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class ActivityController {
  private final ActivityService activityService;

  @GetMapping("/activities/week")
  public List<Activity> getActivitiesForWeek(
      @RequestParam LocalDate weekStartDate, @RequestParam String directionId) {
    return activityService.getActivitiesForWeek(weekStartDate, directionId);
  }

  @GetMapping("/activities/month")
  public List<Activity> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam String directionId) {
    return activityService.getActivitiesForMonth(year, month, directionId);
  }

  @GetMapping("/activities/quarter")
  public List<Activity> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam String directionId) {
    return activityService.getActivitiesForQuarter(year, quarter, directionId);
  }
}