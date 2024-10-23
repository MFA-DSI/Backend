package com.mfa.report.service;

import static org.springframework.data.domain.Sort.Direction.ASC;

import com.mfa.report.endpoint.rest.mapper.ActivityMapper;
import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;
import com.mfa.report.repository.MissionRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MissionService {
  private final MissionRepository repository;
  private final NotificationService notificationService;


  public Mission getMissionById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("mission with id." + id + " not found "));
  }

  public List<Mission> findMissionsByIds(List<String> missionIds) {
    return repository.findByIdIn(missionIds);
  }

  public List<Mission> getAllMission(int page, int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "creationDatetime"));
    return repository.findAll(pageable).getContent();
  }

  public List<Mission> getMissionByDirectionId(String directionId, Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "creationDatetime"));
    return repository.findAllByDirectionId(directionId, pageable);
  }



  public Mission crUpdateMission(Mission mission) {
    Mission mission1 = repository.save(mission);
    mission1.getActivity().forEach(activity -> activity.setMission(mission1));
    return mission1;
  }

  public void deleteMission(Mission mission) {
    notificationService.deleteNotificationByMissionId(mission);
    repository.delete(mission);
  }

  public List<Mission> getMissionActivitiesForWeek(
      LocalDate weekStartDate, String directionId, int page, int pageSize) {
    LocalDate weekEndDate = weekStartDate.plusDays(6);
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "creationDatetime"));
    return repository.findByDirectionAndDate(weekStartDate, weekEndDate, pageable).getContent();
  }

  public List<Mission> getMissionActivitiesForMonth(
      int year, int month, String directionId, int page, int pageSize) {
    LocalDate monthStartDate = LocalDate.of(year, month, 1);
    LocalDate monthEndDate = monthStartDate.with(TemporalAdjusters.lastDayOfMonth());

    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "creationDatetime"));
    return repository.findByDirectionAndDate(monthStartDate, monthEndDate, pageable).getContent();
  }

  public List<Mission> getMissionActivitiesForQuarter(
      int year, int quarter, String directionId, int page, int pageSize) {
    LocalDate quarterStartDate;
    LocalDate quarterEndDate =
        switch (quarter) {
          case 1 -> {
            quarterStartDate = LocalDate.of(year, 1, 1);
            yield LocalDate.of(year, 3, 31);
          }
          case 2 -> {
            quarterStartDate = LocalDate.of(year, 4, 1);
            yield LocalDate.of(year, 6, 30);
          }
          case 3 -> {
            quarterStartDate = LocalDate.of(year, 7, 1);
            yield LocalDate.of(year, 9, 30);
          }
          case 4 -> {
            quarterStartDate = LocalDate.of(year, 10, 1);
            yield LocalDate.of(year, 12, 31);
          }
          default -> throw new IllegalArgumentException("Invalid quarter: " + quarter);
        };
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "creationDatetime"));
    return repository
        .findByDirectionAndDate(quarterStartDate, quarterEndDate, pageable)
        .getContent();
  }
}
