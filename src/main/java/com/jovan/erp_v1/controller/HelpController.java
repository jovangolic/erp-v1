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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.request.HelpRequest;
import com.jovan.erp_v1.response.HelpResponse;
import com.jovan.erp_v1.service.IHelpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/help")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
public class HelpController {

    private final IHelpService helpService;

    @PostMapping("/create")
    public ResponseEntity<HelpResponse> createHelp(@Valid @RequestBody HelpRequest request) {
        HelpResponse response = helpService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HelpResponse> updateHelp(@PathVariable Long id, @Valid @RequestBody HelpRequest request) {
        HelpResponse response = helpService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHelp(@PathVariable Long id) {
        helpService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<HelpResponse> getById(@PathVariable Long id) {
        HelpResponse response = helpService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-help")
    public ResponseEntity<List<HelpResponse>> getAllHelp() {
        List<HelpResponse> responses = helpService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/visible")
    public ResponseEntity<List<HelpResponse>> getVisible() {
        List<HelpResponse> responses = helpService.getVisible();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<HelpResponse>> getByCategory(@PathVariable HelpCategory category) {
        List<HelpResponse> responses = helpService.getByCategory(category);
        return ResponseEntity.ok(responses);
    }

}
