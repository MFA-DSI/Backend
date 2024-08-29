package com.mfa.report.repository;

import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface MissionRepository extends JpaRepository<Mission, String> {
  List<Mission> findAllByDirectionId(String id, Pageable pageable);

  @Query(
          "SELECT m FROM Mission m JOIN Activity a ON m.id = a.mission.id AND a.creationDatetime  BETWEEN :startDate AND :endDate")
  List<Mission> findByDirectionAndDate(
          LocalDate startDate,
          LocalDate endDate);

}
