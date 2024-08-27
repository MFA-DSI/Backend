package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
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
    private  String id;
    private LocalDate dueDatetime;
    @NotBlank(message = "Next task description is mandatory")
    private String description;

    @ManyToOne
    @NotBlank(message = "next task activity is mandatory")
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @JsonIgnore
    private Activity activity;
}
