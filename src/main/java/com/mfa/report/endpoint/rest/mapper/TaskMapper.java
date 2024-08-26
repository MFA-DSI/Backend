package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.Activity;
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
                .dueDatetime(task.getDueDatetime())
                .description(task.getDescription())
                .build();
    }

    public Task toRest(TaskDTO task){
        return Task.builder()
                .dueDatetime(task.getDueDatetime())
                .description(task.getDescription())
                .build();
    }

    public Task ToRestSave(TaskDTO task, Activity activity){
        return  Task.builder()
                .description(task.getDescription())
                .dueDatetime(task.getDueDatetime())
                .activity(activity)
                .build();
    }
}
