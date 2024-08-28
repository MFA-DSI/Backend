package com.mfa.report.service;

import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.repository.ActivityRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
  private final ActivityRepository repository;

  public Activity getActivitiesByMissionId(String id) {
    return repository.findByMissionId(id);
  }

  public Activity getActivityById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("activity with id." + id + " not found "));
  }

  public Activity crUpdateActivity(Activity activity) {
    return repository.save(activity);
  }

  public void UpdateActivities(List<Activity> activityList) {
    repository.saveAll(activityList);
  }

  public void deleteActivities(Activity activity) {
    repository.delete(activity);
  }

  public Activity saveActivityDetails(ActivityDTO activityDTO) {
    Activity activity = new Activity();
    activity.setObservation(activity.getObservation());
    activity.setPrediction(activity.getPrediction());
    activity.setCreationDatetime(activity.getCreationDatetime());

    return repository.save(activity);
  }
}
