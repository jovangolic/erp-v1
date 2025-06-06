package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jovan.erp_v1.enumeration.RoleTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private RoleTypes roleTypes;

	@ManyToMany(mappedBy = "roles")
	@Builder.Default
	private Collection<User> users = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	@Builder.Default
	private Set<Permission> permissions = new HashSet<>();

	public Role(String name) {
		this.name = name;
	}

	public void assignRoleToUser(User user) {
		user.getRoles().add(this);
		this.users.add(user);
	}

	public void removeUserFromRole(User user) {
		user.getRoles().remove(this);
		this.getUsers().remove(user);
	}

	public void removeAllUsersFromRole() {
		if (this.getUsers() != null) {
			List<User> roleUsers = new ArrayList<>(this.getUsers());
			roleUsers.forEach(this::removeUserFromRole);
		}
	}

	public String getNameAsString() {
		return roleTypes != null ? roleTypes.name() : "";
	}
}
