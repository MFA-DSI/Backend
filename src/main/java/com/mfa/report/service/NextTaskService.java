package com.mfa.report.service;


import com.mfa.report.repository.NextTaskRepository;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.model.NextTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NextTaskService {
    private final NextTaskRepository nextTaskRepository;

    public NextTask getAllByActivity(String activityId){
        return nextTaskRepository.findByActivityId(activityId);
    }

    public NextTask getNextTaskById(String id){
        return nextTaskRepository.findById(id).orElseThrow(() -> new NotFoundException("next task with id." + id + " not found "));
    }


    public NextTask crUpdateNextTask(NextTask nextTask){
         return nextTaskRepository.save(nextTask);
    }

    public List<NextTask> addNextTaskList(List<NextTask> nextTaskList){return nextTaskRepository.saveAll(nextTaskList);
    }
}
