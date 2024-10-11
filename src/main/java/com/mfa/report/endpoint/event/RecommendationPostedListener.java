package com.mfa.report.endpoint.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.model.event.RecommendationPostedEvent;
import com.mfa.report.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RecommendationPostedListener {

    @Autowired private NotificationService notificationService;

    @EventListener
    public void onRecommendationPosted(RecommendationPostedEvent event) {
        Recommendation recommendation = event.getRecommendation();
        Direction direction = event.getDirection();

        User poster = recommendation.getResponsible();

        List<User> responsibles = direction.getResponsible();

        responsibles.stream()
                .filter(responsible -> !responsible.equals(poster))
                .forEach(
                        responsible -> {
                            notificationService.notifyRecommendation(responsible, recommendation);
                            notificationService.createRecommendationNotification(responsible, recommendation);
                        });
    }

    @EventListener
    public void onRecommendationUpdated(RecommendationPostedEvent event) {
        Recommendation recommendation = event.getRecommendation();
        Direction direction = event.getDirection();

        User poster = recommendation.getResponsible();

        List<User> responsibles = direction.getResponsible();

        responsibles.stream()
                .filter(responsible -> !responsible.equals(poster))
                .forEach(
                        responsible -> {
                            notificationService.notifyRecommendation(responsible, recommendation);
                            notificationService.createRecommendationNotification(responsible, recommendation);
                        });
    }
}
