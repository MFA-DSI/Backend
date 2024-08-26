package com.mfa.report.repository;

import com.mfa.report.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface MissionRepository extends JpaRepository<Mission,String> {
    List<Mission> findAllByDirectionId(String id);
}
