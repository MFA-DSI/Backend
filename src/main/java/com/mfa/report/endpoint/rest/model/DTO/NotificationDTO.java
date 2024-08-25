package com.mfa.report.endpoint.rest.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDTO {
    private String id;
    private String description;
    private String activityId;
    private boolean viewStatus;
    private String userId;
}
