package com.mfa.report.repository;

import com.mfa.report.model.NextTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface NextTaskRepository extends JpaRepository<NextTask,String> {
    NextTask findByActivityId(String Id);
}
