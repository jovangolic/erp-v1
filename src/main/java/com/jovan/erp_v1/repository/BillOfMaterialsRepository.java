package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.BillOfMaterials;

@Repository
public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long>, JpaSpecificationExecutor<BillOfMaterials> {

    List<BillOfMaterials> findByParentProductId(Long parentProductId);
    List<BillOfMaterials> findByComponentId(Long componentId);
    List<BillOfMaterials> findByQuantityGreaterThan(BigDecimal quantity);
    List<BillOfMaterials> findByQuantity(BigDecimal quantity);
    List<BillOfMaterials> findByQuantityLessThan(BigDecimal quantity);
    
    //nove metode
    
    List<BillOfMaterials> findByParentProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<BillOfMaterials> findByParentProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<BillOfMaterials> findByParentProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<BillOfMaterials> findByComponent_CurrentQuantity(BigDecimal currentQuantity);
    List<BillOfMaterials> findByComponent_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<BillOfMaterials> findByComponent_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<BillOfMaterials> findByParentProductIdAndQuantityGreaterThan(Long parentProductId, BigDecimal quantity);
    List<BillOfMaterials> findByParentProductIdAndQuantityLessThan(Long parentProductId, BigDecimal quantity);
    Optional<BillOfMaterials> findByParentProductIdAndComponentId(Long parentProductId, Long componentId);
    Boolean existsByParentProductIdAndComponentId(Long parentProductId, Long componentId);
    void deleteByParentProductId(Long parentProductId);
    List<BillOfMaterials> findByParentProductIdOrderByQuantityDesc(Long parentProductId);
    List<BillOfMaterials> findByParentProductIdOrderByQuantityAsc(Long parentProductId);
    List<BillOfMaterials> findByQuantityBetween(BigDecimal min, BigDecimal max);
    @Query("SELECT b FROM BillOfMaterials b WHERE b.parentProduct.id = :productId AND b.component.name LIKE %:name%")
    List<BillOfMaterials> findComponentsByProductIdAndComponentNameContaining(@Param("productId") Long productId, @Param("name") String name);
    List<BillOfMaterials> findByComponent_NameContainingIgnoreCase(String name);
    List<BillOfMaterials> findByParentProduct_NameContainingIgnoreCase(String name);
    List<BillOfMaterials> findByComponent_GoodsType(GoodsType goodsType);
    List<BillOfMaterials> findByComponent_SupplierType(SupplierType supplierType);
    List<BillOfMaterials> findByParentProduct_UnitMeasure(UnitMeasure unitMeasure);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.component.shelf.id = :shelfId")
    List<BillOfMaterials> findByComponent_Shelf_Id(@Param("shelfId") Long shelfId);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.parentProduct.storage.id = :storageId")
    List<BillOfMaterials> findByParentProduct_Storage_Id(@Param("storageId") Long storageId);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.parentProduct.shelf.id = :shelfId")
    List<BillOfMaterials> findByParentProduct_Shelf_Id(@Param("shelfId") Long shelfId);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.component.storage.id = :storageId")
    List<BillOfMaterials> findByComponent_Storage_Id(@Param("storageId") Long storageId);
    List<BillOfMaterials> findByParentProduct_GoodsType(GoodsType goodsType);
    List<BillOfMaterials> findByParentProduct_SupplierType(SupplierType supplierType);
    List<BillOfMaterials> findByComponent_UnitMeasure(UnitMeasure unitMeasure);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.parentProduct.supply.id = :supplyId")
    List<BillOfMaterials> findByParentProduct_Supply_Id(@Param("supplyId") Long supplyId);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.component.supply.id = :supplyId")
    List<BillOfMaterials> findByComponent_Supply_Id(@Param("supplyId") Long supplyId);
    List<BillOfMaterials> findByComponent_StorageType(StorageType type);
    List<BillOfMaterials> findByParentProduct_StorageType(StorageType type);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.parentProduct.storage.id = :storageId AND bom.component.goodsType = :goodsType")
    List<BillOfMaterials> findByParentProduct_Storage_IdAndComponent_GoodsType(@Param("storageId") Long storageId,@Param("goodsType") GoodsType goodsType);
    @Query("""
    	    SELECT b FROM BillOfMaterials b
    	    WHERE b.parentProduct.storage.id = b.component.storage.id
    	""")
    List<BillOfMaterials> findWhereParentAndComponentShareSameStorage();
    @Query("""
    	    SELECT b FROM BillOfMaterials b
    	    WHERE b.quantity > :minQuantity AND b.component.goodsType = :goodsType
    	""")
    List<BillOfMaterials> findByMinQuantityAndComponentGoodsType(@Param("minQuantity") BigDecimal minQuantity,@Param("goodsType") GoodsType goodsType);
    @Query("""
    	    SELECT b FROM BillOfMaterials b
    	    WHERE b.component.storage.id = :storageId AND b.component.unitMeasure = :unitMeasure
    	""")
    List<BillOfMaterials> findByComponentStorageAndUnitMeasure(@Param("storageId") Long storageId,@Param("unitMeasure") UnitMeasure unitMeasure);
    @Query("SELECT b FROM BillOfMaterials b ORDER BY b.quantity DESC")
    List<BillOfMaterials> findAllOrderByQuantityDesc();
    @Query("SELECT b FROM BillOfMaterials b ORDER BY b.quantity ASC")
    List<BillOfMaterials> findAllOrderByQuantityAsc();
    
    //nove metode
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.parentProduct.id = :id")
    List<BillOfMaterials> trackParentProduct(@Param("id") Long id);
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.component.id = :id")
    List<BillOfMaterials> trackComponent(@Param("id") Long id);
}
