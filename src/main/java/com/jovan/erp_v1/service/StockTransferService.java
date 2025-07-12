package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.StockTransferErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.StockTransferItemMapper;
import com.jovan.erp_v1.mapper.StockTransferMapper;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StockTransferRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferService implements IStockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;
    private final StorageRepository storageRepository;
    private final StockTransferItemMapper stockTransferItemMapper;

    @Transactional
    @Override
    public StockTransferResponse create(StockTransferRequest request) {
    	validateCreateStockTransferRequest(request);
        StockTransfer stock = stockTransferMapper.toEntity(request);
        StockTransfer saved = stockTransferRepository.save(stock);
        return stockTransferMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public StockTransferResponse update(Long id, StockTransferRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        StockTransfer stock = stockTransferRepository.findById(id)
                .orElseThrow(() -> new StockTransferErrorException("StockTransfer not found " + id));
        validateUpdateStockTransferRequest(request);
        Storage from = fetchFromStorageId(request.fromStorageId());
        Storage to = fetchToStorageId(request.toStorageId());
        stock.setTransferDate(request.transferDate());
        stock.setFromStorage(from);
        stock.setToStorage(to);
        stock.setStatus(request.status());
        stock.getItems().clear();
        List<StockTransferItem> items = request.itemRequest().stream()
                .map(itemReq -> {
                    StockTransferItem item = stockTransferItemMapper.toEntity(itemReq);
                    item.setStockTransfer(stock);
                    return item;
                })
                .collect(Collectors.toList());
        stock.setItems(items);
        StockTransfer saved = stockTransferRepository.save(stock);
        return stockTransferMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!stockTransferRepository.existsById(id)) {
            throw new StockTransferErrorException("StockTransfer not found " + id);
        }
        stockTransferRepository.deleteById(id);
    }

    @Override
    public StockTransferResponse findOne(Long id) {
        StockTransfer stock = stockTransferRepository.findById(id)
                .orElseThrow(() -> new StockTransferErrorException("StockTransfer not found " + id));
        return new StockTransferResponse(stock);
    }

    @Override
    public List<StockTransferResponse> findAll() {
    	List<StockTransfer> items = stockTransferRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No stock-transfer in list found");
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByStatus(TransferStatus status) {
    	if(!stockTransferRepository.existsByStatus(status)) {
    		String msg = String.format("No stock-transfer found with status equal to: %s", status);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByStatus(status);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByTransferDate(LocalDate transferDate) {
    	DateValidator.validateNotInFuture(transferDate, "Transfer date");
    	List<StockTransfer> items = stockTransferRepository.findByTransferDate(transferDate);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No stock-transfer found with date : %s", transferDate.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByTransferDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
    	List<StockTransfer> items = stockTransferRepository.findByTransferDateBetween(from, to);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm.DD.yyyy");
    		String msg = String.format("No stock transfer found with date between %s and %s", from.format(formatter),to.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorageId(Long fromStorageId) {
    	if(!stockTransferRepository.existsByFromStorage_Id(fromStorageId)) {
    		String msg = String.format("No stock-transfer found with from-storage id: %d", fromStorageId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByFromStorageId(fromStorageId);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorageId(Long toStorageId) {
    	if(!stockTransferRepository.existsByToStorage_Id(toStorageId)) {
    		String msg = String.format("No stock-transfer found with to-storage id: %d", toStorageId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByToStorageId(toStorageId);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Name(String fromStorageName) {
    	validateString(fromStorageName);
    	List<StockTransfer> items = stockTransferRepository.findByFromStorage_Name(fromStorageName);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock-transfer found with from-storage name: %s", fromStorageName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Location(String fromLocation) {
    	validateString(fromLocation);
    	List<StockTransfer> items = stockTransferRepository.findByFromStorage_Name(fromLocation);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock-transfer found with from-storage location: %s", fromLocation);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Name(String toStorageName) {
    	validateString(toStorageName);
    	List<StockTransfer> items = stockTransferRepository.findByToStorage_Name(toStorageName);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock-transfer found with to-storage name: %s", toStorageName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Location(String toLocation) {
    	validateString(toLocation);
    	List<StockTransfer> items = stockTransferRepository.findByToStorage_Location(toLocation);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock-transfer found with to-storage location: %s", toLocation);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Type(StorageType fromStorageType) {
    	if(!stockTransferRepository.existsByFromStorage_Type(fromStorageType)) {
    		String msg = String.format("No stock-transfer found with from-storage status: %s", fromStorageType);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> itemsList = stockTransferRepository.findByFromStorage_Type(fromStorageType);
        return itemsList.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Type(StorageType toStorageType) {
    	if(!stockTransferRepository.existsByToStorage_Type(toStorageType)) {
    		String msg = String.format("No stock-transfer found with to-storage status: %s", toStorageType);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByToStorage_Type(toStorageType);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByStatusAndDateRange(TransferStatus status, LocalDate startDate,
            LocalDate endDate) {
    	DateValidator.validateRange(startDate, endDate);
    	if(!stockTransferRepository.existsByStatus(status)) {
    		String msg = String.format("No stock transfers found with transfer status: %s", status);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByStatusAndDateRange(status, startDate, endDate);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromAndToStorageType(StorageType fromType, StorageType toType) {
    	if(!stockTransferRepository.existsByFromAndToStorageType(fromType, toType)) {
    		String msg = String.format("No StockTransfers found with from-storage type '%s' and to-storage type '%s'", fromType, toType);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransfer> items = stockTransferRepository.findByFromAndToStorageType(fromType, toType);
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> searchFromStorageByNameAndLocation(String name, String location) {
    	validateString(location);
    	validateString(name);
    	List<StockTransfer> items = stockTransferRepository.searchFromStorageByNameAndLocation(name, location);
    	if(items.isEmpty()) {
    		String msg = String.format("No name %s and location %s found for from-storage",
    			    name, location);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }
    
    //nove metode

	@Override
	public List<StockTransferResponse> findByFromStorage_Capacity(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByFromStorage_Capacity(capacity)) {
			String msg = String.format("No capacity found for from-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_Capacity(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_Capacity(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByToStorage_Capacity(capacity)) {
			String msg = String.format("No capacity found for to-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByToStorage_Capacity(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByFromStorage_CapacityGreaterThan(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByFromStorage_CapacityGreaterThan(capacity)) {
			String msg = String.format("No capacity greater than found in from-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityGreaterThan(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_CapacityGreaterThan(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByToStorage_CapacityGreaterThan(capacity)) {
			String msg = String.format("No capacity greater than found in to-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByToStorage_CapacityGreaterThan(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByFromStorage_CapacityLessThan(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByFromStorage_CapacityLessThan(capacity)) {
			String msg = String.format("No capacity less than found in from-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityLessThan(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_CapacityLessThan(BigDecimal capacity) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByToStorage_CapacityLessThan(capacity)) {
			String msg = String.format("No capacity less than found in to-storage %s", capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByToStorage_CapacityLessThan(capacity);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByFromStorage_CapacityAndType(BigDecimal capacity, StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByFromStorage_Type(type)) {
			String msg = String.format("No storage type %s for from-storage and capacity %s found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityGreaterThanAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_CapacityAndType(BigDecimal capacity, StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsByToStorage_Type(type)) {
			String msg = String.format("No storage type %s for to-storage and capacity %s  found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityGreaterThanAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByFromStorage_CapacityGreaterThanAndType(BigDecimal capacity,
			StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsFromStorageByCapacityGreaterThanAndType(capacity, type)) {
			String msg = String.format("No storage type %s for from-storage and capacity %s greater than found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityGreaterThanAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_CapacityGreaterThanAndType(BigDecimal capacity,
			StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsToStorageByCapacityGreaterThanAndType(capacity, type)) {
			String msg = String.format("No storage type %s for to-storage and capacity %s greater than found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByToStorage_CapacityGreaterThanAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByFromStorage_CapacityLessThanAndType(BigDecimal capacity,
			StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsFromStorageByCapacityLessThanAndType(capacity, type)) {
			String msg = String.format("No storage type %s for from-storage and capacity %s less than found", type,capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByFromStorage_CapacityLessThanAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StockTransferResponse> findByToStorage_CapacityLessThanAndType(BigDecimal capacity, StorageType type) {
		validaBigDecimal(capacity);
		if(!stockTransferRepository.existsToStorageByCapacityLessThanAndType(capacity, type)) {
			String msg = String.format("No storage type %s for to-storage and capacity %s less than found", type, capacity);
			throw new NoDataFoundException(msg);
		}
		List<StockTransfer> items = stockTransferRepository.findByToStorage_CapacityAndType(capacity, type);
		return items.stream()
				.map(stockTransferMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor null");
		}
	}
	
	private void validaBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
	
	private Storage fetchFromStorageId(Long fromStorageId) {
    	if(fromStorageId == null) {
    		throw new NoDataFoundException("FromStorage ID must not be null");
    	}
    	return storageRepository.findById(fromStorageId).orElseThrow(() -> new NoDataFoundException("FromStorage not found with id "+fromStorageId));
    }
    
    private Storage fetchToStorageId(Long toStorageId) {
    	if(toStorageId == null) {
    		throw new NoDataFoundException("ToStorage ID must not be null");
    	}
    	return storageRepository.findById(toStorageId).orElseThrow(() -> new NoDataFoundException("ToStorage not found with id "+toStorageId));
    }
    
    private void validateUpdateStockTransferRequest(StockTransferRequest request) {
    	DateValidator.validateNotInFuture(request.transferDate(), "Transfer date");
    	validateTransferStatus(request.status());
    	validateStockTransferItemRequest(request.itemRequest());
    }
    
    private void validateCreateStockTransferRequest(StockTransferRequest request) {
    	DateValidator.validateNotInFuture(request.transferDate(), "Transfer date");
    	fetchFromStorageId(request.fromStorageId());
    	fetchToStorageId(request.toStorageId());
    	validateTransferStatus(request.status());
    	validateStockTransferItemRequest(request.itemRequest());
    }
    
    
    private void validateTransferStatus(TransferStatus status) {
    	if(status == null) {
    		throw new ValidationException("TransferStatus status must not be null");
    	}
    }
    
    private void validateStockTransferItemRequest(List<StockTransferItemRequest> request) {
    	if(request == null || request.isEmpty()) {
    		throw new ValidationException("StockTransferItemRequest list must not be null or empty");
    	}
    	for(StockTransferItemRequest item: request) {
    		if(item == null) {
    			throw new ValidationException("StockTransferItem must not be null");
    		}
    		validateStockTransferItem(item);
    	}
    }
    
    private void validateStockTransferItem(StockTransferItemRequest item) {
    	if(item.productId() == null) {
    		throw new ValidationException("Product ID must not be null");
    	}
    	if(item.quantity() == null || item.quantity().compareTo(BigDecimal.ZERO) <= 0) {
    		throw new ValidationException("Quantity must be positive number");
    	}
    }
}
