package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.ShelfMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.ShelfResponse;
import com.jovan.erp_v1.response.ShelfResponseWithGoods;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelfService implements IShelfService {

	private final StorageRepository storageRepository;
	private final ShelfRepository shelfRepository;
	private final GoodsRepository goodsRepository;
	private final ShelfMapper shelfMapper;

	@Transactional
	@Override
	public ShelfResponse createShelf(ShelfRequest request) {
		validateInteger(request.rowCount());
		validateInteger(request.cols());
		Storage storage = fetchStorageId(request.storageId());
		validateGoodsList(request.goods());
		Shelf shelf = shelfMapper.toEntity(request, storage);
		assignGoodsToShelf(shelf, request.goods());
		Shelf saved = shelfRepository.save(shelf);
		return new ShelfResponse(saved);
	}

	@Transactional
	@Override
	public ShelfResponse updateShelf(Long id, ShelfRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		Shelf shelf = shelfRepository.findById(id)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + id));
		validateInteger(request.rowCount());
		validateInteger(request.cols());
		Storage storage = shelf.getStorage();
		if(request.storageId() != null && (storage.getId() == null || !request.storageId().equals(storage.getId()))) {
			storage = fetchStorageId(request.storageId());
		}
		shelfMapper.toEntityUpdate(shelf, request, storage);
		assignGoodsToShelf(shelf, request.goods());
		Shelf updated = shelfRepository.save(shelf);
		return new ShelfResponse(updated);
	}

	@Transactional
	@Override
	public void deleteShelf(Long id) {
		if (!shelfRepository.existsById(id)) {
			throw new ShelfNotFoundException("Shelf not found with id: " + id);
		}
		shelfRepository.deleteById(id);
	}

	@Override
	public boolean existsByRowCountAndStorageId(Integer rows, Long storageId) {
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.existsByRowCountAndStorageId(rows, storageId);
	}

	@Override
	public boolean existsByColsAndStorageId(Integer cols, Long storageId) {
		validateInteger(cols);
		fetchStorageId(storageId);
		return shelfRepository.existsByColsAndStorageId(cols, storageId);
	}

	@Override
	public boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(cols);
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.existsByRowCountAndColsAndStorageId(rows, cols, storageId);
	}

	@Override
	public List<ShelfResponse> findByStorageId(Long storageId) {
		fetchStorageId(storageId);
		List<Shelf> items = shelfRepository.findByStorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-id %d is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ShelfResponse> findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(cols);
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.findByRowCountAndColsAndStorageId(rows, cols, storageId)
				.map(ShelfResponse::new);
	}

	@Override
	public List<ShelfResponse> findByRowCountAndStorageId(Integer rows, Long storageId) {
		fetchStorageId(storageId);
		validateInteger(rows);
		List<Shelf> items = shelfRepository.findByRowCountAndStorageId(rows, storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for row-count %d and storage-id %d is found", rows,storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByColsAndStorageId(Integer cols, Long storageId) {
		fetchStorageId(storageId);
		validateInteger(cols);
		List<Shelf> items = shelfRepository.findByColsAndStorageId(cols, storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for cols %d and storage-id %d is found", cols,storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public ShelfResponseWithGoods getShelfWithGoods(Long shelfId) {
		Shelf shelf = shelfRepository.findById(shelfId)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + shelfId));
		List<GoodsResponse> goodsResponses = shelf.getGoods().stream()
				.map(GoodsResponse::new) 
				.collect(Collectors.toList());
		return new ShelfResponseWithGoods(
				shelf.getId(),
				shelf.getRowCount(),
				shelf.getCols(),
				shelf.getStorage().getId(),
				goodsResponses);
	}

	@Override
	public ShelfResponse findOne(Long id) {
		Shelf shelf = shelfRepository.findById(id)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + id));
		return new ShelfResponse(shelf);
	}

	@Override
	public List<ShelfResponse> findAll() {
		List<Shelf> items = shelfRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Shelf list is empty");
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<Shelf> items = shelfRepository.findByStorage_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s is found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_LocationContainingIgnoreCase(String location) {
		validateString(location);
		List<Shelf> items = shelfRepository.findByStorage_LocationContainingIgnoreCase(location);
		if(items.isEmpty()) {
			String msg = String.format("", location);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Type(StorageType type) {
		validateStorageType(type);
		List<Shelf> items = shelfRepository.findByStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-type %s is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<Shelf> items = shelfRepository.findByStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage capacity greater than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_NameContainingIgnoreCaseAndStorage_Type(String name, StorageType type) {
		validateString(name);
		validateStorageType(type);
		List<Shelf> items = shelfRepository.findByStorage_NameContainingIgnoreCaseAndStorage_Type(name, type);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage-type %s is found", name,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ShelfResponse> findByStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<Shelf> items = shelfRepository.findByStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-capacity less than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Capacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<Shelf> items = shelfRepository.findByStorage_Capacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-capacity %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<Shelf> items = shelfRepository.findByStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-status %s is found",status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageTypeAndStatus(StorageType type, StorageStatus status) {
		validateStorageType(type);
		validateStorageStatus(status);
		List<Shelf> items = shelfRepository.findByStorageTypeAndStatus(type, status);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-type %s and storage-status %s is found", type,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location) {
		validateString(name);
		validateString(location);
		List<Shelf> items = shelfRepository.findByStorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage-location %s is found", name,location);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageNameContainingIgnoreCaseAndCapacity(String name, BigDecimal capacity) {
		validateString(name);
		validateBigDecimal(capacity);
		List<Shelf> items = shelfRepository.findByStorageNameContainingIgnoreCaseAndCapacity(name, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage capacity %s is found", name,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageNameContainingIgnoreCaseAndCapacityGreaterThan(String name, BigDecimal capacity) {
		validateString(name);
		validateBigDecimal(capacity);
		List<Shelf> items = shelfRepository.findByStorageNameContainingIgnoreCaseAndCapacityGreaterThan(name, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage capacity greater than %s is found", name,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageNameContainingIgnoreCaseAndCapacityLessThan(String name, BigDecimal capacity) {
		validateString(name);
		validateBigDecimalNonNegative(capacity);
		List<Shelf> items = shelfRepository.findByStorageNameContainingIgnoreCaseAndCapacityLessThan(name, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage capacity less than %s is found", name,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorageNameContainingIgnoreCaseAndStatus(String name, StorageStatus status) {
		validateString(name);
		validateStorageStatus(status);
		List<Shelf> items = shelfRepository.findByStorageNameContainingIgnoreCaseAndStatus(name, status);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-name %s and storage-status %s is found", name,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ShelfResponse> findByStorageCapacityBetween(BigDecimal min, BigDecimal max){
		validateMinAndMax(min, max);
		List<Shelf> items = shelfRepository.findByStorageCapacityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No Shelf for storage-capacity between %s and %s is found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public BigDecimal countAvailableCapacity(Long storageId) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.countAvailableCapacity();
	}

	@Override
	public boolean hasCapacityFor(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.hasCapacityFor(amount);
	}

	@Override
	public void allocateCapacity(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.allocateCapacity(amount);
        storageRepository.save(storage); 
	}

	@Override
	public void releaseCapacity(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.releaseCapacity(amount);
        storageRepository.save(storage);
	}
	
	private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new ValidationException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new ValidationException("Min ne može biti veći od Max");
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
	
	private void validateGoodsList(List<Long> goods) {
		if(goods == null || goods.isEmpty()) {
			throw new ValidationException("Goods list must not be empty nor null");
		}
		for(Long goodId: goods) {
			if(goodId == null || goodId <= 0) {
				throw new ValidationException("Each good ID must be a positive non-null number");
			}
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new ValidationException("Number must be positive");
		}
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
	
	private void validateStorageType(StorageType type) {
		Optional.ofNullable(type)
		.orElseThrow(() -> new ValidationException("StorageType type must not be null"));
	}
	
	private void validateStorageStatus(StorageStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
	}
	
	private Storage fetchStorageId(Long storageId) {
		if(storageId == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	}
	
	private void assignGoodsToShelf(Shelf shelf, List<Long> goodsIds) {
	    if (goodsIds == null || goodsIds.isEmpty()) {
	        shelf.setGoods(new ArrayList<>());
	        return;
	    }
	    validateGoodsList(goodsIds); 
	    List<Goods> goodsList = goodsRepository.findAllById(goodsIds);
	    for (Goods good : goodsList) {
	        good.setShelf(shelf);
	    }
	    shelf.setGoods(goodsList);
	}

}
