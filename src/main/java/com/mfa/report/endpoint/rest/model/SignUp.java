package com.mfa.report.endpoint.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUp {
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private String directionId;
}
