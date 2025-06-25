package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.CapacityPlanning;

@Repository
public interface CapacityPlanningRepository extends JpaRepository<CapacityPlanning, Long> {

}
