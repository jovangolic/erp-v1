package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.MaterialMovementNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.MaterialMovementMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialMovement;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.MaterialMovementRepository;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialMovementService implements IMaterialMovementService {

    private final MaterialMovementRepository materialMovementRepository;
    private final MaterialMovementMapper materialMovementMapper;
    private final StorageRepository storageRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    @Override
    public MaterialMovementResponse create(MaterialMovementRequest request) {
        Material material = validateMaterialId(request.materialId());
        DateValidator.validateNotNull(request.movementDate(), "Datum kretanja");
        DateValidator.validateNotInFuture(request.movementDate(), "Datum kretanja");
        validateMovementType(request.type());
        validateBigDecimal(request.quantity());
        Storage from = validateFromStorageId(request.fromStorageId());
        Storage to = validateToStorageId(request.toStorageId());
        log.info("Kreiranje materijalnog kretanja: materialId={}, datum={}", request.materialId(),
                request.movementDate());
        MaterialMovement m = materialMovementMapper.toEntity(request,material,from,to);
        MaterialMovement saved = materialMovementRepository.save(m);
        return materialMovementMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialMovementResponse update(Long id, MaterialMovementRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        MaterialMovement m = materialMovementRepository.findById(id).orElseThrow(
                () -> new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id));
        Material material = m.getMaterial();
        if(request.materialId() != null && (m.getMaterial().getId() == null || !request.materialId().equals(material.getId()))) {
        	material = validateMaterialId(request.materialId());
        }
        DateValidator.validateNotNull(request.movementDate(), "Datum kretanja");
        DateValidator.validateNotInFuture(request.movementDate(), "Datum kretanja");
        validateMovementType(request.type());
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Količina mora biti pozitivan broj i ne sme biti null.");
        }
        Storage from = m.getFromStorage();
        if(request.fromStorageId() != null && (from.getId() == null || !request.fromStorageId().equals(from.getId()))) {
        	from = validateFromStorageId(request.fromStorageId());
        }
        Storage to = m.getToStorage();
        if(request.toStorageId() != null && (to.getId() == null || !request.equals(to.getId()))) {
        	to = validateToStorageId(request.toStorageId());
        }
        log.info("Kreiranje materijalnog kretanja: materialId={}, datum={}", request.materialId(),
                request.movementDate());
        materialMovementMapper.toUpdateEntity(m, request,material, from, to);
        return materialMovementMapper.toResponse(materialMovementRepository.save(m));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialMovementRepository.existsById(id)) {
            throw new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id);
        }
        materialMovementRepository.deleteById(id);
    }

    @Override
    public MaterialMovementResponse findOne(Long id) {
        MaterialMovement m = materialMovementRepository.findById(id).orElseThrow(
                () -> new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id));
        return new MaterialMovementResponse(m);
    }

    @Override
    public List<MaterialMovementResponse> findAll() {
        List<MaterialMovement> lista = materialMovementRepository.findAll();
        if (lista.isEmpty()) {
            log.warn("Nema pronađenih kretanja materijala.");
        }
        return lista.stream().map(MaterialMovementResponse::new).toList();
    }

    @Override
    public List<MaterialMovementResponse> findByType(MovementType type) {
        validateMovementType(type);
        List<MaterialMovement> items = materialMovementRepository.findByType(type);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for type %s is found", type);
        	throw new NoDataFoundException(msg);
        }
        return materialMovementRepository.findByType(type).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByQuantity(BigDecimal quantity) {
        validateBigDecimal(quantity);
        List<MaterialMovement> items = materialMovementRepository.findByQuantity(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for quantity %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return materialMovementRepository.findByQuantity(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        validateBigDecimal(quantity);
        List<MaterialMovement> items = materialMovementRepository.findByQuantityGreaterThan(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for quantity greater than %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return materialMovementRepository.findByQuantityGreaterThan(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByQuantityLessThan(BigDecimal quantity) {
        validateBigDecimalNonNegative(quantity);
        List<MaterialMovement> items = materialMovementRepository.findByQuantityLessThan(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for quantity less than %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return materialMovementRepository.findByQuantityLessThan(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_Id(Long fromStorageId) {
        validateFromStorageId(fromStorageId);
        List<MaterialMovement> items = materialMovementRepository.findByFromStorage_Id(fromStorageId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for fromStorage id %d is found", fromStorageId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_Id(Long toStorageId) {
        validateToStorageId(toStorageId);
        List<MaterialMovement> items = materialMovementRepository.findByToStorage_Id(toStorageId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for toStorage id %d is found", toStorageId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_NameContainingIgnoreCase(String fromStorageName) {
        validateString(fromStorageName);
        List<MaterialMovement> items = materialMovementRepository.findByFromStorage_NameContainingIgnoreCase(fromStorageName);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for fromStorage name %s is found", fromStorageName);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_NameContainingIgnoreCase(String toStorageName) {
        validateString(toStorageName);
        List<MaterialMovement> items = materialMovementRepository.findByToStorage_NameContainingIgnoreCase(toStorageName);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for toStorage name %s is found", toStorageName);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_LocationContainingIgnoreCase(String fromStorageLocation) {
        validateString(fromStorageLocation);
        List<MaterialMovement> items = materialMovementRepository.findByToStorage_LocationContainingIgnoreCase(fromStorageLocation);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for fromStorage location %s is found", fromStorageLocation);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_LocationContainingIgnoreCase(String toStorageLocation) {
        validateString(toStorageLocation);
        List<MaterialMovement> items = materialMovementRepository.findByToStorage_LocationContainingIgnoreCase(toStorageLocation);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for toStorage location %s is found", toStorageLocation);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_Capacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<MaterialMovement> items = materialMovementRepository.findByFromStorage_Capacity(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for fromStorage capacity %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_Capacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<MaterialMovement> items = materialMovementRepository.findByToStorage_Capacity(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialMovement for toStorage capacity %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDate(LocalDate movementDate) {
        DateValidator.validateNotInFuture(movementDate, "Datumn kretanja");
        DateValidator.validateNotNull(movementDate, "Datum kretanja");
        List<MaterialMovement> items = materialMovementRepository.findByMovementDate(movementDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialMovement for movement date %s is found",
        			movementDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateBetween(LocalDate start, LocalDate end) {
        DateValidator.validateRange(start, end);
        List<MaterialMovement> items = materialMovementRepository.findByMovementDateBetween(start, end);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialMovement for movement date between %s and %s is found",
        			start.format(formatter), end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateGreaterThanEqual(LocalDate date) {
        DateValidator.validateNotInFuture(date, "Datumn kretanja");
        DateValidator.validateNotNull(date, "Datum kretanja");
        List<MaterialMovement> items = materialMovementRepository.findByMovementDateGreaterThanEqual(date);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialMovement for movement date %s is found",
        			date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateAfterOrEqual(LocalDate movementDate) {
        DateValidator.validateNotInFuture(movementDate, "Datumn kretanja");
        DateValidator.validateNotNull(movementDate, "Datum kretanja");
        List<MaterialMovement> items = materialMovementRepository.findByMovementDateAfterOrEqual(movementDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialMovement for movement date %s is found",
        			movementDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<MaterialMovementResponse> findByFromStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
    	List<MaterialMovement> items = materialMovementRepository.findByFromStorage_CapacityGreaterThan(capacity);
    	if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for fromStorage capacity greater than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByToStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<MaterialMovement> items = materialMovementRepository.findByToStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for toStorage capacity greater than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByFromStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<MaterialMovement> items = materialMovementRepository.findByFromStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for fromStorage capacity less than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByToStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<MaterialMovement> items = materialMovementRepository.findByToStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for toStorage capacity less than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByFromStorage_Type(StorageType type) {
		validateStorageType(type);
		List<MaterialMovement> items = materialMovementRepository.findByToStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for fromStorage type %s", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByToStorage_Type(StorageType type) {
		validateStorageType(type);
		List<MaterialMovement> items = materialMovementRepository.findByToStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for toStorage type %s", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByFromStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<MaterialMovement> items = materialMovementRepository.findByFromStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for fromStorage status %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<MaterialMovementResponse> findByToStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<MaterialMovement> items = materialMovementRepository.findByToStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No MaterialMovement found for toStorage status %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(materialMovementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BigDecimal countAvailableCapacityFromStorage(Long fromStorageId) {
		Storage formStorage = storageRepository.findById(fromStorageId)
                .orElseThrow(() -> new ValidationException("FromStorage not found"));
        return formStorage.countAvailableCapacity();
	}

	@Override
	public BigDecimal countAvailableCapacityToStorage(Long toStorageId) {
		Storage toStorage = storageRepository.findById(toStorageId)
                .orElseThrow(() -> new ValidationException("ToStorage not found"));
        return toStorage.countAvailableCapacity();
	}

	@Override
	public boolean hasCapacityForFromStorage(Long fromStorageId, BigDecimal amount) {
		Storage fromStorage = storageRepository.findById(fromStorageId)
                .orElseThrow(() -> new ValidationException("FromStorage not found"));
        return fromStorage.hasCapacityFor(amount);
		
	}

	@Override
	public boolean hasCapacityForToStoage(Long toStorageId, BigDecimal amount) {
		Storage toStorage = storageRepository.findById(toStorageId)
                .orElseThrow(() -> new ValidationException("ToStorage not found"));
        return toStorage.hasCapacityFor(amount);
		
	}

	@Override
	public void allocateCapacityFromStorage(Long fromStorageId, BigDecimal amount) {
		Storage fromStorage = storageRepository.findById(fromStorageId)
                .orElseThrow(() -> new ValidationException("FromStorage not found"));
		fromStorage.allocateCapacity(amount);
        storageRepository.save(fromStorage); 
		
	}

	@Override
	public void allocateCapacityToStorage(Long toStorageId, BigDecimal amount) {
		Storage toStorage = storageRepository.findById(toStorageId)
                .orElseThrow(() -> new ValidationException("ToStorage not found"));
		toStorage.allocateCapacity(amount);
        storageRepository.save(toStorage); 
		
	}

	@Override
	public void releaseCapacityFromStorage(Long fromStorageId, BigDecimal amount) {
		Storage formStorage = storageRepository.findById(fromStorageId)
                .orElseThrow(() -> new ValidationException("FromStorage not found"));
		formStorage.releaseCapacity(amount);
        storageRepository.save(formStorage); 
	}

	@Override
	public void releaseCapacityToStorage(Long toStorageId, BigDecimal amount) {
		Storage toStorage = storageRepository.findById(toStorageId)
                .orElseThrow(() -> new ValidationException("ToStorage not found"));
		toStorage.releaseCapacity(amount);
        storageRepository.save(toStorage); 
		
	}

    private void validateMovementType(MovementType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tip kretanja (MovementType) ne sme biti null.");
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
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
    
    private void validateStorageStatus(StorageStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
    }
    
    private void validateStorageType(StorageType type) {
    	Optional.ofNullable(type)
    		.orElseThrow(() -> new ValidationException("StorageType type must not be null"));
    }
  
    private Storage validateFromStorageId(Long fromStorageId) {
    	if(fromStorageId == null) {
    		throw new ValidationException("FromStorage ID must not be null");
    	}
    	return storageRepository.findById(fromStorageId).orElseThrow(() -> new ValidationException("FromStorage not found with id "+fromStorageId));
    }
    
    private Storage validateToStorageId(Long toStorageId) {
    	if(toStorageId == null) {
    		throw new ValidationException("ToStorage ID must not be null");
    	}
    	return storageRepository.findById(toStorageId).orElseThrow(() -> new ValidationException("ToStorage not found with id "+toStorageId));
    }
    
    private Material validateMaterialId(Long materialId) {
    	if(materialId == null) {
    		throw new ValidationException("Material ID must not be null");
    	}
    	return materialRepository.findById(materialId).orElseThrow(() -> new ValidationException("Material not found with id "+materialId));
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be null nor empty");
    	}
    }
}
