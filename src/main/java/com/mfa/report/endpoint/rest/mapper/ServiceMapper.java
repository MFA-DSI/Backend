package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.ServiceDTO;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.Service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServiceMapper {

  public com.mfa.report.endpoint.rest.model.RestEntity.Service toDomain(Service service) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Service.builder()
        .id(service.getId())
        .name(service.getName())
        .build();
  }


  public Service toRest(ServiceDTO serviceDTO,Direction direction){
    return  Service.builder()
            .id(serviceDTO.getId())
            .name(serviceDTO.getName())
            .direction(direction)
            .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.Service toRest(Service service){
    return  com.mfa.report.endpoint.rest.model.RestEntity.Service.builder()
            .id(service.getId())
            .name(service.getName())
            .build();
  }

  public Service toRestSave(ServiceDTO serviceDTO, Direction direction, List<Mission> mission){
    return Service.builder()
            .id(serviceDTO.getId())
            .name(serviceDTO.getName())
            .mission(mission)
            .direction(direction)
            .build();
  }
}