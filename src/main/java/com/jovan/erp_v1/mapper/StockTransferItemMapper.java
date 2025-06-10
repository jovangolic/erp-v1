package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockTransferItemMapper {

    private final ProductRepository productRepository;

    public StockTransferItem toEntity(StockTransferItemRequest request) {
        StockTransferItem item = new StockTransferItem();
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found"));
        item.setProduct(product);
        item.setQuantity(request.quantity());
        return item;
    }

    public StockTransferItemResponse toResponse(StockTransferItem item) {
        return new StockTransferItemResponse(item);
    }

    public List<StockTransferItemResponse> toResponseList(List<StockTransferItem> items) {
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
