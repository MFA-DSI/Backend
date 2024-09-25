package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.NextTaskDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.NextTask;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NextTaskMapper {
  private final ActivityService activityService;

  public NextTaskDTO toDomain(NextTask task) {
    return NextTaskDTO.builder()
        .id(task.getId())
        .description(task.getDescription())
        .dueDatetime(task.getDueDatetime())
        .build();
  }

  public NextTask toRest(NextTaskDTO nextTask) {
    return NextTask.builder()
        .description(nextTask.getDescription())
        .dueDatetime(nextTask.getDueDatetime())
        .build();
  }

  public NextTask toRestSave(NextTaskDTO nextTask, Activity activity) {
    return NextTask.builder()
        .id(nextTask.getId())
        .description(nextTask.getDescription())
        .dueDatetime(nextTask.getDueDatetime())
        .activity(activity)
        .build();
  }
}
