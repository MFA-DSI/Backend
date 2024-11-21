package com.mfa.report.model;

import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"notification\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "notification description is mandatory")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @NotBlank(message = "notification responsible is mandatory")
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @NotBlank(message = "responsible is mandatory")
  @Column(name = "responsible")
  private String responsibleDirection;

  @Enumerated(EnumType.STRING)
  private NotificationStatus notificationType;

  private LocalDateTime creationDatetime;

  private boolean viewStatus;
}
