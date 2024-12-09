package com.mfa.report.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "\"performance_realization\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceRealization {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private String id;
    private Long KPI;
    private String realization;

    @OneToOne
    private Activity activity;

}
