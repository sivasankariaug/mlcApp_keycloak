package com.mlc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idroles")
    @JsonIgnore
    private Long idroles;
    @Column(name = "name")
    private String name;
    @Transient
    private boolean composite;
    @Transient
    private boolean clientRole;
    @Transient
    private String containerId;
    @Column(name = "description")
    private String description;
    @Column(name = "keycloak_role_id")
    private String id;
}
