package com.mfa.report.endpoint.rest.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PerfRealizationDTO {
  private String id;
  private Double indicators;
  private String realization;
  private String activityId;
  private String realizationType;
}
