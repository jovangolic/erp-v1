package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;

public interface IInventoryItemsService {

	InventoryItemsResponse create(InventoryItemsRequest request);
	InventoryItemsResponse update(Long id, InventoryItemsRequest request);
	void delete(Long id);
	List<InventoryItemsResponse> getByQuantity(Double quantity);
	List<InventoryItemsResponse> getByItemCondition(Integer itemCondition);
	List<InventoryItemsResponse> getByInventoryId(Long inventoryId);
	List<InventoryItemsResponse> getByProductId(Long productId);
	List<InventoryItemsResponse> getByProductName(String productName);
	InventoryItemsResponse findById(Long id);
	List<InventoryItemsResponse> findAllInventories();

    List<InventoryItemsResponse> findItemsWithDifference(Double threshold);
}
