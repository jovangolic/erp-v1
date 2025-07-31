package com.jovan.erp_v1.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;
import com.jovan.erp_v1.service.IOutboundDeliveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/outboundDeliveries")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class OutboundDeliveryController {

    private final IOutboundDeliveryService outboundDeliveryService;

    @PostMapping("/create/new-outboundDelivery")
    public ResponseEntity<OutboundDeliveryResponse> create(@Valid @RequestBody OutboundDeliveryRequest request) {
        OutboundDeliveryResponse response = outboundDeliveryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OutboundDeliveryResponse> update(@PathVariable Long id,
            @Valid @RequestBody OutboundDeliveryRequest request) {
        OutboundDeliveryResponse response = outboundDeliveryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        outboundDeliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<OutboundDeliveryResponse> findOne(@PathVariable Long id) {
        OutboundDeliveryResponse response = outboundDeliveryService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OutboundDeliveryResponse>> findAll() {
        List<OutboundDeliveryResponse> responses = outboundDeliveryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByStatus(@RequestParam("status") DeliveryStatus status) {
        List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyerId(@PathVariable Long buyerId) {
        List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyerId(buyerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByDeliveryDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByDeliveryDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<OutboundDeliveryResponse>> createAll(
            @RequestBody List<OutboundDeliveryRequest> requests) {
        List<OutboundDeliveryResponse> responses = outboundDeliveryService.createAll(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        outboundDeliveryService.deleteAllByIds(ids);
        return ResponseEntity.noContent().build();
    }
    
    //nove metode
    
    @GetMapping("/search/buyer-company-name")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyer_CompanyName(@RequestParam("companyName") String companyName){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyer_CompanyName(companyName);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/buyer-address")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyer_Address(@RequestParam("address") String address){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyer_Address(address);
    	 return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/buyer-contact-person")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyer_ContactPerson(@RequestParam("contactPerson") String contactPerson){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyer_ContactPerson(contactPerson);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/buyer-email")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyer_EmailLikeIgnoreCase(@RequestParam("email") String email){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyer_EmailLikeIgnoreCase(email);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/buyer-phone-number")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByBuyer_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByBuyer_PhoneNumberLikeIgnoreCase(phoneNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/delivery-date")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByDeliveryDate(@RequestParam("deliveryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByDeliveryDate(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/delivery-date-after")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByDeliveryDateAfter(@RequestParam("deliveryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByDeliveryDateAfter(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/delivery-date-before")
    public ResponseEntity<List<OutboundDeliveryResponse>> findByDeliveryDateBefore(@RequestParam("deliveryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<OutboundDeliveryResponse> responses = outboundDeliveryService.findByDeliveryDateBefore(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
}
