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
public class ActivityDTO {
    private String id;
    private String Name;
    private String observation;
    private LocalDate dueDatetime;
    private TaskDTO task ;
    private NextTaskDTO nextTask;
    private RecommendationDTO recommendation;
    private PerfRealizationDTO perfRealizationDTO;
}
