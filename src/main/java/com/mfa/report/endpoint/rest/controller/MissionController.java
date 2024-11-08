package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.MissionWithDirectionName;
import com.mfa.report.model.*;
import com.mfa.report.model.event.MissionPostedEvent;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.*;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*", allowedHeaders = "*", originPatterns = "*")
@Slf4j
public class MissionController {

  private final MissionService service;
  private final ActivityService activityService;
  private final AssociatedEntitiesService associatedEntitiesService;
  private final DirectionService directionService;
  private final ServiceService serviceService;

  private final MissionMapper mapper;
  private final ActivityMapper activityMapper;

  private final DirectionValidator directionValidator;

  @Autowired private ApplicationEventPublisher eventPublisher;

  @GetMapping("/mission")
  public MissionWithDirectionName getMissionById(@RequestParam String id) {
    return mapper.toDomainDirection(service.getMissionById(id));
  }

  @GetMapping("/mission/directions")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getAllMissionByDirectionId(
      @RequestParam(name = "directionId") String directionId,
      @RequestParam(defaultValue = "1", name = "page") Integer page,
      @RequestParam(defaultValue = "50", name = "page_size") Integer pageSize) {

    return service.getMissionByDirectionId(directionId, page, pageSize).stream()
        .map(mapper::toDomainList)
        .toList();
  }

  @GetMapping("/mission/name")
  @Cacheable(value = "mission", key = "#directionId")
  public List<MissionWithDirectionName> getAllMissionNameByDirectionId(
      @RequestParam(name = "directionId") String directionId,
      @RequestParam(defaultValue = "1", name = "page") Integer page,
      @RequestParam(defaultValue = "15", name = "page_size") Integer pageSize) {

    return service.getMissionByDirectionId(directionId, page, pageSize).stream()
        .map(mapper::toDomainDirection)
        .toList();
  }

  @GetMapping("/mission/all")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getAllMission(
      @RequestParam(defaultValue = "1", name = "page") Integer page,
      @RequestParam(defaultValue = "15", name = "page_size") Integer pageSize) {
    return service.getAllMission(page, pageSize).stream()
        .map(mapper::toDomainList)
        .collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/mission/create")
  @Transactional
  public com.mfa.report.endpoint.rest.model.RestEntity.Mission createOrUpdateMission(
          @RequestParam(name = "directionId") String directionId,
          @RequestParam(name = "userId") String userId,
          @RequestBody MissionDTO missionDTO) {

    Direction direction = directionService.getDirectionById(directionId);
    directionValidator.acceptUser(direction, userId);
    missionDTO.setPostedBy(userId);

    Service service1 = serviceService.getServiceById(missionDTO.getServiceId());
    Mission mission;

    if (missionDTO.getId() != null) {
      // Récupère la mission existante
      mission = service.getMissionById(missionDTO.getId());

      // Clone la liste des activités existantes dans une liste modifiable

      List<Activity> newActivities = missionDTO.getActivityList().stream()
              .map(activityDTO -> {
                Activity activity = activityMapper.toRest(activityDTO);
                activity.setMission(mission);
                return associatedEntitiesService.AttachEntitiesToActivity(
                        activity,
                        activityDTO.getTask(),
                        activityDTO.getNextTask(),
                        activityDTO.getPerformanceRealization());
              })
              .peek(activityService::crUpdateActivity)
              .collect(Collectors.toCollection(ArrayList::new));
    } else {
      // Crée une nouvelle mission
      mission = mapper.toRest(missionDTO, direction, service1);

      List<Activity> activityList = missionDTO.getActivityList().stream()
              .map(activityDTO -> {
                Activity activity = activityMapper.toRest(activityDTO);
                return associatedEntitiesService.AttachEntitiesToActivity(
                        activity,
                        activityDTO.getTask(),
                        activityDTO.getNextTask(),
                        activityDTO.getPerformanceRealization());
              })
              .peek(activityService::crUpdateActivity)
              .collect(Collectors.toCollection(ArrayList::new));

      mission.setActivity(activityList);
    }

    // Enregistre la mission
    service.crUpdateMission(mission);
    eventPublisher.publishEvent(new MissionPostedEvent(mission, direction));

    return mapper.toDomainWithService(mission);
  }




  @PutMapping("/mission/update")
  @Transactional
  public ResponseEntity<MissionDTO> updateMission(
      @RequestParam(name = "missionId") String missionId,
      @RequestParam(name = "userId") String userId,
      @RequestParam(name = "directionId") String directionId,
      @RequestBody MissionDTO missionDTO) {

    Mission existingMission = service.getMissionById(missionId);
    if (existingMission == null) {
      return ResponseEntity.notFound().build();
    }

    Direction direction = directionService.getDirectionById(directionId);
    directionValidator.acceptUser(direction, userId);

    existingMission.setDescription(missionDTO.getName());

    Mission updatedMission = service.crUpdateMission(existingMission);

    return ResponseEntity.ok(mapper.toDomain(updatedMission));
  }

  @DeleteMapping("/mission/delete")
  public ResponseEntity<String> deleteMission(
      @RequestParam(name = "missionId") String missionId,
      @RequestParam(name = "userId") String userId) {

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
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getMissionsForWeek(
      @RequestParam LocalDate weekStartDate,
      @RequestParam String directionId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize) {
    return service.getMissionActivitiesForWeek(weekStartDate, directionId, page, pageSize).stream()
        .map(mapper::toDomainList)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/month")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getMissionsForMonth(
      @RequestParam int year,
      @RequestParam int month,
      @RequestParam String directionId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize) {
    return service.getMissionActivitiesForMonth(year, month, directionId, page, pageSize).stream()
        .map(mapper::toDomainList)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mission/quarter")
  public List<com.mfa.report.endpoint.rest.model.RestEntity.Mission> getMissionsForQuarter(
      @RequestParam int year,
      @RequestParam int quarter,
      @RequestParam String directionId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize) {
    return service
        .getMissionActivitiesForQuarter(year, quarter, directionId, page, pageSize)
        .stream()
        .map(mapper::toDomainList)
        .collect(Collectors.toUnmodifiableList());
  }
}
