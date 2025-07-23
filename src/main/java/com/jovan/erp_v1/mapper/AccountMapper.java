package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class AccountMapper extends AbstractMapper<AccountRequest> {

    public Account toEntity(AccountRequest request) {
        Objects.requireNonNull(request,"AccountRequest must not be null");
        validateIdForCreate(request, AccountRequest::id);
        return buildAccountFromRequest(new Account(), request);
    }

    public Account updateEntity(Account acc, AccountRequest request) {
    	Objects.requireNonNull(request,"AccountRequest must not be null");
    	Objects.requireNonNull(acc,"Account must not be null");
    	validateIdForUpdate(request, AccountRequest::id);
    	return buildAccountFromRequest(acc, request);
    }
    
    private Account buildAccountFromRequest(Account acc, AccountRequest request) {
    	acc.setAccountName(request.accountName());
    	acc.setAccountNumber(request.accountNumber());
    	acc.setType(request.type());
    	acc.setBalance(request.balance());
    	return acc;
    }

    public AccountResponse toResponse(Account acc) {
    	Objects.requireNonNull(acc, "Account must not be null");
        return new AccountResponse(acc);
    }

    public List<AccountResponse> toResponseList(List<Account> accs) {
    	if(accs == null || accs.isEmpty()) {
    		return Collections.emptyList();
    	}
        return accs.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
