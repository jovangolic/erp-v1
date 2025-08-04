package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.UserRequest;
import com.jovan.erp_v1.request.UserRequestForEmployees;
import com.jovan.erp_v1.request.UserRequestForEmployeesDetails;
import com.jovan.erp_v1.response.UserResponse;
import com.jovan.erp_v1.response.UserResponseForEmployees;
import com.jovan.erp_v1.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@PostMapping("/create-superadmin")
	public ResponseEntity<UserResponse> createSuperAdmin(@Valid @RequestBody UserRequest userRequest) {
		UserResponse userResponse = userService.createSuperAdmin(userRequest);
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@PostMapping("/create-admin")
	public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody UserRequest userRequest) {
		UserResponse userResponse = userService.createAdmin(userRequest);
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/create-user")
	public ResponseEntity<UserResponse> createUserByAdmin(@Valid @RequestBody UserRequest request) {
		UserResponse createdUser = userService.createUserByAdmin(request);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	/**
	 * Ova metoda ne koristi polja email,password i username, jer se pre kreiranju
	 * zaposlenog
	 * prvo kreira njegov email, password i username, pa tek onda se kreira novi
	 * zaposleni
	 */
	@PostMapping("/admin/create-employee")
	public ResponseEntity<UserResponseForEmployees> createEmployeeByAdmin(
			@RequestBody @Valid UserRequestForEmployees request) {

		UserResponseForEmployees response = userService.createEmployeesByAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/employee-details/{id}")
	public ResponseEntity<UserResponseForEmployees> updateEmployeeDetails(
			@PathVariable Long id,
			@RequestBody @Valid UserRequestForEmployeesDetails req) {

		UserResponseForEmployees response = userService.updateEmployeeDetails(id, req);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<User> getUserByIdentifier(@PathVariable String identifier) {
		return ResponseEntity.ok(userService.getUserByIdentifier(identifier));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
		return ResponseEntity.ok(userService.updateUser(id, request));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/role/{roleName}")
	public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String roleName) {
		return ResponseEntity.ok(userService.getUsersByRoleName(roleName));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		return userService.findByUsername(username)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

}
