package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.BarCode;


@Repository
public interface BarCodeRepository extends JpaRepository<BarCode, Long>, JpaSpecificationExecutor<BarCode> {

	Optional<BarCode> findByCode(String code);

    List<BarCode> findByGoods_Id(Long goodsId);
    List<BarCode> findByGoods_Name(String goodsName);

    boolean existsByCode(String code);

    List<BarCode> findByScannedAtBetween(LocalDateTime from, LocalDateTime to);
    List<BarCode> findByScannedBy_Id(Long scannedById);
    List<BarCode> findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(
            String userFirstName, String userLastName);
    
    //nove metode
    @Query("SELECT bc FROM BarCode bc WHERE bc.id = :id")
    Optional<BarCode> trackBarCode(@Param("id") Long id);
}
