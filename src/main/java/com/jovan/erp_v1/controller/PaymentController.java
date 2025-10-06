package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.master_card.request.MasterCardPGSPaymentRequest;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.service.IPaymentService;
import com.jovan.erp_v1.service.IUserService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
public class PaymentController {

	private final IPaymentService paymentService;
	private final IUserService userService;
	
	@PreAuthorize(RoleGroups.PAYMENT_FULL_ACCESS)
	@PostMapping("/create/new-payment")
	public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request){
		PaymentResponse response = paymentService.createPayment(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequest request){
		PaymentResponse response = paymentService.updatePayment(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePayment(@PathVariable Long id){
		paymentService.deletePayment(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/{id}")
	public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id){
		PaymentResponse response = paymentService.getPaymentById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<PaymentResponse>> getAllPayments(){
		List<PaymentResponse> responses = paymentService.getAllPayments();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/buyer/{buyerId}")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByBuyer(@PathVariable Long buyerId){
		List<PaymentResponse> response = paymentService.getPaymentsByBuyer(buyerId);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/by-status")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@RequestParam("status") PaymentStatus status){
		List<PaymentResponse> responses = paymentService.getPaymentsByStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/by-method")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByMethod(@RequestParam("method") PaymentMethod method){
		List<PaymentResponse> responses = paymentService.getPaymentsByMethod(method);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/by-amount")
	public ResponseEntity<List<PaymentResponse>> findByAmount(@RequestParam("amount") BigDecimal amount){
		List<PaymentResponse> responses = paymentService.findByAmount(amount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/amount-greater-than")
	public ResponseEntity<List<PaymentResponse>> findByAmountGreaterThan(@RequestParam("amount") BigDecimal amount){
		List<PaymentResponse> responses = paymentService.findByAmountGreaterThan(amount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/amount-less-than")
	public ResponseEntity<List<PaymentResponse>> findByAmountLessThan(@RequestParam("amount") BigDecimal amount){
		List<PaymentResponse> responses = paymentService.findByAmountLessThan(amount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment-date")
	public ResponseEntity<List<PaymentResponse>> findByPaymentDate(@RequestParam("paymentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate){
		List<PaymentResponse> responses = paymentService.findByPaymentDate(paymentDate);
	return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/buyer-company-name")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_CompanyNameContainingIgnoreCase(@RequestParam("buyerCompanyName") String buyerCompanyName){
		List<PaymentResponse> responses = paymentService.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/pib")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_PibContainingIgnoreCase(@RequestParam("pib")  String pib){
		List<PaymentResponse> responses = paymentService.findByBuyer_PibContainingIgnoreCase(pib);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/buyer-address")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_AddressContainingIgnoreCase(@RequestParam("buyerAddress")  String buyerAddress){
		List<PaymentResponse> responses = paymentService.findByBuyer_AddressContainingIgnoreCase(buyerAddress);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/buyer-email")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_EmailContainingIgnoreCase(@RequestParam("buyerEmail")  String buyerEmail){
		List<PaymentResponse> responses = paymentService.findByBuyer_EmailContainingIgnoreCase(buyerEmail);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/buyer-phone-number")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_PhoneNumber(@RequestParam("buyerPhoneNumber")  String buyerPhoneNumber){
		List<PaymentResponse> responses = paymentService.findByBuyer_PhoneNumber(buyerPhoneNumber);
		return ResponseEntity.ok(responses);
	}
		
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/sales/{relatedSalesId}")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_Id(@PathVariable Long relatedSalesId){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_Id(relatedSalesId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/by-sale-created-at")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_CreatedAt(@RequestParam("createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_CreatedAt(createdAt);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/by-sale-total-price-greter-than")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_TotalPriceGreaterThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_TotalPriceGreaterThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/by-sale-total-price-less-than")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_TotalPriceLessThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_TotalPriceLessThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/by-sale-total-price")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_TotalPrice(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_TotalPrice(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/sales-description")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_SalesDescriptionContainingIgnoreCase(@RequestParam("salesDescription")  String salesDescription){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_SalesDescriptionContainingIgnoreCase(salesDescription);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/buyer/{buyerId}/status")
	public ResponseEntity<List<PaymentResponse>> findByBuyer_IdAndStatus(@PathVariable Long buyerId, @RequestParam("status")  PaymentStatus status){
		List<PaymentResponse> responses = paymentService.findByBuyer_IdAndStatus(buyerId, status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/filter-by-date-and-method")
	public ResponseEntity<List<PaymentResponse>> findByPaymentDateBetweenAndMethod(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, 
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end, @RequestParam("method")  PaymentMethod method){
		List<PaymentResponse> responses = paymentService.findByPaymentDateBetweenAndMethod(start, end, method);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/buyer/{buyerId}/search-description")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(@RequestParam("description") String description,@PathVariable Long buyerId){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(description, buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payments/count")
	public ResponseEntity<Long> countByBuyer_Id(@RequestParam("buyerId") Long buyerId){
		Long response = paymentService.countByBuyer_Id(buyerId);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PAYMENT_READ_ACCESS)
	@GetMapping("/payment/sales/{buyerId}")
	public ResponseEntity<List<PaymentResponse>> findByRelatedSales_Buyer_Id(@PathVariable Long buyerId){
		List<PaymentResponse> responses = paymentService.findByRelatedSales_Buyer_Id(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	//metoda za placanje master_card karticom
	
	@PostMapping("/master-card")
	public ResponseEntity<TransactionResponse> payWithMasterCard(@Valid @RequestBody MasterCardPGSPaymentRequest req, Principal principal) throws Exception{
		User user = loadAuthenticatedUser(principal);
		TransactionResponse resp = paymentService.processMasterCardPayment(req.getMerchantId(), req.getAmount(), req.getCurrency(), req.getPaymentToken(), user);
        return ResponseEntity.ok(resp);
	}
	
	//method for user authentication
	private User loadAuthenticatedUser(Principal principal) throws AccessDeniedException {
		//for using SecurityContextHolder, if principal isn't forward
		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		User user = userService.findByUsername(username)
		        .orElseThrow(() -> new UserNotFoundException("User not authenticated."));*/
        if (principal == null) {
            throw new AccessDeniedException("User not authenticated.");
        }
        String username = principal.getName();
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
}
