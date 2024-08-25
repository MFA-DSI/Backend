package com.mfa.report.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"task\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private LocalDate dueDatetime ;

    @NotBlank(message = "task description is mandatory")
    private String description;

    @ManyToOne(targetEntity = Activity.class,fetch = FetchType.LAZY)
    @NotBlank(message = "task activity is mandatory")
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
