package com.mfa.report.endpoint.rest.model.RestEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInformation {
    private String id;
    private String grade;
    private String lastname;
    private String firstname;
}
