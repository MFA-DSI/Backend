package com.mfa.report.endpoint.rest;

import com.mfa.report.endpoint.rest.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Credentials {
  private String email;
  private String password;
  private Role authority;
}
