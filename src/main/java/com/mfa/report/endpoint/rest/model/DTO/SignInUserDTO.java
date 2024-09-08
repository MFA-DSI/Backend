package com.mfa.report.endpoint.rest.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignInUserDTO {
  private String id;
  private String lastname;
  private String firstname;
  private String password;
  private String mail;
  private String directionId;
}
