package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.service.IMaterialTransactionService;
import com.jovan.erp_v1.util.DateValidator;
import com.jovan.erp_v1.exception.MaterialTransactionErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.MaterialTransactionMapper;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.repository.MaterialTransactionRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialTransactionService implements IMaterialTransactionService {

    private final MaterialTransactionRepository materialTransactionRepository;
    private final VendorRepository vendorRepository;
    private final MaterialRepository materialRepository;
    private final MaterialTransactionMapper materialTransactionMapper;

    @Transactional
    @Override
    public MaterialTransactionResponse create(MaterialTransactionRequest request) {
        validateMaterialId(request.materialId());
        validateBigDecimal(request.quantity());
        validateTransactionType(request.type());
        DateValidator.validateNotNull(request.transactionDate(), "Datum ne sme biti null");
        validateVendorId(request.vendorId());
        validateString(request.documentReference());
        validateString(request.notes());
        validateMaterialTransactionStatus(request.status());
        validateUserId(request.createdByUserId());
        MaterialTransaction mt = materialTransactionMapper.toEntity(request);
        MaterialTransaction saved = materialTransactionRepository.save(mt);
        return materialTransactionMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialTransactionResponse update(Long id, MaterialTransactionRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	MaterialTransaction mt = materialTransactionRepository.findById(id).orElseThrow(() -> new MaterialTransactionErrorException("MaterialTransaction not found with id " + id));
    	validateMaterialId(request.materialId());
        validateBigDecimal(request.quantity());
        validateTransactionType(request.type());
        DateValidator.validateNotNull(request.transactionDate(), "Datum ne sme biti null");
        validateVendorId(request.vendorId());
        validateString(request.documentReference());
        validateString(request.notes());
        validateMaterialTransactionStatus(request.status());
        validateUserId(request.createdByUserId());
        materialTransactionMapper.toUpdateEntity(mt, request);
        return materialTransactionMapper.toResponse(materialTransactionRepository.save(mt));
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
    	MaterialTransaction mt = materialTransactionRepository.findById(id).orElseThrow(() -> new MaterialTransactionErrorException("MaterialTransaction not found with id " + id));
        return new MaterialTransactionResponse(mt);
    }

    @Override
    public List<MaterialTransactionResponse> findAll() {
        return materialTransactionRepository.findAll().stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Id(Long materialId) {
    	validateMaterialId(materialId);
        return materialTransactionRepository.findByMaterial_Id(materialId).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode) {
    	validateString(materialCode);
        return materialTransactionRepository.findByMaterial_CodeContainingIgnoreCase(materialCode).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_NameContainingIgnoreCase(String materialName) {
    	validateString(materialName);
        return materialTransactionRepository.findByMaterial_NameContainingIgnoreCase(materialName).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Unit(UnitOfMeasure unit) {
    	validateUnitOfMeasure(unit);
        return materialTransactionRepository.findByMaterial_Unit(unit).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
        return materialTransactionRepository.findByMaterial_CurrentStock(currentStock).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
        return materialTransactionRepository.findByMaterial_CurrentStockGreaterThan(currentStock).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
        return materialTransactionRepository.findByMaterial_CurrentStockLessThan(currentStock).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Storage_Id(Long storageId) {
    	validateStorageId(storageId);
        return materialTransactionRepository.findByMaterial_Storage_Id(storageId).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
    	validateBigDecimal(reorderLevel);
        return materialTransactionRepository.findByMaterial_ReorderLevel(reorderLevel).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantity(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return materialTransactionRepository.findByQuantity(quantity).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return materialTransactionRepository.findByQuantityGreaterThan(quantity).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return materialTransactionRepository.findByQuantityLessThan(quantity).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByType(TransactionType type) {
    	validateTransactionType(type);
        return materialTransactionRepository.findByType(type).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDate(LocalDate transactionDate) {
    	DateValidator.validateNotNull(transactionDate, "Datum ne sme biti null");
        return materialTransactionRepository.findByTransactionDate(transactionDate).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
        
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateBetween(LocalDate transactionDateStart,
            LocalDate transactionDateEnd) {
    	DateValidator.validateRange(transactionDateStart, transactionDateEnd);
        return materialTransactionRepository.findByTransactionDateBetween(transactionDateStart, transactionDateEnd).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateGreaterThanEqual(LocalDate transactionDate) {
    	DateValidator.validateNotNull(transactionDate, "Datum ne sme biti null");
        return materialTransactionRepository.findByTransactionDateGreaterThanEqual(transactionDate).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_Id(Long vendorId) {
    	validateVendorId(vendorId);
        return materialTransactionRepository.findByVendor_Id(vendorId).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_NameContainingIgnoreCase(String vendorName) {
    	validateString(vendorName);
        return materialTransactionRepository.findByVendor_NameContainingIgnoreCase(vendorName).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_EmailContainingIgnoreCase(String vendorEmail) {
    	validateString(vendorEmail);
        return materialTransactionRepository.findByVendor_EmailContainingIgnoreCase(vendorEmail).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_PhoneNumber(String vendorPhone) {
    	validateString(vendorPhone);
        return materialTransactionRepository.findByVendor_PhoneNumber(vendorPhone).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_AddressContainingIgnoreCase(String vendorAddress) {
    	validateString(vendorAddress);
        return materialTransactionRepository.findByVendor_AddressContainingIgnoreCase(vendorAddress).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByDocumentReference(String documentReference) {
    	validateString(documentReference);
        return materialTransactionRepository.findByDocumentReference(documentReference).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByNotes(String notes) {
    	validateString(notes);
        return materialTransactionRepository.findByNotes(notes).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByStatus(MaterialTransactionStatus status) {
    	validateMaterialTransactionStatus(status);
        return materialTransactionRepository.findByStatus(status).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_Id(Long userId) {
    	validateUserId(userId);
        return materialTransactionRepository.findByCreatedByUser_Id(userId).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(
            String userFirstName, String userLastName) {
    	validateDoubleString(userFirstName, userLastName);
        return materialTransactionRepository.findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(userFirstName, userLastName).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_EmailContainingIgnoreCase(String userEmail) {
    	validateString(userEmail);
        return materialTransactionRepository.findByCreatedByUser_EmailContainingIgnoreCase(userEmail).stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }
    
    private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }

    private void validateMaterialId(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Materijal sa ID " + materialId + " ne postoji.");
        }
    }
    
    private void validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new IllegalArgumentException("Skladiste sa ID "+storageId+" ne postoji.");
    	}
    }
    
    private void validateUserId(Long userId) {
    	if(userId == null) {
    		throw new UserNotFoundException("Korisnik sa ID "+userId+" ne postoji");
    	}
    }

    private void validateMaterialTransactionStatus(MaterialTransactionStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status za  MaterialTransactionStatus ne sme biti null.");
        }
    }
    
    private void validateTransactionType(TransactionType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("Tip za TransactionType ne sme biti null");
    	}
    }
    
    private void validateUnitOfMeasure(UnitOfMeasure unit) {
    	if(unit == null) {
    		throw new IllegalArgumentException("Unit za UnitOfMeasure ne sme biti null");
    	}
    }

    private void validateVendorId(Long vendorId) {
        if (!vendorRepository.existsById(vendorId)) {
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
