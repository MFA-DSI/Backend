package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Notification;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("REQUEST_REPORT_CONFIRM")
@AllArgsConstructor
public class RequestConfirmationNotification extends Notification {

    @ManyToOne(cascade = CascadeType.ALL)
    private ReportRequest reportRequest;

    @Column(name = "confirmation_message")
    private String confirmationMessage;

    public RequestConfirmationNotification() {}

    public RequestConfirmationNotification(ReportRequest reportRequest, User recipient, String confirmationMessage) {
    super.setDescription("[DEMANDE DE RAPPORT] - Votre demande sur les activités hebdomadaires de semaine du "+reportRequest.getStartedAt()+" a été bien envoyée au "+ reportRequest.getTargetDirection().getAcronym());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.report_confirmation);
        super.setResponsibleDirection("Responsable: "+reportRequest.getResponsible().getGrade()+" "+reportRequest.getResponsible().getLastname()+" "+reportRequest.getResponsible().getLastname()+" "+reportRequest.getResponsible().getDirection().getAcronym());
        this.reportRequest = reportRequest;
        this.confirmationMessage = confirmationMessage;
    }

    // Getter et Setter
    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    public void setReportRequest(ReportRequest reportRequest) {
        this.reportRequest = reportRequest;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }


}
