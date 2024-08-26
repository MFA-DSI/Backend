package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.DirectionMapper;
import com.mfa.report.endpoint.rest.model.DTO.DirectionDTO;

import com.mfa.report.model.Direction;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
public class DirectionController {
    private final DirectionService service;
    private final DirectionMapper mapper;


    @GetMapping("/direction/{id}")
    public DirectionDTO getDirectionById(@RequestParam  String id){
        Direction direction = service.getDirectionById(id);
        return mapper.toDomain(direction);
    }
    @GetMapping("/direction/all")
    public List<DirectionDTO> getAllDirection(){
        return service.getAllDirection().stream().map(mapper::toDomain).collect(Collectors.toList());
    }


    @PutMapping("/direction/edit")
    public ResponseEntity<Direction> crUpdateDirection(@RequestBody DirectionDTO directionDTO,@RequestParam (name = "directionId") String directionId){

        Direction direction = service.getDirectionById(directionId);
        if(direction != null){
            service.save(direction);
            return ResponseEntity.ok(direction);
        }else {
            Direction direction1 = new Direction();
            direction1.setName(directionDTO.getName());
            service.save(direction1);
            return ResponseEntity.status(HttpStatus.CREATED).body(direction1);
        }
    }
}
