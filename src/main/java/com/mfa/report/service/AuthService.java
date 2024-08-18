package com.mfa.report.service;


import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.Principal;
import com.mfa.report.endpoint.rest.model.Role;
import com.mfa.report.endpoint.rest.model.SignUp;
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

    public String signIn(Auth toAuth){
        String email = toAuth.getEmail();
        UserDetails principal = userDetailsServiceImpl.loadUserByUsername(email);
        if(!passwordEncoder.matches(toAuth.getPassword(),principal.getPassword())){
            throw new UsernameNotFoundException("Wrong Password!");
        }
        return jwtService.generateToken(principal);
    }


    public String signUp(SignUp  toSignUp){
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
                                                .role(Role.USER)
                                                .password(hashedPassword)
                                                .build()
                                )
                        ).get(0);

        Principal principal = Principal.builder().user(createdUser).build();
        return  jwtService.generateToken(principal);
    }

    public User whoami(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String token = authHeader.substring(BEARER_PREFIX_COUNT);
        String email = jwtService.extractEmail(token);
        return userService.getUserByUserMail(email);
    }
}
