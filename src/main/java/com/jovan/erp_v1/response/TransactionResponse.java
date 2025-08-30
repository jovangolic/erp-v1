package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

	private Long id;
	private BigDecimal amount;
	private LocalDateTime transactionDate;
	private TransactionType transactionType;
	private SourceAccountResponse sourceAccountResponse;
	private TargetAccountResponse targetAccountResponse;
	private UserResponse userResponse;
	
	public TransactionResponse(Transaction t) {
		this.id = t.getId();
		this.amount = t.getAmount();
		this.transactionDate = t.getTransactionDate();
		this.transactionType = t.getTransactionType();
		this.sourceAccountResponse = t.getSourceAccount() != null ? new SourceAccountResponse(t.getSourceAccount()) : null;
		this.targetAccountResponse = t.getTargetAccount() != null ? new TargetAccountResponse(t.getTargetAccount()) : null;
		this.userResponse = t.getUser() != null ? new UserResponse(t.getUser()) : null;
		
		// Ako postoji target account, setovanje targetAccountNumber unutar targetAccountResponse
		if(this.targetAccountResponse != null && t.getTargetAccount() != null) {
			String targetAccountNumber = t.getTargetAccount().getAccountNumber();
			this.targetAccountResponse.setAccountNumber(targetAccountNumber); // Setovanje broja racuna u targetAccountResponse objektu	
		}
	}
}
