package com.mfa.report.service;


import com.mfa.report.endpoint.rest.model.*;
import com.mfa.report.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private  final UserService userService;
    private  final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final  String AUTHORIZATION_HEADER = "Authorization";
    private  final int BEARER_PREFIX_COUNT = 7;
    private final DirectionService directionService;

    public AuthResponse signIn(Auth toAuth){
        String email = toAuth.getEmail();
        UserDetails principal = userDetailsServiceImpl.loadUserByUsername(email);
        User user = userService.getUserByUserMail(email);
        if(!passwordEncoder.matches(toAuth.getPassword(),principal.getPassword())){
            throw new UsernameNotFoundException("Wrong Password!");
        }

        return AuthResponse.builder()
                .token(jwtService.generateToken(principal, user.getId()))
                .userId(user.getId())
                .directionId(user.getDirection().getId())
                .build();



    }


    public AuthResponse signUp(SignUp  toSignUp){
        String email = toSignUp.getEmail();
        User existingUser = userService.getUserByUserMail(email);
        if(Objects.nonNull(existingUser)){
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
                                                .build()
                                )
                        ).get(0);
        directionService.saveNewUserToResponsible(toSignUp.getDirectionId(),createdUser);
        Principal principal = Principal.builder().user(createdUser).build();

        return  AuthResponse.builder()
                .token(jwtService.generateToken(principal, principal.getUser().getId()))
                .userId(createdUser.getId())
                .directionId(toSignUp.getDirectionId())
                .build();


    }

    public User whoami(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String token = authHeader.substring(BEARER_PREFIX_COUNT);
        String email = jwtService.extractEmail(token);
        return userService.getUserByUserMail(email);
    }
}
