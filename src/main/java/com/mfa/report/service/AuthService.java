package com.mfa.report.service;

import com.mfa.report.endpoint.rest.model.*;
import com.mfa.report.model.User;
import com.mfa.report.model.validator.UserValidator;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.ForbiddenException;
import com.mfa.report.service.Auth.TokenService;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
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
    String password = toAuth.getPassword();

    User user = userService.getUserByUserMail(email);

    if (!matchPassword(user, password)) {
      throw new BadRequestException("Incorrect password");
    }
    if (user.isPasswordChangeRequired()) {
      throw new BadRequestException("Password change required. Please update your password.");
    }
    return AuthResponse.builder()
        .token(jwtService.generateToken(user.getRole(), user.getId()))
        .userId(user.getId())
        .directionId(user.getDirection().getId())
        .build();
  }

  public AuthResponse SignUpUser(SignUp signUp){
    User existingUser = userService.getUserByUserMail(signUp.getMail());
    if (existingUser == null) {
      throw new BadRequestException("User not found.");
    }

    if (existingUser.isPasswordChangeRequired()) {
      if (!passwordEncoder.matches(signUp.getPassword(), existingUser.getPassword())) {
        throw new BadRequestException("Temporary password is incorrect.");
      }


      String newPassword = passwordEncoder.encode(signUp.getNewPassword());
      existingUser.setPassword(newPassword);
      existingUser.setPasswordChangeRequired(false);

      User user = userService.crupdateUser(List.of(existingUser)).get(0);

      return AuthResponse.builder()
              .token(jwtService.generateToken(user.getRole(), user.getId()))
              .userId(user.getId())
              .directionId(user.getDirection().getId())
              .build();
    } else {
      throw new BadRequestException("Password change is not required.");
    }
  }

  private AuthResponse createUserWithRole(CreateUserRequest request, Role role) {
    String email = request.getEmail();
    User existingUser = userService.getUserByUserMail(email);
    if (Objects.nonNull(existingUser)) {
      throw new BadRequestException("User with the email address: " + email + " already exists.");
    }

    String tempPassword = request.getTempPassword();
    String hashedPassword = passwordEncoder.encode(tempPassword);

    User createdUser =
        userService
            .crupdateUser(
                List.of(
                    User.builder()
                        .username(request.getUsername())
                        .email(email)
                        .direction(directionService.getDirectionById(request.getDirectionId()))
                        .role(role)
                        .password(hashedPassword)
                        .passwordChangeRequired(true)
                        .build()))
            .get(0);

    return AuthResponse.builder()
        .token(jwtService.generateToken(createdUser.getRole(), createdUser.getId()))
        .userId(createdUser.getId())
        .directionId(request.getDirectionId())
        .build();
  }

  public AuthResponse createResponsible(
      String directionId, String userId, CreateUserRequest userRequest,String role) {
    User currentUser = getCurrentAuthenticatedUser(userId);
    if (!currentUser.getRole().equals(Role.ADMIN)
        || !currentUser.getRole().equals(Role.SUPER_ADMIN)) {
      throw new ForbiddenException("User cannot create responsible");
    }
    if(!Objects.equals(currentUser.getRole().toString(), role)){
      throw new BadRequestException("User role is not equal as registered");
    }

    if (!isAdminInSameDirection(currentUser, directionId)) {
      throw new ForbiddenException(
          "You do not have permission to create a Responsible in this direction.");
    }

    return createUserWithRole(userRequest, Role.user);
  }

  public AuthResponse createAdmin(
      String directionId, String userId, CreateUserRequest userRequest,String role) {

    User currentUser = getCurrentAuthenticatedUser(userId);

    if (!currentUser.getRole().equals(Role.SUPER_ADMIN)) {
      throw new ForbiddenException("Responsible cannot create user");
    }
    if(!currentUser.getRole().toString().equals(role)){
      throw new BadRequestException("User role is not equal as registered");
    }

    return createUserWithRole(userRequest, Role.ADMIN);
  }

  private User getCurrentAuthenticatedUser(String id) {
    return userService.getUserById(id);
  }

  private boolean isAdminInSameDirection(User currentUser, String directionId) {
    return currentUser != null
        && Role.ADMIN.equals(currentUser.getRole())
        && currentUser.getDirection().getId().equals(directionId);
  }

  public boolean matchPassword(User user, String password) {
    return passwordEncoder.matches(password, user.getPassword());
  }
}
