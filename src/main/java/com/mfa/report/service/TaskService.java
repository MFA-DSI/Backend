package com.mfa.report.service;

import com.mfa.report.model.Task;
import com.mfa.report.repository.TaskRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;

  public Task getTask(String activityId) {
    return taskRepository.findByActivityId(activityId);
  }

  public Task crUpdateTask(Task task) {
    return taskRepository.save(task);
  }

  public List<Task> crUpdateTasks(List<Task> taskList) {
    return taskRepository.saveAll(taskList);
  }

  public void deleteTask(String id) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Task with id" + id + "not found"));
    taskRepository.delete(task);
  }
}
