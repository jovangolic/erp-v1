package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ProductionOrderErrorException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.ProductionOrderMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;
import com.jovan.erp_v1.util.DateValidator;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderService implements IProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final WorkCenterRepository workCenterRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ProductionOrderResponse create(ProductionOrderRequest request) {
        validateProductionOrderRequest(request);
        ProductionOrder order = productionOrderMapper.toEntity(request);
        ProductionOrder saved = productionOrderRepository.save(order);
        return productionOrderMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ProductionOrderResponse update(Long id, ProductionOrderRequest request) {
        validateProductionOrderRequest(request);
        ProductionOrder o = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ProductionOrderErrorException("ProductionOrder not found with id " + id));
        productionOrderMapper.toUpdateEntity(o, request);
        return productionOrderMapper.toResponse(productionOrderRepository.save(o));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!productionOrderRepository.existsById(id)) {
            throw new ProductionOrderErrorException("ProductionOrder not found with id " + id);
        }
        productionOrderRepository.deleteById(id);
    }

    @Override
    public ProductionOrderResponse findOne(Long id) {
        ProductionOrder o = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ProductionOrderErrorException("ProductionOrder not found with id " + id));
        return new ProductionOrderResponse(o);
    }

    @Override
    public List<ProductionOrderResponse> findAll() {
        return productionOrderRepository.findAll().stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductionOrderResponse findByOrderNumber(String orderNumber) {
    	validateOrderNumberExists(orderNumber);
        ProductionOrder o = productionOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ProductionOrderErrorException("OrderNumber not found for ProductionOrder"));
        return new ProductionOrderResponse(o);
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_Id(Long productId) {
    	fetchProduct(productId);
        return productionOrderRepository.findByProduct_Id(productId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return productionOrderRepository.findByProduct_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
    	validateBigDecimal(currentQuantity);
        return productionOrderRepository.findByProduct_CurrentQuantity(currentQuantity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByStatus(ProductionOrderStatus status) {
    	validateProductionOrderStatus(status);
        return productionOrderRepository.findByStatus(status).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Id(Long workCenterId) {
    	fetchWorkCenter(workCenterId);
        return productionOrderRepository.findByWorkCenter_Id(workCenterId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return productionOrderRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
        return productionOrderRepository.findByWorkCenter_LocationContainingIgnoreCase(location).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Capacity(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_Capacity(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityGreaterThan(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_CapacityGreaterThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityLessThan(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_CapacityLessThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityPlanned(Integer quantityPlanned) {
    	validateInteger(quantityPlanned);
        return productionOrderRepository.findByQuantityPlanned(quantityPlanned).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityProduced(Integer quantityProduced) {
    	validateInteger(quantityProduced);
        return productionOrderRepository.findByQuantityProduced(quantityProduced).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByStartDateBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and End dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("StartDate must not be after end date");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDateBetween(start, end);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDate(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("EndDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByEndDate(endDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByStartDateGreaterThanEqual(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDateGreaterThanEqual(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findOrdersWithStartDateAfterOrEqual(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findOrdersWithStartDateAfterOrEqual(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> searchOrders(
            String productName,
            String workCenterName,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            ProductionOrderStatus status) {
        List<ProductionOrder> filteredOrders = productionOrderRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isBlank()) {
                predicates
                        .add(cb.like(cb.lower(root.get("product").get("name")), "%" + productName.toLowerCase() + "%"));
            }
            if (workCenterName != null && !workCenterName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("workCenter").get("name")),
                        "%" + workCenterName.toLowerCase() + "%"));
            }
            if (startDateFrom != null && startDateTo != null) {
                predicates.add(cb.between(root.get("startDate"), startDateFrom, startDateTo));
            } else if (startDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            } else if (startDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
        return filteredOrders.stream().map(ProductionOrderResponse::new).collect(Collectors.toList());
    }
    
    private void validateInteger(Integer num) {
    	if(num == null || num < 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("String must not be null nor empty");
    	}
    }
    
    private void validateProductionOrderStatus(ProductionOrderStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("ProductionOrderStatus status must not be null");
    	}
    }
    
    private WorkCenter fetchWorkCenter(Long workCenterId) {
    	if(workCenterId == null) {
    		throw new WorkCenterErrorException("WorkCenter ID must not be null");
    	}
    	return workCenterRepository.findById(workCenterId).orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id "+workCenterId));
    }
    
    private Product fetchProduct(Long productId) {
    	if(productId == null) {
    		throw new ProductNotFoundException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id "+productId));
    }
    
    private void validateOrderNumberExists(String orderNumber) {
    	if(orderNumber == null) {
    		throw new IllegalArgumentException("OrderNumber must not be null");
    	}
    	if(!productionOrderRepository.existsByOrderNumber(orderNumber)) {
    		throw new ProductionOrderErrorException("OrderNumber not found "+orderNumber);
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
    private void validateProductionOrderRequest(ProductionOrderRequest request) {
    	validateOrderNumberExists(request.orderNumber());
    	fetchProduct(request.productId());
    	validateInteger(request.quantityPlanned());
    	validateInteger(request.quantityProduced());
    	DateValidator.validateRange(request.startDate(), request.endDate());
    	validateProductionOrderStatus(request.status());
    	fetchWorkCenter(request.workCenterId());
    }

}
