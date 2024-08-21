package com.mfa.report.repository;

import com.mfa.report.repository.model.NextTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NextTaskRepository extends JpaRepository<NextTask,String> {
}
