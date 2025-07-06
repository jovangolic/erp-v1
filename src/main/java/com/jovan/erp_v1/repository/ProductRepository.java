package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.enumeration.UnitMeasure;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    @Query("SELECT p FROM Product p JOIN p.barCodes b WHERE b.code = :code")
    Optional<Product> findByBarCodes(@Param("code") String code);

    @Query("select p from Product p where p.storage.name = :storageName")
    List<Product> findByStorageName(@Param("storageName") String storageName);

    @Query("select p from Product p where p.name = :name and p.storageType = :type")
    List<Product> findByNameAndStorageType(@Param("name") String name, @Param("type") StorageType type);

    @Query("select p from Product p where p.name = :name and p.supplierType = :type")
    List<Product> findByNameAndSupplierType(@Param("name") String name, @Param("type") SupplierType type);

    @Query("select p from Product p where p.currentQuantity <= :quantity")
    List<Product> findByCurrentQuantityLessThan(@Param("quantity") BigDecimal currentQuantity);

    @Query("select p from Product p where p.supplierType = :supplierType")
    List<Product> findBySupplierType(@Param("supplierType") SupplierType supplierType);

    @Query("select p from Product p where p.storageType = :type")
    List<Product> findByStorageType(@Param("type") StorageType storageType);

    @Query("select p from Product p where p.goodsType = :gType")
    List<Product> findByGoodsType(@Param("gType") GoodsType goodsType);

    /*
     * @Query("select p from Product p where p.unitMeasure = :unitMeasure")
     * List<Product> findByUnitMeasure(@Param("unitMeasure") String unitMeasure);
     */
    List<Product> findByUnitMeasure(UnitMeasure unitMeasure);

    @Query("select p from Product p where p.supply.id = :supplyId")
    List<Product> findBySupplyId(@Param("supplyId") Long supplyId);

    @Query("select p from Product p where p.storage = :storage")
    List<Product> findByStorage(@Param("storage") Storage storage);
    
    //nove metode
    @Query("select p from Product p where p.shelf.rowCount = :row and p.shelf.cols = :col and p.shelf.storage.id = :storageId")
    List<Product> findByShelfRowColAndStorage(@Param("row") Integer row,
                                              @Param("col") Integer col,
                                              @Param("storageId") Long storageId);

    @Query("select p from Product p where p.shelf.rowCount = :row")
    List<Product> findByShelfRow(@Param("row") Integer row);

    @Query("select p from Product p where p.shelf.cols = :col")
    List<Product> findByShelfColumn(@Param("col") Integer col);
    
    @Query("select p from Product p where p.supply.quantity >= :quantity")
    List<Product> findBySupplyMinQuantity(@Param("quantity") BigDecimal quantity);

    @Query("select p from Product p where p.supply.updates >= :from and p.supply.updates <= :to")
    List<Product> findBySupplyUpdateRange(@Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);

    @Query("select p from Product p where p.supply.storage.id = :storageId")
    List<Product> findBySupplyStorageId(@Param("storageId") Long storageId);
    Long countByShelfRowCount(Integer rowCount);
    Long countByShelfCols(Integer cols);
    boolean existsByShelfRowCount(Integer rowCount);
    boolean existsByShelfCols(Integer cols);
}
