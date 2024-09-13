package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.MissionWithDirectionName;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.ActivityValidator;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.model.validator.MissionValidator;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.AssociatedEntitiesService;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.MissionService;
import com.mfa.report.service.NextTaskService;
import com.mfa.report.service.PerformanceRealizationService;
import com.mfa.report.service.RecommendationService;
import com.mfa.report.service.TaskService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*", allowedHeaders = "*",originPatterns = "*")
@Slf4j
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
  private final DirectionValidator directionValidator;

  @GetMapping("/mission")
  public MissionWithDirectionName getMissionById(@RequestParam String id) {
    return mapper.toDomainDirection(service.getMissionById(id));
  }

  @GetMapping("/mission/direction")
  @Cacheable(value = "mission", key = "#directionId")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getAllMissionByDirectionId(
      @RequestParam(name = "directionId") String directionId,
      @RequestParam(defaultValue = "1", name = "page") Integer page,
      @RequestParam(defaultValue = "15", name = "page_size") Integer pageSize) {
  
    return service.getMissionByDirectionId(directionId, page, pageSize).stream()
        .map(mapper::toDomainList)
        .toList();
  }
  

  @GetMapping("/mission/all")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getAllMission(
          @RequestParam(defaultValue = "1", name = "page") Integer page,
          @RequestParam(defaultValue = "2", name = "page_size") Integer pageSize
  ){
    return  service.getAllMission(page,pageSize).stream().map(mapper::toDomainList).collect(Collectors.toUnmodifiableList());
  }

  @PostMapping("/mission/create")
  @Transactional
  public MissionDTO createMission(
      @RequestParam(name = "directionId") String directionId,
      @RequestParam(name = "userId") String userId,
      @RequestBody MissionDTO missionDTO) {
    Direction direction = directionService.getDirectionById(directionId);
    directionValidator.acceptUser(direction, userId);
    Mission mission = mapper.toRest(missionDTO, direction);

    List<Activity> activityList =
        missionDTO.getActivityList().stream()
            .map(
                activityDTO -> {
                  Activity activity = activityMapper.toRest(activityDTO);
                  return associatedEntitiesService.AttachEntitiesToActivity(
                      activity,
                      activityDTO.getTask(),
                      activityDTO.getNextTask(),
                      activityDTO.getPerfRealizationDTO());
                })
            .peek(activityService::crUpdateActivity)
            .collect(Collectors.toList());

    mission.setActivity(activityList);
    service.crUpdateMission(mission);

    return mapper.toDomain(mission);
  }

  @PutMapping("/mission/update")
  @Transactional
  public ResponseEntity<MissionDTO> updateMission(
          @RequestParam(name = "missionId") String missionId,
          @RequestParam(name = "userId") String userId,
          @RequestBody MissionDTO missionDTO) {

    // Fetch existing mission
    Mission existingMission = service.getMissionById(missionId);
    if (existingMission == null) {
      return ResponseEntity.notFound().build();
    }

    // Fetch direction and validate user
    Direction direction = directionService.getDirectionById(missionDTO.getDirection().getId());
    directionValidator.acceptUser(direction, userId);

    // Update the existing mission fields
    existingMission.setDescription(missionDTO.getName());

    // Update activities
    List<Activity> updatedActivityList = missionDTO.getActivityList().stream()
            .map(activityDTO -> {
              Activity activity = activityMapper.toRest(activityDTO);
              // Assuming AttachEntitiesToActivity method handles the association and persistence of tasks, nextTasks, etc.
              return associatedEntitiesService.AttachEntitiesToActivity(
                      activity,
                      activityDTO.getTask(),
                      activityDTO.getNextTask(),
                      activityDTO.getPerfRealizationDTO());
            })
            .peek(activityService::crUpdateActivity)
            .collect(Collectors.toList());


    existingMission.setActivity(updatedActivityList);
    
    service.crUpdateMission(existingMission);

    return ResponseEntity.ok(mapper.toDomain(existingMission));
  }



  @DeleteMapping("/mission/delete")
  public ResponseEntity<String> deleteMission(
          @RequestParam(name = "missionId") String missionId,
          @RequestParam(name="userId") String userId
  ) {


    Mission existingMission = service.getMissionById(missionId);
    if (existingMission == null) {
      return ResponseEntity.notFound().build();
    }

    Direction associatedDirection = existingMission.getDirection();
    if (associatedDirection == null) {
      return ResponseEntity.badRequest().body(null);
    }
    directionValidator.acceptUser(associatedDirection, userId);


    service.deleteMission(existingMission);

    return ResponseEntity.ok("Mission deleted successfully");
  }

  @GetMapping("/mission/week")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForWeek(
      @RequestParam LocalDate weekStartDate, @RequestParam String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return service.getActivitiesForWeek(weekStartDate, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/month")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return service.getActivitiesForMonth(year, month, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/quarter")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam String directionId,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int pageSize) {
    return service.getActivitiesForQuarter(year, quarter, directionId,page,pageSize).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }
}
