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
public class PerfRealizationDTO {
    private String id;
    private Long performanceIndicators;
    private String realisation;
    private LocalDate due_datetime;
}
