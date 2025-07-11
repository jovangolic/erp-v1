package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.StockTransferItemErrorException;
import com.jovan.erp_v1.mapper.StockTransferItemMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StockTransferItemRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferItemService implements IStockTransferItemService {

    private final StockTransferItemRepository stockTransferItemRepository;
    private final StockTransferItemMapper stockTransferItemMapper;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public StockTransferItemResponse create(StockTransferItemRequest request) {
        StockTransferItem item = stockTransferItemMapper.toEntity(request);
        StockTransferItem saved = stockTransferItemRepository.save(item);
        return stockTransferItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public StockTransferItemResponse update(Long id, StockTransferItemRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        StockTransferItem item = stockTransferItemRepository.findById(id)
                .orElseThrow(() -> new StockTransferItemErrorException("StockTransferItem not found " + id));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found " + request.productId()));
        if (request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (!item.getProduct().getId().equals(request.productId())) {
            throw new IllegalStateException("Changing product is not allowed once item is created.");
        }
        item.setProduct(product);
        ;
        item.setQuantity(request.quantity());
        StockTransferItem saved = stockTransferItemRepository.save(item);
        return stockTransferItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!stockTransferItemRepository.existsById(id)) {
            throw new StockTransferItemErrorException("StockTransferItem not found " + id);
        }
        stockTransferItemRepository.deleteById(id);
    }

    @Override
    public StockTransferItemResponse findOne(Long id) {
        StockTransferItem item = stockTransferItemRepository.findById(id)
                .orElseThrow(() -> new StockTransferItemErrorException("StockTransferItem not found " + id));
        return new StockTransferItemResponse(item);
    }

    @Override
    public List<StockTransferItemResponse> findAll() {
        return stockTransferItemRepository.findAll().stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<StockTransferItemResponse> findByProductId(Long productId) {
        return stockTransferItemRepository.findByProductId(productId).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByProduct_Name(String name) {
        return stockTransferItemRepository.findByProduct_Name(name).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByProduct_CurrentQuantity(Double currentQuantity) {
        return stockTransferItemRepository.findByProduct_CurrentQuantity(currentQuantity).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantity(BigDecimal quantity) {
        return stockTransferItemRepository.findByQuantity(quantity).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantityLessThan(BigDecimal quantity) {
        return stockTransferItemRepository.findByQuantityLessThan(quantity).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        return stockTransferItemRepository.findByQuantityGreaterThan(quantity).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransferId(Long stockTransferId) {
        return stockTransferItemRepository.findByStockTransferId(stockTransferId).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId) {
        return stockTransferItemRepository.findByStockTransfer_FromStorageId(fromStorageId).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId) {
        return stockTransferItemRepository.findByStockTransfer_ToStorageId(toStorageId).stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }
    
    private Product fetchProductId(Long productId) {
    	if(productId == null) {
    		throw new NoDataFoundException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new NoSuchProductException("Product not found with id: "+productId));
    }

}
