package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record MaterialRequirementRequest(
                Long id,
                @NotNull(message = "ID proizvodnog naloga ne sme biti null") Long productionOrderId,

                @NotNull(message = "ID materijala ne sme biti null") Long materialId,

                @NotNull(message = "Obavezna količina ne sme biti null") @DecimalMin(value = "0.0", inclusive = false, message = "Obavezna količina mora biti veća od nule") BigDecimal requiredQuantity,

                @NotNull(message = "Dostupna količina ne sme biti null") @DecimalMin(value = "0.0", inclusive = true, message = "Dostupna količina ne sme biti negativna") BigDecimal availableQuantity,

                @NotNull(message = "Datum zahteva ne sme biti null") @FutureOrPresent(message = "Datum zahteva ne sme biti u prošlosti") LocalDate requirementDate,

                @NotNull(message = "Status zahteva ne sme biti null") MaterialRequestStatus status,
                @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal shortage) {
}
