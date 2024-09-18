package com.mfa.report.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.repository.ActivityRepository;
import com.mfa.report.repository.Dao.ActivityDAO;
import com.mfa.report.repository.exception.NotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityService {
  private final ActivityRepository repository;
  private final ActivityDAO activityDAO;

  public String toString(Object object) {
    return "YourClass [attribute1=" + object;
  }

  public Activity getActivitiesByMissionId(String id) {
    return repository.findByMissionId(id);
  }

  public Activity getActivityById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("activity with id." + id + " not found "));
  }

  public Page<Activity> getActivities(int page, int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    return repository.findAll(pageable);
  }

  public Activity crUpdateActivity(Activity activity) {
    return repository.save(activity);
  }

  public void UpdateActivities(List<Activity> activityList) {
    repository.saveAll(activityList);
  }

  public void deleteActivity(Activity activity) {
    repository.delete(activity);
  }

  public Activity saveActivityDetails(ActivityDTO activityDTO) {
    Activity activity = new Activity();
    activity.setObservation(activity.getObservation());
    activity.setPrediction(activity.getPrediction());
    activity.setDueDatetime(activity.getDueDatetime());

    return repository.save(activity);
  }

  public List<Activity> getActivitiesForWeek(
      LocalDate weekStartDate, String directionId, int page, int pageSize) {
    LocalDate weekEndDate = weekStartDate.plusDays(6);
    return activityDAO.findActivitiesByDateRangeAndDirection(
        directionId, weekStartDate, weekEndDate, page, pageSize);
  }

  public List<Activity> getActivitiesByDirectionId(String directionId, int page, int pageSize) {
    return activityDAO.findActivitiesByDateRangeAndDirection(
        directionId, null, null, page, pageSize);
  }

  public List<Activity> getActivitiesForMonth(
      int year, int month, String directionId, int page, int pageSize) {
    LocalDate monthStartDate = LocalDate.of(year, month, 1);
    LocalDate monthEndDate = monthStartDate.with(TemporalAdjusters.lastDayOfMonth());
    return activityDAO.findActivitiesByDateRangeAndDirection(
        directionId, monthStartDate, monthEndDate, page, pageSize);
  }

  public List<Activity> getActivitiesForQuarter(
      int year, int quarter, String directionId, int page, int pageSize) {
    LocalDate quarterStartDate;
    LocalDate quarterEndDate =
        switch (quarter) {
          case 1 -> {
            quarterStartDate = LocalDate.of(year, 1, 1);
            yield LocalDate.of(year, 3, 31);
          }
          case 2 -> {
            quarterStartDate = LocalDate.of(year, 4, 1);
            yield LocalDate.of(year, 6, 30);
          }
          case 3 -> {
            quarterStartDate = LocalDate.of(year, 7, 1);
            yield LocalDate.of(year, 9, 30);
          }
          case 4 -> {
            quarterStartDate = LocalDate.of(year, 10, 1);
            yield LocalDate.of(year, 12, 31);
          }
          default -> throw new IllegalArgumentException("Invalid quarter: " + quarter);
        };

    return activityDAO.findActivitiesByDateRangeAndDirection(
        directionId, quarterStartDate, quarterEndDate, page, pageSize);
  }

  public void DeleteActivities(Activity activity) {
    repository.delete(activity);
  }

  public List<Activity> getActivitiesByIds(List<String>ids){
    return repository.findAllById(ids);
  }


}
