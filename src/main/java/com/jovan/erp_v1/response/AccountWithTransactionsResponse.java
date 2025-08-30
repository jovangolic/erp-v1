package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Ovo je poseban objekat koji vraca sve transakcije povezane sa odredjenim Account entitetom.
 *Ovo je takodje detailed verzija -> sa transakcijama
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithTransactionsResponse {

	private Long id;
    private String accountNumber;
    private String accountName;
    private AccountType type;
    private BigDecimal balance;
    private List<TransactionResponse> sourceTransactions;
    private List<TransactionResponse> targetTransactions;

    public AccountWithTransactionsResponse(Account acc) {
        this.id = acc.getId();
        this.accountNumber = acc.getAccountNumber();
        this.accountName = acc.getAccountName();
        this.type = acc.getType();
        this.balance = acc.getBalance();
        this.sourceTransactions = convertToTransactionResponse(acc.getSourceTransactions());
        this.targetTransactions = convertToTransactionResponse(acc.getTargetTransactions());
    }

    private List<TransactionResponse> convertToTransactionResponse(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }
}
