package com.mfa.report.service;

import com.mfa.report.endpoint.event.UserCreatedListener;
import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.endpoint.rest.model.RestEntity.NewUser;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Grade;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.model.event.MissionPostedEvent;
import com.mfa.report.model.event.UserCreatedEvent;
import com.mfa.report.model.validator.UserValidator;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.ForbiddenException;
import com.mfa.report.service.Auth.TokenService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

  @Autowired
  private ApplicationEventPublisher eventPublisher;


  public Object signIn(Auth toAuth) {
    String email = toAuth.getEmail();
    User user = userService.getUserByUserMail(email);

    if (user == null) {
      user = userService.getUserByPhoneNumbers(email);
    }

    if (user == null) {
      throw new BadRequestException("Invalid credentials");
    }

    if (!user.getApproved()) {
      throw new BadRequestException("You must be approved by your admin");
    }
    boolean isPasswordMatch = passwordEncoder.matches(toAuth.getPassword(), user.getPassword());
    if (!isPasswordMatch) {
      throw new BadRequestException("Invalid credentials");
    }

    if (user.isFirstLogin()) {
      return Map.of(
          "message",
          "You must change your password upon first login",
          "userId",
          user.getId(),
          "name",
          user.getGrade() + " " + user.getFirstname() + " " + user.getLastname());
    }

    return AuthResponse.builder()
        .token(jwtService.generateToken(user.getRole(), String.valueOf(user.isStaff())))
        .userId(user.getId())
        .directionId(user.getDirection().getId())
        .build();
  }

  public NewUser approveUser(String toApproveUserId, String userId) {
    User user = userService.getUserById(userId);
    if (!user.getRole().equals(Role.ADMIN) && !user.getRole().equals(Role.SUPER_ADMIN)) {
      throw new ForbiddenException("Your not authorized to approve user");
    }
    User userToApprove = userService.getUserById(toApproveUserId);
    String tempPassword = generateTemporaryPassword();
    userToApprove.setPassword(passwordEncoder.encode(tempPassword));
    userToApprove.setApproved(true);
    userService.crupdateUser(userToApprove);

    return NewUser.builder()
        .id(userToApprove.getId())
        .identity((getValidIdentity(userToApprove.getEmail(), userToApprove.getPhoneNumbers())))
        .name(
            userToApprove.getGrade()
                + "_"
                + userToApprove.getLastname()
                + "_"
                + userToApprove.getFirstname())
        .password(tempPassword)
        .directionName(userToApprove.getDirection().getName())
        .build();
  }

  public AuthResponse updateUserPassword(String userId, String password, String newPassword) {
    User user = userService.getUserById(userId);

    if (!user.isFirstLogin()) {
      throw new BadRequestException("User already approved");
    }

    boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
    if (!isPasswordMatch) {
      throw new BadRequestException("Current password is incorrect.");
    }

    if (passwordEncoder.matches(newPassword, user.getPassword())) {
      throw new BadRequestException("New password cannot be the same as the old password.");
    }

    String hashedNewPassword = passwordEncoder.encode(newPassword);
    user.setFirstLogin(false);
    user.setPassword(hashedNewPassword);

    userService.crupdateUser(user);

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
    if (phoneNumbers != null || email != null) {
      if (phoneNumbers != null) {
        userValidator.validatePhoneNumbers(phoneNumbers);
        User existingUser = userService.getUserByPhoneNumbers(phoneNumbers);
        if (existingUser != null) {
          throw new DuplicateKeyException(
              "Un utilisateur avec le numéro téléphone: " + phoneNumbers + " éxiste déjà.");
        }
      }

      if (email != null) {
        User existingUser = userService.getUserByUserMail(email);
        if (existingUser != null) {
          throw new DuplicateKeyException(
              "Un utilisateur avec l'adresse email : " + email + " éxiste déjà.");
        }
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
                        .password(null)
                        .approved(false)
                        .firstLogin(true)
                        .build()))
            .get(0);

    directionService.saveNewUserToResponsible(toSignUp.getDirectionId(), createdUser);
    eventPublisher.publishEvent(new UserCreatedEvent(createdUser, createdUser.getDirection()));
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
                        .role(Role.ADMIN)
                        .staff(toSignUp.isStaff())
                        .grade(Grade.valueOf(toSignUp.getGrade()))
                        .function(toSignUp.getFunction())
                        .password(passwordEncoder.encode(tempPassword))
                        .approved(true)
                        .firstLogin(true)
                        .staff(toSignUp.isStaff())
                        .build()))
            .get(0);

    directionService.saveNewUserToResponsible(toSignUp.getDirectionId(), createdUser);

    return NewUser.builder()
        .id(createdUser.getId())
        .identity((getValidIdentity(createdUser.getEmail(), createdUser.getPhoneNumbers())))
        .password(tempPassword)
        .directionName(createdUser.getDirection().getName())
        .name(
            createdUser.getGrade()
                + "_"
                + createdUser.getLastname()
                + "_"
                + createdUser.getFirstname())
        .build();
  }

  private String generateTemporaryPassword() {
    return RandomStringUtils.randomAlphanumeric(15);
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
