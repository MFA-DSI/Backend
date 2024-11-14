package com.mfa.report.endpoint.rest.model.RestEntity;

import com.mfa.report.endpoint.rest.model.DTO.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Mission {
    String id;
    String directionName;
    String description;
    Service service;
    String postedBy;
    List<Activity> activityList;
}
