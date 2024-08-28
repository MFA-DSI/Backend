package com.mfa.report.endpoint.rest.mapper;


import com.mfa.report.endpoint.rest.model.DTO.DirectionDTO;
import com.mfa.report.endpoint.rest.model.DTO.DirectionNameDTO;
import com.mfa.report.model.Direction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DirectionMapper {
    private final UserMapper userMapper;


    public DirectionDTO toDomain(Direction direction){
        return DirectionDTO.builder()
                .id(direction.getId())
                .name(direction.getName())
                .responsible(direction.getResponsible().stream().map(userMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public Direction toRest(DirectionDTO directionDTO){
        return Direction.builder()
                .id(directionDTO.getId())
                .name(directionDTO.getName())
                .responsible(directionDTO.getResponsible().stream().map(userDTO -> userMapper.toRest(directionDTO.getResponsible().get(0))).collect(Collectors.toList()))
                .build();
    }

    public DirectionNameDTO toSignDomain (Direction direction){
        return DirectionNameDTO.builder()
                .id(direction.getId())
                .name(direction.getName())
                .build();
    }
}
