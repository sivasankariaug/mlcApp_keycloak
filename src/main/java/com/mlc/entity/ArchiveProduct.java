package com.mlc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="product1")
public class ArchiveProduct {
	@Id
	private long id;
	@Column(name="product_Name")
	private String productName;
	@Column(name="material")
	private String material;
	@Column(name="weight")
	private double weight;
	@Column(name="number_Of_Components")
	private int numberOfComponents;
}
