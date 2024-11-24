package com.mfa.report.service;

import com.mfa.report.endpoint.rest.controller.utils.LocalDateUtils;
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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RequestReportService {
  @Autowired private ReportRequestRepository repository;

  private ActivityService activityService;
  private UserService userService;

  private LocalDateUtils localDateUtils;

  @Autowired private NotificationService notificationService;

  @Autowired private DirectionService directionRepository;


  public ReportRequest createRequest (
      String requestingDirectionId,
      String subDirectionId,
      LocalDate weekStartDate,
      String responsibleId,
      int page,
      int pageSize) {
    // Créer une nouvelle demande de rapport personnalisé
    ReportRequest request = new ReportRequest();
    ReportRequest reportRequest = getReportByDateAndTargetDirection(weekStartDate,subDirectionId);

    if(reportRequest != null){
        throw new  BadRequestException(reportRequest.getRequesterDirection().getAcronym()+": Une rapport de cette semaine a été déja demandée par"+reportRequest.getResponsible().getGrade()+" "+reportRequest.getResponsible().getLastname()+" "+reportRequest.getResponsible().getFirstname());
    }
    User user = userService.getUserById(responsibleId);

    request.setResponsible(user);
    request.setStartedAt(weekStartDate);
    // Récupérer et définir la direction demandeuse
    request.setRequesterDirection(
        directionRepository
            .getDirectionById(requestingDirectionId));


    // Récupérer et définir la sous-direction cible
    request.setTargetDirection(
        directionRepository
            .getDirectionById(subDirectionId));
    String description = request.getTargetDirection().getAcronym()+": Demande de rapport des activites hebdomadaires de la semaine du "+localDateUtils.formatDate(weekStartDate);
    request.setDescription(description);

    // Récupérer les activités via getActivitiesForWeek
    List<Activity> activities =
        activityService.getActivitiesForWeek(
            weekStartDate, subDirectionId, page, pageSize);
    if (activities.isEmpty()) {
      throw new BadRequestException(request.getTargetDirection().getAcronym()+": Aucune activité disponible durant la semaine du "+ localDateUtils.formatDate(weekStartDate)+" pour "+request.getTargetDirection().getAcronym());
    }
    for (Activity activity : activities) {
      activity.setReportRequest(request);
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

  request.setStatus(RequestReportStatus.valueOf(status));
    request.setComment(comment);

    // Mise à jour de la demande dans la base
    return repository.save(request);
  }

  public List<ReportRequest> getAllRequest(String directionId){
    return  repository.findAllByRequesterDirectionId(directionId);
  }

  public List<ReportRequest> getAllTargetedRequest(String directionId){
    return  repository.findAllByTargetDirectionId(directionId);
  }


  public ReportRequest getReportByDateAndTargetDirection(LocalDate date,String directionId){
      return repository.findByStartedAtAndTargetDirectionId(date,directionId);
  }

  public ReportRequest getReportRequestById(String id){
    return repository.findById(id).orElseThrow(()->new NotFoundException("Direction non trouvée"));
  }

  public void deleteReportById(String reportId) {
    if (!repository.existsById(reportId)) {
      throw new NotFoundException("Le rapport avec l'ID " + reportId + " n'existe pas.");
    }
    repository.deleteById(reportId);
  }

}
