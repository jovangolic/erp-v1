package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.mapper.InboundDeliveryMapper;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.InboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.specification.InboundDeliverySpecification;
import com.jovan.erp_v1.request.DeliveryItemInboundRequest;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.InboundDeliverySaveAsRequest;
import com.jovan.erp_v1.search_request.InboundDeliverySearchRequest;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InboundDeliveryService implements InterfejsInboundDeliveryService {

    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final InboundDeliveryMapper inboundDeliveryMapper;
    private final DeliveryItemMapper deliveryItemMapper;
    private final SupplyRepository supplyRepository;
    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public InboundDeliveryResponse create(InboundDeliveryRequest request) {
    	DateValidator.validateNotNull(request.deliveryDate(), "Datum ne sme biti null");
    	Supply supply = validateSupplyId(request.supplyId());
    	Set<Long> productIds = request.itemRequest().stream()
    	        .map(DeliveryItemInboundRequest::productId)
    	        .collect(Collectors.toSet());
    	List<Product> products = productRepository.findAllById(productIds);
    	Map<Long, Product> productMap = products.stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	validateDeliveryStatus(request.status());
    	validateInboundDeliveryItems(request);
        InboundDelivery delivery = inboundDeliveryMapper.toInboundEntity(request,supply,productMap);
        InboundDelivery saved = inboundDeliveryRepository.save(delivery);
        return new InboundDeliveryResponse(saved);
    }

    @Transactional
    @Override
    public InboundDeliveryResponse update(Long id, InboundDeliveryRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	validateInboundDeliveryItems(request);
        InboundDelivery delivery = inboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found " + id));
        DateValidator.validateNotNull(request.deliveryDate(), "Datum ne sme biti null");
    	validateSupplyId(request.supplyId());
    	validateDeliveryStatus(request.status());
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        Supply supply = supplyRepository.findById(request.supplyId())
                .orElseThrow(() -> new SupplyNotFoundException("Supply not found " + request.supplyId()));
        delivery.setSupply(supply);
        delivery.getItems().clear();
        List<DeliveryItem> items = mapInboundDeliveryItems(request.itemRequest(), delivery);
        delivery.getItems().addAll(items);
        InboundDelivery saved = inboundDeliveryRepository.save(delivery);
        return new InboundDeliveryResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!inboundDeliveryRepository.existsById(id)) {
            throw new InboundDeliveryErrorException("InboundDelivery not found " + id);
        }
        inboundDeliveryRepository.deleteById(id);
    }

    @Override
    public InboundDeliveryResponse findByOneId(Long id) {
        InboundDelivery delivery = inboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found " + id));
        return new InboundDeliveryResponse(delivery);
    }

    @Override
    public List<InboundDeliveryResponse> findAll() {
        return inboundDeliveryRepository.findAll().stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findByStatus(DeliveryStatus status) {
    	validateDeliveryStatus(status);
        return inboundDeliveryRepository.findByStatus(status).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findBySupplyId(Long supplyId) {
    	validateSupplyId(supplyId);
        return inboundDeliveryRepository.findBySupplyId(supplyId).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
        return inboundDeliveryRepository.findByDeliveryDateBetween(from, to).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> createAll(List<InboundDeliveryRequest> requests) {
    	if(requests == null || requests.isEmpty()) {
    		return Collections.emptyList();
    	}
    	Set<Long> supplyIds = requests.stream()
    	        .map(InboundDeliveryRequest::supplyId)
    	        .collect(Collectors.toSet());
    	Map<Long, Supply> supplyMap = supplyRepository.findAllById(supplyIds).stream()
    	        .collect(Collectors.toMap(Supply::getId, Function.identity()));
    	Set<Long> productIds = requests.stream()
    	        .flatMap(req -> req.itemRequest().stream())
    	        .map(DeliveryItemInboundRequest::productId)
    	        .collect(Collectors.toSet());
    	Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	List<InboundDelivery> deliveries = requests.stream()
    	        .map(req -> {
    	            Supply supply = supplyMap.get(req.supplyId());
    	            if (supply == null) {
    	                throw new SupplyNotFoundException("Supply not found: " + req.supplyId());
    	            }
    	            return inboundDeliveryMapper.toInboundEntity(req, supply, productMap);
    	        })
    	        .collect(Collectors.toList());
    	 List<InboundDelivery> saved = inboundDeliveryRepository.saveAll(deliveries);
    	 return saved.stream()
    	        .map(inboundDeliveryMapper::toResponse)
    	        .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        inboundDeliveryRepository.deleteAllById(ids);
    }
    
    @Override
	public List<InboundDeliveryResponse> findBySupply_Storage_Id(Long storageId) {
		validateStorageId(storageId);
    	List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_Storage_Id(storageId);
		if(items.isEmpty()){
			String msg = String.format("No InboundDelivery for supply's storage-id %d, is found", storageId);
			throw new NoDataFoundException(msg);
		}
    	return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_Storage_NameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_Storage_NameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s , is found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_Storage_LocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_Storage_LocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s , is found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageCapacity(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageCapacity(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage capacity %s, is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageCapacityGreaterThan(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageCapacityGreaterThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage capacity greater than %s, is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageCapacityLessThan(BigDecimal storageCapacity) {
		validateBigDecimalNonNegative(storageCapacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageCapacityLessThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage capacity less than %s, is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageType(StorageType type) {
		validateStorageType(type);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageType(type);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-type %s, is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageStatus(StorageStatus status) {
		validaStorageStatus(status);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndType(String storageName,
			StorageType type) {
		validateString(storageName);
		validateStorageType(type);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndType(storageName, type);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage-type %s, is found", 
					storageName,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndStatus(String storageName,
			StorageStatus status) {
		validateString(storageName);
		validaStorageStatus(status);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndStatus(storageName, status);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage-status %s, is found",
					storageName,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndType(String storageLocation,
			StorageType type) {
		validateString(storageLocation);
		validateStorageType(type);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageLocationContainingIgnoreCaseAndType(storageLocation, type);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s and storage-type %s, is found", 
					storageLocation,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndStatus(String storageLocation,
			StorageStatus status) {
		validateString(storageLocation);
		validaStorageStatus(status);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageLocationContainingIgnoreCaseAndStatus(storageLocation, status);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s and storage-status %s, is found",
					storageLocation,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacity(String storageName,
			BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimal(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndCapacity(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage capacity %s, is found",
					storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(String storageName,
			BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimal(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage capacity greater than %s, is found",
					storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan(String storageName,
			BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimalNonNegative(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage capacity less than %s, is found",
					storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
			String storageName, String storageLocation) {
		validateString(storageName);
		validateString(storageLocation);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(storageName, storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-name %s and storage-location %s, is found",
					storageName,storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacity(String storageLocation,
			BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimal(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageLocationContainingIgnoreCaseAndCapacity(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s and storage capacity %s, is found",
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(
			String storageLocation, BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimal(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s and storage capacity greater than %s, is found",
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan(
			String storageLocation, BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimalNonNegative(capacity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's storage-location %s and storage capacity less than %s, is found",
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findByStorageWithoutShelvesOrUnknown() {
		List<InboundDelivery> items = inboundDeliveryRepository.findByStorageWithoutShelvesOrUnknown();;
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InboundDelivery for supply's storage without shelves, is found");
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_Quantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_Quantity(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's quantity %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_QuantityGreaterThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_QuantityGreaterThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's quantity greater than %s, is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_QuantityLessThan(BigDecimal quantity) {
		validateBigDecimalNonNegative(quantity);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_QuantityLessThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply's quantity less than %s , is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_QuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_QuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No InboundDelivery for supply quantity between %s and %s, is found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_Updates(LocalDateTime updates) {
		DateValidator.validateNotNull(updates, "Supply updates");
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_Updates(updates);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No InboundDelivery for supply updates %s, found", updates.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_UpdatesAfter(LocalDateTime updates) {
		DateValidator.validateNotInPast(updates, "Supply update after");
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_UpdatesAfter(updates);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No InboundDelivery for supply updates after %s, found", updates.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_UpdatesBefore(LocalDateTime updates) {
		DateValidator.validateNotInFuture(updates, "Supply date before");
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_UpdatesBefore(updates);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No InboundDelivery for supply updates before %s, found", updates.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InboundDeliveryResponse> findBySupply_UpdatesBetween(LocalDateTime updatesFrom, LocalDateTime updatesTo) {
		DateValidator.validateRange(updatesFrom, updatesTo);
		List<InboundDelivery> items = inboundDeliveryRepository.findBySupply_UpdatesBetween(updatesFrom, updatesTo);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No InboundDelivery for supply updates between %s and %s, is found",
					updatesFrom.format(formatter), updatesTo.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inboundDeliveryMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public InboundDeliveryResponse trackInboundDelivery(Long id) {
		List<InboundDelivery> items = inboundDeliveryRepository.trackInboundDelivery(id);
		if(items.isEmpty()) {
			throw new NoDataFoundException("InboundDelivery with id "+id+" not found");
		}
		InboundDelivery saved = items.get(0);
		return new InboundDeliveryResponse(inboundDeliveryRepository.save(saved));
	}

	@Transactional
	@Override
	public InboundDeliveryResponse confirmInboundDelivery(Long id) {
		InboundDelivery items = inboundDeliveryRepository.findById(id).orElseThrow(() -> new ValidationException("InboundDelivery not found with id "+id));
		items.setConfirmed(true);
		items.setInboundStatus(InboundDeliveryStatus.CONFIRMED);
		items.getItems().stream().forEach(i -> i.setConfirmed(true));
		return new InboundDeliveryResponse(inboundDeliveryRepository.save(items));
	}

	@Transactional
	@Override
	public InboundDeliveryResponse cancelInboundDelivery(Long id) {
		InboundDelivery items = inboundDeliveryRepository.findById(id).orElseThrow(() -> new ValidationException("InboundDelivery not found with id "+id));
		if(items.getInboundStatus() != InboundDeliveryStatus.NEW && items.getInboundStatus() != InboundDeliveryStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED inboundDeliveries can be cancelled");
		}
		items.setInboundStatus(InboundDeliveryStatus.CANCELLED);
		return new InboundDeliveryResponse(inboundDeliveryRepository.save(items));
	}

	@Transactional
	@Override
	public InboundDeliveryResponse closeInboundDelivery(Long id) {
		InboundDelivery items = inboundDeliveryRepository.findById(id).orElseThrow(() -> new ValidationException("InboundDelivery not found with id "+id));
		if(items.getInboundStatus() != InboundDeliveryStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED inboundDeliveries can be closed");
		}
		items.setInboundStatus(InboundDeliveryStatus.CLOSED);
		return new InboundDeliveryResponse(inboundDeliveryRepository.save(items));
	}

	@Transactional
	@Override
	public InboundDeliveryResponse changeStatus(Long id, InboundDeliveryStatus status) {
		InboundDelivery items = inboundDeliveryRepository.findById(id).orElseThrow(() -> new ValidationException("InboundDelivery not found with id "+id));
		validateInboundDeliveryStatus(status);
		if(items.getInboundStatus() == InboundDeliveryStatus.CLOSED) {		
			throw new ValidationException("Closed inboundDeliveries cannot change status");
		}
		if(status == InboundDeliveryStatus.CONFIRMED) {
			if(items.getInboundStatus() != InboundDeliveryStatus.NEW) {
				throw new ValidationException("Only NEW inboundDeliveries can be confirmed");
			}
			items.setConfirmed(true);
			items.getItems().forEach(i -> i.setConfirmed(true));
		}
		items.setInboundStatus(status);
		return new InboundDeliveryResponse(inboundDeliveryRepository.save(items));
	}

	@Transactional
	@Override
	public InboundDeliveryResponse saveInboundDelivery(InboundDeliveryRequest request) {
		InboundDelivery ind = InboundDelivery.builder()
				.id(request.id())
				.deliveryDate(request.deliveryDate())
				.supply(validateSupplyId(request.supplyId()))
				.status(request.status())
				.items(mapInboundDeliveryItems(request.itemRequest(), validateInboundDeliveryId(request.id())))
				.build();
		InboundDelivery saved = inboundDeliveryRepository.save(ind);
		return new InboundDeliveryResponse(saved); 
	}
	
	private final AbstractSaveAsService<InboundDelivery, InboundDeliveryResponse> saveAsHelper = new AbstractSaveAsService<InboundDelivery, InboundDeliveryResponse>() {
		
		@Override
		protected InboundDeliveryResponse toResponse(InboundDelivery entity) {
			return new InboundDeliveryResponse(entity);
		}
		
		@Override
		protected JpaRepository<InboundDelivery, Long> getRepository() {
			return inboundDeliveryRepository;
		}
		
		@Override
		protected InboundDelivery copyAndOverride(InboundDelivery source, Map<String, Object> overrides) {
			return InboundDelivery.builder()
					.supply(validateSupplyId(source.getSupply().getId()))
					.status(source.getStatus())
					.confirmed(source.getConfirmed())
					.inboundStatus(source.getInboundStatus())
					.build();
		}
	};
	
	private final AbstractSaveAllService<InboundDelivery, InboundDeliveryResponse> saveAllHelper = new AbstractSaveAllService<InboundDelivery, InboundDeliveryResponse>() {
		
		@Override
		protected Function<InboundDelivery, InboundDeliveryResponse> toResponse() {
			return InboundDeliveryResponse::new;
		}
		
		@Override
		protected JpaRepository<InboundDelivery, Long> getRepository() {
			return inboundDeliveryRepository;
		}
	};

	@Transactional
	@Override
	public InboundDeliveryResponse saveAs(InboundDeliverySaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.supplyId() != null) overrides.put("Supply-ID", request.supplyId());
		if(request.status() != null) overrides.put("Status", request.status());
		if(request.confirmed() != null) overrides.put("Confirmed", request.confirmed());
		if(request.inboundStatus() != null) overrides.put("Inbound-Status", request.inboundStatus());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<InboundDeliveryResponse> saveAll(List<InboundDeliveryRequest> request) {
		List<InboundDelivery> items = request.stream()
				.map(req -> InboundDelivery.builder()
						.id(req.id())
						.supply(validateSupplyId(req.supplyId()))
						.status(req.status())
						.items(mapInboundDeliveryItems(req.itemRequest(), validateInboundDeliveryId(req.id())))
						.build())
				.toList();
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<InboundDeliveryResponse> generalSearch(InboundDeliverySearchRequest request) {
		Specification<InboundDelivery> spec = InboundDeliverySpecification.fromRequest(request);
		List<InboundDelivery> items = inboundDeliveryRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InboundDelivery for given criteria found");
		}
		return items.stream().map(InboundDeliveryResponse::new).collect(Collectors.toList());
	}
    
    private void validateDeliveryStatus(DeliveryStatus status) {
    	if(status == null) {
    		throw new ValidationException("Status za DeliveryStatus nije pronadjen");
    	}
    }
    
    private InboundDelivery validateInboundDeliveryId(Long id) {
    	if(id == null) {
    		throw new ValidationException("InboundDelivery ID must not be null");
    	}
    	return inboundDeliveryRepository.findById(id).orElseThrow(() -> new ValidationException("InboundDelivery no tfound with id "+id));
    }
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new IllegalArgumentException("Supply Id ne sme biti null.");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private List<DeliveryItem> mapInboundDeliveryItems(List<DeliveryItemInboundRequest> itemRequests, InboundDelivery delivery) {
    	if (itemRequests == null || itemRequests.isEmpty()) {
    	    return Collections.emptyList();
    	}
    	Set<Long> productIds = itemRequests.stream()
    	        .map(DeliveryItemInboundRequest::productId)
    	        .collect(Collectors.toSet());
    	Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	if (productMap.size() != productIds.size()) {
    	    Set<Long> notFound = new HashSet<>(productIds);
    	    notFound.removeAll(productMap.keySet());
    	    throw new ProductNotFoundException("Products not found: " + notFound);
    	}
        return itemRequests.stream()
            .map(itemReq -> {
            	Product product = productMap.get(itemReq.productId());
            	if (product == null) {
                    throw new ProductNotFoundException("Product not found: " + itemReq.productId());
                }
                DeliveryItem item = deliveryItemMapper.toInEntity(itemReq,product);
                item.setInboundDelivery(delivery);
                return item;
            })
            .collect(Collectors.toList());
    }
    
    private void validateInboundDeliveryItems(InboundDeliveryRequest requests) {
    	List<DeliveryItemInboundRequest> req = requests.itemRequest();
    	if(req == null || req.isEmpty()) {
    		throw new ValidationException("Request lista ne sme biti prazna");
    	}
    	for(DeliveryItemInboundRequest item: req) {
    		if(item.productId() == null) {
    			throw new NoSuchProductException("Product ID ne sme biti null");
    		}
    		if(item == null || item.quantity().compareTo(BigDecimal.ZERO) <= 0) {
    			throw new ValidationException("Kolicina mora biti veca od nule");
    		}
    		if(item.inboundDeliveryId() == null) {
    			throw new ValidationException("InboundDelivery ID ne sme biti null");
    		}
    	}
    	//provera duplikata
    	Set<Long> productIds = new HashSet<Long>();
    	for(DeliveryItemInboundRequest item: req) {
    		if(!productIds.add(item.productId())) {
    			throw new ValidationException("Duplikat pronadjen za proizvod ID: " + item.productId());
    		}
    	}
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
    
    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException("Tekstualni karakter ne sme biti null ili prazan");
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
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

	private void validateStorageType(StorageType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("StorageType type must not be null"));
	}
	
	private void validaStorageStatus(StorageStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
	}
    
	private Storage validateStorageId(Long storageId) {
		if(storageId == null) {
			throw new ValidationException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
	}
	
	private void validateInboundDeliveryStatus(InboundDeliveryStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("InboundDeliveryStatus status must not be null"));
	}

}


	