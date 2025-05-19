package com.jovan.erp_v1.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
    public void run(String... args) {
        if (userRepository.findByUsername("foreman") == null) {
            User foreman = new User("foreman", "foreman@example.com", passwordEncoder.encode("pass"));
            Role role = roleRepository.findByName("ROLE_STORAGE_FOREMAN").orElseThrow();
            foreman.getRoles().add(role);
            userRepository.save(foreman);
        }

        if (userRepository.findByUsername("employee") == null) {
            User employee = new User("employee", "employee@example.com", passwordEncoder.encode("pass"));
            Role role = roleRepository.findByName("ROLE_STORAGE_EMPLOYEE").orElseThrow();
            employee.getRoles().add(role);
            userRepository.save(employee);
        }
    }
}
