package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


public record JournalItemRequest(
                Long id,
                @NotNull Long accountId,
                @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal debit,
                @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal credit,
                Long journalEntryId) {

        @AssertTrue(message = "Debit or Credit must be greater than 0")
        public boolean isDebitOrCreditValid() {
                return debit.compareTo(BigDecimal.ZERO) > 0 || credit.compareTo(BigDecimal.ZERO) > 0;
        }
}
