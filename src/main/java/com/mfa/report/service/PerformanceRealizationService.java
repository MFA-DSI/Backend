package com.mfa.report.service;

import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.repository.PerformanceRealizationRepository;
import com.mfa.report.repository.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PerformanceRealizationService {
  private final PerformanceRealizationRepository performanceRealizationRepository;

  public PerformanceRealization getByDirectionId(String activityId) {
    return performanceRealizationRepository.findByActivityId(activityId);
  }

  public PerformanceRealization crUpdatePerformance(PerformanceRealization performanceRealization) {
    return performanceRealizationRepository.save(performanceRealization);
  }

  public List<PerformanceRealization> crUpdatePerformances(List<PerformanceRealization> performanceRealization) {
    return performanceRealizationRepository.saveAll(performanceRealization);
  }

  public void deletePerformanceRealization(String id ){
    PerformanceRealization performanceRealization =
        performanceRealizationRepository.findById(id).orElseThrow(() -> new NotFoundException("PerformanceRealization with Id "+id+" not found"));
      performanceRealizationRepository.delete(performanceRealization);
  }
}
