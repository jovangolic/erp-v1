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
public class ShiftSupervisorResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	@Builder.Default
	private Set<String> roles = new HashSet<>();
	
	public ShiftSupervisorResponse(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.roles = user.getRoles().stream()
				.map(Role::getName)
				.collect(Collectors.toSet());
	}
}
