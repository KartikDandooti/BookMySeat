package com.valtech.team3.bookmyseatbackend.models;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.entities.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDisplayModel {
	private List<User> users;
	private List<Project> projects;
}