package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.response.RawMaterialResponse;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long>, JpaSpecificationExecutor<RawMaterial> {

	List<RawMaterial> findByName(String name);

	@Query("SELECT r FROM RawMaterial r JOIN r.barCodes b WHERE b.code = :code")
	List<RawMaterial> findByBarCodes(@Param("code") String code); // <- koristi stream().findFirst() u servisu

	List<RawMaterial> findByProductId(Long productId);

	List<RawMaterial> findByNameContainingIgnoreCase(String name);

	@Query("select r from RawMaterial r where r.storage.name = :storageName")
	List<RawMaterial> findByStorageName(@Param("storageName") String storageName);

	/*
	 * @Query("select r from RawMaterial r where r.unitMeasure = :unitMeasure")
	 * List<RawMaterial> findByUnitMeasure(@Param("unitMeasure") String
	 * unitMeasure);
	 */
	List<RawMaterial> findByUnitMeasure(UnitMeasure unitMeasure);

	@Query("select r from RawMaterial r where r.supply.id = :supplyId")
	List<RawMaterial> findBySupplyId(@Param("supplyId") Long supplyId);

	@Query("select r from RawMaterial r where r.supplierType = :type")
	List<RawMaterial> findBysupplierType(@Param("type") SupplierType type);

	@Query("select r from RawMaterial r where r.storageType = :type")
	List<RawMaterial> findByStorageType(@Param("type") StorageType storageType);

	@Query("select r from RawMaterial r where r.goodsType = :type")
	List<RawMaterial> findByGoodsType(@Param("type") GoodsType goodsType);
	//nove metode
	boolean existsByBarCodes_Code(String code);
	List<RawMaterial> findByCurrentQuantity(BigDecimal currentQuantity);
	List<RawMaterial> findByCurrentQuantityLessThan(BigDecimal currentQuantity);
	List<RawMaterial> findByCurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<RawMaterial> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
	List<RawMaterial> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<RawMaterial> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
	@Query("SELECT rm FROM RawMaterial rm WHERE rm.shelf.id = :shelfId")
	List<RawMaterial> findByShelf_Id(@Param("shelfId") Long shelfId);
	Long countByShelf_RowCount(Integer rowCount);
    Long countByShelf_Cols(Integer cols);
    @Query("SELECT rm FROM RawMaterial rm WHERE rm.shelf.id = :shelfId AND rm.currentQuantity > :quantity")
    List<RawMaterial> findByShelfAndQuantityGreaterThan(@Param("shelfId") Long shelfId, @Param("quantity") BigDecimal quantity);
    @Query("SELECT rm FROM RawMaterial rm WHERE rm.shelf.id = :shelfId AND rm.currentQuantity < :quantity")
    List<RawMaterial> findByShelfAndQuantityLessThan(@Param("shelfId") Long shelfId, @Param("quantity") BigDecimal quantity);
    @Query("SELECT rm FROM RawMaterial rm WHERE rm.shelf.id = :shelfId AND rm.currentQuantity = :quantity")
    List<RawMaterial> findByShelfAndExactQuantity(@Param("shelfId") Long shelfId, @Param("quantity") BigDecimal quantity);
    List<RawMaterial> findByShelf_IdAndCurrentQuantityGreaterThan(Long shelfId, BigDecimal quantity);
    List<RawMaterial> findByShelf_IdAndCurrentQuantityLessThan(Long shelfId, BigDecimal quantity);
    List<RawMaterial> filterRawMaterials(Long shelfId, BigDecimal minQty, BigDecimal maxQty, Long productId);
    boolean existsByShelf_RowCount(Integer rowCount);
    boolean existsByShelf_Cols(Integer cols);
}
