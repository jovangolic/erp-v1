package com.jovan.erp_v1.util;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.model.Role;

import jakarta.annotation.PostConstruct;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        addRoleIfNotExists("ROLE_SUPERADMIN");
        addRoleIfNotExists("ROLE_ADMIN");
        addRoleIfNotExists("ROLE_MANAGER");
        addRoleIfNotExists("ROLE_INVENTORY_MANAGER");
        addRoleIfNotExists("ROLE_PRODUCTION_PLANNER");
        addRoleIfNotExists("ROLE_QUALITY_MANAGER");
        addRoleIfNotExists("ROLE_QUALITY_INSPECTOR");
        addRoleIfNotExists("ROLE_QUALITY_TECHNICIAN");
        addRoleIfNotExists("ROLE_ACCOUNTANT");
        addRoleIfNotExists("ROLE_AUDITOR");
        addRoleIfNotExists("ROLE_FINANCIAL_MANAGER");
        addRoleIfNotExists("ROLE_SECURITY_AUDITOR");
        addRoleIfNotExists("ROLE_INVENTORY_APPROVER");
        addRoleIfNotExists("ROLE_CUSTOMER_SERVICE");
        addRoleIfNotExists("ROLE_LOGISTICS_MANAGER");
        addRoleIfNotExists("ROLE_SUPPLY_CHAIN_MANAGER");
        addRoleIfNotExists("ROLE_TRANSPORT_PLANNER");
        addRoleIfNotExists("ROLE_DISPATCHER");
        addRoleIfNotExists("ROLE_DRIVER");
        addRoleIfNotExists("ROLE_MECHANIC");
        addRoleIfNotExists("ROLE_PRODUCTION_MANAGER");
        addRoleIfNotExists("ROLE_MAINTENANCE_MANAGER");
        addRoleIfNotExists("ROLE_PRODUCTION_OPERATOR");
        addRoleIfNotExists("ROLE_REGULATORY_AUDITOR");
        addRoleIfNotExists("ROLE_DISPOSAL_MANAGER");
        addRoleIfNotExists("ROLE_SALES");
        addRoleIfNotExists("ROLE_SALES_MANAGER");
        addRoleIfNotExists("ROLE_HR_MANAGER");
        addRoleIfNotExists("ROLE_PROCUREMENT");
        addRoleIfNotExists("ROLE_STORAGE_FOREMAN");
        addRoleIfNotExists("ROLE_STORAGE_EMPLOYEE");
    }

    private void addRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Uloga '" + roleName + "' je uspešno dodata u bazu.");
        }
    }
}
