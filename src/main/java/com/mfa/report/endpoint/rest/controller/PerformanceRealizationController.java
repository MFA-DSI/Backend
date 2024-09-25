package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.PerfRealizationMapper;
import com.mfa.report.endpoint.rest.model.DTO.PerfRealizationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.PerformanceRealization;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.PerformanceRealizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction")
public class PerformanceRealizationController {
    private final PerformanceRealizationService service;
    private final ActivityService activityService;
    private final PerfRealizationMapper mapper;


    @PutMapping("/performanceRealization")
    public List<PerfRealizationDTO> crUpdatePerformanceRealization(@RequestBody List<PerfRealizationDTO> perfRealizationDTO,@RequestParam String activityId){
        Activity activity = activityService.getActivityById(activityId);
        List<PerformanceRealization> performanceRealization = service.crUpdatePerformances(perfRealizationDTO.stream().map(e-> mapper.toRestSave(e,activity)).toList());
        return performanceRealization.stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
    }


    @DeleteMapping("/performanceRealization/delete")
    public ResponseEntity<String> deletePerformanceRealization(@RequestParam(name = "performanceRealizationId") String id){
        service.deletePerformanceRealization(id);
        return ResponseEntity.ok("Performance Realization deleted successfully");
    }
}
