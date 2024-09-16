package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.PerformanceRealization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PerfRealizationMapper {

  public PerfRealizationDTO toDomain(PerformanceRealization performanceRealization) {
    return PerfRealizationDTO.builder()
        .id(performanceRealization.getId())
        .realization(performanceRealization.getRealization())
        .indicators(performanceRealization.getKPI())
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.PerformanceRealization toDomainList(PerformanceRealization performanceRealization) {
    return com.mfa.report.endpoint.rest.model.RestEntity.PerformanceRealization.builder()
            .id(performanceRealization.getId())
            .realization(performanceRealization.getRealization())
            .indicators(performanceRealization.getKPI())
            .build();
  }

  public PerformanceRealization toRest(PerfRealizationDTO perfRealizationDTO) {
    return PerformanceRealization.builder()
        .realization(perfRealizationDTO.getRealization())
        .KPI(perfRealizationDTO.getIndicators())
        .build();
  }

  public PerformanceRealization toRestSave(PerfRealizationDTO performanceRealizationDTO, Activity activity){
    return PerformanceRealization.builder()
            .realization(performanceRealizationDTO.getRealization())
            .KPI(performanceRealizationDTO.getIndicators())
            .activity(activity)
            .build();
  }
}
