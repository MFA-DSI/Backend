package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ServiceMapper;
import com.mfa.report.endpoint.rest.model.DTO.ServiceDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.Service;
import com.mfa.report.model.Direction;

import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.ServiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*", allowedHeaders = "*", originPatterns = "*")
@Slf4j
public class ServiceController {
  private final ServiceService service;
  private final DirectionService directionService;
  private final ServiceMapper mapper;
  private final DirectionValidator directionValidator;

  @GetMapping("/service/all")
  public List<Service> getAllDirectionService(@RequestParam String directionId) {
    return service.getAllServiceByDirectionId(directionId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/service/update")
  public Service createService(
      @RequestParam String directionId,
      @RequestParam String userId,
      @RequestBody ServiceDTO serviceDTO) {
    Direction direction1 = directionService.getDirectionById(directionId);
    directionValidator.acceptUser(direction1, userId);
    Service service1;

    if (serviceDTO.getId() != null) {
      com.mfa.report.model.Service tempService = service.getServiceById(serviceDTO.getId());
      tempService.setName(serviceDTO.getName());
      service1 = mapper.toRest(tempService);
      service.saveNewService(tempService);
      return service1;
    }
    return mapper.toDomain(service.saveNewService(mapper.toRest(serviceDTO, direction1)));
  }

  @PostMapping("/directions/{directionId}/services")
  public ResponseEntity<Service> createService(
      @PathVariable String directionId, @RequestBody ServiceDTO serviceToSave) {
    Direction direction = directionService.getDirectionById(directionId);
    com.mfa.report.model.Service service1 = mapper.toRest(serviceToSave, direction);
    service1.setDirection(direction);
    com.mfa.report.model.Service savedService = service.saveNewService(service1);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDomain(savedService));
  }
}
