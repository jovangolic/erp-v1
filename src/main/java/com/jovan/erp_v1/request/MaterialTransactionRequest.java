package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;

public record MaterialTransactionRequest(
        Long id,
        @NotNull Long materialId,
        @NotNull(message = "Količina ne sme biti null.") @Digits(integer = 10, fraction = 3, message = "Količina može imati do 10 cifara i do 3 decimale.") @PositiveOrZero(message = "Količina ne sme biti negativna.") BigDecimal quantity,
        @NotNull TransactionType type,
        @NotNull @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
        @NotNull Long vendorId,
        @NotNull String documentReference,
        @NotNull String notes,
        @NotNull MaterialTransactionStatus status,
        @NotNull Long createdByUserId) {
}
