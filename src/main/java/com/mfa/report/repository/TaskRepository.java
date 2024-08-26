package com.mfa.report.repository;

import com.mfa.report.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {
    Task findByActivityId(String Id);
}
