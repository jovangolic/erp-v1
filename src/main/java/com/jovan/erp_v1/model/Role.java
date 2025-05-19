package com.jovan.erp_v1.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private Collection<User> users = new HashSet<>();
	
	public Role(String name) {
		this.name = name;
	}
	
	//dodeljivanje uloge korisniku
	public void assignRoleToUser(User user) {
		//unakrsno povezivanje
		user.getRoles().add(this);
		this.users.add(user);
	}
	
	//brisanje jednog korisnika
	public void removeUserFromRole(User user) {
		//unakrsno odvezivanje
		user.getRoles().remove(this);
		this.getUsers().remove(user);
	}
	
	//brisanje svih korisnika
	public void removeAllUsersFromRole() {
		if(this.getUsers() != null) {
			List<User> roleUsers = this.getUsers().stream().toList();
			roleUsers.forEach(this::removeUserFromRole);
		}
	}
	
	public String getName() {
		return name != null ? name : "";
	}
}
