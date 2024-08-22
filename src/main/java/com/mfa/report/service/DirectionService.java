package com.mfa.report.service;

import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.repository.model.Direction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DirectionService {
    private final DirectionRepository repository;



    public Direction getDirectionById(String id){
        return repository.findById(id).orElseThrow();
    }
}
