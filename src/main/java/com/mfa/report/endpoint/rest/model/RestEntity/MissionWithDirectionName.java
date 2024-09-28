package com.mfa.report.endpoint.rest.model.RestEntity;

import com.mfa.report.endpoint.rest.model.DTO.ActivityDTO;
import com.mfa.report.endpoint.rest.model.DTO.DirectionNameDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MissionWithDirectionName {
    String id;
    String description;
    DirectionNameDTO direction;
    Service service;
    List<ActivityDTO> activityList;
}
