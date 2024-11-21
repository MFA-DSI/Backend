package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Notification;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_REPORT_CREATED")
public class ReportRequestNotification extends Notification {

  @ManyToOne(cascade = CascadeType.ALL)
  private ReportRequest reportRequest;

  public ReportRequestNotification() {}

  public ReportRequestNotification(ReportRequest request, User recipient, boolean isRecall) {
    // Déterminer le préfixe du titre en fonction du contexte
    String prefix = isRecall ? "[RAPPEL]" : "[DEMANDE DE RAPPORT]";

    // Construire la description de la notification
    super.setDescription(
        prefix
            + " - "
            + request.getRequesterDirection().getAcronym()
            + " - "
            + request.getDescription());

    // Attribuer les autres propriétés à partir des informations du rapport et de l'utilisateur
    super.setUser(recipient);
    super.setCreationDatetime(LocalDateTime.now());
    super.setResponsibleDirection(
        "Responsable: "
            + request.getResponsible().getGrade()
            + " "
            + request.getResponsible().getLastname()
            + " "
            + request.getResponsible().getFirstname()
            + " - "
            + request.getResponsible().getDirection().getAcronym());

    // Type de notification (rapport demandé ou rappel)
    super.setNotificationType(
        isRecall ? NotificationStatus.report_recall : NotificationStatus.report_demand);

    this.reportRequest = request;
  }

  public String getRequestReportId(String id) {
    return reportRequest.getId();
  }
}
