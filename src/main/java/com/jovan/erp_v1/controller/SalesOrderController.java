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
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;
import com.jovan.erp_v1.service.ISalesOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/salesOrders")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class SalesOrderController {

	private final ISalesOrder salesOrder;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-sales-order")
	public ResponseEntity<SalesOrderResponse> create(@Valid @RequestBody SalesOrderRequest request){
		SalesOrderResponse response = salesOrder.createOrder(request);
		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<SalesOrderResponse> update(@PathVariable Long id, @Valid @RequestBody SalesOrderRequest request){
		SalesOrderResponse response = salesOrder.updateOrder(id, request);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/generate-order-number")
    public ResponseEntity<String> generateOrderNumber() {
        String orderNumber = salesOrder.generateOrderNumber();
        return ResponseEntity.ok(orderNumber);
    }
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id){
		salesOrder.deleteSales(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<SalesOrderResponse> getSalesOrderById(@PathVariable Long id){
		SalesOrderResponse response = salesOrder.getOrderById(id);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/get-all")
	public ResponseEntity<List<SalesOrderResponse>> getAllSalesOrders(){
		List<SalesOrderResponse> responses = salesOrder.getAllOrders();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode buyer
	@GetMapping("/buyer/{buyerId}")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_Id(@PathVariable Long buyerId){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_Id(buyerId);
		if (responses.isEmpty()) {
		    return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/byCompanyName")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_CompanyNameContainingIgnoreCase(@RequestParam("companyName") String companyName){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_CompanyNameContainingIgnoreCase(companyName);
		return ResponseEntity.ok(responses);		
	}
	
	@GetMapping("/search/byPib")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_PibContainingIgnoreCase(@RequestParam("pib") String pib){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_PibContainingIgnoreCase(pib);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byAddress")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_Address(@RequestParam("address") String address){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_Address(address);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byContactPerson")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_ContactPerson(@RequestParam("contactPerson") String contactPerson){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_ContactPerson(contactPerson);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byEmail")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_EmailContainingIgnoreCase(@RequestParam("email") String email){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_EmailContainingIgnoreCase(email);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byPhoneNumber")
	public ResponseEntity<List<SalesOrderResponse>> findByBuyer_PhoneNumberContainingIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<SalesOrderResponse> responses = salesOrder.findByBuyer_PhoneNumberContainingIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byInvoiceNumber")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_InvoiceNumberContainingIgnoreCase(@RequestParam("invoiceNumber") String invoiceNumber){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_InvoiceNumberContainingIgnoreCase(invoiceNumber);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/byTotalAmount")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_TotalAmount(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_TotalAmount(totalAmount);
		return ResponseEntity.ok(responses);	
	}
	
	
	@GetMapping("/search/totalAmount/greater-than")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_TotalAmountGreaterThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_TotalAmountGreaterThan(totalAmount);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/totalAmount/less-than")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_TotalAmountLessThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_TotalAmountLessThan(totalAmount);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/totalAmount/between")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_TotalAmountBetween(
	        @RequestParam("min") BigDecimal min,
	        @RequestParam("max") BigDecimal max) {
	    List<SalesOrderResponse> responses = salesOrder.findByInvoice_TotalAmountBetween(min, max);
	    return ResponseEntity.ok(responses);
	}
	
	//issue_date
	
	@GetMapping("/search/issueDate")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_IssueDate(@RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime issueDate){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_IssueDate(issueDate);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/issue-date-after")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_IssueDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_IssueDateAfter(date);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/issue-date-before")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_IssueDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_IssueDateBefore(date);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/issue-date-range")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_IssueDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_IssueDateBetween(start, end);
		return ResponseEntity.ok(responses);	
	}
	
	//due_date
	
	@GetMapping("/search/due-date")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_DueDate(@RequestParam("dueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_DueDate(dueDate);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/due-date-after")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_DueDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_DueDateAfter(date);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/due-date-before")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_DueDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_DueDateBefore(date);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/due-date-range")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_DueDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, 
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_DueDateBetween(start, end);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice-note")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_NoteContainingIgnoreCase(@RequestParam("note") String note){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_NoteContainingIgnoreCase(note);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/buyer/{buyerId}")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Buyer_Id(@PathVariable Long buyerId){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Buyer_Id(buyerId);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/relatedSales/{relatedSalesId}")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_RelatedSales_Id(@PathVariable Long relatedSalesId){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_RelatedSales_Id(relatedSalesId);
		return ResponseEntity.ok(responses);	
	}
	
	//payment
	
	@GetMapping("/search/invoice/payment/{paymentId}")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_Id(@PathVariable Long paymentId){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_Id(paymentId);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment/amount")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_Amount(@RequestParam("amount") BigDecimal amount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_Amount(amount);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment/amount-greater-than")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_AmountGreaterThan(@RequestParam("amount") BigDecimal amount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_AmountGreaterThan(amount);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment/amount-less-than")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_AmountLessThan(@RequestParam("amount") BigDecimal amount){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_AmountLessThan(amount);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment-method")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_Method(@RequestParam("method") PaymentMethod method){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_Method(method);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment-status")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_Status(@RequestParam("status") PaymentStatus status){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_Status(status);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment-reference-number")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(@RequestParam("referenceNumber") String referenceNumber){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(referenceNumber);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment-date")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_PaymentDate(@RequestParam("paymentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_PaymentDate(paymentDate);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/payment-date-range")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_Payment_PaymentDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, 
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_Payment_PaymentDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);	
	}
	
	//user
	
	@GetMapping("/search/invoice/createdBy/{userId}")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_CreatedBy_Id(@PathVariable Long userId){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_CreatedBy_Id(userId);
		if (responses.isEmpty()) {
		    return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/createdBy-email")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_CreatedBy_EmailLikeIgnoreCase(@RequestParam("email") String email){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_CreatedBy_EmailLikeIgnoreCase(email);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/createdBy-address")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_CreatedBy_Address(@RequestParam("address") String address){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_CreatedBy_Address(address);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/createdBy-phone-number")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/search/invoice/createdBy-fullName")
	public ResponseEntity<List<SalesOrderResponse>> findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName){
		List<SalesOrderResponse> responses = salesOrder.findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(responses);	
	}
}
