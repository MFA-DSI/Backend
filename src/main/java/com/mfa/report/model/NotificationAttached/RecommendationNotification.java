package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Notification;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("RECOMMENDATION_CREATED")
public class RecommendationNotification extends Notification {

    @ManyToOne(cascade = CascadeType.ALL)
    private Recommendation recommendation;

    public RecommendationNotification() {
    }


    public RecommendationNotification(Recommendation recommendation, User recipient) {
        super.setDescription("Nouvelle recommandation: " + recommendation.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setResponsibleDirection("Responsable: "+recommendation.getResponsible().getGrade()+" "+recommendation.getResponsible().getLastname()+" "+recommendation.getResponsible().getLastname()+" "+recommendation.getResponsible().getDirection().getAcronym());
        super.setNotificationType(NotificationStatus.recommendation);
        this.recommendation = recommendation;
    }

    public String getRecommendationId() {
        return recommendation.getId();
    }
}
