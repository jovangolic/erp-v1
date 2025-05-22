package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.UserRequest;
import com.jovan.erp_v1.response.UserResponse;

public interface IUserService extends UserDetailsService {
	// Registracija i kreiranje korisnika
	UserResponse registerUser(UserRequest request);

	UserResponse createUserByAdmin(UserRequest request);

	UserResponse createAdmin(UserRequest userRequest);

	UserResponse createSuperAdmin(UserRequest request);

	// Dohvat korisnika
	User getUserById(Long id);

	User getUserByEmail(String email);

	User getUserByUsername(String username);

	User getUserByIdentifier(String identifier);

	User getUser(String email);

	List<User> getUsers();

	List<UserResponse> getUsersByRoleName(String roleName);

	List<User> getUsersByRole(String roleName);

	Optional<User> findByUsername(String username);

	// Ažuriranje i brisanje
	User updateUser(Long id, UserRequest request);

	// void deleteUser(String email);
	void deleteUser(Long userId);

	// Interno snimanje korisnika
	User saveUser(User user);
}
