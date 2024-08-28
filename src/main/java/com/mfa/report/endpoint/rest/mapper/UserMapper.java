package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.SignInUserDTO;
import com.mfa.report.endpoint.rest.model.DTO.UserDTO;
import com.mfa.report.model.User;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final DirectionService directionService;


    public UserDTO toDomain(User user){
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getId())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .mail(user.getEmail())
                .directionId(user.getDirection().getId())
                .build();
    }

    public User toRest(SignInUserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getMail())
                .username(userDTO.getUsername())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .password(userDTO.getPassword())
                .direction(directionService.getDirectionById(userDTO.getDirectionId()))
                .build();
    }

    public User toRest(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getMail())
                .username(userDTO.getUsername())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .direction(directionService.getDirectionById(userDTO.getDirectionId()))
                .build();
    }
}
