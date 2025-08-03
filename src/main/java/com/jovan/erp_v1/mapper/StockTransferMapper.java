package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class StockTransferMapper extends AbstractMapper<StockTransferRequest> {

    public StockTransfer toEntity(StockTransferRequest request, Storage from, Storage to) {
    	Objects.requireNonNull(request, "StockTransferRequest must not be null");
    	Objects.requireNonNull(from, "FromStorage must not be null");
    	Objects.requireNonNull(to, "ToStorage must not be null");
    	validateIdForCreate(request, StockTransferRequest::id);
        StockTransfer stock = new StockTransfer();
        stock.setId(request.id());
        stock.setFromStorage(from);
        stock.setToStorage(to);
        stock.setStatus(request.status());
        stock.setItems(new ArrayList<>());
        return stock;
    }
    
    public StockTransfer toEntityUpdate(StockTransfer stock, StockTransferRequest request, Storage from, Storage to) {
    	Objects.requireNonNull(stock, "StockTransfer must not be null");
    	Objects.requireNonNull(request, "StockTransferRequest must not be null");
    	Objects.requireNonNull(from, "FromStorage must not be null");
    	Objects.requireNonNull(to, "ToStorage must not be null");
    	stock.setFromStorage(from);
        stock.setToStorage(to);
        stock.setStatus(request.status());
    	return stock;
    }

    public StockTransferResponse toResponse(StockTransfer stock) {
    	Objects.requireNonNull(stock, "StockTransfer must not be null");
        return new StockTransferResponse(stock);
    }

    /*
     * public StockTransferResponse toResponse(StockTransfer stock) {
     * List<StockTransferItemResponse> itemResponses =
     * stockTransferItemMapper.toResponseList(stock.getItems());
     * return new StockTransferResponse(stock, itemResponses);
     * }
     */

    public List<StockTransferResponse> toResponseList(List<StockTransfer> stocks) {
    	if(stocks == null || stocks.isEmpty()) {
    		return Collections.emptyList();
    	}
        return stocks.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
