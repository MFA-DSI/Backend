package com.mfa.report.endpoint.rest.model;

import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.endpoint.rest.model.DTO.DirectionDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MissionDTO {
    private String id;
    private String name;
    private DirectionDTO directionDTO;
    private List<ActivityDTO> activityDTOList;
}
