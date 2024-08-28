package com.mfa.report.service;

import static org.springframework.data.domain.Sort.Direction.ASC;

import com.mfa.report.model.Mission;
import com.mfa.report.repository.MissionRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MissionService {
  private final MissionRepository repository;

  public Mission getMissionById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("mission with id." + id + " not found "));
  }

  public List<Mission> getMissionByDirectionId(String directionId, Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(ASC, "description"));
    return repository.findAllByDirectionId(directionId, pageable);
  }

  public Mission crUpdateMission(Mission mission) {
   Mission mission1 = repository.save(mission);
   mission1.getActivity().forEach(activity -> activity.setMission(mission1));

   return mission1;
  }
}
