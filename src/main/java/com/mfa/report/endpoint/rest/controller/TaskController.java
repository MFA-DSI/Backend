package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.TaskMapper;
import com.mfa.report.endpoint.rest.model.DTO.TaskDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Task;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction")
public class TaskController {
    private final ActivityService activityService;
    private  final TaskService service;
    private final TaskMapper mapper;


    @PutMapping("/task")
    public List<TaskDTO> crUpdateTask(@RequestBody List<TaskDTO> taskDTO,@RequestParam String activityId){
        Activity activity = activityService.getActivityById(activityId);

        List<Task> tasks = service.crUpdateTasks(taskDTO.stream().map(e -> mapper.ToRestSave(e, activity)).toList());
        return tasks.stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
    }


    @DeleteMapping("/task/delete")
    public ResponseEntity<String> DeleteTask(@RequestParam String id){
        service.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
