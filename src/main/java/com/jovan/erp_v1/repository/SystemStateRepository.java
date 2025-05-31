package com.jovan.erp_v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.SystemState;

@Repository
public interface SystemStateRepository extends JpaRepository<SystemState, Long> {

    Optional<SystemState> findTopByOrderByIdDesc();
}
