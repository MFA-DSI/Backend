package com.mfa.report.endpoint.rest.controller.auth;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.service.AuthService;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users/")
@CrossOrigin(origins = "*")
public class AuthController {

  @Autowired private final AuthService authService;
  private final DirectionService directionService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> signIn(@RequestBody Auth auth) {
    AuthResponse response = authService.signIn(auth);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/createUser")
  public ResponseEntity<AuthResponse> signUp(@RequestBody SignUp sign) {
    directionService.getDirectionById(sign.getDirectionId());
    AuthResponse response = authService.signUp(sign);
    return ResponseEntity.status(201).body(response);
  }
}
