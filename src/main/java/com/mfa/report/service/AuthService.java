package com.mfa.report.service;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.model.User;
import com.mfa.report.service.Auth.TokenService;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
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

  public AuthResponse signIn(Auth toAuth) {
    String email = toAuth.getEmail();
    User user = userService.getUserByUserMail(email);

    return AuthResponse.builder()
        .token(jwtService.generateToken(user.getRole(),user.getId()))
        .userId(user.getId())
        .directionId(user.getDirection().getId())
        .build();
  }

  public AuthResponse signUp(SignUp toSignUp) {
    String email = toSignUp.getEmail();
    User existingUser = userService.getUserByUserMail(email);
    if (Objects.nonNull(existingUser)) {
      throw new DuplicateKeyException("User with the email address: " + email + " already exists.");
    }

    String hashedPassword = passwordEncoder.encode(toSignUp.getPassword());
    User createdUser =
        userService
            .crupdateUser(
                List.of(
                    User.builder()
                        .username(toSignUp.getUsername())
                        .email(toSignUp.getEmail())
                        .firstname(toSignUp.getFirstname())
                        .lastname(toSignUp.getLastname())
                        .direction(directionService.getDirectionById(toSignUp.getDirectionId()))
                        .role(Role.user)
                        .password(hashedPassword)
                        .build()))
            .get(0);
    directionService.saveNewUserToResponsible(toSignUp.getDirectionId(), createdUser);
    return AuthResponse.builder()
        .token(jwtService.generateToken(createdUser.getRole(),createdUser.getId()))
        .userId(createdUser.getId())
        .directionId(toSignUp.getDirectionId())
        .build();
  }

}
