package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Shelf;


@Repository
public interface ShelfRepository extends JpaRepository<Shelf,Long> {
	
	boolean existsByRowCountAndStorageId(Integer rows, Long storageId);
	boolean existsByColsAndStorageId(Integer cols, Long storageId);
	boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId);
	List<Shelf> findByStorageId(Long storageId);
    Optional<Shelf> findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId);
    List<Shelf> findByRowCountAndStorageId(Integer rows, Long storageId);
    List<Shelf> findByColsAndStorageId(Integer cols, Long storageId);
    
    //nove metode
    List<Shelf> findByStorage_Name(String name);
	List<Shelf> findByStorage_Location(String location);
	List<Shelf> findByStorage_Type(StorageType type);
	List<Shelf> findByStorage_CapacityGreaterThan(BigDecimal capacity);
	List<Shelf> findByStorage_NameAndStorage_Type(String name, StorageType type);
}
