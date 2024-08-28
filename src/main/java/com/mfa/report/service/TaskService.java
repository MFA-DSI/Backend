package com.mfa.report.service;


import com.mfa.report.repository.TaskRepository;
import com.mfa.report.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task getTask(String activityId){
        return taskRepository.findByActivityId(activityId);
    }

    public Task crUpdateTask(Task task){
        return taskRepository.save(task);
    }

    public List<Task> crUpdateTasks(List<Task> taskList){
        return taskRepository.saveAll(taskList);
    }
}
