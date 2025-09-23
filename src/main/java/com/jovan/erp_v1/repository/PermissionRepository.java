package com.jovan.erp_v1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;
import com.jovan.erp_v1.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findByActionType(PermissionActionType actionType);
    List<Permission> findByResourceType(PermissionResourceType resourceType);
    List<Permission> findByActionTypeAndResourceType(PermissionActionType actionType, PermissionResourceType resourceType);

    boolean existsByActionType(PermissionActionType actionType);
    boolean existsByResourceType(PermissionResourceType resourceType);
    
}
