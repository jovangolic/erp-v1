package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.model.MaterialTransaction;

@Repository
public interface MaterialTransactionRepository extends JpaRepository<MaterialTransaction, Long> {

        List<MaterialTransaction> findByMaterial_Id(Long materialId);

        List<MaterialTransaction> findByMaterial_CodeContainingIgnoreCase(String materialCode);

        List<MaterialTransaction> findByMaterial_NameContainingIgnoreCase(String materialName);

        List<MaterialTransaction> findByMaterial_Unit(UnitOfMeasure unit);

        List<MaterialTransaction> findByMaterial_CurrentStock(BigDecimal currentStock);

        List<MaterialTransaction> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);

        List<MaterialTransaction> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);

        @Query("SELECT mt FROM MaterialTransaction mt WHERE mt.material.storage.id = :storageId")
        List<MaterialTransaction> findByMaterial_Storage_Id(@Param("storageId") Long storageId);

        List<MaterialTransaction> findByMaterial_ReorderLevel(BigDecimal reorderLevel);

        List<MaterialTransaction> findByQuantity(BigDecimal quantity);

        List<MaterialTransaction> findByQuantityGreaterThan(BigDecimal quantity);

        List<MaterialTransaction> findByQuantityLessThan(BigDecimal quantity);

        List<MaterialTransaction> findByType(TransactionType type);

        List<MaterialTransaction> findByTransactionDate(LocalDate transactionDate);

        List<MaterialTransaction> findByTransactionDateBetween(LocalDate transactionDateStart,
                        LocalDate transactionDateEnd);

        List<MaterialTransaction> findByTransactionDateGreaterThanEqual(LocalDate transactionDate);

        List<MaterialTransaction> findByVendor_Id(Long vendorId);

        List<MaterialTransaction> findByVendor_NameContainingIgnoreCase(String vendorName);

        List<MaterialTransaction> findByVendor_EmailContainingIgnoreCase(String vendorEmail);

        List<MaterialTransaction> findByVendor_PhoneNumber(String vendorPhone);

        List<MaterialTransaction> findByVendor_AddressContainingIgnoreCase(String vendorAddress);

        List<MaterialTransaction> findByDocumentReference(String documentReference);

        List<MaterialTransaction> findByNotes(String notes);

        List<MaterialTransaction> findByStatus(MaterialTransactionStatus status);

        List<MaterialTransaction> findByCreatedByUser_Id(Long userId);

        List<MaterialTransaction> findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(
                        String userFirstName, String userLastName);

        List<MaterialTransaction> findByCreatedByUser_EmailContainingIgnoreCase(String userEmail);
}
