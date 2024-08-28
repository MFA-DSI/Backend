package com.mfa.report.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDate;
import java.util.List;

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
    private LocalDate creationDatetime;
    private String observation;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotBlank(message = "Activity mission is mandatory")
    private Mission mission;

    @OneToOne
    private PerformanceRealization performanceRealization;

    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> taskList;

    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<NextTask> nexTaskList;

    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Recommendation> recommendations;

}
