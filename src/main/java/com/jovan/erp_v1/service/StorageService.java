package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.StorageMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {

	private final StorageRepository storageRepository;
	private final StorageMapper storageMapper;

	@Transactional
	@Override
	public StorageResponse createStorage(StorageRequest request) {
		validateCreateStorage(request);
		Storage storage = storageMapper.toEntity(request);
		Storage saved = storageRepository.save(storage);
		return storageMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public StorageResponse updateStorage(Long id, StorageRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Storage storage = storageRepository.findById(id)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + id));
		validateUpdateStorage(request);
		storage.setName(request.name());
		storage.setLocation(request.location());
		storage.setCapacity(request.capacity());
		storage.setType(request.type());
		storage.setStatus(request.status());
		Storage update = storageRepository.save(storage);
		return storageMapper.toResponse(update);
	}

	@Transactional
	@Override
	public void deleteStorage(Long id) {
		if (!storageRepository.existsById(id)) {
			throw new StorageNotFoundException("Storage not found with id: " + id);
		}
		storageRepository.deleteById(id);
	}

	@Override
	public List<StorageResponse> getByStorageType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsByType(type)) {
			String msg = String.format("No storage with given type found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public StorageResponse getByStorageId(Long id) {
		Storage storage = storageRepository.findById(id)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found wtij id: " + id));
		return storageMapper.toResponse(storage);
	}

	@Override
	public List<StorageResponse> getByName(String name) {
		validateString(name);
		if(!storageRepository.existsByNameContainingIgnoreCase(name)) {
			String msg = String.format("No storage with given name found %s", name);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByNameContainingIgnoreCase(name);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByLocation(String location) {
		validateString(location);
		if(!storageRepository.existsByLocationContainingIgnoreCase(location)) {
			String msg = String.format("No storage with given location found %s", location);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByLocation(location);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByCapacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		if(!storageRepository.existsByCapacity(capacity)) {
			String msg = String.format("No storage with than capacity found %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByCapacity(capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByNameAndLocation(String name, String location) {
		validateString(name);
		validateString(location);
		List<Storage> items =storageRepository.findByNameAndLocation(name, location);
		if(items.isEmpty()) {
			String msg = String.format("Storage with name %s and location %s not found"
					, name,location);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByTypeAndCapacityGreaterThan(StorageType type, BigDecimal capacity) {
		validateStorageType(type);
		validateBigDecimal(capacity);
		if(!storageRepository.existsByTypeAndCapacityGreaterThan(type, capacity)) {
			String msg = String.format("Storage with type '%s' and capacity > %s found",
					type,capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeAndCapacityGreaterThan(type, capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getStoragesWithMinGoods(Integer minCount) {
		validateInteger(minCount);
		List<Storage> all = storageRepository.findAll();
		List<Storage> filtered = all.stream()
				.filter(storage -> storage.getGoods().size() >= minCount)
				.toList();
		return storageMapper.toResponseList(filtered);
	}

	@Override
	public List<StorageResponse> getByNameContainingIgnoreCase(String name) {
		validateString(name);
		List<Storage> storages = storageRepository.findByNameContainingIgnoreCase(name);
		if(storages.isEmpty()) {
			String msg = String.format("No storage with name equal found %s", name);
			throw new NoDataFoundException(msg);
		}
		return storageMapper.toResponseList(storages);
	}

	@Override
	public List<StorageResponse> getAllStorage() {
		List<Storage> items = storageRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No storages are found");
		}
		return items.stream()
				.map(StorageResponse::new)
				.collect(Collectors.toList());
	}
	
	//nove metode

	@Override
	public List<StorageResponse> findByTypeAndCapacityLessThan(StorageType type, BigDecimal capacity) {
		validateBigDecimal(capacity);
		validateStorageType(type);
		if(!storageRepository.existsByType(type)) {
			String msg = String.format("No storage with type '%s' and capacity < %s found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeAndCapacityLessThan(type, capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		if(!storageRepository.existsByCapacityGreaterThan(capacity)) {
			String msg = String.format("Storage with capacity greater than not found %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByCapacityGreaterThan(capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByCapacityLessThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		if(!storageRepository.existsByCapacityLessThan(capacity)) {
			String msg = String.format("Storage with capacity less than not found %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByCapacityLessThan(capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByNameAndLocationAndCapacity(String name, String location, BigDecimal capacity) {
		validateString(name);
		validateString(location);
		validateBigDecimal(capacity);
		if(!storageRepository.existsByNameAndLocationAndCapacity(name, location, capacity)) {
			String msg = String.format("Storage with name %s and location %s and capacity %s not found",
					name,location,capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByNameAndLocationAndCapacity(name, location, capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByTypeAndLocation(StorageType type, String location) {
		validateStorageType(type);
		validateString(location);
		if(!storageRepository.existsByTypeAndLocation(type, location)) {
			String msg = String.format("Storage with type %s and location %s not found",
					type,location);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeAndLocation(type, location);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByTypeAndName(StorageType type, String name) {
		validateStorageType(type);
		validateString(name);
		if(!storageRepository.existsByType(type)) {
			String msg = String.format("Storage with type %s and name %s not found",
					type,name);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeAndName(type, name);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByLocationAndCapacity(StorageType type, BigDecimal capacity) {
		validateStorageType(type);
		validateBigDecimal(capacity);
		if(!storageRepository.existsByLocationAndCapacity(type, capacity)) {
			String msg = String.format("Storage with type %s and capacity %s not found",
					type,capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByLocationAndCapacity(type, capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByTypeAndLocationAndCapacity(StorageType type, String location, BigDecimal capacity) {
		validateStorageType(type);
		validateString(location);
		validateBigDecimal(capacity);
		if(!storageRepository.existsByTypeAndLocationAndCapacity(type, location, capacity)) {
			String msg = String.format("Storage with type %s location %s and capacity %s not found",
					type,location,capacity);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeAndLocationAndCapacity(type, location, capacity);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location) {
		validateString(name);
		validateString(location);
		if(!storageRepository.existsByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location)) {
			String msg = String.format("Storage with name %s and location %s found",
					name,location);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByCapacityBetween(BigDecimal min, BigDecimal max) {
		validateBigDecimal(max);
		validateBigDecimal(min);
		if(!storageRepository.existsByCapacityBetween(min, max)) {
			String msg = String.format("No capacity range found between %s and %s",
					min,max);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items =storageRepository.findByCapacityBetween(min, max);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByTypeOrderByCapacityDesc(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsByType(type)) {
			String msg = String.format("No storage found for type '%s' ordered by capacity descending", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByTypeOrderByCapacityDesc(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByLocationOrderByNameAsc(String location) {
		validateString(location);
		if(!storageRepository.existsByLocation(location)) {
			String msg = String.format("No storage found for location '%s' ordered by name ascending", location);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByLocationOrderByNameAsc(location);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStoragesWithoutGoods() {
		if(!storageRepository.existsStoragesWithoutGoods()) {
			throw new NoDataFoundException("No storage found without goods");
		}
		List<Storage> items = storageRepository.findStoragesWithoutGoods();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByExactShelfCount(Integer shelfCount) {
		validateShelfCount(shelfCount);
		if(!storageRepository.existsByExactShelfCount(shelfCount)) {
			String msg = String.format("Exact shelf count not found for given storage %d", shelfCount);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByExactShelfCount(shelfCount);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findByLocationContainingIgnoreCaseAndType(String location, StorageType type) {
		validateString(location);
		validateStorageType(type);
		if(!storageRepository.existsByLocationContainingIgnoreCaseAndType(location, type)) {
			String msg = String.format("No storage found with location containing '%s' and type '%s'", location, type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findByLocationContainingIgnoreCaseAndType(location, type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStoragesWithMaterials() {
		if(!storageRepository.existsStoragesWithMaterials()) {
			throw new NoDataFoundException("No storages with materials found");
		}
		List<Storage> items = storageRepository.findStoragesWithMaterials();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStoragesWithWorkCenters() {
		if(!storageRepository.existsStoragesWithWorkCenters()) {
			throw new NoDataFoundException("No storages with work-centers found");
		}
		List<Storage> items = storageRepository.findStoragesWithWorkCenters();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStoragesWithoutShelves() {
		if(!storageRepository.existsStorageWithoutShelves()) {
			throw new NoDataFoundException("No storage without shelves found");
		}
		List<Storage> items = storageRepository.findStoragesWithoutShelves();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findAvailableStorages() {
		if(!storageRepository.existsAvailableStorages()) {
			throw new NoDataFoundException("No availabe storages found");
		}
		List<Storage> items = storageRepository.findAvailableStorages();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findSuitableStoragesForShipment(BigDecimal minCapacity) {
		validateBigDecimal(minCapacity);
		List<Storage> items = storageRepository.findSuitableStoragesForShipment(minCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No suitable storages from min-capacity found %s", minCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findEmptyStorages() {
		if(!storageRepository.existsCompletelyEmptyStorage()) {
			throw new NoDataFoundException("No empty storages found");
		}
		List<Storage> items =storageRepository.findEmptyStorages();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStorageWithoutGoodsAndMaterialsByType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsStorageWithoutGoodsAndMaterialsByType(type)) {
			String msg = String.format("No storage-type without goods and materials found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findStorageWithoutGoodsAndMaterialsByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStorageWithGoodsAndMaterialsByType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsStorageWithGoodsAndMaterialsByType(type)) {
			String msg = String.format("No storage-type with goods and materials found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findStorageWithGoodsAndMaterialsByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findStorageWithGoodsOrMaterialsByType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsStorageWithGoodsOrMaterialsByType(type)) {
			String msg = String.format("No storage-type with goods or materials found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findStorageWithGoodsOrMaterialsByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findAllByType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsByType(type)) {
			String msg = String.format("No storage with all types found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findAllByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findEmptyStorageByType(StorageType type) {
		validateStorageType(type);
		if(!storageRepository.existsEmptyStorageByType(type)) {
			String msg = String.format("No empty storage found with type equal %s", type);
			throw new NoDataFoundException(msg);
		}
		List<Storage> items = storageRepository.findEmptyStorageByType(type);
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findProductionStorage() {
		if(!storageRepository.existsProductionStorage()) {
			throw new NoDataFoundException("No storage-type equal to production found");
		}
		List<Storage> items = storageRepository.findProductionStorage();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findDistributionStorage() {
		if(!storageRepository.existsDistributionStorage()) {
			throw new NoDataFoundException("No storage-type equal to distribution found");
		}
		List<Storage> items = storageRepository.findDistributionStorage();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findOpenStorage() {
		if(!storageRepository.existsOpenStorage()) {
			throw new NoDataFoundException("No storage-type equal to open found");
		}
		List<Storage> items = storageRepository.findOpenStorage();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findClosedStorage() {
		if(!storageRepository.existsClosedStorage()) {
			throw new NoDataFoundException("No storage-type equal to closed found");
		}
		List<Storage> items = storageRepository.findClosedStorage();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> findInterimStorage() {
		if(!storageRepository.existsInterimStorage()) {
			throw new NoDataFoundException("No storage type equal to interim found");
		}
		List<Storage> items = storageRepository.findInterimStorage();
		return items.stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
	
	private void validateStorageStatus(StorageStatus status) {
		if(status == null) {
			throw new ValidationException("StorageStatus status must not be null");
		}
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new ValidationException("StorageType type must not be null");
		}
	}
	
	private void validateCreateStorage(StorageRequest request) {
		validateString(request.name());
		validateString(request.location());
		validateBigDecimal(request.capacity());
		validateStorageType(request.type());
		validateShelfRequest(request.shelves());
		validateStorageStatus(request.status());
	}
	
	private void validateUpdateStorage(StorageRequest request) {
		validateString(request.name());
		validateString(request.location());
		validateBigDecimal(request.capacity());
		validateStorageType(request.type());
		validateShelfRequest(request.shelves());
		validateStorageStatus(request.status());
	}
	
	private void validateShelfRequest(List<ShelfRequest> shelves) {
		if(shelves == null || shelves.isEmpty()) {
			throw new ValidationException("ShelfRequest must not be null nor empty");
		}
		for(ShelfRequest sh: shelves) {
			if(sh == null) {
				throw new ValidationException("ShelfRequest must not be null");
			}
			validateShelfRequest(sh);
		}
	}
	
	
	private void validateShelfRequest(ShelfRequest request) {
		if (request.rowCount() == null) {
	        throw new ValidationException("Row-count must not be null");
	    }
	    if (request.cols() == null) {
	        throw new ValidationException("Cols must not be null");
	    }
	    if (request.rowCount() < 1) {
	        throw new ValidationException("Row-count must be at least 1");
	    }
	    if (request.cols() < 1) {
	        throw new ValidationException("Cols must be at least 1");
	    }
		if(request.goods() == null || request.goods().isEmpty()) {
			throw new ValidationException("Goods list must not be null nor empty");
		}
		for (Long goodId : request.goods()) {
	        if (goodId == null || goodId <= 0) {
	            throw new ValidationException("Goods list contains invalid ID (must be positive and non-null)");
	        }
	    }
	}
	
	private void validateShelfRequestRequest(List<ShelfRequest> requests) {
		if(requests == null || requests.isEmpty()) {
			throw new ValidationException("ShelfRequest must not be null nor empty");
		}
		for(ShelfRequest req : requests) {
			if(req == null) {
				throw new ValidationException("ShelfRequest must not be null");
			}
			validateShelfRequestRequest(req);
		}
	}
	
	private void validateShelfRequestRequest(ShelfRequest request) {
		if (request.rowCount() == null) {
	        throw new ValidationException("Row-count must not be null");
	    }
	    if (request.cols() == null) {
	        throw new ValidationException("Cols must not be null");
	    }
	    if (request.rowCount() < 1) {
	        throw new ValidationException("Row-count must be at least 1");
	    }
	    if (request.cols() < 1) {
	        throw new ValidationException("Cols must be at least 1");
	    }
		if(request.goods() == null || request.goods().isEmpty()) {
			throw new ValidationException("Goods list must not be null nor empty");
		}
		if(request.storageId() == null) {
			throw new ValidationException("Storage ID must not be null"); //ova je metoda dodata samo za update metodu
		}
		for (Long goodId : request.goods()) {
	        if (goodId == null || goodId <= 0) {
	            throw new ValidationException("Goods list contains invalid ID (must be positive and non-null)");
	        }
	    }
	}
	
	private void validateShelfCount(Integer shelfCount) {
		if(shelfCount == null || shelfCount < 1) {
			throw new ValidationException("Shelf-count must not be null nor negative number");
		}
	}
	
	private void validateCols(Integer cols) {
		if(cols == null || cols < 1) {
			throw new ValidationException("Cols must not be null nor negative number");
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num < 1) {
			throw new ValidationException("Number must not be null nor negative");
		}
	}
	
	private void validateRowCount(Integer rowCount) {
		if(rowCount == null || rowCount < 1) {
			throw new ValidationException("Row-count must not be null nor negative number");
		}
	}

}
