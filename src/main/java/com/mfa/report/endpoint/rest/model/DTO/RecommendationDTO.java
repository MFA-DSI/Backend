package com.mfa.report.endpoint.rest.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecommendationDTO {
    private String id;
    private String description;
    private boolean validate_status;
    private String committerId;
    private String activityId;
    private LocalDate commitDatetime;
}
