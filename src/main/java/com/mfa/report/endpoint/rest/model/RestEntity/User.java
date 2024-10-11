package com.mfa.report.endpoint.rest.model.RestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private String id;
    private String lastname;
    private String firstname;
    private String mail;
    private String function;
    private String grade;
    private String direction;
}
