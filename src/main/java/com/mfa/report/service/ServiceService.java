package com.mfa.report.service;

import com.mfa.report.repository.ServiceRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServiceService {
  private final ServiceRepository repository;

  public List<com.mfa.report.model.Service> getAllServiceByDirectionId(String directionId) {
    return repository.findAllByDirectionId(directionId);
  }

  public com.mfa.report.model.Service saveNewService(com.mfa.report.model.Service service) {
    return repository.save(service);
  }

  public com.mfa.report.model.Service getServiceById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Service with id:" + id + " not found"));
  }

  public void removeService(String id) {
    com.mfa.report.model.Service service = getServiceById(id);
    repository.delete(service);
  }
}
