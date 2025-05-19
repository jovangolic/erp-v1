package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.GoodsType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;

public record BarCodeRequest(
	    @Null(message = "Id mora biti null za kreiranje")
	    Long id,

	    @NotBlank(message = "Code je obavezan")
	    String code,

	    // Nema validacije jer se postavlja u backendu ako je null
	    LocalDateTime scannedAt,

	    String scannedBy, // PROMENA: String umesto LocalDateTime

	    @NotNull(message = "GoodsId je obavezan")
	    Long goodsId
	) {}
