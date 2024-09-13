package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.endpoint.rest.model.DTO.MissionNameDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.MissionWithDirectionName;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MissionMapper {
  private final DirectionMapper directionMapper;
  private final ActivityMapper activityMapper;

  public MissionDTO toDomain(Mission mission) {
    return MissionDTO.builder()
        .id(mission.getId())
        .name(mission.getDescription())
        .direction(directionMapper.toDomain(mission.getDirection()))
        .activityList(
            mission.getActivity().stream()
                .map(activityMapper::toDomain)
                .collect(Collectors.toList()))
        .build();
  }

  public com.mfa.report.endpoint.rest.model.RestEntity.Mission toDomainList(Mission mission) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Mission.builder()
        .id(mission.getId())
        .description(mission.getDescription())
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

  public Mission toRest(MissionDTO mission, Direction direction) {
    return Mission.builder().description(mission.getName()).direction(direction).build();
  }
}
