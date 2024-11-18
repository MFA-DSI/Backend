package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.ReportRequestDTO;
import com.mfa.report.model.ReportRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReportRequestMapper {
    private DirectionMapper directionMapper;
    private ActivityMapper activityMapper;

    public ReportRequestDTO toDomain(ReportRequest request){
        return ReportRequestDTO.builder()
                .id(request.getId())
                .description(request.getDescription())
                .responsibleId(request.getResponsible().getId())
                .requesterDirection(directionMapper.toSignDomain(request.getRequesterDirection()))
                .targetDirection(directionMapper.toSignDomain(request.getTargetDirection()))
                .createdAt(request.getCreatedAt())
                .status(String.valueOf(request.getStatus()))
                .activityList(request.getActivities().stream().map(activityMapper::toDomainList).collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
