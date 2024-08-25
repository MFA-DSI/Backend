package com.mfa.report.service;

import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.model.Direction;
import com.mfa.report.repository.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectionService {
    private final DirectionRepository repository;

    public Direction getDirectionById(String id){
        return repository.findById(id).orElseThrow(()->new NotFoundException("Direction with id:"+id+" not found"));
    }

    public Direction save (Direction direction){
        return repository.save(direction);
    }

    public List<Direction> getAllDirection(){
        return repository.findAll();
    }
}
