package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountName;
    private AccountType type;
    private BigDecimal balance;
    private AccountStatus status;
    private Boolean confirmed;

    public AccountResponse(Account acc) {
        this.id = acc.getId();
        this.accountNumber = acc.getAccountNumber();
        this.accountName = acc.getAccountName();
        this.type = acc.getType();
        this.balance = acc.getBalance();
        this.status = acc.getStatus();
        this.confirmed = acc.getConfirmed();
    }
    
}
