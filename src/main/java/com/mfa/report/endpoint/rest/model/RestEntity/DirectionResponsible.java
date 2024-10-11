package com.mfa.report.endpoint.rest.model.RestEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DirectionResponsible {
    private  String id;
    private String firstName;
    private String lastName;
    private String grade;
    private String function;
}
