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

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.mapper.ProductMapper;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.service.IProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class ProductController {

	
	private final IProductService productService;
	private final ProductMapper productMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-product")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
		ProductResponse response = productService.createProduct(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request){
		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/product/{id}")
	public ResponseEntity<ProductResponse> getOneProduct(@PathVariable Long id){
		ProductResponse response = productService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		List<ProductResponse> responses = productService.findAll();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-barCode")
	public ResponseEntity<ProductResponse> findByBarCode(@RequestParam("barCode") String barCode){
		ProductResponse response = productService.findByBarCode(barCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/product-by-quantity")
	public ResponseEntity<List<ProductResponse>> findByCurrentQuantityLessThan(@RequestParam("quantity") Double quantity){
		List<ProductResponse> responses = productService.findByCurrentQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-name")
	public ResponseEntity<List<ProductResponse>> findByName(@RequestParam("name") String name){
		List<ProductResponse> responses = productService.findByName(name);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-storage/{storageId}")
	public ResponseEntity<List<ProductResponse>> findByStorageId(@PathVariable Long storageId){
		List<ProductResponse> responses = productService.findByStorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-supplier-type")
	public ResponseEntity<List<ProductResponse>> findBysupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<ProductResponse> responses = productService.findBySupplierType(supplierType);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-storage-type")
	public ResponseEntity<List<ProductResponse>> findByStorageType(@RequestParam("storageType") StorageType storageType){
		List<ProductResponse> responses = productService.findByStorageType(storageType);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/product-by-goods-type")
	public ResponseEntity<List<ProductResponse>> findByGoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<ProductResponse> responses = productService.findByGoodsType(goodsType);
		return ResponseEntity.ok(responses);
	}
}
