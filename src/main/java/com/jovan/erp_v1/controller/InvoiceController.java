package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.mapper.InvoiceMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;
import com.jovan.erp_v1.service.IInvoiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class InvoiceController {

	private final IInvoiceService invoiceService;
	private final InvoiceMapper invoiceMapper;
	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	private final PaymentRepository paymentRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-invoice")
	public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request){
		InvoiceResponse response = invoiceService.createInvoice(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{invoiceId}")
	public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long invoideId, @Valid @RequestBody InvoiceRequest request){
		InvoiceResponse updated = invoiceService.updateInvoice(invoideId, request);
		return ResponseEntity.ok(updated);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{invoiceId}")
	public ResponseEntity<Void> deleteInvoice(@PathVariable Long invoiceId){
		invoiceService.deleteInvoice(invoiceId);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/invoice/{id}")
	public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id){
		InvoiceResponse response = invoiceService.getInvoiceById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-invoices")
	public ResponseEntity<List<InvoiceResponse>> getAllInvoices(){
		List<InvoiceResponse> responses = invoiceService.getAllInvoices();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice-status")
	public ResponseEntity<List<InvoiceResponse>> getByInvoiceStatus(
	        @RequestParam("status") InvoiceStatus status) {
	    List<InvoiceResponse> responses = invoiceService.findByStatus(status);
	    return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/invoice/buyer-status")
	public ResponseEntity<List<InvoiceResponse>> getByBuyerAndStatus(@RequestParam("buyerId") Long buyerId,@RequestParam("status") InvoiceStatus status){
		Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + buyerId));
		List<InvoiceResponse> responses = invoiceService.findByBuyerIdAndStatus(buyerId, status);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/total-amount")
	public ResponseEntity<List<InvoiceResponse>> getByTotalAmount(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<InvoiceResponse> responses = invoiceService.findByTotalAmount(totalAmount);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/buyer/{buyerId}")
	public ResponseEntity<List<InvoiceResponse>> getByBuyerId(@PathVariable Long buyerId){
		List<InvoiceResponse> responses = invoiceService.findByBuyerId(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/sales/{salesId}")
	public ResponseEntity<List<InvoiceResponse>> getBySalesId(@PathVariable Long salesId){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_Id(salesId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/payment/{paymentId}")
	public ResponseEntity<List<InvoiceResponse>> getByPaymentId(@PathVariable Long paymentId){
		List<InvoiceResponse> responses = invoiceService.findByPayment_Id(paymentId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/issue-date-between")
	public ResponseEntity<List<InvoiceResponse>> getByIssueDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<InvoiceResponse> responses = invoiceService.findByIssueDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/due-date-before")
	public ResponseEntity<List<InvoiceResponse>> getByDueDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<InvoiceResponse> responses = invoiceService.findByDueDateBefore(date);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/search-fragment/{fragment}")
	public ResponseEntity<List<InvoiceResponse>> searchByInvoiceNumberFragment(@PathVariable String fragment){
		List<InvoiceResponse> responses = invoiceService.findByInvoiceNumberContainingIgnoreCase(fragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/invoice/exists")
	public ResponseEntity<Boolean> existsByInvoiceNumber(@RequestParam("invoiceNumber") String invoiceNumber){
		Boolean ifExists = invoiceService.existsByInvoiceNumber(invoiceNumber);
		return ResponseEntity.ok(ifExists);
	}
	
	@GetMapping("/invoice/sorted-buyer/{buyerId}")
	public ResponseEntity<List<InvoiceResponse>> getInvoicesByBuyerSortedByIssueDate(@PathVariable Long buyerId){
		List<InvoiceResponse> responses = invoiceService.findInvoicesByBuyerSortedByIssueDate(buyerId);
		return ResponseEntity.ok(responses);
	}
	
}
