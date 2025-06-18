package com.jovan.erp_v1.request;

import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRequest(
		Long id,

		@NotBlank(message = "Naziv proizvoda je obavezan") String name,

		@NotBlank(message = "Jedinica mere je obavezna") UnitMeasure unitMeasure,

		@NotNull(message = "Tip dobavljača je obavezan") SupplierType supplierType,

		@NotNull(message = "Tip skladišta je obavezan") StorageType storageType,

		@NotNull(message = "Tip robe je obavezan") GoodsType goodsType,

		@NotNull(message = "ID skladišta je obavezan") Long storageId,

		@NotNull(message = "Trenutna količina je obavezna") @PositiveOrZero(message = "Količina mora biti 0 ili veća") Double currentQuantity,
		List<@Valid BarCodeRequest> barCodes) {

}
