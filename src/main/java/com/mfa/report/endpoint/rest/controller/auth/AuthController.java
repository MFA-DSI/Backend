package com.mfa.report.endpoint.rest.controller.auth;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.endpoint.rest.model.CreateUserRequest;
import com.mfa.report.service.AuthService;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/postAdmin")
  public ResponseEntity<AuthResponse> createAdmin(@RequestAttribute("role") String role,@RequestParam String directionId, @RequestParam String userId, @RequestBody  CreateUserRequest createUserRequest){
    return ResponseEntity.status(201).body( authService.createResponsible(directionId,userId,createUserRequest,role));
  }

  @PostMapping("/postResponsible")
  public ResponseEntity<AuthResponse> createResponsible(@RequestAttribute("role") String role,String directionId,String userId,@RequestBody  CreateUserRequest createUserRequest){
    return ResponseEntity.status(201).body( authService.createResponsible(directionId,userId,createUserRequest,role));
  }

}
