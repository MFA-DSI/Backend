package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.service.MissionService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ActivityMapper {
  private final TaskMapper taskMapper;
  private final NextTaskMapper nextTaskMapper;
  private final PerfRealizationMapper perfRealizationMapper;
  private final RecommendationMapper recommendationMapper;
  private final MissionService service;

  public ActivityDTO toDomain(Activity activity) {
    return ActivityDTO.builder()
        .id(activity.getId())
        .description(activity.getDescription())
        .dueDatetime(activity.getDueDatetime())
        .observation(activity.getObservation())
        .prediction(activity.getPrediction())
        .task(
            activity.getTaskList() != null
                ? activity.getTaskList().stream()
                    .map(taskMapper::toDomain)
                    .collect(Collectors.toList())
                : null)
        .nextTask(
            activity.getNexTaskList() != null
                ? activity.getNexTaskList().stream()
                    .map(nextTaskMapper::toDomain)
                    .collect(Collectors.toList())
                : null)
        .performanceRealization(
            activity.getPerformanceRealization() != null
                ? activity.getPerformanceRealization().stream()
                    .map(perfRealizationMapper::toDomain)
                    .collect(Collectors.toUnmodifiableList())
                : null)
        .recommendation(
            activity.getRecommendations() == null
                ? null
                : activity.getRecommendations().stream()
                    .map(recommendationMapper::toDomain)
                    .collect(Collectors.toList()))
        .build();
  }

  public Activity toRest(ActivityDTO activityDTO) {
    return Activity.builder()
        .description(activityDTO.getDescription())
        .prediction(activityDTO.getPrediction())
        .observation(activityDTO.getObservation())
        .dueDatetime(activityDTO.getDueDatetime())
        .creationDatetime(LocalDateTime.now())
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.Activity toDomainList(Activity activity) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Activity.builder()
        .id(activity.getId())
        .description(activity.getDescription())
        .performanceRealization(
            activity.getPerformanceRealization().stream()
                .map(perfRealizationMapper::toDomainList)
                .collect(Collectors.toUnmodifiableList()))
        .build();
  }
}
