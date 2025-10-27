package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.statistics.inventory.InventoryEmployeeStatDTO;
import com.jovan.erp_v1.statistics.inventory.InventoryForemanStatDTO;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

    List<Inventory> findByStorageEmployee(User storageEmployee);
    List<Inventory> findByStorageEmployeeId(Long storageEmployeeId); 
    List<Inventory> findByStorageForemanId(Long storageForemanId); 
    List<Inventory> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Inventory> findByStatus(InventoryStatus status);
    List<Inventory> findByDate(LocalDate date);
    @Query("SELECT i FROM Inventory i WHERE i.storageEmployee = :magacioner AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findInventoryByStorageEmployeeAndDateRange(@Param("magacioner") User magacioner,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    @Query("SELECT i FROM Inventory i WHERE i.status <> 'FINAL' OR i.aligned = false")
    List<Inventory> findPendingInventories();
    
    //nove metode
    List<Inventory> findByDateAfter(LocalDate date);
    List<Inventory> findByDateBefore(LocalDate date);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageEmployee.firstName) LIKE LOWER(CONCAT('%',:firstname, '%')) AND LOWER(i.storageEmployee.lastName) LIKE LOWER(CONCAT('%',:lastName, '%'))")
    List<Inventory> findByStorageEmployee_FullNameContainingIgnoreCase(@Param("firstName") String firstName,@Param("lastName") String lastName);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageForeman.firstName) LIKE LOWER(CONCAT('%',:firstname, '%')) AND LOWER(i.storageForeman.lastName) LIKE LOWER(CONCAT('%',:lastName, '%'))")
    List<Inventory> findBystorageForeman_FullNameContainingIgnoreCase(@Param("firstName") String firstName,@Param("lastName") String lastName);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageEmployee.email) LIKE LOWER(CONCAT('%',:email, '%'))")
    List<Inventory> findByStorageEmployee_EmailLikeIgnoreCase(@Param("email") String email);
    @Query("SELECT i FROM Inventory i WHERE i.storageEmployee.address = :address")
    List<Inventory> findByStorageEmployee_Address(@Param("address") String address);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageEmployee.phoneNumber) LIKE LOWER(CONCAT('%',:phoneNumber, '%'))")
    List<Inventory> findByStorageEmployee_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT i FROM Inventory i WHERE i.storageForeman.address = :address")
    List<Inventory> findByStorageForeman_Address(@Param("address") String address);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageForeman.phoneNumber) LIKE LOWER(CONCAT('%',:phoneNumber, '%'))")
    List<Inventory> findByStorageForeman_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.storageForeman.email) LIKE LOWER(CONCAT('%',:email, '%'))")
    List<Inventory> findByStorageForeman_EmailLikeIgnoreCase(@Param("email") String email);
    @Query("SELECT i FROM Inventory i WHERE i.status = :status AND LOWER(i.storageEmployee.firstName) LIKE LOWER(CONCAT('%',:firstname, '%')) AND LOWER(i.storageEmployee.lastName) LIKE LOWER(CONCAT('%',:lastName, '%'))")
    List<Inventory> findByStatusAndStorageEmployeeFullNameContainingIgnoreCase(@Param("status") InventoryStatus status, @Param("firstName") String firstName,@Param("lastName") String lastName);
    @Query("SELECT i FROM Inventory i WHERE i.status = :status AND LOWER(i.storageForeman.firstName) LIKE LOWER(CONCAT('%',:firstname, '%')) AND LOWER(i.storageForeman.lastName) LIKE LOWER(CONCAT('%',:lastName, '%'))")
    List<Inventory> findByStatusAndStorageForemanFullNameContainingIgnoreCase(@Param("status") InventoryStatus status, @Param("firstName") String firstName,@Param("lastName") String lastName);
    @Query("SELECT i FROM Inventory i WHERE i.storageForeman.id = :magacionerId AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findInventoryByStorageForemanIdAndDateRange(@Param("magacionerId") Long magacionerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    @Query("SELECT i FROM Inventory i WHERE i.storageEmployee.id = :employeeId AND i.status = :status")
    List<Inventory> findByStorageEmployeeIdAndStatus(@Param("employeeId") Long employeeId,
                                                     @Param("status") InventoryStatus status);
    @Query("SELECT i FROM Inventory i WHERE i.storageForeman.id = :foremanId AND i.status = :status")
    List<Inventory> findByStorageForemanIdAndStatus(@Param("foremanId") Long foremanId,
                                                    @Param("status") InventoryStatus status);
    @Query("SELECT i FROM Inventory i WHERE i.storageEmployee.id = :employeeId AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findByStorageEmployeeIdAndDateBetween(@Param("employeeId") Long employeeId,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
    @Query("SELECT i FROM Inventory i WHERE i.storageForeman.id = :foremanId AND i.date BETWEEN :startDate AND :endDate")
    List<Inventory> findByStorageForemanIdAndDateBetween(@Param("foremanId") Long foremanId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.storageForeman.id = :foremanId")
    Long countByStorageForemanId(@Param("foreman") Long foremanId);
    Boolean existsByStatus(InventoryStatus status);
    
    //nove metode
    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.inventoryItems WHERE i.id = :id")
    List<Inventory> trackInventory(@Param("id") Long id);
    @Query("""
    		SELECT new com.jovan.erp_v1.statistics.inventory.InventoryEmployeeStatDTO(
    		COUNT(inv),
    		inv.storageEmployeeId
    		)
    		FROM Inventory inv
    		WHERE inv.confirmed = true
    		GROUP BY inv.storageEmployee.id, inv.status, inv.typeStatus
    		""")
    List<InventoryEmployeeStatDTO> countInventoryByEmployee();
    @Query("""
    		SELECT new com.jovan.erp_v1.statistics.inventory.InventoryForemanStatDTO(
    		COUNT(inv),
    		inv.storageForemanId
    		)
    		FROM Inventory inv
    		WHERE inv.confirmed = true
    		GROUP BY inv.storageForemanId.id, inv.status, inv.typeStatus
    		""")
    List<InventoryForemanStatDTO> countInventoryByForeman();
}
