package com.jovan.erp_v1.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;


public record BarCodeRequest(
	    @Null(message = "Id mora biti null za kreiranje")
	    Long id,
	    @NotBlank(message = "Code je obavezan")
	    String code,
	    LocalDateTime scannedAt,
	    Long scannedById, 
	    @NotNull(message = "GoodsId je obavezan")
	    Long goodsId
	) {}
