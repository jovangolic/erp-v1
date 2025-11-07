package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.search_request.InspectionSearchRequest;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class InspectionSpecifications {
	
	public static Specification<Inspection> fromRequest(InspectionSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasInspectionCode(req.code()))
				.and(hasInspectionType(req.type()))
				.and(hasInspectionDate(req.inspectionDate()))
				.and(hasInspectionDateBefore(req.inspectionDateBefore()))
				.and(hasInspectionDateAfter(req.inspectionDateAfter()))
				.and(hasInspectionDateRange(req.inspectionDateAfter(), req.inspectionDateBefore()))
				.and(hasBatchId(req.batchId()))
				.and(hasBatchIdRange(req.batchIdFrom(), req.batchIdTo()))
				.and(hasBatchCode(req.batchCode()))
				.and(hasBatchProductId(req.batchProductId()))
				.and(hasBatchProductIdRange(req.batchProductIdFrom(), req.batchProductIdTo()))
				.and(hasBatchQuantityProduced(req.quantityProduced()))
				.and(hasBatchQuantityProducedRange(req.quantityProducedMin(), req.quantityProducedMax()))
				.and(hasBatchProdDate(req.productionDate()))
				.and(hasBatchProdDateBefore(req.productionDateBefore()))
				.and(hasBatchProdDateAfter(req.productionDateAfter()))
				.and(hasBatchProdDateRange(req.productionDateAfter(), req.productionDateBefore()))
				.and(hasBatchExpDate(req.expiryDate()))
				.and(hasBatchExpDateBefore(req.expiryDateBefore()))
				.and(hasBatchExpDateAfter(req.expiryDateAfter()))
				.and(hasBatchExpDateRange(req.expiryDateAfter(), req.expiryDateBefore()))
				.and(hasProductId(req.productId()))
				.and(hasProductIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasProductCurrentQuantity(req.productCurrentQuantity()))
				.and(hasProductCurrentQuantityRange(req.productCurrentQuantityMin(), req.productCurrentQuantityMax()))
				.and(hasProductName(req.productName()))
				.and(hasProductUnitMeasure(req.unitMeasure()))
				.and(hasProductSupplierType(req.supplierType()))
				.and(hasProductStorageType(req.storageType()))
				.and(hasProductGoodsType(req.goodsType()))
				.and(hasProductStorageId(req.storageId()))
				.and(hasProductStorageIdRange(req.storageIdFrom(), req.storageIdTo()))
				.and(hasInspectorId(req.inspectorId()))
				.and(hasInspectorIdRange(req.inspectorIdFrom(), req.inspectorIdTo()))
				.and(hasInspectorFullName(req.firstName(), req.lastName()))
				.and(hasQuantityInspected(req.quantityInspected()))
				.and(hasQuantityInspectedRange(req.quantityInspectedMin(), req.quantityInspectedMin()))
				.and(hasQuantityAccepted(req.quantityAccepted()))
				.and(hasQuantityAcceptedRange(req.quantityAcceptedMin(), req.quantityAcceptedMax()))
				.and(hasQuantityRejected(req.quantityRejected()))
				.and(hasQuantityRejectedRange(req.quantityRejectedMin(), req.quantityRejectedMax()))
				.and(hasNote(req.notes()))
				.and(hasInspectionResult(req.result()))
				.and(hasQualityCheckId(req.qualityCheckId()))
				.and(hasQualityCheckIdRange(req.qualityCheckIdFrom(), req.qualityCheckIdTo()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasLocDate(req.locDate()))
				.and(hasLocDateBefore(req.locDateBefore()))
				.and(hasLocDateAfter(req.locDateAfter()))
				.and(hasLocDateRange(req.locDateAfter(), req.locDateBefore()))
				.and(hasQualityCheckInspectorId(req.qualityCheckInspectorId()))
				.and(hasQualityCheckInspectorIdRange(req.qualityCheckInspectorIdFrom(), req.qualityCheckInspectorIdTo()))
				.and(hasReferenceType(req.referenceType()))
				.and(hasQualityCheckType(req.checkType()))
				.and(hasQualityCheckStatus(req.checkStatus()))
				.and(hasInspectionStatus(req.status()));
	}
	
	public static Specification<Inspection> hasId(Long id){
		return(root ,query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Inspection> hasIdRange(Long from, Long to){
		return(root, query, cb) -> {
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
	
	public static Specification<Inspection> hasQualityCheckStatus(QualityCheckStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("qualityCheck").get("status"), status);
	}
	
	public static Specification<Inspection> hasQualityCheckType(QualityCheckType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("qualityCheck").get("checkType"), type);
	}
	
	public static Specification<Inspection> hasReferenceType(ReferenceType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("qualityCheck").get("referenceType"), type);
	}
	
	public static Specification<Inspection> hasQualityCheckInspectorIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("qualityCheck").get("inspector").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("qualityCheck").get("inspector").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("qualityCheck").get("inspector").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasQualityCheckInspectorId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("qualityCheck").get("inspector").get("id"), id);
	}
	
	public static Specification<Inspection> hasLocDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("qualityCheck").get("locDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("qualityCheck").get("locDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("qualityCheck").get("locDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasLocDateAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("qualityCheck").get("locDate"), aft);
	}
	
	public static Specification<Inspection> hasLocDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("qualityCheck").get("locDate"), bef);
	}
	
	public static Specification<Inspection> hasLocDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("qualityCheck").get("locDate"), ld);
	}
	
	public static Specification<Inspection> hasQualityCheckIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("qualityCheck").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("qualityCheck").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("qualityCheck").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasQualityCheckId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("qualityCheck").get("id"), id);
	}
	
	public static Specification<Inspection> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<Inspection> hasInspectionStatus(InspectionStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<Inspection> hasInspectionResult(InspectionResult r){
		return(root, query, cb) -> r == null ? null : cb.equal(root.get("result"), r);
	}
	
	public static Specification<Inspection> hasNote(String note){
		return(root, query, cb) -> {
			if(note == null || note.isBlank()) return null;
			return cb.like(cb.lower(root.get("notes")), "%" +note.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inspection> hasQuantityRejectedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantityRejected"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantityRejected"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantityRejected"), max);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasQuantityRejected(Integer qp){
		return(root, query, cb) -> qp == null ? null : cb.equal(root.get("quantityRejected"), qp);
	}
	
	public static Specification<Inspection> hasQuantityAcceptedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantityAccepted"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantityAccepted"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantityAccepted"), max);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasQuantityAccepted(Integer qp){
		return(root, query, cb) -> qp == null ? null : cb.equal(root.get("quantityAccepted"), qp);
	}
	
	public static Specification<Inspection> hasQuantityInspectedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantityInspected"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantityInspected"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantityInspected"), max);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasQuantityInspected(Integer qp){
		return(root, query, cb) -> qp == null ? null : cb.equal(root.get("quantityInspected"), qp);
	}
	
	public static Specification<Inspection> hasInspectorFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			Predicate firstNamePredicate = null;
			Predicate lastNamePredicate = null;
			if(first != null && !first.isBlank()) {
				firstNamePredicate = cb.like(cb.lower(root.get("inspector").get("firstName")), "%" + first.toLowerCase().trim() + "%"); 
			}
			if(last != null && !last.isBlank()) {
				lastNamePredicate = cb.like(cb.lower(root.get("inspector").get("lastName")), "%" + last.toLowerCase().trim() + "%");
			}
			if(firstNamePredicate != null && lastNamePredicate != null) {
				cb.and(firstNamePredicate, lastNamePredicate);
			}
			else if(firstNamePredicate != null) {
				return firstNamePredicate;
			}
			else {
				return lastNamePredicate;
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasInspectorIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspector").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspector").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspector").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasInspectorId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspector").get("id"), id);
	}
	
	public static Specification<Inspection> hasProductStorageIdRange(Long from , Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("storage").get("id"), from , to);
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
	
	public static Specification<Inspection> hasProductStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("storage").get("id"), id);
	}
	
	public static Specification<Inspection> hasProductGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("goodsType"), type);
	}
	
	public static Specification<Inspection> hasProductStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("storageType"), type);
	}
	
	public static Specification<Inspection> hasProductSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("supplierType"), type);
	}
	
	public static Specification<Inspection> hasProductUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("product").get("unitMeasure"), um);
	}
	
	public static Specification<Inspection> hasProductName(String name){
		return(root, query, cb) -> {
			if(name == null || name.isBlank()) return null;
			return cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inspection> hasProductCurrentQuantityRange(BigDecimal min, BigDecimal max){
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
	
	public static Specification<Inspection> hasProductCurrentQuantity(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.equal(root.get("product").get("currentQuantity"), cq);
	}
	
	public static Specification<Inspection> hasProductIdRange(Long from, Long to){
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
	
	public static Specification<Inspection> hasProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("id"), id);
	}
	
	public static Specification<Inspection> hasBatchExpDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("batch").get("expiryDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("batch").get("expiryDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("batch").get("expiryDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasBatchExpDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("batch").get("expiryDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchExpDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("batch").get("expiryDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchExpDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("batch").get("expiryDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchProdDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("batch").get("productionDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("batch").get("productionDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("batch").get("productionDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasBatchProdDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("batch").get("productionDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchProdDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("batch").get("productionDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchProdDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("batch").get("productionDate"), ld);
	}
	
	public static Specification<Inspection> hasBatchQuantityProducedRange(Integer from, Integer to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("batch").get("quantityProduced"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("batch").get("quantityProduced"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("batch").get("quantityProduced"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasBatchQuantityProduced(Integer qp){
		return(root, query, cb) -> qp == null ? null : cb.equal(root.get("batch").get("quantityProduced"), qp);
	}
	
	public static Specification<Inspection> hasBatchProductIdRange(Long from, Long to){
		return(root, query, cb) ->{
			if(from != null && to != null) {
				return cb.between(root.get("batch").get("product").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("batch").get("product").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("batch").get("product").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasBatchProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("batch").get("product").get("id"), id);
	}
	
	public static Specification<Inspection> hasBatchCode(String code){
		return(root, query, cb) -> {
			if(code == null || code.isBlank()) return null;
			return cb.like(cb.lower(root.get("batch").get("id")), "%" + code.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inspection> hasBatchIdRange(Long from , Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("batch").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("batch").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("batch").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasBatchId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("batch").get("id"), id);
	}
	
	public static Specification<Inspection> hasInspectionDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("inspectionDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("inspectionDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("inspectionDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Inspection> hasInspectionDateAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("inspectionDate"), aft);
	}
	
	public static Specification<Inspection> hasInspectionDateBefore(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("inspectionDate"), ld);
	}
	
	public static Specification<Inspection> hasInspectionDate(LocalDateTime dt){
		return(root, query, cb) -> dt == null ? null : cb.equal(root.get("inspectionDate"), dt);
	}
	
	public static Specification<Inspection> hasInspectionType(InspectionType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
	}
	
	public static Specification<Inspection> hasInspectionCode(String code){
		return(root, query, cb) -> {
			if(code == null || code.isBlank()) return null;
			return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase().trim() + "%");
		};
	}

	public static Specification<Inspection> hasStorageNameLike(String storageName) {
        return (root, query, cb) -> {
            if (storageName == null || storageName.isBlank()) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.like(cb.lower(storage.get("name")), "%" + storageName.toLowerCase() + "%");
        };
    }

    public static Specification<Inspection> hasStorageLocationLike(String storageLocation) {
        return (root, query, cb) -> {
            if (storageLocation == null || storageLocation.isBlank()) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.like(cb.lower(storage.get("location")), "%" + storageLocation.toLowerCase() + "%");
        };
    }

    public static Specification<Inspection> hasStorageCapacityEqual(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.equal(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.greaterThanOrEqualTo(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityLessThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.lessThanOrEqualTo(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null || max == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.between(storage.get("capacity"), min, max);
        };
    }
}
