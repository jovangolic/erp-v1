package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Driver;

@Repository
public interface DriversRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByName(String name);

    Optional<Driver> findByPhone(String phone);
}
