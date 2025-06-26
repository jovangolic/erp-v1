package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.exception.ProductionOrderErrorException;
import com.jovan.erp_v1.mapper.ProductionOrderMapper;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderService implements IProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductionOrderMapper productionOrderMapper;

    @Transactional
    @Override
    public ProductionOrderResponse create(ProductionOrderRequest request) {
        if (request.orderNumber() == null) {
            throw new IllegalArgumentException("OrderNumber must not be null");
        }
        if (request.productId() == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (request.workCenterId() == null) {
            throw new IllegalArgumentException("WorkCenterId must not be null");
        }
        if (request.quantityPlanned() == null || request.quantityProduced() == null) {
            throw new IllegalArgumentException("qualityPlanned and quantityProduced must not be null");
        }
        if (request.endDate() != null && request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("StartDate must not be after endDate");
        }
        if (request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("StartDate must not be after endDate");
        }
        ProductionOrder order = productionOrderMapper.toEntity(request);
        ProductionOrder saved = productionOrderRepository.save(order);
        return productionOrderMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ProductionOrderResponse update(Long id, ProductionOrderRequest request) {
        if (request.orderNumber() == null) {
            throw new IllegalArgumentException("OrderNumber must not be null");
        }
        if (request.productId() == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (request.workCenterId() == null) {
            throw new IllegalArgumentException("WorkCenterId must not be null");
        }
        if (request.quantityPlanned() == null || request.quantityProduced() == null) {
            throw new IllegalArgumentException("qualityPlanned and quantityProduced must not be null");
        }
        if (request.endDate() != null && request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("StartDate must not be after endDate");
        }
        if (request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("StartDate must not be after endDate");
        }
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
        ProductionOrder o = productionOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ProductionOrderErrorException("OrderNumber not found for ProductionOrder"));
        return new ProductionOrderResponse(o);
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_Id(Long productId) {
        return productionOrderRepository.findByProduct_Id(productId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_NameContainingIgnoreCase(String name) {
        return productionOrderRepository.findByProduct_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_CurrentQuantity(Double currentQuantity) {
        return productionOrderRepository.findByProduct_CurrentQuantity(currentQuantity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByStatus(ProductionOrderStatus status) {
        return productionOrderRepository.findByStatus(status).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Id(Long workCenterId) {
        return productionOrderRepository.findByWorkCenter_Id(workCenterId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
        return productionOrderRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
        return productionOrderRepository.findByWorkCenter_LocationContainingIgnoreCase(location).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Capacity(Integer capacity) {
        return productionOrderRepository.findByWorkCenter_Capacity(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityGreaterThan(Integer capacity) {
        return productionOrderRepository.findByWorkCenter_CapacityGreaterThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityLessThan(Integer capacity) {
        return productionOrderRepository.findByWorkCenter_CapacityLessThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityPlanned(Integer quantityPlanned) {
        return productionOrderRepository.findByQuantityPlanned(quantityPlanned).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityProduced(Integer quantityProduced) {
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

}
