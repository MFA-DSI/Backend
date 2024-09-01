package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Recommendation;
import com.mfa.report.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RecommendationMapper {
  private final ActivityService activityService;

  public RecommendationDTO toDomain(Recommendation recommendation) {
    return RecommendationDTO.builder()
        .id(recommendation.getId())
        .description(recommendation.getDescription())
        .validate_status(recommendation.isApproved())
        .build();
  }

  public Recommendation toRest(RecommendationDTO recommendationDTO) {
    return Recommendation.builder()
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .description(recommendationDTO.getDescription())
        .approved(recommendationDTO.isValidate_status())
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .build();
  }

  public Recommendation toRestSave(RecommendationDTO recommendationDTO, Activity activity) {
    return Recommendation.builder()
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .description(recommendationDTO.getDescription())
        .approved(recommendationDTO.isValidate_status())
        .creationDatetime(recommendationDTO.getCommitDatetime())
        .activity(activity)
        .build();
  }
}
