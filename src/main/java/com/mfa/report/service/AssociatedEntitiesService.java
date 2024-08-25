package com.mfa.report.service;

import com.mfa.report.endpoint.rest.mapper.NextTaskMapper;
import com.mfa.report.endpoint.rest.mapper.PerfRealizationMapper;
import com.mfa.report.endpoint.rest.mapper.RecommendationMapper;
import com.mfa.report.endpoint.rest.mapper.TaskMapper;
import com.mfa.report.endpoint.rest.model.DTO.NextTaskDTO;
import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.NextTask;
import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.Task;
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

    public void AttachEntitiesToActivity(String activityId, List<TaskDTO> taskList, List<NextTaskDTO> nextTaskList, List<RecommendationDTO> recommendations, PerfRealizationDTO perfRealizationDTO){

        taskList.forEach(task -> {
            task.setActivityId(activityId);
            saveTaskAsync(taskMapper.toRest(task));
        });

        nextTaskList.forEach(nextTaskDTO -> {
                    nextTaskDTO.setActivityId(activityId);
                    saveNextTaskAsync(nextTaskMapper.toRest(nextTaskDTO));
                }
        );

        recommendations.forEach(recommendationDTO ->{
            recommendationDTO.setActivityId(activityId);
            saveRecommendationAsync(recommendationMapper.toRest(recommendationDTO));
        });

            perfRealizationDTO.setActivityId(activityId);
            savePerformanceRealizationAsync(perfRealizationMapper.toRest(perfRealizationDTO));
    }
}
