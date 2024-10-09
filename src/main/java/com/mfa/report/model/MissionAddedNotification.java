package com.mfa.report.model;

import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("MISSION_ADDED")
public class MissionAddedNotification extends Notification {
    @ManyToOne
    private Mission mission;

    public MissionAddedNotification(Mission mission, User recipient) {
        super.setDescription("Nouvelle mission ajoutée: " + mission.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.mission);
        this.mission = mission;
    }

    public String getMissionId() {
        return mission.getId();
    }
}
