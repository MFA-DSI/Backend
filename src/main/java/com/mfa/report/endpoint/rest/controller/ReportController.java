package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.event.ReportRequestEvent;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.service.DirectionService;
import com.mfa.report.service.RequestReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
@Slf4j
public class ReportController {
  @Autowired private final RequestReportService reportRequestService;
  private DirectionService directionService;

  private DirectionValidator directionValidator;

  @Autowired private ApplicationEventPublisher eventPublisher;

  /** Création de demandes de rapports personnalisés pour une liste de sous-directions. */
  @PostMapping("/report/other_direction/create")
  public ResponseEntity<List<ReportRequest>> createRequests(
      @RequestParam String requestingDirectionId,
      @RequestParam String responsibleId,
      @RequestParam List<String> subDirectionIds,
      @RequestParam LocalDate weekStartDate,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize) {
    Direction direction = directionService.getDirectionById(requestingDirectionId);
    directionValidator.acceptUser(direction, responsibleId);

    List<ReportRequest> requests =
        subDirectionIds.stream()
            .map(
                subDirectionId -> {
                  ReportRequest request =
                      reportRequestService.createRequest(
                          requestingDirectionId, subDirectionId, weekStartDate,responsibleId, page, pageSize);

                  // Publier un événement après la création de chaque demande
                  eventPublisher.publishEvent(new ReportRequestEvent(request, "CREATED"));
                  return request;
                })
            .collect(Collectors.toList());

    return ResponseEntity.ok(requests);
  }

  /** Répondre à une demande de rapport. */
  @PostMapping("/{requestId}/respond")
  public ResponseEntity<ReportRequest> respondToRequest(
      @PathVariable String requestId,
      @RequestParam String targetDirectionId,
      @RequestParam String status,
      @RequestParam(required = false) String comment) {

    ReportRequest request =
        reportRequestService.respondToRequest(requestId, targetDirectionId, status, comment);
    // Publier un événement de confirmation après réponse à la demande
    eventPublisher.publishEvent(new ReportRequestEvent(request, "APPROVED"));
    return ResponseEntity.ok(request);
  }
}