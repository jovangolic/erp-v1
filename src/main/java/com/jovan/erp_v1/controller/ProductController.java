package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.service.IProductService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
public class ProductController {

	private final IProductService productService;
	
	@PreAuthorize(RoleGroups.PRODUCT_FULL_ACCESS)
	@PostMapping("/create/new-product")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
		ProductResponse response = productService.createProduct(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request){
		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product/{id}")
	public ResponseEntity<ProductResponse> getOneProduct(@PathVariable Long id){
		ProductResponse response = productService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		List<ProductResponse> responses = productService.findAll();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-barCode")
	public ResponseEntity<ProductResponse> findByBarCode(@RequestParam("barCode") String barCode){
		ProductResponse response = productService.findByBarCode(barCode);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-quantity")
	public ResponseEntity<List<ProductResponse>> findByCurrentQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
		List<ProductResponse> responses = productService.findByCurrentQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-name")
	public ResponseEntity<List<ProductResponse>> findByName(@RequestParam("name") String name){
		List<ProductResponse> responses = productService.findByName(name);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-storage/{storageId}")
	public ResponseEntity<List<ProductResponse>> findByStorageId(@PathVariable Long storageId){
		List<ProductResponse> responses = productService.findByStorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-supplier-type")
	public ResponseEntity<List<ProductResponse>> findBysupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<ProductResponse> responses = productService.findBySupplierType(supplierType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-storage-type")
	public ResponseEntity<List<ProductResponse>> findByStorageType(@RequestParam("storageType") StorageType storageType){
		List<ProductResponse> responses = productService.findByStorageType(storageType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/product-by-goods-type")
	public ResponseEntity<List<ProductResponse>> findByGoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<ProductResponse> responses = productService.findByGoodsType(goodsType);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/by-unitMeasure")
	public ResponseEntity<List<ProductResponse>> findByUnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<ProductResponse> responses = productService.findByUnitMeasure(unitMeasure);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/storage/{storageId}/shelf")
	public ResponseEntity<List<ProductResponse>> findByShelfRowColAndStorage(@RequestParam("row") Integer row,@RequestParam("col") Integer col,@PathVariable Long storageId){
		List<ProductResponse> responses = productService.findByShelfRowColAndStorage(row, col, storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/shelf/row")
	public ResponseEntity<List<ProductResponse>> findByShelfRow(@RequestParam("row") Integer row){
		List<ProductResponse> responses = productService.findByShelfRow(row);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/shelf/col")
	public ResponseEntity<List<ProductResponse>> findByShelfColumn(@RequestParam("col") Integer col){
		List<ProductResponse> responses = productService.findByShelfColumn(col);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/supply-min-quantity")
	public ResponseEntity<List<ProductResponse>> findBySupplyMinQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<ProductResponse> responses = productService.findBySupplyMinQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/supply/updated")
	public ResponseEntity<List<ProductResponse>>  findBySupplyUpdateRange(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
		List<ProductResponse> responses = productService.findBySupplyUpdateRange(from, to);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/storage/{storageId}")
	public ResponseEntity<List<ProductResponse>> findBySupplyStorageId(@PathVariable Long storageId){
		List<ProductResponse> responses = productService.findBySupplyStorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/count/shelf/rows")
	public ResponseEntity<Long> countByShelfRowCount(@RequestParam("rowCount") Integer rowCount){
		Long responses = productService.countByShelfRowCount(rowCount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PRODUCT_READ_ACCESS)
	@GetMapping("/count/shelf/cols")
	public ResponseEntity<Long> countByShelfCols(@RequestParam("cols") Integer cols){
		Long responses = productService.countByShelfCols(cols);
		return ResponseEntity.ok(responses);
	}
}
