package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.StockTransferErrorException;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StockTransferRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockTransferItemMapper {

    private final ProductRepository productRepository;
    private final StockTransferRepository stockTransferRepository;

    public StockTransferItem toEntity(StockTransferItemRequest request) {
        StockTransferItem item = new StockTransferItem();
        Product product = fetchProductId(request.productId());
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setStockTransfer(fetchStockTransferId(request.stockTransferId()));
        return item;
    }

    public StockTransferItemResponse toResponse(StockTransferItem item) {
    	Objects.requireNonNull(item, "StockTransferItem must not be null");
        return new StockTransferItemResponse(item);
    }

    public List<StockTransferItemResponse> toResponseList(List<StockTransferItem> items) {
    	if(items == null  || items.isEmpty()) {
    		return Collections.emptyList();
    	}
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    private Product fetchProductId(Long productId) {
    	if(productId == null) {
    		throw new NoDataFoundException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new NoSuchProductException("Product not found with id: "+productId));
    }
    
    private StockTransfer fetchStockTransferId(Long stockTransferId) {
    	if(stockTransferId == null) {
    		throw new NoDataFoundException("StockTransfer ID must not be null");
    	}
    	return stockTransferRepository.findById(stockTransferId).orElseThrow(() -> new StockTransferErrorException("StockTransfer not found with id: "+stockTransferId));
    }
}
