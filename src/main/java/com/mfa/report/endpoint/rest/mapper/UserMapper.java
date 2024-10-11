package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.SignInUserDTO;
import com.mfa.report.endpoint.rest.model.DTO.UserDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.DirectionResponsible;
import com.mfa.report.endpoint.rest.model.RestEntity.Responsible;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Grade;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
  private final DirectionService directionService;

  public UserDTO toDomain(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .firstname(user.getId())
        .lastname(user.getLastname())
        .grade(user.getGrade().name())
        .function(user.getFunction())
        .mail(user.getEmail())
        .directionId(user.getDirection().getId())
        .build();
  }

  public User toRest(SignInUserDTO userDTO) {
    return User.builder()
        .id(userDTO.getId())
        .email(userDTO.getMail())
        .firstname(userDTO.getFirstname())
        .lastname(userDTO.getLastname())
        .password(userDTO.getPassword())
        .direction(directionService.getDirectionById(userDTO.getDirectionId()))
        .build();
  }

  public User toRest(UserDTO userDTO) {
    return User.builder()
        .id(userDTO.getId())
        .email(userDTO.getMail())
        .firstname(userDTO.getFirstname())
        .lastname(userDTO.getLastname())
        .grade(Grade.valueOf(userDTO.getGrade()))
        .function(userDTO.getFunction())
        .direction(directionService.getDirectionById(userDTO.getDirectionId()))
        .build();
  }

  public Responsible ToDomainResponsible(User user) {
    return Responsible.builder()
        .id(user.getId())
        .directionName(user.getDirection().getName())
        .userName(user.getFirstname())
        .grade(String.valueOf(user.getGrade()))
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.User toDomainUser(User user) {
    return com.mfa.report.endpoint.rest.model.RestEntity.User.builder()
        .id(user.getId())
        .mail(user.getEmail())
        .lastname(user.getLastname())
        .firstname(user.getFirstname())
        .grade(String.valueOf(user.getGrade()))
        .function(user.getFunction())
        .direction(user.getDirection().getName())
        .build();
  }

  public DirectionResponsible toDomainResponsible(User user) {
    return DirectionResponsible.builder()
        .id(user.getId())
        .firstName(user.getFirstname())
        .lastName(user.getLastname())
        .function(user.getFunction())
        .grade(String.valueOf(user.getGrade()))
        .build();
  }
}
