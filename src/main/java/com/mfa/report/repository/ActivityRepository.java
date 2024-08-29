package com.mfa.report.repository;

import com.mfa.report.model.Activity;
import java.time.LocalDate;
import java.util.List;

import com.mfa.report.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ActivityRepository extends JpaRepository<Activity, String> {
  Activity findByMissionId(String id);

  @Query(
      "SELECT a FROM Activity a LEFT JOIN FETCH a.taskList t LEFT JOIN FETCH a.nexTaskList nt LEFT JOIN FETCH a.recommendations r WHERE a.id = :id")
  Activity findByIdWithActivityDetails(String id);


  //TODO: fix this and attach logo to direction
  @Query(
      "SELECT m FROM Mission m JOIN Activity a ON m.id = a.mission.id AND a.creationDatetime  BETWEEN :startDate AND :endDate")
  List<Activity> findByDirectionAndDate(
       LocalDate startDate,
       LocalDate endDate);

}
