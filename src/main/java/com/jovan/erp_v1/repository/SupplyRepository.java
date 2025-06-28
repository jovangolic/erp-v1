package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    List<Supply> findByStorage(Storage storage);

    List<Supply> findByUpdates(LocalDateTime updates);

    List<Supply> findByGoodsContaining(Goods good);

    List<Supply> findByGoods_Name(String name);

    @Query("SELECT s FROM Supply s JOIN s.goods g WHERE g.name = :name")
    List<Supply> findSuppliesByGoodsName(@Param("name") String name);

    @Query("SELECT s FROM Supply s WHERE s.quantity >= :minQuantity")
    List<Supply> findSuppliesWithMinQuantity(@Param("minQuantity") BigDecimal minQuantity);

    @Query("SELECT s FROM Supply s JOIN s.goods g WHERE TYPE(g) = :clazz")
    List<Supply> findSuppliesByGoodsClass(@Param("clazz") Class<? extends Goods> clazz);

    @Query("SELECT s FROM Supply s WHERE s.storage.id = :storageId")
    List<Supply> findSuppliesByStorageId(@Param("storageId") Long storageId);

}
