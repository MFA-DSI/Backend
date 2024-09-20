package com.mfa.report.service;

import com.mfa.report.model.NextTask;
import com.mfa.report.repository.NextTaskRepository;
import com.mfa.report.repository.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NextTaskService {
  private final NextTaskRepository nextTaskRepository;

  public NextTask getAllByActivity(String activityId) {
    return nextTaskRepository.findByActivityId(activityId);
  }

  public NextTask getNextTaskById(String id) {
    return nextTaskRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("next task with id." + id + " not found "));
  }

  public NextTask crUpdateNextTask(NextTask nextTask) {
    return nextTaskRepository.save(nextTask);
  }

  public List<NextTask> crUpdateNextTasks(List<NextTask> nextTaskList) {
    return nextTaskRepository.saveAll(nextTaskList);
  }

  public List<NextTask> getNextTaskPastDate(){
    return nextTaskRepository.findByDueDatetimeBefore(LocalDate.now());
  }

  public void deleteNextTask(String id){
    NextTask nextTask = nextTaskRepository.findById(id).orElseThrow(() -> new NotFoundException("Next Task with id"+id+"not found"));
    nextTaskRepository.delete(nextTask);
  }
}
