package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;

public interface IStockTransferItemService {

    StockTransferItemResponse create(StockTransferItemRequest request);

    StockTransferItemResponse update(Long id, StockTransferItemRequest request);

    void delete(Long id);

    StockTransferItemResponse findOne(Long id);

    List<StockTransferItemResponse> findAll();

    List<StockTransferItemResponse> findByProductId(Long productId);

    List<StockTransferItemResponse> findByProduct_Name(String name);

    List<StockTransferItemResponse> findByProduct_CurrentQuantity(Double currentQuantity);

    List<StockTransferItemResponse> findByQuantity(Double quantity);

    List<StockTransferItemResponse> findByQuantityLessThan(Double quantity);

    List<StockTransferItemResponse> findByQuantityGreaterThan(Double quantity);

    List<StockTransferItemResponse> findByStockTransferId(Long stockTransferId);

    List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId);

    List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId);
}
