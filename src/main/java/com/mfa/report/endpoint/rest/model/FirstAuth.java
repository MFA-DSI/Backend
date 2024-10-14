package com.mfa.report.endpoint.rest.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FirstAuth {
    private String oldPassword;
    private String newPassword;
}
