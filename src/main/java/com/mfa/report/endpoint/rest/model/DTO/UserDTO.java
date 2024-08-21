package com.mfa.report.endpoint.rest.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private String id;
    private String username;
    private String lastname;
    private String firstname;
    private String mail;
    private String directionId;
}
