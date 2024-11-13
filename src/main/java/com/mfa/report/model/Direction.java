package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"direction\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Direction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "direction name is mandatory")
  private String name;

  @OneToMany private List<User> responsible;

  @NotBlank(message = "Direction abbreviation (acronym) is mandatory")
  private String acronym;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  @JsonIgnore
  private Direction parentDirection;

  @OneToMany(mappedBy = "parentDirection")
  private List<Direction> subDirections;

  @OneToMany(mappedBy = "direction")
  @JsonIgnore
  private List<Mission> mission;
}
