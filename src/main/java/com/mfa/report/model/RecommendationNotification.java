package com.mfa.report.model;

import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("RECOMMENDATION_CREATED")
public class RecommendationNotification extends Notification {

    @ManyToOne
    private final Recommendation recommendation;

    public RecommendationNotification(Recommendation recommendation, User recipient) {
        super.setDescription("Nouvelle recommandation: " + recommendation.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.recommendation);
        this.recommendation = recommendation;
    }

    public String getRecommendationId() {
        return recommendation.getId();
    }
}