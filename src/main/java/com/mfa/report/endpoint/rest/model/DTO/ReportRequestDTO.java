package com.mfa.report.endpoint.rest.model.DTO;


import com.mfa.report.endpoint.rest.model.RestEntity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRequestDTO {
    private String id;
    private String description;
    private String responsibleId;
    private DirectionNameDTO requesterDirection;
    private DirectionNameDTO targetDirection;
    private LocalDate createdAt;
    private String status;
    private List<Activity> activityList;
}
