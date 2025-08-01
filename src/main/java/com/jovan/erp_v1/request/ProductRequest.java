package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
		Long id,

		@NotBlank(message = "Naziv proizvoda je obavezan") String name,

		@NotBlank(message = "Jedinica mere je obavezna") UnitMeasure unitMeasure,

		@NotNull(message = "Tip dobavljača je obavezan") SupplierType supplierType,

		@NotNull(message = "Tip skladišta je obavezan") StorageType storageType,

		@NotNull(message = "Tip robe je obavezan") GoodsType goodsType,

		@NotNull(message = "ID skladišta je obavezan") Long storageId,

		@NotNull(message = "ID nabavke je obavezan") Long supplyId,

	    @NotNull(message = "ID police je obavezan") Long shelfId,
		@NotNull(message = "Ukupna cena je obavezna")
		@DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti veća od nule")
		@Digits(integer = 10, fraction = 2, message = "Maksimalno 10 cifara pre i 2 posle decimalne tačke") BigDecimal currentQuantity,
		List<@Valid BarCodeRequest> barCodes) {

}
