package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.model.StockTransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferResponse {

    private Long id;
    private LocalDate transferDate;
    private FromStorageResponse fromStorage;
    private ToStorageResponse toStorage;
    private TransferStatus status;
    private List<StockTransferItemResponse> itemResponse;

    public StockTransferResponse(StockTransfer stock) {
        this.id = stock.getId();
        this.transferDate = stock.getTransferDate();
        this.fromStorage = new FromStorageResponse(stock.getFromStorage());
        this.toStorage = new ToStorageResponse(stock.getToStorage());
        this.status = stock.getStatus();
        this.itemResponse = stock.getItems().stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }
}
