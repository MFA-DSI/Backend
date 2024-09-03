package com.mfa.report.service;

import com.mfa.report.endpoint.rest.mapper.RecommendationMapper;
import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.repository.RecommendationRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendationService {
  private final RecommendationRepository repository;
  private final ActivityService activityService;
  private final UserService userService;
  private final RecommendationMapper mapper;
  private final NotificationService notificationService;

  public Recommendation getRecommendationByActivity(String id) {
    return repository.findByActivityId(id);
  }

  public Recommendation crUpdateRecommendation(Recommendation recommendation) {
    return repository.save(recommendation);
  }

  @Transactional
  public Recommendation addRecommendation(String activityId, RecommendationDTO recommendationDTO) {

    Activity activity = activityService.getActivityById(activityId);

    List<User> responsibles = activity.getMission().getDirection().getResponsible();

    boolean isCommitterResponsible =
        responsibles.stream()
            .anyMatch(
                responsible -> responsible.getId().equals(recommendationDTO.getCommitterId()));

    Recommendation savedRecommendation;
    User committer = userService.getUserById(recommendationDTO.getCommitterId());

    if (isCommitterResponsible) {
      recommendationDTO.setValidate_status(true);
      savedRecommendation = repository.save(mapper.toRestSave(recommendationDTO,activity));
    } else {
      savedRecommendation = repository.save(mapper.toRestSave(recommendationDTO,activity));
      notificationService.sendNotificationToResponsible(responsibles, savedRecommendation);
    }

    return savedRecommendation;
  }
}
