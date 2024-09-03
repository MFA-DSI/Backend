package com.mfa.report.service;

import static org.springframework.data.domain.Sort.Direction.ASC;

import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;
import com.mfa.report.repository.MissionRepository;
import com.mfa.report.repository.exception.NotFoundException;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MissionService {
  private final MissionRepository repository;

  public Mission getMissionById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("mission with id." + id + " not found "));
  }

  public List<Mission> getMissionByDirectionId(String directionId, Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(ASC, "description"));
    return repository.findAllByDirectionId(directionId, pageable);
  }

  public Mission crUpdateMission(Mission mission) {
    Mission mission1 = repository.save(mission);
    mission1.getActivity().forEach(activity -> activity.setMission(mission1));

    return mission1;
  }

  public List<Mission> getActivitiesForWeek(LocalDate weekStartDate, String directionId) {
    LocalDate weekEndDate = weekStartDate.plusDays(6);
    return repository.findByDirectionAndDate(weekStartDate, weekEndDate);
  }

  public List<Mission> getActivitiesForMonth(int year, int month, String directionId) {
    LocalDate monthStartDate = LocalDate.of(year, month, 1);
    LocalDate monthEndDate = monthStartDate.with(TemporalAdjusters.lastDayOfMonth());
    log.info(String.valueOf("direction : " + directionId));
    return repository.findByDirectionAndDate(monthStartDate, monthEndDate);
  }

  public List<Mission> getActivitiesForQuarter(int year, int quarter, String directionId) {
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

    return repository.findByDirectionAndDate(quarterStartDate, quarterEndDate);
  }
}
