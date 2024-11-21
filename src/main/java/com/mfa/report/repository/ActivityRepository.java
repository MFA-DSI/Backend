package com.mfa.report.repository;

import com.mfa.report.model.Activity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ActivityRepository extends JpaRepository<Activity, String> {
  Activity findByMissionId(String id);
  List<Activity> findByReportRequestId(String id);
}
