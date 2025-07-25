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

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;
import com.jovan.erp_v1.service.IDeliveryItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-items")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class DeliveryItemController {

    private final IDeliveryItemService deliveryItemService;

    @PostMapping("/create/new-delivery-item")
    public ResponseEntity<DeliveryItemResponse> create(@Valid @RequestBody DeliveryItemRequest request) {
        DeliveryItemResponse response = deliveryItemService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DeliveryItemResponse> update(@PathVariable Long id,
            @Valid @RequestBody DeliveryItemRequest request) {
        DeliveryItemResponse response = deliveryItemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<DeliveryItemResponse> findById(@PathVariable Long id) {
        DeliveryItemResponse response = deliveryItemService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<DeliveryItemResponse>> findAll() {
        List<DeliveryItemResponse> responses = deliveryItemService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-quantity")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantity(@RequestParam("quantity")BigDecimal quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quantity-less-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/inbound-date-range")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_DeliveryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_DeliveryDateBetween(start,
                end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/outbound-date-range")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_DeliveryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_DeliveryDateBetween(start,
                end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<DeliveryItemResponse> findByProduct(@PathVariable Long productId) {
        DeliveryItemResponse response = deliveryItemService.findByProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inbound/{inboundId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDeliveryId(@PathVariable Long inboundId) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDeliveryId(inboundId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/outbound/{outboundId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDeliveryId(@PathVariable Long outboundId) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDeliveryId(outboundId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Name(@PathVariable String name) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Name(name);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @GetMapping("/product-current-quantity")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_CurrentQuantity(currentQuantity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product-current-quantity-greater-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product-current-quantity-less-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_CurrentQuantityLessThan(@RequestParam("") BigDecimal currentQuantity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_CurrentQuantityLessThan(currentQuantity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-unit-measure")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-supplier-type")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_SupplierType(supplierType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-storage-type")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_StorageType(storageType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-goods-type")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_GoodsType(goodsType);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product/storage/{storageId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_Id(@PathVariable Long storageId){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_Id(storageId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-storage-name")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_NameContainingIgnoreCase(@RequestParam("storageName") String storageName){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_NameContainingIgnoreCase(storageName);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-by-storage-location")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_LocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_LocationContainingIgnoreCase(storageLocation);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-storage-capacity")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_Capacity(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_Capacity(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-storage-capacity-greater-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_CapacityGreaterThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_CapacityGreaterThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-storage-capacity-less-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_CapacityLessThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_CapacityLessThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-storage-type")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_StorageType(@RequestParam("type") StorageType type){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_StorageType(type);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/product-storage-status")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Storage_StorageStatus(@RequestParam("status") StorageStatus status){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Storage_StorageStatus(status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/calculate-used-capacity/{storageId}")
    public ResponseEntity<BigDecimal> calculateUsedCapacityByStorageId(@PathVariable Long storageId){
    	BigDecimal responses = deliveryItemService.calculateUsedCapacityByStorageId(storageId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/sum-inbound-quantity/{storageId}")
    public ResponseEntity<BigDecimal> sumInboundQuantityByStorageId(@PathVariable Long storageId){
    	BigDecimal responses = deliveryItemService.sumInboundQuantityByStorageId(storageId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/sum-outbound-quantity/{storageId}")
    public ResponseEntity<BigDecimal> sumOutboundQuantityByStorageId(@PathVariable Long storageId){
    	BigDecimal responses = deliveryItemService.sumOutboundQuantityByStorageId(storageId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product/supply/{supplyId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByProductSupplyId(@PathVariable Long supplyId){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProductSupplyId(supplyId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product/shelf/{shelfId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByProductShelfId(@PathVariable Long shelfId){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProductShelfId(shelfId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product-shelf-row-count")
    public ResponseEntity<List<DeliveryItemResponse>> findByProductShelfRowCount(@RequestParam("rowCount") Integer rowCount){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProductShelfRowCount(rowCount);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product-shelf-cols")
    public ResponseEntity<List<DeliveryItemResponse>> findByProductShelfCols(@RequestParam("cols") Integer cols){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByProductShelfCols(cols);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/product-without-shelf")
    public ResponseEntity<List<DeliveryItemResponse>> findProductsWithoutShelf(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findProductsWithoutShelf();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound-delivery-date")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_DeliveryDate(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_DeliveryDate(start);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound/supply/{supplyId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Supply_Id(@PathVariable Long supplyId ){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Supply_Id(supplyId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound-delivery-status")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Status(@RequestParam("status") DeliveryStatus status){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound-delivery-status-pending")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Status_Pending(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Status_Pending();
    	return ResponseEntity.ok(responses);
    }
     
    @GetMapping("/inbound-delivery-status-in-transit")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Status_InTransit(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Status_InTransit();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound-delivery-status-delivered")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Status_Delivered(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Status_Delivered();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/inbound-delivery-status-cancelled")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_Status_Cancelled(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_Status_Cancelled();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-date")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_DeliveryDate(@RequestParam("deliveryDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_DeliveryDate(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-status")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_Status(@RequestParam("status") DeliveryStatus status){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-status-pending")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_Status_Pending(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_Status_Pending();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-status-in-transit")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_Status_InTransit(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_Status_InTransit();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-status-delivered")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_Status_Delivered(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_Status_Delivered();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound-delivery-status-cancelled")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_Status_Cancelled(){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_Status_Cancelled();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer/{buyerId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerId(@PathVariable Long buyerId){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerId(buyerId);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer-company-name")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerNameContainingIgnoreCase(@RequestParam("companyName") String companyName){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerNameContainingIgnoreCase(companyName);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer-address")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerAddress(@RequestParam("address") String address){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerAddress(address);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer-email")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerEmailLikeIgnoreCase(@RequestParam("buyerEmail") String buyerEmail){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerEmailLikeIgnoreCase(buyerEmail);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer-phone-number")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerPhoneNumberLikeIgnoreCase(phoneNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/outbound/buyer-contact-person")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase(@RequestParam("contactPerson") String contactPerson){
    	List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_BuyerContactPersonContainingIgnoreCase(contactPerson);
    	return ResponseEntity.ok(responses);
    }
    
}
