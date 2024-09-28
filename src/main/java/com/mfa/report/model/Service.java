package com.mfa.report.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"service\"")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @NotBlank(message = "service name is mandatory")
  private String name;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "direction_id")
  private Direction direction;

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Mission> mission;
}
