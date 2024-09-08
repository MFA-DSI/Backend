package com.mfa.report.endpoint.rest.model.RestEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PerformanceRealization {
    private  String id;
    private Long indicators;
    private String realization;
}
