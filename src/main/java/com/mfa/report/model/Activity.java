package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "\"activity\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "Activity description is mandatory")
  private String description;

  private String prediction;
  private LocalDate dueDatetime;
  private String observation;
  private LocalDateTime creationDatetime;

  @ManyToOne
  @NotBlank(message = "Activity mission is mandatory")
  @JoinColumn(name = "mission_id")
  private Mission mission;

  @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PerformanceRealization> performanceRealization;

  @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> taskList;

  @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NextTask> nexTaskList;

  @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Recommendation> recommendations;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "report_request_id")
  private ReportRequest reportRequest;
}
