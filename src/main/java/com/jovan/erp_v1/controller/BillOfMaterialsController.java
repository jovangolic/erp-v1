package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;
import com.jovan.erp_v1.service.IBillOfMaterialsService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billOfMaterials")
@PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
public class BillOfMaterialsController {

    private final IBillOfMaterialsService billOfMaterialsService;

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_FULL_ACCESS)
    @PostMapping("/create/new-billOfMaterial")
    public ResponseEntity<BillOfMaterialsResponse> create(@Valid @RequestBody BillOfMaterialsRequest request) {
        BillOfMaterialsResponse response = billOfMaterialsService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<BillOfMaterialsResponse> update(@PathVariable Long id,
            @Valid @RequestBody BillOfMaterialsRequest request) {
        BillOfMaterialsResponse response = billOfMaterialsService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billOfMaterialsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<BillOfMaterialsResponse> findOne(@PathVariable Long id) {
        BillOfMaterialsResponse response = billOfMaterialsService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<BillOfMaterialsResponse>> findAll() {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/by-parent/{parentProductId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductId(@PathVariable Long parentProductId) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductId(parentProductId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/by-component/{componentId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponentId(@PathVariable Long componentId) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponentId(componentId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/by-quantity")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/quantity-less-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/filter-boms")
    public ResponseEntity<List<BillOfMaterialsResponse>> filterBOMs(
    		@RequestParam(required = false) Long parentProductId, 
    		@RequestParam(required = false) Long componentId,
    		@RequestParam(required = false) BigDecimal minQuantity,
    		@RequestParam(required = false) BigDecimal maxQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.filterBOMs(parentProductId, componentId, minQuantity, maxQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-current-quantity")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_CurrentQuantity(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-current-quantity-less-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_CurrentQuantityLessThan(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-current-quantity-greater-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_CurrentQuantityGreaterThan(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-current-quantity")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_CurrentQuantity(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-current-quantity-less-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_CurrentQuantityLessThan(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-current-quantity-greater-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_CurrentQuantityGreaterThan(currentQuantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/by-parent-and-quantity-greater-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductIdAndQuantityGreaterThan(@RequestParam("parentProductId") Long parentProductId,
    		@RequestParam("quantity") BigDecimal quantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductIdAndQuantityGreaterThan(parentProductId, quantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/by-parent-and-quantity-less-than")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductIdAndQuantityLessThan(@RequestParam("parentProductId") Long parentProductId,
    		@RequestParam("quantity") BigDecimal quantity){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductIdAndQuantityLessThan(parentProductId, quantity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product/{parentProductId}/component/{componentId}")
    public ResponseEntity<BillOfMaterialsResponse>  findByParentProductIdAndComponentId(@PathVariable Long parentProductId,@PathVariable Long componentId){
    	BillOfMaterialsResponse responses = billOfMaterialsService.findByParentProductIdAndComponentId(parentProductId, componentId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/exists-by-parent-component")
    public ResponseEntity<Boolean> existsByParentProductIdAndComponentId(@RequestParam("parentProductId") Long parentProductId,
    		@RequestParam("componentId") Long componentId){
    	Boolean responses = billOfMaterialsService.existsByParentProductIdAndComponentId(parentProductId, componentId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_FULL_ACCESS)
    @DeleteMapping("/delete-parent/{parentProductId}")
    public ResponseEntity<Void> deleteByParentProductId(@PathVariable Long parentProductId){
    	billOfMaterialsService.deleteByParentProductId(parentProductId);
    	return ResponseEntity.noContent().build();	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product-by-quantity-desc")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductIdOrderByQuantityDesc(@RequestParam("parentProductId") Long parentProductId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductIdOrderByQuantityDesc(parentProductId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product-by-quantity-asc")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductIdOrderByQuantityAsc(@RequestParam("parentProductId") Long parentProductId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductIdOrderByQuantityAsc(parentProductId);
    	return ResponseEntity.ok(responses);	
    }
     
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/quantity-between")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantityBetween(min, max);
    	return ResponseEntity.ok(responses);	
    }
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/product/{productId}/components-by-name")
    public ResponseEntity<List<BillOfMaterialsResponse>> findComponentsByProductIdAndComponentNameContaining(@PathVariable Long productId, @RequestParam("name") String name){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findComponentsByProductIdAndComponentNameContaining(productId, name);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-by-name")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_NameContainingIgnoreCase(@RequestParam("name") String name){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_NameContainingIgnoreCase(name);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-by-name")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_NameContainingIgnoreCase(@RequestParam("name") String name){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_NameContainingIgnoreCase(name);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-by-goods-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_GoodsType(goodsType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-by-supplier-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_SupplierType(supplierType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-by-unit-measure")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/component/shelf/{shelfId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_Shelf_Id(@PathVariable Long shelfId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_Shelf_Id(shelfId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product/storage/{storageId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_Storage_Id(@PathVariable Long storageId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_Storage_Id(storageId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product/shelf/{shelfId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_Shelf_Id(@PathVariable Long shelfId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_Shelf_Id(shelfId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/component/storage/{storageId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_Storage_Id(@PathVariable Long storageId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_Storage_Id(storageId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-by-goods-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_GoodsType(goodsType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-by-supplier-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_SupplierType(supplierType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-by-unit-measure")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product/supply/{supplyId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_Supply_Id(@PathVariable Long supplyId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_Supply_Id(supplyId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/component/supply/{supplyId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_Supply_Id(@PathVariable Long supplyId){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_Supply_Id(supplyId);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/component-by-storage-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponent_StorageType(@RequestParam("type") StorageType type){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponent_StorageType(type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-product-by-storage-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_StorageType(@RequestParam("type") StorageType type){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_StorageType(type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/parent-product/storage/{storageId}/goods-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProduct_Storage_IdAndComponent_GoodsType(@PathVariable Long storageId,@RequestParam("goodsType") GoodsType goodsType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProduct_Storage_IdAndComponent_GoodsType(storageId, goodsType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/parent-component-share-same-storage")
    public ResponseEntity<List<BillOfMaterialsResponse>> findWhereParentAndComponentShareSameStorage(){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findWhereParentAndComponentShareSameStorage();
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/min-quantity-component-goods-type")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByMinQuantityAndComponentGoodsType(@RequestParam("minQuantity") BigDecimal minQuantity,@RequestParam("goodsType") GoodsType goodsType){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByMinQuantityAndComponentGoodsType(minQuantity, goodsType);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/by-component-storage-and-unit")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponentStorageAndUnitMeasure(@RequestParam("storageId") Long storageId,@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponentStorageAndUnitMeasure(storageId, unitMeasure);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/quantity-desc")
    public ResponseEntity<List<BillOfMaterialsResponse>> findAllOrderByQuantityDesc(){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findAllOrderByQuantityDesc();
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize(RoleGroups.BILL_OF_MATERIAL_READ_ACCESS)
    @GetMapping("/search/quantity-asc")
    public ResponseEntity<List<BillOfMaterialsResponse>> findAllOrderByQuantityAsc(){
    	List<BillOfMaterialsResponse> responses = billOfMaterialsService.findAllOrderByQuantityAsc();
    	return ResponseEntity.ok(responses);	
    }
    
}
