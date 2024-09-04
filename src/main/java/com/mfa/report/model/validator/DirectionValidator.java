package com.mfa.report.model.validator;

import com.mfa.report.model.Direction;
import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.exception.ForbiddenException;
import java.util.HashSet;
import java.util.List;
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

  public void accept(String id){
    if(!repository.findById(id).isPresent()){
      throw new NotFoundException("direction with id "+id+" not found");
    }
  }
  public void acceptUser(Direction direction, String userId) {
    boolean isResponsible =
        direction.getResponsible().stream().anyMatch(user -> user.getId().equals(userId));
    if (!isResponsible) {
      throw new ForbiddenException(
          "The responsible of one direction cannot post mission to other direction");
    }

  }
}
