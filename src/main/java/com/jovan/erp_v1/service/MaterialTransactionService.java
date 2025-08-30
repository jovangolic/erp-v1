package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.enumeration.MaterialTransactionType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.util.DateValidator;
import com.jovan.erp_v1.exception.MaterialTransactionErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.MaterialTransactionMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.MaterialTransactionRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialTransactionService implements IMaterialTransactionService {

    private final MaterialTransactionRepository materialTransactionRepository;
    private final VendorRepository vendorRepository;
    private final MaterialRepository materialRepository;
    private final MaterialTransactionMapper materialTransactionMapper;
    private final UserRepository userRepository;
    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public MaterialTransactionResponse create(MaterialTransactionRequest request) {
        Material material = validateMaterialId(request.materialId());
        validateBigDecimal(request.quantity());
        validateTransactionType(request.type());
        DateValidator.validateNotNull(request.transactionDate(), "Datum ne sme biti null");
        Vendor vendor = validateVendorId(request.vendorId());
        validateString(request.documentReference());
        validateString(request.notes());
        validateMaterialTransactionStatus(request.status());
        User user = validateUserId(request.createdByUserId());
        MaterialTransaction mt = materialTransactionMapper.toEntity(request,material,vendor,user);
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
    	Material material = mt.getMaterial();
    	if(request.materialId() != null && (material.getId() == null || !request.materialId().equals(material.getId()))) {
    		material = validateMaterialId(request.materialId());
    	}
        validateBigDecimal(request.quantity());
        validateTransactionType(request.type());
        DateValidator.validateNotNull(request.transactionDate(), "Datum ne sme biti null");
        Vendor vendor = mt.getVendor();
        if(request.vendorId() != null && (vendor.getId() == null || !request.vendorId().equals(vendor.getId()))) {
        	vendor = validateVendorId(request.vendorId());
        }
        validateString(request.documentReference());
        validateString(request.notes());
        validateMaterialTransactionStatus(request.status());
        User user = mt.getCreatedByUser();
        if(request.createdByUserId() != null && (user.getId() == null || !request.createdByUserId().equals(user.getId()))) {
        	user = validateUserId(request.createdByUserId());
        }
        materialTransactionMapper.toUpdateEntity(mt, request, material,vendor,user);
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
    	List<MaterialTransaction> items = materialTransactionRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("MaterialTransaction list is empty");
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Id(Long materialId) {
    	validateMaterialId(materialId);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_Id(materialId);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material-id %d is found", materialId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode) {
    	validateString(materialCode);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_CodeContainingIgnoreCase(materialCode);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material-code %s is found", materialCode);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_NameContainingIgnoreCase(String materialName) {
    	validateString(materialName);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_NameContainingIgnoreCase(materialName);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material name %s is found", materialName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Unit(UnitOfMeasure unit) {
    	validateUnitOfMeasure(unit);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_Unit(unit);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material unit %s is found", unit);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_CurrentStock(currentStock);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material current-stock %s is found", currentStock);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_CurrentStockGreaterThan(currentStock);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material current-stock greater than %s is found", currentStock);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
    	validateBigDecimalNonNegative(currentStock);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_CurrentStockLessThan(currentStock);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material current-stock less than %s is found", currentStock);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_Storage_Id(Long storageId) {
    	validateStorageId(storageId);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_Storage_Id(storageId);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material storage-id %d is found", storageId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
    	validateBigDecimal(reorderLevel);
    	List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_ReorderLevel(reorderLevel);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material reorder-level %s is found", reorderLevel);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }
    
    @Override
	public List<MaterialTransactionResponse> findByMaterial_ReorderLevelGreaterThan(BigDecimal reorderLevel) {
		validateBigDecimal(reorderLevel);
		List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_ReorderLevelGreaterThan(reorderLevel);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material reorder-level greater than %s is found", reorderLevel);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
	}

	@Override
	public List<MaterialTransactionResponse> findByMaterial_ReorderLevelLessThan(BigDecimal reorderLevel) {
		validateBigDecimalNonNegative(reorderLevel);
		List<MaterialTransaction> items = materialTransactionRepository.findByMaterial_ReorderLevelLessThan(reorderLevel);
		if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for material reorder-level less than %s is found", reorderLevel);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
	}

    @Override
    public List<MaterialTransactionResponse> findByQuantity(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<MaterialTransaction> items = materialTransactionRepository.findByQuantity(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction found for quantity %s", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<MaterialTransaction> items = materialTransactionRepository.findByQuantityGreaterThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction found for quantity greater than %s", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
    	List<MaterialTransaction> items = materialTransactionRepository.findByQuantityLessThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction found for quantity less than %s", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByType(MaterialTransactionType type) {
    	validateTransactionType(type);
    	List<MaterialTransaction> items = materialTransactionRepository.findByType(type);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction found for type %s", type);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDate(LocalDate transactionDate) {
    	DateValidator.validateNotNull(transactionDate, "Datum ne sme biti null");
    	List<MaterialTransaction> items = materialTransactionRepository.findByTransactionDate(transactionDate);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No MaterialTransaction for transaction date %s is found", transactionDate.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
        
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateBetween(LocalDate transactionDateStart,
            LocalDate transactionDateEnd) {
    	DateValidator.validateRange(transactionDateStart, transactionDateEnd);
    	List<MaterialTransaction> items = materialTransactionRepository.findByTransactionDateBetween(transactionDateStart, transactionDateEnd);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No MaterialTransaction for transaction date between %s and %s is found",
    				transactionDateStart.format(formatter), transactionDateEnd.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByTransactionDateGreaterThanEqual(LocalDate transactionDate) {
    	DateValidator.validateNotNull(transactionDate, "Datum ne sme biti null");
    	List<MaterialTransaction> items = materialTransactionRepository.findByTransactionDateGreaterThanEqual(transactionDate);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No MaterialTransaction for transaction date %s is found", transactionDate.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_Id(Long vendorId) {
    	validateVendorId(vendorId);
    	List<MaterialTransaction> items = materialTransactionRepository.findByVendor_Id(vendorId);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for vendor's id %d is found", vendorId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_NameContainingIgnoreCase(String vendorName) {
    	validateString(vendorName);
    	List<MaterialTransaction> items = materialTransactionRepository.findByVendor_NameContainingIgnoreCase(vendorName);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for vendor's name %s is found", vendorName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_EmailContainingIgnoreCase(String vendorEmail) {
    	validateString(vendorEmail);
    	List<MaterialTransaction> items = materialTransactionRepository.findByVendor_EmailContainingIgnoreCase(vendorEmail);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for vendor's email %s is found", vendorEmail);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_PhoneNumber(String vendorPhone) {
    	validateString(vendorPhone);
    	List<MaterialTransaction> items = materialTransactionRepository.findByVendor_PhoneNumber(vendorPhone);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for vendor's phone-number %s is found", vendorPhone);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByVendor_AddressContainingIgnoreCase(String vendorAddress) {
    	validateString(vendorAddress);
    	List<MaterialTransaction> items = materialTransactionRepository.findByVendor_AddressContainingIgnoreCase(vendorAddress);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for vendor's address %s is found", vendorAddress);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByDocumentReference(String documentReference) {
    	validateString(documentReference);
    	List<MaterialTransaction> items = materialTransactionRepository.findByDocumentReference(documentReference);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for document reference %s is found", documentReference);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByNotes(String notes) {
    	validateString(notes);
    	List<MaterialTransaction> items = materialTransactionRepository.findByNotes(notes);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for notes %s is found", notes);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByStatus(MaterialTransactionStatus status) {
    	validateMaterialTransactionStatus(status);
    	List<MaterialTransaction> items = materialTransactionRepository.findByStatus(status);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for status %s is found", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_Id(Long userId) {
    	validateUserId(userId);
    	List<MaterialTransaction> items = materialTransactionRepository.findByCreatedByUser_Id(userId);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for user-id %d is found", userId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(
            String userFirstName, String userLastName) {
    	validateDoubleString(userFirstName, userLastName);
    	List<MaterialTransaction> items = materialTransactionRepository
    			.findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(userFirstName, userLastName);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for user's first-name %s and last-name %s is found", 
    				userFirstName,userFirstName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }

    @Override
    public List<MaterialTransactionResponse> findByCreatedByUser_EmailContainingIgnoreCase(String userEmail) {
    	validateString(userEmail);
    	List<MaterialTransaction> items = materialTransactionRepository.findByCreatedByUser_EmailContainingIgnoreCase(userEmail);
    	if(items.isEmpty()) {
    		String msg = String.format("No MaterialTransaction for user's email %s is found", userEmail);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
        		.map(MaterialTransactionResponse::new)
        		.collect(Collectors.toList());
    }
    
    private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new ValidationException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }

    private Material validateMaterialId(Long materialId) {
        if(materialId == null) {
        	throw new ValidationException("Material ID must not be null");
        }
        return materialRepository.findById(materialId).orElseThrow(() -> new ValidationException("Material not found with id "+materialId));
    }
    
    private Storage validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("Skladiste sa ID "+storageId+" ne postoji.");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private User validateUserId(Long userId) {
    	if(userId == null) {
    		throw new ValidationException("Korisnik sa ID "+userId+" ne postoji");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
    }

    private void validateMaterialTransactionStatus(MaterialTransactionStatus status) {
        if (status == null) {
            throw new ValidationException("Status za  MaterialTransactionStatus ne sme biti null.");
        }
    }
    
    private void validateTransactionType(MaterialTransactionType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("Tip za TransactionType ne sme biti null");
    	}
    }
    
    private void validateUnitOfMeasure(UnitOfMeasure unit) {
    	if(unit == null) {
    		throw new IllegalArgumentException("Unit za UnitOfMeasure ne sme biti null");
    	}
    }

    private Vendor validateVendorId(Long vendorId) {
        if(vendorId == null) {
        	throw new ValidationException("Vendor ID must not be null");
        }
        return vendorRepository.findById(vendorId).orElseThrow(() -> new ValidationException("Vendor not found with id "+vendorId));
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
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}

}
