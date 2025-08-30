package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.Transaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class TransactionMapper extends AbstractMapper<TransactionRequest> {

	public Transaction toEntity(TransactionRequest request, Account sourceAcc, Account targetAcc, User user) {
		Objects.requireNonNull(request, "TransactionRequest must not be null");
		Objects.requireNonNull(sourceAcc, "SourceAccount must not be null");
		Objects.requireNonNull(targetAcc, "TargetAccount must not be null");
		Objects.requireNonNull(user, "User must not be null");
		validateIdForCreate(request, TransactionRequest::id);
		Transaction t = new Transaction();
		t.setId(request.id());
		t.setAmount(request.amount());
		t.setTransactionType(request.transactionType());
		t.setSourceAccount(sourceAcc);
		t.setTargetAccount(targetAcc);
		t.setUser(user);
		return t;
	}
	
	public Transaction toEntityUpdate(Transaction t, TransactionRequest request, Account sourceAcc, Account targetAcc, User user) {
		Objects.requireNonNull(t, "Transaction must not be null");
		Objects.requireNonNull(request, "TransactionRequest must not be null");
		Objects.requireNonNull(sourceAcc, "SourceAccount must not be null");
		Objects.requireNonNull(targetAcc, "TargetAccount must not be null");
		Objects.requireNonNull(user, "User must not be null");
		validateIdForUpdate(request, TransactionRequest::id);
		t.setAmount(request.amount());
		t.setTransactionType(request.transactionType());
		t.setSourceAccount(sourceAcc);
		t.setTargetAccount(targetAcc);
		t.setUser(user);
		return t;
	}
	
	public TransactionResponse toResponse(Transaction t) {
		Objects.requireNonNull(t, "Transaction must not be null");
		return new TransactionResponse(t);
	}
	
	public List<TransactionResponse> toResponseList(List<Transaction> t){
		if(t == null || t.isEmpty()) {
			return Collections.emptyList();
		}
		return t.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
