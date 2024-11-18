package com.mfa.report.service;

import com.mfa.report.model.Activity;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.RequestReportStatus;
import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.ReportRequestRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RequestReportService {
  @Autowired private ReportRequestRepository repository;

  private ActivityService activityService;
  private UserService userService;

  @Autowired private NotificationService notificationService;

  @Autowired private DirectionRepository directionRepository;

  public ReportRequest createRequest(
      String requestingDirectionId,
      String subDirectionId,
      LocalDate weekStartDate,
      String responsibleId,
      int page,
      int pageSize) {
    // Créer une nouvelle demande de rapport personnalisé
    ReportRequest request = new ReportRequest();

    User user = userService.getUserById(responsibleId);
    String description = "Demande de rapport des activites hebdomadaires de la semaine du "+weekStartDate.toString();
    request.setDescription(description);
    request.setResponsible(user);
    // Récupérer et définir la direction demandeuse
    request.setRequesterDirection(
        directionRepository
            .findById(requestingDirectionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid requesting direction ID")));

    // Récupérer et définir la sous-direction cible
    request.setTargetDirection(
        directionRepository
            .findById(subDirectionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid sub-direction ID")));

    // Récupérer les activités via getActivitiesForWeek
    List<Activity> activities =
        activityService.getActivitiesForWeek(
            weekStartDate, subDirectionId, page, pageSize);
    if (activities.isEmpty()) {
      throw new BadRequestException("Aucune activité disponible durant la semaine du "+weekStartDate);
    }
    request.setActivities(activities);

    // Définir le statut initial, la date de création et la date d'expiration
    request.setStatus(RequestReportStatus.PENDING);
    request.setCreatedAt(LocalDate.now());
    request.setExpirationAt(LocalDate.now().plusDays(7));
    return repository.save(request);
  }

  public ReportRequest respondToRequest(String requestId, String targetDirectionId, String status, String comment) {
    // Recherche de la demande par ID
    ReportRequest request =
        repository.findById(requestId).orElseThrow(() -> new NotFoundException("Demand not found"));

    // Validation de la direction cible
    if (!request.getTargetDirection().getId().equals(targetDirectionId)) {
      throw new BadRequestException("Cette direction n'est pas autorisée à répondre à cette demande.");
    }

    // Validation et mise à jour du statut
    if ("APPROVED".equalsIgnoreCase(status)) {
      request.setStatus(RequestReportStatus.APPROVED);
      request.setComment(null);
    } else if ("REJECTED".equalsIgnoreCase(status)) {
      if (comment == null || comment.isBlank()) {
        throw new BadRequestException("Un commentaire est requis lors du rejet d'une demande.");
      }
      request.setStatus(RequestReportStatus.REJECTED);
      request.setComment(comment);
    } else {
      throw new BadRequestException(
          "Statut non valide fourni. Les valeurs autorisées sont « APPROUVÉ » ou « REJETÉ ».");
    }

    // Mise à jour de la demande dans la base
    return repository.save(request);
  }

}
