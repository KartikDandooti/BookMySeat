package com.valtech.bookmyseat.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Restrain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int restrainId;

	@Column(nullable = false)
	private boolean restrainStatus;

	@OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, mappedBy = "restrain")
	private List<User> user;

	@OneToOne(targetEntity = Floor.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "floorId", nullable = false)
	private Floor floor;
}