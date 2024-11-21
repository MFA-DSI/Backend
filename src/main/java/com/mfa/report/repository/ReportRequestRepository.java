package com.mfa.report.repository;

import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.enumerated.RequestReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface ReportRequestRepository extends JpaRepository<ReportRequest, String> {

  List<ReportRequest> findAllByRequesterDirectionId(String directionId);
  List<ReportRequest> findAllByTargetDirectionId(String directionId);
  ReportRequest findByStartedAtAndTargetDirectionId(LocalDate startedAt,String directionId);

  List<ReportRequest> findByStatusAndExpirationAtBefore(
      @Param("status") RequestReportStatus status, @Param("expirationAt") LocalDateTime expiration);
}
