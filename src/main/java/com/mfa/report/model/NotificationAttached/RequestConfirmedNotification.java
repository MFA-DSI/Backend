package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Notification;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_REPORT_APPROVED")
@AllArgsConstructor
@NoArgsConstructor
public class RequestConfirmedNotification extends Notification {

  @ManyToOne(cascade = CascadeType.ALL)
  private ReportRequest reportRequest;

  public RequestConfirmedNotification(ReportRequest reportRequest, User recipient) {
    super.setDescription("Votre demande : " + reportRequest.getDescription() + " a été approuvée");
    super.setUser(recipient);
    super.setCreationDatetime(LocalDateTime.now());
    super.setNotificationType(NotificationStatus.report_accepted);
    this.reportRequest = reportRequest;
  }

  // Getters et Setters
  public ReportRequest getReportRequest() {
    return reportRequest;
  }

  public void setReportRequest(ReportRequest reportRequest) {
    this.reportRequest = reportRequest;
  }
}
