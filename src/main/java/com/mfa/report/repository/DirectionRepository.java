package com.mfa.report.repository;

import com.mfa.report.repository.model.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface DirectionRepository extends JpaRepository<Direction,String> {
}
