package com.jovan.erp_v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.PermissionType;
import com.jovan.erp_v1.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionType(PermissionType type);
}
