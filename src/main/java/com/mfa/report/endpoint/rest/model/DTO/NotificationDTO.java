package com.mfa.report.endpoint.rest.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDTO {
  private String id;
  private String description;
  private String activityId;
  private boolean viewStatus;
  private String status;
  private String userId;
  private LocalDateTime creationDatetime;
}
