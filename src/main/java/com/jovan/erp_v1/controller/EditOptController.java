package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.EditOptType;
import com.jovan.erp_v1.request.EditOptRequest;
import com.jovan.erp_v1.response.EditOptResponse;
import com.jovan.erp_v1.service.IEditOptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/edit-opt")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@Validated
@PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
public class EditOptController {

    private final IEditOptService editOptService;

    @PostMapping("/create")
    public ResponseEntity<EditOptResponse> create(@Valid @RequestBody EditOptRequest request) {
        EditOptResponse response = editOptService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EditOptResponse> update(@PathVariable Long id, @Valid @RequestBody EditOptRequest request) {
        EditOptResponse response = editOptService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        editOptService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<EditOptResponse> getById(@PathVariable Long id) {
        EditOptResponse response = editOptService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<EditOptResponse>> getAll() {
        List<EditOptResponse> responses = editOptService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("by-type/{type}")
    public ResponseEntity<List<EditOptResponse>> getByType(@PathVariable EditOptType type) {
        List<EditOptResponse> responses = editOptService.getByType(type);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/by-name")
    public ResponseEntity<EditOptResponse> findByName(@RequestParam("name") String name){
    	EditOptResponse response = editOptService.findByName(name);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/by-value")
    public ResponseEntity<EditOptResponse> findByValue(@RequestParam("value") String value){
    	EditOptResponse response = editOptService.findByValue(value);
    	return ResponseEntity.ok(response);
    }
    
}
