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
        .activityId(notification.getActivity().getId())
        .build();
  }

  public Notification toRest(NotificationDTO notificationDTO) {
    return Notification.builder()
        .id(notificationDTO.getId())
        .description(notificationDTO.getDescription())
        .viewStatus(notificationDTO.isViewStatus())
        .user(userService.getUserById(notificationDTO.getUserId()))
        .activity(activityService.getActivityById(notificationDTO.getActivityId()))
        .build();
  }
}
