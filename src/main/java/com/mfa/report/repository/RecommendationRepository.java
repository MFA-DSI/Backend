package com.mfa.report.repository;

import com.mfa.report.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {
  Recommendation findByActivityId(String activityId);
}
