package com.mfa.report.service;

import com.mfa.report.endpoint.rest.mapper.NextTaskMapper;
import com.mfa.report.endpoint.rest.mapper.PerfRealizationMapper;
import com.mfa.report.endpoint.rest.mapper.RecommendationMapper;
import com.mfa.report.endpoint.rest.mapper.TaskMapper;
import com.mfa.report.endpoint.rest.model.DTO.NextTaskDTO;
import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.*;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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



    public Task saveTaskAsync(Task task){
        Task task1 = taskService.crUpdateTask(task);
        return task1;
    }


    public Recommendation saveRecommendationAsync(Recommendation recommendation){
        Recommendation recommendation1 = recommendationService.crUpdateRecommendation(recommendation);
        return recommendation1;
    }


    public NextTask saveNextTaskAsync(NextTask nextTask){
        NextTask nextTask1 = nextTaskService.addNextTask(nextTask);
        return nextTask1;
    }


    public PerformanceRealization savePerformanceRealizationAsync(PerformanceRealization performanceRealization){
        PerformanceRealization performanceRealization1 = performanceRealizationService.crUpdatePerformance(performanceRealization);
        return performanceRealization1;
    }

    public Activity AttachEntitiesToActivity(Activity activity, List<TaskDTO> taskList, List<NextTaskDTO> nextTaskList, List<RecommendationDTO> recommendations, PerfRealizationDTO perfRealizationDTO){

        taskList.forEach(task -> {
            task.setActivityId(activity.getId());
            saveTaskAsync(taskMapper.toRest(task));
        });
        activity.setTaskList(taskList.stream().map(taskMapper::toRest).toList());

        nextTaskList.forEach(nextTaskDTO -> {
                    nextTaskDTO.setActivityId(activity.getId());
                    saveNextTaskAsync(nextTaskMapper.toRest(nextTaskDTO));
                }
        );
        activity.setNexTaskList(nextTaskList.stream().map(nextTaskMapper::toRest).toList());

        recommendations.forEach(recommendationDTO ->{
            recommendationDTO.setActivityId(activity.getId());
            saveRecommendationAsync(recommendationMapper.toRest(recommendationDTO));
        });
        activity.setRecommendations(recommendations.stream().map(recommendationMapper::toRest).toList());

            perfRealizationDTO.setActivityId(activity.getId());
            PerformanceRealization performanceRealization = savePerformanceRealizationAsync(perfRealizationMapper.toRest(perfRealizationDTO));
            activity.setPerformanceRealization(performanceRealization);

            return activity;
    }
}
