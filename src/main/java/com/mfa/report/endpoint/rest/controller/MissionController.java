package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/mission/{id}")
  public MissionDTO getMissionById(@PathVariable String id) {
    return mapper.toDomain(service.getMissionById(id));
  }

  @GetMapping("/mission/all")
  @Cacheable(value = "mission", key = "#directionId")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getAllMission(
      @RequestParam(name = "directionId") String directionId,
      @RequestParam(defaultValue = "1", name = "page") Integer page,
      @RequestParam(defaultValue = "15", name = "page_size") Integer pageSize) {
  log.info("it was a get");
    return service.getMissionByDirectionId(directionId, page, pageSize).stream()
        .map(mapper::toDomainList)
        .toList();
  }

  // public List<MissionDTO> updateMission(@RequestBody MissionDTO missionDTO , @RequestParam String
  // directionId){
  //     List<Activity> activity =
  // missionDTO.getActivityDTOList().stream().map(activityMapper::toRest).toList();
  //     activityValidator.accept(activity);
  //    activityService.UpdateActivities(activity);
  //      return new List<MissionDTO>() {
  //    }
  // }

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

  @GetMapping("/mission/week")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForWeek(
      @RequestParam LocalDate weekStartDate, @RequestParam String directionId) {
    return service.getActivitiesForWeek(weekStartDate, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/month")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForMonth(
      @RequestParam int year, @RequestParam int month, @RequestParam String directionId) {
    return service.getActivitiesForMonth(year, month, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/quarter")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionDTO> getActivitiesForQuarter(
      @RequestParam int year, @RequestParam int quarter, @RequestParam String directionId) {
    return service.getActivitiesForQuarter(year, quarter, directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }
}
