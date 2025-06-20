package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.LedgerType;
import com.jovan.erp_v1.model.LedgerEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryResponse {

    private Long id;
    private LocalDateTime entryDate;
    private BigDecimal amount;
    private String description;
    private AccountResponse response;
    private LedgerType type;

    public LedgerEntryResponse(LedgerEntry entry) {
        this.id = entry.getId();
        this.entryDate = entry.getEntryDate();
        this.amount = entry.getAmount();
        this.description = entry.getDescription();
        this.response = new AccountResponse(entry.getAccount());
        this.type = entry.getType();
    }
}
