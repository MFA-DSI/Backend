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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void saveTaskAsync(Task task){
        taskService.crUpdateTask(task);
    }

    @Async
    public void saveRecommendationAsync(Recommendation recommendation){
        recommendationService.crUpdateRecommendation(recommendation);
    }

    @Async
    public void saveNextTaskAsync(NextTask nextTask){
        nextTaskService.crUpdateNextTask(nextTask);
    }


    @Async
    public PerformanceRealization savePerformanceRealizationAsync(PerformanceRealization performanceRealization){
        return performanceRealizationService.crUpdatePerformance(performanceRealization);
    }

    public Activity AttachEntitiesToActivity(Activity activity, List<TaskDTO> taskList, List<NextTaskDTO> nextTaskList, List<RecommendationDTO> recommendations, PerfRealizationDTO perfRealizationDTO){

        taskList.forEach(task -> {
            task.setActivityId(activity.getId());
            saveTaskAsync(taskMapper.ToRestSave(task,activity));
        });
        activity.setTaskList(taskList.stream().map(taskMapper::toRest).toList());


        nextTaskList.forEach(nextTaskDTO -> {
                    nextTaskDTO.setActivityId(activity.getId());
                   saveNextTaskAsync(nextTaskMapper.toRestSave(nextTaskDTO,activity));
                }
        );

        activity.setNexTaskList(nextTaskList.stream().map(nextTaskMapper::toRest).toList());



        recommendations.forEach(recommendationDTO ->{
            recommendationDTO.setActivityId(activity.getId());
            saveRecommendationAsync(recommendationMapper.toRestSave(recommendationDTO,activity));
        });
        activity.setRecommendations(recommendations.stream().map(recommendationMapper::toRest).toList());

            perfRealizationDTO.setActivityId(activity.getId());
            PerformanceRealization performanceRealization = savePerformanceRealizationAsync(perfRealizationMapper.toRest(perfRealizationDTO));
            activity.setPerformanceRealization(performanceRealization);

            return activity;
    }
}
