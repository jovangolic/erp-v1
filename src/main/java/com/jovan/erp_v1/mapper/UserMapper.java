package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.request.UserRequest;
import com.jovan.erp_v1.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

	private final RoleRepository roleRepository;
	
	public User toEntity(UserRequest request) {
		if (request == null) {
            return null;
        }
        User user = new User();
        user.setId(request.id());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setPassword(request.password());
        // Mapiranje roleIds u Role entitete
        if (request.roleIds() != null && !request.roleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        return user;
	}
	
	public UserResponse toResponse(User user) {
		if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAddress(user.getAddress());
        // Mapiranje uloga u set stringova imena uloga
        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
            response.setRoles(roleNames);
        } else {
            response.setRoles(new HashSet<>());
        }
        return response;
	}
	
	public List<UserResponse> toResponseList(List<User> users){
		if(users == null || users.isEmpty()) {
			return Collections.emptyList();
		}
		return users.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
	public void updateUser(UserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.email() != null) user.setEmail(request.email());
        if (request.username() != null) user.setUsername(request.username());
        if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());
        if (request.address() != null) user.setAddress(request.address());
        if (request.password() != null) user.setPassword(request.password());
        
        if (request.roleIds() != null && !request.roleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
            user.setRoles(roles);
        }
        
    }
}
