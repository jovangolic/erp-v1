package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.service.IMaterialTransactionService;
import com.jovan.erp_v1.exception.MaterialTransactionErrorException;
import com.jovan.erp_v1.repository.MaterialTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialTransactionService implements IMaterialTransactionService {

    private final MaterialTransactionRepository materialTransactionRepository;
    private final VendorRepository vendorRepository;

    @Transactional
    @Override
    public MaterialTransactionResponse create(MaterialTransactionRequest request) {

        return null;
    }

    @Transactional
    @Override
    public MaterialTransactionResponse update(Long id, MaterialTransactionRequest request) {
        return null;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialTransactionRepository.existsById(id)) {
            throw new MaterialTransactionErrorException("MaterialTransaction not found with id " + id);
        }
        materialTransactionRepository.deleteById(id);
    }

    @Override
    public MaterialTransactionResponse findOne(Long id) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findAll() {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Id(Long materialId) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_NameContainingIgnoreCase(String materialName) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Unit(UnitOfMeasure unit) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Storage_Id(Long storageId) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantity(BigDecimal quantity) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityLessThan(BigDecimal quantity) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByType(TransactionType type) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDate(LocalDate transactionDate) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateBetween(LocalDate transactionDateStart,
            LocalDate transactionDateEnd) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateGreaterThanEqual(LocalDate transactionDate) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_Id(Long vendorId) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_NameContainingIgnoreCase(String vendorName) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_EmailContainingIgnoreCase(String vendorEmail) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_Phone(String vendorPhone) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_AddressContainingIgnoreCase(String vendorAddress) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByDocumentReference(String documentReference) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByNotes(String notes) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByStatus(MaterialTransactionStatus status) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_Id(Long userId) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_FirstNameAndLastNameContainingIgnoreCase(
            String userFirstName,
            String userLastName) {
        return null;
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_EmailContainingIgnoreCase(String userEmail) {
        return null;
    }

    private void validateMaterialId(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Materijal sa ID " + materialId + " ne postoji.");
        }
    }

    private void validateMaterialRequestStatus(MaterialTransactionStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status za  MaterialTransactionStatus ne sme biti null.");
        }
    }

    private void validateVendorId(Long vendorId) {
        if (!vendorRepository_1.existsById(vendorId)) {
            throw new IllegalArgumentException("Vendor sa ID " + vendorId + " ne postoji.");
        }
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }
}
