package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.NextTaskMapper;
import com.mfa.report.endpoint.rest.model.DTO.NextTaskDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.NextTask;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.NextTaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/direction")
@CrossOrigin(origins = "*", allowedHeaders = "*", originPatterns = "*")
public class NextTaskController {
    private final NextTaskService service;
    private final ActivityService activityService;
    private final NextTaskMapper mapper;

    @PutMapping("/nextTask")
    public List<NextTask> crUpdate(@RequestBody List<NextTaskDTO> nextTaskDTO,@RequestParam  String activityId){
        Activity activity = activityService.getActivityById(activityId);
        return service.crUpdateNextTasks(nextTaskDTO.stream().map(e->mapper.toRestSave(e,activity)).toList());
    }


    @DeleteMapping("/nextTask/delete")
    public ResponseEntity<String> deleteNextTask(@RequestParam String id){
        service.deleteNextTask(id);
        return ResponseEntity.ok("Next task deleted successfully");
    }

}
