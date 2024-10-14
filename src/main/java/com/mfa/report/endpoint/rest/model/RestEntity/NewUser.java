package com.mfa.report.endpoint.rest.model.RestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewUser {
    private String id;
    private String identity;
    private String password;
}
