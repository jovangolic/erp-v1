package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.StockTransferItem;

@Repository
public interface StockTransferItemRepository extends JpaRepository<StockTransferItem, Long> {

    List<StockTransferItem> findByProductId(Long productId);

    List<StockTransferItem> findByProduct_Name(String name);

    List<StockTransferItem> findByProduct_CurrentQuantity(Double currentQuantity);

    List<StockTransferItem> findByQuantity(Double quantity);

    List<StockTransferItem> findByQuantityLessThan(Double quantity);

    List<StockTransferItem> findByQuantityGreaterThan(Double quantity);

    List<StockTransferItem> findByStockTransferId(Long stockTransferId);

    List<StockTransferItem> findByStockTransfer_FromStorageId(Long fromStorageId);

    List<StockTransferItem> findByStockTransfer_ToStorageId(Long toStorageId);
}
