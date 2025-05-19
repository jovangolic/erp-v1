package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GoodsRequest(
		Long id,
		@NotBlank(message = "Naziv ne sme biti prazan")
	    String name,

	    @NotNull(message = "Tip dobavljača je obavezan")
	    SupplierType supplierType,

	    @NotNull(message = "Tip skladištenja je obavezan")
	    StorageType storageType,

	    @NotNull(message = "Tip robe je obavezan")
	    GoodsType goodsType,
	    
	    Long storageId,

	    @NotBlank(message = "Naziv skladišta ne sme biti prazan")
	    String storageName,

	    @NotNull(message = "ID nabavke je obavezan")
	    Long supplyId
		) {
}
