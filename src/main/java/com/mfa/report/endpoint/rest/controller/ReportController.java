package com.mfa.report.endpoint.rest.controller;

import com.mfa.report.endpoint.rest.mapper.ReportRequestMapper;
import com.mfa.report.endpoint.rest.model.DTO.ReportRequestDTO;
import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.event.ReportRequestEvent;
import com.mfa.report.model.validator.DirectionValidator;
import com.mfa.report.repository.exception.BadRequestException;
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

  private ReportRequestMapper mapper;
  private DirectionValidator directionValidator;

  @Autowired private ApplicationEventPublisher eventPublisher;

  /** Création de demandes de rapports personnalisés pour une liste de sous-directions. */
  @PostMapping("/report/other_direction/create")
  public ResponseEntity<List<ReportRequestDTO>> createRequests(
      @RequestParam String requesterDirectionId,
      @RequestParam String responsibleId,
      @RequestBody List<String> subDirectionIds,
      @RequestParam LocalDate weekStartDate,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int pageSize) {
    Direction direction = directionService.getDirectionById(requesterDirectionId);
    directionValidator.acceptUser(direction, responsibleId);

    List<ReportRequest> requests =
        subDirectionIds.stream()
            .map(
                subDirectionId -> {
                  ReportRequest request =
                      reportRequestService.createRequest(
                          requesterDirectionId, subDirectionId, weekStartDate,responsibleId, page, pageSize);

                  // Publier un événement après la création de chaque demande
                  eventPublisher.publishEvent(new ReportRequestEvent(request, "CREATED"));
                  return request;
                })
            .collect(Collectors.toList());


    List<ReportRequestDTO>  reportRequestDTOS = requests.stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
    return ResponseEntity.ok(reportRequestDTOS);
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
}
