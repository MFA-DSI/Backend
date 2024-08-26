package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.model.*;
import com.mfa.report.model.validator.ActivityValidator;
import com.mfa.report.model.validator.MissionValidator;
import com.mfa.report.service.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
public class MissionController {

    private final MissionService service;
    private final ActivityService activityService;
    private final TaskService taskService;
    private final NextTaskService nextTaskService;
    private final RecommendationService recommendationService;
    private final PerformanceRealizationService performanceRealization;
    private final AssociatedEntitiesService associatedEntitiesService;
    private final DirectionService directionService;

    private final MissionMapper mapper;
    private final ActivityMapper activityMapper;

    private final MissionValidator missionValidator;
    private final ActivityValidator activityValidator;
    @GetMapping("/mission/{id}")
    public MissionDTO getMissionById(@PathVariable  String id){
        return mapper.toDomain(service.getMissionById(id));
    }

    @GetMapping("/mission/all")
    public List<MissionDTO> getAllMission(@RequestParam(name = "directionId") String directionId){
        return service.getMissionByDirectionId(directionId).stream().map(mapper::toDomain).toList();
    }

   // public List<MissionDTO> updateMission(@RequestBody MissionDTO missionDTO , @RequestParam String directionId){
   //     List<Activity> activity = missionDTO.getActivityDTOList().stream().map(activityMapper::toRest).toList();
   //     activityValidator.accept(activity);
    //    activityService.UpdateActivities(activity);
      //      return new List<MissionDTO>() {
        //    }
    //}

    @PostMapping("/mission/create")
    @Transactional
    public MissionDTO createMission(@RequestParam(name = "directionId") String directionId,@RequestBody  MissionDTO missionDTO){
        Direction direction = directionService.getDirectionById(directionId);

        Mission mission = mapper.toRest(missionDTO,direction);


        List<Activity> activityList = new ArrayList<>();
        for(ActivityDTO activityDTO : missionDTO.getActivityList()){
          Activity activity =  activityMapper.toRest(activityDTO);

          Activity activity1 =  associatedEntitiesService.AttachEntitiesToActivity(activity, activityDTO.getTask(), activityDTO.getNextTask(), activityDTO.getRecommendation(), activityDTO.getPerfRealizationDTO());
            activityService.crUpdateActivity(activity1);
          activityList.add(activity1);
        }

        mission.setActivity(activityList);
        service.crUpdateMission(mission);
        return mapper.toDomain(mission);
    }
}
