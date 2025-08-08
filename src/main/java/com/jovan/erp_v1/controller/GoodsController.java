package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.service.IGoodsService;
import com.jovan.erp_v1.util.RoleGroups;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
public class GoodsController {

	private final IGoodsService goodsService;

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@PostMapping("/by-name")
	public ResponseEntity<List<GoodsResponse>> findByName(@RequestParam("name") String name) {
		List<GoodsResponse> responses = goodsService.findByName(name);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-barCode")
	public ResponseEntity<List<GoodsResponse>> findByBarCode(@RequestParam("barCode") String barCode) {
		List<GoodsResponse> response = goodsService.findByBarCodes(barCode);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-unitMeasure")
	public ResponseEntity<List<GoodsResponse>> findByUnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure) {
		List<GoodsResponse> responses = goodsService.findByUnitMeasure(unitMeasure);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-supplierType")
	public ResponseEntity<List<GoodsResponse>> findBySupplierType(@RequestParam("type") SupplierType type) {
		List<GoodsResponse> responses = goodsService.findBySupplierType(type);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-storageType")
	public ResponseEntity<List<GoodsResponse>> findByStorageType(@RequestParam("type") StorageType type) {
		List<GoodsResponse> responses = goodsService.findByStorageType(type);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-goodsType")
	public ResponseEntity<List<GoodsResponse>> findByGoodsType(@RequestParam("type") GoodsType type) {
		List<GoodsResponse> responses = goodsService.findByGoodsType(type);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-storageName")
	public ResponseEntity<List<GoodsResponse>> findByStorageName(@RequestParam("storageName") String storageName) {
		List<GoodsResponse> responses = goodsService.findByStorageName(storageName);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/supply/{supplyId}")
	public ResponseEntity<List<GoodsResponse>> findBySupplyId(@PathVariable Long supplyId) {
		List<GoodsResponse> responses = goodsService.findBySupplyId(supplyId);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("by-barCode-and-goodsType")
	public ResponseEntity<List<GoodsResponse>> findByBarCodeAndGoodsType(@RequestParam("barCode") String barCode,
			@RequestParam("goodsType") GoodsType goodsType) {
		List<GoodsResponse> responses = goodsService.findByBarCodeAndGoodsType(barCode, goodsType);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-barCode-and-storageId")
	public ResponseEntity<List<GoodsResponse>> findByBarCodeAndStorageId(@RequestParam("barCode") String barCode,
			@RequestParam("storageId") Long storageId) {
		List<GoodsResponse> responses = goodsService.findByBarCodeAndStorageId(barCode, storageId);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.GOODS_READ_ACCESS)
	@GetMapping("/by-single-barCode")
	public ResponseEntity<GoodsResponse> findSingleByBarCode(@RequestParam("barCode") String barCode) {
		GoodsResponse response = goodsService.findSingleByBarCode(barCode);
		return ResponseEntity.ok(response);
	}
}
