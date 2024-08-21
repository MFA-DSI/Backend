package com.mfa.report.repository;

import com.mfa.report.repository.model.PerformanceRealization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@EnableJpaRepositories
@Repository
public interface PerfomanceRealization extends JpaRepository<PerformanceRealization,String> {
}
