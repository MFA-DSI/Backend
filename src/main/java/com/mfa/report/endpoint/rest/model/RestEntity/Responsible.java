package com.mfa.report.endpoint.rest.model.RestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Responsible {
    private String id;
    private String userName;
    private String grade;
    private String directionName;

}
