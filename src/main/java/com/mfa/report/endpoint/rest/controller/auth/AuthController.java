package com.mfa.report.endpoint.rest.controller.auth;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.AuthResponse;
import com.mfa.report.endpoint.rest.model.FirstAuth;
import com.mfa.report.endpoint.rest.model.RestEntity.NewUser;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.service.AuthService;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users/")
@CrossOrigin(origins = "*")
public class AuthController {

  @Autowired private final AuthService authService;
  private final DirectionService directionService;

  @PostMapping("/login")
  public ResponseEntity<?> signIn(@RequestBody Auth auth) {
    Object response = authService.signIn(auth);

    if (response instanceof Map) {
      return ResponseEntity.status(200).body(response);
    }
    return ResponseEntity.status(200).body((AuthResponse) response);
  }

  @PutMapping("/first_login")
  public ResponseEntity<AuthResponse> firstSignIn(@RequestParam String userId,@RequestBody FirstAuth auth) {
    AuthResponse response = authService.updateUserPassword(userId,auth.getOldPassword(),auth.getNewPassword());

    return ResponseEntity.status(200).body( response);
  }




  @PutMapping("/user/approve")
  public ResponseEntity<String> approveUser(@RequestParam String toApproveId,@RequestParam String userId) {
    authService.approveUser(toApproveId,userId);
    return ResponseEntity.status(200).body("user with id "+toApproveId+" approved");
  }

  @PostMapping("/createUser")
  public ResponseEntity<NewUser> signUp(@RequestBody SignUp sign) {
    directionService.getDirectionById(sign.getDirectionId());
    NewUser response = authService.signUp(sign);
    return ResponseEntity.status(201).body(response);
  }

  @PostMapping("/createAdmin")
  public ResponseEntity<NewUser> signUpAdmin(@RequestBody SignUp sign) {
    directionService.getDirectionById(sign.getDirectionId());
    NewUser response = authService.signUpAdmin(sign);
    return ResponseEntity.status(201).body(response);
  }
}
