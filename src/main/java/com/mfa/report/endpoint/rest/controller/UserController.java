package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.UserMapper;
import com.mfa.report.endpoint.rest.model.DTO.UserDTO;
import com.mfa.report.endpoint.rest.model.UserToUpdate;
import com.mfa.report.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user/")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @PutMapping("/modify")
    public ResponseEntity<UserDTO> modifyRole(@RequestParam String id,@RequestParam String role){
        return ResponseEntity.ok(mapper.toDomain(service.ModifyUserRole(id,role)));
    }
    

}
