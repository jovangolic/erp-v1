package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;
import com.jovan.erp_v1.service.ISystemSetting;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
public class SystemSettingController {

    private ISystemSetting settings;

    @PostMapping("/create-setting")
    public ResponseEntity<SystemSettingResponse> create(@Valid @RequestBody SystemSettingCreateRequest request) {
        SystemSettingResponse response = settings.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<SystemSettingResponse> update(@Valid @RequestBody SystemSettingUpdateRequest request) {
        SystemSettingResponse response = settings.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        settings.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<SystemSettingResponse> getOneById(@PathVariable Long id) {
        SystemSettingResponse response = settings.getOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    public ResponseEntity<SystemSettingResponse> getByKey(@PathVariable String key) {
        SystemSettingResponse response = settings.getByKey(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SystemSettingResponse>> getAll() {
        List<SystemSettingResponse> responses = settings.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SystemSettingResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(settings.getByCategory(category));
    }

}
