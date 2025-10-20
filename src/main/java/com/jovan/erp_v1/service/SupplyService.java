package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.dto.StorageGoodsCountDTO;
import com.jovan.erp_v1.dto.StorageIncomingMovementCountDTO;
import com.jovan.erp_v1.dto.StorageIncomingTransferCountDTO;
import com.jovan.erp_v1.dto.StorageMaterialCountDTO;
import com.jovan.erp_v1.dto.StorageOutgoingMovementCountDTO;
import com.jovan.erp_v1.dto.StorageOutgoingTransferCountDTO;
import com.jovan.erp_v1.dto.StorageShelfCountDTO;
import com.jovan.erp_v1.dto.StorageShipmentCountDTO;
import com.jovan.erp_v1.dto.StorageWorkCenterCountDTO;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
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
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyService implements ISupplyService {

	private final SupplyRepository supplyRepository;
	private final SupplyMapper supplyMapper;
	private final StorageRepository storageRepository;
	private final GoodsRepository goodsRepository;

	@Transactional
	@Override
	public SupplyResponse createSupply(SupplyRequest request) {
		Storage storage = fetchStorageId(request.storageId()); //prva provera
		List<Long> goodsList = validateGoodsIds(request.goodsIds()); //druga provera
		validateBigDecimal(request.quantity()); //treca provera
		DateValidator.validatePastOrPresent(request.updates(), "Date"); //cetvrta provera
		List<Goods> goodsItems = goodsRepository.findAllById(goodsList);
		if(goodsItems.size() != goodsList.size()) {
			throw new ValidationException("Some Goods not found by given IDs.");
		}
		Supply supply = supplyMapper.toEntity(request,storage,goodsItems);
		/*if (supply.getUpdates() == null) {
			supply.setUpdates(LocalDateTime.now());
		}*/
		Supply saved = supplyRepository.save(supply);
		return supplyMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public SupplyResponse updateSupply(Long id, SupplyRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Supply existing = supplyRepository.findById(id)
				.orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));
		List<Long> goodsList = validateGoodsIds(request.goodsIds()); 
		validateBigDecimal(request.quantity()); 
		DateValidator.validatePastOrPresent(request.updates(), "Date"); 
		List<Goods> goodsItems = goodsRepository.findAllById(goodsList);
		if(goodsItems.size() != goodsList.size()) {
			throw new ValidationException("Some Goods not found by given IDs.");
		}
		Storage storage = fetchStorageId(request.storageId());
		existing = supplyMapper.toEntityUpdate(existing, request, storage, goodsItems);
		Supply updated = supplyRepository.save(existing);
		return supplyMapper.toResponse(updated);
	}

	@Transactional
	@Override
	public void deleteSupply(Long id) {
		if (!supplyRepository.existsById(id)) {
			throw new SupplyNotFoundException("Supply not found with id: " + id);
		}
		supplyRepository.deleteById(id);
	}

	@Override
	public SupplyResponse findOne(Long supplyId) {
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new SupplyNotFoundException("Supply with id: " + supplyId + " not found"));
		return supplyMapper.toResponse(supply);
	}

	@Override
	public List<SupplyResponse> getAllSupply() {
		List<Supply> items = supplyRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No items found in list for supply");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> getBySuppliesByGoodsName(String name) {
		validateString(name);
		List<Supply> items = supplyRepository.findSuppliesByGoodsName(name);
		if(items.isEmpty()) {
			String msg = String.format("Supplies with goods name not found %s", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> getBySuppliesWithMinQuantity(BigDecimal minQuantity) {
		validateBigDecimalNonNegative(minQuantity);
		List<Supply> items = supplyRepository.findSuppliesWithMinQuantity(minQuantity);
		if(items.isEmpty()) {
			String msg = String.format("Supplies with min-quantity not found %s", minQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> getBySuppliesByStorageId(Long storageId) {
		fetchStorageId(storageId);
		List<Supply> items = supplyRepository.findSuppliesByStorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No supplies found for storage-id %d", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	//nove metode

	@Override
	public List<SupplyResponse> findByUpdatesBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Supply> items = supplyRepository.findByUpdatesBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("Storage with given updates date between %s and %s is not found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<Supply> items = supplyRepository.findByStorage_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given name %s is not found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_LocationContainingIgnoreCase(String location) {
		validateString(location);
		List<Supply> items = supplyRepository.findByStorage_LocationContainingIgnoreCase(location);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given location %s uis not found", location);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Capacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<Supply> items = supplyRepository.findByStorage_Capacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given capacity %s is not found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);;
		List<Supply> items = supplyRepository.findByStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with capacity greater than %s is not found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<Supply> items = supplyRepository.findByStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with capacity less than %s is not found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type(StorageType type) {
		validateStorageType(type);
		List<Supply> items = supplyRepository.findByStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given type %s is not found",
					type);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<Supply> items = supplyRepository.findByStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given status %s is not found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type_AndCapacity(StorageType type, BigDecimal capacity) {
		validateStorageType(type);
		validateBigDecimal(capacity);
		List<Supply> items = supplyRepository.findByStorageTypeAndCapacity(type, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given type %s and capacity %s is not found",
					type,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type_AndCapacityGreaterThan(StorageType type, BigDecimal capacity) {
		validateStorageType(type);
		validateBigDecimal(capacity);
		List<Supply> items = supplyRepository.findByStorageTypeAndCapacityGreaterThan(type, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given type %s and capacity greater than %s is not found",
					type,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type_AndCapacityLessThan(StorageType type, BigDecimal capacity) {
		validateStorageType(type);
		validateBigDecimalNonNegative(capacity);
		List<Supply> items = supplyRepository.findByStorageTypeAndCapacityLessThan(type, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given type %s and capacity less than %s is not found",
					type,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type_AndStatus(StorageType type, StorageStatus status) {
		validateStorageType(type);
		validateStorageStatus(status);
		List<Supply> items = supplyRepository.findByStorageTypeAndStatus(type, status);
		if(items.isEmpty()) {
			String msg = String.format("Storage with given type % and status %s is not found",
					type,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Type_AndLocation(StorageType type, String location) {
		validateStorageType(type);
		validateString(location);
		List<Supply> items = supplyRepository.findByStorageTypeAndLocation(type, location);
		if(items.isEmpty()) {
			String msg = String.format("Storage with type %s and location %s is not found",
					type,location);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Location_AndCapacity(String location, BigDecimal capacity) {
		validateString(location);
		validateBigDecimal(capacity);
		List<Supply> items = supplyRepository.findByStorageLocationAndCapacity(location, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with location %s and capacity % is not found",
					location,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Location_AndCapacityGreaterThan(String location, BigDecimal capacity) {
		validateString(location);
		validateBigDecimal(capacity);
		List<Supply> items = supplyRepository.findByStorageLocationAndCapacityGreaterThan(location, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with location %s and capacity greater than %s is not found",
					location,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_Location_AndCapacityLessThan(String location, BigDecimal capacity) {
		validateString(location);
		validateBigDecimalNonNegative(capacity);
		List<Supply> items = supplyRepository.findByStorageLocationAndCapacityLessThan(location, capacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with location %s and capacity less than %s is not found",
					location,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_CapacityBetween(BigDecimal min, BigDecimal max) {
		validateBigDecimalNonNegative(min);
		validateBigDecimal(max);
		List<Supply> items = supplyRepository.findByStorage_CapacityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("Storage with capacity between %s and %s is not found",
					min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageGoodsCountResponse> countGoodsPerStorage() {
		List<StorageGoodsCountDTO> items = supplyRepository.countGoodsPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for goods per storage is not found");
		}
		return items.stream()
				.map(StorageGoodsCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageShelfCountResponse> countShelvesPerStorage() {
		List<StorageShelfCountDTO> items = supplyRepository.countShelvesPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for shelves per storage is not found");
		}
		return items.stream()
				.map(StorageShelfCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageShipmentCountResponse> countOutgoingShipmentsPerStorage() {
		List<StorageShipmentCountDTO> items = supplyRepository.countOutgoingShipmentsPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for outgoing shipments per storage is not found");
		}
		return items.stream()
				.map(StorageShipmentCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageOutgoingTransferCountResponse> countOutgoingTransfersPerStorage() {
		List<StorageOutgoingTransferCountDTO> items = supplyRepository.countOutgoingTransfersPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for outgoing transfers per storage is not found");
		}
		return items.stream()
				.map(StorageOutgoingTransferCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageIncomingTransferCountResponse> countIncomingTransfersPerStorage() {
		List<StorageIncomingTransferCountDTO> items = supplyRepository.countIncomingTransfersPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for incoming transfers per storage is not found");
		}
		return items.stream()
				.map(StorageIncomingTransferCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageMaterialCountResponse> countMaterialsPerStorage() {
		List<StorageMaterialCountDTO> items = supplyRepository.countMaterialsPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for materials per storage is not found");
		}
		return items.stream()
				.map(StorageMaterialCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageOutgoingMovementCountResponse> countOutgoingMaterialMovementsPerStorage() {
		List<StorageOutgoingMovementCountDTO> items = supplyRepository.countOutgoingMaterialMovementsPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for outgoing material movements per storage is not found");
		}
		return items.stream()
				.map(StorageOutgoingMovementCountResponse::new)
				.collect(Collectors.toList());
		}

	@Override
	public List<StorageIncomingMovementCountResponse> countIncomingMaterialMovementsPerStorage() {
		List<StorageIncomingMovementCountDTO> items = supplyRepository.countIncomingMaterialMovementsPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for incoming material movements per storage is not found");
		}
		return items.stream()
				.map(StorageIncomingMovementCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageWorkCenterCountResponse> countWorkCentersPerStorage() {
		List<StorageWorkCenterCountDTO> items = supplyRepository.countWorkCentersPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Count for work-centers per storage is not found");
		}
		return items.stream()
				.map(StorageWorkCenterCountResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageWithMinGoodsCount(Integer minGoodsCount) {
		validateMinInteger(minGoodsCount);
		List<Supply> items = supplyRepository.findByStorageWithMinGoodsCount(minGoodsCount);
		if(items.isEmpty()) {
			String msg = String.format("Storage with minimal goods count is not found %d", minGoodsCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageWithMaxGoodsCount(Integer maxGoodsCount) {
		validatePositiveInteger(maxGoodsCount);
		List<Supply> items = supplyRepository.findByStorageWithMaxGoodsCount(maxGoodsCount);
		if(items.isEmpty()) {
			String msg = String.format("Storage with maximum goods count is not found %d", maxGoodsCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageContainingMaterial(String name) {
		validateString(name);
		List<Supply> items = supplyRepository.findByStorageContainingMaterial(name);
		if(items.isEmpty()) {
			String msg = String.format("Storage containing certain material name is not found %s", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByShelfCapacityInStorage(BigDecimal minShelfCapacity) {
		validateBigDecimalNonNegative(minShelfCapacity);
		List<Supply> items = supplyRepository.findByShelfCapacityInStorage(minShelfCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Shelf capacity in storage is not found %s", minShelfCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageUsedAsTransferOrigin() {
		List<Supply> items = supplyRepository.findByStorageUsedAsTransferOrigin();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage used ad transfer orogin is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findSuppliesWithWorkCenters() {
		List<Supply> items = supplyRepository.findSuppliesWithWorkCenters();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Supplies with work-center are not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_UsedCapacityGreaterThan(BigDecimal usedCapacity) {
		validateBigDecimal(usedCapacity);
		List<Supply> items = supplyRepository.findByStorage_UsedCapacityGreaterThan(usedCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with used-capacity greater than is not found %s", usedCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorage_UsedCapacityLessThan(BigDecimal usedCapacity) {
		validateBigDecimalNonNegative(usedCapacity);
		List<Supply> items = supplyRepository.findByStorage_UsedCapacityLessThan(usedCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Storage with used-capacity less than is not found %s", usedCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageWithEmptyShelves() {
		List<Supply> items = supplyRepository.findByStorageWithEmptyShelves();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with empty shelves is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findByStorageUsedAsTransferDestination() {
		List<Supply> items = supplyRepository.findByStorageUsedAsTransferDestination();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage used as transfer destination is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findProductionStorage() {
		List<Supply> items = supplyRepository.findProductionStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'PRODUCTION' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findDistributionStorage() {
		List<Supply> items = supplyRepository.findDistributionStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'DISTRIBUTION' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findOpenStorage() {
		List<Supply> items = supplyRepository.findOpenStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'OPEN' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findClosedStorage() {
		List<Supply> items = supplyRepository.findClosedStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'CLOSED' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findInterimStorage() {
		List<Supply> items = supplyRepository.findInterimStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'INTERIM' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findAvailableStorage() {
		List<Supply> items = supplyRepository.findAvailableStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with type 'AVAILABLE' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findActiveStorage() {
		List<Supply> items = supplyRepository.findActiveStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'ACTIVE' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findUnderMaintenanceStorage() {
		List<Supply> items = supplyRepository.findUnderMaintenanceStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'UNDER_MAINTENANCE' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findDecommissionedStorage() {
		List<Supply> items = supplyRepository.findDecommissionedStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'DECOMMISSIONED' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findReservedStorage() {
		List<Supply> items = supplyRepository.findReservedStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'RESERVED' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findTemporaryStorage() {
		List<Supply> items = supplyRepository.findTemporaryStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'TEMPORARY' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyResponse> findFullStorage() {
		/*if(!supplyRepository.existsByStorageStatus(StorageStatus.FULL)) {
			throw new NoDataFoundException("Storage with status 'FULL' is not found");
		}*/
		List<Supply> items = supplyRepository.findFullStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Storage with status 'FULL' is not found");
		}
		return items.stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new ValidationException("StorageType type must not be null");
		}
	}
	
	private void validateStorageStatus(StorageStatus status) {
		if(status == null) {
			throw new ValidationException("StorageStatus status must not be null");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateMinInteger(Integer num) {
		if(num == null || num < 0) {
			throw new ValidationException("Number must not be null nor negative");
		}
	}
	
	private void validatePositiveInteger(Integer num) {
		if (num == null || num <= 0) {
			throw new ValidationException("Number must be a positive integer (greater than 0)");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private Storage fetchStorageId(Long storageId) {
		if(storageId == null || storageId <= 0) {
			throw new ValidationException("Storage ID must be a positive number and not null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	}
	
	private List<Long> validateGoodsIds(List<Long> goodsIds) {
		if(goodsIds == null || goodsIds.isEmpty()) {
			return Collections.emptyList();
		}
		for(Long id: goodsIds) {
			if(id == null || id <= 0) {
				throw new ValidationException("Goods-id must not be null nor negative");
			}
		}
		return goodsIds;
	}
}
