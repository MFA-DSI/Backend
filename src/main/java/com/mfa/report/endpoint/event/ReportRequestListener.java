package com.mfa.report.endpoint.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.model.event.ReportRequestEvent;
import com.mfa.report.service.NotificationService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReportRequestListener {

  @Autowired private NotificationService notificationService;

  @EventListener(condition = "#event.type == 'CREATED'")
  public void onReportRequestCreated(ReportRequestEvent event) {
    ReportRequest reportRequest = event.getReportRequest();
    Direction targetDirection = reportRequest.getTargetDirection();

    // Retrieves users with the ADMIN or SUPER_ADMIN role in the target direction
    List<User> adminUsers =
        targetDirection.getResponsible().stream()
            .filter(
                user ->
                    user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))
            .collect(Collectors.toList());

    // Sends a notification to each administrator in the target direction
    adminUsers.forEach(
        admin -> {
          notificationService.createRequestReportNotification(admin, reportRequest);
        });

    // Sends a creation confirmation to the requester
    User responsible = reportRequest.getResponsible();
    notificationService.createConfirmationNotification(responsible, reportRequest);
  }

  @EventListener(condition = "#event.type == 'APPROVED'")
  public void onRequestReportApproved(ReportRequestEvent event) {
    ReportRequest reportRequest = event.getReportRequest();

    // Récupérer le demandeur pour notifier de l'approbation
    User requester = reportRequest.getResponsible();

    notificationService.createConfirmedReportNotification(requester, reportRequest);
  }

  @EventListener(condition = "#event.type == 'RECALL'")
  public void onRecallRequestReportApproved(ReportRequestEvent event) {
    ReportRequest reportRequest = event.getReportRequest();
    Direction targetDirection = reportRequest.getTargetDirection();
    // Récupérer le demandeur pour notifier de l'approbation
    List<User> adminUsers =
        targetDirection.getResponsible().stream()
            .filter(
                user ->
                    user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))
            .collect(Collectors.toList());

    // Envoie une notification à chaque administrateur de la direction cible

    adminUsers.forEach(
        admin -> {
          notificationService.createRecallRequestReportNotification(admin, reportRequest);
        });
  }
}
