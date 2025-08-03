package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class StockTransferItemMapper extends AbstractMapper<StockTransferItemRequest> {


    public StockTransferItem toEntity(StockTransferItemRequest request,Product product, StockTransfer stock) {
    	Objects.requireNonNull(request, "StockTransferItemRequest must not be null");
    	Objects.requireNonNull(product, "Product must not be null");
    	Objects.requireNonNull(stock, "StockTransfer must not be null");
    	validateIdForCreate(request, StockTransferItemRequest::id);
        StockTransferItem item = new StockTransferItem();
        item.setId(request.id());
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setStockTransfer(stock);
        return item;
    }
    
    public StockTransferItem toEntityUpdate(StockTransferItem item,StockTransferItemRequest request,Product product, StockTransfer stock) {
    	Objects.requireNonNull(item, "StockTransfer must not be null");
    	Objects.requireNonNull(request, "StockTransferItemRequest must not be null");
    	Objects.requireNonNull(product, "Product must not be null");
    	Objects.requireNonNull(stock, "StockTransfer must not be null");
    	item.setProduct(product);
    	item.setStockTransfer(stock);
    	item.setQuantity(request.quantity());
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
    
}
