package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.save_as.InboundDeliverySaveAsRequest;
import com.jovan.erp_v1.search_request.InboundDeliverySearchRequest;

public interface InterfejsInboundDeliveryService {

    InboundDeliveryResponse create(InboundDeliveryRequest request);
    InboundDeliveryResponse update(Long id, InboundDeliveryRequest request);
    void delete(Long id);
    InboundDeliveryResponse findByOneId(Long id);
    List<InboundDeliveryResponse> findAll();
    List<InboundDeliveryResponse> findByStatus(DeliveryStatus status);
    List<InboundDeliveryResponse> findBySupplyId(Long supplyId);
    List<InboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to);
    // Bulk metode
    List<InboundDeliveryResponse> createAll(List<InboundDeliveryRequest> requests);
    void deleteAllByIds(List<Long> ids);
    
    //nove metode
    List<InboundDeliveryResponse> findBySupply_Storage_Id( Long storageId);
    List<InboundDeliveryResponse> findBySupply_Storage_NameContainingIgnoreCase( String storageName);
    List<InboundDeliveryResponse> findBySupply_Storage_LocationContainingIgnoreCase( String storageLocation);
    List<InboundDeliveryResponse> findBySupply_StorageCapacity( BigDecimal storageCapacity);
    List<InboundDeliveryResponse> findBySupply_StorageCapacityGreaterThan( BigDecimal storageCapacity);
    List<InboundDeliveryResponse> findBySupply_StorageCapacityLessThan( BigDecimal storageCapacity);
    List<InboundDeliveryResponse> findBySupply_StorageType( StorageType type);
    List<InboundDeliveryResponse> findBySupply_StorageStatus( StorageStatus status);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndType( String storageName, StorageType type);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndStatus( String storageName, StorageStatus status);
    List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndType( String storageLocation,StorageType type);
    List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndStatus( String storagLocation, StorageStatus status);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacity( String storageName, BigDecimal capacity);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan( String storageName, BigDecimal capacity);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan( String storageName, BigDecimal capacity);
    List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase( String storageName, String storageLocation);
    List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacity( String storageLocation,  BigDecimal capacity);
    List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(String storageLocation,  BigDecimal capacity);
    List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan( String storageLocation,  BigDecimal capacity);
    List<InboundDeliveryResponse> findByStorageWithoutShelvesOrUnknown();
    List<InboundDeliveryResponse> findBySupply_Quantity(BigDecimal quantity);
    List<InboundDeliveryResponse> findBySupply_QuantityGreaterThan(BigDecimal quantity);
    List<InboundDeliveryResponse> findBySupply_QuantityLessThan(BigDecimal quantity);
    List<InboundDeliveryResponse> findBySupply_QuantityBetween(BigDecimal min, BigDecimal max);
    List<InboundDeliveryResponse> findBySupply_Updates(LocalDateTime updates);
    List<InboundDeliveryResponse> findBySupply_UpdatesAfter(LocalDateTime updates);
    List<InboundDeliveryResponse> findBySupply_UpdatesBefore(LocalDateTime updates);
    List<InboundDeliveryResponse> findBySupply_UpdatesBetween(LocalDateTime updatesFrom, LocalDateTime updatesTo);
    
    //nove metode
    InboundDeliveryResponse trackInboundDelivery(Long id);
    InboundDeliveryResponse confirmInboundDelivery(Long id);
    InboundDeliveryResponse cancelInboundDelivery(Long id);
    InboundDeliveryResponse closeInboundDelivery(Long id);
    InboundDeliveryResponse changeStatus(Long id, InboundDeliveryStatus status);
    InboundDeliveryResponse saveInboundDelivery(InboundDeliveryRequest request);
    InboundDeliveryResponse saveAs(InboundDeliverySaveAsRequest request);
    List<InboundDeliveryResponse> saveAll(List<InboundDeliveryRequest> request);
    List<InboundDeliveryResponse> generalSearch(InboundDeliverySearchRequest request);
}
