package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String role);

	boolean existsByName(String name);
	
	Optional<Role> findById(Long id);
	
	List<Role> findAllByUsers_Id(Long userId);

}
