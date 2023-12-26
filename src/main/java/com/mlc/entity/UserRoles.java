package com.mlc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_roles")
public class UserRoles {
    @Column(name = "id_user_roles")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUserRoles;
    @Column(name = "id_user_master")
    private Long idUserMaster;
    @Column(name = "id_roles")
    private Long idRoles;
    @Transient
    private String userName;
    @Transient
    private String roleName;
}
