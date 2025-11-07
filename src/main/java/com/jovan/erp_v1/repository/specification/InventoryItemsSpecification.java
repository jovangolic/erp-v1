package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InventoryItemsStatus;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.repository.InventoryItemsRepository;
import com.jovan.erp_v1.search_request.InventoryItemsSearchRequest;
import com.jovan.erp_v1.statistics.inventory_items.InventoryStatRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryItemsSpecification {
	
	private final InventoryItemsRepository inventoryItemsRepository;

	public static Specification<InventoryItems> fromRequest(InventoryItemsSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasInventoryId(req.inventoryId()))
				.and(hasInventoryIdRange(req.inventoryIdFrom(), req.inventoryIdTo()))
				.and(hasStorageEmployeeId(req.storageEmployeeId()))
				.and(hasStorageEmployeeIdRange(req.storageEmployeeIdFrom(), req.storageEmployeeIdTo()))
				.and(hasStorageForemanId(req.storageForemanId()))
				.and(hasStorageForemanIdRange(req.storageForemanIdFrom(), req.storageForemanIdTo()))
				.and(hasInventoryDate(req.inventoryDate()))
				.and(hasInventoryDateBefore(req.inventoryDateBefore()))
				.and(hasInventoryDateAfter(req.inventoryDateAfter()))
				.and(hasInventoryDateRange(req.inventoryDateAfter(), req.inventoryDateBefore()))
				.and(hasInventoryAligned(req.inventoryAligned()))
				.and(hasInventoryStatus(req.inventoryStatus()))
				.and(hasProductId(req.productId()))
				.and(hasProductIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasProductCurrentQuantity(req.productCurrentQuantity()))
				.and(hasProductCurrentQuantityRange(req.productCurrentQuantityMin(), req.productCurrentQuantityMax()))
				.and(hasProductName(req.productName()))
				.and(hasProductUnitMeasure(req.unitMeasure()))
				.and(hasProductSupplierType(req.supplierType()))
				.and(hasProductStorageType(req.storageType()))
				.and(hasProductGoodsType(req.goodsType()))
				.and(hasProductStorageId(req.productStorageId()))
				.and(hasProductStorageIdRange(req.productStorageIdFrom(), req.productStorageIdTo()))
				.and(hasProductSupplyId(req.productSupplyId()))
				.and(hasProductSupplyIdRange(req.productSupplyIdFrom(), req.productSupplyIdTo()))
				.and(hasProductShelfId(req.productShelfId()))
				.and(hasProductShelfIdRange(req.productShelfIdFrom(), req.productShelfIdTo()))
				.and(hasQuantity(req.quantity()))
				.and(hasQuantityRange(req.quantityMin(), req.quantityMax()))
				.and(hasItemCondition(req.itemCondition()))
				.and(hasItemConditionRange(req.itemConditionMin(), req.itemConditionMax()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasInventoryItemsStatus(req.status()));
	}
	
	public static Specification<InventoryItems> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<InventoryItems> hasIdRange(Long from, Long to){
		return(root ,query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasItemConditionRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("itemCondition"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("itemCondition"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("itemCondition"), max);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasItemCondition(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("itemCondition"), bd);
	}
	
	public static Specification<InventoryItems> hasQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasQuantity(BigDecimal q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("quantity"), q);
	}
	
	public static Specification<InventoryItems> hasProductShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasProductShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("shelf").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasProductSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasProductSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("supply").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasProductStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasProductStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("storage").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasProductGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("goodsType"), type);
	}
	
	public static Specification<InventoryItems> hasProductStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("storageType"), type);
	}
	
	public static Specification<InventoryItems> hasProductSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("supplierType"), type);
	}
	
	public static Specification<InventoryItems> hasProductUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("product").get("unitMeasure"), um);
	}
	
	public static Specification<InventoryItems> hasProductName(String name){
		return(root, quey, cb) -> {
			if(name == null || name.isBlank()) return null;
			return cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InventoryItems> hasProductCurrentQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("product").get("currentQuantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("currentQuantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("currentQuantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasProductCurrentQuantity(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("product").get("currentQuantity"), bd);
	}
	
	public static Specification<InventoryItems> hasProductIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasInventoryStatus(InventoryStatus status){
		return(root, query, cb) -> status == null ? null  :cb.equal(root.get("inventory").get("status"), status);
	}
	
	public static Specification<InventoryItems> hasInventoryAligned(Boolean al){
		return(root, query, cb) -> al == null ? null : cb.equal(root.get("inventory").get("aligned"), al);
	}
	
	public static Specification<InventoryItems> hasInventoryDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("inventory").get("date"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("inventory").get("date"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("inventory").get("date"), end);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasInventoryDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("inventory").get("date"), aft);
	}
	
	public static Specification<InventoryItems> hasInventoryDateBefore(LocalDate bef){
		return(root, query, cb)-> bef == null ? null : cb.lessThan(root.get("inventory").get("date"), bef);
	}
	
	public static Specification<InventoryItems> hasInventoryDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("inventory").get("date"), ld);
	}
	
	public static Specification<InventoryItems> hasStorageForemanIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inventory").get("storageForeman").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inventory").get("storageForeman").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inventory").get("storageForeman").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasStorageForemanId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inventory").get("storageForeman").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasStorageEmployeeIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inventory").get("storageEmployee").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inventory").get("storageEmployee").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inventory").get("storageEmployee").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasStorageEmployeeId(Long id){
		return(root, query, cb)-> id == null ? null : cb.equal(root.get("inventory").get("storageEmployee").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasInventoryIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inventory").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inventory").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inventory").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InventoryItems> hasInventoryId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inventory").get("id"), id);
	}
	
	public static Specification<InventoryItems> hasInventoryItemsStatus(InventoryItemsStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<InventoryItems> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public List<InventoryItems> findByFilters(InventoryStatRequest req){
		Specification<InventoryItems> spec = Specification.where(null);
		if(req.inventoryId() != null) {
			spec = spec.and((root,query, cb) -> cb.equal(root.get("inventory").get("id"), req.inventoryId()));
		}
		if(req.productId() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("product").get("id"), req.productId()));
		}
		if(req.storageId() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("product").get("storage").get("id"), req.storageId()));
		}
		if (req.fromDate() != null)
	        spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inventory").get("date"), req.fromDate()));

	    if (req.toDate() != null)
	        spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("inventory").get("date"), req.toDate()));

	    if (req.onlyConfirmed() != null)
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("confirmed"), req.onlyConfirmed()));
	    return inventoryItemsRepository.findAll(spec);
	}
}
