package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ReportRequestMapper;
import com.mfa.report.endpoint.rest.model.DTO.ReportRequestDTO;
import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.event.ReportRequestEvent;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.RequestReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@Slf4j
public class ReportController {
  @Autowired private final RequestReportService reportRequestService;
  private DirectionService directionService;

  private ReportRequestMapper mapper;
  private DirectionValidator directionValidator;

  @Autowired private ApplicationEventPublisher eventPublisher;

  /** Création de demandes de rapports personnalisés pour une liste de sous-directions. */
  @PostMapping("/report/other_direction/create")
  public ResponseEntity<List<Map<String, Object>>> createRequests(
          @RequestParam String requesterDirectionId,
          @RequestParam String responsibleId,
          @RequestBody List<String> subDirectionIds,
          @RequestParam LocalDate weekStartDate,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "15") int pageSize) {

    // Vérification des prérequis
    Direction direction = directionService.getDirectionById(requesterDirectionId);
    directionValidator.acceptUser(direction, responsibleId);

    // Liste pour les résultats
    List<Map<String, Object>> results = new ArrayList<>();

    // Parcourir chaque sous-direction pour traitement individuel
    for (String subDirectionId : subDirectionIds) {
      Map<String, Object> result = new HashMap<>();
      result.put("subDirectionId", subDirectionId);

      try {
        // Création de la demande
        ReportRequest request = reportRequestService.createRequest(
                requesterDirectionId, subDirectionId, weekStartDate, responsibleId, page, pageSize);

        // Publier un événement en cas de succès
        eventPublisher.publishEvent(new ReportRequestEvent(request, "CREATED"));

        // Ajouter les détails de la demande réussie
        result.put("status", "SUCCESS");
        result.put("data", mapper.toDomain(request));

      } catch (BadRequestException e) {
        // Erreurs métier, comme une activité vide
        result.put("status", "ERROR");
        result.put("message", e.getMessage());
      } catch (Exception e) {
        // Autres erreurs non anticipées
        result.put("status", "ERROR");
        result.put("message", "Erreur inattendue : " + e.getMessage());
      }

      // Ajouter le résultat dans la liste des résultats
      results.add(result);
    }

    // Retourner tous les résultats au client
    return ResponseEntity.ok(results);
  }


  /** Répondre à une demande de rapport. */
  @PostMapping("/report/{requestId}/respond")
  public ResponseEntity<ReportRequestDTO> respondToRequest(
          @PathVariable String requestId,
          @RequestParam String targetDirectionId,
          @RequestParam String status,
          @RequestBody(required = false) String comment) {
    if("REJECTED".equalsIgnoreCase(status) && comment == null || comment.isBlank()){
      throw new BadRequestException("Un commentaire est requis lors du rejet d'une demande.");
    }
    ReportRequest request =
        reportRequestService.respondToRequest(requestId, targetDirectionId, status, comment);


    // Publier un événement de confirmation après réponse à la demande
    eventPublisher.publishEvent(new ReportRequestEvent(request, status));
    return ResponseEntity.ok(mapper.toDomain(request));
  }

  @GetMapping("/report/all_request")
  public List<ReportRequestDTO> getAllRequestByDirectionId(@RequestParam String directionId){
    return reportRequestService.getAllRequest(directionId).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }
  @GetMapping("/report/all_targeted")
  public List<ReportRequestDTO> getAllTargetedRequestByDirectionId(@RequestParam String directionId){
    return reportRequestService.getAllTargetedRequest(directionId).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
  }

  @DeleteMapping("/delete/reports/{reportId}")
  public ResponseEntity<Map<String, String>> deleteReport(@PathVariable String reportId) {
    try {
      // Vérification si le rapport existe
      ReportRequest report = reportRequestService.getReportRequestById(reportId);
      if (report == null) {
        throw new NotFoundException("Le rapport avec l'ID " + reportId + " n'existe pas.");
      }

      // Suppression du rapport
      reportRequestService.deleteReportById(reportId);

      // Retourner un message de succès
      Map<String, String> response = new HashMap<>();
      response.put("message", "Rapport supprimé avec succès !");
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      // Retourner un message en cas de rapport non trouvé
      Map<String, String> response = new HashMap<>();
      response.put("message", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      // Retourner un message générique en cas d'erreur serveur
      Map<String, String> response = new HashMap<>();
      response.put("message", "Erreur lors de la suppression du rapport.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/recall/report/{reportId}")
  public ResponseEntity<Map<String, String>> recallReport(@PathVariable String reportId) {
    try {
      // Vérification si le rapport existe
      ReportRequest report = reportRequestService.getReportRequestById(reportId);
      if (report == null) {
        throw new NotFoundException("Le rapport avec l'ID " + reportId + " n'existe pas.");
      }

      // Relancer un événement pour notifier
      eventPublisher.publishEvent(new ReportRequestEvent(report, "RECALL"));

      // Retourner un message de succès
      Map<String, String> response = new HashMap<>();
      response.put("message", "Rapport rappelé et notification envoyée avec succès !");
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      // Retourner un message en cas de rapport non trouvé
      Map<String, String> response = new HashMap<>();
      response.put("message", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      // Retourner un message générique en cas d'erreur serveur
      Map<String, String> response = new HashMap<>();
      response.put("message", "Erreur lors du rappel du rapport.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }



}
