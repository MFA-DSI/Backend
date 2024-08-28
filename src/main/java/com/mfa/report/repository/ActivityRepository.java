package com.mfa.report.repository;

import com.mfa.report.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ActivityRepository extends JpaRepository<Activity, String> {
  Activity findByMissionId(String id);

  @Query(
      "SELECT a FROM Activity a LEFT JOIN FETCH a.taskList t LEFT JOIN FETCH a.nexTaskList nt LEFT JOIN FETCH a.recommendations r WHERE a.id = :id")
  Activity findByIdWithActivityDetails(String id);
}
