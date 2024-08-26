package com.mfa.report.service;


import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.repository.PerformanceRealizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PerformanceRealizationService {
    private final PerformanceRealizationRepository performanceRealizationRepository;

    public PerformanceRealization getByDirectionId(String activityId){
        return performanceRealizationRepository.findByActivityId(activityId);
    }

    public PerformanceRealization crUpdatePerformance(PerformanceRealization performanceRealization){
        return performanceRealizationRepository.save(performanceRealization);
    }
}
