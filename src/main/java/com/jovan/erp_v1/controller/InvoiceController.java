package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
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
	private final BuyerRepository buyerRepository;
	
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
		List<InvoiceResponse> responses = invoiceService.findByBuyerIdAndStatus(buyer.getId(), status);
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
	
	//nove metode
	
	@GetMapping("/buyer-company-name")
	public ResponseEntity<List<InvoiceResponse>> findByBuyerCompanyNameContainingIgnoreCase(@RequestParam("companyName") String companyName){
		List<InvoiceResponse> responses = invoiceService.findByBuyerCompanyNameContainingIgnoreCase(companyName);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/buyer-pib")
	public ResponseEntity<List<InvoiceResponse>> findByBuyerPib(@RequestParam("pib") String pib){
		List<InvoiceResponse> responses = invoiceService.findByBuyerPib(pib);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/buyer-email")
	public ResponseEntity<List<InvoiceResponse>> findByBuyerEmailContainingIgnoreCase(@RequestParam("email") String email){
		List<InvoiceResponse> responses = invoiceService.findByBuyerEmailContainingIgnoreCase(email);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/buyer-phone-number")
	public ResponseEntity<List<InvoiceResponse>> findByBuyerPhoneNumber(@RequestParam("phoneNumber") String phoneNumber){
		List<InvoiceResponse> responses = invoiceService.findByBuyerPhoneNumber(phoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-note")
	public ResponseEntity<List<InvoiceResponse>> findByNoteContainingIgnoreCase(@RequestParam("note") String note){
		List<InvoiceResponse> responses = invoiceService.findByNoteContainingIgnoreCase(note);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/relatedSales/{relatedSalesId}")
	public ResponseEntity<List<InvoiceResponse>> findByRelatedSales_Id(@PathVariable Long relatedSalesId){
		List<InvoiceResponse> responses = invoiceService.findByRelatedSales_Id(relatedSalesId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/relatedSales-totalPrice")
	public ResponseEntity<List<InvoiceResponse>> findByRelatedSales_TotalPrice(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<InvoiceResponse> responses = invoiceService.findByRelatedSales_TotalPrice(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/relatedSales-totalPrice-greate-than")
	public ResponseEntity<List<InvoiceResponse>> findByRelatedSales_TotalPriceGreaterThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<InvoiceResponse> responses = invoiceService.findByRelatedSales_TotalPriceGreaterThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/relatedSales-totalPrice-less-than")
	public ResponseEntity<List<InvoiceResponse>> findByRelatedSales_TotalPriceLessThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<InvoiceResponse> responses = invoiceService.findByRelatedSales_TotalPriceLessThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/payment-amount")
	public ResponseEntity<List<InvoiceResponse>> findByPayment_Amount(@RequestParam("amount") BigDecimal amount){
		List<InvoiceResponse> responses = invoiceService.findByPayment_Amount(amount);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment-date")
	public ResponseEntity<List<InvoiceResponse>> findByPayment_PaymentDate(@RequestParam("paymentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate){
		List<InvoiceResponse> responses = invoiceService.findByPayment_PaymentDate(paymentDate);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment-date-range")
	public ResponseEntity<List<InvoiceResponse>> findByPayment_PaymentDateBetween(@RequestParam("paymentDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDateStart,
            @RequestParam("paymentDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDateEnd){
		List<InvoiceResponse> responses = invoiceService.findByPayment_PaymentDateBetween(paymentDateStart, paymentDateEnd);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment-method")
	public ResponseEntity<List<InvoiceResponse>> findByPayment_Method(@RequestParam("method") PaymentMethod method){
		List<InvoiceResponse> responses = invoiceService.findByPayment_Method(method);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment-reference-number")
	public ResponseEntity<List<InvoiceResponse>> findByPayment_ReferenceNumberContainingIgnoreCase(@RequestParam("referenceNumber") String referenceNumber){
		List<InvoiceResponse> responses = invoiceService.findByPayment_ReferenceNumberContainingIgnoreCase(referenceNumber);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder/{salesOrderId}")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_Id(@PathVariable Long salesOrderId){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_Id(salesOrderId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-date")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_OrderDate(@RequestParam("orderDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDate){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_OrderDate(orderDate);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-date-range")
	public ResponseEntity<List<InvoiceResponse>> indBySalesOrder_OrderDateBetween(@RequestParam("orderDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDateStart, @RequestParam("orderDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDateEnd){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_OrderDateBetween(orderDateStart, orderDateEnd);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-total-amount")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_TotalAmount(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_TotalAmount(totalAmount);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-total-amount-greater-than")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_TotalAmountGreaterThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_TotalAmountGreaterThan(totalAmount);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-total-amount-less-than")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_TotalAmountLessThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_TotalAmountLessThan(totalAmount);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-status")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_Status(@RequestParam("status") OrderStatus status){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_Status(status);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/salesOrder-note")
	public ResponseEntity<List<InvoiceResponse>> findBySalesOrder_NoteContainingIgnoreCase(@RequestParam("note") String note){
		List<InvoiceResponse> responses = invoiceService.findBySalesOrder_NoteContainingIgnoreCase(note);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/createdBy/{createdById}")
	public ResponseEntity<List<InvoiceResponse>> findByCreatedBy_Id(@PathVariable Long createdById){
		List<InvoiceResponse> responses = invoiceService.findByCreatedBy_Id(createdById);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/createdBy-email")
	public ResponseEntity<List<InvoiceResponse>>  findByCreatedBy_EmailContainingIgnoreCase(@RequestParam("createdByEmail") String createdByEmail){
		List<InvoiceResponse> responses = invoiceService.findByCreatedBy_EmailContainingIgnoreCase(createdByEmail);
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/createdBy-first-last-name")
	public ResponseEntity<List<InvoiceResponse>>  findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(@RequestParam("createdByFirstName") String createdByFirstName,@RequestParam("createdByLastName") String createdByLastName){
		List<InvoiceResponse> responses = invoiceService.findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(createdByFirstName, createdByLastName);
				return ResponseEntity.ok(responses);
	}
	
	
}
