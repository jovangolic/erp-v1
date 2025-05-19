package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
