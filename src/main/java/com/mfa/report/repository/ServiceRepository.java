package com.mfa.report.repository;

import com.mfa.report.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ServiceRepository extends JpaRepository<Service,String> {
        List<Service> findAllByDirectionId(String directionId);
}
