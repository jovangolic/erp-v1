package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.response.RawMaterialResponse;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

	 List<RawMaterial> findByName(String name);

	 @Query("SELECT r FROM RawMaterial r JOIN r.barCodes b WHERE b.code = :code")
	 List<RawMaterial> findByBarCodes(@Param("code") String code); // <- koristi stream().findFirst() u servisu

	 List<RawMaterial> findByProductId(Long productId);

	 List<RawMaterial> findByNameContainingIgnoreCase(String name);

	 @Query("select r from RawMaterial r where r.storage.name = :storageName")
	 List<RawMaterial> findByStorageName(@Param("storageName") String storageName);

	 @Query("select r from RawMaterial r where r.unitMeasure = :unitMeasure")
	 List<RawMaterial> findByUnitMeasure(@Param("unitMeasure") String unitMeasure);

	 @Query("select r from RawMaterial r where r.supply.id = :supplyId")
	 List<RawMaterial> findBySupplyId(@Param("supplyId") Long supplyId);

	 @Query("select r from RawMaterial r where r.supplierType = :type")
	 List<RawMaterial> findBysupplierType(@Param("type") SupplierType type);

	 @Query("select r from RawMaterial r where r.storageType = :type")
	 List<RawMaterial> findByStorageType(@Param("type") StorageType storageType);

	 @Query("select r from RawMaterial r where r.goodsType = :type")
	 List<RawMaterial> findByGoodsType(@Param("type") GoodsType goodsType);
}
