package com.jovan.erp_v1.request;

import java.util.Collection;
import java.util.Set;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.model.User;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

	private Long id;

	@NotNull(message = "Naziv uloge je obavezan")
	private String name;

	@NotNull(message = "Tip uloge je obavezan")
	private RoleTypes roleTypes;

	private Collection<User> users;

	private Set<Long> permissionIds;
}
