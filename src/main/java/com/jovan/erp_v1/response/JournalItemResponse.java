package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.JournalItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalItemResponse {

    private Long id;
    private AccountResponse response;
    private BigDecimal debit;
    private BigDecimal credit;
    private JournalForEntryItemResponse journalForEntryItemResponse;

    public JournalItemResponse(JournalItem item) {
        this.id = item.getId();
        this.response = new AccountResponse(item.getAccount());
        this.debit = item.getDebit();
        this.credit = item.getCredit();
        this.journalForEntryItemResponse = item.getJournalEntry() != null ? new JournalForEntryItemResponse(item.getJournalEntry()) : null;
    }
}
