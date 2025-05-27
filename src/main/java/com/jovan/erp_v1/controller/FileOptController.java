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

import com.jovan.erp_v1.enumeration.FileExtension;
import com.jovan.erp_v1.request.FileOptRequest;
import com.jovan.erp_v1.response.FileOptResponse;
import com.jovan.erp_v1.service.IFileOptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file-opt")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class FileOptController {

    private final IFileOptService fileOptService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<FileOptResponse> create(@Valid @RequestBody FileOptRequest request) {
        FileOptResponse response = fileOptService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<FileOptResponse> updateFileOpt(@PathVariable Long id,
            @Valid @RequestBody FileOptRequest request) {
        FileOptResponse response = fileOptService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFileOpt(@PathVariable Long id) {
        fileOptService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<FileOptResponse>> getAllFileOpts() {
        List<FileOptResponse> responses = fileOptService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<FileOptResponse> getFileOptById(@PathVariable Long id) {
        FileOptResponse response = fileOptService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-extension/{extension}")
    public ResponseEntity<List<FileOptResponse>> getByExtension(@PathVariable FileExtension extension) {
        List<FileOptResponse> responses = fileOptService.getByExtension(extension);
        return ResponseEntity.ok(responses);
    }

}
