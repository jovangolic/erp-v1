package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.DeliveryItemErrorException;
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.DeliveryItemRepository;
import com.jovan.erp_v1.repository.InboundDeliveryRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryItemService implements IDeliveryItemService {

    private final DeliveryItemRepository deliveryItemRepository;
    private final ProductRepository productRepository;
    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final OutboundDeliveryRepository outboundDeliveryRepository;
    private final DeliveryItemMapper deliveryItemMapper;
    private final BuyerRepository buyerRepository;
    private final SupplyRepository supplyRepository;
    private final ShelfRepository shelfRepository;
    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public DeliveryItemResponse create(DeliveryItemRequest request) {
    	Product product = validateProductId(request.productId());
        InboundDelivery inbound = validateInboundDelivery(request.inboundDeliveryId());
        OutboundDelivery outbound = validateOutboundDelivery(request.outboundDeliveryId());
        validateQuantity(request.quantity());
        DeliveryItem item = DeliveryItem.builder()
                .product(product)
                .inboundDelivery(inbound)
                .outboundDelivery(outbound)
                .quantity(request.quantity())
                .build();
        DeliveryItem saved = deliveryItemRepository.save(item);
        return new DeliveryItemResponse(saved);
    }

    @Transactional
    @Override
    public DeliveryItemResponse update(Long id, DeliveryItemRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        DeliveryItem item = deliveryItemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("DeliveryItem not found " + id));
        Product product = validateProductId(request.productId());
        InboundDelivery inbound = validateInboundDelivery(request.inboundDeliveryId());
        OutboundDelivery outbound = validateOutboundDelivery(request.outboundDeliveryId());
        validateQuantity(request.quantity());
        deliveryItemMapper.toDeliveryItemEntityUpdate(item, request, product, inbound, outbound);
        DeliveryItem updated = deliveryItemRepository.save(item);
        return new DeliveryItemResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!deliveryItemRepository.existsById(id)) {
            throw new DeliveryItemErrorException("DeliveryItem not found " + id);
        }
        deliveryItemRepository.deleteById(id);
    }

    @Override
    public DeliveryItemResponse findById(Long id) {
        DeliveryItem item = deliveryItemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("DeliveryItem not found " + id));
        return new DeliveryItemResponse(item);
    }

    @Override
    public List<DeliveryItemResponse> findAll() {
    	List<DeliveryItem> items = deliveryItemRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of DeliveryItem is empty");
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantity(BigDecimal quantity) {
    	validateQuantity(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantity(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity  %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateQuantity(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantityGreaterThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity greater than %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantityLessThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity less than %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_DeliveryDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No DeliveryItem with inbound-delivery date between %s and %s, is found",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_DeliveryDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No DeliveryItem with outbound-delivery date between %s and %s, is found",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryItemResponse findByProduct(Long productId) {
    	validateProductId(productId);
        DeliveryItem item = deliveryItemRepository.findByProductId(productId);
        if (item == null) {
            throw new ProductNotFoundException("Product not found " + productId);
        }
        return new DeliveryItemResponse(item);
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDeliveryId(Long inboundId) {
    	validateInboundDeliveryId(inboundId);
    	List<DeliveryItem> items = deliveryItemRepository.findByInboundDeliveryId(inboundId);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with inbound-delivery id %d, is found", inboundId);
    		throw new NoDataFoundException(msg);
    	}
        return deliveryItemRepository.findByInboundDeliveryId(inboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId) {
    	validateOutboundDeliveryId(outboundId);
    	List<DeliveryItem> items = deliveryItemRepository.findByOutboundDeliveryId(outboundId);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with outbound-delivery id %d, is found", outboundId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByProduct_Name(String name) {
    	validateString(name);
    	List<DeliveryItem> items = deliveryItemRepository.findByProduct_Name(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with product name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }
    
    //nove metode
    
    @Override
	public List<DeliveryItemResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product current quantity %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product current quantity greater than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product current quantity less than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product unit measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product supplier type %s is found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product storage type %s is found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product goods type %s is found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_Id(Long storageId) {
		validateStorageId(storageId);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_Id(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product storage id %d is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_NameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_NameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product storage name %s is found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_LocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_LocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product storage location %s is found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_Capacity(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_Capacity(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with product for storage capacity %s is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_CapacityGreaterThan(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_CapacityGreaterThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with product for storage capacity greater than %s is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_CapacityLessThan(BigDecimal storageCapacity) {
		validateBigDecimalNonNegative(storageCapacity);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_CapacityLessThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with product for storage capacity less than %s is found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_StorageType(StorageType type) {
		validateStorageType(type);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_StorageType(type);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with product for storage type %s is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProduct_Storage_StorageStatus(StorageStatus status) {
		validateStorageStatus(status);
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_StorageStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with product for storage status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public BigDecimal calculateUsedCapacityByStorageId(Long storageId) {
		List<DeliveryItem> items = deliveryItemRepository.findByProduct_Storage_Id(storageId);
        return items.stream()
            .map(DeliveryItem::getQuantity)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal sumInboundQuantityByStorageId(Long storageId) {
		validateStorageId(storageId);
		return deliveryItemRepository.sumInboundQuantityByStorageId(storageId);
	}

	@Override
	public BigDecimal sumOutboundQuantityByStorageId(Long storageId) {
		validateStorageId(storageId);
		return deliveryItemRepository.sumOutboundQuantityByStorageId(storageId);
	}

	@Override
	public List<DeliveryItemResponse> findByProductSupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<DeliveryItem> items = deliveryItemRepository.findByProductSupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product supply id %d, is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProductShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<DeliveryItem> items = deliveryItemRepository.findByProductShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product shelf id %d, is found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProductShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<DeliveryItem> items = deliveryItemRepository.findByProductShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product shelf row %d is found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByProductShelfCols(Integer cols) {
		validateInteger(cols);
		List<DeliveryItem> items = deliveryItemRepository.findByProductShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for product shelf cols %d is found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findProductsWithoutShelf() {
		List<DeliveryItem> items = deliveryItemRepository.findProductsWithoutShelf();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Deliveryitem for products without shelf, is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_DeliveryDate(LocalDate start) {
		DateValidator.validateNotInPast(start, "Start date");
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_DeliveryDate(start);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No DeliveryItem with inboundDelivery date %s if found", start.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Supply_Id(Long supplyId) {
		validateSupplyId(supplyId);
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Supply_Id(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveItem with inboundDelivery for supply id %d, is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Status(DeliveryStatus status) {
		validateDeliveryStatus(status);
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for inboundDelivery status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Status_Pending() {
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Status_Pending();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for InboundDelivery status equal to 'PENDING' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Status_InTransit() {
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Status_InTransit();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for InboundDelivery status equal to 'IN_TRANSIT' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Status_Delivered() {
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Status_Delivered();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for InboundDelivery status equal to 'DELIVERED' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByInboundDelivery_Status_Cancelled() {
		List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_Status_Cancelled();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for InboundDelivery status equal to 'CANCELLED' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate) {
		DateValidator.validateNotInPast(deliveryDate, "Delivery date");
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_DeliveryDate(deliveryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No DeliveryItem for outboundDelivery date %s is found", 
					deliveryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_Status(DeliveryStatus status) {
		validateDeliveryStatus(status);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem for outboundeDelivery status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_Status_Pending() {
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_Status_Pending();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for outboundDelivery status equal to 'PENDING' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_Status_InTransit() {
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_Status_InTransit();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for outboundDelivery status equal to 'IN_TRANSIT' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_Status_Delivered() {
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_Status_Delivered();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for outboundDelivery status equal to 'DELIVERED' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_Status_Cancelled() {
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_Status_Cancelled();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No DeliveryItem for outboundDelivery status equal to 'CANCELLED' is found");
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerId(Long buyerId) {
		validateBuyerId(buyerId);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerId(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer id %d, is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerNameContainingIgnoreCase(String companyName) {
		validateString(companyName);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerNameContainingIgnoreCase(companyName);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer company-name %s, is found", companyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerAddress(String address) {
		validateString(address);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerAddress(address);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer address %s, is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerEmailLikeIgnoreCase(String buyerEmail) {
		validateString(buyerEmail);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerEmailLikeIgnoreCase(buyerEmail);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer email %s, is found", buyerEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer phone-number %s, is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<DeliveryItemResponse> findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase(
			String contactPerson) {
		validateString(contactPerson);
		List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase(contactPerson);
		if(items.isEmpty()) {
			String msg = String.format("No DeliveryItem with outboundDelivery for buyer contact-person %s is found", contactPerson);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(DeliveryItemResponse::new).collect(Collectors.toList());
	}
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }
    
    private void validateQuantity(BigDecimal quantity) {
    	if(quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Quantity mora biti pozitivan broj");
    	}
    }
    
    private Product validateProductId(Long productId) {
		if(productId == null) {
	    		throw new NoSuchProductException("Product ID "+productId+" ne postoji");
	    }
		return productRepository.findById(productId).orElseThrow(() -> new ValidationException("Product not found with id "+productId));
	}
    
    private void validateOutboundDeliveryId(Long outboundDeliveryId) {
		if(outboundDeliveryId == null) {
	    		throw new OutboundDeliveryErrorException("OutboundDelivery ID "+outboundDeliveryId+" ne postoji");
	    	}
	}
    
    private void validateInboundDeliveryId(Long inboundDeliveryId) {
		if(inboundDeliveryId == null) {
	    		throw new InboundDeliveryErrorException("InboundDelivery ID "+inboundDeliveryId+" ne postoji");
	    	}
	}
    
    private void validateGoodsType(GoodsType goodsType) {
    	Optional.ofNullable(goodsType)
    		.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
    }
    
    private void validateStorageType(StorageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Unit za StorageType ne sme biti null");
        }
    }
    
    private void validateStorageStatus(StorageStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
    }
    
    private void validateDeliveryStatus(DeliveryStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("DeliveryStatus status must not be null"));
    }
    
    private void validateSupplierType(SupplierType supplierType) {
    	Optional.ofNullable(supplierType)
    		.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
    }
    
    private void validateUnitMeasure(UnitMeasure unitMeasure) {
    	Optional.ofNullable(unitMeasure)
    		.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
    }
    
    private Buyer validateBuyerId(Long buyerId) {
    	if(buyerId == null) {
    		throw new ValidationException("Buyer ID must not be null");
    	}
    	return buyerRepository.findById(buyerId).orElseThrow(() -> new ValidationException("Buyer not found with id "+buyerId));
    }
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new ValidationException("Supply ID must not be null");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private Shelf validateShelfId(Long shelfId) {
    	if(shelfId == null) {
    		throw new ValidationException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
    }
    
    private Storage validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("Storage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private InboundDelivery validateInboundDelivery(Long inboundId) {
    	if(inboundId == null) {
    		throw new ValidationException("InboundDelivery ID must not be null");
    	}
    	return inboundDeliveryRepository.findById(inboundId).orElseThrow(() -> new ValidationException("InboundDelivery not found with id "+inboundId));
    }
    
    private OutboundDelivery validateOutboundDelivery(Long outId) {
    	if(outId == null) {
    		throw new ValidationException("OutboundDelivery ID must not be null");
    	}
    	return outboundDeliveryRepository.findById(outId).orElseThrow(() -> new ValidationException("OutboundDelivery not found with id "+outId));
    }
    
    private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

}
