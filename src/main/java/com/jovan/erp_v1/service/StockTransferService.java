package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.exception.StockTransferErrorException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.StockTransferItemMapper;
import com.jovan.erp_v1.mapper.StockTransferMapper;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StockTransferRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferService implements IStockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;
    private final StorageRepository storageRepository;
    private final StockTransferItemMapper stockTransferItemMapper;

    @Transactional
    @Override
    public StockTransferResponse create(StockTransferRequest request) {
        StockTransfer stock = stockTransferMapper.toEntity(request);
        StockTransfer saved = stockTransferRepository.save(stock);
        return stockTransferMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public StockTransferResponse update(Long id, StockTransferRequest request) {
        StockTransfer stock = stockTransferRepository.findById(id)
                .orElseThrow(() -> new StockTransferErrorException("StockTransfer not found " + id));
        Storage from = storageRepository.findById(request.fromStorageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage-from not found"));
        Storage to = storageRepository.findById(request.toStorageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage-to not found"));
        stock.setTransferDate(request.transferDate());
        stock.setFromStorage(from);
        stock.setToStorage(to);
        stock.setStatus(request.status());
        stock.getItems().clear();
        List<StockTransferItem> items = request.itemRequest().stream()
                .map(itemReq -> {
                    StockTransferItem item = stockTransferItemMapper.toEntity(itemReq);
                    item.setStockTransfer(stock);
                    return item;
                })
                .collect(Collectors.toList());
        stock.setItems(items);
        StockTransfer saved = stockTransferRepository.save(stock);
        return stockTransferMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!stockTransferRepository.existsById(id)) {
            throw new StockTransferErrorException("StockTransfer not found " + id);
        }
        stockTransferRepository.deleteById(id);
    }

    @Override
    public StockTransferResponse findOne(Long id) {
        StockTransfer stock = stockTransferRepository.findById(id)
                .orElseThrow(() -> new StockTransferErrorException("StockTransfer not found " + id));
        return new StockTransferResponse(stock);
    }

    @Override
    public List<StockTransferResponse> findAll() {
        return stockTransferRepository.findAll().stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByStatus(TransferStatus status) {
        return stockTransferRepository.findByStatus(status).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByTransferDate(LocalDate transferDate) {
        return stockTransferRepository.findByTransferDate(transferDate).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByTransferDateBetween(LocalDate from, LocalDate to) {
        return stockTransferRepository.findByTransferDateBetween(from, to).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorageId(Long fromStorageId) {
        return stockTransferRepository.findByFromStorageId(fromStorageId).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorageId(Long toStorageId) {
        return stockTransferRepository.findByFromStorageId(toStorageId).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Name(String fromStorageName) {
        return stockTransferRepository.findByFromStorage_Name(fromStorageName).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Location(String fromLocation) {
        return stockTransferRepository.findByFromStorage_Location(fromLocation).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Name(String toStorageName) {
        return stockTransferRepository.findByToStorage_Name(toStorageName).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Location(String toLocation) {
        return stockTransferRepository.findByToStorage_Location(toLocation).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromStorage_Type(StorageType fromStorageType) {
        return stockTransferRepository.findByFromStorage_Type(fromStorageType).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByToStorage_Type(StorageType toStorageType) {
        return stockTransferRepository.findByToStorage_Type(toStorageType).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByStatusAndDateRange(TransferStatus status, LocalDate startDate,
            LocalDate endDate) {
        return stockTransferRepository.findByStatusAndDateRange(status, startDate, endDate).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> findByFromAndToStorageType(StorageType fromType, StorageType toType) {
        return stockTransferRepository.findByFromAndToStorageType(fromType, toType).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferResponse> searchFromStorageByNameAndLocation(String name, String location) {
        return stockTransferRepository.searchFromStorageByNameAndLocation(name, location).stream()
                .map(StockTransferResponse::new)
                .collect(Collectors.toList());
    }
}
