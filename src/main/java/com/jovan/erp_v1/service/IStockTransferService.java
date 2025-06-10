package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;

public interface IStockTransferService {

    StockTransferResponse create(StockTransferRequest request);

    StockTransferResponse update(Long id, StockTransferRequest request);

    void delete(Long id);

    StockTransferResponse findOne(Long id);

    List<StockTransferResponse> findAll();

    List<StockTransferResponse> findByStatus(TransferStatus status);

    List<StockTransferResponse> findByTransferDate(LocalDate transferDate);

    List<StockTransferResponse> findByTransferDateBetween(LocalDate from, LocalDate to);

    List<StockTransferResponse> findByFromStorageId(Long fromStorageId);

    List<StockTransferResponse> findByToStorageId(Long toStorageId);

    List<StockTransferResponse> findByFromStorage_Name(String fromStorageName);

    List<StockTransferResponse> findByFromStorage_Location(String fromLocation);

    List<StockTransferResponse> findByToStorage_Name(String toStorageName);

    List<StockTransferResponse> findByToStorage_Location(String toLocation);

    List<StockTransferResponse> findByFromStorage_Type(StorageType fromStorageType);

    List<StockTransferResponse> findByToStorage_Type(StorageType toStorageType);

    List<StockTransferResponse> findByStatusAndDateRange(TransferStatus status, LocalDate startDate, LocalDate endDate);

    List<StockTransferResponse> findByFromAndToStorageType(StorageType fromType, StorageType toType);

    List<StockTransferResponse> searchFromStorageByNameAndLocation(String name, String location);

}
