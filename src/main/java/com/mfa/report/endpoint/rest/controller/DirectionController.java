package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.DirectionMapper;
import com.mfa.report.endpoint.rest.mapper.UserMapper;
import com.mfa.report.endpoint.rest.model.DTO.DirectionDTO;
import com.mfa.report.endpoint.rest.model.DTO.DirectionNameDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.DirectionResponsible;
import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
import com.mfa.report.service.DirectionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
public class DirectionController {

  private final DirectionService service;
  private final DirectionMapper mapper;
  private final UserMapper userMapper;

  @GetMapping("/{id}")
  public DirectionDTO getDirectionById(@PathVariable String id) {
    Direction direction = service.getDirectionById(id);
    return mapper.toDomain(direction);
  }

  @GetMapping("/name/{id}")
  public DirectionNameDTO getDirectionNameById(@PathVariable String id) {
    Direction direction = service.getDirectionById(id);
    return mapper.toSignDomain(direction);
  }

  @GetMapping("/all")
  public List<DirectionNameDTO> getAllDirection() {
    return service.getAllDirection().stream()
        .map(mapper::toSignDomain)
        .collect(Collectors.toList());
  }

  @PostMapping("/create")
  public ResponseEntity<Direction> addDirection(@RequestBody  DirectionDTO direction) {
    Direction savedDirection = service.save(mapper.toRest(direction));
    return ResponseEntity.status(HttpStatus.CREATED).body(savedDirection);
  }

  @PostMapping("/create/{parentId}/sub-direction")
  public ResponseEntity<Direction> addSubDirection(
          @PathVariable String parentId,
          @RequestBody DirectionDTO subDirection) {
    Direction parentDirection = service.getDirectionById(parentId);
    Direction subDirectionToSave = mapper.toRest(subDirection);
    subDirectionToSave.setParentDirection(parentDirection);
    Direction savedSubDirection = service.save(subDirectionToSave);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedSubDirection);
  }


  @GetMapping("/sub_direction")
  public List<DirectionNameDTO> getAllSubDirectionName(@RequestParam String directionId){
    return service.getSubDirectionByDirectionId(directionId).stream().map(mapper::toSignDomain).collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/edit")
  public ResponseEntity<DirectionNameDTO> crUpdateDirection(
      @RequestBody DirectionDTO directionDTO,
      @RequestParam(name = "directionId") String directionId) {
    Direction direction = service.getDirectionById(directionId);
    direction.setName(directionDTO.getName());
    service.save(direction);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toSignDomain(direction));
  }
  
  @GetMapping("/responsible")
  public ResponseEntity<List<DirectionResponsible>> getAllDirectionResponsible(@RequestParam String directionId){
      List<User> responsibles = service.getDirectionById(directionId).getResponsible();
      return ResponseEntity.ok(responsibles.stream().map(userMapper::toDomainResponsible).collect(Collectors.toUnmodifiableList()));
  }
}
