package com.mlc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "user_master")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserMaster {
    @Id
    @Column(name = "iduser")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "user_name")
    private String username;
    @Column(name = "status")
    private boolean enabled;
    @Transient
    private List<Credentials> credentials;
    @Column(name ="keycloak_user_id")
    @JsonIgnore
    private String keycloakUserId;
}
