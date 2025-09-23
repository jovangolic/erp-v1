package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;
import com.jovan.erp_v1.service.IPermissionService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.PERMISSION_FULL_ACCESS)
public class PermissionController {

    private final IPermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAll() {
        return ResponseEntity.ok(permissionService.getAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id) {
        PermissionResponse response = permissionService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PermissionResponse> update(@PathVariable Long id,
            @Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.update(id, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/by-action-type")
    public ResponseEntity<List<PermissionResponse>> findByActionType(@RequestParam("actionType") PermissionActionType actionType){
    	List<PermissionResponse> items = permissionService.findByActionType(actionType);
    	return ResponseEntity.ok(items);
    }
    
    @GetMapping("/by-resource-type")
    public ResponseEntity<List<PermissionResponse>> findByResourceType(@RequestParam("resourceType") PermissionResourceType resourceType){
    	List<PermissionResponse> items = permissionService.findByResourceType(resourceType);
    	return ResponseEntity.ok(items);
    }
    
    @GetMapping("/action-type-and-resource-type")
    public ResponseEntity<List<PermissionResponse>> findByActionTypeAndResourceType(@RequestParam("actionType") PermissionActionType actionType,
    		@RequestParam("resourceType") PermissionResourceType resourceType){
    	List<PermissionResponse> items = permissionService.findByActionTypeAndResourceType(actionType, resourceType);
    	return ResponseEntity.ok(items);
    }
    
    @GetMapping("/exists/by-action-type")
    public ResponseEntity<Boolean> existsByActionType(@RequestParam("actionType") PermissionActionType actionType){
    	Boolean items = permissionService.existsByActionType(actionType);
    	return ResponseEntity.ok(items);
    }
    
    @GetMapping("/exists/by-resource-type")
    public ResponseEntity<Boolean> existsByResourceType(@RequestParam("resourceType") PermissionResourceType resourceType){
    	Boolean items = permissionService.existsByResourceType(resourceType);
    	return ResponseEntity.ok(items);
    }
    
    @PostMapping("/save")
    public ResponseEntity<PermissionResponse> savePermission(@Valid @RequestBody PermissionRequest request){
    	PermissionResponse items = permissionService.savePermission(request);
    	return ResponseEntity.ok(items);
    }
}
