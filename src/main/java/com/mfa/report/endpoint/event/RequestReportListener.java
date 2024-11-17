package com.mfa.report.endpoint.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.model.event.ReportRequestEvent;
import com.mfa.report.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Component
public class RequestReportListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener(condition = "#event.type == 'CREATED'")
    public void onRequestReportCreated(ReportRequestEvent event) {
        ReportRequest reportRequest = event.getReportRequest();
        Direction targetDirection = reportRequest.getTargetDirection();

        // Récupère les utilisateurs ayant le rôle ADMIN ou SUPER_ADMIN dans la direction cible
        List<User> adminUsers = targetDirection.getResponsible().stream()
                .filter(user -> user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))
                .collect(Collectors.toList());

        // Envoie une notification à chaque administrateur de la direction cible

        adminUsers.forEach(admin -> {
            CompletableFuture.runAsync(()->{
                notificationService.createRequestReportNotification(admin, reportRequest);
            });
        });

        // Envoie une confirmation de création au demandeur
        User responsible = reportRequest.getResponsible();
        CompletableFuture.runAsync(()->{
            notificationService.createConfirmationNotification(responsible, reportRequest);
        });
        }


    @EventListener(condition = "#event.type == 'APPROVED'")
    public void onRequestReportApproved(ReportRequestEvent event) {
        ReportRequest reportRequest = event.getReportRequest();

        // Récupérer le demandeur pour notifier de l'approbation
        User requester = reportRequest.getResponsible();
        CompletableFuture.runAsync(() ->
                notificationService.createConfirmedReportNotification(requester, reportRequest)
        );
    }
}
