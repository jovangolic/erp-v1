package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.save_as.GenericSaveAsRequest;
import com.jovan.erp_v1.service.ILocalizedOptionService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/localizedOptions")
@PreAuthorize(RoleGroups.LOCALIZED_OPTION_FULL_ACCESS)
public class LocalizedOptionController {

    private final ILocalizedOptionService localizedOptionService;

    @PostMapping("/create/new-localizedOption")
    public ResponseEntity<LocalizedOptionResponse> create(@Valid @RequestBody LocalizedOptionRequest request) {
        return ResponseEntity.ok(localizedOptionService.create(request));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<LocalizedOptionResponse>> getAll() {
        return ResponseEntity.ok(localizedOptionService.getAll());
    }

    @GetMapping("/option/{optionId}/translations")
    public ResponseEntity<List<LocalizedOptionResponse>> getTranslationsForOption(@PathVariable Long optionId) {
        List<LocalizedOptionResponse> translations = localizedOptionService.getTranslationsForOption(optionId);
        return ResponseEntity.ok(translations);
    }

    // Dodavanje novog prevoda za konkretnu opciju
    @PostMapping("/option/{optionId}/translations")
    public ResponseEntity<LocalizedOptionResponse> addTranslationForOption(
            @PathVariable Long optionId,
            @Valid @RequestBody LocalizedOptionRequest request) {
        LocalizedOptionResponse createdTranslation = localizedOptionService.addTranslationForOption(optionId, request);
        return ResponseEntity.ok(createdTranslation);
    }

    @DeleteMapping("/deleteAll-option/{optionId}")
    public ResponseEntity<Void> deleteAllByOptionId(@PathVariable Long optionId) {
        localizedOptionService.deleteAllByOptionId(optionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/option/{optionId}/language/{languageId}")
    public ResponseEntity<LocalizedOptionResponse> getTranslation(@PathVariable Long optionId,
            @PathVariable Long languageId) {
        LocalizedOptionResponse response = localizedOptionService.getTranslation(optionId, languageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/translations/language/{languageId}")
    public ResponseEntity<List<LocalizedOptionResponse>> getAllByLanguage(@PathVariable Long languageId) {
        List<LocalizedOptionResponse> responses = localizedOptionService.getAllByLanguage(languageId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<LocalizedOptionResponse> findOne(@PathVariable Long id) {
        LocalizedOptionResponse response = localizedOptionService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LocalizedOptionResponse> update(@PathVariable Long id,
            @Valid @RequestBody LocalizedOptionRequest request) {
        LocalizedOptionResponse response = localizedOptionService.update(id, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/label")
    public ResponseEntity<List<LocalizedOptionResponse>> findByOption_Label(@RequestParam("label") String label){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByOption_Label(label);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/value")
    public ResponseEntity<List<LocalizedOptionResponse>> findByOption_Value(@RequestParam("value") String value){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByOption_Value(value);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/category")
    public ResponseEntity<List<LocalizedOptionResponse>> findByOption_Category(@RequestParam("category") OptionCategory category){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByOption_Category(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/language/{languageId}")
    public ResponseEntity<List<LocalizedOptionResponse>> findByLanguage_Id(@PathVariable Long languageId){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByLanguage_Id(languageId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/language/code-type")
    public ResponseEntity<List<LocalizedOptionResponse>> findByLanguage_LanguageCodeType(@RequestParam("languageCodeType") LanguageCodeType languageCodeType){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByLanguage_LanguageCodeType(languageCodeType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/language/name-type")
    public ResponseEntity<List<LocalizedOptionResponse>> findByLanguage_LanguageNameType(@RequestParam("languageNameType") LanguageNameType languageNameType){
    	List<LocalizedOptionResponse> responses = localizedOptionService.findByLanguage_LanguageNameType(languageNameType);
    	return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/save")
    public ResponseEntity<LocalizedOptionResponse> saveLozalizedOptions(@Valid @RequestBody LocalizedOptionRequest request){
    	LocalizedOptionResponse items = localizedOptionService.saveLozalizedOptions(request);
    	return ResponseEntity.ok(items);
    }
    
    @PostMapping("/save-as")
    public ResponseEntity<LocalizedOptionResponse> saveAs(
            @RequestBody GenericSaveAsRequest request) {
        LocalizedOptionResponse item = localizedOptionService.saveAs(request.sourceId(), request.newLabel());
        return ResponseEntity.ok(item);
    }
    
    @PostMapping("/save-all")
    public ResponseEntity<List<LocalizedOptionResponse>> saveAll(@RequestBody List<LocalizedOptionRequest> requests){
    	List<LocalizedOptionResponse> items = localizedOptionService.saveAll(requests);
    	return ResponseEntity.ok(items);
    }
}
