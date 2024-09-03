package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"recommendation\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "Recommendation description is mandatory")
  private String description;

  private LocalDate creationDatetime;

  @Column(name = "is_approved")
  private boolean approved;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User responsible;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_id")
  @JsonIgnore
  private Activity activity;
}
