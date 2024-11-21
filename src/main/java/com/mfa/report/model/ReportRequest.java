package com.mfa.report.model;

import com.mfa.report.model.enumerated.RequestReportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"report_request\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "request description is mandatory")
  private String description;

  @ManyToOne
  @JoinColumn(name = "responsible_id", nullable = false)
  private User responsible;

  private LocalDate startedAt;

  @ManyToOne
  @JoinColumn(name = "requesting_direction_id", nullable = false)
  private Direction requesterDirection;

  @ManyToOne
  @JoinColumn(name = "target_direction_id", nullable = false)
  private Direction targetDirection;

  @Column(nullable = false)
  private LocalDate createdAt;

  @Column(nullable = false)
  private LocalDate expirationAt;

  @Enumerated(EnumType.STRING)
  private RequestReportStatus
      status; // Statut de la demande: "PENDING", "APPROVED", "REJECTED", "EXPIRED"

  @OneToMany(mappedBy = "reportRequest", cascade = CascadeType.ALL)
  private List<Activity> activities; // Liste des activités associées

  @Column(length = 500)
  private String comment; // Commentaire (obligatoire en cas de rejet)
}
