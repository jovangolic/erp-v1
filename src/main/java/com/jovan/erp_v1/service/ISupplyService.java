package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.StorageGoodsCountResponse;
import com.jovan.erp_v1.response.StorageIncomingMovementCountResponse;
import com.jovan.erp_v1.response.StorageIncomingTransferCountResponse;
import com.jovan.erp_v1.response.StorageMaterialCountResponse;
import com.jovan.erp_v1.response.StorageOutgoingMovementCountResponse;
import com.jovan.erp_v1.response.StorageOutgoingTransferCountResponse;
import com.jovan.erp_v1.response.StorageShelfCountResponse;
import com.jovan.erp_v1.response.StorageShipmentCountResponse;
import com.jovan.erp_v1.response.StorageWorkCenterCountResponse;
import com.jovan.erp_v1.response.SupplyResponse;

public interface ISupplyService {

	SupplyResponse createSupply(SupplyRequest request);
	SupplyResponse updateSupply(Long id, SupplyRequest request);
	void deleteSupply(Long id);
	SupplyResponse findOne(Long supplyId);
	List<SupplyResponse> getAllSupply();
	List<SupplyResponse> getBySuppliesByGoodsName(String name);
	List<SupplyResponse> getBySuppliesWithMinQuantity(BigDecimal minQuantity);
	List<SupplyResponse> getBySuppliesByStorageId(Long storageId);
	
	//nove metode
	List<SupplyResponse> findByUpdatesBetween(LocalDateTime start, LocalDateTime end);
    List<SupplyResponse> findByStorage_NameContainingIgnoreCase(String name);
    List<SupplyResponse> findByStorage_LocationContainingIgnoreCase(String location);
    List<SupplyResponse> findByStorage_Capacity(BigDecimal capacity);
    List<SupplyResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity);
    List<SupplyResponse> findByStorage_CapacityLessThan(BigDecimal capacity);
    List<SupplyResponse> findByStorage_Type(StorageType type);
    List<SupplyResponse> findByStorage_Status(StorageStatus status);
    List<SupplyResponse> findByStorage_Type_AndCapacity(StorageType type, BigDecimal capacity);
    List<SupplyResponse> findByStorage_Type_AndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    List<SupplyResponse> findByStorage_Type_AndCapacityLessThan(StorageType type, BigDecimal capacity);
    List<SupplyResponse> findByStorage_Type_AndStatus(StorageType type, StorageStatus status);
    List<SupplyResponse> findByStorage_Type_AndLocation(StorageType type, String location);
    List<SupplyResponse> findByStorage_Location_AndCapacity(String location, BigDecimal capacity);
    List<SupplyResponse> findByStorage_Location_AndCapacityGreaterThan(String location, BigDecimal capacity);
    List<SupplyResponse> findByStorage_Location_AndCapacityLessThan(String location, BigDecimal capacity);
    List<SupplyResponse> findByStorage_CapacityBetween(BigDecimal min, BigDecimal max);
    List<StorageGoodsCountResponse> countGoodsPerStorage();
    List<StorageShelfCountResponse> countShelvesPerStorage();
    List<StorageShipmentCountResponse> countOutgoingShipmentsPerStorage();
    List<StorageOutgoingTransferCountResponse> countOutgoingTransfersPerStorage();
    List<StorageIncomingTransferCountResponse> countIncomingTransfersPerStorage();
    List<StorageMaterialCountResponse> countMaterialsPerStorage();
    List<StorageOutgoingMovementCountResponse> countOutgoingMaterialMovementsPerStorage();
    List<StorageIncomingMovementCountResponse> countIncomingMaterialMovementsPerStorage();
    List<StorageWorkCenterCountResponse> countWorkCentersPerStorage();
    List<SupplyResponse> findByStorageWithMinGoodsCount( Integer minGoodsCount);
    List<SupplyResponse> findByStorageWithMaxGoodsCount( Integer maxGoodsCount);
    List<SupplyResponse> findByStorageContainingMaterial( String name);
    List<SupplyResponse> findByShelfCapacityInStorage( BigDecimal minShelfCapacity);
    List<SupplyResponse> findByStorageUsedAsTransferOrigin();
    List<SupplyResponse> findSuppliesWithWorkCenters();
    List<SupplyResponse> findByStorage_UsedCapacityGreaterThan(BigDecimal usedCapacity);
    List<SupplyResponse> findByStorage_UsedCapacityLessThan(BigDecimal usedCapacity);
    List<SupplyResponse> findByStorageWithEmptyShelves();
    List<SupplyResponse> findByStorageUsedAsTransferDestination();
    List<SupplyResponse> findProductionStorage();
    List<SupplyResponse> findDistributionStorage();
    List<SupplyResponse> findOpenStorage();
    List<SupplyResponse> findClosedStorage();
    List<SupplyResponse> findInterimStorage();
    List<SupplyResponse> findAvailableStorage();
    List<SupplyResponse> findActiveStorage();
    List<SupplyResponse> findUnderMaintenanceStorage();
    List<SupplyResponse> findDecommissionedStorage();
    List<SupplyResponse> findReservedStorage();
    List<SupplyResponse> findTemporaryStorage();
    List<SupplyResponse> findFullStorage();
}
