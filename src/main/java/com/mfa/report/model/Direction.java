package com.mfa.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  @OneToMany @JsonIgnore private List<Mission> mission;
}
