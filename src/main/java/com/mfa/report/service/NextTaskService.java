package com.mfa.report.service;

import com.mfa.report.model.*;
import com.mfa.report.repository.NextTaskRepository;
import com.mfa.report.repository.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

  public List<User> getResponsibleFromNextTask(LocalDate currentDatetime) {
    Optional<NextTask> nextTaskOpt = nextTaskRepository.findFirstByDueDatetimeAfterOrderByDueDatetimeAsc(currentDatetime);

    if (nextTaskOpt.isPresent()) {
      NextTask nextTask = nextTaskOpt.get();
      Activity activity = nextTask.getActivity();
      Mission mission = activity.getMission();
      Direction direction = mission.getDirection();
      return direction.getResponsible();
    }
    return Collections.emptyList();
  }
}
