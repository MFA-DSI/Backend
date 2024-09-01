package com.mfa.report.endpoint.rest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    public String accessToken;
}
