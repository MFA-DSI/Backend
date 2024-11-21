package com.mfa.report.endpoint.rest.model.RestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Notification{
    private String id;
    private String description;
    private boolean viewStatus;
    private String status;
    private LocalDateTime creationDatetime;
    private String responsibleDirection;

}
