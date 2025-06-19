package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    public Account toEntity(AccountRequest request) {
        Account ac = new Account();
        if (request.id() != null) {
            ac.setId(request.id());
        }
        ac.setAccountNumber(request.accountNumber());
        ac.setAccountName(request.accountName());
        ac.setType(request.type());
        ac.setBalance(request.balance());
        return ac;
    }

    public void updateEntity(Account acc, AccountRequest request) {
        acc.setAccountName(request.accountName());
        acc.setAccountNumber(request.accountNumber());
        acc.setType(request.type());
        acc.setBalance(request.balance());
    }

    public AccountResponse toResponse(Account acc) {
        return new AccountResponse(acc);
    }

    public List<AccountResponse> toResponseList(List<Account> accs) {
        return accs.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
