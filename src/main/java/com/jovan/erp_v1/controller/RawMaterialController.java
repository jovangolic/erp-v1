package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
import com.jovan.erp_v1.mapper.RawMaterialMapper;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.RawMaterialResponse;
import com.jovan.erp_v1.service.IRawMaterialService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rawMaterials")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
public class RawMaterialController {

	private final IRawMaterialService rawMaterialService;
	private final RawMaterialMapper rawMaterialMapper;

	@PreAuthorize(RoleGroups.RAW_MATERIAL_FULL_ACCESS)
	@PostMapping("/create/new-rawMaterial")
	public ResponseEntity<RawMaterialResponse> create(@Valid @RequestBody RawMaterialRequest request) {
		RawMaterialResponse response = rawMaterialService.save(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<RawMaterialResponse> update(@PathVariable Long id,
			@Valid @RequestBody RawMaterialRequest request) {
		RawMaterialResponse response = rawMaterialService.update(id, request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		rawMaterialService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<RawMaterialResponse> findOne(@PathVariable Long id) {
		RawMaterialResponse response = rawMaterialService.findById(id);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/get-all-raw-materials")
	public ResponseEntity<List<RawMaterialResponse>> findAll() {
		List<RawMaterialResponse> responses = rawMaterialService.findAll();
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("find-by-material-name")
	public ResponseEntity<List<RawMaterialResponse>> findByName(@RequestParam("name") String name) {
		List<RawMaterialResponse> responses = rawMaterialService.findByName(name);
		return ResponseEntity.ok(responses);
	}

	// nove metode
	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/filter")
	public ResponseEntity<List<RawMaterialResponse>> filterRawMaterials(
			@RequestParam(required = false) Long shelfId,
			@RequestParam(required = false) BigDecimal minQty,
			@RequestParam(required = false) BigDecimal maxQty,
			@RequestParam(required = false) Long productId) {
		List<RawMaterial> materials = rawMaterialService.filterRawMaterial(shelfId, minQty, maxQty, productId);
		List<RawMaterialResponse> response = materials.stream()
				.map(rawMaterialMapper::toResponse)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/current-quantity")
	public ResponseEntity<List<RawMaterialResponse>> findByCurrentQuantity(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByCurrentQuantity(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/current-quantity-less-than")
	public ResponseEntity<List<RawMaterialResponse>> findByCurrentQuantityLessThan(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByCurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/current-quantity-greater-than")
	public ResponseEntity<List<RawMaterialResponse>> findByCurrentQuantityGreaterThan(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByCurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/product/current-quantity")
	public ResponseEntity<List<RawMaterialResponse>> findByProduct_CurrentQuantity(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByProduct_CurrentQuantity(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/product/current-quantity-greater-than")
	public ResponseEntity<List<RawMaterialResponse>> findByProduct_CurrentQuantityGreaterThan(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService
				.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/product/current-quantity-less-than")
	public ResponseEntity<List<RawMaterialResponse>> findByProduct_CurrentQuantityLessThan(
			@RequestParam("currentQuantity") BigDecimal currentQuantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByProduct_CurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}")
	public ResponseEntity<List<RawMaterialResponse>> findByShelf_Id(@PathVariable Long shelfId) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelf_Id(shelfId);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/count/shelf/rows")
	public ResponseEntity<Long> countByShelf_RowCount(@RequestParam("rowCount") Integer rowCount) {
		Long responses = rawMaterialService.countByShelf_RowCount(rowCount);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/count/shelf/cols")
	public ResponseEntity<Long> countByShelf_Cols(@RequestParam("cols") Integer cols) {
		Long responses = rawMaterialService.countByShelf_Cols(cols);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}/quantity-greater-than")
	public ResponseEntity<List<RawMaterialResponse>> findByShelfAndQuantityGreaterThan(@PathVariable Long shelfId,
			@RequestParam("quantity") BigDecimal quantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelfAndQuantityGreaterThan(shelfId, quantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}/quantity-less-than")
	public ResponseEntity<List<RawMaterialResponse>> findByShelfAndQuantityLessThan(@PathVariable Long shelfId,
			@RequestParam("quantity") BigDecimal quantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelfAndQuantityLessThan(shelfId, quantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}/quantity-equal")
	public ResponseEntity<List<RawMaterialResponse>> findByShelfAndExactQuantity(@PathVariable Long shelfId,
			@RequestParam("quantity") BigDecimal quantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelfAndExactQuantity(shelfId, quantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}/current-quantity-greater-than")
	public ResponseEntity<List<RawMaterialResponse>> findByShelf_IdAndCurrentQuantityGreaterThan(
			@PathVariable Long shelfId, @RequestParam("quantity") BigDecimal quantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelf_IdAndCurrentQuantityGreaterThan(shelfId,
				quantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/shelf/{shelfId}/current-quantity-less-than")
	public ResponseEntity<List<RawMaterialResponse>> findByShelf_IdAndCurrentQuantityLessThan(
			@PathVariable Long shelfId, @RequestParam("quantity") BigDecimal quantity) {
		List<RawMaterialResponse> responses = rawMaterialService.findByShelf_IdAndCurrentQuantityLessThan(shelfId,
				quantity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/supplierType")
	public ResponseEntity<List<RawMaterialResponse>> findBySupplierType(
			@RequestParam("supplierType") SupplierType supplierType) {
		List<RawMaterialResponse> responses = rawMaterialService.findBySupplierType(supplierType);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/storageType")
	public ResponseEntity<List<RawMaterialResponse>> findByStorageType(
			@RequestParam("storageType") StorageType storageType) {
		List<RawMaterialResponse> responses = rawMaterialService.findByStorageType(storageType);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/goodsType")
	public ResponseEntity<List<RawMaterialResponse>> findByGoodsType(@RequestParam("storageType") GoodsType goodsType) {
		List<RawMaterialResponse> responses = rawMaterialService.findByGoodsType(goodsType);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.RAW_MATERIAL_READ_ACCESS)
	@GetMapping("/unitMeasure")
	public ResponseEntity<List<RawMaterialResponse>> findByUnitMeasure(
			@RequestParam("unitMeasure") UnitMeasure unitMeasure) {
		List<RawMaterialResponse> responses = rawMaterialService.findByUnitMeasure(unitMeasure);
		return ResponseEntity.ok(responses);
	}
}
