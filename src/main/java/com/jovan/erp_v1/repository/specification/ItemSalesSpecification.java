package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.search_request.ItemSalesSearchRequest;

public class ItemSalesSpecification {
	
	public static Specification<ItemSales> fromRequest(ItemSalesSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))				
				.and(hasGoodsId(req.goodsId()))
				.and(hasGoodsIdRange(req.goodsIdFrom(), req.goodsIdTo()))
				.and(hasGoodsName(req.goodsName()))
				.and(hasUnitMeasure(req.unitMeasure()))
				.and(hasSupplierType(req.supplierType()))
				.and(hasStorageType(req.storageType()))
				.and(hasGoodsType(req.goodsType()))
				.and(hasGoodsStorageId(req.goodsStorageId()))
				.and(hasGoodsStorageIdRange(req.goodsStorageIdFrom(), req.goodsStorageIdTo()))
				.and(hasGoodsSupplyId(req.goodsSupplyId()))
				.and(hasGoodsSupplyIdRange(req.goodsSupplyIdFrom(), req.goodsSupplyIdTo()))
				.and(hasGoodsShelfId(req.goodsShelfId()))
				.and(hasGoodsShelfIdRange(req.goodsShelfIdFrom(), req.goodsShelfIdTo()))
				.and(hasSalesId(req.salesId()))
				.and(hasSalesIdRange(req.salesIdFrom(), req.salesIdTo()))
				.and(hasSalesBuyerId(req.salesBuyerId()))
				.and(hasSalesBuyerIdRange(req.salesBuyerIdFrom(), req.salesBuyerIdTo()))
				.and(hasSalesCreatedAt(req.salesCreatedAt()))
				.and(hasSalesCreatedAtBefore(req.salesCreatedAtBefore()))
				.and(hasSalesCreatedAtAfter(req.salesCreatedAtAfter()))
				.and(hasSalesCreatedAtRange(req.salesCreatedAtAfter(), req.salesCreatedAtBefore()))
				.and(hasSalesTotalPrice(req.salesTotalPrice()))
				.and(hasSalesTotalPriceRange(req.salesTotalPriceMin(), req.salesTotalPriceMax()))
				.and(hasSalesDescription(req.salesDescription()))
				.and(hasProcurementId(req.procurementId()))
				.and(hasProcurementIdRange(req.procurementIdFrom(), req.procurementIdTo()))
				.and(hasProcurementDate(req.procurementDate()))
				.and(hasProcurementDateBefore(req.procurementDateBefore()))
				.and(hasProcurementDateAfter(req.procurementDateAfter()))
				.and(hasProcurementDateRange(req.procurementDateAfter(), req.procurementDateBefore()))
				.and(hasProcurementTotalCost(req.procurementTotalCost()))
				.and(procurementTotalCostRange(req.procurementTotalCostMin(), req.procurementTotalCostMax()))
				.and(hasSalesOrderId(req.salesOrderId()))
				.and(hasSalesOrderIdRange(req.salesOrderIdFrom(), req.salesOrderIdTo()))
				.and(hasSalesOrderNumber(req.orderNumber()))
				.and(hasSalesOrderDate(req.orderDate()))
				.and(hasSalesOrderDateBefore(req.orderDateBefore()))
				.and(hasSalesOrderDateAfter(req.orderDateAfter()))
				.and(hasSalesOrderDateRange(req.orderDateAfter(), req.orderDateBefore()))
				.and(hasTotalAmount(req.totalAmount()))
				.and(hasTotalAmountRange(req.totalAmountMin(), req.totalAmountMax()))
				.and(hasSalesOrdBuyerId(req.salesOrdBuyerId()))
				.and(hasSalesOrdBuyerIdRange(req.salesOrdBuyerIdFrom(), req.salesOrdBuyerIdTo()))
				.and(hasSalesOrdInvoiceId(req.salesOrdInvoiceId()))
				.and(hasSalesOrdInvoiceIdRange(req.salesOrdInvoiceIdFrom(), req.salesOrdInvoiceIdTo()))
				.and(hasSalesOrderStatus(req.orderStatus()))
				.and(hasSalesOrderNote(req.salesOrdNote()))
				.and(hasQuantity(req.quantity()))
				.and(hasQuantityRange(req.quantityMin(), req.quantityMax()))
				.and(hasUnitPrice(req.unitPrice()))
				.and(hasUnitPriceRange(req.unitPriceMin(), req.unitPriceMax()))
				.and(hasBuyerId(req.salesBuyerId()));
	}
	
	public static Specification<ItemSales> hasId(Long id){
		return(root, query, cb) -> id == null ? null  :cb.equal(root.get("id"), id);
	}
	
	public static Specification<ItemSales> hasIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("id"), from , to);
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
	
	public static Specification<ItemSales> hasUnitPriceRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("unitPrice"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("unitPrice"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("unitPrice"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasUnitPrice(BigDecimal bd){
		return(root ,query, cb) -> bd == null ? null : cb.equal(root.get("unitPrice"), bd);
	}
	
	public static Specification<ItemSales> hasQuantityRange(BigDecimal min, BigDecimal max){
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
	
	public static Specification<ItemSales> hasQuantity(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("quantity"), bd);
	}
	
	public static Specification<ItemSales> hasSalesOrderNote(String str){
		return(root, query, cb) -> {
			if(str == null || str.trim().isBlank()) return null;
			return cb.like(cb.lower(root.get("salesOrder").get("note")), "%" + str.trim().toLowerCase() + "%");
		};
	}
	
	public static Specification<ItemSales> hasSalesOrdInvoiceIdRange(Long from ,Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("salesOrder").get("invoice").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("invoice").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("invoice").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesOrdInvoiceId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("salesOrder").get("invoice").get("id"), id);
	}
	
	public static Specification<ItemSales> hasSalesOrdBuyerIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("salesOrder").get("buyer").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesOrdBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("salesOrder").get("buyer").get("id"), id);
	}
	
	public static Specification<ItemSales> hasTotalAmountRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("salesOrder").get("totalAmount"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("totalAmount"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("totalAmount"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasTotalAmount(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("salesOrder").get("totalAmount"), bd);
	}
	 
	public static Specification<ItemSales> hasSalesOrderDateRange(LocalDateTime min, LocalDateTime max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("salesOrder").get("orderDate"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("orderDate"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("orderDate"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesOrderDateAfter(LocalDateTime aft){
		return(root ,query, cb) -> aft == null ? null : cb.greaterThan(root.get("salesOrder").get("orderDate"), aft);
	}
	
	public static Specification<ItemSales> hasSalesOrderDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("salesOrder").get("orderDate"), bef);
	}
	
	public static Specification<ItemSales> hasSalesOrderDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("salesOrder").get("orderDate"), ld);
	}
	
	public static Specification<ItemSales> hasSalesOrderNumber(String str){
		return(root, query, cb) -> {
			if(str == null || str.trim().isBlank()) return null;
			return cb.like(cb.lower(root.get("salesOrder").get("orderNumber")), "%" + str.trim().toLowerCase() + "%");
		};
	}
	
	public static Specification<ItemSales> hasSalesOrderIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("salesOrder").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesOrderId(Long id){
		return(root ,query, cb) -> id == null ? null : cb.equal(root.get("salesOrder").get("id"), id);
	}
	
	public static Specification<ItemSales> procurementTotalCostRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("procurement").get("totalCost"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("procurement").get("totalCost"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("procurement").get("totalCost"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasProcurementTotalCost(BigDecimal bd){
		return(root ,query, cb) -> bd == null ? null : cb.equal(root.get("procurement").get("totalCost"), bd);
	}

	public static Specification<ItemSales> hasProcurementDateRange(LocalDateTime min, LocalDateTime max){
		return(root, query,cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("procurement").get("locdate"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("procurement").get("locdate"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("procurement").get("locdate"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasProcurementDateAfter(LocalDateTime aft){
		return(root, query ,cb) -> aft == null ? null : cb.greaterThan(root.get("procurement").get("locdate"), aft);
	}
	
	public static Specification<ItemSales> hasProcurementDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("procurement").get("locdate"), bef);
	}
	
	public static Specification<ItemSales> hasProcurementDate(LocalDateTime ld){
		return(root ,query, cb) -> ld == null ? null : cb.equal(root.get("procurement").get("locdate"), ld);
	} 
	
	public static Specification<ItemSales> hasProcurementIdRange(Long from, Long to){
		return(root,  query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("procurement").get("id"), from, to);
			}
			else if(from != null ) {
				return cb.greaterThanOrEqualTo(root.get("procurement").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("procurement").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasProcurementId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("procurement").get("id"), id);
	}
	
	public static Specification<ItemSales> hasSalesDescription(String str){
		return(root, query, cb) -> {
			if(str == null || str.trim().isBlank()) return null;
			return cb.like(cb.lower(root.get("sales").get("salesDescription")), "%" + str.trim().toLowerCase() + "%");
		};
	}
	
	public static Specification<ItemSales> hasSalesTotalPriceRange(BigDecimal min, BigDecimal max){
		return(root ,query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("sales").get("totalPrice"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("sales").get("totalPrice"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("sales").get("totalPrice"), max);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesTotalPrice(BigDecimal bd){
		return(root ,query, cb) -> bd == null ? null : cb.equal(root.get("sales").get("totalPrice"), bd);
	}
	
	public static Specification<ItemSales> hasSalesCreatedAtRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("sales").get("createdAt"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("sales").get("createdAt"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("sales").get("createdAt"), end);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesCreatedAtAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("sales").get("createdAt"), aft);
	}
	
	public static Specification<ItemSales> hasSalesCreatedAtBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("sales").get("createdAt"), bef);
	}
	
	public static Specification<ItemSales> hasSalesCreatedAt(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("sales").get("createdAt"), ld);
	}
	
	public static Specification<ItemSales> hasSalesBuyerIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("sales").get("buyer").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("sales").get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("sales").get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("sales").get("buyer").get("id"), id);
	}
	
	public static Specification<ItemSales> hasSalesIdRange(Long from, Long to){
		return(root, query ,cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("sales").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("sales").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("sales").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasSalesId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("sales").get("id"), id);
	}
	
	public static Specification<ItemSales> hasGoodsShelfIdRange(Long from, Long to){
		return(root,query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("goods").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasGoodsShelfId(Long id){
		return(root ,query, cb) -> id == null ? null : cb.equal(root.get("goods").get("shelf").get("id"), id);
	}
	
	public static Specification<ItemSales> hasGoodsSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("goods").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasGoodsSupplyId(Long id){
		return(root ,query, cb) -> id == null ? null : cb.equal(root.get("goods").get("supply").get("id"), id);
	}
	
	public static Specification<ItemSales> hasGoodsStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("goods").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ItemSales> hasGoodsStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("goods").get("storage").get("id"), id);
	}
	
	public static Specification<ItemSales> hasGoodsType(GoodsType gt){
		return(root, query, cb) -> gt == null ? null : cb.equal(root.get("goods").get("goodsType"), gt);
	}
	
	public static Specification<ItemSales> hasStorageType(StorageType st){
		return(root, query, cb) -> st == null ? null : cb.equal(root.get("goods").get("storageType"), st);
	}
	
	public static Specification<ItemSales> hasSupplierType(SupplierType st){
		return(root, query, cb) -> st == null ? null : cb.equal(root.get("goods").get("supplierType"), st);
	}
	
	public static Specification<ItemSales> hasUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("unitMeasure"), um);
	}
	
	public static Specification<ItemSales> hasGoodsName(String name){
		return(root ,query, cb) -> {
			if (name == null || name.trim().isBlank()) return null;
			return cb.like(cb.lower(root.get("goods").get("name")), "%" + name.trim().toLowerCase() + "%");
		};
	}
	
	public static Specification<ItemSales> hasGoodsIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("goods").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("id"), to);
			}
			return null;
		};
	}

	public static Specification<ItemSales> hasGoodsId(Long goodsId) {
        return (root, query, cb) -> cb.equal(root.get("goods").get("id"), goodsId);
    }

    public static Specification<ItemSales> hasBuyerId(Long buyerId) {
        return (root, query, cb) -> cb.equal(root.get("sales").get("buyer").get("id"), buyerId);
    }

    public static Specification<ItemSales> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> cb.greaterThan(root.get("sales").get("createdAt"), date);
    }

    public static Specification<ItemSales> quantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), quantity);
    }

    public static Specification<ItemSales> unitPriceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> cb.between(root.get("unitPrice"), min, max);
    }

    public static Specification<ItemSales> hasSalesOrderStatus(OrderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("salesOrder").get("status"), status);
    }
    
    public static Specification<ItemSales> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("sales").get("createdAt"), start, end);
    }
    
    public static Specification<ItemSales> totalPriceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("sales").get("totalPrice"), min, max);
    }
}
