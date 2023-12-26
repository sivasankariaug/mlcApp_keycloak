package com.mlc.entity;

import lombok.Data;
@Data
public class AdminIfo {
    private String adminUser;
    private String adminPassword;
    private UserMaster userMaster;
    private Roles roles;
}
