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

import com.jovan.erp_v1.request.RoleRequest;
import com.jovan.erp_v1.response.RoleResponse;
import com.jovan.erp_v1.service.IRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") 
public class RoleController {

	private final IRoleService roleService;
	
	
	// Kreiranje nove uloge (samo admin)
    @PostMapping("/create-new-role")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        RoleResponse createdRole = roleService.createRole(roleRequest);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    // Ažuriranje postojeće uloge (samo admin)
    @PutMapping("/update/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequest updatedRole) {
        RoleResponse updatedRoleResponse = roleService.updateRole(roleId, updatedRole);
        return ResponseEntity.ok(updatedRoleResponse);
    }

    // Brisanje uloge (samo admin)
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    // Dohvatanje svih uloga
    @GetMapping("get-all-roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Dohvatanje uloge po ID-u
    @GetMapping("/role/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long roleId) {
        RoleResponse roleResponse = roleService.getRoleById(roleId);
        return ResponseEntity.ok(roleResponse);
    }

    // Dodeljivanje korisnika određenoj ulozi (samo admin)
    @PostMapping("/{roleId}/assign/{userId}")
    public ResponseEntity<Void> assignUserToRole(@PathVariable Long roleId, @PathVariable Long userId) {
        roleService.assignUserToRole(roleId, userId);
        return ResponseEntity.ok().build();
    }
}
