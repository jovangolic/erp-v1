package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.DeliveryItem;

@Repository
public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long>, JpaSpecificationExecutor<DeliveryItem> {

    List<DeliveryItem> findByInboundDeliveryId(Long inboundId);
    List<DeliveryItem> findByOutboundDeliveryId(Long outboundId);
    List<DeliveryItem> findByQuantity(BigDecimal quantity);
    List<DeliveryItem> findByQuantityGreaterThan(BigDecimal quantity);
    List<DeliveryItem> findByQuantityLessThan(BigDecimal quantity);
    @Query("SELECT d FROM DeliveryItem d WHERE d.product.id = :productId")
    DeliveryItem findByProductId(@Param("productId") Long productId);
    List<DeliveryItem> findByProduct_Name(String name);
    List<DeliveryItem> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
    List<DeliveryItem> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
    
    //nove metode
    List<DeliveryItem> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<DeliveryItem> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<DeliveryItem> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<DeliveryItem> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<DeliveryItem> findByProduct_SupplierType(SupplierType supplierType);
    List<DeliveryItem> findByProduct_StorageType(StorageType storageType);
    List<DeliveryItem> findByProduct_GoodsType(GoodsType goodsType);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.id = :storageId")
    List<DeliveryItem> findByProduct_Storage_Id(@Param("storageId") Long storageId);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER (di.product.storage.name) LIKE LOWER (CONCAT('%', :storageName, '%'))")
    List<DeliveryItem> findByProduct_Storage_NameContainingIgnoreCase(@Param("storageName") String storageName);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER(di.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
    List<DeliveryItem> findByProduct_Storage_LocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.capacity = :storageCapacity")
    List<DeliveryItem> findByProduct_Storage_Capacity(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.capacity >= :storageCapacity")
    List<DeliveryItem> findByProduct_Storage_CapacityGreaterThan(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.capacity <= :storageCapacity")
    List<DeliveryItem> findByProduct_Storage_CapacityLessThan(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.type = :type")
    List<DeliveryItem> findByProduct_Storage_StorageType(@Param("type") StorageType type);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.storage.status = :status")
    List<DeliveryItem> findByProduct_Storage_StorageStatus(@Param("status") StorageStatus status);
    @Query("""
    	    SELECT COALESCE(SUM(di.quantity), 0) 
    	    FROM DeliveryItem di 
    	    WHERE di.product.storage.id = :storageId
    	""")
    BigDecimal calculateUsedCapacityByStorageId(@Param("storageId") Long storageId);
    @Query("""
    	    SELECT COALESCE(SUM(di.quantity), 0) 
    	    FROM DeliveryItem di 
    	    WHERE di.product.storage.id = :storageId AND di.inboundDelivery IS NOT NULL
    	""")
    BigDecimal sumInboundQuantityByStorageId(Long storageId);

    	@Query("""
    	    SELECT COALESCE(SUM(di.quantity), 0) 
    	    FROM DeliveryItem di 
    	    WHERE di.product.storage.id = :storageId AND di.outboundDelivery IS NOT NULL
    	""")
    BigDecimal sumOutboundQuantityByStorageId(Long storageId);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.supply.id = :supplyId")	
    List<DeliveryItem> findByProductSupplyId(@Param("supplyId") Long supplyId);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.shelf.id = :shelfId")	
    List<DeliveryItem> findByProductShelfId(@Param("shelfId") Long shelfId);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.shelf.rowCount = :rowCount")	
    List<DeliveryItem> findByProductShelfRowCount(@Param("rowCount") Integer rowCount);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.shelf.cols = :cols")	
    List<DeliveryItem> findByProductShelfCols(@Param("cols") Integer cols);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.shelf IS NULL")
    List<DeliveryItem> findProductsWithoutShelf();
    List<DeliveryItem> findByInboundDelivery_DeliveryDate(LocalDate start);
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.supply.id = :supplyId")
    List<DeliveryItem> findByInboundDelivery_Supply_Id(@Param("supplyId") Long supplyId );
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.status = :status")
    List<DeliveryItem> findByInboundDelivery_Status(@Param("status") DeliveryStatus status);
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.status  = 'PENDING'")
    List<DeliveryItem> findByInboundDelivery_Status_Pending();
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.status  = 'IN_TRANSIT'")
    List<DeliveryItem> findByInboundDelivery_Status_InTransit();
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.status  = 'DELIVERED'")
    List<DeliveryItem> findByInboundDelivery_Status_Delivered();
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.status  = 'CANCELLED'")
    List<DeliveryItem> findByInboundDelivery_Status_Cancelled();
    List<DeliveryItem> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.status = :status")
    List<DeliveryItem> findByOutboundDelivery_Status(@Param("status") DeliveryStatus status);
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.status  = 'PENDING'")
    List<DeliveryItem> findByOutboundDelivery_Status_Pending();
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.status  = 'IN_TRANSIT'")
    List<DeliveryItem> findByOutboundDelivery_Status_InTransit();
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.status  = 'DELIVERED'")
    List<DeliveryItem> findByOutboundDelivery_Status_Delivered();
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.status  = 'CANCELLED'")
    List<DeliveryItem> findByOutboundDelivery_Status_Cancelled();
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.buyer.id = :buyerId")
    List<DeliveryItem> findByOutboundDelivery_BuyerId(@Param("buyerId") Long buyerId);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER(di.outboundDelivery.buyer.companyName) LIKE LOWER(CONCAT('%', :companyName ,'%'))")
    List<DeliveryItem> findByOutboundDelivery_BuyerNameContainingIgnoreCase(@Param("companyName") String companyName);
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.buyer.address = :address")
    List<DeliveryItem> findByOutboundDelivery_BuyerAddress(@Param("buyerId") String address);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER(di.outboundDelivery.buyer.email) LIKE LOWER(CONCAT('%', :buyerEmail, '%'))")
    List<DeliveryItem> findByOutboundDelivery_BuyerEmailLikeIgnoreCase(@Param("buyerEmail") String buyerEmail);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER(di.outboundDelivery.buyer.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    List<DeliveryItem> findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT di FROM DeliveryItem di WHERE LOWER(di.outboundDelivery.buyer.contactPerson) LIKE LOWER(CONCAT('%', :contactPerson, '%'))")
    List<DeliveryItem> findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase(@Param("phoneNumber") String contactPerson);
    
    //nove metode
    @Query("SELECT di FROM DeliveryItem di WHERE di.id = :id")
    Optional<DeliveryItem> trackDeliveryItem(@Param("id") Long id);
    @Query("SELECT di FROM DeliveryItem di WHERE di.product.id = :productId")
    List<DeliveryItem> trackByProduct(@Param("productId") Long productId);
    @Query("SELECT di FROM DeliveryItem di WHERE di.inboundDelivery.id = :deliveryId")
    List<DeliveryItem> trackByInboundDelivery(@Param("deliveryId") Long deliveryId);
    @Query("SELECT di FROM DeliveryItem di WHERE di.outboundDelivery.id = :deliveryId")
    List<DeliveryItem> trackByOutboundDelivery(@Param("deliveryId") Long deliveryId);
}
