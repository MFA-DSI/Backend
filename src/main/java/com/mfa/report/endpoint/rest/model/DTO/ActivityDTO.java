package com.mfa.report.endpoint.rest.model.DTO;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ActivityDTO {
  private String id;
  private String description;
  private String observation;
  private String prediction;
  private LocalDate dueDatetime;
  private List<TaskDTO> task;
  private List<NextTaskDTO> nextTask;
  private List<RecommendationDTO> recommendation;
  private List<PerfRealizationDTO> perfRealizationDTO;
}
