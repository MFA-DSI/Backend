package com.mfa.report.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "\"mission\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private Direction direction;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private List<Activity> activity;
}
