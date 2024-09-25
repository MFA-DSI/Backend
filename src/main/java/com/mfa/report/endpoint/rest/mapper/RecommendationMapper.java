package com.mfa.report.endpoint.rest.mapper;

import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Recommendation;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.UserService;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RecommendationMapper {
  private final ActivityService activityService;
  private final UserService userService;
  private final UserMapper userMapper;

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

  public com.mfa.report.endpoint.rest.model.RestEntity.Recommendation toDomainResponse(
      Recommendation recommendation) {
    return com.mfa.report.endpoint.rest.model.RestEntity.Recommendation.builder()
        .id(recommendation.getId())
        .committer(userMapper.ToDomainResponsible(recommendation.getResponsible()))
        .commitDatetime(recommendation.getCreationDatetime())
        .description(recommendation.getDescription())
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
