package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"next_task\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NextTask {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private LocalDate dueDatetime;

  @NotBlank(message = "Next task description is mandatory")
  private String description;

  @ManyToOne
  @NotBlank(message = "next task activity is mandatory")
  @JoinColumn(name = "activity_id")
  @JsonIgnore
  private Activity activity;
}
