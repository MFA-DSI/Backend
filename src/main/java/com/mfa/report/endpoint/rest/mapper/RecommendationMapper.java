package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Recommendation;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class RecommendationMapper {
  private final ActivityService activityService;
  private final UserService userService;

  public RecommendationDTO toDomain(Recommendation recommendation) {
    return RecommendationDTO.builder()
        .id(recommendation.getId())
        .description(recommendation.getDescription())
        .validate_status(recommendation.isApproved())
        .committerId(recommendation.getResponsible().getId())
        .commitDatetime(recommendation.getCreationDatetime())
        .activityId(recommendation.getActivity().getId())
        .build();
  }

  public Recommendation toRest(RecommendationDTO recommendationDTO) {
    return Recommendation.builder()
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .description(recommendationDTO.getDescription())
        .approved(recommendationDTO.isValidate_status())
        .responsible(userService.getUserById(recommendationDTO.getCommitterId()))
        .creationDatetime(LocalDate.now())
        .build();
  }

  public Recommendation toRestSave(RecommendationDTO recommendationDTO, Activity activity) {
    return Recommendation.builder()
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .description(recommendationDTO.getDescription())
        .approved(recommendationDTO.isValidate_status())
        .creationDatetime(LocalDate.now())
        .responsible(userService.getUserById(recommendationDTO.getCommitterId()))
        .activity(activity)
        .build();
  }
}
