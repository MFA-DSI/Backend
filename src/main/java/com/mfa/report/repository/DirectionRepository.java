package com.mfa.report.repository;

import com.mfa.report.model.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface DirectionRepository extends JpaRepository<Direction, String> {
  Optional<Direction> findById(String id);
}
