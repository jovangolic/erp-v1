package com.jovan.erp_v1.response;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String phoneNumber;
	private String address;
	//private Collection<Role> roles = new HashSet<>();
	@Builder.Default
	private Set<String> roles = new HashSet<>();
	
	public UserResponse(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.roles = user.getRoles().stream()
				.map(Role::getName)
				.collect(Collectors.toSet());
	}
	
	public UserResponse(User user, Role role) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
		this.roles.add(role.getName());
	}
}
