package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    List<Storage> findByType(StorageType type);

    List<Storage> findByName(String name);

    List<Storage> findByLocation(String location);

    List<Storage> findByCapacity(BigDecimal capacity);

    List<Storage> findByNameAndLocation(String name, String location);

    List<Storage> findByTypeAndCapacityGreaterThan(StorageType type, Double capacity);

    @Query("SELECT s FROM Storage s WHERE SIZE(s.goods) > :minCount")
    List<Storage> findStoragesWithMinGoods(@Param("minCount") int minCount);

    List<Storage> findByNameContainingIgnoreCase(String name);
}
