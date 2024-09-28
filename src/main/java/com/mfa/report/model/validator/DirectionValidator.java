package com.mfa.report.model.validator;

import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.Service;
import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.ForbiddenException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.mfa.report.repository.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DirectionValidator implements Consumer<Direction> {
  private final DirectionRepository repository;

  public void accept(List<Direction> directionList) {
    directionList.forEach(this::accept);
  }

  @Override
  public void accept(Direction direction) {
    Set<String> violationsMessage = new HashSet<>();
  }

  public void acceptUser(Direction direction, String userId) {
    boolean isResponsible =
        direction.getResponsible().stream().anyMatch(user -> user.getId().equals(userId));
    if (!isResponsible) {
      throw new ForbiddenException(
          "The responsible of one direction cannot post mission to other direction");
    }
  }


  public void isServiceInMission(Direction direction, Mission mission,String serviceId) {
    // Vérifier si la mission est liée à la direction donnée
    if (!direction.getMission().contains(mission)) {
     throw new NotFoundException("cette mission n'est pas rattaché à cette direction");
    }
    Service services = mission.getService();
    if(Objects.equals(services.getId(), serviceId)){
      throw new BadRequestException("this mission is not attached to the service");
    };
  }
}
