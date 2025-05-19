package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.response.GoodsResponse;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

	List<Goods> findByName(String name);
	
	Goods findGoodsById(Long id);

    List<Goods> findByNameContainingIgnoreCase(String name);

    List<Goods> findByStorage(Storage storage);

    @Query("SELECT g FROM Goods g WHERE g.name = :name AND g.storageType = :type")
    List<Goods> findByNameAndStorageType(@Param("name") String name, @Param("type") StorageType type);

    @Query("SELECT g FROM Goods g WHERE g.name = :name AND g.supplierType = :type")
    List<Goods> findByNameAndSupplierType(@Param("name") String name, @Param("type") SupplierType type);

    @Query("SELECT g FROM Goods g WHERE g.goodsType = :type")
    List<Goods> findByGoodsType(@Param("type") GoodsType type);

    @Query("SELECT g FROM Goods g WHERE g.storage.name = :storageName")
    List<Goods> findByStorageName(@Param("storageName") String storageName);

    @Query("SELECT g FROM Goods g WHERE g.supply.id = :supplyId")
    List<Goods> findBySupplyId(@Param("supplyId") Long supplyId);

    @Query("SELECT g FROM Goods g WHERE g.unitMeasure = :unitMeasure")
    List<Goods> findByUnitMeasure(@Param("unitMeasure") String unitMeasure);
    
    @Query("SELECT g FROM Goods g JOIN g.barCodes b WHERE b.code = :barCode")
    List<Goods> findAllByBarCodes(@Param("barCode") String barCode);
    
    @Query("SELECT g FROM Goods g JOIN g.barCodes b WHERE b.code = :barCode AND g.goodsType = :goodsType")
    List<Goods> findAllByBarCodesAndGoodsType(@Param("barCode") String barCode, @Param("goodsType") GoodsType goodsType);
    
    @Query("SELECT g FROM Goods g JOIN g.barCodes b WHERE b.code = :barCode AND g.storage.id = :storageId")
    List<Goods> findAllByBarCodesAndStorageId(@Param("barCode") String barCode, @Param("storageId") Long storageId);
    
    @Query("SELECT b.goods FROM BarCode b WHERE b.code = :code")
    Optional<Goods> findGoodsByBarCode(@Param("code") String code);
    
    @Query("SELECT g FROM Goods g WHERE g.shelf.id = :shelf_id")
    List<Goods> findByShelfId(@Param("shelf_id") Long shelf_id);
}

