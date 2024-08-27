package com.mfa.report.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"notification\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  String id;

    @NotBlank(message = "notification description is mandatory")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @NotBlank(message = "notification responsible is mandatory")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    private boolean viewStatus;


}
