package com.mfa.report.endpoint.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateUserRequest {
  private String email;
  private String username;
  private String tempPassword;
  private String directionId;
}
