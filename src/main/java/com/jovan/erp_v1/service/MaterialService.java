package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.DuplicateCodeException;
import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ResourceNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.MaterialMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public MaterialResponse create(MaterialRequest request) {
        if (materialRepository.findByCode(request.code()).size() > 0) {
            throw new DuplicateCodeException("Material with code '" + request.code() + "' already exists.");
        }
        validateMaterialRequest(request);
        Storage storage = validateStorageId(request.storageId());
        Material m = materialMapper.toEntity(request,storage);
        Material saved = materialRepository.save(m);
        return materialMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialResponse update(Long id, MaterialRequest request) {
    	if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
        Material existing = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id " + id));
        List<Material> materialsWithSameCode = materialRepository.findByCode(request.code());
        boolean isDuplicateCode = materialsWithSameCode.stream()
                .anyMatch(m -> !m.getId().equals(id));
        if (isDuplicateCode) {
            throw new DuplicateCodeException("Material with code '" + request.code() + "' already exists.");
        }
        validateMaterialRequest(request);
        Storage st = existing.getStorage();
        if(request.storageId() != null && (st.getId() == null || !request.storageId().equals(st.getId()))) {
        	st = validateStorageId(request.storageId());
        }
        materialMapper.toUpdateEntity(existing, request,st);
        Material updated = materialRepository.save(existing);
        return new MaterialResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new MaterialNotFoundException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    public MaterialResponse findOne(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + id));
        return new MaterialResponse(m);
    }

    @Override
    public List<MaterialResponse> findAll() {
    	List<Material> items = materialRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Material list is empty");
    	}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> searchMaterials(String name, String code, UnitOfMeasure unit, BigDecimal currentStock,
            Long storageId, BigDecimal reorderLevel) {
        List<Material> materials = materialRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (code != null && !code.isEmpty()) {
                predicates.add(cb.equal(root.get("code"), code));
            }
            if (unit != null) {
                predicates.add(cb.equal(root.get("unit"), unit));
            }
            if (currentStock != null) {
                predicates.add(cb.equal(root.get("currentStock"), currentStock));
            }
            if (storageId != null) {
                predicates.add(cb.equal(root.get("storage").get("id"), storageId));
            }
            if (reorderLevel != null) {
                predicates.add(cb.equal(root.get("reorderLevel"), reorderLevel));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
        return materials.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Id(Long storageId) {
    	validateStorageId(storageId);
    	List<Material> items = materialRepository.findByStorage_Id(storageId);
    	if(items.isEmpty()) {
    		String msg = String.format("No Material for storage-id %d is found", storageId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByCode(String code) {
    	validateCodeExists(code);
    	List<Material> items = materialRepository.findByCode(code);
    	if(items.isEmpty()) {
    		String msg = String.format("No Material by code %s is found", code);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByNameContainingIgnoreCase(String name) {
    	validateString(name);
    	List<Material> items = materialRepository.findByNameContainingIgnoreCase(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No Material by name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> search(String name, String code) {
    	validateString(name);
    	validateCodeExists(code);
    	List<Material> items = materialRepository.search(name, code);
    	if(items.isEmpty()) {
    		String msg = String.format("No Material for name %s and code %s is found", name,code);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByUnit(UnitOfMeasure unit) {
    	validateUnitOfMeasure(unit);
    	List<Material> items = materialRepository.findByUnit(unit);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for unit of measure %s is found", unit);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Name(String storageName) {
    	validateString(storageName);
    	List<Material> items = materialRepository.findByStorage_Name(storageName);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for storage name %s is found", storageName);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Capacity(BigDecimal capacity) {
    	validateBigDecimal(capacity);
    	List<Material> items = materialRepository.findByStorage_Capacity(capacity);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for storage capacity %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Type(StorageType type) {
    	validateStorageType(type);
    	List<Material> items = materialRepository.findByStorage_Type(type);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for storage-type %s is found", type);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByCurrentStock(BigDecimal currentStock) {
    	validateBigDecimal(currentStock);
    	List<Material> items = materialRepository.findByCurrentStock(currentStock);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for current-stoct %s is found", currentStock);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByReorderLevel(BigDecimal reorderLevel) {
    	validateBigDecimal(reorderLevel);
    	List<Material> items = materialRepository.findByReorderLevel(reorderLevel);
    	if(items.isEmpty()) {
			String msg = String.format("No Material for reorder-level than %s is found", reorderLevel);
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<MaterialResponse> findByCurrentStockGreaterThan(BigDecimal currentStock) {
		validateBigDecimal(currentStock);
		List<Material> items = materialRepository.findByCurrentStockGreaterThan(currentStock);
		if(items.isEmpty()) {
			String msg = String.format("No Material for current-stoct greater than %s is found", currentStock);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByCurrentStockLessThan(BigDecimal currentStock) {
		validateBigDecimalNonNegative(currentStock);
		List<Material> items = materialRepository.findByCurrentStockLessThan(currentStock);
		if(items.isEmpty()) {
			String msg = String.format("No Material for current-stoct less than %s is found", currentStock);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
	}
	
	@Override
	public List<MaterialResponse> findByReorderLevelGreaterThan(BigDecimal reorderLevel) {
		validateBigDecimal(reorderLevel);
		List<Material> items = materialRepository.findByReorderLevelGreaterThan(reorderLevel);
		if(items.isEmpty()) {
			String msg = String.format("No Material for reorder-level greater than %s is found", reorderLevel);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByReorderLevelLessThan(BigDecimal reorderLevel) {
		validateBigDecimalNonNegative(reorderLevel);
		List<Material> items = materialRepository.findByReorderLevelLessThan(reorderLevel);
		if(items.isEmpty()) {
			String msg = String.format("No Material for reorder-level less than %s is found", reorderLevel);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByStorage_LocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<Material> items = materialRepository.findByStorage_LocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No Material for storage location %s is found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<Material> items = materialRepository.findByStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Material for storage capacity greater than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<Material> items = materialRepository.findByStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Material for storage capacity less than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialResponse> findByStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<Material> items = materialRepository.findByStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No Material for storage-status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public BigDecimal countAvailableCapacity(Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.countAvailableCapacity();
    }

	@Override
    public boolean hasCapacityFor(Long storageId, BigDecimal amount) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.hasCapacityFor(amount);
    }

	@Override
    public void allocateCapacity(Long storageId, BigDecimal amount) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.allocateCapacity(amount);
        storageRepository.save(storage); 
    }

	@Override
    public void releaseCapacity(Long storageId, BigDecimal amount) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.releaseCapacity(amount);
        storageRepository.save(storage);
    }
	
	private void validateStorageStatus(StorageStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateCodeExists(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code must not be null or empty");
        }
        if (!materialRepository.existsByCode(code)) {
            throw new MaterialNotFoundException("Material with code '" + code + "' not found.");
        }
    }
    
    private void validateUnitOfMeasure(UnitOfMeasure unit) {
    	if(unit == null) {
    		throw new IllegalArgumentException("UnitOfMeasure unit must not be null nor empty");
    	}
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("String must not be null nor empty");
    	}
    }
    
    private void validateStorageType(StorageType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("StorageType type must not be null nor empty");
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <  0) {
    		throw new IllegalArgumentException("");
    	}
    }
    
    private Storage validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new StorageNotFoundException("Storage ID must not be null nor empty");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private void validateMaterialRequest(MaterialRequest request) {
        validateString(request.name());
        validateUnitOfMeasure(request.unit());
        validateBigDecimal(request.currentStock());
        validateBigDecimal(request.reorderLevel());
    }

}
