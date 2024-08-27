package com.mfa.report.endpoint.rest.model.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NextTaskDTO {
    private String id;
    private String description;
    private LocalDate dueDatetime;
    private String activityId;
}
