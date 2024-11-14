package com.mfa.report.service;

import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DirectionService {
  private final DirectionRepository repository;

  public Direction getDirectionById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Direction with id:" + id + " not found"));
  }

  public List<Direction> getSubDirectionByDirectionId(String id){
      return repository.findById(id).get().getSubDirections();
  }
  public Direction save(Direction direction) {
    return repository.save(direction);
  }

  public List<Direction> getAllDirection() {
    return repository.findAll();
  }

  public Direction saveNewUserToResponsible(String id, User user) {
    Direction direction = repository.getById(id);
    direction.getResponsible().add(user);
    return repository.save(direction);
  }
}
