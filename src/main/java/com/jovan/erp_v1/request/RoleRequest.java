package com.jovan.erp_v1.request;

import java.util.Collection;

import com.jovan.erp_v1.model.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

	private Long id;

	@NotBlank(message = "Naziv uloge je obavezan")
	private String name;

	private Collection<User> users;
}
