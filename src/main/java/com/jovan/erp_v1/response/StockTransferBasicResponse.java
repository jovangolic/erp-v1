package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.model.StockTransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferBasicResponse {

	private Long id;
    private LocalDate transferDate;
    private FromStorageResponse fromStorage;
    private ToStorageResponse toStorage;
    private TransferStatus status;
    
    public StockTransferBasicResponse(StockTransfer stock) {
        this.id = stock.getId();
        this.transferDate = stock.getTransferDate();
        this.fromStorage = stock.getFromStorage() != null ? new FromStorageResponse(stock.getFromStorage()) : null;
        this.toStorage = stock.getToStorage() != null ? new ToStorageResponse(stock.getToStorage()) : null;
        this.status = stock.getStatus();
    }
}
