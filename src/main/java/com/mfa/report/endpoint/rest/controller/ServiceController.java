package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ServiceMapper;
import com.mfa.report.endpoint.rest.model.DTO.ServiceDTO;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.ServiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@Slf4j
public class ServiceController {
    private final ServiceService service;
    private final DirectionService directionService;
    private  final ServiceMapper mapper;
    private final DirectionValidator directionValidator;



    @GetMapping("/service/all")
    public List<ServiceDTO> getAllDirectionService(@RequestParam String id){
        return service.getAllServiceByDirectionId(id).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
    }

    @PutMapping("/service/create")
    public ServiceDTO createService(@RequestParam String directionId,@RequestParam String userId,@RequestBody ServiceDTO serviceDTO){
        Direction direction1 = directionService.getDirectionById(directionId);
        directionValidator.acceptUser(direction1,userId);
       return mapper.toDomain(service.saveNewService(mapper.toRest(serviceDTO)));
    }
}
