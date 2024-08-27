package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.service.MissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ActivityMapper {
    private final TaskMapper taskMapper;
    private final NextTaskMapper nextTaskMapper;
    private final PerfRealizationMapper perfRealizationMapper;
    private final RecommendationMapper recommendationMapper;
    private final MissionService service;

    public ActivityDTO toDomain(Activity activity){
        return ActivityDTO.builder()
                .id(activity.getId())
                .description(activity.getDescription())
                .dueDatetime(activity.getCreationDatetime())
                .observation(activity.getObservation())
                .prediction(activity.getPrediction())
                .task(activity.getTaskList().stream().map(taskMapper::toDomain).collect(Collectors.toList()))
                .nextTask(activity.getNexTaskList().stream().map(nextTaskMapper::toDomain).collect(Collectors.toList()))
                .perfRealizationDTO(perfRealizationMapper.toDomain(activity.getPerformanceRealization()) )
                .recommendation(activity.getRecommendations().stream().map(recommendationMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public Activity toRest(ActivityDTO activityDTO){
        return Activity.builder()
                .description(activityDTO.getDescription())
                .prediction(activityDTO.getPrediction())
                .observation(activityDTO.getObservation())
                .creationDatetime(activityDTO.getDueDatetime())
                .build();
    }




}
