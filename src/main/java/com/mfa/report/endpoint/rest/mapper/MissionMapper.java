package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.endpoint.rest.model.DTO.MissionNameDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.MissionWithDirectionName;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.Service;
import com.mfa.report.service.UserService;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MissionMapper {
  private final DirectionMapper directionMapper;
  private final ActivityMapper activityMapper;
  private final ServiceMapper serviceMapper;
  private final UserService userService;

  public MissionDTO toDomain(Mission mission) {
    return MissionDTO.builder()
        .id(mission.getId())
        .name(mission.getDescription())
        .direction(directionMapper.toDomain(mission.getDirection()))
        .serviceId(mission.getService().getId())
        .activityList(
            mission.getActivity().stream()
                .map(activityMapper::toDomain)
                .collect(Collectors.toList()))
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.Mission toDomainWithService(
      Mission mission) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Mission.builder()
        .id(mission.getId())
        .description(mission.getDescription())
        .service(serviceMapper.toDomain(mission.getService()))
        .activityList(
            mission.getActivity().stream()
                .map(activityMapper::toDomainList)
                .collect(Collectors.toList()))
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.Mission toDomainList(Mission mission) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Mission.builder()
        .id(mission.getId())
        .description(mission.getDescription())
        .directionName(mission.getDirection().getAcronym())
        .service(serviceMapper.toRest(mission.getService()))
        .activityList(
            mission.getActivity().stream()
                .map(activityMapper::toDomainList)
                .collect(Collectors.toUnmodifiableList()))
        .build();
  }

  public MissionWithDirectionName toDomainDirection(Mission mission) {
    return MissionWithDirectionName.builder()
        .id(mission.getId())
        .description(mission.getDescription())
        .service(serviceMapper.toRest(mission.getService()))
        .direction(directionMapper.toSignDomain(mission.getDirection()))
        .activityList(
            mission.getActivity().stream()
                .map(activityMapper::toDomain)
                .collect(Collectors.toList()))
        .build();
  }

  public MissionNameDTO toDomainName(Mission mission) {
    return MissionNameDTO.builder().description(mission.getDescription()).build();
  }

  public Mission toRest(MissionDTO mission, Direction direction, Service service) {
    return Mission.builder()
        .id(mission.getId())
        .description(mission.getName())
        .postedBy(userService.getUserById(mission.getPostedBy()))
        .direction(direction)
        .creationDatetime(LocalDateTime.now())
        .service(service)
        .build();
  }
}
