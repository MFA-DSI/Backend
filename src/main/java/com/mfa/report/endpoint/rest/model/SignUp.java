package com.mfa.report.endpoint.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUp {
  private String firstname;
  private String lastname;
  private String email;
  private String phoneNumbers;
  private String function;
  private String grade;
  private String password;
  private boolean staff;
  private String directionId;
}
