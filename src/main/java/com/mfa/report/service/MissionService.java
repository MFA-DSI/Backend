package com.mfa.report.service;


import com.mfa.report.repository.MissionRepository;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.model.Mission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MissionService{
    private final MissionRepository repository;

    public Mission getMissionById(String id){
       return repository.findById(id).orElseThrow(() -> new NotFoundException("mission with id." + id + " not found "));
    }

    public List<Mission> getMissionByDirectionId(String directionId){
        return repository.findAllByDirectionId(directionId);
    }

    public Mission crUpdateMission (Mission mission){
        return repository.save(mission);
    }

}