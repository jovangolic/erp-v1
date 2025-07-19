package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
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

    //nove metode
    
    
    @GetMapping("/search/data-type")
    public ResponseEntity<List<SystemSettingResponse>> findByDataType(@RequestParam("dataType") SettingDataType dataType){
    	List<SystemSettingResponse> responses = settings.findByDataType(dataType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/value-and-category")
    public ResponseEntity<List<SystemSettingResponse>> findByValueAndCategory(@RequestParam("value") String value,@RequestParam("category") SystemSettingCategory category){
    	List<SystemSettingResponse> responses = settings.findByValueAndCategory(value, category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-string")
    public ResponseEntity<List<SystemSettingResponse>> findString(){
    	List<SystemSettingResponse> responses = settings.findString();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-integer")
    public ResponseEntity<List<SystemSettingResponse>> findInteger(){
    	List<SystemSettingResponse> responses = settings.findInteger();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-boolean")
    public ResponseEntity<List<SystemSettingResponse>> findBoolean(){
    	List<SystemSettingResponse> responses = settings.findBoolean();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-double")
    public ResponseEntity<List<SystemSettingResponse>> findDouble(){
    	List<SystemSettingResponse> responses = settings.findDouble();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-date")
    public ResponseEntity<List<SystemSettingResponse>> findDate(){
    	List<SystemSettingResponse> responses = settings.findDate();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-time")
    public ResponseEntity<List<SystemSettingResponse>> findTime(){
    	List<SystemSettingResponse> responses = settings.findTime();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-date-time")
    public ResponseEntity<List<SystemSettingResponse>> findDateTime(){
    	List<SystemSettingResponse> responses = settings.findDateTime();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/date-type-and-value")
    public ResponseEntity<List<SystemSettingResponse>> findByDataTypeAndValue(@RequestParam("dateType") SettingDataType dateType,@RequestParam("value") String value){
    	List<SystemSettingResponse> responses = settings.findByDataTypeAndValue(dateType, value);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-category-and-isVisible")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndIsVisibleTrue(@RequestParam("category") SystemSettingCategory category){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndIsVisibleTrue(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/editable-and-isVisible")
    public ResponseEntity<List<SystemSettingResponse>> findByEditableTrueAndIsVisibleTrue(){
    	List<SystemSettingResponse> responses = settings.findByEditableTrueAndIsVisibleTrue();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/setting-key")
    public ResponseEntity<List<SystemSettingResponse>> findBySettingKeyContainingIgnoreCase(@RequestParam("keyword") String keyword){
    	List<SystemSettingResponse> responses = settings.findBySettingKeyContainingIgnoreCase(keyword);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/setting-key-starstwith")
    public ResponseEntity<List<SystemSettingResponse>> findBySettingKeyStartingWith(@RequestParam("prefix") String prefix){
    	List<SystemSettingResponse> responses = settings.findBySettingKeyStartingWith(prefix);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/setting-key-endswith")
    public ResponseEntity<List<SystemSettingResponse>> findBySettingKeyEndingWith(@RequestParam("suffix") String suffix){
    	List<SystemSettingResponse> responses = settings.findBySettingKeyEndingWith(suffix);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/setting-key-contains")
    public ResponseEntity<List<SystemSettingResponse>> findBySettingKeyContaining(@RequestParam("substring") String substring){
    	List<SystemSettingResponse> responses = settings.findBySettingKeyContaining(substring);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/createdAt-between")
    public ResponseEntity<List<SystemSettingResponse>> findByCreatedAtBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
    	List<SystemSettingResponse> responses = settings.findByCreatedAtBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/updatedAt-after")
    public ResponseEntity<List<SystemSettingResponse>> findByUpdatedAtAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
    	List<SystemSettingResponse> responses = settings.findByUpdatedAtAfter(time);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/updateAt-before")
    public ResponseEntity<List<SystemSettingResponse>> findByUpdatedAtBefore(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
    	List<SystemSettingResponse> responses = settings.findByUpdatedAtBefore(time);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-order-by-asc")
    public ResponseEntity<List<SystemSettingResponse>> findAllByCategoryOrderBySettingKeyAsc(@RequestParam("category") SystemSettingCategory category){
    	List<SystemSettingResponse> responses = settings.findAllByCategoryOrderBySettingKeyAsc(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/data-type-order-by-desc")
    public ResponseEntity<List<SystemSettingResponse>> findAllByDataTypeOrderByUpdatedAtDesc(@RequestParam("dataType") SettingDataType dataType){
    	List<SystemSettingResponse> responses = settings.findAllByDataTypeOrderByUpdatedAtDesc(dataType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/data-type-in")
    public ResponseEntity<List<SystemSettingResponse>> findByDataTypeIn( @RequestParam("types") List<SettingDataType> types){
    	List<SystemSettingResponse> responses = settings.findByDataTypeIn(types);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/seach/data-type-custom")
    public ResponseEntity<List<SystemSettingResponse>> findByDataTypeCustom(@RequestParam("type") SettingDataType type){
    	List<SystemSettingResponse> responses = settings.findByDataTypeCustom(type);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-general")
    public ResponseEntity<List<SystemSettingResponse>> findGeneral(){
    	List<SystemSettingResponse> responses = settings.findGeneral();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-security")
    public ResponseEntity<List<SystemSettingResponse>> findSecurity(){
    	List<SystemSettingResponse> responses = settings.findSecurity();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-notifications")
    public ResponseEntity<List<SystemSettingResponse>> findNotifications(){
    	List<SystemSettingResponse> responses = settings.findNotifications();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-ui")
    public ResponseEntity<List<SystemSettingResponse>> findUi(){
    	List<SystemSettingResponse> responses = settings.findUi();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-performance")
    public ResponseEntity<List<SystemSettingResponse>> findPerformance(){
    	List<SystemSettingResponse> responses = settings.findPerformance();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-email")
    public ResponseEntity<List<SystemSettingResponse>> findEmail(){
    	List<SystemSettingResponse> responses = settings.findEmail();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-integrations")
    public ResponseEntity<List<SystemSettingResponse>> findIntegrations(){
    	List<SystemSettingResponse> responses = settings.findIntegrations();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-feature-flags")
    public ResponseEntity<List<SystemSettingResponse>> findFeatureFlags(){
    	List<SystemSettingResponse> responses = settings.findFeatureFlags();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-user-management")
    public ResponseEntity<List<SystemSettingResponse>> findUserManagement(){
    	List<SystemSettingResponse> responses = settings.findUserManagement();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/all-category")
    public ResponseEntity<List<SystemSettingResponse>> findAllByCategory(@RequestParam("category") SystemSettingCategory category){
    	List<SystemSettingResponse> responses = settings.findAllByCategory(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-by-category")
    public ResponseEntity<Boolean> existsByCategory(@RequestParam("category") SystemSettingCategory category){
    	Boolean responses = settings.existsByCategory(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/count-category")
    public ResponseEntity<Long> countByCategory(@RequestParam("category") SystemSettingCategory category){
    	Long responses = settings.countByCategory(category);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-and-data-type")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndDataType(@RequestParam("category") SystemSettingCategory category,@RequestParam("dataType") SettingDataType dataType){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndDataType(category, dataType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-by-category-and-data-type")
    public ResponseEntity<Boolean> existsByCategoryAndDataType(@RequestParam("category") SystemSettingCategory category,@RequestParam("dataType") SettingDataType dataType){
    	Boolean responses = settings.existsByCategoryAndDataType(category, dataType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-distinct-categories")
    public ResponseEntity<List<SystemSettingCategory>> findDistinctCategories(){
    	List<SystemSettingCategory> responses = settings.findDistinctCategories();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-and-editable")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndEditable(@RequestParam("category") SystemSettingCategory category,@RequestParam("editable") Boolean editable){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndEditable(category, editable);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-visible")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndIsVisible(@RequestParam("category") SystemSettingCategory category,@RequestParam("isVisible") Boolean isVisible){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndIsVisible(category, isVisible);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-and-value")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndValue(@RequestParam("category") SystemSettingCategory category,@RequestParam("value") String value){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndValue(category, value);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-category-in")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryIn(@RequestParam("categories") List<SystemSettingCategory> categories){
    	List<SystemSettingResponse> responses = settings.findByCategoryIn(categories);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-and-createAt-after")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndCreatedAtAfter(@RequestParam("category") SystemSettingCategory category,
    		@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndCreatedAtAfter(category, fromDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/category-and-update-range")
    public ResponseEntity<List<SystemSettingResponse>> findByCategoryAndUpdatedAtBetween(@RequestParam("category") SystemSettingCategory category,
    		@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
    	List<SystemSettingResponse> responses = settings.findByCategoryAndUpdatedAtBetween(category, start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/count-by-category-and-editable")
    public ResponseEntity<Long> countByCategoryAndEditable(@RequestParam("category") SystemSettingCategory category,@RequestParam("editable") Boolean editable){
    	Long responses = settings.countByCategoryAndEditable(category, editable);
    	return ResponseEntity.ok(responses);
    }
    
   
}
