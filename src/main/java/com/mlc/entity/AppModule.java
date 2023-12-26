package com.mlc.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="app_modules")
@Data
public class AppModule {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="idModule")
private long idModule;
@Column(name="moduleName")
private String moduleName;
@Column(name="description")
private String description;

}
