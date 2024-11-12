package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.UserMapper;
import com.mfa.report.endpoint.rest.model.DTO.UserDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.User;
import com.mfa.report.model.enumerated.Grade;
import com.mfa.report.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
  private final UserService service;
  private final UserMapper mapper;

  @PutMapping("/modify")
  public ResponseEntity<UserDTO> modifyUserInformation(@RequestParam String id, @RequestBody   UserDTO userInfoUpdate) {
    com.mfa.report.model.User user = service.getUserById(id);
    user.setFirstname(userInfoUpdate.getFirstname());
    user.setLastname(userInfoUpdate.getLastname());
    user.setGrade(Grade.valueOf(userInfoUpdate.getGrade()));
    user.setEmail(userInfoUpdate.getMail());
    user.setFunction(userInfoUpdate.getFonction());
    user.setPhoneNumbers(userInfoUpdate.getPhoneNumbers());
    service.crupdateUser(user);
    return ResponseEntity.ok(mapper.toDomain(user));
  }

  @GetMapping("/information")
  public ResponseEntity<User> getUserInformation(@RequestParam String id) {
    return ResponseEntity.ok(mapper.toDomainUser(service.getUserById(id)));
  }

}
