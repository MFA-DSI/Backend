package com.mfa.report.model;

import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("TASK_NOTIFICATION")
public class NextTaskNotification extends Notification {
    @ManyToOne
    private NextTask task;

    public NextTaskNotification(NextTask task, User recipient) {
        super.setDescription("Nouvelle tâche à venir: " + task.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.task);
        this.task = task;
    }

    public String getTaskId() {
        return task.getId();
    }

    public String getTaskDescription() {
        return task.getDescription();
    }
}
