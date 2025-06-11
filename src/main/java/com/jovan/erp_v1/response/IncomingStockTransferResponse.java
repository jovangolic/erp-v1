package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TransferStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingStockTransferResponse {

    private Long id;
    private LocalDate transferDate;
    private FromStorageResponse fromStorage;
    private TransferStatus status;
}