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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.request.OptionRequest;
import com.jovan.erp_v1.response.OptionResponse;
import com.jovan.erp_v1.service.IOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/option")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
public class OptionController {

    private final IOptionService optionService;

    @PostMapping("/create")
    public ResponseEntity<OptionResponse> create(@Valid @RequestBody OptionRequest request) {
        return ResponseEntity.ok(optionService.create(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OptionResponse> update(@PathVariable Long id, @Valid @RequestBody OptionRequest request) {
        return ResponseEntity.ok(optionService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        optionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<OptionResponse> getOne(@PathVariable Long id) {
        OptionResponse response = optionService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<OptionResponse>> getAll() {
        return ResponseEntity.ok(optionService.getAll());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<OptionResponse>> getByCategory(@PathVariable OptionCategory category) {
        return ResponseEntity.ok(optionService.getByCategory(category));
    }
    
    @GetMapping("/category-is-active")
    public ResponseEntity<List<OptionResponse>>findByCategoryAndActiveTrue(@RequestParam("category") OptionCategory category){
    	List<OptionResponse> responses = optionService.findByCategoryAndActiveTrue(category);
    	return ResponseEntity.ok(responses);
    }
}
