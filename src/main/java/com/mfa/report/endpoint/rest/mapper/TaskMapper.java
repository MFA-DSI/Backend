package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.Task;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskMapper {
    private final ActivityService activityService;
    public TaskDTO toDomain(Task task){
        return TaskDTO.builder()
                .id(task.getId())
                .due_datetime(task.getDueDatetime())
                .description(task.getDescription())
                .build();
    }

    public Task toRest(TaskDTO task){
        return Task.builder()
                .dueDatetime(task.getDue_datetime())
                .description(task.getDescription())
                .build();
    }
}
