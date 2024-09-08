package com.mfa.report.service;

import com.mfa.report.endpoint.rest.mapper.NextTaskMapper;
import com.mfa.report.endpoint.rest.mapper.PerfRealizationMapper;
import com.mfa.report.endpoint.rest.mapper.RecommendationMapper;
import com.mfa.report.endpoint.rest.mapper.TaskMapper;
import com.mfa.report.endpoint.rest.model.DTO.NextTaskDTO;
import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;
import com.mfa.report.model.NextTask;
import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.model.Task;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AssociatedEntitiesService {
  private final TaskService taskService;
  private final RecommendationService recommendationService;
  private final NextTaskService nextTaskService;
  private final PerformanceRealizationService performanceRealizationService;

  private final TaskMapper taskMapper;
  private final NextTaskMapper nextTaskMapper;
  private final RecommendationMapper recommendationMapper;
  private final PerfRealizationMapper perfRealizationMapper;

  @Async
  public CompletableFuture<Void> saveTaskAsync(Task task) {
    try {
      taskService.crUpdateTask(task);
      return CompletableFuture.completedFuture(null);
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  @Async
  public CompletableFuture<Void> saveNextTaskAsync(NextTask nextTask) {
    try {
      nextTaskService.crUpdateNextTask(nextTask);
      return CompletableFuture.completedFuture(null);
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  @Async
  public PerformanceRealization savePerformanceRealizationAsync(
          PerformanceRealization performanceRealization) {
    return performanceRealizationService.crUpdatePerformance(performanceRealization);
  }

  public Activity AttachEntitiesToActivity(
          Activity activity,
          List<TaskDTO> taskList,
          List<NextTaskDTO> nextTaskList,
          List<PerfRealizationDTO> perfRealizationDTO) {

    if(taskList != null && !taskList.isEmpty()){
      taskList.forEach(
              task -> {
                task.setActivityId(activity.getId());
                saveTaskAsync(taskMapper.ToRestSave(task, activity));
              });
      activity.setTaskList(taskList.stream().map(taskMapper::toRest).toList());
    }


    if(nextTaskList != null && !nextTaskList.isEmpty()){
      nextTaskList.forEach(
              nextTaskDTO -> {
                nextTaskDTO.setActivityId(activity.getId());
                saveNextTaskAsync(nextTaskMapper.toRestSave(nextTaskDTO, activity));
              });

      activity.setNexTaskList(nextTaskList.stream().map(nextTaskMapper::toRest).toList());

    }
    if(perfRealizationDTO != null && ! perfRealizationDTO.isEmpty()){
      perfRealizationDTO.forEach(
              (perfRealizationDTO1 -> {
                perfRealizationDTO1.setActivityId(activity.getId());
                savePerformanceRealizationAsync(perfRealizationMapper.toRest(perfRealizationDTO1));
              }));
      activity.setPerformanceRealization(
              perfRealizationDTO.stream().map(perfRealizationMapper::toRest).toList());

    }


    return activity;
  }

  @Async
  public void attachActivitiesToMission(List<Activity> activity, Mission mission) {
    activity.forEach(activity1 -> activity1.setMission(mission));
  }
}
