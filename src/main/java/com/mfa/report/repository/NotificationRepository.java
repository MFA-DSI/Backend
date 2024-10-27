package com.mfa.report.repository;

import com.mfa.report.model.Mission;
import com.mfa.report.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
  Page<Notification> findAllByUserId(String id, Pageable pageable);


  @Modifying
  @Transactional
  @Query("DELETE FROM Notification n WHERE n.mission = :mission")
  void deleteAllNotificationFromMission_Id(Mission mission);
}
