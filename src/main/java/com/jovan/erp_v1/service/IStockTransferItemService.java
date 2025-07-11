package com.jovan.erp_v1.service;

import java.math.BigDecimal;
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
    List<StockTransferItemResponse> findByQuantity(BigDecimal quantity);
    List<StockTransferItemResponse> findByQuantityLessThan(BigDecimal quantity);
    List<StockTransferItemResponse> findByQuantityGreaterThan(BigDecimal quantity);
    List<StockTransferItemResponse> findByStockTransferId(Long stockTransferId);
    List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId);
    List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId);
}
