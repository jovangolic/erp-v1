package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.enumeration.UnitMeasure;
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
    List<StockTransferItemResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<StockTransferItemResponse> findByQuantity(BigDecimal quantity);
    List<StockTransferItemResponse> findByQuantityLessThan(BigDecimal quantity);
    List<StockTransferItemResponse> findByQuantityGreaterThan(BigDecimal quantity);
    List<StockTransferItemResponse> findByStockTransferId(Long stockTransferId);
    List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId);
    List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId);
    //nove metode
    List<StockTransferItemResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<StockTransferItemResponse> findByProduct_UnitMeasureAndProduct_StorageType(UnitMeasure unitMeasure, StorageType storageType);
    List<StockTransferItemResponse> findByProduct_SupplierType(SupplierType supplierType);
    List<StockTransferItemResponse> findByProduct_GoodsType(GoodsType goodsType);
    List<StockTransferItemResponse> findByProduct_StorageType(StorageType storageType);
    List<StockTransferItemResponse> findByProduct_Shelf_Id(Long shelfId);
    List<StockTransferItemResponse> findByProduct_Shelf_RowCount( Integer rowCount);
    List<StockTransferItemResponse> findByProduct_Shelf_Cols(Integer cols);
    List<StockTransferItemResponse> findByProduct_Shelf_RowCountGreaterThanEqual( Integer rowCount);
    List<StockTransferItemResponse> findByProduct_Shelf_ColsGreaterThanEqual( Integer cols);
    List<StockTransferItemResponse> findByProduct_Shelf_RowCountLessThanEqual(Integer rowCount);
    List<StockTransferItemResponse> findByProduct_Shelf_ColsLessThanEqual( Integer cols);
    List<StockTransferItemResponse> findByProduct_Shelf_RowCountBetween(Integer minRowCount,  Integer maxRowCount);
    List<StockTransferItemResponse> findByProduct_Shelf_ColsBetween( Integer minCols,  Integer maxCols);
    List<StockTransferItemResponse> findByProduct_Supply_Id(Long supplyId);
    List<StockTransferItemResponse> findByStockTransfer_Status(TransferStatus status);
    List<StockTransferItemResponse> findByStockTransfer_TransferDate(LocalDate transferDate);
    List<StockTransferItemResponse> findByStockTransfer_TransferDateBetween(LocalDate transferDateStart, LocalDate transferDateEnd);
    List<StockTransferItemResponse> findByStockTransfer_StatusIn(List<TransferStatus> statuses);
    List<StockTransferItemResponse> findByStockTransfer_StatusNot(TransferStatus status);
    List<StockTransferItemResponse> findByStockTransfer_TransferDateAfter(LocalDate date);
    List<StockTransferItemResponse> findByStockTransfer_TransferDateBefore(LocalDate date);
    List<StockTransferItemResponse> findByStockTransfer_StatusAndQuantityGreaterThan(TransferStatus status, BigDecimal quantity);
    List<StockTransferItemResponse> findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(TransferStatus status, LocalDate start, LocalDate end);
    //fromStorage
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_NameContainingIgnoreCase(String name);
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_LocationContainingIgnoreCase( String location);
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_Capacity( BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_CapacityGreaterThan( BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_CapacityLessThan( BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_FromStorage_Type( StorageType type);
    //toStorage
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_NameContainingIgnoreCase( String name);
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_LocationContainingIgnoreCase(String location);
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_Capacity( BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_CapacityGreaterThan(BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_CapacityLessThan( BigDecimal capacity);
    List<StockTransferItemResponse> findByStockTransfer_ToStorage_Type(StorageType type);
}
