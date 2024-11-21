package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Mission;
import com.mfa.report.model.Notification;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

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
        super.setResponsibleDirection("Responsable: "+mission.getPostedBy().getGrade()+" "+mission.getPostedBy().getLastname()+" "+mission.getPostedBy().getLastname()+" "+mission.getPostedBy().getDirection().getAcronym());
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.mission);
        this.mission = mission;
    }

    public String getMissionId() {
        return mission.getId();
    }
}
