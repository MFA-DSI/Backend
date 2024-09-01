package com.mfa.report.repository;

import com.mfa.report.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
  Optional<Notification> findByUserId(String id);
}
