package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;
import com.jovan.erp_v1.service.IStockTransferItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stockTransferItems")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN')")
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

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<StockTransferItemResponse> findOne(@PathVariable Long id) {
        StockTransferItemResponse response = stockTransferItemService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<StockTransferItemResponse>> findAll() {
        List<StockTransferItemResponse> responses = stockTransferItemService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByProductId(@PathVariable Long productId) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-name")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Name(@RequestParam("name") String name) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Name(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-currentQuantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_CurrentQuantity(
            @RequestParam("currentQuantity") BigDecimal currentQuantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByProduct_CurrentQuantity(currentQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-less-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-greater-quantity")
    public ResponseEntity<List<StockTransferItemResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/stockTransfer/{stockTransferId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransferId(@PathVariable Long stockTransferId) {
        List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransferId(stockTransferId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{fromStorageId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorageId(
            @PathVariable Long fromStorageId) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByStockTransfer_FromStorageId(fromStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{toStorageId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorageId(
            @PathVariable Long toStorageId) {
        List<StockTransferItemResponse> responses = stockTransferItemService
                .findByStockTransfer_ToStorageId(toStorageId);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search-unit-measure")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(responses);		
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/storage-type-and-unit-measure")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_UnitMeasureAndProduct_StorageType(@RequestParam("unitMeasure") UnitMeasure unitMeasure,@RequestParam("storageType") StorageType storageType){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_UnitMeasureAndProduct_StorageType(unitMeasure, storageType);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search-supplier-type")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_SupplierType(supplierType);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search-goods-type")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_GoodsType(goodsType);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search-storage-type")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_StorageType(storageType);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/shelf/{shelfId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_Id(@PathVariable Long shelfId){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_Id(shelfId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/row-count")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_RowCount(@RequestParam("rowCount") Integer rowCount){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_RowCount(rowCount);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/cols")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_Cols(@RequestParam("cols") Integer cols){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_Cols(cols);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/row-count-greater-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_RowCountGreaterThanEqual(@RequestParam("rowCount") Integer rowCount){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_RowCountGreaterThanEqual(rowCount);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/cols-greater-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_ColsGreaterThanEqual(@RequestParam("cols") Integer cols){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_ColsGreaterThanEqual(cols);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/row-count-less-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_RowCountLessThanEqual(@RequestParam("rowCount") Integer rowCount){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_RowCountLessThanEqual(rowCount);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/cols-less-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_ColsLessThanEqual(@RequestParam("cols") Integer cols){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_ColsLessThanEqual(cols);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/seach/product/row-count-range")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_RowCountBetween(@RequestParam("minRowCount") Integer minRowCount, @RequestParam("maxRowCount") Integer maxRowCount){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_RowCountBetween(minRowCount, maxRowCount);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/cols-range")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Shelf_ColsBetween(@RequestParam("minCols") Integer minCols, @RequestParam("maxCols") Integer maxCols){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Shelf_ColsBetween(minCols, maxCols);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/product/supply/{supplyId}")
    public ResponseEntity<List<StockTransferItemResponse>> findByProduct_Supply_Id(@PathVariable Long supplyId){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByProduct_Supply_Id(supplyId);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-status")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_Status(@RequestParam("status") TransferStatus status){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-date")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_TransferDate(@RequestParam("transferDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transferDate){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_TransferDate(transferDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-date-range")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_TransferDateBetween(@RequestParam("transferDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transferDateStart,
            @RequestParam("transferDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transferDateEnd){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_TransferDateBetween(transferDateStart, transferDateEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-statuses")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_StatusIn(@Valid @RequestParam("statuses") List<TransferStatus> statuses){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_StatusIn(statuses);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-status-not")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_StatusNot(@RequestParam("status") TransferStatus status){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_StatusNot(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-date-after")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_TransferDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_TransferDateAfter(date);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-date-before")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_TransferDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_TransferDateBefore(date);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer-quantity-greater-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_StatusAndQuantityGreaterThan(@RequestParam("status") TransferStatus status,@RequestParam("quantity") BigDecimal quantity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_StatusAndQuantityGreaterThan(status, quantity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/status-and-date-range")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(@RequestParam("status") TransferStatus status, 
    		@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(status, start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-name")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_NameContainingIgnoreCase(@RequestParam("name") String name){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_NameContainingIgnoreCase(name);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-location")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_LocationContainingIgnoreCase(@RequestParam("location") String location){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_LocationContainingIgnoreCase(location);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-capacity")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_Capacity(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-capcaty-greater-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-capacity-less-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/from-storage-type")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_FromStorage_Type(@RequestParam("type") StorageType type){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_FromStorage_Type(type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-name")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_NameContainingIgnoreCase(@RequestParam("name") String name){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_NameContainingIgnoreCase(name);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-location")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_LocationContainingIgnoreCase(@RequestParam("location") String location){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_LocationContainingIgnoreCase(location);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-capacity")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_Capacity(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-capacity-greater-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-capacity-less-than")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/search/stock-transfer/to-storage-type")
    public ResponseEntity<List<StockTransferItemResponse>> findByStockTransfer_ToStorage_Type(@RequestParam("type") StorageType type){
    	List<StockTransferItemResponse> responses = stockTransferItemService.findByStockTransfer_ToStorage_Type(type);
    	return ResponseEntity.ok(responses);
    }
    
}
