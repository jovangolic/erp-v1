package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.User;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findById(Long id);

    List<Inventory> findByStorageEmployee(User storageEmployee);

    List<Inventory> findByStorageEmployeeId(Long storageEmployeeId); // ✔️

    List<Inventory> findByStorageForemanId(Long storageForemanId);   // ✔️

    List<Inventory> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Inventory> findByStatus(InventoryStatus status);

    List<Inventory> findByDate(LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.storageEmployee = :magacioner AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findInventoryByStorageEmployeeAndDateRange(@Param("magacioner") User magacioner, 
                                                                @Param("startDate") LocalDate startDate, 
                                                                @Param("endDate") LocalDate endDate);

    @Query("SELECT i FROM Inventory i WHERE i.status <> 'FINAL' OR i.aligned = false")
    List<Inventory> findPendingInventories();
}

