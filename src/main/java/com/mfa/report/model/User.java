package com.mfa.report.model;


import com.mfa.report.endpoint.rest.model.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotBlank(message = "username is mandatory")
    private String username;

    @Column(name = "first_name")
    @NotBlank(message = "firstname is mandatory")
    private String firstname;

    @Column(name = "last_name")
    @NotBlank(message = "lastname is mandatory")
    private String lastname;

    @Column(unique = true)
    @NotBlank(message = "mail is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    private List<Notification> notificationList;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @NotBlank(message = "user should be attached to one direction")
    private Direction direction;
}
