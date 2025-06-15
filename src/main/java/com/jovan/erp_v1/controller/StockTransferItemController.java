package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;
import com.jovan.erp_v1.service.IStockTransferItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stockTransferItems")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
public class StockTransferItemController {

    private final IStockTransferItemService stockTransferItemService;

    @PostMapping("/create/new-stockTransferItem")
    public ResponseEntity<StockTransferItemResponse> create(@Valid @RequestBody StockTransferItemRequest request) {
        StockTransferItemResponse response = stockTransferItemService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StockTransferItemResponse> update(@PathVariable Long id,
            @Valid @RequestBody StockTransferItemRequest request) {
        StockTransferItemResponse response = stockTransferItemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockTransferItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<StockTransferItemResponse> findOne(@PathVariable Long id) {
        StockTransferItemResponse response = stockTransferItemService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<StockTransferItemResponse>> findAll() {
        List<StockTransferItemResponse> responses = stockTransferItemService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByProductId(@PathVariable Long productId) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-name")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Name(@RequestParam("name") String name) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Name(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-currentQuantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_CurrentQuantity(
            @RequestParam("currentQuantity") Double currentQuantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByProduct_CurrentQuantity(currentQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantity(@RequestParam("quantity") Double quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-less-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantityLessThan(
            @RequestParam("quantity") Double quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-greater-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") Double quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/stockTransfer/{stockTransferId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransferId(@PathVariable Long stockTransferId) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransferId(stockTransferId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{fromStorageId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorageId(
            @PathVariable Long fromStorageId) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByStockTransfer_FromStorageId(fromStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{toStorageId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorageId(
            @PathVariable Long toStorageId) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByStockTransfer_ToStorageId(toStorageId);
        return ResponseEntity.ok(responses);
    }

}
