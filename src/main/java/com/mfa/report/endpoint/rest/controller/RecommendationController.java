package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.NotificationMapper;
import com.mfa.report.endpoint.rest.mapper.RecommendationMapper;
import com.mfa.report.endpoint.rest.model.Comment;
import com.mfa.report.endpoint.rest.model.DTO.RecommendationDTO;
import com.mfa.report.model.Activity;
import com.mfa.report.model.Mission;
import com.mfa.report.model.Notification;
import com.mfa.report.model.User;
import com.mfa.report.service.ActivityService;
import com.mfa.report.service.NotificationService;
import com.mfa.report.service.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", originPatterns = "*")
public class RecommendationController {
    private final RecommendationService service;
    private  final NotificationService notificationService;
    private final ActivityService activityService;

    private final NotificationMapper notificationMapper;
    private final RecommendationMapper mapper;

    @PostMapping("/activity/recommendation")
    public RecommendationDTO PostRecommendation (@RequestBody  RecommendationDTO recommendationDTO,@RequestParam String activityId){
           return mapper.toDomain(service.addRecommendation(activityId,recommendationDTO));
    }
}
