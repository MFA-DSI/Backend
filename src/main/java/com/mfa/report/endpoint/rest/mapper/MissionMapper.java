package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
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

  public Mission toRest(MissionDTO mission, Direction direction) {
    return Mission.builder().description(mission.getName()).direction(direction).build();
  }
}
