package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;

public interface IDeliveryItemService {

    DeliveryItemResponse create(DeliveryItemRequest request);
    DeliveryItemResponse update(Long id, DeliveryItemRequest request);
    void delete(Long id);
    DeliveryItemResponse findById(Long id);
    List<DeliveryItemResponse> findAll();
    List<DeliveryItemResponse> findByQuantity(BigDecimal quantity);
    List<DeliveryItemResponse> findByQuantityGreaterThan(BigDecimal quantity);
    List<DeliveryItemResponse> findByQuantityLessThan(BigDecimal quantity);
    List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
    List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
    DeliveryItemResponse findByProduct(Long productId);
    List<DeliveryItemResponse> findByInboundDeliveryId(Long inboundId);
    List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId);
    List<DeliveryItemResponse> findByProduct_Name(String name);
    
    //nove metode
    List<DeliveryItemResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<DeliveryItemResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<DeliveryItemResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<DeliveryItemResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<DeliveryItemResponse> findByProduct_SupplierType(SupplierType supplierType);
    List<DeliveryItemResponse> findByProduct_StorageType(StorageType storageType);
    List<DeliveryItemResponse> findByProduct_GoodsType(GoodsType goodsType);
    List<DeliveryItemResponse> findByProduct_Storage_Id( Long storageId);
    List<DeliveryItemResponse> findByProduct_Storage_NameContainingIgnoreCase( String storageName);
    List<DeliveryItemResponse> findByProduct_Storage_LocationContainingIgnoreCase( String storageLocation);
    List<DeliveryItemResponse> findByProduct_Storage_Capacity( BigDecimal storageCapacity);
    List<DeliveryItemResponse> findByProduct_Storage_CapacityGreaterThan( BigDecimal storageCapacity);
    List<DeliveryItemResponse> findByProduct_Storage_CapacityLessThan( BigDecimal storageCapacity);
    List<DeliveryItemResponse> findByProduct_Storage_StorageType( StorageType type);
    List<DeliveryItemResponse> findByProduct_Storage_StorageStatus( StorageStatus status);
    BigDecimal calculateUsedCapacityByStorageId( Long storageId);
    BigDecimal sumInboundQuantityByStorageId(Long storageId);
    BigDecimal sumOutboundQuantityByStorageId(Long storageId);
    List<DeliveryItemResponse> findByProductSupplyId( Long supplyId);
    List<DeliveryItemResponse> findByProductShelfId( Long shelfId);	
    List<DeliveryItemResponse> findByProductShelfRowCount( Integer rowCount);
    List<DeliveryItemResponse> findByProductShelfCols(Integer cols);
    List<DeliveryItemResponse> findProductsWithoutShelf();
    List<DeliveryItemResponse> findByInboundDelivery_DeliveryDate(LocalDate start);
    List<DeliveryItemResponse> findByInboundDelivery_Supply_Id( Long supplyId );
    List<DeliveryItemResponse> findByInboundDelivery_Status( DeliveryStatus status);
    List<DeliveryItemResponse> findByInboundDelivery_Status_Pending();
    List<DeliveryItemResponse> findByInboundDelivery_Status_InTransit();
    List<DeliveryItemResponse> findByInboundDelivery_Status_Delivered();
    List<DeliveryItemResponse> findByInboundDelivery_Status_Cancelled();
    List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    List<DeliveryItemResponse> findByOutboundDelivery_Status( DeliveryStatus status);
    List<DeliveryItemResponse> findByOutboundDelivery_Status_Pending();
    List<DeliveryItemResponse> findByOutboundDelivery_Status_InTransit();
    List<DeliveryItemResponse> findByOutboundDelivery_Status_Delivered();
    List<DeliveryItemResponse> findByOutboundDelivery_Status_Cancelled();
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerId( Long buyerId);
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerNameContainingIgnoreCase( String companyName);
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerAddress(String address);
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerEmailLikeIgnoreCase( String buyerEmail);
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase( String phoneNumber);
    List<DeliveryItemResponse> findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase( String contactPerson);
}
