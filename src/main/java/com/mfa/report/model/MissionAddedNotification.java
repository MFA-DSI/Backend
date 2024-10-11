package com.mfa.report.model;

import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("MISSION_ADDED")
@AllArgsConstructor
public class MissionAddedNotification extends Notification {
    @ManyToOne
    private Mission mission;

    public MissionAddedNotification(){
    }

    public MissionAddedNotification(Mission mission, User recipient) {
        super.setDescription(mission.getPostedBy().getGrade()+" "+mission.getPostedBy().getLastname()+" "+mission.getPostedBy().getLastname()+" "+"a ajouté une mission: " + mission.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.mission);
        this.mission = mission;
    }

    public String getMissionId() {
        return mission.getId();
    }
}
