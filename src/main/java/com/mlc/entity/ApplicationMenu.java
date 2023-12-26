package com.mlc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="application_menus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationMenu {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="idMenu")
private long id;
@Column(name="menuName")
private String menuName;
@Column(name="description")
private String description;

@ManyToOne
@JoinColumn(name="module_id")
private AppModule module;

@ManyToOne
@JoinColumn(name="parent_menu_id")
private ApplicationMenu parentMenu;

}
