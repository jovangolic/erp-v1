package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.EmailAlreadyExistsException;
import com.jovan.erp_v1.exception.RoleErrorNotFoundException;
import com.jovan.erp_v1.exception.UserAlreadyExistsException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.UserMapper;
import com.jovan.erp_v1.model.CustomUserDetails;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.UserRequest;
import com.jovan.erp_v1.response.UserResponse;
import com.jovan.erp_v1.security.JwtAuthenticationFilter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("userService")
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	@Transactional
	public UserResponse registerUser(UserRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already in use.");
		}
		if (userRepository.existsByUsername(request.username())) {
			throw new UserAlreadyExistsException("Username already in use.");
		}
		User user = new User();
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setPhoneNumber(request.phoneNumber());
		user.setAddress(request.address());
		String username = generateUsername(user.getFirstName(), user.getLastName());
		user.setUsername(username);
		String rawPassword = request.password();
		if (rawPassword == null || rawPassword.isBlank()) {
			rawPassword = generatePassword(); // fallback
			logger.warn("Password not provided, generated password used: {}", rawPassword);
		} else {
			logger.info("Password received: {}", rawPassword);
		}
		user.setPassword(passwordEncoder.encode(rawPassword));
		Set<Role> roles = request.roleIds().stream()
				.map(roleId -> roleRepository.findById(roleId)
						.orElseThrow(() -> new RoleErrorNotFoundException("Role not found: " + roleId)))
				.collect(Collectors.toSet());
		user.setRoles(roles);
		User savedUser = userRepository.save(user);
		return new UserResponse(savedUser);
	}

	@Override
	public List<UserResponse> getUsers() {
		return userRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException("Can't delete user with email: " + userId);
		}
		userRepository.deleteById(userId);
	}

	@Override
	public User getUser(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new EmailAlreadyExistsException("User not found with email: " + email));
	}

	@Override
	public User getUserByIdentifier(String identifier) {
		if (identifier.contains("@")) {
			// Email login
			return userRepository.findByEmail(identifier)
					.orElseThrow(() -> new UserNotFoundException("Email not found"));
		}
		return userRepository.findByEmail(identifier)
				.or(() -> userRepository.findByPhoneNumber(identifier))
				.orElseThrow(() -> new EmailAlreadyExistsException("User not found with identifier: " + identifier));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new EmailAlreadyExistsException("User not found with email: " + email));
	}

	@Transactional
	@Override
	public User updateUser(Long id, UserRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userMapper.updateUser(request, user);
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
	}

	@Override
	public List<User> getUsersByRole(String roleName) {
		return userRepository.findUsersByRoleName(roleName);
	}

	@Transactional
	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<UserResponse> getUsersByRoleName(String roleName) {
		return userRepository.findUsersByRoleName(roleName)
				.stream()
				.map(userMapper::toResponse)
				.collect(Collectors.toList());
	}

	// Konverzija User u UserResponse bez MapStructa (ako ne koristiš DTO mapper)
	private UserResponse toResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getUsername(),
				user.getPhoneNumber(),
				user.getAddress(),
				user.getRoles().stream()
						.map(Role::getName)
						.collect(Collectors.toSet()));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	private String generateUsername(String firstName, String lastName) {
		String base = firstName.toLowerCase() + "." + lastName.toLowerCase();
		int suffix = 1;
		String username = base;
		while (userRepository.existsByUsername(username)) {
			username = base + suffix++;
		}
		return username;
	}

	private String generatePassword() {
		return UUID.randomUUID().toString().substring(0, 20);
	}

	@Transactional
	@Override
	public UserResponse createUserByAdmin(UserRequest request) {
		// 1. Proveri da li već postoji korisnik sa istim email-om ili korisničkim
		// imenom
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email već postoji");
		}
		if (userRepository.existsByUsername(request.username())) {
			throw new IllegalArgumentException("Korisničko ime već postoji");
		}
		// 2. Dohvati role po ID-jevima
		Set<Role> roles = request.roleIds().stream()
				.map(roleId -> roleRepository.findById(roleId)
						.orElseThrow(() -> new IllegalArgumentException("Rola sa ID " + roleId + " ne postoji")))
				.collect(Collectors.toSet());
		// 3. Kreiraj novog korisnika
		User user = new User();
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setUsername(request.username());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setPhoneNumber(request.phoneNumber());
		user.setAddress(request.address());
		user.setRoles(roles);
		// 4. Sačuvaj korisnika
		User savedUser = userRepository.save(user);
		// 5. Vrati UserResponse DTO
		return new UserResponse(savedUser);
	}

	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(identifier)
				.or(() -> userRepository.findByEmail(identifier))
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username/email: " + identifier));

		return new CustomUserDetails(user);
	}

	@Override
	@Transactional
	public UserResponse createAdmin(UserRequest request) {
		return createUserWithRole(request, "ROLE_ADMIN");
	}

	@Override
	@Transactional
	public UserResponse createSuperAdmin(UserRequest request) {
		return createUserWithRole(request, "ROLE_SUPERADMIN");
	}

	private UserResponse createUserWithRole(UserRequest request, String roleName) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already in use.");
		}
		if (userRepository.existsByUsername(request.username())) {
			throw new UserAlreadyExistsException("Username already in use.");
		}
		User user = new User();
		user.setUsername(request.username());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setPhoneNumber(request.phoneNumber());
		user.setAddress(request.address());
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RoleErrorNotFoundException("Role not found"));
		user.getRoles().add(role);
		userRepository.save(user);
		return new UserResponse(user, role);
	}

	private User buildUserFromRequest(UserRequest request, boolean generatePassword) {
		User user = new User();
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setPhoneNumber(request.phoneNumber());
		user.setAddress(request.address());
		String username = generateUsername(request.firstName(), request.lastName());
		user.setUsername(username);
		String rawPassword = generatePassword ? generatePassword() : request.password();
		user.setPassword(passwordEncoder.encode(rawPassword));
		Set<Role> roles = roleRepository.findAllById(request.roleIds()).stream().collect(Collectors.toSet());
		user.setRoles(roles);
		return user;
	}

	private User buildUserFromRequest(UserRequest request, boolean generatePassword, String roleName) {
		User user = buildUserFromRequest(request, generatePassword);
		if (roleName != null) {
			Role role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new RoleErrorNotFoundException("Role not found"));
			user.getRoles().add(role);
		}
		return user;
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
