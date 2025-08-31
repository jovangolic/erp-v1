package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.WorkCenterMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.repository.specification.WorkCenterSpecification;
import com.jovan.erp_v1.request.CountWorkCenterCapacityRequest;
import com.jovan.erp_v1.request.CountWorkCenterResultRequest;
import com.jovan.erp_v1.request.CountWorkCentersByStorageStatusRequest;
import com.jovan.erp_v1.request.CountWorkCentersByStorageTypeRequest;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.CountWorkCenterCapacityResponse;
import com.jovan.erp_v1.response.CountWorkCenterResultResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageStatusResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageTypeResponse;
import com.jovan.erp_v1.response.WorkCenterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkCenterService implements IWorkCenterService {

    private final WorkCenterRepository workCenterRepository;
    private final WorkCenterMapper workCenterMapper;
    private final StorageRepository storageRepository;
    

    @Transactional
    @Override
    public WorkCenterResponse create(WorkCenterRequest request) {
        validateString(request.name());
        validateString(request.location());
        validateBigDecimal(request.capacity());
        Storage storage = fetchStorage(request.localStorageId());
        WorkCenter wc = workCenterMapper.toEntity(request,storage);
        WorkCenter saved = workCenterRepository.save(wc);
        return workCenterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public WorkCenterResponse update(Long id, WorkCenterRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + id));
        validateString(request.name());
        validateString(request.location());
        validateBigDecimal(request.capacity());
        Storage st = wc.getLocalStorage();
        if(request.localStorageId() != null && (st.getId() == null) || !request.localStorageId().equals(st.getId())){
        	st = fetchStorage(request.localStorageId());
        }
        workCenterMapper.toUpdateEntity(wc, request, st);
        WorkCenter updated = workCenterRepository.save(wc);
        return workCenterMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!workCenterRepository.existsById(id)) {
            throw new WorkCenterErrorException("WorkCenter not found with id: " + id);
        }
        workCenterRepository.deleteById(id);
    }

    @Override
    public WorkCenterResponse findOne(Long id) {
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + id));
        return new WorkCenterResponse(wc);
    }

    @Override
    public List<WorkCenterResponse> findAll() {
    	List<WorkCenter> items = workCenterRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List for all work-centers is empty");
    	}
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByName(String name) {
        validateString(name);
        List<WorkCenter> items = workCenterRepository.findByName(name);
        if(items.isEmpty()) {
        	String msg = String.format("Work-center name equal to %s is not found", name);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<WorkCenter> items = workCenterRepository.findByCapacity(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with capacity %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocation(String location) {
        validateString(location);
        List<WorkCenter> items = workCenterRepository.findByLocation(location);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with location equal to %s is found", location);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameAndLocation(String name, String location) {
        validateDoubleString(name, location);
        List<WorkCenter> items = workCenterRepository.findByNameAndLocation(name, location);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with name %s and location %s is found",
        			name,location);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityGreaterThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<WorkCenter> items = workCenterRepository.findByCapacityGreaterThan(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with capacity greater than %s is found",capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityLessThan(BigDecimal capacity) {
        validateBigDecimalNonNegative(capacity);
        List<WorkCenter> items = workCenterRepository.findByCapacityLessThan(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with capacity less than %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameContainingIgnoreCase(String name) {
        validateString(name);
        List<WorkCenter> items = workCenterRepository.findByNameContainingIgnoreCase(name);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with given name %s is found", name);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationContainingIgnoreCase(String location) {
        validateString(location);
        List<WorkCenter> items = workCenterRepository.findByLocationContainingIgnoreCase(location);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with given location %s is gound", location);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityBetween(BigDecimal min, BigDecimal max) {
        validateMinAndMax(min, max);
        List<WorkCenter> items = workCenterRepository.findByCapacityBetween(min, max);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with capacity between %s and %s is found", min,max);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationOrderByCapacityDesc(String location) {
        validateString(location);
        List<WorkCenter> items = workCenterRepository.findByLocationOrderByCapacityDesc(location);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with location %s order by capacity in descending, found", location);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Id(Long localStorageId) {
        validateStorageId(localStorageId);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_Id(localStorageId);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with local storage id %d is found", localStorageId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_NameContainingIgnoreCase(String localStorageName) {
        validateString(localStorageName);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_NameContainingIgnoreCase(localStorageName);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center for local storage with given name %s is found", localStorageName);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_LocationContainingIgnoreCase(String localStorageLocation) {
        validateString(localStorageLocation);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_LocationContainingIgnoreCase(localStorageLocation);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center for local storage with given location %s is found", localStorageLocation);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Capacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_Capacity(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center for local storage with capacity %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_CapacityLessThan(BigDecimal capacity) {
        validateBigDecimalNonNegative(capacity);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_CapacityLessThan(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center for local storage with capacity less than %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_CapacityGreaterThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_CapacityGreaterThan(capacity);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center for local stoarge with capacity greater than %s is found", capacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Type(StorageType localStorageType) {
        validateStorageType(localStorageType);
        List<WorkCenter> items = workCenterRepository.findByLocalStorage_Type(localStorageType);
        if(items.isEmpty()) {
        	String msg = String.format("No work-center with given storage type %s is found ", localStorageType);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkCenterResponse> filterWorkCenters(String name, String location,
            BigDecimal capacityMin, BigDecimal capacityMax,
            StorageType type, StorageStatus status) {
		Specification<WorkCenter> spec = Specification
		.where(WorkCenterSpecification.nameContains(name))
		.and(WorkCenterSpecification.locationContains(location))
		.and(WorkCenterSpecification.capacityGreaterThan(capacityMin))
		.and(WorkCenterSpecification.capacityLessThan(capacityMax))
		.and(WorkCenterSpecification.storageTypeEquals(type))
		.and(WorkCenterSpecification.storageStatusEquals(status));
		List<WorkCenter> items = workCenterRepository.findAll(spec);
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}
    
    @Override
	public List<WorkCenterResponse> findByTypeProduction() {
		List<WorkCenter> items = workCenterRepository.findByTypeProduction();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'PRODUCTION' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByTypeDistribution() {
		List<WorkCenter> items = workCenterRepository.findByTypeDistribution();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'DISTRIBUTION' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByTypeOpen() {
		List<WorkCenter> items = workCenterRepository.findByTypeOpen();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'OPEN' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByTypeClosed() {
		List<WorkCenter> items = workCenterRepository.findByTypeClosed();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'CLOSED' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByTypeInterim() {
		List<WorkCenter> items = workCenterRepository.findByTypeInterim();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'INTERIM' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByTypeAvailable() {
		List<WorkCenter> items = workCenterRepository.findByTypeAvailable();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage type 'AVAILABLE' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocalStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<WorkCenter> items = workCenterRepository.findByLocalStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with local storage status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusActive() {
		List<WorkCenter> items = workCenterRepository.findByStatusActive();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'ACTIVE' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusUnder_Maintenance() {
		List<WorkCenter> items = workCenterRepository.findByStatusUnder_Maintenance();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'UNDER_MAINTENANCE' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusDecommissioned() {
		List<WorkCenter> items = workCenterRepository.findByStatusDecommissioned();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'DECOMMISSIONED' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusReserved() {
		List<WorkCenter> items = workCenterRepository.findByStatusReserved();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'RESERVED' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusTemporary() {
		List<WorkCenter> items = workCenterRepository.findByStatusTemporary();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'TEMPORARY' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByStatusFull() {
		List<WorkCenter> items = workCenterRepository.findByStatusFull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with storage status 'FULL' is found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocationAndCapacityGreaterThan(String location, BigDecimal capacity) {
		validateString(location);
		validateBigDecimal(capacity);
		List<WorkCenter> items = workCenterRepository.findByLocationAndCapacityGreaterThan(location, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with storage location %s and capacity greater than %s are found",
					location,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name,
			String location) {
		validateString(name);
		validateString(location);
		List<WorkCenter> items = workCenterRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with storage name %s and storage location %s are found", name,location);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocalStorage_TypeAndLocalStorage_Status(StorageType type,
			StorageStatus status) {
		validateStorageType(type);
		validateStorageStatus(status);
		List<WorkCenter> items = workCenterRepository.findByLocalStorage_TypeAndLocalStorage_Status(type, status);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with storage type %s and storage status %s are found", type,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByIdBetween(Long startId, Long endId) {
		if(startId == null || endId == null) {
			throw new ValidationException("Ids for start and end must not be null");
		}
		List<WorkCenter> items = workCenterRepository.findByIdBetween(startId, endId);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with ids between %d and %d are found", startId,endId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocalStorageIsNull() {
		List<WorkCenter> items = workCenterRepository.findByLocalStorageIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers found where local storage is null");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocalStorageIsNotNull() {
		List<WorkCenter> items = workCenterRepository.findByLocalStorageIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers found where local storage is not null");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findAllByOrderByCapacityAsc() {
		List<WorkCenter> items = workCenterRepository.findAllByOrderByCapacityAsc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with capacity in ascending order, is not found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findAllByOrderByCapacityDesc() {
		List<WorkCenter> items = workCenterRepository.findAllByOrderByCapacityDesc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No work-centers with capacity in descending order, is not found");
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocationIn(List<String> locations) {
		validateListOfStrings(locations);
		List<WorkCenter> items = workCenterRepository.findByLocationIn(locations);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with list of locations %s are found", locations);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByNameContainingIgnoreCaseAndLocalStorage_Status(String name,
			StorageStatus status) {
		validateString(name);
		validateStorageStatus(status);
		List<WorkCenter> items = workCenterRepository.findByNameContainingIgnoreCaseAndLocalStorage_Status(name, status);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with given name %s and status %s for local-storage, is not found",
					name,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<WorkCenterResponse> findByLocationContainingIgnoreCaseAndLocalStorage_Type(String location,
			StorageType type) {
		validateString(location);
		validateStorageType(type);
		List<WorkCenter> items = workCenterRepository.findByLocationContainingIgnoreCaseAndLocalStorage_Type(location, type);
		if(items.isEmpty()) {
			String msg = String.format("No work-centers with location %s and storage type %s is found",
					location,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(workCenterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<CountWorkCenterCapacityResponse> countWorkCentersByCapacity() {
		List<CountWorkCenterCapacityRequest> items = workCenterRepository.countWorkCentersByCapacity(); 
		if(items.isEmpty()) {
			throw new NoDataFoundException("No counts for work-centers with capacity found");
		}
		return items.stream().map(CountWorkCenterCapacityResponse::new).collect(Collectors.toList());
	}

	@Override
	public Long countWorkCentersByCapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		Long items = workCenterRepository.countWorkCentersByCapacityLessThan(capacity);
		return items;
	}

	@Override
	public Long countWorkCentersByCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		Long items = workCenterRepository.countWorkCentersByCapacityGreaterThan(capacity);
		return items;
	}

	@Override
	public List<CountWorkCenterResultResponse> countWorkCentersByLocation() {
		List<CountWorkCenterResultRequest> items = workCenterRepository.countWorkCentersByLocation();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No counts for work-centers with location found");
		}
		return items.stream().map(CountWorkCenterResultResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<CountWorkCentersByStorageStatusResponse> countWorkCentersByStorageStatus() {
		List<CountWorkCentersByStorageStatusRequest> items = workCenterRepository.countWorkCentersByStorageStatus();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No counts for work-centers with storage status, is found");
		}
		return items.stream().map(CountWorkCentersByStorageStatusResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<CountWorkCentersByStorageTypeResponse> countWorkCentersByStorageType() {
		List<CountWorkCentersByStorageTypeRequest> items = workCenterRepository.countWorkCentersByStorageType();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No counts for work-centers with storage type is found");
		}
		return items.stream().map(CountWorkCentersByStorageTypeResponse::new).collect(Collectors.toList());
	}
	
	private void validateListOfStrings(List<String> locations) {
		if(locations == null || locations.isEmpty()) {
			throw new ValidationException("List containing the locations must not be empty nor null");
		}
		for (int i = 0; i < locations.size(); i++) {
		    String str = locations.get(i);
		    if (str == null || str.trim().isEmpty()) {
		        throw new ValidationException("Invalid location at index " + i + ": must not be null or blank");
		    }
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


    private void validateStorageId(Long storageId) {
        if (storageId == null) {
            throw new IllegalArgumentException("ID za skladiste ne sme biti null");
        }
    }
    
    private Storage fetchStorage(Long storageId) {
        if (storageId == null) {
            throw new StorageNotFoundException("Storage ID must not be null");
        }
        return storageRepository.findById(storageId)
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + storageId));
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateStorageType(StorageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Unit za StorageType ne sme biti null");
        }
    }
    
    private void validateStorageStatus(StorageStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min ne može biti veći od Max");
        }
    }

    private void validateDoubleString(String name, String loc) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("String name ne sme biti null ili prazan");
        }
        if (loc == null || loc.trim().isEmpty()) {
            throw new IllegalArgumentException("String lokacija ne sme biti null ili prazan");
        }
    }  
}
