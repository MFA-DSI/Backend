package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PerfRealizationMapper {


    public PerfRealizationDTO toDomain (PerformanceRealization performanceRealization){
        return PerfRealizationDTO.builder()
                .id(performanceRealization.getId())
                .realization(performanceRealization.getRealization())
                .performanceIndicators(performanceRealization.getKPI())
                .build();
    }

    public PerformanceRealization toRest(PerfRealizationDTO perfRealizationDTO){
        return PerformanceRealization.builder()
                .realization(perfRealizationDTO.getRealization())
                .KPI(perfRealizationDTO.getPerformanceIndicators())
                .build();
    }
}
