package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageStatus;
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
    List<Shelf> findByStorage_NameContainingIgnoreCase(String name);
	List<Shelf> findByStorage_LocationContainingIgnoreCase(String location);
	List<Shelf> findByStorage_Type(StorageType type);
	List<Shelf> findByStorage_CapacityGreaterThan(BigDecimal capacity);
	List<Shelf> findByStorage_NameContainingIgnoreCaseAndStorage_Type(String name, StorageType type);
	
	List<Shelf> findByStorage_CapacityLessThan(BigDecimal capacity);
	List<Shelf> findByStorage_Capacity(BigDecimal capacity);
	List<Shelf> findByStorage_Status(StorageStatus status);
	@Query("SELECT s FROM Shelf s WHERE s.storage.type = :type AND s.storage.status = :status ")
	List<Shelf> findByStorageTypeAndStatus(@Param("type") StorageType type,@Param("status") StorageStatus status);
	@Query("SELECT s FROM Shelf s WHERE LOWER(s.storage.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.storage.location = :location")
	List<Shelf> findByStorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@Param("name") String name,@Param("location") String location);
	@Query("SELECT s FROM Shelf s WHERE LOWER(s.storage.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.storage.capacity = :capacity")
	List<Shelf> findByStorageNameContainingIgnoreCaseAndCapacity(@Param("name") String name,@Param("capacity") BigDecimal capacity);
	@Query("SELECT s FROM Shelf s WHERE LOWER(s.storage.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.storage.capacity  >= :capacity")
	List<Shelf> findByStorageNameContainingIgnoreCaseAndCapacityGreaterThan(@Param("name") String name,@Param("capacity") BigDecimal capacity);
	@Query("SELECT s FROM Shelf s WHERE LOWER(s.storage.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.storage.capacity <= :capacity")
	List<Shelf> findByStorageNameContainingIgnoreCaseAndCapacityLessThan(@Param("name") String name,@Param("capacity") BigDecimal capacity);
	@Query("SELECT s FROM Shelf s WHERE LOWER(s.storage.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.storage.status = :status")
	List<Shelf> findByStorageNameContainingIgnoreCaseAndStatus(@Param("name") String name,@Param("status") StorageStatus status);
	@Query("SELECT s FROM Shelf s WHERE COALESCE(s.storage.capacity, 0) BETWEEN :min AND :max")
	List<Shelf> findByStorageCapacityBetween(@Param("min") BigDecimal min,@Param("max") BigDecimal max);
}
