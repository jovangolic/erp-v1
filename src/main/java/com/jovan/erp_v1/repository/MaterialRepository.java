package com.jovan.erp_v1.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import java.math.BigDecimal;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {

    List<Material> findByStorage_Id(Long storageId);

    List<Material> findByCode(String code);

    List<Material> findByNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM Material m WHERE " +
            "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:code IS NULL OR m.code = :code)")
    List<Material> search(@Param("name") String name, @Param("code") String code);

    List<Material> findByUnit(UnitOfMeasure unit);

    List<Material> findByStorage_Name(String storageName);

    List<Material> findByStorage_Capacity(BigDecimal capacity);

    List<Material> findByStorage_Type(StorageType type);

    List<Material> findByCurrentStock(BigDecimal currentStock);
    List<Material> findByCurrentStockGreaterThan(BigDecimal currentStock);
    List<Material> findByCurrentStockLessThan(BigDecimal currentStock);

    List<Material> findByReorderLevel(BigDecimal reorderLevel);
    boolean existsByCode(String code);

}
