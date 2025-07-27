package com.jovan.erp_v1.response;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.jovan.erp_v1.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageEmployeeForUserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNumber;
	private String address;
	@Builder.Default
	private Set<String> roles = new HashSet<>();
	//private Collection<Role> roles = new HashSet<>();
	private String username;
	
	public StorageEmployeeForUserResponse(User u) {
		this.id = u.getId();
		this.firstName = u.getFirstName();
		this.lastName = u.getLastName();
		this.email = u.getEmail();
		this.password = u.getPassword();
		this.address = u.getAddress();
		this.roles = u.getRoles().stream()
				.map(role -> role.getName()) 
                .collect(Collectors.toSet());
		this.username = u.getUsername();
	}
}
