package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;

import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;

public interface IMaterialTransactionService {

        MaterialTransactionResponse create(MaterialTransactionRequest request);

        MaterialTransactionResponse update(Long id, MaterialTransactionRequest request);

        void delete(Long id);

        MaterialTransactionResponse findOne(Long id);

        List<MaterialTransactionResponse> findAll();

        List<MaterialTransactionResponse> findByMaterial_Id(Long materialId);

        List<MaterialTransactionResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode);

        List<MaterialTransactionResponse> findByMaterial_NameContainingIgnoreCase(String materialName);

        List<MaterialTransactionResponse> findByMaterial_Unit(UnitOfMeasure unit);

        List<MaterialTransactionResponse> findByMaterial_CurrentStock(BigDecimal currentStock);

        List<MaterialTransactionResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);

        List<MaterialTransactionResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);

        List<MaterialTransactionResponse> findByMaterial_Storage_Id(Long storageId);

        List<MaterialTransactionResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel);

        List<MaterialTransactionResponse> findByQuantity(BigDecimal quantity);

        List<MaterialTransactionResponse> findByQuantityGreaterThan(BigDecimal quantity);

        List<MaterialTransactionResponse> findByQuantityLessThan(BigDecimal quantity);

        List<MaterialTransactionResponse> findByType(TransactionType type);

        List<MaterialTransactionResponse> findByTransactionDate(LocalDate transactionDate);

        List<MaterialTransactionResponse> findByTransactionDateBetween(LocalDate transactionDateStart,
                        LocalDate transactionDateEnd);

        List<MaterialTransactionResponse> findByTransactionDateGreaterThanEqual(LocalDate transactionDate);

        List<MaterialTransactionResponse> findByVendor_Id(Long vendorId);

        List<MaterialTransactionResponse> findByVendor_NameContainingIgnoreCase(String vendorName);

        List<MaterialTransactionResponse> findByVendor_EmailContainingIgnoreCase(String vendorEmail);

        List<MaterialTransactionResponse> findByVendor_Phone(String vendorPhone);

        List<MaterialTransactionResponse> findByVendor_AddressContainingIgnoreCase(String vendorAddress);

        List<MaterialTransactionResponse> findByDocumentReference(String documentReference);

        List<MaterialTransactionResponse> findByNotes(String notes);

        List<MaterialTransactionResponse> findByStatus(MaterialTransactionStatus status);

        List<MaterialTransactionResponse> findByCreatedByUser_Id(Long userId);

        List<MaterialTransactionResponse> findByCreatedByUser_FirstNameAndLastNameContainingIgnoreCase(
                        String userFirstName,
                        String userLastName);

        List<MaterialTransactionResponse> findByCreatedByUser_EmailContainingIgnoreCase(String userEmail);
}
