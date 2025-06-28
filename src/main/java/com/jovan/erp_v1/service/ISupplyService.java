package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.SupplyResponse;

public interface ISupplyService {

	SupplyResponse createSupply(SupplyRequest request);

	SupplyResponse updateSupply(Long id, SupplyRequest request);

	void deleteSupply(Long id);

	SupplyResponse getBySupplyId(Long supplyId);

	List<SupplyResponse> getAllSupply();

	List<SupplyResponse> getByStorage(Storage storage);

	List<SupplyResponse> getBySuppliesByGoodsName(String name);

	List<SupplyResponse> getBySuppliesWithMinQuantity(BigDecimal minQuantity);

	List<SupplyResponse> getBySuppliesByStorageId(Long storageId);

}
