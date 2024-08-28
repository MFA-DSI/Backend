package com.mfa.report.endpoint.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthResponse {
  private String token;
  private String userId;
  private String directionId;
}
