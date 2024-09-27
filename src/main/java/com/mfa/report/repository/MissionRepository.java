package com.mfa.report.repository;

import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface MissionRepository extends JpaRepository<Mission, String> {
  
  @Query("SELECT m FROM Mission m WHERE m.direction.id = :directionId")
  List<Mission> findAllByDirectionId(@Param("directionId") String directionId,Pageable pageable);

  @Query("SELECT m FROM Mission m JOIN Activity a ON m.id = a.mission.id AND a.dueDatetime BETWEEN :startDate AND :endDate")
  Page<Mission> findByDirectionAndDate(
           LocalDate startDate,
           LocalDate endDate,
          Pageable pageable);

}
