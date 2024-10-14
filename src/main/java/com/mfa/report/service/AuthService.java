package com.mfa.report.service;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.endpoint.rest.model.RestEntity.NewUser;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Grade;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.model.validator.UserValidator;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.service.Auth.TokenService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final UserService userService;
  private final UserDetailsServiceImpl userDetailsServiceImpl;
  private final TokenService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final String AUTHORIZATION_HEADER = "Authorization";
  private final int BEARER_PREFIX_COUNT = 7;
  private final DirectionService directionService;
  private final UserValidator userValidator;

  public AuthResponse signIn(Auth toAuth) {
    String email = toAuth.getEmail();
    User user = userService.getUserByUserMail(email);

    if (user == null) {
      throw new BadRequestException("Invalid credentials");
    }

    boolean isPasswordMatch = passwordEncoder.matches(toAuth.getPassword(), user.getPassword());

    if (!isPasswordMatch) {
      throw new BadRequestException("Invalid credentials");
    }
    return AuthResponse.builder()
        .token(jwtService.generateToken(user.getRole(), user.getId()))
        .userId(user.getId())
        .directionId(user.getDirection().getId())
        .build();
  }

  public NewUser signUp(SignUp toSignUp) {
    String email = toSignUp.getEmail();
    String phoneNumbers = toSignUp.getPhoneNumbers();

    if ((email == null || !userValidator.isValidEmail(email))
        && (phoneNumbers == null || phoneNumbers.isEmpty())) {
      throw new IllegalArgumentException(
          "Either a valid email or at least one valid phone number must be provided.");
    }
    if (phoneNumbers != null) {
      userValidator.validatePhoneNumbers(phoneNumbers);
    }
    if (email != null) {
      User existingUser = userService.getUserByUserMail(email);
      if (existingUser != null) {
        throw new DuplicateKeyException(
            "User with the email address: " + email + " already exists.");
      }
    }


    String tempPassword = generateTemporaryPassword();
    User createdUser =
        userService
            .crupdateUser(
                List.of(
                    User.builder()
                        .email(email)
                        .phoneNumbers(phoneNumbers)
                        .firstname(toSignUp.getFirstname())
                        .lastname(toSignUp.getLastname())
                        .direction(directionService.getDirectionById(toSignUp.getDirectionId()))
                        .role(Role.user)
                        .grade(Grade.valueOf(toSignUp.getGrade()))
                        .function(toSignUp.getFunction())
                        .password(passwordEncoder.encode(tempPassword))
                        .approved(false)
                        .build()))
            .get(0);

    directionService.saveNewUserToResponsible(toSignUp.getDirectionId(), createdUser);

    return NewUser.builder()
        .id(createdUser.getId())
        .identity((getValidIdentity(createdUser.getEmail(), createdUser.getPhoneNumbers())))
        .password(tempPassword)
        .build();
  }

  public NewUser signUpAdmin(SignUp toSignUp) {
    String email = toSignUp.getEmail();
    String phoneNumbers = toSignUp.getPhoneNumbers();

    if ((email == null || !userValidator.isValidEmail(email))
        && (phoneNumbers == null || phoneNumbers.isEmpty())) {
      throw new IllegalArgumentException(
          "Either a valid email or at least one valid phone number must be provided.");
    }


    if(phoneNumbers!= null){
      userValidator.validatePhoneNumbers(phoneNumbers);
    }


    if (email != null) {
      User existingUser = userService.getUserByUserMail(email);
      if (existingUser != null) {
        throw new DuplicateKeyException(
            "User with the email address: " + email + " already exists.");
      }
    }
    String tempPassword = generateTemporaryPassword();
    User createdUser =
        userService
            .crupdateUser(
                List.of(
                    User.builder()
                        .email(email)
                        .phoneNumbers(phoneNumbers)
                        .firstname(toSignUp.getFirstname())
                        .lastname(toSignUp.getLastname())
                        .direction(directionService.getDirectionById(toSignUp.getDirectionId()))
                        .role(Role.ADMIN)
                        .grade(Grade.valueOf(toSignUp.getGrade()))
                        .function(toSignUp.getFunction())
                        .password(passwordEncoder.encode(tempPassword))
                        .approved(false)
                        .build()))
            .get(0);

    directionService.saveNewUserToResponsible(toSignUp.getDirectionId(), createdUser);

    return NewUser.builder()
            .id(createdUser.getId())
            .identity((getValidIdentity(createdUser.getEmail(), createdUser.getPhoneNumbers())))
            .password(tempPassword)
            .build();
  }

  private String generateTemporaryPassword() {
    return  RandomStringUtils.randomAlphanumeric(15);
  }

  private String getValidIdentity(String email, String phoneNumber) {
    if (email != null && !email.isEmpty()) {
      return email;
    } else if (phoneNumber != null && !phoneNumber.isEmpty()) {
      return phoneNumber;
    } else {
      throw new IllegalArgumentException("Both email and phone number are null or empty.");
    }
  }
}
