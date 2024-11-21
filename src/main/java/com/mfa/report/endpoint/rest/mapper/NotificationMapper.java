package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.NotificationDTO;
import com.mfa.report.model.Notification;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationMapper {
  private final UserService userService;
  private final ActivityService activityService;

  public NotificationDTO toDomain(Notification notification) {
    return NotificationDTO.builder()
        .id(notification.getId())
        .viewStatus(notification.isViewStatus())
        .description(notification.getDescription())
        .userId(notification.getUser().getId())
        .responsibleDirection(notification.getResponsibleDirection())
        .status(String.valueOf(notification.getNotificationType()))
        .creationDatetime(notification.getCreationDatetime())
        .build();
  }
  ;

  public com.mfa.report.endpoint.rest.model.RestEntity.Notification toDomainView(
      Notification notification) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Notification.builder()
        .id(notification.getId())
        .viewStatus(notification.isViewStatus())
        .description(notification.getDescription())
        .status(String.valueOf(notification.getNotificationType()))
        .responsibleDirection(notification.getResponsibleDirection())
        .creationDatetime(notification.getCreationDatetime())
        .build();
  }
  ;
}
