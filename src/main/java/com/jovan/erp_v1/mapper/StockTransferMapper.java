package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockTransferMapper {

    private final StorageRepository storageRepository;
    private final StockTransferItemMapper stockTransferItemMapper;

    public StockTransfer toEntity(StockTransferRequest request) {
        StockTransfer stock = new StockTransfer();
        Storage from = fetchFromStorageId(request.fromStorageId());
        Storage to = fetchToStorageId(request.toStorageId());
        stock.setTransferDate(request.transferDate());
        stock.setFromStorage(from);
        stock.setToStorage(to);
        stock.setStatus(request.status());
        List<StockTransferItem> items = request.itemRequest().stream()
                .map(itemReq -> {
                    StockTransferItem item = stockTransferItemMapper.toEntity(itemReq);
                    item.setStockTransfer(stock);
                    return item;
                })
                .collect(Collectors.toList());
        stock.setItems(items);
        ;
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
    
    private Storage fetchFromStorageId(Long fromStorageId) {
    	if(fromStorageId == null) {
    		throw new NoDataFoundException("FromStorage ID must not be null");
    	}
    	return storageRepository.findById(fromStorageId).orElseThrow(() -> new NoDataFoundException("FromStorage not found with id "+fromStorageId));
    }
    
    private Storage fetchToStorageId(Long toStorageId) {
    	if(toStorageId == null) {
    		throw new NoDataFoundException("FromStorage ID must not be null");
    	}
    	return storageRepository.findById(toStorageId).orElseThrow(() -> new NoDataFoundException("FromStorage not found with id "+toStorageId));
    }
}
