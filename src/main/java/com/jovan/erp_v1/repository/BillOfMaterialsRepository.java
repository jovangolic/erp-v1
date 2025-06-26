package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.BillOfMaterials;

@Repository
public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long> {

    List<BillOfMaterials> findByParentProductId(Long parentProductId);

    List<BillOfMaterials> findByComponentId(Long componentId);

    List<BillOfMaterials> findByQuantityGreaterThan(BigDecimal quantity);

    List<BillOfMaterials> findByQuantity(BigDecimal quantity);

    List<BillOfMaterials> findByQuantityLessThan(BigDecimal quantity);
}
