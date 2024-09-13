package com.mfa.report.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

  @ManyToOne
  @JoinColumn(name = "direction_id")
  private Direction direction;

  @OneToMany(
      mappedBy = "mission",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Activity> activity;
}
