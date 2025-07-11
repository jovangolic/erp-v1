package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.StockTransferItem;

@Repository
public interface StockTransferItemRepository extends JpaRepository<StockTransferItem, Long> {

    List<StockTransferItem> findByProductId(Long productId);
    List<StockTransferItem> findByProduct_NameContainingIgnoreCase(String name);
    List<StockTransferItem> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<StockTransferItem> findByQuantity(BigDecimal quantity);
    List<StockTransferItem> findByQuantityLessThan(BigDecimal quantity);
    List<StockTransferItem> findByQuantityGreaterThan(BigDecimal quantity);
    List<StockTransferItem> findByStockTransferId(Long stockTransferId);
    List<StockTransferItem> findByStockTransfer_FromStorageId(Long fromStorageId);
    List<StockTransferItem> findByStockTransfer_ToStorageId(Long toStorageId);
    //nove metode
    List<StockTransferItem> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<StockTransferItem> findByProduct_UnitMeasureAndProduct_StorageType(UnitMeasure unitMeasure, StorageType storageType);
    List<StockTransferItem> findByProduct_SupplierType(SupplierType supplierType);
    List<StockTransferItem> findByProduct_GoodsType(GoodsType goodsType);
    List<StockTransferItem> findByProduct_StorageType(StorageType storageType);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.id = :shelfId")
    List<StockTransferItem> findByProduct_Shelf_Id(@Param("shelfId") Long shelfId);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.rowCount = :rowCount")
    List<StockTransferItem> findByProduct_Shelf_RowCount(@Param("rowCount") Integer rowCount);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.cols = :cols")
    List<StockTransferItem> findByProduct_Shelf_Cols(@Param("cols") Integer cols);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.rowCount >= :rowCount")
    List<StockTransferItem> findByProduct_Shelf_RowCountGreaterThanEqual(@Param("rowCount") Integer rowCount);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.cols >= :cols")
    List<StockTransferItem> findByProduct_Shelf_ColsGreaterThanEqual(@Param("cols") Integer cols);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.rowCount <= :rowCount")
    List<StockTransferItem> findByProduct_Shelf_RowCountLessThanEqual(@Param("rowCount") Integer rowCount);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.cols <= :cols")
    List<StockTransferItem> findByProduct_Shelf_ColsLessThanEqual(@Param("cols") Integer cols);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.rowCount BETWEEN :minRowCount AND :maxRowCount")
    List<StockTransferItem> findByProduct_Shelf_RowCountBetween(@Param("minRowCount") Integer minRowCount, @Param("maxRowCount") Integer maxRowCount);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.shelf.cols BETWEEN :minCols AND :maxCols")
    List<StockTransferItem> findByProduct_Shelf_ColsBetween(@Param("minCols") Integer minCols, @Param("maxCols") Integer maxCols);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.product.supply.id = :supplyId")
    List<StockTransferItem> findByProduct_Supply_Id(@Param("supplyId") Long supplyId);
    List<StockTransferItem> findByStockTransfer_Status(TransferStatus status);
    List<StockTransferItem> findByStockTransfer_TransferDate(LocalDate transferDate);
    List<StockTransferItem> findByStockTransfer_TransferDateBetween(LocalDate transferDateStart, LocalDate transferDateEnd);
    List<StockTransferItem> findByStockTransfer_StatusIn(List<TransferStatus> statuses);
    List<StockTransferItem> findByStockTransfer_StatusNot(TransferStatus status);
    List<StockTransferItem> findByStockTransfer_TransferDateAfter(LocalDate date);
    List<StockTransferItem> findByStockTransfer_TransferDateBefore(LocalDate date);
    List<StockTransferItem> findByStockTransfer_StatusAndQuantityGreaterThan(TransferStatus status, BigDecimal quantity);
    List<StockTransferItem> findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(TransferStatus status, LocalDate start, LocalDate end);
    //fromStorage
    @Query("SELECT sti FROM StockTransferItem sti WHERE LOWER(sti.stockTransfer.fromStorage.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StockTransferItem> findByStockTransfer_FromStorage_NameContainingIgnoreCase(@Param("name") String name);
    @Query("SELECT sti FROM StockTransferItem sti WHERE LOWER(sti.stockTransfer.fromStorage.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<StockTransferItem> findByStockTransfer_FromStorage_LocationContainingIgnoreCase(@Param("location") String location);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.capacity = :capacity")
    List<StockTransferItem> findByStockTransfer_FromStorage_Capacity(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.capacity >= :capacity")
    List<StockTransferItem> findByStockTransfer_FromStorage_CapacityGreaterThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.capacity <= :capacity")
    List<StockTransferItem> findByStockTransfer_FromStorage_CapacityLessThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.type = :type")
    List<StockTransferItem> findByStockTransfer_FromStorage_Type(@Param("type") StorageType type);
    //toStorage
    @Query("SELECT sti FROM StockTransferItem sti WHERE LOWER(sti.stockTransfer.toStorage.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StockTransferItem> findByStockTransfer_ToStorage_NameContainingIgnoreCase(@Param("name") String name);
    @Query("SELECT sti FROM StockTransferItem sti WHERE LOWER(sti.stockTransfer.toStorage.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<StockTransferItem> findByStockTransfer_ToStorage_LocationContainingIgnoreCase(@Param("location") String location);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.capacity = :capacity")
    List<StockTransferItem> findByStockTransfer_ToStorage_Capacity(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.capacity >= :capacity")
    List<StockTransferItem> findByStockTransfer_ToStorage_CapacityGreaterThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.capacity <= :capacity")
    List<StockTransferItem> findByStockTransfer_ToStorage_CapacityLessThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT sti FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.type = :type")
    List<StockTransferItem> findByStockTransfer_ToStorage_Type(@Param("type") StorageType type);
    
    //boolean
    boolean existsByStockTransfer_StatusIn(List<TransferStatus> statuses);
    boolean existsByStockTransfer_Status(TransferStatus status);
    boolean existsByStockTransfer_TransferDate(LocalDate date);
    boolean existsByStockTransfer_TransferDateBetween(LocalDate start, LocalDate end);
    boolean existsByStockTransfer_TransferDateBefore(LocalDate date);
    boolean existsByStockTransfer_TransferDateAfter(LocalDate date);
    boolean existsByStockTransfer_StatusAndStockTransfer_TransferDateBetween(TransferStatus status, LocalDate start, LocalDate end);
    boolean existsByProductId(Long productId);
    boolean existsByStockTransferId(Long stockTransferId);
    boolean existsByStockTransfer_FromStorageId(Long fromStorageId);
    boolean existsByStockTransfer_ToStorageId(Long toStorageId);
    //storageType
    boolean existsByStockTransfer_FromStorage_Type(StorageType type);
    boolean existsByStockTransfer_ToStorage_Type(StorageType type);
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.type = 'PRODUCTION'")
    boolean existsFromProductionStorage();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.fromStorage.type = 'DISTRIBUTION'")
    boolean existsFromDistributionStorage();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.type = 'PRODUCTION'")
    boolean existsToProductionStorage();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.toStorage.type = 'DISTRIBUTION'")
    boolean existsToDistributionStorage();
    //transferStatus
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.status = 'INITIATED'")
    boolean existsInitiatedTransfers();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.status = 'IN_TRANSIT'")
    boolean existsInTransitTransfers();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.status = 'COMPLETED'")
    boolean existsCompletedTransfers();
    @Query("SELECT COUNT(sti) > 0 FROM StockTransferItem sti WHERE sti.stockTransfer.status = 'CANCELLED'")
    boolean existsCancelledTransfers();
    //shelf
    boolean existsByProduct_Shelf_RowCount(Integer rowCount);
    boolean existsByProduct_Shelf_RowCountGreaterThanEqual(Integer rowCount);
    boolean existsByProduct_Shelf_RowCountLessThanEqual(Integer rowCount);
    boolean existsByProduct_Shelf_Cols(Integer cols);
    boolean existsByProduct_Shelf_ColsGreaterThanEqual(Integer cols);
    boolean existsByProduct_Shelf_ColsLessThanEqual(Integer cols);
    boolean existsByProduct_Shelf_ColsBetween(Integer minCols, Integer maxCols);
    boolean existsByProduct_Shelf_RowCountBetween(Integer minRowCount,Integer maxRowCount);
    boolean existsByProduct_Shelf_Id(Long shelfId);
}
