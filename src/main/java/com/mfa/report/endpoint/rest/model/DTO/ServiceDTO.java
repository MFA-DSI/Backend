package com.mfa.report.endpoint.rest.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceDTO {
    private String id;
    private String name;
    private List<MissionDTO> mission;
    private DirectionDTO direction;
}
